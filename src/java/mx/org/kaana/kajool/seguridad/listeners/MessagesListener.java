package mx.org.kaana.kajool.seguridad.listeners;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date Sep 28, 2012
 *@time 9:50:07 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 *
 */

/**
 * Enables messages to be rendered on different pages from which they were set.
 *
 * After each phase where messages may be added, this moves the messages
 * from the page-scoped FacesContext to the session-scoped session map.
 *
 * Before messages are rendered, this moves the messages from the
 * session-scoped session map back to the page-scoped FacesContext.
 *
 * Only global messages, not associated with a particular component, are
 * moved. Component messages cannot be rendered on pages other than the one on
 * which they were added.
 *
 * To enable multi-page messages support, add a <code>lifecycle</code> block to your
 * faces-config.xml file. That block should contain a single
 * <code>phase-listener</code> block containing the fully-qualified classname
 * of this file.
 *
 * @author Jesse Wilson jesse[AT]odel.on.ca
 * @secondaryAuthor Lincoln Baxter III lincoln[AT]ocpsoft.com
 */
public class MessagesListener implements javax.faces.event.PhaseListener {
	
	private static final long serialVersionUID=-3277756885369792777L;
  private static final String sessionToken = "MULTI_PAGE_MESSAGES_SUPPORT";
	
 /*
  * Save messages into the session after every phase.
  */
	@Override
	public void afterPhase(PhaseEvent event) {
    if(!PhaseId.RENDER_RESPONSE.equals(event.getPhaseId())){
       FacesContext facesContext = event.getFacesContext();
       this.save(facesContext);
    }	// if
	}

	/*
	 * Check to see if we are "naturally" in the RENDER_RESPONSE phase. If we
	 * have arrived here and the response is already complete, then the page is
	 * not going to show up: don't display messages yet.
	 */	
	@Override
	public void beforePhase(PhaseEvent event) {
		FacesContext facesContext= event.getFacesContext();
		this.save(facesContext);
		if(PhaseId.RENDER_RESPONSE.equals(event.getPhaseId())) {
			if(!facesContext.getResponseComplete())
				this.restore(facesContext);
		} // if
	}

	@Override
	public PhaseId getPhaseId() {
		return PhaseId.ANY_PHASE;
	}

  @SuppressWarnings("unchecked")
	private int save(final FacesContext facesContext) {
		List<FacesMessage> messages=new ArrayList<FacesMessage>();
		for(Iterator<FacesMessage> iter=facesContext.getMessages(null); iter.hasNext();) {
			messages.add(iter.next());
			iter.remove();
		} // for
		if(messages.isEmpty())
			return 0;
		Map<String, Object> sessionMap=facesContext.getExternalContext().getSessionMap();
		List<FacesMessage> existingMessages=(List<FacesMessage>) sessionMap.get(sessionToken);
		if(existingMessages!= null)
			existingMessages.addAll(messages);
		else
			sessionMap.put(sessionToken, messages);
		return messages.size();
	}

	@SuppressWarnings("unchecked")
	private int restore(final FacesContext facesContext) {
		Map<String, Object> sessionMap=facesContext.getExternalContext().getSessionMap();
		List<FacesMessage> messages=(List<FacesMessage>) sessionMap.remove(sessionToken);
		if(messages==null)
			return 0;
		int restoredCount=messages.size();
		for(Object element: messages)
			facesContext.addMessage(null, (FacesMessage) element);
		return restoredCount;
	}
	
}
