package mx.org.kaana.kajool.procesos.acceso.backing;

import java.awt.event.ActionEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.json.Decoder;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.kajool.procesos.acceso.beans.Car;
import mx.org.kaana.kajool.procesos.acceso.beans.Empleado;
import mx.org.kaana.kajool.procesos.acceso.reglas.CarService;
import mx.org.kaana.kajool.procesos.comun.Comun;
import mx.org.kaana.kajool.procesos.enums.EEstatus;
import mx.org.kaana.kajool.procesos.enums.EPerfiles;
import mx.org.kaana.kajool.procesos.utilerias.graficasperfiles.beans.Highcharts;
import mx.org.kaana.kajool.procesos.utilerias.graficasperfiles.beans.HighchartsPie;
import mx.org.kaana.kajool.procesos.utilerias.graficasperfiles.beans.JsonChart;
import mx.org.kaana.kajool.procesos.utilerias.graficasperfiles.beans.Title;
import mx.org.kaana.kajool.procesos.utilerias.graficasperfiles.reglas.BuildChart;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatLazyModel;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UISelectItem;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.DualListModel;
import org.primefaces.model.LazyScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;
import org.primefaces.model.TreeNode;
import org.primefaces.model.chart.LineChartModel;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 21/08/2015
 * @time 12:27:03 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
@ManagedBean(name = "kajoolBienvenida")
@ViewScoped
public class Bienvenida extends Comun implements Serializable {

  private static final long serialVersionUID = 5323749709626263801L;
  private static final Log LOG = LogFactory.getLog(Bienvenida.class);
  private DualListModel<String> cities;
  private LineChartModel lineModel;  
  private List<Car> cars;
  private DefaultScheduleModel eventModel;
  private ScheduleModel lazyEventModel;
  private ScheduleEvent event = new DefaultScheduleEvent();
  private TreeNode root1;
  private TreeNode root2;
  private TreeNode selectedNode;
  private TreeNode[] selectedNodes1;

  public List<Car> getCars() {
    return this.cars;
  }

  public LineChartModel getLineModel() {
    return this.lineModel;
  }  

  public DualListModel<String> getCities() {
    return cities;
  }

  public void setCities(DualListModel<String> cities) {
    this.cities = cities;
  }

  public List<String> getCountries() {
    return this.cities.getSource();
  }  
  
