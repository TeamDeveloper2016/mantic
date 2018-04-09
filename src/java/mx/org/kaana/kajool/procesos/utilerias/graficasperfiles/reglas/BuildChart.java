package mx.org.kaana.kajool.procesos.utilerias.graficasperfiles.reglas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.db.dto.TrJanalIndicadoresPerfilDto;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.procesos.enums.EPerfiles;
import mx.org.kaana.kajool.procesos.enums.ETipoGrafica;
import mx.org.kaana.kajool.procesos.utilerias.graficasperfiles.beans.Bar;
import mx.org.kaana.kajool.procesos.utilerias.graficasperfiles.beans.Chart;
import mx.org.kaana.kajool.procesos.utilerias.graficasperfiles.beans.Credits;
import mx.org.kaana.kajool.procesos.utilerias.graficasperfiles.beans.DataLabels;
import mx.org.kaana.kajool.procesos.utilerias.graficasperfiles.beans.DetailData;
import mx.org.kaana.kajool.procesos.utilerias.graficasperfiles.beans.Highcharts;
import mx.org.kaana.kajool.procesos.utilerias.graficasperfiles.beans.HighchartsPie;
import mx.org.kaana.kajool.procesos.utilerias.graficasperfiles.beans.Labels;
import mx.org.kaana.kajool.procesos.utilerias.graficasperfiles.beans.Legend;
import mx.org.kaana.kajool.procesos.utilerias.graficasperfiles.beans.Pie;
import mx.org.kaana.kajool.procesos.utilerias.graficasperfiles.beans.PlotOptions;
import mx.org.kaana.kajool.procesos.utilerias.graficasperfiles.beans.Series;
import mx.org.kaana.kajool.procesos.utilerias.graficasperfiles.beans.Title;
import mx.org.kaana.kajool.procesos.utilerias.graficasperfiles.beans.Tooltip;
import mx.org.kaana.kajool.procesos.utilerias.graficasperfiles.beans.Totales;
import mx.org.kaana.kajool.procesos.utilerias.graficasperfiles.beans.TotalesPie;
import mx.org.kaana.kajool.procesos.utilerias.graficasperfiles.beans.Xaxis;
import mx.org.kaana.kajool.procesos.utilerias.graficasperfiles.beans.Yaxis;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.pagina.JsfBase;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 17/10/2016
 *@time 09:51:48 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class BuildChart implements Serializable{

  private static final long serialVersionUID = -7923640837107557494L;
  private Long idPerfil;
  private Long idGrupo;

  public BuildChart(Long idPerfil, Long idGrupo) {
    this.idPerfil = idPerfil;
    this.idGrupo = idGrupo;
  } // BuildChart

  public List<Highcharts> build() throws Exception{
    return build(ETipoGrafica.values()[(int)(Math.random()*3)]);
  } // build
  
  public Highcharts buildNacional() throws Exception{    
    return loadCharPropertiesNacional("Avance estatal de captura", "Captura", "porcentaje", "Entidades", "entidad", "VistaIndicadoresPerfilesDto", "avanceEstatal", "", ETipoGrafica.values()[(int)(Math.random()*2)]);
  } // build
  
  public List<Highcharts> build(ETipoGrafica tipoChart) throws Exception{
    List<Highcharts> regresar                    = null;
    List<TrJanalIndicadoresPerfilDto> registrados= null;
    try {
      regresar= new ArrayList<>();
      registrados= toIndicadoresRegistradosPerfil();
      for(TrJanalIndicadoresPerfilDto indicador: registrados){
        regresar.add(loadCharProperties(indicador, tipoChart));
      } // for
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  } // build

  private List<TrJanalIndicadoresPerfilDto> toIndicadoresRegistradosPerfil() throws Exception{
    List<TrJanalIndicadoresPerfilDto> regresar= null;
    Map<String, Object>params                 = null;
    try {
      params= new HashMap<>();
      params.put(Constantes.SQL_CONDICION, "id_perfil=".concat(this.idPerfil.toString()));
      regresar= DaoFactory.getInstance().findViewCriteria(TrJanalIndicadoresPerfilDto.class, params, Constantes.SQL_TODOS_REGISTROS);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    finally{
      Methods.clean(params);
    } // finally
    return regresar;
  } // toIndicadoresRegistradosPerfil

  private Highcharts loadCharProperties(TrJanalIndicadoresPerfilDto indicador, ETipoGrafica tipoChart) throws Exception{
    Highcharts regresar        = null;
    List<Entity> records       = null;
    List<String> categoriesList= null;
    String[] categories        = null;
    List<Long> dataList        = null;
    Long[] data                = null;
    Totales[] totales          = null;
    try {
      dataList= new ArrayList<>();
      categoriesList= new ArrayList<>();
      records= loadRecords(indicador.getVista(), indicador.getIdVista());
      for(Entity record: records)
        categoriesList.add(record.toString(indicador.getAliasLadoy()));
      for(Entity record: records)
        dataList.add(record.toLong(indicador.getAliasLadox()));
      categories= new String[categoriesList.size()];
      categories= categoriesList.toArray(categories);
      data= new Long[dataList.size()];
      data= dataList.toArray(data);
      totales= new Totales[]{new Totales(indicador.getTituloLadox(), data)};
      regresar= new Highcharts(
              new Chart(tipoChart.getName()),
              new Title(indicador.getTituloGeneral()),
              new Xaxis(categories, new Title(indicador.getTituloLadoy())),
              new Yaxis(0, new Title(indicador.getTituloLadox()), new Labels("justify")),
              new Tooltip("  ".concat(indicador.getDescripcionConteo()), indicador.getAliasLadox().equals("porcentaje") ? "{point.name}</br><b>{point.y:.2f}%</b>" : ""),
              new PlotOptions(new Bar(new DataLabels(false)), new Series(new DataLabels(true, indicador.getAliasLadox().equals("porcentaje") ? "{point.y:.1f}%" : ""))),
              new Legend("vertical", "right", "top", 0, 40, true, 1, "#FFFFFF", true),
              new Credits(false),
              totales);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  } // loadCharProperties
  
  private Highcharts loadCharPropertiesNacional(String tituloGeneral, String tituloLadox, String aliasLadox, String tituloLadoy, String aliasLadoy, String vista, String idVista, String descripcion, ETipoGrafica tipoChart) throws Exception{
    Highcharts regresar        = null;
    List<Entity> records       = null;
    List<String> categoriesList= null;
    String[] categories        = null;
    List<Long> dataList        = null;
    Long[] data                = null;
    Totales[] totales          = null;
    try {
      dataList= new ArrayList<>();
      categoriesList= new ArrayList<>();
      records= loadRecords(vista, idVista);
      for(Entity record: records)
        categoriesList.add(record.toString(aliasLadoy));
      for(Entity record: records)
        dataList.add(record.toLong(aliasLadox));
      categories= new String[categoriesList.size()];
      categories= categoriesList.toArray(categories);
      data= new Long[dataList.size()];
      data= dataList.toArray(data);
      totales= new Totales[]{new Totales(tituloLadox, data)};
      regresar= new Highcharts(
              new Chart(tipoChart.getName()),
              new Title(tituloGeneral),
              new Xaxis(categories, new Title(tituloLadoy)),
              new Yaxis(0, new Title(tituloLadox), new Labels("justify")),
              new Tooltip("  ".concat(descripcion), aliasLadox.equals("porcentaje") ? "{point.name}</br><b>{point.y:.2f}%</b>" : ""),
              new PlotOptions(new Bar(new DataLabels(false)), new Series(new DataLabels(true, aliasLadox.equals("porcentaje") ? "{point.y:.1f}%" : ""))),
              new Legend("vertical", "right", "top", 0, 40, true, 1, "#FFFFFF", true),
              new Credits(false),
              totales);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  } // loadCharProperties

  private List<Entity> loadRecords(String vista, String idXml) throws Exception{
    List<Entity> regresar    = null;
    Map<String, Object>params= null;
    try {
      params= new HashMap<>();
      params.put("idGrupo", this.idGrupo);
      params.put("idEntidad", JsfBase.getAutentifica().getEmpleado().getIdEntidad());
      params.put("idUsuario", JsfBase.getIdUsuario());
      regresar= DaoFactory.getInstance().toEntitySet(vista, idXml, params, Constantes.SQL_TODOS_REGISTROS);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    finally{
      Methods.clean(params);
    } // finally
    return regresar;
  } // loadRecords
  
  public HighchartsPie buildPie() throws Exception{
    return buildPie(EPerfiles.fromOrdinal(this.idPerfil));
  } // buildPie
  
  public HighchartsPie buildPie(EPerfiles perfil) throws Exception{
    HighchartsPie regresar= null;
    try {
      switch(perfil){
        case ADMINISTRADOR_ENCUESTA:
        case ADMINISTRADOR:
        case CONSULTOR:
          regresar= loadCharPropertiesPie("nacional", "", "Con captura", "Sin captura", "capturaCompleta", "sinCaptura", "viviendas");
          break;
        case RESPONSABLE_ESTATAL:
        case CAPTURISTA:        
          regresar= loadCharPropertiesPie("estatal", "", "Con captura", "Sin captura", "capturaCompleta", "sinCaptura", "viviendas");
          break;
      } // switch
    } // try
    catch (Exception e) {      
      throw e;
    } // catch    
    return regresar;
  } // buildPie
  
  private HighchartsPie loadCharPropertiesPie(String idVista, String titulo, String x, String y, String campoX, String campoY, String tipoConteo) throws Exception{
    return loadCharPropertiesPie("VistaIndicadoresPerfilesDto", idVista, titulo, x, y, campoX, campoY, tipoConteo);
  } // loadCharPropertiesPie
  
  private HighchartsPie loadCharPropertiesPie(String vista, String idVista, String titulo, String x, String y, String campoX, String campoY, String tipoConteo) throws Exception{
    HighchartsPie regresar     = null;
    List<Entity> records       = null;
    List<String> categoriesList= null;    
    List<DetailData> dataList  = null;
    DetailData[] data          = null;
    TotalesPie[] totales       = null;
    try {
      dataList= new ArrayList<>();
      categoriesList= new ArrayList<>();
      records= loadRecords(vista, idVista);      
      categoriesList.add("Total");
      for(Entity record: records){
        dataList.add(new DetailData(x.concat(": ").concat(record.toString(Cadena.toBeanName("por_".concat(Cadena.toSqlName(campoX))))).concat("%"), record.toLong(campoX)));
        dataList.add(new DetailData(y.concat(": ").concat(record.toString(Cadena.toBeanName("por_".concat(Cadena.toSqlName(campoY))))).concat("%"), record.toLong(campoY)));
      } // for            
      data= new DetailData[dataList.size()];
      data= dataList.toArray(data);
      totales= new TotalesPie[]{new TotalesPie("Total", data)};
      regresar= new HighchartsPie(              
              new Chart(ETipoGrafica.PIE.getName(), "250"),
              new Title(titulo),              
              new Tooltip("  ".concat(tipoConteo)),
              new PlotOptions(new Pie(true, "pointer", new DataLabels(false), true)),              
              totales);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  } // loadCharProperties
}
