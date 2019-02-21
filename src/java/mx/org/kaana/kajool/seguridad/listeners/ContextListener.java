package mx.org.kaana.kajool.seguridad.listeners;

import java.beans.PropertyEditorManager;
import java.io.File;
import java.io.FileInputStream;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.pagina.Messages;
import mx.org.kaana.kajool.procesos.beans.UsuariosEnLinea;
import mx.org.kaana.kajool.seguridad.filters.control.LockUser;
import mx.org.kaana.kajool.seguridad.init.Loader;
import mx.org.kaana.libs.archivo.Archivo;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reportes.FileSearch;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@WebListener("Monitoreo del sitio web")
public class ContextListener implements ServletContextListener {

  private static final Log LOG = LogFactory.getLog(ContextListener.class);

  @Override
  public void contextInitialized(ServletContextEvent event) {
    ServletContext context = event.getServletContext();
    setInitialAttribute(context, "nombreAplicacion", Constantes.NOMBRE_DE_APLICACION);
    UsuariosEnLinea usuarios= null;
    System.setProperty("java.awt.headless", "true");
    LOG.info("[+SISTEMA] ".concat((String) context.getAttribute("nombreAplicacion")));
    try {
      editors();
      usuarios= new UsuariosEnLinea();
      context.setAttribute(Constantes.ATRIBUTO_USUARIOS_SITIO, usuarios);
      context.setAttribute(Constantes.ATRIBUTO_BLOQUEO_USUARIOS, new LockUser());
      LOG.info("[+CONTROL DE USUARIOS] ".concat(Fecha.formatear(Fecha.FECHA_HORA, usuarios.getHora())));
      pathLoadFile(context);
      setJasperTempPath(context);
      Loader.getInstance(event);
      System.setProperty("java.awt.headless", "true");
      LOG.info("[+PASANDO LOGOTIPOS]");
			this.toMoveResource(Configuracion.getInstance().getPropiedadSistemaServidor("proveedores"), context.getRealPath("/resources/janal/img/proveedores/"), "*");
      LOG.info("[-PASANDO LOGOTIPOS]");
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // try
  } // contextInitialized

  @Override
  public void contextDestroyed(ServletContextEvent event) {
    ServletContext context  = event.getServletContext();
    UsuariosEnLinea usuarios= (UsuariosEnLinea) context.getAttribute(Constantes.ATRIBUTO_USUARIOS_SITIO);
    LOG.info("[-CONTROL DE USUARIOS] ".concat("\n\n").concat(usuarios.toString()));
    LOG.info("[-SISTEMA]".concat((String) context.getAttribute("nombreAplicacion")));
    context.removeAttribute(Constantes.ATRIBUTO_USUARIOS_SITIO);
    context.removeAttribute(Constantes.ATRIBUTO_BLOQUEO_USUARIOS);
  } // contextDestroyed

  private void editors() {
    /* Registro de las clases no wrappers Map, List, Time, Date, Timestamp */
    PropertyEditorManager.registerEditor(java.util.Map.class, mx.org.kaana.libs.editor.MapEditor.class);
    PropertyEditorManager.registerEditor(java.util.List.class, mx.org.kaana.libs.editor.ListEditor.class);
    PropertyEditorManager.registerEditor(java.sql.Time.class, mx.org.kaana.libs.editor.TimeEditor.class);
    PropertyEditorManager.registerEditor(java.sql.Date.class, mx.org.kaana.libs.editor.DateEditor.class);
    PropertyEditorManager.registerEditor(java.sql.Timestamp.class, mx.org.kaana.libs.editor.TimestampEditor.class);
    LOG.info("Load messages system: ".concat(Messages.getInstance().getPropiedad("mensajes_cargados_memoria")));
  } // editors

  private void pathLoadFile(ServletContext context) {
    String systemPath= context.getRealPath("/");
    String find = systemPath.substring(systemPath.length() - 1);
    if (find.contains("/") || find.contains("\\"))
      systemPath = systemPath.concat(Constantes.RUTA_TEMPORALES);
    else
      systemPath = systemPath.concat(File.separator.concat(Constantes.RUTA_TEMPORALES));
    System.setProperty("java.io.tmpdir", systemPath);
  } // pathLoadFile

  private void setInitialAttribute(ServletContext context, String initParamName, String defaultValue) {
    String initialValue = context.getInitParameter(initParamName);
    if (initialValue != null)
      context.setAttribute(initParamName, initialValue);
    else
      context.setAttribute(initParamName, defaultValue);
  } // setInitialAttribute

  private void setJasperTempPath(ServletContext context) {
    System.setProperty("jasper.reports.compile.temp", context.getRealPath("/Temporal/"));
  } // setJasperTempPath
	
	private void toMoveResource(String target, String source, String type) {
    FileSearch fileSearch = new FileSearch();
    fileSearch.searchDirectory(new File(target), type.toLowerCase());
    if(fileSearch.getResult().size()> 0)
		  for (String matched: fileSearch.getResult()) {
				String name= matched.substring((matched.lastIndexOf("/")< 0? matched.lastIndexOf("\\"): matched.lastIndexOf("/"))+ 1);
   		  try {
					File result= new File(source.concat(name));
					if(result.exists())
						LOG.info("Resources logotipo exists: "+ result.getAbsoluteFile());
					else {
						LOG.info("Copy resource logotipo: "+ result.getAbsoluteFile());
						Archivo.toWriteFile(result, new FileInputStream(new File(matched)));
					} // if
				} // try
				catch (Exception e) {
					Error.mensaje(e);
				} // catch
      } // for 
	}
	
}
