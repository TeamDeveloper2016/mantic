package mx.org.kaana.kajool.seguridad.servlets;

import com.sun.faces.application.ApplicationAssociate;
import com.sun.faces.mgbean.ManagedBeanInfo;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.convert.FacesConverter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.JsfUtilities;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.seguridad.beanmanager.BackingFinder;
import mx.org.kaana.xml.Modulos;
import mx.org.kaana.xml.Modulos.Paths;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.reflections.Reflections;
/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date Feb 18, 2014
 * @time 11:26:05 AM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public class LoaderManagedBean extends HttpServlet {

	private static final long serialVersionUID  =-8242919895555312331L;
	private static final Log LOG                =LogFactory.getLog(LoaderManagedBean.class);
	private static final String CONVERTIDORES   ="mx.org.kaana.libs.pagina.convertidores";

	/**
	 * Encargado del registro de todas las clases de la aplicación  que se encuentran fuera del contexto del web application y que utilizan @Annotation
	 * @throws ServletException
	 */
	
	@Override
	public void init() throws ServletException {
		ApplicationAssociate application= null;
		Modulos modulos                 = null;
		List<String> projects           = null;
		try {
			application= ApplicationAssociate.getInstance(JsfUtilities.getExternalContext());
			projects   = new ArrayList<>();
			modulos    = new Modulos(Configuracion.getInstance().toFileModule());
			modulos.load(projects, "", Paths.MANAGED_BEANS);
			LOG.info("Iniciando el proceso de registro de Managed Beans");
			for (String project : projects) {
				if (!project.equals("janal"))
					register(application, BackingFinder.getInstance().findManagedBeans(project.toLowerCase()));					
			}// for
			loadExtras(application);
			loadConvertidores();
			load(application);
			LOG.info("Termino el proceso de registro de Managed Beans");
		} // try
		catch (Exception e) {
			Error.mensaje(e);
		} // catch		
		finally {
			Methods.clean(projects);
		} // finally
	} // init
	
	/**
	 * Realiza el registro de todos los Managed Beans que se encuentran fuera del contexto del web application.
	 * @param application Objeto que permite realizar el registro de Managed Beans en la aplicación.
	 * @param managedBeans Collección de clases encontradas fuera del contexto del  con anotacion @ManegdBean.
	 * @throws Exception
	 */
	
	private void register(ApplicationAssociate application, Set<Class<?>> managedBeans) throws Exception {
		String scope                                    = null;
		ManagedBeanInfo managedBean                     = null;
		ManagedBeanInfo.ManagedProperty managedProperty = null;
		List<ManagedBeanInfo.ManagedProperty> properties= null;
		String nameManage                               = "";
		try {	
			for (Class<?> clase : managedBeans) {				
				scope= BackingFinder.getInstance().findScope(clase);
				nameManage= clase.getAnnotation(ManagedBean.class).name();
        properties= new ArrayList<>();
        for (Field atributo : clase.getDeclaredFields()) {
          if (atributo.isAnnotationPresent(javax.faces.bean.ManagedProperty.class)) {
            for (Annotation annotation:  atributo.getDeclaredAnnotations()) {
              if (annotation instanceof javax.faces.bean.ManagedProperty) {
                managedProperty= new ManagedBeanInfo.ManagedProperty(atributo.getName(), atributo.getType().getName(),((javax.faces.bean.ManagedProperty)annotation).value(), null, null);					
                properties.add(managedProperty);
              } // if
            } // for
          } // if
        } // for
        managedBean= new ManagedBeanInfo(nameManage, clase.getName(), scope, null, null, properties.isEmpty()?null:properties, null);
				if (application.getBeanManager().getRegisteredBeans().get(nameManage)==null) {
					LOG.info("Registrando managed bean clase["+clase.getName()+"] scope["+scope+"]");
				  application.getBeanManager().register(managedBean);
				}	// if
				else
				  LOG.info("Existe  managed bean registrado clase["+clase.getName()+"] scope["+scope+"]");				
			} // for
		} // try
		catch (Exception e) {
			throw e;
		} // catch		
	} // register

	/**
	 * Realiza el registro de la clase Util para la aplicación.
	 * @param application Objeto que permite realizar el registro de Managed Beans en la aplicación.
	 * @throws Exception
	 */
	
	private void loadExtras(ApplicationAssociate application) throws Exception {
		ManagedBeanInfo managedBean= null;
    Class<?> clase             = null;
		try {
      try {
         clase= Class.forName("mx.org.kaana.libs.pagina.UtilAplicacion");
      } // try
      catch (Exception e) {
        LOG.info("La clase util no ha sido cargada");
      } // catch
      if (clase!= null) {
			  managedBean= new ManagedBeanInfo("kajoolUtilAplicacion", "mx.org.kaana.libs.pagina.UtilAplicacion", BackingFinder.getInstance().findScope(clase), null, null, null, null);
			  application.getBeanManager().register(managedBean);
      } // if
		} // try
		catch (Exception e) {
			throw e;
		} // catch
	} // loadExtras

	/**
	 * Realiza el registro de los convertidores utilizados en la aplicación.
	 * @throws Exception
	 */
	
	private void loadConvertidores() throws Exception {
		LOG.info("Iniciando el proceso de registro de convertidores");
		Application application    = JsfUtilities.getFacesContext().getApplication();
		Set<Class<?>> convertidores= null;
		Reflections reflections    = new Reflections(CONVERTIDORES);
		convertidores= reflections.getTypesAnnotatedWith(FacesConverter.class);
		for (Class<?> clase : convertidores) {
			application.addConverter(clase.getAnnotation(FacesConverter.class).value(), clase.getName());
			LOG.info("Registrando convertidor clase[".concat(clase.getName()).concat("] id [".concat(clase.getAnnotation(FacesConverter.class).value()).concat("]")));
		}// for	
	} // loadConvertidores

	/**
	 * Reliza el registro de Managed Beans adicionales.
	 * @param application Objeto que permite realizar el registro de Managed Beans en la aplicación.
	 * @throws Exception
	 */
	
	private void load(ApplicationAssociate application) throws Exception {
		Set<Class<?>> managedBeans= null;
		final String[] extras     = new String[]{"mx.org.kaana.kajool.movil"   ,"mx.org.kaana.kajool.plantillas"   , "mx.org.kaana.kajool.template",
			                                       "mx.org.kaana.kajool.acceso"  ,"mx.org.kaana.kajool.mantenimiento","mx.org.kaana.kajool.grupostrabajo",
																			       "mx.org.kaana.kajool.tableros","mx.org.kaana.kajool.catalogos"    ,"mx.org.kaana.kajool.importacion",
																			       "mx.org.kaana.kajool.captura" ,"mx.org.kaana.kajool.indicadores"  ,"mx.org.kaana.kajool.contenedor",
																			       "mx.org.kaana.kajool.dialogos"};
		Reflections reflections= null;
		try {
			LOG.info("Iniciando el proceso de registro de Managed Beans .jar");
			for (String extra : extras) {
				reflections=new Reflections(extra);
				managedBeans=reflections.getTypesAnnotatedWith(ManagedBean.class);
				register(application, managedBeans);
			} // for			
		} // try
		catch (Exception e) {
			throw e;
		} // catch		
	} // load
}
