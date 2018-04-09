package mx.org.kaana.xml;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.procesos.reportes.beans.Modelo;
import mx.org.kaana.libs.archivo.MetaField;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Error;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 12/10/2016
 * @time 02:46:37 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public class Tokens {

  protected static final String TOKEN = "__";
  protected static final String FORCE_INTEGER = "__N";
  protected static final String FORCE_DOUBLE = "__F";
  protected static final String FORCE_FLOAT = "__T";

  private String nombre;
  private Modelo definicion;
  private List<MetaField> fields;

  public Tokens(String nombre, Modelo definicion) throws SQLException {
    this.nombre = nombre;
    this.definicion = definicion;
    this.fields = new ArrayList<>();
    init();
  }

  public String getNombre() {
    return nombre;
  }

  public Modelo getDefinicion() {
    return definicion;
  }

  public List<MetaField> getFields() {
    return fields;
  }

  private void init() throws SQLException {
    Connection connection = null;
    Statement statement = null;
    ResultSet resultSet = null;
    ResultSetMetaData metaData = null;
    try {
      StringBuilder sb = new StringBuilder();
      sb.append("select * from (");
      sb.append(Dml.getInstance().getSelect(this.definicion.getProceso(), this.definicion.getIdXml(), this.definicion.getParams()));
      sb.append(") tabla where 1=1");
      connection = DaoFactory.getInstance().getConnection();
      statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
      resultSet = statement.executeQuery(sb.toString());
      metaData = resultSet.getMetaData();
      for (int x = 1; x <= metaData.getColumnCount(); x++) {
        this.fields.add(new MetaField(Cadena.toBeanNameEspecial(metaData.getColumnName(x)), metaData.getColumnType(x), metaData.getPrecision(x) > 255 ? 254 : metaData.getPrecision(x), metaData.getScale(x), metaData.getColumnName(x)));
      }
      int index = this.fields.indexOf(new MetaField("rownum", 0, 0, 0, "rownum"));
      if (index >= 0) {
        this.fields.remove(index);
      }
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
    finally {
      try {
        if (resultSet != null) {
          resultSet.close();
        }
        resultSet = null;
        if (statement != null) {
          statement.close();
        }
        statement = null;
      } // try
      catch (Exception ex) {
        Error.mensaje(ex);
      } // catch
      if (connection != null) {
        connection.close();
      }
      connection = null;
    } // finally
  }

  protected String fixName(String name, boolean dbf) {
    String regresar = name;
    if (regresar.indexOf(TOKEN) > 0) {
      regresar = regresar.substring(0, regresar.indexOf(TOKEN));
    }
    if (dbf) {
      if (regresar.length() > 10) {
        regresar = regresar.substring(0, 9);
      }
    }
    return regresar;
  }

  protected String fixName(String name) {
    return fixName(name, true);
  }

  @Override
  protected void finalize() throws Throwable {
    super.finalize();
    this.fields.clear();
    this.fields = null;
  }
}
