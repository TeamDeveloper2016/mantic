package mx.org.kaana.kajool.db.comun.sqlite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.kajool.db.comun.transform.TransformDto;
import mx.org.kaana.kajool.db.comun.transform.TransformEntity;
import mx.org.kaana.kajool.db.comun.transform.Transformer;
import mx.org.kaana.xml.Dml;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date Feb 18, 2013
 * @time 2:33:08 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public final class Manejador<T extends IBaseDto> {

  private String sqliteFile;
  private static final String JDBC_SQLITE="org.sqlite.JDBC";

  public Manejador(String sqliteFile) {
    this.sqliteFile=sqliteFile;
  }

  public Connection getConnection() {
    Connection regresar=null;
    try {
      Class.forName(JDBC_SQLITE);
      regresar=DriverManager.getConnection("jdbc:sqlite:".concat(this.sqliteFile));
      regresar.setAutoCommit(false);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
    return regresar;
  }

  public void closeConnection(Connection connection) {
    try {
      if (connection!=null) {
        connection.close();
      }
      connection=null;
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
  }

  public void doRollBack(Connection connection) {
    try {
      if (connection!=null) {
        connection.rollback();
      }
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
  }

  private Long ejecutar(Connection connection, String sentencia) throws Exception {
    Long regresar=-1L;
    Statement statement=null;
    ResultSet resultSet=null;
    try {
      statement=connection.createStatement();
      regresar=((Number) statement.executeUpdate(sentencia)).longValue();
    }
    catch (Exception e) {
      Error.mensaje(e);
      throw e;
    }
    finally {
      try {
        if (resultSet!=null) {
          resultSet.close();
        }
        resultSet=null;
        if (statement!=null) {
          statement.close();
        }
        statement=null;
      }
      catch (Exception e) {
        Error.mensaje(e);
      } // try
    } // try
    return regresar;
  }

  public Long insert(String sql) throws Exception {
    Long regresar=-1L;
    Connection connection=null;
    try {
      connection=getConnection();
      regresar=insert(connection, sql);
      connection.commit();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      doRollBack(connection);
      throw e;
    } // catch
    finally {
      closeConnection(connection);
    } // finally
    return regresar;
  }

  public Long insert(String process, Map<String, Object> params) throws Exception {
    Long regresar=-1L;
    try {
      regresar=insert(process, Constantes.DML_SELECT, params);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      throw e;
    } // catch
    return regresar;
  }

  public Long insert(String process, String idXml, Map<String, Object> params) throws Exception {
    Long regresar=-1L;
    try {
      regresar=insert(Dml.getInstance().getInsert(process, idXml, params));
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      throw e;
    } // catch
    return regresar;
  }

  public Long insert(Connection connection, String process, Map<String, Object> params) throws Exception {
    Long regresar=-1L;
    try {
      regresar=insert(connection, process, Constantes.DML_SELECT, params);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      throw e;
    } // catch
    return regresar;
  }

  public Long insert(Connection connection, String process, String idXml, Map<String, Object> params) throws Exception {
    Long regresar=-1L;
    try {
      regresar=insert(connection, Dml.getInstance().getInsert(process, idXml, params));
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      throw e;
    } // catch
    return regresar;
  }

  public Long insert(Connection connection, String sql) throws Exception {
    Long regresar=-1L;
    try {
      regresar=ejecutar(connection, sql);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      throw e;
    } // catch
    return regresar;
  }

  public Long update(String sql) throws Exception {
    Long regresar=-1L;
    Connection connection=null;
    try {
      connection=getConnection();
      regresar=update(connection, sql);
      connection.commit();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      doRollBack(connection);
      throw e;
    } // catch
    finally {
      closeConnection(connection);
    } // finally
    return regresar;
  }

  public Long update(Connection connection, String sql) throws Exception {
    Long regresar=-1L;
    try {
      regresar=ejecutar(connection, sql);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      throw e;
    } // catch
    return regresar;
  }

  public Long update(Connection connection, String process, Map<String, Object> params) throws Exception {
    Long regresar=-1L;
    try {
      regresar=update(connection, process, Constantes.DML_SELECT, params);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
    return regresar;
  }

  public Long update(Connection connection, String process, String idXml, Map<String, Object> params) throws Exception {
    Long regresar=-1L;
    try {
      regresar=update(connection, Dml.getInstance().getUpdate(process, idXml, params));
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
    return regresar;
  }

  public Long delete(String sql) throws Exception {
    Long regresar=-1L;
    Connection connection=null;
    try {
      connection=getConnection();
      regresar=delete(connection, sql);
      connection.commit();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      doRollBack(connection);
      throw e;
    } // catch
    finally {
      closeConnection(connection);
    } // finally
    return regresar;
  }

  public Long delete(Connection connection, String sql) throws Exception {
    Long regresar=-1L;
    try {
      regresar=ejecutar(connection, sql);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      throw e;
    } // catch
    return regresar;
  }

  public Long delete(Connection connection, String process, Map<String, Object> params) throws Exception {
    Long regresar=-1L;
    try {
      regresar=delete(connection, process, Constantes.DML_SELECT, params);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
    return regresar;
  }

  public Long delete(Connection connection, String process, String idXml, Map<String, Object> params) throws Exception {
    Long regresar=-1L;
    try {
      regresar=delete(connection, Dml.getInstance().getUpdate(process, idXml, params));
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
    return regresar;
  }

  public T findIdentically (Class<? extends IBaseDto> dto, String process,Map<String,Object> params) throws Exception {
   return findIdentically(dto,process,Constantes.DML_IDENTICO,params);
  }

  public T findIdentically (Class<? extends IBaseDto> dto, String process, String idXml,Map<String,Object> params) throws Exception {
    return toRecordDto(dto, process,idXml,params);
  }

  public T findIdentically (Connection connection, Class<? extends IBaseDto> dto, String process, String idXml,Map<String,Object> params) throws Exception {
    return toRecordDto(connection, dto,process, idXml, params);
  }

  private String[] toFields(ResultSet rst) throws Exception {
    ResultSetMetaData rstm=rst.getMetaData();
    String[] regresar=new String[rstm.getColumnCount()];
    for (int x=0; x<rstm.getColumnCount(); x++) {
      regresar[x]=rstm.getColumnLabel(x+1);
    } // for
    return regresar;
  }

  private Object[] toData(ResultSet rst, String fields[]) throws Exception {
    Object[] regresar=new Object[fields.length];
    for (int x=0; x<fields.length; x++) {
      regresar[x]=rst.getObject(fields[x]);
    } // for
    return regresar;
  }

  private List<T> transform(Connection connection, Transformer transformer, String sql, Long registros) throws Exception {
    List<T> regresar=null;
    Statement statement=null;
    ResultSet resultSet=null;
    Long contador=1L;
    String[] fields=null;
    Object[] data=null;
    try {
      statement=connection.createStatement();
      resultSet=statement.executeQuery(sql);
      if (resultSet.next()) {
        regresar=new ArrayList<T>();
        fields=toFields(resultSet);
        data=toData(resultSet, fields);
        regresar.add((T) transformer.transformTuple(data, fields));
        while (resultSet.next()&&(registros==-1L||contador<registros)) {
          data=toData(resultSet, transformer.getBdNames());
          regresar.add((T) transformer.tuple(data, fields, transformer.getBdNames()));
          contador++;
        } // while
      } // if
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      throw e;
    } // catch
    finally {
      try {
        if (resultSet!=null) {
          resultSet.close();
        } // if
        resultSet=null;
        if (statement!=null) {
          statement.close();
        } // if
        statement=null;
      } // try
      catch (Exception e) {
        Error.mensaje(e);
      } // try
    } // try
    return regresar;
  }

  private List<T> toTransformEntitySet(Connection connection, String sql, Long registros) throws Exception {
    List<T> regresar=null;
    try {
      regresar=transform(connection, new TransformEntity(), sql, registros);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      throw e;
    } // catch
    return regresar;
  }

  private List<T> toTransformEntitySet(String sql, Long registros) throws Exception {
    List<T> regresar=null;
    Connection connection=null;
    try {
      connection=getConnection();
      regresar=toTransformEntitySet(connection, sql, registros);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      throw e;
    } // catch
    return regresar;
  }

  private List<T> toDtos(Connection connection, Class<? extends IBaseDto> dto, String sql, Long registros) throws Exception {
    List<T> regresar=null;
    try {
      regresar=transform(connection, new TransformDto(dto), sql, registros);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      throw e;
    } // catch
    return regresar;
  }

  private List<T> toDtos(Class<? extends IBaseDto> dto, String sql, Long registros) throws Exception {
    List<T> regresar=null;
    Connection connection=null;
    try {
      connection=getConnection();
      regresar=toDtos(connection, dto, sql, registros);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      throw e;
    } // catch
    finally {
      closeConnection(connection);
    }
    return regresar;
  }

  public T toRecordDto(Class<? extends IBaseDto> dto, String sql) throws Exception {
    T regresar=null;
    List<T> record=toRecordsDto(dto, sql, ((Number) Constantes.SQL_PRIMER_REGISTRO).longValue());
    if (record!=null&&!record.isEmpty()) {
      regresar=record.get(0);
    }
    return regresar;
  }

  public T toRecordDto(Class<? extends IBaseDto> dto, String process, Map<String, Object> params) throws Exception {
    return toRecordDto(dto, Dml.getInstance().getSelect(process, Constantes.DML_SELECT, params));
  }

  public T toRecordDto(Class<? extends IBaseDto> dto, String process, String idXml, Map<String, Object> params) throws Exception {
    return toRecordDto(dto, Dml.getInstance().getSelect(process, idXml, params));
  }

  public T toRecordDto(Connection connection, Class<? extends IBaseDto> dto, String sql) throws Exception {
    T regresar=null;
    List<T> record=toRecordsDto(connection, dto, sql, ((Number) Constantes.SQL_PRIMER_REGISTRO).longValue());
    if (record!=null&&!record.isEmpty()) {
      regresar=record.get(0);
    }
    return regresar;
  }

  public T toRecordDto(Connection connection, Class<? extends IBaseDto> dto, String process, Map<String, Object> params) throws Exception {
    return toRecordDto(connection, dto, Dml.getInstance().getSelect(process, Constantes.DML_SELECT, params));
  }

  public T toRecordDto(Connection connection, Class<? extends IBaseDto> dto, String process, String idXml, Map<String, Object> params) throws Exception {
    return toRecordDto(connection, dto, Dml.getInstance().getSelect(process, idXml, params));
  }

  public List<T> toRecordsDto(Class<? extends IBaseDto> dto, String sql) throws Exception {
    return toRecordsDto(dto, sql, Constantes.SQL_TODOS_REGISTROS);
  }

  public List<T> toRecordsDto(Class<? extends IBaseDto> dto, String sql, Long registros) throws Exception {
    return toDtos(dto, sql, registros);
  }

  public List<T> toRecordsDto(Class<? extends IBaseDto> dto, String process, String idXml, Map<String, Object> params) throws Exception {
    return toRecordsDto(dto, process, idXml, params, Constantes.SQL_TODOS_REGISTROS);
  }

  public List<T> toRecordsDto(Class<? extends IBaseDto> dto, String process, String idXml, Map<String, Object> params, Long registros) throws Exception {
    List<T> regresar=null;
    try {
      regresar=toRecordsDto(dto, Dml.getInstance().getSelect(process, idXml, params), registros);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      throw e;
    } // catch
    return regresar;
  }

  public List<T> toRecordsDto(Connection connection, Class<? extends IBaseDto> dto, String sql) throws Exception {
    return toRecordsDto(connection, dto, sql, Constantes.SQL_TODOS_REGISTROS);
  }

  public List<T> toRecordsDto(Connection connection, Class<? extends IBaseDto> dto, String sql, Long registros) throws Exception {
    return toDtos(connection, dto, sql, registros);
  }

  public List<T> toRecordsDto(Connection connection, Class<? extends IBaseDto> dto, String process, String idXml, Map<String, Object> params) throws Exception {
    return toRecordsDto(connection, dto, process, idXml, params, Constantes.SQL_TODOS_REGISTROS);
  }

  public List<T> toRecordsDto(Connection connection, Class<? extends IBaseDto> dto, String process, String idXml, Map<String, Object> params, Long registros) throws Exception {
    List<T> regresar=null;
    try {
      regresar=toRecordsDto(connection, dto, Dml.getInstance().getSelect(process, idXml, params), registros);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      throw e;
    } // catch
    return regresar;
  }

  public T toEntity (String sql) throws Exception {
   T regresar=null;
   List<T> record=toEntitySet(sql,((Number)Constantes.SQL_PRIMER_REGISTRO).longValue());
    if (record!=null&&!record.isEmpty()) {
      regresar=record.get(0);
    }
    return regresar;
  }

  public T toEntity(String process, Map<String, Object> params) throws Exception {
    return toEntity(process, Constantes.DML_SELECT, params);
  }

  public T toEntity(String process, String idXml, Map<String, Object> params) throws Exception {
    return toEntity( Dml.getInstance().getSelect(process, idXml, params));
  }

  public T toEntity(Connection connection, String sql) throws Exception {
    T regresar=null;
    List<T> record=toEntitySet(connection,sql, ((Number) Constantes.SQL_PRIMER_REGISTRO).longValue());
    if (record!=null&&!record.isEmpty()) {
      regresar=record.get(0);
    }
    return regresar;
  }

  public T toEntity(Connection connection, String process, Map<String, Object> params) throws Exception {
    return toEntity(connection,process,Constantes.DML_SELECT, params);
  }

  public T toEntity(Connection connection, String process, String idXml, Map<String, Object> params) throws Exception {
    return toEntity(connection,Dml.getInstance().getSelect(process, idXml, params));
  }

  public List<T> toEntitySet(String sql) throws Exception {
    return toEntitySet(sql, Constantes.SQL_TODOS_REGISTROS);
  }

  public List<T> toEntitySet(String sql, Long registros) throws Exception {
    return toTransformEntitySet(sql, registros);
  }

  public List<T> toEntitySet(String process, String idXml, Map<String, Object> params) throws Exception {
    return toEntitySet(process, idXml, params, Constantes.SQL_TODOS_REGISTROS);
  }

  public List<T> toEntitySet(String process, String idXml, Map<String, Object> params, Long registros) throws Exception {
    List<T> regresar=null;
    try {
      regresar=toEntitySet(Dml.getInstance().getSelect(process, idXml, params), registros);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      throw e;
    } // catch
    return regresar;
  }

  public List<T> toEntitySet(Connection connection, String sql) throws Exception {
    return toEntitySet(connection, sql, Constantes.SQL_TODOS_REGISTROS);
  }

  public List<T> toEntitySet(Connection connection, String sql, Long registros) throws Exception {
    return toTransformEntitySet(connection, sql, registros);
  }

  public List<T> toEntitySet(Connection connection, String process, String idXml, Map<String, Object> params) throws Exception {
    return toEntitySet(connection, process, idXml, params, Constantes.SQL_TODOS_REGISTROS);
  }

  public List<T> toEntitySet(Connection connection, String process, String idXml, Map<String, Object> params, Long registros) throws Exception {
    List<T> regresar=null;
    try {
      regresar=toEntitySet(connection, Dml.getInstance().getSelect(process, idXml, params), registros);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      throw e;
    } // catch
    return regresar;
  }

  public static void main(String[] args) {
    Manejador manejador=new Manejador("/home/default/Desarrollo/Plataforma/netbeans/18Febrero2013/mixto/web/WEB-INF/sqlite/poblacion.sys");
    List<IBaseDto> registros=null;
    try {
      //System.out.println(manejador.insert(" insert into tc_ambitos values (6,\'central\')"));
      //registros=manejador.toEntity("select * from tc_ambitos");
      //registros=manejador.toRecordsDto(RhTcAmbitosDto.class, "select * from tc_ambitos", 10L);

      //for (IBaseDto record : registros) {
       // System.out.println(record.toMap());
     // }
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    }
  }
}
