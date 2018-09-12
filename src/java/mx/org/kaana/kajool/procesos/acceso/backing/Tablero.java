package mx.org.kaana.kajool.procesos.acceso.backing;

import java.io.Serializable;
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
import mx.org.kaana.kajool.procesos.enums.ETipoGrafica;
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

  public List<UISelectItem> getSucursales() {
    return sucursales;
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
      loadPieModel();      
      loadLineModelNacional();      
      loadLineModel();
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
  }

  @Override
  public void doLoad() {
    List<Columna> campos= null;
    try {
      campos = new ArrayList<>();      
      this.attrs.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
      this.attrs.put("idUsuario", JsfBase.getIdUsuario());
      this.attrs.put("codigo", "");
      this.attrs.put("expresion", "");
      this.attrs.put("sortOrder", "");
      this.lazyModel = new FormatLazyModel("VistaArticulosDto", "row", this.attrs, campos);
      UIBackingUtilities.resetDataTable();
    } // try
    catch (Exception e) {
      JsfBase.addMessageError(e);
    } // catch
  } // doLoad

  private void loadLineModelNacional() throws Exception {
    Highcharts charts = null;
    Highcharts chartsCaja = null;
    BuildChart buildChart = null;
    try {
      buildChart = new BuildChart(JsfBase.getAutentifica().getPersona().getIdPerfil(), Long.valueOf(this.attrs.get("idGrupo").toString()));
      charts = buildChart.buildNacional(ETipoGrafica.COLUMNA);
      chartsCaja = buildChart.buildNacionalCaja(ETipoGrafica.LINEAL);
      this.attrs.put("tituloGeneralNacional", charts.getTitle().getText());
      this.attrs.put("jsonGeneralNacional", Decoder.toJson(charts));
			this.attrs.put("tituloGeneralNacionalCaja", chartsCaja.getTitle().getText());
      this.attrs.put("jsonGeneralNacionalCaja", Decoder.toJson(chartsCaja));
    } // try
    catch (Exception e) {
      throw e;
    } // catch    
  } // loadLineModelNacional 

  private void loadLineModel() throws Exception {
    List<Highcharts> charts = null;
    List<JsonChart> jsons = null;
    BuildChart buildChart = null;
    try {
      jsons = new ArrayList<>();
      buildChart = new BuildChart(JsfBase.getAutentifica().getPersona().getIdPerfil(), Long.valueOf(this.attrs.get("idGrupo").toString()));
      charts = buildChart.build(ETipoGrafica.BARRAS);
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

  private void loadPieModel() throws Exception {
    HighchartsPie chartUtilidad= null;
    HighchartsPie chartVentas  = null;
    JsonChart json = null;
    JsonChart jsonVentas = null;
    BuildChart buildChart = null;
    try {
      buildChart = new BuildChart(JsfBase.getAutentifica().getPersona().getIdPerfil(), JsfBase.getAutentifica().getPersona().getIdGrupo());
      chartUtilidad = buildChart.loadCharPropertiesPie("", "", "", "", "", "", "", "");
      chartVentas = buildChart.loadCharPropertiesPie("", "", "", "", "", "", "", "", true);
      json = new JsonChart("avanceNacional", "Articulos con mas utilidad", Decoder.toJson(chartUtilidad));
      jsonVentas = new JsonChart("avanceNacional", "Articulos con mas ventas", Decoder.toJson(chartVentas));
      this.attrs.put("jsonNacional", json);
      this.attrs.put("jsonVentas", jsonVentas);
    } // try
    catch (Exception e) {
      throw e;
    } // catch        
  } // loadPieModel

  private void loadMeses() {
    List<UISelectItem> meses = null;
    try {
      meses = new ArrayList<>();
      for (String mes : Fecha.meses(12)) {
        meses.add(new UISelectItem(mes, mes));
      }
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
        if (contadores != null) {
          this.attrs.put("contadoresMes", contadores);
        }
      } // if
      else {
        JsfBase.addMessage("Reporte por fecha", "Las fechas seleccionadas son inconsistentes. Favor de verificarlas", ETipoMensaje.ERROR);
      }
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
      if (contadores != null) {
        this.attrs.put("contadoresGenerales", contadores);
      }
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
  }

  public void doLoadSucursales() {
    try {
      this.sucursales = new ArrayList<>();
      for (ESucursales sucursal : ESucursales.values()) 
        this.sucursales.add(new UISelectItem(sucursal.getIdKey(), Cadena.reemplazarCaracter(sucursal.getSucursal(), '_', ' ')));
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
  }
}
