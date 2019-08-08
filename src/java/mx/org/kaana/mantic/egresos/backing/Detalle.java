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
import java.util.List;
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
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.EFormatos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatLazyModel;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.archivo.Archivo;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.db.dto.TcManticEgresosDto;
import mx.org.kaana.mantic.egresos.reglas.Transaccion;
import mx.org.kaana.mantic.enums.ECuentasEgresos;
import mx.org.kaana.mantic.inventarios.comun.IBaseImportar;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@Named(value = "manticEgresosDetalle")
@ViewScoped
public class Detalle extends IBaseImportar implements Serializable {

  private static final long serialVersionUID= 8793667741599428879L;
	private FormatLazyModel egresosNotasEntradas;
	private FormatLazyModel egresosCreditosNotas;
	private FormatLazyModel egresosEmpresasPagos;
	private FormatLazyModel egresosNotas;		

	public FormatLazyModel getEgresosNotasEntradas() {
		return egresosNotasEntradas;
	}

	public FormatLazyModel getEgresosCreditosNotas() {
		return egresosCreditosNotas;
	}

	public FormatLazyModel getEgresosEmpresasPagos() {
		return egresosEmpresasPagos;
	}

	public FormatLazyModel getEgresosNotas() {
		return egresosNotas;
	}	
	
