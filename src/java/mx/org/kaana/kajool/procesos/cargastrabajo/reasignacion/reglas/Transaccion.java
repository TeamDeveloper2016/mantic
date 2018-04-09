package mx.org.kaana.kajool.procesos.cargastrabajo.reasignacion.reglas;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 11/10/2016
 *@time 01:31:34 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.beans.SelectionItem;
import org.hibernate.Session;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.kajool.db.dto.TcJanalMuestrasDto;
import mx.org.kaana.kajool.db.dto.TrJanalBitacoraReasignacionesDto;
import mx.org.kaana.kajool.db.dto.TrJanalMovimientosDto;
import mx.org.kaana.kajool.procesos.enums.ETipoMovimiento;
import org.apache.log4j.Logger;

public class Transaccion extends IBaseTnx {

  private static final Logger LOG = Logger.getLogger(Transaccion.class);
  protected Map<String, List<SelectionItem>> movimientos;
	private Long idUsuarioOrigen;
	private Long idUsuarioDestino;

  public Transaccion(Map<String, List<SelectionItem>> movimientos, Long idUsuarioOrigen, Long idUsuarioDestino) {
		this.movimientos     = movimientos;
		this.idUsuarioOrigen = idUsuarioOrigen;
		this.idUsuarioDestino= idUsuarioDestino;
	}	// Transaccion

