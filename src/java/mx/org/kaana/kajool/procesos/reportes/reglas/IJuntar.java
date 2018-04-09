package mx.org.kaana.kajool.procesos.reportes.reglas;

import java.util.List;
import mx.org.kaana.kajool.procesos.reportes.beans.Definicion;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 14/11/2016
 *@time 11:20:22 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public interface IJuntar {

	public String getNombre();
	public String getRegresar();
	public Boolean getComprimir();
  public List<Definicion> getDefiniciones();
  public Boolean getIntercalar();
	public Boolean getAutomatico();
}
