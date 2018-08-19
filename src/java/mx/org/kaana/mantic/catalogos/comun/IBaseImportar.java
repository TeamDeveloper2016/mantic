package mx.org.kaana.mantic.catalogos.comun;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.EFormatos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.KajoolBaseException;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.articulos.beans.Importado;
import mx.org.kaana.mantic.db.dto.TcManticListasPreciosDetallesDto;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

public abstract class IBaseImportar extends IBaseAttribute implements Serializable {

	private static final long serialVersionUID= 5868488964656514537L;
	private static final Log LOG              = LogFactory.getLog(IBaseImportar.class);
  private static final int BUFFER_SIZE      = 6124;
	
	private Importado xls;
	private Importado pdf;
	private Importado file;	
  private List<TcManticListasPreciosDetallesDto> articulos;

  public Importado getXls() {
    return xls;
  }

	public Importado getPdf() {
		return pdf;
	}

	public Importado getFile() {
		return file;
	}

	public void setFile(Importado file) {
		this.file = file;
	}

  public List<TcManticListasPreciosDetallesDto> getArticulos() {
    return articulos;
  }

  public void setArticulos(List<TcManticListasPreciosDetallesDto> articulos) {
    this.articulos = articulos;
  }
		
	protected void doFileUpload(FileUploadEvent event, Long fechaFactura, String carpeta, String clave) {
		this.doFileUpload(event, fechaFactura, carpeta, clave, true, 1D);
	}
	
	protected void doFileUpload(FileUploadEvent event, Long fechaFactura, String carpeta, String clave, Boolean sinIva, Double tipoDeCambio) {
		StringBuilder path= new StringBuilder();  
		StringBuilder temp= new StringBuilder();  
    File result       = null;		
		Long fileSize     = 0L;
		boolean isXls     = false;
    String nombreArchivo = null;
		try {
			Calendar calendar= Calendar.getInstance();
			calendar.setTimeInMillis(fechaFactura);
      path.append(carpeta);
      temp.append(JsfBase.getAutentifica().getEmpresa().getNombreCorto().replaceAll(" ", ""));
      temp.append("/");
      temp.append(Calendar.getInstance().get(Calendar.YEAR));
      temp.append("/");
      temp.append(Fecha.getNombreMes(calendar.get(Calendar.MONTH)).toUpperCase());
      temp.append("/");
      temp.append(clave.trim());
      temp.append("/");
			path.append(temp.toString());
			result= new File(path.toString());		
			if (!result.exists())
				result.mkdirs();
      nombreArchivo = Cadena.rellenar(Fecha.formatear(Fecha.FECHA_HORA_LARGA), 17, '0', false).trim().concat("_").concat(event.getFile().getFileName().toUpperCase());
      path.append(nombreArchivo);
			result = new File(path.toString());
			if (result.exists())
				result.delete();			      
			isXls= event.getFile().getFileName().toUpperCase().endsWith(EFormatos.XLS.name());
			this.toWriteFile(result, event.getFile().getInputstream(), isXls);
			fileSize= event.getFile().getSize();			
			if(isXls) {
				if(!this.toVerificaXls(result)){
          throw new KajoolBaseException("El archivo ["+this.xls.getName()+ "] no tiene el formato adecuado para la carga" );
        }
        this.xls= new Importado(nombreArchivo, event.getFile().getContentType(), EFormatos.XLS, event.getFile().getSize(), fileSize.equals(0L) ? fileSize: fileSize/1024, event.getFile().equals(0L)? " Bytes": " Kb", temp.toString(), (String)this.attrs.get("observaciones"));
        this.attrs.put("xls", this.xls.getName());
			} //
			else
			  if(event.getFile().getFileName().toUpperCase().endsWith(EFormatos.PDF.name())) {
			    this.pdf= new Importado(nombreArchivo, event.getFile().getContentType(), EFormatos.PDF, event.getFile().getSize(), fileSize.equals(0L) ? fileSize: fileSize/1024, event.getFile().equals(0L)? " Bytes": " Kb", temp.toString(), (String)this.attrs.get("observaciones"));
  				this.attrs.put("pdf", this.pdf.getName()); 
				} // if
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessage("Importar:", "El archivo no pudo ser importado !", ETipoMensaje.ERROR);
			if(result!= null)
			  result.delete();
		} // catch
	} // doFileUpload	
	
