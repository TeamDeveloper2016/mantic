package mx.org.kaana.kajool.procesos.importacion.reglas;

import java.io.Serializable;
import java.util.List;
import mx.org.kaana.kajool.procesos.importacion.beans.Importado;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date Jan 23, 2013
 *@time 9:24:29 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Contenido implements Serializable, IValidar {

  private static final long serialVersionUID=-175772408911775158L;
  private static final Log LOG=LogFactory.getLog(Contenido.class);

  @Override
  public boolean single(Importado file) {
    LOG.info(file);
    return true;
  }

  @Override
  public boolean all(List<Importado> files) {
    LOG.info(files);
    return true;
  }
}
