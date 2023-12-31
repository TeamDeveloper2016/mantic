package mx.org.kaana.kalan.catalogos.pacientes.citas.backing;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kalan.catalogos.pacientes.citas.reglas.EventosLazyModel;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.pagina.IBaseFilter;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

@Named(value = "kalanCatalogosPacientesCitasAgenda")
@ViewScoped
public class Agenda extends IBaseFilter implements Serializable {

  private static final long serialVersionUID = 8793667741599428879L;
  
  private ScheduleModel lazyEventModel;
  private Integer idCriterio;

  public ScheduleModel getLazyEventModel() {
    return lazyEventModel;
  }

  @PostConstruct
  @Override
  protected void init() {
    try {
      this.idCriterio= 0;
      this.attrs.put("idClienteProcess", JsfBase.getFlashAttribute("idClienteProcess"));
      this.doLoad();
      this.toLoadPersonal();
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
      switch(this.idCriterio) {
        case 0: // BUSCAR POR CLIENTE
          cliente = (UISelectEntity)this.attrs.get("cliente");
          clientes= (List<UISelectEntity>)this.attrs.get("clientes");
          if(clientes!= null && cliente!= null && clientes.indexOf(cliente)>= 0) 
            sb.append("concat(tc_mantic_clientes.razon_social, ' ', ifnull(tc_mantic_clientes.paterno, ''), ' ', ifnull(tc_mantic_clientes.materno, '')) regexp '.*").append(clientes.get(clientes.indexOf(cliente)).toString("razonSocial").replaceAll(Constantes.CLEAN_SQL, "").trim().replaceAll("(,| |\\t)+", ".*")).append(".*' and ");				
          else 
            if(!Cadena.isVacio(JsfBase.getParametro("razonSocial_input"))) 
              sb.append("concat(tc_mantic_clientes.razon_social, ' ', ifnull(tc_mantic_clientes.paterno, ''), ' ', ifnull(tc_mantic_clientes.materno, '')) regexp '.*").append(JsfBase.getParametro("razonSocial_input").replaceAll(Constantes.CLEAN_SQL, "").trim().replaceAll("(,| |\\t)+", ".*")).append(".*' and ");				
          break;
        case 1: // BUSCAR POR SERVICIO
          if(!Cadena.isVacio(this.attrs.get("idServicio").toString()) && !Objects.equals(this.attrs.get("idServicio").toString(), "-1"))
            sb.append("(tc_kalan_citas_detalles.id_articulo= ").append(this.attrs.get("idServicio").toString()).append(") and ");
          break;
        case 2: // BUSCAR POR DOCTOR
          if(!Cadena.isVacio(this.attrs.get("idPersona").toString())) {
            if(Objects.equals(this.attrs.get("idPersona").toString(), "0"))
              sb.append("(tc_kalan_citas.id_atendio is null) and ");
            else 
              if(!Objects.equals(this.attrs.get("idPersona").toString(), "-1"))
                sb.append("(tc_kalan_citas.id_atendio= ").append(this.attrs.get("idPersona").toString()).append(") and ");
          } // if  
          break;
      } // switch
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
				codigo= codigo.toUpperCase().replaceAll("(,| |\\t)+", ".*");
			} // if	
			else
				codigo= "WXYZ";
  		params.put(Constantes.SQL_CONDICION, "(upper(concat(tc_mantic_clientes.razon_social, ' ', ifnull(tc_mantic_clientes.paterno, ''), ' ', ifnull(tc_mantic_clientes.materno, ''))) regexp '.*".concat(codigo).concat(".*' or upper(tc_mantic_clientes.rfc) regexp '.*").concat(codigo).concat(".*')"));
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
  
  public String doPageAgregar() {
    String regresar= null;
    try {
      SelectEvent event= (SelectEvent)this.attrs.get("seleccionado");
      if(event!= null) {
        Date fecha  = (Date)event.getObject();
        Calendar dia= Calendar.getInstance();
        dia.add(Calendar.DATE, -1);
        Date actual = new Date(dia.getTimeInMillis());
        dia.setTimeInMillis(fecha.getTime());
        dia.add(Calendar.DATE, 1);
        fecha= new Date(dia.getTimeInMillis());
        if(fecha.before(actual)) 
          JsfBase.addMessage("Fecha", "No se puede agendar sobre fechas pasadas, ".concat("fecha seleccionada [").concat(
            Fecha.formatear(Fecha.FECHA_NOMBRE_DIA, actual)
          ).concat("]"), ETipoMensaje.ALERTA);
        else {
          JsfBase.setFlashAttribute("accion", EAccion.AGREGAR);
          JsfBase.setFlashAttribute("idCita", -1L);
          JsfBase.setFlashAttribute("idCliente", -1L);
          JsfBase.setFlashAttribute("fecha", new Timestamp(fecha.getTime()));
          JsfBase.setFlashAttribute("retorno", "/Paginas/Kalan/Catalogos/Pacientes/Citas/agenda");
          regresar= "nuevo".concat(Constantes.REDIRECIONAR);
        } // if
      } // if  
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
    return regresar;
  }

  public String doPageModificar() {
    String regresar= null;
    try {
      ScheduleEvent event= (ScheduleEvent)this.attrs.get("seleccionado");
      if(event!= null) {
        Entity cliente= (Entity)event.getData();
			  JsfBase.setFlashAttribute("accion", EAccion.MODIFICAR);
			  JsfBase.setFlashAttribute("idCita", cliente.toLong("idCita"));
			  JsfBase.setFlashAttribute("idCliente", cliente.toLong("idCliente"));
			  JsfBase.setFlashAttribute("fecha", cliente.toTimestamp("inicio"));
			  JsfBase.setFlashAttribute("retorno", "/Paginas/Kalan/Catalogos/Pacientes/Citas/agenda");
        regresar= "nuevo".concat(Constantes.REDIRECIONAR);
      } // if  
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
    return regresar;
  }
    
  public void doEventSelect(SelectEvent selectEvent) {
    this.attrs.put("seleccionado", selectEvent.getObject());
  }

  public void doDateSelect(SelectEvent selectEvent) {
    this.attrs.put("seleccionado", selectEvent);
  }

	private void toLoadPersonal() {
		List<Columna> columns        = new ArrayList<>();    
    Map<String, Object> params   = new HashMap<>();
    List<UISelectEntity> personas= null;
    try {      
			params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getDependencias());			
			params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);			
      columns.add(new Columna("empleado", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("correo", EFormatoDinamicos.MAYUSCULAS));
      personas= (List<UISelectEntity>) UIEntity.seleccione("VistaClientesCitasDto", "personas", params, columns, "empleado");
			this.attrs.put("personas", personas);
      if(personas!= null && !personas.isEmpty()) {
        this.attrs.put("idPersona", personas.get(0));
        Entity sinDoctor= new Entity(0L);
        sinDoctor.put("idPersona", new Value("idPersona", 0L, "id_persona"));
        sinDoctor.put("idEmpresaPersona", new Value("idEmpresaPersona", 0L, "id_empresa_persona"));
        sinDoctor.put("empleado", new Value("empleado", "SIN DOCTOR"));
        personas.add(1, new UISelectEntity(sinDoctor));
      } // if  
    } // try
    catch (Exception e) {
			throw e;
    } // catch	
    finally {
      Methods.clean(params);
      Methods.clean(columns);
    } // finally    
	}
  
  private void toLoadServicios() {
		List<Columna> columns     = new ArrayList<>();    
    Map<String, Object> params= new HashMap<>();
    List<UISelectEntity> servicios= null;
    try {      
			params.put("idArticuloTipo", "4,5");			
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
    switch(event.getTab().getTitle()) {
      case "Cliente":
    		this.idCriterio= 0;
        break;
      case "Servicio":
    		this.idCriterio= 1;
        break;
      case "Atiende":
    		this.idCriterio= 2;
        break;
    } // switch
  }  
  
}
