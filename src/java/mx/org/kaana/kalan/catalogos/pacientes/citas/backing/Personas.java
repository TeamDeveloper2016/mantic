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
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EAccion;
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

@Named(value = "kalanCatalogosPacientesCitasPersonas")
@ViewScoped
public class Personas extends IBaseFilter implements Serializable {

  private static final long serialVersionUID = 8793667741599428879L;
  
  private Entity seleccionado;  

  @PostConstruct
  @Override
  protected void init() {
    try {
      this.attrs.put("fecha", new Date(Calendar.getInstance().getTimeInMillis()));
      this.attrs.put("idPersonaProcess", JsfBase.getFlashAttribute("idPersonaProcess"));
      this.doLoad();
      this.attrs.put("idPersonaProcess", null);
      this.attrs.put("registro", Fecha.formatear(Fecha.DIA_FECHA, (Date)this.attrs.get("fecha")));
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

	public Entity getSeleccionado() {
		return seleccionado;
	}

	public void setSeleccionado(Entity seleccionado) {
		this.seleccionado = seleccionado;
	}	
  
  @Override
  public void doLoad() {
    List<Columna> columns    = new ArrayList<>();		
		Map<String, Object>params= this.toPrepare();
    try {
      this.attrs.put("registro", Fecha.formatear(Fecha.DIA_FECHA, (Date)this.attrs.get("fecha")));
      columns.add(new Columna("rfc", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("empleado", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("correo", EFormatoDinamicos.MAYUSCULAS));
      this.lazyModel = new FormatCustomLazy("VistaClientesCitasDto", "citados", params, columns);
      UIBackingUtilities.resetDataGrid();
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
		UISelectEntity persona      = null;
		List<UISelectEntity>personas= null;
		try {
			persona = (UISelectEntity)this.attrs.get("persona");
			personas= (List<UISelectEntity>)this.attrs.get("personas");
			if(!Cadena.isVacio(this.attrs.get("idPersonaProcess"))) 
        sb.append("(tr_mantic_empresa_personal.id_empresa_persona= ").append(this.attrs.get("idPersonaProcess")).append(") and");
			if(personas!= null && persona!= null && personas.indexOf(persona)>= 0) 
        sb.append("(tr_mantic_empresa_personal.id_empresa_persona= ").append(personas.get(personas.indexOf(persona)).toLong("idEmpresaPersona")).append(") and");
			else 
				if(!Cadena.isVacio(JsfBase.getParametro("razonSocial_input"))) {
          String codigo= JsfBase.getParametro("razonSocial_input").replaceAll(Constantes.CLEAN_SQL, "").trim().replaceAll("(,| |\\t)+", ".*.*");
          sb.append("(upper(concat(tc_mantic_personas.nombres, ' ', ifnull(tc_mantic_personas.paterno, ''), ' ', ifnull(tc_mantic_personas.materno, ''))) regexp '.*").append(codigo).append(".*' or upper(tc_mantic_personas.rfc) regexp '.*").append(codigo).append(".*') and");
        } // if  
			regresar.put("fecha", Fecha.formatear(Fecha.FECHA_ESTANDAR, (Date)this.attrs.get("fecha")));			
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
			JsfBase.setFlashAttribute("accion", eaccion);		
			JsfBase.setFlashAttribute("idCliente", (eaccion.equals(EAccion.MODIFICAR) || eaccion.equals(EAccion.CONSULTAR)) ? ((Entity)this.attrs.get("seleccionado")).getKey() : -1L);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch
		return "accion".concat(Constantes.REDIRECIONAR);
  } // doAccion

	public List<UISelectEntity> doCompletePersona(String codigo) {
 		List<Columna> columns     = new ArrayList<>();
    Map<String, Object> params= new HashMap<>();
    try {
      columns.add(new Columna("rfc", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("empleado", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("correo", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("citados", EFormatoDinamicos.NUMERO_SIN_DECIMALES));
  		params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
			if(!Cadena.isVacio(codigo)) {
  			codigo= codigo.replaceAll(Constantes.CLEAN_SQL, "").trim();
				codigo= codigo.toUpperCase().replaceAll("(,| |\\t)+", ".*.*");
			} // if	
			else
				codigo= "WXYZ";
			params.put("fecha", Fecha.formatear(Fecha.FECHA_ESTANDAR, (Date)this.attrs.get("fecha")));			
  		params.put(Constantes.SQL_CONDICION, "(upper(concat(tc_mantic_personas.nombres, ' ', ifnull(tc_mantic_personas.paterno, ''), ' ', ifnull(tc_mantic_personas.materno, ''))) regexp '.*".concat(codigo).concat(".*' or upper(tc_mantic_personas.rfc) regexp '.*").concat(codigo).concat(".*')"));
      this.attrs.put("personas", UIEntity.build("VistaClientesCitasDto", "citados", params, columns, 40L));
		} // try
	  catch (Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    } // finally
		return (List<UISelectEntity>)this.attrs.get("personas");
	}	

  public String doCitados() {
    String regresar= null;
    try {
			JsfBase.setFlashAttribute("persona", this.seleccionado);
			JsfBase.setFlashAttribute("idPersona", this.seleccionado.getKey());
			JsfBase.setFlashAttribute("fecha", new Date(Calendar.getInstance().getTimeInMillis()));		
			JsfBase.setFlashAttribute("retorno", "/Paginas/Kalan/Catalogos/Pacientes/Citas/personas.jsf");
			regresar= "citados".concat(Constantes.REDIRECIONAR);			
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
    return regresar;
  }
  
  public void doCitas(Entity row) {
    Map<Long, Citado>clientes = new HashMap<>();
    Map<String, Object> params= new HashMap<>();
    try {
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
  }
  
}