	public void doFileUpload(FileUploadEvent event, String clave, String propiedadServidor) {
		StringBuilder path= new StringBuilder();  
		StringBuilder temp= new StringBuilder();  
    File result       = null;		
		Long fileSize     = 0L;
		try {			
      path.append(Configuracion.getInstance().getPropiedadSistemaServidor(propiedadServidor));
      temp.append(JsfBase.getAutentifica().getEmpresa().getNombreCorto().replaceAll(" ", ""));
      temp.append("/");
      temp.append(clave);
      temp.append("/");      
			path.append(temp.toString());
			result= new File(path.toString());		
			if (!result.exists())
				result.mkdirs();
      path.append(event.getFile().getFileName().toUpperCase());
			result = new File(path.toString());
			if (result.exists())
				result.delete();			      
			this.toWriteFile(result, event.getFile().getInputstream());
			fileSize= event.getFile().getSize();						
			this.file= new Importado(event.getFile().getFileName().toUpperCase(), event.getFile().getContentType(), EFormatos.PDF, event.getFile().getSize(), fileSize.equals(0L) ? fileSize: fileSize/1024, event.getFile().equals(0L)? " Bytes": " Kb", temp.toString(), (String)this.attrs.get("observaciones"));
  		this.attrs.put("file", this.file.getName()); 			
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessage("Importar:", "El archivo no pudo ser importado !", ETipoMensaje.ERROR);
			if(result!= null)
			  result.delete();
		} // catch
	} // doFileUpload	
	
	private void toWriteFile(File result, InputStream upload) throws Exception {
		toWriteFile(result, upload, false);
	} // toWriteFile
  
  private void toWriteFile(File result, InputStream upload, boolean xls) throws Exception{
		FileOutputStream fileOutputStream= new FileOutputStream(result);
		InputStream inputStream          = upload;
		byte[] buffer                    = new byte[BUFFER_SIZE];
		int bulk;
		try{
      while(true) {
        bulk= inputStream.read(buffer);
        if (bulk < 0) 
          break;  
        fileOutputStream.write(buffer, 0, bulk);
        fileOutputStream.flush();
      } // while
      fileOutputStream.close();
      inputStream.close();
		} // try
		catch (Exception e) {
			throw e;
		} // catch
	} // toWriteFile
 
  private Boolean toVerificaXls(File archivo) throws Exception{
		Boolean regresar	      = false;
		Workbook workbook	      = null;
		Sheet sheet             = null;
		StringBuilder encabezado= new StringBuilder();
		try {
			workbook= Workbook.getWorkbook(archivo);
			sheet		= workbook.getSheet(0);
			if(sheet != null && sheet.getColumns()==4 && sheet.getRows()>= 2){
				for (int columna = 0; columna < 4; columna++){
					encabezado.append(sheet.getCell(columna,0).getContents());
					encabezado.append("|");
				} // for
				encabezado.delete(encabezado.length()-1, encabezado.length());
				if(encabezado.toString().equals("CLAVE|AUXILIAR|DESCRIPCION|PRECIO")){
          this.articulos = new ArrayList<>();
          for(int fila= 1; fila<sheet.getRows(); fila++){
            //(idListaPrecio,descripcion, idListaPrecioDetalle, codigo, precio, auxiliar) 
            getArticulos().add(new TcManticListasPreciosDetallesDto(
            -1L,
            sheet.getCell(2,fila).getContents(),
            -1L,
            sheet.getCell(0,fila).getContents(),
            Double.valueOf(sheet.getCell(3,fila).getContents()),
            sheet.getCell(1,fila).getContents()));
          } // for
          regresar = true;
        }
			} // if
		} // try
		catch (IOException | BiffException e) {
			throw e;
		} // catch
    finally {
      if(workbook!= null){
        workbook.close();
        workbook = null;
      }
    } // finally
		return regresar;
	} // toVerificaXls
	
	protected void doLoadFiles(String proceso, Long idSelected, String idNombre) {
		this.doLoadFiles(proceso, idSelected, idNombre, true, 1D);
	}
	
