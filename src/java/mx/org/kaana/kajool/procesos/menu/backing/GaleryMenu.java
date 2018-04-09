package mx.org.kaana.kajool.procesos.menu.backing;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date Aug 31, 2015
 * @time 10:14:59 AM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
import com.coolservlets.beans.menu.MenuModel;
import java.io.File;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.PhaseId;
import javax.servlet.http.HttpSession;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.libs.pagina.UISelect;
import mx.org.kaana.libs.pagina.UISelectItem;
import mx.org.kaana.libs.pagina.UtilAplicacion;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.db.dto.TrJanalBuzonDto;
import mx.org.kaana.kajool.enums.EMenus;
import mx.org.kaana.kajool.enums.EPropiedadesServidor;
import mx.org.kaana.kajool.procesos.acceso.menu.FactoryMenu;
import mx.org.kaana.kajool.procesos.acceso.menu.reglas.IBaseMenu;
import mx.org.kaana.kajool.procesos.beans.InformacionSistema;
import mx.org.kaana.kajool.procesos.beans.Jobs;
import mx.org.kaana.kajool.procesos.menu.reglas.Transaccion;
import mx.org.kaana.kajool.seguridad.quartz.Especial;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.primefaces.context.RequestContext;
import org.primefaces.model.chart.MeterGaugeChartModel;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.matchers.GroupMatcher;

@ManagedBean(name = "kajoolMenuGaleryMenu")
@ViewScoped
public class GaleryMenu implements Serializable {

  private static final long serialVersionUID= -1709303072566861688L;
  private static final Log LOG              = LogFactory.getLog(GaleryMenu.class);
  private static final String MB            = " Mb";
  private static final String GB            = " Gb";
  private static final String TOTAL         = "Total: ";
  private static final String UTILIZADA     = "Utilizada: ";
  private static final String DISPONIBLE    = "Disponible: ";
  private static long MEGABYTE              = 1024L * 1024L;
  private static long GIGABYTE              = 1024L * 1024L * 1024L;
  
  private List<UISelectItem> modulos, problematicas;
  private TrJanalBuzonDto trJanalBuzonDto;
  private Map<String, Object> systemInfo;
  private MeterGaugeChartModel memory;
  private MeterGaugeChartModel disk;  
  private MenuModel sentinel;  
  private String encabezado;
  private String mmenu;  

  public Map<String, Object> getSystemInformation() {
    return systemInfo;
  }

  public MeterGaugeChartModel getMemory() {
    return memory;
  }

  public MeterGaugeChartModel getDisk() {
    return disk;
  }

  public String getMmenu() {
    return mmenu;
  }

  public String getEncabezado() {
    return encabezado;
  }

  public MenuModel getSentinel() {
    return sentinel;
  }

  public List<UISelectItem> getModulos() {
    return modulos;
  }

  public void setModulos(List<UISelectItem> modulos) {
    this.modulos = modulos;
  }

  public TrJanalBuzonDto getTrJanalBuzonDto() {
    return trJanalBuzonDto;
  }

  public void setTrJanalBuzonDto(TrJanalBuzonDto trJanalBuzonDto) {
    this.trJanalBuzonDto = trJanalBuzonDto;
  }

  public List<UISelectItem> getProblematicas() {
    return problematicas;
  }

  public void setProblematicas(List<UISelectItem> problematicas) {
    this.problematicas = problematicas;
  }

  @PostConstruct
  private void init() {
    try {
      this.mmenu     = (String) toMenu(EMenus.MMENU);
      this.encabezado= (String) toMenu(EMenus.ENCABEZADO);
      this.sentinel  = (MenuModel) toMenu(EMenus.SENTINEL);
      if(JsfBase.getFacesContext().getCurrentPhaseId().equals(PhaseId.RENDER_RESPONSE) || JsfBase.getFacesContext().getCurrentPhaseId().equals(PhaseId.RESTORE_VIEW))		
        doLoadInformacionSistema(false);
      this.systemInfo.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
      this.modulos= UISelect.build("TcJanalModulosDto", "comboModulos", this.systemInfo, "descripcion");
      this.problematicas= UISelect.build("TcJanalProblematicasDto", "row", this.systemInfo, "descripcion");
      this.systemInfo.remove(Constantes.SQL_CONDICION);
      this.trJanalBuzonDto= new TrJanalBuzonDto();
    } // try
    catch (Exception e) {
      JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
  } // init

  private Object toMenu(EMenus menu) {
    Object regresar        = null;
    FactoryMenu factoryMenu= null;
    IBaseMenu baseMenu     = null;
    HttpSession session    = null;
    try {
      session    = JsfBase.getSession();
      factoryMenu= new FactoryMenu();
      switch (menu) {
        case ENCABEZADO:
        case SENTINEL:
          if (session.getAttribute(menu.getVariableSesion()) == null) {
            LOG.info("Se esta cargando el menu  ".concat(menu.name()));
            baseMenu = factoryMenu.toMenu(menu);
          } // if 
          break;
        case MMENU:
          if (session.getAttribute(menu.getVariableSesion()) == null) {
            LOG.info("Se esta cargando el menu  ".concat(menu.name()));
            baseMenu = factoryMenu.toMenu(menu);
          } // if  
          break;
      }// switch
      if (baseMenu!= null) {
        regresar = baseMenu.toBuild();
        if (JsfBase.getAutentifica().getCredenciales().isMenuEncabezado()) {
        if (JsfBase.getAutentifica().getCredenciales().isMenuEncabezado()) 
          LOG.info("Se esta cargando el menu  ".concat(menu.name()));
          session.setAttribute(menu.getVariableSesion(), regresar);        
        } // if  
      } //if				
      else 
        regresar = session.getAttribute(menu.getVariableSesion());      
    }// try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessage("Error al procesar las opciones del menu !", ETipoMensaje.ERROR);
    }// catch
    return regresar;
  } // toMenu

