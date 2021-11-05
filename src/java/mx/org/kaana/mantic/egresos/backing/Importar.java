package mx.org.kaana.mantic.egresos.backing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.EFormatos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.articulos.beans.Importado;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.archivo.Archivo;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UISelect;
import mx.org.kaana.libs.pagina.UISelectItem;
import mx.org.kaana.mantic.db.dto.TcManticArchivosDto;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import mx.org.kaana.mantic.db.dto.TcManticEgresosArchivosDto;
import mx.org.kaana.mantic.db.dto.TcManticEgresosDto;
import mx.org.kaana.mantic.egresos.reglas.Transaccion;
import mx.org.kaana.mantic.enums.EEstatusEgresos;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 7/05/2018
 *@time 03:29:13 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

@Named(value= "manticEgresosImportar")
@ViewScoped
public class Importar extends IBaseAttribute implements Serializable {

	private static final Log LOG              = LogFactory.getLog(Importar.class);
  private static final long serialVersionUID= 327353488565639367L;
	private TcManticEgresosDto egreso;
	private Long idEgreso;	
	private Importado xml;
	private Importado pdf;
	private Importado jpg;
  private String path;

	public Importado getXml() {
		return xml;
	} // getXml
	
	public Importado getPdf() {
		return pdf;
	} // getPdf

  public Importado getJpg() {
    return jpg;
  }

	public TcManticEgresosDto getEgreso() {
		return egreso;
	}	// getEgreso
	
  public String getPath() {
    return path;
  }
  
