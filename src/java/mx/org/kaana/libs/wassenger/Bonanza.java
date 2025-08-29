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
 *@date 25/06/2021
 *@time 09:55:07 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public final class Bonanza implements Serializable {

  private static final long serialVersionUID = -6510759858245467836L;
  private static final Log LOG = LogFactory.getLog(Bonanza.class);
  
  private static final String IMOX_TOKEN       = "IMOX_TOKEN";
  public static final String IMOX_GROUP_MANTIC = "5214491813810-1598307650@g.us";
  public static final String IMOX_GROUP_KANAL  = "5214491813810-1598307650@g.us";
  public static final String IMOX_GROUP_TSAAK  = "5214491813810-1598307650@g.us";
  
  private static final String BODY_MESSAGE_MANTIC= "\"phone\":\"+521{celular}\",\"message\":\"Estimad@ _{nombre}_:\\n\\n{saludo}, te estaremos enviando únicamente las notificaciones más importantes respecto a compras con nosotros. Emisión y descarga de facturas principalmente.\\n\\nNo podremos contestar a tus mensajes en este número.\\n\\nSi desea contactarnos puedes ser a *ventas@{host}* y/o al telefono/whatsapp *{notifica}*\\n\\nPara aceptar estas notificaciones, puedes escribir *hola* en cualquier momento sobre este chat.\\n\\nGracias por comprar en *_{empresa}_*.\"";
  private static final String BODY_MESSAGE_KALAN = "\"phone\":\"+521{celular}\",\"message\":\"Estimad@ _{nombre}_:\\n\\n{saludo}, te estaremos enviando notificaciones respecto a nosotros. Sobre todo promociones y avisos de tus citas agendadas principalmente.\\n\\nNo podremos contestar a tus mensajes en este número.\\n\\nSi desea contactarnos puedes ser a *ventas@{host}* y/o al telefono/whatsapp *{notifica}*\\n\\nPara aceptar estas notificaciones, puedes escribir *hola* en cualquier momento sobre este chat.\\n\\nGracias por confiar en nosotros *_{empresa}_*.\"";
  private static final String BODY_MESSAGE_TSAAK = "\"phone\":\"+521{celular}\",\"message\":\"Estimad@ _{nombre}_:\\n\\n{saludo}, te estaremos enviando notificaciones respecto a nosotros. Sobre todo promociones y avisos de tus citas agendadas principalmente.\\n\\nNo podremos contestar a tus mensajes en este número.\\n\\nSi desea contactarnos puedes ser a *ventas@{host}* y/o al telefono/whatsapp *{notifica}*\\n\\nPara aceptar estas notificaciones, puedes escribir *hola* en cualquier momento sobre este chat.\\n\\nGracias por confiar en nosotros *_{empresa}_*.\"";

  private static final String BODY_MASIVO_MANTIC= "\"phone\":\"+521{celular}\",\"message\":\"Estimad@ _{nombre}_:\\n\\n{saludo}, {contenido}.\\n\\nGracias por comprar en *_{empresa}_*.\"";
  private static final String BODY_MASIVO_KALAN = "\"phone\":\"+521{celular}\",\"message\":\"Estimad@ _{nombre}_:\\n\\n{saludo}, {contenido}.\\n\\nGracias por confiar en nosotros *_{empresa}_*.\"";
  private static final String BODY_MASIVO_TSAAK = "\"phone\":\"+521{celular}\",\"message\":\"Estimad@ _{nombre}_:\\n\\n{saludo}, {contenido}.\\n\\nGracias por confiar en nosotros *_{empresa}_*.\"";
  
  private static final String BODY_PROVEEDOR   = "\"phone\":\"+521{celular}\",\"message\":\"Estimado proveedor _{nombre}_:\\n\\n{saludo}, te estaremos enviando únicamente las notificaciones más importantes respecto a las ordenes de compras que te haremos principalmente.\\n\\nNo podremos contestar a tus mensajes en este número.\\n\\nSi desea contactarnos puedes ser a *ventas@{host}* y/o al telefono/whatsapp *{notifica}*\\n\\nPara aceptar estas notificaciones, puedes escribir *hola* en cualquier momento sobre este chat.\\n\\nGracias por comprar en *_{empresa}_*.\"";
  
  private static final String BODY_FACTURA_MANTIC= "\"phone\":\"+521{celular}\",\"message\":\"Estimad@ _{nombre}_:\\n\\n{saludo}, te hacemos llegar la factura con folio *{ticket}* del día *{fecha}*, en el siguiente link se adjuntan sus archivos PDF y XML de su factura emitida\\n\\n{reporte}\\n\\nPara cualquier duda o aclaración *ventas@{host}* y/o al telefono/whatsapp *{notifica}*, se tienen *24 hrs* para descargar todos los documentos.\\n\\nAgradecemos su preferencia *_{empresa}_*.\"";
  private static final String BODY_FACTURA_KALAN = "\"phone\":\"+521{celular}\",\"message\":\"Estimad@ _{nombre}_:\\n\\n{saludo}, te hacemos llegar la factura con folio *{ticket}* del día *{fecha}*, en el siguiente link se adjuntan sus archivos PDF y XML de su factura emitida\\n\\n{reporte}\\n\\nPara cualquier duda o aclaración *ventas@{host}* y/o al telefono/whatsapp *{notifica}*, se tienen *24 hrs* para descargar todos los documentos.\\n\\nAgradecemos su preferencia *_{empresa}_*.\"";
  private static final String BODY_FACTURA_TSAAK = "\"phone\":\"+521{celular}\",\"message\":\"Estimad@ _{nombre}_:\\n\\n{saludo}, te hacemos llegar la factura con folio *{ticket}* del día *{fecha}*, en el siguiente link se adjuntan sus archivos PDF y XML de su factura emitida\\n\\n{reporte}\\n\\nPara cualquier duda o aclaración *ventas@{host}* y/o al telefono/whatsapp *{notifica}*, se tienen *24 hrs* para descargar todos los documentos.\\n\\nAgradecemos su preferencia *_{empresa}_*.\"";
  
  private static final String BODY_TICKET_MANTIC= "\"phone\":\"+521{celular}\",\"message\":\"Estimad@ _{nombre}_:\\n\\n{saludo}, te hacemos llegar el ticket con folio *{ticket}* del día *{fecha}*, en el siguiente link se adjuntan el archivo PDF del ticket\\n\\nhttps://{host}/Temporal/Pdf/{reporte}\\n\\nPara cualquier duda o aclaración *ventas@{host}* y/o al telefono/whatsapp *{notifica}*, se tienen *24 hrs* para descargar el documento.\\n\\nAgradecemos su preferencia *_{empresa}_*.\"";
  private static final String BODY_TICKET_KALAN = "\"phone\":\"+521{celular}\",\"message\":\"Estimad@ _{nombre}_:\\n\\n{saludo}, te hacemos llegar el ticket con folio *{ticket}* del día *{fecha}*, en el siguiente link se adjuntan el archivo PDF del ticket\\n\\nhttps://{host}/Temporal/Pdf/{reporte}\\n\\nPara cualquier duda o aclaración *ventas@{host}* y/o al telefono/whatsapp *{notifica}*, se tienen *24 hrs* para descargar el documento.\\n\\nAgradecemos su preferencia *_{empresa}_*.\"";
  private static final String BODY_TICKET_TSAAK = "\"phone\":\"+521{celular}\",\"message\":\"Estimad@ _{nombre}_:\\n\\n{saludo}, te hacemos llegar el ticket con folio *{ticket}* del día *{fecha}*, en el siguiente link se adjuntan el archivo PDF del ticket\\n\\nhttps://{host}/Temporal/Pdf/{reporte}\\n\\nPara cualquier duda o aclaración *ventas@{host}* y/o al telefono/whatsapp *{notifica}*, se tienen *24 hrs* para descargar el documento.\\n\\nAgradecemos su preferencia *_{empresa}_*.\"";
  
  private static final String BODY_DEVOLUCION  = "\"phone\":\"+521{celular}\",\"message\":\"Estimad@ _{nombre}_:\\n\\n{saludo}, su cuenta presenta un movimiento, en el siguiente link se adjuntan el archivo PDF referente a ello\\n\\nhttps://{host}/Temporal/Pdf/{reporte}\\n\\nPara cualquier duda o aclaración *ventas@{host}* y/o al telefono/whatsapp *{notifica}*, se tienen *24 hrs* para descargar todos los documentos.\\n\\nAgradecemos su preferencia *_{empresa}_*.\"";
  private static final String BODY_PAGO_CUENTA = "\"phone\":\"+521{celular}\",\"message\":\"Estimad@ _{nombre}_:\\n\\n{saludo}, gracias por su pago, en el siguiente link se adjunta un PDF con un resumen y estatus de los tickets/facturas a los cuales fue abonado el pago\\n\\n https://{host}/Temporal/Pdf/{reporte}\\n\\nPara cualquier duda o aclaración *ventas@{host}* y/o al telefono/whatsapp *{notifica}*, se tienen *24 hrs* para descargar todos los documentos.\\n\\nAgradecemos su preferencia *_{empresa}_*.\"";
  private static final String BODY_ORDEN_COMPRA= "\"phone\":\"+521{celular}\",\"message\":\"Estimado proveedor _{nombre}_:\\n\\n{saludo}, en el siguiente link se adjunta un PDF con una orden de compra\\n\\nhttps://{host}/Temporal/Pdf/{reporte}\\n\\nFavor de verificar en la misma orden la sucursal de entrega.\\n\\nPara cualquier duda o aclaración *ventas@{host}* y/o al telefono/whatsapp *{notifica}*.\\n\\n*_{empresa}_*.\"";
  private static final String BODY_CHECK_CORREO= "\"phone\":\"+521{celular}\",\"message\":\"Hola _{nombre}_,\\n\\n{saludo}, se te hace llegar la lista de correos de los clientes que fueron eliminados del servidor de *producción* por ser incorrectos o porque no son validos con corte al *{fecha}*\\n\\n{reporte}_{empresa}_\"";
  private static final String BODY_CHECK_RFC   = "\"phone\":\"+521{celular}\",\"message\":\"Hola _{nombre}_,\\n\\n{saludo}, se te hace llegar la lista de *RFC's* de los clientes que fueron eliminados del servidor de *producción* por estar con formato incorrecto, no estan activos o no estan dados de alta en el SAT, con corte al *{fecha}*\\n\\n{reporte}\\n\\n*_{empresa}_*\"";
  
  private static final String BODY_GASTO_CHICA = "\"phone\":\"+521{celular}\",\"message\":\"Hola _{nombre}_,\\n\\n{saludo}, te notificamos que los gastos a pagar por concepto de caja chica ascienden a {reporte} pesos de la semana *{nomina}* del {periodo} \\nSi tienes alguna duda, favor de reportarlo de inmediato a tu administrativo.\\n\\n*_{empresa}_*\"";
  private static final String BODY_CAJA_CHICA  = "\"phone\":\"+521{celular}\",\"message\":\"Hola _{nombre}_,\\n\\n{saludo}, te hacemos llegar el reporte de caja chica de los *residentes* de la semana *{nomina}* del {periodo}, hacer clic en el siguiente enlace: https://{host}/Temporal/Pdf/{reporte}\\nSe tienen *24 hrs* para descargar el reporte de gastos de caja chica.\\n\\n*_{empresa}_*\"";
  private static final String BODY_OPEN_NOMINA = "\"group\":\"{celular}\",\"message\":\"Estimad@s _{nombre}_,\\n\\n{saludo}, en este momento se ha hecho corte de la nómina *{nomina}* del {periodo}, con un total de *{reporte}* favor de verificar el registro de los destajos; se les hace saber tambien que a las *14:00 hrs* se hará el *corte de caja chica* para que de favor verifiquen el registro de sus gastos. Si se hace algún *ajuste* en los *destajos* a partir de este momento de algun *contratista* o *subcontratista* favor de *indicarlo* en este *chat* para reprocesar su nómina.\\n\\n*_{empresa}_*\"";
  private static final String BODY_CLOSE_NOMINA= "\"group\":\"{celular}\",\"message\":\"Estimad@s _{nombre}_,\\n\\n{saludo}, en este momento se ha hecho *cierre* de la nómina *{nomina}*; cualquier registro de destajos y de gasto de caja chica se vera reflejado para la siguiente nómina ó _semana_.\\n\\n_{empresa}_\"";

  private static final String BODY_CONTEOS        = "\"phone\":\"+521{celular}\",\"message\":\"Hola _{nombre}_,\\n\\n{saludo}, te notificamos que C. *{usuario}* ya envió el requerimiento para contabilizar *{reporte}* producto(s) o articulo(s) con numero de folio [*{ticket}*] y nombre [*{fecha}*], para tu conocimiento y revisión en la aplicación web.\\n\\n*_{empresa}_*\"";
  private static final String BODY_CONTEOS_DESTINO= "\"phone\":\"+521{celular}\",\"message\":\"Hola _{nombre}_,\\n\\n{saludo}, te notificamos que has recibido un requerimiento por parte de C. *{usuario}* para contabilizar *{reporte}* producto(s) o articulo(s) con numero de folio [*{ticket}*] y nombre [*{fecha}*], favor de ingresar a la aplicación móvil para atender este requerimiento.\\n\\n*_{empresa}_*\"";
  private static final String BODY_CONTEOS_FUENTE = "\"phone\":\"+521{celular}\",\"message\":\"Hola _{nombre}_,\\n\\n{saludo}, te notificamos que C. *{usuario}* fue notificado para atender el requerimiento para contabilizar *{reporte}* producto(s) o articulo(s) con numero de folio [*{ticket}*] y nombre [*{fecha}*], para tu conocimiento y seguimiento.\\n\\n*_{empresa}_*\"";

  private static final String BODY_SOLICITUD        = "\"phone\":\"+521{celular}\",\"message\":\"Hola _{nombre}_,\\n\\n{saludo}, te notificamos que C. *{usuario}* ya envió la solicitud de *{reporte}* producto(s) o articulo(s) con numero de folio [*{ticket}*], para tu conocimiento y revisión en la aplicación web.\\n\\n*_{empresa}_*\"";
  private static final String BODY_SOLICITUD_FUENTE = "\"phone\":\"+521{celular}\",\"message\":\"Hola _{nombre}_,\\n\\n{saludo}, te notificamos que fueron notificados para atender la solicitud de *{reporte}* producto(s) o articulo(s) con numero de folio [*{ticket}*], para tu conocimiento y seguimiento.\\n\\n*_{empresa}_*\"";
  private static final String BODY_SOLICITUD_DESTINO= "\"phone\":\"+521{celular}\",\"message\":\"Hola _{nombre}_,\\n\\n{saludo}, te notificamos que has recibido *una solictud* por parte de C. *{usuario}* para transferir *{reporte}* producto(s) o articulo(s) con numero de folio [*{ticket}*], favor de ingresar a la aplicación móvil para atender este requerimiento.\\n\\n*_{empresa}_*\"";
  
  private static final String PATH_REPORT      = "{numero}.- {documento}; https://{host}/Temporal/Pdf/{reporte}\\n";
  private static final int LENGTH_CELL_PHONE   = 10;

  private String token;
  private String nombre;
  private String celular;
  private String reporte;
  private String ticket;
  private String fecha;
  private Map<String, Object> contratistas;

  public Bonanza() {
    this("", "");
  }
  
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
    this.fecha  = fecha;
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
    this.setCelular(celular, Boolean.TRUE);
  }

  public void setCelular(String celular, Boolean clean) {
    this.celular = clean? this.clean(celular): celular;
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

  public void setTicket(String ticket) {
    this.ticket = ticket;
  }

  public void setFecha(String fecha) {
    this.fecha = fecha;
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
    if(Objects.equals(this.celular.length(), LENGTH_CELL_PHONE) || this.celular.contains("@")) {
      Message message= null;
      Value value    = null; 
      String mensaje = BODY_MESSAGE_MANTIC;
      Map<String, Object> params = new HashMap<>();   
      switch(Configuracion.getInstance().getPropiedad("sistema.empresa.principal")) {
        case "mantic":
          mensaje= BODY_MESSAGE_MANTIC;
          break;
        case "kalan":
          mensaje= BODY_MESSAGE_KALAN;
          break;
        case "tsaak":
          mensaje= BODY_MESSAGE_TSAAK;
          break;
      } // swtich
      try {
        params.put("nombre", this.nombre);
        params.put("celular", this.celular);
        params.put("empresa", Cadena.letraCapital(Configuracion.getInstance().getEmpresa("titulo")));
        params.put("host", Configuracion.getInstance().getEmpresa("host"));
        params.put("notifica", Configuracion.getInstance().getEmpresa("celular"));
        params.put("saludo", this.toSaludo());
        params.put("idTipoMensaje", ETypeMessage.BIENVENIDA.getId());
        if(sesion!= null)
          value= (Value)DaoFactory.getInstance().toField(sesion, "TcManticMensajesDto", "existe", params, "idKey");
        else
          value= (Value)DaoFactory.getInstance().toField("TcManticMensajesDto", "existe", params, "idKey");
        if(value== null) {
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
              LOG.error("[doSendMessage] No se envio el mensaje ["+ this.celular+ "] "+ response.getStatusText()+ "\n"+ response.getBody());
              message= new Message();
              message.setMessage(" {"+ Cadena.replaceParams(mensaje, params, true)+ "}");
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
          LOG.warn("[doSendMessage] Ya había sido notificado este celular por whatsapp ["+ this.celular+ "]");
      } // try
      catch(Exception e) {
        Error.mensaje(e);
      } // catch
      finally {
        Methods.clean(params);
      } // finally
    } // if
    else 
      LOG.error("[doSendMessage] No se envio el mensaje ["+ this.celular+ "]");
  }
  public void doSendMasivo(Session sesion, String contenido) {
    if(Objects.equals(this.celular.length(), LENGTH_CELL_PHONE) || this.celular.contains("@")) {
      Message message= null;
      Value value    = null; 
      String mensaje = BODY_MASIVO_MANTIC;
      Map<String, Object> params = new HashMap<>();   
      switch(Configuracion.getInstance().getPropiedad("sistema.empresa.principal")) {
        case "mantic":
          mensaje= BODY_MASIVO_MANTIC;
          break;
        case "kalan":
          mensaje= BODY_MASIVO_KALAN;
          break;
        case "tsaak":
          mensaje= BODY_MASIVO_TSAAK;
          break;
      } // swtich
      try {
        params.put("nombre", this.nombre);
        params.put("celular", this.celular);
        params.put("empresa", Cadena.letraCapital(Configuracion.getInstance().getEmpresa("titulo")));
        params.put("host", Configuracion.getInstance().getEmpresa("host"));
        params.put("notifica", Configuracion.getInstance().getEmpresa("celular"));
        params.put("contenido", contenido);
        params.put("saludo", this.toSaludo());
        params.put("idTipoMensaje", ETypeMessage.BIENVENIDA.getId());
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
            LOG.error("[doSendMessage] No se envio el mensaje ["+ this.celular+ "] "+ response.getStatusText()+ "\n"+ response.getBody());
            message= new Message();
            message.setMessage(" {"+ Cadena.replaceParams(mensaje, params, true)+ "}");
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
      } // try
      catch(Exception e) {
        Error.mensaje(e);
      } // catch
      finally {
        Methods.clean(params);
      } // finally
    } // if
    else 
      LOG.error("[doSendMessage] No se envio el mensaje ["+ this.celular+ "]");
  }

  public void doSendProveedor(Session sesion) {
    if(Objects.equals(this.celular.length(), LENGTH_CELL_PHONE) || this.celular.contains("@")) {
      Message message= null;
      Value value    = null; 
      Map<String, Object> params = new HashMap<>();        
      try {
        params.put("nombre", this.nombre);
        params.put("celular", this.celular);
        params.put("saludo", this.toSaludo());
        params.put("empresa", Cadena.letraCapital(Configuracion.getInstance().getEmpresa("titulo")));
        params.put("host", Configuracion.getInstance().getEmpresa("host"));
        params.put("notifica", Configuracion.getInstance().getEmpresa("celular"));
        params.put("idTipoMensaje", ETypeMessage.BIENVENIDA.getId());
        if(sesion!= null)
          value= (Value)DaoFactory.getInstance().toField(sesion, "TcManticMensajesDto", "existe", params, "idKey");
        else
          value= (Value)DaoFactory.getInstance().toField("TcManticMensajesDto", "existe", params, "idKey");
        if(value== null) {
          if(!Configuracion.getInstance().isEtapaProduccion())
            LOG.warn(params.toString()+ " {"+ Cadena.replaceParams(BODY_PROVEEDOR, params, true)+ "}");
          else {  
            HttpResponse<String> response = Unirest.post("https://api.wassenger.com/v1/messages")
            .header("Content-Type", "application/json")
            .header("Token", this.token)
            .body("{"+ Cadena.replaceParams(BODY_PROVEEDOR, params, true)+ "}")
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
              LOG.error("[doSendProveedor] No se envio el mensaje ["+ this.celular+ "] "+ response.getStatusText()+ "\n"+ response.getBody());
              message= new Message();
              message.setMessage(" {"+ Cadena.replaceParams(BODY_PROVEEDOR, params, true)+ "}");
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
          LOG.warn("[doSendProveedor] Ya había sido notificado este celular por whatsapp ["+ this.celular+ "]");
      } // try
      catch(Exception e) {
        Error.mensaje(e);
      } // catch
      finally {
        Methods.clean(params);
      } // finally
    } // if
    else 
      LOG.error("[doSendProveedor] No se envio el mensaje ["+ this.celular+ "]");
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
      params.put("empresa", Cadena.letraCapital(Configuracion.getInstance().getEmpresa("titulo")));      
      params.put("host", Configuracion.getInstance().getEmpresa("host"));
      params.put("notifica", Configuracion.getInstance().getEmpresa("celular"));
      params.put("saludo", this.toSaludo());
      if(!Configuracion.getInstance().isEtapaProduccion())
        LOG.warn(params.toString()+ " {"+ Cadena.replaceParams(BODY_OPEN_NOMINA, params, true)+ "}");
      else {  
        HttpResponse<String> response = Unirest.post("https://api.wassenger.com/v1/messages")
        .header("Content-Type", "application/json")
        .header("Token", this.token)
        .body("{"+ Cadena.replaceParams(BODY_OPEN_NOMINA, params, true)+ "}")
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
          LOG.error("[doSendCorteNomina] No se puedo enviar el mensaje por whatsapp al grupo ["+ this.celular+ "] "+ response.getStatusText()+ "\n"+ response.getBody());
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
      params.put("empresa", Cadena.letraCapital(Configuracion.getInstance().getEmpresa("titulo")));      
      params.put("host", Configuracion.getInstance().getEmpresa("host"));
      params.put("notifica", Configuracion.getInstance().getEmpresa("celular"));
      params.put("saludo", this.toSaludo());
      if(!Configuracion.getInstance().isEtapaProduccion())
        LOG.warn(params.toString()+ " {"+ Cadena.replaceParams(BODY_CLOSE_NOMINA, params, true)+ "}");
      else {  
        HttpResponse<String> response = Unirest.post("https://api.wassenger.com/v1/messages")
        .header("Content-Type", "application/json")
        .header("Token", this.token)
        .body("{"+ Cadena.replaceParams(BODY_CLOSE_NOMINA, params, true)+ "}")
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
          LOG.error("[doSendCirreNomina] No se puedo enviar el mensaje por whatsapp al grupo ["+ this.celular+ "] "+ response.getStatusText()+ "\n"+ response.getBody());
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
    StringBuilder regresar= new StringBuilder("(PDF) https://".concat(Configuracion.getInstance().getEmpresa("host")).concat("/Temporal/Pdf/"));
    regresar.append(pdf);
    regresar.append("\\n(XML) https://").append(Configuracion.getInstance().getEmpresa("host")).append("/Temporal/Pdf/");
    regresar.append(xml);
    return regresar.toString();
  }
  
  public void doSendFactura(Session sesion) {
    if(Objects.equals(this.celular.length(), LENGTH_CELL_PHONE) || this.celular.contains("@")) {
      Message message= null;
      String mensaje = BODY_FACTURA_MANTIC;
      Map<String, Object> params = new HashMap<>();        
      try {
        params.put("nombre", this.nombre);
        params.put("celular", this.celular);
        params.put("reporte", this.reporte);
        params.put("ticket", this.ticket);
        params.put("fecha", this.fecha);
        params.put("empresa", Cadena.letraCapital(Configuracion.getInstance().getEmpresa("titulo")));        
        params.put("host", Configuracion.getInstance().getEmpresa("host"));
        params.put("notifica", Configuracion.getInstance().getEmpresa("celular"));
        params.put("saludo", this.toSaludo());
        switch(Configuracion.getInstance().getPropiedad("sistema.empresa.principal")) {
          case "mantic":
            mensaje= BODY_FACTURA_MANTIC;
            break;
          case "kalan":
            mensaje= BODY_FACTURA_KALAN;
            break;
          case "tsaak":
            mensaje= BODY_FACTURA_TSAAK;
            break;
        } // swtich
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
            message= gson.fromJson(response.getBody(), Message.class);
            if(message!= null) 
              message.init();
            else {
              message= new Message();
              message.setMessage(" {"+ Cadena.replaceParams(mensaje, params, true)+ "}");
            } // else  
          } // if  
          else {
            LOG.error("[doSendFactura] No se envio el mensaje ["+ this.celular+ "] "+ response.getStatusText()+ "\n"+ response.getBody());
            message= new Message();
            message.setMessage(" {"+ Cadena.replaceParams(mensaje, params, true)+ "}");
          } // if  
          message.setTelefono(this.celular);
          message.setIdSendStatus(new Long(response.getStatus()));
          message.setSendStatus(response.getStatusText());
          message.setIdTipoMensaje(ETypeMessage.CONTRATISTA.getId());
          message.setIdUsuario(JsfBase.getFacesContext()!= null && JsfBase.getRequest()!= null && JsfBase.getAutentifica()!= null && JsfBase.getAutentifica().getPersona()!= null? JsfBase.getIdUsuario(): 2L);
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
      LOG.error("[doSendFactura]No se envio el mensaje ["+ this.celular+ "]");
  } // doSendFactura
  
  public void doSendTicket() {
    this.doSendTicket(null);
  }
  
  public void doSendTicket(Session sesion) {
    if(Objects.equals(this.celular.length(), LENGTH_CELL_PHONE) || this.celular.contains("@")) {
      Message message= null;
      String mensaje = null;
      Map<String, Object> params = new HashMap<>();        
      try {
        params.put("nombre", this.nombre);
        params.put("celular", this.celular);
        params.put("reporte", this.reporte);
        params.put("ticket", this.ticket);
        params.put("fecha", this.fecha);
        params.put("empresa", Cadena.letraCapital(Configuracion.getInstance().getEmpresa("titulo")));        
        params.put("host", Configuracion.getInstance().getEmpresa("host"));
        params.put("notifica", Configuracion.getInstance().getEmpresa("celular"));
        params.put("saludo", this.toSaludo());
        switch(Configuracion.getInstance().getPropiedad("sistema.empresa.principal")) {
          case "mantic":
            mensaje= BODY_TICKET_MANTIC;
            break;
          case "kalan":
            mensaje= BODY_TICKET_KALAN;
            break;
          case "tsaak":
            mensaje= BODY_TICKET_TSAAK;
            break;
        } // swtich
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
            message= gson.fromJson(response.getBody(), Message.class);
            if(message!= null) 
              message.init();
            else {
              message= new Message();
              message.setMessage(" {"+ Cadena.replaceParams(mensaje, params, true)+ "}");
            } // else  
          } // if  
          else {
            LOG.error("[doSendFactura] No se envio el mensaje ["+ this.celular+ "] "+ response.getStatusText()+ "\n"+ response.getBody());
            message= new Message();
            message.setMessage(" {"+ Cadena.replaceParams(mensaje, params, true)+ "}");
          } // if  
          message.setTelefono(this.celular);
          message.setIdSendStatus(new Long(response.getStatus()));
          message.setSendStatus(response.getStatusText());
          message.setIdTipoMensaje(ETypeMessage.CLIENTE.getId());
          message.setIdUsuario(JsfBase.getFacesContext()!= null && JsfBase.getRequest()!= null && JsfBase.getAutentifica()!= null && JsfBase.getAutentifica().getPersona()!= null? JsfBase.getIdUsuario(): 2L);
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
      LOG.error("[doSendFactura]No se envio el mensaje ["+ this.celular+ "]");
  } // doSendTicket
  
  public void doSendDevolucion(Session sesion) {
    if(Objects.equals(this.celular.length(), LENGTH_CELL_PHONE) || this.celular.contains("@")) {
      Message message= null;
      Map<String, Object> params = new HashMap<>();        
      try {
        params.put("nombre", this.nombre);
        params.put("celular", this.celular);
        params.put("reporte", this.reporte);
        params.put("ticket", this.ticket);
        params.put("fecha", this.fecha);
        params.put("empresa", Cadena.letraCapital(Configuracion.getInstance().getEmpresa("titulo")));        
        params.put("host", Configuracion.getInstance().getEmpresa("host"));
        params.put("notifica", Configuracion.getInstance().getEmpresa("celular"));
        params.put("saludo", this.toSaludo());
        if(!Configuracion.getInstance().isEtapaProduccion())
          LOG.warn(params.toString()+ " {"+ Cadena.replaceParams(BODY_DEVOLUCION, params, true)+ "}");
        else {  
          HttpResponse<String> response = Unirest.post("https://api.wassenger.com/v1/messages")
          .header("Content-Type", "application/json")
          .header("Token", this.token)
          .body("{"+ Cadena.replaceParams(BODY_DEVOLUCION, params, true)+ "}")
          .asString();
          LOG.error("Enviado: "+ response.getBody());
          if(Objects.equals(response.getStatus(), 201)) {
            Gson gson= new Gson();
            message= gson.fromJson(response.getBody(), Message.class);
            if(message!= null) 
              message.init();
            else {
              message= new Message();
              message.setMessage(" {"+ Cadena.replaceParams(BODY_DEVOLUCION, params, true)+ "}");
            } // else  
          } // if  
          else {
            LOG.error("[doSendDevolucion] No se envio el mensaje ["+ this.celular+ "] "+ response.getStatusText()+ "\n"+ response.getBody());
            message= new Message();
            message.setMessage(" {"+ Cadena.replaceParams(BODY_DEVOLUCION, params, true)+ "}");
          } // if  
          message.setTelefono(this.celular);
          message.setIdSendStatus(new Long(response.getStatus()));
          message.setSendStatus(response.getStatusText());
          message.setIdTipoMensaje(ETypeMessage.CLIENTE.getId());
          message.setIdUsuario(JsfBase.getFacesContext()!= null && JsfBase.getRequest()!= null && JsfBase.getAutentifica()!= null && JsfBase.getAutentifica().getPersona()!= null? JsfBase.getIdUsuario(): 2L);
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
      LOG.error("[doSendDevolucion]No se envio el mensaje ["+ this.celular+ "]");
  } // doSendDevolucion
  
  public void doSendPagoCuenta(Session sesion) {
    if(Objects.equals(this.celular.length(), LENGTH_CELL_PHONE) || this.celular.contains("@")) {
      Message message= null;
      Map<String, Object> params = new HashMap<>();        
      try {
        params.put("nombre", this.nombre);
        params.put("celular", this.celular);
        params.put("reporte", this.reporte);
        params.put("ticket", this.ticket);
        params.put("fecha", this.fecha);
        params.put("empresa", Cadena.letraCapital(Configuracion.getInstance().getEmpresa("titulo")));        
        params.put("host", Configuracion.getInstance().getEmpresa("host"));
        params.put("notifica", Configuracion.getInstance().getEmpresa("celular"));
        params.put("saludo", this.toSaludo());
        if(!Configuracion.getInstance().isEtapaProduccion())
          LOG.warn(params.toString()+ " {"+ Cadena.replaceParams(BODY_PAGO_CUENTA, params, true)+ "}");
        else {  
          HttpResponse<String> response = Unirest.post("https://api.wassenger.com/v1/messages")
          .header("Content-Type", "application/json")
          .header("Token", this.token)
          .body("{"+ Cadena.replaceParams(BODY_PAGO_CUENTA, params, true)+ "}")
          .asString();
          LOG.error("Enviado: "+ response.getBody());
          if(Objects.equals(response.getStatus(), 201)) {
            Gson gson= new Gson();
            message= gson.fromJson(response.getBody(), Message.class);
            if(message!= null) 
              message.init();
            else {
              message= new Message();
              message.setMessage(" {"+ Cadena.replaceParams(BODY_PAGO_CUENTA, params, true)+ "}");
            } // else  
          } // if  
          else {
            LOG.error("[doSendPagoCuenta] No se envio el mensaje ["+ this.celular+ "] "+ response.getStatusText()+ "\n"+ response.getBody());
            message= new Message();
            message.setMessage(" {"+ Cadena.replaceParams(BODY_PAGO_CUENTA, params, true)+ "}");
          } // if  
          message.setTelefono(this.celular);
          message.setIdSendStatus(new Long(response.getStatus()));
          message.setSendStatus(response.getStatusText());
          message.setIdTipoMensaje(ETypeMessage.CLIENTE.getId());
          message.setIdUsuario(JsfBase.getFacesContext()!= null && JsfBase.getRequest()!= null && JsfBase.getAutentifica()!= null && JsfBase.getAutentifica().getPersona()!= null? JsfBase.getIdUsuario(): 2L);
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
      LOG.error("[doSendPagoCuenta]No se envio el mensaje ["+ this.celular+ "]");
  } // doSendPagoCuenta
  
  public void doSendOrdenCompra() {
    this.doSendOrdenCompra(null);
  }
  
  public void doSendOrdenCompra(Session sesion) {
    if(Objects.equals(this.celular.length(), LENGTH_CELL_PHONE) || this.celular.contains("@")) {
      Message message= null;
      Map<String, Object> params = new HashMap<>();        
      try {
        params.put("nombre", this.nombre);
        params.put("celular", this.celular);
        params.put("reporte", this.reporte);
        params.put("ticket", this.ticket);
        params.put("fecha", this.fecha);
        params.put("empresa", Cadena.letraCapital(Configuracion.getInstance().getEmpresa("titulo")));        
        params.put("host", Configuracion.getInstance().getEmpresa("host"));
        params.put("notifica", Configuracion.getInstance().getEmpresa("celular"));
        params.put("saludo", this.toSaludo());
        if(!Configuracion.getInstance().isEtapaProduccion())
          LOG.warn(params.toString()+ " {"+ Cadena.replaceParams(BODY_ORDEN_COMPRA, params, true)+ "}");
        else {  
          HttpResponse<String> response = Unirest.post("https://api.wassenger.com/v1/messages")
          .header("Content-Type", "application/json")
          .header("Token", this.token)
          .body("{"+ Cadena.replaceParams(BODY_ORDEN_COMPRA, params, true)+ "}")
          .asString();
          LOG.error("Enviado: "+ response.getBody());
          if(Objects.equals(response.getStatus(), 201)) {
            Gson gson= new Gson();
            message= gson.fromJson(response.getBody(), Message.class);
            if(message!= null) 
              message.init();
            else {
              message= new Message();
              message.setMessage(" {"+ Cadena.replaceParams(BODY_ORDEN_COMPRA, params, true)+ "}");
            } // else  
          } // if  
          else {
            LOG.error("[doSendOrdenCompra] No se envio el mensaje ["+ this.celular+ "] "+ response.getStatusText()+ "\n"+ response.getBody());
            message= new Message();
            message.setMessage(" {"+ Cadena.replaceParams(BODY_ORDEN_COMPRA, params, true)+ "}");
          } // if  
          message.setTelefono(this.celular);
          message.setIdSendStatus(new Long(response.getStatus()));
          message.setSendStatus(response.getStatusText());
          message.setIdTipoMensaje(ETypeMessage.CLIENTE.getId());
          message.setIdUsuario(JsfBase.getFacesContext()!= null && JsfBase.getRequest()!= null && JsfBase.getAutentifica()!= null && JsfBase.getAutentifica().getPersona()!= null? JsfBase.getIdUsuario(): 2L);
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
      LOG.error("[doSendOrdenCompra]No se envio el mensaje ["+ this.celular+ "]");
  } // doSendOrdenCompra

  public void doSendGasto(Session sesion) {
    if(Objects.equals(this.celular.length(), LENGTH_CELL_PHONE) || this.celular.contains("@")) {
      Message message= null;
      Map<String, Object> params = new HashMap<>();        
      try {
        params.put("nombre", this.nombre);
        params.put("celular", this.celular);
        params.put("reporte", this.reporte);
        params.put("ticket", this.ticket);
        params.put("fecha", this.fecha);
        params.put("empresa", Cadena.letraCapital(Configuracion.getInstance().getEmpresa("titulo")));        
        params.put("host", Configuracion.getInstance().getEmpresa("host"));
        params.put("notifica", Configuracion.getInstance().getEmpresa("celular"));
        params.put("saludo", this.toSaludo());
        if(!Configuracion.getInstance().isEtapaProduccion())
          LOG.warn(params.toString()+ " {"+ Cadena.replaceParams(BODY_GASTO_CHICA, params, true)+ "}");
        else {  
          HttpResponse<String> response = Unirest.post("https://api.wassenger.com/v1/messages")
          .header("Content-Type", "application/json")
          .header("Token", this.token)
          .body("{"+ Cadena.replaceParams(BODY_GASTO_CHICA, params, true)+ "}")
          .asString();
          LOG.error("Enviado: "+ response.getBody());
          if(Objects.equals(response.getStatus(), 201)) {
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
            LOG.error("[doSendGasto] No se envio el mensaje ["+ this.celular+ "] "+ response.getStatusText()+ "\n"+ response.getBody());
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
      LOG.error("[doSendGasto] No se envio el mensaje ["+ this.celular+ "]");
  }
  
  public void doSendCajaChica(Session sesion) {
    if(Objects.equals(this.celular.length(), LENGTH_CELL_PHONE) || this.celular.contains("@")) {
      Message message= null;
      Map<String, Object> params = new HashMap<>();        
      try {
        params.put("nombre", this.nombre);
        params.put("celular", this.celular);
        params.put("reporte", this.reporte);
        params.put("ticket", this.ticket);
        params.put("fecha", this.fecha);
        params.put("empresa", Cadena.letraCapital(Configuracion.getInstance().getEmpresa("titulo")));        
        params.put("host", Configuracion.getInstance().getEmpresa("host"));
        params.put("notifica", Configuracion.getInstance().getEmpresa("celular"));
        params.put("saludo", this.toSaludo());
        if(!Configuracion.getInstance().isEtapaProduccion())
          LOG.warn(params.toString()+ " {"+ Cadena.replaceParams(BODY_CAJA_CHICA, params, true)+ "}");
        else {  
          HttpResponse<String> response = Unirest.post("https://api.wassenger.com/v1/messages")
          .header("Content-Type", "application/json")
          .header("Token", this.token)
          .body("{"+ Cadena.replaceParams(BODY_CAJA_CHICA, params, true)+ "}")
          .asString();
          LOG.error("Enviado: "+ response.getBody());
          if(Objects.equals(response.getStatus(), 201)) {
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
            LOG.error("[doSendCajaChica] No se envio el mensaje ["+ this.celular+ "] "+ response.getStatusText()+ "\n"+ response.getBody());
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
      LOG.error("[doSendCajaChica] No se envio el mensaje ["+ this.celular+ "]");
  }
  
  public void doSendCorreo() {
    this.doSendCorreo(null);
  }
  
  public void doSendCorreo(Session sesion) {
    if(Objects.equals(this.celular.length(), LENGTH_CELL_PHONE) || this.celular.contains("@")) {
      Message message= null;
      Map<String, Object> params = new HashMap<>();        
      try {
        params.put("nombre", this.nombre);
        params.put("celular", this.celular);
        params.put("reporte", this.reporte);
        params.put("ticket", this.ticket);
        params.put("fecha", this.fecha);
        params.put("empresa", Cadena.letraCapital(Configuracion.getInstance().getEmpresa("titulo")));        
        params.put("host", Configuracion.getInstance().getEmpresa("host"));
        params.put("notifica", Configuracion.getInstance().getEmpresa("celular"));
        params.put("saludo", this.toSaludo());
        if(!Configuracion.getInstance().isEtapaProduccion())
          LOG.warn(params.toString()+ " {"+ Cadena.replaceParams(BODY_CHECK_CORREO, params, true)+ "}");
        else {  
          HttpResponse<String> response = Unirest.post("https://api.wassenger.com/v1/messages")
          .header("Content-Type", "application/json")
          .header("Token", this.token)
          .body("{"+ Cadena.replaceParams(BODY_CHECK_CORREO, params, true)+ "}")
          .asString();
          LOG.error("Enviado: "+ response.getBody());
          if(Objects.equals(response.getStatus(), 201)) {
            Gson gson= new Gson();
            message= gson.fromJson(response.getBody(), Message.class);
            if(message!= null) 
              message.init();
            else {
              message= new Message();
              message.setMessage(" {"+ Cadena.replaceParams(BODY_CHECK_CORREO, params, true)+ "}");
            } // else  
          } // if  
          else {
            LOG.error("[doSendCorreo] No se envio el mensaje ["+ this.celular+ "] "+ response.getStatusText()+ "\n"+ response.getBody());
            message= new Message();
            message.setMessage(" {"+ Cadena.replaceParams(BODY_CHECK_CORREO, params, true)+ "}");
          } // if  
          message.setTelefono(this.celular);
          message.setIdSendStatus(new Long(response.getStatus()));
          message.setSendStatus(response.getStatusText());
          message.setIdTipoMensaje(ETypeMessage.ADMINISTRADOR.getId());
          message.setIdUsuario(JsfBase.getFacesContext()!= null && JsfBase.getRequest()!= null && JsfBase.getAutentifica()!= null && JsfBase.getAutentifica().getPersona()!= null? JsfBase.getIdUsuario(): 2L);
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
      LOG.error("[doSendCorreo]No se envio el mensaje ["+ this.celular+ "]");
  } // doSendCorreo
  
  public void doSendRfc() {
    this.doSendRfc(null);
  }
  
  public void doSendRfc(Session sesion) {
    if(Objects.equals(this.celular.length(), LENGTH_CELL_PHONE) || this.celular.contains("@")) {
      Message message= null;
      Map<String, Object> params = new HashMap<>();        
      try {
        params.put("nombre", this.nombre);
        params.put("celular", this.celular);
        params.put("reporte", this.reporte);
        params.put("ticket", this.ticket);
        params.put("fecha", this.fecha);
        params.put("empresa", Cadena.letraCapital(Configuracion.getInstance().getEmpresa("titulo")));        
        params.put("host", Configuracion.getInstance().getEmpresa("host"));
        params.put("notifica", Configuracion.getInstance().getEmpresa("celular"));
        params.put("saludo", this.toSaludo());
        if(!Configuracion.getInstance().isEtapaProduccion())
          LOG.warn(params.toString()+ " {"+ Cadena.replaceParams(BODY_CHECK_RFC, params, true)+ "}");
        else {  
          HttpResponse<String> response = Unirest.post("https://api.wassenger.com/v1/messages")
          .header("Content-Type", "application/json")
          .header("Token", this.token)
          .body("{"+ Cadena.replaceParams(BODY_CHECK_RFC, params, true)+ "}")
          .asString();
          LOG.error("Enviado: "+ response.getBody());
          if(Objects.equals(response.getStatus(), 201)) {
            Gson gson= new Gson();
            message= gson.fromJson(response.getBody(), Message.class);
            if(message!= null) 
              message.init();
            else {
              message= new Message();
              message.setMessage(" {"+ Cadena.replaceParams(BODY_CHECK_RFC, params, true)+ "}");
            } // else  
          } // if  
          else {
            LOG.error("[doSendRfc] No se envio el mensaje ["+ this.celular+ "] "+ response.getStatusText()+ "\n"+ response.getBody());
            message= new Message();
            message.setMessage(" {"+ Cadena.replaceParams(BODY_CHECK_RFC, params, true)+ "}");
          } // if  
          message.setTelefono(this.celular);
          message.setIdSendStatus(new Long(response.getStatus()));
          message.setSendStatus(response.getStatusText());
          message.setIdTipoMensaje(ETypeMessage.ADMINISTRADOR.getId());
          message.setIdUsuario(JsfBase.getFacesContext()!= null && JsfBase.getRequest()!= null && JsfBase.getAutentifica()!= null && JsfBase.getAutentifica().getPersona()!= null? JsfBase.getIdUsuario(): 2L);
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
      LOG.error("[doSendRfc]No se envio el mensaje ["+ this.celular+ "]");
  } // doSendRfc
  
  private void prepare() {
    StringBuilder archivos= new StringBuilder();
    if(this.contratistas!= null && !this.contratistas.isEmpty()) {
      Map<String, Object> params = new HashMap<>();
      try {        
        int count= 1;
        for (String key: this.contratistas.keySet()) {
          params.put("numero", count++);
          params.put("contratista", key);
          params.put("reporte", this.contratistas.get(key));
          params.put("host", Configuracion.getInstance().getEmpresa("host"));
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
    Map<String, Object> params= new HashMap<>();        
    try {
      params.put("nombre", this.nombre);
      params.put("celular", this.celular);
      params.put("reporte", this.reporte);
      params.put("ticket", this.ticket);
      params.put("fecha", this.fecha);
      params.put("empresa", Cadena.letraCapital(Configuracion.getInstance().getEmpresa("titulo")));        
      params.put("host", Configuracion.getInstance().getEmpresa("host"));
      params.put("notifica", Configuracion.getInstance().getEmpresa("celular"));
      params.put("saludo", this.toSaludo());
      switch(Configuracion.getInstance().getPropiedad("sistema.empresa.principal")) {
        case "mantic":
          LOG.info("{"+ Cadena.replaceParams(BODY_FACTURA_MANTIC, params, true)+ "}");
          break;
        case "kalan":
          LOG.info("{"+ Cadena.replaceParams(BODY_FACTURA_KALAN, params, true)+ "}");
          break;
        case "tsaak":
          LOG.info("{"+ Cadena.replaceParams(BODY_FACTURA_TSAAK, params, true)+ "}");
          break;
      } // swtich
      switch(Configuracion.getInstance().getPropiedad("sistema.empresa.principal")) {
        case "mantic":
          LOG.info("{"+ Cadena.replaceParams(BODY_MESSAGE_MANTIC, params, true)+ "}");
          break;
        case "kalan":
          LOG.info("{"+ Cadena.replaceParams(BODY_MESSAGE_KALAN, params, true)+ "}");
          break;
        case "tsaak":
          LOG.info("{"+ Cadena.replaceParams(BODY_MESSAGE_TSAAK, params, true)+ "}");
          break;
      } // swtich
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
      regresar= "Buenos días";
    else
      if(hour>= 12 && hour< 19)
        regresar= "Buenas tardes";
      else 
        regresar= "Buenas noches";
    return regresar;
  }

  public void doSendConteo(Session sesion, String usuario) {
    if(Objects.equals(this.celular.length(), LENGTH_CELL_PHONE) || this.celular.contains("@")) {
      Message message           = null;
      Map<String, Object> params= new HashMap<>();        
      try {
        params.put("nombre", this.nombre);
        params.put("celular", this.celular);
        params.put("reporte", this.reporte);
        params.put("ticket", this.ticket);
        params.put("fecha", this.fecha);
        params.put("usuario", usuario);
        params.put("empresa", Cadena.letraCapital(Configuracion.getInstance().getEmpresa("titulo")));        
        params.put("host", Configuracion.getInstance().getEmpresa("host"));
        params.put("notifica", Configuracion.getInstance().getEmpresa("celular"));
        params.put("saludo", this.toSaludo());
        if(!Configuracion.getInstance().isEtapaProduccion())
          LOG.warn(params.toString()+ " {"+ Cadena.replaceParams(BODY_CONTEOS, params, true)+ "}");
        else {  
          HttpResponse<String> response = Unirest.post("https://api.wassenger.com/v1/messages")
          .header("Content-Type", "application/json")
          .header("Token", this.token)
          .body("{"+ Cadena.replaceParams(BODY_CONTEOS, params, true)+ "}")
          .asString();
          LOG.error("Enviado: "+ response.getBody());
          if(Objects.equals(response.getStatus(), 201)) {
            Gson gson= new Gson();
            message= gson.fromJson(response.getBody(), Message.class);
            if(message!= null) 
              message.init();
            else {
              message= new Message();
              message.setMessage(" {"+ Cadena.replaceParams(BODY_CONTEOS, params, true)+ "}");
            } // else  
          } // if  
          else {
            LOG.error("[doSendConteo] No se envio el mensaje ["+ this.celular+ "] "+ response.getStatusText()+ "\n"+ response.getBody());
            message= new Message();
            message.setMessage(" {"+ Cadena.replaceParams(BODY_CONTEOS, params, true)+ "}");
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
      LOG.error("[doSendConteo] No se envio el mensaje ["+ this.celular+ "]");
  }
  
  public void doSendConteoDestino(Session sesion, String usuario) {
    if(Objects.equals(this.celular.length(), LENGTH_CELL_PHONE) || this.celular.contains("@")) {
      Message message           = null;
      Map<String, Object> params= new HashMap<>();        
      try {
        params.put("nombre", this.nombre);
        params.put("celular", this.celular);
        params.put("reporte", this.reporte);
        params.put("ticket", this.ticket);
        params.put("fecha", this.fecha);
        params.put("usuario", usuario);
        params.put("empresa", Cadena.letraCapital(Configuracion.getInstance().getEmpresa("titulo")));        
        params.put("host", Configuracion.getInstance().getEmpresa("host"));
        params.put("notifica", Configuracion.getInstance().getEmpresa("celular"));
        params.put("saludo", this.toSaludo());
        if(!Configuracion.getInstance().isEtapaProduccion())
          LOG.warn(params.toString()+ " {"+ Cadena.replaceParams(BODY_CONTEOS_DESTINO, params, true)+ "}");
        else {  
          HttpResponse<String> response = Unirest.post("https://api.wassenger.com/v1/messages")
          .header("Content-Type", "application/json")
          .header("Token", this.token)
          .body("{"+ Cadena.replaceParams(BODY_CONTEOS_DESTINO, params, true)+ "}")
          .asString();
          LOG.error("Enviado: "+ response.getBody());
          if(Objects.equals(response.getStatus(), 201)) {
            Gson gson= new Gson();
            message= gson.fromJson(response.getBody(), Message.class);
            if(message!= null) 
              message.init();
            else {
              message= new Message();
              message.setMessage(" {"+ Cadena.replaceParams(BODY_CONTEOS_DESTINO, params, true)+ "}");
            } // else  
          } // if  
          else {
            LOG.error("[doSendConteoDestino] No se envio el mensaje ["+ this.celular+ "] "+ response.getStatusText()+ "\n"+ response.getBody());
            message= new Message();
            message.setMessage(" {"+ Cadena.replaceParams(BODY_CONTEOS_DESTINO, params, true)+ "}");
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
      LOG.error("[doSendConteoDestino] No se envio el mensaje ["+ this.celular+ "]");
  }
  
  public void doSendConteoFuente(Session sesion, String usuario) {
    if(Objects.equals(this.celular.length(), LENGTH_CELL_PHONE) || this.celular.contains("@")) {
      Message message           = null;
      Map<String, Object> params= new HashMap<>();        
      try {
        params.put("nombre", this.nombre);
        params.put("celular", this.celular);
        params.put("reporte", this.reporte);
        params.put("ticket", this.ticket);
        params.put("fecha", this.fecha);
        params.put("usuario", usuario);
        params.put("empresa", Cadena.letraCapital(Configuracion.getInstance().getEmpresa("titulo")));        
        params.put("host", Configuracion.getInstance().getEmpresa("host"));
        params.put("notifica", Configuracion.getInstance().getEmpresa("celular"));
        params.put("saludo", this.toSaludo());
        if(!Configuracion.getInstance().isEtapaProduccion())
          LOG.warn(params.toString()+ " {"+ Cadena.replaceParams(BODY_CONTEOS_FUENTE, params, true)+ "}");
        else {  
          HttpResponse<String> response = Unirest.post("https://api.wassenger.com/v1/messages")
          .header("Content-Type", "application/json")
          .header("Token", this.token)
          .body("{"+ Cadena.replaceParams(BODY_CONTEOS_FUENTE, params, true)+ "}")
          .asString();
          LOG.error("Enviado: "+ response.getBody());
          if(Objects.equals(response.getStatus(), 201)) {
            Gson gson= new Gson();
            message= gson.fromJson(response.getBody(), Message.class);
            if(message!= null) 
              message.init();
            else {
              message= new Message();
              message.setMessage(" {"+ Cadena.replaceParams(BODY_CONTEOS_FUENTE, params, true)+ "}");
            } // else  
          } // if  
          else {
            LOG.error("[doSendConteoFuente] No se envio el mensaje ["+ this.celular+ "] "+ response.getStatusText()+ "\n"+ response.getBody());
            message= new Message();
            message.setMessage(" {"+ Cadena.replaceParams(BODY_CONTEOS_FUENTE, params, true)+ "}");
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
      LOG.error("[doSendConteoFuente] No se envio el mensaje ["+ this.celular+ "]");
  }

  public void doSendSolicitud(Session sesion, String usuario) {
    if(Objects.equals(this.celular.length(), LENGTH_CELL_PHONE) || this.celular.contains("@")) {
      Message message           = null;
      Map<String, Object> params= new HashMap<>();        
      try {
        params.put("nombre", this.nombre);
        params.put("celular", this.celular);
        params.put("reporte", this.reporte);
        params.put("ticket", this.ticket);
        params.put("fecha", this.fecha);
        params.put("usuario", usuario);
        params.put("empresa", Cadena.letraCapital(Configuracion.getInstance().getEmpresa("titulo")));        
        params.put("host", Configuracion.getInstance().getEmpresa("host"));
        params.put("notifica", Configuracion.getInstance().getEmpresa("celular"));
        params.put("saludo", this.toSaludo());
        if(!Configuracion.getInstance().isEtapaProduccion())
          LOG.warn(params.toString()+ " {"+ Cadena.replaceParams(BODY_SOLICITUD, params, true)+ "}");
        else {  
          HttpResponse<String> response = Unirest.post("https://api.wassenger.com/v1/messages")
          .header("Content-Type", "application/json")
          .header("Token", this.token)
          .body("{"+ Cadena.replaceParams(BODY_CONTEOS, params, true)+ "}")
          .asString();
          LOG.error("Enviado: "+ response.getBody());
          if(Objects.equals(response.getStatus(), 201)) {
            Gson gson= new Gson();
            message= gson.fromJson(response.getBody(), Message.class);
            if(message!= null) 
              message.init();
            else {
              message= new Message();
              message.setMessage(" {"+ Cadena.replaceParams(BODY_SOLICITUD, params, true)+ "}");
            } // else  
          } // if  
          else {
            LOG.error("[doSendSolicitud] No se envio el mensaje ["+ this.celular+ "] "+ response.getStatusText()+ "\n"+ response.getBody());
            message= new Message();
            message.setMessage(" {"+ Cadena.replaceParams(BODY_SOLICITUD, params, true)+ "}");
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
      LOG.error("[doSendSolicitud] No se envio el mensaje ["+ this.celular+ "]");
  }
  
  public void doSendSolicitudFuente(Session sesion) {
    if(Objects.equals(this.celular.length(), LENGTH_CELL_PHONE) || this.celular.contains("@")) {
      Message message           = null;
      Map<String, Object> params= new HashMap<>();        
      try {
        params.put("nombre", this.nombre);
        params.put("celular", this.celular);
        params.put("reporte", this.reporte);
        params.put("ticket", this.ticket);
        params.put("fecha", this.fecha);
        params.put("empresa", Cadena.letraCapital(Configuracion.getInstance().getEmpresa("titulo")));        
        params.put("host", Configuracion.getInstance().getEmpresa("host"));
        params.put("notifica", Configuracion.getInstance().getEmpresa("celular"));
        params.put("saludo", this.toSaludo());
        if(!Configuracion.getInstance().isEtapaProduccion())
          LOG.warn(params.toString()+ " {"+ Cadena.replaceParams(BODY_SOLICITUD_FUENTE, params, true)+ "}");
        else {  
          HttpResponse<String> response = Unirest.post("https://api.wassenger.com/v1/messages")
          .header("Content-Type", "application/json")
          .header("Token", this.token)
          .body("{"+ Cadena.replaceParams(BODY_SOLICITUD_FUENTE, params, true)+ "}")
          .asString();
          LOG.error("Enviado: "+ response.getBody());
          if(Objects.equals(response.getStatus(), 201)) {
            Gson gson= new Gson();
            message= gson.fromJson(response.getBody(), Message.class);
            if(message!= null) 
              message.init();
            else {
              message= new Message();
              message.setMessage(" {"+ Cadena.replaceParams(BODY_SOLICITUD_FUENTE, params, true)+ "}");
            } // else  
          } // if  
          else {
            LOG.error("[doSendSolicitudFuente] No se envio el mensaje ["+ this.celular+ "] "+ response.getStatusText()+ "\n"+ response.getBody());
            message= new Message();
            message.setMessage(" {"+ Cadena.replaceParams(BODY_CONTEOS_FUENTE, params, true)+ "}");
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
      LOG.error("[doSendSolicitudFuente] No se envio el mensaje ["+ this.celular+ "]");
  }

  public void doSendSolicitudDestino(Session sesion, String usuario) {
    if(Objects.equals(this.celular.length(), LENGTH_CELL_PHONE) || this.celular.contains("@")) {
      Message message           = null;
      Map<String, Object> params= new HashMap<>();        
      try {
        params.put("nombre", this.nombre);
        params.put("celular", this.celular);
        params.put("reporte", this.reporte);
        params.put("ticket", this.ticket);
        params.put("fecha", this.fecha);
        params.put("usuario", usuario);
        params.put("empresa", Cadena.letraCapital(Configuracion.getInstance().getEmpresa("titulo")));        
        params.put("host", Configuracion.getInstance().getEmpresa("host"));
        params.put("notifica", Configuracion.getInstance().getEmpresa("celular"));
        params.put("saludo", this.toSaludo());
        if(!Configuracion.getInstance().isEtapaProduccion())
          LOG.warn(params.toString()+ " {"+ Cadena.replaceParams(BODY_SOLICITUD_DESTINO, params, true)+ "}");
        else {  
          HttpResponse<String> response = Unirest.post("https://api.wassenger.com/v1/messages")
          .header("Content-Type", "application/json")
          .header("Token", this.token)
          .body("{"+ Cadena.replaceParams(BODY_SOLICITUD_FUENTE, params, true)+ "}")
          .asString();
          LOG.error("Enviado: "+ response.getBody());
          if(Objects.equals(response.getStatus(), 201)) {
            Gson gson= new Gson();
            message= gson.fromJson(response.getBody(), Message.class);
            if(message!= null) 
              message.init();
            else {
              message= new Message();
              message.setMessage(" {"+ Cadena.replaceParams(BODY_SOLICITUD_DESTINO, params, true)+ "}");
            } // else  
          } // if  
          else {
            LOG.error("[doSendSolicitudDestino] No se envio el mensaje ["+ this.celular+ "] "+ response.getStatusText()+ "\n"+ response.getBody());
            message= new Message();
            message.setMessage(" {"+ Cadena.replaceParams(BODY_SOLICITUD_DESTINO, params, true)+ "}");
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
      LOG.error("[doSendSolicitudDestino] No se envio el mensaje ["+ this.celular+ "]");
  }

  public void doSendSaludo() {
    String mensaje= BODY_MESSAGE_MANTIC;
    if(Objects.equals(this.celular.length(), LENGTH_CELL_PHONE) || this.celular.contains("@")) {
      Message message           = null;
      Map<String, Object> params= new HashMap<>();        
      try {
        params.put("nombre", this.nombre);
        params.put("celular", this.celular);
        params.put("empresa", Cadena.letraCapital(Configuracion.getInstance().getEmpresa("titulo")));        
        params.put("host", Configuracion.getInstance().getEmpresa("host"));
        params.put("notifica", Configuracion.getInstance().getEmpresa("celular"));
        params.put("saludo", this.toSaludo());
        params.put("idTipoMensaje", ETypeMessage.BIENVENIDA.getId());
        switch(Configuracion.getInstance().getPropiedad("sistema.empresa.principal")) {
          case "mantic":
            mensaje= BODY_MESSAGE_MANTIC;
            break;
          case "kalan":
            mensaje= BODY_MESSAGE_KALAN;
            break;
          case "tsaak":
            mensaje= BODY_MESSAGE_TSAAK;
            break;
        } // swtich
        if(!Objects.equals(Configuracion.getInstance().getEtapaServidor(), EEtapaServidor.DESARROLLO))
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
            LOG.error("[doSendSaludo] No se envio el mensaje ["+ this.celular+ "] "+ response.getStatusText()+ "\n"+ response.getBody());
            message= new Message();
            message.setMessage(" {"+ Cadena.replaceParams(mensaje, params, true)+ "}");
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
      LOG.error("[doSendSaludo] No se envio el mensaje ["+ this.celular+ "]");
  }
  
  public static void main(String ... args) {
    //Bonanza message= new Bonanza("Alejandro Jiménez García", "449-209-05-86", "holix.pdf", "2021-20", "15/06/2021 al 30/06/2021");
    //message.doSendSaludo();
    Map<String, Object> actores= new HashMap<>();
    Encriptar encriptar        = new Encriptar();    
    actores.put("Alejandro Jiménez García", encriptar.desencriptar("cd4b3e3924191b057b8187"));
    actores.put("Daniel Davalos Gutiérrez", encriptar.desencriptar("443124130375ec53c7c5cd"));
    actores.put("Sandy Martínez Montoya", encriptar.desencriptar("2b160b0a71ea69d54a4cb4"));
    Bonanza notificar= new Bonanza();
    for (String item: actores.keySet()) {
      notificar.setNombre(Cadena.nombrePersona(item));
      notificar.setCelular((String)actores.get(item));
      notificar.setReporte("Hola");
      notificar.setFecha(Fecha.formatear(Fecha.DIA_FECHA_HORA_CORTA));
      LOG.info("Enviando mensaje de whatsapp al celular: "+ notificar.getCelular());
      notificar.doSendRfc();
    } // for
  }  
  
}
