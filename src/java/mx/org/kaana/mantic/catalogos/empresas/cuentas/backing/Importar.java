package mx.org.kaana.mantic.catalogos.empresas.cuentas.backing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.Date;
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
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.archivo.Archivo;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UISelect;
import mx.org.kaana.libs.pagina.UISelectItem;
import mx.org.kaana.mantic.db.dto.TcManticEmpresasDeudasDto;
import mx.org.kaana.mantic.db.dto.TcManticNotasEntradasDto;
import mx.org.kaana.mantic.db.dto.TcManticProveedoresDto;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import mx.org.kaana.mantic.catalogos.empresas.cuentas.reglas.Transaccion;
import mx.org.kaana.mantic.db.dto.TcManticEmpresasPagosDto;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 7/05/2018
 *@time 03:29:13 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

@Named(value= "manticCatalogosEmpresasCuentasImportar")
@ViewScoped
public class Importar extends IBaseAttribute implements Serializable {

	private static final Log LOG=LogFactory.getLog(Importar.class);
  private static final long serialVersionUID= 327353488565639367L;
	
	private TcManticEmpresasDeudasDto deuda;
	private TcManticProveedoresDto proveedor;
	private TcManticNotasEntradasDto notaEntrada;
	private Long idEmpresaDeuda;
	private Importado importado;	
	
	public Importado getImportado() {
		return importado;
	}

	public TcManticEmpresasDeudasDto getDeuda() {
		return deuda;
	}

	public TcManticNotasEntradasDto getNotaEntrada() {
		return notaEntrada;
	}	

	public TcManticProveedoresDto getProveedor() {
		return proveedor;
	}
	
