
package mx.org.kaana.kajool.seguridad.jarfile;

import java.io.InputStream;
import java.net.URL;

/**
 *@company Instituto Nacional de Estadistica y Geografia
 *@project KAJOOL (Control system polls)
 *@date 6/05/2014
 *@time 05:25:03 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class SearchFileJar {

  private static SearchFileJar instance;
  private static Object mutex;

  static {
    mutex=new Object();
  }

  private SearchFileJar() {
  }

  public static SearchFileJar getInstance() {
    synchronized (mutex) {
      if (instance==null) {
        instance=new SearchFileJar();
      } // if
    } // if
    return instance;
  }

  public InputStream toInputStream(String name) {
    ClassLoader loader = this.getClass().getClassLoader();
    return loader.getResourceAsStream(name);
  }

  public InputStream toInputStream(String proyecto, String name) throws Exception {
    InputStream regresar= null;
    String path         = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();					
    path                = path.substring(0, path.lastIndexOf("/")+ 1).concat(proyecto.toUpperCase().concat(".jar")); 								
    try {						
			URL url = new URL("jar:file:".concat(path).concat("!").concat("/").concat((name)));
      regresar= url.openStream();
    } // try
    catch(Exception e) {
      throw e;
    } // catch
    return regresar;
  }

}
