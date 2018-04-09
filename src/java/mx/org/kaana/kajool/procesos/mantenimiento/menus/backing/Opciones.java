package mx.org.kaana.kajool.procesos.mantenimiento.menus.backing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.procesos.mantenimiento.gruposperfiles.opcionesmenu.reglas.TreeMenu;
import mx.org.kaana.kajool.procesos.mantenimiento.menus.beans.OpcionMenu;
import mx.org.kaana.kajool.procesos.mantenimiento.gruposperfiles.opcionesmenu.reglas.Transaccion;
import mx.org.kaana.libs.Constantes;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.NodeUnselectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;


/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 26/08/2015
 * @time 06:29:09 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
@ManagedBean(name="kajoolMantenimientoMenusOpciones")
@ViewScoped
public class Opciones implements Serializable {

	private static final long serialVersionUID=5957311718733751637L;
	private TreeNode root;
	private TreeMenu treeMenu;
	private TreeNode[] selectedNodes;
	private Long idGrupo;
	private Long idPerfil;

	public Long getIdGrupo() {
		return idGrupo;
	}

	public void setIdGrupo(Long idGrupo) {
		this.idGrupo=idGrupo;
	}

	public Long getIdPerfil() {
		return idPerfil;
	}

	public void setIdPerfil(Long idPerfil) {
		this.idPerfil=idPerfil;
	}
	private boolean isPerfiles;

	public TreeNode[] getSelectedNodes() {
		return selectedNodes;
	}

	public void setSelectedNodes(TreeNode[] selectedNodes) {
		this.selectedNodes=selectedNodes;
	}

	public TreeNode getRoot() {
		return root;
	}

	@PostConstruct
	private void init() {
		this.root      =new DefaultTreeNode("root", null);
		this.idGrupo   =JsfBase.seekParameter("idGrupo")!=null ? Numero.getLong(JsfBase.seekParameter("idGrupo").toString()) : this.idGrupo;
		this.idPerfil  =JsfBase.seekParameter("idPerfil")!=null ? Numero.getLong(JsfBase.seekParameter("idPerfil").toString()) : this.idPerfil;
		this.isPerfiles=!(this.idPerfil==null||this.idPerfil.equals(0L));
		Map params     =getParams();
		this.treeMenu=new TreeMenu(this.root, params.get("llaveXml").toString(), params, this.isPerfiles, true);
	} // init

	public String doGuardar() {
		List<OpcionMenu> originales   =null;
		List<OpcionMenu> seleccionados=null;
		List<OpcionMenu> nuevos       =null;
		Transaccion transaccion       =null;
		try {
			JsfBase.setFlashAttribute("idGrupo", this.idGrupo);
			originales=getOriginales();
			seleccionados=getSeleccionados();
			nuevos=getSeleccionados();
			nuevos.removeAll(originales);
			originales.removeAll(seleccionados);
			transaccion=new Transaccion(nuevos, originales, this.idGrupo, this.idPerfil, this.isPerfiles);
			if (transaccion.ejecutar(EAccion.AGREGAR));
			  JsfBase.addMessage("Opciones de menú modificadas con éxito");
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
		finally {
			Methods.clean(originales);
			Methods.clean(seleccionados);
			Methods.clean(nuevos);
		} // finally
		return doRegresar();
	}

	private Map getParams() {
		Map regresar=new HashMap();
		if (this.isPerfiles) {
			regresar.put("idPerfil", idPerfil);
			regresar.put("llaveXml", "VistaPerfilesMenusDto");
		} // if
		else {
			regresar.put("llaveXml", "VistaGruposMenusDto");
		} // else
		regresar.put("idGrupo", this.idGrupo);
		return regresar;
	}

	public String doRegresar() {
		String regresar=null;
		if (!this.isPerfiles) {
			regresar="/Paginas/Mantenimiento/GruposPerfiles/Grupos/filtro";
		} // if
		else {
			JsfBase.setFlashAttribute("idGrupo", this.idGrupo);
			regresar="/Paginas/Mantenimiento/GruposPerfiles/Perfiles/filtro";
		} // else
		return regresar;
	}

	public void onNodeUnselect(NodeUnselectEvent event) {
		seleccionarPadres(event.getTreeNode());
		event.getTreeNode().setSelected(false);
	}

	public void onNodeSelect(NodeSelectEvent event) {
		seleccionarPadres(event.getTreeNode());
	}

	private List<OpcionMenu> getSeleccionados() {
		List<OpcionMenu> regresar=new ArrayList<>();
		int i=0;
		for (i=0; i<this.selectedNodes.length; i++) {
			Entity entity=(Entity) this.selectedNodes[i].getData();
			if (!isPerfiles) {
				regresar.add(new OpcionMenu(entity.toLong("idMenu"), entity.toString("descripcion"), entity.toLong("idMenuGrupo")));
			} // if
			else {
				regresar.add(new OpcionMenu(entity.toLong("idMenu"), entity.toString("descripcion"), entity.toLong("idMenuGrupo"), entity.toLong("idMenuPerfil")));
			} // else
		} // for
		return regresar;
	}

	private List<OpcionMenu> getOriginales() throws Exception {
		List<OpcionMenu> regresar=new ArrayList<>();
		Map params=new HashMap();
		try {
			params=getParams();			
			regresar=DaoFactory.getInstance().toEntitySet(OpcionMenu.class, params.get("llaveXml").toString(), "seleccionados", this.getParams(), Constantes.SQL_TODOS_REGISTROS);
		} // try
		catch (Exception e) {
			throw e;
		} // catch
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	}

	private void seleccionarPadres(TreeNode nodo) {
		TreeNode padre=nodo.getParent();
		while (padre!=this.root) {
			for (TreeNode node : padre.getChildren()) {
				if (node.isSelected()) {
					padre.setSelected(true);
					padre.setExpanded(true);
					break;
				} // if
			} // for
			padre=padre.getParent();
		} // while
	}
}
