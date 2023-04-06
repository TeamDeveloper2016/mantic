package mx.org.kaana.kajool.seguridad.filters;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.kajool.procesos.acceso.beans.Autentifica;
import mx.org.kaana.kajool.procesos.beans.UsuariosEnLinea;
import mx.org.kaana.kajool.procesos.beans.Usuario;
import mx.org.kaana.kajool.seguridad.filters.control.LockUser;
import mx.org.kaana.libs.pagina.JsfBase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


@WebFilter("/Paginas/*")
public class PageFilter extends HttpServlet implements Filter {

  private static final long serialVersionUID = 1031685320126950899L;
  private static final Log LOG = LogFactory.getLog(PageFilter.class);
  private static final String PAGE_EXTENSION_EXCLUDE = "|jpg|gif|png|bmp|jpeg|css|js|txt|pdf|xls|dbf|html|jasper|jrxml|";
  private FilterConfig filterConfig;
  private String paginaIndice;
  private String paginaExclusion;
  private String paginaSesion;
  private String paginaBloqueo;
  private ServletContext application;
  private String mensaje;
  private String mensajeModificado;

  public void setFilterConfig(FilterConfig filterConfig) {
    this.filterConfig = filterConfig;
  }

  public FilterConfig getFilterConfig() {
    return filterConfig;
  }

  public void setPaginaIndice(String paginaIndice) {
    if (paginaIndice == null) {
      this.paginaIndice = "/acceso";
    } else {
      this.paginaIndice = paginaIndice;
    }
  }

  public String getPaginaIndice() {
    return paginaIndice;
  }

  public void setPaginaExclusion(String paginaExclusion) {
    if (paginaExclusion == null) {
      //this.paginaExclusion = "/Librerias/Funciones/errorPage.jsp?error=Favor de autenticarse";
      this.paginaExclusion = "/Exclusiones/error";
    } else {
      this.paginaExclusion = paginaExclusion;
    }
  }

  public String getPaginaExclusion() {
    return paginaExclusion;
  }

  public void setPaginaSesion(String paginaSesion) {
    if (paginaSesion == null) {
      //this.paginaSesion = "/Librerias/Funciones/errorPage.jsf?error=Error&loginError=kajool.admin";
      this.paginaSesion = "/Exclusiones/error";
    } else {
      this.paginaSesion = paginaSesion;
    }
  }

  public String getPaginaSesion() {
    return paginaSesion;
  }

  public void setPaginaBloqueo(String paginaBloqueo) {
    if (paginaBloqueo == null) {
      this.paginaBloqueo = "/Exclusiones/bloqueo";
    } else {
      this.paginaBloqueo = paginaBloqueo;
    }
  }

  public String getPaginaBloqueo() {
    return paginaBloqueo;
  }

  public void setApplication(ServletContext application) {
    this.application = application;
  }

  public ServletContext getApplication() {
    return application;
  }

  public void setMensaje(String mensaje) {
    if (mensaje == null) {
      this.mensaje = "/Paginas/Mantenimiento/Notificar/mensaje.txt";
    } else {
      this.mensaje = mensaje;
    }
  }

  public void setMensajeModificado(String mensajeModificado) {
    if (mensajeModificado == null) {
      this.mensajeModificado = "/Paginas/Mantenimiento/Notificar/mensajeModificado.txt";
    } else {
      this.mensajeModificado = mensajeModificado;
    }
  }

  public String getMensaje() {
    return mensaje;
  }

  public String getMensajeModificado() {
    return mensajeModificado;
  }

  @Override
  public void init(FilterConfig filterConfig) {
    setFilterConfig(filterConfig);
    setApplication(filterConfig.getServletContext());
    setPaginaIndice(filterConfig.getInitParameter("paginaIndice"));
    setPaginaExclusion(filterConfig.getInitParameter("paginaExclusion"));
    setPaginaSesion(filterConfig.getInitParameter("paginaSesion"));
    setPaginaBloqueo(filterConfig.getInitParameter("paginaBloqueo"));
    setMensaje(filterConfig.getInitParameter("mensaje"));
    setMensajeModificado(filterConfig.getInitParameter("mensajeModificado"));
  }

