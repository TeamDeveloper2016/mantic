package mx.org.kaana.libs.reportes;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRReport;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @company Instituto Nacional de Estadistica y Geografia
 * @project KAJOOL (Control system polls)
 * @date 22/05/2014
 * @time 12:54:51 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public class RebuildJasper {
  private static final Log LOG = LogFactory.getLog(RebuildJasper.class);

  private static final String JASPER_COMPILE= ".jasper";
  private static final String JASPER_SOURCE = ".jrxml";
  private static final String JASPER_FILL   = ".jrprint";
  private static final String JASPER_OLD    = ".janal";
  private static final String JASPER_PDF    = ".pdf";

  private List<String> sources;

  public RebuildJasper(String report) {
    this.sources= new ArrayList<>();
    this.sources.add(report);
  }

  public RebuildJasper(String[] projects) throws JRException {
    this.sources= new ArrayList<>();
    load(projects);
  }

  private void load(String[] projects) throws JRException {
    FileSearch fileSearch = new FileSearch();
    for (String project: projects) {
      fileSearch.searchDirectory(new File(project), RebuildJasper.JASPER_COMPILE);
      int count= fileSearch.getResult().size();
      if (count== 0)
        LOG.warn("\nNo se encontraron resultados !");
      else {
        LOG.info("\nEncontrado "+ count+ " resultado(s)!\n");
        this.sources.addAll(fileSearch.getResult());
      } // else
    } // for
  }

  public void execute() throws JRException {
    for (String source: sources) {
      LOG.info("Encontrado: "+ source);
      // sacar el nombre del reporte sin la extension
      String report= source.substring(0, source.lastIndexOf("."));
      File old     = new File(source);
      // cargar el reporte (.jasper) con las librerias del jasper
      JasperReport jasper= (JasperReport) JRLoader.loadObject(old);
      Map<String, Object> params= new HashMap<>();
      params.put(JRParameter.REPORT_CLASS_LOADER, jasper.getClass().getClassLoader());

      // cargar el reporte con los datos (.jrprint) partiendo del reporte compilado
      // JasperFillManager.fillReportToFile(jasper, report.concat(JASPER_FILL), params);
      // generar el reporte de salida en formato .pdf
      // JasperExportManager.exportReportToPdfFile(report.concat(JASPER_FILL), report.concat(JASPER_PDF));

      LOG.info("Procesando: ".concat(report.concat(JASPER_SOURCE)));
      // generar el reporte fuente (.jrxml) dejandolo en la misma ruta
      JasperCompileManager.writeReportToXmlFile((JRReport)jasper, report.concat(JASPER_SOURCE));
      // renombrar el reporte .jasper por uno lamado .janal
      LOG.info("Renombrando: ".concat(report.concat(JASPER_OLD)));
      old.renameTo(new File(report.concat(JASPER_OLD)));
      // invocar el metodo que da la ruta el archivo ya generado para que pueda ser modificado
      updateSource(report.concat(JASPER_SOURCE));
      // cargar el reporte fuente (.jrxml) con las librerias del jasperreport
      JasperDesign jrxml= (JasperDesign) JRXmlLoader.load(new File(report.concat(JASPER_SOURCE)));
      // compilar de nueva cuenta el reporte (.jrxml) y lo deja en la misma ruta
      JasperCompileManager.compileReportToFile((JasperDesign)jrxml, source);
      LOG.info("Recompilado: ".concat(source));
    } // for
  }

  @Override
  protected void finalize() throws Throwable {
    if(this.sources!= null)
      this.sources.clear();
    super.finalize();
  }

  public void updateSource(String jrXml) {
    LOG.info("updateSource: ".concat(jrXml));
  }

  @SuppressWarnings("empty-statement")
  public static void main(String[] args) throws JRException, FileNotFoundException {
    RebuildJasper rebuild= new RebuildJasper("D:/Plataforma/Netbeans/JasperReport/src/kajool/Paginas/Explotacion/Operativos/Reportes/enhRep1Subreporte1.jasper");
    String[] projects= {
      "D:/Plataforma/Netbeans/Kajool/src/java",
      "D:/Plataforma/Netbeans/Kajool/JANAL/src",
    };
//    RebuildJasper rebuild= new RebuildJasper(projects);
    rebuild.execute();
  }

}
