package mx.org.kaana.libs.reportes.scriptlets;

import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Map;
import mx.org.kaana.kajool.catalogos.IFormatos;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.enums.EFormatos;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.ETareas;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 9/09/2015
 * @time 04:53:19 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public class Reporte {

	private static final Log LOG=LogFactory.getLog(Reporte.class);
	private Map parameters;
  private String source ;
  private String target;
  private String fileName;
	private JRDataSource jRDataSource;
	private Boolean pagination;
	private Boolean previsualizar;
  protected Boolean pdfConSeguridad;

	public Reporte(String source, String target, Map parameters, String fileName, JRDataSource jRDataSource, Boolean pagination, Boolean previsualizar, Boolean pdfConSeguridad) {
    this.source    = source;
		this.target    = target;
		this.parameters= parameters;
    this.fileName  = fileName;
		this.jRDataSource= jRDataSource;
		this.pagination = pagination;
		this.previsualizar = previsualizar;
    this.pdfConSeguridad= pdfConSeguridad;
  }
  
	public Reporte(String source, String target, Map parameters, String fileName, JRDataSource jRDataSource, Boolean pagination, Boolean previsualizar) {
    this(source, target, parameters, fileName, jRDataSource, pagination, previsualizar, false);
  }
	
  public Reporte(String source, String target, Map parameters, String fileName, JRDataSource jRDataSource) {
    this(source, target, parameters, fileName, jRDataSource, false, false);
  }
	
  public Reporte(String source, String target, Map parameters, JRDataSource jRDataSource, Boolean pagination) {
    this(source, target, parameters, null, jRDataSource , pagination,false);
  }
  
  public Reporte(Boolean pdfConSeguridad, String source, String target, Map parameters, String fileName, Boolean pagination, Boolean previsualizar) {//se puso al pricipio el atributo pdfConSeguridad para evitar problemas con los otros constructores
		this(source, target, parameters, fileName, null, pagination,previsualizar, pdfConSeguridad);
	}
  
  public Reporte(String source, String target, Map parameters, String fileName, Boolean pagination, Boolean previsualizar) {
		this(source, target, parameters, fileName, null, pagination,previsualizar);
	}
  
  public Reporte(String source, String target, Map parameters, String fileName, Boolean pagination) {
		this(source, target, parameters, fileName, null, pagination,false);
	}
	
  public Reporte(String source, String target, Map parameters, JRDataSource jRDataSource) {
    this(source, target, parameters, null, jRDataSource);
  }
  
  public Reporte(String source, String target, Map parameters, String fileName) {
		this(source, target, parameters, fileName, null , false,false);
	}

	public void setParameter(Map parameters) {
		this.parameters=parameters;
	}

	public void setSource(String source) {
		this.source=source.replace('\\', File.separatorChar);
	}

	public void setTarget(String target) {
		this.target=target;
	}

	public void setFileName(String fileName) {
		this.fileName=fileName;
	}

  public byte[] procesarMemoria(boolean compile, JasperReport input) throws Exception {
    if (compile)
      procesar(ETareas.COMPILE);
		byte[] file          = null;
		Connection connection= null;
		try {
			connection= DaoFactory.getInstance().getConnection();
			file      = JasperExportManager.exportReportToPdf(JasperFillManager.fillReport(input, parameters, connection));
		} // try
		catch(Exception e) {
			Error.mensaje(e);
		} // catch
		finally {
			if(connection!= null)
				connection.close();
			connection= null;
		} // finally
    return file;
  }
  
  public byte[] procesarMemoria(boolean compile) throws Exception {
    return procesarMemoria(compile, (JasperReport)JRLoader.loadObject(new File(source+ ".jasper")));
  } // procesarMemoria

  public boolean procesar(IFormatos taskName) throws Exception {
    boolean regresar = true;
    Exportar exportar= null;
    try {
      exportar= new Exportar(this.target+ this.fileName, this.source, (EFormatos)taskName, this.parameters, this.jRDataSource, this.pagination, this.pdfConSeguridad);
      exportar.procesar((EFormatos)taskName);
    } // try
    catch (Exception e) {
			regresar= false;
      throw e;
    } // catch
    return regresar;
  } // procesar
  
  public boolean procesar(IFormatos taskName, InputStream input) throws Exception {
    boolean regresar = true;
    Exportar exportar= null;
    try {
      exportar= new Exportar(this.target+ this.fileName, this.source, (EFormatos)taskName, this.parameters, this.jRDataSource, this.pagination, this.pdfConSeguridad);
      exportar.procesar(input);
    } // try
    catch (Exception e) {
			regresar= false;
      throw e;
    } // catch
    return regresar;
  } 
}
