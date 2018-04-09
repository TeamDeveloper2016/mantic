package mx.org.kaana.kajool.seguridad.quartz;



import java.io.File;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.servlet.ServletContextEvent;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.beans.TareaServidor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date Sep 24, 2013
 * @time 10:11:17 AM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public final class Especial implements Serializable {
	
	private static final long serialVersionUID= 4320495081656905398L;
	private static final Log LOG              = LogFactory.getLog(Especial.class);	
	private static Object mutex;
  private static Especial instance;
	private Scheduler scheduler;	
	private List<TareaServidor> tareaServidor;
	private ServletContextEvent servletContextEvent;
	private String path;
	private Timestamp registro;	
	
	private Especial() {						
	}
	
	static {
    mutex=new Object();
  }

  public static Especial getInstance() {
    if (instance == null) {
      synchronized (mutex) {
        if (instance == null) {
          instance=new Especial();					
        }
      } // synchronized
    } // if
    return instance;
  }
	
	private void setScheduler(Scheduler scheduler) {
		this.scheduler=scheduler;
	}

	public Scheduler getScheduler() {
		return scheduler;
	}

	public List<TareaServidor> getTareaServidor() {
		return tareaServidor;
	}

	public ServletContextEvent getServletContextEvent() {
		return servletContextEvent;
	}	

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path= path;
	}

	public Timestamp getRegistro() {
		return registro;
	}		
/*			
  private boolean asignaServidor() {				
	  List<TareaServidor> servidores= null;	
		boolean regresar              = false;
		try {					
			servidores= (List<TareaServidor>)DaoFactory.getInstance().toEntitySet(TareaServidor.class,"TcTareasServidoresDto","servidores",new HashMap<String,String>());			
			for (TareaServidor tarea :servidores){
	      regresar= this.path.equals(Cadena.reemplazarCaracter(tarea.getPath(), '/',File.separatorChar));
			  if (regresar) 					
			 	 this.tareaServidor.add(tarea);			  			  					
			} // for					
			regresar= !this.tareaServidor.isEmpty();
		} // try
		catch (Exception e) {
		  Error.mensaje(e);
		} // catch
		finally {		
			 Methods.clean(servidores);			
		} // finally
		return regresar;
  }	
*/	
	private boolean asignaLocalServidor() {
		 TareaServidor tarea= null;	
		 boolean regresar   = true;
		try {
			tarea = new TareaServidor(this.path,"0 2/5 * * * ?");
			tareaServidor.add(tarea);
		} // try
		catch (Exception e) {
			regresar = false;
			Error.mensaje(e);
		} // cath
		return regresar;
	}
	
	public void refreshPath(ServletContextEvent servletContextEvent) {				
	  this.registro = new Timestamp(Calendar.getInstance().getTimeInMillis());
		this.path= servletContextEvent.getServletContext().getRealPath(File.separator);		
		this.path=Cadena.reemplazarCaracter(this.path,'/',File.separatorChar);		
		this.path= Cadena.reemplazarCaracter(this.path,'\\',File.separatorChar);	
	}
	
	public boolean validate(ServletContextEvent servletContextEvent) {
		boolean regresar= false;		
		String realPath = null;
		int pos         = 0;
		this.tareaServidor        = null;
		try {
			this.tareaServidor= new ArrayList<>();
			LOG.info("Iniciando validación de Quartz");
			realPath=servletContextEvent.getServletContext().getRealPath(File.separator);						
			LOG.info("Path recuperado [".concat(realPath).concat("]"));
			pos = realPath.lastIndexOf(Constantes.NOMBRE_DE_APLICACION.toUpperCase());			
			this.registro = new Timestamp(Calendar.getInstance().getTimeInMillis());
			if (pos>-1) {				
				this.path= realPath.substring(0, pos+Constantes.NOMBRE_DE_APLICACION.length()).concat(File.separator);
				this.path= Cadena.reemplazarCaracter(this.path,'/',File.separatorChar);
				this.path= Cadena.reemplazarCaracter(this.path,'\\',File.separatorChar);	
				LOG.info("Path server [".concat(this.path).concat("]"));
				//regresar= Configuracion.getInstance().getPropiedad("sistema.quartz.tareas").equals("true")?asignaServidor():asignaLocalServidor();									
				regresar= asignaLocalServidor();									
				LOG.info("MultiProcesamiento  server [".concat(String.valueOf(regresar).concat("]")));
			}// if		
			else {
				refreshPath(servletContextEvent);	
			}	
			this.servletContextEvent = servletContextEvent;
			
		} // try
		catch (Exception e) {			
			Error.mensaje(e);
		} // catch		
		return regresar;
	}

	private String toParameter (ServletContextEvent servletContextEvent , String parameter) {
   return servletContextEvent.getServletContext().getInitParameter(parameter);
  }

  private void addParameters (ServletContextEvent cfg) throws SchedulerException {
    this.scheduler.getContext().put("pathContext", cfg.getServletContext().getRealPath("/"));
    this.scheduler.getContext().put("application", cfg.getServletContext());
    this.scheduler.getContext().put("servidor", toParameter(cfg,"servidor"));
  }	
	
	public void init() {
		try {						
			SchedulerFactory sf=new StdSchedulerFactory(toParameter(servletContextEvent,"quartz-config-file"));
			setScheduler(sf.getScheduler());						
			getScheduler().start();
			addParameters(this.servletContextEvent);			
			LOG.info("Ejecutando quartz");
			load();
		} // try
		catch (Exception e) {
			Error.mensaje(e);
		} // catch
	}

	private void load() throws Exception {		
		try {	
			Jobs jobs= new Jobs(this.scheduler);
			jobs.toBuild();
		} // 0try
		catch (Exception e) {
		  throw e;
		} // catch
	}
}
