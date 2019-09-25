package mx.org.kaana.kajool.seguridad.quartz;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 21-sep-2015
 *@time 20:38:23
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.TriggerBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.recurso.Configuracion;

public class Jobs {

	private static final Log LOG       = LogFactory.getLog(Jobs.class);
	public static final String XML_JOBS= "/mx/org/kaana/kajool/cfg/jobs/job-kajool.xml";
	public static final String JOB		 = "tarea";
	public static final String CLASS	 = "clase";		
	public static final String GENERICO= "*";	
	private Scheduler scheduler;
	
	public Jobs(Scheduler scheduler) {
		this.scheduler=scheduler;
	}
	
	public void toBuild() {
    DocumentBuilderFactory fabrica= null;
    DocumentBuilder builder       = null;
		String expression             = null;		
    try {
      fabrica= DocumentBuilderFactory.newInstance();
      builder= fabrica.newDocumentBuilder();
      LOG.debug("Procesando los modulos ".concat(XML_JOBS));
        Document files = builder.parse(this.getClass().getResourceAsStream(XML_JOBS));
        NodeList names = toJobs(files, JOB);
        for (int z = 0; z < names.getLength(); z++) {
          Element item = (Element) names.item(z);
					if (item.getAttribute("load")!= null && item.getAttribute("load").equals("true")) {
						NodeList clase = toJobs(item, CLASS);
						for (int x = 0; x < clase.getLength(); x++) {
							Element element = (Element) clase.item(x);
							expression= element.getAttribute("expresion");
							if(evaluaElement(element)){
								if (element.getAttribute("load")== null || (element.getAttribute("load").equals("true")) || (element.getAttribute("load").equals(""))) 
									load(element.getTextContent(), expression, item.getAttribute("id").concat(element.getAttribute("id")).concat("Cron"), item.getAttribute("id").concat(element.getAttribute("id")), false);
								else																				
									load(element.getTextContent(), expression, item.getAttribute("id").concat(element.getAttribute("id")).concat("Cron"), item.getAttribute("id").concat(element.getAttribute("id")), true);
							} // if
							else
								load( element.getTextContent(), expression, item.getAttribute("id").concat(element.getAttribute("id")).concat("Cron"), item.getAttribute("id").concat(element.getAttribute("id")), true);							
						} // for
					} // if
        } // for y
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
  } // sections

	private NodeList toJobs(Node node, String label) {
    NodeList regresar= null;
    try {
			if(node instanceof Document)
        regresar = ((Document)node).getElementsByTagName(label);
			if(node instanceof Element)
        regresar = ((Element)node).getElementsByTagName(label);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
    return regresar;
  }
	
	private void load(String jobClass, String expression,String trigger, String job, boolean sleep)  {
		JobDetail jobIntegracion      = null;
		CronTrigger triggerIntegracion= null;	
		try {			
			Class<? extends Job> clase = (Class<? extends Job>) Class.forName(jobClass);
			jobIntegracion=JobBuilder.newJob(clase).withIdentity(job, Constantes.NOMBRE_DE_APLICACION).build();
			triggerIntegracion=TriggerBuilder.newTrigger().withIdentity(trigger, Constantes.NOMBRE_DE_APLICACION).withSchedule(CronScheduleBuilder.cronSchedule(expression)).build();
			this.scheduler.scheduleJob(jobIntegracion, triggerIntegracion);
			if(sleep)
				this.scheduler.pauseJob(jobIntegracion.getKey());
		} // try
		catch(Exception e) {
			LOG.warn(e);
		} // catch
	}
	
	private boolean evaluaElement(Element element) throws Exception{
		boolean regresar = true;		
		Configuracion cfg= null;
		try {			
			cfg= Configuracion.getInstance();
			if(!cfg.isEtapaDesarrollo())
				regresar= Especial.getInstance().getPath().equals(getAtributo(element, cfg.getEtapaServidor().toLowerCase())) || GENERICO.equals(element.getAttribute(cfg.getEtapaServidor().toLowerCase()));							
		} // try
		catch (Exception e) {						
			throw e;
		} // catch		
		return regresar;
	}
	
	private String getAtributo(Element element, String esquema) throws Exception{
		String regresar= null;
		try {
			regresar= Cadena.reemplazarCaracter(element.getAttribute(esquema), '/', File.separatorChar);			
			LOG.info("Path xml job [".concat(regresar).concat("]"));
		} // try
		catch (Exception e) {						
			throw e;
		} // catch		
		return regresar;
	}
	
}