  @PostConstruct
  @Override
  protected void init() {		
		Long idEgreso = -1L;
		Long idEstatus= 1L;
    try {    	            
			idEgreso= (Long) JsfBase.getFlashAttribute("idEgreso");			
      this.attrs.put("egreso", DaoFactory.getInstance().findById(TcManticEgresosDto.class, idEgreso));  
			idEstatus= ((TcManticEgresosDto)this.attrs.get("egreso")).getIdEgresoEstatus();
			this.attrs.put("estatus", idEstatus.equals(1L) ? "REGISTRADO" : (idEstatus.equals(2L) ? "INCOMPLETO" : "COMPLETO"));
      this.attrs.put("idEgreso", idEgreso);  
			this.attrs.put("skipComponentsXls", "javax.faces.component.UIComponent,org.primefaces.component.outputpanel.OutputPanel,org.primefaces.component.menuitem.UIMenuItem,org.primefaces.component.separator.UISeparator,org.primefaces.component.rowtoggler.RowToggler, org.primefaces.component.menubutton.MenuButton");
			this.attrs.put("skipComponentsPdf", "org.primefaces.component.outputpanel.OutputPanel,com.sun.faces.facelets.component.UIRepeat,org.primefaces.component.menuitem.UIMenuItem,org.primefaces.component.separator.UISeparator,org.primefaces.component.rowtoggler.RowToggler,org.primefaces.component.menubutton.MenuButton");
			doLoad();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init	
	
  @Override
  public void doLoad() {    		
    try {      			
			doLoadNotasEntradas();
			doLoadCreditosNotas();
			doLoadEmpresasPagos();
			doLoadNotas();			
			doLoadImportados();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    finally {
    } // finally		
  } // doLoad	

	private void doLoadImportados() {
		List<Columna> columns= null;
		try {
			columns= new ArrayList<>();
      columns.add(new Columna("ruta", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("usuario", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("observaciones", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA_CORTA));
		  this.attrs.put("importados", UIEntity.build("VistaEgresosDto", "importados", ((TcManticEgresosDto)this.attrs.get("egreso")).toMap(), columns));
		} // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
    finally {
      Methods.clean(columns);
    }// finally
  } // doLoadImportados
	
	private void doLoadNotasEntradas() throws Exception{
		List<Columna> campos= null;
		try {
			campos= new ArrayList<>();
			campos.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA_CORTA));
			this.egresosNotasEntradas= new FormatLazyModel("VistaEgresosDto", "notasEntradas", this.attrs, campos);			
			UIBackingUtilities.resetDataTable("tablaNotasEntrada");
		} // try
		catch (Exception e) {			
			throw e; 
		} // catch		
		finally{
			Methods.clean(campos);
		} // finally
	} // doLoadNotasEntradas
	
	private void doLoadCreditosNotas() throws Exception{
		List<Columna> campos= null;
		try {
			campos= new ArrayList<>();
			campos.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA_CORTA));
			this.egresosCreditosNotas= new FormatLazyModel("VistaEgresosDto", "creditosNotas", this.attrs, campos);	
			UIBackingUtilities.resetDataTable("tablaCreditosNotas");
		} // try
		catch (Exception e) {			
			throw e; 
		} // catch		
		finally{
			Methods.clean(campos);
		} // finally
	} // doLoadCreditosNotas
	
	private void doLoadEmpresasPagos() throws Exception{
		List<Columna> campos= null;
		try {
			campos= new ArrayList<>();
			campos.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA_CORTA));
			this.egresosEmpresasPagos= new FormatLazyModel("VistaEgresosDto", "empresasPagos", this.attrs, campos);			
			UIBackingUtilities.resetDataTable("tablaEmpresasPagos");
		} // try
		catch (Exception e) {			
			throw e; 
		} // catch		
		finally{
			Methods.clean(campos);
		} // finally
	} // doLoadEmpresasPagos
	
	private void doLoadNotas() throws Exception{
		List<Columna> campos= null;
		try {
			campos= new ArrayList<>();
			campos.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA_CORTA));
			this.egresosNotas= new FormatLazyModel("VistaEgresosDto", "notas", this.attrs, campos);
			UIBackingUtilities.resetDataTable("tablaNotas");
		} // try
		catch (Exception e) {			
			throw e; 
		} // catch		
		finally{
			Methods.clean(campos);
		} // finally
	} // doLoadNotas
	
  public String doCancelar() {   
  	JsfBase.setFlashAttribute("idEgreso", this.attrs.get("idEgreso"));
    return "filtro".concat(Constantes.REDIRECIONAR);
  } // doCancelar
	
	public void doEliminarNotaEntrada(){
		Transaccion transaccion= null;
		Entity notaEntrada     = null;
		try {			
			notaEntrada= (Entity)this.attrs.get("notaEntrada");			
			transaccion= new Transaccion(notaEntrada.toLong("idEmpresaPago"), ECuentasEgresos.EMPRESA_PAGO);
			if(transaccion.ejecutar(EAccion.ELIMINAR))
				JsfBase.addMessage("Se eliminó correctamente el registro", ETipoMensaje.INFORMACION);
			else
				JsfBase.addMessage("No fue porsible eliminar el registro", ETipoMensaje.INFORMACION);
			doLoadNotasEntradas();
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch			
	} // doEliminarNotaEntrada

	public void doEliminarCreditoNota(){
		Transaccion transaccion= null;
		Entity creditoNota     = null;
		try {			
			creditoNota= (Entity)this.attrs.get("creditoNota");			
			transaccion= new Transaccion(creditoNota.toLong("idEmpresaPago"), ECuentasEgresos.EMPRESA_PAGO);
			if(transaccion.ejecutar(EAccion.ELIMINAR))
				JsfBase.addMessage("Se eliminó correctamente el registro", ETipoMensaje.INFORMACION);
			else
				JsfBase.addMessage("No fue porsible eliminar el registro", ETipoMensaje.INFORMACION);
			doLoadCreditosNotas();
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch			
	} // doEliminarCreditoNota
	
	public void doEliminarEmpresaPago(){
		Transaccion transaccion= null;
		Entity empresaPago     = null;
		try {			
			empresaPago= (Entity)this.attrs.get("empresaPago");			
			transaccion= new Transaccion(empresaPago.getKey(), ECuentasEgresos.EMPRESA_PAGO);
			if(transaccion.ejecutar(EAccion.ELIMINAR))
				JsfBase.addMessage("Se eliminó correctamente el registro", ETipoMensaje.INFORMACION);
			else
				JsfBase.addMessage("No fue porsible eliminar el registro", ETipoMensaje.INFORMACION);			
			doLoadEmpresasPagos();
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch			
	} // doEliminarEmpresaPago
	
	public void doEliminarNota(){
		Transaccion transaccion= null;
		Entity nota            = null;
		try {			
			nota= (Entity)this.attrs.get("nota");			
			transaccion= new Transaccion(nota.getKey(), ECuentasEgresos.NOTA);
			if(transaccion.ejecutar(EAccion.ELIMINAR))
				JsfBase.addMessage("Se eliminó correctamente el registro", ETipoMensaje.INFORMACION);
			else
				JsfBase.addMessage("No fue porsible eliminar el registro", ETipoMensaje.INFORMACION);			
			doLoadNotas();
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch			
	} // doEliminarEmpresaPago
	
	@Override
	public StreamedContent doFileDownload(UISelectEntity file) {
		return doFileDownloadEntity((Entity)file);
	}
	
	public StreamedContent doFileDownloadEntity(Entity file) {
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
        JsfBase.addMessage("No existe el archivo:"+ file.toString("nombre")+ ", favor de verificarlo.");
			} // else	
		} // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
		return regresar;
	} // doFileDownload  

	@Override
	public void doViewPdfDocument(UISelectEntity item) {
		this.toCopyDocument(item.toString("alias"), item.toString("nombre"));
	} // doViewPdfDocument

	@Override
	public void doViewXmlDocument(UISelectEntity item) {
		this.doViewFile(item.toString("alias"));
	} // doViewXmlDocument	
	
	@Override
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
	
	@Override
	public void toCopyDocument(String alias, String name) {
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
	
}