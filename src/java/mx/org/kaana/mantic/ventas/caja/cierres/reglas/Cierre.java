package mx.org.kaana.mantic.ventas.caja.cierres.reglas;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.kajool.reglas.beans.Siguiente;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.db.dto.TcManticCierresAlertasDto;
import mx.org.kaana.mantic.db.dto.TcManticCierresBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticCierresCajasDto;
import mx.org.kaana.mantic.db.dto.TcManticCierresDto;
import mx.org.kaana.mantic.db.dto.TcManticTiposMediosPagosDto;
import mx.org.kaana.mantic.enums.ETipoMediosPago;
import mx.org.kaana.mantic.ventas.caja.cierres.beans.Denominacion;
import mx.org.kaana.mantic.ventas.caja.cierres.beans.Importe;
import org.apache.log4j.Logger;
import org.hibernate.Session;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 7/08/2018
 *@time 01:09:18 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Cierre extends IBaseTnx implements Serializable  {

	private static final long serialVersionUID=-2843960976905038609L;
  private static final Logger LOG = Logger.getLogger(Cierre.class);

	private Long idCaja;
	private Double inicial;
	private TcManticCierresDto cierre;
	private List<Importe> importes;
	private List<Denominacion> denominaciones;
	private Long idApertura;
	private String messageError;

  public Long getIdCaja() {
    return idCaja;
  }

  public void setIdCaja(Long idCaja) {
    this.idCaja = idCaja;
  }
  
	public Cierre(Long idCaja) {
		this(idCaja, 0D);
	}
	
	public Cierre(Long idCaja, Double inicial) {
		this(idCaja, inicial, new TcManticCierresDto("", -1L, 2L, JsfBase.getIdUsuario(), 1L, "", 0L, new Long(Fecha.getAnioActual()), new Timestamp(Calendar.getInstance().getTimeInMillis())), Collections.EMPTY_LIST, Collections.EMPTY_LIST);
	}
	
	public Cierre(Long idCaja, Double inicial, TcManticCierresDto cierre, List<Importe> importes, List<Denominacion> denominaciones) {
		this.idCaja        = idCaja;
		this.inicial       = inicial;
		this.cierre        = cierre;
		this.importes      = importes;
		this.denominaciones= denominaciones;
		this.idApertura    = -1L;
	}

	public Long getIdApertura() {
		return idApertura;
	}

	public TcManticCierresDto getCierre() {
		return cierre;
	}
	
	@Override
	protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {
		boolean regresar                     = false;
		List<TcManticTiposMediosPagosDto> all= null;
		TcManticCierresCajasDto registro     = null; 
		TcManticCierresBitacoraDto bitacora  = null;
		Map<String, Object> params           = new HashMap<>();
		try {
			Siguiente consecutivo= null;
			this.messageError= "Ocurrio un error en ".concat(accion.name().toLowerCase()).concat(" en el cierre de caja.");
			switch(accion) {
				case AGREGAR:
					bitacora= new TcManticCierresBitacoraDto("CIERRE DEFINITIVO", -1L, this.cierre.getIdCierre(), JsfBase.getIdUsuario(), 3L);
					regresar= DaoFactory.getInstance().insert(sesion, bitacora)>= 1L;
					this.cierre.setIdCierreEstatus(3L);
					this.cierre.setIdDiferencias(this.toDiferencias()? 1L: 2L);
					this.cierre.setTermino(new Timestamp(Calendar.getInstance().getTimeInMillis()));
					regresar= DaoFactory.getInstance().update(sesion, this.cierre)>= 1L;
					sesion.flush();
					for (Importe importe: this.importes) {
						LOG.error("[Cierre] Medio pago:" + importe.getIdTipoMedioPago() + ", Cierre:" + importe.getIdCierreCaja()+ ", Caja:" + importe.getIdCaja() + ", Disponible:" + importe.getDisponible()+ ", Acumulado anterior:" + importe.getAcumulado() + ", Saldo anterior:" + importe.getSaldo());
					  DaoFactory.getInstance().update(sesion, importe);
					} // for
					for (Denominacion denominacion: this.denominaciones) 
					  DaoFactory.getInstance().insert(sesion, denominacion);
					// cambiar el estatus a todas las ventas realizadas en dia que no fueron cobradas a canceladas
					//params.put("idCaja", this.idCaja);
					//params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
					//DaoFactory.getInstance().updateAll(sesion, TcManticVentasDto.class, params);

					// cambiar de estatus todas las alertas de esta caja para iniciar el nuevo corte
					params.put("idCaja", this.idCaja);
					params.put("idCierre", this.cierre.getIdCierre());
					DaoFactory.getInstance().updateAll(sesion, TcManticCierresAlertasDto.class, params);
					
					// inicio del nuevo corte de caja con los valores iniciales
					consecutivo= this.toSiguiente(sesion);
					TcManticCierresDto apertura= new TcManticCierresDto(
						consecutivo.getConsecutivo(), -1L, 2L, JsfBase.getIdUsuario(), 1L, "", consecutivo.getOrden(), new Long(Fecha.getAnioActual()), new Timestamp(Calendar.getInstance().getTimeInMillis())
					);
					regresar= DaoFactory.getInstance().insert(sesion, apertura)>= 1L;
					this.idApertura= apertura.getIdCierre();					
					params.put("intermediario", ETipoMediosPago.INTERMEDIARIO_PAGOS.getIdTipoMedioPago());
					all= (List<TcManticTiposMediosPagosDto>)DaoFactory.getInstance().findViewCriteria(TcManticTiposMediosPagosDto.class, params, "caja");
					for (TcManticTiposMediosPagosDto medio : all) {
						if(medio.getIdTipoMedioPago().equals(1L))
						  registro= new TcManticCierresCajasDto(medio.getIdTipoMedioPago(), apertura.getIdCierre(), 0D, this.idCaja, -1L, this.inicial, new Date(Calendar.getInstance().getTimeInMillis()), 0D, this.inicial);
						else
						  registro= new TcManticCierresCajasDto(medio.getIdTipoMedioPago(), apertura.getIdCierre(), 0D, this.idCaja, -1L, 0D, new Date(Calendar.getInstance().getTimeInMillis()), 0D, 0D);
					  DaoFactory.getInstance().insert(sesion, registro);
					} // for
					for (Denominacion denominacion: this.denominaciones) {
						Denominacion clon= denominacion.copy();
						clon.setIdCierreMoneda(-1L);
						clon.setIdCierre(apertura.getIdCierre());
						clon.setIdEfectivo(2L);
					  DaoFactory.getInstance().insert(sesion, clon);
					} // for
					
					bitacora= new TcManticCierresBitacoraDto("APERTURA DE CAJA", -1L, apertura.getIdCierre(), JsfBase.getIdUsuario(), 1L);
					regresar= DaoFactory.getInstance().insert(sesion, bitacora)>= 1L;
					break;
				case PROCESAR:
					params.put("idCaja", this.idCaja);
					params.put("idCierre", this.cierre.getIdCierre());					
					TcManticCierresCajasDto efectivo= (TcManticCierresCajasDto)DaoFactory.getInstance().findFirst(sesion, TcManticCierresCajasDto.class, "caja", params);
					if(efectivo!= null) {
						if(efectivo.getAcumulado()== 0) {
						  efectivo.setDisponible(this.inicial);
						  efectivo.setSaldo(this.inicial);
						} // if
						else {
							double diferencia= this.inicial- efectivo.getDisponible();
						  efectivo.setDisponible(efectivo.getDisponible()+ diferencia);
						  efectivo.setSaldo(efectivo.getSaldo()+ diferencia);
						} // else
						regresar= DaoFactory.getInstance().update(sesion, efectivo)> 0L;
					} // if
					for (Denominacion denominacion: this.denominaciones) 
					  regresar= DaoFactory.getInstance().update(sesion, denominacion)> 0L;
					break;
				case REGISTRAR:
					regresar= this.toNewCierreCaja(sesion);
					break;
				case ELIMINAR:
          params.put("idCaja", this.idCaja);
					this.cierre= (TcManticCierresDto)DaoFactory.getInstance().toEntity(sesion, TcManticCierresDto.class, "VistaCierresCajasDto", "depurar", params);
					if(this.cierre!= null) {
  					// FALTA VALIDAR QUE NO TENGA NINGUNA VENTA ASOCIADA A LA CAJA (tr_ventas_medio_pago) Y FALTA VALIDAR QUE NO TENGA NINGUN RETIRO O ABONO DE CAJA (tc_mantic_cierres_retiros) 
						Value value= DaoFactory.getInstance().toField(sesion, "TrManticVentaMedioPagoDto", "depurar", params, "total");
						if(value.toLong()== 0) {
						  value= DaoFactory.getInstance().toField(sesion, "VistaCierresCajasDto", "abonos", params, "total");
						  if(value.toLong()== 0) {
								this.cierre.setIdCierreEstatus(3L);
								this.cierre.setObservaciones("CIERRE CANCELADO POR QUE SE ELIMINO LA CAJA ["+ this.idCaja+ "]");
								regresar= DaoFactory.getInstance().update(sesion, this.cierre)>= 1L;
								params.put("idCaja", this.idCaja);
								params.put("idCierre", this.cierre.getIdCierre());
								DaoFactory.getInstance().deleteAll(TcManticCierresCajasDto.class, params);
								bitacora= new TcManticCierresBitacoraDto("CIERRE CANCELADO POR QUE SE ELIMINO LA CAJA", -1L, this.cierre.getIdCierre(), JsfBase.getIdUsuario(), 4L);
								regresar= DaoFactory.getInstance().insert(sesion, bitacora)>= 1L;
							} // if	
							else
								new RuntimeException("No se puede eliminar la caja porque tiene abonos/retiros asociados ["+ value.toLong()+ "]");
						} // if	
						else
							new RuntimeException("No se puede eliminar la caja porque tiene ventas asociadas ["+ value.toLong()+ "]");
					} // if	
					break;
			} // switch
		  if(!regresar)
        throw new Exception("");
		} // try
		catch (Exception e) {
      Error.mensaje(e);			
			throw new Exception(this.messageError.concat("<br/>")+ e);
		} // catch		
		finally {
			Methods.clean(params);
		} // finally
		LOG.info("Se genero de forma correcta el cierre de caja: "+ this.cierre.getConsecutivo());
		return regresar;
	}

	private Siguiente toSiguiente(Session sesion) throws Exception {
		Siguiente regresar        = null;
		Map<String, Object> params= null;
		try {
			params=new HashMap<>();
			params.put("ejercicio", this.getCurrentYear());
			params.put("idCierre", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
			params.put("operador", this.getCurrentSign());
			Value next= DaoFactory.getInstance().toField(sesion, "TcManticCierresDto", "siguiente", params, "siguiente");
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
			
	private boolean toDiferencias() {
		boolean regresar= false;
		for (Importe importe : this.importes) {
			if(!regresar && !importe.getSaldo().equals(importe.getImporte()))
				regresar= true;
		} // for
		return regresar;
	}
  
  protected boolean toRegistrar(Session sesion) throws Exception {
		boolean regresar= true;
    List<TcManticTiposMediosPagosDto> all= null; 
		TcManticCierresBitacoraDto bitacora  = null;
    TcManticCierresCajasDto registro     = null; 
		Siguiente consecutivo= this.toSiguiente(sesion);
    try {
      this.cierre.setConsecutivo(consecutivo.getConsecutivo());
      this.cierre.setOrden(consecutivo.getOrden());
      regresar= DaoFactory.getInstance().insert(sesion, this.cierre)>= 1L;
			Map<String, Object> params= new HashMap<>();
			params.put("intermediario", ETipoMediosPago.INTERMEDIARIO_PAGOS.getIdTipoMedioPago());
      all= (List<TcManticTiposMediosPagosDto>)DaoFactory.getInstance().findViewCriteria(TcManticTiposMediosPagosDto.class, params, "caja");
      for (TcManticTiposMediosPagosDto medio : all) {
        if(medio.getIdTipoMedioPago().equals(1L))
          registro= new TcManticCierresCajasDto(medio.getIdTipoMedioPago(), this.cierre.getIdCierre(), 0D, this.idCaja, -1L, this.inicial, new Date(Calendar.getInstance().getTimeInMillis()), 0D, this.inicial);
        else
          registro= new TcManticCierresCajasDto(medio.getIdTipoMedioPago(), this.cierre.getIdCierre(), 0D, this.idCaja, -1L, 0D, new Date(Calendar.getInstance().getTimeInMillis()), 0D, 0D);
        DaoFactory.getInstance().insert(sesion, registro);
      } // for
      if(this.denominaciones!= null && !this.denominaciones.isEmpty())
        for (Denominacion denominacion: this.denominaciones) {
          denominacion.setIdCierre(this.cierre.getIdCierre());
          regresar= DaoFactory.getInstance().insert(sesion, denominacion)>= 1L;
        } // for
      bitacora= new TcManticCierresBitacoraDto("", -1L, this.cierre.getIdCierre(), JsfBase.getIdUsuario(), 1L);
      regresar= DaoFactory.getInstance().insert(sesion, bitacora)>= 1L;
    }
    catch (Exception e) {
      throw e;
    } // catch
		return regresar;
	}
  
  protected boolean toModificaRegistro(Session sesion, TcManticCierresCajasDto cierreCaja) throws Exception {
		boolean regresar= true; 
    TcManticCierresBitacoraDto bitacora  = null;
    try {
      regresar= DaoFactory.getInstance().update(sesion, this.cierre)>= 1L;
      DaoFactory.getInstance().update(sesion, cierreCaja);
      if(this.denominaciones!= null && !this.denominaciones.isEmpty())
        for (Denominacion denominacion: this.denominaciones) {
          denominacion.setIdCierre(this.cierre.getIdCierre());
          regresar= DaoFactory.getInstance().update(sesion, denominacion)>= 1L;
        } // for
      bitacora= new TcManticCierresBitacoraDto("MODIFICADO DESDE MODIFICAR CAJAS", -1L, this.cierre.getIdCierre(), JsfBase.getIdUsuario(), 1L);
      regresar= DaoFactory.getInstance().insert(sesion, bitacora)>= 1L;
    }
    catch (Exception e) {
      throw e;
    } // catch
		return regresar;
	}
  
  protected boolean toEliminar(Session sesion) throws Exception {
		boolean regresar                     = true;
    Map<String, Object> params           = new HashMap<>();
    TcManticCierresBitacoraDto bitacora  = null;
    try {
      params.put("idCaja", this.idCaja);
      this.cierre= (TcManticCierresDto)DaoFactory.getInstance().toEntity(sesion, TcManticCierresDto.class, "VistaCierresCajasDto", "depurar", params);
      if(this.cierre!= null) {
        // FALTA VALIDAR QUE NO TENGA NINGUNA VENTA ASOCIADA A LA CAJA (tr_ventas_medio_pago) Y FALTA VALIDAR QUE NO TENGA NINGUN RETIRO O ABONO DE CAJA (tc_mantic_cierres_retiros) 
        Value value= DaoFactory.getInstance().toField(sesion, "TrManticVentaMedioPagoDto", "depurar", params, "total");
        if(value.toLong()== 0) {
          value= DaoFactory.getInstance().toField(sesion, "VistaCierresCajasDto", "abonos", params, "total");
          if(value.toLong()== 0) {
            this.cierre.setIdCierreEstatus(3L);
            this.cierre.setObservaciones("CIERRE CANCELADO POR QUE SE ELIMINO LA CAJA ["+ this.idCaja+ "]");
            regresar= DaoFactory.getInstance().update(sesion, this.cierre)>= 1L;
            params.put("idCaja", this.idCaja);
            params.put("idCierre", this.cierre.getIdCierre());
            DaoFactory.getInstance().deleteAll(TcManticCierresCajasDto.class, params);
            bitacora= new TcManticCierresBitacoraDto("CIERRE CANCELADO POR QUE SE ELIMINO LA CAJA", -1L, this.cierre.getIdCierre(), JsfBase.getIdUsuario(), 4L);
            regresar= DaoFactory.getInstance().insert(sesion, bitacora)>= 1L;
          } // if	
          else
            new RuntimeException("No se puede eliminar la caja porque tiene abonos/retiros asociados ["+ value.toLong()+ "]");
        } // if	
        else
          new RuntimeException("No se puede eliminar la caja porque tiene ventas asociadas ["+ value.toLong()+ "]");
      } // if	
    }
    catch (Exception e) {
      throw e;
    } // catch
		return regresar;
	}  
	
  public boolean toNewCierreCaja(Session sesion) throws Exception {
		TcManticCierresCajasDto registro= null; 		
		Siguiente consecutivo= this.toSiguiente(sesion);
		this.cierre.setConsecutivo(consecutivo.getConsecutivo());
		this.cierre.setOrden(consecutivo.getOrden());
		DaoFactory.getInstance().insert(sesion, this.cierre);
		Map<String, Object>params= new HashMap<>();
		params.put("intermediario", ETipoMediosPago.INTERMEDIARIO_PAGOS.getIdTipoMedioPago());
		List<TcManticTiposMediosPagosDto> all= (List<TcManticTiposMediosPagosDto>)DaoFactory.getInstance().findViewCriteria(TcManticTiposMediosPagosDto.class, params, "caja");
		for (TcManticTiposMediosPagosDto medio : all) {
			if(medio.getIdTipoMedioPago().equals(1L))
				registro= new TcManticCierresCajasDto(medio.getIdTipoMedioPago(), this.cierre.getIdCierre(), 0D, this.idCaja, -1L, this.inicial, new Date(Calendar.getInstance().getTimeInMillis()), 0D, this.inicial);
			else
				registro= new TcManticCierresCajasDto(medio.getIdTipoMedioPago(), this.cierre.getIdCierre(), 0D, this.idCaja, -1L, 0D, new Date(Calendar.getInstance().getTimeInMillis()), 0D, 0D);
			DaoFactory.getInstance().insert(sesion, registro);
		} // for
		if(this.denominaciones!= null && !this.denominaciones.isEmpty())
			for (Denominacion denominacion: this.denominaciones) {
				denominacion.setIdCierre(this.cierre.getIdCierre());
				DaoFactory.getInstance().insert(sesion, denominacion);
			} // for
		TcManticCierresBitacoraDto bitacora= new TcManticCierresBitacoraDto("", -1L, this.cierre.getIdCierre(), JsfBase.getIdUsuario(), 1L);
		return DaoFactory.getInstance().insert(sesion, bitacora)>= 1L;
	}	
}