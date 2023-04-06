package mx.org.kaana.kalan.catalogos.pacientes.citas.backing;

import java.io.Serializable;
import java.sql.Date;
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
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import mx.org.kaana.kalan.catalogos.pacientes.citas.beans.Citado;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.pagina.IBaseFilter;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.libs.wassenger.Saras;

@Named(value = "kalanCatalogosPacientesCitasCitados")
@ViewScoped
public class Citados extends IBaseFilter implements Serializable {

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
      this.attrs.put("persona", JsfBase.getFlashAttribute("persona"));
      this.attrs.put("idPersona", JsfBase.getFlashAttribute("idPersona"));
      this.attrs.put("fecha", (Date)JsfBase.getFlashAttribute("fecha"));
      this.attrs.put("retorno", JsfBase.getFlashAttribute("retorno")== null? "personas": JsfBase.getFlashAttribute("retorno"));
      this.attrs.put("registro", Fecha.formatear(Fecha.DIA_FECHA, (Date)this.attrs.get("fecha")));
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
      params.put("idEmpresaPersona", this.attrs.get("idPersona"));
      params.put("sortOrder", "order by tc_kalan_citas.inicio");
      columns.add(new Columna("cliente", EFormatoDinamicos.MAYUSCULAS));      
      columns.add(new Columna("inicio", EFormatoDinamicos.FECHA_HORA));    
      columns.add(new Columna("termino", EFormatoDinamicos.FECHA_HORA));    
      columns.add(new Columna("servicios", EFormatoDinamicos.MAYUSCULAS));    
      columns.add(new Columna("estatus", EFormatoDinamicos.MAYUSCULAS));    
      this.lazyModel = new FormatCustomLazy("VistaClientesCitasDto", "programados", params, columns);
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
		try {
  	  regresar.put("fecha", Fecha.formatear(Fecha.FECHA_ESTANDAR, (Date)this.attrs.get("fecha")));						
  	  regresar.put("idEmpresaPersona", this.attrs.get("idPersona"));			
			regresar.put("sucursales", JsfBase.getAutentifica().getEmpresa().getDependencias());			
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toPrepare
	
  public void doCitas() {
    Map<Long, Citado>clientes = new HashMap<>();
    Map<String, Object> params= new HashMap<>();
    try {
      Entity row= (Entity)this.attrs.get("persona");
      if(row!= null && !row.isEmpty() && !Objects.equals(row.toString("celular"), null)) {
        if(!Objects.equals(row.toLong("citados"), 0L)) {
          params.put("idEmpresaPersona", row.toLong("idEmpresaPersona"));
          params.put("sortOrder", "order by tc_kalan_citas.inicio");
          List<Entity> citados= (List<Entity>)DaoFactory.getInstance().toEntitySet("VistaClientesCitasDto", "programados", params);
          for (Entity item: citados) {
            params.put("idCita", item.toLong("idCita"));      
            List<Entity> servicios= (List<Entity>)DaoFactory.getInstance().toEntitySet("VistaClientesCitasDto", "detalle", params);
            if(servicios== null || servicios.isEmpty()) 
              servicios= new ArrayList<>();
            clientes.put(item.getKey(), new Citado(item.getKey(), item.toString("cliente"), item.toTimestamp("inicio"), servicios));
          } // for
          Saras notificar= new Saras();
          notificar.setNombre(Cadena.nombrePersona(row.toString("empleado")));
          notificar.setCelular(row.toString("celular"));
          notificar.doSendAgenda(clientes);
          JsfBase.addMessage("WhatsApp", "El mensaje fué enviado con éxito !", ETipoMensaje.INFORMACION);
        } // else 
        else
          JsfBase.addMessage("WhatsApp", "El empleado no tiene citas agendadas !", ETipoMensaje.INFORMACION);
      } // else 
      else
        JsfBase.addMessage("WhatsApp", "El empleado no tiene celular registrado !", ETipoMensaje.INFORMACION);
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
    finally {
      Methods.clean(clientes);
      Methods.clean(params);
    } // finally
  } // doCitas

  public String doAccion(String accion) {
    EAccion eaccion= null;
		try {
			eaccion= EAccion.valueOf(accion.toUpperCase());
			JsfBase.setFlashAttribute("accion", eaccion);		
			JsfBase.setFlashAttribute("retorno", "/Paginas/Kalan/Catalogos/Pacientes/Citas/personas");		
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
			JsfBase.setFlashAttribute("idPersonaProcess", this.attrs.get("idPersona"));
			JsfBase.setFlashAttribute("idPersona", this.attrs.get("idPersona"));
			regresar= ((String)this.attrs.get("retorno")).concat(Constantes.REDIRECIONAR);			
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
    return regresar;
  }

  public void doAjustar(Entity item) {
		JsfBase.setFlashAttribute("idCita", item.toLong("idCita"));		
		JsfBase.setFlashAttribute("idCliente", item.toLong("idCliente"));		
		JsfBase.setFlashAttribute("fecha", item.toTimestamp("inicia"));		
  }
  
}
