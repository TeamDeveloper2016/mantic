package mx.org.kaana.libs.motor;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date Jul 6, 2012
 *@time 11:44:42 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.libs.motor.beans.DetalleMotor;
import mx.org.kaana.libs.pagina.JsfUtilities;

public class GeneracionConsulta {

  private DetalleMotor detalleMotor;

  public GeneracionConsulta(DetalleMotor detalleMotor) {
    this.detalleMotor = detalleMotor;
  }

  public DetalleMotor getDetalleMotor() {
    return detalleMotor;
  }

  public void setDetalleMotor(DetalleMotor detalleMotor) {
    this.detalleMotor=detalleMotor;
  }

  public boolean validarConsulta() throws Exception {
    boolean valido        = false;
    Connection connection = null;
    Statement stm         = null;
    ResultSet rst         = null;
    try {
      connection = DaoFactory.getInstance().getConnection();
      stm        = connection.createStatement();
      rst        = stm.executeQuery(this.detalleMotor.getResultadoXML());
      valido     = true;
    } // try
    catch(Exception e) {
      JsfUtilities.addMessage("Sentencia sql no válida. Verifique por favor.", ETipoMensaje.ERROR);
      valido = false;
      throw e;
    } // catch
        finally {
      if(rst != null)
        rst.close();
      rst = null;
      if(stm != null)
        stm.close();
      stm = null;
      if(connection != null)
        connection.close();
      connection = null;
    } // finally

    return valido;
  }

  public List<String> recuperarCamposLLave() throws Exception {
    List<String> camposLlave  = null;
    Connection connection     = null;
    Statement stm             = null;
    ResultSet rst             = null;
    ResultSetMetaData rsmd    = null;
    String nameColumna        = null;
    String typeData           = null;
    try {
      camposLlave = new ArrayList<String>();
      connection = DaoFactory.getInstance().getConnection();
      stm = connection.createStatement();
      rst = stm.executeQuery(this.detalleMotor.getResultadoXML());
      rsmd = rst.getMetaData();
      for(int x = 1; x <= rsmd.getColumnCount(); x++) {
        nameColumna = rsmd.getColumnName(x);
        typeData = rsmd.getColumnTypeName(x);
        if((typeData.equals("NUMBER")) && (rsmd.getScale(x)== 0)) {
          camposLlave.add(nameColumna);
        } // if
      } // for x
    } // try
    catch(Exception e) {
      Error.mensaje(e);
      throw e;
    }
    finally {
      if(rst != null)
        rst.close();
      rst = null;
      if(stm != null)
        stm.close();
      stm = null;
      if(connection != null)
        connection.close();
      connection = null;
    } // finally
    return camposLlave;
  }


   public List<String> recuperarCampos() throws Exception {
    List<String> camposLlave  = null;
    Connection connection     = null;
    Statement stm             = null;
    ResultSet rst             = null;
    ResultSetMetaData rsmd    = null;
    String nameColumna        = null;
    try {
      camposLlave = new ArrayList<String>();
      connection = DaoFactory.getInstance().getConnection();
      stm = connection.createStatement();
      rst = stm.executeQuery(this.detalleMotor.getResultadoXML());
      rsmd = rst.getMetaData();
      for(int x = 1; x <= rsmd.getColumnCount(); x++) {
        nameColumna = rsmd.getColumnName(x);
        if((rsmd.getScale(x)== 0)) {
          camposLlave.add(nameColumna);
        } // if
      } // for x
    } // try
    catch(Exception e) {
      mx.org.kaana.libs.formato.Error.mensaje(e);
      throw e;
    }
    finally {
      if(rst != null)
        rst.close();
      rst = null;
      if(stm != null)
        stm.close();
      stm = null;
      if(connection != null)
        connection.close();
      connection = null;
    } // finally
    return camposLlave;
  }

  public List<String> recuperarTodosCampos() throws Exception {
    List<String> camposLlave  = null;
    Connection connection     = null;
    Statement stm             = null;
    ResultSet rst             = null;
    ResultSetMetaData rsmd    = null;
    String nameColumna        = null;
    try {
      camposLlave = new ArrayList<String>();
      connection = DaoFactory.getInstance().getConnection();
      stm = connection.createStatement();
      rst = stm.executeQuery(this.detalleMotor.getResultadoXML());
      rsmd = rst.getMetaData();
      for(int x = 1; x <= rsmd.getColumnCount(); x++) {
        nameColumna = rsmd.getColumnName(x);
        camposLlave.add(nameColumna);
      } // for x
    } // try
    catch(Exception e) {
      mx.org.kaana.libs.formato.Error.mensaje(e);
      throw e;
    }
    finally {
      if(rst != null)
        rst.close();
      rst = null;
      if(stm != null)
        stm.close();
      stm = null;
      if(connection != null)
        connection.close();
      connection = null;
    } // finally
    return camposLlave;
  }



}