	protected void doLoadFiles(String proceso, Long idSelected, String idNombre, Boolean sinIva, Double tipoDeCambio) {
		Entity tmp= null;
		if(idSelected> 0) {
			Map<String, Object> params=null;
			try {
				params=new HashMap<>();
				params.put(idNombre, idSelected);
				params.put("idTipoArchivo", 8L);
				tmp= (Entity)DaoFactory.getInstance().toEntity(proceso, "exists", params);
				if(tmp!= null) {
					this.xls= new Importado(tmp.toString("nombre"), "XLS", EFormatos.XLS, 0L, tmp.toLong("tamanio"), "", tmp.toString("ruta"), tmp.toString("observaciones"));
					this.toVerificaXls(new File(tmp.toString("alias")));
  				this.attrs.put("xls", this.xls.getName()); 
				} // if	
				params.put("idTipoArchivo", 2L);
				tmp= (Entity)DaoFactory.getInstance().toEntity(proceso, "exists", params);
				if(tmp!= null) {
					this.pdf= new Importado(tmp.toString("nombre"), "PDF", EFormatos.PDF, 0L, tmp.toLong("tamanio"), "", tmp.toString("ruta"), tmp.toString("observaciones"));
  				this.attrs.put("pdf", this.pdf.getName()); 
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
	}

	public StreamedContent doPdfFileDownload(String carpeta) {
		StreamedContent regresar= null;
		try {
		  InputStream stream = new FileInputStream(new File(carpeta.concat(this.pdf.getRuta()).concat(this.pdf.getName())));
	    regresar= new DefaultStreamedContent(stream, "application/pdf", this.pdf.getName());
		} // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
		return regresar;
	}	
	
	public StreamedContent doFileDownload(UISelectEntity file) {
		StreamedContent regresar= null;
		try {
		  InputStream stream = new FileInputStream(new File(file.toString("alias")));
			if(file.toLong("idTipoArchivo").equals(8L))
		    regresar= new DefaultStreamedContent(stream, EFormatos.XLS.getContent(), file.toString("nombre"));
			else
		    regresar= new DefaultStreamedContent(stream, EFormatos.PDF.getContent(), file.toString("nombre"));
		} // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
		return regresar;
	}

	public void doViewFileDocument(UISelectEntity item) {
		this.doViewPdfDocument(item);
	}
	
	public void doViewPdfDocument(UISelectEntity item) { 
		this.toCopyDocument(item.toString("alias"), item.toString("nombre"));
	}
	
	protected void doViewDocument(String carpeta) {
		this.toCopyDocument(carpeta.concat(this.pdf.getRuta()).concat(this.pdf.getName()), this.pdf.getName());
	}
	
	protected void doViewDocumentFile(String carpeta) {
		this.toCopyDocument(carpeta.concat(this.file.getRuta()).concat(this.file.getName()), this.file.getName());
	}

	public void doViewXlsDocument(UISelectEntity item) {
		this.toViewFile(item.toString("alias"));
	}

	protected void doViewFile(String carpeta) {
		this.toViewFile(carpeta.concat(this.xls.getRuta()).concat(this.xls.getName()));
	}	
	
	protected void toViewFile(String nameXml) {
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
	
	private String prettyFormat(String input, int indent) throws Exception {
		Source xlsInput = new StreamSource(new StringReader(input));
		StringWriter stringWriter = new StringWriter();
		StreamResult xlsOutput = new StreamResult(stringWriter);
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		transformerFactory.setAttribute("indent-number", indent);
		Transformer transformer = transformerFactory.newTransformer(); 
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.transform(xlsInput, xlsOutput);
		return xlsOutput.getWriter().toString();
  }
	
  private void toCopyDocument(String alias, String name) {
		try {
  	  this.attrs.put("temporal", JsfBase.getContext().concat("/").concat(Constantes.RUTA_TEMPORALES).concat(name).concat("?pfdrid_c=true"));
  		File file= new File(JsfBase.getRealPath().concat(Constantes.RUTA_TEMPORALES).concat(name));
	  	FileInputStream input= new FileInputStream(new File(alias));
      this.toWriteFile(file, input);		
		} // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
	}	
	
	public void doCerrar() {
		try {
			String name= (String)this.attrs.get("temporal");
			if(name.endsWith("XLS"))
				name= JsfBase.getContext().concat(name);
			else
				name= name.substring(0, name.lastIndexOf("?"));
			File file= new File(JsfBase.getRealPath().concat(name));
			file.delete();
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);
		} // catch
	}

	protected void doLoadImportados(String proceso, String idXml, Map<String, Object> params) {
		List<Columna> columns= null;
		try {
			columns= new ArrayList<>();
      columns.add(new Columna("ruta", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("usuario", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("observaciones", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("registro", EFormatoDinamicos.FECHA_CORTA));
		  this.attrs.put("importados", UIEntity.build(proceso, idXml, params, columns));
		} // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
    finally {
      Methods.clean(columns);
    } // finally
  } 
		
}
