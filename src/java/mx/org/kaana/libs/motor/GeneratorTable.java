package mx.org.kaana.libs.motor;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date Jul 3, 2012
 *@time 10:13:02 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;

public class GeneratorTable extends GenerationFiles {

  public GeneratorTable(String nombreTabla, String paquete, boolean isBean, String llavePrimaria, boolean isNemonico, boolean dao, String nombreEncuesta) throws Exception {
    super(nombreTabla.trim(), paquete, isBean, llavePrimaria, isNemonico, dao, nombreEncuesta);
  }

  @Override
  protected void setNamePk() throws Exception {
    Connection connection = null;
    ResultSet keys        = null;
    DatabaseMetaData dmd  = null;
		String primaryKey     = null;
    try {
      if(getLlavePrimaria()== null) {
        connection = DaoFactory.getInstance().getConnection();
        dmd        = connection.getMetaData();
        keys       = dmd.getPrimaryKeys("", "", getNombreTabla().toUpperCase());
				while(keys.next()){
					primaryKey = keys.getString("COLUMN_NAME");
				} // while        
        setPk(primaryKey);
      } // if
      else
        setPk(getLlavePrimaria());
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
    finally {
      if (keys!= null)
        keys.close();
      keys = null;
      if (connection != null)
        connection.close();
      connection = null;
    }
  }

  @Override
  protected void setUniqueKey() throws Exception {
    Map<String, RenglonCampo> regresar= new HashMap<>();
    Map<String, Integer>      auxiliar= new HashMap<>();
    Connection connection = null;
    ResultSet keys        = null;
    ResultSet columns     = null;
    DatabaseMetaData dmd  = null;
    String typeData       = null;
    try {
      boolean listUniqueIndex = true;
      connection= DaoFactory.getInstance().getConnection();
      dmd       = connection.getMetaData();
      keys      = dmd.getIndexInfo(null, null, getNombreTabla().toUpperCase(), listUniqueIndex, true);
      while(keys.next()) {
        String indexName = keys.getString("INDEX_NAME");
        String columnName= keys.getString("COLUMN_NAME");
        /**if((indexName == null) || (indexName.indexOf("UK")< 0)) {**/
        if((indexName == null)) 
          continue;
        columns= dmd.getColumns(null, null, getNombreTabla().toUpperCase(), null);
        while(columns.next()) {
          if(columnName.equals(columns.getString("COLUMN_NAME"))) {
            typeData= columns.getString("TYPE_NAME");
            regresar.put(columnName, new RenglonCampo(columnName, typeData, auxiliar, isNemonico()));
          } // if
        } // while colums
      } // while keys
      if(regresar.isEmpty())
        throw new Exception("La tabla no tiene una llave unica.");
      else
      setDetailUnique(regresar);
    }
    catch(Exception e) {
      Error.mensaje(e);
      throw e;
    } // try
    finally {
      if(connection != null)
        connection.close();
      connection=null;
      if(auxiliar!= null)
        auxiliar.clear();
      auxiliar= null;
    } // finally
  }

  @Override
  protected void setDetailFields() throws Exception {
    setDetailFields("select * from ".concat(getNombreTabla()));
  }

  @Override
  public String getSqlXml() throws Exception {
    return super.getSqlXml();
  }

  @Override
  protected String getParse(String typeFile) throws Exception {
    return getParse(typeFile, "Table");
  }

  @Override
  public String getPathCfg() {
    return super.getPathCfg();
  }

  @Override
  protected void setDetalleCampos() {
  }

}
