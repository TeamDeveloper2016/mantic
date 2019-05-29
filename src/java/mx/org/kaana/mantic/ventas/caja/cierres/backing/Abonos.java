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
import mx.org.kaana.mantic.ventas.beans.TicketVenta;
import mx.org.kaana.mantic.ventas.caja.beans.Pago;
import mx.org.kaana.mantic.ventas.caja.cierres.reglas.CreateCierre;
import mx.org.kaana.mantic.ventas.caja.cierres.reglas.Transaccion;
import mx.org.kaana.mantic.ventas.caja.reglas.CreateTicket;
import mx.org.kaana.mantic.ventas.reglas.AdminTickets;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 7/05/2018
 *@time 03:29:13 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

@Named(value= "manticVentasCajaCierresAbonos")
@ViewScoped
public class Abonos extends IBaseAttribute implements Serializable {

  private static final long serialVersionUID = 327393488565639361L;
  
	private Entity caja;
	private EAccion accion;

  @Override
	@PostConstruct
  protected void init() {		
    try {
      this.accion = EAccion.ASIGNAR;
			this.attrs.put("retorno", JsfBase.getParametro("zwkl")== null || "0".equals(JsfBase.getParametro("zwkl"))? "ambos": "/Paginas/Mantic/Ventas/Caja/accion");
      this.attrs.put("idCierre", JsfBase.getFlashAttribute("idCierre")== null? -1L: JsfBase.getFlashAttribute("idCierre"));
			this.attrs.put("idEmpresa", JsfBase.getFlashAttribute("idEmpresa"));
			this.attrs.put("idCaja", JsfBase.getFlashAttribute("idCaja"));
      this.attrs.put("limite", 3000.0);
      this.attrs.put("importe", 0.0);
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
		Value cierre= null;
    try {
      this.attrs.put("nombreAccion", Cadena.letraCapital(this.accion.name()));
    	if(JsfBase.getFlashAttribute("idCierre")== null) {
				this.toLoadEmpresas();
				cierre= (Value)DaoFactory.getInstance().toField("VistaCierresCajasDto", "cierre", this.attrs, "idKey");
				if(cierre!= null)
					this.attrs.put("idCierre", cierre.toLong());
				this.caja= (Entity)DaoFactory.getInstance().toEntity("VistaCierresCajasDto", "caja", this.attrs);
			} // if	
			else {
				this.caja= (Entity)DaoFactory.getInstance().toEntity("VistaCierresCajasDto", "caja", this.attrs);
				this.toLoadEmpresas();
			} // if
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

  public String doAceptar() {  
    Transaccion transaccion= null;
    String regresar        = null;
		CreateCierre ticket    = null;
    try {			
			TcManticCierresRetirosDto abono= new TcManticCierresRetirosDto(-1L);
			abono.setIdAbono(1L);
			abono.setIdTipoMedioPago(1L);
			abono.setConcepto((String)this.attrs.get("concepto"));
			abono.setImporte((Double)this.attrs.get("importe"));
			transaccion = new Transaccion((Long)this.attrs.get("idCierre"), abono);
			if (transaccion.ejecutar(this.accion)) {
				if(this.accion.equals(EAccion.ASIGNAR)) {	
					if(JsfBase.isCajero())
						regresar = "/Paginas/Mantic/Ventas/Caja/accion".concat(Constantes.REDIRECIONAR);
					else
						regresar = "ambos".concat(Constantes.REDIRECIONAR);
     			JsfBase.setFlashAttribute("idEmpresa", this.attrs.get("idEmpresa"));
		    	JsfBase.setFlashAttribute("idCaja", this.attrs.get("idCaja"));
 	        JsfBase.setFlashAttribute("idCierreEstatus", this.caja.toLong("idCierreEstatus"));    			
					ticket= new CreateCierre(abono.getImporte(), "ABONO:" + abono.getConsecutivo());
					UIBackingUtilities.execute("jsTicket.imprimirTicket('" + ticket.getPrincipal().getClave()  + "-" + abono.getConsecutivo() + "','" + ticket.toHtml() + "');");
					UIBackingUtilities.execute("jsTicket.clicTicket();");
					UIBackingUtilities.execute("executeReturn(" + abono.getConsecutivo() + ");");							
					init();
				} // if	
 				if(!this.accion.equals(EAccion.CONSULTAR)) 
  				JsfBase.addMessage("Se ".concat(this.accion.equals(EAccion.AGREGAR)? "agregó": this.accion.equals(EAccion.COMPLETO) ? "aplicó": "modificó").concat(" el abono de caja."), ETipoMensaje.INFORMACION);
  			JsfBase.setFlashAttribute("idCierre", this.attrs.get("idCierre"));
			} // if
			else 
				JsfBase.addMessage("Ocurrió un error al registrar el abono de caja.", ETipoMensaje.ERROR);      			
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    return regresar;
  } // doAccion
	
  public String doCancelar() {   
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
    return ((String)this.attrs.get("retorno")).concat(Constantes.REDIRECIONAR);
  } 
	
	private void toLoadEmpresas() {
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
			if(this.attrs.get("idCajas")!= null)
				this.attrs.put("limite", ((UISelectEntity)this.attrs.get("idCajas")).toDouble("limite"));
 		  this.attrs.put("idCaja", ((UISelectEntity)this.attrs.get("idCajas")).getKey());
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