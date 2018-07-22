package mx.org.kaana.mantic.inventarios.creditos.backing;

import static codec.pkcs7.Verifier.BUFFER_SIZE;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.EFormatos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.formato.Global;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.articulos.beans.Importado;
import mx.org.kaana.mantic.compras.ordenes.beans.Articulo;
import mx.org.kaana.mantic.inventarios.creditos.reglas.Transaccion;
import mx.org.kaana.mantic.db.dto.TcManticDevolucionesDto;
import mx.org.kaana.mantic.db.dto.TcManticCreditosArchivosDto;
import mx.org.kaana.mantic.db.dto.TcManticNotasEntradasDto;
import mx.org.kaana.mantic.inventarios.creditos.beans.NotaCredito;
import mx.org.kaana.mantic.libs.factura.beans.ComprobanteFiscal;
import mx.org.kaana.mantic.libs.factura.beans.Concepto;
import mx.org.kaana.mantic.libs.factura.reglas.Reader;
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

@Named(value= "manticInventariosCreditosAccion")
@ViewScoped
public class Accion extends IBaseAttribute implements Serializable {

  private static final long serialVersionUID = 327393488565639361L;

	private Long idTipoCreditoNota;
	private EAccion accion;
	private NotaCredito orden;
	private Importado xml;
	private Importado pdf;

	public String getAgregar() {
		return this.accion.equals(EAccion.AGREGAR)? "none": "";
	}

	public Boolean getModificar() {
		return this.accion.equals(EAccion.MODIFICAR);
	}

	public NotaCredito getOrden() {
		return orden;
	}

	public void setOrden(NotaCredito orden) {
		this.orden=orden;
	}

	public Long getIdTipoCreditoNota() {
		return idTipoCreditoNota;
	}

	public Importado getXml() {
		return xml;
	}

	public void setXml(Importado xml) {
		this.xml=xml;
	}

	public Importado getPdf() {
		return pdf;
	}

	public void setPdf(Importado pdf) {
		this.pdf=pdf;
	}
	
