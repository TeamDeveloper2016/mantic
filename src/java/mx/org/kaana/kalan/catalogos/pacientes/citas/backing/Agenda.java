package mx.org.kaana.kalan.catalogos.pacientes.citas.backing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kalan.catalogos.pacientes.citas.reglas.EventosLazyModel;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.pagina.IBaseFilter;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.ScheduleModel;

@Named(value = "kalanCatalogosPacientesCitasAgenda")
@ViewScoped
public class Agenda extends IBaseFilter implements Serializable {

  private static final long serialVersionUID = 8793667741599428879L;
  
  private ScheduleModel lazyEventModel;
  private Boolean criterio;

  public ScheduleModel getLazyEventModel() {
    return lazyEventModel;
  }

  @PostConstruct
  @Override
  protected void init() {
    try {
      this.criterio= Boolean.TRUE;
      this.attrs.put("idClienteProcess", JsfBase.getFlashAttribute("idClienteProcess"));
      this.doLoad();
      this.toLoadServicios();
      this.attrs.put("idClienteProcess", null);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

  @Override
  public void doLoad() {
    List<Columna> columns    = new ArrayList<>();		
		Map<String, Object>params= this.toPrepare();
    try {
      columns.add(new Columna("cliente", EFormatoDinamicos.MAYUSCULAS));      
      columns.add(new Columna("servicios", EFormatoDinamicos.MAYUSCULAS));    
      columns.add(new Columna("estatus", EFormatoDinamicos.MAYUSCULAS));    
  		params.put("idCliente", -1L);
  		params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
      this.lazyEventModel = new EventosLazyModel("VistaClientesCitasDto", "agenda", params, columns);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    } // finally		
  } // doLoad

	private Map<String, Object> toPrepare() {
		Map<String, Object> regresar= new HashMap();
		StringBuilder sb            = null;
		UISelectEntity cliente      = null;
		List<UISelectEntity>clientes= null;
		try {
			sb      = new StringBuilder("");
      if(this.criterio) { 
        // BUSCAR POR CLIENTE
        cliente = (UISelectEntity)this.attrs.get("cliente");
        clientes= (List<UISelectEntity>)this.attrs.get("clientes");
        if(clientes!= null && cliente!= null && clientes.indexOf(cliente)>= 0) 
          sb.append("concat(tc_mantic_clientes.razon_social, ' ', ifnull(tc_mantic_clientes.paterno, ''), ' ', ifnull(tc_mantic_clientes.materno, '')) regexp '.*").append(clientes.get(clientes.indexOf(cliente)).toString("razonSocial").replaceAll(Constantes.CLEAN_SQL, "").trim().replaceAll("(,| |\\t)+", ".*.*")).append(".*' and ");				
        else 
          if(!Cadena.isVacio(JsfBase.getParametro("razonSocial_input"))) 
            sb.append("concat(tc_mantic_clientes.razon_social, ' ', ifnull(tc_mantic_clientes.paterno, ''), ' ', ifnull(tc_mantic_clientes.materno, '')) regexp '.*").append(JsfBase.getParametro("razonSocial_input").replaceAll(Constantes.CLEAN_SQL, "").trim().replaceAll("(,| |\\t)+", ".*.*")).append(".*' and ");				
      } // if
      else {
        // BUSCAR POR SERVICIO
        if(!Cadena.isVacio(this.attrs.get("idServicio")) && !Objects.equals(this.attrs.get("idServicio"), "-1"))
          sb.append("(tc_kalan_citas_detalles.id_articulo= ").append(this.attrs.get("idServicio")).append(") and ");
      } // else
			if(Cadena.isVacio(sb))
				sb.append(Constantes.SQL_VERDADERO);
			else
				sb.delete(sb.length()- 4, sb.length());
			regresar.put(Constantes.SQL_CONDICION, sb.toString());
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toPrepare
	
  public String doAccion(String accion) {
		return "accion".concat(Constantes.REDIRECIONAR);
  } // doAccion

  public void doEliminar() {
		try {
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch			
  } // doEliminar
	
	public List<UISelectEntity> doCompleteCliente(String codigo) {
 		List<Columna> columns     = new ArrayList<>();
    Map<String, Object> params= new HashMap<>();
    try {
      columns.add(new Columna("cliente", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("inicio", EFormatoDinamicos.DIA_FECHA_HORA_CORTA));
      columns.add(new Columna("servicios", EFormatoDinamicos.MAYUSCULAS));
  		params.put("idCliente", -1L);
  		params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
			if(!Cadena.isVacio(codigo)) {
  			codigo= codigo.replaceAll(Constantes.CLEAN_SQL, "").trim();
				codigo= codigo.toUpperCase().replaceAll("(,| |\\t)+", ".*.*");
			} // if	
			else
				codigo= "WXYZ";
  		params.put(Constantes.SQL_CONDICION, "(upper(concat(razon_social, ' ', ifnull(paterno, ''), ' ', ifnull(materno, ''))) regexp '.*".concat(codigo).concat(".*' or upper(rfc) regexp '.*").concat(codigo).concat(".*')"));
      this.attrs.put("clientes", UIEntity.build("VistaClientesCitasDto", "clientes", params, columns, 40L));
		} // try
	  catch (Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    }// finally
		return (List<UISelectEntity>)this.attrs.get("clientes");
	}	
  
  public void doEventSelect(SelectEvent selectEvent) {
    // event = (ScheduleEvent) selectEvent.getObject();
  }

  public void doDateSelect(SelectEvent selectEvent) {
    // event = new DefaultScheduleEvent("", (Date) selectEvent.getObject(), (Date) selectEvent.getObject());
  }

  public void doEventMove(ScheduleEntryMoveEvent event) {
    // FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event moved", "Day delta:" + event.getDayDelta() + ", Minute delta:" + event.getMinuteDelta());
  }

  public void doEventResize(ScheduleEntryResizeEvent event) {
    // FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event resized", "Day delta:" + event.getDayDelta() + ", Minute delta:" + event.getMinuteDelta());
  }
  
  private void toLoadServicios() {
		List<Columna> columns     = new ArrayList<>();    
    Map<String, Object> params= new HashMap<>();
    List<UISelectEntity> servicios= null;
    try {      
			params.put("idArticuloTipo", 4L);			
      columns.add(new Columna("codigo", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      servicios = (List<UISelectEntity>)UIEntity.seleccione("VistaClientesCitasDto", "servicios", params, columns, "codigo");
      if(servicios!= null && !servicios.isEmpty())
        this.attrs.put("idServicio", servicios.get(0));
      else
        this.attrs.put("idServicio", new UISelectEntity(-1L));
      this.attrs.put("servicios", servicios);
    } // try
    catch (Exception e) {
			throw e;
    } // catch	
    finally {
      Methods.clean(params);
      Methods.clean(columns);
    } // finally    
  }

	public void doTabChange(TabChangeEvent event) {
		this.criterio= Objects.equals(event.getTab().getTitle(), "Cliente");
  }  
  
}
