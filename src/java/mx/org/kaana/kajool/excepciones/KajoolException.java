package mx.org.kaana.kajool.excepciones;

import java.util.Map;
import mx.org.kaana.libs.pagina.BaseException;
import mx.org.kaana.kajool.enums.EExcepciones;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 16/04/2014
 *@time 02:07:27 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class KajoolException extends BaseException {

  private static final String KAJOOL= "kajool";
  private static final long serialVersionUID = 4681084408352851951L;

  public KajoolException(EExcepciones excepcion) {
    super(KAJOOL, excepcion.getKey());
  }

  public KajoolException(EExcepciones excepcion, Map<String, Object> params){
    super(KAJOOL, excepcion.getKey(), params);
  }

}
