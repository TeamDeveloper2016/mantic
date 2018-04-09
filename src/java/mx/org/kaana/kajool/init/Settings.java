
package mx.org.kaana.kajool.init;

import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Error;

/**
 *@company Instituto Nacional de Estadistica y Geografia
 *@project KAJOOL (Control system polls)
 *@date 21/05/2015
 *@time 07:09:25 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Settings {

  private final String JANAL_DEFAULT_PATH   = "/mx/org/kaana/kajool/init/";
  private final String JANAL_PATH_DB        = "/mx/org/kaana/kajool/db/cfg";
  private final String JANAL_MODULES        = "/mx/org/kaana/kajool/db/cfg/modulos.xml";
  private final String HIBERNATE_MAPPING    = "hibernate-mapping.xml";
  private final String HIBERNATE_DEVELOPMENT= "hibernate-desarrollo.xml";
  private final String HIBERNATE_DINAMIC    = "hibernate-dinamico.xml";
  private final String HIBERNATE_JNDI       = "hibernate-jndi.xml";
  private final String JANAL_MESSAGES       = "kajool.properties";
  private final String JANAL_DML            = "janal.xml";

  private static Settings instance;
  private static Object mutex;

  static {
    mutex = new Object();
  }

  private Settings() {
  }

  public static Settings getInstance() {
    synchronized (mutex) {
      if (instance == null) {
        try  {
          instance = new Settings();
        } // try
        catch (Exception e)  {
          Error.mensaje(e);
        } // catch
      } // if
    } // synchronized
    return instance;
  }

	public String toDefaultPath() {
    return this.JANAL_DEFAULT_PATH;
	}

	public String toFileModules() {
    return this.JANAL_MODULES;
	}

	public String toDefaultModules() {
    return this.JANAL_DEFAULT_PATH.concat(this.JANAL_MODULES);
	}

  public String getHibernateCustomFile() {
    return this.JANAL_PATH_DB.concat(this.HIBERNATE_DEVELOPMENT);
  }

  public String getHibernateCustomMapping() {
    return this.JANAL_PATH_DB.concat(this.HIBERNATE_MAPPING);
  }

  public String getDefaultHibernateMapping() {
    return this.JANAL_DEFAULT_PATH.concat(this.HIBERNATE_MAPPING);
  }

  public String getCustomProperties() {
    return this.JANAL_DEFAULT_PATH.concat(Constantes.JANAL_DEFAULT_PROPERTIES);
  }

  public String getCustomMessages() {
    return this.JANAL_DEFAULT_PATH.concat(this.JANAL_MESSAGES);
  }

  public String getCustomDml() {
    return "/mx/org/kaana/kajool/db/".concat(this.JANAL_DML);
  }

  public String getDefaultDml() {
    return this.JANAL_DEFAULT_PATH.concat(this.JANAL_DML);
  }
	
	public String getDefaultHibernateDinamic() {
    return this.JANAL_DEFAULT_PATH.concat(this.HIBERNATE_DINAMIC);
  }
	
	public String getDefaultHibernateJndi() {
    return this.JANAL_DEFAULT_PATH.concat(this.HIBERNATE_JNDI);
  }

}
