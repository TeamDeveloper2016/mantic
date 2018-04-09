package mx.org.kaana.libs.motor;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date Jul 19, 2012
 *@time 12:09:38 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

import java.text.MessageFormat;

import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;

public class FormatQuery {

  private String query;

  public FormatQuery(String query) {
    setQuery(query);
  }

  public void setQuery(String query) {
    this.query = query;
  }

  public String getQuery() {
    return query;
  }

  public String complementarQuery() throws Exception {
    Connection connection = null;
    Statement stm         = null;
    ResultSet rst         = null;
    ResultSetMetaData rsmd= null;
    StringBuffer sb       = null;
    try {
      sb        = new StringBuffer();
      sb.append("select rownum id_vista");
      connection= DaoFactory.getInstance().getConnection();
      stm       = connection.createStatement();
      rst       = stm.executeQuery(getQuery());
      rsmd      = rst.getMetaData();
      for(int x= 1; x <= rsmd.getColumnCount(); x++) {
        sb.append(",");
        sb.append(rsmd.getColumnName(x));
      } // for x
      sb.append(" from (");
      sb.append(getQuery());
      sb.append(")");
    } // try
    catch(Exception e) {
      Error.mensaje(e);
    } // catch
    finally {
      if(rst != null)
        rst.close();
      rst=null;
      if(stm != null)
        stm.close();
      stm=null;
      if(connection != null)
        connection.close();
      connection=null;
    } // finally
    return sb.toString();
  }

/**
 * A partir del query recibido, éste se encapsula en un select general
 * de la forma:
 * select * from ( query  )
 * @return
 */
  public String encapsularQuery() {
    final String queryGenerico = "select * from ( {0} ) datos";
    String regresar = MessageFormat.format(queryGenerico, new Object[]{ this.query});
    return  regresar.concat(" where {condicion}");
  } // encapsuarQuery
}
