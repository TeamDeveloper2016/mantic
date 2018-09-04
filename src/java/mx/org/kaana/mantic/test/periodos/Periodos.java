package mx.org.kaana.mantic.test.periodos;

import mx.org.kaana.mantic.consultas.enums.EPeriodos;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 31/08/2018
 * @time 01:17:08 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public class Periodos {

	private static final Log LOG=LogFactory.getLog(Periodos.class);
	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		LOG.info("HOY: "+ EPeriodos.HOY.toString());
		LOG.info("ULTIMOS 7 DIAS: "+ EPeriodos.ULTIMOS_7DIAS.toString());
		LOG.info("ULTIMA SEMANA: "+ EPeriodos.ULTIMA_SEMANA.toString());
		// LOG.info("ULTIMA 15 DIAS: "+ EPeriodos.ULTIMOS_15DIAS.toString());
		// LOG.info("ULTIMA QUINCENA: "+ EPeriodos.ULTIMA_QUINCENA.toString());
	}

}
