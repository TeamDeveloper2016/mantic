package mx.org.kaana.libs.wassenger;

import com.google.gson.Gson;
import com.itextpdf.xmp.impl.Base64;
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
import mx.org.kaana.libs.recurso.Configuracion;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 16/05/2022
 *@time 08:11:07 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public final class Rfc implements Serializable {

  private static final long serialVersionUID = -6510759858245467836L;
  private static final Log LOG = LogFactory.getLog(Rfc.class);
  
  private String rfc;
  private String token;
  private String api;
  private String user;
  private String pass;

  public Rfc() {
    this("");  
  }
  
  public Rfc(String rfc) {
    this.rfc= rfc;
    if(Configuracion.getInstance().isEtapaProduccion()) {
		  this.api= "api.facturama.mx";
      this.token= Base64.encode("FERRBONANZA:ZABONAN2018");
      this.user="FERRBONANZA";
      this.pass="ZABONAN2018";
    } // if
    else {
      this.api= "apisandbox.facturama.mx";
      this.token= Base64.encode("FERRBONANZASANDBOX:zabonan2018sandbox");
      this.user="FERRBONANZASANDBOX";
      this.pass="zabonan2018sandbox";
    } // else  
  }

  public String getRfc() {
    return rfc;
  }

  public void setRfc(String rfc) {
    this.rfc = rfc;
  }

  public Sat doValidate() {
    Sat regresar= null;
    try {
      HttpResponse<String> response = Unirest.get("https://".concat(this.api).concat("/customers/status?rfc=").concat(this.rfc))
      .basicAuth(this.user, this.pass)
      .asString();
      LOG.warn("Enviado: "+ response.getBody());
      if(Objects.equals(response.getStatus(), 200)) {
        Gson gson= new Gson();
        regresar  = gson.fromJson(response.getBody(), Sat.class);
        LOG.info("Correcto: ["+ regresar+ "] ");
      } // if  
      else 
        LOG.error("[doValidate] RFC ["+ this.rfc+ "] "+ response.getStatusText()+ "\n"+ response.getBody());
    } // try
    catch(Exception e) {
      Error.mensaje(e);
    } // catch
    return regresar;
  }
  
  public static void main(String ... args) throws Exception {
    Rfc rfc  = new Rfc();
    Sat token= null;
    int count= 0;
    List<Entity> correos= (List<Entity>)DaoFactory.getInstance().toEntitySet("TcManticClientesDto", "rfcs", Collections.EMPTY_MAP);
    for (Entity item : correos) {
      rfc.setRfc(item.toString("rfc"));
      token= rfc.doValidate();
      item.put("rfc", new Value("rfc", token.getRfc()));
      item.put("formato", new Value("formato", token.getFormatoCorrecto()));
      item.put("activo", new Value("activo", token.getActivo()));
      item.put("localizado", new Value("localizado", token.getLocalizado()));
      break;
    } // for
    count= 0;
    for (Entity item : correos) {
      LOG.info((count++)+ "|"+ item.toLong("idCliente")+ "|"+ item.toString("razonSocial")+ "|"+ item.toString("rfc")+ "|"+ item.toString("formato")+ "|"+ item.toString("activo")+ "|"+ item.toString("localizado"));
      break;
    } // for
  }  
  
}
