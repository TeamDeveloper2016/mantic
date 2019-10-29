package mx.org.kaana.kajool.procesos.utilerias.graficasperfiles.reglas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
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
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.mantic.enums.EEstatusVentas;
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
	private String condicionGeneral;

  public BuildChart() {
		this(Constantes.SQL_VERDADERO);
	} // BuildChart
	
  public BuildChart(String condicionGeneral) {
		this(-1L, -1L, condicionGeneral);
	} // BuildChart
	
  public BuildChart(Long idPerfil, Long idGrupo, String condicionGeneral) {
    this.idPerfil        = idPerfil;
    this.idGrupo         = idGrupo;
		this.vistaGeneral    = EGraficasTablero.getVista();
		this.condicionGeneral= condicionGeneral;
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
  public HighchartsPie buildArticulosMasVendidos() throws Exception{ 
		return loadCharPropertiesPie(EGraficasTablero.ART_MAS_VENDIDOS);
	} // buildCuentasPagar  
  
	public HighchartsPie buildArticulosMasUtilidad() throws Exception{ 
		return loadCharPropertiesPie(EGraficasTablero.ART_MAS_UTILIDAD);
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
      records= loadRecords(this.vistaGeneral, grafica.getIdVista(), grafica.getRecords(), grafica);      
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

  private List<Entity> loadRecords(String vista, String idXml, Long records, EGraficasTablero grafica) throws Exception {
    List<Entity> regresar    = null;
    Map<String, Object>params= null;
    try {
      params= new HashMap<>();
      params.put("idGrupo", this.idGrupo);
      params.put("idUsuario", JsfBase.getIdUsuario());
      params.put("condicionGeneral", toFormatCondicionGeneral(grafica));
      params.put("estatusVentas", EEstatusVentas.CREDITO.getIdEstatusVenta()+","+EEstatusVentas.PAGADA.getIdEstatusVenta()+","+EEstatusVentas.TERMINADA.getIdEstatusVenta()+","+EEstatusVentas.TIMBRADA.getIdEstatusVenta());
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
	
	private String toFormatCondicionGeneral(EGraficasTablero grafica){
		return toFormatCondicionGeneral(grafica, this.condicionGeneral);
	} // toFormatCondicionGeneral

	public String toFormatCondicionGeneral(EGraficasTablero grafica, String condicion){
		String regresar= Constantes.SQL_VERDADERO;
		try {
			if(condicion.matches("(.*)registro(.*)"))
				regresar= condicion.replaceAll("registro", grafica.getTablaPivote().concat(".").concat("registro"));			
		} // try
		catch (Exception e) {			
			throw e;
		} // catch
		return regresar;		
	} // toFormatCondicionGeneral
		
	public HighchartsPie loadCharPropertiesPie(EGraficasTablero grafica) throws Exception{
    HighchartsPie regresar     = null;
    List<Entity> records       = null;
    List<String> categoriesList= null;    
    List<DetailData> dataList  = null;
    DetailData[] data          = null;
    TotalesPie[] totales       = null;
		String nombre              = null;
    try {
      dataList= new ArrayList<>();
      categoriesList= new ArrayList<>();
      records= loadRecords(this.vistaGeneral, grafica.getIdVista(), grafica.getRecords(), grafica);      
      categoriesList.add(grafica.getTituloLadoy());
			if(!records.isEmpty()) {
				for(Entity record: records){ 					
					nombre= toFormatName(Cadena.eliminaCaracter(record.toString(grafica.getAliasLadoy()), '"'));
					dataList.add(new DetailData(nombre.concat("<br>").concat(record.toString("porcentaje")).concat("%"), record.toLong(grafica.getAliasLadox())));									
				} // 
			} // if			
			data= new DetailData[dataList.size()];
			data= dataList.toArray(data);
			totales= new TotalesPie[]{new TotalesPie(grafica.getTituloLadox(), data)};
			regresar= new HighchartsPie(              
							new Chart(grafica.getTipoChart().getName(), grafica.getHeight()),							            
							new Title(" "),              
							new Tooltip(" "),
							new PlotOptions(new Pie(true, "pointer", new DataLabels(true), false)),              
							totales);													
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  } // loadCharProperties
	
	private String toFormatName(String name){
		String regresar= "";
		int count      = 0;
		int countChart = 0;
		Long countEsp  = 0L;
		Double espacios= 0D;
		Long result    = 0L;
		try {
			for(count=0; count<name.length(); count++){
				if(name.charAt(count)== ' ')
					espacios= espacios + 1;
			} // for
			if(espacios > 0)
				result= Math.round(espacios/2);
			else
				regresar= name;
			if(result>0){
				for(countChart=0; countChart<name.length(); countChart++){
					if(name.charAt(countChart)== ' ')
						countEsp= countEsp + 1;
					if(countEsp.equals(result)){
						regresar= regresar.concat("<br>");
						countEsp= countEsp + 1;
					} // if
					else
						regresar= regresar.concat(Character.toString(name.charAt(countChart)));
				} // for
			}
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // 
}
