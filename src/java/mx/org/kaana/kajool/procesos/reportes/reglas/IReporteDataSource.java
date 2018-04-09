package mx.org.kaana.kajool.procesos.reportes.reglas;

import net.sf.jasperreports.engine.JRDataSource;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 9/09/2015
 * @time 05:08:54 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public interface IReporteDataSource extends IReporte {

	public JRDataSource getJrDataSource();
}
