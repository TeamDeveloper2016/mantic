package mx.org.kaana.kajool.procesos.utilerias.informacionsistema.backing;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 16/10/2016
 *@time 04:36:43 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.PhaseId;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UtilAplicacion;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EPropiedadesServidor;
import mx.org.kaana.kajool.procesos.beans.InformacionSistema;
import mx.org.kaana.kajool.procesos.beans.Jobs;
import mx.org.kaana.kajool.seguridad.quartz.Especial;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.primefaces.model.chart.MeterGaugeChartModel;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.matchers.GroupMatcher;

@ManagedBean(name="kajoolMantenimientoUtileriasSistemaFiltro")
@ViewScoped
public class Filtro extends IBaseAttribute implements Serializable {

	private static final Log LOG              = LogFactory.getLog(Filtro.class);	
  private static final long serialVersionUID= 5334376316925398222L;
  private static final String MB            = " Mb";
	private static final String GB            = " Gb";
	private static final String TOTAL         = "Total: ";
	private static final String UTILIZADA     = "Utilizada: ";
	private static final String DISPONIBLE    = "Disponible: ";
  private static long MEGABYTE= 1024L * 1024L;
  private static long GIGABYTE= 1024L * 1024L * 1024L;
	private Map<String, Object> systemInfo;
	private MeterGaugeChartModel memory;	
	private MeterGaugeChartModel disk;	  	

	public Map<String, Object> getSystemInformation() {
		return systemInfo;
	}

	public MeterGaugeChartModel getMemory() {
		return memory;
	}	

	public MeterGaugeChartModel getDisk() {
		return disk;
	}		

