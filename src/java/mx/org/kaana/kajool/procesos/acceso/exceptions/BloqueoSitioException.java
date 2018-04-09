package mx.org.kaana.kajool.procesos.acceso.exceptions;

import mx.org.kaana.libs.pagina.BaseException;

/**
 * @company Instituto Nacional de Estadistica y Geografia
 * @project KAJOOL (Control system polls)
 * @date 26/03/2014
 * @time 04:34:47 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public class BloqueoSitioException extends BaseException {

  private static final long serialVersionUID = 7683180225349045579L;

  public BloqueoSitioException() {
    super("janal.bloqueo.error");
  }

  public BloqueoSitioException(String msg) {
    super(msg);
  }

}
