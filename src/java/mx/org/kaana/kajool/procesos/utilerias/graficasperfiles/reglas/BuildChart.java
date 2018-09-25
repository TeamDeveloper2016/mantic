package mx.org.kaana.kajool.procesos.utilerias.graficasperfiles.reglas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
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
import mx.org.kaana.mantic.enums.EGraficasTablero;

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
	private String vistaGeneral;

  public BuildChart() {
		this(-1L, -1L);
	} // BuildChart
	
  public BuildChart(Long idPerfil, Long idGrupo) {
    this.idPerfil    = idPerfil;
    this.idGrupo     = idGrupo;
		this.vistaGeneral= EGraficasTablero.getVista();
  } // BuildChart  
	
  public Highcharts buildUtilidadSucursal() throws Exception{ 
		return loadGeneralChar(EGraficasTablero.UTILIDAD_SUCURSAL);
	} //buildUtilidadSucursal  
  
	public Highcharts buildUtilidadCaja() throws Exception{ 
		return loadGeneralChar(EGraficasTablero.UTILIDAD_CAJA);
	} // buildUtilidadCaja  
	
  public Highcharts buildCuentasCobrar() throws Exception{ 
		return loadGeneralChar(EGraficasTablero.CUENTAS_COBRAR);
	} // buildCuentasCobrar  
  
  public Highcharts buildCuentasPagar() throws Exception{ 
		return loadGeneralChar(EGraficasTablero.CUENTAS_PAGAR);
	} // buildCuentasPagar  
  
  public List<Highcharts> build() throws Exception{
    List<Highcharts> regresar= null;
    try {
      regresar= new ArrayList<>();      	
			for(EGraficasTablero grafica: EGraficasTablero.values()){
				if(grafica.equals(EGraficasTablero.VENTAS_SUCURSAL))
					regresar.add(loadGeneralChar(grafica));								
			} // for
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  } // build  
  
  private Highcharts loadGeneralChar(EGraficasTablero grafica) throws Exception{
    Highcharts regresar        = null;
    List<Entity> records       = null;
    List<String> categoriesList= null;
    String[] categories        = null;
    List<Long> dataList        = null;
    Totales[] totales          = null;
		List<Totales> totalesList  = null;
		Integer count              = 0;
    try {
			totalesList= new ArrayList<>();
      categoriesList= new ArrayList<>();
      records= loadRecords(this.vistaGeneral, grafica.getIdVista(), grafica.getRecords());      
			if(!records.isEmpty()){				
				for(Entity record: records)
					categoriesList.add(record.toString(grafica.getAliasLadoy()));
				for(count=0; count< grafica.getNumColumnas(); count++){
					dataList= new ArrayList<>();
					for(Entity record: records)
						dataList.add(record.toLong(grafica.getAliasLadox().split("~")[count]));
					totalesList.add(new Totales(Cadena.letraCapital(grafica.getAliasLadox().split("~")[count]), dataList.toArray(new Long[dataList.size()])));
				}				
			} // if
      categories= new String[categoriesList.size()];
      categories= categoriesList.toArray(categories);      
      totales= totalesList.toArray(new Totales[totalesList.size()]);
      regresar= new Highcharts(
              new Chart(grafica.getTipoChart().getName(), grafica.getHeight()),
              new Title(grafica.getTituloGeneral()),
              new Xaxis(categories, new Title(grafica.getTituloLadoy())),
              new Yaxis(0, new Title(grafica.getTituloLadox()), new Labels("justify")),
              new Tooltip("  ".concat(grafica.getDescripcion()), grafica.getAliasLadox().equals("porcentaje") ? "{point.name}</br><b>{point.y:.2f}%</b>" : ""),
              new PlotOptions(new Bar(new DataLabels(false)), new Series(new DataLabels(true, grafica.getAliasLadox().equals("porcentaje") ? "{point.y:.1f}%" : ""))),
              new Legend("vertical", "left", "middle", -10, grafica.getSizeY(), true, 1, "#FFFFFF", true),
              new Credits(false),
              totales);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  } // loadGeneralChar  

  private List<Entity> loadRecords(String vista, String idXml, Long records) throws Exception {
    List<Entity> regresar    = null;
    Map<String, Object>params= null;
    try {
      params= new HashMap<>();
      params.put("idGrupo", this.idGrupo);
      params.put("idUsuario", JsfBase.getIdUsuario());
      regresar= DaoFactory.getInstance().toEntitySet(vista, idXml, params, records);
    } // try
    catch (Exception e) {
      mx.org.kaana.libs.formato.Error.mensaje(e);
			regresar= new ArrayList<>();
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
  
  public HighchartsPie loadCharPropertiesPie(String vista, String idVista, String titulo, String x, String y, String campoX, String campoY, String tipoConteo) throws Exception{
		return loadCharPropertiesPie(vista, idVista, titulo, x, y, campoX, campoY, tipoConteo, false);
	}
  
	public HighchartsPie loadCharPropertiesPie(String vista, String idVista, String titulo, String x, String y, String campoX, String campoY, String tipoConteo, boolean defaultLoad) throws Exception{
    HighchartsPie regresar     = null;
    List<Entity> records       = null;
    List<String> categoriesList= null;    
    List<DetailData> dataList  = null;
    DetailData[] data          = null;
    TotalesPie[] totales       = null;
    try {
      dataList= new ArrayList<>();
      categoriesList= new ArrayList<>();
      //records= loadRecords(vista, idVista);      
      records= new ArrayList<>();
      categoriesList.add("Total");
			if(!records.isEmpty()) {
				for(Entity record: records) {
					dataList.add(new DetailData(x.concat(": ").concat(record.toString(Cadena.toBeanName("por_".concat(Cadena.toSqlName(campoX))))).concat("%"), record.toLong(campoX)));
					dataList.add(new DetailData(y.concat(": ").concat(record.toString(Cadena.toBeanName("por_".concat(Cadena.toSqlName(campoY))))).concat("%"), record.toLong(campoY)));        
				} // for
			} // if
			else {				
				if(defaultLoad){
					dataList.add(new DetailData("ACEITE P/COMPRESOR<br> 250 ML GONI".concat(": ").concat(Cadena.toBeanName("".concat(Cadena.toSqlName("15")))).concat("%"), 30L));
					dataList.add(new DetailData("ACEITE LUBRI. <br>100 ML. SELANUSA".concat(": ").concat(Cadena.toBeanName("".concat(Cadena.toSqlName("5")))).concat("%"), 10L));
					dataList.add(new DetailData("AEROGRAFO <br>ADIR #684".concat(": ").concat(Cadena.toBeanName("".concat(Cadena.toSqlName("50")))).concat("%"), 70L));
					dataList.add(new DetailData("AGUJA ARREA #6".concat(": ").concat(Cadena.toBeanName("".concat(Cadena.toSqlName("10")))).concat("%"), 25L));
					dataList.add(new DetailData("ARCO P/SEGUETA<br> FOY 17E".concat(": ").concat(Cadena.toBeanName("".concat(Cadena.toSqlName("20")))).concat("%"), 45L));
				} // if
				else{
					dataList.add(new DetailData("ATOMIZADOR <br>1 LT ECONOMICO".concat(": ").concat(Cadena.toBeanName("".concat(Cadena.toSqlName("15")))).concat("%"), 30L));
					dataList.add(new DetailData("AZADON C/MANGO<br> TRUPER AL-1M #1".concat(": ").concat(Cadena.toBeanName("".concat(Cadena.toSqlName("30")))).concat("%"), 55L));
					dataList.add(new DetailData("BANDOLA C/SEGURO<br> TRUE POWER PZA.".concat(": ").concat(Cadena.toBeanName("".concat(Cadena.toSqlName("15")))).concat("%"), 21L));
					dataList.add(new DetailData("BANQUETERO GALV.".concat(": ").concat(Cadena.toBeanName("".concat(Cadena.toSqlName("10")))).concat("%"), 23L));
					dataList.add(new DetailData("BARRETA ECONOMICA<br> 1 X 1.8 MTS".concat(": ").concat(Cadena.toBeanName("".concat(Cadena.toSqlName("30")))).concat("%"), 54L));
				} // else
			} // else
			data= new DetailData[dataList.size()];
			data= dataList.toArray(data);
			totales= new TotalesPie[]{new TotalesPie("Total", data)};
			regresar= new HighchartsPie(              
							new Chart(ETipoGrafica.PIE.getName(), "313"),
							//new Title(titulo),              
							new Title(" "),              
							new Tooltip("  ".concat(tipoConteo)),
							new PlotOptions(new Pie(true, "pointer", new DataLabels(true), false)),              
								totales);													
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  } // loadCharProperties
}
