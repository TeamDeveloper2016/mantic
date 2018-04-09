package mx.org.kaana.kajool.procesos.mantenimiento.indicadores.administracion.reglas;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date Mar 20, 2015
 *@time 2:02:30 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.libs.Constantes;
import org.hibernate.Session;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.beans.SelectionItem;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.ESql;

import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.kajool.db.dto.TcJanalDefinicionesAvancesDto;
import mx.org.kaana.kajool.db.dto.TrJanalEstatusAvancesDto;
import mx.org.kaana.kajool.db.dto.TrJanalPerfilesAvancesDto;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Transaccion extends IBaseTnx {

	private static final Log LOG = LogFactory.getLog(Transaccion.class);
	private TcJanalDefinicionesAvancesDto definicionAvance;
	private List<SelectionItem> listaEstatus;
	private Long idDefinicionAvance;
	private String[] perfiles;
	
	public Transaccion(Long idDefinicionAvance){
		this(null, null, idDefinicionAvance, null);
	}

	public Transaccion(TcJanalDefinicionesAvancesDto definicionAvance, List<SelectionItem> listaEstatus, String[] perfiles){
		this(definicionAvance, listaEstatus, -1L, perfiles);
	}

	public Transaccion(TcJanalDefinicionesAvancesDto definicionAvance, List<SelectionItem> listaEstatus, Long idDefinicionAvance, String[] perfiles) {
		this.definicionAvance=definicionAvance;
		this.listaEstatus=listaEstatus;
		this.idDefinicionAvance=idDefinicionAvance;
		this.perfiles= perfiles;
	}

	@Override
	protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {
		boolean regresar					= false;
		Long resultado						= -1L;
		Map<String, Object> params= null;
    try {
			params= new HashMap<>();
      switch(accion) {
        case AGREGAR :
					this.definicionAvance.setEstatusAplican(formeatearEstatus(this.listaEstatus));
          resultado= DaoFactory.getInstance().insert(sesion, this.definicionAvance);
					regresar = resultado != null && resultado.intValue() >= 1;
					if(this.perfiles!= null && this.perfiles.length> 0)
						regresar= registrarPerfiles(sesion, this.perfiles, resultado);
					regresar = registrarRelEstatusAvance(sesion, this.listaEstatus, resultado);
        break;
        case MODIFICAR :
					params.put("idDefinicionAvance", this.definicionAvance.getIdDefinicionAvance());
					this.definicionAvance.setEstatusAplican(formeatearEstatus(this.listaEstatus));
					resultado= DaoFactory.getInstance().execute(ESql.DELETE, sesion, "TrJanalEstatusAvancesDto", "rows", params);
					regresar = registrarRelEstatusAvance(sesion, this.listaEstatus, this.definicionAvance.getIdDefinicionAvance());
					regresar= registrarPerfiles(sesion, this.perfiles, this.definicionAvance.getIdDefinicionAvance());
					resultado= DaoFactory.getInstance().update(sesion, this.definicionAvance);
          regresar = resultado != null && resultado.intValue() >= 1;
        break;
        case ELIMINAR :
					params.put("idDefinicionAvance", this.idDefinicionAvance);
					regresar= eliminarPerfiles(sesion, this.idDefinicionAvance);
					resultado= DaoFactory.getInstance().execute(ESql.DELETE, sesion, "TrJanalEstatusAvancesDto", "rows", params);
					resultado= DaoFactory.getInstance().execute(ESql.DELETE, sesion, "TcJanalTablasTempAvaDto", "rows", params);
					resultado= DaoFactory.getInstance().delete(sesion, TcJanalDefinicionesAvancesDto.class, this.idDefinicionAvance);
					regresar = resultado != null && resultado.intValue() >= 1;
        break;
      } // switch
      LOG.info("Transaccion ".concat(accion.name()).concat(" para ? ["+ regresar+ "]."));
    } // try
    catch (Exception e) {
      throw e;
    } // catch
		finally{
			Methods.clean(params);
		} // finally
    return regresar;
	}
	
	private String formeatearEstatus(List<SelectionItem> lista){
		String regresar= "";
		for(SelectionItem item: lista){
			regresar= regresar.concat(item.getItem().concat(Constantes.SEPARADOR));
		} // for
		regresar= regresar.substring(0, regresar.length()- 1).toUpperCase();
		return regresar;
	}
	
	private boolean registrarRelEstatusAvance(Session sesion, List<SelectionItem> lista, Long idDefinicionAvance) throws Exception{
		boolean regresar					= false;
		TrJanalEstatusAvancesDto dto= null;
		Long orden= 1L;
		try {
			for(SelectionItem item: lista){
				dto= new TrJanalEstatusAvancesDto();
				dto.setIdDefinicionAvance(idDefinicionAvance);
				dto.setIdEstatusAvance(Numero.getLong(item.getKey()));
				dto.setOrden(orden);
				regresar= DaoFactory.getInstance().insert(sesion, dto)>= 1;
				orden++;
			} // for
		} // try
		catch (Exception e) {
			throw e;
		} // catch
		return regresar;
	}
	
	private boolean registrarPerfiles(Session sesion, String[] lista, Long idKey) throws Exception{
		boolean regresar															 = false;
		TrJanalPerfilesAvancesDto trJanalPerfilesAvancesDto= null;
		try {
			regresar= eliminarPerfiles(sesion, idKey);
			for(String item: lista){
				trJanalPerfilesAvancesDto= new TrJanalPerfilesAvancesDto();
				trJanalPerfilesAvancesDto.setIdDefinicionAvance(idKey);
				trJanalPerfilesAvancesDto.setIdPerfil(Numero.getLong(item));
				regresar= DaoFactory.getInstance().insert(sesion, trJanalPerfilesAvancesDto)>= 1;
			} // for
		} // try
		catch (Exception e) {
			throw e;
		} // catch
		return regresar;
	}
	
	private boolean eliminarPerfiles(Session sesion, Long idKey) throws Exception{
		boolean regresar					= false;
		Map<String, Object> params= null;
		try {
			params= new HashMap<>();
			params.put("idDefinicionAvance", idKey);
			regresar= DaoFactory.getInstance().execute(ESql.DELETE, sesion, "TrJanalPerfilesAvancesDto", "rows", params)>= 1;
		} // try
		catch (Exception e) {
			throw e;
		} // catch
		finally{
			Methods.clean(params);
		} // finally
		return regresar;
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		Methods.clean(this.listaEstatus);
	}

}
