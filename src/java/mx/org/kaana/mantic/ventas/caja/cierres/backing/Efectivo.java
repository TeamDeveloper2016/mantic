package mx.org.kaana.mantic.ventas.caja.cierres.backing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.inventarios.creditos.reglas.Transaccion;
import org.primefaces.context.RequestContext;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 7/05/2018
 *@time 03:29:13 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

@Named(value= "manticVentasCajaCierresEfectivo")
@ViewScoped
public class Efectivo extends IBaseAttribute implements Serializable {

  private static final long serialVersionUID = 327393488565639361L;
  
	private Entity caja;
	private EAccion accion;

  @Override
	@PostConstruct
  protected void init() {		
    try {
			if(JsfBase.getFlashAttribute("accion")== null)
				RequestContext.getCurrentInstance().execute("janal.isPostBack('cancelar')");
      this.accion = JsfBase.getFlashAttribute("accion")== null? EAccion.AGREGAR: (EAccion)JsfBase.getFlashAttribute("accion");
      this.attrs.put("idCierre", JsfBase.getFlashAttribute("idCierre")== null? -1L: JsfBase.getFlashAttribute("idCierre"));
      this.attrs.put("retorno", JsfBase.getFlashAttribute("retorno"));
			this.doLoad();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

  public void doLoad() {
    try {
      this.attrs.put("nombreAccion", Cadena.letraCapital(this.accion.name()));
			this.caja    = (Entity)DaoFactory.getInstance().toEntity("VistaCierresCajasDto", "caja", this.attrs);
			Value retiros= (Value)DaoFactory.getInstance().toField("VistaCierresCajasDto", "saldo", this.attrs, "retiros");
      this.attrs.put("caja", this.caja);
      this.attrs.put("retiros", retiros!= null? retiros.toDouble(): 0D);
			this.toLoadEmpresas();
      switch (this.accion) {
        case AGREGAR:				
          break;
        case MODIFICAR:					
        case CONSULTAR:					
          break;
      } // switch
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // doLoad

  public String doAceptar() {  
    Transaccion transaccion= null;
    String regresar        = null;
    try {			
			transaccion = new Transaccion(null, null);
			if (transaccion.ejecutar(this.accion)) {
				if(this.accion.equals(EAccion.AGREGAR)) {
 				  regresar = this.attrs.get("retorno").toString().concat(Constantes.REDIRECIONAR);
				} // if	
 				if(!this.accion.equals(EAccion.CONSULTAR)) 
  				JsfBase.addMessage("Se ".concat(this.accion.equals(EAccion.AGREGAR)? "agregó": this.accion.equals(EAccion.COMPLETO) ? "aplicó": "modificó").concat(" el retiro de caja."), ETipoMensaje.INFORMACION);
  			JsfBase.setFlashAttribute("idCierre", this.attrs.get("idCierre"));
			} // if
			else 
				JsfBase.addMessage("Ocurrió un error al registrar el retiro de caja.", ETipoMensaje.ERROR);      			
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    return regresar;
  } // doAccion
	
  public String doCancelar() {   
  	JsfBase.setFlashAttribute("idCierre", this.attrs.get("idCierre"));
    return ((String)this.attrs.get("retorno")).concat(Constantes.REDIRECIONAR);
  } 
	
	private void toLoadEmpresas() {
		List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
    try {
			columns= new ArrayList<>();
			if(JsfBase.getAutentifica().getEmpresa().isMatriz())
        params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresaDepende());
			else
				params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
			params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
			params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
      columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
			List<UISelectEntity> sucursales= (List<UISelectEntity>) UIEntity.build("TcManticEmpresasDto", "empresas", params, columns);
      this.attrs.put("sucursales", sucursales);
			if(this.caja!= null) {
				int index= sucursales.indexOf(new UISelectEntity(new Entity(this.caja.toLong("idEmpresa"))));
				if(index>= 0)
					this.attrs.put("idEmpresa", sucursales.get(index));
				else
			    this.attrs.put("idEmpresa", UIBackingUtilities.toFirstKeySelectEntity((List<UISelectEntity>)this.attrs.get("sucursales")));
			} // if
			else
			  this.attrs.put("idEmpresa", UIBackingUtilities.toFirstKeySelectEntity((List<UISelectEntity>)this.attrs.get("sucursales")));
			this.doLoadCajas();
    } // try
    catch (Exception e) {
      throw e;
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    }// finally
	}
	
	public void doLoadCajas() {
		List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
    try {
			columns= new ArrayList<>();
      columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
			params.put("idEmpresa", ((UISelectEntity)this.attrs.get("idEmpresa")).getKey());
			List<UISelectEntity> cajas= (List<UISelectEntity>) UIEntity.build("TcManticCajasDto", "cajas", params, columns);
      this.attrs.put("cajas", cajas);
			if(this.caja!= null) {
				int index= cajas.indexOf(new UISelectEntity(new Entity(this.caja.toLong("idCaja"))));
				if(index>= 0)
					this.attrs.put("idCaja", cajas.get(index));
				else
    			this.attrs.put("idCaja", UIBackingUtilities.toFirstKeySelectEntity((List<UISelectEntity>)this.attrs.get("cajas")));
			} // if
			else
  			this.attrs.put("idCaja", UIBackingUtilities.toFirstKeySelectEntity((List<UISelectEntity>)this.attrs.get("cajas")));
    } // try
    catch (Exception e) {
      throw e;
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    }// finally
	}

	
}