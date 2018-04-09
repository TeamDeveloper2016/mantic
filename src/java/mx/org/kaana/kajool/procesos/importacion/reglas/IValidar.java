package mx.org.kaana.kajool.procesos.importacion.reglas;

import java.util.List;
import mx.org.kaana.kajool.procesos.importacion.beans.Importado;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date Jan 22, 2013
 *@time 4:16:25 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public interface IValidar {

   public boolean single(Importado file);
   public boolean all(List<Importado> files);

}
