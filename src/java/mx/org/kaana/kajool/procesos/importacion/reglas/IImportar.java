package mx.org.kaana.kajool.procesos.importacion.reglas;

import java.util.List;
import mx.org.kaana.kajool.procesos.importacion.beans.Importado;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date Jan 22, 2013
 *@time 4:00:33 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public interface IImportar {

  public String getTitle();
  public String getReturn();
  public String getContinue();
  public String getFormats();
  public IValidar validate();
  public List<Importado> getFiles();
  public boolean isMultiple();

}