  @PostConstruct
  @Override
	protected void init() {
    try {			
      doLoad(true);
    } // try
    catch(Exception e) {
			JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
	} // init

  public void doLoad(boolean valida) {		
    try {
      this.systemInfo= new HashMap<>();		
				this.systemInfo.put("javaVersion"      , System.getProperty("java.version"));
				this.systemInfo.put("osVersion"        , System.getProperty("os.name").concat(" ").concat(System.getProperty("os.arch")).concat(" ").concat(System.getProperty("os.version")));
				this.systemInfo.put("jsfVersion"       , com.sun.faces.util.CollectionsUtils.class.getPackage().getImplementationTitle().concat(" ").concat(com.sun.faces.util.CollectionsUtils.class.getPackage().getImplementationVersion()));
				this.systemInfo.put("hibernateVersion" , Hibernate.class.getPackage().getImplementationVersion());
				this.systemInfo.put("primeVersion"     , org.primefaces.util.Constants.class.getPackage().getImplementationVersion());
				this.systemInfo.put("extensionsVersion", org.primefaces.extensions.util.Constants.class.getPackage().getImplementationVersion());		
				this.systemInfo.put("browser"          , Cadena.letraCapital(JsfBase.getBrowser().name()));		
				this.systemInfo.put("jasperReport"     , Cadena.letraCapital(net.sf.jasperreports.util.CastorUtil.class.getPackage().getImplementationVersion()));					
				this.systemInfo.put("date"             , Fecha.formatear(Fecha.FECHA_HORA_CORTA, Especial.getInstance().getRegistro()));
				this.systemInfo.put("quartz"           ,"2.2.2");
        loadDatosMemoria();
				if(valida){					
					loadDataBaseInfo();						
					loadJobs();
					loadProperties();
				} // if
    } // try
    catch(Exception e) {
			JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
  } // doLoad

  public void loadDatosMemoria() throws Exception{
    try {
      loadMemoryInfo();
      loadDiskInfo();
    } // try
    catch (Exception e) {
      throw e;
    } // catch
  } // loadDatosMemoria

  private long byteToMegabyte(Long bytes){
		return bytes / MEGABYTE;
	} // byteToMegabyte
	
	private long byteToGigabyte(Long bytes){
		return bytes / GIGABYTE;
	} // byteToGigabyte

  private void loadMemoryInfo() throws Exception{		
		Runtime runtime       = null;
		List<Number> intervals= null;				
		Long memoriaTotal     = null;		
		Long memoriaDisponible= null;
		Long interval         = null;
		List<InformacionSistema> memoryInfo= null;		
		try {			
			this.systemInfo= this.systemInfo== null ? new HashMap(): this.systemInfo;
      memoryInfo= new ArrayList<>();
			intervals = new ArrayList<>();
			runtime  = Runtime.getRuntime();
			memoriaTotal     = byteToMegabyte(runtime.totalMemory());			
			memoriaDisponible= byteToMegabyte(runtime.freeMemory());
			interval= ((memoriaTotal)/3);
			memoryInfo.add(new InformacionSistema(Numero.formatear(Numero.MILES_SIN_DECIMALES, memoriaTotal), MB, TOTAL));
			memoryInfo.add(new InformacionSistema(Numero.formatear(Numero.MILES_SIN_DECIMALES, memoriaDisponible), MB, DISPONIBLE));
			memoryInfo.add(new InformacionSistema(Numero.formatear(Numero.MILES_SIN_DECIMALES,(memoriaTotal-memoriaDisponible)), MB, UTILIZADA));			
			this.systemInfo.put("memoriaTotalDesc", memoriaTotal);									
			this.systemInfo.put("memoryInfo", memoryInfo);
			this.systemInfo.put("interval", interval);									
			this.systemInfo.put("memoryInfoUsed", memoriaTotal - memoriaDisponible);
			this.systemInfo.put("memoryInfoMax", memoriaTotal);			
			this.systemInfo.put("memoriaDisponible", memoriaDisponible);			
			this.memory= new MeterGaugeChartModel((memoriaTotal-memoriaDisponible), toIntevals(interval,memoriaTotal)); 	
			this.memory.setMin(0D);
			this.memory.setMax((Long)systemInfo.get("memoriaTotalDesc"));
			this.memory.setLabelHeightAdjust(10);
			this.memory.setIntervalOuterRadius(null);
			this.memory.setGaugeLabel(MB);
			this.memory.setSeriesColors(null);
		} // try
		catch (Exception e) {
			throw e;
		} // catch		
		finally{
			Methods.clean(intervals);
		} // finally
	} // loadMemoryInfo
	
	private void loadDiskInfo() throws Exception{
		File disco                         = null;
		Long espacioTotal                  = 0L;
		Long espacioUtilizado              = 0L;
		Long espacioDisponible             = 0L;
		String path                        = null;		
		List<InformacionSistema> discoLocal= null;		
		Long interval                      = null;
		try {						
			discoLocal= new ArrayList<>();
			this.disk = new MeterGaugeChartModel();
			path = Especial.getInstance().getPath()== null ? JsfBase.getRealPath(): Especial.getInstance().getPath();			
			this.systemInfo.put("path", path);
      disco= new File(path);					
			espacioDisponible= byteToGigabyte(disco.getFreeSpace());
			espacioUtilizado = byteToGigabyte(disco.getUsableSpace());
			espacioTotal     = espacioDisponible + espacioUtilizado;			
			discoLocal.add(new InformacionSistema(Numero.formatear(Numero.MILES_SIN_DECIMALES, espacioTotal), GB, TOTAL));
			discoLocal.add(new InformacionSistema(Numero.formatear(Numero.MILES_SIN_DECIMALES, espacioDisponible), GB, DISPONIBLE));			
			discoLocal.add(new InformacionSistema(Numero.formatear(Numero.MILES_SIN_DECIMALES, espacioUtilizado), GB, UTILIZADA.substring(0,UTILIZADA.length()-1).concat("o")));			
			interval= espacioTotal / 3;
			this.systemInfo.put("diskInfo", discoLocal);			
			this.systemInfo.put("diskInfoUsed", espacioUtilizado);
			this.systemInfo.put("diskInfoMax", espacioTotal);						
			this.systemInfo.put("espacioDisponible", espacioDisponible);						
			this.disk= new MeterGaugeChartModel(espacioUtilizado, toIntevals(interval,espacioTotal)); 	
			this.disk.setMin(0D);
			this.disk.setMax(espacioTotal);
			this.disk.setLabelHeightAdjust(10);
			this.disk.setIntervalOuterRadius(null);
			this.disk.setGaugeLabel(GB);
			this.disk.setSeriesColors(null);
		} // try
		catch (Exception e) {
			throw e;
		} // catch				
	} // loadDiskInfo

  private void loadDataBaseInfo() throws Exception{
		Entity dbDescripcion= null;
		try {
			dbDescripcion= (Entity) DaoFactory.getInstance().toEntity("VistaInformacionDb", "descripcion", Collections.EMPTY_MAP);
			if(dbDescripcion!= null){		
				this.systemInfo.put("dataBaseInfo", dbDescripcion.toString("descripcion"));					
			} // if
		} // try
		catch (Exception e) {
			throw e;
		} // catch				
	} // loadDataBaseInfo
	
	private void loadJobs() throws SchedulerException, Exception{
		List<Jobs> jobs= null;
		try {			
			if(Especial.getInstance().getScheduler()!= null){
				jobs= new ArrayList<>();
				for (String groupName : Especial.getInstance().getScheduler().getJobGroupNames()) {
					for (JobKey jobKey : Especial.getInstance().getScheduler().getJobKeys(GroupMatcher.jobGroupEquals(groupName))) { 										
						List<Trigger> triggers = (List<Trigger>) Especial.getInstance().getScheduler().getTriggersOfJob(jobKey);						
						jobs.add(new Jobs(jobKey.getName(), jobKey.getGroup(), triggers.get(0).getNextFireTime(), triggers.get(0).getPreviousFireTime()));												
					} // for
				} // for				
				this.systemInfo.put("mostrarJobs", true);
				this.systemInfo.put("jobs", jobs);
			} // if
			else
				this.systemInfo.put("mostrarJobs", false);
		} // try
		catch (Exception e) {						
			throw e;
		} // catch		
	} // loadJobs
	
	private void loadProperties() throws Exception{
		List<InformacionSistema> properties= null;							
		String servidor                    = null;
		UtilAplicacion util                = null;
		try {
			util    = new UtilAplicacion();			
			servidor= util.getServidor().toLowerCase();						
			properties= new ArrayList<>();		
			for(EPropiedadesServidor propiedad: EPropiedadesServidor.values()){
				if(propiedad.isRequiereServidor())
					properties.add(new InformacionSistema(getPropiedad(propiedad.getPropiedad(), servidor), getValorPropiedad(propiedad.getPropiedad(), servidor)));
				else
					properties.add(new InformacionSistema(propiedad.getPropiedad(), Configuracion.getInstance().getPropiedad(propiedad.getPropiedad())));									
			} // for
			properties.add(new InformacionSistema("session", String.valueOf(JsfBase.getSession().getMaxInactiveInterval()).concat(" sec.")));
			this.systemInfo.put("properties", properties);
		} // try
		catch (Exception e) {						
			throw e;
		} // catch		
	} // loadProperties
	
	private List<Number> toIntevals(Long interval,Long memoriaTotal) {
		List<Number> regresar = new ArrayList<>();
		regresar.add(interval);
		regresar.add(interval * 2);
		regresar.add(memoriaTotal);
		return regresar;
	} // toIntervals

  private String getPropiedad(String propiedad, String servidor) throws Exception{
		String regresar= null;		
		try {			
			regresar= propiedad.concat(Constantes.SEPARADOR_PROPIEDADES).concat(servidor);
		} // try
		catch (Exception e) {						
			throw e;
		} // catch		
		return regresar;
	} // getPropiedad
	
	private String getValorPropiedad(String propiedad, String servidor) throws Exception{
		String regresar= null;		
		try {			
			regresar= Configuracion.getInstance().getPropiedad(getPropiedad(propiedad, servidor));
		} // try
		catch (Exception e) {						
			throw e;
		} // catch		
		return regresar;
	} // getValorPropiedad

  @Override
	protected void finalize() throws Throwable {
		super.finalize(); //To change body of generated methods, choose Tools | Templates.
		Methods.clean(this.systemInfo);
	}	// finalize
}
