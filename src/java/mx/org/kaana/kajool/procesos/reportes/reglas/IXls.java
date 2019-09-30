package mx.org.kaana.kajool.procesos.reportes.reglas;

import java.util.Map;
import mx.org.kaana.kajool.procesos.reportes.beans.Modelo;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 12/10/2016
 *@time 02:46:37 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public interface IXls {

	public Modelo getModelo();
  public String getNombre();
  public String getRegresar();
  public Boolean getComprimir();
  public String getPatron();
  public String getCampos();
	public Map<String, Object> getParametros();
  public void setParametros(Map<String, Object> parametros);
  
}
