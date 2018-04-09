package mx.org.kaana.libs.pagina;

import java.io.Writer;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import mx.org.kaana.libs.Constantes;
import static mx.org.kaana.libs.pagina.UIMessage.toMessage;
import mx.org.kaana.kajool.enums.EBrowser;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.recurso.Configuracion;
import org.primefaces.context.RequestContext;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 7/05/2015
 *@time 01:41:09 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */
@ManagedBean(name = "jsfUtilities")
@ApplicationScoped
public  class  JsfUtilities {

	
  public static FacesContext getFacesContext() {
    return FacesContext.getCurrentInstance();
  }

  public static ExternalContext getExternalContext() {
    return FacesContext.getCurrentInstance().getExternalContext();
  }

  public static UIComponent getViewRoot() {
    return FacesContext.getCurrentInstance().getViewRoot();
  }

  public static HttpServletRequest getRequest() {
    return (HttpServletRequest)getFacesContext().getExternalContext().getRequest();
  }

  public static ServletContext getApplication() {
    return getSession().getServletContext();
  }

  public static HttpSession getSession() {
    return (HttpSession)getRequest().getSession();
  }

  public static HttpServletResponse getResponse() {
    return (HttpServletResponse)getExternalContext().getResponse();
  }

  public static Writer getOutHtml() {
    return (Writer)FacesContext.getCurrentInstance().getResponseWriter();
  }

  public static String getParameter(String parametro) {
    return (String)getExternalContext().getRequestMap().get(parametro);
  }

  public static String getParametro(String parametro) {
		String regresar= getExternalContext().getRequestParameterMap().get(parametro);
		if(regresar== null)
			regresar= getParameter(parametro);
    return regresar;
  }

  public static Object getAttribute(String atributo) {
    return getExternalContext().getRequestMap().get(atributo);
  }

  public static String getRealPath(String path) {
    if (path != null)
      return getApplication().getRealPath(path);
    else
      return null;
  }

  public static String getRealPath() {
    return getRealPath("/");
  }

  public static void redirect(String url) throws Exception {
    getExternalContext().redirect(url);
  }

  public static String getRootViewId() {
    return FacesContext.getCurrentInstance().getViewRoot().getViewId();
  }

  public static String getRootViewComponentId() {
    return getViewRoot().getId();
  }

  public static Object ELAsObject(String expresion) {
    return FacesContext.getCurrentInstance().getApplication().getELResolver().getValue(FacesContext.getCurrentInstance().getELContext(), null, expresion);
  }	
	
  public static Map<String, String> getParameterMap() {
    return getExternalContext().getRequestParameterMap();
  }

  public static boolean isPostBack() {
  	return FacesContext.getCurrentInstance().isPostback();  	
  }

  public static HttpServletResponse obtenerHttpServletResponse() {
    return (HttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse();
  }

  public static HttpServletRequest obtenerHttpServletRequest() {
    return (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
  }

  public static String getContext() {
    return getRequest().getContextPath();
  }

  public static Object removePageEL(String key) {
    return JsfUtilities.getViewRoot().getAttributes().remove(key);
  }

  public static Object getPageEL(String key) {
    return JsfUtilities.getViewRoot().getAttributes().get(key);
  }

  public static void setPageEL(String key, Object value) {
    JsfUtilities.getViewRoot().getAttributes().put(key, value);
  }	
	
	public static void setFlashAttributeInterprete (String key, Object value) {  	
	   FacesContext.getCurrentInstance().getExternalContext().getFlash().put(key, value);
		 JsfUtilities.getSession().removeAttribute("kajoolCapturaInterpreteLanzador");
	}

  public static void setFlashAttribute (String key, Object value) {  	
  	FacesContext.getCurrentInstance().getExternalContext().getFlash().put(key, value);
  }

  public static Object getFlashAttribute (String key) {
  	return FacesContext.getCurrentInstance().getExternalContext().getFlash().get(key);
  }

  public static void setViewAttribute (String key, Object value) {  	
  	FacesContext.getCurrentInstance().getViewRoot().getViewMap().put(key, value);
  }

  public static Object getViewAttribute (String key) {
  	return FacesContext.getCurrentInstance().getViewRoot().getViewMap().get(key);
  }

  public static void addMessage(String descripcion) {
    addMessage(ETipoMensaje.INFORMACION.getTituloMensaje(), descripcion);
  }

  public static void addMessage(String descripcion, ETipoMensaje tipoMensaje) {
    addMessage(tipoMensaje.getTituloMensaje(), descripcion, tipoMensaje);
  }

  public static void addMessage(String titulo, String descripcion) {
    addMessage(titulo, descripcion, ETipoMensaje.INFORMACION);
  }

  public static void addMessage(String titulo, String descripcion, ETipoMensaje tipoMensaje) {
  	FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(tipoMensaje.getTipoMensaje(), titulo, descripcion));				
  }

  public static void addMessageError(Exception e) {
  	FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(ETipoMensaje.ERROR.getTipoMensaje(), ETipoMensaje.ERROR.getTituloMensaje(), String.valueOf(e)));				
  }
	
	/*Metodos para lanzar los mensajes que se encuentran registrados en el archivo properties de cada uno de los proyectos*/
	
  public static void addMsgProperties(String key) {		
    addMessage(ETipoMensaje.INFORMACION.getTituloMensaje(), toMessage(key));
  }

	public static void addMsgProperties(String proyecto, String key) {		
    addMessage(ETipoMensaje.INFORMACION.getTituloMensaje(), toMessage(proyecto, key));
  }
	
	public static void addMsgProperties(String key, Map<String, Object> params) {		
    addMessage(ETipoMensaje.INFORMACION.getTituloMensaje(), toMessage(key, params));
  }
	
