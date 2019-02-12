package mx.org.kaana.mantic.catalogos.almacenes.ubicaciones.backing;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.pagina.IBaseFilter;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.almacenes.ubicaciones.beans.OrganigramUbicacion;
import mx.org.kaana.mantic.catalogos.almacenes.ubicaciones.reglas.MotorBusqueda;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.organigram.OrganigramNodeCollapseEvent;
import org.primefaces.model.DefaultOrganigramNode;
import org.primefaces.model.OrganigramNode;

@Named(value = "manticCatalogosAlmacenesOrganigrama")
@ViewScoped
public class Organigrama extends IBaseFilter implements Serializable {

  private static final long serialVersionUID = 8793667741599428332L;	
	private OrganigramNode organigram;
  private OrganigramNode node;	

	public OrganigramNode getOrganigram() {
		return organigram;
	}

	public void setOrganigram(OrganigramNode organigram) {
		this.organigram = organigram;
	}

	public OrganigramNode getNode() {
		return node;
	}

	public void setNode(OrganigramNode node) {
		this.node = node;
	}		
	
  @PostConstruct
  @Override
  protected void init() {
    try {			       						
      this.attrs.put("isMatriz", JsfBase.getAutentifica().getEmpresa().isMatriz());
			this.attrs.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
			this.attrs.put("sortOrder", "order by	tc_mantic_empresas_deudas.registro desc");
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
			busqueda = new MotorBusqueda(((boolean)this.attrs.get("isMatriz")) ? JsfBase.getAutentifica().getEmpresa().getDependencias() : this.attrs.get("idEmpresa").toString());
			this.organigram= new DefaultOrganigramNode("principal", busqueda.toParent(), null);
			this.organigram.getChildren().add(new DefaultOrganigramNode());      
			createTree(this.organigram);		
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

	private void createTree(OrganigramNode seleccionado){
		MotorBusqueda gestor    = null;
    List<OrganigramNode> childrens= null;        
		try {
      gestor= new MotorBusqueda(this.attrs.get("idEmpresaDeuda").toString());
      childrens= gestor.toChildrens();
      seleccionado.getChildren().clear();
      if(!childrens.isEmpty()){        
        for(OrganigramNode childNode: childrens){
          if(!((OrganigramUbicacion)childNode.getData()).isUltimoNivel()) 
            childNode.getChildren().add(new DefaultOrganigramNode());
          childNode.setParent(seleccionado);     
					seleccionado.setExpanded(true);
          seleccionado.getChildren().add(childNode);					
        } // for				
      } // if                  
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);		
		} // catch		
	} // createTree
	
	public void onNodeExpand(SelectEvent event){     
    MotorBusqueda gestor          = null;
    List<OrganigramNode> childrens= null;        
    OrganigramNode seleccionado   = null;     
    try {      
      seleccionado= (OrganigramNode) event.getObject();
      gestor= new MotorBusqueda(((OrganigramUbicacion) seleccionado.getData()).getKey().toString());
      childrens= gestor.toChildrens();
      seleccionado.getChildren().clear();
      if(!childrens.isEmpty()){        
        for(OrganigramNode childNode: childrens){
          if(!((OrganigramUbicacion)childNode.getData()).isUltimoNivel()) 
            childNode.getChildren().add(new DefaultOrganigramNode());
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
		return ((String)this.attrs.get("retorno")).concat(Constantes.REDIRECIONAR);
	} // doCancelar  
	
	public void doConsultarDetalle() {
    OrganigramUbicacion seleccionado= null;
    RequestContext rc     = null;
    try {
      rc= RequestContext.getCurrentInstance();
      if(this.node!= null) {
        seleccionado= (OrganigramUbicacion) this.node.getData();        
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
}