package mx.org.kaana.kajool.enums;

import java.io.File;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Variables;
import mx.org.kaana.kajool.catalogos.IFormatos;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.xml.Dml;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date May 28, 2012
 *@time 12:29:00 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public enum EFormatos implements IFormatos {

	PDF("icono-pdf",    "icon/acciones/pdf.png",    "application/pdf",                                                           ETipoFormato.IREPORT,   "(Adobe Systems) Formato de documento portable"),
	XLS("icono-xls",    "icon/acciones/xls.png",    "application/vnd.ms-excel",                                                  ETipoFormato.IREPORT,   "(Microsoft) Hoja de calculo"),
	XLSX("icono-xls",   "icon/acciones/xls.png",    "application/vnd.ms-excel.12",                                               ETipoFormato.IREPORT,   "(Microsoft) Hoja de calculo extensible"),
	CSV("icono-csv",    "icon/acciones/csv.png",    "text/csv",                                                                  ETipoFormato.IREPORT,   "(Ascii) Archivo texto separados por comas"),
	RTF("icono-rtf",    "icon/acciones/doc.png",    "application/rtf",                                                           ETipoFormato.IREPORT,   "(Microsoft) Formato de texto enriquecido"),
	DOC("icono-rtf",    "icon/acciones/doc.png",    "application/msword",                                                        ETipoFormato.IREPORT,   "(Microsoft) Procesador de textos"),
	DOCX("icono-rtf",   "icon/acciones/doc.png",    "application/vnd.ms-word.document.12",                                       ETipoFormato.IREPORT,   "(Microsoft) Procesador de textos extensible"),
	PPT("icono-rtf",    "icon/acciones/doc.png",    "application/mspowerpoint",                                                  ETipoFormato.IREPORT,   "(Microsoft, Ecma, ISO/IEC) Extension de archivos para presentaciones multimedia de Microsoft"),
	PPTX("icono-rtf",   "icon/acciones/doc.png",   "application/vnd.openxmlformats-officedocument.presentationml.presentation", ETipoFormato.IREPORT,   "(Microsoft, Ecma, ISO/IEC) Extension de archivos para presentaciones multimedia de Microsoft"),
	ODS("icono-xls",    "icon/acciones/doc.png",    "application/vnd.oasis.opendocument.spreadsheet",                            ETipoFormato.IREPORT,   "(Sun Microsystems, OASIS)Almacen de datos operacional"),
	ODT("icono-rtf",    "icon/acciones/doc.png",    "application/vnd.oasis.opendocument.text",                                   ETipoFormato.IREPORT,   "(Sun Microsystems, OASIS) OpenDocument"),
	HTML("icono-html",  "icon/acciones/html.png",   "text/html",                                                                 ETipoFormato.IREPORT,   "(World Wide Web Consortium & WHATWG) Lenguaje de marcado de hipertexto"),
	XML("icono-xml",    "icon/acciones/xml.png",    "text/html",                                                                 ETipoFormato.IREPORT,   "(World Wide Web Consortium) Lenguaje de marcas extensible"),
	XHTML("icono-html", "icon/acciones/html.png",   "text/xhtml",                                                                ETipoFormato.IREPORT,   "(World Wide Web Consortium) Lenguaje de Marcado de Hipertexto Extensible"),
	JXL("icono-html",   "icon/acciones/html.png",   "application/vnd.ms-excel; charset=ISO-8859-1",                              ETipoFormato.IREPORT,   ""),
	DBF("icono-dbf",    "icon/acciones/foxpro.png", "text/dbf",                                                                  ETipoFormato.LIBRE,     "(Microsoft) Base de datos del archivo"),
	TXT("icono-txt",    "icon/acciones/txt.png",    "text/csv",                                                                  ETipoFormato.LIBRE,     "Formato de texto plano"),
	ZIP("icono-zip",    "icon/acciones/zip.png",    "application/zip",                                                           ETipoFormato.COMPRIMIDO,"(Phil Katz, PKWARE) Archivo comprimido"),
	FREE("icono-zip",   "icon/acciones/zip.png",    "application/zip",                                                           ETipoFormato.LIBRE,     "(Phil Katz, PKWARE) Archivo comprimido");
	
	private String icono;
	private String image;
	private String content;
	private ETipoFormato type;
	private String company;
	private Long idTipoArchivo;

	private EFormatos(String icono, String image, String content, ETipoFormato type, String company) {
		this.icono    = icono;
		this.image    = image;
		this.content  = content;
		this.type     = type;
		this.company  = company;
		this.idTipoArchivo= toIdTipoArchivo();
	}

	public String getIcono() {
		return icono;
	}

	public String getImage() {
		return image;
	}

	public String getContent() {
		return content;
	}

	public ETipoFormato getType() {
		return type;
	}
	
	public String toPath() {
		return Constantes.RUTA_TEMPORALES.concat(Cadena.letraCapital(this.name())).concat(File.separator);
	}

	public String getCompany() {
		return company;
	}

	public Long getIdTipoArchivo() {
		return idTipoArchivo;
	}
	
	private Long toIdTipoArchivo() {
		Long id= -1L;
		Value formato;
		try {
			if (Dml.getInstance().exists("TcTiposArchivosDto",Constantes.DML_SELECT)) {
			  formato = DaoFactory.getInstance().toField("TcTiposArchivosDto", Variables.toMap(Constantes.SQL_CONDICION.concat("~descripcion='").concat(name().toUpperCase()).concat("'")), "idTipoArchivo");
			  id = formato != null ?formato.toLong():-1L;
			}		
		} // try
		catch (Exception e) {
			mx.org.kaana.libs.formato.Error.mensaje(e);
		} // catch
		return id;
	}
}
