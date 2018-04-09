package mx.org.kaana.kajool.procesos.mantenimiento.gruposperfiles.opcionesencabezado.backing;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 21/09/2015
 *@time 08:20:48 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import java.util.List;
import java.io.Serializable;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.db.dto.TcJanalPerfilesDto;
import mx.org.kaana.kajool.procesos.mantenimiento.gruposperfiles.opcionesencabezado.reglas.Transaccion;
import mx.org.kaana.kajool.procesos.mantenimiento.gruposperfiles.opcionesmenu.reglas.TreeMenu;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.NodeUnselectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

@ManagedBean(name="kajoolMantenimientoGruposperfilesOpcionesEncabezado")
@ViewScoped
public class Filtro extends IBaseAttribute implements Serializable {

  private static final Log LOG = LogFactory.getLog(Filtro.class);
  private static final long serialVersionUID = -4544079195942956429L;
	private TreeNode[] selectedNodes;

  public TreeNode[] getSelectedNodes() {
    return selectedNodes;
  }

  public void setSelectedNodes(TreeNode[] selectedNodes) {
    this.selectedNodes = selectedNodes;
  }

  @PostConstruct
  @Override
	protected void init() {
    try {
      this.attrs.put("root", new DefaultTreeNode("root", null));
      this.attrs.put("idGrupo", JsfBase.getFlashAttribute("idGrupo") != null ? (Long)(JsfBase.getFlashAttribute("idGrupo")) : -1L);
      this.attrs.put("idPerfil", JsfBase.getFlashAttribute("idPerfil") != null ? (Long)(JsfBase.getFlashAttribute("idPerfil")) : -1L);
      this.attrs.put("descPerfil", ((TcJanalPerfilesDto)DaoFactory.getInstance().findById(TcJanalPerfilesDto.class, Numero.getLong(this.attrs.get("idPerfil").toString()))).getDescripcion());
      doLoad();
    } // try
    catch(Exception e) {
      Error.mensaje(e);
    } // catch
	} // init

	public String doGuardar(){
      List<Entity>  originales  = null;
      List<Entity> seleccionados= null;
      List<Entity> nuevos       = null;
      Transaccion transaccion       = null;
    try {
      originales = getOriginales();
      seleccionados= getSeleccionados();
      nuevos = getSeleccionados();
      nuevos.removeAll(originales);
      originales.removeAll(seleccionados);
      transaccion = new Transaccion(nuevos, originales, Long.valueOf(this.attrs.get("idPerfil").toString()));
      transaccion.ejecutar(EAccion.AGREGAR);
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

  public String doRegresar(){
    JsfBase.setFlashAttribute("idGrupo", Long.valueOf(this.attrs.get("idGrupo").toString()));
    return "/Paginas/Mantenimiento/GruposPerfiles/Perfiles/filtro".concat(Constantes.REDIRECIONAR);
  }//doRegresar

  public void onNodeUnselect(NodeUnselectEvent event) {
    seleccionarPadres(event.getTreeNode());
    event.getTreeNode().setSelected(false);
  }  //onNodeSelect

  public void onNodeSelect(NodeSelectEvent event) {
      seleccionarPadres(event.getTreeNode());
   }//onNodeUnselect

  private List<Entity> getSeleccionados(){
    List<Entity> regresar = new ArrayList();
    for(int i = 0; i< this.selectedNodes.length;i++){
      Entity entity = (Entity)this.selectedNodes[i].getData();
      regresar.add(entity);
    } // for
    return regresar;
  }//getSeleccionados

  private List<Entity> getOriginales(){
    List<Entity> regresar = new ArrayList();
    try {
      regresar = DaoFactory.getInstance().toEntitySet("VistaPerfilesMenusEncabezadoDto", "seleccionados",this.attrs, Constantes.SQL_TODOS_REGISTROS);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
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
      this.attrs.put("treeMenu", new TreeMenu(((DefaultTreeNode)this.attrs.get("root")), "VistaPerfilesMenusEncabezadoDto", this.attrs, true, true));
    } // try
    catch (Exception e) {
      JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
  }//doLoad

}
