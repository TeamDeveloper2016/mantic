package mx.org.kaana.kajool.procesos.cargastrabajo.reasignacion.reglas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.beans.SelectionItem;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 11/10/2016
 *@time 03:08:00 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class GestorSQL implements Serializable{

  private static final long serialVersionUID= 5089550588472484554L;
  private String curp;	
	private Long idGrupo;
	private Long idEntidad;

	public GestorSQL(String curp) {
		this(curp, JsfBase.getAutentifica().getEmpleado().getIdGrupo(), null);		
	}		
	
	public GestorSQL(Long idGrupo){
		this(null, idGrupo, null);
	}
	
	public GestorSQL(String curp,  Long idGrupo, Long idEntidad) {
		this.curp     = curp;
		this.idGrupo  = idGrupo;
    this.idEntidad= idEntidad;
	}

  public Entity getInformacionUsuario(Long idPerfil) throws Exception {
    Entity regresar          = null;		
		Map<String, Object>params= null;		
		try {
			params= new HashMap<>();								
			params.put("curp", this.curp);
			params.put("idGrupo", this.idGrupo);
			params.put("entidad", this.idEntidad);
			params.put("idCapturista", idPerfil);
			regresar= (Entity) DaoFactory.getInstance().toEntity("VistaReasignacionCargasTrabajoDto", "busquedaUsuarioCurp", params);		
		} // try
		catch (Exception e) {						
			throw e;
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
		return regresar;
  } // getInformacionUsuario

  public List<SelectionItem> cargarFolios(Long idUsuario) throws Exception {
    List<SelectionItem> regresar= null;
    List<Entity> folios         = null;
    try {
      regresar= new ArrayList<>();
      folios= getFolios(idUsuario);		
      for(Entity centro: folios)
        regresar.add(new SelectionItem(centro.getKey().toString(), centro.get("control").toString().concat(" - ").concat(centro.get("folio").toString())));
    } // try
    catch(Exception e) {
      throw e;
    } // catch
    return regresar;
  } // cargarFolios

  private List<Entity> getFolios(Long idUsuario) throws Exception {
    List<Entity>regresar     = null;
		Map<String, Object>params= null;
		try {
			params= new HashMap<>();
			params.put("idUsuario", idUsuario);
			params.put("idGrupo", this.idGrupo);
			params.put("entidad", this.idEntidad);
			regresar= DaoFactory.getInstance().toEntitySet("VistaReasignacionCargasTrabajoDto", "reasignacion", params, Constantes.SQL_TODOS_REGISTROS);					
		} // try
		catch (Exception e) {						
			throw e;
		} // catch
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
  } // getFolios
}
