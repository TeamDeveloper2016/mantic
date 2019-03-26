package mx.org.kaana.mantic.catalogos.almacenes.ubicaciones.reglas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.mantic.catalogos.almacenes.ubicaciones.beans.OrganigramUbicacion;
import mx.org.kaana.mantic.enums.ENivelUbicacion;
import org.primefaces.model.DefaultOrganigramNode;
import org.primefaces.model.OrganigramNode;

public class MotorBusqueda implements Serializable {

	private static final long serialVersionUID=5778899664321119975L;	
	private OrganigramUbicacion ubicacion;		
	
	public MotorBusqueda(OrganigramUbicacion ubicacion) {
		this.ubicacion= ubicacion;		
	} // MotorBusqueda	
	
	public List<OrganigramNode> toChildrens() throws Exception {
		List<OrganigramNode> regresar  = null;
		List<OrganigramUbicacion> nodes= null;		
		try {
			regresar= new ArrayList<>();						
			if(this.ubicacion.getNivel()!= null){
				nodes= DaoFactory.getInstance().toEntitySet(OrganigramUbicacion.class, "VistaUbicacionesDto", this.ubicacion.getNivel().getIdXml(), this.ubicacion.toMap(), Constantes.SQL_TODOS_REGISTROS);
				if(!nodes.isEmpty()){				
					for(OrganigramUbicacion item: nodes) {							
						item.setUltimoNivel(this.ubicacion.getNivel().equals(ENivelUbicacion.CHAROLA));								
						item.setNivel(ENivelUbicacion.fromIdNivel(this.ubicacion.getNivel().getIdNivelUbicacion() + 1L));
						regresar.add(new DefaultOrganigramNode(ENivelUbicacion.fromIdNivel(this.ubicacion.getNivel().getIdNivelUbicacion()).getType(), item, null));
					} // for
				} // if
			} // if
		} // try 
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toChildrens	
}