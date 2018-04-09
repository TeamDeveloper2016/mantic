package mx.org.kaana.kajool.seguridad.servlets;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 26/03/2015
 * @time 04:04:08 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import javax.faces.webapp.FacesServlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@WebServlet(name = "ResourcesRegistrers", loadOnStartup = 1, urlPatterns = {"*.css","*.js", "*.less"})
public class ResourcesRegistrers extends HttpServlet {
	
	private static final Log LOG              = LogFactory.getLog(ResourcesRegistrers.class);
	private static final long serialVersionUID= -3348325900500571351L;	
  private static final String RESOURCES_PATH= "mx.org.kaana.seguridad.urlresolver.RESOURCES_PATH";

  private String nameContext;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
    this.nameContext= config.getInitParameter(RESOURCES_PATH);
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/* if this servlet is not mapped to a path, use the request URI */
		String path= request.getPathInfo();
		if (path==null)
			path= request.getRequestURI().substring(request.getContextPath().length());
    if(this.nameContext== null)
      this.nameContext= request.getContextPath().substring(1);
  	LOG.debug("ResourcesRegistrers ".concat(this.nameContext).concat(", ").concat(path));
		URL resource= Thread.currentThread().getContextClassLoader().getResource(this.nameContext.toLowerCase().concat("/").concat(path.substring(1)));
		if (resource== null) {
			ServletContext sc= getServletContext();
			String filename  = sc.getRealPath(path);
			try {
				resource= sc.getResource(path);
        if(resource== null)
	    		LOG.info("During load: "+ resource+ ": "+path +" : "+ filename+ "  main resource.");
  		} // trye
			catch (Exception e) {
  			LOG.warn(resource+ ": "+path +" : "+ filename+ "  not found.");
			} // Recurso en proyecto principal
			if (resource==null)
				response.sendError(HttpServletResponse.SC_NOT_FOUND, path+ " denied");
		}
		/* failure conditions */
		if (path.endsWith(".seam")) {
			javax.faces.webapp.FacesServlet facesServlet=new FacesServlet();
			facesServlet.service(request, response);
			return;
		} // if
		if (path.endsWith(".class")) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN, path+" denied");
			return;
		} // if
		if (resource != null) {
		/* check modification date */
			URLConnection connection=resource.openConnection();
			long lastModified=connection.getLastModified();
			long ifModifiedSince=request.getDateHeader("If-Modified-Since");
			if (ifModifiedSince!=-1&&lastModified<=ifModifiedSince) {
				response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
				return;
			} // if
			 /* write to response */
			response.setContentType(getServletContext().getMimeType(path));
			OutputStream out=new BufferedOutputStream(response.getOutputStream(), 512);
			InputStream in=new BufferedInputStream(resource.openStream(), 512);
			try {
				int len;
				byte[] data=new byte[512];
				while ((len=in.read(data))!=-1) {
					out.write(data, 0, len);
				} // while
			} // try
			finally {
				out.close();
				in.close();
				if (connection.getInputStream()!=null) {
					connection.getInputStream().close();
				} // if
			} // finally
		} // if
	} /* doGet() */

	@Override
	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
