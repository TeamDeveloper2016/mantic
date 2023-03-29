package mx.org.kaana.libs.wassenger;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import mx.org.kaana.libs.formato.Error;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kalan.catalogos.pacientes.citas.beans.Citado;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Encriptar;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 08/03/2023
 *@time 09:55:07 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public final class Citas implements Serializable {

  private static final long serialVersionUID = -6510759858245427836L;
  private static final Log LOG = LogFactory.getLog(Citas.class);
  
  private static final String IMOX_TOKEN       = "IMOX_TOKEN";
  public static final String IMOX_GROUP_MANTIC = "5214491813810-1598307650@g.us";
  public static final String IMOX_GROUP_KANAL  = "5214491813810-1598307650@g.us";
  public static final String IMOX_GROUP_TSAAK  = "5214491813810-1598307650@g.us";
  
  private static final String CITA_MESSAGE_MANTIC= "\"phone\":\"+521{celular}\",\"message\":\"Estimad@ _{nombre}_:\\n\\n{saludo}, te notificamos que se ha *{estatus}* la cita para el día *{fecha}* hrs. para realizar los siguientes servicios:\\n{servicios}\\nPara cualquier cambio en su cita, por favor no dude en contactarnos al teléfono *{notifica}*\\nGracias por preferirnos *_{empresa}_*.\\n\\nchatbot *IMOX* _Soluciones web_ (4492090586)\"";
  private static final String CITA_MESSAGE_KALAN = "\"phone\":\"+521{celular}\",\"message\":\"Estimad@ _{nombre}_:\\n\\n{saludo}, te notificamos que se ha *{estatus}* la cita para el día *{fecha}* hrs. para realizar los siguientes servicios:\\n{servicios}\\nPara cualquier cambio en su cita, por favor no dude en contactarnos al teléfono *{notifica}*\\nGracias por preferirnos *_{empresa}_*.\\n\\nchatbot *IMOX* _Soluciones web_ (4492090586)\"";
  private static final String CITA_MESSAGE_TSAAK = "\"phone\":\"+521{celular}\",\"message\":\"Estimad@ _{nombre}_:\\n\\n{saludo}, te notificamos que se ha *{estatus}* la cita para el día *{fecha}* hrs. para realizar los siguientes servicios:\\n{servicios}\\nPara cualquier cambio en su cita, por favor no dude en contactarnos al teléfeno *{notifica}*\\nGracias por preferirnos *_{empresa}_*.\\n\\nchatbot *IMOX* _Soluciones web_ (4492090586)\"";
  
  private static final String ATIENDE_MESSAGE_MANTIC= "\"phone\":\"+521{celular}\",\"message\":\"Estimad@ _{nombre}_:\\n\\n{saludo}, te notificamos que *{cliente}* ha *{estatus}* una cita para que lo atiendas el día *{fecha}* hrs. para realizarle los siguientes servicios:\\n{servicios}\\nEn caso de no poder atender al cliente, favor de notificar de inmediato al teléfono *{notifica}*\\nAtentamente *_{empresa}_*.\\n\\nchatbot *IMOX* _Soluciones web_ (4492090586)\"";
  private static final String ATIENDE_MESSAGE_KALAN = "\"phone\":\"+521{celular}\",\"message\":\"Estimad@ _{nombre}_:\\n\\n{saludo}, te notificamos que *{cliente}* ha *{estatus}* una cita para que lo atiendas el día *{fecha}* hrs. para realizarle los siguientes servicios:\\n{servicios}\\nEn caso de no poder atender al cliente, favor de notificar de inmediato al teléfono *{notifica}*\\nAtentamente *_{empresa}_*.\\n\\nchatbot *IMOX* _Soluciones web_ (4492090586)\"";
  private static final String ATIENDE_MESSAGE_TSAAK = "\"phone\":\"+521{celular}\",\"message\":\"Estimad@ _{nombre}_:\\n\\n{saludo}, te notificamos que *{cliente}* ha *{estatus}* una cita para que lo atiendas el día *{fecha}* hrs. para realizarle los siguientes servicios:\\n{servicios}\\nEn caso de no poder atender al cliente, favor de notificar de inmediato al teléfono *{notifica}*\\nAtentamente *_{empresa}_*.\\n\\nchatbot *IMOX* _Soluciones web_ (4492090586)\"";
  
  private static final String AGENDA_MESSAGE_MANTIC= "\"phone\":\"+521{celular}\",\"message\":\"Estimad@ _{nombre}_:\\n\\n{saludo}, te enviamos las citas por atender el día de hoy *{fecha}*\\n\\n{clientes}Atentamente *_{empresa}_*.\\n\\nchatbot *IMOX* _Soluciones web_ (4492090586)\"";
  private static final String AGENDA_MESSAGE_KALAN = "\"phone\":\"+521{celular}\",\"message\":\"Estimad@ _{nombre}_:\\n\\n{saludo}, te enviamos las citas por atender el día de hoy *{fecha}*\\n\\n{clientes}Atentamente *_{empresa}_*.\\n\\nchatbot *IMOX* _Soluciones web_ (4492090586)\"";
  private static final String AGENDA_MESSAGE_TSAAK = "\"phone\":\"+521{celular}\",\"message\":\"Estimad@ _{nombre}_:\\n\\n{saludo}, te enviamos las citas por atender el día de hoy *{fecha}*\\n\\n{clientes}Atentamente *_{empresa}_*.\\n\\nchatbot *IMOX* _Soluciones web_ (4492090586)\"";
  
  private static final String PATH_REPORT   = "{numero}.- {documento}; https://{host}/Temporal/Pdf/{reporte}\\n";
  private static final int LENGTH_CELL_PHONE= 10;

  private String token;
  private String nombre;
  private String celular;
  private Timestamp fecha;
  private String estatus;
  private List<Entity> servicios;

  public Citas() {
    this("", "", new Timestamp(Calendar.getInstance().getTimeInMillis()), "agendada");
    this.servicios= new ArrayList<>();
    this.servicios.add(new Entity(1L, "nombre", "MANICURE"));
    this.servicios.add(new Entity(2L, "nombre", "DEPILACIÓN"));
    this.servicios.add(new Entity(3L, "nombre", "CORTE DE CABALLERO"));
  }
  
  public Citas(String nombre, String celular, Timestamp fecha, String estatus) {
    this(nombre, celular, fecha, estatus, Collections.EMPTY_LIST);
  }
  
  public Citas(String nombre, String celular, Timestamp fecha, String estatus, List<Entity> servicios) {
    this.nombre = Cadena.nombrePersona(nombre);
    this.celular= this.clean(celular);
    this.fecha  = fecha;
    this.estatus= estatus;
    this.token  = System.getenv(IMOX_TOKEN);
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getCelular() {
    return celular;
  }

  public void setCelular(String celular) {
    this.setCelular(celular, Boolean.TRUE);
  }

  public void setCelular(String celular, Boolean clean) {
    this.celular = clean? this.clean(celular): celular;
  }

  public Timestamp getFecha() {
    return fecha;
  }

  public void setFecha(Timestamp fecha) {
    this.fecha = fecha;
  }

  public String getEstatus() {
    return estatus;
  }

  public void setEstatus(String estatus) {
    this.estatus = estatus;
  }

  public List<Entity> getServicios() {
    return servicios;
  }

  public void setServicios(List<Entity> servicios) {
    this.servicios = servicios;
  }

  private String toServicios() {
    return toServicios(this.servicios);
  }
  
  private String toServicios(List<Entity> items) {
    StringBuilder regresar= new StringBuilder();
    if(items!= null && !items.isEmpty())
      for (Entity item: items) {
        regresar.append("   - *").append(item.toString("nombre")).append("*\\n");
      } // for
    return regresar.length()> 0? regresar.toString(): "*SIN SERVICIOS*\\n";
  }
          
  @Override
  public String toString() {
    return "Message{" + "celular=" + celular+ '}';
  }
  
  private String toSaludo() {
    String regresar= null;
    Calendar calendar= Calendar.getInstance();
    int hour= calendar.get(Calendar.HOUR_OF_DAY);
    if(hour>= 5 && hour< 12)
      regresar= "Buenos días";
    else
      if(hour>= 12 && hour< 19)
        regresar= "Buenas tardes";
      else 
        regresar= "Buenas noches";
    return regresar;
  }
  
  private String clean(String number) {
    StringBuilder regresar= new StringBuilder();
    if(number!= null) 
      for (int x= 0; x< number.length(); x++) {
        if(number.charAt(x)>= '0' && number.charAt(x)<= '9') 
          regresar.append(number.charAt(x));
      } // for
    return regresar.toString();
  }

  public void doSendCitaCliente() {
    this.doSendCitaCliente(null);
  }

  public void doSendCitaCliente(Session sesion) {
    if(Objects.equals(this.celular.length(), LENGTH_CELL_PHONE) || this.celular.contains("@")) {
      Message message= null;
      String mensaje = CITA_MESSAGE_MANTIC;
      Map<String, Object> params = new HashMap<>();   
      switch(Configuracion.getInstance().getPropiedad("sistema.empresa.principal")) {
        case "mantic":
          mensaje= CITA_MESSAGE_MANTIC;
          break;
        case "kalan":
          mensaje= CITA_MESSAGE_KALAN;
          break;
        case "tsaak":
          mensaje= CITA_MESSAGE_TSAAK;
          break;
      } // swtich
      try {
        params.put("nombre", this.nombre);
        params.put("celular", this.celular);
        params.put("fecha", Fecha.formatear(Fecha.DIA_FECHA_HORA_CORTA, this.fecha));
        params.put("estatus", this.estatus);
        params.put("servicios", this.toServicios());
        params.put("empresa", Cadena.nombrePersona(Configuracion.getInstance().getEmpresa("titulo")));
        params.put("host", Configuracion.getInstance().getEmpresa("host"));
        params.put("notifica", Configuracion.getInstance().getEmpresa("celular"));
        params.put("saludo", this.toSaludo());
        params.put("idTipoMensaje", ETypeMessage.CITAS.getId());
        if(!Configuracion.getInstance().isEtapaProduccion())
          LOG.warn(params.toString()+ " {"+ Cadena.replaceParams(mensaje, params, true)+ "}");
        else {  
          HttpResponse<String> response = Unirest.post("https://api.wassenger.com/v1/messages")
          .header("Content-Type", "application/json")
          .header("Token", this.token)
          .body("{"+ Cadena.replaceParams(mensaje, params, true)+ "}")
          .asString();
          LOG.error("Enviado: "+ response.getBody());
          if(Objects.equals(response.getStatus(), 201)) {
            Gson gson= new Gson();
            message  = gson.fromJson(response.getBody(), Message.class);
            if(message!= null)
              message.init();
            else
              message= new Message();
          } // if  
          else {
            LOG.error("[doSendCitaCliente] No se envio el mensaje whatsapp  ["+ this.celular+ "] "+ response.getStatusText()+ "\n"+ response.getBody());
            message= new Message();
            message.setMessage(" {"+ Cadena.replaceParams(mensaje, params, true)+ "}");
          } // else  
          message.setTelefono(this.celular);
          message.setIdSendStatus(new Long(response.getStatus()));
          message.setSendStatus(response.getStatusText());
          message.setIdTipoMensaje((Long)params.get("idTipoMensaje"));
          message.setIdUsuario(JsfBase.getIdUsuario());
          if(sesion!= null)
            DaoFactory.getInstance().insert(sesion, message);
          else
            DaoFactory.getInstance().insert(message);
        } // else  
      } // try
      catch(Exception e) {
        Error.mensaje(e);
      } // catch
      finally {
        Methods.clean(params);
      } // finally
    } // if
    else 
      LOG.error("[doSendCitaCliente] No se envio el mensaje por whatsapp  ["+ this.celular+ "]");
  }
  
  public void doSendCitaAtiende(String cliente) {
    this.doSendCitaAtiende(null, cliente);
  }

  public void doSendCitaAtiende(Session sesion, String cliente) {
    if(Objects.equals(this.celular.length(), LENGTH_CELL_PHONE) || this.celular.contains("@")) {
      Message message= null;
      String mensaje = ATIENDE_MESSAGE_MANTIC;
      Map<String, Object> params = new HashMap<>();   
      switch(Configuracion.getInstance().getPropiedad("sistema.empresa.principal")) {
        case "mantic":
          mensaje= ATIENDE_MESSAGE_MANTIC;
          break;
        case "kalan":
          mensaje= ATIENDE_MESSAGE_KALAN;
          break;
        case "tsaak":
          mensaje= ATIENDE_MESSAGE_TSAAK;
          break;
      } // swtich
      try {
        params.put("nombre", this.nombre);
        params.put("celular", this.celular);
        params.put("fecha", Fecha.formatear(Fecha.DIA_FECHA_HORA_CORTA, this.fecha));
        params.put("estatus", this.estatus);
        params.put("cliente", cliente);
        params.put("servicios", this.toServicios());
        params.put("empresa", Cadena.nombrePersona(Configuracion.getInstance().getEmpresa("titulo")));
        params.put("host", Configuracion.getInstance().getEmpresa("host"));
        params.put("notifica", Configuracion.getInstance().getEmpresa("celular"));
        params.put("saludo", this.toSaludo());
        params.put("idTipoMensaje", ETypeMessage.CITAS.getId());
        if(!Configuracion.getInstance().isEtapaProduccion())
          LOG.warn(params.toString()+ " {"+ Cadena.replaceParams(mensaje, params, true)+ "}");
        else {  
          HttpResponse<String> response = Unirest.post("https://api.wassenger.com/v1/messages")
          .header("Content-Type", "application/json")
          .header("Token", this.token)
          .body("{"+ Cadena.replaceParams(mensaje, params, true)+ "}")
          .asString();
          LOG.error("Enviado: "+ response.getBody());
          if(Objects.equals(response.getStatus(), 201)) {
            Gson gson= new Gson();
            message  = gson.fromJson(response.getBody(), Message.class);
            if(message!= null)
              message.init();
            else
              message= new Message();
          } // if  
          else {
            LOG.error("[doSendCitaAtiende] No se envio el mensaje whatsapp  ["+ this.celular+ "] "+ response.getStatusText()+ "\n"+ response.getBody());
            message= new Message();
            message.setMessage(" {"+ Cadena.replaceParams(mensaje, params, true)+ "}");
          } // else  
          message.setTelefono(this.celular);
          message.setIdSendStatus(new Long(response.getStatus()));
          message.setSendStatus(response.getStatusText());
          message.setIdTipoMensaje((Long)params.get("idTipoMensaje"));
          message.setIdUsuario(JsfBase.getIdUsuario());
          if(sesion!= null)
            DaoFactory.getInstance().insert(sesion, message);
          else
            DaoFactory.getInstance().insert(message);
        } // else  
      } // try
      catch(Exception e) {
        Error.mensaje(e);
      } // catch
      finally {
        Methods.clean(params);
      } // finally
    } // if
    else 
      LOG.error("[doSendCitaAtiende] No se envio el mensaje por whatsapp  ["+ this.celular+ "]");
  }
  
  public void doSendAgenda(Map<Long, Citado> clientes) {
    this.doSendAgenda(null, clientes);
  }

  public void doSendAgenda(Session sesion, Map<Long, Citado> clientes) {
    if(Objects.equals(this.celular.length(), LENGTH_CELL_PHONE) || this.celular.contains("@")) {
      Message message= null;
      String mensaje = ATIENDE_MESSAGE_MANTIC;
      Map<String, Object> params = new HashMap<>();   
      switch(Configuracion.getInstance().getPropiedad("sistema.empresa.principal")) {
        case "mantic":
          mensaje= AGENDA_MESSAGE_MANTIC;
          break;
        case "kalan":
          mensaje= AGENDA_MESSAGE_KALAN;
          break;
        case "tsaak":
          mensaje= AGENDA_MESSAGE_TSAAK;
          break;
      } // swtich
      try {
        params.put("nombre", this.nombre);
        params.put("celular", this.celular);
        params.put("fecha", Fecha.formatear(Fecha.FECHA_CORTA, this.fecha));
        params.put("clientes", this.toClientes(clientes));
        params.put("empresa", Cadena.nombrePersona(Configuracion.getInstance().getEmpresa("titulo")));
        params.put("host", Configuracion.getInstance().getEmpresa("host"));
        params.put("notifica", Configuracion.getInstance().getEmpresa("celular"));
        params.put("saludo", this.toSaludo());
        params.put("idTipoMensaje", ETypeMessage.CITAS.getId());
        if(!Configuracion.getInstance().isEtapaProduccion())
          LOG.warn(params.toString()+ " {"+ Cadena.replaceParams(mensaje, params, true)+ "}");
        else {  
          HttpResponse<String> response = Unirest.post("https://api.wassenger.com/v1/messages")
          .header("Content-Type", "application/json")
          .header("Token", this.token)
          .body("{"+ Cadena.replaceParams(mensaje, params, true)+ "}")
          .asString();
          LOG.error("Enviado: "+ response.getBody());
          if(Objects.equals(response.getStatus(), 201)) {
            Gson gson= new Gson();
            message  = gson.fromJson(response.getBody(), Message.class);
            if(message!= null)
              message.init();
            else
              message= new Message();
          } // if  
          else {
            LOG.error("[doSendAgenda] No se envio el mensaje whatsapp  ["+ this.celular+ "] "+ response.getStatusText()+ "\n"+ response.getBody());
            message= new Message();
            message.setMessage(" {"+ Cadena.replaceParams(mensaje, params, true)+ "}");
          } // else  
          message.setTelefono(this.celular);
          message.setIdSendStatus(new Long(response.getStatus()));
          message.setSendStatus(response.getStatusText());
          message.setIdTipoMensaje((Long)params.get("idTipoMensaje"));
          message.setIdUsuario(JsfBase.getIdUsuario());
          if(sesion!= null)
            DaoFactory.getInstance().insert(sesion, message);
          else
            DaoFactory.getInstance().insert(message);
        } // else  
      } // try
      catch(Exception e) {
        Error.mensaje(e);
      } // catch
      finally {
        Methods.clean(params);
      } // finally
    } // if
    else 
      LOG.error("[doSendAgenda] No se envio el mensaje por whatsapp  ["+ this.celular+ "]");
  }  
  
  private String toClientes(Map<Long, Citado> clientes) throws Exception {
    StringBuilder regresar= new StringBuilder();
    try {      
      for (Long key: clientes.keySet()) {
        Citado item= clientes.get(key);
        regresar.append("*").append(item.getNombre()).append("* citado a las *").append(item.toHora()).append("* hrs, para los siguientes servicios:\\n");
        regresar.append(this.toServicios(item.getServicios())).append("\\n");
      } // for
    } // try
    catch (Exception e) {
      throw e;
    } // catch	
    return Objects.equals(regresar.length(), 0)? "NO TIENES CITAS PARA HOY": regresar.toString();
  }
  
  public static void main(String ... args) {
    Map<String, Object> actores= new HashMap<>();
    Encriptar encriptar        = new Encriptar();    
    actores.put("Alejandro Jiménez García", encriptar.desencriptar("cd4b3e3924191b057b8187"));
    
    List<Entity> servicios= new ArrayList<>();
    servicios.add(new Entity(1L, "nombre", "MANICURE"));
    servicios.add(new Entity(2L, "nombre", "DEPILACIÓN"));
    servicios.add(new Entity(3L, "nombre", "CORTE DE CABALLERO"));
    Map<Long, Citado>clientes= new HashMap<>();
    clientes.put(1L, new Citado(1L, "Alejandro Jimenez", new Timestamp(Calendar.getInstance().getTimeInMillis()), servicios));
    clientes.put(2L, new Citado(2L, "Juan Pérez", new Timestamp(Calendar.getInstance().getTimeInMillis()), servicios));
    clientes.put(3L, new Citado(3L, "María García", new Timestamp(Calendar.getInstance().getTimeInMillis()), servicios));
    Citas notificar= new Citas();
    for (String item: actores.keySet()) {
      notificar.setNombre(Cadena.nombrePersona(item));
      notificar.setCelular((String)actores.get(item));
      LOG.info("Enviando mensaje de whatsapp al celular: "+ notificar.getCelular());
      notificar.doSendCitaCliente();
      // notificar.doSendCitaAtiende(Cadena.nombrePersona("Juan Perez López"));
      // notificar.doSendAgenda(clientes);
    } // for
  }  

}
