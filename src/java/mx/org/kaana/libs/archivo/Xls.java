package mx.org.kaana.libs.archivo;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import jxl.Workbook;
import jxl.write.Label;
import mx.org.kaana.kajool.catalogos.backing.Monitoreo;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.page.LinkPage;
import mx.org.kaana.kajool.db.comun.page.PageRecords;
import mx.org.kaana.kajool.procesos.reportes.beans.Modelo;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.JsfBase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class Xls extends XlsBase {

	private static final long serialVersionUID = 7676537739920483322L;
	protected List<IBaseDto> registros;
	private Modelo definicion;  
  private int totalColumnas;
  private boolean algunos;
  protected String campos;
	private String nombreArchivo;
  private Monitoreo monitoreo;
	private static final Log LOG= LogFactory.getLog(Xls.class);
  
  public Xls(String nombreArchivo, Modelo definicion, String campos) {    
		this.nombreArchivo= nombreArchivo;
		this.definicion= definicion;
		this.campos= campos;  
		init();
  }
  
  public Xls(String archivo, Modelo definicion) {
    this(archivo, definicion, null);    
  }
  
	private void init(){
		try {      
      setPosicionColumna(0);
      setPosicionFila(0);
      setIndiceColumna(0);			                 
      algunos= campos!= null;
      this.monitoreo= JsfBase.getAutentifica().getMonitoreo();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
	}
	
  public void setAlgunos(boolean algunos){
    this.algunos= algunos;
  }
  
  public boolean isAlgunos() {
    return algunos;
  }

	public Modelo getDefinicion() {
		return definicion;
	}

	public void setCampos(String campos){
    this.campos= campos;
  }
  
  public String getCampos() {
		return campos;
	}
	
	protected void setTotalColumnas() {
    try {      
      if (!isAlgunos()) {        
        this.totalColumnas= this.registros.get(0).toMap().keySet().size();        
      } // if
      else 
        this.totalColumnas= this.campos.split(",").length;     
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    }// catch
  }

  public int getTotalColumnas() {
    return totalColumnas;
  }

	public String getNombreArchivo() {
		return nombreArchivo;
	}
  	
  protected Map convertirHashMap(IBaseDto registro) throws Exception {
    Map columna             = null;    
    Map registroMap         = null;
    String [] nombreColumnas= null;
    try  {            
      registroMap= registro.toMap();            
      nombreColumnas= this.campos.split(",");
      columna= new HashMap();
      for (int i=0; i< getColumnasInformacion(); i++) {        
        columna.put("col"+String.valueOf(i), registroMap.get(Cadena.toBeanName(nombreColumnas[i])));
      } // for  
    } // try 
    catch (Exception e)  {
      Error.mensaje(e);
    } // catch
    return columna;    
  }
  	
	@Override
  protected String getNombresColumnas() {
    StringBuffer columnas	= new StringBuffer();    
    String regresar				= null;    
		Set<String> fields		= null;		
    try {               
			fields= this.registros.get(0).toMap().keySet();
			for(String field : fields) {
				columnas.append(field);
				columnas.append(",");
			} // for x    
			columnas = columnas.delete(columnas.length() -1, columnas.length());
			regresar= columnas.toString();      
    }
    catch (Exception e) {
      Error.mensaje(e);
    } // try
    return regresar.toUpperCase();
  }

	@Override
  protected int getColumnasInformacion() {
    return getTotalColumnas();
  }

	private void detail(List<IBaseDto> registros) throws Exception {
		Map columna    = null;
		int fila       = 1;
		try {						
			for (IBaseDto registro: registros) {
        columna= convertirHashMap(registro);
        for (int x= 0; x < columna.size(); x++) {
          LOG.info("[generarRegistros]  registro: " + fila + " celda: " + columna.get("col"+String.valueOf(x)));
          Label label= new Label(getPosicionColumna() + x , getPosicionFila()+ fila, columna.get("col"+String.valueOf(x))==null?"":columna.get("col"+String.valueOf(x)).toString());         
          hoja.addCell(label);
        } // for x
        columna.clear();
        columna= null;
        fila++;
        this.monitoreo.incrementar();
        LOG.info("Registro: "+ fila+ " procesado. !");       
      } // for
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			throw e;
		} // catch
	}
	
	@Override
  public boolean generarRegistros(boolean titulo) throws Exception {
    boolean termino	= true; 			
		int top					= new Long(Constantes.SQL_TODOS_REGISTROS).intValue();
    try {			
			PageRecords pages= DaoFactory.getInstance().toEntityPage(getDefinicion().getProceso(), getDefinicion().getIdXml(), getDefinicion().getParams(), 0, top);
			if ((pages!= null) && (!pages.getList().isEmpty())) {				
        this.monitoreo.comenzar(DaoFactory.getInstance().toSize(getDefinicion().getProceso(), getDefinicion().getIdXml(), getDefinicion().getParams()));
				libro= Workbook.createWorkbook(new File(this.nombreArchivo));
				hoja = libro.createSheet("MANTIC", 0);
				this.registros= pages.getList();
				if (!isAlgunos())
					this.campos= getNombresColumnas();		
				if (titulo)
					procesarEncabezado(this.campos);   
				setTotalColumnas();
				pages.calculate(true);
				List<LinkPage> list= pages.getPages();								
				detail((List)pages.getList());
				for(LinkPage page: list) {
					PageRecords values= DaoFactory.getInstance().toEntityPage(getDefinicion().getProceso(), getDefinicion().getIdXml(), getDefinicion().getParams(), (int)(page.getIndex()* top), top);
					detail((List)values.getList());
				} // for				
			} // if
    } // try
    catch (Exception e) {
			termino= false;
      Error.mensaje(e);
    } // catch
		finally {
  	  getLibro().write();
      getLibro().close();
    } // finally
    return termino;
  }
 
  public boolean procesar() throws Exception {        
    return generarRegistros(true);
  } 

  public boolean procesar(int posColumna, int posFila, boolean colocarTitulo) throws Exception {    
    setPosicionFila(posColumna);
    setPosicionColumna(posFila);    
    return generarRegistros(colocarTitulo);
  } 
  
  public static void main(String[] args) {
    Xls xls				= null;    
    Map parametros= null;
    Modelo modelo	= null;
    try {      
      parametros= new HashMap();
      parametros.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);      
			modelo= new Modelo(parametros, "TcManticArticulosDto", "row");
      xls= new Xls("E:\\pruebaExcel.xls", modelo);      
      xls.procesar();      
    }
    catch(Exception e) {
      Error.mensaje(e);
    } // try
  } // main

}