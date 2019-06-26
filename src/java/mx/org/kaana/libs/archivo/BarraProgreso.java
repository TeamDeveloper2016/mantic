package mx.org.kaana.libs.archivo;

import java.io.PrintWriter;

import java.io.Writer;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.progreso.ProgressMonitor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class BarraProgreso {

  private boolean debug       = false;
  
  // Estas variables son para colocarle la barra de progreso al reporte
  private int currentRecord   = 0;
  private int recordCount     = 1;
  private boolean htmlProgress= false;
  private boolean showProgress= false;

  private Collection coleccion = null;
  private int porcentaje       = 0;
  private int avancePorcetaje  = 0;
  private PrintWriter printer  = null;
  private String nombreFuncion;
  
  private static final Log LOG= LogFactory.getLog(BarraProgreso.class);

  public BarraProgreso() {
    this(false);
  }

  public BarraProgreso(boolean debug) {
    this(null, debug);
  }

  public BarraProgreso(PrintWriter printer, boolean debug) {
    this(printer, debug, null);
  }
  
  public BarraProgreso(PrintWriter printer, boolean debug, String nombreFuncion) {
    setPrinter(printer);
    setDebug(debug);
    setNombreFuncion(nombreFuncion);
  }

  protected void progreso(int currentRecord) {
    setCurrentRecord(currentRecord);
    if (isShowProgress())
      LOG.debug(" Registro: " + getCurrentRecord() + " de " + getRecordCount() + " porcentaje: " + ((getCurrentRecord() * 100/ getRecordCount()) + "%"));
    if (isHtmlProgress())
      avance();
  } // progress

  protected void avance() {
    if (debug)
      LOG.debug("avance");
    // Determinar los avances de las barras de progreso
    avancePorcetaje = (int)(getCurrentRecord() * 100 / getRecordCount());
    if (porcentaje != avancePorcetaje) {
      Iterator iterator = coleccion.iterator();
      while (iterator.hasNext()) {
        ProgressMonitor progressmonitor = (ProgressMonitor)iterator.next();
        progressmonitor.setMessage(" Registro [" + getCurrentRecord() + " de " + getRecordCount() + "]");
        progressmonitor.setProgress(getCurrentRecord());
        progressmonitor.printProgress();
      }// while
      porcentaje = avancePorcetaje;
      getPrinter().flush();
    }// if
    if (avancePorcetaje >= 100) {
      //getPrinter().println("<br><br>");
      getPrinter().flush();
    }// if
  } // setRegistroActual

  private void insertarBarras() {
    String function = "actualizarProgreso";
    if (!Cadena.isVacio(getNombreFuncion()))
      function = getNombreFuncion();
    if (debug)
      LOG.debug("insertarBarras");
    coleccion = new Vector();
    // Prepare the progress monitors...
    ProgressMonitor statusMonitor = new ProgressMonitor();
    statusMonitor.setPrinter(getPrinter());    
    statusMonitor.setFormat("<script> window.status = \"( {1}% ) Procesado : {0} ... \";".concat(function).concat("({1});</script>"));
    addProgressMonitor(statusMonitor);
  } // insertarBarras

  private void actualizarBarras() {
    String function = "actualizarProgreso";
    if (!Cadena.isVacio(getNombreFuncion()))
      function = getNombreFuncion();
      
    if (debug)
      LOG.debug("actualizarBarras");
    Iterator iterator = coleccion.iterator();
    int contador = 0;
    while (iterator.hasNext()) {
      ProgressMonitor progressmonitor = (ProgressMonitor)iterator.next();
      progressmonitor.setBarra();
      progressmonitor.setMin(0);
      progressmonitor.setMax(recordCount);
      switch (contador) {
      case 0: // for the status bar of the web browser.        
          progressmonitor.setFormat("<script> window.status = \"( {1}% ) Procesado : {0} ... \";".concat(function).concat("({1});</script>"));
        break;
      }// switch
      contador++;
    }// while
  } // actualizarBarras

  private void agrearBarrasProgreso() {
    if (debug)
      LOG.debug("agrearBarrasProgreso");
    // Crear arreglo de barras de progreso
    if (coleccion != null)
      actualizarBarras();
    else
      insertarBarras();
  }

  private void addProgressMonitor(ProgressMonitor progressmonitor) {
    if (debug)
      LOG.debug("addProgressMonitor");
    progressmonitor.setMin(0);
    progressmonitor.setMax(recordCount);
    coleccion.add(progressmonitor);
  }

  public void setDebug(boolean debug) {
    this.debug = debug;
  }

  public boolean isDebug() {
    return debug;
  }

  private void setShowProgress(boolean showProgress) {
    this.showProgress = showProgress;
  }

  private boolean isShowProgress() {
    return showProgress;
  }

  private void setHtmlProgress(boolean htmlProgress) {
    this.htmlProgress = htmlProgress;
  }

  private boolean isHtmlProgress() {
    return htmlProgress;
  }

  private void setCurrentRecord(int currentRecord) {
    this.currentRecord = currentRecord;
  }

  private int getCurrentRecord() {
    return currentRecord;
  }
  
  public void setPrinter(Writer printer) {
    if(printer!= null)
      this.printer = new PrintWriter(printer);
  } // setPrinter

  private PrintWriter getPrinter() {
    return printer;
  } // getPrinter

  private void setRecordCount(int recordCount) {
    this.recordCount = recordCount;
  }
  
  private int getRecordCount() {
    return recordCount;
  }
  
  public void setNombreFuncion(String nombreFuncion) {
    this.nombreFuncion = nombreFuncion;
  }

  public String getNombreFuncion() {
    return nombreFuncion;
  }
   
  public void monitoreo(int recordCount, Writer printer) {
    setRecordCount(recordCount);
    setPrinter(printer);
    setShowProgress(getRecordCount()> 0);
    setHtmlProgress(getPrinter()!= null);
    if(isShowProgress() && isHtmlProgress())
      agrearBarrasProgreso();
  }

  public void monitoreo(int recordCount) {
   monitoreo(recordCount, getPrinter());
  }

}
