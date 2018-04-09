package mx.org.kaana.libs.pagina;

import java.util.Map;

/**
 * @company Instituto Nacional de Estadistica y Geografia
 * @project KAJOOL (Control system polls)
 * @date 26/03/2014
 * @time 05:07:12 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public class BaseException extends KajoolBaseException {
  private static final long serialVersionUID = -9164866930711041059L;

  public BaseException(String key) {
    super(UIMessage.toMessage(key));
  }

  public BaseException(String proyecto, String key) {
    super(UIMessage.toMessage(proyecto, key));
  }

  public BaseException(String proyecto, String key, Map<String, Object> params) {
    super(UIMessage.toMessage(proyecto, key, params));
  }

  public BaseException(String proyecto, String key, Throwable e) {
    super(UIMessage.toMessage(proyecto, key), e);
  }

  public BaseException(String proyecto, String key, Map<String, Object> params, Throwable e) {
    super(UIMessage.toMessage(proyecto, key, params), e);
  }


}
