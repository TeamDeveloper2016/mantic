package mx.org.kaana.kajool.procesos.acceso.backing;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.ESucursales;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.json.Decoder;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.kajool.procesos.acceso.beans.Persona;
import mx.org.kaana.kajool.procesos.comun.Comun;
import mx.org.kaana.kajool.procesos.enums.EPerfiles;
import mx.org.kaana.kajool.procesos.utilerias.graficasperfiles.beans.Highcharts;
import mx.org.kaana.kajool.procesos.utilerias.graficasperfiles.beans.HighchartsPie;
import mx.org.kaana.kajool.procesos.utilerias.graficasperfiles.beans.JsonChart;
import mx.org.kaana.kajool.procesos.utilerias.graficasperfiles.beans.Title;
import mx.org.kaana.kajool.procesos.utilerias.graficasperfiles.reglas.BuildChart;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.pagina.UISelectItem;
import mx.org.kaana.mantic.enums.EPeriodosTableros;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 21/08/2015
 * @time 12:27:03 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */

@Named(value = "kajoolTablero")
@ViewScoped
public class Tablero extends Comun implements Serializable {

  private static final long serialVersionUID = 5323749709626263801L;
  private static final Log LOG = LogFactory.getLog(Tablero.class);
  private List<UISelectItem> sucursales;
	private List<Entity> articulos;

  public List<UISelectItem> getSucursales() {
    return sucursales;
  }  

	public List<Entity> getArticulos() {
		return articulos;
	}	
	
