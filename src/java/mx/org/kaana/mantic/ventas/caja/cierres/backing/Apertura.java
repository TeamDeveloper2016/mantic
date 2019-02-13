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
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.db.dto.TcManticCierresDto;
import mx.org.kaana.mantic.ventas.caja.cierres.beans.Denominacion;
import mx.org.kaana.mantic.ventas.caja.cierres.reglas.Cierre;
import org.primefaces.context.RequestContext;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 7/05/2018
 *@time 03:29:13 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

@Named(value= "manticVentasCajaCierresApertura")
@ViewScoped
public class Apertura extends IBaseAttribute implements Serializable {

  private static final long serialVersionUID = 323323488565639361L;
  
	private List<Denominacion> fondos;
	private EAccion accion;

	public List<Denominacion> getFondos() {
		return fondos;
	}

  @Override
	@PostConstruct
  protected void init() {		
    try {
			if(JsfBase.getFlashAttribute("accion")== null)
				UIBackingUtilities.execute("janal.isPostBack('cancelar')");
      this.accion = JsfBase.getFlashAttribute("accion")== null? EAccion.AGREGAR: (EAccion)JsfBase.getFlashAttribute("accion");
			if(JsfBase.getAutentifica().getEmpresa().isMatriz())
        this.attrs.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresaDepende());
			else
				this.attrs.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
			this.attrs.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
			this.attrs.put("limite", 0D);
			this.attrs.put("efectivo", 0D);
			this.attrs.put("disponible", 0D);
			this.doLoad();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

  public void doLoad() {
    try {
  		this.attrs.put("limite", 3000D);
  		this.attrs.put("idCierre", -1);
  		this.attrs.put("idEfectivo", 2);
      this.attrs.put("nombreAccion", Cadena.letraCapital(this.accion.name()));
			this.fondos= (List<Denominacion>)DaoFactory.getInstance().toEntitySet(Denominacion.class, "TcManticMonedasDto", "denominacion", this.attrs);
			this.toLoadEmpresas();
			this.doCalculate();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // doLoad

  public String doAceptar() {  
    Cierre transaccion= null;
    String regresar   = null;
    try {			
			TcManticCierresDto cierre= new TcManticCierresDto("", -1L, 2L, JsfBase.getIdUsuario(), 1L, "ESTA CAJA SE APERTURO DESDE EL MODULO DE CIERRE DE CAJAS", 1L, new Long(Fecha.getAnioActual()));
			transaccion = new Cierre(((UISelectEntity)this.attrs.get("idCaja")).getKey(), (Double)this.attrs.get("disponible"), cierre, null, this.fondos);
			if (transaccion.ejecutar(this.accion)) {
			  regresar = "filtro".concat(Constantes.REDIRECIONAR);
   			UIBackingUtilities.execute("janal.alert('Se gener\\u00F3 correctamente la apertura de caja con consecutivo: "+ cierre.getConsecutivo()+ "');");
  			JsfBase.setFlashAttribute("idCierre", this.attrs.get("idCierre"));
			} // if
			else 
				JsfBase.addMessage("Ocurrió un error al realizar el registro de fonde de caja.", ETipoMensaje.ERROR);      			
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    return regresar;
  } // doAccion
	
  public String doCancelar() {   
  	JsfBase.setFlashAttribute("idCierre", this.attrs.get("idCierre"));
    return "filtro".concat(Constantes.REDIRECIONAR);
  } 
	
	private void toLoadEmpresas() {
		List<Columna> columns = null;
    try {
			columns= new ArrayList<>();
      columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
			List<UISelectEntity> sucursales= (List<UISelectEntity>) UIEntity.build("TcManticEmpresasDto", "empresas", this.attrs, columns);
 			this.attrs.put("idEmpresa", UIBackingUtilities.toFirstKeySelectEntity(sucursales));
      this.attrs.put("empresas", sucursales);
			this.doLoadCajas();
    } // try
    catch (Exception e) {
      throw e;
    } // catch   
    finally {
      Methods.clean(columns);
    }// finally
	}
	
	public void doLoadCajas() {
		List<Columna> columns     = null;
		Map<String, Object> params= null;
    try {
			params = new HashMap<>();
			columns= new ArrayList<>();
			params.put("idEmpresa", ((UISelectEntity)this.attrs.get("idEmpresa")).getKey());
      columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
			List<UISelectEntity> cajas= (List<UISelectEntity>) UIEntity.build("VistaCierresCajasDto", "unica", params, columns);
      this.attrs.put("cajas", cajas);
 			this.attrs.put("temporal", UIBackingUtilities.toFirstKeySelectEntity((List<UISelectEntity>)this.attrs.get("cajas")));
			if(cajas!= null && !cajas.isEmpty() && this.attrs.get("temporal")!= null)
				this.attrs.put("limite", ((UISelectEntity)this.attrs.get("temporal")).toDouble("limite"));
    } // try
    catch (Exception e) {
      throw e;
    } // catch   
    finally {
      Methods.clean(columns);
			Methods.clean(params);
    }// finally
	}

	public void doCalculate() {
		Double sum  = 0D;
		for (Denominacion fondo: this.fondos) {
			fondo.setImporte(Numero.toRedondearSat(fondo.getDenominacion()* fondo.getCantidad()));
			sum+= fondo.getImporte();
		} // for
    this.attrs.put("disponible", Numero.toRedondearSat(sum));		
    this.attrs.put("efectivo", Numero.toRedondearSat(sum));		
	}
	
}