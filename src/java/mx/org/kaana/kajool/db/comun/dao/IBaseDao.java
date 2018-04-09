package mx.org.kaana.kajool.db.comun.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.db.comun.dao.exception.DaoInsertIdenticallyException;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.hibernate.FieldDto;
import mx.org.kaana.kajool.db.comun.hibernate.SessionFactoryFacade;
import mx.org.kaana.kajool.db.comun.page.PageRecords;
import mx.org.kaana.xml.Dml;
import mx.org.kaana.kajool.enums.ESql;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;


public abstract class IBaseDao <T extends IBaseDto> {
	
	private Long idFuenteDato= -1L;

	public Long getIdFuenteDato() {
		return idFuenteDato;
	}

	public void setIdFuenteDato(Long idFuenteDato) {
		this.idFuenteDato=idFuenteDato;
	}

  public abstract Long getKey();

  public abstract void setKey(Long key) ;

  public abstract T findById(Long key)throws Exception;

  public abstract T findById(Session session, Long key) throws Exception;

  public abstract T findById(Session session) throws Exception;

  public abstract T findIdentically(Map params) throws Exception;

  public abstract T findIdentically(Session session, Map params) throws Exception;

  public abstract T findFirst(Map params) throws Exception;

  public abstract T findFirst(Session session, Map params) throws Exception;

  public abstract T findFirst(Session session, Map params, String idXml) throws Exception;

  public abstract T findFirst(Map params, String idXml) throws Exception;

  public abstract boolean exist(Long key)throws Exception;

  public abstract boolean exist(Session session, Long key) throws Exception;

  public abstract boolean exist(Session session) throws Exception;

  public abstract boolean exist() throws Exception;

  public abstract List<T> findAll() throws Exception;

  public abstract List<T> findAll(Long maxRecords) throws Exception;

  public abstract List<T> findAll(Session session) throws Exception;

  public abstract List<T> findAll(Session session, Long maxRecords) throws Exception;

  public abstract List<T> findViewCriteria(Map params) throws Exception;

  public abstract List<T> findViewCriteria(Map params, Long maxRecords) throws Exception;

  public abstract List<T> findViewCriteria(Map params, Long maxRecords, String idXml) throws Exception;

  public abstract List<T> findViewCriteria(Session session, Map params) throws Exception;

  public abstract List<T> findViewCriteria(Session session, Map params, Long maxRecords) throws Exception;

  public abstract List<T> findViewCriteria(Session session, Map params, String idXml) throws Exception;

  public abstract List<T> findViewCriteria(Session session, Map params, Long maxRecords, String idXml) throws Exception;

  public abstract Long delete() throws Exception;

  public abstract Long delete(Long key) throws Exception;

  public abstract Long delete(Session session, Long key) throws Exception;

  public abstract Long update(Session session,Map fieldsDto) throws Exception;

  public abstract Long update(Map fieldsDto) throws Exception;

  public abstract Long update(Session session,  Long key, Map fieldsDto) throws Exception;

  public abstract Long update(Long key , Map fieldsDto) throws Exception;

  public abstract Long deleteAll(Map params) throws Exception ;

  public abstract Long deleteAll(Session session, Map params) throws Exception ;

  public abstract Long updateAll(Map params, String idSeccionXml) throws Exception ;

  public abstract Long updateAll(Session session, Map params, String idSeccionXml) throws Exception ;

  public abstract Long updateAll(Map params) throws Exception ;

  public abstract Long updateAll(Session session, Map params) throws Exception ;

  public abstract Long size() throws Exception;

  public abstract Long size(Map params) throws Exception;

  public abstract Long size(String idXml) throws Exception;

  public abstract Long size(String idXml, Map params) throws Exception;

  public abstract Long size(Session session) throws Exception;

  public abstract Long size(Session session, String idXml) throws Exception;

  public abstract Long size(Session session, Map params) throws Exception;

  public abstract Long size(Session session, String idXml, Map params) throws Exception;

  public abstract PageRecords findPage(Map params, Integer first, Integer records) throws Exception;

  public abstract PageRecords findPage(Session session, Map params, Integer first, Integer records) throws Exception;

  public abstract Long findKeyByIdentically(Session session, Map params) throws Exception;

