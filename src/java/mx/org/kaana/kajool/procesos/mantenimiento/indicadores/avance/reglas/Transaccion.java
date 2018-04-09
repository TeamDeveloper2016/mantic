package mx.org.kaana.kajool.procesos.mantenimiento.indicadores.avance.reglas;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date Jul 24, 2014
 *@time 10:08:58 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Fecha;
import org.hibernate.Session;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.ECortes;
import mx.org.kaana.kajool.procesos.mantenimiento.indicadores.avance.beans.DefinicionTemporal;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.kajool.enums.ESql;
import mx.org.kaana.kajool.db.dto.TcJanalDefinicionesAvancesDto;
import mx.org.kaana.kajool.db.dto.TcJanalTablasTempAvaDto;
import mx.org.kaana.kajool.db.dto.TrJanalBitacorasAvancesDto;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Transaccion extends IBaseTnx {

	private static final Log LOG = LogFactory.getLog(Transaccion.class);
	private List<DefinicionTemporal> definiciones;

	public Transaccion(List<DefinicionTemporal> definiciones){
		this.definiciones= definiciones;
	}

	@Override
  protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {
    boolean regresar		 = false;
		Long idBitacoraAvance= null;
    try {
      switch(accion) {
        case AGREGAR :
          for(DefinicionTemporal definicion: this.definiciones){
						idBitacoraAvance= agregarBitacoraAvance(sesion, definicion.getTcJanalDefinicionesAvancesDto());
						generarTablasTemporales(sesion, definicion);
						agregarDetalleAvance(sesion, definicion, idBitacoraAvance);
						terminarBitacoraAvance(idBitacoraAvance);
					} // for
        break;
      } // switch
      LOG.info("Transaccion ".concat(accion.name()).concat(" para ? ["+ regresar+ "]."));
    } // try
    catch (Exception e) {
      throw e;
    } // catch
		finally{
			eliminarTablasTemporales(sesion);
		} // finally
    return regresar;
  } // ejecutar
	
	private Long agregarBitacoraAvance(Session sesion, TcJanalDefinicionesAvancesDto definicion) throws Exception{
		TrJanalBitacorasAvancesDto bitacoraAvance		= null;
		TrJanalBitacorasAvancesDto bitacoraAvanceClon= null;
		Map<String, Object> params							= null;
		Long regresar														= null;
		try {
			params= new HashMap<>();
			params.put("idDefinicionAvance", definicion.getKey());
			params.put("estatus", 1L);
			bitacoraAvance= (TrJanalBitacorasAvancesDto) DaoFactory.getInstance().findFirst(sesion, TrJanalBitacorasAvancesDto.class, "estatus", params);
			if(bitacoraAvance== null){
				bitacoraAvance= new TrJanalBitacorasAvancesDto();
				bitacoraAvance.setEstatus(1L);
				bitacoraAvance.setDescripcionProceso("Periodo: (".concat(Fecha.formatear( "dd/MM/yyyy",bitacoraAvance.getInicio())).concat(")"));
				bitacoraAvance.setFin(null);
				bitacoraAvance.setIdDefinicionAvance(definicion.getKey());
				regresar= DaoFactory.getInstance().insert(bitacoraAvance);
			} // if
			else{
				bitacoraAvanceClon= (TrJanalBitacorasAvancesDto) bitacoraAvance.clone();
				bitacoraAvance.setEstatus(0L);
				DaoFactory.getInstance().update(bitacoraAvance);
				bitacoraAvanceClon.setInicio(new Timestamp(Calendar.getInstance().getTimeInMillis()));
				bitacoraAvanceClon.setDescripcionProceso("Periodo: (".concat(Fecha.formatear("dd/MM/yyyy", bitacoraAvanceClon.getInicio())).concat(")"));
				bitacoraAvanceClon.setFin(null);
				regresar= DaoFactory.getInstance().insert(bitacoraAvanceClon);
			} // else
		} // try
		catch (Exception e) {
			throw e;
		} // catch
		finally{
			Methods.clean(params);
		} // finally
		return regresar;
	}
	
	private void terminarBitacoraAvance(Long idBitacoraAvance) throws Exception{
		Map<String, Object> params= new HashMap<>();
		try {
			params.put("fin", new Timestamp(Calendar.getInstance().getTimeInMillis()));
			DaoFactory.getInstance().update(TrJanalBitacorasAvancesDto.class, idBitacoraAvance, params);
		} // try
		catch (Exception e) {
			throw e;
		} // catch
		finally{
			Methods.clean(params);
		} // finally
	}
	
	private void generarTablasTemporales(Session sesion, DefinicionTemporal definicion) throws Exception{
		StringBuilder sql					= null;
		String[] tipo							= null;
		Map<String, Object> params= null;
		try {
			params= new HashMap<>();
			for(TcJanalTablasTempAvaDto tabla: definicion.getTablasTemporales()){
				sql= new StringBuilder();
				for(String campo: Cadena.toList(tabla.getCampos())){
					tipo= campo.split(Constantes.TILDE);
					sql.append(tipo[0]);
					sql.append(" ");
					sql.append(tipo[1]);
					sql.append(",");
				} // for campo
				sql.deleteCharAt(sql.length()- 1);
				params.put("tabla", tabla.getNombreTabla());
				params.put("campos", sql.toString());
				DaoFactory.getInstance().execute(ESql.INSERT, sesion, "AdministracionTablas", "row", params);
				params.clear();
				params.put("idGrupo", definicion.getTcJanalDefinicionesAvancesDto().getIdGrupo());
				DaoFactory.getInstance().execute(ESql.SELECT, tabla.getProceso(), tabla.getIdXml(), params);
				params.clear();
			} // for tabla
		} // try
		catch (Exception e) {
			throw e;
		} // catch
		finally{
			Methods.clean(params);
		} // finally
	}
	
	private void eliminarTablasTemporales(Session sesion) throws Exception{
		Map<String, Object> params= null;
		try {
			params= new HashMap<>();
			for(DefinicionTemporal definicion: this.definiciones){
				for(TcJanalTablasTempAvaDto tabla: definicion.getTablasTemporales()){
					params.put("tabla", tabla.getNombreTabla());
					DaoFactory.getInstance().execute(ESql.DELETE, sesion, "AdministracionTablas", "rows", params);
					DaoFactory.getInstance().execute(ESql.DELETE, sesion, "AdministracionTablas", "drop", params);
				} // for
			} // for
		} // try
		catch (Exception e) {
			throw e;
		} // catch
		finally{
			Methods.clean(params);
		} // finally
	}
	
	private void agregarDetalleAvance(Session sesion, DefinicionTemporal definicion, Long idBitacoraAvance) throws Exception {
		Map<String, Object> params= null;
		try {
			params= new HashMap<>();
			for(String estatusAplica: Cadena.toList(definicion.getTcJanalDefinicionesAvancesDto().getEstatusAplican())){
				estatusAplica= estatusAplica.replaceAll(" ", "_").toLowerCase();
				if(definicion.getTablasTemporales().size()> 1)
					params.put("tablaTemporal", definicion.getTablasTemporales().get(definicion.getTablasTemporales().size()- 1).getNombreTabla());
				else
					params.put("tablaTemporal", definicion.getTablasTemporales().get(0).getNombreTabla());
				params.put("idBitacoraAvance", idBitacoraAvance);
				params.put("nombreEstatus", estatusAplica);
				params.put("idDefinicionAvance", definicion.getTcJanalDefinicionesAvancesDto().getKey());
				for(ECortes eCortes: ECortes.values()){
					params.put("nivel", eCortes.getAmbito());
					switch(eCortes){
						case CENTRAL:
							params.put(Constantes.SQL_CONDICION, "id_organizacion is null and id_entidad is null and id_municipio is null and id_organizacion_grupo is null");
						break;
						case REGIONAL:
							params.put(Constantes.SQL_CONDICION, "id_organizacion is not null and id_entidad is null and id_municipio is null and id_organizacion_grupo is null");
						break;
						case ESTATAL:
							params.put(Constantes.SQL_CONDICION, "id_organizacion is not null and id_entidad is not null and id_municipio is null and id_organizacion_grupo is null");
						break;
						case MUNICIPIO:
							params.put(Constantes.SQL_CONDICION, "id_organizacion is not null and id_entidad is not null and id_municipio is not null and id_organizacion_grupo is null");
						break;
						case LOCALIDAD:
							params.put(Constantes.SQL_CONDICION, "id_organizacion is not null and id_entidad is not null and id_municipio is not null and id_organizacion_grupo is not null");
						break;
					} // switch
					DaoFactory.getInstance().execute(ESql.INSERT, sesion, "VistaAvanceIndicadores", "executeDetallesAvances", params);
				} // for
				params.clear();
			} // for
		} // try
		catch (Exception e) {
			throw e;
		} // catch
		finally{
			Methods.clean(params);
		} // finally
	}

}
