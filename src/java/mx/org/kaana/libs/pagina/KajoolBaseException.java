package mx.org.kaana.libs.pagina;

/**
 * @company Instituto Nacional de Estadistica y Geografia
 * @project KAJOOL (Control system polls)
 * @date 26/03/2014
 * @time 06:04:23 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public class KajoolBaseException extends RuntimeException {

	private static final long serialVersionUID=4197383798009448157L;

  public KajoolBaseException(Exception msg) {
    super(msg);
  }

  public KajoolBaseException(String msg) {
    super(msg);
  }

  public KajoolBaseException(String msg, Throwable e) {
    super(msg, e);
  }

  @Override
  public String toString() {
    String s = getClass().getSimpleName();
    String message = super.getLocalizedMessage();
    return (message != null) ? (s + ": " + message) : s;
  }
}
