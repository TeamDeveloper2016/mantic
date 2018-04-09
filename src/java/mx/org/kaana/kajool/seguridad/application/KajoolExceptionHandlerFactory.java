package mx.org.kaana.kajool.seguridad.application;

import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerFactory;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date Dec 20, 2012
 *@time 9:47:01 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class KajoolExceptionHandlerFactory extends ExceptionHandlerFactory {

	private ExceptionHandlerFactory base;

  public KajoolExceptionHandlerFactory(ExceptionHandlerFactory base) {
    this.base = base;
  }

	@Override
  public ExceptionHandler getExceptionHandler() {
    return new KajoolExceptionHandler(this.base.getExceptionHandler());
  }

}
