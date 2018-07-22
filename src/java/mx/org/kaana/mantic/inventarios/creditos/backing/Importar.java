package mx.org.kaana.mantic.inventarios.creditos.backing;

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
import java.util.Random;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.EFormatos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.articulos.beans.Importado;
import mx.org.kaana.mantic.compras.ordenes.beans.Articulo;
import mx.org.kaana.mantic.db.dto.TcManticCreditosArchivosDto;
import mx.org.kaana.mantic.libs.factura.beans.ComprobanteFiscal;
import mx.org.kaana.mantic.libs.factura.beans.Concepto;
import mx.org.kaana.mantic.libs.factura.reglas.Reader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.Collections;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.mantic.db.dto.TcManticCreditosNotasDto;
import mx.org.kaana.mantic.db.dto.TcManticDevolucionesDto;
import mx.org.kaana.mantic.db.dto.TcManticNotasEntradasDto;
import mx.org.kaana.mantic.db.dto.TcManticProveedoresDto;
import mx.org.kaana.mantic.inventarios.creditos.reglas.Importados;
import mx.org.kaana.mantic.libs.factura.beans.Emisor;
import mx.org.kaana.mantic.libs.factura.beans.Receptor;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 7/05/2018
 *@time 03:29:13 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

@Named(value= "manticInventariosCreditosImportar")
@ViewScoped
public class Importar extends IBaseAttribute implements Serializable {

	private static final Log LOG=LogFactory.getLog(Importar.class);
  private static final long serialVersionUID= 327353488565639367L;
	private static final int BUFFER_SIZE      = 6124;
	
	private TcManticCreditosNotasDto orden;
	private TcManticProveedoresDto proveedor;
	private Importado xml;
	private Importado pdf;
	private Emisor emisor;
	private Receptor receptor;
	private Long idCreditoNota;

	public Importado getXml() {
		return xml;
	}
	
	public Importado getPdf() {
		return pdf;
	}

	public TcManticCreditosNotasDto getOrden() {
		return orden;
	}

	public TcManticProveedoresDto getProveedor() {
		return proveedor;
	}

	public Emisor getEmisor() {
		return emisor;
	}

	public Receptor getReceptor() {
		return receptor;
	}
	
	public Boolean getDiferente() {
	  return this.emisor!= null && this.proveedor!= null &&	!this.emisor.getRfc().equals(this.proveedor.getRfc());
	}
	
