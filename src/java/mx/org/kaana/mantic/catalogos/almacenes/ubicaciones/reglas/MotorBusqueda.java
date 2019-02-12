package mx.org.kaana.mantic.catalogos.almacenes.ubicaciones.reglas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.mantic.catalogos.almacenes.ubicaciones.beans.OrganigramUbicacion;
import org.primefaces.model.DefaultOrganigramNode;
import org.primefaces.model.OrganigramNode;

public class MotorBusqueda implements Serializable {

	private static final long serialVersionUID=5778899664321119975L;	
	private String id;	

	public MotorBusqueda(String id) {
		this.id= id;
	} // MotorBusqueda
	
	public OrganigramUbicacion toParent() throws Exception {
		OrganigramUbicacion regresar       = null;
		Map<String, Object>params= null;
		try {
			params= new HashMap<>();
			params.put("idEmpresa", this.id);
			params.put("sortOrder", "");			
			regresar= (OrganigramUbicacion) DaoFactory.getInstance().toEntity(OrganigramUbicacion.class, "VistaEmpresasDto", "cuentas", params);
		} // try // try // try // try
		catch (Exception e) {
			throw e;
		} // catch		
		return regresar;
	} // toParent
	
	public List<OrganigramNode> toChildrens() throws Exception {
		List<OrganigramNode> regresar= null;
		List<OrganigramUbicacion> nodes     = null;
		Map<String, Object>params = null;
		try {
			regresar= new ArrayList<>();
				params= new HashMap<>();
				params.put("idEmpresaDeuda", this.id);
				nodes= DaoFactory.getInstance().toEntitySet(OrganigramUbicacion.class, "VistaEmpresasDto", "pagosDeuda", params, Constantes.SQL_TODOS_REGISTROS);
				if(!nodes.isEmpty()){				
					for(OrganigramUbicacion item: nodes) {						
						item.setUltimoNivel(true);								
						regresar.add(new DefaultOrganigramNode("pago", item, null));
					} // for
				} // if
		} // try // try // try // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toChildrens	
}