package mx.org.kaana.kajool.procesos.acceso.backing;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ESucursales;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.json.Decoder;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.kajool.procesos.acceso.beans.Persona;
import mx.org.kaana.kajool.procesos.acceso.beans.VentaEmpleado;
import mx.org.kaana.kajool.procesos.comun.Comun;
import mx.org.kaana.kajool.procesos.enums.EPerfiles;
import mx.org.kaana.kajool.procesos.utilerias.graficasperfiles.beans.Highcharts;
import mx.org.kaana.kajool.procesos.utilerias.graficasperfiles.beans.HighchartsPie;
import mx.org.kaana.kajool.procesos.utilerias.graficasperfiles.beans.JsonChart;
import mx.org.kaana.kajool.procesos.utilerias.graficasperfiles.beans.Title;
import mx.org.kaana.kajool.procesos.utilerias.graficasperfiles.reglas.BuildChart;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.formato.Global;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UISelectItem;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.db.dto.TcManticControlRespaldosDto;
import mx.org.kaana.mantic.db.dto.TcManticRespaldosDto;
import mx.org.kaana.mantic.enums.EEstatusVentas;
import mx.org.kaana.mantic.enums.EGraficasTablero;
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

  private static final long serialVersionUID= 5323749709626263801L;
  private static final Log LOG              = LogFactory.getLog(Tablero.class);
	private String mes[] = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
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
    Persona empleado  = null;
		String fechaActual= null;
    try {
      empleado= JsfBase.getAutentifica().getPersona();
			this.attrs.put("calendario", Calendar.getInstance());
			fechaActual= Fecha.formatear(Fecha.FECHA_MINIMA, cambiarDia(Fecha.formatear(Fecha.FECHA_MINIMA, (Calendar)this.attrs.get("calendario")), Boolean.FALSE));
      this.attrs.put("isAdmin", JsfBase.isAdminEncuestaOrAdmin());
      this.attrs.put("titulotabla", EPerfiles.fromOrdinal(empleado.getIdPerfil()).getTituloTabla());
      this.attrs.put("isTablaGeneral", EPerfiles.CAPTURISTA.getKey().equals(empleado.getIdPerfil()));
      this.attrs.put("pathConfiguracion", JsfBase.getApplication().getContextPath() + "/Paginas/Utilerias/InformacionSistema/filtro.jsf");
      this.attrs.put("pathCaptura", JsfBase.getApplication().getContextPath() + "/Paginas/Captura/filtro.jsf");
      this.attrs.put("pathMensajes", JsfBase.getApplication().getContextPath() + "/Paginas/Mantenimiento/Mensajes/Notificacion/filtro.jsf");
      this.attrs.put("vigenciaInicial", new java.sql.Date(Calendar.getInstance().getTimeInMillis()));
      this.attrs.put("vigenciaFin", new java.sql.Date(Calendar.getInstance().getTimeInMillis()));						
			this.attrs.put("estatusVentas", EEstatusVentas.CREDITO.getIdEstatusVenta()+","+EEstatusVentas.PAGADA.getIdEstatusVenta()+","+EEstatusVentas.TERMINADA.getIdEstatusVenta()+","+EEstatusVentas.TIMBRADA.getIdEstatusVenta());
			this.initPeriodos(fechaActual);
			this.loadAllCharts();       
      this.doLoadSucursales();
      this.doLoad();
      this.loadMeses();
      //doLoadContadoresMeses();
      this.loadContadoresGenerales();
      this.toMensajesNoLeidos(); 
			if(JsfBase.isAdminEncuestaOrAdmin())
			  this.checkDonwloadBackup();
    } // try
    catch (Exception e) {
      JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch        
  } // init

	private void initPeriodos(String fechaActual){
		try {
			for(EGraficasTablero grafica: EGraficasTablero.values()){
				this.attrs.put("fechaSeleccionada".concat(grafica.getIdPivote()), fechaActual);
				this.attrs.put("fechaSeleccionadaSemana".concat(grafica.getIdPivote()), fechaActual);			
				this.attrs.put("fechaSeleccionadaQuincena".concat(grafica.getIdPivote()), fechaActual);			
				this.attrs.put("fechaSeleccionadaMes".concat(grafica.getIdPivote()), fechaActual);			
				this.attrs.put("fechaSeleccionadaTrimestre".concat(grafica.getIdPivote()), fechaActual);			
				this.attrs.put("fechaSeleccionadaSemestre".concat(grafica.getIdPivote()), fechaActual);			
				this.attrs.put("fechaSeleccionadaAnio".concat(grafica.getIdPivote()), fechaActual);			
				this.attrs.put("idPeriodo".concat(grafica.getIdPivote()), 1L);
				this.attrs.put("nombrePeriodo".concat(grafica.getIdPivote()), EPeriodosTableros.fromIdPeriodo((Long)this.attrs.get("idPeriodo".concat(grafica.getIdPivote()))).getNombre());
				this.attrs.put("ultimoDia".concat(grafica.getIdPivote()), false);
				this.attrs.put("ultimoPeriodo".concat(grafica.getIdPivote()), false);
				this.attrs.put("primerPeriodo".concat(grafica.getIdPivote()), true);
				this.attrs.put("condicionGeneral".concat(grafica.getIdPivote()), Constantes.SQL_VERDADERO);
				this.attrs.put("renderedDia".concat(grafica.getIdPivote()), false);
				this.attrs.put("renderedSemana".concat(grafica.getIdPivote()), true);
				this.attrs.put("renderedQuincena".concat(grafica.getIdPivote()), true);
				this.attrs.put("renderedMes".concat(grafica.getIdPivote()), true);
				this.attrs.put("renderedTrimestre".concat(grafica.getIdPivote()), true);
				this.attrs.put("renderedSemestre".concat(grafica.getIdPivote()), true);
				this.attrs.put("renderedAnio".concat(grafica.getIdPivote()), true);			
			} // for
		} // try
		catch (Exception e) {		
			throw e;
		} // catch		
	} // initPeriodos
	
  @Override
  public void doLoad() {
    try {
      this.attrs.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
      this.attrs.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
      this.attrs.put("idUsuario", JsfBase.getIdUsuario());
      this.attrs.put("almacen", "");
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
			doLoadChartUtilidadSucursal();
			doLoadChartUtilidadCaja();
			doLoadChartCuentasCobrar();
			doLoadChartCuentasPagar();
			doLoadChartsGeneral();
			doLoadChartsArticulos();
			doLoadVentasEmpleado();
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
	} // loadAllCharts
	
	private void loadChart(EGraficasTablero grafica) throws Exception{
		try {			
			switch(grafica){
				case UTILIDAD_SUCURSAL:
					doLoadChartUtilidadSucursal();
					break;
				case UTILIDAD_CAJA:
					doLoadChartUtilidadCaja();
					break;
				case VENTAS_SUCURSAL:
					doLoadChartsGeneral();
					break;
				case ART_MAS_UTILIDAD:
					doLoadChartsArticulos();
					break;
				case ART_MAS_VENDIDOS:
					doLoadChartsArticulos();
					break;
				case CUENTAS_COBRAR:
					doLoadChartCuentasCobrar();
					break;
				case CUENTAS_PAGAR:
					doLoadChartCuentasPagar();
					break;
				case VENTAS_EMPLEADO:
					doLoadVentasEmpleado();
					break;
			} // switch
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
	} // loadAllCharts
	
	public void doLoadChartUtilidadSucursal() throws Exception{
		Highcharts utilidadPorSucursal= null;
		BuildChart buildChart         = null;
		try {
			buildChart = new BuildChart(toCreateCondicion(EGraficasTablero.UTILIDAD_SUCURSAL));
			utilidadPorSucursal = buildChart.buildUtilidadSucursal();
      this.attrs.put("utilidadPorSucursal", Decoder.toJson(utilidadPorSucursal).replace("\n", ""));
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
	} // loadChartUtilidadSucursal
	
	public void doLoadChartUtilidadCaja() throws Exception{
		BuildChart buildChart     = null;
		Highcharts utilidadPorCaja= null;		
		try {
			buildChart = new BuildChart(toCreateCondicion(EGraficasTablero.UTILIDAD_CAJA));
			utilidadPorCaja = buildChart.buildUtilidadCaja();            
      this.attrs.put("jsonUtilidadPorCaja", Decoder.toJson(utilidadPorCaja).replace("\n", ""));
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
	} // loadChartUtilidadSucursal
	
	public void doLoadChartCuentasCobrar() throws Exception{
		BuildChart buildChart      = null;
		Highcharts cuentasPorCobrar= null;
		try {
			buildChart = new BuildChart(toCreateCondicion(EGraficasTablero.CUENTAS_COBRAR));       
			cuentasPorCobrar = buildChart.buildCuentasCobrar();
      this.attrs.put("jsonCobro", Decoder.toJson(cuentasPorCobrar).replace("\n", ""));
		} // try
		catch (Exception e) {
			throw e;
		} // catch		
	} // loadChartCuentasCobrar
	
  private void doLoadChartCuentasPagar() throws Exception {            
    BuildChart buildChart     = null;
		Highcharts cuentasPorPagar= null;
    try {
      buildChart = new BuildChart(toCreateCondicion(EGraficasTablero.CUENTAS_PAGAR));           
      cuentasPorPagar = buildChart.buildCuentasPagar();      
      this.attrs.put("jsonPago", Decoder.toJson(cuentasPorPagar).replace("\n", ""));
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
      buildChart = new BuildChart(toCreateCondicion(EGraficasTablero.VENTAS_SUCURSAL));
      charts= buildChart.build();
      for (Highcharts chart : charts) {
        JsonChart json = new JsonChart(Cadena.eliminaCaracter(chart.getTitle().getText(), ' ').toLowerCase(), chart.getTitle().getText(), "");
        chart.setTitle(new Title(""));
        json.setJson(Decoder.toJson(chart).replace("\n", ""));
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
      buildChart = new BuildChart(toCreateCondicion(EGraficasTablero.ART_MAS_UTILIDAD));
      artMasUtilidad= buildChart.buildArticulosMasUtilidad();
			jsonArtMasUtilidad = new JsonChart("avanceNacional", "Articulos con mas utilidad", Decoder.toJson(artMasUtilidad).replace("\n", ""));
			this.attrs.put("jsonUtilidad", jsonArtMasUtilidad);
			buildChart = new BuildChart(toCreateCondicion(EGraficasTablero.ART_MAS_VENDIDOS));
      artMasVentas  = buildChart.buildArticulosMasVendidos();      
      jsonArtMasVentas = new JsonChart("avanceNacional", "Articulos con mas ventas", Decoder.toJson(artMasVentas).replace("\n", ""));      
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
    Entity contadores = null;
    try {
      contadores = (Entity) DaoFactory.getInstance().toEntity("VistaContadoresBienvenidaDto", "nacional", this.attrs);
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
	
	public void doCambiarDia(boolean isAdelante, String enumGrafica) {
    Calendar seleccionada   = null;
    Calendar actual         = null;
		EGraficasTablero grafica= null;
    try {
			grafica= EGraficasTablero.fromNameTablero(enumGrafica);
      this.attrs.put("fechaSeleccionada".concat(grafica.getIdPivote()), Fecha.formatear(Fecha.FECHA_MINIMA, cambiarDia(this.attrs.get("fechaSeleccionada".concat(grafica.getIdPivote())).toString(), isAdelante)));      
      if(isAdelante) {
        seleccionada= Calendar.getInstance();
        seleccionada.setTime(new SimpleDateFormat("dd/MM/yy").parse(this.attrs.get("fechaSeleccionada".concat(grafica.getIdPivote())).toString()));
        actual= Calendar.getInstance();
        actual.setTime(new SimpleDateFormat("dd/MM/yy").parse(Fecha.getHoy()));
        this.attrs.put("ultimoDia".concat(grafica.getIdPivote()), seleccionada.compareTo(actual)== 0);
      } // if
      else
        this.attrs.put("ultimoDia".concat(grafica.getIdPivote()), false);			
			loadChart(grafica);
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
	
	public void doCambiarPeriodo(boolean isAdelante, String enumGrafica) {    
		Long idPeriodo          = -1L;
    EGraficasTablero grafica= null;
    try {
			grafica= EGraficasTablero.fromNameTablero(enumGrafica);
			idPeriodo= (Long) this.attrs.get("idPeriodo".concat(grafica.getIdPivote()));
      if(isAdelante)
				idPeriodo= idPeriodo + 1L;        
			else
				idPeriodo= idPeriodo - 1L;      
			this.attrs.put("nombrePeriodo".concat(grafica.getIdPivote()), EPeriodosTableros.fromIdPeriodo(idPeriodo).getNombre());      
			this.attrs.put("primerPeriodo".concat(grafica.getIdPivote()), idPeriodo.equals(EPeriodosTableros.DIA.getIdTipoPeriodo()));
			this.attrs.put("ultimoPeriodo".concat(grafica.getIdPivote()), idPeriodo.equals(EPeriodosTableros.ANIO.getIdTipoPeriodo()));
			refreshPeriodos(idPeriodo, grafica);
			this.attrs.put("idPeriodo".concat(grafica.getIdPivote()), idPeriodo);						
			loadChart(grafica);
    } // try
    catch(Exception e) {
			LOG.debug("Error en método doCambiarDia: ".concat(e.toString()));
      JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
  } // doCambiarDia
	
	private void refreshPeriodos(Long idPeriodo, EGraficasTablero grafica){		
    try {    	
			this.attrs.put("renderedDia".concat(grafica.getIdPivote()), true);
			this.attrs.put("renderedSemana".concat(grafica.getIdPivote()), true);
			this.attrs.put("renderedQuincena".concat(grafica.getIdPivote()), true);
			this.attrs.put("renderedMes".concat(grafica.getIdPivote()), true);
			this.attrs.put("renderedTrimestre".concat(grafica.getIdPivote()), true);
			this.attrs.put("renderedSemestre".concat(grafica.getIdPivote()), true);
			this.attrs.put("renderedAnio".concat(grafica.getIdPivote()), true);			
			switch(EPeriodosTableros.fromIdPeriodo(idPeriodo)){
				case DIA:
					this.attrs.put("renderedDia".concat(grafica.getIdPivote()), false);
					break;
				case SEMANA:
					this.attrs.put("renderedSemana".concat(grafica.getIdPivote()), false);
					break;
				case QUINCENA:
					this.attrs.put("renderedQuincena".concat(grafica.getIdPivote()), false);
					break;
				case MES:
					this.attrs.put("renderedMes".concat(grafica.getIdPivote()), false);
					break;
				case TRIMESTRE:
					this.attrs.put("renderedTrimestre".concat(grafica.getIdPivote()), false);
					break;
				case SEMESTRE:
					this.attrs.put("renderedSemestre".concat(grafica.getIdPivote()), false);
					break;
				case ANIO:
					this.attrs.put("renderedAnio".concat(grafica.getIdPivote()), false);			
					break;
			} // switch
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
	} // refreshPeriodos
	
	private String toCreateCondicion(EGraficasTablero grafica) throws Exception{
		String regresar= null;
		Long idPeriodo = -1L;
		try {
			idPeriodo= (Long) this.attrs.get("idPeriodo".concat(grafica.getIdPivote()));
			switch(EPeriodosTableros.fromIdPeriodo(idPeriodo)){
				case DIA:
					regresar= toCondicionDia("fechaSeleccionada".concat(grafica.getIdPivote()));
					break;
				case SEMANA:
					regresar= toCondicionSemana("fechaSeleccionadaSemana".concat(grafica.getIdPivote()), grafica.getIdPivote());
					break;
				case QUINCENA:
					regresar= toCondicionQuincena("fechaSeleccionadaQuincena".concat(grafica.getIdPivote()), grafica.getIdPivote());
					break;
				case MES:
					regresar= toCondicionMes("fechaSeleccionadaMes".concat(grafica.getIdPivote()), grafica.getIdPivote());
					break;
				case TRIMESTRE:
					regresar= toCondicionTrimestre("fechaSeleccionadaTrimestre".concat(grafica.getIdPivote()), grafica.getIdPivote());
					break;
				case SEMESTRE:
					regresar= toCondicionSemestre("fechaSeleccionadaSemestre".concat(grafica.getIdPivote()), grafica.getIdPivote());
					break;
				case ANIO:
					regresar= toCondicionAnio("fechaSeleccionadaAnio".concat(grafica.getIdPivote()), grafica.getIdPivote());
					break;
			} // switch
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toCreateCondicion
	
	private String toCondicionDia(String nombreFecha) throws Exception{
		String regresar      = null;
		Calendar seleccionada= null;
		try {      
			seleccionada= Calendar.getInstance();
			seleccionada.setTime(new SimpleDateFormat("dd/MM/yy").parse(this.attrs.get(nombreFecha).toString()));			
			regresar= "date_format( registro , '%Y%m%d') = '".concat(Fecha.formatear(Fecha.FECHA_ESTANDAR, seleccionada.getTime())).concat("'"); 
		} // try
		catch (Exception e) {			
			throw e;
		} // catch	
		return regresar;
	} // toCondicionDia 
	
	public void doCambiarSemana(boolean isAdelante, String enumGrafica) {
    Calendar seleccionada= null;    
    EGraficasTablero grafica= null;
    try {
			grafica= EGraficasTablero.fromNameTablero(enumGrafica);
      this.attrs.put("fechaSeleccionadaSemana".concat(grafica.getIdPivote()), Fecha.formatear(Fecha.FECHA_MINIMA, cambiarSemana(this.attrs.get("fechaSeleccionadaSemana".concat(grafica.getIdPivote())).toString(), isAdelante)));      
			seleccionada= Calendar.getInstance();
      seleccionada.setTime(new SimpleDateFormat("dd/MM/yy").parse(this.attrs.get("fechaSeleccionadaSemana".concat(grafica.getIdPivote())).toString()));      			            
			loadChart(grafica);
    } // try
    catch(Exception e) {
			LOG.debug("Error en método doCambiarDia: ".concat(e.toString()));
      JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
  } // doCambiarDia
  
  private Calendar cambiarSemana(String fecha, Boolean isAdelante) throws Exception {
    Calendar regresar= null;
    try {
      regresar= Calendar.getInstance();
      regresar.setTime(new SimpleDateFormat("dd/MM/yy").parse(fecha));
      if(isAdelante)         
        regresar.add(Calendar.WEEK_OF_MONTH, 1);
      else         
        regresar.add(Calendar.WEEK_OF_MONTH, -1);
    } // try
		catch (Exception e) {
			LOG.debug("Error en método cambiarDia: ".concat(e.toString()));
      throw e;
		} // catch
    return regresar;
  } // cambiarQuincena
	
	private String toCondicionSemana(String nombreFecha, String nombrePivote) throws Exception{
		Calendar seleccionada= null;
		StringBuilder sb     = null;
		StringBuilder title  = null;
		try {      			
			seleccionada= Calendar.getInstance();
			seleccionada.setTime(new SimpleDateFormat("dd/MM/yy").parse(this.attrs.get(nombreFecha).toString()));									
			seleccionada.add(Calendar.WEEK_OF_YEAR, -1);
			sb= new StringBuilder("");
			title= new StringBuilder("");
			seleccionada.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
			sb.append("date_format( registro , '%Y%m%d')>= '".concat(Fecha.formatear(Fecha.FECHA_ESTANDAR, seleccionada.getTime())).concat("'"));			
			title.append(Fecha.formatear(Fecha.FECHA_CORTA, seleccionada)).append(" - ");			
			seleccionada.add(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
			sb.append(" and date_format( registro , '%Y%m%d')<= '".concat(Fecha.formatear(Fecha.FECHA_ESTANDAR, seleccionada.getTime())).concat("'"));			
			title.append(Fecha.formatear(Fecha.FECHA_CORTA, seleccionada));			
			this.attrs.put("semana".concat(nombrePivote), "7 dias");			
			this.attrs.put("semanaTitle".concat(nombrePivote), title);			
		} // try
		catch (Exception e) {			
			throw e;
		} // catch	
		return sb.toString();
	} // toCondicionSemana
	
	public void doCambiarQuincena(boolean isAdelante, String enumGrafica) {
    Calendar seleccionada= null;    
    EGraficasTablero grafica= null;
    try {
			grafica= EGraficasTablero.fromNameTablero(enumGrafica);
      this.attrs.put("fechaSeleccionadaQuincena".concat(grafica.getIdPivote()), Fecha.formatear(Fecha.FECHA_MINIMA, cambiarQuincena(this.attrs.get("fechaSeleccionadaQuincena".concat(grafica.getIdPivote())).toString(), isAdelante)));      
			seleccionada= Calendar.getInstance();
      seleccionada.setTime(new SimpleDateFormat("dd/MM/yy").parse(this.attrs.get("fechaSeleccionadaQuincena".concat(grafica.getIdPivote())).toString()));      			            
			loadChart(grafica);
    } // try
    catch(Exception e) {
			LOG.debug("Error en método doCambiarDia: ".concat(e.toString()));
      JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
  } // doCambiarDia
  
  private Calendar cambiarQuincena(String fecha, Boolean isAdelante) throws Exception {
    Calendar regresar= null;
    try {
      regresar= Calendar.getInstance();
      regresar.setTime(new SimpleDateFormat("dd/MM/yy").parse(fecha));
      if(isAdelante)         
        regresar.add(Calendar.WEEK_OF_MONTH, 2);
      else         
        regresar.add(Calendar.WEEK_OF_MONTH, -2);
    } // try
		catch (Exception e) {
			LOG.debug("Error en método cambiarQuincena: ".concat(e.toString()));
      throw e;
		} // catch
    return regresar;
  } // cambiarQuincena
	
	private String toCondicionQuincena(String nombreFecha, String nombrePivote) throws Exception{
		Calendar seleccionada= null;
		StringBuilder sb     = null;
		StringBuilder title  = null;
		try {      
			seleccionada= Calendar.getInstance();
			seleccionada.setTime(new SimpleDateFormat("dd/MM/yy").parse(this.attrs.get(nombreFecha).toString()));									
			sb= new StringBuilder("");
			title= new StringBuilder("");
			if(seleccionada.get(Calendar.DAY_OF_MONTH)<= 15){
				seleccionada.set(Calendar.WEEK_OF_MONTH, 1);
				seleccionada.set(Calendar.DAY_OF_MONTH, 1);
				sb.append("date_format( registro , '%Y%m%d')>= '".concat(Fecha.formatear(Fecha.FECHA_ESTANDAR, seleccionada.getTime())).concat("'"));			
				title.append(Fecha.formatear(Fecha.FECHA_CORTA, seleccionada)).append(" - ");
				seleccionada.add(Calendar.DAY_OF_MONTH, 14);
				sb.append(" and date_format( registro , '%Y%m%d')<= '".concat(Fecha.formatear(Fecha.FECHA_ESTANDAR, seleccionada.getTime())).concat("'"));			
				title.append(Fecha.formatear(Fecha.FECHA_CORTA, seleccionada));
			} // if
			else{
				seleccionada.set(Calendar.DAY_OF_MONTH, 16);
				sb.append("date_format( registro , '%Y%m%d')>= '".concat(Fecha.formatear(Fecha.FECHA_ESTANDAR, seleccionada.getTime())).concat("'"));			
				title.append(Fecha.formatear(Fecha.FECHA_CORTA, seleccionada)).append(" - ");
				seleccionada.set(Calendar.DAY_OF_MONTH, seleccionada.getActualMaximum(Calendar.DAY_OF_MONTH));
				sb.append(" and date_format( registro , '%Y%m%d')<= '".concat(Fecha.formatear(Fecha.FECHA_ESTANDAR, seleccionada.getTime())).concat("'"));			
				title.append(Fecha.formatear(Fecha.FECHA_CORTA, seleccionada));
			} // else
			this.attrs.put("quincena".concat(nombrePivote), "15 dias");			
			this.attrs.put("quincenaTitle".concat(nombrePivote), title);			
		} // try
		catch (Exception e) {			
			throw e;
		} // catch	
		return sb.toString();
	} // toCondicionQuincena
	
	public void doCambiarMes(boolean isAdelante, String enumGrafica) {
    Calendar seleccionada= null;    
    EGraficasTablero grafica= null;
    try {
			grafica= EGraficasTablero.fromNameTablero(enumGrafica);
      this.attrs.put("fechaSeleccionadaMes".concat(grafica.getIdPivote()), Fecha.formatear(Fecha.FECHA_MINIMA, cambiarMes(this.attrs.get("fechaSeleccionadaMes".concat(grafica.getIdPivote())).toString(), isAdelante)));      
			seleccionada= Calendar.getInstance();
      seleccionada.setTime(new SimpleDateFormat("dd/MM/yy").parse(this.attrs.get("fechaSeleccionadaMes".concat(grafica.getIdPivote())).toString()));      			            
			loadChart(grafica);
    } // try
    catch(Exception e) {
			LOG.debug("Error en método doCambiarDia: ".concat(e.toString()));
      JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
  } // doCambiarDia
  
  private Calendar cambiarMes(String fecha, Boolean isAdelante) throws Exception {
    Calendar regresar= null;
    try {
      regresar= Calendar.getInstance();
      regresar.setTime(new SimpleDateFormat("dd/MM/yy").parse(fecha));
      if(isAdelante)         
        regresar.add(Calendar.MONTH, 1);
      else         
        regresar.add(Calendar.MONTH, -1);
    } // try
		catch (Exception e) {
			LOG.debug("Error en método cambiarQuincena: ".concat(e.toString()));
      throw e;
		} // catch
    return regresar;
  } // cambiarQuincena
	
	private String toCondicionMes(String nombreFecha, String nombrePivote) throws Exception{
		Calendar seleccionada= null;
		StringBuilder sb     = null;		
		try {      
			seleccionada= Calendar.getInstance();
			seleccionada.setTime(new SimpleDateFormat("dd/MM/yy").parse(this.attrs.get(nombreFecha).toString()));									
			sb= new StringBuilder("");						
			seleccionada.set(Calendar.DAY_OF_MONTH, 1);
			sb.append("date_format( registro , '%Y%m%d')>= '".concat(Fecha.formatear(Fecha.FECHA_ESTANDAR, seleccionada.getTime())).concat("'"));			
			seleccionada.add(Calendar.DAY_OF_MONTH, seleccionada.getActualMaximum(Calendar.DAY_OF_MONTH)-1);
			sb.append(" and date_format( registro , '%Y%m%d')<= '".concat(Fecha.formatear(Fecha.FECHA_ESTANDAR, seleccionada.getTime())).concat("'"));						
			this.attrs.put("mes".concat(nombrePivote), this.mes[seleccionada.get(Calendar.MONTH)]);			
			this.attrs.put("mesTitle".concat(nombrePivote), " del " + seleccionada.get(Calendar.YEAR));			
		} // try
		catch (Exception e) {			
			throw e; 
		} // catch				
		return sb.toString();
	} // toCondicionMes
	
	public void doCambiarTrimestre(boolean isAdelante, String enumGrafica) {
    Calendar seleccionada= null;    
    EGraficasTablero grafica= null;
    try {
			grafica= EGraficasTablero.fromNameTablero(enumGrafica);
      this.attrs.put("fechaSeleccionadaTrimestre".concat(grafica.getIdPivote()), Fecha.formatear(Fecha.FECHA_MINIMA, cambiarTrimestre(this.attrs.get("fechaSeleccionadaTrimestre".concat(grafica.getIdPivote())).toString(), isAdelante)));      
			seleccionada= Calendar.getInstance();
      seleccionada.setTime(new SimpleDateFormat("dd/MM/yy").parse(this.attrs.get("fechaSeleccionadaTrimestre".concat(grafica.getIdPivote())).toString()));      			            
			loadChart(grafica);
    } // try
    catch(Exception e) {
			LOG.debug("Error en método doCambiarDia: ".concat(e.toString()));
      JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
  } // doCambiarDia
  
  private Calendar cambiarTrimestre(String fecha, Boolean isAdelante) throws Exception {
    Calendar regresar= null;
    try {
      regresar= Calendar.getInstance();
      regresar.setTime(new SimpleDateFormat("dd/MM/yy").parse(fecha));
      if(isAdelante)         
        regresar.add(Calendar.MONTH, 3);
      else         
        regresar.add(Calendar.MONTH, -3);
    } // try
		catch (Exception e) {
			LOG.debug("Error en método cambiarQuincena: ".concat(e.toString()));
      throw e;
		} // catch
    return regresar;
  } // cambiarTrimestre
	
	private String toCondicionTrimestre(String nombreFecha, String nombrePivote) throws Exception{
		Calendar seleccionada= null;
		StringBuilder sb     = null;
		StringBuilder title  = null;
		try {      
			seleccionada= Calendar.getInstance();
			seleccionada.setTime(new SimpleDateFormat("dd/MM/yy").parse(this.attrs.get(nombreFecha).toString()));									
			sb= new StringBuilder("");
			title= new StringBuilder("");
			if(seleccionada.get(Calendar.MONTH)<= 2){
				seleccionada.set(Calendar.MONTH, 0);
				seleccionada.set(Calendar.DAY_OF_MONTH, 1);
				sb.append("date_format( registro , '%Y%m%d')>= '".concat(Fecha.formatear(Fecha.FECHA_ESTANDAR, seleccionada.getTime())).concat("'"));			
				title.append(this.mes[seleccionada.get(Calendar.MONTH)]).append(" - ");
				seleccionada.add(Calendar.MONTH, 2);
				seleccionada.set(Calendar.DAY_OF_MONTH, seleccionada.getActualMaximum(Calendar.DAY_OF_MONTH));
				sb.append(" and date_format( registro , '%Y%m%d')<= '".concat(Fecha.formatear(Fecha.FECHA_ESTANDAR, seleccionada.getTime())).concat("'"));			
				title.append(this.mes[seleccionada.get(Calendar.MONTH)]);				
			} // if
			else if (seleccionada.get(Calendar.MONTH)<= 5){
				seleccionada.set(Calendar.MONTH, 3);
				seleccionada.set(Calendar.DAY_OF_MONTH, 1);
				sb.append("date_format( registro , '%Y%m%d')>= '".concat(Fecha.formatear(Fecha.FECHA_ESTANDAR, seleccionada.getTime())).concat("'"));			
				title.append(this.mes[seleccionada.get(Calendar.MONTH)]).append(" - ");
				seleccionada.add(Calendar.MONTH, 2);
				seleccionada.set(Calendar.DAY_OF_MONTH, seleccionada.getActualMaximum(Calendar.DAY_OF_MONTH));
				sb.append(" and date_format( registro , '%Y%m%d')<= '".concat(Fecha.formatear(Fecha.FECHA_ESTANDAR, seleccionada.getTime())).concat("'"));			
				title.append(this.mes[seleccionada.get(Calendar.MONTH)]);
			} // else
			else if (seleccionada.get(Calendar.MONTH)<= 8){
				seleccionada.set(Calendar.MONTH, 6);
				seleccionada.set(Calendar.DAY_OF_MONTH, 1);
				sb.append("date_format( registro , '%Y%m%d')>= '".concat(Fecha.formatear(Fecha.FECHA_ESTANDAR, seleccionada.getTime())).concat("'"));			
				title.append(this.mes[seleccionada.get(Calendar.MONTH)]).append(" - ");
				seleccionada.add(Calendar.MONTH, 2);
				seleccionada.set(Calendar.DAY_OF_MONTH, seleccionada.getActualMaximum(Calendar.DAY_OF_MONTH));
				sb.append(" and date_format( registro , '%Y%m%d')<= '".concat(Fecha.formatear(Fecha.FECHA_ESTANDAR, seleccionada.getTime())).concat("'"));			
				title.append(this.mes[seleccionada.get(Calendar.MONTH)]);
			} // else
			else {
				seleccionada.set(Calendar.MONTH, 9);
				seleccionada.set(Calendar.DAY_OF_MONTH, 1);
				sb.append("date_format( registro , '%Y%m%d')>= '".concat(Fecha.formatear(Fecha.FECHA_ESTANDAR, seleccionada.getTime())).concat("'"));			
				title.append(this.mes[seleccionada.get(Calendar.MONTH)]).append(" - ");
				seleccionada.add(Calendar.MONTH, 2);
				seleccionada.set(Calendar.DAY_OF_MONTH, seleccionada.getActualMaximum(Calendar.DAY_OF_MONTH));
				sb.append(" and date_format( registro , '%Y%m%d')<= '".concat(Fecha.formatear(Fecha.FECHA_ESTANDAR, seleccionada.getTime())).concat("'"));			
				title.append(this.mes[seleccionada.get(Calendar.MONTH)]);
			} // else
			this.attrs.put("trimestre".concat(nombrePivote), "3 meses");			
			this.attrs.put("trimestreTitle".concat(nombrePivote), title + " del " + seleccionada.get(Calendar.YEAR));			
		} // try
		catch (Exception e) {			
			throw e; 
		} // catch		
		return sb.toString();
	} // toCondicionTrimestre
	
	public void doCambiarSemestre(boolean isAdelante, String enumGrafica) {
    Calendar seleccionada= null;    
    EGraficasTablero grafica= null;
    try {
			grafica= EGraficasTablero.fromNameTablero(enumGrafica);
      this.attrs.put("fechaSeleccionadaSemestre".concat(grafica.getIdPivote()), Fecha.formatear(Fecha.FECHA_MINIMA, cambiarSemestre(this.attrs.get("fechaSeleccionadaSemestre".concat(grafica.getIdPivote())).toString(), isAdelante)));      
			seleccionada= Calendar.getInstance();
      seleccionada.setTime(new SimpleDateFormat("dd/MM/yy").parse(this.attrs.get("fechaSeleccionadaSemestre".concat(grafica.getIdPivote())).toString()));      			            
			loadChart(grafica);
    } // try
    catch(Exception e) {
			LOG.debug("Error en método doCambiarDia: ".concat(e.toString()));
      JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
  } // doCambiarSemestre
  
  private Calendar cambiarSemestre(String fecha, Boolean isAdelante) throws Exception {
    Calendar regresar= null;
    try {
      regresar= Calendar.getInstance();
      regresar.setTime(new SimpleDateFormat("dd/MM/yy").parse(fecha));
      if(isAdelante)         
        regresar.add(Calendar.MONTH, 6);
      else         
        regresar.add(Calendar.MONTH, -6);
    } // try
		catch (Exception e) {
			LOG.debug("Error en método cambiarQuincena: ".concat(e.toString()));
      throw e;
		} // catch
    return regresar;
  } // cambiarSemestre
	
	private String toCondicionSemestre(String nombreFecha, String nombrePivote) throws Exception{
		Calendar seleccionada= null;
		StringBuilder sb     = null;
		StringBuilder title  = null;
		try {      
			seleccionada= Calendar.getInstance();
			seleccionada.setTime(new SimpleDateFormat("dd/MM/yy").parse(this.attrs.get(nombreFecha).toString()));									
			sb= new StringBuilder("");
			title= new StringBuilder("");
			if(seleccionada.get(Calendar.MONTH)<= 5){
				seleccionada.set(Calendar.MONTH, 0);
				seleccionada.set(Calendar.DAY_OF_MONTH, 1);
				sb.append("date_format( registro , '%Y%m%d')>= '".concat(Fecha.formatear(Fecha.FECHA_ESTANDAR, seleccionada.getTime())).concat("'"));			
				title.append(this.mes[seleccionada.get(Calendar.MONTH)]).append(" - ");
				seleccionada.set(Calendar.MONTH, 5);
				seleccionada.set(Calendar.DAY_OF_MONTH, seleccionada.getActualMaximum(Calendar.DAY_OF_MONTH));
				sb.append(" and date_format( registro , '%Y%m%d')<= '".concat(Fecha.formatear(Fecha.FECHA_ESTANDAR, seleccionada.getTime())).concat("'"));			
				title.append(this.mes[seleccionada.get(Calendar.MONTH)]);
			} // if
			else{
				seleccionada.set(Calendar.MONTH, 6);
				seleccionada.set(Calendar.DAY_OF_MONTH, 1);
				sb.append("date_format( registro , '%Y%m%d')>= '".concat(Fecha.formatear(Fecha.FECHA_ESTANDAR, seleccionada.getTime())).concat("'"));			
				title.append(this.mes[seleccionada.get(Calendar.MONTH)]).append(" - ");
				seleccionada.set(Calendar.MONTH, 11);
				seleccionada.set(Calendar.DAY_OF_MONTH, seleccionada.getActualMaximum(Calendar.DAY_OF_MONTH));
				sb.append(" and date_format( registro , '%Y%m%d')<= '".concat(Fecha.formatear(Fecha.FECHA_ESTANDAR, seleccionada.getTime())).concat("'"));			
				title.append(this.mes[seleccionada.get(Calendar.MONTH)]);
			} // else
			this.attrs.put("semestre".concat(nombrePivote), "6 meses");			
			this.attrs.put("semestreTitle".concat(nombrePivote), title + " del " + seleccionada.get(Calendar.YEAR));			
		} // try
		catch (Exception e) {			
			throw e; 
		} // catch		
		return sb.toString();
	} // toCondicionSemestre
	
	public void doCambiarAnio(boolean isAdelante, String enumGrafica) {
    Calendar seleccionada   = null;    
    EGraficasTablero grafica= null;
    try {
			grafica= EGraficasTablero.fromNameTablero(enumGrafica);
      this.attrs.put("fechaSeleccionadaAnio".concat(grafica.getIdPivote()), Fecha.formatear(Fecha.FECHA_MINIMA, cambiarAnio(this.attrs.get("fechaSeleccionadaAnio".concat(grafica.getIdPivote())).toString(), isAdelante)));      
			seleccionada= Calendar.getInstance();
      seleccionada.setTime(new SimpleDateFormat("dd/MM/yy").parse(this.attrs.get("fechaSeleccionadaAnio".concat(grafica.getIdPivote())).toString()));      			            
			loadChart(grafica);
    } // try
    catch(Exception e) {
			LOG.debug("Error en método doCambiarDia: ".concat(e.toString()));
      JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
  } // doCambiarSemestre
  
  private Calendar cambiarAnio(String fecha, Boolean isAdelante) throws Exception {
    Calendar regresar= null;
    try {
      regresar= Calendar.getInstance();
      regresar.setTime(new SimpleDateFormat("dd/MM/yy").parse(fecha));
      if(isAdelante)         
        regresar.add(Calendar.YEAR, 1);
      else         
        regresar.add(Calendar.YEAR, -1);
    } // try
		catch (Exception e) {
			LOG.debug("Error en método cambiarQuincena: ".concat(e.toString()));
      throw e;
		} // catch
    return regresar;
  } // cambiarAnio
	
	private String toCondicionAnio(String nombreFecha, String nombrePivote) throws Exception{
		Calendar seleccionada= null;
		StringBuilder sb     = null;
		StringBuilder title  = null;
		try {      
			sb= new StringBuilder("");
			title= new StringBuilder("");
			seleccionada= Calendar.getInstance();						
			seleccionada.setTime(new SimpleDateFormat("dd/MM/yy").parse(this.attrs.get(nombreFecha).toString()));									
			seleccionada.set(Calendar.DAY_OF_YEAR, 1);
			sb.append("date_format( registro , '%Y%m%d')>= '".concat(Fecha.formatear(Fecha.FECHA_ESTANDAR, seleccionada.getTime())).concat("'"));			
			seleccionada.add(Calendar.DAY_OF_YEAR, seleccionada.getActualMaximum(Calendar.DAY_OF_YEAR)-1);
			sb.append(" and date_format( registro , '%Y%m%d')<= '".concat(Fecha.formatear(Fecha.FECHA_ESTANDAR, seleccionada.getTime())).concat("'"));						
			title.append(seleccionada.get(Calendar.YEAR));
			this.attrs.put("anio".concat(nombrePivote), seleccionada.get(Calendar.YEAR));			
			this.attrs.put("anioTitle".concat(nombrePivote), title);			
		} // try
		catch (Exception e) {			
			throw e;
		} // catch	
		return sb.toString();
	} // toCondicionAnio
	
	public void doLoadVentasEmpleado(){
		List<VentaEmpleado> ventas= null;
		Map<String, Object>params = null;
		BuildChart build          = null;
		try {
			build= new BuildChart();
			params= new HashMap<>();
			params.put("condicionGeneral", build.toFormatCondicionGeneral(EGraficasTablero.VENTAS_EMPLEADO, toCreateCondicion(EGraficasTablero.VENTAS_EMPLEADO)));
			params.put("estatusVentas", EEstatusVentas.CREDITO.getIdEstatusVenta()+","+EEstatusVentas.PAGADA.getIdEstatusVenta()+","+EEstatusVentas.TERMINADA.getIdEstatusVenta()+","+EEstatusVentas.TIMBRADA.getIdEstatusVenta());
			ventas= DaoFactory.getInstance().toEntitySet(VentaEmpleado.class, "VistaIndicadoresTableroDto", "ventasPorEmpleado", params, Constantes.SQL_TODOS_REGISTROS);
			this.attrs.put("ventasEmpleados", ventas);
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch
		finally {
			Methods.clean(params);
		} // finally
	} // doLoadVentasEmpleado	
	
  private void checkDonwloadBackup() {
	  Map<String, Object> params= null;
		try {
		  params=new HashMap<>();
			TcManticRespaldosDto respaldo= (TcManticRespaldosDto)DaoFactory.getInstance().toEntity(TcManticRespaldosDto.class, "TcManticRespaldosDto", "ultimo", Collections.EMPTY_MAP);
			if(respaldo!= null) {
			  params.put("idRespaldo", respaldo.getIdRespaldo());
				TcManticControlRespaldosDto control= (TcManticControlRespaldosDto)DaoFactory.getInstance().toEntity(TcManticControlRespaldosDto.class, "TcManticControlRespaldosDto", "ultimo", params);
			  if(control!= null) {
					if(!control.getIdRespaldo().equals(respaldo.getIdRespaldo())) {
					  this.attrs.put("messageBackup", "NO se ha DESCARGADO el respaldo de la 'Base de Datos', desde "+ Global.format(EFormatoDinamicos.DIA_FECHA_HORA, control.getRegistro()));
  				  UIBackingUtilities.execute("PF('downloadBackup').show()");
	  			} // if
				} // if	
				else {
					this.attrs.put("messageBackup", "NUNCA se ha DESCARGADO el respaldo de la 'Base de Datos', por favor realice una descarga a su equipo de trabajo !");
				  UIBackingUtilities.execute("PF('downloadBackup').show()");
				} // else	
			} // if
		} // try
		catch (Exception e) {
			Error.mensaje(e);
		} // catch
		finally {
			Methods.clean(params);
		} // finally	
	} // checkDonwloadBackup
}