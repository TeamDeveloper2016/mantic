package mx.org.kaana.mantic.ventas.caja.cierres.reglas;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.kajool.reglas.beans.Siguiente;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.formato.Global;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.db.dto.TcManticCajasDto;
import mx.org.kaana.mantic.db.dto.TcManticCierresAlertasDto;
import mx.org.kaana.mantic.db.dto.TcManticCierresBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticCierresCajasDto;
import mx.org.kaana.mantic.db.dto.TcManticCierresDto;
import mx.org.kaana.mantic.db.dto.TcManticCierresRetirosDto;
import org.apache.log4j.Logger;
import org.hibernate.Session;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 7/08/2018
 *@time 01:09:18 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Transaccion extends IBaseTnx implements Serializable  {

	private static final long serialVersionUID=-2843960976905038609L;
  private static final Logger LOG = Logger.getLogger(Transaccion.class);

	private TcManticCierresRetirosDto retiro;
	private Long idCierre;
	private String messageError;
	
	public Transaccion(Long idCierre, TcManticCierresRetirosDto retiro) {
		this.idCierre= idCierre;
		this.retiro= retiro;
	}
	
	@Override
	protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {
		boolean regresar= false;
		TcManticCierresDto cierre          = null;
		TcManticCierresCajasDto caja       = null;
		TcManticCierresBitacoraDto bitacora= null;
		try {
			this.messageError= "Ocurrio un error en ".concat(accion.name().toLowerCase()).concat(" el retiro de caja.");
			Siguiente consecutivo = null;
			switch(accion) {
				case AGREGAR:
				case ASIGNAR:
					if(this.retiro.getIdAbono().equals(1L))
  					bitacora= new TcManticCierresBitacoraDto("ABONO DE EFECTIVO", -1L, this.idCierre, JsfBase.getIdUsuario(), 2L);
				  else	
  					bitacora= new TcManticCierresBitacoraDto("RETIRO DE EFECTIVO", -1L, this.idCierre, JsfBase.getIdUsuario(), 2L);
					regresar= DaoFactory.getInstance().insert(sesion, bitacora)>= 1L;
					caja= (TcManticCierresCajasDto)DaoFactory.getInstance().findFirst(TcManticCierresCajasDto.class, "caja", bitacora.toMap());
					if(this.retiro.getIdAbono().equals(1L)) {
  					caja.setSaldo(Numero.toRedondearSat(caja.getSaldo()+ Math.abs(this.retiro.getImporte())));
						caja.setAcumulado(Numero.toRedondearSat(caja.getAcumulado()+ Math.abs(this.retiro.getImporte())));
					} // if	
					else {
  					caja.setSaldo(Numero.toRedondearSat(caja.getSaldo()- Math.abs(this.retiro.getImporte())));
						caja.setAcumulado(Numero.toRedondearSat(caja.getAcumulado()- Math.abs(this.retiro.getImporte())));
  					this.retiro.setImporte(this.retiro.getImporte()* -1L);
					} // else
					regresar= DaoFactory.getInstance().update(sesion, caja)>= 1L;
					consecutivo= this.toSiguiente(sesion);
					this.retiro.setConsecutivo(consecutivo.getConsecutivo());
					this.retiro.setOrden(consecutivo.getOrden());
					this.retiro.setEjercicio(new Long(Fecha.getAnioActual()));
					this.retiro.setIdUsuario(JsfBase.getIdUsuario());
					this.retiro.setIdCierreCaja(caja.getIdCierreCaja());
					regresar= DaoFactory.getInstance().insert(sesion, this.retiro)>= 1L;
					cierre= (TcManticCierresDto)DaoFactory.getInstance().findById(TcManticCierresDto.class, this.idCierre);
					cierre.setIdCierreEstatus(2L);
					regresar= DaoFactory.getInstance().update(sesion, cierre)>= 1L;
					this.toCheckCajaAlerta(sesion, caja);
					break;
				case ELIMINAR:
					this.retiro.setIdTerminado(1L);
					regresar= DaoFactory.getInstance().update(sesion, this.retiro)>= 1L;
					if(this.retiro.getIdAbono().equals(1L))
					  this.retiro.setConcepto("CANCELADO POR "+ JsfBase.getAutentifica().getCredenciales().getCuenta());
					else
					  this.retiro.setConcepto("CANCELADO POR "+ JsfBase.getAutentifica().getCredenciales().getCuenta());
					this.retiro.setImporte(this.retiro.getImporte()* -1L);
					consecutivo= this.toSiguiente(sesion);
					this.retiro= new TcManticCierresRetirosDto(
						consecutivo.getConsecutivo(), 
						JsfBase.getIdUsuario(), 
						-1L, 
						"CONSECUTIVO: ".concat(this.retiro.getConsecutivo()), 
						this.retiro.getIdCierreCaja(), 
						consecutivo.getOrden(), 
						this.retiro.getIdAbono(), 
						this.retiro.getImporte(), 
						new Long(Fecha.getAnioActual()), 
						this.retiro.getConcepto(), 
						this.retiro.getIdTipoMedioPago(),
						1L,
						JsfBase.getAutentifica().getPersona().getIdPersona()
					);
					regresar= DaoFactory.getInstance().insert(sesion, this.retiro)>= 1L;
					if(this.retiro.getIdAbono().equals(1L))
					  bitacora= new TcManticCierresBitacoraDto("ABONO DE EFECTIVO CANCELADO POR "+ JsfBase.getAutentifica().getCredenciales().getCuenta(), -1L, this.idCierre, JsfBase.getIdUsuario(), 2L);
					else
					  bitacora= new TcManticCierresBitacoraDto("RETIRO DE EFECTIVO CANCELADO POR "+ JsfBase.getAutentifica().getCredenciales().getCuenta(), -1L, this.idCierre, JsfBase.getIdUsuario(), 2L);
					regresar= DaoFactory.getInstance().insert(sesion, bitacora)>= 1L;
					caja= (TcManticCierresCajasDto)DaoFactory.getInstance().findFirst(TcManticCierresCajasDto.class, "caja", bitacora.toMap());
					if(this.retiro.getIdAbono().equals(1L)) {
  					caja.setSaldo(Numero.toRedondearSat(caja.getSaldo()- Math.abs(this.retiro.getImporte())));
						caja.setAcumulado(Numero.toRedondearSat(caja.getAcumulado()- Math.abs(this.retiro.getImporte())));
					} // if	
					else {
  					caja.setSaldo(Numero.toRedondearSat(caja.getSaldo()+ Math.abs(this.retiro.getImporte())));
						caja.setAcumulado(Numero.toRedondearSat(caja.getAcumulado()+ Math.abs(this.retiro.getImporte())));
					} // else	
					regresar= DaoFactory.getInstance().update(sesion, caja)>= 1L;
					this.toCheckCajaAlerta(sesion, caja);
					break;
			} // switch
		  if(!regresar)
        throw new Exception("");
		} // try
		catch (Exception e) {
      Error.mensaje(e);			
			throw new Exception(this.messageError.concat("<br/>")+ e);
		} // catch		
		LOG.info("Se genero de forma correcta el registro del "+ (this.retiro.getIdAbono().equals(1L)? "abono": "retiro")+ " de caja: "+ this.retiro.getConsecutivo());
		return regresar;
	}

	private Siguiente toSiguiente(Session sesion) throws Exception {
		Siguiente regresar        = null;
		Map<String, Object> params= null;
		try {
			params=new HashMap<>();
			params.put("ejercicio", this.getCurrentYear());
			params.put("operador", this.getCurrentSign());
			Value next= DaoFactory.getInstance().toField(sesion, "TcManticCierresRetirosDto", "siguiente", params, "siguiente");
			if(next.getData()!= null)
			  regresar= new Siguiente(next.toLong());
			else
			  regresar= new Siguiente(Configuracion.getInstance().isEtapaDesarrollo()? 900001L: 1L);
		} // try
		catch (Exception e) {
			throw e;
		} // catch
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	}	
			
	private void toCheckCajaAlerta(Session sesion, TcManticCierresCajasDto caja) throws Exception {
		Map<String, Object> params= null;
		TcManticCajasDto limite   = null;
		Entity existe             = null; 
		try {
			params=new HashMap<>();
			// esto es para quitar la alerta por el retiro de efectivo
			limite= (TcManticCajasDto)DaoFactory.getInstance().findById(TcManticCajasDto.class, caja.getIdCaja());
			if(limite!= null) {
				if(caja.getSaldo()< limite.getLimite()) {
					params.put("idCierre", caja.getIdCierre());
					DaoFactory.getInstance().updateAll(sesion, TcManticCierresAlertasDto.class, params);
				} // if
				else {
				 existe= (Entity)DaoFactory.getInstance().toEntity(sesion, "VistaCierresCajasDto", "alerta", params);
				 if(existe== null || existe.isEmpty()) {
  				 params.put("idCierre", caja.getIdCierre());
					 DaoFactory.getInstance().updateAll(sesion, TcManticCierresAlertasDto.class, params);
					 TcManticCierresAlertasDto alerta= new TcManticCierresAlertasDto(caja.getIdCierre(), JsfBase.getIdUsuario(), 1L, "EL SALDO EN EFECTIVO ["+ 
						 Global.format(EFormatoDinamicos.MONEDA_SAT_DECIMALES, caja.getSaldo())+ 
						 "] DE ESTA CAJA SUPERA AL LIMITE ["+ Global.format(EFormatoDinamicos.MONEDA_SAT_DECIMALES, limite.getLimite())
						 + "] ESTABLECIDO PARA LA MISMA, EN EL PROCESO "+ (this.retiro.getIdAbono().equals(2L)? "RETIRO": "ABONO")+ "[AUTOMATICO]", -1L, caja.getSaldo());
					 DaoFactory.getInstance().insert(sesion, alerta);
				 } // if
				} // else
			} // if					
		} // try
		finally {
			Methods.clean(params);
		} // finally
	}
}
