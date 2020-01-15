package mx.org.kaana.mantic.incidentes.reglas;

import java.sql.Date;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.kajool.reglas.beans.Siguiente;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.db.dto.TcManticIncidentesBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticIncidentesDto;
import mx.org.kaana.mantic.enums.EEstatusIncidentes;
import mx.org.kaana.mantic.incidentes.beans.Incidente;
import org.hibernate.Session;

public class Transaccion extends IBaseTnx {

	private Incidente incidente;
	private String messageError;	

	public Transaccion(Incidente incidente) {
		this.incidente = incidente;
	}
	
	@Override
	protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {
		boolean regresar= false;
		try {
			switch(accion){
				case AGREGAR:			
					if(verificaExistente(sesion))
						regresar= registrarIncidente(sesion);
					break;
				case MODIFICAR:									
					if(verificaExistente(sesion))
						regresar= modificarIncidente(sesion, false);
					break;				
				case ASIGNAR:
					regresar= modificarIncidente(sesion, true);
					break;
			} // switch
			if(!regresar)
        throw new Exception("");
		} // try
		catch (Exception e) {			
			throw new Exception(this.messageError.concat("<br/>") + e);
		} // catch		
		return regresar;
	} // ejecutar
	
	private boolean verificaExistente(Session sesion) throws Exception{
		boolean regresar         = true;
		Entity registro          = null;
		Map<String, Object>params= null;
		int totalDias            = 0;
		Date initDate            = null;
		Calendar calendar        = null;
		try {
			this.messageError= "No es posible agregar una incidencia del mismo tipo al mismo empleado cuando hay una vigente.";
			totalDias= Fecha.getBetweenDays(this.incidente.getVigenciaInicio(), this.incidente.getVigenciaFin());
			initDate= this.incidente.getVigenciaInicio();
			if(totalDias== 0)
				totalDias=1;
			for(int count=0; count< totalDias; count++){
				params= new HashMap<>();
				params.put("idPersona", this.incidente.getIdPersona());
				params.put("idTipoIncidente", this.incidente.getIdTipoIncidente());
				params.put("estatus", toEstatus());
				params.put("fecha", Fecha.formatear(Fecha.FECHA_MYSQL, initDate));
				registro= (Entity) DaoFactory.getInstance().toEntity(sesion, "TcManticIncidentesDto", "existente", params);
				if(registro!= null && registro.isValid()){
					regresar= false;
					break;
				} // if
				else{
					calendar= Calendar.getInstance();
					calendar.setTime(initDate);
					calendar.add(Calendar.DAY_OF_YEAR, 1);
					initDate= new Date(calendar.getTimeInMillis());
				} // else
			} // for
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // verificaExistente
	
	private String toEstatus(){
		StringBuilder regresar= null;
		try {
			regresar= new StringBuilder("");
			regresar.append(EEstatusIncidentes.ACEPTADA.getIdEstatusInicidente());
			regresar.append(",");
			regresar.append(EEstatusIncidentes.APLICADA.getIdEstatusInicidente());
			regresar.append(",");
			regresar.append(EEstatusIncidentes.CAPTURADA.getIdEstatusInicidente());		
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar.toString();
	} // toEstatus
	
	private boolean registrarIncidente(Session sesion) throws Exception{
		boolean regresar         = false;
		TcManticIncidentesDto dto= null;
		Long key                 = -1L;
		Siguiente consecutivo    = null;
		try {
			dto= new TcManticIncidentesDto();
			consecutivo= this.toSiguiente(sesion);			
			dto.setConsecutivo(consecutivo.getOrden().toString());			
			dto.setOrden(consecutivo.getOrden());			
			dto.setEjercicio(Long.valueOf(Fecha.getAnioActual()));
			dto.setIdIncidenteEstatus(EEstatusIncidentes.CAPTURADA.getIdEstatusInicidente());
			dto.setIdPersona(this.incidente.getIdPersona());
			dto.setIdTipoIncidente(this.incidente.getIdTipoIncidente());
			dto.setIdUsuario(JsfBase.getIdUsuario());
			dto.setObservaciones(this.incidente.getObservaciones());
			dto.setVigenciaInicio(this.incidente.getVigenciaInicio());
			dto.setVigenciaFin(this.incidente.getVigenciaFin());		
			key= DaoFactory.getInstance().insert(sesion, dto);
			if(key>= 1L)
				regresar= registrarBitacora(sesion, key, EEstatusIncidentes.CAPTURADA.getIdEstatusInicidente());
		} // try
		catch (Exception e) {			
			throw e;
		} // catch	
		return regresar;
	} // registrarIncidente
	
	private boolean registrarBitacora(Session sesion, Long idIncidente, Long idEstatus) throws Exception{
		boolean regresar                 = false;
		TcManticIncidentesBitacoraDto dto= null;
		try {
			dto= new TcManticIncidentesBitacoraDto();
			dto.setIdIncidente(idIncidente);
			dto.setIdIncidenteEstatus(idEstatus);
			dto.setIdUsuario(JsfBase.getIdUsuario());
			dto.setJustificacion(this.incidente.getObservaciones());
			regresar= DaoFactory.getInstance().insert(sesion, dto)>= 1L;
		} // try
		catch (Exception e) {
			throw e;
		} // catch		
		return regresar;
	} // registrarBitacora
	
	private Siguiente toSiguiente(Session sesion) throws Exception {
		Siguiente regresar        = null;
		Map<String, Object> params= null;
		try {
			params=new HashMap<>();
			params.put("ejercicio", this.getCurrentYear());			
			params.put("operador", this.getCurrentSign());
			Value next= DaoFactory.getInstance().toField(sesion, "TcManticIncidentesDto", "siguiente", params, "siguiente");
			if(next.getData()!= null)
				regresar= new Siguiente(next.toLong());
			else
				regresar= new Siguiente(Configuracion.getInstance().isEtapaDesarrollo()? 900001L: 1L); 
		} // try		
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	} // toSiguiente
	
	private boolean modificarIncidente(Session sesion, boolean estatus) throws Exception{
		boolean regresar         = false;
		TcManticIncidentesDto dto= null;
		try {
			dto= (TcManticIncidentesDto) DaoFactory.getInstance().findById(sesion, TcManticIncidentesDto.class, this.incidente.getIdIncidente());
			if(estatus)
				dto.setIdIncidenteEstatus(this.incidente.getIdIncidenteEstatus());
			dto.setIdPersona(this.incidente.getIdPersona());
			dto.setIdTipoIncidente(this.incidente.getIdTipoIncidente());			
			dto.setObservaciones(this.incidente.getObservaciones());
			dto.setVigenciaInicio(this.incidente.getVigenciaInicio());
			dto.setVigenciaFin(this.incidente.getVigenciaFin());		
			if(DaoFactory.getInstance().update(sesion, dto)>= 1L){
				this.incidente.setObservaciones("Actualizaci�n del incidente anteriormente registrado.");
				regresar= registrarBitacora(sesion, this.incidente.getIdIncidente(), dto.getIdIncidenteEstatus());
			} // if
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // modificarIncidente
	
	private boolean eliminarIncidente(Session sesion) throws Exception{
		boolean regresar= false;
		TcManticIncidentesDto dto= null;
		try {
			dto= (TcManticIncidentesDto) DaoFactory.getInstance().findById(sesion, TcManticIncidentesDto.class, this.incidente.getIdIncidente());
			dto.setIdIncidenteEstatus(EEstatusIncidentes.CANCELADA.getIdEstatusInicidente());
			if(DaoFactory.getInstance().update(sesion, dto)>= 1L){
				this.incidente.setObservaciones("La incidencia fue cancelada.");
				regresar= registrarBitacora(sesion, this.incidente.getIdIncidente(), dto.getIdIncidenteEstatus());
			} // if
		} // try
		catch (Exception e) {			
			throw e;
		} // catch
		return regresar;
	} // eliminarIncidente
}
