package mx.org.kaana.kajool.procesos.plantillas.formularios.backing;

/**
 *@company Instituto Nacional de Estadistica y Geografia
 *@project KAJOOL (Control system polls)
 *@date 11-mar-2014
 *@time 15:37:37
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.primefaces.model.CheckboxTreeNode;
import org.primefaces.model.TreeNode;

@ManagedBean(name="kajoolPlantillasFormulariosFiltro")
@ViewScoped
public class Filtro extends IBaseAttribute implements Serializable {

  private static final Log LOG = LogFactory.getLog(Filtro.class);
  private static final long serialVersionUID = 1003282869965361701L;

  private List<UISelectEntity> registros;
  private TreeNode root;
  private TreeNode[] selecteds;

  public List<UISelectEntity> getRegistros() {
    return registros;
  }

  public TreeNode getRoot() {
    return root;
  }

  public TreeNode[] getSelecteds() {
    return selecteds;
  }

  public void setSelecteds(TreeNode[] selecteds) {
    this.selecteds = selecteds;
  }

  @PostConstruct
  @Override
	protected void init() {
    try {
      this.root= new CheckboxTreeNode("Root", null);
      TreeNode node0 = new CheckboxTreeNode("Node 0", this.root);
      TreeNode node1 = new CheckboxTreeNode("Node 1", this.root);
      TreeNode node2 = new CheckboxTreeNode("Node 2", this.root);

      TreeNode node00 = new CheckboxTreeNode("Node 0.0", node0);
      TreeNode node01 = new CheckboxTreeNode("Node 0.1", node0);

      TreeNode node10 = new CheckboxTreeNode("Node 1.0", node1);
      TreeNode node11 = new CheckboxTreeNode("Node 1.1", node1);

      TreeNode node000 = new CheckboxTreeNode("Node 0.0.0", node00);
      TreeNode node001 = new CheckboxTreeNode("Node 0.0.1", node00);
      TreeNode node010 = new CheckboxTreeNode("Node 0.1.0", node01);

      TreeNode node100 = new CheckboxTreeNode("Node 1.0.0", node10);
      doLoad();
    } // try
    catch(Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch
	} // init

  public void doLoad() {
    try {
      this.registros= UIEntity.build("TcJanalTiposDatosDto", "row");
      this.attrs.put("idTipoDato", UIBackingUtilities.toFirstKeySelectEntity(this.registros));
    } // try
    catch(Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch
  }

  public void doAceptar() {
    LOG.debug(this.attrs.get("idTipoDato"));
    this.attrs.put("idTipoDato", this.registros.get(this.registros.indexOf((UISelectEntity)this.attrs.get("idTipoDato"))));
    JsfBase.addMessage("Seleccionado: "+ ((UISelectEntity)this.attrs.get("idTipoDato")).toMap());
  }

  public void doDisplaySelectedMultiple() {
    if (this.selecteds != null && this.selecteds.length > 0) {
      StringBuilder builder = new StringBuilder();
      for (TreeNode node : this.selecteds) {
        builder.append(node.getData().toString());
        builder.append("<br />");
      } // for
      FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Selected", builder.toString());
      FacesContext.getCurrentInstance().addMessage(null, message);
    } // if
  }

}