  private void setHeaders(ServletResponse response) {
    HttpServletResponse httpServletResponse = (HttpServletResponse) response;
    httpServletResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
    httpServletResponse.setHeader("Pragma", "no-cache"); // HTTP 1.0.
    httpServletResponse.setDateHeader("Expires", 0); // Proxies.
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
    String contexto        = ((HttpServletRequest) request).getContextPath() != null ? ((HttpServletRequest) request).getContextPath() : "/".concat(Configuracion.getInstance().getPropiedad("sistema.contexto"));
    String pagina          = ((HttpServletRequest) request).getRequestURI();
    HttpSession session    = null;
    Autentifica autentifica= null;
    LockUser lockUsers      = null;
    try {
      LOG.info("Procesando recurso ".concat(pagina));
      if (isProcessFilter(pagina)) {
        session = ((HttpServletRequest) request).getSession();
        if (session != null) {
          //response.setCharacterEncoding("ISO-8859-1");
          lockUsers   = (LockUser)request.getServletContext().getAttribute(Constantes.ATRIBUTO_BLOQUEO_USUARIOS);
          autentifica = (Autentifica) session.getAttribute(Constantes.ATRIBUTO_AUTENTIFICA);
          if (autentifica!= null && !JsfBase.isLockUsers(lockUsers, autentifica) && (autentifica.isFirmado() || Configuracion.getInstance().isFreeAccess())) {
            setHeaders(response);
            autentifica.setPaginaActual(pagina);
            filterChain.doFilter(request, response);
            this.processMessage(lockUsers, autentifica, request.getServletContext(), session, response);
          } // if
          else 
            this.redireccionar((HttpServletResponse) response, contexto.concat(paginaBloqueo));
        } // if
        else {
          redireccionar((HttpServletResponse) response, contexto.concat(paginaBloqueo));
        }
      } // if
      else {
        redireccionar((HttpServletResponse) response, contexto.concat(paginaBloqueo));
      }
    } // try
    catch(IllegalStateException e) {
      redireccionar((HttpServletResponse) response, contexto.concat(paginaBloqueo));
    } //
    catch (Exception e) {
      Error.mensaje(e);
      redireccionar((HttpServletResponse) response, contexto.concat(paginaBloqueo));
    } // catch
  }

  private boolean isProcessFilter(String pagina) {
    String extension = pagina.substring(pagina.lastIndexOf(".") + 1).toLowerCase();
    return PAGE_EXTENSION_EXCLUDE.indexOf(extension) == -1;
  }

  private void redireccionar(HttpServletResponse response, String pagina) throws IOException {
    if (getFilterConfig() != null) {
      if (pagina != null && !"".equals(pagina)) {
        response.sendRedirect(pagina);
      }	// if
    } // if
  } // redireccionar

  @Override
  public void destroy() {
    setFilterConfig(null);
  }

  @Override
  public void finalize() {
    filterConfig = null;
  }

  private void processMessage(LockUser lockUsers, Autentifica autentifica, ServletContext application, HttpSession session, ServletResponse response) {
    PrintWriter writer= null;
    try {
      if(response.getContentType()== null || response.getContentType().indexOf("text/html")>= 0) {
        writer= response.getWriter();
        writer.print("<div class=\"DispNone\">");
        writer.print("<span>Copyright(c) MANTIC 2018 application</span><br/>");
        writer.print("<span>Usuario:Team Developer 2018 <team.developer@kaana.org.mx>@kaana.org.mx></span>");
        writer.print("</div>");
        UsuariosEnLinea users= (UsuariosEnLinea)application.getAttribute(Constantes.ATRIBUTO_USUARIOS_SITIO);
        Usuario user         = (Usuario)users.getCuenta(session.getId(), autentifica.getPersona().getCuenta());
        if(lockUsers.isActivedAndMoreMessage() && !user.getMensaje().equals(lockUsers.toIndexMessage())) {
          user.setMensaje(lockUsers.toIndexMessage());
          writer.print("<script type=\"text/javascript\">");
          writer.print("  $(document).ready(function(){if(typeof(PF('notification'))!== 'undefined') {PF('notification').show();setTimeout(\"PF('notification').hide();\", 15000)}})");
          writer.print("</script>");
        } // if  
      } // if  
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
  }
  
}
