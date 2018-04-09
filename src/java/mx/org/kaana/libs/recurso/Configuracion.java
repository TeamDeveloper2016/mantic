package mx.org.kaana.libs.recurso;


import java.io.File;
import java.io.InputStream;
import java.util.Properties;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EEtapaServidor;
import mx.org.kaana.kajool.init.Settings;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Configuracion {

  private static final Log LOG              = LogFactory.getLog(Configuracion.class);
  private static final String CLASSES_FOLDER= "classes";
  private static final String DB_PACKAGE    = "db";

  private static Configuracion instance;
  private static Object mutex= null;
  private Properties properties;

  static {
    mutex=new Object();
  }

  private Configuracion() {
    loadProperties();
  }

  public static Configuracion getInstance() {
    if (instance == null) {
      synchronized (mutex) {
        if (instance == null)
          instance=new Configuracion();
      } // synchronized
    } // if
    return instance;
  } // getInstance

  public void reload() {
    try  {
      instance= new Configuracion();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
  }
  
  private void loadProperties() {
    InputStream is= this.getClass().getResourceAsStream("/".concat(Constantes.KAANA_PROPERTIES));
    try {
      this.properties= new Properties();
      // kajool default kajool.properties
      this.properties.load(this.getClass().getResourceAsStream(Settings.getInstance().getCustomProperties()));
      LOG.info("Se cargo el archivo de default de kajool.properties");
      if(is!= null)
        this.properties.load(is);
    } // try
    catch (Exception e) {
      Error.mensaje(e, "Estar seguro que esta en el CLASSPATH ".concat(Constantes.KAANA_PROPERTIES));
    } // catch
  } // loadProperties

  public String getPropiedad(String id) {
    String regresar = null;
    try {
      regresar= getProperties().getProperty(id);
    } // try
    catch (Exception e) {
      LOG.warn("No se pudo leer la propiedad ".concat(id).concat(". !"));
      regresar= "";
    } // catch
    return regresar;
  } // getPropiedad

  public int getPropiedadInt(String id) {
    int regresar= 0;
    String value= null;
    try {
      value   = getProperties().getProperty(id);
      regresar= Integer.parseInt(value);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      regresar=0;
    } // catch
    return regresar;
  } // getPropiedadInt

  public static byte getPropiedadByte(String id) {
    byte regresar= 0;
    String value = null;
    try {
      value   = getInstance().getProperties().getProperty(id);
      regresar= (byte)value.toCharArray()[0];
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      regresar=0;
    } // catch
    return regresar;
  } // getPropiedadByte

  public static double getPropiedadDouble(String id) {
    double regresar= 0;
    String value   = null;
    try {
      value   = getInstance().getProperties().getProperty(id);
      regresar= Double.parseDouble(value);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
    return regresar;
  } // getPropiedadDouble

  public String getPropiedad(String secion, String id) {
    return getPropiedad(secion.concat(".").concat(id));
  } // getPropiedad

  public void finalize() {
    instance.getProperties().clear();
    instance= null;
    mutex   = null;
  } // finalize

  private Properties getProperties() {
    return properties;
  } // getProperties
	
	public String getPropiedadSistemaServidor(String id) {
		String regresar = null;
		String propiedad= null;
    try {
      propiedad = "sistema.".concat(id).concat(".").concat(getEtapaServidor().toLowerCase());			
      regresar = getProperties().getProperty(propiedad);
    } // try
    catch (Exception e) {
      LOG.warn("No se pudo leer la propiedad ".concat(id).concat(". !"));
    } // catch
    return regresar;
  } // getPropiedadSistemaServidor

  public String getPropiedadServidor(String id) {
    try {
      String servidor= getEtapaServidor().toLowerCase();
      id=id.concat(".").concat(servidor);
      return getProperties().getProperty(id);
    } // try
    catch (Exception e) {
      LOG.warn("No se pudo leer la propiedad ".concat(id).concat(". !"));
    } // catch
    return null;
  }	// getPropiedadServidor
	
	public String getVersion () {
	  return Configuracion.getInstance().getPropiedad("sistema.version");
	} // getVersion
	
	public EEtapaServidor getEtapaServidor() {
		EEtapaServidor regresar= null;
    try {
      regresar = EEtapaServidor.valueOf(getProperties().getProperty("sistema.servidor").toUpperCase());
    }// try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
    return regresar;
  } // getEtapaServidor

  public String getEstiloPersonalizado() {
    String css= "kajool";
    try {
      css= getProperties().getProperty("sistema.css").concat(".css");
    } // try
    catch (Exception e) {
      LOG.warn("No se pudo leer la propiedad ".concat("sistema.css").concat(". !"));
    } // catch
    return css;
  } // getEstiloPersonalizado

  public static String dbPackagePath(Class<?> clase) {
    String path           = clase.getProtectionDomain().getCodeSource().getLocation().getPath();
    StringBuilder regresar= new StringBuilder();
    regresar.append(path.substring(0,path.lastIndexOf("/WEB-INF")+ 9));//9 posixiones para llegar antes de las classes
    regresar.append(CLASSES_FOLDER);
    regresar.append(File.separatorChar);
    regresar.append(DB_PACKAGE);
    regresar.append(File.separatorChar);
    return regresar.toString();
  } // dbPackagePath
	
	public String toFileModule() {
    String item= getPropiedad("sistema.modulos");
    return Cadena.isVacio(item)? Settings.getInstance().toFileModules(): item;
	} // toFileModule

  public String getHibernateCustomFile() {
    String item= getPropiedadServidor("hibernate.db.connection");
    return Cadena.isVacio(item)? Settings.getInstance().getHibernateCustomFile(): item;
  } // getHibernateCustomFile

  public String getHibernateCustomMapping() {
    String item= getPropiedad("hibernate.file.mapping");
    return Cadena.isVacio(item)? Settings.getInstance().getHibernateCustomMapping(): item;
  } // getHibernateCustomMapping

  public boolean isEtapaProduccion() {
    return getEtapaServidor().equals(EEtapaServidor.PRODUCCION);
  } // isEtapaProduccion

  public boolean isEtapaDesarrollo() {
    return getEtapaServidor().equals(EEtapaServidor.DESARROLLO);
  } // isEtapaDesarrollo

  public boolean isEtapaPruebas() {
    return getEtapaServidor().equals(EEtapaServidor.PRUEBAS);
  } // isEtapaPruebas
	
  public boolean isEtapaCapacitacion() {
    return getEtapaServidor().equals(EEtapaServidor.CAPACITACION);
  } // isEtapaCapacitacion

  public boolean isFreeAccess() {
    LOG.warn("Acceso libre [".concat(String.valueOf(Configuracion.getInstance().getPropiedadServidor("sistema.firmarse").equalsIgnoreCase("no"))).concat("]"));
    return Configuracion.getInstance().getPropiedadServidor("sistema.firmarse").equalsIgnoreCase("no");
  } // isFreeAccess
}
