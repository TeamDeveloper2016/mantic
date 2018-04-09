package mx.org.kaana.kajool.procesos.reportes.reglas;

import java.util.Map;
import mx.org.kaana.kajool.enums.EFormatos;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 1/09/2015
 *@time 10:32:59 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public interface IReporte {

	public String getJrxml();
	public Map<String, Object> getParametros();
	public String getProceso();
	public String getIdXml();
	public Map<String, Object> getParams();
	public String getNombre();
	public EFormatos getFormato();
	public Long getTitulo();
	public Boolean getAutomatico();
	public String getRegresar();
	public Boolean getComprimir();
}