	public static void addMsgProperties(String proyecto, String key, Map<String, Object> params) {		
    addMessage(ETipoMensaje.INFORMACION.getTituloMensaje(), toMessage(proyecto, key, params));
  }

  public static void addMsgProperties(String key, ETipoMensaje tipoMensaje) {
    addMessage(tipoMensaje.getTituloMensaje(), toMessage(key), tipoMensaje);
  }

	public static void addMsgProperties(String proyecto, String key, ETipoMensaje tipoMensaje) {
    addMessage(tipoMensaje.getTituloMensaje(), toMessage(proyecto, key), tipoMensaje);
  }
	
	public static void addMsgProperties(String key, Map<String, Object> params, ETipoMensaje tipoMensaje) {
    addMessage(tipoMensaje.getTituloMensaje(), toMessage(key, params), tipoMensaje);
  }
	
	public static void addMsgProperties(String proyecto, String key, Map<String, Object> params, ETipoMensaje tipoMensaje) {
    addMessage(tipoMensaje.getTituloMensaje(), toMessage(proyecto, key, params), tipoMensaje);
  }

  public static void addMsgsProperties(String key, String titulo) {
    addMessage(titulo, toMessage(key), ETipoMensaje.INFORMACION);
  }
	
  public static void addMsgProperties(String proyecto, String key, String titulo) {
    addMessage(titulo, toMessage(proyecto, key), ETipoMensaje.INFORMACION);
  }
	
  public static void addMsgProperties(String key, Map<String, Object> params, String titulo) {
    addMessage(titulo, toMessage(key, params), ETipoMensaje.INFORMACION);
  }
	
  public static void addMsgProperties(String proyecto, String key, Map<String, Object> params, String titulo) {
    addMessage(titulo, toMessage(proyecto, key, params), ETipoMensaje.INFORMACION);
  }

  public static void addMsgsProperties(String key, String titulo, ETipoMensaje tipoMensaje) {
  	FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(tipoMensaje.getTipoMensaje(), titulo, toMessage(key)));				
  }
	
  public static void addMsgProperties(String proyecto, String key, String titulo, ETipoMensaje tipoMensaje) {
  	FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(tipoMensaje.getTipoMensaje(), titulo, toMessage(proyecto, key)));				
  }
	
  public static void addMsgProperties(String key, Map<String, Object> params, String titulo, ETipoMensaje tipoMensaje) {
  	FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(tipoMensaje.getTipoMensaje(), titulo, toMessage(key, params)));				
  }
	
  public static void addMsgProperties(String proyecto, String key, Map<String, Object> params, String titulo, ETipoMensaje tipoMensaje) {
  	FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(tipoMensaje.getTipoMensaje(), titulo, toMessage(proyecto, key, params)));				
  }

  public static Object seekParameter(String key) {
		Object value = null;
		if (getParametro(key) != null) {
		  value = getParametro(key);
		} // if		
		else
			if ( getFlashAttribute(key) != null)
		    value = getFlashAttribute(key);			

		return value;
	}	
	
  public static String getSessionId() {
    return getSession().getId();
  }	

	public static void keepMessage() {
    Flash flash= JsfUtilities.getExternalContext().getFlash();
    flash.setKeepMessages(true);
	}
	
	public static Object findComponent(String name) {
		return FacesContext.getCurrentInstance().getViewRoot().findComponent(Constantes.NOMBRE_FORMULARIO.concat(name));
	}
	
  public static void cleanFlashParams(){
    Flash flash = getFacesContext().getExternalContext().getFlash();
    Iterator iterator = flash.entrySet().iterator();
    while (iterator.hasNext()) {
      Map.Entry item = (Map.Entry)iterator.next();
      flash.remove(item.getKey().toString());
    } // while
  }

  public static void addRequestParams(Map<String, String> params) {
		for(String name: params.keySet()) {
      JsfUtilities.getExternalContext().getRequestMap().put(name, params.get(name));
      JsfUtilities.setFlashAttribute(name, params.get(name));
    }
	}

  public static String toPathAplication(){
    return System.getProperty("user.dir");
  }

  public static void addAlert(String titulo, String descripcion, ETipoMensaje tipoMensaje) {
   FacesMessage alert = new FacesMessage(tipoMensaje.getTipoMensaje(), titulo, descripcion);
   RequestContext.getCurrentInstance().showMessageInDialog(alert);
  }

  public static void addAlert(String descripcion, ETipoMensaje tipoMensaje) {
     addAlert(tipoMensaje.getTituloMensaje(),descripcion,tipoMensaje);
  }

  public static void addAlert(String descripcion) {
     addAlert(descripcion, ETipoMensaje.INFORMACION);
  }

  public static EBrowser getBrowser() {
    ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
    String userAgent = externalContext.getRequestHeaderMap().get("User-Agent");
    return EBrowser.detect(userAgent);
  }

  public static String doContextRoot () {
     return Cadena.stringToBytes(getContext());
  }

  public static String getStage() {
    return Configuracion.getInstance().getEtapaServidor().toLowerCase();
  }

  public static String getDefaultJsKajool() {
		return Constantes.ENTER.concat("<script type=\"text/javascript\">Janal.Control.name= [").concat(doContextRoot()).concat("];Janal.Control.stage= [").concat(Cadena.stringToBytes(getStage())).concat("];</script>").concat(Constantes.ENTER);
  }

  public static void  cleanSesion(HttpSession session) {
    Enumeration attributes = session.getAttributeNames();
    String elemento = null;
    while (attributes.hasMoreElements()) {
      elemento = attributes.nextElement().toString();
      try {
        if (elemento != null) {
          session.removeAttribute(elemento);
        }
      } // try
      catch (Exception e) {
        Error.mensaje(e);
      } // catch
    } // while
  }

}
