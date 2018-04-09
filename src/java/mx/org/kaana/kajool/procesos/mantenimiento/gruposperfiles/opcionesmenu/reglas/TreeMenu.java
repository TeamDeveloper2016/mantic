package mx.org.kaana.kajool.procesos.mantenimiento.gruposperfiles.opcionesmenu.reglas;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 27/08/2015
 * @time 10:59:37 AM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public class TreeMenu implements Serializable {

	private static final long serialVersionUID=335367674427792597L;
	private TreeNode root;
	private String llaveXml;
	private Map params;
	private boolean isExpandir;
	private boolean isSeleccionar;

	public TreeMenu(TreeNode root, String llaveXml, Map params, boolean isExpandir, boolean isSeleccionar) {
		this.root=root;
		this.llaveXml=llaveXml;
		this.params=params;
		this.isExpandir=isExpandir;
		this.isSeleccionar=isSeleccionar;
		load();
	}

	public TreeNode getRoot() {
		return root;
	}

	public Map getParams() {
		return params;
	}

	public void build(List<Entity> list, TreeNode padre) {
		try {
			TreeNode nodo   =null;
			Long nivel      =null;
			Long idSeleccion=null;
			while (!list.isEmpty()) {
				if (nivel!=null&&nodo!=null&&nivel.longValue()>list.get(0).toLong("nivel")) {
					break;
				} // if
				nivel=list.get(0).toLong("nivel");
				nodo=new DefaultTreeNode(list.get(0), padre);
				if (isSeleccionar) {
					idSeleccion=list.get(0).toLong("idSeleccion");
					if (!idSeleccion.equals(0L)) {
						nodo.setSelected(true);
					} // if
				} // if
				nodo.setExpanded(this.isExpandir);
				list.remove(0);
				if (!list.isEmpty()) {
					if (list.get(0).toLong("nivel")>nivel) {
						build(list, nodo);
					} // if
					else if (list.get(0).toLong("nivel")<nivel) {
						break;
					} // else
				} // if
			} // while
		} // try
		catch (Exception e) {
			mx.org.kaana.libs.formato.Error.mensaje(e);
		} // catch
	}

	private void load() {
		try {
		/*	String nombre=DaoFactory.getInstance().toEntity("VistaGruposMenusDto", "nombreMenuEncuesta", this.params).toValue("descripcion").toString();
			this.params.put("encuesta", nombre);
			String clave=DaoFactory.getInstance().toEntity("VistaGruposMenusDto", "claveMenuEncuesta", this.params).toValue("clave").toString();
			this.params.put("clave", clave);
		*/	List<Entity> listaTcMenus=(List<Entity>) DaoFactory.getInstance().toEntitySet(llaveXml, "row", this.params, Constantes.SQL_TODOS_REGISTROS);
			build(listaTcMenus, this.root);
		} // try
		catch (Exception e) {
			mx.org.kaana.libs.formato.Error.mensaje(e);
		} // catch
	}

	public static void main(String... args) {
		//	TreeMenu treeMenu= new TreeMenu();
		//  treeMenu.init();
	}
}