  public abstract Long findKeyByIdentically(Map params) throws Exception;

  public abstract Boolean hasBlob();
		
  public Long insert (Long idFuenteDato, T dto) throws Exception {
    Transaction transaction= null;
    Long recordKey         = -1L;		
	  Session session        = null;
    try {			
			session= SessionFactoryFacade.getInstance().getSession(idFuenteDato);
      transaction = session.beginTransaction();
      session.clear();
      recordKey   = this.insert(session, dto);
      transaction.commit();
    } // try
    catch (Exception e ) {
      if (transaction!= null)
        transaction.rollback();
      throw e;
    } // catch
    finally {
      if (session!= null) {
        session.close();
      }
      session    = null;
      transaction= null;
    } // finally
    return recordKey;
  }

  public Long insert(Session session, T dto)  throws Exception {
    Long recordKey = -1L;
    try {
      if(Dml.getInstance().exists(dto.toHbmClass().getSimpleName(), Constantes.DML_IDENTICO))
        if (findIdentically(session, dto.toHbmClass(), dto.toMap())!= null)
          throw new DaoInsertIdenticallyException();
      recordKey = (Long)session.save(dto.toHbmClass().getName(),dto);
      } // try
    catch (Exception e) {
      throw e;
    } // catch
    return recordKey;
  }

  protected Long delete(Session session, T dto) throws Exception {
    try {
      return delete(session, dto.toHbmClass());
    } // try
    catch (Exception e ) {
      throw e;
    } // catch
  }