  @PostConstruct
  @Override
  protected void init() {
    Persona empleado = null;
    try {
      empleado = JsfBase.getAutentifica().getPersona();
      this.attrs.put("isAdmin", JsfBase.isAdminEncuestaOrAdmin());
      this.attrs.put("titulotabla", EPerfiles.fromOrdinal(empleado.getIdPerfil()).getTituloTabla());
      this.attrs.put("isTablaGeneral", EPerfiles.CAPTURISTA.getKey().equals(empleado.getIdPerfil()));
      this.attrs.put("pathConfiguracion", JsfBase.getApplication().getContextPath() + "/Paginas/Utilerias/InformacionSistema/filtro.jsf");
      this.attrs.put("pathCaptura", JsfBase.getApplication().getContextPath() + "/Paginas/Captura/filtro.jsf");
      this.attrs.put("pathMensajes", JsfBase.getApplication().getContextPath() + "/Paginas/Mantenimiento/Mensajes/Notificacion/filtro.jsf");
      this.attrs.put("vigenciaInicial", new java.sql.Date(Calendar.getInstance().getTimeInMillis()));
      this.attrs.put("vigenciaFin", new java.sql.Date(Calendar.getInstance().getTimeInMillis()));
			this.attrs.put("calendario", Calendar.getInstance());
			this.attrs.put("fechaSeleccionada", Fecha.formatear(Fecha.FECHA_MINIMA, cambiarDia(Fecha.formatear(Fecha.FECHA_MINIMA, (Calendar)this.attrs.get("calendario")), Boolean.FALSE)));
			this.attrs.put("idPeriodo", 1L);
			this.attrs.put("nombrePeriodo", EPeriodosTableros.fromIdPeriodo((Long)this.attrs.get("idPeriodo")).getNombre());
			this.attrs.put("ultimoDia", false);
			this.attrs.put("ultimoPeriodo", false);
			this.attrs.put("primerPeriodo", true);
			this.attrs.put("condicionGeneral", Constantes.SQL_VERDADERO);
			initPeriodos();
			loadAllCharts();       
      doLoadSucursales();
      doLoad();
      loadMeses();
      //doLoadContadoresMeses();
      loadContadoresGenerales();
      toMensajesNoLeidos();            			
    } // try
    catch (Exception e) {
      JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch        
  } // init

	private void initPeriodos(){
		try {
			this.attrs.put("renderedDia", false);
			this.attrs.put("renderedSemana", true);
			this.attrs.put("renderedQuincena", true);
			this.attrs.put("renderedMes", true);
			this.attrs.put("renderedTrimestre", true);
			this.attrs.put("renderedSemestre", true);
			this.attrs.put("renderedAnio", true);			
		} // try
		catch (Exception e) {		
			throw e;
		} // catch		
	} // initPeriodos
	
  @Override
  public void doLoad() {
    try {
      this.attrs.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
      this.attrs.put("idUsuario", JsfBase.getIdUsuario());
      this.attrs.put("codigo", "");
      this.attrs.put("expresion", "");
      this.attrs.put("sortOrder", "order by tc_mantic_articulos.stock asc");      
			this.articulos= DaoFactory.getInstance().toEntitySet("VistaArticulosDto", "row", this.attrs, 50L);
    } // try
    catch (Exception e) {
      JsfBase.addMessageError(e);
    } // catch
  } // doLoad

	private void loadAllCharts() throws Exception{
		try {
			toCreateCondicion();
			doLoadChartUtilidadSucursal();
			doLoadChartUtilidadCaja();
			doLoadChartCuentasCobrar();
			doLoadChartCuentasPagar();
			doLoadChartsGeneral();
			doLoadChartsArticulos();
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
	} // loadAllCharts
	
	public void doLoadChartUtilidadSucursal() throws Exception{
		Highcharts utilidadPorSucursal= null;
		BuildChart buildChart         = null;
		try {
			buildChart = new BuildChart(this.attrs.get("condicionGeneral").toString());
			utilidadPorSucursal = buildChart.buildUtilidadSucursal();
      this.attrs.put("utilidadPorSucursal", Decoder.toJson(utilidadPorSucursal));
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
	} // loadChartUtilidadSucursal
	
	public void doLoadChartUtilidadCaja() throws Exception{
		BuildChart buildChart     = null;
		Highcharts utilidadPorCaja= null;		
		try {
			buildChart = new BuildChart(this.attrs.get("condicionGeneral").toString());
			utilidadPorCaja = buildChart.buildUtilidadCaja();            
      this.attrs.put("jsonUtilidadPorCaja", Decoder.toJson(utilidadPorCaja));
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
	} // loadChartUtilidadSucursal
	
	public void doLoadChartCuentasCobrar() throws Exception{
		BuildChart buildChart      = null;
		Highcharts cuentasPorCobrar= null;
		try {
			buildChart = new BuildChart(this.attrs.get("condicionGeneral").toString());       
			cuentasPorCobrar = buildChart.buildCuentasCobrar();
      this.attrs.put("jsonCobro", Decoder.toJson(cuentasPorCobrar));
		} // try
		catch (Exception e) {
			throw e;
		} // catch		
	} // loadChartCuentasCobrar
	
  private void doLoadChartCuentasPagar() throws Exception {            
    BuildChart buildChart     = null;
		Highcharts cuentasPorPagar= null;
    try {
      buildChart = new BuildChart(this.attrs.get("condicionGeneral").toString());           
      cuentasPorPagar = buildChart.buildCuentasPagar();      
      this.attrs.put("jsonPago", Decoder.toJson(cuentasPorPagar));
    } // try
    catch (Exception e) {
      throw e;
    } // catch    
  } // loadLineModelNacional 

  private void doLoadChartsGeneral() throws Exception {
    List<Highcharts> charts= null;
    List<JsonChart> jsons  = null;
    BuildChart buildChart  = null;
    try {
      jsons = new ArrayList<>();
      buildChart = new BuildChart(this.attrs.get("condicionGeneral").toString());
      charts= buildChart.build();
      for (Highcharts chart : charts) {
        JsonChart json = new JsonChart(Cadena.eliminaCaracter(chart.getTitle().getText(), ' ').toLowerCase(), chart.getTitle().getText(), "");
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

  private void doLoadChartsArticulos() throws Exception {
    HighchartsPie artMasUtilidad= null;
    HighchartsPie artMasVentas  = null;
    JsonChart jsonArtMasUtilidad= null;
    JsonChart jsonArtMasVentas  = null;
    BuildChart buildChart       = null;
    try {
      buildChart = new BuildChart(this.attrs.get("condicionGeneral").toString());
      artMasUtilidad= buildChart.buildArticulosMasUtilidad();
      artMasVentas  = buildChart.buildArticulosMasVendidos();
      jsonArtMasUtilidad = new JsonChart("avanceNacional", "Articulos con mas utilidad", Decoder.toJson(artMasUtilidad));
      jsonArtMasVentas = new JsonChart("avanceNacional", "Articulos con mas ventas", Decoder.toJson(artMasVentas));
      this.attrs.put("jsonUtilidad", jsonArtMasUtilidad);
      this.attrs.put("jsonVentas", jsonArtMasVentas);
    } // try
    catch (Exception e) {
      throw e;
    } // catch        
  } // loadPieModel

  private void loadMeses() {
    List<UISelectItem> meses = null;
    try {
      meses = new ArrayList<>();
      for (String mes : Fecha.meses(12)) 
        meses.add(new UISelectItem(mes, mes));      
      this.attrs.put("meses", meses);
      this.attrs.put("mes", Fecha.meses(12)[Fecha.getMesActual() - 1]);
    } // try
    catch (Exception e) {
      throw e;
    } // catch    
  } // loadMeses

  public void doLoadContadoresMeses() throws Exception {
    EPerfiles perfil = null;
    Entity contadores = null;
    String fecha = null;
    String fechaFin = null;
    java.sql.Date fechaDate = null;
    java.sql.Date fechaDateFin = null;
    try {
      fechaDate = (java.sql.Date) this.attrs.get("vigenciaInicial");
      fechaDateFin = (java.sql.Date) this.attrs.get("vigenciaFin");
      if (fechaDate.equals(fechaDateFin) || fechaDate.before(fechaDateFin)) {
        fecha = Fecha.formatear(Fecha.FECHA_ESTANDAR, fechaDate);
        fechaFin = Fecha.formatear(Fecha.FECHA_ESTANDAR, fechaDateFin);
        this.attrs.put("anio", fecha.substring(0, 4));
        this.attrs.put("mesSeleccion", fecha.substring(4, 6));
        this.attrs.put("dia", fecha.substring(6, 8));
        this.attrs.put("anioFin", fechaFin.substring(0, 4));
        this.attrs.put("mesSeleccionFin", fechaFin.substring(4, 6));
        this.attrs.put("diaFin", fechaFin.substring(6, 8));
        perfil = EPerfiles.fromOrdinal(JsfBase.getAutentifica().getPersona().getIdPerfil());
        contadores = (Entity) DaoFactory.getInstance().toEntity("VistaContadoresBienvenidaMensualesDto", perfil.getIdVistaContadoresMes(), this.attrs);
        if (contadores != null) 
          this.attrs.put("contadoresMes", contadores);
      } // if
      else 
        JsfBase.addMessage("Reporte por fecha", "Las fechas seleccionadas son inconsistentes. Favor de verificarlas", ETipoMensaje.ERROR);
    } // try
    catch (Exception e) {
      throw e;
    } // catch    
  } // loadContadoresGenerales 

  private void loadContadoresGenerales() throws Exception {
    EPerfiles perfil = null;
    Entity contadores = null;
    try {
      perfil = EPerfiles.fromOrdinal(JsfBase.getAutentifica().getPersona().getIdPerfil());
      contadores = (Entity) DaoFactory.getInstance().toEntity("VistaContadoresBienvenidaDto", perfil.getIdVistaContadores(), this.attrs);
      if (contadores != null) 
        this.attrs.put("contadoresGenerales", contadores);
    } // try
    catch (Exception e) {
      throw e;
    } // catch    
  } // loadContadoresGenerales

  private void toMensajesNoLeidos() throws Exception {
    Entity mensajesNoLeidos = null;
    try {
      mensajesNoLeidos = (Entity) DaoFactory.getInstance().toEntity("VistaTrJanalMensajesUsuariosDto", "contadorNoLeidos", this.attrs);
      this.attrs.put("mensajesNoLeidos", mensajesNoLeidos != null ? mensajesNoLeidos.toString("cantidad") : "0");
    } // try
    catch (Exception e) {
      throw e;
    } // catch    
  } // toMensajesNoLeidos

  public void doLoadSucursales() {
    try {
      this.sucursales= new ArrayList<>();
      for (ESucursales sucursal : ESucursales.values()) 
        this.sucursales.add(new UISelectItem(sucursal.getIdKey(), Cadena.reemplazarCaracter(sucursal.getSucursal(), '_', ' ')));
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
  } // doLoadSucursales
	
	public void doCambiarDia(boolean isAdelante) {
    Calendar seleccionada= null;
    Calendar actual      = null;
    try {
      this.attrs.put("fechaSeleccionada", Fecha.formatear(Fecha.FECHA_MINIMA, cambiarDia(this.attrs.get("fechaSeleccionada").toString(), isAdelante)));      
      if(isAdelante) {
        seleccionada= Calendar.getInstance();
        seleccionada.setTime(new SimpleDateFormat("dd/MM/yy").parse(this.attrs.get("fechaSeleccionada").toString()));
        actual= Calendar.getInstance();
        actual.setTime(new SimpleDateFormat("dd/MM/yy").parse(Fecha.getHoy()));
        this.attrs.put("ultimoDia", seleccionada.compareTo(actual)== 0);
      } // if
      else
        this.attrs.put("ultimoDia", false);			
			loadAllCharts();
    } // try
    catch(Exception e) {
			LOG.debug("Error en método doCambiarDia: ".concat(e.toString()));
      JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
  } // doCambiarDia
  
  private Calendar cambiarDia(String fecha, Boolean isAdelante) throws Exception {
    Calendar regresar= null;
    try {
      regresar= Calendar.getInstance();
      regresar.setTime(new SimpleDateFormat("dd/MM/yy").parse(fecha));
      if(isAdelante)         
        regresar.add(Calendar.DATE, 1);
      else         
        regresar.add(Calendar.DATE, -1);
    } // try
		catch (Exception e) {
			LOG.debug("Error en método cambiarDia: ".concat(e.toString()));
      throw e;
		} // catch
    return regresar;
  } // cambiarDia
	
	public void doCambiarPeriodo(boolean isAdelante) {    
		Long idPeriodo= -1L;
    try {    
			idPeriodo= (Long) this.attrs.get("idPeriodo");
      if(isAdelante){ 
				idPeriodo= idPeriodo + 1L;
        this.attrs.put("nombrePeriodo", EPeriodosTableros.fromIdPeriodo(idPeriodo).getNombre());      
				this.attrs.put("primerPeriodo", idPeriodo.equals(EPeriodosTableros.DIA.getIdTipoPeriodo()));
				this.attrs.put("ultimoPeriodo", idPeriodo.equals(EPeriodosTableros.ANIO.getIdTipoPeriodo()));
			} // if
			else{
				idPeriodo= idPeriodo - 1L;
        this.attrs.put("nombrePeriodo", EPeriodosTableros.fromIdPeriodo(idPeriodo).getNombre());
				this.attrs.put("primerPeriodo", idPeriodo.equals(EPeriodosTableros.DIA.getIdTipoPeriodo()));
				this.attrs.put("ultimoPeriodo", idPeriodo.equals(EPeriodosTableros.ANIO.getIdTipoPeriodo()));
			} // else
			refreshPeriodos(idPeriodo);
			this.attrs.put("idPeriodo", idPeriodo);						
			loadAllCharts();
    } // try
    catch(Exception e) {
			LOG.debug("Error en método doCambiarDia: ".concat(e.toString()));
      JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
  } // doCambiarDia
	
	private void refreshPeriodos(Long idPeriodo){		
    try {    	
			this.attrs.put("renderedDia", true);
			this.attrs.put("renderedSemana", true);
			this.attrs.put("renderedQuincena", true);
			this.attrs.put("renderedMes", true);
			this.attrs.put("renderedTrimestre", true);
			this.attrs.put("renderedSemestre", true);
			this.attrs.put("renderedAnio", true);			
			switch(EPeriodosTableros.fromIdPeriodo(idPeriodo)){
				case DIA:
					this.attrs.put("renderedDia", false);
					break;
				case SEMANA:
					this.attrs.put("renderedSemana", false);
					break;
				case QUINCENA:
					this.attrs.put("renderedQuincena", false);
					break;
				case MES:
					this.attrs.put("renderedMes", false);
					break;
				case TRIMESTRE:
					this.attrs.put("renderedTrimestre", false);
					break;
				case SEMESTRE:
					this.attrs.put("renderedSemestre", false);
					break;
				case ANIO:
					this.attrs.put("renderedAnio", false);			
					break;
			} // switch
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
	} // refreshPeriodos
	
	private void toCreateCondicion() throws Exception{
		Long idPeriodo= -1L;
		try {
			idPeriodo= (Long) this.attrs.get("idPeriodo");
			switch(EPeriodosTableros.fromIdPeriodo(idPeriodo)){
				case DIA:
					toCondicionDia();
					break;
				case SEMANA:
					toCondicionSemana();
					break;
				case QUINCENA:
					toCondicionQuincena();
					break;
				case MES:
					toCondicionMes();
					break;
				case TRIMESTRE:
					toCondicionTrimestre();
					break;
				case SEMESTRE:
					toCondicionSemestre();
					break;
				case ANIO:
					toCondicionAnio();
					break;
			} // switch
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
	} // toCreateCondicion
	
	private void toCondicionDia() throws Exception{
		Calendar seleccionada= null;
		try {      
			seleccionada= Calendar.getInstance();
			seleccionada.setTime(new SimpleDateFormat("dd/MM/yy").parse(this.attrs.get("fechaSeleccionada").toString()));			
			this.attrs.put("condicionGeneral", "date_format( registro , '%Y%m%d') = '".concat(Fecha.formatear(Fecha.FECHA_ESTANDAR, seleccionada.getTime())).concat("'"));      
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
	} // toCondicionDia
	
	private void toCondicionSemana() throws Exception{
		Calendar seleccionada= null;
		StringBuilder sb     = null;
		try {      
			seleccionada= Calendar.getInstance();
			sb= new StringBuilder("");
			seleccionada.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
			sb.append("date_format( registro , '%Y%m%d')>= '".concat(Fecha.formatear(Fecha.FECHA_ESTANDAR, seleccionada.getTime())).concat("'"));			
			seleccionada.add(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
			sb.append("and date_format( registro , '%Y%m%d')<= '".concat(Fecha.formatear(Fecha.FECHA_ESTANDAR, seleccionada.getTime())).concat("'"));			
			this.attrs.put("condicionGeneral", sb.toString());      			
		} // try
		catch (Exception e) {			
			throw e;
		} // catch	
	} // toCondicionSemana
	
	private void toCondicionQuincena() throws Exception{
		Calendar seleccionada= null;
		StringBuilder sb     = null;
		try {      
			seleccionada= Calendar.getInstance();
			sb= new StringBuilder("");
			if(seleccionada.get(Calendar.DAY_OF_MONTH)<= 15){
				seleccionada.set(Calendar.WEEK_OF_MONTH, 1);
				seleccionada.set(Calendar.DAY_OF_MONTH, 1);
				sb.append("date_format( registro , '%Y%m%d')>= '".concat(Fecha.formatear(Fecha.FECHA_ESTANDAR, seleccionada.getTime())).concat("'"));			
				seleccionada.add(Calendar.DAY_OF_MONTH, 14);
				sb.append("and date_format( registro , '%Y%m%d')<= '".concat(Fecha.formatear(Fecha.FECHA_ESTANDAR, seleccionada.getTime())).concat("'"));			
			} // if
			else{
				seleccionada.set(Calendar.DAY_OF_MONTH, 16);
				sb.append("date_format( registro , '%Y%m%d')>= '".concat(Fecha.formatear(Fecha.FECHA_ESTANDAR, seleccionada.getTime())).concat("'"));			
				seleccionada.set(Calendar.DAY_OF_MONTH, seleccionada.getActualMaximum(Calendar.DAY_OF_MONTH));
				sb.append("and date_format( registro , '%Y%m%d')<= '".concat(Fecha.formatear(Fecha.FECHA_ESTANDAR, seleccionada.getTime())).concat("'"));			
			} // else
			this.attrs.put("condicionGeneral", sb.toString());      			
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
	} // toCondicionQuincena
	
	private void toCondicionMes(){
		Calendar seleccionada= null;
		StringBuilder sb     = null;
		try {      
			seleccionada= Calendar.getInstance();
			sb= new StringBuilder("");						
			seleccionada.set(Calendar.DAY_OF_MONTH, 1);
			sb.append("date_format( registro , '%Y%m%d')>= '".concat(Fecha.formatear(Fecha.FECHA_ESTANDAR, seleccionada.getTime())).concat("'"));			
			seleccionada.add(Calendar.DAY_OF_MONTH, seleccionada.getMaximum(Calendar.DAY_OF_MONTH));
			sb.append("and date_format( registro , '%Y%m%d')<= '".concat(Fecha.formatear(Fecha.FECHA_ESTANDAR, seleccionada.getTime())).concat("'"));						
			this.attrs.put("condicionGeneral", sb.toString());      			
		} // try
		catch (Exception e) {			
			throw e;
		} // catch				
	} // toCondicionMes
	
	private void toCondicionTrimestre(){
		Calendar seleccionada= null;
		StringBuilder sb     = null;
		try {      
			seleccionada= Calendar.getInstance();
			sb= new StringBuilder("");
			if(seleccionada.get(Calendar.MONTH)<= 2){
				seleccionada.set(Calendar.MONTH, 0);
				seleccionada.set(Calendar.DAY_OF_MONTH, 1);
				sb.append("date_format( registro , '%Y%m%d')>= '".concat(Fecha.formatear(Fecha.FECHA_ESTANDAR, seleccionada.getTime())).concat("'"));			
				seleccionada.set(Calendar.MONTH, 2);
				seleccionada.set(Calendar.DAY_OF_MONTH, seleccionada.getMaximum(Calendar.DAY_OF_MONTH));
				sb.append("and date_format( registro , '%Y%m%d')<= '".concat(Fecha.formatear(Fecha.FECHA_ESTANDAR, seleccionada.getTime())).concat("'"));			
			} // if
			else if (seleccionada.get(Calendar.MONTH)<= 5){
				seleccionada.set(Calendar.MONTH, 3);
				seleccionada.set(Calendar.DAY_OF_MONTH, 1);
				sb.append("date_format( registro , '%Y%m%d')>= '".concat(Fecha.formatear(Fecha.FECHA_ESTANDAR, seleccionada.getTime())).concat("'"));			
				seleccionada.set(Calendar.MONTH, 5);
				seleccionada.set(Calendar.DAY_OF_MONTH, seleccionada.getMaximum(Calendar.DAY_OF_MONTH));
				sb.append("and date_format( registro , '%Y%m%d')<= '".concat(Fecha.formatear(Fecha.FECHA_ESTANDAR, seleccionada.getTime())).concat("'"));			
			} // else
			else if (seleccionada.get(Calendar.MONTH)<= 8){
				seleccionada.set(Calendar.MONTH, 6);
				seleccionada.set(Calendar.DAY_OF_MONTH, 1);
				sb.append("date_format( registro , '%Y%m%d')>= '".concat(Fecha.formatear(Fecha.FECHA_ESTANDAR, seleccionada.getTime())).concat("'"));			
				seleccionada.set(Calendar.MONTH, 8);
				seleccionada.set(Calendar.DAY_OF_MONTH, seleccionada.getMaximum(Calendar.DAY_OF_MONTH));
				sb.append("and date_format( registro , '%Y%m%d')<= '".concat(Fecha.formatear(Fecha.FECHA_ESTANDAR, seleccionada.getTime())).concat("'"));			
			} // else
			else {
				seleccionada.set(Calendar.MONTH, 9);
				seleccionada.set(Calendar.DAY_OF_MONTH, 1);
				sb.append("date_format( registro , '%Y%m%d')>= '".concat(Fecha.formatear(Fecha.FECHA_ESTANDAR, seleccionada.getTime())).concat("'"));			
				seleccionada.set(Calendar.MONTH, 11);
				seleccionada.set(Calendar.DAY_OF_MONTH, seleccionada.getMaximum(Calendar.DAY_OF_MONTH));
				sb.append("and date_format( registro , '%Y%m%d')<= '".concat(Fecha.formatear(Fecha.FECHA_ESTANDAR, seleccionada.getTime())).concat("'"));			
			} // else
			this.attrs.put("condicionGeneral", sb.toString());      			
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
	} // toCondicionTrimestre
	
	private void toCondicionSemestre(){
		Calendar seleccionada= null;
		StringBuilder sb     = null;
		try {      
			seleccionada= Calendar.getInstance();
			sb= new StringBuilder("");
			if(seleccionada.get(Calendar.MONTH)<= 5){
				seleccionada.set(Calendar.MONTH, 0);
				seleccionada.set(Calendar.DAY_OF_MONTH, 1);
				sb.append("date_format( registro , '%Y%m%d')>= '".concat(Fecha.formatear(Fecha.FECHA_ESTANDAR, seleccionada.getTime())).concat("'"));			
				seleccionada.set(Calendar.MONTH, 5);
				seleccionada.set(Calendar.DAY_OF_MONTH, seleccionada.getMaximum(Calendar.DAY_OF_MONTH));
				sb.append("and date_format( registro , '%Y%m%d')<= '".concat(Fecha.formatear(Fecha.FECHA_ESTANDAR, seleccionada.getTime())).concat("'"));			
			} // if
			else{
				seleccionada.set(Calendar.MONTH, 6);
				seleccionada.set(Calendar.DAY_OF_MONTH, 1);
				sb.append("date_format( registro , '%Y%m%d')>= '".concat(Fecha.formatear(Fecha.FECHA_ESTANDAR, seleccionada.getTime())).concat("'"));			
				seleccionada.set(Calendar.MONTH, 11);
				seleccionada.set(Calendar.DAY_OF_MONTH, seleccionada.getMaximum(Calendar.DAY_OF_MONTH));
				sb.append("and date_format( registro , '%Y%m%d')<= '".concat(Fecha.formatear(Fecha.FECHA_ESTANDAR, seleccionada.getTime())).concat("'"));			
			} // else
			this.attrs.put("condicionGeneral", sb.toString());      			
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
	} // toCondicionSemestre
	
	private void toCondicionAnio(){
		Calendar seleccionada= null;
		StringBuilder sb     = null;
		try {      
			sb= new StringBuilder("");
			seleccionada= Calendar.getInstance();						
			seleccionada.set(Calendar.DAY_OF_YEAR, 1);
			sb.append("date_format( registro , '%Y%m%d')>= '".concat(Fecha.formatear(Fecha.FECHA_ESTANDAR, seleccionada.getTime())).concat("'"));			
			seleccionada.add(Calendar.DAY_OF_YEAR, seleccionada.getMaximum(Calendar.DAY_OF_YEAR));
			sb.append("and date_format( registro , '%Y%m%d')<= '".concat(Fecha.formatear(Fecha.FECHA_ESTANDAR, seleccionada.getTime())).concat("'"));						
			this.attrs.put("condicionGeneral", sb.toString());      			
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
	} // toCondicionAnio
}
