package mx.org.kaana.mantic.catalogos.almacenes.ubicaciones.backing;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.pagina.IBaseFilter;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.almacenes.ubicaciones.beans.OrganigramUbicacion;
import mx.org.kaana.mantic.catalogos.almacenes.ubicaciones.reglas.MotorBusqueda;
import mx.org.kaana.mantic.catalogos.almacenes.ubicaciones.reglas.Transaccion;
import mx.org.kaana.mantic.enums.ENivelUbicacion;
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
			this.attrs.put("empresaOrganigram", ((boolean)this.attrs.get("isMatriz")) ? JsfBase.getAutentifica().getEmpresa().getDependencias() : this.attrs.get("idEmpresa").toString());
			this.attrs.put("descripcion", "");
			this.doLoad();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init
 
  @Override
  public void doLoad() {
    List<Columna> columns       = null;
		Map<String, Object> params  = null;		
		OrganigramUbicacion initNode= null;
    try {      
			initNode= new OrganigramUbicacion(-1L);
			initNode.setIdEmpresa(this.attrs.get("empresaOrganigram").toString());
			this.organigram= new DefaultOrganigramNode("principal", initNode, null);						
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
      gestor= new MotorBusqueda((OrganigramUbicacion)seleccionado.getData());
      childrens= gestor.toChildrens();
      seleccionado.getChildren().clear();
      if(!childrens.isEmpty()){        
        for(OrganigramNode childNode: childrens){
          if(!((OrganigramUbicacion)childNode.getData()).isUltimoNivel()) 
            childNode.getChildren().add(new DefaultOrganigramNode());
          childNode.setParent(seleccionado);               
					childNode.setExpanded(true);  
					childNode.setSelectable(true);  
          childNode.setCollapsible(false);     
          childNode.setDraggable(false);     
          childNode.setDroppable(false);     
					seleccionado.setExpanded(true);          
					seleccionado.setSelectable(true);          
					seleccionado.setCollapsible(false);          
					createTree(childNode);
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
      gestor= new MotorBusqueda((OrganigramUbicacion) seleccionado.getData());
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
	
	public void doAgregar() {
    OrganigramUbicacion seleccionado= null;
    RequestContext rc               = null;
    try {
      rc= UIBackingUtilities.getCurrentInstance();
      if(this.node!= null) {
        seleccionado= (OrganigramUbicacion) this.node.getData(); 				
        this.attrs.put("tipo", ENivelUbicacion.fromIdNivel(seleccionado.getNivel().getIdNivelUbicacion()).name().toLowerCase());        
        rc.update("dialogoDetalle");
        rc.execute("PF('dlgDetalle').show();");
      } // if
      else{
        rc.execute("janal.desbloquear();");        
        JsfBase.addMessage("Detalle", "Es necesario seleccionar una ubicaci�n para mostrar la informacion.", ETipoMensaje.ERROR);
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
	
	public void doAceptar(){
		Transaccion transaccion= null;
		try {
			transaccion= new Transaccion((OrganigramUbicacion)this.node, this.attrs.get("descripcion").toString());
			if(transaccion.ejecutar(EAccion.AGREGAR)){
				JsfBase.addMessage("Se agreg� de forma correcta el nivel", ETipoMensaje.INFORMACION);
				doLoad();
			} // if
			else
				JsfBase.addMessage("Ocurri� un error al agregar el nivel", ETipoMensaje.ERROR);
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
	} // doAceptar
}