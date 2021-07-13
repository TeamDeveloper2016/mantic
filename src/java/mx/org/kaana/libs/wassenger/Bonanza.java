package mx.org.kaana.libs.wassenger;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import mx.org.kaana.libs.formato.Error;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.kajool.enums.EEtapaServidor;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 25/06/2021
 *@time 09:55:07 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public final class Bonanza implements Serializable {

  private static final long serialVersionUID = -6510759858245467836L;
  private static final Log LOG = LogFactory.getLog(Bonanza.class);
  
  private static final String IMOX_TOKEN      = "IMOX_TOKEN";
  public static final String IMOX_GROUP       = "5214491813810-1598307650@g.us";
  private static final String BODY_MESSAGE    = "\"phone\":\"+521{celular}\",\"message\":\"Hola _{nombre}_,\\n{saludo}, somos *Ferreteria Bonanza*, si deseas mantener una comunicaci�n con nosotros por este medio, favor de aceptar el mensaje respondiendo con un *hola* en esta conversaci�n (_soy un chatbot asociado al sistema_).\\n\\n_Ferreteria *Bonanza*_ !\"";
  private static final String BODY_FACTURA    = "\"phone\":\"+521{celular}\",\"message\":\"Hola _{nombre}_,\\n{saludo}, te hacemos llegar la factura con folio *{ticket}* del d�a *{fecha}*, para descarcarla favor de hacer clic en el siguiente enlace:{reporte}Para cualquier duda o aclaraci�n por favor no dude en contactarnos a la siguiente cuenta de correo *ventas@ferreteriabonanza.com*.\\n\\n_Ferreteria *Bonanza*_ !\"";
  
  private static final String BODY_RESIDENTE  = "\"phone\":\"+521{celular}\",\"message\":\"Hola _{nombre}_,\\n\\n{saludo}, te hacemos llegar los reportes de los destajos de los *contratistas* o *subcontratistas* de la n�mina *{nomina}* del {periodo}, hacer clic en los siguientes enlaces:\\n{reporte}\\nSe tienen *24 hrs* para descargar todos los reportes.\\n\\nCAFU Construcciones\"";
  private static final String BODY_GASTO_CHICA= "\"phone\":\"+521{celular}\",\"message\":\"Hola _{nombre}_,\\n\\n{saludo}, te notificamos que los gastos a pagar por concepto de caja chica ascienden a {reporte} pesos de la semana *{nomina}* del {periodo} \\nSi tienes alguna duda, favor de reportarlo de inmediato a tu administrativo.\\n\\nCAFU Construcciones\"";
  private static final String BODY_CAJA_CHICA = "\"phone\":\"+521{celular}\",\"message\":\"Hola _{nombre}_,\\n\\n{saludo}, te hacemos llegar el reporte de caja chica de los *residentes* de la semana *{nomina}* del {periodo}, hacer clic en el siguiente enlace: https://cafu.jvmhost.net/Temporal/Pdf/{reporte}\\nSe tienen *24 hrs* para descargar el reporte de gastos de caja chica.\\n\\nCAFU Construcciones\"";
  private static final String BODY_OPEN_NOMINA= "\"group\":\"{celular}\",\"message\":\"Estimad@s _{nombre}_,\\n\\n{saludo}, en este momento se ha hecho corte de la n�mina *{nomina}* del {periodo}, con un total de *{reporte}* favor de verificar el registro de los destajos; se les hace saber tambien que a las *14:00 hrs* se har� el *corte de caja chica* para que de favor verifiquen el registro de sus gastos. Si se hace alg�n *ajuste* en los *destajos* a partir de este momento de algun *contratista* o *subcontratista* favor de *indicarlo* en este *chat* para reprocesar su n�mina.\\n\\nCAFU Construcciones\"";
  private static final String BODY_CLOSE_NOMINA= "\"group\":\"{celular}\",\"message\":\"Estimad@s _{nombre}_,\\n\\n{saludo}, en este momento se ha hecho *cierre* de la n�mina *{nomina}*; cualquier registro de destajos y de gasto de caja chica se vera reflejado para la siguiente n�mina � _semana_.\\n\\nCAFU Construcciones\"";
  private static final String PATH_REPORT     = "{numero}.- {documento}; https://ferreteriabonanza.com/Temporal/Pdf/{reporte}\\n";
  private static final int LENGTH_CELL_PHONE  = 10;

  private String token;
  private String nombre;
  private String celular;
  private String reporte;
  private String ticket;
  private String fecha;
  private Map<String, Object> contratistas;

  public Bonanza(String nombre, String celular) {
    this(nombre, celular, "", "", Collections.EMPTY_MAP);
  }
  
  public Bonanza(String nombre, String celular, String reporte, String ticket, String fecha) {
    this(nombre, celular, ticket, fecha, Collections.EMPTY_MAP);
    this.reporte= reporte;
  }

  public Bonanza(String ticket, String fecha, Map<String, Object> contratistas) {
    this("", "", ticket, fecha, contratistas);
  }
  
  public Bonanza(String nombre, String celular, String ticket, String fecha, Map<String, Object> contratistas) {
    this.nombre = Cadena.nombrePersona(nombre);
    this.celular= this.clean(celular);
    this.ticket = ticket;
    this.fecha= fecha;
    this.token  = System.getenv(IMOX_TOKEN);
    this.contratistas= contratistas;
    this.prepare();
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
    this.celular = celular;
  }

  public String getReporte() {
    return reporte;
  }

  public void setReporte(String reporte) {
    this.reporte = reporte;
  }

  public Map<String, Object> getContratistas() {
    return contratistas;
  }

  public void setContratistas(Map<String, Object> contratistas) {
    this.contratistas = contratistas;
  }

  @Override
  public String toString() {
    return "Message{" + "celular=" + celular + ", reporte=" + reporte + '}';
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

  public void doSendMessage() {
    this.doSendMessage(null);
  }

  public void doSendMessage(Session sesion) {
    if(Objects.equals(this.celular.length(), LENGTH_CELL_PHONE)) {
      Message message= null;
      Value value    = null; 
      Map<String, Object> params = new HashMap<>();        
      try {
        params.put("nombre", this.nombre);
        params.put("celular", this.celular);
        params.put("saludo", this.toSaludo());
        params.put("idTipoMensaje", ETypeMessage.BIENVENIDA.getId());
        if(sesion!= null)
          value= (Value)DaoFactory.getInstance().toField(sesion, "TcManticMensajesDto", "existe", params, "idKey");
        else
          value= (Value)DaoFactory.getInstance().toField("TcManticMensajesDto", "existe", params, "idKey");
        if(value== null) {
          if(!Objects.equals(Configuracion.getInstance().getEtapaServidor(), EEtapaServidor.PRODUCCION))
            LOG.warn(params.toString()+ " {"+ Cadena.replaceParams(BODY_MESSAGE, params, true)+ "}");
          else {  
            HttpResponse<String> response = Unirest.post("https://api.wassenger.com/v1/messages")
            .header("Content-Type", "application/json")
            .header("Token", this.token)
            .body("{"+ Cadena.replaceParams(BODY_MESSAGE, params, true)+ "}")
            .asString();
            if(Objects.equals(response.getStatus(), 201)) {
              LOG.warn("Enviado: "+ response.getBody());
              Gson gson= new Gson();
              message  = gson.fromJson(response.getBody(), Message.class);
              if(message!= null)
                message.init();
              else
                message= new Message();
            } // if  
            else {
              LOG.error("[doSendMessage] No se puedo enviar el mensaje por whatsup al celular ["+ this.celular+ "] "+ response.getStatusText()+ "\n"+ response.getBody());
              message= new Message();
              message.setMessage(" {"+ Cadena.replaceParams(BODY_MESSAGE, params, true)+ "}");
            } // else  
            message.setTelefono(this.celular);
            message.setIdSendStatus(new Long(response.getStatus()));
            message.setSendStatus(response.getStatusText());
            message.setIdTipoMensaje(ETypeMessage.BIENVENIDA.getId());
            message.setIdUsuario(JsfBase.getIdUsuario());
            if(sesion!= null)
              DaoFactory.getInstance().insert(sesion, message);
            else
              DaoFactory.getInstance().insert(message);
          } // else  
        } // if  
        else 
          LOG.warn("[doSendMessage] Ya hab�a sido notificado este celular por whatsup ["+ this.celular+ "]");
      } // try
      catch(Exception e) {
        Error.mensaje(e);
      } // catch
      finally {
        Methods.clean(params);
      } // finally
    } // if
    else 
      LOG.error("[doSendMessage] No se puedo enviar el mensaje por whatsup al celular ["+ this.celular+ "]");
  }

  public void doSendCorteNomina(Session sesion) {
    Message message= null;
    Map<String, Object> params = new HashMap<>();        
    try {
      params.put("nombre", this.nombre);
      params.put("celular", this.celular);
      params.put("reporte", this.reporte);
      params.put("ticket", this.ticket);
      params.put("fecha", this.fecha);
      params.put("saludo", this.toSaludo());
      if(!Objects.equals(Configuracion.getInstance().getEtapaServidor(), EEtapaServidor.PRODUCCION))
        LOG.warn(params.toString()+ " {"+ Cadena.replaceParams(BODY_OPEN_NOMINA, params, true)+ "}");
      else {  
        HttpResponse<String> response = Unirest.post("https://api.wassenger.com/v1/messages")
        .header("Content-Type", "application/json")
        .header("Token", this.token)
        .body("{"+ Cadena.replaceParams(BODY_OPEN_NOMINA, params, true)+ "}")
        .asString();
        if(Objects.equals(response.getStatus(), 201)) {
          LOG.warn("Enviado: "+ response.getBody());
          Gson gson= new Gson();
          message  = gson.fromJson(response.getBody(), Message.class);
          if(message!= null)
            message.init();
          else
            message= new Message();
        } // if  
        else {
          LOG.error("[doSendCorteNomina] No se puedo enviar el mensaje por whatsup al grupo ["+ this.celular+ "] "+ response.getStatusText()+ "\n"+ response.getBody());
          message= new Message();
          message.setMessage(" {"+ Cadena.replaceParams(BODY_OPEN_NOMINA, params, true)+ "}");
        } // else  
        message.setTelefono(this.celular);
        message.setIdSendStatus(new Long(response.getStatus()));
        message.setSendStatus(response.getStatusText());
        message.setIdTipoMensaje(ETypeMessage.RESIDENTE.getId());
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
  }
  
  public void doSendCierreNomina(Session sesion) {
    Message message= null;
    Map<String, Object> params = new HashMap<>();        
    try {
      params.put("nombre", this.nombre);
      params.put("celular", this.celular);
      params.put("reporte", this.reporte);
      params.put("ticket", this.ticket);
      params.put("fecha", this.fecha);
      params.put("saludo", this.toSaludo());
      if(!Objects.equals(Configuracion.getInstance().getEtapaServidor(), EEtapaServidor.PRODUCCION))
        LOG.warn(params.toString()+ " {"+ Cadena.replaceParams(BODY_CLOSE_NOMINA, params, true)+ "}");
      else {  
        HttpResponse<String> response = Unirest.post("https://api.wassenger.com/v1/messages")
        .header("Content-Type", "application/json")
        .header("Token", this.token)
        .body("{"+ Cadena.replaceParams(BODY_CLOSE_NOMINA, params, true)+ "}")
        .asString();
        if(Objects.equals(response.getStatus(), 201)) {
          LOG.warn("Enviado: "+ response.getBody());
          Gson gson= new Gson();
          message  = gson.fromJson(response.getBody(), Message.class);
          if(message!= null)
            message.init();
          else
            message= new Message();
        } // if  
        else {
          LOG.error("[doSendCirreNomina] No se puedo enviar el mensaje por whatsup al grupo ["+ this.celular+ "] "+ response.getStatusText()+ "\n"+ response.getBody());
          message= new Message();
          message.setMessage(" {"+ Cadena.replaceParams(BODY_CLOSE_NOMINA, params, true)+ "}");
        } // else  
        message.setTelefono(this.celular);
        message.setIdSendStatus(new Long(response.getStatus()));
        message.setSendStatus(response.getStatusText());
        message.setIdTipoMensaje(ETypeMessage.RESIDENTE.getId());
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
  }
  
  public void doSendFactura() {
    this.doSendFactura(null);
  }
  
  public static String toPathFiles(String pdf, String xml) {
    StringBuilder regresar= new StringBuilder("\\n(PDF) https://ferreteriabonanza.com/Temporal/Pdf/");
    regresar.append(pdf);
    regresar.append("\\n(XML) https://ferreteriabonanza.com/Temporal/Pdf/");
    regresar.append(xml);
    regresar.append("\\n");
    return regresar.toString();
  }
  
  public void doSendFactura(Session sesion) {
    if(Objects.equals(this.celular.length(), LENGTH_CELL_PHONE)) {
      Message message= null;
      Map<String, Object> params = new HashMap<>();        
      try {
        params.put("nombre", this.nombre);
        params.put("celular", this.celular);
        params.put("reporte", this.reporte);
        params.put("ticket", this.ticket);
        params.put("fecha", this.fecha);
        params.put("saludo", this.toSaludo());
        if(!Objects.equals(Configuracion.getInstance().getEtapaServidor(), EEtapaServidor.PRODUCCION))
          LOG.warn(params.toString()+ " {"+ Cadena.replaceParams(BODY_FACTURA, params, true)+ "}");
        else {  
          HttpResponse<String> response = Unirest.post("https://api.wassenger.com/v1/messages")
          .header("Content-Type", "application/json")
          .header("Token", this.token)
          .body("{"+ Cadena.replaceParams(BODY_FACTURA, params, true)+ "}")
          .asString();
          if(Objects.equals(response.getStatus(), 201)) {
            LOG.warn("Enviado: "+ response.getBody());
            Gson gson= new Gson();
            message= gson.fromJson(response.getBody(), Message.class);
            if(message!= null) 
              message.init();
            else {
              message= new Message();
              message.setMessage(" {"+ Cadena.replaceParams(BODY_FACTURA, params, true)+ "}");
            } // else  
          } // if  
          else {
            LOG.error("[doSendFactura] No se puedo enviar el mensaje por whatsup al celular ["+ this.celular+ "] "+ response.getStatusText()+ "\n"+ response.getBody());
            message= new Message();
            message.setMessage(" {"+ Cadena.replaceParams(BODY_FACTURA, params, true)+ "}");
          } // if  
          message.setTelefono(this.celular);
          message.setIdSendStatus(new Long(response.getStatus()));
          message.setSendStatus(response.getStatusText());
          message.setIdTipoMensaje(ETypeMessage.CONTRATISTA.getId());
          message.setIdUsuario(JsfBase.getAutentifica()!= null && JsfBase.getAutentifica().getPersona()!= null? JsfBase.getIdUsuario(): 2L);
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
      LOG.error("[doSendFactura]No se puedo enviar el mensaje por whatsup al celular ["+ this.celular+ "]");
  } // doSendFactura

  public void doSendResidentes(Session sesion) {
    if(Objects.equals(this.celular.length(), LENGTH_CELL_PHONE)) {
      Message message= null;
      Map<String, Object> params = new HashMap<>();        
      try {
        params.put("nombre", this.nombre);
        params.put("celular", this.celular);
        params.put("reporte", this.reporte);
        params.put("ticket", this.ticket);
        params.put("fecha", this.fecha);
        params.put("saludo", this.toSaludo());
        if(!Objects.equals(Configuracion.getInstance().getEtapaServidor(), EEtapaServidor.PRODUCCION))
          LOG.warn(params.toString()+ " {"+ Cadena.replaceParams(BODY_RESIDENTE, params, true)+ "}");
        else {  
          HttpResponse<String> response = Unirest.post("https://api.wassenger.com/v1/messages")
          .header("Content-Type", "application/json")
          .header("Token", this.token)
          .body("{"+ Cadena.replaceParams(BODY_RESIDENTE, params, true)+ "}")
          .asString();
          if(Objects.equals(response.getStatus(), 201)) {
            LOG.warn("Enviado: "+ response.getBody());
            Gson gson= new Gson();
            message= gson.fromJson(response.getBody(), Message.class);
            if(message!= null) 
              message.init();
            else {
              message= new Message();
              message.setMessage(" {"+ Cadena.replaceParams(BODY_RESIDENTE, params, true)+ "}");
            } // else  
          } // if  
          else {
            LOG.error("[doSendResidentes] No se puedo enviar el mensaje por whatsup al celular ["+ this.celular+ "] "+ response.getStatusText()+ "\n"+ response.getBody());
            message= new Message();
            message.setMessage(" {"+ Cadena.replaceParams(BODY_RESIDENTE, params, true)+ "}");
          } // if  
          message.setTelefono(this.celular);
          message.setIdSendStatus(new Long(response.getStatus()));
          message.setSendStatus(response.getStatusText());
          message.setIdTipoMensaje(ETypeMessage.RESIDENTE.getId());
          message.setIdUsuario(JsfBase.getIdUsuario());
          if(sesion!= null)
            DaoFactory.getInstance().insert(sesion, message);
          else
            DaoFactory.getInstance().insert(message);
        } // if  
      } // try
      catch(Exception e) {
        Error.mensaje(e);
      } // catch
      finally {
        Methods.clean(params);
      } // finally
    } // if
    else 
      LOG.error("[doSendResidentes] No se puedo enviar el mensaje por whatsup al celular ["+ this.celular+ "]");
  }

  public void doSendGasto(Session sesion) {
    if(Objects.equals(this.celular.length(), LENGTH_CELL_PHONE)) {
      Message message= null;
      Map<String, Object> params = new HashMap<>();        
      try {
        params.put("nombre", this.nombre);
        params.put("celular", this.celular);
        params.put("reporte", this.reporte);
        params.put("ticket", this.ticket);
        params.put("fecha", this.fecha);
        params.put("saludo", this.toSaludo());
        if(!Objects.equals(Configuracion.getInstance().getEtapaServidor(), EEtapaServidor.PRODUCCION))
          LOG.warn(params.toString()+ " {"+ Cadena.replaceParams(BODY_GASTO_CHICA, params, true)+ "}");
        else {  
          HttpResponse<String> response = Unirest.post("https://api.wassenger.com/v1/messages")
          .header("Content-Type", "application/json")
          .header("Token", this.token)
          .body("{"+ Cadena.replaceParams(BODY_GASTO_CHICA, params, true)+ "}")
          .asString();
          if(Objects.equals(response.getStatus(), 201)) {
            LOG.warn("Enviado: "+ response.getBody());
            Gson gson= new Gson();
            message= gson.fromJson(response.getBody(), Message.class);
            if(message!= null) 
              message.init();
            else {
              message= new Message();
              message.setMessage(" {"+ Cadena.replaceParams(BODY_GASTO_CHICA, params, true)+ "}");
            } // else  
          } // if  
          else {
            LOG.error("[doSendGasto] No se puedo enviar el mensaje por whatsup al celular ["+ this.celular+ "] "+ response.getStatusText()+ "\n"+ response.getBody());
            message= new Message();
            message.setMessage(" {"+ Cadena.replaceParams(BODY_GASTO_CHICA, params, true)+ "}");
          } // if  
          message.setTelefono(this.celular);
          message.setIdSendStatus(new Long(response.getStatus()));
          message.setSendStatus(response.getStatusText());
          message.setIdTipoMensaje(ETypeMessage.RESIDENTE.getId());
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
      LOG.error("[doSendGasto] No se puedo enviar el mensaje por whatsup al celular ["+ this.celular+ "]");
  }
  
  public void doSendCajaChica(Session sesion) {
    if(Objects.equals(this.celular.length(), LENGTH_CELL_PHONE)) {
      Message message= null;
      Map<String, Object> params = new HashMap<>();        
      try {
        params.put("nombre", this.nombre);
        params.put("celular", this.celular);
        params.put("reporte", this.reporte);
        params.put("ticket", this.ticket);
        params.put("fecha", this.fecha);
        params.put("saludo", this.toSaludo());
        if(!Objects.equals(Configuracion.getInstance().getEtapaServidor(), EEtapaServidor.PRODUCCION))
          LOG.warn(params.toString()+ " {"+ Cadena.replaceParams(BODY_CAJA_CHICA, params, true)+ "}");
        else {  
          HttpResponse<String> response = Unirest.post("https://api.wassenger.com/v1/messages")
          .header("Content-Type", "application/json")
          .header("Token", this.token)
          .body("{"+ Cadena.replaceParams(BODY_CAJA_CHICA, params, true)+ "}")
          .asString();
          if(Objects.equals(response.getStatus(), 201)) {
            LOG.warn("Enviado: "+ response.getBody());
            Gson gson= new Gson();
            message= gson.fromJson(response.getBody(), Message.class);
            if(message!= null) 
              message.init();
            else {
              message= new Message();
              message.setMessage(" {"+ Cadena.replaceParams(BODY_CAJA_CHICA, params, true)+ "}");
            } // else  
          } // if  
          else {
            LOG.error("[doSendCajaChica] No se puedo enviar el mensaje por whatsup al celular ["+ this.celular+ "] "+ response.getStatusText()+ "\n"+ response.getBody());
            message= new Message();
            message.setMessage(" {"+ Cadena.replaceParams(BODY_CAJA_CHICA, params, true)+ "}");
          } // if  
          message.setTelefono(this.celular);
          message.setIdSendStatus(new Long(response.getStatus()));
          message.setSendStatus(response.getStatusText());
          message.setIdTipoMensaje(ETypeMessage.ADMINISTRADOR.getId());
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
      LOG.error("[doSendCajaChica] No se puedo enviar el mensaje por whatsup al celular ["+ this.celular+ "]");
  }
  
  private void prepare() {
    StringBuilder archivos= new StringBuilder();
    if(this.contratistas!= null && !this.contratistas.isEmpty()) {
      Map<String, Object> params = null;
      try {        
        params= new HashMap<>();        
        int count= 1;
        for (String key: this.contratistas.keySet()) {
          params.put("numero", count++);
          params.put("contratista", key);
          params.put("reporte", this.contratistas.get(key));
          archivos.append(Cadena.replaceParams(PATH_REPORT, params, true));
        } // for
      } // try
      catch (Exception e) {
        Error.mensaje(e);
      } // catch	
      finally {
        Methods.clean(params);
      } // finally
      this.reporte= archivos.toString();
    } // if  
  }
  
  public void doSendDemo() {
    Map<String, Object> params = new HashMap<>();        
    try {
      params.put("nombre", this.nombre);
      params.put("celular", this.celular);
      params.put("reporte", this.reporte);
      params.put("ticket", this.ticket);
      params.put("fecha", this.fecha);
      params.put("saludo", this.toSaludo());
      LOG.info("{"+ Cadena.replaceParams(BODY_FACTURA, params, true)+ "}");
      LOG.info("{"+ Cadena.replaceParams(BODY_MESSAGE, params, true)+ "}");
      LOG.info("{"+ Cadena.replaceParams(BODY_RESIDENTE, params, true)+ "}");
    } // try
    catch(Exception e) {
      Error.mensaje(e);
    } // catch
    finally {
      Methods.clean(params);
    } // finally
  }
  
  private String toSaludo() {
    String regresar= null;
    Calendar calendar= Calendar.getInstance();
    int hour= calendar.get(Calendar.HOUR_OF_DAY);
    if(hour>= 5 && hour< 12)
      regresar= "Buenos d�as";
    else
      if(hour>= 12 && hour< 19)
        regresar= "Buenas tardes";
      else 
        regresar= "Buenas noches";
    return regresar;
  }

  public void doSendSaludo() {
    if(Objects.equals(this.celular.length(), LENGTH_CELL_PHONE)) {
      Message message= null;
      Map<String, Object> params = new HashMap<>();        
      try {
        params.put("nombre", this.nombre);
        params.put("celular", this.celular);
        params.put("saludo", this.toSaludo());
        params.put("idTipoMensaje", ETypeMessage.BIENVENIDA.getId());
        if(!Objects.equals(Configuracion.getInstance().getEtapaServidor(), EEtapaServidor.DESARROLLO))
          LOG.warn(params.toString()+ " {"+ Cadena.replaceParams(BODY_MESSAGE, params, true)+ "}");
        else {  
          HttpResponse<String> response = Unirest.post("https://api.wassenger.com/v1/messages")
          .header("Content-Type", "application/json")
          .header("Token", this.token)
          .body("{"+ Cadena.replaceParams(BODY_MESSAGE, params, true)+ "}")
          .asString();
          if(Objects.equals(response.getStatus(), 201)) {
            LOG.warn("Enviado: "+ response.getBody());
            Gson gson= new Gson();
            message  = gson.fromJson(response.getBody(), Message.class);
            if(message!= null)
              message.init();
            else
              message= new Message();
          } // if  
          else {
            LOG.error("[doSendSaludo] No se puedo enviar el mensaje por whatsup al celular ["+ this.celular+ "] "+ response.getStatusText()+ "\n"+ response.getBody());
            message= new Message();
            message.setMessage(" {"+ Cadena.replaceParams(BODY_MESSAGE, params, true)+ "}");
          } // else  
          message.setTelefono(this.celular);
          message.setIdSendStatus(new Long(response.getStatus()));
          message.setSendStatus(response.getStatusText());
          message.setIdTipoMensaje(ETypeMessage.BIENVENIDA.getId());
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
      LOG.error("[doSendSaludo] No se puedo enviar el mensaje por whatsup al celular ["+ this.celular+ "]");
  }
  
  public static void main(String ... args) {
//    String nombres[]  = {"Carlos Calderon Solano", "Juan Jos� Fuentes Ramirez Espa�a", "Christopher Castro Jim�nez"};
//    String celulares[]= {"4491813810", "4491152255", "4491087126"};
    Bonanza message= new Bonanza("Alejandro Jim�nez Garc�a", "449-209-05-86", "holix.pdf", "2021-20", "15/06/2021 al 30/06/2021");
    message.doSendSaludo();
//    for (int x= 0; x < nombres.length; x++) {
//      message.setNombre(nombres[x]);
//      message.setCelular(celulares[x]);
//      // message.doSendDemo();
//      message.doSendMessage();
//      // message.doSendDestajo();
//    } // for
//    String body= "{\"id\":\"60d607099faa17d7ac70f63d\",\"waId\":\"3EB06320796368264337\",\"phone\":\"+5214492090586\",\"wid\":\"5214492090586@c.us\",\"status\":\"queued\",\"deliveryStatus\":\"queued\",\"createdAt\":\"2021-06-25T16:40:41.225Z\",\"deliverAt\":\"2021-06-25T16:40:41.206Z\",\"message\":\"Hola _Alejandro jim�nez garc�a_,\nBuenos d�as, te hacemos llegar el reporte de los destajos de la n�mina *2021-20* del *15/06/2021 al 30/06/2021*, hacer clic en el siguiente enlace: https://cafu.jvmhost.net/Temporal/Pdf/CAFU_2021062410492325_orden_de_compra_detalle.pdf\nSi tienes alguna duda, favor de reportarlo de inmediato a tu residente; tienes *24 hrs* para descargar el reporte.\nCAFU Construcciones S.A.\",\"priority\":\"normal\",\"retentionPolicy\":\"plan_defaults\",\"retry\":{\"count\":0},\"webhookStatus\":\"pending\",\"media\":{\"format\":\"native\"},\"device\":\"60d4987e48b592aa82f09e92\"}";
//    Gson gson= new Gson();
//    Message message= gson.fromJson(body, Message.class);
//    if(message!= null)
//      message.init();
//    LOG.info(message);
//    Map<String, Object> params= new HashMap<>();
//    params.put("Alex J�menez", "uno.pdf");
//    params.put("Axel J�menez", "dos.pdf");
//    params.put("Yanely J�menez", "tres.pdf");
//    params.put("Yaretzy J�menez", "cuatro.pdf");
//    Cafu cafu= new Cafu("Alejandro Jimenez", "4492090586", "2021-23", "*21/06/2021* al *30/06/2021*", params);
//    LOG.info(cafu.getReporte());
//    cafu.doSendDemo();
  }  
  
}
