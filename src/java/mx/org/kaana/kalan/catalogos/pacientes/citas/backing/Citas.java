package mx.org.kaana.kalan.catalogos.pacientes.citas.backing;

import java.io.Serializable;
import java.sql.Timestamp;
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
import mx.org.kaana.libs.pagina.IBaseFilter;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.libs.wassenger.Saras;

@Named(value = "kalanCatalogosPacientesCitasCitas")
@ViewScoped
public class Citas extends IBaseFilter implements Serializable {

  private static final long serialVersionUID = 8793667741599428879L;
  private Entity cliente;

  public Entity getCliente() {
    return cliente;
  }

  public void setCliente(Entity cliente) {
    this.cliente = cliente;
  }
  
  @PostConstruct
  @Override
  protected void init() {
    try {
			if(JsfBase.getFlashAttribute("idCliente")== null)
				UIBackingUtilities.execute("janal.isPostBack('cancelar')");
      this.attrs.put("idCliente", JsfBase.getFlashAttribute("idCliente"));
      // this.attrs.put("retorno", JsfBase.getFlashAttribute("retorno")== null? "clientes": JsfBase.getFlashAttribute("retorno"));
      this.attrs.put("retorno", "clientes");
      this.doLoad();
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
      params.put("idCliente", this.attrs.get("idCliente"));
      columns.add(new Columna("inicio", EFormatoDinamicos.DIA_FECHA_HORA_CORTA));    
      this.cliente= (Entity)DaoFactory.getInstance().toEntity("VistaClientesCitasDto", "clientes", params);
      if(this.cliente!= null && !this.cliente.isEmpty())
        UIBackingUtilities.toFormatEntity(this.cliente, columns);
      params.put("sortOrder", "order by tc_kalan_citas.inicio desc");
      columns.clear();
      columns.add(new Columna("cliente", EFormatoDinamicos.MAYUSCULAS));      
      columns.add(new Columna("inicio", EFormatoDinamicos.FECHA_HORA));    
      columns.add(new Columna("termino", EFormatoDinamicos.FECHA_HORA));    
      columns.add(new Columna("servicios", EFormatoDinamicos.MAYUSCULAS));    
      columns.add(new Columna("estatus", EFormatoDinamicos.MAYUSCULAS));    
      this.lazyModel = new FormatCustomLazy("VistaClientesCitasDto", params, columns);
      UIBackingUtilities.resetDataTable();
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
		Map<String, Object> regresar= new HashMap<>();
    StringBuilder sb            = new StringBuilder();
		try {
			if(!Cadena.isVacio(this.attrs.get("idCliente"))) 
        sb.append("(tc_mantic_clientes.id_cliente= ").append(this.attrs.get("idCliente")).append(") and");
			regresar.put("sucursales", JsfBase.getAutentifica().getEmpresa().getDependencias());			
			if(Objects.equals(sb.length(), 0))
        regresar.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
      else
        regresar.put(Constantes.SQL_CONDICION, sb.substring(0, sb.length()- 4));
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toPrepare
	
  public String doAccion(String accion) {
    EAccion eaccion= null;
		try {
			eaccion= EAccion.valueOf(accion.toUpperCase());
      if(Objects.equals(eaccion, EAccion.AGREGAR)) {
			  JsfBase.setFlashAttribute("accion", eaccion);		
			  JsfBase.setFlashAttribute("idCliente", this.attrs.get("idCliente"));		
			  JsfBase.setFlashAttribute("fecha", new Timestamp(Calendar.getInstance().getTimeInMillis()));		
      } // if
      else {
        Entity seleccionado= (Entity)this.attrs.get("seleccionado");
        JsfBase.setFlashAttribute("idCita", seleccionado.toLong("idCita"));		
        JsfBase.setFlashAttribute("idCliente", seleccionado.toLong("idCliente"));		
        JsfBase.setFlashAttribute("fecha", seleccionado.toTimestamp("inicia"));
      } // else  
			JsfBase.setFlashAttribute("retorno", "/Paginas/Kalan/Catalogos/Pacientes/Citas/citas.jsf");		
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch
		return "nuevo".concat(Constantes.REDIRECIONAR);
  } // doAccion

  public String doCancelar() {
    String regresar= null;
    try {
			JsfBase.setFlashAttribute("idClienteProcess", this.attrs.get("idCliente"));
			regresar= ((String)this.attrs.get("retorno")).concat(Constantes.REDIRECIONAR);			
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
    return regresar;
  }
  
  public void doMensaje(Entity item) {
    Map<String, Object> params= new HashMap<>();
    try {
      if(item!= null && !item.isEmpty()) {
        params.put("idCita", item.toLong("idCita"));      
        params.put("idCliente", item.toLong("idCliente"));
        Entity citado= (Entity)DaoFactory.getInstance().toEntity("VistaClientesCitasDto", "paciente", params);
        if(citado!= null && !citado.isEmpty() && !Objects.equals(citado.toString("celular"), null)) {
          List<Entity> servicios= (List<Entity>)DaoFactory.getInstance().toEntitySet("VistaClientesCitasDto", "detalle", params);
          if(servicios== null || servicios.isEmpty()) 
            servicios= new ArrayList<>();
          Saras notificar= new Saras(item.toString("cliente"), citado.toString("celular"), item.toTimestamp("inicia"), citado.toString("estatus"), servicios);
          notificar.doSendCitaCliente();
          JsfBase.addMessage("WhatsApp", "El mensaje fué enviado con éxito !", ETipoMensaje.INFORMACION);
        } // if
        else 
          JsfBase.addMessage("WhatsApp", "El empleado no tiene celular registrado !", ETipoMensaje.INFORMACION);
      } // if
      else 
        JsfBase.addMessage("WhatsApp", "No se pudo enviar el mensaje, intente de nuevo !", ETipoMensaje.ERROR);
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
    finally {
      Methods.clean(params);
    } // finally
  }

  public String doEliminar() {  
    String regresar        = null;
    Transaccion transaccion= null;
    Entity seleccionado    = null;
    Map<String, Object> params= new HashMap<>();
    try {
      seleccionado     = (Entity)this.attrs.get("seleccionado");
      Paciente paciente= new Paciente(
        seleccionado.toLong("idCliente"), // Long idCliente, 
        seleccionado.toTimestamp("inicia"), // Timestamp inicio, 
        seleccionado.toTimestamp("termina"), // Timestamp termino, 
        this.cliente.toString("celular"), // String celular, 
        this.cliente.toString("correo"), // String correo, 
        seleccionado.toLong("idCliente") // Long idCita
      );
      paciente.setRazonSocial(this.cliente.toString("razonSocial"));
      paciente.setPaterno(this.cliente.toString("paterno"));
      paciente.setMaterno(this.cliente.toString("materno"));
      params.put("idCita", seleccionado.toLong("idCita"));      
      // RECUPERA LOS SERVICIOS DE LA CITA
      List<Entity> servicios= (List<Entity>)DaoFactory.getInstance().toEntitySet("VistaClientesCitasDto", "detalle", params);
      if(servicios== null || servicios.isEmpty()) 
        servicios= new ArrayList<>();
      // RECUPERA LOS DATOS DE LA PERSONA QUE LO VA ATENDER
      if(!Objects.equals(seleccionado.toLong("idAtendio"), null) && !Objects.equals(seleccionado.toLong("idAtendio"), -1L)) {
        params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getDependencias());			
        params.put(Constantes.SQL_CONDICION, "(tr_mantic_empresa_personal.id_empresa_persona="+ seleccionado.toLong("idAtendio")+ ")");			
        Entity persona= (Entity)DaoFactory.getInstance().toEntity("VistaClientesCitasDto", "personas", params);
        paciente.setIkAtendio(new UISelectEntity(persona));
      } // if
      if(!Objects.equals(this.attrs.get("justificacion"), null))
        paciente.setComentarios((String)this.attrs.get("justificacion"));
      transaccion= new Transaccion(paciente, servicios);
      if (transaccion.ejecutar(EAccion.ELIMINAR)) {
        JsfBase.setFlashAttribute("idCita", seleccionado.toLong("idCita"));
        JsfBase.setFlashAttribute("idCliente", seleccionado.toLong("idCliente"));
        JsfBase.addMessage("Se eliminó la cita de forma correcta", ETipoMensaje.INFORMACION);
      } // if
      else 
        JsfBase.addMessage("Ocurrió un error al registrar eliminar la cita", ETipoMensaje.ERROR);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    finally {
      Methods.clean(params);
    } // finally
    return regresar;
  }

  public String doImportar() {  
    String regresar    = null;
    Entity seleccionado= null;
    try {
      seleccionado     = (Entity)this.attrs.get("seleccionado");
			JsfBase.setFlashAttribute("accion", EAccion.AGREGAR);		
      JsfBase.setFlashAttribute("idCita", seleccionado.toLong("idCita"));
      JsfBase.setFlashAttribute("idCliente", seleccionado.toLong("idCliente"));
			JsfBase.setFlashAttribute("retorno", "/Paginas/Kalan/Catalogos/Pacientes/Citas/citas.jsf");
			regresar= "/Paginas/Kalan/Catalogos/Pacientes/Expedientes/importar".concat(Constantes.REDIRECIONAR);			
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    return regresar;
  }  
  
  public String doDiagnostico() {  
    String regresar    = null;
    Entity seleccionado= null;
    try {
      seleccionado     = (Entity)this.attrs.get("seleccionado");
      JsfBase.setFlashAttribute("idCita", seleccionado.toLong("idCita"));
      JsfBase.setFlashAttribute("idCliente", seleccionado.toLong("idCliente"));
			JsfBase.setFlashAttribute("retorno", "/Paginas/Kalan/Catalogos/Pacientes/Citas/citas.jsf");
			regresar= "/Paginas/Kalan/Catalogos/Pacientes/Expedientes/diagnostico".concat(Constantes.REDIRECIONAR);			
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    return regresar;
  }  
  
}