  @Override
	@PostConstruct
  protected void init() {		
    try {
			if(JsfBase.getFlashAttribute("accion")== null)
				RequestContext.getCurrentInstance().execute("janal.isPostBack('cancelar')");
      this.idTipoCreditoNota= JsfBase.getFlashAttribute("idTipoCreditoNota")== null? -1L: (Long)JsfBase.getFlashAttribute("idTipoCreditoNota");
      this.accion = JsfBase.getFlashAttribute("accion")== null? EAccion.AGREGAR: (EAccion)JsfBase.getFlashAttribute("accion");
      this.attrs.put("idCreditoNota", JsfBase.getFlashAttribute("idCreditoNota")== null? -1L: JsfBase.getFlashAttribute("idCreditoNota"));
      this.attrs.put("idDevolucion", JsfBase.getFlashAttribute("idDevolucion")== null? -1L: JsfBase.getFlashAttribute("idDevolucion"));
      this.attrs.put("idNotaEntrada", JsfBase.getFlashAttribute("idNotaEntrada")== null? -1L: JsfBase.getFlashAttribute("idNotaEntrada"));
      this.attrs.put("idProveedor", -1L);
			this.attrs.put("retorno", JsfBase.getFlashAttribute("retorno")== null? "filtro": JsfBase.getFlashAttribute("retorno"));
			this.attrs.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
			this.attrs.put("formatos", Constantes.PATRON_IMPORTAR_FACTURA);
			this.attrs.put("carpeta", "TEMPORAL");
			doLoad();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

  public void doLoad() {
    try {
      this.attrs.put("nombreAccion", Cadena.letraCapital(this.accion.name()));
      switch (this.accion) {
        case AGREGAR:											
          this.orden= new NotaCredito(-1L, (Long)this.attrs.get("idDevolucion"));
					this.orden.setIdTipoCreditoNota(this.idTipoCreditoNota);
          break;
        case MODIFICAR:					
        case CONSULTAR:					
          this.orden= (NotaCredito)DaoFactory.getInstance().toEntity(NotaCredito.class, "TcManticCreditosNotasDto", "detalle", this.attrs);
					this.orden.setIkDevolucion(new UISelectEntity(new Entity(this.orden.getIdDevolucion())));
					this.orden.setIkNotaEntrada(new UISelectEntity(new Entity(this.orden.getIdNotaEntrada())));
					this.orden.setIkProveedor(new UISelectEntity(new Entity(this.orden.getIdProveedor())));
					this.attrs.put("idTipoCreditoNota", this.orden.getIdTipoCreditoNota());
          break;
      } // switch
			this.toLoadCatalog();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // doLoad

  public String doAceptar() {  
    Transaccion transaccion= null;
    String regresar        = null;
    try {			
			transaccion = new Transaccion(this.orden, (Double)this.attrs.get("importe"), this.xml, this.pdf);
			if (transaccion.ejecutar(this.accion)) {
				if(this.accion.equals(EAccion.AGREGAR)) {
 				  regresar = this.attrs.get("retorno").toString().concat(Constantes.REDIRECIONAR);
   			  RequestContext.getCurrentInstance().execute("janal.back(' generó la nota de crédito ', '"+ this.orden.getConsecutivo()+ "');");
				} // if	
 				if(!this.accion.equals(EAccion.CONSULTAR)) 
  				JsfBase.addMessage("Se ".concat(this.accion.equals(EAccion.AGREGAR) ? "agregó" : this.accion.equals(EAccion.COMPLETO) ? "aplicó": "modificó").concat(" la nota de credito."), ETipoMensaje.INFORMACION);
  			JsfBase.setFlashAttribute("idCreditoNota", ((NotaCredito)this.orden).getIdCreditoNota());
			} // if
			else 
				JsfBase.addMessage("Ocurrió un error al registrar la nota de crédito.", ETipoMensaje.ERROR);      			
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    return regresar;
  } // doAccion
	
  public String doCancelar() {   
  	JsfBase.setFlashAttribute("idCreditoNota",this.orden.getIdCreditoNota());
    return (String)this.attrs.get("retorno");
  } 

	private void toLoadCatalog() {
		List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
		Value importe             = null; 
    try {
			columns= new ArrayList<>();
      columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("razonSocial", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("total", EFormatoDinamicos.MONEDA_SAT_DECIMALES));
			params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
			switch(this.idTipoCreditoNota.intValue()) {
				case 1:
					importe= DaoFactory.getInstance().toField("VistaCreditosNotasDto", "parcial", this.attrs, "total");
					if(importe.getData()!= null)
						this.attrs.put("parcial", importe.toDouble());
					else
						this.attrs.put("parcial", 0D);
    			TcManticDevolucionesDto devolucion= (Long)this.attrs.get("idDevolucion")< 0L? new TcManticDevolucionesDto(): (TcManticDevolucionesDto)DaoFactory.getInstance().findById(TcManticDevolucionesDto.class, (Long)this.attrs.get("idDevolucion"));
					this.orden.setImporte(Numero.toRedondearSat(devolucion.getTotal()- (Double)this.attrs.get("parcial")));
					this.attrs.put("importe", this.orden.getImporte());
					this.attrs.put("total", Global.format(EFormatoDinamicos.MONEDA_SAT_DECIMALES, devolucion.getTotal()));
					this.attrs.put("parcial", Global.format(EFormatoDinamicos.MONEDA_SAT_DECIMALES, (Double)this.attrs.get("parcial")));
					params.put("idDevolucion", this.attrs.get("idDevolucion"));
					if((Long)this.attrs.get("idDevolucion")> 0L) {
						this.attrs.put("devoluciones", UIEntity.build("VistaCreditosNotasDto", "devoluciones", params, columns));
						List<UISelectEntity> devoluciones= (List<UISelectEntity>)this.attrs.get("devoluciones");
						if(devoluciones!= null && !devoluciones.isEmpty()) {
							if(this.accion.equals(EAccion.AGREGAR))
							  this.orden.setIkDevolucion(devoluciones.get(0));
						  else
                this.orden.setIkDevolucion(devoluciones.get(devoluciones.indexOf(this.orden.getIkDevolucion())));							
						  this.attrs.put("carpeta", this.orden.getIkDevolucion().toString("clave"));
						} // if	
					} // if	
					break;
				case 2:
					importe= DaoFactory.getInstance().toField("VistaCreditosNotasDto", "total", this.attrs, "total");
					if(importe.getData()!= null)
						this.attrs.put("parcial", importe.toDouble());
					else
						this.attrs.put("parcial", 0D);
    			TcManticNotasEntradasDto notaEntrada= (Long)this.attrs.get("idNotaEntrada")< 0L? new TcManticNotasEntradasDto(): (TcManticNotasEntradasDto)DaoFactory.getInstance().findById(TcManticNotasEntradasDto.class, (Long)this.attrs.get("idNotaEntrada"));
					this.orden.setImporte(Numero.toRedondearSat(notaEntrada.getTotal()- (Double)this.attrs.get("parcial")));
					this.attrs.put("importe", this.orden.getImporte());
					this.attrs.put("total", Global.format(EFormatoDinamicos.MONEDA_SAT_DECIMALES, notaEntrada.getTotal()));
					this.attrs.put("parcial", Global.format(EFormatoDinamicos.MONEDA_SAT_DECIMALES, (Double)this.attrs.get("parcial")));
					params.put("idNotaEntrada", this.attrs.get("idNotaEntrada"));
					if((Long)this.attrs.get("idNotaEntrada")> 0L) {
						this.attrs.put("notas", UIEntity.build("VistaCreditosNotasDto", "notas", params, columns));
						List<UISelectEntity> notas= (List<UISelectEntity>)this.attrs.get("notas");
						if(notas!= null && !notas.isEmpty()) {
 							if(this.accion.equals(EAccion.AGREGAR))
  							this.orden.setIkNotaEntrada(notas.get(0));
						  else
                this.orden.setIkNotaEntrada(notas.get(notas.indexOf(this.orden.getIkNotaEntrada())));							
   						this.attrs.put("carpeta", this.orden.getIkNotaEntrada().toString("clave"));
						} // if	
					} // if	
					break;
				case 3:
					params.put("idProveedor", this.attrs.get("idProveedor"));
					this.orden.setImporte(0D);
					columns.remove(columns.size()- 1);
					this.attrs.put("proveedores", UIEntity.build("VistaOrdenesComprasDto", "moneda", params, columns));
					List<UISelectEntity> proveedores= (List<UISelectEntity>)this.attrs.get("proveedores");
					if(proveedores!= null && !proveedores.isEmpty()) {
						if(this.accion.equals(EAccion.AGREGAR))
							this.orden.setIkProveedor(proveedores.get(0));
						else
							this.orden.setIkProveedor(proveedores.get(proveedores.indexOf(this.orden.getIkProveedor())));							
 						this.attrs.put("carpeta", this.orden.getIkProveedor().toString("clave"));
					} // if	
					break;
			} // switch
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
      temp.append(this.attrs.get("carpeta"));
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
			} //
			else
			  if(event.getFile().getFileName().toUpperCase().endsWith(EFormatos.PDF.name())) 
			    this.pdf= new Importado(event.getFile().getFileName().toUpperCase(), event.getFile().getContentType(), EFormatos.PDF, event.getFile().getSize(), fileSize.equals(0L) ? fileSize: fileSize/1024, event.getFile().equals(0L)? " Bytes": " Kb", temp.toString(), (String)this.attrs.get("observaciones"));
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
			for (Concepto concepto: factura.getConceptos()) {
		    //this(sinIva, tipoDeCambio, nombre, codigo, costo, descuento, idOrdenCompra, extras, importe, propio, iva, totalImpuesto, subTotal, cantidad, idOrdenDetalle, idArticulo, totalDescuentos, idProveedor, ultimo, solicitado, stock, excedentes, sat, unidadMedida);
		    faltantes.add(new Articulo(
				  true,
					-1L,
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
					this.orden.getIdProveedor(),
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
		TcManticCreditosArchivosDto tmp= null;
		if(event.getTab().getTitle().equals("Importar")) {
			Long idCreditoNota= this.orden.getIdCreditoNota();
			if(idCreditoNota!= null && idCreditoNota> 0) {
				Map<String, Object> params=null;
				try {
					params=new HashMap<>();
					params.put("idCreditoNota", idCreditoNota);
					params.put("idTipoArchivo", 1L);
				  tmp= (TcManticCreditosArchivosDto)DaoFactory.getInstance().findFirst(TcManticCreditosArchivosDto.class, "exists", params);
					if(tmp!= null) 
						this.xml= new Importado(tmp.getNombre(), "XML", EFormatos.XML, 0L, tmp.getTamanio(), "", tmp.getRuta(), tmp.getObservaciones());
					params.put("idTipoArchivo", 2L);
				  tmp= (TcManticCreditosArchivosDto)DaoFactory.getInstance().findFirst(TcManticCreditosArchivosDto.class, "exists", params);
					if(tmp!= null) 
						this.pdf= new Importado(tmp.getNombre(), "PDF", EFormatos.PDF, 0L, tmp.getTamanio(), "", tmp.getRuta(), tmp.getObservaciones());
				} // try
				catch (Exception e) {
					Error.mensaje(e);
          JsfBase.addMessageError(e);
				} // catch
				finally {
					Methods.clean(params);
				} // finally
			} // if
		} // if
	}
	
	public StreamedContent doFileDownload() {
		StreamedContent regresar= null;
		try {
		  InputStream stream = new FileInputStream(new File(Configuracion.getInstance().getPropiedadSistemaServidor("notascreditos").concat(this.pdf.getRuta()).concat(this.pdf.getName())));
	    regresar= new DefaultStreamedContent(stream, "application/pdf", this.pdf.getName());
		} // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
		return regresar;
	}
	
	public void doViewDocument() {
		try {
			this.attrs.put("temporal", JsfBase.getContext().concat("/").concat(Constantes.RUTA_TEMPORALES).concat(this.pdf.getName()).concat("?pfdrid_c=true"));
			String name= JsfBase.getRealPath(Constantes.RUTA_TEMPORALES).concat(this.pdf.getName());
  		File file= new File(name);
	  	FileInputStream input= new FileInputStream(new File(Configuracion.getInstance().getPropiedadSistemaServidor("notascreditos").concat(this.pdf.getRuta()).concat(this.pdf.getName())));
      this.toWriteFile(file, input);		
		} // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
	}
	
	public void doViewFile() {
		this.doViewFile(Configuracion.getInstance().getPropiedadSistemaServidor("notascreditos").concat(this.xml.getRuta()).concat(this.xml.getName()));
	}
	
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

	public void doCerrar() {
		try {
			String name= (String)this.attrs.get("temporal");
			if(name.endsWith("XML"))
				name= JsfBase.getContext().concat(name);
			else
				name= name.substring(0, name.lastIndexOf("?"));
			File file= new File(JsfBase.getRealPath(name));
			file.delete();
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);
		} // catch
	}

}