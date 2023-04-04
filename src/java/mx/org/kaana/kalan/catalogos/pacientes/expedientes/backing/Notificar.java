package mx.org.kaana.kalan.catalogos.pacientes.expedientes.backing;

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
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import mx.org.kaana.kalan.catalogos.pacientes.expedientes.reglas.Transaccion;
import mx.org.kaana.kalan.db.dto.TcKalanMensajesDto;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.pagina.IBaseFilter;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;

@Named(value = "kalanCatalogosPacientesExpedientesNotificar")
@ViewScoped
public class Notificar extends IBaseFilter implements Serializable {

  private static final long serialVersionUID = 8793667741599428879L;
  private Map<String, Object> criterio;
  private TcKalanMensajesDto mensaje;

  public TcKalanMensajesDto getMensaje() {
    return mensaje;
  }

  public void setMensaje(TcKalanMensajesDto mensaje) {
    this.mensaje = mensaje;
  }

  @PostConstruct
  @Override
  protected void init() {
    try {
//			if(JsfBase.getFlashAttribute("criterio")== null)
//				UIBackingUtilities.execute("janal.isPostBack('cancelar')");
//      this.criterio= (Map<String, Object>)JsfBase.getFlashAttribute("criterio");
      this.criterio= new HashMap<>();
      this.criterio.put("sucursales", JsfBase.getAutentifica().getEmpresa().getDependencias());			
      this.criterio.put(Constantes.SQL_CONDICION, "(tc_mantic_clientes.id_cliente!= 1)");			
      this.attrs.put("retorno", JsfBase.getFlashAttribute("retorno")== null? "clientes": JsfBase.getFlashAttribute("retorno"));
			this.attrs.put("fecha", new Timestamp(Calendar.getInstance().getTimeInMillis()));
			this.attrs.put("idProgramar", 2);
      this.doLoad();
      this.toLoadMensajes();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

  @Override
  public void doLoad() {
    List<Columna> columns    = new ArrayList<>();	
		Map<String, Object>params= new HashMap<>(this.criterio);
    try {
      params.put("sortOrder", "order by tc_mantic_clientes.razon_social, tc_mantic_clientes.paterno, tc_mantic_clientes.materno");
      columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));      
      columns.add(new Columna("rfc", EFormatoDinamicos.MAYUSCULAS));      
      columns.add(new Columna("cliente", EFormatoDinamicos.MAYUSCULAS));      
      columns.add(new Columna("inicio", EFormatoDinamicos.DIA_FECHA_HORA_CORTA));      
      this.lazyModel = new FormatCustomLazy("VistaClientesCitasDto", "notificar", params, columns);
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

  public void doAceptar() {
		Map<String, Object>params= new HashMap<>(this.criterio);
		Transaccion transaccion= null;
    try {
      params.put("sortOrder", "order by tc_mantic_clientes.razon_social, tc_mantic_clientes.paterno, tc_mantic_clientes.materno");
      List<Entity> items= (List<Entity>)DaoFactory.getInstance().toEntitySet("VistaClientesCitasDto", "notificar", params);
      mensaje.setDescripcion((String)this.attrs.get("mensaje"));
      transaccion= new Transaccion(items, mensaje);
      switch((Integer)this.attrs.get("idProgramar")) {
        case 1:
          mensaje.setCuando(new Timestamp(Calendar.getInstance().getTimeInMillis()));
          if(transaccion.ejecutar(EAccion.PROCESAR)) 
            JsfBase.addMessage("WhatsApp", "Se envió el mensaje a "+ items.size()+ " clientes !", ETipoMensaje.INFORMACION);
          else
            JsfBase.addMessage("WhatsApp", "Ocurrió un error en el envio del mensaje !", ETipoMensaje.INFORMACION);
          break;
        case 2:
          mensaje.setCuando((Timestamp)this.attrs.get("fecha"));
          if(transaccion.ejecutar(EAccion.REPROCESAR)) 
            JsfBase.addMessage("WhatsApp", "Se registró el mensaje con "+ items.size()+ " clientes !", ETipoMensaje.INFORMACION);
          else
            JsfBase.addMessage("WhatsApp", "Ocurrió un error en el registro del mensaje !", ETipoMensaje.INFORMACION);
          break;
      } // switch
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
    finally {
      Methods.clean(params);
    } // finally		
  } // doAceptar

  public String doCancelar() {
    String regresar= null;
    try {
			JsfBase.setFlashAttribute("citerio", this.attrs.get("citerio"));
			regresar= ((String)this.attrs.get("retorno")).concat(Constantes.REDIRECIONAR);			
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
    return regresar;
  }

  public String toColor(Entity row) {
		return Cadena.isVacio(row.toString("celular"))? "janal-tr-yellow": "";
	} 
  
  public void doCleanMensaje() {
    // "<strong> negritas *
    // "<em> italica _
    // "<u> tachado ~
    // "<br> enter ////n
//    String mensaje= (String)this.attrs.get("mensaje");
//    if(!Cadena.isVacio(mensaje))
//      mensaje= mensaje.replaceAll("<p>", "").replaceAll("</p>", "").replaceAll("<br>", "\\\\n").replaceAll("<strong>", "*").replaceAll("</strong>", "*").replaceAll("<em>", "_").replaceAll("</em>", "_").replaceAll("<u>", "~").replaceAll("</u>", "~");
//    this.attrs.put("mensaje", mensaje.replaceAll("\n", "\\n"));
  }

  public void doUpdateFecha() {
    Calendar inicio = Calendar.getInstance();
    Calendar termino= Calendar.getInstance();
    inicio.setTimeInMillis(((Timestamp)this.attrs.get("fecha")).getTime());
    if(inicio.before(termino)) {
      this.attrs.put("fecha", new Timestamp(termino.getTimeInMillis()));
      JsfBase.addMessage("Fecha", "La fecha y hora tienen que ser mayor que la actual !", ETipoMensaje.INFORMACION);
    } // if  
  }

  private void toLoadMensajes() {
    List<Columna> columns     = new ArrayList<>();    
    Map<String, Object> params= new HashMap<>();
    List<UISelectEntity> programados= null;
    try {      
			params.put("fecha", Fecha.formatear(Fecha.FECHA_HORA_LARGA));			
      columns.add(new Columna("codigo", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      programados= UIEntity.seleccione("TcKalanMensajesDto", "vigentes", params, columns, "consecutivo");
      if(programados!= null && !programados.isEmpty()) {
        this.attrs.put("idMensaje", UIBackingUtilities.toFirstKeySelectEntity(programados));         
        for (UISelectEntity item : programados) {
          if(!Objects.equals(item.toString("descrpcion"), null) && item.toString("descrpcion").length()> 40) {
            String descripcion= item.toString("descrpcion");
            item.get("descripcion").setData(descripcion.substring(0, 40).concat(" ..."));
          } // if  
        } // for
        this.doFindMensaje();
      } // if
      this.attrs.put("programados", programados); 
      this.doFindMensaje();
    } // try
    catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
    } // catch	
    finally {
      Methods.clean(params);
      Methods.clean(columns);
    } // finally        
  }

  public void doFindMensaje() {
    List<UISelectEntity> programados= null;
    UISelectEntity idMensaje        = null;
    Long key                        = -1L;
    try {      
      idMensaje= (UISelectEntity)this.attrs.get("idMensaje");
      if(!Objects.equals(idMensaje, null)) { 
        if(!idMensaje.isEmpty() && idMensaje.size()> 1) 
          key= idMensaje.getKey();
        else {
          programados= (List<UISelectEntity>)this.attrs.get("programados");
          if(programados!= null && !programados.isEmpty()) {
            int index  = programados.indexOf(idMensaje);
            if(index>= 0) {
              key= programados.get(0).getKey();
              this.attrs.put("idMensaje", programados.get(0));
            } // if  
            else {
              idMensaje= UIBackingUtilities.toFirstKeySelectEntity(programados);
              this.attrs.put("idMensaje", idMensaje);
              key= idMensaje.getKey();
            } // else  
          } // if
          else 
            this.attrs.put("idMensaje", new UISelectEntity(-1L));  
        } // else
      } // if
      if(!Objects.equals(key, -1L)) {
        this.mensaje= (TcKalanMensajesDto)DaoFactory.getInstance().findById(TcKalanMensajesDto.class, key);
        if(Objects.equals(this.mensaje, null)) 
          this.mensaje= new TcKalanMensajesDto();
      } // if  
      else
        this.mensaje= new TcKalanMensajesDto();
      this.attrs.put("idProgramar", this.mensaje.isValid()? 2L: 1L);
    } // try
    catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
    } // catch	
  }

}
