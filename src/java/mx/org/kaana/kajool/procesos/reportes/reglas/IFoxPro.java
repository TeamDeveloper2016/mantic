package mx.org.kaana.kajool.procesos.reportes.reglas;

import java.util.List;
import mx.org.kaana.kajool.procesos.reportes.beans.Modelo;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 12/10/2016
 *@time 02:46:37 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public interface IFoxPro {

  public String getNombreDBF(Integer index);
  public String getPatron();
  public String getNombre();
  public String getRegresar();
  public Boolean getComprimir();
  public List<Modelo> getDefiniciones();  
	public Boolean getValidate();
	public Boolean getAsignarNombreDbf();
  
}
