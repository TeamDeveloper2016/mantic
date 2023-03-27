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
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import mx.org.kaana.kalan.catalogos.pacientes.citas.beans.Paciente;
import mx.org.kaana.kalan.catalogos.pacientes.citas.reglas.Transaccion;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.libs.pagina.IBaseFilter;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.recurso.TcConfiguraciones;
import mx.org.kaana.libs.reflection.Methods;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


@Named(value = "kalanCatalogosPacientesCitasNuevo")
@ViewScoped
public class Nuevo extends IBaseFilter implements Serializable {

  private static final Log LOG = LogFactory.getLog(Nuevo.class);
  private static final long serialVersionUID = 327393488565639267L;  
	
  private EAccion accion;
  private Paciente paciente;
	private Entity[] seleccionados;

  public Paciente getPaciente() {
    return paciente;
  }

  public void setPaciente(Paciente paciente) {
    this.paciente = paciente;
  }
          
	public Entity[] getSeleccionados() {
		return seleccionados;
	}

	public void setSeleccionados(Entity[] seleccionados) {
		this.seleccionados = seleccionados;
	}	
  
  @PostConstruct
  @Override
  protected void init() {
    try {
      this.accion= JsfBase.getFlashAttribute("accion")== null? EAccion.AGREGAR: (EAccion)JsfBase.getFlashAttribute("accion");
      this.attrs.put("idCita", JsfBase.getFlashAttribute("idCita")== null? -1L: JsfBase.getFlashAttribute("idCita"));
      this.attrs.put("idCliente", JsfBase.getFlashAttribute("idCliente")== null? -1L: JsfBase.getFlashAttribute("idCliente"));
      this.attrs.put("fecha", JsfBase.getFlashAttribute("fecha")== null? new Date(Calendar.getInstance().getTimeInMillis()): JsfBase.getFlashAttribute("fecha"));
      this.attrs.put("retorno", JsfBase.getFlashAttribute("retorno")== null? "agenda": JsfBase.getFlashAttribute("retorno"));
      this.attrs.put("activeIndex", Objects.equals(this.accion, EAccion.AGREGAR)? 0: 1);
      this.seleccionados= new Entity[]{};      
      this.doLoad();   
      this.toLoadPersonal();
      this.toLoadServicios();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

  @Override
  public void doLoad() {
    try {
      this.attrs.put("nombreAccion", Cadena.letraCapital(this.accion.name()));
      switch (this.accion) {
        case AGREGAR:
          this.paciente= new Paciente((Date)this.attrs.get("fecha"));
          this.doUpdateInicio();
          break;
        case MODIFICAR:
        case CONSULTAR:
          this.toLoadPaciente((Long)this.attrs.get("idCita"), (Long)this.attrs.get("idCliente"));
          this.attrs.put("fecha", new Date(this.paciente.getInicio().getTime()));
          break;
      } // switch      
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // doLoad

	private void toLoadPersonal() {
		List<Columna> columns        = new ArrayList<>();    
    Map<String, Object> params   = new HashMap<>();
    List<UISelectEntity> personal= null;
    try {      
			params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getDependencias());			
      columns.add(new Columna("empleado", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("correo", EFormatoDinamicos.MAYUSCULAS));
      personal= (List<UISelectEntity>) UIEntity.seleccione("VistaClientesCitasDto", "personal", params, columns, "empleado");
			this.attrs.put("personal", personal);
      if(personal!= null && !personal.isEmpty()) 
        this.paciente.setIkAtendio(personal.get(0));
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
    try {      
			params.put("idArticuloTipo", 4L);			
      columns.add(new Columna("codigo", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      this.lazyModel = new FormatCustomLazy("VistaClientesCitasDto", "servicios", params, columns);
      UIBackingUtilities.resetDataTable();
    } // try
    catch (Exception e) {
			throw e;
    } // catch	
    finally {
      Methods.clean(params);
      Methods.clean(columns);
    } // finally    
  }
  
  public String doAceptar() {  
    String regresar        = null;
    Transaccion transaccion= null;
    try {
      LOG.info(this.seleccionados);
      if(this.seleccionados!= null && this.seleccionados.length>0) {
        transaccion= new Transaccion(this.paciente, this.seleccionados);
        if (transaccion.ejecutar(this.accion)) {
          JsfBase.setFlashAttribute("idCita", this.paciente.getIdCita());
          JsfBase.setFlashAttribute("idCliente", this.paciente.getIdCliente());
          JsfBase.addMessage("Se registro el cliente de forma correcta", ETipoMensaje.INFORMACION);
          regresar= this.doCancelar();
        } // if
        else 
          JsfBase.addMessage("Ocurrió un error al registrar el cliente", ETipoMensaje.ERROR);
      } // if
      else
        JsfBase.addMessage("Servicios", "Se tiene que seleccionar al menos un servicio", ETipoMensaje.ALERTA);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    return regresar;
  } // doAccion

  public String doCancelar() {   
    String regresar= null;
    try {
      JsfBase.setFlashAttribute("idCita", this.paciente.getIdCita());
      JsfBase.setFlashAttribute("idClienteProcess", this.paciente.getIdCliente());
  		regresar= ((String)this.attrs.get("retorno")).concat(Constantes.REDIRECIONAR);			
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
    return regresar;
  } // doCancelar

  public void doRowSeleccionado() {
    LOG.info(this.seleccionados.length);
  }
 
  public void doUpdateInicio() {
    Calendar inicio = Calendar.getInstance();
    Calendar termino= Calendar.getInstance();
    inicio.setTimeInMillis(this.paciente.getInicio().getTime());
    termino.setTimeInMillis(this.paciente.getTermino().getTime());
    Long promedio   = Numero.getLong(TcConfiguraciones.getInstance().getPropiedadServidor("tiempo.cita"), 30L);
    termino.setTimeInMillis(inicio.getTimeInMillis());
    termino.add(Calendar.MINUTE, promedio.intValue());
    this.paciente.setTermino(new Timestamp(termino.getTimeInMillis()));
  }
          
  public void doUpdateTermino() {
    Calendar inicio = Calendar.getInstance();
    Calendar termino= Calendar.getInstance();
    inicio.setTimeInMillis(this.paciente.getInicio().getTime());
    termino.setTimeInMillis(this.paciente.getTermino().getTime());
    Long minutos    = Fecha.diferenciaMinutos(inicio.getTimeInMillis(), termino.getTimeInMillis());
    Long promedio   = Numero.getLong(TcConfiguraciones.getInstance().getPropiedadServidor("tiempo.cita"), 30L);
    if((minutos< 0) || (minutos< promedio)) {
      termino.add(Calendar.MINUTE, promedio.intValue()* -1);
      this.paciente.setInicio(new Timestamp(termino.getTimeInMillis()));
    } // if  
  }
 
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

  public void doLoadGenerales() {
 		UISelectEntity cliente      = null;
		List<UISelectEntity>clientes= null;
		try {
			cliente = (UISelectEntity)this.attrs.get("cliente");
			clientes= (List<UISelectEntity>)this.attrs.get("clientes");
      if(clientes!= null && !clientes.isEmpty()) {
        int index= clientes.indexOf(cliente);
        if(index>= 0) {
          cliente= clientes.get(index);
          this.attrs.put("cliente", cliente);
        } // if  
        // BUSCAR EL CLIENTE Y QUE DEVUELVA UN OBJETO DE TIPO PACIENTE
        this.toLoadPaciente((Long)this.attrs.get("idCita"), cliente.toLong("idCliente"));
        this.doUpdateInicio();
      } // if          
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
 } 

  private void toLoadPaciente(Long idCita, Long idCliente) {
    Map<String, Object> params= new HashMap<>();
    UISelectEntity ikAtendio  = this.paciente.getIkAtendio();
    try {      
      params.put("idCita", idCita);      
      params.put("idCliente", idCliente);      
      this.paciente= (Paciente)DaoFactory.getInstance().toEntity(Paciente.class, "VistaClientesCitasDto", "paciente", params);
      if(this.paciente!= null) 
        this.paciente.setIkAtendio(ikAtendio);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);      
    } // catch	
    finally {
      Methods.clean(params);
    } // finally
  }
  
}