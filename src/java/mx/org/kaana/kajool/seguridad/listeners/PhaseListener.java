package mx.org.kaana.kajool.seguridad.listeners;

import javax.faces.context.ExternalContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.servlet.http.HttpServletResponse;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date Jun 29, 2012
 * @time 4:08:08 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class PhaseListener implements javax.faces.event.PhaseListener {
	
	private static final long serialVersionUID=-4394343772098097773L;

	@Override
  public PhaseId getPhaseId() {
    return PhaseId.RENDER_RESPONSE;
  }

	@Override
  public void afterPhase(PhaseEvent event) {
  }

	@Override
  public void beforePhase(PhaseEvent event) {
     ExternalContext ectx = event.getFacesContext().getExternalContext();
     HttpServletResponse response = (HttpServletResponse) ectx.getResponse();
     response.addHeader("X-UA-Compatible", "IE=edge");
  }
	
}