  @PostConstruct
  @Override
  protected void init() {
    Empleado empleado= null;
    try {      
      empleado= JsfBase.getAutentifica().getEmpleado();
      this.attrs.put("isAdmin", JsfBase.isAdminEncuestaOrAdmin());
      this.attrs.put("titulotabla", EPerfiles.fromOrdinal(empleado.getIdPerfil()).getTituloTabla());
      this.attrs.put("isTablaGeneral", EPerfiles.CAPTURISTA.getKey().equals(empleado.getIdPerfil()));
      this.attrs.put("pathConfiguracion", JsfBase.getApplication().getContextPath() + "/Paginas/Utilerias/InformacionSistema/filtro.jsf");
      this.attrs.put("pathCaptura", JsfBase.getApplication().getContextPath() + "/Paginas/Captura/filtro.jsf");      
      this.attrs.put("pathMensajes", JsfBase.getApplication().getContextPath() +"/Paginas/Mantenimiento/Mensajes/Notificacion/filtro.jsf");            
      this.attrs.put("vigenciaInicial", new java.sql.Date(Calendar.getInstance().getTimeInMillis()));
      this.attrs.put("vigenciaFin", new java.sql.Date(Calendar.getInstance().getTimeInMillis()));
      loadPieModel();
      if(JsfBase.isAdminEncuestaOrAdmin())
        loadLineModelNacional();
      loadLineModel();
      loadEntidades();
      doLoad();
      loadMeses();
      doLoadContadoresMeses();
      loadContadoresGenerales();
      toMensajesNoLeidos();
      this.cars = (new CarService()).createCars(50);            
      List<String> citiesSource = new ArrayList<>();
      List<String> citiesTarget = new ArrayList<>();
      citiesSource.add("San Francisco");
      citiesSource.add("London");
      citiesSource.add("Paris");
      citiesSource.add("Istanbul");
      citiesSource.add("Berlin");
      citiesSource.add("Barcelona");
      citiesSource.add("Rome");
      cities = new DualListModel<>(citiesSource, citiesTarget);
      eventModel = new DefaultScheduleModel();
      eventModel.addEvent(new DefaultScheduleEvent("Champions League Match", previousDay8Pm(), previousDay11Pm()));
      eventModel.addEvent(new DefaultScheduleEvent("Birthday Party", today1Pm(), today6Pm()));
      eventModel.addEvent(new DefaultScheduleEvent("Breakfast at Tiffanys", nextDay9Am(), nextDay11Am()));
      eventModel.addEvent(new DefaultScheduleEvent("Plant the new garden stuff", theDayAfter3Pm(), fourDaysLater3pm()));
      lazyEventModel = new LazyScheduleModel() {
        @Override
        public void loadEvents(Date start, Date end) {
          Date random = getRandomDate(start);
          addEvent(new DefaultScheduleEvent("Lazy Event 1", random, random));
          random = getRandomDate(start);
          addEvent(new DefaultScheduleEvent("Lazy Event 2", random, random));
        }
      };

      root1 = (new CarService()).createDocuments();
      root2 = (new CarService()).createCheckboxDocuments();
    } // try
    catch (Exception e) {
      JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch        
  }

  @Override
  public void doLoad() {
    EPerfiles perfil   = null;    
    Empleado empleado  = null;
    List<Columna>campos= null;
    try {
      campos= new ArrayList<>();
      if(!Boolean.valueOf(this.attrs.get("isTablaGeneral").toString()))
        campos.add(new Columna("porcentaje", EFormatoDinamicos.NUMERO_SIN_DECIMALES));
      empleado= JsfBase.getAutentifica().getEmpleado();
      perfil= EPerfiles.fromOrdinal(empleado.getIdPerfil());      
      this.attrs.put("idGrupo", empleado.getIdGrupo());
      this.attrs.put("idPerfilCapturista", EPerfiles.CAPTURISTA.getKey());
      this.attrs.put("idEntidad", JsfBase.isAdminEncuestaOrAdmin() ? this.attrs.get("entidad") : empleado.getIdEntidad());           
      this.attrs.put("idUsuario", JsfBase.getIdUsuario());
      this.attrs.put("folioCompleto", EEstatus.COMPLETO.getKey() + "," + EEstatus.PARCIAL.getKey() + "," + EEstatus.LIBERADO_CAMPO.getKey());
      this.lazyModel= new FormatLazyModel(perfil.getVista(), perfil.getIdVista(), this.attrs, campos);
      UIBackingUtilities.resetDataTable();
    } // try
    catch (Exception e) {
      JsfBase.addMessageError(e);
    } // catch
  } // doLoad
  
  private void loadLineModelNacional() throws Exception{
    Highcharts charts    = null;
    BuildChart buildChart= null;
    try {
      buildChart= new BuildChart(JsfBase.getAutentifica().getEmpleado().getIdPerfil(), Long.valueOf(this.attrs.get("idGrupo").toString()));
      charts    = buildChart.buildNacional();
      this.attrs.put("tituloGeneralNacional", charts.getTitle().getText());
      this.attrs.put("jsonGeneralNacional", Decoder.toJson(charts));
    } // try
    catch (Exception e) {      
      throw e;
    } // catch    
  } // loadLineModelNacional 
  
  private void loadLineModel() throws Exception{
    List<Highcharts> charts= null;
    List<JsonChart> jsons  = null;
    BuildChart buildChart  = null;
    try {
      jsons     = new ArrayList<>();
      buildChart= new BuildChart(JsfBase.getAutentifica().getEmpleado().getIdPerfil(), Long.valueOf(this.attrs.get("idGrupo").toString()));
      charts    = buildChart.build();
      for(Highcharts chart: charts) {
        JsonChart json= new JsonChart(Cadena.eliminaCaracter(chart.getTitle().getText(), ' ').toLowerCase(), chart.getTitle().getText(), "");
        chart.setTitle(new Title(""));
        json.setJson(Decoder.toJson(chart));
        jsons.add(json);
      } // if
      this.attrs.put("jsons", jsons);
    } // try
    catch (Exception e) {      
      throw e;
    } // catch    
  } // loadLineModel    

  private void loadPieModel() throws Exception {
    HighchartsPie chart  = null;
    JsonChart json       = null;
    BuildChart buildChart= null;
    try {
      buildChart= new BuildChart(JsfBase.getAutentifica().getEmpleado().getIdPerfil(), JsfBase.getAutentifica().getEmpleado().getIdGrupo());
      chart= buildChart.buildPie();
      json= new JsonChart("avanceNacional", "Avance " + (((boolean)this.attrs.get("isTablaGeneral")) ? "estatal" : "nacional") + " de captura", Decoder.toJson(chart));
      this.attrs.put("jsonNacional", json);   
      this.attrs.put("capturaPendiente", chart.getSeries()[0].getData()[1].getY());
    } // try
    catch (Exception e) {
      throw e;
    } // catch        
  } // loadPieModel
  
  private void loadMeses(){    
    List<UISelectItem> meses= null;
    try {
      meses= new ArrayList<>();
      for(String mes: Fecha.meses(12))
        meses.add(new UISelectItem(mes, mes));
      this.attrs.put("meses", meses);     
      this.attrs.put("mes", Fecha.meses(12)[Fecha.getMesActual()-1]);
    } // try
    catch (Exception e) {      
      throw e;
    } // catch    
  } // loadMeses

  public void doLoadContadoresMeses() throws Exception{
    EPerfiles perfil          = null;   
    Entity contadores         = null;
    String fecha              = null;
    String fechaFin           = null;
    java.sql.Date fechaDate   = null;
    java.sql.Date fechaDateFin= null;
    try {
      fechaDate   = (java.sql.Date) this.attrs.get("vigenciaInicial");
      fechaDateFin= (java.sql.Date) this.attrs.get("vigenciaFin");
      if(fechaDate.equals(fechaDateFin) || fechaDate.before(fechaDateFin)){
        fecha= Fecha.formatear(Fecha.FECHA_ESTANDAR, fechaDate);
        fechaFin= Fecha.formatear(Fecha.FECHA_ESTANDAR, fechaDateFin);      
        this.attrs.put("anio", fecha.substring(0, 4));
        this.attrs.put("mesSeleccion", fecha.substring(4, 6));
        this.attrs.put("dia", fecha.substring(6, 8));
        this.attrs.put("anioFin", fechaFin.substring(0, 4));
        this.attrs.put("mesSeleccionFin", fechaFin.substring(4, 6));
        this.attrs.put("diaFin", fechaFin.substring(6, 8));
        perfil= EPerfiles.fromOrdinal(JsfBase.getAutentifica().getEmpleado().getIdPerfil());      
        contadores= (Entity) DaoFactory.getInstance().toEntity("VistaContadoresBienvenidaMensualesDto", perfil.getIdVistaContadoresMes(), this.attrs);
        if(contadores!= null)
          this.attrs.put("contadoresMes", contadores);
      } // if
      else
        JsfBase.addMessage("Reporte por fecha", "Las fechas seleccionadas son inconsistentes. Favor de verificarlas", ETipoMensaje.ERROR);
    } // try
    catch (Exception e) {      
      throw e;
    } // catch    
  } // loadContadoresGenerales
  
  private Long toMesSeleccionado(){
    Long regresar = null;
    String mes    = null;
    String[] meses= null;
    try {
      mes= this.attrs.get("mes").toString();
      meses= Fecha.meses(12);
      for(int contador=0; contador<meses.length; contador++){
        if(mes.equals(meses[contador]))
          regresar= new Long(contador+1);
      } // for
    } // try
    catch (Exception e) {      
      throw e;
    } // catch    
    return regresar;
  }
  
  private void loadContadoresGenerales() throws Exception{
    EPerfiles perfil = null;   
    Entity contadores= null;
    try {
      perfil= EPerfiles.fromOrdinal(JsfBase.getAutentifica().getEmpleado().getIdPerfil());
      contadores= (Entity) DaoFactory.getInstance().toEntity("VistaContadoresBienvenidaDto", perfil.getIdVistaContadores(), this.attrs);
      if(contadores!= null)
        this.attrs.put("contadoresGenerales", contadores);
    } // try
    catch (Exception e) {      
      throw e;
    } // catch    
  } // loadContadoresGenerales
  
  private void toMensajesNoLeidos() throws Exception{
    Entity mensajesNoLeidos= null;
    try {
      mensajesNoLeidos= (Entity) DaoFactory.getInstance().toEntity("VistaTrJanalMensajesUsuariosDto", "contadorNoLeidos", this.attrs);
      this.attrs.put("mensajesNoLeidos", mensajesNoLeidos!= null ? mensajesNoLeidos.toString("cantidad") : "0");
    } // try
    catch (Exception e) {      
      throw e;
    } // catch    
  }
  
  public Date getRandomDate(Date base) {
    Calendar date = Calendar.getInstance();
    date.setTime(base);
    date.add(Calendar.DATE, ((int) (Math.random() * 30)) + 1);    //set random day of month
    return date.getTime();
  }

  public Date getInitialDate() {
    Calendar calendar = Calendar.getInstance();
    calendar.set(calendar.get(Calendar.YEAR), Calendar.FEBRUARY, calendar.get(Calendar.DATE), 0, 0, 0);
    return calendar.getTime();
  }

  public DefaultScheduleModel getEventModel() {
    return eventModel;
  }

  public ScheduleModel getLazyEventModel() {
    return lazyEventModel;
  }

  private Calendar today() {
    Calendar calendar = Calendar.getInstance();
    calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), 0, 0, 0);
    return calendar;
  }

  private Date previousDay8Pm() {
    Calendar t = (Calendar) today().clone();
    t.set(Calendar.AM_PM, Calendar.PM);
    t.set(Calendar.DATE, t.get(Calendar.DATE) - 1);
    t.set(Calendar.HOUR, 8);
    return t.getTime();
  }

  private Date previousDay11Pm() {
    Calendar t = (Calendar) today().clone();
    t.set(Calendar.AM_PM, Calendar.PM);
    t.set(Calendar.DATE, t.get(Calendar.DATE) - 1);
    t.set(Calendar.HOUR, 11);
    return t.getTime();
  }

  private Date today1Pm() {
    Calendar t = (Calendar) today().clone();
    t.set(Calendar.AM_PM, Calendar.PM);
    t.set(Calendar.HOUR, 1);
    return t.getTime();
  }

  private Date theDayAfter3Pm() {
    Calendar t = (Calendar) today().clone();
    t.set(Calendar.DATE, t.get(Calendar.DATE) + 2);
    t.set(Calendar.AM_PM, Calendar.PM);
    t.set(Calendar.HOUR, 3);
    return t.getTime();
  }

  private Date today6Pm() {
    Calendar t = (Calendar) today().clone();
    t.set(Calendar.AM_PM, Calendar.PM);
    t.set(Calendar.HOUR, 6);
    return t.getTime();
  }

  private Date nextDay9Am() {
    Calendar t = (Calendar) today().clone();
    t.set(Calendar.AM_PM, Calendar.AM);
    t.set(Calendar.DATE, t.get(Calendar.DATE) + 1);
    t.set(Calendar.HOUR, 9);
    return t.getTime();
  }

  private Date nextDay11Am() {
    Calendar t = (Calendar) today().clone();
    t.set(Calendar.AM_PM, Calendar.AM);
    t.set(Calendar.DATE, t.get(Calendar.DATE) + 1);
    t.set(Calendar.HOUR, 11);
    return t.getTime();
  }

  private Date fourDaysLater3pm() {
    Calendar t = (Calendar) today().clone();
    t.set(Calendar.AM_PM, Calendar.PM);
    t.set(Calendar.DATE, t.get(Calendar.DATE) + 4);
    t.set(Calendar.HOUR, 3);
    return t.getTime();
  }

  public ScheduleEvent getEvent() {
    return event;
  }

  public void setEvent(ScheduleEvent event) {
    this.event = event;
  }

  public void addEvent(ActionEvent actionEvent) {
    if (event.getId() == null) {
      eventModel.addEvent(event);
    } else {
      eventModel.updateEvent(event);
    }

    event = new DefaultScheduleEvent();
  }

  public void onEventSelect(SelectEvent selectEvent) {
    event = (ScheduleEvent) selectEvent.getObject();
  }

  public void onDateSelect(SelectEvent selectEvent) {
    event = new DefaultScheduleEvent("", (Date) selectEvent.getObject(), (Date) selectEvent.getObject());
  }

  public void onEventMove(ScheduleEntryMoveEvent event) {
    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event moved", "Day delta:" + event.getDayDelta() + ", Minute delta:" + event.getMinuteDelta());
    addMessage(message);
  }

  public void onEventResize(ScheduleEntryResizeEvent event) {
    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event resized", "Day delta:" + event.getDayDelta() + ", Minute delta:" + event.getMinuteDelta());
    addMessage(message);
  }

  private void addMessage(FacesMessage message) {
    FacesContext.getCurrentInstance().addMessage(null, message);
  }

  public TreeNode getRoot1() {
    return root1;
  }

  public TreeNode getRoot2() {
    return root2;
  }

  public TreeNode getSelectedNode() {
    return selectedNode;
  }

  public void setSelectedNode(TreeNode selectedNode) {
    this.selectedNode = selectedNode;
  }

  public TreeNode[] getSelectedNodes1() {
    return selectedNodes1;
  }

  public void setSelectedNodes1(TreeNode[] selectedNodes1) {
    this.selectedNodes1 = selectedNodes1;
  }
  
}
