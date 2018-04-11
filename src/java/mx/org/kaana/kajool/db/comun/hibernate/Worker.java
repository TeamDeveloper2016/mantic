package mx.org.kaana.kajool.db.comun.hibernate;



import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import org.hibernate.jdbc.Work;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date Jul 3, 2012
 * @time 10:07:00 AM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Worker implements Work,Serializable  {
  
  private static final long serialVersionUID = 8642443248337783235L;

  private Connection connection;
  
  @Override
  public void execute(Connection conection) throws SQLException {
   this.connection = conection;
  }

  public Connection getConnection() {
    return connection;
  }

}
