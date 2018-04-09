package mx.org.kaana.kajool.procesos.mantenimiento.gruposperfiles.opcionesmenu.backing;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date Sep 2, 2015
 * @time 10:55:29 AM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.procesos.mantenimiento.gruposperfiles.opcionesmenu.reglas.TreeMenu;
import org.primefaces.event.NodeUnselectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.dto.TcJanalGruposDto;
import mx.org.kaana.kajool.db.dto.TcJanalPerfilesDto;
import mx.org.kaana.kajool.procesos.mantenimiento.menus.beans.OpcionMenu;
import mx.org.kaana.kajool.procesos.mantenimiento.gruposperfiles.opcionesmenu.reglas.Transaccion;
import mx.org.kaana.kajool.enums.EAccion;
import org.primefaces.event.NodeSelectEvent;

@ManagedBean(name="kajoolMantenimientoGruposperfilesOpciones")
@ViewScoped
public class Filtro extends IBaseAttribute implements Serializable{
  private static final long serialVersionUID=4145363731900920046L;

  private TreeNode[] selectedNodes;
  private boolean isPerfiles;

  public TreeNode[] getSelectedNodes() {
    return selectedNodes;
  }

  public void setSelectedNodes(TreeNode[] selectedNodes) {
    this.selectedNodes=selectedNodes;
  }

  @PostConstruct
  @Override
  protected void init() {
    Long idGrupo = null;
    Long idPerfil= null;
    try {
      this.attrs.put("root", new DefaultTreeNode("root", null));
      idGrupo = JsfBase.getFlashAttribute("idGrupo") != null ? (Long)(JsfBase.getFlashAttribute("idGrupo")) : -1L;
      idPerfil = JsfBase.getFlashAttribute("idPerfil") != null ? (Long)(JsfBase.getFlashAttribute("idPerfil")) : -1L;
      this.isPerfiles = !(idPerfil.equals(-1L));
      this.attrs.put("idGrupo", idGrupo);
      this.attrs.put("titulo", this.isPerfiles? "del perfil: ".concat(((TcJanalPerfilesDto)DaoFactory.getInstance().findById(TcJanalPerfilesDto.class, idPerfil)).getDescripcion()): "del grupo: ".concat(((TcJanalGruposDto)DaoFactory.getInstance().findById(TcJanalGruposDto.class, idGrupo)).getDescripcion()));
      this.attrs.put("idPerfil", idPerfil);
      doLoad();
    } // try
    catch (Exception e) {
      JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
  } // init

  public String doGuardar(){
      List<OpcionMenu>  originales  = null;
      List<OpcionMenu> seleccionados= null;
      List<OpcionMenu> nuevos       = null;
      Transaccion transaccion       = null;
    try {
      //JsfBase.setFlashAttribute("idGrupo", this.attrs.get("idGrupo"));
      originales = getOriginales();
      seleccionados= getSeleccionados();
      nuevos = getSeleccionados();
      nuevos.removeAll(originales);
      originales.removeAll(seleccionados);
      transaccion = new Transaccion(nuevos, originales, Long.valueOf(this.attrs.get("idGrupo").toString()), Long.valueOf(this.attrs.get("idPerfil").toString()), this.isPerfiles);
      if(transaccion.ejecutar(EAccion.AGREGAR));
        JsfBase.addMessage("Opciones de menú modificadas con éxito");
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    finally{
      Methods.clean(originales);
      Methods.clean(seleccionados);
      Methods.clean(nuevos);
    } // finally
   return doRegresar();
  }//doGuardar

  private Map getParams(){
    Map regresar = new HashMap();
    if (this.isPerfiles){
      regresar.put("idPerfil", this.attrs.get("idPerfil"));
      regresar.put("llaveXml", "VistaPerfilesMenusDto") ;
     } // if
     else{
      regresar.put("llaveXml", "VistaGruposMenusDto") ;
    }
    regresar.put("idGrupo", this.attrs.get("idGrupo"));
    return regresar;
  }//getParams

  public String doRegresar(){
    String regresar = null;
    if (!this.isPerfiles)
      regresar = "/Paginas/Mantenimiento/GruposPerfiles/Grupos/filtro".concat(Constantes.REDIRECIONAR);
    else{
       JsfBase.setFlashAttribute("idGrupo", Long.valueOf(this.attrs.get("idGrupo").toString()));
       regresar = "/Paginas/Mantenimiento/GruposPerfiles/Perfiles/filtro".concat(Constantes.REDIRECIONAR);
    }//else
    return regresar;
  }//doRegresar
	
	
  public void onNodeUnselect(NodeUnselectEvent event) {
    seleccionarPadres(event.getTreeNode());
    event.getTreeNode().setSelected(false);
  }  //onNodeSelect

  public void onNodeSelect(NodeSelectEvent event) {
      seleccionarPadres(event.getTreeNode());
   }//onNodeUnselect

  private List<OpcionMenu> getSeleccionados(){
    List<OpcionMenu> regresar = new ArrayList();
    int i = 0;
    for(i = 0; i< this.selectedNodes.length;i++){
      Entity entity = (Entity)this.selectedNodes[i].getData();
      if(!isPerfiles)
        regresar.add(new OpcionMenu(entity.toLong("idMenu"), entity.toString("descripcion"), entity.toLong("idMenuGrupo")));
     else
        regresar.add(new OpcionMenu(entity.toLong("idMenu"), entity.toString("descripcion"), entity.toLong("idMenuGrupo"),entity.toLong("idMenuPerfil")));
    } // for
    return regresar;
  }//getSeleccionados

  private List<OpcionMenu> getOriginales(){
    List<OpcionMenu> regresar = new ArrayList();
    Map params = new HashMap();
    try {
      params = getParams();
      /*String nombre = DaoFactory.getInstance().toEntity("VistaGruposMenusDto", "nombreMenuEncuesta", this.getParams()).toValue("descripcion").toString();
      params.put("encuesta", nombre);
      String clave = DaoFactory.getInstance().toEntity("VistaGruposMenusDto", "claveMenuEncuesta", params).toValue("clave").toString();
      params.put("clave", clave);
     */ regresar = DaoFactory.getInstance().toEntitySet(OpcionMenu.class, params.get("llaveXml").toString(), "seleccionados",params, -1L);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
    finally{
      Methods.clean(params);
    }
    return regresar;
  }//getOriginales

   private void seleccionarPadres(TreeNode nodo){
     TreeNode padre = nodo.getParent();
		 while(padre!= ((DefaultTreeNode)this.attrs.get("root"))){
		 	 for(TreeNode node: padre.getChildren()){
				 if(node.isSelected()){
				 	 padre.setSelected(true);
				 	 padre.setExpanded(true);
					 break;
				} // if
			} // for
			padre = padre.getParent();
		} // while
  }//seleccionarPadres

  public void doLoad() {
    try {
      Map params = getParams();
      this.attrs.put("treeMenu", new TreeMenu(((DefaultTreeNode)this.attrs.get("root")), params.get("llaveXml").toString(), params, this.isPerfiles, true));
    } // try
    catch (Exception e) {
      JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
  }//doLoad
	
}