  public void doLoadInformacionSistema(boolean valida) {
    try {
      this.systemInfo = new HashMap<>();
      this.systemInfo.put("javaVersion", System.getProperty("java.version"));
      this.systemInfo.put("osVersion", System.getProperty("os.name").concat(" ").concat(System.getProperty("os.arch")).concat(" ").concat(System.getProperty("os.version")));
      this.systemInfo.put("jsfVersion", com.sun.faces.util.CollectionsUtils.class.getPackage().getImplementationTitle().concat(" ").concat(com.sun.faces.util.CollectionsUtils.class.getPackage().getImplementationVersion()));
      this.systemInfo.put("hibernateVersion", Hibernate.class.getPackage().getImplementationVersion());
      this.systemInfo.put("primeVersion", org.primefaces.util.Constants.class.getPackage().getImplementationVersion());
      this.systemInfo.put("extensionsVersion", org.primefaces.extensions.util.Constants.class.getPackage().getImplementationVersion());
      this.systemInfo.put("browser", Cadena.letraCapital(JsfBase.getBrowser().name()));
      this.systemInfo.put("jasperReport", Cadena.letraCapital(net.sf.jasperreports.util.CastorUtil.class.getPackage().getImplementationVersion()));
      this.systemInfo.put("date", Fecha.formatear(Fecha.FECHA_HORA_CORTA, Especial.getInstance().getRegistro()));
      loadMemoryInfo();
      loadDiskInfo();
      if (valida) {
        loadDataBaseInfo();
        loadJobs();
        loadProperties();
      } // if
    } // try
    catch (Exception e) {
      JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch	
  } // doLoadInformacionSistema

  private void loadMemoryInfo() throws Exception {
    List<InformacionSistema> memoryInfo= null;
    Runtime runtime                    = null;
    List<Number> intervals             = null;
    Long memoriaTotal                  = null;
    Long memoriaDisponible             = null;
    Long interval                      = null;    
    try {
      memoryInfo = new ArrayList<>();
      intervals = new ArrayList<>();
      runtime = Runtime.getRuntime();
      memoriaTotal = byteToMegabyte(runtime.totalMemory());
      memoriaDisponible = byteToMegabyte(runtime.freeMemory());
      interval = ((memoriaTotal) / 3);
      memoryInfo.add(new InformacionSistema(Numero.formatear(Numero.MILES_SIN_DECIMALES, memoriaTotal), MB, TOTAL));
      memoryInfo.add(new InformacionSistema(Numero.formatear(Numero.MILES_SIN_DECIMALES, memoriaDisponible), MB, DISPONIBLE));
      memoryInfo.add(new InformacionSistema(Numero.formatear(Numero.MILES_SIN_DECIMALES, (memoriaTotal - memoriaDisponible)), MB, UTILIZADA));
      this.systemInfo.put("memoriaTotalDesc", memoriaTotal);
      this.systemInfo.put("memoryInfo", memoryInfo);
      this.systemInfo.put("interval", interval);
      this.systemInfo.put("memoryInfoUsed", memoriaTotal - memoriaDisponible);
      this.systemInfo.put("memoryInfoMax", memoriaTotal);
      this.memory = new MeterGaugeChartModel((memoriaTotal - memoriaDisponible), toIntevals(interval, memoriaTotal));
      this.memory.setMin(0D);
      this.memory.setMax((Long) systemInfo.get("memoriaTotalDesc"));
      this.memory.setLabelHeightAdjust(10);
      this.memory.setIntervalOuterRadius(null);
      this.memory.setGaugeLabel(MB);
      this.memory.setSeriesColors(null);
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    finally {
      Methods.clean(intervals);
    } // finally
  } // loadMemoryInfo

  private void loadDiskInfo() throws Exception {
    List<InformacionSistema> discoLocal= null;
    File disco                         = null;
    Long espacioTotal                  = 0L;
    Long espacioUtilizado              = 0L;
    Long espacioDisponible             = 0L;
    String path                        = null;    
    Long interval                      = null;
    try {
      discoLocal= new ArrayList<>();
      this.disk= new MeterGaugeChartModel();
      path= JsfBase.getApplication().getRealPath(File.separator);
      disco= new File(path);
      this.systemInfo.put("path", path);
      espacioDisponible= byteToGigabyte(disco.getFreeSpace());
      espacioUtilizado= byteToGigabyte(disco.getUsableSpace());
      espacioTotal= espacioDisponible + espacioUtilizado;
      discoLocal.add(new InformacionSistema(Numero.formatear(Numero.MILES_SIN_DECIMALES, espacioTotal), GB, TOTAL));
      discoLocal.add(new InformacionSistema(Numero.formatear(Numero.MILES_SIN_DECIMALES, espacioDisponible), GB, DISPONIBLE));
      discoLocal.add(new InformacionSistema(Numero.formatear(Numero.MILES_SIN_DECIMALES, espacioUtilizado), GB, UTILIZADA.substring(0, UTILIZADA.length() - 1).concat("o")));
      interval= espacioTotal / 3;
      this.systemInfo.put("diskInfo", discoLocal);
      this.systemInfo.put("diskInfoUsed", espacioUtilizado);
      this.systemInfo.put("diskInfoMax", espacioTotal);
      this.disk = new MeterGaugeChartModel(espacioUtilizado, toIntevals(interval, espacioTotal));
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

  private void loadDataBaseInfo() throws Exception {
    List<Entity> dbDescripcion= null;
    try {
      dbDescripcion = DaoFactory.getInstance().toEntitySet("VistaInformacionDb", "descripcion", Collections.EMPTY_MAP);
      if (dbDescripcion != null) 
        this.systemInfo.put("dataBaseInfo", dbDescripcion);      
    } // try
    catch (Exception e) {
      throw e;
    } // catch				
  } // loadDataBaseInfo

  private void loadJobs() throws SchedulerException, Exception {
    List<Jobs> jobs= null;
    try {
      if (Especial.getInstance().getScheduler() != null) {
        jobs = new ArrayList<>();
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

  private void loadProperties() throws Exception {
    List<InformacionSistema> properties= null;
    String servidor                    = null;
    UtilAplicacion util                = null;
    try {
      util= new UtilAplicacion();
      servidor= util.getServidor().toLowerCase();
      properties= new ArrayList<>();
      for (EPropiedadesServidor propiedad : EPropiedadesServidor.values()) {
        if (propiedad.isRequiereServidor()) 
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

  private List<Number> toIntevals(Long interval, Long memoriaTotal) {
    List<Number> regresar = new ArrayList<>();
    regresar.add(interval);
    regresar.add(interval * 2);
    regresar.add(memoriaTotal);
    return regresar;
  } // toIntevals

  private String getPropiedad(String propiedad, String servidor) throws Exception {
    String regresar= null;
    try {
      regresar = propiedad.concat(Constantes.SEPARADOR_PROPIEDADES).concat(servidor);
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    return regresar;
  } // getPropiedad

  private String getValorPropiedad(String propiedad, String servidor) throws Exception {
    String regresar = null;
    try {
      regresar = Configuracion.getInstance().getPropiedad(getPropiedad(propiedad, servidor));
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    return regresar;
  } // getValorPropiedad

  private long byteToMegabyte(Long bytes) {
    return bytes / MEGABYTE;
  } // byteToMegabyte

  private long byteToGigabyte(Long bytes) {
    return bytes / GIGABYTE;
  } // byteToGigabyte

  public void doGuardarBuzon(){
    Transaccion transaccion= null;
    try {
      this.trJanalBuzonDto.setIdUsuario(JsfBase.getIdUsuario());
      this.trJanalBuzonDto.setPagina(JsfBase.getAutentifica().getPaginaActual());
      this.trJanalBuzonDto.setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
      transaccion= new Transaccion(this.trJanalBuzonDto);
      transaccion.ejecutar(EAccion.AGREGAR);
      JsfBase.addMsgProperties("janal.accion.ok.agregar");
      trJanalBuzonDto= new TrJanalBuzonDto();
      RequestContext.getCurrentInstance().update("dialogoBuzonSugerencias");
    } // try
    catch (Exception e) {
      JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch		
  } // doGuardarBuzon

  @Override
  protected void finalize() throws Throwable {
    super.finalize(); //To change body of generated methods, choose Tools | Templates.
    Methods.clean(this.systemInfo);
  } // finalize
}
