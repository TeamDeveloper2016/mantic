package mx.org.kaana.kajool.test.curl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.reflection.Methods;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 29/11/2022
 *@time 03:21:44 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public class Multifuncionales {

  private static final Log LOG = LogFactory.getLog(Multifuncionales.class);
  
  private static final String COCKIE  = "C:/Software/Curl-7_86_0_2/bin/curl --connect-timeout 188.496 --retry 2 -d \"ggt_textbox(10002)={user}&ggt_textbox(10003)={password}&ggt_select(10004)=0&action=loginbtn&ggt_hidden(10008)=0&ordinate=0&token2=860E16FEEDFF90A193CC60847A66971023C05AFC5E90F5EA647BCF5569B013794616C597ABDB9733\" http://{ip}/login.html -c {path}{output}.txt";
  private static final String DOWNLOAD= "C:/Software/Curl-7_86_0_2/bin/curl --connect-timeout 188.496 --retry 2 -L -b {path}{output}.txt \"http://{ip}/joblog_download.html?format=0^&order=1^&selectItem=1101111111101111111111111111111111111111111101111111111111111111^&date=0^&delAfterSave=0\" -H \"Accept-Encoding: gzip, deflate\" -H \"Accept-Language: es-419,es;q=0.8\" -H \"Upgrade-Insecure-Requests: 1\" -H \"User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36\" -H \"Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8\" -H \"Referer: http://{ip}/sysmgt_joblog_save.html\" -H \"Connection: keep-alive\" -H \"Cache-Control: max-age=0\" --compressed -o {path}{data}.csv";
  
  private List<Entity> dispositivos;

  public Multifuncionales() {
    this.init();
  }
 
  private void init() {
    Map<String, Object> params = new HashMap<>();
    try {      
      params.put("id", "");      
      //this.dispositivos= (List<Entity>)DaoFactory.getInstance().toEntity("", "", params);
      this.dispositivos= new ArrayList<>();
      Entity item= new Entity(1L);
      item.put("usuario", new Value("usuario", "admin"));
      item.put("contrasenia", new Value("contrasenia", "admin"));
      item.put("ip", new Value("ip", "10.15.154.106"));
      item.put("serie", new Value("serie", "MX-3571_25103176"));
      this.dispositivos.add(item);
      item= new Entity(2L);
      item.put("usuario", new Value("usuario", "admin"));
      item.put("contrasenia", new Value("contrasenia", "admin"));
      item.put("ip", new Value("ip", "10.7.7.220"));
      item.put("serie", new Value("serie", "MX-3571_25104906"));
      this.dispositivos.add(item);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);      
    } // catch	
    finally {
      Methods.clean(params);
    } // finally
  }
  
  public void process() {
    Map<String, Object> params = new HashMap<>();
    try {      
      String path= "d:/temporal/multi/cgor/".concat(Fecha.getHoyEstandar());
      File file= new File(path);
      if(!file.exists()) 
        file.mkdir();
      path= path.concat("/");
      for (Entity item : this.dispositivos) {
        params.put("user", item.toString("usuario"));      
        params.put("password", item.toString("contrasenia"));      
        params.put("ip", item.toString("ip"));      
        params.put("output", "COCKIE-".concat(item.toString("serie")));      
        params.put("path", path);      
        String command = Cadena.replaceParams(this.COCKIE, params, true);
        LOG.info("CREANDO COCKIE PARA ".concat(item.toString("serie")));
        Process process= Runtime.getRuntime().exec(command);
    		int processComplete = process.waitFor();
    		/*NOTE: processComplete=0 if correctly executed, will contain other values if not*/
    		if (processComplete== 0) {        
          params.put("data", "MX-".concat(item.toString("serie")));      
          command= Cadena.replaceParams(this.DOWNLOAD, params, true);
          LOG.info("DESCARGANDO ARCHIVO DE LOG ".concat(item.toString("serie")));
          process= Runtime.getRuntime().exec(command);
      		processComplete = process.waitFor();
      		if (processComplete== 0) {        
             // REALIZAR EL INSERT EN LA TABLA DE BITACORA
             LOG.info("SE GENERO DE FORMA CORRECTA ".concat(item.toString("serie")));
          } // if
          else {
            // REALIZAR EL INSERT EN LA TABLA DE BITACORA
            // NO SE GENERO EL ARCHIVO DEL LOG
            LOG.error("NO SE GENERO EL ARCHIVO DEL LOG");
          } // else  
        } // if
        else {
          // REALIZAR EL INSERT EN LA TABLA DE BITACORA
          // NO SE GENERO LA COCKIE PARA EL MULTIFUNCIONAL
          LOG.error("NO SE GENERO LA COCKIE PARA EL MULTIFUNCIONAL");
        } // else
      } // for
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);      
    } // catch	
    finally {
      Methods.clean(params);
    } // finally
  }
  
  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    Multifuncionales multifuncionales= new  Multifuncionales();
    multifuncionales.process();
  }

}