  protected Long delete(Session session, Class dto) throws Exception {
		String sql = null;
    try {
			sql="delete from ".concat(Methods.toNameTable(dto).concat(" where ").concat(Methods.toKeyName(dto,false)).concat(" =").concat(getKey().toString()));
			return executeQueryAll(session,sql);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
  }

  protected Long delete(T dto) throws Exception {
    try {
      return delete(dto.toHbmClass());
    } // try
    catch (Exception e) {
      throw e;
    } // catch
  }

  protected Long delete(Class dto) throws Exception {
    Session session        = null;
    Transaction transaction= null;
		Long regresar = -1L;
    try {
      session    = SessionFactoryFacade.getInstance().getSession();
      transaction= session.beginTransaction();
      session.clear();
      regresar = delete(session, dto);
      transaction.commit();
    } // try
    catch (Exception e) {
      if (transaction!= null)
        transaction.rollback();
      throw e;
    } // catch
    finally {
      if (session != null) {
        session.close();
      }
      session    = null;
      transaction= null;
    } // finally
		return regresar;
  }

  protected Long update (Session session, T dto, Map fieldsDto) throws Exception  {
    try {
      return update(session,dto.toHbmClass(),fieldsDto);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
  }

  protected Long update(Session session, Class dto, Map fieldsDto) throws Exception  {
    T ibaseDto= null;
		Long regresar = -1L;
    try {
      ibaseDto =(T)session.get(dto, getKey());
      if (ibaseDto!= null) {
        FieldDto.updateFields(ibaseDto, fieldsDto);
        regresar = (Long)session.save(dto.getSimpleName(), ibaseDto);      				
      }// if
    } // try
    catch (Exception e) {
      throw e;
    } // catch
		return regresar;	
  }

  protected Long update(Class dto, Map fieldsDto) throws Exception  {
    Session session        = null;
    Transaction transaction= null;
		Long regresar          = -1L;
    try {
      session      =  SessionFactoryFacade.getInstance().getSession();
      transaction  =  session.beginTransaction();
      session.clear();
      regresar = update(session, dto, fieldsDto);
      transaction.commit();
    } // try
    catch (Exception e) {
      if (transaction!= null)
        transaction.rollback();
      throw e;
    } // catch
    finally {
      if (session!= null) {
        session.close();
      }
      session    = null;
      transaction= null;
    } // finally
		return regresar;
  }

  protected Long update(T dto, Map fieldsDto) throws Exception  {
    try {
      return  update(dto.toHbmClass(), fieldsDto);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
  }

   protected Long update(T dto) throws Exception  {
     try {
      return  update(dto.toHbmClass(),dto.toMap());
     } // try
     catch (Exception e) {
       throw e;
     } // catch     		
   }

   protected Long update(Session session, T dto) throws Exception  {
      try {
        return update(session, dto, dto.toMap());
      } // try
      catch (Exception e) {
        throw e;
      } // catch
   }

   protected List<T> findAll(Class dto, Long maxRecords) throws Exception {
     Map params = null;
     List<T> dtos = null;
     try {
       params = new HashMap();
       params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
       dtos = findViewCriteria(dto, params, maxRecords, Constantes.DML_SELECT);
     } // try
     catch (Exception e) {
       throw e;
     } // catch
     finally {
       Methods.clean(params);
     } // finally
     return dtos;
  }

  protected List<T> findAll(Session session, Class dto) throws Exception {
    List<T> regresar = null;
    Map params = new HashMap();
    try {
      params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
      regresar  = findViewCriteria(session, dto, params, Constantes.SQL_MAXIMO_REGISTROS, Constantes.DML_SELECT);
    } // try
    catch(Exception e) {
      throw e;
    } // catch
    return regresar;
  }

  protected List<T> findAll(Session session, Class dto, Long maxRecords)  throws Exception {
    List<T> regresar = null;
    Map params = new HashMap();
    try {
      params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
      regresar = findViewCriteria(session, dto, params, maxRecords, Constantes.DML_SELECT);
    } // try
    catch(Exception e) {
      throw e;
    } // catch
    return regresar;
  }

  protected List<T> findViewCriteria(Class dto, Map params, Long maxRecords, String idXml) throws Exception {
		return findViewCriteria(-1L, dto, params, maxRecords, idXml);
	}
	
  protected List<T> findViewCriteria(Long idFuenteDato, Class dto, Map params, Long maxRecords, String idXml) throws Exception {
    List<T> pojos          = null;
    Session session        = null;
    Transaction transaction= null;
    try {
      session     = SessionFactoryFacade.getInstance().getSession(idFuenteDato);
      transaction = session.beginTransaction();
      session.clear();
      pojos       = findViewCriteria(session, dto, params, maxRecords, idXml);
      transaction.commit();
    } // try
    catch (Exception e) {
      if (transaction!= null) {
        transaction.rollback();
      }
      throw e;
    } // catch
    finally {
      if (session != null) {
        session.close();
      }
      session    = null;
      transaction= null;
    } // finally
    return pojos;
  }

  protected List<T> findViewCriteria(Session session, Class dto, Map params, Long maxRecords, String idXml) throws Exception {
    List<T> regresar  = null;
    SQLQuery consulta = null;
    Dml dml           = null;
    try {
      dml   = Dml.getInstance();
      consulta= session.createSQLQuery(dml.getSelect(dto.getSimpleName(), idXml, params)).addEntity(dto);
      if (maxRecords!= Constantes.SQL_TODOS_REGISTROS) {
        consulta.setMaxResults(maxRecords.intValue());
      }
      regresar = consulta.list();
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    finally {
      dml= null;
    } // finally
    return regresar;
  }

  protected T findById(Session session, Class dto) throws Exception {
    T ibaseDto= null;
    try {
      ibaseDto = (T)session.get(dto, getKey());
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    return ibaseDto;
  }

  protected T findById(Class dto) throws Exception {
    Session session          = null;
    T  iBaseDto              = null;
    Transaction transaction  = null;
    try {
      session    = SessionFactoryFacade.getInstance().getSession();
      transaction= session.beginTransaction();
      session.clear();
      iBaseDto = findById(session, dto);
      transaction.commit();
    } // try
    catch (Exception e) {
      if(transaction!= null) {
        transaction.rollback();
      }
      throw e;
    } // catch
    finally {
      if (session!= null) {
        session.close();
      }
      session    = null;
      transaction= null;
    } // finally
    return iBaseDto;
  }

  protected boolean exist (Session session, Class dto) throws Exception {
    boolean found = false;
    try {
      found = findById(session, dto)!= null;
    } // try
    catch (Exception e ) {
      throw e;
    } // catch
    return found;
  }

  protected boolean exist(Class dto) throws Exception {
    boolean found = false;
    try {
      found = findById(dto)!=null;
    } // try
    catch (Exception e ) {
      throw e;
    } // catch
    return found;
  }

  protected T findFirst(Session session, Class dto, Map params, String idXml) throws Exception {
    T  regresar = null;
    List<T> dtos= null;
    try {
      dtos = findViewCriteria(session, dto, params, 1L, idXml);
      if (dtos!= null && dtos.size()> 0) {
        regresar = dtos.get(0);
      } // if
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  }

  protected T findFirst(Class dto, Map params, String idXml) throws Exception {
		return findFirst(-1L, dto, params, idXml);
	}
	
  protected T findFirst(Long idFuenteDatos, Class dto, Map params, String idXml) throws Exception {
    T  regresar = null;
    List<T> dtos= null;
    try {
      dtos = findViewCriteria(idFuenteDatos, dto, params, 1L, idXml);
      if (dtos!= null && dtos.size()> 0) {
        regresar = dtos.get(0);
      }
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  }

  protected T findIdentically(Class dto, Map params) throws Exception {
		return findIdentically(-1L, dto, params);
	}
	
  protected T findIdentically(Long idFuenteDato, Class dto, Map params) throws Exception {
    T regresar = null;
    try {
      regresar = findFirst(idFuenteDato, dto, params, Constantes.DML_IDENTICO);
    } // try
    catch  (Exception e) {
      throw e;
    } // catch
    return regresar;
  }

  protected T findIdentically(Session session, Class dto, Map params) throws Exception {
    T  regresar = null;
    try {
      regresar = findFirst(session, dto, params, Constantes.DML_IDENTICO);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  }


  protected void delete$(Session session, Long key) throws Exception {
    try {
       delete(session,key);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
  }

  //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
  protected Long deleteAll(Map params, Class dto) throws Exception {
    return executeAll(params, Constantes.DML_ELIMINAR_TODOS, ESql.DELETE, dto);
  }

  protected Long deleteAll(Session session, Map params, Class dto) throws Exception{
    return excecuteAll(session, params, Constantes.DML_ELIMINAR_TODOS, ESql.DELETE, dto);
  }

  protected Long deleteAll(Map params, String idSeccionXml, Class dto) throws Exception {
    return executeAll(params, idSeccionXml, ESql.DELETE, dto);
  }

  protected Long deleteAll(Session session, Map params, String idSeccionXml, Class dto) throws Exception {
    return excecuteAll(session, params, idSeccionXml, ESql.DELETE, dto);
  }

  //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
  protected Long updateAll(Map params, Class dto) throws Exception {
    return executeAll(params, Constantes.DML_ACTUALIZAR_TODOS, ESql.UPDATE, dto);
  }

  protected Long updateAll(Session session, Map params, Class dto) throws Exception {
    return excecuteAll(session, params, Constantes.DML_ACTUALIZAR_TODOS, ESql.UPDATE, dto);
  }

  protected Long updateAll(Map params, String idSeccionXml, Class dto) throws Exception {
    return executeAll(params, idSeccionXml, ESql.UPDATE, dto);
  }

  protected Long updateAll(Session session, Map params, String idSeccionXml, Class dto) throws Exception {
    return excecuteAll(session, params, idSeccionXml, ESql.UPDATE, dto);
  }

 //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
  private Long excecuteAll(Session session, Map params, String idXml, ESql operation, Class dto) throws Exception {
    Long regresar = -1L;
    try {
      regresar = executeQueryAll(session,getQueryDml(idXml, params, operation, dto));
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  }

  private Long executeAll(Map params, String idXml, ESql operation, Class dto) throws Exception {
    Long regresar = -1L;
    Session session = null;
    Transaction transaction = null;
    try {
      session = SessionFactoryFacade.getInstance().getSession();
      transaction = session.beginTransaction();
      session.clear();
      regresar = executeQueryAll(session,getQueryDml(idXml, params, operation, dto));
      transaction.commit();
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    finally {
      if (session != null) {
        session.close();
      }
      session = null;
      transaction = null;
    } // finally
    return regresar;
  }

  private Long executeQueryAll(Session session, String query) throws Exception {
    Long regresar = -1L;
    SQLQuery consulta = null;
    try {
      consulta = session.createSQLQuery(query);
      regresar = ((Number)consulta.executeUpdate()).longValue();
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    finally {
      consulta = null;
    } // finally
    return regresar;
  }

  private String getQueryDml(String idXml, Map params, ESql operation, Class dto) throws Exception {
    Dml dml = null;
    String regresar = null;
    try {
      dml = Dml.getInstance();
      if (operation == ESql.UPDATE) {
        regresar = dml.getUpdate(dto.getSimpleName(), idXml, params);
      }
      else {
        regresar = dml.getDelete(dto.getSimpleName(), idXml, params);
      }
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    finally {
      dml = null;
    } // finally
    return regresar;
  }

  protected Long size(Session session, Class dto, String idXml, Map<String, Object> params) throws Exception {
    Long regresar= 0L;
    Dml dml      = null;
    String sql   = null;
    String total = null;
    try {
      dml     = Dml.getInstance();
      sql     = dml.getSelect(dto.getSimpleName(), idXml, params);
      total   = "select count(*) count from (".concat(sql).concat(") datos");
      regresar= ((Number) DaoFactory.getInstance().toField(session, total, "count").getData()).longValue();
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  }

  protected Long size(Session session, Class dto, Map<String, Object> params) throws Exception {
    return size(session, dto, Constantes.DML_SELECT, params);
  }

  protected Long size(Session session, Class dto, String idXml) throws Exception  {
    Map<String, Object> params = new HashMap<>();
    Long  regresar  = 0L;
    try {
      params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
      regresar = size(session, dto, idXml, params);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    finally {
			Methods.clean(params);
    } // finally
    return regresar;
  }

  protected Long size(Session session, Class dto) throws Exception  {
    return size(session, dto, dto.getSimpleName());
  }

  protected Long size(Class dto, String idXml, Map<String, Object> params) throws Exception {
    Long regresar           = 0L;
    Session session         = null;
    Transaction  transaction= null;
    try {
      session     = SessionFactoryFacade.getInstance().getSession();
      transaction = session.beginTransaction();
      session.clear();
      regresar = size(session, dto, idXml, params);
      transaction.commit();
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    finally {
       if (session != null) {
         session.close();
       }
       session    = null;
       transaction= null;
     } // finally
    return regresar;
  }

  protected Long size(Class dto, Map<String, Object> params) throws Exception {
    return size(dto, Constantes.DML_SELECT, params);
  }

  protected Long size(Class dto, String idXml) throws Exception {
    Map<String, Object> params = new HashMap<>();
    Long regresar = 0L;
    try {
      params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
      regresar = size(dto, idXml, params);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    finally {
			Methods.clean(params);
    } // finally
    return regresar;
  }

  protected Long size(Class dto) throws Exception {
    return size(dto, dto.getSimpleName());
  }

  protected PageRecords findPage(Class dto, Map params, Integer first, Integer records) throws Exception {
      PageRecords regresar    = null;
      Session session         = null;
      Transaction transaction = null;
      try {
        session     = SessionFactoryFacade.getInstance().getSession();
        transaction = session.beginTransaction();
        session.clear();
        regresar    = findPage(session, dto, params, first, records);
        transaction.commit();
      } // try
      catch (Exception e) {
        if (transaction!= null) {
          transaction.rollback();
        }
        throw e;
      } // catch
      finally {
        if (session != null) {
          session.close();
        }
        session    = null;
        transaction= null;
      } // finally
      return regresar;
    }

    protected PageRecords findPage(Session session, Class dto, Map params, Integer first, Integer records) throws Exception {
      PageRecords regresar= null;
      SQLQuery consulta   = null;
      Dml dml             = null;
      Long count          = 0L;
      List<IBaseDto> list= null;
      try {
        dml      = Dml.getInstance();
        consulta = session.createSQLQuery(dml.getSelect(dto.getSimpleName(), Constantes.DML_SELECT, params)).addEntity(dto);
        consulta.setFirstResult(first);
        consulta.setMaxResults(records);
        count    = size(session,  dto, params);
        list     = consulta.list();
        regresar = new PageRecords((int)(first* records), records, count.intValue(), list);
      } // try
      catch (Exception e) {
        throw e;
      } // catch
      finally {
        dml = null;
      } // finally
      return regresar;
    }       	
}
