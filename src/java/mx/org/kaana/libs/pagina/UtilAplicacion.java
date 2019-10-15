package mx.org.kaana.libs.pagina;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletContext;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.archivo.Archivo;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.recurso.Configuracion;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.util.HSSFColor;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.procesos.mantenimiento.contadores.reglas.Ayudas;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 14/08/2015
 * @time 12:28:03 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */

@Named(value = "kajoolUtilAplicacion")
@ApplicationScoped
public class UtilAplicacion {

  private static final String STYLE_SHEET = "<link type=\"text/css\" rel=\"stylesheet\" href=\"{0}/javax.faces.resource/css/{1}.jsf?ln={2}\">";

  private static final String LESS  = "janal.mantic.less.min-2.5.0.js";
  private static final String CSS   = "janal.mantic.core-0.6.4.css";
  private static final String CORE  = "jquery.janal.core-3.1.8.js";
  private static final String VENTAS= "jquery.janal.ventas.core-0.9.2.js";
	private static final String TICKET= "jquery.janal.ticket.core-0.0.5.js";
  private static final String CALC  = "jquery.janal.arts.core-2.2.6.js";
  private static final String KARDEX= "jquery.janal.kardex.core-0.5.3.js";
  private static final String DLGS  = "jquery.janal.precio.core-0.5.1.js";
  private static final String FUSION= "jquery.janal.fusion.charts-3.3.1.js";

  public String getTituloSistema() {
    return Configuracion.getInstance().getPropiedad("sistema.titulo").toUpperCase();
  }

  public String getTituloCorto() {
    return Configuracion.getInstance().getPropiedad("sistema.corto");
  }

  public String getIngresoCurp() {
    return Configuracion.getInstance().getPropiedad("sistema.ingreso.curp");
  }

  public String getVersionAplicacion() {
    final String version = "Versión {0}";
    String regresar = MessageFormat.format(version, new Object[]{Configuracion.getInstance().getVersion()});
    return regresar;
  }

  public String getVersion() {
    return "V ".concat(Configuracion.getInstance().getVersion());
  }

  public String getCurrentDate() {
    return Fecha.getHoyMesCorto();
  }

  public String getLogo() {
    String janalLogo = "janal-logo";
    if (!Configuracion.getInstance().isEtapaProduccion()) {
      janalLogo = janalLogo.concat("-").concat(Configuracion.getInstance().getEtapaServidor().toLowerCase());
    }
    janalLogo = janalLogo.concat(".png");
    return janalLogo;
  }

  public String getServidor() {
    return Configuracion.getInstance().getEtapaServidor().toUpperCase();
  }

  public String getServidorEspecial() {
    return Configuracion.getInstance().isEtapaProduccion() ? "" : Configuracion.getInstance().getEtapaServidor().toUpperCase();
  }

  public String getDefaultScripts() {
    StringBuilder sb = new StringBuilder();
    sb.append("<script type=\"text/javascript\">Janal.Control.name= [").append(doContextRoot()).append("];Janal.Control.stage= [").append(Cadena.stringToBytes(getEtapa())).append("];</script>").append(Constantes.ENTER);
    return sb.toString();
  }

  public String toTitle(String title, Integer count) {
    return !Cadena.isVacio(title) && title.length() > count ? title.substring(0, count) : title;
  }

  public String getDefaultCss() {
    StringBuilder sb = new StringBuilder();
    String[] css = {"sentinel-core-1.0.2.css|sentinel", "sentinel-font-icon-1.0.0.css|sentinel", "sentinel-layout-1.0.2.css|sentinel", CSS.concat("|janal")};
    for (String item : css) {
      String[] values = item.split("[|]");
      sb.append(MessageFormat.format(STYLE_SHEET, JsfUtilities.getContext(), values[0], values[1]));
    } // for
    return sb.toString();
  }

  public String getNameFile() {
    return Archivo.toFormatNameFile("exportar");
  }

  public void doPostProcessXls(Object document) {
    HSSFWorkbook wb = (HSSFWorkbook) document;
    HSSFSheet sheet = wb.getSheetAt(0);
    HSSFRow header = sheet.getRow(0);
    HSSFCellStyle cellStyle = wb.createCellStyle();
    cellStyle.setFillForegroundColor(HSSFColor.GREEN.index);
    cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
    for (int i = 0; i < header.getPhysicalNumberOfCells(); i++) {
      HSSFCell cell = header.getCell(i);
      cell.setCellStyle(cellStyle);
    }	// for
  }

