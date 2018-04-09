package mx.org.kaana.kajool.procesos.mantenimiento.indicadores.avance.jobs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.db.dto.TcJanalDefinicionesAvancesDto;
import mx.org.kaana.kajool.db.dto.TcJanalTablasTempAvaDto;
import mx.org.kaana.kajool.procesos.mantenimiento.indicadores.avance.beans.DefinicionTemporal;
import mx.org.kaana.kajool.procesos.mantenimiento.indicadores.avance.reglas.Transaccion;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date Jul 23, 2014
 *@time 2:39:15 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public abstract class IndicadorAvance implements Job, Serializable{
	
	private static final Logger LOG= Logger.getLogger(IndicadorAvance.class);
	private static final long serialVersionUID=8320879033120893756L;
	
	private Long idGrupo;
	
	public abstract Long getGrupo();

	@Override
	public void execute(JobExecutionContext jec) throws JobExecutionException {
		List<TcJanalDefinicionesAvancesDto> tcJanalDefinicionesAvancesDto= null;
		Map<String, Object> params														 = null;
		List<DefinicionTemporal> definiciones									 = null;
		Transaccion transaccion																 = null;
		try {
			this.idGrupo= getGrupo();
			params= new HashMap<>();
			definiciones= new ArrayList<>();
			params.put("idGrupo", this.idGrupo);
			params.put("activo", 1L);
			tcJanalDefinicionesAvancesDto= DaoFactory.getInstance().toEntitySet(TcJanalDefinicionesAvancesDto.class, "TcJanalDefinicionesAvancesDto", "activo", params);
			for(TcJanalDefinicionesAvancesDto definicionAvance: tcJanalDefinicionesAvancesDto)
				definiciones.add(agregarDefinicion(definicionAvance));
			transaccion= new Transaccion(definiciones);
			transaccion.ejecutar(EAccion.AGREGAR);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
		} // catch
		finally{
			Methods.clean(definiciones);
			Methods.clean(params);
			Methods.clean(tcJanalDefinicionesAvancesDto);
		} // finally
	}
	
	private DefinicionTemporal agregarDefinicion(TcJanalDefinicionesAvancesDto dto) throws Exception{
		DefinicionTemporal regresar= null;
		Map<String, Object> params = null;
		try {
			params= new HashMap<>();
			params.put("idDefinicionAvance", dto.getKey());
			regresar= new DefinicionTemporal(dto, DaoFactory.getInstance().toEntitySet(TcJanalTablasTempAvaDto.class, "TcJanalTablasTempAvaDto", "definicionAvance", params));
		} // try
		catch (Exception e) {
			throw e;
		} // catch
		finally{
			Methods.clean(params);
		} // finally
		return regresar;
	}

}
