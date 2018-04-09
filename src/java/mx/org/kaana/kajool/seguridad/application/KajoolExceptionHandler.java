package mx.org.kaana.kajool.seguridad.application;

import java.util.Iterator;
import javax.faces.FacesException;
import javax.faces.application.NavigationHandler;
import javax.faces.application.ViewExpiredException;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date Dec 20, 2012
 * @time 9:48:50 AM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 *
 */
public class KajoolExceptionHandler extends ExceptionHandlerWrapper {

	private ExceptionHandler wrapped;

	public KajoolExceptionHandler(ExceptionHandler wrapped) {
		this.wrapped=wrapped;
	}

	@Override
	public ExceptionHandler getWrapped() {
		return this.wrapped;
	}

	@Override
	public void handle() throws FacesException {
		Iterable events=this.wrapped.getUnhandledExceptionQueuedEvents();
		for(Iterator it=events.iterator(); it.hasNext();) {
			ExceptionQueuedEvent event      = (ExceptionQueuedEvent) it.next();
			ExceptionQueuedEventContext eqec= event.getContext();
			FacesContext context            = eqec.getContext();
			NavigationHandler navHandler    = context.getApplication().getNavigationHandler();
			if((eqec.getException() instanceof ViewExpiredException)) {
				try {
					//navHandler.handleNavigation(context, null, "/indice?faces-redirect=true&expired=true");
				} // try
				finally {
					it.remove();
				} // finally
			} // if
			else {
				try {

					//navHandler.handleNavigation(context, null, "/Paginas/Contenedor/error?faces-redirect=true&expired=true");

					//context.getExternalContext().getRequestMap().put("javax.servlet.error.exception", eqec.getException());
					//navHandler.handleNavigation(context, null, "/Paginas/Contenedor/error?faces-redirect=true");

				} // try
				finally {
					it.remove();
				} // finally
			} // else
		}
		this.wrapped.handle();
	}
	
}
