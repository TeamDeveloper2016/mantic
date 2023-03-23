package mx.org.kaana.kalan.catalogos.pacientes.citas.backing;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.pagina.IBaseFilter;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.LazyScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

@Named(value = "kalanCatalogosPacientesCitasAgenda")
@ViewScoped
public class Agenda extends IBaseFilter implements Serializable {

  private static final long serialVersionUID = 8793667741599428879L;
  
  private ScheduleModel lazyEventModel;
  private ScheduleEvent event;  

  public ScheduleModel getLazyEventModel() {
    return lazyEventModel;
  }

  @PostConstruct
  @Override
  protected void init() {
    try {
      this.event= new DefaultScheduleEvent();
      if(JsfBase.getFlashAttribute("idClienteProcess")!= null) {
        this.attrs.put("idClienteProcess", JsfBase.getFlashAttribute("idClienteProcess"));
        this.doLoad();
        this.attrs.put("idClienteProcess", null);
      } // if
      
      this.lazyEventModel = new LazyScheduleModel() {
        
        private static final long serialVersionUID = 4573591941737903141L;
        
        public void loadEvents(Date start, Date end) {
          Date random = getRandomDate(start);
          addEvent(new DefaultScheduleEvent("Lazy Event 1", random, random));

          random = getRandomDate(start);
          addEvent(new DefaultScheduleEvent("Lazy Event 2", random, random));
        }  
        
      };      
      
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
      columns.add(new Columna("inicio", EFormatoDinamicos.FECHA_HORA));    
      columns.add(new Columna("motivo", EFormatoDinamicos.MAYUSCULAS));    
      columns.add(new Columna("estatus", EFormatoDinamicos.MAYUSCULAS));    
      params.put("sortOrder", "order by tc_mantic_clientes.razon_social, tc_mantic_clientes.paterno, tc_mantic_clientes.materno");
			params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getDependencias());			
      
      
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
			cliente = (UISelectEntity)this.attrs.get("cliente");
			clientes= (List<UISelectEntity>)this.attrs.get("clientes");
  		if(!Cadena.isVacio(this.attrs.get("fecha")))
	  	  sb.append("(date_format(tc_kalan_citas.cuando, '%Y%m%d')= '").append(Fecha.formatear(Fecha.FECHA_ESTANDAR, (Date)this.attrs.get("fecha"))).append("') and ");	
			if(clientes!= null && cliente!= null && clientes.indexOf(cliente)>= 0) 
				sb.append("concat(tc_mantic_clientes.razon_social, ' ', ifnull(tc_mantic_clientes.paterno, ''), ' ', ifnull(tc_mantic_clientes.materno, '')) regexp '.*").append(clientes.get(clientes.indexOf(cliente)).toString("razonSocial").replaceAll(Constantes.CLEAN_SQL, "").trim().replaceAll("(,| |\\t)+", ".*.*")).append(".*' and ");				
			else 
				if(!Cadena.isVacio(JsfBase.getParametro("razonSocial_input"))) 
					sb.append("concat(tc_mantic_clientes.razon_social, ' ', ifnull(tc_mantic_clientes.paterno, ''), ' ', ifnull(tc_mantic_clientes.materno, '')) regexp '.*").append(JsfBase.getParametro("razonSocial_input").replaceAll(Constantes.CLEAN_SQL, "").trim().replaceAll("(,| |\\t)+", ".*.*")).append(".*' and ");				
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

  public Date getRandomDate(Date base) {
    Calendar date = Calendar.getInstance();
    date.setTime(base);
    date.add(Calendar.DATE, ((int) (Math.random() * 30)) + 1);
    return new Date(date.getTimeInMillis());
  }

  public void addEvent(ActionEvent actionEvent) {
    if (event.getId() == null) 
      lazyEventModel.addEvent(event);
    else 
      lazyEventModel.updateEvent(event);
    event = new DefaultScheduleEvent();
  }
 
}
