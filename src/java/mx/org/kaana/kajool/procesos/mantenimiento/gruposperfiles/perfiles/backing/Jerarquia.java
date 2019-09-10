package mx.org.kaana.kajool.procesos.mantenimiento.gruposperfiles.perfiles.backing;

/**
 *@company KAANA
 *@project SECO (Sistema de seguimiento y control de proyetos estadisticos)
 *@date Jan 15, 2013
 *@time 2:19:30 PM 
 *@author Juan Pablo Medina Adame <juan.adame@inegi.org.mx>
 */

import java.io.Serializable;
import java.util.*;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.beans.SelectionItem;
import mx.org.kaana.kajool.db.dto.TrJanalPerfilesJerarquiasDto;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.procesos.mantenimiento.gruposperfiles.perfiles.reglas.Transaccion;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;

@ManagedBean(name="kajoolMantenimientoGruposperfilesPerfilesJerarquia")
@ViewScoped
public class Jerarquia implements Serializable {
  
  private static final long serialVersionUID= -7555759164396254107L;
  private DualListModel<SelectionItem> selectionItems;
	private Long idGrupo;
	private Long idPerfil;
	private String descripcion;

	public DualListModel<SelectionItem> getSelectionItems() {
		return selectionItems;
	}

	public void setSelectionItems(DualListModel<SelectionItem> selectionItems) {
		this.selectionItems=selectionItems;
	}

	public String getDescripcion() {
		return descripcion;
	}
	
  @PostConstruct
	private void init() {
    try {
     this.idGrupo= (Long)JsfBase.getFlashAttribute("idGrupo");
     this.idPerfil= (Long)JsfBase.getFlashAttribute("idPerfil");
     this.descripcion=(String)JsfBase.getFlashAttribute("descripcion");
		 this.selectionItems= new DualListModel<>(loadPerfiles(true), loadPerfiles(false));      
    } // try
    catch(Exception e) {
			JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
	} // init
	
	private List<SelectionItem> loadPerfiles(boolean disponibles) throws Exception {
		List<SelectionItem> regresar= null;
		Map<String, Object> params  = null;
		List<Entity>consulta        = null;
		try {
			regresar= new ArrayList<>();
			params  = new HashMap<>();
			if(disponibles)
				params.put("idGrupo", this.idGrupo);
			params.put("idPerfil", this.idPerfil);
			consulta= DaoFactory.getInstance(). toEntitySet("VistaMantenimientoPerfilesDto" , disponibles ? "jerarquiaMostrarDisponibles" : "jerarquiaMostrarAsignados", params, Constantes.SQL_TODOS_REGISTROS);
			if (consulta != null){
				for(Entity item : consulta) 
				  regresar.add(new SelectionItem(item.getKey().toString(), item.toString("descripcion")));				
			} // if
		} // try
		catch (Exception e) {
			throw e;
		} // catch
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	} // cargarDisponibles

	public void doTransferir(TransferEvent event) {		
		int total               = 0;					
		StringBuilder sb        = null;
		StringBuilder sbDelete  = null;
		List<SelectionItem> temp= null;
		try {			
			sb= new StringBuilder();				
			sbDelete= new StringBuilder();				
			temp= new ArrayList<>();		
			temp.addAll((List<SelectionItem>)event.getItems());
			for(SelectionItem seleccionado: temp){
				if(((List<SelectionItem>)event.getItems()).indexOf(seleccionado)!=-1){
					if(event.isAdd()){					
							total++;
							sb.append(seleccionado.getKey());
							sb.append(",");												
					}	// if	
					else{
						sbDelete.append(seleccionado.getKey());
						sbDelete.append(",");
					} // else
				} // if
				else{
					total++;
					sb.append(seleccionado.getKey());
					sb.append(",");		
				} // else
			} // for						
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch				
	} // doTransferir
	
	public String doAceptar() {
		List<TrJanalPerfilesJerarquiasDto> jerarquias= null;
		Transaccion transaccion= null;
		long orden= 0;		
		try {
			jerarquias= new ArrayList<>();
			for(SelectionItem item: this.selectionItems.getTarget()) 
				jerarquias.add(new TrJanalPerfilesJerarquiasDto(this.idPerfil, ++orden, Long.valueOf(item.getKey()), -1L));			
			transaccion = new Transaccion(jerarquias, this.idPerfil);
			if(transaccion.ejecutar(EAccion.GENERAR))
				JsfBase.setFlashAttribute("idGrupo", this.idGrupo);
		} // try
		catch(Exception e) {
			JsfBase.addMessageError(e);
		} // catch		
		return "filtro".concat(Constantes.REDIRECIONAR);
	} // doAceptar
	
	public String doCancelar() {
		JsfBase.setFlashAttribute("idGrupo", this.idGrupo);
		return "filtro".concat(Constantes.REDIRECIONAR);
	} // doCancelar	
}