  public void doPreProcessPdf(Object document) {
    Document pdf;
    Image logoKaajol;
    ServletContext servletContext;
    String rutaLogoKajool;
    Chunk kajool;
    String hora;
    String version;
    BaseFont font;
    Paragraph parrafoEncabezado;
    HeaderFooter header;
    Paragraph parrafoPieDePagina;
    HeaderFooter footer;
    try {
      font = BaseFont.createFont(BaseFont.HELVETICA_BOLD, "Cp1252", false);
      servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
      rutaLogoKajool = servletContext.getRealPath("").concat(Constantes.RUTA_IMAGENES).concat("logo.png");
      logoKaajol = Image.getInstance(rutaLogoKajool);
      logoKaajol.scalePercent(50);
      kajool = new Chunk(logoKaajol, 0, -15);
      parrafoEncabezado = new Paragraph();
      parrafoEncabezado.add(new Phrase(Cadena.letraCapital(Constantes.NOMBRE_DE_APLICACION), new Font(font)));
      parrafoEncabezado.add(kajool);
      parrafoEncabezado.setAlignment(Paragraph.ALIGN_CENTER);
      header = new HeaderFooter(parrafoEncabezado, false);
      header.setBorder(Rectangle.NO_BORDER);
      header.setAlignment(HeaderFooter.ALIGN_CENTER);
      parrafoPieDePagina = new Paragraph();
      hora = Fecha.formatear(Fecha.FECHA_HORA_CORTA, Calendar.getInstance().getTime());
      version = Configuracion.getInstance().isEtapaProduccion() ? getVersionAplicacion() : getVersionAplicacion().concat(" - ").concat(getServidor());
      parrafoPieDePagina.add(new Phrase(hora.concat("          ").concat(version), new Font(font)));
      parrafoPieDePagina.setAlignment(Phrase.ALIGN_CENTER);
      footer = new HeaderFooter(parrafoPieDePagina, true);
      footer.setAlignment(HeaderFooter.ALIGN_CENTER);
      footer.setBorder(Rectangle.NO_BORDER);
      pdf = (Document) document;
      pdf.setPageSize(PageSize.LETTER);
      pdf.setHeader(header);
      pdf.setFooter(footer);
      pdf.setMargins(-40, -40, 50, 20);
      pdf.open();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
  }

  public String doContextRoot() {
    return JsfUtilities.doContextRoot();
  }

  public Boolean getIsMobile() {
    return JsfUtilities.getBrowser().isMobile();
  }

  public String getMobile() {
    return JsfUtilities.getBrowser().name();
  }

  public String getContextMenu() {
    return JsfUtilities.getBrowser().getDevice().getEvent();
  }

  public Boolean getProduccion() {
    return Configuracion.getInstance().getEtapaServidor().toLowerCase().equals("produccion");
  }

  public String getEtapa() {
    return Configuracion.getInstance().getEtapaServidor().toLowerCase();
  }

  public String getLibraryLess() {
    return "less/" + this.LESS;
  }

  public String getLibraryJs() {
    return "core/" + this.CORE;
  }
  
	public String getLibraryVentasJs() {
    return "core/" + this.VENTAS;
  }
	
	public String getLibraryTicketJs() {
    return "core/" + this.TICKET;
  }

	public String getLibraryKardexJs() {
    return "core/" + this.KARDEX;
  }

  public String getLibraryArticulosJs() {
    return "core/" + this.CALC;
  }

	public String getLibraryPreciosJs() {
    return "core/" + this.DLGS;
  }

  public String getDefaultResourceImg() {
    return JsfUtilities.getContext().concat("/".concat(Constantes.JAVAX_FACES_RESOURCE).concat("/img"));
  }

  public String getDefaultResourceIcon() {
    return JsfUtilities.getContext().concat("/".concat(Constantes.JAVAX_FACES_RESOURCE).concat("/icon"));
  }

  public String getDefaultResourceFusion() {
    return JsfUtilities.getContext().concat("/".concat(Constantes.JAVAX_FACES_RESOURCE).concat("/fusion"));
  }

  public String getDefaultResourceFlash() {
    return JsfUtilities.getContext().concat("/".concat(Constantes.JAVAX_FACES_RESOURCE).concat("/flash"));
  }

  public String getDefaultResourceMap() {
    return getDefaultResourceIcon().concat("/maps");
  }

  public String getDefaultResourceLibrary() {
    return ".jsf?ln=janal";
  }

  public String getLibraryFusion() {
    return "fusion/chart/" + this.FUSION;
  }

  public boolean isDirectivo() {
    return JsfBase.isDirectivo();
  }

  public String toTextHelp(Integer idAyuda) {
    return Ayudas.getInstance().value(Long.valueOf(idAyuda.toString()));
  }

  public void actualizarContadorMensajes() {
    UIBackingUtilities.execute("Sentinel.updateNotifications(".concat(getContadorMensajes().toString()).concat(");"));
  }

  public Long getContadorMensajes() {
    Long regresar = 0L;
    Map<String, Object> params = new HashMap();
    try {
      params.put("idUsuario", JsfBase.getAutentifica().getPersona().getIdUsuario());
      regresar = DaoFactory.getInstance().toField("VistaTrJanalMensajesUsuariosDto", "contadorNoLeidos", params, "cantidad").toLong();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
    return regresar;
  }

  public boolean isAdmin() {
    boolean regresar = false;
    try {
      regresar = JsfBase.isAdmin();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    }
    return regresar;
  }

  public String getNotificacion() {
    return JsfBase.toLockUsers().toMessage();
  }

  public boolean isLockUserStarted() {
    return JsfBase.toLockUsers().isLock();
  }

  public boolean isLockUserActived() {
    return JsfBase.toLockUsers().isActived();
  }

	public String getModulo() {
		return JsfBase.getCodigoModulo();
	}
	
}