	@PostConstruct
  @Override
  protected void init() {		
		Map<String, Object> params        = null;
		List<UISelectItem> tiposDocumentos= null;
    try {
			if(JsfBase.getFlashAttribute("idEmpresaDeuda")== null)
				UIBackingUtilities.execute("janal.isPostBack('cancelar')");
			this.attrs.put("paginator", false);
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
			tiposDocumentos= UISelect.build("TcManticTiposComprobantesDto", "row", params, "nombre", " ", EFormatoDinamicos.MAYUSCULAS);
			this.attrs.put("tiposDocumentos", tiposDocumentos);
			this.attrs.put("tipoDocumento", UIBackingUtilities.toFirstKeySelectItem(tiposDocumentos));
      this.attrs.put("fecha", new Date(Calendar.getInstance().getTimeInMillis()));
			this.idEmpresaDeuda= JsfBase.getFlashAttribute("idEmpresaDeuda")== null? -1L: (Long)JsfBase.getFlashAttribute("idEmpresaDeuda");
			this.deuda= (TcManticEmpresasDeudasDto)DaoFactory.getInstance().findById(TcManticEmpresasDeudasDto.class, this.idEmpresaDeuda);
			if(this.deuda!= null) {
				this.notaEntrada= (TcManticNotasEntradasDto) DaoFactory.getInstance().findById(TcManticNotasEntradasDto.class, this.deuda.getIdNotaEntrada());
				this.proveedor= (TcManticProveedoresDto) DaoFactory.getInstance().findById(TcManticProveedoresDto.class, this.notaEntrada.getIdProveedor());
				this.toLoadCatalog();
			} // if
			this.attrs.put("retorno", JsfBase.getFlashAttribute("retorno")== null? "filtro": JsfBase.getFlashAttribute("retorno"));
			this.attrs.put("formatos", Constantes.PATRON_IMPORTAR);  		
			this.importado= null;			
			this.attrs.put("importado", ""); 
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
      columns.add(new Columna("persona", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("observaciones", EFormatoDinamicos.MONEDA_SAT_DECIMALES));			
			params.put("idEmpresaDeuda", this.idEmpresaDeuda);
			this.attrs.put("pagos", UIEntity.build("VistaEmpresasDto", "pagosDeuda", params, columns));
    } // try
    catch (Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    } // finally
	} // toLoadCatalog

  private void doLoadImportados() {
		List<Columna> columns= null;
		try {
			columns= new ArrayList<>();
      columns.add(new Columna("ruta", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("usuario", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("observaciones", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA_CORTA));
      columns.add(new Columna("registroPago", EFormatoDinamicos.FECHA_CORTA));
      columns.add(new Columna("pago", EFormatoDinamicos.MONEDA_CON_DECIMALES));
		  this.attrs.put("importados", UIEntity.build("VistaEmpresasDto", "importados", this.deuda.toMap(), columns));
			this.attrs.put("paginator", this.attrs.get("importados")!= null && ((List<UISelectEntity>)this.attrs.get("importados")).size()>15);
		} // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
    finally {
      Methods.clean(columns);
    }// finally
  } // doLoad

	public void doFileUpload(FileUploadEvent event) {
		StringBuilder path= new StringBuilder();  
		StringBuilder temp= new StringBuilder();  
		String nameFile   = Archivo.toFormatNameFile(event.getFile().getFileName().toUpperCase());
    File result       = null;		
		Long fileSize     = 0L;
		try {
			Calendar calendar= Calendar.getInstance();
			calendar.setTimeInMillis(this.deuda.getRegistro().getTime());
      path.append(Configuracion.getInstance().getPropiedadSistemaServidor("pagos"));
      temp.append(JsfBase.getAutentifica().getEmpresa().getIdEmpresa().toString());
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
      path.append(nameFile);
			result = new File(path.toString());
			if (result.exists())
				result.delete();			      
			Archivo.toWriteFile(result, event.getFile().getInputstream());
			fileSize= event.getFile().getSize();
			if(nameFile.endsWith(EFormatos.XML.name())) {
			  this.importado= new Importado(nameFile, event.getFile().getContentType(), EFormatos.XML, event.getFile().getSize(), fileSize.equals(0L) ? fileSize: fileSize/1024, event.getFile().equals(0L)? " Bytes": " Kb", temp.toString(), (String)this.attrs.get("observaciones"), event.getFile().getFileName().toUpperCase());
				this.toReadFactura(result);
				this.attrs.put("importado", this.importado.getName());
			} //
			else {
				this.importado= new Importado(nameFile, event.getFile().getContentType(), EFormatos.PDF, event.getFile().getSize(), fileSize.equals(0L) ? fileSize: fileSize/1024, event.getFile().equals(0L)? " Bytes": " Kb", temp.toString(), (String)this.attrs.get("observaciones"), event.getFile().getFileName().toUpperCase());
				this.attrs.put("importado", this.importado.getName());
			} // if			
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessage("Importar:", "El archivo no pudo ser importado !", ETipoMensaje.ERROR);
			if(result!= null)
			  result.delete();
		} // catch
	} // doFileUpload	
	
	private void toReadFactura(File file) throws Exception {
    Reader reader            = null;
		ComprobanteFiscal factura= null;
		List<Articulo> faltantes = null;
		try {
			faltantes= new ArrayList<>();
			reader = new Reader(file.getAbsolutePath());
			factura= reader.execute();
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
    Map<String, Object> params = null;
    try {      
      params = new HashMap<>();      
      if(event.getTab().getTitle().equals("Archivos")) 
        this.doLoadImportados();		
      else 
        if(event.getTab().getTitle().equals("Facturas") && this.notaEntrada!= null && this.notaEntrada.isValid()) {
          params.put("idNotaEntrada", this.notaEntrada.getIdNotaEntrada());      
          params.put("idTipoDocumento", 13L);      
          this.doLoadImportados("VistaNotasEntradasDto", "importados", params);
        } // if  
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);      
    } // catch	
    finally {
      Methods.clean(params);
    } // finally
	}	// doTabChange	

	protected void doLoadImportados(String proceso, String idXml, Map<String, Object> params) {
		List<Columna> columns= null;
		try {
			columns= new ArrayList<>();
      columns.add(new Columna("ruta", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("usuario", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("observaciones", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA_CORTA));
		  this.attrs.put("facturas", UIEntity.build(proceso, idXml, params, columns));
		} // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
    finally {
      Methods.clean(columns);
    } // finally
  } // doLoadImportados	
	
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
	}

  public String doCancelar() {   
  	JsfBase.setFlashAttribute("idEmpresaDeuda", this.idEmpresaDeuda);
    return ((String)this.attrs.get("retorno")).concat(Constantes.REDIRECIONAR);
  } // doCancelar

	
	public void doViewPdfDocument(UISelectEntity item) {
		this.toCopyDocument(item.toString("alias"), item.toString("nombre"));
	} // doViewPdfDocument
	
	public void doViewDocument() {
		this.toCopyDocument(Configuracion.getInstance().getPropiedadSistemaServidor("pagos").concat(this.importado.getRuta()).concat(this.importado.getName()), this.importado.getName());
	} // doViewDocument

	public void doViewXmlDocument(UISelectEntity item) {
		this.doViewFile(item.toString("alias"));
	} // doViewXmlDocument

	public void doViewFile() {
		this.doViewFile(Configuracion.getInstance().getPropiedadSistemaServidor("pagos").concat(this.importado.getRuta()).concat(this.importado.getName()));
	} // doViewFile
	
	public void doViewFile(String nameXml) {
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
			regresar= this.prettyFormat(sb.substring(3), 2);
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
	} // doViewFile
	
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
  		File source= new File(JsfBase.getRealPath(Constantes.PATH_INVOICE).concat(name));
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
	
	public void doAceptar() {		
		Transaccion transaccion= null;
		try {			
			if(this.importado!= null){
				if(this.getImportado()!= null && Cadena.isVacio(this.getImportado().getObservaciones()))
					this.getImportado().setObservaciones(this.attrs.get("observaciones")!= null? (String)this.attrs.get("observaciones"): null);
				if(this.importado.getFormat().equals(EFormatos.PDF))
					transaccion= new Transaccion(this.deuda, null, this.importado, Long.valueOf(this.attrs.get("pago").toString()), Long.valueOf(this.attrs.get("tipoDocumento").toString()), (Date)this.attrs.get("fecha"));
				else
					transaccion= new Transaccion(this.deuda, this.importado, null, Long.valueOf(this.attrs.get("pago").toString()), Long.valueOf(this.attrs.get("tipoDocumento").toString()), (Date)this.attrs.get("fecha"));
				if(transaccion.ejecutar(EAccion.REGISTRAR)) {
					UIBackingUtilities.execute("janal.alert('Se importaron los archivos de forma correcta !');");		
					this.importado= null;			
					this.attrs.put("importado", ""); 
					this.attrs.put("observaciones", "");
				} // if
			} // if
			else
				JsfBase.addMessage("Importar archivo", "Es necesario seleccionar un archivo.", ETipoMensaje.ERROR);
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);
		} // catch    
	} // doAceptar
	
	public void doCerrar() {
		try {
//			String name= (String)this.attrs.get("temporal");
//			if(name.endsWith("XML"))
//				name= JsfBase.getContext().concat(name);
//			else
//				name= name.substring(0, name.lastIndexOf("?"));
//			File file= new File(JsfBase.getRealPath(name));
//			file.delete();
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);
		} // catch
	}	// doCerrar
	
	public void doEliminar(UISelectEntity item){
		Transaccion transaccion= null;
		try {
			transaccion= new Transaccion(item.getKey());
			if(transaccion.ejecutar(EAccion.ELIMINAR)){
				JsfBase.addMessage("Eliminar documento", "El documento se eliminó de forma correcta", ETipoMensaje.INFORMACION);
				doLoadImportados();
			} // if
			else
				JsfBase.addMessage("Eliminar documento", "Ocurrió un error al eliminar el documento", ETipoMensaje.ERROR);
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
	} // doEliminar
}