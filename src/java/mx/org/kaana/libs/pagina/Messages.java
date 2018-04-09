package mx.org.kaana.libs.pagina;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.init.Settings;
import mx.org.kaana.xml.Modulos;
import mx.org.kaana.xml.Modulos.Paths;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 12-feb-2014
 * @time 16:15:50
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public class Messages implements Serializable {

  private static final long serialVersionUID = -3710559783673469814L;
  private static final Log LOG = LogFactory.getLog(Messages.class);
  private static Map<String, Message> messages;

  private static Messages instance;
  private static Object mutex;

  static {
    messages = new HashMap<>();
  }

  static {
    mutex = new Object();
  }

  private Messages() {
    init();
    load();
  }

  public void reload() {
    try {
      instance = new Messages();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
  }

  public static Messages getInstance() {
    if (instance == null) {
      synchronized (mutex) {
        if (instance == null) {
          instance = new Messages();
        }
      } // synchronized
    } // if
    return instance;
  }

  private void load() {
    for (String key : this.messages.keySet()) {
      Message message = this.messages.get(key);
      InputStream is = null;
      try {
        is = this.getClass().getResourceAsStream(message.getResource().concat(key).concat(".properties"));
        if (is != null) {
          message.getProperties().load(is);
        } else {
          String path = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
          path = path.substring(0, path.lastIndexOf("/") + 1).concat(key.toUpperCase().concat(".jar"));
          try {
            URL url = new URL("jar:file:".concat(path).concat("!").concat((message.getResource().concat("/").concat(key).concat(".properties")).replace(File.separator, "/")));
            if (is != null) {
              is = url.openStream();
              message.getProperties().load(is);
            } else {
              LOG.warn("No se encontro el archivo de propiedades para los mensajes [".concat(key).concat("]"));
            }
          } // try
          catch (Exception e) {
            LOG.warn("No se encontró el archivo de propiedades para los mensajes [".concat(key).concat("]") + e);
          } // catch
        } // else
      } // try
      catch (Exception e) {
        Error.mensaje(e, "Estar seguro que esta en el CLASSPATH ".concat(message.toString()).concat(".properties"));
      } // catch
    } // for
  }

  private void init() {
    List<String> atributos = null;
    Modulos modulos = null;
    try {
      atributos = new ArrayList<>();
      modulos = new Modulos(Configuracion.getInstance().toFileModule());
      modulos.load(atributos, "", Paths.MESSAGES);
      for (String addMessage : atributos) {
        String[] attrs = addMessage.split(Constantes.TILDE);
        if (attrs.length > 1) {
          if (!attrs[1].equals("")) {
            this.messages.put(attrs[0], new Message(attrs[0], attrs[1], new Properties()));
          }
        } // if
      } // for
      Message message = new Message(Constantes.NOMBRE_DE_APLICACION.toLowerCase(), Settings.getInstance().toDefaultPath(), new Properties());
      // kajool default kajool.properties
      message.getProperties().load(this.getClass().getResourceAsStream(Settings.getInstance().getCustomMessages()));
      this.messages.put(Constantes.NOMBRE_DE_APLICACION.toLowerCase(), message);
      LOG.info("Se cargo el archivo default de mensajes de ".concat(Constantes.NOMBRE_DE_APLICACION));
    } // try
    catch (Exception e) {
      LOG.warn("Error al cargar el archivo de modulos ".concat(Configuracion.getInstance().toFileModule()));
    } // catch		
  }

  public String getPropiedad(String proyecto, String key) {
    String regresar = null;
    if (this.messages.containsKey(proyecto.toLowerCase())) {
      regresar = this.messages.get(proyecto).getProperties().getProperty(key);
    } else {
      throw new KajoolBaseException("No existe el mensaje [".concat(key).concat("] en el proyecto [").concat(proyecto).concat("]"));
    }
    return regresar;
  }

  public String getPropiedad(String key) {
    return getPropiedad(Constantes.NOMBRE_DE_APLICACION, key);
  }

  @Override
  protected void finalize() throws Throwable {
    super.finalize();
    //Methods.clean(this.messages);
  }

}
