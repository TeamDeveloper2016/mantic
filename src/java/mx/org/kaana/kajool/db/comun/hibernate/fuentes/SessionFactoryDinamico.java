package mx.org.kaana.kajool.db.comun.hibernate.fuentes;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date Jul 3, 2012
 * @time 10:03:53 AM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.db.comun.hibernate.ClassFinder;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.xml.Dml;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public final class SessionFactoryDinamico {

  private static final Log LOG = LogFactory.getLog(SessionFactoryDinamico.class);
  private static final String KEY_DATA_BASE_USER = "janal.session.{0}.user.{1}";
  private static final String KEY_DATA_BASE_PASSWORD = "janal.session.{0}.password.{1}";
  private static final String KEY_DATA_BASE_URL = "janal.session.{0}.url.{1}";
  private static final String KEY_DATA_BASE_JNDI = "janal.session.{0}.jndi.{1}";
  private static final String KEY_DATA_BASE_BATCH_SIZE = "janal.session.{0}.batch_size.{1}";
  private static final String KEY_DATA_BASE_DIALECT = "janal.session.{0}.dialect.{1}";
  private static final String KEY_DATA_BASE_DRIVER = "janal.session.{0}.driver.{1}";
  private static final String KEY_DATA_BASE_MAPPING = "janal.session.{0}.mapping.{1}";
  private static final String KEY_HIBERNATE_MAPPING_DTO = "mapping";
  private static final String KEY_HIBERNATE_JNDI = "jndi";
  private static final String DEFAULT_DIALECT = "org.hibernate.dialect.Oracle10gDialect";
  private static final String DEFAULT_DRIVER = "oracle.jdbc.driver.OracleDriver";
  private static final String DEFAULT_BATCH_SIZE = "20";
  private static final String TOMCAT_DATASOURCE_PREFIX = "java://comp/env/";

  private static Object mutex;

  private static SessionFactoryDinamico instance;
  private Map<Long, SessionFactory> fuentes;

  static {
    mutex = new Object();
  }

  private SessionFactoryDinamico() {
    this.fuentes = new HashMap<>();
  }

  public static SessionFactoryDinamico getInstance() {
    synchronized (mutex) {
      if (instance == null) {
        instance = new SessionFactoryDinamico();
      } // if
    } // synchronized
    return instance;
  }

  public Session openSession(Long idFuenteDato) {
    return this.fuentes.get(idFuenteDato).openSession();
  }

  public Session getCurrentSession(Long idFuenteDato) {
    return this.fuentes.get(idFuenteDato).getCurrentSession();
  }

  private Map readParams(Long idFuenteDato) throws Exception {
    Map regresar = new HashMap();
    Map params = new HashMap();
    try {
      params.put("idFuenteDatos", idFuenteDato);
      Entity datosConexion = (Entity) DaoFactory.getInstance().toEntity("TcFuentesDatosDto", "basesFuentesDatos", params);
      if (datosConexion != null) {
        regresar = datosConexion.toMap();
      }
    } // try
    catch (Exception e) {
      throw e;
    } //catch
    finally {
      Methods.clean(params);
    } // finally
    return regresar;
  }

  private Map<String, String> loadParamsProperties(Long idFuenteDato) throws Exception {
    Configuracion cfgProperties = Configuracion.getInstance();
    Map<String, String> regresar = new HashMap<>();
    String etapa = cfgProperties.getEtapaServidor().toLowerCase();
    String userProperty = MessageFormat.format(KEY_DATA_BASE_USER, new Object[]{idFuenteDato, etapa});
    String passwordProperty = MessageFormat.format(KEY_DATA_BASE_PASSWORD, new Object[]{idFuenteDato, etapa});
    String urlProperty = MessageFormat.format(KEY_DATA_BASE_URL, new Object[]{idFuenteDato, etapa});
    String jndiProperty = MessageFormat.format(KEY_DATA_BASE_JNDI, new Object[]{idFuenteDato, etapa});
    String batchProperty = MessageFormat.format(KEY_DATA_BASE_BATCH_SIZE, new Object[]{idFuenteDato, etapa});
    String dialectProperty = MessageFormat.format(KEY_DATA_BASE_DIALECT, new Object[]{idFuenteDato, etapa});
    String driverProperty = MessageFormat.format(KEY_DATA_BASE_DRIVER, new Object[]{idFuenteDato, etapa});
    String mappingProperty = MessageFormat.format(KEY_DATA_BASE_MAPPING, new Object[]{idFuenteDato, etapa});
    if (cfgProperties.getPropiedad(mappingProperty) != null) {
      regresar.put(KEY_HIBERNATE_MAPPING_DTO, cfgProperties.getPropiedad(mappingProperty));
    }
    if (cfgProperties.getPropiedad(userProperty) != null && cfgProperties.getPropiedad(passwordProperty) != null && cfgProperties.getPropiedad(urlProperty) != null) {
      LOG.info("La conexión se realizara por jdbc usuario [".concat(userProperty).concat("] url [".concat(urlProperty).concat("]")));
      if (cfgProperties.getPropiedad(driverProperty) == null) {
        LOG.info("El driver no fue especificado se cargara el default".concat(DEFAULT_DRIVER));
      }
      regresar.put("manejador", cfgProperties.getPropiedad(driverProperty) == null ? DEFAULT_DRIVER : cfgProperties.getPropiedad(driverProperty));
      regresar.put("url", cfgProperties.getPropiedad(urlProperty));
      regresar.put("usuario", cfgProperties.getPropiedad(userProperty));
      regresar.put("contrasenia", cfgProperties.getPropiedad(passwordProperty));
      regresar.put("lenguaje", cfgProperties.getPropiedad(dialectProperty) == null ? DEFAULT_DIALECT : cfgProperties.getPropiedad(dialectProperty));
    }// if
    else {
      if (cfgProperties.getPropiedad(jndiProperty) != null) {
        regresar.put(KEY_HIBERNATE_JNDI, cfgProperties.getPropiedad(jndiProperty));
        regresar.put("batchSize", cfgProperties.getPropiedad(batchProperty) == null ? DEFAULT_BATCH_SIZE : cfgProperties.getPropiedad(batchProperty));
        regresar.put("lenguaje", cfgProperties.getPropiedad(dialectProperty) == null ? DEFAULT_DIALECT : cfgProperties.getPropiedad(dialectProperty));
      }// if			
      else {
        LOG.warn("No se encontraron propiedades para el registro de bases de datos adicionales en el archivo kajool.properties");
      }
    }
    return regresar;
  }

  /**
   * *
   * Metodo para crear archivo de configuracion en base al idFuenteDato y la clase que se quiere asociar
   */
  private void registry(List<Class> dtos, Map params, Long idFuenteDatos) throws Exception {
    registry(dtos, params, idFuenteDatos, false);
  }

  private void registry(List<Class> dtos, Map params, Long idFuenteDatos, boolean jndi) throws Exception {
    LectorPlantilla lectorPlantilla = null;
    Configuration configuration = null;
    ServiceRegistry serviceRegistry = null;
    SessionFactory sessionFactoryDinamico = null;
    try {
      lectorPlantilla = new LectorPlantilla(jndi);
      configuration   = new Configuration();
      configuration.configure(lectorPlantilla.construir(params));
      if (dtos != null && !dtos.isEmpty()) {
        for (Class dto : dtos) {
          configuration.addAnnotatedClass(dto);
        }
      }
      serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
      sessionFactoryDinamico = configuration.buildSessionFactory(serviceRegistry);
      sessionFactoryDinamico.getAllClassMetadata();
      this.fuentes.put(idFuenteDatos, sessionFactoryDinamico);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
  }

  public void load(Long idFuenteDatos) throws Exception {
    List<Class> dtos = null;
    Map params = null;
    boolean dataSource = false;
    try {
      if (!this.fuentes.containsKey(idFuenteDatos)) {
        if (Dml.getInstance().isKajool()) {
          dtos = loadDtos(idFuenteDatos);
          params = readParams(idFuenteDatos);
          dataSource = params.get(KEY_HIBERNATE_JNDI) != null && params.get(KEY_HIBERNATE_JNDI).toString() != null && !(params.get(KEY_HIBERNATE_JNDI).equals(""));
          if (dataSource && (getClass().getResource("/").getPath().contains("build"))) {
            params.put("batchSize", DEFAULT_BATCH_SIZE);
            params.put(KEY_HIBERNATE_JNDI, TOMCAT_DATASOURCE_PREFIX.concat(params.get(KEY_HIBERNATE_JNDI).toString()));
          }
          registry(dtos, params, idFuenteDatos, dataSource);
        }// if
        else {
          params = loadParamsProperties(idFuenteDatos);
          if (!params.isEmpty()) {
            if (params.containsKey(KEY_HIBERNATE_MAPPING_DTO)) {
              dtos = loadDtos(params.get(KEY_HIBERNATE_MAPPING_DTO).toString());
            }
            registry(dtos, params, idFuenteDatos, params.containsKey(KEY_HIBERNATE_JNDI));
          } // if		
        } // else
      } // if		
    } // try
    catch (Exception e) {
      throw new RuntimeException(e);
    } // catch  
  } // load

  public void load(Long idFuenteDatos, boolean properties) throws Exception {
    List<Class> dtos = null;
    Map params = null;
    boolean dataSource = false;
    try {
      if (!this.fuentes.containsKey(idFuenteDatos)) {
        if (!properties) {
          dtos = loadDtos(idFuenteDatos);
          params = readParams(idFuenteDatos);
          dataSource = params.get(KEY_HIBERNATE_JNDI) != null && params.get(KEY_HIBERNATE_JNDI).toString() != null && !(params.get(KEY_HIBERNATE_JNDI).equals(""));
          if (dataSource && (getClass().getResource("/").getPath().contains("build"))) {
            params.put("batchSize", DEFAULT_BATCH_SIZE);
            params.put(KEY_HIBERNATE_JNDI, TOMCAT_DATASOURCE_PREFIX.concat(params.get(KEY_HIBERNATE_JNDI).toString()));
          }
          registry(dtos, params, idFuenteDatos, dataSource);
        }// if
        else {
          params = loadParamsProperties(idFuenteDatos);
          if (!params.isEmpty()) {
            if (params.containsKey(KEY_HIBERNATE_MAPPING_DTO)) {
              dtos = loadDtos(params.get(KEY_HIBERNATE_MAPPING_DTO).toString());
            }
            registry(dtos, params, idFuenteDatos, params.containsKey(KEY_HIBERNATE_JNDI));
          } // if		
        } // else
      } // if		
    } // try
    catch (Exception e) {
      throw new RuntimeException(e);
    } // catch  
  } // load

  private List<Class> loadDtos(String mappingDto) throws IOException {
    return new ArrayList(ClassFinder.findClassesMapping(mappingDto));
  }

  private List<Class> loadDtos(Long idFuenteDato) throws Exception {
    List<Class> regresar = null;
    List<Entity> clases = null;
    Map<String, Object> params = null;
    try {
      LOG.info("Obteniendo dto de la fuente de datos con el id[".concat("]"));
      params = new HashMap<>();
      params.put("idFuenteDato", idFuenteDato);
      clases = DaoFactory.getInstance().toEntitySet("TrFuentesDtoDto", "findIdFuenteDato", params, Constantes.SQL_TODOS_REGISTROS);
      regresar = new ArrayList<>();
      if (clases != null && !clases.isEmpty()) {
        for (Entity clase : clases) {
          regresar.add(Class.forName(clase.toString("clase")));
        } // for
      } // for
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    finally {
      Methods.clean(params);
    } // finally
    return regresar;
  }
}