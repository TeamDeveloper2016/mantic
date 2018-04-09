package mx.org.kaana.kajool.seguridad.application;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.faces.FacesException;
import javax.faces.application.ViewHandler;
import javax.faces.application.ViewHandlerWrapper;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import mx.org.kaana.libs.formato.Error;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 5/08/2014
 * @time 09:45:40 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public class KajoolViewHandle extends ViewHandlerWrapper {

	private static final Log LOG=LogFactory.getLog(KajoolViewHandle.class);
	private ViewHandler parent;

	public KajoolViewHandle(ViewHandler parent) {
		LOG.info("KajoolViewHandle Parent View Handler:"+parent.getClass());
		this.parent=parent;
	}

	@Override
	public Locale calculateLocale(FacesContext facesContext) {
		return parent.calculateLocale(facesContext);
	}

	@Override
	public String calculateRenderKitId(FacesContext facesContext) {
		String renderKitId=parent.calculateRenderKitId(facesContext);
		LOG.info("KajoolViewHandle.calculateRenderKitId():RenderKitId: "+renderKitId);
		return renderKitId;
	}

	@Override
	public UIViewRoot createView(FacesContext facesContext, String viewId) {
		return parent.createView(facesContext, viewId);
	}

	@Override
	public String getActionURL(FacesContext facesContext, String actionId) {
		return parent.getActionURL(facesContext, actionId);
	}

	@Override
	public String getResourceURL(FacesContext facesContext, String resId) {
		return parent.getResourceURL(facesContext, resId);
	}

	@Override
	public String getRedirectURL(FacesContext context, String viewId, Map<String, List<String>> parameters, boolean includeViewParams) {
		return parent.getRedirectURL(context, viewId, parameters, includeViewParams);
	}

	@Override
	public String getBookmarkableURL(FacesContext context, String viewId, Map<String, List<String>> parameters, boolean includeViewParams) {
		return parent.getBookmarkableURL(context, viewId, parameters, includeViewParams);
	}
	
	

	@Override
	public void renderView(FacesContext facesContext, UIViewRoot uivr) throws IOException, FacesException {
		parent.renderView(facesContext, uivr);
	}

	@Override
	public UIViewRoot restoreView(FacesContext facesContext, String viewId) {
		UIViewRoot root=null;
		root=parent.restoreView(facesContext, viewId);		
		if (root==null) {
			root=createView(facesContext, viewId);
			try {
			  facesContext.getApplication().getViewHandler().getViewDeclarationLanguage(facesContext, viewId).buildView(facesContext, root);
			}// try
			catch (Exception e) {
			  Error.mensaje(e);
			}// catch			
		}
		return root;
	}

	@Override
	public void writeState(FacesContext facesContext) throws IOException {
		parent.writeState(facesContext);
	}

	@Override
	public ViewHandler getWrapped() {
		return this.parent;
	}	
	
}