	@PostConstruct
  @Override
  protected void init() {		
    try {
			if(JsfBase.getFlashAttribute("idEgreso")== null)
				UIBackingUtilities.execute("janal.isPostBack('cancelar')");
      this.idEgreso= JsfBase.getFlashAttribute("idEgreso")== null ? -1L : (Long)JsfBase.getFlashAttribute("idEgreso");
			this.egreso= (TcManticEgresosDto)DaoFactory.getInstance().findById(TcManticEgresosDto.class, this.idEgreso);
			if(this.egreso!= null) {
				this.attrs.put("fechaRegistro", Fecha.formatear(Fecha.FECHA_CORTA, this.egreso.getFecha()));
				this.attrs.put("estatus", EEstatusEgresos.fromIdEstatusEgreso(this.egreso.getIdEgresoEstatus()).name());								
			} // if
			this.attrs.put("retorno", JsfBase.getFlashAttribute("retorno")== null? "filtro": JsfBase.getFlashAttribute("retorno"));
			this.xml= null;
			this.pdf= null;
			this.jpg= null;
      this.toLoadDocumentos();
      this.attrs.put("idTipoDocumento", -1L);
      this.attrs.put("formatos", Constantes.PATRON_IMPORTAR_DOCUMENTOS);
			this.attrs.put("xml", ""); 
    	this.attrs.put("pdf", ""); 
      this.attrs.put("jpg", ""); 
      String dns= Configuracion.getInstance().getPropiedadServidor("sistema.dns");
      this.path = dns.substring(0, dns.lastIndexOf("/")+ 1).concat(Configuracion.getInstance().getEtapaServidor().name().toLowerCase()).concat("/egresos/");
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init	

  private void toLoadDocumentos() {
    Map<String, Object> params = null;
    try {      
      params = new HashMap<>();      
      params.put("agrupador", "6");
      List<UISelectItem> documentos= UISelect.seleccione("TcManticTiposArchivosDto", "grupo", params, "nombre", EFormatoDinamicos.MAYUSCULAS, Constantes.SQL_TODOS_REGISTROS, "idKey");
      this.attrs.put("documentos", documentos);
      this.attrs.put("idTipoDocumento", UIBackingUtilities.toFirstKeySelectItem(documentos));
     } // try
    catch (Exception e) {
      JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
    finally {
      Methods.clean(params);
    } // finally
  }
  
  private void doLoadImportados() {
		List<Columna> columns= null;
    Map<String, Object> params = null;
    try {      
      params = new HashMap<>();      
      params.put("idEgreso", this.egreso.getIdEgreso());      
      params.put("idTipoDocumento", -1L);   
			columns= new ArrayList<>();
      columns.add(new Columna("ruta", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("usuario", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("observaciones", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA_CORTA));
		  this.attrs.put("importados", UIEntity.build("VistaEgresosDto", "importados", params, columns));
		} // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
    finally {
      Methods.clean(columns);
    }// finally
  } // doLoadImportados

	public void doFileUpload(FileUploadEvent event) {
		StringBuilder folder= new StringBuilder();  
		StringBuilder temp= new StringBuilder();  
		String nameFile   = Archivo.toFormatNameFile(event.getFile().getFileName().toUpperCase());
    File result       = null;		
		Long fileSize     = 0L;
		try {			
			Calendar calendar= Calendar.getInstance();
			calendar.setTimeInMillis(this.egreso.getRegistro().getTime());
      folder.append(Configuracion.getInstance().getPropiedadSistemaServidor("egresos"));
      temp.append(JsfBase.getAutentifica().getEmpresa().getIdEmpresa().toString());
      temp.append("/");
      temp.append(Calendar.getInstance().get(Calendar.YEAR));
      temp.append("/");
      temp.append(Fecha.getNombreMes(calendar.get(Calendar.MONTH)).toUpperCase());
      temp.append("/");
			folder.append(temp.toString());
			result= new File(folder.toString());		
			if (!result.exists())
				result.mkdirs();
      String ruta= folder.toString();
      folder.append(nameFile);
			result = new File(folder.toString());
			if (result.exists())
				result.delete();			      
			Archivo.toWriteFile(result, event.getFile().getInputstream());
			fileSize= event.getFile().getSize();			
      String observaciones= (String)this.attrs.get("observaciones");
      Long idTipoDocumento= 14L;
      if(this.attrs.get("idTipoDocumento")!= null)
        idTipoDocumento= (Long)this.attrs.get("idTipoDocumento");
			if(event.getFile().getFileName().toUpperCase().endsWith(EFormatos.XML.name())) {
			  this.xml= new Importado(nameFile, event.getFile().getContentType(), EFormatos.XML, event.getFile().getSize(), fileSize.equals(0L) ? fileSize: fileSize/1024, event.getFile().equals(0L)? " Bytes": " Kb", temp.toString(), observaciones, event.getFile().getFileName().toUpperCase(), idTipoDocumento);
				this.attrs.put("xml", this.xml.getName());
        this.toSaveFileRecord(event.getFile().getFileName().toUpperCase(), ruta, folder.toString(), this.xml.getName());            
			} // if
			else 
        if(event.getFile().getFileName().toUpperCase().endsWith(EFormatos.PDF.name())) {
				  this.pdf= new Importado(nameFile, event.getFile().getContentType(), EFormatos.PDF, event.getFile().getSize(), fileSize.equals(0L) ? fileSize: fileSize/1024, event.getFile().equals(0L)? " Bytes": " Kb", temp.toString(), observaciones, event.getFile().getFileName().toUpperCase(), idTipoDocumento);
				  this.attrs.put("pdf", this.pdf.getName()); 
          this.toSaveFileRecord(event.getFile().getFileName().toUpperCase(), ruta, folder.toString(), this.pdf.getName());            
			  } // else if
        else
          if(event.getFile().getFileName().toUpperCase().endsWith(EFormatos.JPG.name()) || event.getFile().getFileName().toUpperCase().endsWith(EFormatos.PNG.name())) {
            this.jpg= new Importado(nameFile, event.getFile().getContentType(), EFormatos.JPG, event.getFile().getSize(), fileSize.equals(0L) ? fileSize: fileSize/1024, event.getFile().equals(0L)? " Bytes": " Kb", temp.toString(), observaciones, event.getFile().getFileName().toUpperCase(), idTipoDocumento);
            this.attrs.put("jpg", this.jpg.getName()); 
            UIBackingUtilities.execute("reload();");
            this.toSaveFileRecord(event.getFile().getFileName().toUpperCase(), ruta, folder.toString(), this.jpg.getName());            
          } // else if
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessage("Importar:", "El archivo no pudo ser importado !", ETipoMensaje.ERROR);
			if(result!= null)
			  result.delete();
		} // catch
	} // doFileUpload	
	
	public void doTabChange(TabChangeEvent event) {
		if(event.getTab().getTitle().equals("Archivos")) 
			this.doLoadImportados();
	}	// doTabChange	

	private void doLoadFiles() {
		TcManticEgresosArchivosDto tmp= null;
		if(this.egreso.getIdEgreso()> 0) {
			Map<String, Object> params=null;
			try {
				params=new HashMap<>();
				params.put("idEgreso", this.idEgreso);				
				params.put("idTipoArchivo", 1L);
				tmp= (TcManticEgresosArchivosDto)DaoFactory.getInstance().toEntity(TcManticEgresosArchivosDto.class, "VistaEgresosDto", "exists", params); 
				if(tmp!= null) {
					this.xml= new Importado(tmp.getNombre(), EFormatos.XML.name(), EFormatos.XML, 0L, tmp.getTamanio(), "", tmp.getRuta(), tmp.getObservaciones());
  				this.attrs.put("xml", this.xml.getName()); 
				} // if	
				params.put("idTipoArchivo", 2L);
				tmp= (TcManticEgresosArchivosDto)DaoFactory.getInstance().toEntity(TcManticEgresosArchivosDto.class, "VistaEgresosDto", "exists", params); 
				if(tmp!= null) {
					this.pdf= new Importado(tmp.getNombre(), EFormatos.PDF.name(), EFormatos.PDF, 0L, tmp.getTamanio(), "", tmp.getRuta(), tmp.getObservaciones());
  				this.attrs.put("pdf", this.pdf.getName()); 
				} // if	
				params.put("idTipoArchivo", 17L);
				tmp= (TcManticEgresosArchivosDto)DaoFactory.getInstance().toEntity(TcManticEgresosArchivosDto.class, "VistaEgresosDto", "exists", params); 
				if(tmp!= null) {
					this.jpg= new Importado(tmp.getNombre(), EFormatos.JPG.name(), EFormatos.JPG, 0L, tmp.getTamanio(), "", tmp.getRuta(), tmp.getObservaciones());
  				this.attrs.put("jpg", this.pdf.getName()); 
				} // if	
			} // try
			catch (Exception e) {
				Error.mensaje(e);
				JsfBase.addMessageError(e);
			} // catch
			finally {
				Methods.clean(params);
			} // finally
		} // if
	} // doLoadFiles

	public StreamedContent doFileDownload(UISelectEntity file) {
		StreamedContent regresar= null;
		try {
			File reference= new File(file.toString("alias"));
			if(reference.exists()) {
				InputStream stream = new FileInputStream(reference);
				if(file.toLong("idTipoArchivo").equals(1L))
					regresar= new DefaultStreamedContent(stream, EFormatos.XML.getContent(), file.toString("nombre"));
				else
					regresar= new DefaultStreamedContent(stream, EFormatos.PDF.getContent(), file.toString("nombre"));
			} // if	
			else {
				LOG.warn("No existe el archivo: "+ file.toString("alias"));
        JsfBase.addMessage("No existe el archivo:"+ file.toString("nombre")+ ", favor de verificarlo.");
			} // else	
		} // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
		return regresar;
	} // doFileDownload

  public String doCancelar() {   
  	JsfBase.setFlashAttribute("idEgreso", this.idEgreso);
    return ((String)this.attrs.get("retorno")).concat(Constantes.REDIRECIONAR);
  } // doCancelar

	public void doViewPdfDocument(UISelectEntity item) {
		this.toCopyDocument(item.toString("alias"), item.toString("nombre"));
	} // doViewPdfDocument
	
	public void doViewDocument() {
		this.toCopyDocument(Configuracion.getInstance().getPropiedadSistemaServidor("egresos").concat(this.pdf.getRuta()).concat(this.pdf.getName()), this.pdf.getName());
	} // doViewDocument

	public void doViewXmlDocument(UISelectEntity item) {
		this.doViewFile(item.toString("alias"));
	} // doViewXmlDocument

	public void doViewFile() {
		this.doViewFile(Configuracion.getInstance().getPropiedadSistemaServidor("egresos").concat(this.xml.getRuta()).concat(this.xml.getName()));
	} // doViewFile
	
	protected void doViewFile(String nameXml) {
		String regresar   = "";
		String name       = nameXml;
    StringBuilder sb  = new StringBuilder("");
    FileReader in     = null;
		BufferedReader br = null;
		try {
			in= new FileReader(name);
			br= new BufferedReader(in);
			String line;
			while ((line = br.readLine()) != null) {
  			sb.append(line);
			} // while
			regresar= this.prettyFormat(sb.toString().startsWith("<")? sb.toString(): sb.substring(3), 2);
		} // try
		catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
		} // catch
		finally {
			try {
        if(br != null)
					br.close();
				if(in != null)
  				in.close();
			} // try
			catch (IOException e) {
        JsfBase.addMessageError(e);
			} // catch
		} // finally
		this.attrs.put("temporal", regresar);
	}
	
	public String prettyFormat(String input, int indent) throws Exception {
		Source xmlInput = new StreamSource(new StringReader(input));
		StringWriter stringWriter = new StringWriter();
		StreamResult xmlOutput = new StreamResult(stringWriter);
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		transformerFactory.setAttribute("indent-number", indent);
		Transformer transformer = transformerFactory.newTransformer(); 
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.transform(xmlInput, xmlOutput);
		return xmlOutput.getWriter().toString();
  } // prettyFormat
	
  private void toCopyDocument(String alias, String name) {
		try {
  	  this.attrs.put("temporal", JsfBase.getContext().concat("/").concat(Constantes.PATH_INVOICE).concat(name).concat("?pfdrid_c=true"));
  		File source= new File(JsfBase.getRealPath().concat(Constantes.PATH_INVOICE).concat(name));
			if(!source.exists()) {
  	  	FileInputStream input= new FileInputStream(new File(alias));
        Archivo.toWriteFile(source, input);		
			} // if	
		} // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
	}	// toCopyDocument
	
	public String doAceptar() {
		String regresar        = null;
		Transaccion transaccion= null;
		try {
      if(this.getXml()!= null || this.getPdf()!= null || this.getJpg()!= null) {
        transaccion= new Transaccion(this.egreso, this.xml, this.pdf, this.jpg);
        if(transaccion.ejecutar(EAccion.REGISTRAR)) {
          JsfBase.addMessage("Alerta", "Se actualizó y se importaron los documentos de forma correcta !");
          this.attrs.put("idTipoDocumento", -1L);
          this.reset();
          // regresar= this.doCancelar();
        } // if
      } // if
      else 
        JsfBase.addMessage("Archivo", "Se tiene que subir un archivo en formato XML, PDF o JPG", ETipoMensaje.ALERTA);	
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);
		} // catch
    return regresar;
	} // doAceptar
  
  /*
   TcManticArchivosDto para el campo de idEliminar
   1 SIGNIFICA QUE EL ARCHIVO SE DEBE DE QUEDAR
   2 SIGNIFICA QUE EL ARCHIVO SE TIENE QUE ELIMINAR
   3 SIGNIFICA QUE EL ARCHIVO YA FUE ELIMINADO
   4 SIGNIFICA QUE EL ARCHIVO SE INTENTO ELIMINAR PERO NO EXISTE
  */
  private void toSaveFileRecord(String archivo, String ruta, String alias, String nombre) throws Exception {
		TcManticArchivosDto registro= new TcManticArchivosDto(
			archivo, // String archivo, 
			2L, // Long idEliminado, 
			ruta, // String ruta, 
			JsfBase.getIdUsuario(), // Long idUsuario, 
			alias, // String alias, 
			-1L, // Long idArchivo, 
			nombre // String nombre
		);
		DaoFactory.getInstance().insert(registro);
	}

  public void reset() {
    this.xml= null;  
    this.pdf= null;  
    this.jpg= null;  
  }   
  
  public void doValueUpload() {
    LOG.info(this.attrs.get("idTipoDocumento")); 
    if(this.attrs.get("observaciones")!= null)
      this.attrs.put("observaciones", ((String)this.attrs.get("observaciones")).toUpperCase());
  }
  
	public String toColor(Entity row) {
		return "";
	} 
 
  public void doDelete(Entity row) {
 		Transaccion transaccion= null;
		try {
      transaccion= new Transaccion(row);
      if(transaccion.ejecutar(EAccion.MOVIMIENTOS)) 
        JsfBase.addMessage("Alerta", "Se "+ (Objects.equals(row.toLong("idEliminado"), 1L)? "eliminó": "recuperó")+ " de forma correcta el documento");      
      else 
        JsfBase.addMessage("Error", "No se pudo ser "+ (Objects.equals(row.toLong("idEliminado"), 2L)? "eliminado": "recuperado")+ " el documento", ETipoMensaje.ALERTA);	
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);
		} // catch
  }
  
}