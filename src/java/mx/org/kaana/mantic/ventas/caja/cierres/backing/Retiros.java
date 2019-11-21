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
import mx.org.kaana.mantic.db.dto.TcManticCierresRetirosDto;
import mx.org.kaana.mantic.ventas.caja.cierres.reglas.CreateCierre;
import mx.org.kaana.mantic.ventas.caja.cierres.reglas.Transaccion;
import mx.org.kaana.mantic.ventas.reglas.CambioUsuario;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 7/05/2018
 *@time 03:29:13 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

@Named(value= "manticVentasCajaCierresRetiros")
@ViewScoped
public class Retiros extends IBaseAttribute implements Serializable {

  private static final long serialVersionUID = 327393488565639361L;
	private static final Log LOG=LogFactory.getLog(Retiros.class);
  
	private Entity caja;
	private EAccion accion;

  @Override
	@PostConstruct
  protected void init() {		
    try {
      this.accion = EAccion.AGREGAR;
  		this.attrs.put("ok", Boolean.FALSE);
			this.attrs.put("retorno", JsfBase.getParametro("zwkl")== null || "0".equals(JsfBase.getParametro("zwkl"))? "ambos": "/Paginas/Mantic/Ventas/Caja/accion");
			this.attrs.put("idEmpresa", JsfBase.getFlashAttribute("idEmpresa"));
			this.attrs.put("idCaja", JsfBase.getFlashAttribute("idCaja"));
      this.attrs.put("importe", 0D);
      this.attrs.put("retiros", 0D);
      this.attrs.put("abonos", 0D);
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
			this.toLoadEmpresas();
			this.caja= (Entity)DaoFactory.getInstance().toEntity("VistaCierresCajasDto", "caja", this.attrs);
			if(this.caja== null) {
				this.caja= new Entity(-1L);
				this.caja.put("saldo", new Value("saldo", 0D));
				this.caja.put("idCierreEstatus", new Value("idCierreEstatus", -1L));
			} // 
      this.attrs.put("caja", this.caja);
			Entity retiros= (Entity)DaoFactory.getInstance().toEntity("VistaCierresCajasDto", "ambos", this.attrs);
      this.attrs.put("retiros", retiros!= null && retiros.get("retiros").getData()!= null? retiros.toDouble("retiros"): 0D);
      this.attrs.put("abonos", retiros!= null && retiros.get("abonos").getData()!= null? retiros.toDouble("abonos"): 0D);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // doLoad

  public String doAceptar(Long idAutorizo) {  
    Transaccion transaccion= null;
		CreateCierre ticket    = null;
    try {			
			TcManticCierresRetirosDto retiro= new TcManticCierresRetirosDto(-1L);
			retiro.setIdAbono(2L);
			retiro.setIdTipoMedioPago(1L);
			retiro.setConcepto((String)this.attrs.get("concepto"));
			retiro.setImporte((Double)this.attrs.get("importe"));
			retiro.setIdAutorizo(idAutorizo);
			transaccion = new Transaccion((Long)this.attrs.get("idCierre"), retiro);
			if (transaccion.ejecutar(this.accion)) {
				if(this.accion.equals(EAccion.AGREGAR)) {	
     			JsfBase.setFlashAttribute("idEmpresa", this.attrs.get("idEmpresa"));
		    	JsfBase.setFlashAttribute("idCaja", this.attrs.get("idCaja"));
 	        JsfBase.setFlashAttribute("idCierreEstatus", this.caja.toLong("idCierreEstatus"));    			
					ticket= new CreateCierre(retiro.getImporte(), "RETIRO:" + retiro.getConsecutivo(), idAutorizo);
					UIBackingUtilities.execute("jsTicket.imprimirTicket('" + ticket.getPrincipal().getClave()  + "-" + retiro.getConsecutivo() + "','" + ticket.toHtml() + "');");
					UIBackingUtilities.execute("jsTicket.process('"+ JsfBase.getContext()+ "/Paginas/Mantic/Ventas/Caja/accion.jsf');");
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
    return null;
  } // doAccion
	
  public String doCancelar() {
		String regresar= (String)this.attrs.get("retorno");
		try {
			if(JsfBase.isCajero())
				regresar= "/Paginas/Mantic/Ventas/Caja/accion";
			else
				if(Cadena.isVacio(regresar))
					regresar= "ambos";
			JsfBase.setFlashAttribute("idCierre", this.attrs.get("idCierre"));
			if(this.attrs.get("idEmpresa")== null) {
				if(this.attrs.get("idEmpresas")!= null)
					JsfBase.setFlashAttribute("idEmpresa", ((UISelectEntity)this.attrs.get("idEmpresas")).getKey());
			} // if
			else	
				JsfBase.setFlashAttribute("idEmpresa", this.attrs.get("idEmpresa"));
			if(this.attrs.get("idCaja")== null) {
				if(this.attrs.get("idCajas")!= null)
					JsfBase.setFlashAttribute("idCaja", ((UISelectEntity)this.attrs.get("idCajas")).getKey());
			} // if
			else	
				JsfBase.setFlashAttribute("idCaja", this.attrs.get("idCaja"));
			if(this.caja!= null)
				JsfBase.setFlashAttribute("idCierreEstatus", this.caja.toLong("idCierreEstatus"));
			if(this.caja!= null)
				JsfBase.setFlashAttribute("idCierreEstatus", this.caja.toLong("idCierreEstatus"));
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    return regresar.concat(Constantes.REDIRECIONAR);
  } 
	
	private void toLoadEmpresas() throws Exception {
		List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
    try {
			columns= new ArrayList<>();
			if(this.attrs.get("idEmpresa")== null)
			  if(JsfBase.getAutentifica().getEmpresa().isMatriz())
          params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresaDepende());
			  else
				  params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
		  else
  		  params.put("idEmpresa", this.attrs.get("idEmpresa"));
			params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
			params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
      columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
			List<UISelectEntity> sucursales= (List<UISelectEntity>) UIEntity.build("TcManticEmpresasDto", "empresas", params, columns);
      this.attrs.put("sucursales", sucursales);
			if(this.caja!= null) {
				int index= sucursales.indexOf(new UISelectEntity(new Entity(this.caja.toLong("idEmpresa"))));
				if(index>= 0)
					this.attrs.put("idEmpresas", sucursales.get(index));
				else
			    this.attrs.put("idEmpresas", UIBackingUtilities.toFirstKeySelectEntity((List<UISelectEntity>)this.attrs.get("sucursales")));
			} // if
			else
			  this.attrs.put("idEmpresas", UIBackingUtilities.toFirstKeySelectEntity((List<UISelectEntity>)this.attrs.get("sucursales")));
			this.attrs.put("idEmpresa", ((UISelectEntity)this.attrs.get("idEmpresas")).getKey());
			this.doLoadCajas();
    } // try
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    }// finally
	}
	
	public void doLoadCajas() throws Exception {
		List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
    try {
			columns= new ArrayList<>();
      columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
			params.put("idEmpresa", ((UISelectEntity)this.attrs.get("idEmpresas")).getKey());
			List<UISelectEntity> cajas= (List<UISelectEntity>) UIEntity.build("TcManticCajasDto", "cajas", params, columns);
      this.attrs.put("cajas", cajas);
			if(this.caja!= null) {
				int index= cajas.indexOf(new UISelectEntity(new Entity(this.caja.toLong("idCaja"))));
				if(index>= 0)
					this.attrs.put("idCajas", cajas.get(index));
				else
    			this.attrs.put("idCajas", UIBackingUtilities.toFirstKeySelectEntity((List<UISelectEntity>)this.attrs.get("cajas")));
			} // if
			else
  			this.attrs.put("idCajas", UIBackingUtilities.toFirstKeySelectEntity((List<UISelectEntity>)this.attrs.get("cajas")));
			this.doLoadCierres();
    } // try
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    }// finally
	}

	public String doCheckUser() {
		String regresar   = null;
		String cuenta     = (String)this.attrs.get("cuenta");
		String contrasenia= (String)this.attrs.get("contrasenia");
		try {
			CambioUsuario	usuario= new CambioUsuario(cuenta, contrasenia);			
			if(usuario.validaPrivilegiosDescuentos()) {
				this.attrs.put("cuenta", "");
				this.attrs.put("contrasenia", "");
				regresar= this.doAceptar(usuario.getIdPersona());
				this.attrs.put("ok", Boolean.FALSE);
				UIBackingUtilities.execute("PF('widgetDialogoAutorizacion').hide();");
			} // if
			else
				this.attrs.put("ok", Boolean.TRUE);
	  } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
		return regresar;
	}
	
	public void doLoadCierres() {
		try {
 		  this.attrs.put("idCaja", ((UISelectEntity)this.attrs.get("idCajas")).getKey());
			Value cierre= (Value)DaoFactory.getInstance().toField("VistaCierresCajasDto", "cierre", this.attrs, "idKey");
			if(cierre!= null)
				this.attrs.put("idCierre", cierre.toLong());
			else
				this.attrs.put("idCierre", null);
	  } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
	}
	
}