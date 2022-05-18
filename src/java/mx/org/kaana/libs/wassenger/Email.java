package mx.org.kaana.libs.wassenger;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import mx.org.kaana.libs.formato.Error;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.libs.formato.Cadena;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 16/05/2022
 *@time 08:11:07 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public final class Email implements Serializable {

  private static final long serialVersionUID = -6510759858245467836L;
  private static final Log LOG = LogFactory.getLog(Email.class);
  
  private String nombre;
  private String correo;

  public Email() {
    this("", "");  
  }
  
  public Email(String nombre, String correo) {
    this.nombre= Cadena.nombrePersona(nombre);
    this.correo= correo;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getCorreo() {
    return correo;
  }

  public void setCorreo(String correo) {
    this.correo = correo;
  }

  public Token doValidate() {
    Token regresar= null;
    try {
      HttpResponse<String> response = Unirest.post("https://garridodiaz.com/emailvalidator/index.php?email=".concat(this.correo))
      .header("Content-Type", "application/json")
      .body("{}")
      .asString();
      if(Objects.equals(response.getStatus(), 200)) {
        LOG.warn("Enviado: "+ response.getBody());
        Gson gson= new Gson();
        regresar  = gson.fromJson(response.getBody(), Token.class);
        LOG.info("Correcto: ["+ regresar+ "] ");
      } // if  
      else 
        LOG.error("[doValidate] Correo ["+ this.correo+ "] "+ response.getStatusText()+ "\n"+ response.getBody());
    } // try
    catch(Exception e) {
      Error.mensaje(e);
    } // catch
    return regresar;
  }
  
  public static void main(String ... args) throws Exception {
    Email email= new Email();
    Token token= null;
    int count  = 0;
    List<Entity> correos= (List<Entity>)DaoFactory.getInstance().toEntitySet("TcManticClientesDto", "correos", Collections.EMPTY_MAP);
    for (Entity item : correos) {
      email.setNombre(item.toString("razonSocial"));
      email.setCorreo(item.toString("valor"));
      token= email.doValidate();
      item.put("valido", new Value("valido", token.getValid()));
      item.put("mensaje", new Value("mensaje", token.getMessage()));
    } // for
    count= 0;
    for (Entity item : correos) {
      if(!item.toBoolean("valido"))
        LOG.info((count++)+ "|"+ item.toLong("idClienteTipoContacto")+ "|"+ item.toString("razonSocial")+ "|"+ item.toString("valor")+"|"+ item.toString("mensaje"));
    } // for
  }  
  
}
