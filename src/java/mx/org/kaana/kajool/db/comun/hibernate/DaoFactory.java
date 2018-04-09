package mx.org.kaana.kajool.db.comun.hibernate;

/**
 * Esta clase se encarga de la ejecución de sentencias sql sobre la base de datos
 * para altas, bajas y consultas.
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date Nov 17, 2010
 * @time 9:52:19 AM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.libs.formato.Variables;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.db.comun.dao.IBaseDao;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.kajool.db.comun.page.PageRecords;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.kajool.db.comun.transform.TransformDto;
import mx.org.kaana.kajool.db.comun.transform.TransformEntity;
import mx.org.kaana.kajool.db.comun.transform.TransformSourceEntity;
import mx.org.kaana.kajool.db.comun.transform.Transformer;
import mx.org.kaana.xml.Dml;
import mx.org.kaana.kajool.enums.ESql;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SQLQuery;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.transform.Transformers;

import org.w3c.dom.Document;

public final class DaoFactory<T extends IBaseDto> {

  private static final Log LOG=LogFactory.getLog(DaoFactory.class);
  private static final String ENTITY="/kajool/model/entity[@id='";
  private static final String PATTERN="']/pattern[@id='";
  private static final String POJO="']/pojo[@id='";
  private static DaoFactory instance;
  private static Object mutex;

  /**
	 * Inicialización de variable mutex
	 */
  static {
    mutex=new Object();
  }

  /**
	 * Contructor default
	 */
  private DaoFactory() {
  }

  /**
	 * Devuelve la instancia de la clase.
	 * @return Instancia de la clase.
	 */
  public static DaoFactory getInstance() {
    synchronized (mutex) {
      if (instance==null) {
        instance=new DaoFactory();
      }
    } // if
    return instance;
  }

  /**
	 * Devuelve la instancia de una clase Document en el que se encuentran acumulados los
   * archivos xml con las sentencias sql para interaccion de hibernate con la base de datos
	 * @return instancia de Document con sentencias sql
	 */
  private Document getDocumento() {
    return Dml.getInstance().getDocumento();
  }

  private String evaluate(String expresion) throws XPathExpressionException {
    XPathFactory xPFabrica=XPathFactory.newInstance();
    XPath xPath=xPFabrica.newXPath();
    String pojo=xPath.evaluate(expresion, getDocumento());
    LOG.info("DaoFactory (".concat(pojo).concat(")"));
    return pojo;
  }

	/**
	 * Devuelve instancia de clase que implementa IBaseDto indicada por la cadena nameDto
	 * @return instancia de clase que implementa IBaseDto
	 */
  public IBaseDto loadDTO(String nameDto) throws Exception {
    Class intance=Class.forName(nameDto);
    return (IBaseDto) intance.newInstance();
  }

  /**
	 * Devuelve instancia de clase que implementa IBaseDto definida por los atributos
   * de Class dto.
	 * @return instancia de clase que implementa IBaseDto
	 */
  public IBaseDto loadDTO(Class dto) throws Exception {
      return  loadDTO(dto.getName());
  }

  private String toDao(String key) throws XPathExpressionException {
    return evaluate(ENTITY.concat(key).concat(PATTERN).concat("dao").concat("']"));
  }

  private IBaseDao loadDAO(Class dto) throws Exception {
    Class motor=Class.forName(toDao(dto.getSimpleName()));
    return (IBaseDao) motor.newInstance();
  }

  /**
	 * Inserta un registro a la base de datos a partir de una instancia de un Dto
   * @param dto Dto a insertar
	 * @return Valor Long con el idKey del registro agregado. Si valor=-1 el registro no se insertó
   * @throws Exception
	 */
  public Long insert(T dto) throws Exception {
    Long key=-1L;
    try {
      key= insert(-1L, dto);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    return key;
  }

  /**
	 * Inserta un registro a la base de datos a partir de una instancia de un Dto y una
   * sesión de hibernate previamente inicializada para la persistencia de los datos
   * @param session Session de hibernate en la cual persisten los datos
   * @param dto Dto a insertar
	 * @return Valor Long con el idKey del registro agregado. Si valor=-1 el registro no se insertó
   * @throws Exception
	 */
  public Long insert(Session session, T dto) throws Exception {
    IBaseDao dao=null;
    Long key=-1L;
    try {
      dao=new DaoFacade(dto.toHbmClass());
      key=dao.insert(session, dto);
    } // try
    catch (Exception e) {
      throw e;
    } // cath
    return key;
  }
	
	/**
	 * Inserta un registro a una base de datos externa al sistema base, a partir de
   * una instancia de un Dto
   * @param idFuenteDato Id de del registro en la BD, en el cual se guardan las configuraciones para la conección a la BD externa
   * @param dto Dto a insertar
	 * @return Valor Long con el idKey del registro agregado. Si valor=-1 el registro no se insertó
	 * @throws Exception
   */
  public Long insert(Long idFuenteDato, T dto) throws Exception {
    IBaseDao dao= null;
    Long key    = -1L;		
    try {			
      dao=new DaoFacade(dto.toHbmClass());
      key=dao.insert(idFuenteDato, dto);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    return key;
  }

   /**
	 * Elimina un registro a la base de datos a partir de una instancia de un Dto
   * @param dto Dto a eliminar
	 * @return Valor Long con el idKey del registro eliminado. Si valor=-1 el registro no se eliminó.
	 * @throws Exception
   */
  public Long delete(T dto) throws Exception {
    try {
      return delete(dto.toHbmClass(), dto.getKey());
    } //  try
    catch (Exception e) {
      throw e;
    } // catch
  }

  /**
  * Elimina un registro a la base de datos indicando el dto que mapea a
  * a la base de datos y el campo llave del registro a eliminar
  * @param dto Instancia de Class que indica el dto que mapea la tabla a modificar
  * @param key idKey del regisatro a eliminar
  * @return Valor Long con el idKey del registro eliminado. Si valor=-1 el registro no se eliminó.
	* @throws Exception
  */
  public Long delete(Class dto, Long key) throws Exception {
    IBaseDao dao=null;
    try {
      dao=new DaoFacade(dto);
      dao.setKey(key);
      return dao.delete();
    } // end try
    catch (Exception e) {
      throw e;
    } // catch
  }

  /**
  * Elimina un registro a la base de datos indicando el dto que mapea a
  * a la base de datos, el campo llave del registro a eliminar y una Session de
  * hibernate para la persistencia de los datos.
  * @param session Session de hibernate en la cual persisten los datos
  * @param dto Instancia de Class que indica el dto que mapea la tabla a modificar
  * @param key idKey del regisatro a eliminar
  * @return Valor Long con el idKey del registro eliminado. Si valor=-1 el registro no se eliminó.
  * @throws Exception
  */
  public Long delete(Session session, Class dto, Long key) throws Exception {
    IBaseDao dao=null;
    try {
      dao=new DaoFacade(dto);
      return dao.delete(session, key);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
  }

  /**
  * Elimina un registro a la base de datos a partir de una instancia IBaseDto que contiene
  * la informacion del registro a eliminar y una Session de hibernate para
  * la persistencia de los datos.
  * @param session Session de hibernate en la cual persisten los datos
  * @param dto Dto que contiene la informacion del registro a eliminar
  * @return Valor Long con el idKey del registro eliminado. Si valor=-1 el registro no se eliminó.
	* @throws Exception
  */
  public Long delete(Session session, T dto) throws Exception {
    try {
      return delete(session, dto.toHbmClass(), dto.getKey());
    } // try
    catch (Exception e) {
      throw e;
    } // catch
  }

  /**
  * Actualiza un registro a la base de datos a partir de una instancia IBaseDto que contiene
  * la informacion del registro a modificar y una Session de hibernate para
  * la persistencia de los datos.
  * @param session Session de hibernate en la cual persisten los datos
  * @param dto Dto que contiene la informacion del registro a modificar
  * @return Valor Long con el idKey del registro modificado. Si valor=-1 el registro no se eliminó.
	* @throws Exception
  */
  public Long update(Session session, T dto) throws Exception {
    try {
      return update(session, dto, dto.toMap());
    } // try
    catch (Exception e) {
      throw e;
    } // catch
  }

  /**
  * Actualiza un registro a la base de datos a partir de una instancia IBaseDto que contiene
  * la informacion del registro a modificar, un mapa en el cual
  * se indican los campos y valores a modificar del registro y una Session de
  * hibernate para la persistencia de los datos.
  * @param session Session de hibernate en la cual persisten los datos
  * @param dto Dto que contiene la informacion del registro a modificar
  * @param fieldsDto Map<String,Object> que contiene los campos a modificar con su correspondite nuevo valor.
  * @return Valor Long con el idKey del registro modificado. Si valor=-1 el registro no se eliminó.
  * @throws Exception
  */
  public Long update(Session session, T dto, Map fieldsDto) throws Exception {
    try {
      return update(session, dto.toHbmClass(), dto.getKey(), fieldsDto);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
  }

  /**
  * Actualiza un registro a la base de datos a partir del la clase del dto que mapea
  * la tabla base de datos,  el idKey de registro a modificar, un mapa en el cual
  * se indican los campos y valores a modificar del registro y una Session de
  * hibernate para la persistencia de los datos.
  * @param session Session de hibernate en la cual persisten los datos
  * @param dto Dto que contiene la informacion del registro a modificar
  * @param key idKey del registro a modificar
  * @param fieldsDto Map<String,Object> que contiene los campos a modificar con su correspondite nuevo valor.
  * @return Valor Long con el idKey del registro modificado. Si valor=-1 el registro no se eliminó.
  * @throws Exception
  */
  public Long update(Session session, Class dto, Long key, Map fieldsDto) throws Exception {
    IBaseDao dao=null;
    try {
      dao=new DaoFacade(dto);
      fieldsDto.put("key", key);
      return dao.update(session, fieldsDto);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
  }

  /**
	 * Modifica un registro a la base de datos a partir de una instancia de IBaseDto
   * @param dto Dto a modificar
	 * @return Valor Long con el idKey del registro modificado. Si valor=-1 el registro no se insertó
	 * @throws Exception
   */
  public Long update(T dto) throws Exception {
    try {
      return update(dto, dto.toMap());
    } // try
    catch (Exception e) {
      throw e;
    } // catch
  }

  /**
	 * Modifica un registro a la base de datos a partir de una instancia IBaseDto
   * y un mapa en el cual se indican los campos y valores a modificar del registro
   * @param dto Dto a modificar
   * @param fieldsDto Map<String,Object> que contiene los campos a modificar con su correspondite nuevo valor.
	 * @return Valor Long con el idKey del registro modificado. Si valor=-1 el registro no se insertó
	 * @throws Exception
   */
  public Long update(T dto, Map fieldsDto) throws Exception {
    try {
      return update(dto.toHbmClass(), dto.getKey(), fieldsDto);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
  }

  /**
	 * Modifica un registro a la base de datos a partir de una instancia Class del
   * dto que mapea a la tabla de la base de datos, el idKey del registro a modificar
   * y un mapa en el cual se indican los campos y valores a modificar del registro
   * @param dto Dto a modificar
   * @param key Valor del idKey del registro a modificar
   * @param fieldsDto Map<String,Object> que contiene los campos a modificar con su correspondite nuevo valor.
	 * @return Valor Long con el idKey del registro modificado. Si valor=-1 el registro no se insertó
   * @throws Exception
   */
  public Long update(Class dto, Long key, Map fieldsDto) throws Exception {
    IBaseDao dao=null;
    try {
      dao=new DaoFacade(dto);
      fieldsDto.put("key", key);
      return dao.update(fieldsDto);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
  }

  /**
	 * Realiza una consulta a la base de datos a partir de una condicion establecida "params"
   * de la tabla que mapea la clase Class dto.
   * @param dto Clase dto
   * @param params Map<String,Object> que contiene la condicion a evaluar en la tabla.
	 * @return List<T> lista de registros que cumplen con la condición, donde el generico T es las clase Dto que mapea la tabla.
	 * @throws Exception
   */
  public List<T> findViewCriteria(Class dto, Map params) throws Exception {
    List<T> regresar=null;
    try {
      regresar=this.findViewCriteria(dto, params, Constantes.SQL_MAXIMO_REGISTROS);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  }

  /**
	 * Realiza una consulta a la base de datos a partir de una condicion establecida "params"
   * de la tabla que mapea la clase Class dto limitando un numero máximo de registros a obtener.
   * @param dto Clase dto
   * @param params Map<String,Object> que contiene la condicion a evaluar en la tabla.
   * @param maxRecords Numero Long que indica la cantidad maxima de registros a obtener.
	 * @return List<T> lista de registros que cumplen con la condición, donde el generico T es las clase Dto que mapea la tabla.
	 * @throws Exception
   */
  public List<T> findViewCriteria(Class dto, Map params, Long maxRecords) throws Exception {
    IBaseDao dao=null;
    List<T> regresar=null;
    try {
      dao=new DaoFacade(dto);
      regresar=dao.findViewCriteria(params, maxRecords);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  }

  /**
	 * Realiza una consulta a la base de datos a partir de una condicion establecida "params"
   * de la tabla que mapea la clase Class dto limitando un numero máximo de registros a obtener
   * y con una Session de hibernate para la persistencia de los datos.
   * @param session Session de hibernate en la que persisten los datos.
   * @param dto Clase dto.
   * @param params Map<String,Object> que contiene la condicion a evaluar en la tabla.
   * @param maxRecords Numero Long que indica la cantidad maxima de registros a obtener.
	 * @return List<T> lista de registros que cumplen con la condición, donde el generico T es las clase Dto que mapea la tabla.
	 * @throws Exception
   */
  public List<T> findViewCriteria(Session session, Class dto, Map params, Long maxRecords) throws Exception {
    IBaseDao dao=null;
    List<T> regresar=null;
    try {
      dao=new DaoFacade(dto);
      regresar=dao.findViewCriteria(session, params, maxRecords);
    } // try
    catch (Exception e) {
      throw e;
    } // end
    return regresar;
  }

  /**
	 * Realiza una consulta a la base de datos a partir de una condicion establecida "params"
   * de la tabla que mapea la clase Class dto y con una session de hibermate para la persistencia
   * de los datos
   * @param session Session de hibernate en la que persisten los datos.
   * @param dto Clase dto
   * @param params Map<String,Object> que contiene la condicion a evaluar en la tabla.
	 * @return List<T> lista de registros que cumplen con la condición, donde el generico T es las clase Dto que mapea la tabla.
	 * @throws Exception
   */
  public List<T> findViewCriteria(Session session, Class dto, Map params) throws Exception {
    List<T> regresar=null;
    try {
      regresar=this.findViewCriteria(session, dto, params, Constantes.SQL_MAXIMO_REGISTROS);
    } // try
    catch (Exception e) {
      throw e;
    } //  catch
    return regresar;
  }

  /**
	 * Realiza una consulta a la base de datos a partir de una condicion establecida "params"
   * de la tabla que mapea la clase Class dto. Usando como consulta la misma que se encuentre
   * en archivo XML con un id de select indicado y id unit con nombre igual al nombre del Class Dto.
   * @param dto Clase dto
   * @param params Map<String,Object> que contiene la condicion a evaluar en la tabla.
   * @param idXml Nombre de id del select de la consulta a utilizar.
	 * @return List<T> lista de registros que cumplen con la condición, donde el generico T es las clase Dto que mapea la tabla.
	 * @throws Exception
   */
  public List<T> findViewCriteria(Class<T> dto, Map params, String idXml) throws Exception {
    List<T> regresar=null;
    try {
      regresar=this.findViewCriteria(dto, params, Constantes.SQL_MAXIMO_REGISTROS, idXml);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  }

  /**
	 * Realiza una consulta a la base de datos a partir de una condicion establecida "params"
   * de la tabla que mapea la clase Class dto. Usando como consulta la misma que se encuentre
   * en archivo XML con un id de select indicado y id unit con nombre igual al nombre del Class Dto.
   * Limitando la cantidad de registros a obtener.
   * @param dto Clase dto
   * @param params Map<String,Object> que contiene la condicion a evaluar en la tabla.
   * @param records cantidad maxima de registros a obtener.
   * @param idXml Nombre de id del select de la consulta a utilizar.
	 * @return List<T> lista de registros que cumplen con la condición, donde el generico T es las clase Dto que mapea la tabla.
	 * @throws Exception
   */
  public List<T> findViewCriteria(Class<T> dto, Map params, Long records, String idXml) throws Exception {
    IBaseDao dao=null;
    List<T> regresar=null;
    try {
      dao=new DaoFacade(dto);
      regresar=dao.findViewCriteria(params, records, idXml);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  }

  /**
	 * Realiza una consulta a la base de datos a partir de una condicion establecida "params"
   * de la tabla que mapea la clase Class dto. Usando como consulta la misma que se encuentre
   * en archivo XML con un id de select indicado y id unit con nombre igual al nombre del Class Dto.
   * Teniendo una session de hibernate para la persistencia de los datos.
   * @param session Session de hibernate en al que persisen los datos.
   * @param dto Clase dto
   * @param params Map<String,Object> que contiene la condicion a evaluar en la tabla.
   * @param idXml Nombre de id del select de la consulta a utilizar.
	 * @return List<T> lista de registros que cumplen con la condición, donde el generico T es las clase Dto que mapea la tabla.
	 * @throws Exception
   */
  public List<T> findViewCriteria(Session session, Class<T> dto, Map params, String idXml) throws Exception {
    List<T> regresar=null;
    try {
      regresar=findViewCriteria(session, dto, params, Constantes.SQL_MAXIMO_REGISTROS, idXml);
    } // try
    catch (Exception e) {
      throw e;
    } // end
    return regresar;
  }

  /**
	 * Realiza una consulta a la base de datos a partir de una condicion establecida "params"
   * de la tabla que mapea la clase Class dto. Usando como consulta la misma que se encuentre
   * en archivo XML con un id de select indicado y id unit con nombre igual al nombre del Class Dto.
   * Limitando la cantidad de registros a obtener y teniendo una session para la persistencia de los datos.
   * @param session Session de hibernate en la que persisten los datos.
   * @param dto Clase dto
   * @param params Map<String,Object> que contiene la condicion a evaluar en la tabla.
   * @param records cantidad maxima de registros a obtener.
   * @param idXml Nombre de id del select de la consulta a utilizar.
	 * @return List<T> lista de registros que cumplen con la condición, donde el generico T es las clase Dto que mapea la tabla.
   * @throws Exception
   */
  public List<T> findViewCriteria(Session session, Class<T> dto, Map params, Long records, String idXml) throws Exception {
    IBaseDao dao=null;
    List<T> regresar=null;
    try {
      dao=new DaoFacade(dto);
      regresar=dao.findViewCriteria(session, params, records, idXml);
    } // try
    catch (Exception e) {
      throw e;
    } // end
    return regresar;
  }

  /**
	 * Realiza una consulta a una base de datos externa registrada en una tabla del sistema base a partir
   * de una condicion establecida "params" de la tabla que mapea la clase Class dto.
   * @param idFuenteDatos Id del registro que contiene las configuraciones para la coneccion base de datos externa.
   * @param dto Clase dto
   * @param params Map<String,Object> que contiene la condicion a evaluar en la tabla.
	 * @return List<T> lista de registros que cumplen con la condición, donde el generico T es las clase Dto que mapea la tabla.
	 * @throws Exception
   */
  public List<T> findViewCriteria(Long idFuenteDatos, Class dto, Map params) throws Exception {
    List<T> regresar=null;
    try {
      regresar=this.findViewCriteria(idFuenteDatos, dto, params, Constantes.SQL_MAXIMO_REGISTROS);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  }

  /**
	 * Realiza una consulta a una base de datos externa registrada en una tabla del sistema base a partir
   * de una condicion establecida "params" de la tabla que mapea la clase Class dto
   * limitando un numero máximo de registros a obtener.
   * @param idFuenteDatos Id del registro que contiene las configuraciones para la coneccion base de datos externa.
   * @param dto Clase dto
   * @param params Map<String,Object> que contiene la condicion a evaluar en la tabla.
   * @param maxRecords Numero Long que indica la cantidad maxima de registros a obtener.
	 * @return List<T> lista de registros que cumplen con la condición, donde el generico T es las clase Dto que mapea la tabla.
	 * @throws Exception
   */
  public List<T> findViewCriteria(Long idFuenteDatos, Class dto, Map params, Long maxRecords) throws Exception {
    IBaseDao dao=null;
    List<T> regresar=null;
    Session session=null;
    Transaction transaction=null;
    try {
      dao=new DaoFacade(dto);
      dao.setIdFuenteDato(idFuenteDatos);
      session=SessionFactoryFacade.getInstance().getSession(idFuenteDatos);
      transaction=session.beginTransaction();
      session.clear();
      regresar=dao.findViewCriteria(session, params, maxRecords);
      transaction.commit();
    } // try
    catch (Exception e) {
      if (transaction!=null) {
        transaction.rollback();
      }
      throw e;
    } // catch
    finally {
      if (session!=null) {
        session.close();
      }
      session=null;
      transaction=null;
    } // finally
    return regresar;
  }

  /**
	 * Realiza una consulta la base de datos sobre todos los registros de la clase "Class dto"
   * que existen en la tabla.
   * @param dto Clase dto que hace referencia a la tabla en la que se va a buscar
   * @return List<T> lista de registros contenidos en la tabla mapeada por la clase "Class dto", donde el generico T es las clase Dto que mapea la tabla.
	 * @throws Exception
   */
  public List<T> findAll(Class dto) throws Exception {
    List<T> regresar=null;
    try {
      regresar=this.findAll(dto, Constantes.SQL_MAXIMO_REGISTROS);
    } // try
    catch (Exception e) {
      throw e;
    } //  catch
    return regresar;
  }

  /**
	 * Realiza una consulta la base de datos sobre todos los registros de la clase "Class dto"
   * limitando una cantidad maxima de registros a obtener
   * @param dto Clase dto que hace referencia a la tabla en la que se va a buscar
   * @param maxRecords Cantidad maxima de registros a obtener.
   * @return List<T> lista de registros contenidos en la tabla mapeada por la clase "Class dto", donde el generico T es las clase Dto que mapea la tabla.
	 * @throws Exception
   */
  public List<T> findAll(Class dto, Long maxRecords) throws Exception {
    IBaseDao dao=null;
    List<T> regresar=null;
    try {
      dao=new DaoFacade(dto);
      regresar=dao.findAll(maxRecords);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  }

  /**
	 * Realiza una consulta la base de datos sobre todos los registros de la clase "Class dto"
   * que existen en la tabla, teniendo una session de hibernate para la persistencia de los datos
   * @param session Session de hibernate en la que persisten los datos.
   * @param dto Clase dto que hace referencia a la tabla en la que se va a buscar
   * @return List<T> lista de registros contenidos en la tabla mapeada por la clase "Class dto", donde el generico T es las clase Dto que mapea la tabla.
	 * @throws Exception
   */
  public List<T> findAll(Session session, Class dto) throws Exception {
    List<T> regresar=null;
    try {
      regresar=this.findAll(session, dto, Constantes.SQL_MAXIMO_REGISTROS);
    } //  try
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  }

  /**
	 * Realiza una consulta la base de datos sobre todos los registros de la clase "Class dto"
   * limitando una cantidad maxima de registros a obtener teniendo una Session de
   * hinbernate para la persistencia de los datos.
   * @param session Session de hibernate en la que persisten los datos.
   * @param dto Clase dto que hace referencia a la tabla en la que se va a buscar
   * @param maxRecords Cantidad maxima de registros a obtener.
   * @return List<T> lista de registros contenidos en la tabla mapeada por la clase "Class dto", donde el generico T es las clase Dto que mapea la tabla.
	 * @throws Exception
   */
  public List<T> findAll(Session session, Class dto, Long maxRecords) throws Exception {
    IBaseDao dao=null;
    List<T> regresar=null;
    try {
      dao=new DaoFacade(dto);
      regresar=dao.findAll(session, maxRecords);
    } //  try
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  }

   /**
	 * Realiza una consulta a una base de datos externa registrada en una tabla del sitema base,
   * sobre todos los registros de la clase "Class dto" que existen en la tabla.
   * @param idFuenteDatos Id del registro que contiene las configuraciones para la coneccion base de datos externa.
   * @param dto Clase dto que hace referencia a la tabla en la que se va a buscar
   * @return List<T> lista de registros contenidos en la tabla mapeada por la clase "Class dto", donde el generico T es las clase Dto que mapea la tabla.
	 * @throws Exception
   */
  public List<T> findAll(Long idFuenteDato, Class dto) throws Exception {
    List<T> regresar=null;
    try {
      regresar=this.findAll(idFuenteDato, dto, Constantes.SQL_MAXIMO_REGISTROS);
    } // try
    catch (Exception e) {
      throw e;
    } //  catch
    return regresar;
  }

  /**
	 * Realiza una consulta a una base de datos externa registrada en una tabla del sitema base,
   * sobre todos los registros de la clase "Class dto", limitando una cantidad maxima de registros a obtener
   * @param idFuenteDatos Id del registro que contiene las configuraciones para la coneccion base de datos externa.
   * @param dto Clase dto que hace referencia a la tabla en la que se va a buscar
   * @param maxRecords Cantidad maxima de registros a obtener.
   * @return List<T> lista de registros contenidos en la tabla mapeada por la clase "Class dto", donde el generico T es las clase Dto que mapea la tabla.
	 * @throws Exception
   */
  public List<T> findAll(Long idFuenteDato, Class dto, Long maxRecords) throws Exception {
    IBaseDao dao=null;
    List<T> regresar=null;
    Session session=null;
    Transaction transaction=null;
    try {
      dao=new DaoFacade(dto);
      dao.setIdFuenteDato(idFuenteDato);
      session=SessionFactoryFacade.getInstance().getSession(idFuenteDato);
      transaction=session.beginTransaction();
      session.clear();
      regresar=findAll(session, dto, maxRecords);
      transaction.commit();
    } // try
    catch (Exception e) {
      if (transaction!=null) {
        transaction.rollback();
      }
      throw e;
    } // catch
    return regresar;
  }

  /**
	 * Realiza una consulta a la tabla de la base de datos mapeada por la instancia
   * "Class dto", localizando un idKey dentro de ella y con una Session hibernate
   * para la persistencia de los datos.
   * @param session Session de hibernate en la que persisten los datos.
   * @param dto Clase dto que hace referencia a la tabla en la que se va a buscar
   * @param key Id a bucar en la tabla
   * @return T: es el registro localizado, donde T es una instancia de la clase dto que mapea a la tabla. Si valor=null, el registro no se encontro.
	 * @throws Exception
   */
  public T findById(Session session, Class dto, Long key) throws Exception {
    IBaseDao dao=null;
    T regresar=null;
    try {
      dao=new DaoFacade(dto);
      dao.setKey(key);
      regresar=(T) dao.findById(session, key);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  }

  /**
	 * Realiza una consulta a la tabla de la base de datos mapeada por la instancia
   * "Class dto", localizando un idKey dentro de ella.
   * @param dto Clase dto que hace referencia a la tabla en la que se va a buscar
   * @param key Id a bucar en la tabla
   * @return T: es el registro localizado, donde T es una instancia de la clase dto que mapea a la tabla. Si valor=null, el registro no se encontro.
	 * @throws Exception
   */
  public T findById(Class dto, Long key) throws Exception {
    IBaseDao dao=null;
    T regresar=null;
    try {
      dao=new DaoFacade(dto);
      dao.setKey(key);
      regresar=(T) dao.findById(key);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  }

  /**
	 * Realiza una consulta a la tabla de la base de datos mapeada por la instancia
   * "Class dto", verificando la existencia de un idKey determinado dentro de la tabla
   * y teniendo una Session hibernate para la persistencia de los datos.
   * @param session Session de hibernate en la que persisten los datos.
   * @param dto Clase dto que hace referencia a la tabla en la que se va a buscar
   * @param key Id a bucar en la tabla
   * @return Valor booleano que indica si se encontro el registro en la tabla.
	 * @throws Exception
   */
  public boolean exist(Session session, Class dto, Long key) throws Exception {
    boolean found=false;
    IBaseDao dao=null;
    try {
      dao=new DaoFacade(dto);
      dao.setKey(key);
      found=dao.exist(session, key);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    return found;
  }

  /**
	 * Realiza una consulta a la tabla de la base de datos mapeada por la instancia
   * "Class dto", verificando la existencia de un idKey determinado dentro de la tabla
   * @param dto Clase dto que hace referencia a la tabla en la que se va a buscar
   * @param key Id a bucar en la tabla
   * @return Valor booleano que indica si se encontro el registro en la tabla.
	 * @throws Exception
   */
  public boolean exist(Class dto, Long key) throws Exception {
    boolean found=false;
    IBaseDao dao=null;
    try {
      dao=new DaoFacade(dto);
      dao.setKey(key);
      found=dao.exist(key);
    } //  try
    catch (Exception e) {
      throw e;
    } //  catch
    return found;
  }

  /**
	 * Realiza una consulta a la tabla de la base de datos mapeada por la instancia
   * "Class dto" devolviendo el registro inmediato indicado por la condicion "params".
   * @param dto Clase dto que hace referencia a la tabla en la que se va a buscar
   * @param params Map<String,Object> que contiene la condicion a evaluar en la base de datos
   * @return T: es el registro localizado, donde T es una instancia de la clase dto que mapea a la tabla. Si valor=null, la tabla no contiene registros con esa condición.
	 * @throws Exception
   */
  public T findFirst(Class dto, Map params) throws Exception {
    T regresar=null;
    IBaseDao dao=null;
    try {
      dao=new DaoFacade(dto);
      regresar=(T) dao.findFirst(params);
    } //  try
    catch (Exception e) {
      throw e;
    } //  catch
    return regresar;
  }

  /**
	 * Realiza una consulta a la tabla de la base de datos mapeada por la instancia
   * "Class dto" devolviendo el registro inmediato indicado por la condicion "params",
   * que se evaluarán en consulta que se encuentre en archivo XML con un id de select
   * indicado e id unit con nombre igual al nombre de clase contenido en"Class Dto"
   * y teniendo una Session de hibernate para la persistencia de los datos.
   * @param session Session hibernate en la que persisten los datos.
   * @param dto Clase dto que hace referencia a la tabla en la que se va a buscar
   * @param idXml Nombre de id del select de la consulta a utilizar.
   * @param params Map<String,Object> que contiene la condicion a evaluar en la base de datos
   * @return T: es el registro localizado, donde T es una instancia de la clase dto que mapea a la tabla. Si valor=null, la tabla no contiene registros con esa condición.
	 * @throws Exception
   */
  public T findFirst(Session session, Class dto, String idXml, Map params) throws Exception {
    T regresar=null;
    IBaseDao dao=null;
    try {
      dao=new DaoFacade(dto);
      regresar=(T) dao.findFirst(session, params, idXml);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  }

  /**
	 * Realiza una consulta a la tabla de la base de datos mapeada por la instancia
   * "Class dto" devolviendo el registro inmediato indicado por la condicion "params",
   * que se evaluarán en consulta que se encuentre en archivo XML con un id de select
   * indicado e id unit con nombre igual al nombre de clase contenido en"Class Dto"
   * @param dto Clase dto que hace referencia a la tabla en la que se va a buscar
   * @param idXml Nombre de id del select de la consulta a utilizar.
   * @param params Map<String,Object> que contiene la condicion a evaluar en la base de datos
   * @return T: es el registro localizado, donde T es una instancia de la clase dto que mapea a la tabla. Si valor=null, la tabla no contiene registros con esa condición.
	 * @throws Exception
   */
  public T findFirst(Class dto, String idXml, Map params) throws Exception {
    T regresar=null;
    IBaseDao dao=null;
    try {
      dao=new DaoFacade(dto);
      regresar=(T) dao.findFirst(params, idXml);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  }

  /**
	 * Realiza una consulta a la tabla de la base de datos mapeada por la instancia
   * "Class dto" devolviendo el registro inmediato indicado por la condicion "params".
   * Teniendo una session de hibernate para la persistencia de los datos.
   * @param session Session de hibernate en la que persisten los datos.
   * @param dto Clase dto que hace referencia a la tabla en la que se va a buscar
   * @param params Map<String,Object> que contiene la condicion a evaluar en la base de datos
   * @return T: es el registro localizado, donde T es una instancia de la clase dto que mapea a la tabla. Si valor=null, la tabla no contiene registros con esa condición.
	 * @throws Exception
   */
  public T findFirst(Session session, Class dto, Map params) throws Exception {
    T regresar=null;
    IBaseDao dao=null;
    try {
      dao=new DaoFacade(dto);
      regresar=(T) dao.findFirst(session, params);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  }

  /**
	 * Realiza una consulta a la tabla de la base de datos mapeada por la instancia
   * "Class dto" devolviendo el registro inmediato indicado por la condicion "params",
   * que se evaluarán en consulta que se encuentre en archivo XML con un id de select
   * indicado e id unit con nombre igual al nombre de clase contenido en"Class Dto"
   * y teniendo una Session de hibernate para la persistencia de los datos.
   * @param session Session hibernate en la que persisten los datos.
   * @param dto Clase dto que hace referencia a la tabla en la que se va a buscar
   * @param idXml Nombre de id del select de la consulta a utilizar.
   * @param params Map<String,Object> que contiene la condicion a evaluar en la base de datos
   * @return T: es el registro localizado, donde T es una instancia de la clase dto que mapea a la tabla. Si valor=null, la tabla no contiene registros con esa condición.
	 * @throws Exception
   */
  public T findFirst(Session session, Class dto, Map params, String idXml) throws Exception {
    T regresar=null;
    IBaseDao dao=null;
    try {
      dao=new DaoFacade(dto);
      regresar=(T) dao.findFirst(session, params, idXml);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  }

  /**
	 * Realiza una consulta a una tabla de base de datos externa al sistema base mapeada por la instancia
   * "Class dto" devolviendo el registro inmediato indicado por la condicion "params".
   * @param idFuenteDato Id de del registro en la BD, en el cual se guardan las configuraciones para la conección a la BD externa
   * @param dto Clase dto que hace referencia a la tabla en la que se va a buscar
   * @param params Map que contiene la condicion a evaluar en la base de datos
   * @return T: es el registro localizado, donde T es una instancia de la clase dto que mapea a la tabla. Si valor=null, la tabla no contiene registros con esa condición.
	 * @throws Exception
   */
  public T findFirst(Long idFuenteDato, Class dto, Map params) throws Exception {
    T regresar=null;
    IBaseDao dao=null;
    Session session=null;
    Transaction transaction=null;
    try {
      dao=new DaoFacade(dto);
      dao.setIdFuenteDato(idFuenteDato);
      session=SessionFactoryFacade.getInstance().getSession(idFuenteDato);
      transaction=session.beginTransaction();
      session.clear();
      regresar=(T) findFirst(session, dto, params);
      transaction.commit();
    } //  try
    catch (Exception e) {
      if (transaction!=null) {
        transaction.rollback();
      }
      throw e;
    } //  catch
    return regresar;
  }

   /**
	 * Realiza una consulta a la tabla de la base de datos mapeada por la instancia
   * "Class dto" devolviendo el registro exactamente descrito por la condicion "params".
   * Teniendo una session de hibernate para la persistencia de los datos.
   * @param session Session de hibernate en la que persisten los datos.
   * @param dto Clase dto que hace referencia a la tabla en la que se va a buscar
   * @param params Map que contiene la condicion a evaluar en la base de datos
   * @return T: es el registro localizado, donde T es una instancia de la clase dto que mapea a la tabla. Si valor=null, la tabla no contiene registros con esa condición.
	 * @throws Exception
   */
  public T findIdentically(Session session, Class dto, Map params) throws Exception {
    T regresar=null;
    IBaseDao dao=null;
    try {
      dao=new DaoFacade(dto);
      regresar=(T) dao.findIdentically(session, params);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  }

   /**
	 * Realiza una consulta a la tabla de la base de datos mapeada por la instancia
   * "Class dto" devolviendo el registro exactamente descrito por la condicion "params".
   * @param dto Clase dto que hace referencia a la tabla en la que se va a buscar
   * @param params Map que contiene la condicion a evaluar en la base de datos
   * @return T: es el registro localizado, donde T es una instancia de la clase dto que mapea a la tabla. Si valor=null, la tabla no contiene registros con esa condición.
	 * @throws Exception
   */
  public T findIdentically(Class dto, Map params) throws Exception {
    T regresar=null;
    IBaseDao dao=null;
    try {
      dao=new DaoFacade(dto);
      regresar=(T) dao.findIdentically(params);
    } //  try
    catch (Exception e) {
      throw e;
    } //  catch
    return regresar;
  }


   /**
	 * Elimina todos los registros de la tabla mapeada por la clase "Class dto", y que
   * coinciden con la condicion descrita en "params".
   * @param dto Clase dto que hace referencia a la tabla en la que se va a buscar
   * @param params Map que contiene la condicion a evaluar en la base de datos
   * @return Valor del Id de registro eliminado Si valor=-1, no se eliminó registro alguno
	 * @throws Exception
   */
  public Long deleteAll(Class dto, Map params) throws Exception {
    IBaseDao dao=null;
    Long regresar=-1L;
    try {
      dao=new DaoFacade(dto);
      regresar=dao.deleteAll(params);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  }

  /**
	 * Elimina todos los registros de la tabla mapeada por la clase "Class dto", y que
   * coinciden con la condicion descrita en "params". Teniendo una Session
   * de hibernate para la persistencia de los datos.
   * @param session Session de hibernate en la que persisten los datos.
   * @param dto Clase dto que hace referencia a la tabla en la que se va a buscar
   * @param params Map que contiene la condicion a evaluar en la base de datos
   * @return Valor del Id de registro eliminado Si valor=-1, no se eliminó registro alguno
	 * @throws Exception
   */
  public Long deleteAll(Session session, Class dto, Map params) throws Exception {
    IBaseDao dao=null;
    Long regresar=-1L;
    try {
      dao=new DaoFacade(dto);
      regresar=dao.deleteAll(session, params);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  }

   /**
	 * Actualiza todos los registros de la tabla mapeada por la clase "Class dto",
   * obtenidos por la consulta indicada contendida en los archivos xml, definidos
   * por las condiciones de "params"
   * @param dto Clase dto que hace referencia a la tabla en la que se va a buscar.
   * @param params Map que contiene la condicion a evaluar en la base de datos.
   * @param idSeccionXml String que contiene el nombre del id de la consulta definida en archivo xml.
   * @return Registros modificados Si valor=-1, no se modificó ningún registro.
	 * @throws Exception
   */
  public Long updateAll(Class dto, Map params, String idSeccionXml) throws Exception {
    IBaseDao dao=null;
    Long regresar=-1L;
    try {
      dao=new DaoFacade(dto);
      regresar=dao.updateAll(params, idSeccionXml);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  }

  /**
	 * Actualiza todos los registros de la tabla mapeada por la clase "Class dto",
   * obtenidos por la consulta identificada por "idSeccionXml" que esta contendida en los archivos xml, definidos
   * por las condiciones de "params". Teniendo una Session de hibernate para la
   * persistencia de los datos.
   * @param session Session de hibernate en la que persisten los datos.
   * @param dto Clase dto que hace referencia a la tabla en la que se va a buscar.
   * @param params Map que contiene la condicion a evaluar en la base de datos.
   * @param idSeccionXml String que contiene el nombre del id de la consulta definida en archivo xml.
   * @return Registros modificados Si valor=-1, no se modificó ningún registro.
	 * @throws Exception
   */
  public Long updateAll(Session session, Class dto, Map params, String idSeccionXml) throws Exception {
    IBaseDao dao=null;
    Long regresar=-1L;
    try {
      dao=new DaoFacade(dto);
      regresar=dao.updateAll(session, params, idSeccionXml);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  }

 /**
	 * Actualiza todos los registros de la tabla mapeada por la clase "Class dto"
   * que coinciden con las condiciones descritas "params".
   * @param dto Clase dto que hace referencia a la tabla en la que se va a buscar.
   * @param params Map que contiene la condicion a evaluar en la base de datos.
   * @return Registros modificados Si valor=-1, no se modificó ningún registro.
	 * @throws Exception
   */
  public Long updateAll(Class dto, Map params) throws Exception {
    IBaseDao dao=null;
    Long regresar=-1L;
    try {
      dao=new DaoFacade(dto);
      regresar=dao.updateAll(params);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  }

  /**
	 * Actualiza todos los registros de la tabla mapeada por la clase "Class dto"
   * que coinciden con las condiciones descritas "params". Teniendo una session
   * de hibernate para la persistencia de los datos.
   * @param session Session de hibernate para la persistencia de los datos.
   * @param dto Clase dto que hace referencia a la tabla en la que se va a buscar.
   * @param params Map que contiene la condicion a evaluar en la base de datos.
   * @return Registros modificados Si valor=-1, no se modificó ningún registro.
	 * @throws Exception
   */
  public Long updateAll(Session session, Class dto, Map params) throws Exception {
    IBaseDao dao=null;
    Long regresar=-1L;
    try {
      dao=new DaoFacade(dto);
      regresar=dao.updateAll(session, params);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  }

  /**
	 * Obtiene la cantidad de registros contenidos en la tabla mapeada por la clase "Class dto"
   * que coinciden con la condicion descrita por "params". Teniendo una session
   * de hibernate para la persistencia de los datos.
   * @param session Session de hibernate para la persistencia de los datos.
   * @param dto Clase dto que hace referencia a la tabla en la que se va a buscar.
   * @param params Map<String, Object> que contiene la condicion a evaluar en la base de datos.
   * @return Total de registros que coinciden con la consulta.
	 * @throws Exception
   */
  public Long size(Session session, Class dto, Map<String, Object> params) throws Exception {
    IBaseDao dao=null;
    Long regresar=0L;
    try {
      dao=new DaoFacade(dto);
      regresar=dao.size(session, params);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  }

  /**
	 * Obtiene la cantidad de registros contenidos en la tabla mapeada por la clase "Class dto".
   * Teniendo una session de hibernate para la persistencia de los datos.
   * @param session Session de hibernate para la persistencia de los datos.
   * @param dto Clase dto que hace referencia a la tabla en la que se va a buscar.
   * @return Total de registros que coinciden con la consulta.
	 * @throws Exception
   */
  public Long size(Session session, Class dto) throws Exception {
    IBaseDao dao=null;
    Long regresar=0L;
    try {
      dao=new DaoFacade(dto);
      regresar=dao.size(session);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  }

  /**
	 * Obtiene la cantidad de registros contenidos en la tabla mapeada por la clase "Class dto".
   * Resultado de la consulta identidicada por "idXml" que esta contendida en los archivos xml.
   * Teniendo una session de hibernate para la persistencia de los datos.
   * @param session Session de hibernate para la persistencia de los datos.
   * @param dto Clase dto que hace referencia a la tabla en la que se va a buscar.
   * @param idXml String que define el id de la consulta en archivos xml.
   * @return Total de registros que coinciden con la consulta.
	 * @throws Exception
   */
  public Long size(Session session, Class dto, String idXml) throws Exception {
    IBaseDao dao=null;
    Long regresar=0L;
    try {
      dao=new DaoFacade(dto);
      regresar=dao.size(session, idXml);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  }

  /**
	 * Obtiene la cantidad de registros contenidos en la tabla mapeada por la clase "Class dto".
   * Resultado de la consulta identidicada por "idXml" que esta contendida en los archivos xml
   * descritos por la condicion "params".
   * @param session Session de hibernate para la persistencia de los datos.
   * @param dto Clase dto que hace referencia a la tabla en la que se va a buscar.
   * @param params Map<String, Object> que contiene los parametros a evaluar en la consulta.
   * @return Total de registros que coinciden con la consulta.
	 * @throws Exception
   */
  public Long size(Class dto, String idXml, Map<String, Object> params) throws Exception {
    IBaseDao dao=null;
    Long regresar=0L;
    try {
      dao=new DaoFacade(dto);
      regresar=dao.size(idXml, params);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  }

  /**
	 * Actualiza todos los registros de la tabla mapeada por la clase "Class dto"
   * que coinciden con las condiciones descritas "params".
   * @param dto Clase dto que hace referencia a la tabla en la que se va a buscar.
   * @param params Map<String, Object> que contiene la condicion a evaluar en la base de datos.
   * @return Total de registros que coinciden con la consulta.
	 * @throws Exception
   */
  public Long size(Class dto, Map<String, Object> params) throws Exception {
    return size(dto, Constantes.DML_SELECT, params);
  }

  /**
	 * Obtiene la cantidad de registros contenidos en la tabla mapeada por la clase "Class dto".
   * Resultado de la consulta identidicada por "idXml" que esta contendida en los archivos xml.
   * @param dto Clase dto que hace referencia a la tabla en la que se va a buscar.
   * @param idXml String que define el id de la consulta en archivos xml.
   * @return Total de registros que coinciden con la consulta.
	 * @throws Exception
   */
  public Long size(Class dto, String idXml) throws Exception {
    IBaseDao dao=null;
    Long regresar=0L;
    try {
      dao=new DaoFacade(dto);
      regresar=dao.size(idXml);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  }

  /**
	 * Obtiene la cantidad de registros contenidos en la tabla mapeada por la clase "Class dto".
   * @param dto Clase dto que hace referencia a la tabla en la que se va a buscar.
   * @return Total de registros que coinciden con la consulta.
	 * @throws Exception
   */
  public Long size(Class dto) throws Exception {
    IBaseDao dao=null;
    Long regresar=0L;
    try {
      dao=new DaoFacade(dto);
      regresar=dao.size();
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  }

  public PageRecords findPage(Class dto, Map params, Integer first, Integer records) throws Exception {
    IBaseDao dao=null;
    PageRecords regresar=null;
    try {
      dao=new DaoFacade(dto);
      regresar=dao.findPage(params, first, records);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  }

  public PageRecords findPage(Session session, Class dto, Map params, Integer first, Integer records) throws Exception {
    IBaseDao dao=null;
    PageRecords regresar=null;
    try {
      dao=new DaoFacade(dto);
      regresar=dao.findPage(session, params, first, records);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  }

  public PageRecords findPage(Long idFuenteDato, Class dto, Map params, Integer first, Integer records) throws Exception {
    IBaseDao dao=null;
    PageRecords regresar=null;
    try {
      dao=new DaoFacade(dto);
      dao.setIdFuenteDato(idFuenteDato);
      regresar=dao.findPage(params, first, records);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  }

  public PageRecords findPage(Long idFuenteDato, Session session, Class dto, Map params, Integer first, Integer records) throws Exception {
    IBaseDao dao=null;
    PageRecords regresar=null;
    try {
      dao=new DaoFacade(dto);
      dao.setIdFuenteDato(idFuenteDato);
      regresar=dao.findPage(session, params, first, records);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  }

  public List<IBaseDto> toChoose(Class dto, String keyColon, String condicionAsociada) throws Exception {
    String[] key=null;
    List<IBaseDto> regresar=null;
    try {
      if (keyColon!=null&&keyColon.length()>0) {
        key=keyColon.split(",");
        regresar=toChoose(dto, key, condicionAsociada);
      }
    }
    catch (Exception e) {
      throw e;
    }
    return regresar;
  }

  public List<IBaseDto> toChoose(Class dto, String[] keys, String condicionAsociada) throws Exception {
    List<IBaseDto> regresar=null;
    Map<String, Object> params=new HashMap<>();
    try {
      if (keys!=null) {
        IBaseDao _motor=new DaoFacade(dto);
        regresar=new ArrayList<>();
        for (String key : keys) {
          if (key!=null&&key.trim().length()>0) {
            _motor.setKey(Numero.getLong(key));
            params.put(Constantes.SQL_CONDICION, condicionAsociada.concat(key));
            regresar.add(_motor.findFirst(params));
          } // if
        } // for
        _motor=null;
      } // if
    }
    catch (Exception e) {
      throw e;
    }// catch
    finally {
			Methods.clean(params);
    }// finally
    return regresar;
  }

  /**
	 * Obtiene el valor del campo definido por "name" de una consulta sql, la
   * cual devuelve uno o mas campos. Teniendo una session de hibernate para
   * la persistencia de los datos.
   * @param session Session de hibernate en la que persisten los datos.
   * @param sql Sentencia sql que realizara consulta a la base de datos
   * @param name Nombre del campo de la tabla, del que se extraerá el valor.
   * @return Value que contiene valor del campo obtenido.
	 * @throws Exception
   */
  public Value toField(Session session, String sql, String name) throws Exception {
    Value regresar=null;
    SQLQuery query=null;
    List list=null;
    Entity entity=null;
    Object[] resultado=null;
    try {
      query=session.createSQLQuery(sql);
      query.setMaxResults(Constantes.SQL_PRIMER_REGISTRO);
      list=query.setResultTransformer(new TransformEntity()).list();
      if (list!=null&&list.size()>0) {
        entity=(Entity) list.get(0);
        if (name!=null) {
          //field = Cadena.toBeanName(name);
          if (entity.containsKey(name)) {
            regresar=entity.get(name);
          }
          else {
            throw new RuntimeException("El nombre del campo '".concat(name).concat("' no existe."));
          }
        } // if
        else {
          resultado=entity.values().toArray();
          regresar=(Value) resultado[0];
        } // else
      } // if
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    finally {
      if (list!=null) {
        list.clear();
      }
      list=null;
      query=null;
    } // finally
    return regresar;
  }

  /**
	 * Obtiene el valor del campo definido consulta sql, la cual devuelve un solo campo.
   * Teniendo una session de hibernate para la persistencia de los datos.
   * @param session Session de hibernate en la que persisten los datos.
   * @param sql Sentencia sql con un solo campo seleccionado.
   * @return Value que contiene valor del campo obtenido.
	 * @throws Exception
   */
  public Value toField(Session session, String sql) throws Exception {
    String name=null;
    return toField(session, sql, name);
  }

  /**
	 * Obtiene el valor del campo definido consulta sql, la cual devuelve un solo campo.
   * @param sql Sentencia sql con un solo campo seleccionado.
   * @return Value que contiene valor del campo obtenido.
	 * @throws Exception
   */
  public Value toField(String sql) throws Exception {
    String name=null;
    return toField(sql, name);
  }

  /**
	 * Obtiene el valor del campo definido por "name" del registro devuelto por la
   * consulta contenida en archivo xml, definida por el unit "process" y select "idXml" y que coincide
   * con las condiciones descritas en "params". Teniendo una session de hibernate para
   * la persistencia de los datos.
   * @param session Session de hibernate en la que persisten los datos.
   * @param process Id unit del proceso que contiene la consulta en archivo xml.
   * @param idXml Id del select contenido en el unit definido por "process".
   * @param params Map que contiene los parametros a evaluar en la consulta.
   * @param name Nombre del campo de la tabla, del que se extraerá el valor.
   * @return Value que contiene valor del campo obtenido.
	 * @throws Exception
   */
  public Value toField(Session session, String process, String idXml, Map params, String name) throws Exception {
    Value regresar=null;
    Dml dml=null;
    try {
      dml=Dml.getInstance();
      regresar=toField(session, dml.getSelect(process, idXml, params), name);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    finally {
      dml=null;
    } // finally
    return regresar;
  }

  /**
	 * Obtiene el valor del campo del registro devuelto por la consulta contenida en archivo xml,
   * definida por el unit "process" y el select por default (row) y que coincide con las
   * condiciones descritas en "params". La consulta solo debe un campo dentro del select.
   * Teniendo una session de hibernate para la persistencia de los datos.
   * @param session Session de hibernate en la que persisten los datos.
   * @param process Id unit del proceso que contiene la consulta en archivo xml.
   * @param params Map que contiene los parametros a evaluar en la consulta.
   * @return Value que contiene valor del campo obtenido.
	 * @throws Exception
   */
  public Value toField(Session session, String process, Map params) throws Exception {
    return toField(session, process, Constantes.DML_SELECT, params, null);
  }

  /**
	 * Obtiene el valor del campo definido por "name" del registro devuelto por la
   * consulta contenida en archivo xml, definida por el unit "process" y select "idXml" y que coincide
   * con las condiciones descritas en "params".
   * @param process Id unit del proceso que contiene la consulta en archivo xml.
   * @param idXml Id del select contenido en el unit definido por "process".
   * @param params Map que contiene los parametros a evaluar en la consulta.
   * @param name Nombre del campo de la tabla, del que se extraerá el valor.
   * @return Value que contiene valor del campo obtenido.
	 * @throws Exception
   */
  public Value toField(String process, String idXml, Map params, String name) throws Exception {
    Value regresar          =null;
    Session session         =null;
    Transaction transaction =null;
    try {
      session=SessionFactoryFacade.getInstance().getSession();
      transaction=session.beginTransaction();
      session.clear();
      regresar=toField(session, process, idXml, params, name);
      transaction.commit();
    } // try
    catch (Exception e) {
      if (transaction!=null) {
        transaction.rollback();
      }
      throw e;
    } // catch
    finally {
      if (session!=null) {
        session.close();
      }
      session=null;
      transaction=null;
    } // finally
    return regresar;
  }

  /**
	 * Obtiene el valor del campo definido por "name" del registro devuelto por la
   * consulta contenida en archivo xml, definida por el unit "process" y el select por default (row)
   * y que coincide con las condiciones descritas en "params".
   * @param process Id unit del proceso que contiene la consulta en archivo xml.
   * @param params Map que contiene los parametros a evaluar en la consulta.
   * @param name Nombre del campo de la tabla, del que se extraerá el valor.
   * @return Value que contiene valor del campo obtenido.
	 * @throws Exception
   */
  public Value toField(String process, Map params, String name) throws Exception {
    return toField(process, Constantes.DML_SELECT, params, name);
  }

  /**
	 * Obtiene el valor del campo del registro devuelto por la
   * consulta contenida en archivo xml, definida por el unit "process" y el select por default (row)
   * y que coincide con las condiciones descritas en "params". La consulta solo debe contener un
   * campo dentro de su select.
   * @param process Id unit del proceso que contiene la consulta en archivo xml.
   * @param params Map que contiene los parametros a evaluar en la consulta.
   * @return Value que contiene valor del campo obtenido.
	 * @throws Exception
   */
  public Value toField(String process, Map params) throws Exception {
    return toField(process, Constantes.DML_SELECT, params);
  }

  /**
	 * Obtiene el valor del campo del registro devuelto por la consulta contenida
   * en archivo xml, definida por el unit "process" y select "idXml" y que coincide
   * con las condiciones descritas en "params". La consulta solo debe contener
   * un campo dentro del select.
   * @param process Id unit del proceso que contiene la consulta en archivo xml.
   * @param idXml Id del select contenido en el unit definido por "process".
   * @param params Map que contiene los parametros a evaluar en la consulta.
   * @return Value que contiene valor del campo obtenido.
	 * @throws Exception
   */
  public Value toField(String process, String idXml, Map params) throws Exception {
    return toField(process, idXml, params, null);
  }

  /**
	 * Obtiene el valor del campo definido por "name" de una consulta sql, la
   * cual devuelve uno o mas campos.
   * @param sql Sentencia sql que realizara consulta a la base de datos.
   * @param name Nombre del campo de la tabla, del que se extraerá el valor.
   * @return Value que contiene valor del campo obtenido.
	 * @throws Exception
   */
  public Value toField(String sql, String name) throws Exception {
    Value regresar          =null;
    Session session         =null;
    Transaction transaction =null;
    try {
      session=SessionFactoryFacade.getInstance().getSession();
      transaction=session.beginTransaction();
      session.clear();
      regresar=toField(session, sql, name);
      transaction.commit();
    } // try
    catch (Exception e) {
      if (transaction!=null) {
        transaction.rollback();
      }
      throw e;
    } // catch
    finally {
      if (session!=null) {
        session.close();
      }
      session=null;
      transaction=null;
    } // finally
    return regresar;
  }

  /**
	 * Obtiene el registro definido por consulta sql, teniendo una session de hibernate
   * para la persistencia de los datos. Los nombres de los campos son renombrados
   * por hibermate a notacion dromedario.
   * @param session Session de hibernate en la que persisten los datos.
   * @param sql Sentencia select de sql.
   * @return Entity que contiene el registro obtenido.
	 * @throws Exception
   */
  public T toEntity(Session session, String sql) throws Exception {
    Entity regresar=null;
    SQLQuery query=null;
    List list=null;
    try {
      list = toRecordsEntity(session, sql, new TransformEntity());
      if (list!=null&&list.size()>0) {
        regresar=(Entity) list.get(0);
      } // if
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    finally {
      if (list!=null) {
        list.clear();
      }
      list=null;
      query=null;
    } // finally
    return (T) regresar;
  }
	
  /**
	 * Obtiene el registro definido por consulta sql, teniendo una session de hibernate
   * para la persistencia de los datos. Los nombres de los campos son devueltos tal cual
   * se encuentran en la base de datos.
   * @param session Session de hibernate en la que persisten los datos.
   * @param sql Sentencia select de sql.
   * @return Entity que contiene el registro obtenido.
	 * @throws Exception
   */
  public T toSourceEntity(Session session, String sql) throws Exception {
    Entity regresar=null;
    SQLQuery query=null;
    List list=null;
    try {
      list = toRecordsEntity(session, sql, new TransformSourceEntity());
      if (list!=null&&list.size()>0) {
        regresar=(Entity) list.get(0);
      } // if
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    finally {
      if (list!=null) {
        list.clear();
      }
      list=null;
      query=null;
    } // finally
    return (T) regresar;
  }

  /**
	 * Obtiene el registro devuelto por la consulta contenida en archivo xml,
   * definida por el unit "process" y el select "idXml" y que coincide
   * con las condiciones descritas en "params". Teniendo una session de hibernate para
   * la persistencia de los datos. Los nombres de los campos son renombrados por
   * hibernate a notacion dromedario.
   * @param session Session de hibernate en la que persisten los datos.
   * @param process Id unit del proceso que contiene la consulta en archivo xml.
   * @param idXml Id del select contenido en el unit definido por "process".
   * @param params Map que contiene los parametros a evaluar en la consulta.
   * @return Entity que contiene el registro obtenido.
	 * @throws Exception
   */
  public T toEntity(Session session, String process, String idXml, Map params) throws Exception {
    T regresar=null;
    Dml dml=null;
    StringBuilder sb=new StringBuilder();
    try {
      dml=Dml.getInstance();
      if (idXml!=null&&idXml.equals(Constantes.DML_RESERVADO)) {
        sb.append("select * from (");
        sb.append(dml.getSelect(process, Constantes.DML_SELECT, params));
        sb.append(") datos where ");
        sb.append(params.get(Constantes.SQL_RESERVADO));
      } // if
      else {
        sb.append(dml.getSelect(process, idXml, params));
      }
      regresar=toEntity(session, sb.toString());
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    finally {
      dml=null;
    } // finally
    return regresar;
  }
	
  /**
	 * Obtiene el registro devuelto por la consulta contenida en archivo xml,
   * definida por el unit "process" y el select "idXml" y que coincide
   * con las condiciones descritas en "params". Teniendo una session de hibernate para
   * la persistencia de los datos. Los nombres de los campos son devueltos tal cual
   * se encuentran en la base de datos.
   * @param session Session de hibernate en la que persisten los datos.
   * @param process Id unit del proceso que contiene la consulta en archivo xml.
   * @param idXml Id del select contenido en el unit definido por "process".
   * @param params Map que contiene los parametros a evaluar en la consulta.
   * @return Entity que contiene el registro obtenido.
	 * @throws Exception
   */
  public T toSourceEntity(Session session, String process, String idXml, Map params) throws Exception {
    T regresar=null;
    Dml dml=null;
    StringBuilder sb=new StringBuilder();
    try {
      dml=Dml.getInstance();
      if (idXml!=null&&idXml.equals(Constantes.DML_RESERVADO)) {
        sb.append("select * from (");
        sb.append(dml.getSelect(process, Constantes.DML_SELECT, params));
        sb.append(") datos where ");
        sb.append(params.get(Constantes.SQL_RESERVADO));
      } // if
      else {
        sb.append(dml.getSelect(process, idXml, params));
      }
      regresar=toSourceEntity(session, sb.toString());
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    finally {
      dml=null;
    } // finally
    return regresar;
  }

  /**
	 * Obtiene el registro devuelto por la consulta contenida en archivo xml,
   * definida por el unit "process" y el select por default (row) y que coincide con las
   * condiciones descritas en "params".Teniendo una session de hibernate
   * para la persistencia de los datos. Los nombres de los campos son renombrados
   * por hibernate a nomacion dromedario.
   * @param session Session de hibernate en la que persisten los datos.
   * @param process Id unit del proceso que contiene la consulta en archivo xml.
   * @param params Map que contiene los parametros a evaluar en la consulta.
   * @return Entity que contiene el registro obtenido.
	 * @throws Exception
   */
  public T toEntity(Session session, String process, Map params) throws Exception {
    return toEntity(session, process, Constantes.DML_SELECT, params);
  }
	
  /**
	 * Obtiene el registro devuelto por la consulta contenida en archivo xml,
   * definida por el unit "process" y el select por default (row) y que coincide con las
   * condiciones descritas en "params".Teniendo una session de hibernate
   * para la persistencia de los datos. Los nombres de los campos son devueltos tal cual
   * se encuentran en la base de datos.
   * @param session Session de hibernate en la que persisten los datos.
   * @param process Id unit del proceso que contiene la consulta en archivo xml.
   * @param params Map que contiene los parametros a evaluar en la consulta.
   * @return Entity que contiene el registro obtenido.
	 * @throws Exception
   */
  public T toSourceEntity(Session session, String process, Map params) throws Exception {
    return toSourceEntity(session, process, Constantes.DML_SELECT, params);
  }

  /**
	 * Obtiene el registro devuelto por la consulta contenida en archivo xml,
   * definida por el unit "process" y el select por default (row)
   * y que coincide con las condiciones descritas en "params". Los nombres de
   * de los campos son renombrados por hibernate a notacion dromedario.
   * @param process Id unit del proceso que contiene la consulta en archivo xml.
   * @param params Map que contiene los parametros a evaluar en la consulta.
   * @return Entity que contiene el registro obtenido.
	 * @throws Exception
   */
  public T toEntity(String process, Map params) throws Exception {
    return toEntity(process, Constantes.DML_SELECT, params);
  }
	
  /**
	 * Obtiene el registro devuelto por la consulta contenida en archivo xml,
   * definida por el unit "process" y el select por default (row)
   * y que coincide con las condiciones descritas en "params". Los nombres de
   * de los campos son devueltos tal cual se encuentran en la base de datos.
   * @param process Id unit del proceso que contiene la consulta en archivo xml.
   * @param params Map que contiene los parametros a evaluar en la consulta.
   * @return Entity que contiene el registro obtenido.
	 * @throws Exception
   */
  public T toSourceEntity(String process, Map params) throws Exception {
    return toSourceEntity(process, Constantes.DML_SELECT, params);
  }

  /**
	 * Obtiene el registro devuelto por la consulta contenida en archivo xml,
   * definida por el unit "process" y el select "idXml" y que coincide
   * con las condiciones descritas en "params". Los nombres de los campos son renombrados por
   * hibernate a notacion dromedario.
   * @param process Id unit del proceso que contiene la consulta en archivo xml.
   * @param idXml Id del select contenido en el unit definido por "process".
   * @param params Map que contiene los parametros a evaluar en la consulta.
   * @return Entity que contiene el registro obtenido.
	 * @throws Exception
   */
  public T toEntity(String process, String idXml, Map params) throws Exception {
    T regresar              =null;
    Session session         =null;
    Transaction transaction =null;
    try {
      session=SessionFactoryFacade.getInstance().getSession();
      transaction=session.beginTransaction();
      session.clear();
      regresar=toEntity(session, process, idXml, params);
      transaction.commit();
    } // try
    catch (Exception e) {
      if (transaction!=null) {
        transaction.rollback();
      }
      throw e;
    } // catch
    finally {
      if (session!=null) {
        session.close();
      }
      session=null;
      transaction=null;
    } // finally
    return regresar;
  }

  /**
	 * Obtiene el registro devuelto por la consulta contenida en archivo xml,
   * definida por el unit "process" y el select "idXml" y que coincide
   * con las condiciones descritas en "params". Los nombres de los campos son
   * devueltos tal cual se encuentran en la base de datos.
   * @param process Id unit del proceso que contiene la consulta en archivo xml.
   * @param idXml Id del select contenido en el unit definido por "process".
   * @param params Map que contiene los parametros a evaluar en la consulta.
   * @return Entity que contiene el registro obtenido.
	 * @throws Exception
   */
  public T toSourceEntity(String process, String idXml, Map params) throws Exception {
    T regresar              =null;
    Session session         =null;
    Transaction transaction =null;
    try {
      session=SessionFactoryFacade.getInstance().getSession();
      transaction=session.beginTransaction();
      session.clear();
      regresar=toSourceEntity(session, process, idXml, params);
      transaction.commit();
    } // try
    catch (Exception e) {
      if (transaction!=null) {
        transaction.rollback();
      }
      throw e;
    } // catch
    finally {
      if (session!=null) {
        session.close();
      }
      session=null;
      transaction=null;
    } // finally
    return regresar;
  }

  /**
	 * Obtiene el registro definido por consulta sql, teniendo una session de hibernate
   * para la persistencia de los datos. Los nombres de los campos son renombrados
   * por hibernate con notacion dromedario.
   * se encuentran en la base de datos.
   * @param sql Sentencia select de sql.
   * @return Entity que contiene el registro obtenido.
	 * @throws Exception
   */
  public T toEntity(String sql) throws Exception {
    T regresar              = null;
    Session session         = null;
    Transaction transaction = null;
    try {
      session=SessionFactoryFacade.getInstance().getSession();
      transaction=session.beginTransaction();
      session.clear();
      regresar=toEntity(session, sql);
      transaction.commit();
    } // try
    catch (Exception e) {
      if (transaction!=null) {
        transaction.rollback();
      }
      throw e;
    } // catch
    finally {
      if (session!=null) {
        session.close();
      }
      session=null;
      transaction=null;
    } // finally
    return regresar;
  }
	
	/**
	 * Obtiene el registro definido por consulta sql, teniendo una session de hibernate
   * para la persistencia de los datos. Los nombres de los campos son devueltos tal cual
   * se encuentran en la base de datos.
   * @param sql Sentencia select de sql.
   * @return Entity que contiene el registro obtenido.
	 * @throws Exception
   */
  public T toSourceEntity(String sql) throws Exception {
    T regresar              =null;
    Session session         =null;
    Transaction transaction =null;
    try {
      session=SessionFactoryFacade.getInstance().getSession();
      transaction=session.beginTransaction();
      session.clear();
      regresar=toSourceEntity(session, sql);
      transaction.commit();
    } // try
    catch (Exception e) {
      if (transaction!=null) {
        transaction.rollback();
      }
      throw e;
    } // catch
    finally {
      if (session!=null) {
        session.close();
      }
      session=null;
      transaction=null;
    } // finally
    return regresar;
  }

  public T toEntity(Session session, Class<IBaseDto> dto, String sql) throws Exception {
    T regresar=null;
    SQLQuery query=null;
    List list=null;
    try {
      query=session.createSQLQuery(sql);
      query.setMaxResults(Constantes.SQL_PRIMER_REGISTRO);
      list=query.setResultTransformer(new TransformDto(dto)).list();
      if (list!=null&&list.size()>0) {
        regresar=(T) list.get(0);
      }
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    finally {
      if (list!=null) {
        list.clear();
      }
      list=null;
      query=null;
    } // finally
    return regresar;
  }

  public T toEntity(Session session, Class<IBaseDto> dto, Map params) throws Exception {
    T regresar=null;
    Dml dml=null;
    try {
      dml=Dml.getInstance();
      regresar=toEntity(session, dto, dml.getSelect(dto.getSimpleName(), Constantes.DML_SELECT, params));
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    finally {
      dml=null;
    } // finally
    return regresar;
  }

  public T toEntity(Session session, Class<IBaseDto> dto) throws Exception {
    T regresar=null;
    Map<String, Object> params=new HashMap<>();
    try {
      params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
      regresar=toEntity(session, dto, params);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    finally {
			Methods.clean(params);
    } // finally
    return regresar;
  }

  public T toEntity(Session session, Class<IBaseDto> dto, String process, String idXml, Map params) throws Exception {
    T regresar=null;
    Dml dml=null;
    try {
      dml=Dml.getInstance();
      regresar=toEntity(session, dto, dml.getSelect(process, idXml, params));
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    finally {
      dml=null;
    } // finally
    return regresar;
  }

  public T toEntity(Session session, Class<IBaseDto> dto, String process, Map params) throws Exception {
    return toEntity(session, dto, process, Constantes.DML_SELECT, params);
  }

  public T toEntity(Class<IBaseDto> dto, Map params) throws Exception {
    T regresar               =null;
    Session session          =null;
    Transaction transaction  =null;
    try {
      session    =SessionFactoryFacade.getInstance().getSession();
      transaction=session.beginTransaction();
      session.clear();
      regresar=toEntity(session, dto, params);
      transaction.commit();
    } // try
    catch (Exception e) {
      if (transaction!=null) {
        transaction.rollback();
      }
      throw e;
    } // catch
    finally {
      if (session!=null) {
        session.close();
      }
      session=null;
      transaction=null;
    } // finally
    return regresar;
  }

  public T toEntity(Class<IBaseDto> dto) throws Exception {
    T regresar=null;
    Map<String, Object> params=new HashMap<>();
    try {
      params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
      regresar=toEntity(dto, params);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    finally {
      Methods.clean(params);
    } // finally
    return regresar;
  }

  public T toEntity(Class<IBaseDto> dto, String sql) throws Exception {
    T regresar=null;
    Session session=null;
    Transaction transaction=null;
    try {
      session=SessionFactoryFacade.getInstance().getSession();
      transaction=session.beginTransaction();
      session.clear();
      regresar=toEntity(session, dto, sql);
      transaction.commit();
    } // try
    catch (Exception e) {
      if (transaction!=null) {
        transaction.rollback();
      }
      throw e;
    } // catch
    finally {
      if (session!=null) {
        session.close();
      }
      session=null;
      transaction=null;
    } // finally
    return regresar;
  }

  public T toEntity(Class<IBaseDto> dto, String process, Map params) throws Exception {
    return toEntity(dto, process, Constantes.DML_SELECT, params);
  }

  public T toEntity(Class<IBaseDto> dto, String process, String idXml, Map params) throws Exception {
    T regresar              =null;
    Session session         =null;
    Transaction transaction =null;
    try {
      session=SessionFactoryFacade.getInstance().getSession();
      transaction=session.beginTransaction();
      session.clear();
      regresar=toEntity(session, dto, process, idXml, params);
      transaction.commit();
    } // try
    catch (Exception e) {
      if (transaction!=null) {
        transaction.rollback();
      }
      throw e;
    } // catch
    finally {
      if (session!=null) {
        session.close();
      }
      session=null;
      transaction=null;
    } // finally
    return regresar;
  }

  public T toEntity(Long idFuenteDato, String process, String idXml, Map params) throws Exception {
    T regresar=null;
    Session session=null;
    Transaction transaction=null;
    try {
      session=SessionFactoryFacade.getInstance().getSession(idFuenteDato);
      transaction=session.beginTransaction();
      session.clear();
      regresar=toEntity(session, process, idXml, params);
      transaction.commit();
    } // try
    catch (Exception e) {
      if (transaction!=null) {
        transaction.rollback();
      }
      throw e;
    } // catch
    finally {
      if (session!=null) {
        session.close();
      }
      session=null;
      transaction=null;
    } // finally
    return regresar;
  }
	
  public T toSourceEntity(Long idFuenteDato, String process, String idXml, Map params) throws Exception {
    T regresar=null;
    Session session=null;
    Transaction transaction=null;
    try {
      session=SessionFactoryFacade.getInstance().getSession(idFuenteDato);
      transaction=session.beginTransaction();
      session.clear();
      regresar=toSourceEntity(session, process, idXml, params);
      transaction.commit();
    } // try
    catch (Exception e) {
      if (transaction!=null) {
        transaction.rollback();
      }
      throw e;
    } // catch
    finally {
      if (session!=null) {
        session.close();
      }
      session=null;
      transaction=null;
    } // finally
    return regresar;
  }

  public T toEntity(Long idFuenteDato, String sql) throws Exception {
    T regresar=null;
    Session session=null;
    Transaction transaction=null;
    try {
      session=SessionFactoryFacade.getInstance().getSession(idFuenteDato);
      transaction=session.beginTransaction();
      session.clear();
      regresar=toEntity(session, sql);
      transaction.commit();
    } // try
    catch (Exception e) {
      if (transaction!=null) {
        transaction.rollback();
      }
      throw e;
    } // catch
    finally {
      if (session!=null) {
        session.close();
      }
      session=null;
      transaction=null;
    } // finally
    return regresar;
  }
	
  public T toSourceEntity(Long idFuenteDato, String sql) throws Exception {
    T regresar=null;
    Session session=null;
    Transaction transaction=null;
    try {
      session=SessionFactoryFacade.getInstance().getSession(idFuenteDato);
      transaction=session.beginTransaction();
      session.clear();
      regresar=toSourceEntity(session, sql);
      transaction.commit();
    } // try
    catch (Exception e) {
      if (transaction!=null) {
        transaction.rollback();
      }
      throw e;
    } // catch
    finally {
      if (session!=null) {
        session.close();
      }
      session=null;
      transaction=null;
    } // finally
    return regresar;
  }

  public T toEntity(Long idFuenteDato, Class<IBaseDto> dto, Map params) throws Exception {
    T regresar=null;
    Session session=null;
    Transaction transaction=null;
    try {
      session=SessionFactoryFacade.getInstance().getSession(idFuenteDato);
      transaction=session.beginTransaction();
      session.clear();
      regresar=toEntity(session, dto, params);
      transaction.commit();
    } // try
    catch (Exception e) {
      if (transaction!=null) {
        transaction.rollback();
      }
      throw e;
    } // catch
    finally {
      if (session!=null) {
        session.close();
      }
      session=null;
      transaction=null;
    } // finally
    return regresar;
  }

  public T toEntity(Long idFuenteDato, Class<IBaseDto> dto) throws Exception {
    T regresar=null;
    Map params=null;
    try {
      params=new HashMap();
      params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
      regresar=toEntity(idFuenteDato, dto, params);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    finally {
      if (params!=null) {
        params.clear();
      }
      params=null;
    } // finally
    return regresar;
  }

  public T toEntity(Long idFuenteDato, Class<IBaseDto> dto, String sql) throws Exception {
    T regresar=null;
    Session session=null;
    Transaction transaction=null;
    try {
      session=SessionFactoryFacade.getInstance().getSession(idFuenteDato);
      transaction=session.beginTransaction();
      session.clear();
      regresar=toEntity(session, dto, sql);
      transaction.commit();
    } // try
    catch (Exception e) {
      if (transaction!=null) {
        transaction.rollback();
      }
      throw e;
    } // catch
    finally {
      if (session!=null) {
        session.close();
      }
      session=null;
      transaction=null;
    } // finally
    return regresar;
  }

  public T toEntity(Long idFuenteDato, Class<IBaseDto> dto, String process, Map params) throws Exception {
    return toEntity(idFuenteDato, dto, process, Constantes.DML_SELECT, params);
  }

  public T toEntity(Long idFuenteDato, Class<IBaseDto> dto, String process, String idXml, Map params) throws Exception {
    T regresar=null;
    Session session=null;
    Transaction transaction=null;
    try {
      session=SessionFactoryFacade.getInstance().getSession(idFuenteDato);
      transaction=session.beginTransaction();
      session.clear();
      regresar=toEntity(session, dto, process, idXml, params);
      transaction.commit();
    } // try
    catch (Exception e) {
      if (transaction!=null) {
        transaction.rollback();
      }
      throw e;
    } // catch
    finally {
      if (session!=null) {
        session.close();
      }
      session=null;
      transaction=null;
    } // finally
    return regresar;
  }

  public List<T> toEntitySet(Session session, String sql, Long records) throws Exception {
    List<T> regresar=null;
    try {
      regresar=toRecordsEntity(session, sql, records, new TransformEntity());
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  }

  public List<T> toEntitySet(Session session, String sql) throws Exception {
    return toEntitySet(session, sql, Constantes.SQL_MAXIMO_REGISTROS);
  }

  public List<T> toEntitySet(Session session, String process, String idXml, Map params, Long records) throws Exception {
    List<T> regresar=null;
    Dml dml=null;
    try {
      dml=Dml.getInstance();
      regresar=toEntitySet(session, dml.getSelect(process, idXml, params), records);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    finally {
      dml=null;
    } // finally
    return regresar;
  }

  public List<T> toEntitySet(Session session, String process, Map params, Long records) throws Exception {
    return toEntitySet(session, process, Constantes.DML_SELECT, params, records);
  }

  public List<T> toEntitySet(Session session, String process, String idXml, Map params) throws Exception {
    return toEntitySet(session, process, idXml, params, Constantes.SQL_MAXIMO_REGISTROS);
  }

  public List<T> toEntitySet(Session session, String process, Map params) throws Exception {
    return toEntitySet(session, process, Constantes.DML_SELECT, params, Constantes.SQL_MAXIMO_REGISTROS);
  }

  public List<T> toEntitySet(String process, Map params, Long records) throws Exception {
    return toEntitySet(process, Constantes.DML_SELECT, params, records);
  }

  public List<T> toEntitySet(String process, String idXml, Map params, Long records) throws Exception {
    List<T> regresar        =null;
    Session session         =null;
    Transaction transaction =null;
    try {
      session=SessionFactoryFacade.getInstance().getSession();
      transaction=session.beginTransaction();
      session.clear();
      regresar=toEntitySet(session, process, idXml, params, records);
      transaction.commit();
    } // try
    catch (Exception e) {
      if (transaction!=null) {
        transaction.rollback();
      }
      throw e;
    } // catch
    finally {
      if (session!=null) {
        session.close();
      }
      session=null;
      transaction=null;
    } // finally
    return regresar;
  }

  public List<T> toEntitySet(String process, String idXml, Map params) throws Exception {
    return toEntitySet(process, idXml, params, Constantes.SQL_MAXIMO_REGISTROS);
  }

  public List<T> toEntitySet(String process, Map params) throws Exception {
    return toEntitySet(process,  params, Constantes.SQL_MAXIMO_REGISTROS);
  }

  public List<T> toEntitySet(String sql, Long records) throws Exception {
    List<T> regresar        =null;
    Session session         =null;
    Transaction transaction =null;
    try {
      session=SessionFactoryFacade.getInstance().getSession();
      transaction=session.beginTransaction();
      session.clear();
      regresar=toEntitySet(session, sql, records);
      transaction.commit();
    } // try
    catch (Exception e) {
      if (transaction!=null) {
        transaction.rollback();
      }
      throw e;
    } // catch
    finally {
      if (session!=null) {
        session.close();
      }
      session=null;
      transaction=null;
    } // finally
    return regresar;
  }

  public List<T> toEntitySet(String sql) throws Exception {
    return toEntitySet(sql, Constantes.SQL_MAXIMO_REGISTROS);
  }

  public List<T> toEntitySet(Session session, String sql, Integer first, Integer records) throws Exception {
    List<T> regresar=null;
    try {
			regresar=toRecordsEntity(session, sql, first, records, new TransformEntity());
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  }

  public List<T> toEntitySet(Session session, String process, String idXml, Map params, Integer first, Integer records) throws Exception {
    List<T> regresar=null;
    Dml dml=null;
    try {
      dml=Dml.getInstance();
      regresar=toEntitySet(session, dml.getSelect(process, idXml, params), first, records);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    finally {
      dml=null;
    } // finally
    return regresar;
  }

  public List<T> toEntitySet(String process, String idXml, Map params, Integer first, Integer records) throws Exception {
    List<T> regresar=null;
    Session session=null;
    Transaction transaction=null;
    try {
      session    =SessionFactoryFacade.getInstance().getSession();
      transaction=session.beginTransaction();
      session.clear();
      regresar=toEntitySet(session, process, idXml, params, first, records);
      transaction.commit();
    } // try
    catch (Exception e) {
      if (transaction!=null) {
        transaction.rollback();
      }
      throw e;
    } // catch
    finally {
      if (session!=null) {
        session.close();
      }
      session=null;
      transaction=null;
    } // finally
    return regresar;
  }

  public List<T> toEntitySet(String sql, Integer first, Integer records) throws Exception {
    List<T> regresar        =null;
    Session session         =null;
    Transaction transaction =null;
    try {
      session=SessionFactoryFacade.getInstance().getSession();
      transaction=session.beginTransaction();
      session.clear();
      regresar=toEntitySet(session, sql, first, records);
      transaction.commit();
    } // try
    catch (Exception e) {
      if (transaction!=null) {
        transaction.rollback();
      }
      throw e;
    } // catch
    finally {
      if (session!=null)
        session.close();
      session=null;
      transaction=null;
    } // finally
    return regresar;
  }

  public List<T> toEntitySet(Session session, Class<IBaseDto> dto, String sql, Long records) throws Exception {
    List<T> regresar=null;
    SQLQuery query=null;
    try {
      query=session.createSQLQuery(sql);
      if (records!=Constantes.SQL_TODOS_REGISTROS)
        query.setMaxResults(records.intValue());
      regresar=query.setResultTransformer(new TransformDto(dto)).list();
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    finally {
      query=null;
    } // finally
    return regresar;
  }

  public List<T> toEntitySet(Session session, Class<IBaseDto> dto, String sql) throws Exception {
    return toEntitySet(session, dto, Collections.EMPTY_MAP, sql);
  }

  public List<T> toEntitySet(Session session, Class<IBaseDto> dto, Map params, String sql) throws Exception {
    return toEntitySet(session, dto, params, sql, Constantes.SQL_MAXIMO_REGISTROS);
  }

  public List<T> toEntitySet(Session session, Class<IBaseDto> dto, Map params, String sql, Long records) throws Exception {
    return toEntitySet(session, dto, Cadena.replaceParams(sql, params), records);
  }

  public List<T> toEntitySet(Session session, Class<IBaseDto> dto, Map params, Long records) throws Exception {
    List<T> regresar=null;
    Dml dml=null;
    try {
      dml=Dml.getInstance();
      regresar=toEntitySet(session, dto, dml.getSelect(dto.getSimpleName(), Constantes.DML_SELECT, params), records);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    finally {
      dml=null;
    } // finally
    return regresar;
  }

  public List<T> toEntitySet(Session session, Class<IBaseDto> dto, Map params) throws Exception {
    return toEntitySet(session, dto, params, Constantes.SQL_MAXIMO_REGISTROS);
  }

  public List<T> toEntitySet(Session session, Class<IBaseDto> dto, String process, String idXml, Map params, Long records) throws Exception {
    List<T> regresar=null;
    Dml dml=null;
    try {
      dml=Dml.getInstance();
      regresar=toEntitySet(session, dto, dml.getSelect(process, idXml, params), records);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    finally {
      dml=null;
    } // finally
    return regresar;
  }

  public List<T> toEntitySet(Session session, Class<IBaseDto> dto, String process, Map params, Long records) throws Exception {
    return toEntitySet(session, dto, process, Constantes.DML_SELECT, params, records);
  }

  public List<T> toEntitySet(Session session, Class<IBaseDto> dto, String process, String idXml, Map params) throws Exception {
    return toEntitySet(session, dto, process, idXml, params, Constantes.SQL_MAXIMO_REGISTROS);
  }

  public List<T> toEntitySet(Session session, Class<IBaseDto> dto, String process, Map params) throws Exception {
    return toEntitySet(session, dto, process, Constantes.DML_SELECT, params, Constantes.SQL_MAXIMO_REGISTROS);
  }

  public List<T> toEntitySet(Class<IBaseDto> dto, Map params, Long records) throws Exception {
    List<T> regresar       =null;
    Session session        =null;
    Transaction transaction=null;
    try {
      session=SessionFactoryFacade.getInstance().getSession();
      transaction=session.beginTransaction();
      session.clear();
      regresar=toEntitySet(session, dto, params, records);
      transaction.commit();
    } // try
    catch (Exception e) {
      if (transaction!=null) {
        transaction.rollback();
      }
      throw e;
    } // catch
    finally {
      if (session!=null) {
        session.close();
      }
      session=null;
      transaction=null;
    } // finally
    return regresar;
  }

  public List<T> toEntitySet(Class<IBaseDto> dto, Map params) throws Exception {
    return toEntitySet(dto, params, Constantes.SQL_MAXIMO_REGISTROS);
  }

  public List<T> toEntitySet(Class<IBaseDto> dto, Long records) throws Exception {
    List<T> regresar=null;
    Map<String, Object> params=new HashMap<>();
    try {
      params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
      regresar=toEntitySet(dto, params, records);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    finally {
      Methods.clean(params);
    } // finally
    return regresar;
  }

  public List<T> toEntitySet(Class<IBaseDto> dto) throws Exception {
    return toEntitySet(dto, Constantes.SQL_MAXIMO_REGISTROS);
  }

  public List<T> toEntitySet(Class<IBaseDto> dto, String process, String idXml, Map params, Long records) throws Exception {
    List<T> regresar        =null;
    Session session         =null;
    Transaction transaction =null;
    try {
      session=SessionFactoryFacade.getInstance().getSession();
      transaction=session.beginTransaction();
      session.clear();
      regresar=toEntitySet(session, dto, process, idXml, params, records);
      transaction.commit();
    } // try
    catch (Exception e) {
      if (transaction!=null) {
        transaction.rollback();
      }
      throw e;
    } // catch
    finally {
      if (session!=null) {
        session.close();
      }
      session=null;
      transaction=null;
    } // finally
    return regresar;
  }

  public List<T> toEntitySet(Class<IBaseDto> dto, String process, Map params, Long records) throws Exception {
    return toEntitySet(dto, process, Constantes.DML_SELECT, params, records);
  }

  public List<T> toEntitySet(Class<IBaseDto> dto, String process, String idXml, Map params) throws Exception {
    return toEntitySet(dto, process, idXml, params, Constantes.SQL_MAXIMO_REGISTROS);
  }

  public List<T> toEntitySet(Class<IBaseDto> dto, String sql, Long records) throws Exception {
    List<T> regresar        =null;
    Session session         =null;
    Transaction transaction =null;
    try {
      session=SessionFactoryFacade.getInstance().getSession();
      transaction=session.beginTransaction();
      session.clear();
      regresar=toEntitySet(session, dto, sql, records);
      transaction.commit();
    } // try
    catch (Exception e) {
      if (transaction!=null) {
        transaction.rollback();
      }
      throw e;
    } // catch
    finally {
      if (session!=null) {
        session.close();
      }
      session=null;
      transaction=null;
    } // finally
    return regresar;
  }

  public List<T> toEntitySet(Class<IBaseDto> dto, String sql) throws Exception {
    return toEntitySet(dto, Collections.EMPTY_MAP, sql);
  }

  public List<T> toEntitySet(Class<IBaseDto> dto, Map params, String sql) throws Exception {
    return toEntitySet(dto, params, sql, Constantes.SQL_MAXIMO_REGISTROS);
  }

  public List<T> toEntitySet(Class<IBaseDto> dto, Map params, String sql, Long records) throws Exception {
    return toEntitySet(dto, Cadena.replaceParams(sql, params), records);
  }

  public List<T> toEntitySet(Class<IBaseDto> dto, String process, Map params) throws Exception {
    List<T> regresar=null;
    Dml dml=null;
    try {
      dml=Dml.getInstance();
      regresar=toEntitySet(dto, dml.getSelect(process, Constantes.DML_SELECT, params), Constantes.SQL_MAXIMO_REGISTROS);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    finally {
      dml=null;
    } // finally
    return regresar;
  }

  public List<T> toEntitySet(Long idFuenteDato, String process, Map params, Long records) throws Exception {
    return toEntitySet(idFuenteDato, process, Constantes.DML_SELECT, params, records);
  }

  public List<T> toEntitySet(Long idFuenteDato, String process, String idXml, Map params, Long records) throws Exception {
    List<T> regresar=null;
    Session session=null;
    Transaction transaction=null;
    try {
      session=SessionFactoryFacade.getInstance().getSession(idFuenteDato);
      transaction=session.beginTransaction();
      session.clear();
      regresar=toEntitySet(session, process, idXml, params, records);
      transaction.commit();
    } // try
    catch (Exception e) {
      if (transaction!=null) {
        transaction.rollback();
      }
      throw e;
    } // catch
    finally {
      if (session!=null) {
        session.close();
      }
      session=null;
      transaction=null;
    } // finally
    return regresar;
  }

  public List<T> toEntitySet(Long idFuenteDato, String process, String idXml, Map params) throws Exception {
    return toEntitySet(idFuenteDato, process, idXml, params, Constantes.SQL_MAXIMO_REGISTROS);
  }

  public List<T> toEntitySet(Long idFuenteDato, String sql, Long records) throws Exception {
    List<T> regresar=null;
    Session session=null;
    Transaction transaction=null;
    try {
      session=SessionFactoryFacade.getInstance().getSession(idFuenteDato);
      transaction=session.beginTransaction();
      session.clear();
      regresar=toEntitySet(session, sql, records);
      transaction.commit();
    } // try
    catch (Exception e) {
      if (transaction!=null) {
        transaction.rollback();
      }
      throw e;
    } // catch
    finally {
      if (session!=null) {
        session.close();
      }
      session=null;
      transaction=null;
    } // finally
    return regresar;
  }

  public List<T> toEntitySet(Long idFuenteDato, String sql) throws Exception {
    return toEntitySet(idFuenteDato, sql, Constantes.SQL_MAXIMO_REGISTROS);
  }

  public List<T> toEntitySet(Long idFuenteDato, String process, String idXml, Map params, Integer first, Integer records) throws Exception {
    List<T> regresar=null;
    Session session=null;
    Transaction transaction=null;
    try {
      session=SessionFactoryFacade.getInstance().getSession(idFuenteDato);
      transaction=session.beginTransaction();
      session.clear();
      regresar=toEntitySet(session, process, idXml, params, first, records);
      transaction.commit();
    } // try
    catch (Exception e) {
      if (transaction!=null) {
        transaction.rollback();
      }
      throw e;
    } // catch
    finally {
      if (session!=null) {
        session.close();
      }
      session=null;
      transaction=null;
    } // finally
    return regresar;
  }

  public List<T> toEntitySet(Long idFuenteDato, String sql, Integer first, Integer records) throws Exception {
    List<T> regresar=null;
    Session session=null;
    Transaction transaction=null;
    try {
      session=SessionFactoryFacade.getInstance().getSession(idFuenteDato);
      transaction=session.beginTransaction();
      session.clear();
      regresar=toEntitySet(session, sql, first, records);
      transaction.commit();
    } // try
    catch (Exception e) {
      if (transaction!=null) {
        transaction.rollback();
      }
      throw e;
    } // catch
    finally {
      if (session!=null) {
        session.close();
      }
      session=null;
      transaction=null;
    } // finally
    return regresar;
  }

  public List<T> toEntitySet(Long idFuenteDato, Class<IBaseDto> dto, Map params, Long records) throws Exception {
    List<T> regresar=null;
    Session session=null;
    Transaction transaction=null;
    try {
      session=SessionFactoryFacade.getInstance().getSession(idFuenteDato);
      transaction=session.beginTransaction();
      session.clear();
      regresar=toEntitySet(session, dto, params, records);
      transaction.commit();
    } // try
    catch (Exception e) {
      if (transaction!=null) {
        transaction.rollback();
      }
      throw e;
    } // catch
    finally {
      if (session!=null) {
        session.close();
      }
      session=null;
      transaction=null;
    } // finally
    return regresar;
  }

  public List<T> toEntitySet(Long idFuenteDato, Class<IBaseDto> dto, Map params) throws Exception {
    return toEntitySet(idFuenteDato, dto, params, Constantes.SQL_MAXIMO_REGISTROS);
  }

  public List<T> toEntitySet(Long idFuenteDato, Class<IBaseDto> dto, Long records) throws Exception {
    List<T> regresar=null;
    Map params=null;
    try {
      params=new HashMap();
      params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
      regresar=toEntitySet(idFuenteDato, dto, params, records);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    finally {
      Methods.clean(params);
    } // finally
    return regresar;
  }

  public List<T> toEntitySet(Long idFuenteDato, Class<IBaseDto> dto) throws Exception {
    return toEntitySet(idFuenteDato, dto, Constantes.SQL_MAXIMO_REGISTROS);
  }

  public List<T> toEntitySet(Long idFuenteDato, Class<IBaseDto> dto, String process, String idXml, Map params, Long records) throws Exception {
    List<T> regresar=null;
    Session session=null;
    Transaction transaction=null;
    try {
      session=SessionFactoryFacade.getInstance().getSession(idFuenteDato);
      transaction=session.beginTransaction();
      session.clear();
      regresar=toEntitySet(session, dto, process, idXml, params, records);
      transaction.commit();
    } // try
    catch (Exception e) {
      if (transaction!=null) {
        transaction.rollback();
      }
      throw e;
    } // catch
    finally {
      if (session!=null) {
        session.close();
      }
      session=null;
      transaction=null;
    } // finally
    return regresar;
  }

  public List<T> toEntitySet(Long idFuenteDato, Class<IBaseDto> dto, String process, Map params, Long records) throws Exception {
    return toEntitySet(idFuenteDato, dto, process, Constantes.DML_SELECT, params, records);
  }

  public List<T> toEntitySet(Long idFuenteDato, Class<IBaseDto> dto, String process, String idXml, Map params) throws Exception {
    return toEntitySet(idFuenteDato, dto, process, idXml, params, Constantes.SQL_MAXIMO_REGISTROS);
  }

  public List<T> toEntitySet(Long idFuenteDato, Class<IBaseDto> dto, String sql, Long records) throws Exception {
    List<T> regresar=null;
    Session session=null;
    Transaction transaction=null;
    try {
      session=SessionFactoryFacade.getInstance().getSession(idFuenteDato);
      transaction=session.beginTransaction();
      session.clear();
      regresar=toEntitySet(session, dto, sql, records);
      transaction.commit();
    } // try
    catch (Exception e) {
      if (transaction!=null) {
        transaction.rollback();
      }
      throw e;
    } // catch
    finally {
      if (session!=null) {
        session.close();
      }
      session=null;
      transaction=null;
    } // finally
    return regresar;
  }

  public List<T> toEntitySet(Long idFuenteDato, Class<IBaseDto> dto, String sql) throws Exception {
    return toEntitySet(idFuenteDato, dto, sql, Constantes.SQL_MAXIMO_REGISTROS);
  }

  public List<T> toEntitySet(Long idFuenteDato, Class<IBaseDto> dto, String process, Map params) throws Exception {
    List<T> regresar=null;
    Dml dml=null;
    try {
      dml=Dml.getInstance();
      params=Collections.unmodifiableMap(params);
      regresar=toEntitySet(idFuenteDato, dto, dml.getSelect(process, Constantes.DML_SELECT, params), Constantes.SQL_MAXIMO_REGISTROS);
      params=new HashMap(params);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    finally {
      dml=null;
    } // finally
    return regresar;
  }

  public List<T> toEntityPage(Session session, Class<IBaseDto> dto, String sql, Integer first, Integer records) throws Exception {
    List<T> regresar=null;
    SQLQuery query=null;
    try {
      query=session.createSQLQuery(sql);
      query.setFirstResult(first);
      query.setMaxResults(records);
      regresar=query.setResultTransformer(new TransformDto(dto)).list();
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    finally {
      query=null;
    } // finally
    return regresar;
  }

  public List<T> toEntityPage(Session session, Class<IBaseDto> dto, String process, String idXml, Map params, Integer first, Integer records) throws Exception {
    List<T> regresar=null;
    Dml dml=null;
    try {
      dml=Dml.getInstance();
      regresar=toEntityPage(session, dto, dml.getSelect(process, idXml, params), first, records);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    finally {
      dml=null;
    } // finally
    return regresar;
  }

  public List<T> toEntityPage(Class<IBaseDto> dto, String process, String idXml, Map params, Integer first, Integer records) throws Exception {
    List<T> regresar=null;
    Session session=null;
    Transaction transaction=null;
    try {
      session=SessionFactoryFacade.getInstance().getSession();
      transaction=session.beginTransaction();
      session.clear();
      regresar=toEntityPage(session, dto, process, idXml, params, first, records);
      transaction.commit();
    } // try
    catch (Exception e) {
      if (transaction!=null) {
        transaction.rollback();
      }
      throw e;
    } // catch
    finally {
      if (session!=null) {
        session.close();
      }
      session=null;
      transaction=null;
    } // finally
    return regresar;
  }

  public List<T> toEntityPage(Class<IBaseDto> dto, String sql, Integer first, Integer records) throws Exception {
    List<T> regresar=null;
    Session session=null;
    Transaction transaction=null;
    try {
      session=SessionFactoryFacade.getInstance().getSession();
      transaction=session.beginTransaction();
      session.clear();
      regresar=toEntityPage(session, dto, sql, first, records);
      transaction.commit();
    } // try
    catch (Exception e) {
      if (transaction!=null) {
        transaction.rollback();
      }
      throw e;
    } // catch
    finally {
      if (session!=null) {
        session.close();
      }
      session=null;
      transaction=null;
    } // finally
    return regresar;
  }

  public List<T> toEntityPage(Class<IBaseDto> dto, String process, Map params, Integer first, Integer records) throws Exception {
    return toEntityPage(dto, process, Constantes.DML_SELECT, params, first, records);
  }

  public List<T> toEntityPage(Class<IBaseDto> dto, Map params, Integer first, Integer records) throws Exception {
    return toEntityPage(dto, dto.getSimpleName(), Constantes.DML_SELECT, params, first, records);
  }

  public List<T> toEntityPage(Class<IBaseDto> dto, Integer first, Integer records) throws Exception {
    List<T> regresar=null;
    Map<String, Object> params=new HashMap<>();
    try {
      params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
      regresar=toEntityPage(dto, params, first, records);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    finally {
			Methods.clean(params);
    } // finally
    return regresar;
  }

  public PageRecords toEntityPage(Session session, String sql, Integer first, Integer records) throws Exception {
    PageRecords regresar=null;
    SQLQuery query=null;
    List<IBaseDto> list=null;
    try {
      query=session.createSQLQuery(sql);
      query.setFirstResult(first);
      query.setMaxResults(records);
      list=query.setResultTransformer(new TransformEntity()).list();
      String total="select count(*) count from (".concat(sql).concat(") datos");
      Long count=((Number) toField(session, total, "count").getData()).longValue();
      regresar=new PageRecords((int)(first/records), records, count.intValue(), list);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    finally {
      query=null;
    } // finally
    return regresar;
  }

  public PageRecords toEntityPage(Session session, String process, String idXml, Map<String, Object> params, Integer first, Integer records) throws Exception {
    PageRecords regresar=null;
    Dml dml=null;
    try {
      dml=Dml.getInstance();
      regresar=toEntityPage(session, dml.getSelect(process, idXml, params), first, records);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    finally {
      dml=null;
    } // finally
    return regresar;
  }

  public PageRecords toEntityPage(String process, String idXml, Map<String, Object> params, Integer first, Integer records) throws Exception {
    PageRecords regresar=null;
    Session session=null;
    Transaction transaction=null;
    try {
      session=SessionFactoryFacade.getInstance().getSession();
      transaction=session.beginTransaction();
      session.clear();
      regresar=toEntityPage(session, process, idXml, params, first, records);
      transaction.commit();
    } // try
    catch (Exception e) {
      if (transaction!=null) {
        transaction.rollback();
      }
      throw e;
    } // catch
    finally {
      if (session!=null) {
        session.close();
      }
      session=null;
      transaction=null;
    } // finally
    return regresar;
  }

  public PageRecords toEntityPage(Long idFuenteDato, String process, String idXml, Map<String, Object> params, Integer first, Integer records) throws Exception {
    PageRecords regresar=null;
    Session session=null;
    Transaction transaction=null;
    try {
      session=SessionFactoryFacade.getInstance().getSession(idFuenteDato);
      transaction=session.beginTransaction();
      session.clear();
      regresar=toEntityPage(session, process, idXml, params, first, records);
      transaction.commit();
    } // try
    catch (Exception e) {
      if (transaction!=null) {
        transaction.rollback();
      }
      throw e;
    } // catch
    finally {
      if (session!=null) {
        session.close();
      }
      session=null;
      transaction=null;
    } // finally
    return regresar;
  }

  public PageRecords toEntityPage(String sql, Integer first, Integer records) throws Exception {
    PageRecords regresar=null;
    Session session=null;
    Transaction transaction=null;
    try {
      session=SessionFactoryFacade.getInstance().getSession();
      transaction=session.beginTransaction();
      session.clear();
      regresar=toEntityPage(session, sql, first, records);
      transaction.commit();
    } // try
    catch (Exception e) {
      if (transaction!=null) {
        transaction.rollback();
      }
      throw e;
    } // catch
    finally {
      if (session!=null) {
        session.close();
      }
      session=null;
      transaction=null;
    } // finally
    return regresar;
  }

  public PageRecords toEntityPage(Session session, String process, Map<String, Object> params, Integer first, Integer records) throws Exception {
    return toEntityPage(session, process, Constantes.DML_SELECT, params, first, records);
  }

  public PageRecords toEntityPage(String process, Map<String, Object> params, Integer first, Integer records) throws Exception {
    return toEntityPage(process, Constantes.DML_SELECT, params, first, records);
  }

  public PageRecords toEntityPage(String process, String idXml, Integer first, Integer records) throws Exception {
    PageRecords regresar=null;
    Map<String, Object> params=new HashMap<>();
    try {
      params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
      regresar=toEntityPage(process, idXml, params, first, records);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    finally {
			Methods.clean(params);
    } // finally
    return regresar;
  }

  public List<T> toEntityPage(Long idFuenteDato, Class<IBaseDto> dto, String process, String idXml, Map params, Integer first, Integer records) throws Exception {
    List<T> regresar=null;
    Session session=null;
    Transaction transaction=null;
    try {
      session=SessionFactoryFacade.getInstance().getSession(idFuenteDato);
      transaction=session.beginTransaction();
      session.clear();
      regresar=toEntityPage(session, dto, process, idXml, params, first, records);
      transaction.commit();
    } // try
    catch (Exception e) {
      if (transaction!=null) {
        transaction.rollback();
      }
      throw e;
    } // catch
    finally {
      if (session!=null) {
        session.close();
      }
      session=null;
      transaction=null;
    } // finally
    return regresar;
  }

  public PageRecords toEntityPage(Long idFuenteDato, String process, String idXml, Integer first, Integer records) throws Exception {
    PageRecords regresar       =null;
    Map<String, Object> params =null;
    try {
      params=new HashMap<>();
      params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
      regresar=toEntityPage(idFuenteDato, process, idXml, params, first, records);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    finally {
      if (params!=null) {
        params.clear();
      }
      params=null;
    } // finally
    return regresar;
  }

  public PageRecords toEntityPage(Long idFuenteDato, String process, Map<String, Object> params, Integer first, Integer records) throws Exception {
    return toEntityPage(idFuenteDato, process, Constantes.DML_SELECT, params, first, records);
  }

  public PageRecords toEntityPage(Long idFuenteDato, String sql, Integer first, Integer records) throws Exception {
    PageRecords regresar=null;
    Session session=null;
    Transaction transaction=null;
    try {
      session=SessionFactoryFacade.getInstance().getSession(idFuenteDato);
      transaction=session.beginTransaction();
      session.clear();
      regresar=toEntityPage(session, sql, first, records);
      transaction.commit();
    } // try
    catch (Exception e) {
      if (transaction!=null) {
        transaction.rollback();
      }
      throw e;
    } // catch
    finally {
      if (session!=null) {
        session.close();
      }
      session=null;
      transaction=null;
    } // finally
    return regresar;
  }

  public Long execute(Session session, String sql) throws Exception {
    Long regresar=-1L;
    SQLQuery query=null;
    try {
      query=session.createSQLQuery(sql);
      regresar=Long.valueOf(query.executeUpdate());
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    finally {
      query=null;
    } // finally
    return regresar;
  }

  public Long execute(ESql action, Session session, String process, String idXml, Map<String, Object> params) throws Exception {
    Long regresar=-1L;
    Dml dml=null;
    try {
      dml=Dml.getInstance();
      regresar=Long.valueOf(execute(session, dml.getDML(process, idXml, params, action)));
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    finally {
      dml=null;
    } // finally
    return regresar;
  }

  public Long execute(ESql action, Session session, String process, String idXml, String params) throws Exception {
    Long regresar=-1L;
    Variables vars=null;
    try {
      vars=new Variables(params, '|');
      regresar=execute(action, session, process, idXml, vars.getMap());
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    finally {
      vars=null;
    }
    return regresar;
  }

  public Long execute(ESql action, String process, String idXml, Map<String, Object> params) throws Exception {
		return execute(-1L, action, process, idXml, params);
	}
	
  public Long execute(Long idFuenteDato, ESql action, String process, String idXml, Map<String, Object> params) throws Exception {
    Long regresar           =-1L;
    Session session         =null;
    Transaction transaction =null;
    try {
      session=SessionFactoryFacade.getInstance().getSession(idFuenteDato);
      transaction=session.beginTransaction();
      session.clear();
      regresar=execute(action, session, process, idXml, params);
      transaction.commit();
    } // try
    catch (Exception e) {
      if (transaction!=null) {
        transaction.rollback();
      }
      throw e;
    } // catch
    finally {
      if (session!=null) {
        session.close();
      }
      session=null;
      transaction=null;
    } // finally
    return regresar;
  }

  public Long execute(ESql action, String process, String idXml, String params) throws Exception {
		return execute(-1L, action, process, idXml, params);
	}
	
  public Long execute(Long idFuenteDato, ESql action, String process, String idXml, String params) throws Exception {
    Long regresar=-1L;
    Variables vars=null;
    try {
      vars=new Variables(params, '|');
      regresar=execute(idFuenteDato, action, process, idXml, vars.getMap());
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    finally {
      vars=null;
    } // finally
    return regresar;
  }
	
  public Long execute(String sql) throws Exception {
		return execute(-1L, sql);
	}
	
  public Long execute(Long idFuenteDato, String sql) throws Exception {
    Long regresar=-1L;
    Session session=null;
    Transaction transaction=null;
    try {
      session=SessionFactoryFacade.getInstance().getSession(idFuenteDato);
      transaction=session.beginTransaction();
      session.clear();
      regresar=execute(session, sql);
      transaction.commit();
    } // try
    catch (Exception e) {
      if (transaction!=null) {
        transaction.rollback();
      }
      throw e;
    } // catch
    finally {
      if (session!=null) {
        session.close();
      }
      session=null;
      transaction=null;
    } // finally
    return regresar;
  }

  public Connection getConnection(Session session) throws Exception {
    Connection regresar=null;
    try {
			regresar=((SessionImplementor) session).getJdbcConnectionAccess().obtainConnection();
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  }

  public Connection getConnection() throws Exception {
    Connection regresar=null;
    Session session=null;
    try {
      session=SessionFactoryFacade.getInstance().getSession();
      regresar=getConnection(session);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  }

  public Connection getConnection(Long idFuenteDato) throws Exception {
    Session session=null;
    Connection regresar=null;
    try {
      session=SessionFactoryFacade.getInstance().getSession(idFuenteDato);
      regresar=getConnection(session);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  }

  public Long toSize(Session session, String sql) throws Exception {
    Long regresar= -1L;
    try {
      String total="select count(*) count from (".concat(sql).concat(") datos");
      regresar= ((Number)toField(session, total, "count").getData()).longValue();
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  }

  public Long toSize(Session session, String process, String idXml, Map params) throws Exception {
    Long regresar= -1L;
    Dml dml      = null;
    try {
      dml= Dml.getInstance();
      regresar= toSize(session, dml.getSelect(process, idXml, params));
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    finally {
      dml=null;
    } // finally
    return regresar;
  }

  public Long toSize(Session session, String process, Map params) throws Exception {
    return toSize(session, process, Constantes.DML_SELECT, params);
  }

  public Long toSize(String process, String idXml, Map params) throws Exception {
    Long regresar  = -1L;
    Session session= null;
    Transaction transaction=null;
    try {
      session     = SessionFactoryFacade.getInstance().getSession();
      transaction = session.beginTransaction();
      session.clear();
      regresar= toSize(session, process, idXml, params);
      transaction.commit();
    } // try
    catch (Exception e) {
      if (transaction!=null) {
        transaction.rollback();
      }
      throw e;
    } // catch
    finally {
      if (session!=null) {
        session.close();
      }
      session=null;
      transaction=null;
    } // finally
    return regresar;
  }

  public Long toSize(Long idFuenteDato, String process, Map params) throws Exception {
    return toSize(idFuenteDato, process, Constantes.DML_SELECT, params);
  }

  public Long toSize(Long idFuenteDato, String process, String idXml, Map params) throws Exception {
    Long regresar  = -1L;
    Session session= null;
    Transaction transaction=null;
    try {
      session=SessionFactoryFacade.getInstance().getSession(idFuenteDato);
      transaction=session.beginTransaction();
      session.clear();
      regresar= toSize(session, process, idXml, params);
      transaction.commit();
    } // try
    catch (Exception e) {
      if (transaction!=null) {
        transaction.rollback();
      }
      throw e;
    } // catch
    finally {
      if (session!=null) {
        session.close();
      }
      session=null;
      transaction=null;
    } // finally
    return regresar;
  }

  public Long toSize(String process, Map params) throws Exception {
    return toSize(process, Constantes.DML_SELECT, params);
  }

  public Long toSize(String sql) throws Exception {
    Long regresar          =-1L;
    Session session        =null;
    Transaction transaction=null;
    try {
      session     = SessionFactoryFacade.getInstance().getSession();
      transaction = session.beginTransaction();
      session.clear();
      regresar= toSize(session, sql);
      transaction.commit();
    } // try
    catch (Exception e) {
      if (transaction!=null) {
        transaction.rollback();
      }
      throw e;
    } // catch
    finally {
      if (session!=null) {
        session.close();
      }
      session=null;
      transaction=null;
    } // finally
    return regresar;
  }

  public Long toSize(Long idFuenteDato, String sql) throws Exception {
    Long regresar  = -1L;
    Session session= null;
    Transaction transaction=null;
    try {
      session=SessionFactoryFacade.getInstance().getSession(idFuenteDato);
      transaction=session.beginTransaction();
      session.clear();
      regresar= toSize(session, sql);
      transaction.commit();
    } // try
    catch (Exception e) {
      if (transaction!=null) {
        transaction.rollback();
      }// if
      throw e;
    } // catch
    finally {
      if (session!=null) {
        session.close();
      }
      session=null;
      transaction=null;
    } // finally
    return regresar;
  }
	
 public List<T> toSourceEntitySet(String sql) throws Exception {
    return toSourceEntitySet(sql, Constantes.SQL_MAXIMO_REGISTROS);
 }

 public List<T> toSourceEntitySet(String sql, Long records) throws Exception {
    List<T> regresar        =null;
    Session session         =null;
    Transaction transaction =null;
    try {
      session=SessionFactoryFacade.getInstance().getSession();
      transaction=session.beginTransaction();
      session.clear();
      regresar=toSourceEntitySet(session, sql, records);
      transaction.commit();
    } // try
    catch (Exception e) {
      if (transaction!=null) {
        transaction.rollback();
      }// if
      throw e;
    } // catch
    finally {
      if (session!=null) {
        session.close();
      } // if
      session=null;
      transaction=null;
    } // finally
    return regresar;
  }

  public List<T> toSourceEntitySet(Session session, String sql, Long records) throws Exception {
    List<T> regresar=null;
    try {
      regresar=toRecordsEntity(session, sql, records,new TransformSourceEntity());
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  }

  private List<T> toRecordsEntity(Session session, String sql,Transformer transformer) throws Exception{
		List<T> regresar=null;
		SQLQuery query  =null;
		try {
			query=session.createSQLQuery(sql);
      query.setMaxResults(Constantes.SQL_PRIMER_REGISTRO);
			regresar=query.setResultTransformer(transformer).list();
		} // try
		catch (Exception e) {
			throw e;
		} // catch
		finally {
			query = null;
		}//finally
		return regresar;
	}

  private List<T> toRecordsEntity(Session session, String sql, Long records, Transformer transformer) throws Exception{
		List<T> regresar=null;
		SQLQuery query  =null;
		try {
			query=session.createSQLQuery(sql);
      if (records!=Constantes.SQL_TODOS_REGISTROS) {
        query.setMaxResults(records.intValue());
      }
			regresar=query.setResultTransformer(transformer).list();
		} // try
		catch (Exception e) {
			throw e;
		} // catch
		finally {
			query = null;
		}//finally
		return regresar;
	}
	
  private List<T> toRecordsEntity(Session session, String sql, Integer first, Integer records,Transformer transformer) throws Exception{
		List<T> regresar=null;
		SQLQuery query  =null;
		try {
			query=session.createSQLQuery(sql);
      query.setFirstResult(first);
      query.setMaxResults(records);
      regresar=query.setResultTransformer(transformer).list();
		} // try
		catch (Exception e) {
			throw e;
		} // catch
		finally {
			query = null;
		}//finally
		return regresar;
	}
	
	public List<T> toSourceEntitySet(Session session, String sql) throws Exception {
    return toSourceEntitySet(session, sql, Constantes.SQL_MAXIMO_REGISTROS);
  }

  public List<T> toSourceEntitySet(Session session, String process, String idXml, Map params, Long records) throws Exception {
    List<T> regresar=null;
    Dml dml=null;
    try {
      dml=Dml.getInstance();
      regresar=toSourceEntitySet(session, dml.getSelect(process, idXml, params), records);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    finally {
      dml=null;
    } // finally
    return regresar;
  }

  public List<T> toSourceEntitySet(Session session, String process, Map params, Long records) throws Exception {
    return toSourceEntitySet(session, process, Constantes.DML_SELECT, params, records);
  }

  public List<T> toSourceEntitySet(Session session, String process, String idXml, Map params) throws Exception {
    return toSourceEntitySet(session, process, idXml, params, Constantes.SQL_MAXIMO_REGISTROS);
  }

  public List<T> toSourceEntitySet(Session session, String process, Map params) throws Exception {
    return toSourceEntitySet(session, process, Constantes.DML_SELECT, params, Constantes.SQL_MAXIMO_REGISTROS);
  }

  public List<T> toSourceEntitySet(String process, Map params, Long records) throws Exception {
    return toSourceEntitySet(process, Constantes.DML_SELECT, params, records);
  }

  public List<T> toSourceEntitySet(String process, String idXml, Map params, Long records) throws Exception {
    List<T> regresar        = null;
    Session session         = null;
    Transaction transaction = null;
    try {
      session      = SessionFactoryFacade.getInstance().getSession();
      transaction = session.beginTransaction();
      session.clear();
      regresar=toSourceEntitySet(session, process, idXml, params, records);
      transaction.commit();
    } // try
    catch (Exception e) {
      if (transaction!=null) {
        transaction.rollback();
      } // if
      throw e;
    } // catch
    finally {
      if (session!=null) {
        session.close();
      }
      session=null;
      transaction=null;
    } // finally
    return regresar;
  }

  public List<T> toSourceEntitySet(String process, String idXml, Map params) throws Exception {
    return toSourceEntitySet(process, idXml, params, Constantes.SQL_MAXIMO_REGISTROS);
  }

  public List<T> toSourceEntitySet(String process, Map params) throws Exception {
    return toSourceEntitySet(process,  params, Constantes.SQL_MAXIMO_REGISTROS);
  }
	
  public List<T> toSourceEntitySet(Session session, String sql, Integer first, Integer records) throws Exception {
    List<T> regresar=null;
    try {
			regresar = toRecordsEntity(session, sql, first, records, new TransformSourceEntity());
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  }

  public List<T> toSourceEntitySet(Session session, String process, String idXml, Map params, Integer first, Integer records) throws Exception {
    List<T> regresar=null;
    Dml dml=null;
    try {
      dml=Dml.getInstance();
      regresar=toSourceEntitySet(session, dml.getSelect(process, idXml, params), first, records);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    finally {
      dml=null;
    } // finally
    return regresar;
  }

  public List<T> toSourceEntitySet(String process, String idXml, Map params, Integer first, Integer records) throws Exception {
    List<T> regresar=null;
    Session session=null;
    Transaction transaction=null;
    try {
      session=SessionFactoryFacade.getInstance().getSession();
      transaction=session.beginTransaction();
      session.clear();
      regresar=toSourceEntitySet(session, process, idXml, params, first, records);
      transaction.commit();
    } // try
    catch (Exception e) {
      if (transaction!=null) {
        transaction.rollback();
      } // if
      throw e;
    } // catch
    finally {
      if (session!=null) {
        session.close();
      } // if
      session=null;
      transaction=null;
    } // finally
    return regresar;
  }

  public List<T> toSourceEntitySet(String sql, Integer first, Integer records) throws Exception {
    List<T> regresar       =null;
    Session session        =null;
    Transaction transaction=null;
    try {
      session    =SessionFactoryFacade.getInstance().getSession();
      transaction=session.beginTransaction();
      session.clear();
      regresar=toSourceEntitySet(session, sql, first, records);
      transaction.commit();
    } // try
    catch (Exception e) {
      if (transaction!=null) {
        transaction.rollback();
      } // if
      throw e;
    } // catch
    finally {
      if (session!=null) {
        session.close();
      } // if
      session=null;
      transaction=null;
    } // finally
    return regresar;
  }

  public List<T> toSourceEntitySet(Long idFuenteDato, String process, Map params, Long records) throws Exception {
    return toSourceEntitySet(idFuenteDato, process, Constantes.DML_SELECT, params, records);
  }

  public List<T> toSourceEntitySet(Long idFuenteDato, String process, String idXml, Map params, Long records) throws Exception {
    List<T> regresar=null;
    Session session=null;
    Transaction transaction=null;
    try {
      session=SessionFactoryFacade.getInstance().getSession(idFuenteDato);
      transaction=session.beginTransaction();
      session.clear();
      regresar=toSourceEntitySet(session, process, idXml, params, records);
      transaction.commit();
    } // try
    catch (Exception e) {
      if (transaction!=null) {
        transaction.rollback();
      } // if
      throw e;
    } // catch
    finally {
      if (session!=null) {
        session.close();
      }
      session=null;
      transaction=null;
    } // finally
    return regresar;
  }

  public List<T> toSourceEntitySet(Long idFuenteDato, String process, String idXml, Map params) throws Exception {
    return toSourceEntitySet(idFuenteDato, process, idXml, params, Constantes.SQL_MAXIMO_REGISTROS);
  }

  public List<T> toSourceEntitySet(Long idFuenteDato, String sql, Long records) throws Exception {
    List<T> regresar=null;
    Session session=null;
    Transaction transaction=null;
    try {
      session=SessionFactoryFacade.getInstance().getSession(idFuenteDato);
      transaction=session.beginTransaction();
      session.clear();
      regresar=toSourceEntitySet(session, sql, records);
      transaction.commit();
    } // try
    catch (Exception e) {
      if (transaction!=null) {
        transaction.rollback();
      } //
      throw e;
    } // catch
    finally {
      if (session!=null) {
        session.close();
      } // if
      session=null;
      transaction=null;
    } // finally
    return regresar;
  }

  public List<T> toSourceEntitySet(Long idFuenteDato, String sql) throws Exception {
    return toSourceEntitySet(idFuenteDato, sql, Constantes.SQL_MAXIMO_REGISTROS);
  }

  public List<T> toSourceEntitySet(Long idFuenteDato, String process, String idXml, Map params, Integer first, Integer records) throws Exception {
    List<T> regresar=null;
    Session session=null;
    Transaction transaction=null;
    try {
      session=SessionFactoryFacade.getInstance().getSession(idFuenteDato);
      transaction=session.beginTransaction();
      session.clear();
      regresar=toSourceEntitySet(session, process, idXml, params, first, records);
      transaction.commit();
    } // try
    catch (Exception e) {
      if (transaction!=null) {
        transaction.rollback();
      } // if
      throw e;
    } // catch
    finally {
      if (session!=null) {
        session.close();
      } // if
      session=null;
      transaction=null;
    } // finally
    return regresar;
  }

  public List<T> toSourceEntitySet(Long idFuenteDato, String sql, Integer first, Integer records) throws Exception {
    List<T> regresar=null;
    Session session=null;
    Transaction transaction=null;
    try {
      session=SessionFactoryFacade.getInstance().getSession(idFuenteDato);
      transaction=session.beginTransaction();
      session.clear();
      regresar=toSourceEntitySet(session, sql, first, records);
      transaction.commit();
    } // try
    catch (Exception e) {
      if (transaction!=null) {
        transaction.rollback();
      }
      throw e;
    } // catch
    finally {
      if (session!=null) {
        session.close();
      } // if
      session=null;
      transaction=null;
    } // finally
    return regresar;
  }

  public List toEntityMap(String sql) throws Exception {
    return toEntityMap(sql, Constantes.SQL_MAXIMO_REGISTROS);
  }

  public List toEntityMap(String sql, Long records) throws Exception {
    List regresar          = null;
    Session session        = null;
    Transaction transaction= null;
    try {
      session=SessionFactoryFacade.getInstance().getSession();
      transaction=session.beginTransaction();
      session.clear();
      regresar= toEntityMap(sql, records, session);
      transaction.commit();
    } // try
    catch (Exception e) {
      if (transaction!=null) {
        transaction.rollback();
      }
      throw e;
    } // catch
    finally {
      if (session!=null) {
        session.close();
      }
      session=null;
      transaction=null;
    } // finally
    return regresar;
  }

  public List toEntityMap(String sql, Long records, Session session) throws Exception {
		List regresar = null;
		SQLQuery query= null;
		try {
			query=session.createSQLQuery(sql);
      if (records!=Constantes.SQL_TODOS_REGISTROS)
        query.setMaxResults(records.intValue());
			regresar=query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		} // try
		catch (Exception e) {
			throw e;
		} // catch
		finally {
			query = null;
		}//finally
		return regresar;
  }

  public List toEntityMap(String process, Map params) throws Exception {
    return toEntityMap(process, Constantes.DML_SELECT, params);
  }

  public List toEntityMap(String process, String idXml, Map params) throws Exception {
    return toEntityMap(process, idXml, params, Constantes.SQL_MAXIMO_REGISTROS);
  }

  public List toEntityMap(String process, String idXml, Map params, Long records) throws Exception {
    List regresar= null;
    Dml dml      = null;
    try {
      dml=Dml.getInstance();
      regresar= toEntityMap(dml.getSelect(process, idXml, params), records);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    finally {
      dml=null;
    } // finally
    return regresar;
  }

  public List toEntityMap(String process, Session session) throws Exception {
    return toEntityMap(process, Collections.EMPTY_MAP, session);
  }

  public List toEntityMap(String process, Map params, Session session) throws Exception {
    return toEntityMap(process, Constantes.DML_SELECT, params, session);
  }

  public List toEntityMap(String process, String idXml, Map params, Session session) throws Exception {
    return toEntityMap(process, Constantes.DML_SELECT, params, Constantes.SQL_MAXIMO_REGISTROS, session);
  }

  public List toEntityMap(String process, String idXml, Map params, Long records, Session session) throws Exception {
    List regresar= null;
    Dml dml      = null;
    try {
      dml=Dml.getInstance();
      regresar= toEntityMap(dml.getSelect(process, idXml, params), records, session);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    finally {
      dml=null;
    } // finally
    return regresar;
  }

  public List toEntityMap(Session session, String sql) throws Exception {
    return toEntityMap(session, sql, Collections.EMPTY_MAP);
  }

  public List toEntityMap(Session session, String sql, Map params) throws Exception {
    return toEntityMap(session, sql, params, Constantes.SQL_MAXIMO_REGISTROS);
  }

  public List toEntityMap(Session session, String sql, Map params, Long records) throws Exception {
    return toEntityMap(Cadena.replaceParams(sql, params), records, session);
  }

  public ScrollableResults toSqlScrollable(Session session, String sql, Long records) throws Exception {
		ScrollableResults regresar= null;
		SQLQuery query            = null;
		try {
			query=session.createSQLQuery(sql);
      if (records!=Constantes.SQL_TODOS_REGISTROS)
        query.setMaxResults(records.intValue());
			regresar=query.scroll(ScrollMode.SCROLL_INSENSITIVE);
		} // try
		catch (Exception e) {
			throw e;
		} // catch
		finally {
			query = null;
		}//finally
		return regresar;
  }

  public ScrollableResults toScrollable(Session session, String process) throws Exception {
    return toScrollable(session, process, Collections.EMPTY_MAP);
  }

  public ScrollableResults toScrollable(Session session, String process, Map params) throws Exception {
    return toScrollable(session, process, Constantes.DML_SELECT, params);
  }

  public ScrollableResults toScrollable(Session session, String process, String idXml, Map params) throws Exception {
    return toScrollable(session, process, Constantes.DML_SELECT, params, Constantes.SQL_MAXIMO_REGISTROS);
  }

  public ScrollableResults toScrollable(Session session, String process, String idXml, Map params, Long records) throws Exception {
    ScrollableResults regresar= null;
    Dml dml                   = null;
    try {
      dml=Dml.getInstance();
      regresar= toSqlScrollable(session, dml.getSelect(process, idXml, params), records);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    finally {
      dml=null;
    } // finally
    return regresar;
  }

  public ScrollableResults toSqlScrollable(Session session, String sql) throws Exception {
    return toSqlScrollable(session, sql, Collections.EMPTY_MAP);
  }

  public ScrollableResults toSqlScrollable(Session session, String sql, Map params) throws Exception {
    return toSqlScrollable(session, sql, params, Constantes.SQL_MAXIMO_REGISTROS);
  }

  public ScrollableResults toSqlScrollable(Session session, String sql, Map params, Long records) throws Exception {
    return toSqlScrollable(session, Cadena.replaceParams(sql, params), records);
  }

}
