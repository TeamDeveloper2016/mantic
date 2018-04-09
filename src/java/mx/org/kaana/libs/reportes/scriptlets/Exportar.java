package mx.org.kaana.libs.reportes.scriptlets;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.enums.EFormatos;
import static mx.org.kaana.kajool.enums.EFormatos.CSV;
import static mx.org.kaana.kajool.enums.EFormatos.DOCX;
import static mx.org.kaana.kajool.enums.EFormatos.HTML;
import static mx.org.kaana.kajool.enums.EFormatos.JXL;
import static mx.org.kaana.kajool.enums.EFormatos.ODS;
import static mx.org.kaana.kajool.enums.EFormatos.ODT;
import static mx.org.kaana.kajool.enums.EFormatos.PDF;
import static mx.org.kaana.kajool.enums.EFormatos.PPTX;
import static mx.org.kaana.kajool.enums.EFormatos.RTF;
import static mx.org.kaana.kajool.enums.EFormatos.XHTML;
import static mx.org.kaana.kajool.enums.EFormatos.XLS;
import static mx.org.kaana.kajool.enums.EFormatos.XLSX;
import static mx.org.kaana.kajool.enums.EFormatos.XML;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.oasis.JROdsExporter;
import net.sf.jasperreports.engine.export.oasis.JROdtExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRPptxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleCsvExporterConfiguration;
import net.sf.jasperreports.export.SimpleDocxExporterConfiguration;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOdsReportConfiguration;
import net.sf.jasperreports.export.SimpleOdtExporterConfiguration;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePptxExporterConfiguration;
import net.sf.jasperreports.export.SimpleRtfExporterConfiguration;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 9/09/2015
 * @time 04:56:22 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public class Exportar implements Serializable {

	private static final Log LOG=LogFactory.getLog(Exportar.class);
	private static final long serialVersionUID=1968365709629412162L;
	private String fileName;
	private String source;
	private EFormatos formato;
	private Map<String, Object> params;
	private JRDataSource jRDataSource;
	private Boolean pagination;

	public Exportar(String fileName, String source, EFormatos formato, Map<String, Object> params, JRDataSource jRDataSource, Boolean pagination) {
		this.fileName=fileName;
		this.source=source;
		this.formato=formato;
		this.params=params;
		this.jRDataSource=jRDataSource;
		this.pagination=pagination;
	}

	public Exportar(String fileName, String source, EFormatos formato, Map<String, Object> params, JRDataSource jRDataSource) {
		this.fileName=fileName;
		this.source=source;
		this.formato=formato;
		this.params=params;
		this.jRDataSource=jRDataSource;
	}

	public Exportar(String fileName, String source, Map<String, Object> params, JRDataSource jRDataSource) {
		this(fileName, source, null, params, jRDataSource);
	}

	public void procesar(EFormatos formato) throws Exception {
		this.formato=formato;
		try {
			fill();
			procesar();
		} // try
		catch (Exception e) {
			throw e;
		} // catch
	} // procesar

	public void procesar(InputStream input) throws Exception {
		try {
			fill(input);
			procesar();
		} // try
		catch (Exception e) {
			throw e;
		} // catch
	}

	public void procesar() throws Exception {
		try {
			switch (this.formato) {
				case PDF:
					pdf();
					break;
				case XLS:
					xls();
					break;
				case CSV:
					csv();
					break;
				case RTF:
					rtf();
					break;
				case DOCX:
					docx();
					break;
				case PPTX:
					pptx();
					break;
				case ODS:
					ods();
					break;
				case ODT:
					odt();
					break;
				case HTML:
					html();
					break;
				case XML:
					xml();
					break;
				case XHTML:
					xhtml();
					break;
				case JXL:
					jxl();
					break;
				case XLSX:
					xlsx();
					break;
			} // switch
		} // try
		catch (Exception e) {
			throw e;
		} // catch
	} // procesar

	private void fill() throws Exception {
		Long start=0L;
		Connection connection=null;
		try {
			start=System.currentTimeMillis();
			connection=DaoFactory.getInstance().getConnection();
			if (this.jRDataSource==null) {
				JasperFillManager.fillReportToFile(this.source.concat(".jasper"), this.fileName.concat(".jrprint"), this.params, connection);
			} // if
			else {
				JasperFillManager.fillReportToFile(this.source.concat(".jasper"), this.fileName.concat(".jrprint"), this.params, this.jRDataSource);
			} // esle
			LOG.debug("Filling-tiempo: ".concat(String.valueOf(System.currentTimeMillis()-start)));
		} // try
		catch (Exception e) {
			throw e;
		} // catch
		finally {
			if (connection!=null) {
				connection.close();
			}
			connection=null;
		} // finally
	} // fill

	private void fill(InputStream input) throws Exception {
		Long start=0L;
		Connection connection=null;
		OutputStream output=null;
		try {
      new File(this.fileName.substring(0,this.fileName.lastIndexOf(File.separator))).mkdirs();
			output=new FileOutputStream(this.fileName.concat(".jrprint"));
			start=System.currentTimeMillis();
			connection=DaoFactory.getInstance().getConnection();
			if (this.jRDataSource==null) {
				JasperFillManager.fillReportToStream(input, output, this.params, connection);
			} // if
			else {
				JasperFillManager.fillReportToStream(input, output, this.params, this.jRDataSource);
			} // else
			LOG.debug("Filling-tiempo: ".concat(String.valueOf(System.currentTimeMillis()-start)));
		} // try
		catch (Exception e) {
			throw e;
		} // catch
		finally {
			if (output!=null) {
				output.close();
			} // if
			output=null;
			if (connection!=null) {
				connection.close();
			} // if
			connection=null;
		} // finally
	} // fill

	private void pdf() throws Exception {
		Long start=0L;
		try {
			start=System.currentTimeMillis();
			JasperExportManager.exportReportToPdfFile(this.fileName.concat(".jrprint"), this.fileName.concat(".pdf"));
			LOG.debug("PDF-Tiempo de creacion: ".concat(String.valueOf(System.currentTimeMillis()-start)).concat(" de ").concat(this.fileName).concat(".pdf"));
		} // try
		catch (Exception e) {
			throw e;
		} // catch
	} // pdf

	private void xmlEmbed() throws Exception {
		Long start=0L;
		try {
			start=System.currentTimeMillis();
			JasperExportManager.exportReportToXmlFile(this.fileName.concat(".jrprint"), this.fileName.concat(".xml"), true);
			LOG.debug("XML-Tiempo de creacion: ".concat(String.valueOf(System.currentTimeMillis()-start)).concat(" de ").concat(this.fileName).concat(".xml"));
		} // try
		catch (Exception e) {
			throw e;
		} // catch
	} // xmlEmbed

	private void xml() throws Exception {
		Long start=0L;
		try {
			start=System.currentTimeMillis();
			JasperExportManager.exportReportToXmlFile(this.fileName.concat(".jrprint"), this.fileName.concat(".xml"), false);
			LOG.debug("XML-Tiempo de creacion: ".concat(String.valueOf(System.currentTimeMillis()-start)).concat(" de ").concat(this.fileName).concat(".xml"));
		} // try
		catch (Exception e) {
			throw e;
		} // catch
	} // xml

	private void html() throws Exception {
		Long start=0L;
		try {
			start=System.currentTimeMillis();
			JasperExportManager.exportReportToHtmlFile(this.fileName.concat(".jrprint"), this.fileName.concat(".html"));
			LOG.debug("HTML-Tiempo de creacion: ".concat(String.valueOf(System.currentTimeMillis()-start)).concat(" de ").concat(this.fileName).concat(".html"));
		} // try
		catch (Exception e) {
			throw e;
		} // catch
	} // html

	private void rtf() throws Exception {
		Long start=0L;
		File sourceFile=null;
		JasperPrint jasperPrint=null;
		JRRtfExporter exporter=null;
		List<JasperPrint> listPrint=null;
		try {
			start=System.currentTimeMillis();
			sourceFile=new File(this.fileName.concat(".jrprint"));
			jasperPrint=(JasperPrint) JRLoader.loadObject(sourceFile);
			exporter=new JRRtfExporter();
			listPrint=new ArrayList<>();
			listPrint.add(jasperPrint);
			exporter.setExporterInput(SimpleExporterInput.getInstance(listPrint));
			exporter.setExporterOutput(new SimpleWriterExporterOutput(this.fileName.concat(".rtf")));
			SimpleRtfExporterConfiguration configuration=new SimpleRtfExporterConfiguration();
			configuration.setOverrideHints(true);
			exporter.setConfiguration(configuration);
			exporter.exportReport();
			LOG.debug("RTF-Tiempo de creacion: ".concat(String.valueOf(System.currentTimeMillis()-start)).concat(" de ").concat(this.fileName).concat(".rtf"));
		} // try
		catch (Exception e) {
			throw e;
		} // catch
	} // rtf

	private void xls() throws Exception {
		Long start=0L;
		File sourceFile=null;
		JasperPrint jasperPrint=null;
		JRXlsExporter exporter=null;
		List<JasperPrint> listPrint=null;
		try {
			start=System.currentTimeMillis();
			sourceFile=new File(this.fileName.concat(".jrprint"));
			jasperPrint=(JasperPrint) JRLoader.loadObject(sourceFile);
			exporter=new JRXlsExporter();
			listPrint=new ArrayList<>();
			listPrint.add(jasperPrint);
			exporter.setExporterInput(SimpleExporterInput.getInstance(listPrint));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(this.fileName.concat(".xls")));
			SimpleXlsReportConfiguration configuration=new SimpleXlsReportConfiguration();
			configuration.setOnePagePerSheet(this.pagination);
			exporter.setConfiguration(configuration);
			exporter.exportReport();
			LOG.debug("XLS-Tiempo de creacion: ".concat(String.valueOf(System.currentTimeMillis()-start)).concat(" de ").concat(this.fileName).concat(".xls"));
		} // try
		catch (Exception e) {
			throw e;
		} // catch
	} // xls

	private void jxl() throws Exception {
		Long start=0L;
		File sourceFile=null;
		JasperPrint jasperPrint=null;
		JRXlsExporter exporter=null;
		List<JasperPrint> listPrint=null;
		try {
			start=System.currentTimeMillis();
			sourceFile=new File(this.fileName.concat(".jrprint"));
			jasperPrint=(JasperPrint) JRLoader.loadObject(sourceFile);
			exporter=new JRXlsExporter();
			listPrint=new ArrayList<>();
			listPrint.add(jasperPrint);
			exporter.setExporterInput(SimpleExporterInput.getInstance(listPrint));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(this.fileName.concat(".jxl")));
			SimpleXlsReportConfiguration configuration=new SimpleXlsReportConfiguration();
			configuration.setOnePagePerSheet(true);
			exporter.setConfiguration(configuration);
			exporter.exportReport();
			LOG.debug("JXL XML-Tiempo de creacion: ".concat(String.valueOf(System.currentTimeMillis()-start)).concat(" de ").concat(this.fileName).concat(".jxl.xls"));
		} // try
		catch (Exception e) {
			throw e;
		} // catch
	} // jxl

	private void csv() throws Exception {
		Long start=0L;
		File sourceFile=null;
		JasperPrint jasperPrint=null;
		JRCsvExporter exporter=null;
		List<JasperPrint> listPrint=null;
		try {
			start=System.currentTimeMillis();
			sourceFile=new File(this.fileName.concat(".jrprint"));
			jasperPrint=(JasperPrint) JRLoader.loadObject(sourceFile);
			exporter=new JRCsvExporter();
			listPrint=new ArrayList<>();
			listPrint.add(jasperPrint);
			exporter.setExporterInput(SimpleExporterInput.getInstance(listPrint));
			exporter.setExporterOutput(new SimpleWriterExporterOutput(this.fileName.concat(".csv")));
			SimpleCsvExporterConfiguration configurations=new SimpleCsvExporterConfiguration();
			configurations.setOverrideHints(true);
			exporter.setConfiguration(configurations);
			exporter.exportReport();
			LOG.debug("CSV-Tiempo de creacion: ".concat(String.valueOf(System.currentTimeMillis()-start)).concat(" de ").concat(this.fileName).concat(".csv"));
		} // try
		catch (Exception e) {
			throw e;
		} // catch
	} // csv

	private void odt() throws Exception {
		Long start=0L;
		File sourceFile=null;
		JasperPrint jasperPrint=null;
		JROdtExporter exporter=null;
		List<JasperPrint> listPrint=null;
		try {
			start=System.currentTimeMillis();
			sourceFile=new File(this.fileName.concat(".jrprint"));
			jasperPrint=(JasperPrint) JRLoader.loadObject(sourceFile);
			exporter=new JROdtExporter();
			listPrint=new ArrayList<>();
			listPrint.add(jasperPrint);
			exporter.setExporterInput(SimpleExporterInput.getInstance(listPrint));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(this.fileName.concat(".odt")));
			SimpleOdtExporterConfiguration configuration=new SimpleOdtExporterConfiguration();
			configuration.setOverrideHints(true);
			exporter.setConfiguration(configuration);
			exporter.exportReport();
			LOG.debug("ODT-Tiempo de creacion: ".concat(String.valueOf(System.currentTimeMillis()-start)).concat(" de ").concat(this.fileName).concat(".odt"));
		} // try
		catch (Exception e) {
			throw e;
		} // catch
	} // odt

	private void ods() throws Exception {
		Long start=0L;
		File sourceFile=null;
		JasperPrint jasperPrint=null;
		JROdsExporter exporter=null;
		List<JasperPrint> listPrint=null;
		try {
			start=System.currentTimeMillis();
			sourceFile=new File(this.fileName.concat(".jrprint"));
			jasperPrint=(JasperPrint) JRLoader.loadObject(sourceFile);
			exporter=new JROdsExporter();
			listPrint=new ArrayList<>();
			listPrint.add(jasperPrint);
			exporter.setExporterInput(SimpleExporterInput.getInstance(listPrint));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(this.fileName.concat(".ods")));
			SimpleOdsReportConfiguration configuration=new SimpleOdsReportConfiguration();
			configuration.setOnePagePerSheet(true);
			exporter.setConfiguration(configuration);
			exporter.exportReport();
			LOG.debug("ODS-Tiempo de creacion: ".concat(String.valueOf(System.currentTimeMillis()-start)).concat(" de ").concat(this.fileName).concat(".ods"));
		} // try
		catch (Exception e) {
			throw e;
		} // catch
	} // ods

	private void docx() throws Exception {
		Long start=0L;
		File sourceFile=null;
		JasperPrint jasperPrint=null;
		JRDocxExporter exporter=null;
		List<JasperPrint> listPrint=null;
		try {
			start=System.currentTimeMillis();
			sourceFile=new File(this.fileName.concat(".jrprint"));
			jasperPrint=(JasperPrint) JRLoader.loadObject(sourceFile);
			exporter=new JRDocxExporter();
			listPrint=new ArrayList<>();
			listPrint.add(jasperPrint);
			exporter.setExporterInput(SimpleExporterInput.getInstance(listPrint));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(this.fileName.concat(".docx")));
			SimpleDocxExporterConfiguration configuration=new SimpleDocxExporterConfiguration();
			configuration.setOverrideHints(true);
			exporter.setConfiguration(configuration);
			exporter.exportReport();
			LOG.debug("DOCX-Tiempo de creacion: ".concat(String.valueOf(System.currentTimeMillis()-start)).concat(" de ").concat(this.fileName).concat(".docx"));
		} // try
		catch (Exception e) {
			throw e;
		} // catch
	} // docx

	private void xlsx() throws Exception {
		Long start=0L;
		File sourceFile=null;
		JasperPrint jasperPrint=null;
		JRXlsxExporter exporter=null;
		List<JasperPrint> listPrint=null;
		try {
			start=System.currentTimeMillis();
			sourceFile=new File(this.fileName.concat(".jrprint"));
			jasperPrint=(JasperPrint) JRLoader.loadObject(sourceFile);
			exporter=new JRXlsxExporter();
			listPrint=new ArrayList<>();
			listPrint.add(jasperPrint);
			exporter.setExporterInput(SimpleExporterInput.getInstance(listPrint));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(this.fileName.concat(".xlsx")));
			SimpleXlsxReportConfiguration configuration=new SimpleXlsxReportConfiguration();
			configuration.setOnePagePerSheet(true);
			exporter.setConfiguration(configuration);
			exporter.exportReport();
			LOG.debug("XLSX-Tiempo de creacion: ".concat(String.valueOf(System.currentTimeMillis()-start)).concat(" de ").concat(this.fileName).concat(".xlsx"));
		} // try
		catch (Exception e) {
			throw e;
		} // catch
	} // xlsx

	private void pptx() throws Exception {
		Long start=0L;
		File sourceFile=null;
		JasperPrint jasperPrint=null;
		JRPptxExporter exporter=null;
		List<JasperPrint> listPrint=null;
		try {
			start=System.currentTimeMillis();
			sourceFile=new File(this.fileName.concat(".jrprint"));
			jasperPrint=(JasperPrint) JRLoader.loadObject(sourceFile);
			exporter=new JRPptxExporter();
			listPrint=new ArrayList<>();
			listPrint.add(jasperPrint);
			exporter.setExporterInput(SimpleExporterInput.getInstance(listPrint));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(this.fileName.concat(".pptx")));
			SimplePptxExporterConfiguration configuration=new SimplePptxExporterConfiguration();
			configuration.setOverrideHints(true);
			exporter.setConfiguration(configuration);
			exporter.exportReport();
			LOG.debug("PPTX-Tiempo de creacion: ".concat(String.valueOf(System.currentTimeMillis()-start)).concat(" de ").concat(this.fileName).concat(".pptx"));
		} // try
		catch (Exception e) {
			throw e;
		} // catch
	} // pptx

	private void xhtml() throws Exception {
		Long start=0L;
		try {
			start=System.currentTimeMillis();
			JasperExportManager.exportReportToHtmlFile(this.fileName.concat(".jrprint"), this.fileName.concat(".xhtml"));
			LOG.debug("XML-Tiempo de creacion: ".concat(String.valueOf(System.currentTimeMillis()-start)).concat(" de ").concat(this.fileName).concat(".xhtml"));
		} // try
		catch (Exception e) {
			throw e;
		} // catch
	} // xhtml
}
