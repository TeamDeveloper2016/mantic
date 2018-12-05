package mx.org.kaana.kajool.test;

import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 4/12/2018
 * @time 09:41:20 PM 
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public class SQL {

	  private static final Log LOG=LogFactory.getLog(SQL.class);
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
       Entity count= (Entity)DaoFactory.getInstance().toEntity("select count(*) as total from tc_mantic_facturas");
			 LOG.info(count.toMap());
    }

}
