package mx.org.kaana.mantic.compras.ordenes.backing;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.archivo.Archivo;
import mx.org.kaana.libs.archivo.Zip;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.pagina.IBaseFilter;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.compras.ordenes.beans.TreeOrden;
import mx.org.kaana.mantic.compras.ordenes.reglas.MotorBusqueda;
import org.primefaces.context.RequestContext;
import org.primefaces.event.NodeExpandEvent;
import org.primefaces.event.organigram.OrganigramNodeCollapseEvent;
import org.primefaces.model.DefaultOrganigramNode;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.OrganigramNode;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.TreeNode;

@Named(value = "manticComprasOrdenesEstructura")
@ViewScoped
public class Estructura extends IBaseFilter implements Serializable {

  private static final long serialVersionUID = 8793667741599428332L;	
	private TreeNode tree;
  private TreeNode node;
	private List<String> files;

	public TreeNode getTree() {
		return tree;
	}

	public void setTree(TreeNode tree) {
		this.tree = tree;
	}

	public TreeNode getNode() {
		return node;
	}

	public void setNode(TreeNode node) {
		this.node = node;
	}	

	public List<String> getFiles() {
		return files;
	}
	
  @PostConstruct
  @Override
  protected void init() {
    try {
			if(JsfBase.getFlashAttribute("idOrdenCompra")== null)
				UIBackingUtilities.execute("janal.isPostBack('cancelar')");
			this.files= new ArrayList<>();
      this.attrs.put("isMatriz", JsfBase.getAutentifica().getEmpresa().isMatriz());
			this.attrs.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
      this.attrs.put("idOrdenCompra", JsfBase.getFlashAttribute("idOrdenCompra"));			
			this.doLoad();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init
 
  @Override
  public void doLoad() {
    List<Columna> columns     = null;
		Map<String, Object> params= null;
		MotorBusqueda busqueda    = null;
    try {      
			busqueda = new MotorBusqueda(Long.valueOf(this.attrs.get("idOrdenCompra").toString()));
			this.tree= new DefaultTreeNode("orden", busqueda.toParent(), null);
			this.tree.getChildren().add(new DefaultTreeNode());      
			createTree(this.tree);		
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    finally {
      Methods.clean(params);
      Methods.clean(columns);
    } // finally		
  } // doLoad

	private void createTree(TreeNode seleccionado){
		MotorBusqueda gestor    = null;
    List<TreeNode> childrens= null;        
		try {
      gestor= new MotorBusqueda((TreeOrden) seleccionado.getData());
      childrens= gestor.toChildrens();
      seleccionado.getChildren().clear();
      if(!childrens.isEmpty()){        
        for(TreeNode childNode: childrens){
          if(!((TreeOrden)childNode.getData()).isUltimoNivel()) 
            childNode.getChildren().add(new DefaultTreeNode(new TreeOrden(), childNode));
          childNode.setParent(seleccionado);     
					seleccionado.setExpanded(true);
          seleccionado.getChildren().add(childNode);
					createTree(childNode);
        } // for
				if(gestor.getFiles().size()> 0)
  			  this.files.addAll(gestor.getFiles());
      } // if                  
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);		
		} // catch		
	} // createTree
	
	public void onNodeExpand(NodeExpandEvent event){     
    MotorBusqueda gestor    = null;
    List<TreeNode> childrens= null;        
    TreeNode seleccionado   = null;     
    try {      
      seleccionado= event.getTreeNode();
      gestor= new MotorBusqueda((TreeOrden) seleccionado.getData());
      childrens= gestor.toChildrens();
      seleccionado.getChildren().clear();
      if(!childrens.isEmpty()){        
        for(TreeNode childNode: childrens){
          if(!((TreeOrden)childNode.getData()).isUltimoNivel()) 
            childNode.getChildren().add(new DefaultTreeNode(new TreeOrden(), childNode));
          childNode.setParent(seleccionado);      
          seleccionado.getChildren().add(childNode);
        } // for
      } // if                  
    } // try
    catch (Exception e) {
      JsfBase.addMessageError(e);
      Error.mensaje(e);      
    } // catch    
  } // doExpand  
	
	public void onCollapseNode(OrganigramNodeCollapseEvent event){
    OrganigramNode seleccionado= null;    
    try {
      seleccionado= event.getOrganigramNode();
      seleccionado.getChildren().clear();
      seleccionado.getChildren().add(new DefaultOrganigramNode());
      seleccionado.setExpanded(false);      
    } // try
    catch (Exception e) {
      JsfBase.addMessageError(e);
      Error.mensaje(e);      
    } // catch    
  } // onCollapseNode
	
	public String doCancelar(){
		return "filtro".concat(Constantes.REDIRECIONAR);
	} // doCancelar  
	
	public void doConsultarDetalle() {
    TreeOrden seleccionado= null;
    RequestContext rc     = null;
    try {
      rc= UIBackingUtilities.getCurrentInstance();
      if(this.node!= null) {
        seleccionado= (TreeOrden) this.node.getData();
        this.attrs.put("isPrincipal", seleccionado.getProveedor()!= null);
        this.attrs.put("detalle", seleccionado);        
        rc.update("dialogoDetalle");
        rc.execute("PF('dlgDetalle').show();");
      } // if
      else{
        rc.execute("janal.desbloquear();");        
        JsfBase.addMessage("Detalle", "Es necesario seleccionar un documento, para mostrar la informacion.", ETipoMensaje.ERROR);
      } // else
    } // try
    catch (Exception e) {
      JsfBase.addMessageError(e);
      Error.mensaje(e);      
    } // catch    
    finally{
      this.node= null;
    } // finally      
  } // doConsultarDetalle
	
	public String doAccion() {
		String regresar       = null;
		TreeOrden seleccionado= null;
    RequestContext rc     = null;
    try {
      rc= UIBackingUtilities.getCurrentInstance();
      if(this.node!= null){
				seleccionado= (TreeOrden) this.node.getData();       
				switch(seleccionado.getTipo()) {
					case DEVOLUCION:
    				JsfBase.setFlashAttribute("idNotaEntrada", seleccionado.getIdNotaEntrada());
						break;
				} // switch
				JsfBase.setFlashAttribute("retorno", "/Paginas/Mantic/Compras/Ordenes/estructura.jsf".concat(Constantes.REDIRECIONAR));
				JsfBase.setFlashAttribute(seleccionado.getTipo().getIdKey(), seleccionado.getId());
				JsfBase.setFlashAttribute("accion", EAccion.CONSULTAR);
				regresar= seleccionado.getTipo().getRuta();
      } // if
      else{
        rc.execute("janal.desbloquear();");        
        JsfBase.addMessage("Consultar", "Es necesario seleccionar un nodo", ETipoMensaje.ERROR);
      } // else
    } // try
    catch (Exception e) {
      JsfBase.addMessageError(e);
      Error.mensaje(e);      
    } // catch    
    finally {
      this.node= null;
    } // finally      
		return regresar;
  } // doConsultarDetalle  		
	
  public StreamedContent doExportar() {
		StreamedContent regresar= null;
		try {
			regresar= this.toZipFile(this.files.toArray(new String[0]));
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch
		return regresar;
	}

	private StreamedContent toZipFile(String[] files) throws Exception {
		String zipName    = null;
		String temporal   = Archivo.toFormatNameFile("DOCUMENTOS.").concat(EFormatos.ZIP.name().toLowerCase());
		InputStream stream= null;
		try {
			Zip zip= new Zip();
			zipName= "/".concat(Constantes.RUTA_TEMPORALES).concat(Cadena.letraCapital(EFormatos.ZIP.name()).concat("/").concat(temporal));
			zip.setDebug(true);
			zip.setEliminar(false);
			zip.especial(JsfBase.getRealPath(zipName), files);
  	  stream = new FileInputStream(new File(JsfBase.getRealPath(zipName)));
		} // try
		catch (Exception e) {
			throw e;
		} // catch
    return new DefaultStreamedContent(stream, EFormatos.ZIP.getContent(), temporal);		
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		Methods.clean(this.files);
	}

}