  @Override
  protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {
    boolean regresar								= false;		
		int count												= 0;
		int countTotal									= 0;
		List<SelectionItem> contenidoMap= null;
    try {
      switch(accion) {
        case PROCESAR :					
					for(String keyMap: this.movimientos.keySet()){
						contenidoMap= this.movimientos.get(keyMap);
						if(!contenidoMap.isEmpty()){
							countTotal= countTotal + contenidoMap.size();
							for(SelectionItem item: contenidoMap){	
								regresar= inicializaMotorMovimientos(keyMap, item.getKey(), sesion);
								if(regresar)
									count++;
							}//for							
						} // if
					} // for										
        break;
      } // switch
			if(!regresar)
				throw new RuntimeException("Ocurrio un error en el proceso de reasignación.");
			JsfBase.addMessage("Movimientos", "[" + countTotal + "] Reasignaciones solicitadas \n [" + count + "] Reasignaciones procesadas", ETipoMensaje.INFORMACION);
			LOG.info("[" + countTotal + "] Reasignaciones solicitadas [" + count + "] Reasignaciones procesadas");
      LOG.info("Transaccion ".concat(accion.name()).concat(" para ? ["+ regresar+ "]."));			
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  } // ejecutar

  protected boolean inicializaMotorMovimientos(String keyMap, String itemKey, Session sesion) throws Exception{
		boolean regresar    = false;
		Long idEstatusActual= null;
		try {
			idEstatusActual= getEstatusActual(Long.valueOf(itemKey), sesion);
			if(keyMap.equals(this.idUsuarioDestino.toString()))
				regresar= motorMovimientos(sesion, this.idUsuarioDestino.toString(), this.idUsuarioOrigen.toString(), itemKey, idEstatusActual);													
			else if(keyMap.equals(this.idUsuarioOrigen.toString()))
				regresar= motorMovimientos(sesion, this.idUsuarioOrigen.toString(), this.idUsuarioDestino.toString(), itemKey, idEstatusActual);										
		} // try
		catch (Exception e) {						
			throw e;
		} // catch		
		return regresar;
	} // inicializaMotorMovimientos
	
	private boolean motorMovimientos(Session sesion, String idUsuarioAlta, String idUsuarioBaja, String idMuestra, Long idEstatus) throws Exception{
		boolean regresar= false;
		try {			
			if(movimientoBitacoraAct(sesion, idUsuarioAlta, idUsuarioBaja, idMuestra, ETipoMovimiento.ALTA.getKey())>= 1L){
				if(movimientoBitacoraAct(sesion, idUsuarioBaja, idUsuarioBaja, idMuestra, ETipoMovimiento.BAJA.getKey())>= 1L)
					regresar= generarMovimiento(sesion, idUsuarioAlta, idMuestra, idEstatus);				
			} // if			
		} // try
		catch (Exception e) {						
			throw e;
		} // catch		
		return regresar;
	} // motorMovimientos
	
	private Long movimientoBitacoraAct(Session sesion, String idUsuarioAct, String idUsuarioAnt, String idMuestra, Long idTipoMovimiento) throws Exception {		
		TrJanalBitacoraReasignacionesDto bitacora= null;
		Long regresar                            = 1L;
		try {			
			bitacora= new TrJanalBitacoraReasignacionesDto();
      bitacora.setIdUsuario(JsfBase.getIdUsuario());
      bitacora.setIdUsuarioAct(Long.valueOf(idUsuarioAct));
      bitacora.setIdUsuarioAnt(Long.valueOf(idUsuarioAnt));
			bitacora.setIdMuestra(Long.valueOf(idMuestra));						
			bitacora.setIdTipoMovimiento(idTipoMovimiento);						
			regresar= DaoFactory.getInstance().insert(sesion, bitacora);							
		} // try
		catch (Exception e) {
			throw e;
		} // catch
		return regresar;
	} // movimientoBitacoraAct
	
	protected boolean generarMovimiento(Session sesion, String idUsuario, String idMuestra, Long idEstatus) throws Exception {
		TrJanalMovimientosDto movimiento= null; 		
		Map<String, Object> params = null;		
		Long idMovimiento          = -1L;
		boolean regresar           = false;		
		try {
      params= new HashMap<>();
			movimiento= new TrJanalMovimientosDto();			
			movimiento.setIdEstatus(idEstatus);
			movimiento.setIdMuestra(Long.valueOf(idMuestra));			
			movimiento.setIdUsuario(Long.valueOf(idUsuario));
			movimiento.setObservaciones("Generación de movimiento[Usuario asigna[" + idUsuario + "] Muestra[" + idMuestra + "] Usuario aplica[" + JsfBase.getIdUsuario() + "]]");						
      movimiento.setIdSupervisa(getIdSupervisa(Long.valueOf(idMuestra), sesion));
      params.put("idMuestra", idMuestra);
			idMovimiento= DaoFactory.getInstance().insert(sesion, movimiento);
			sesion.flush();						
			params.clear();
			params.put("idMovimiento", idMovimiento);							
			regresar= DaoFactory.getInstance().update(sesion, TcJanalMuestrasDto.class, Long.valueOf(idMuestra), params) >= 1L;			
		} // try // try // try // try
		catch (Exception e) {
			throw e;
		} // catch
		finally{
			Methods.clean(params);
		} // finally
		return regresar;
	} // generarMovimiento				
	
	private Long getEstatusActual(Long idMuestra, Session sesion) throws Exception{
		Long regresar            = -1L;
		Map<String, Object>params= null;
		try {
			params= new HashMap<>();
			params.put("idMuestra", idMuestra);
			regresar= DaoFactory.getInstance().toField(sesion, "VistaReasignacionCargasTrabajoDto", "estatusActualMuestra", params, "idEstatus").toLong();
		} // try
		catch (Exception e) {						
			throw e;
		} // catch
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	} // getEstatusActual

	private Long getIdSupervisa(Long idMuestra, Session sesion) throws Exception{
		Long regresar            = -1L;
		Map<String, Object>params= null;
		try {
			params= new HashMap<>();
			params.put("idMuestra", idMuestra);
			regresar= DaoFactory.getInstance().toField(sesion, "VistaReasignacionCargasTrabajoDto", "estatusActualMuestra", params, "idSupervisa").toLong();
		} // try
		catch (Exception e) {						
			throw e;
		} // catch
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	} // getEstatusActual
}