	@PostConstruct
  @Override
  protected void init() {		
		TcManticDevolucionesDto devolucion  = null;
		TcManticNotasEntradasDto notaEntrada= null;
    try {
			if(JsfBase.getFlashAttribute("idCreditoNota")== null)
				RequestContext.getCurrentInstance().execute("janal.isPostBack('cancelar')");
      idCreditoNota= JsfBase.getFlashAttribute("idCreditoNota")== null? -1L: (Long)JsfBase.getFlashAttribute("idCreditoNota");
			this.orden= (TcManticCreditosNotasDto)DaoFactory.getInstance().findById(TcManticCreditosNotasDto.class, idCreditoNota);
			if(this.orden!= null) {
  			switch(this.orden.getIdTipoCreditoNota().intValue()) {
	  			case 1:
  			    devolucion = (TcManticDevolucionesDto)DaoFactory.getInstance().findById(TcManticDevolucionesDto.class, this.orden.getIdDevolucion());
  			    notaEntrada= (TcManticNotasEntradasDto)DaoFactory.getInstance().findById(TcManticNotasEntradasDto.class, devolucion.getIdNotaEntrada());
  			    this.proveedor= (TcManticProveedoresDto)DaoFactory.getInstance().findById(TcManticProveedoresDto.class, notaEntrada.getIdProveedor());
					  break;
	  			case 2:
  			    notaEntrada= (TcManticNotasEntradasDto)DaoFactory.getInstance().findById(TcManticNotasEntradasDto.class, this.orden.getIdNotaEntrada());
  			    this.proveedor= (TcManticProveedoresDto)DaoFactory.getInstance().findById(TcManticProveedoresDto.class, notaEntrada.getIdProveedor());
					  break;
	  			case 3:
  			    this.proveedor= (TcManticProveedoresDto)DaoFactory.getInstance().findById(TcManticProveedoresDto.class, this.orden.getIdProveedor());
					  break;
				} // switch
 			  this.toLoadCatalog();
			} // if
			this.attrs.put("retorno", JsfBase.getFlashAttribute("retorno")== null? "filtro": JsfBase.getFlashAttribute("retorno"));
			this.attrs.put("formatos", Constantes.PATRON_IMPORTAR_FACTURA);
  		this.xml= null;
			this.pdf= null;
  		this.attrs.put("xml", ""); 
			this.attrs.put("pdf", ""); 
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

	private void toLoadCatalog() {
		List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
    try {
			columns= new ArrayList<>();
      columns.add(new Columna("razonSocial", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("rfc", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
			params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
			params.put("idProveedor", this.proveedor.getIdProveedor());
			this.attrs.put("proveedores", UIEntity.build("VistaCreditosNotasDto", "proveedores", params, columns));
    } // try
    catch (Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    } // finally
	}

  private void doLoadImportados() {
		List<Columna> columns= null;
		try {
			columns= new ArrayList<>();
      columns.add(new Columna("ruta", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("usuario", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("observaciones", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA_CORTA));
		  this.attrs.put("importados", UIEntity.build("VistaCreditosNotasDto", "importados", this.orden.toMap(), columns));
		} // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
    finally {
      Methods.clean(columns);
    } // finally
  } // doLoad

	public void doFileUpload(FileUploadEvent event) {
		StringBuilder path= new StringBuilder();  
		StringBuilder temp= new StringBuilder();  
    File result       = null;		
		Long fileSize     = 0L;
		try {
			Calendar calendar= Calendar.getInstance();
			calendar.setTimeInMillis(this.orden.getRegistro().getTime());
      path.append(Configuracion.getInstance().getPropiedadSistemaServidor("notascreditos"));
      temp.append(JsfBase.getAutentifica().getEmpresa().getNombreCorto().replaceAll(" ", ""));
      temp.append("/");
      temp.append(Calendar.getInstance().get(Calendar.YEAR));
      temp.append("/");
      temp.append(Fecha.getNombreMes(calendar.get(Calendar.MONTH)).toUpperCase());
      temp.append("/");
      temp.append(this.proveedor.getClave());
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
			if(event.getFile().getFileName().toUpperCase().endsWith(EFormatos.XML.name())) {
			  this.xml= new Importado(event.getFile().getFileName().toUpperCase(), event.getFile().getContentType(), EFormatos.XML, event.getFile().getSize(), fileSize.equals(0L) ? fileSize: fileSize/1024, event.getFile().equals(0L)? " Bytes": " Kb", temp.toString(), (String)this.attrs.get("observaciones"));
				this.toReadFactura(result);
 				this.attrs.put("xml", this.xml.getName()); 
			} //
			else
			  if(event.getFile().getFileName().toUpperCase().endsWith(EFormatos.PDF.name())) {
			    this.pdf= new Importado(event.getFile().getFileName().toUpperCase(), event.getFile().getContentType(), EFormatos.PDF, event.getFile().getSize(), fileSize.equals(0L) ? fileSize: fileSize/1024, event.getFile().equals(0L)? " Bytes": " Kb", temp.toString(), (String)this.attrs.get("observaciones"));
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
	
	private void toWriteFile(File result, InputStream upload) throws Exception {
		FileOutputStream fileOutputStream= new FileOutputStream(result);
		InputStream inputStream          = upload;
		byte[] buffer                    = new byte[BUFFER_SIZE];
		int bulk;
		while(true) {
			bulk= inputStream.read(buffer);
			if (bulk < 0) 
				break;        
			fileOutputStream.write(buffer, 0, bulk);
			fileOutputStream.flush();
		} // while
		fileOutputStream.close();
		inputStream.close();
	} 

	private void toReadFactura(File file) throws Exception {
    Reader reader            = null;
		ComprobanteFiscal factura= null;
		List<Articulo> faltantes = null;
		try {
			faltantes= new ArrayList<>();
			reader = new Reader(file.getAbsolutePath());
			factura= reader.execute();
			this.emisor  = factura.getEmisor();
			this.receptor= factura.getReceptor();
			for (Concepto concepto: factura.getConceptos()) {
		    //this(sinIva, tipoDeCambio, nombre, codigo, costo, descuento, idOrdenCompra, extras, importe, propio, iva, totalImpuesto, subTotal, cantidad, idOrdenDetalle, idArticulo, totalDescuentos, idProveedor, ultimo, solicitado, stock, excedentes, sat, unidadMedida);
		    faltantes.add(new Articulo(
				  true,
					1L,
					concepto.getDescripcion(),
					concepto.getNoIdentificacion(),
					Numero.toRedondearSat(Double.parseDouble(concepto.getValorUnitario())),
					"",
					-1L,
					"",
					0D,
					"",
					Double.parseDouble(concepto.getTraslado().getTasaCuota())* 100,
					0D,
					Numero.toRedondearSat(Double.parseDouble(concepto.getImporte())),
					Double.parseDouble(concepto.getCantidad()),
					-1L,
					new Random().nextLong(),
					0D,
					-1L,
					false,
					false,
					0D,
					0D,
					concepto.getClaveProdServ(),
					concepto.getUnidad(),
					2L
				));
			} // for
			Collections.sort(faltantes);
			this.attrs.put("faltantes", faltantes);
		} // try
		catch (Exception e) {
			throw e;
		} // catch
	}
	
	public void doTabChange(TabChangeEvent event) {
		if(event.getTab().getTitle().equals("Archivos")) 
			this.doLoadImportados();
		else
		  if(event.getTab().getTitle().equals("Importar")) 
				this.doLoadFiles();
	}		

	private void doLoadFiles() {
		TcManticCreditosArchivosDto tmp= null;
		if(this.orden.getIdCreditoNota()> 0) {
			Map<String, Object> params=null;
			try {
				params=new HashMap<>();
				params.put("idCreditoNota", this.orden.getIdCreditoNota());
				params.put("idTipoArchivo", 1L);
				tmp= (TcManticCreditosArchivosDto)DaoFactory.getInstance().findFirst(TcManticCreditosArchivosDto.class, "exists", params);
				if(tmp!= null) {
					this.xml= new Importado(tmp.getNombre(), "XML", EFormatos.XML, 0L, tmp.getTamanio(), "", tmp.getRuta(), tmp.getObservaciones());
					this.toReadFactura(new File(tmp.getAlias()));
  				this.attrs.put("xml", this.xml.getName()); 
				} // if	
				params.put("idTipoArchivo", 2L);
				tmp= (TcManticCreditosArchivosDto)DaoFactory.getInstance().findFirst(TcManticCreditosArchivosDto.class, "exists", params);
				if(tmp!= null) {
					this.pdf= new Importado(tmp.getNombre(), "PDF", EFormatos.PDF, 0L, tmp.getTamanio(), "", tmp.getRuta(), tmp.getObservaciones());
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

	public StreamedContent doFileDownload(UISelectEntity file) {
		StreamedContent regresar= null;
		try {
		  InputStream stream = new FileInputStream(new File(file.toString("alias")));
			if(file.toLong("idTipoArchivo").equals(1L))
		    regresar= new DefaultStreamedContent(stream, EFormatos.XML.getContent(), file.toString("nombre"));
			else
		    regresar= new DefaultStreamedContent(stream, EFormatos.PDF.getContent(), file.toString("nombre"));
		} // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
		return regresar;
	}

  public String doCancelar() {   
  	JsfBase.setFlashAttribute("idCreditoNota", this.idCreditoNota);
    return ((String)this.attrs.get("retorno")).concat(Constantes.REDIRECIONAR);
  } // doCancelar


	public void doViewPdfDocument(UISelectEntity item) {
		this.toCopyDocument(item.toString("alias"), item.toString("nombre"));
	}
	
	public void doViewDocument() {
		this.toCopyDocument(Configuracion.getInstance().getPropiedadSistemaServidor("notascreditos").concat(this.pdf.getRuta()).concat(this.pdf.getName()), this.pdf.getName());
	}

	public void doViewXmlDocument(UISelectEntity item) {
		this.doViewFile(item.toString("alias"));
	}

	public void doViewFile() {
		this.doViewFile(Configuracion.getInstance().getPropiedadSistemaServidor("notascreditos").concat(this.xml.getRuta()).concat(this.xml.getName()));
	}
	
	private void doViewFile(String nameXml) {
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
  }
	
  private void toCopyDocument(String alias, String name) {
		try {
			this.attrs.put("temporal", JsfBase.getContext().concat("/").concat(Constantes.RUTA_TEMPORALES).concat(name).concat("?pfdrid_c=true"));
  		File file= new File(JsfBase.getRealPath(Constantes.RUTA_TEMPORALES).concat(name));
	  	FileInputStream input= new FileInputStream(new File(alias));
      this.toWriteFile(file, input);		
			RequestContext.getCurrentInstance().update("dialogoPDF");
		} // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
	}	

	public String doAceptar() {
		String regresar= null;
		try {
			Importados importados= new Importados(this.orden, this.xml, this.pdf);
      if(importados.ejecutar(EAccion.AGREGAR)) {
      	RequestContext.getCurrentInstance().execute("janal.alert('Se importaron los archivos de forma correcta !');");
				regresar= this.doCancelar();
			} // if
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);
		} // catch
    return regresar;
	}

	public void doCerrar() {
		try {
			String name= (String)this.attrs.get("temporal");
			File file= new File(JsfBase.getRealPath(name.substring(0, name.lastIndexOf("?"))));
			file.delete();
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);
		} // catch
	}
	
	public void doUpdateRfc() {
		try {
			this.proveedor.setRfc(this.emisor.getRfc());
			if(DaoFactory.getInstance().update(this.proveedor)>= 1L)
				RequestContext.getCurrentInstance().execute("janal.alert('Proveedor actualizado de forma correcta, con RFC "+ this.proveedor.getRfc()+ " !');");
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);
		} // catch		
	}
	
}