package mx.org.kaana.kajool.db.comun.hibernate;

import java.util.List;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.dao.IBaseDao;
import mx.org.kaana.kajool.db.comun.dao.exception.DaoInsertIdenticallyException;
import mx.org.kaana.libs.Constantes;
import org.hibernate.Session;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.kajool.db.comun.page.PageRecords;
import mx.org.kaana.xml.Dml;

public class DaoFacade<P extends IBaseDto> extends IBaseDao {

  private Long key;
  private Class hbmClass;


  public DaoFacade(Class dto) {
    this(dto, -1L);
  }

  public DaoFacade(Class hbmClass, Long key) {
    this.hbmClass= hbmClass;
    this.key=key;
  }

  @Override
  public Long getKey() {
    return key;
  }

  @Override
  public void setKey(Long key) {
    this.key=key;
  }

  @Override
  public List<P> findAll() throws Exception {
    List<P> regresar= null;
    try {
      regresar= findAll(Constantes.SQL_MAXIMO_REGISTROS);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  }

  @Override
  public List<P> findAll(Long maxRecords) throws Exception {
    List<P> regresar= null;
    try {
      regresar= super.findAll(this.hbmClass, maxRecords);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  }

  @Override
  public List<P> findAll(Session session) throws Exception {
    List<P> regresar= null;
    try {
      regresar= this.findAll(session, Constantes.SQL_MAXIMO_REGISTROS);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  }

  @Override
  public List<P> findAll(Session session, Long maxRecords) throws Exception {
    List<P> regresar= null;
    try {
      regresar= super.findAll(session, this.hbmClass, maxRecords);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  }

  @Override
  public List<P> findViewCriteria(Map params) throws Exception {
    List<P> regresar= null;
    try {
      regresar= this.findViewCriteria(params, Constantes.SQL_MAXIMO_REGISTROS);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  }

  @Override
  public List<P> findViewCriteria(Map params, Long maxRecords) throws Exception {
    List<P> regresar= null;
    try {
      regresar= super.findViewCriteria(this.hbmClass, params, maxRecords, Constantes.DML_SELECT);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  }

  @Override
  public List<P> findViewCriteria(Session session, Map params) throws Exception {
    List<P> regresar= null;
    try {
      regresar= this.findViewCriteria(session, params, Constantes.SQL_MAXIMO_REGISTROS);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  }

  @Override
  public List<P> findViewCriteria(Session session, Map params, Long maxRecords) throws Exception {
    List<P> regresar= null;
    try {
      regresar= super.findViewCriteria(session, this.hbmClass, params, maxRecords, Constantes.DML_SELECT);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  }

  @Override
  public Long delete() throws Exception {
    try {
      return delete(getKey());
    } // try
    catch (Exception e ) {
      throw e;
    } // catch
  }

  @Override
  public Long delete(Long key) throws Exception {
    try {
      setKey(key);
      return super.delete(this.hbmClass);
    } // try
    catch (Exception e ) {
      throw e;
    } // catch
  }

  public Long delete(Session session) throws Exception {
    try {
      return delete(session, getKey());
    } // try
    catch (Exception e) {
      throw e;
    } // catch
  }

  @Override
  public Long delete(Session session, Long key) throws Exception {
    try {
      setKey(key);
      return super.delete(session, this.hbmClass);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
  }

  @Override
  public Long delete(IBaseDto dto) throws Exception {
    try {
      setKey(dto.getKey());
      return super.delete(dto);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
  }

  @Override
  public Long delete(Session session, IBaseDto dto) throws Exception {
    try {
      setKey(dto.getKey());
      return super.delete(session, dto);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
  }

  @Override
  public Long insert(Session session, IBaseDto dto) throws Exception {
    Long regresar= -1L;
    try {
      if(Dml.getInstance().exists(dto.toHbmClass().getSimpleName(), Constantes.DML_IDENTICO))
        if (findIdentically(session, dto.toHbmClass(), dto.toMap())!= null)
          throw new DaoInsertIdenticallyException();
      regresar= super.insert(session, dto);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  }

  public Long insert$(Session session, IBaseDto dto) throws Exception {
    Long regresar= -1L;
    try {
      regresar = (Long)session.save(dto.toHbmClass().getName(),dto);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  }

  @Override
  public Long insert(Long idFuenteDatos, IBaseDto dto) throws Exception {
    Long regresar = -1L;
    try {
      if (findIdentically(idFuenteDatos, dto.toHbmClass(), dto.toMap())!= null) {
        throw new DaoInsertIdenticallyException();
      } // if
      regresar = super.insert(idFuenteDatos, dto);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  }

  @Override
  public Long update(Session session, Map fieldsDto) throws Exception {
    try {
      return update(session, (Long)fieldsDto.get("key"), fieldsDto);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
  }

  @Override
  public Long update(Map fieldsDto) throws Exception {
    try {
       return update((Long)fieldsDto.get("key"), fieldsDto);
    }  // try
    catch (Exception e) {
      throw e;
    } // catch
  }

  @Override
  public Long update(IBaseDto dto) throws Exception {
    Map fieldsDto = null;
    try {
      fieldsDto = dto.toMap();
      fieldsDto.put("key", dto.getKey());
      return update(fieldsDto);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
  }

  @Override
  public Long update(Long key, Map fieldsDto)  throws Exception {
    try {
      setKey(key);
      return super.update(this.hbmClass, fieldsDto);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
  }

  @Override
  public Long update(Session session, Long key, Map fieldsDto) throws Exception {
    try {
      setKey(key);
      return super.update(session, this.hbmClass, fieldsDto);
    } // try
    catch(Exception e)  {
      throw e;
    } // catch
  }

  @Override
  public Long update(Session session, IBaseDto dto) throws Exception {
    try {
      return update(session, dto.getKey(), dto.toMap());
    }  // try
    catch (Exception e) {
      throw e;
    }  // catch
  }

  public P findById() throws Exception {
    return findById(getKey());
  }

  @Override
  public P findById(Long key) throws Exception {
    P regresar= null;
    try {
      setKey(key);
      regresar= (P)super.findById(this.hbmClass);
    }
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  }

  @Override
  public P findById(Session session) throws Exception {
    P regresar= null;
    try {
      regresar= findById(session, getKey());
    }
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  }

  @Override
  public P findById(Session session, Long key) throws Exception {
    P regresar= null;
    try {
      setKey(key);
      regresar= (P)super.findById(session, this.hbmClass);
    }
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  }

  @Override
  public boolean exist() throws Exception {
    return exist(getKey());
  }

  @Override
  public boolean exist(Long key) throws Exception {
    boolean regresar= false;
    try {
      setKey(key);
      regresar= super.exist(this.hbmClass);
    }
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  }

  @Override
  public boolean exist(Session session) throws Exception {
    return exist(session, getKey());
  }

  @Override
  public boolean exist(Session session, Long key) throws Exception {
    boolean regresar= false;
    try {
      setKey(key);
      regresar= super.exist(session, this.hbmClass);
    }
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  }

  public boolean exist(Session session, IBaseDto dto) throws Exception {
    boolean regresar= false;
    try {
      regresar= exist(session, dto.getKey());
    } // catch
    catch (Exception e) {
      throw e;
    } // cattch
    return regresar;
  }

  public boolean exist(P dto) throws Exception {
    boolean regresar= false;
    try {
      regresar= exist(dto.getKey());
    } // catch
    catch (Exception e) {
      throw e;
    } // cattch
    return regresar;
  }

  public P findIndentically(Map params)  throws Exception  {
    P regresa= null;
    try {
      regresa= (P)super.findIdentically(this.hbmClass, params);
    }
    catch (Exception e) {
      throw e;
    } // catch
    return regresa;
  }

  public P findIndentically(Session session, Map params)  throws Exception {
    P regresa= null;
    try {
      regresa= (P)super.findIdentically(session, this.hbmClass, params);
    }
    catch (Exception e) {
      throw e;
    } // catch
    return regresa;
  }

  @Override
  public P findFirst(Map params) throws Exception {
    P regresa= null;
    try {
      regresa= (P)super.findFirst(this.hbmClass, params, Constantes.DML_SELECT);
    }
    catch (Exception e) {
      throw e;
    } // catch
    return regresa;
  }

  @Override
  public P findFirst(Session session, Map params) throws Exception {
    P regresa= null;
    try {
      regresa= (P)super.findFirst(session,this.hbmClass, params, Constantes.DML_SELECT);
    }
    catch (Exception e) {
      throw e;
    } // catch
    return regresa;
  }

  @Override
  public P findFirst( Map params, String idXml) throws Exception {
    P regresa = null;
    try {
      regresa = (P)super.findFirst(this.hbmClass, params, idXml);
   }
    catch (Exception e) {
      throw e;
    } // catch
    return  regresa;
  }

  @Override
  public P findFirst(Session session, Map params, String idXml) throws Exception {
    P regresa = null;
    try {
      regresa = (P)super.findFirst(session, this.hbmClass, params, idXml);
    }
    catch (Exception e) {
      throw e;
   } // catch
    return  regresa;
  }

  @Override
  public Long deleteAll(Map params) throws Exception {
    Long regresa=-1L;
    try {
      regresa= super.deleteAll(params, this.hbmClass);
    }
    catch (Exception e) {
      throw e;
    } // catch
    return regresa;
  }

  @Override
  public Long deleteAll(Session session, Map params) throws Exception {
    Long regresa=-1L;
    try {
      regresa= super.deleteAll(session, params, this.hbmClass);
    }
    catch (Exception e) {
      throw e;
    } // catch
    return regresa;
  }

  @Override
  public Long updateAll(Map params, String idSeccionXml) throws Exception {
    Long regresa=-1L;
    try {
      regresa= super.updateAll(params, idSeccionXml, this.hbmClass);
    }
    catch (Exception e) {
      throw e;
    } // catch
    return regresa;
  }

  @Override
  public Long updateAll(Session session, Map params, String idSeccionXml) throws Exception {
    Long regresa=-1L;
    try {
      regresa= super.updateAll(session, params, idSeccionXml, this.hbmClass);
    }
    catch (Exception e) {
      throw e;
    } // catch
    return regresa;
  }

  @Override
  public Long updateAll(Map params) throws Exception {
    Long regresa=-1L;
    try {
      regresa= super.updateAll(params, this.hbmClass);
    }
    catch (Exception e) {
      throw e;
    } // catch
    return regresa;
  }

  @Override
  public Long updateAll(Session session, Map params) throws Exception {
    Long regresa=-1L;
    try {
      regresa= super.updateAll(session, params, this.hbmClass);
    }
    catch (Exception e) {
      throw e;
    } // catch
    return regresa;
  }

  @Override
  public PageRecords findPage(Map params, Integer first, Integer records) throws Exception {
    PageRecords regresar= null;
    try {
      regresar= super.findPage(this.hbmClass, params, first, records);
    } // catch
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  }

  @Override
  public PageRecords findPage(Session session, Map params, Integer first, Integer records) throws Exception {
    PageRecords regresar= null;
    try {
      regresar = super.findPage(session, this.hbmClass, params, first, records);
    } // catch
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  }

  @Override
  public Long size() throws Exception {
    Long regresar = null;
    try {
      regresar=super.size(this.hbmClass);
    } // catch
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  }

  @Override
  public Long size(Map params) throws Exception {
    Long regresar = null;
    try {
      regresar=super.size(this.hbmClass, params);
    } // catch
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  }

  @Override
  public Long size(String idXml) throws Exception {
    Long regresar = null;
    try {
      regresar=super.size(this.hbmClass, idXml);
    } // catch
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  }

  @Override
  public Long size(String idXml, Map params) throws Exception {
    Long regresar = null;
    try {
      regresar=super.size(this.hbmClass, idXml, params);
    } // catch
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  }

  @Override
  public Long size(Session session) throws Exception {
    Long regresar = null;
    try {
      regresar=super.size(session, this.hbmClass);
    } // catch
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  }

  @Override
  public Long size(Session session, String idXml) throws Exception {
    Long regresar = null;
    try {
      regresar=super.size(session, this.hbmClass, idXml);
    } // catch
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  }

  @Override
  public Long size(Session session, Map params) throws Exception {
    Long regresar = null;
    try {
      regresar=super.size(session, this.hbmClass, params);
    } // catch
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  }

  @Override
  public Long size(Session session, String idXml, Map params) throws Exception {
    Long regresar = null;
    try {
      regresar=super.size(session, this.hbmClass, idXml, params);
    } // catch
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  }

  @Override
  public List<P> findViewCriteria(Session session, Map params, String idXml) throws Exception {
    return super.findViewCriteria(session, this.hbmClass, params, Constantes.SQL_MAXIMO_REGISTROS, idXml) ;
  }

  @Override
  public List<P> findViewCriteria(Map params, Long records, String idXml) throws Exception {
    List<P> regresar = null;
    try {
      regresar =super.findViewCriteria(this.hbmClass, params, records, idXml);
    } // catch
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  }

  @Override
  public List<P> findViewCriteria(Session session, Map params, Long records, String idXml) throws Exception {
    List<P> regresar = null;
    try {
      regresar = super.findViewCriteria(session, this.hbmClass, params, records, idXml);
    } // catch
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  }

  public Long findKeyByIndentically(Map params) throws Exception {
    Long regresa= -1L;
    P dto       = null;
    try {
      dto = this.findIndentically(params);
      if (dto != null)
        regresa = dto.getKey();
    } // catch
    catch (Exception e) {
      throw e;
    } // catch
    return regresa;
  }

  public Long findKeyByIndentically(Session session, Map params) throws Exception {
    Long regresa= -1L;
    P dto       = null;
    try {
      dto = this.findIndentically(session, params);
      if (dto != null)
        regresa = dto.getKey();
    } // catch
    catch (Exception e) {
      throw e;
    } // catch
    return regresa;
  }

  @Override
  public Boolean hasBlob() {
    return false;
  }

  public Long findKeyByIdentical( Map params) throws Exception {
    Long regresa= -1L;
    P dto       = null;
    try {
      dto = this.findIdentically(params);
      if (dto != null)
        regresa = dto.getKey();
    } // catch
    catch (Exception e) {
      throw e;
    } // catch
    return regresa;
  }

  public Long findKeyByIdentical(Session session, Map params) throws Exception {
    Long regresa= -1L;
    P dto       = null;
    try {
      dto = this.findIdentically(session, params);
      if (dto != null)
        regresa = dto.getKey();
    }
    catch (Exception e) {
      throw e;
    } // catch
    return regresa;
  }

  @Override
  public P findIdentically(Map params)  throws Exception  {
    P regresa= null;
    try {
      regresa= (P)super.findIdentically(this.hbmClass, params);
    }
    catch (Exception e) {
      throw e;
    } // catch
    return regresa;
  }

  @Override
  public P findIdentically(Session session, Map params)  throws Exception {
    P regresa= null;
    try {
      regresa= (P)super.findIdentically(session, this.hbmClass, params);
    }
    catch (Exception e) {
      throw e;
    } // catch
    return regresa;
  }

  @Override
  public Long findKeyByIdentically(Session session, Map params) throws Exception {
    Long regresa= -1L;
    P dto       = null;
    try {
      dto = this.findIdentically(session, params);
      if (dto != null)
        regresa = dto.getKey();
    } // catch
    catch (Exception e) {
      throw e;
    } // catch
    return regresa;
  }

  @Override
  public Long findKeyByIdentically(Map params) throws Exception {
    Long regresa= -1L;
    P dto       = null;
    try {
      dto = this.findIdentically(params);
      if (dto != null)
        regresa = dto.getKey();
    } // catch
    catch (Exception e) {
      throw e;
    } // catch
    return regresa;
  }

}
