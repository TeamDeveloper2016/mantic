package mx.org.kaana.libs.facturama.test;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.facturama.models.Client;
import mx.org.kaana.libs.facturama.models.response.CfdiSearchResult;
import mx.org.kaana.libs.facturama.reglas.CFDIFactory;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.libs.formato.Error;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 30/10/2018
 * @time 10:29:39 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public class Clientes {
	
	private static final Log LOG=LogFactory.getLog(Clientes.class);

	/**
	 * @param args the command line arguments
	 */
	
	private static Long toFindEntidad(String entidad) throws Exception {
		Long regresar= -1L;
		Map<String, Object> params=null;
		try {
			params=new HashMap<>();
			String codigo= entidad.toUpperCase().replaceAll(Constantes.CLEAN_SQL, "").trim();
			params.put("descripcion", codigo.replaceAll("(,| |\\t)+", ".*.*"));
			Value value= DaoFactory.getInstance().toField("TcJanalEntidadesDto", "entidad", params, "idEntidad");
			if(value!= null && value.getData()!= null)
				regresar= value.toLong();
			else {
				value= DaoFactory.getInstance().toField("TcJanalEntidadesDto", "primero", params, "idEntidad");
  			if(value!= null && value.getData()!= null)
	  			regresar= value.toLong();
			} // else
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			throw e;
		} // catch
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	}

	private static Long toFindMunicipio(Long idEntidad, String municipio) throws Exception {
		Long regresar= -1L;
		Map<String, Object> params=null;
		try {
			params=new HashMap<>();
			params.put("idEntidad", idEntidad);
			String codigo= municipio.toUpperCase().replaceAll(Constantes.CLEAN_SQL, "").trim();
			params.put("descripcion", codigo.replaceAll("(,| |\\t)+", ".*.*"));
			Value value= DaoFactory.getInstance().toField("TcJanalMunicipiosDto", "municipio", params, "idMunicipio");
			if(value!= null && value.getData()!= null)
				regresar= value.toLong();
			else {
				value= DaoFactory.getInstance().toField("TcJanalMunicipiosDto", "primero", params, "idMunicipio");
  			if(value!= null && value.getData()!= null)
	  			regresar= value.toLong();
			} // else
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			throw e;
		} // catch
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	}
	
	private static Long toFindLocalidad(Long idMunicipio, String localidad) throws Exception {
		Long regresar= -1L;
		Map<String, Object> params=null;
		try {
			params=new HashMap<>();
			params.put("idMunicipio", idMunicipio);
			params.put("descripcion", localidad!= null? localidad.toUpperCase().replaceAll(Constantes.CLEAN_SQL, "").trim().replaceAll("(,| |\\t)+", ".*.*"): "XYZ");
			Value value= DaoFactory.getInstance().toField("TcJanalLocalidadesDto", "localidad", params, "idLocalidad");
			if(value!= null && value.getData()!= null)
				regresar= value.toLong();
			else {
				value= DaoFactory.getInstance().toField("TcJanalLocalidadesDto", "primero", params, "idLocalidad");
  			if(value!= null && value.getData()!= null)
	  			regresar= value.toLong();
			} // else
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			throw e;
		} // catch
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	}
	
	public static void main(String[] args) throws Exception {
		List<CfdiSearchResult> cfdis= CFDIFactory.getInstance().getCfdis();
		List<Client> clients        =	CFDIFactory.getInstance().getClients();
		Collections.sort(cfdis);
		int count= 0;
		StringBuilder ent= new StringBuilder();
		StringBuilder mun= new StringBuilder();
		StringBuilder loc= new StringBuilder();
		for (CfdiSearchResult cfdi : cfdis) {
			Client client= clients.get(clients.indexOf(new Client(cfdi.getRfc())));
			//LOG.warn(++count+ ".- "+ cfdi.getDate()+ " -> "+ client.getName()+ "  ["+ client.getRfc()+ "]");
			Long idEntidad  = toFindEntidad(client.getAddress().getState());
			Long idMunicipio= toFindMunicipio(idEntidad, client.getAddress().getMunicipality());
			Long idLocalidad= toFindLocalidad(idMunicipio, client.getAddress().getLocality());
			LOG.warn(idEntidad+ " ["+ client.getAddress().getState()+ "], "+ idMunicipio+ " ["+ client.getAddress().getMunicipality()+ "], "+ idLocalidad+ "["+ client.getAddress().getLocality()+ "]");
			//LOG.warn(client.getAddress().getState()+ " - "+ client.getAddress().getMunicipality()+ " - "+ client.getAddress().getLocality()+ " - "+ client.getAddress().getNeighborhood());
			// break;
			if(idEntidad< 0L)
				ent.append(client.getAddress().getState()).append(", ");
			if(idMunicipio< 0L)
				mun.append(client.getAddress().getMunicipality()).append(", ");
			if(idLocalidad< 0L)
				loc.append(client.getAddress().getLocality()).append(", ");
		} // for
		LOG.info("Entidades que no existen: ["+ ent.toString()+ "]");
		LOG.info("Municipios que no existen: ["+ mun.toString()+ "]");
		LOG.info("Localidades que no existen: ["+ loc.toString()+ "]");
		LOG.info("Ok");

	}

}
