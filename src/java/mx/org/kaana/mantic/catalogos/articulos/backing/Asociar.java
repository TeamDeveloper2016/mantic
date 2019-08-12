package mx.org.kaana.mantic.catalogos.articulos.backing;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.EFormatos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.procesos.comun.Comun;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.archivo.Archivo;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.recurso.LoadImages;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.articulos.beans.Importado;
import mx.org.kaana.mantic.catalogos.articulos.reglas.Transaccion;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.StreamedContent;

@Named(value = "manticCatalogosArticulosAsociar")
@ViewScoped
public class Asociar extends Comun implements Serializable {

  private static final long serialVersionUID= 8793667741599428879L;
	private static final String BYTES         = " Bytes";	
	private static final String K_BYTES       = " Kb";	
	private Importado importado;
	private StreamedContent image;
	protected Entity[] selecteds;	

	public Entity[] getSelecteds() {
		return selecteds;
	}

	public void setSelecteds(Entity[] selecteds) {
		this.selecteds=selecteds;
	}
	
	public Importado getImportado() {
		return importado;
	}

	public void setImportado(Importado importado) {
		this.importado = importado;
	}
	
	public StreamedContent getImage() {
		return image;
	}
	
  @PostConstruct
  @Override
  protected void init() {
    try {    	      
      this.attrs.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());   
			this.attrs.put("idNotaEntrada", JsfBase.getFlashAttribute("idNotaEntrada")== null? -1L: JsfBase.getFlashAttribute("idNotaEntrada"));
			this.attrs.put("retorno", JsfBase.getFlashAttribute("retorno")== null? "/Paginas/Mantic/Inventarios/Entradas/filtro" : JsfBase.getFlashAttribute("retorno"));
			this.image= LoadImages.getImage(-1L);
			doLoad();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

  @Override
  public void doLoad() {
    List<Columna> columns     = null;
		Map<String, Object> params= null;
    try {
      columns = new ArrayList<>();
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("propio", EFormatoDinamicos.MAYUSCULAS));
			params= new HashMap<>();
      params.put("idNotaEntrada", this.attrs.get("idNotaEntrada"));
      this.lazyModel = new FormatCustomLazy("VistaNotasEntradasDto", "detalleImage", params, columns);
      UIBackingUtilities.resetDataTable();
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
		String genericPath= null;  
		String nameFile   = Archivo.toFormatNameFile(event.getFile().getFileName().toUpperCase());
    File result       = null;		
		Long fileSize     = 0L;
		File filePath     = null;
		try {
			if (this.importado != null && !Cadena.isVacio(this.importado.getName())) 
				doEliminarTemporal();
			genericPath= Configuracion.getInstance().getPropiedadSistemaServidor("path.image").concat(JsfBase.getAutentifica().getEmpresa().getIdEmpresa().toString()).concat("/");
			result= new File(genericPath.concat(nameFile));		
			filePath= new File(genericPath);
			if (!filePath.exists())
				filePath.mkdirs();
			if (result.exists())
				result.delete();			      
			Archivo.toWriteFile(result, event.getFile().getInputstream());
			fileSize= event.getFile().getSize();
			this.importado= new Importado(nameFile, event.getFile().getContentType(), EFormatos.FREE, event.getFile().getSize(), fileSize.equals(0L) ? fileSize : fileSize/1024, event.getFile().equals(0L) ? BYTES : K_BYTES, genericPath, "", event.getFile().getFileName());      
			this.image= LoadImages.getFile(genericPath.concat(nameFile));
			this.toMessageImage();					
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessage("Importar archivo", "El archivo no pudo ser importado.", ETipoMensaje.ERROR);
		} // catch
	} // doFileUpload	
	
	private void doEliminarTemporal() throws Exception{
		File result= null;
		try {
			liberarImage();
			if (this.importado != null && !Cadena.isVacio(this.importado.getName())) {
				result= new File(Configuracion.getInstance().getPropiedadSistemaServidor("path.image").concat(JsfBase.getAutentifica().getEmpresa().getIdEmpresa().toString()).concat("/").concat(this.importado.getName()));
				if (result.exists())
					result.delete();
			} // if						
		} // try
		catch (Exception e) {		
			throw e;
		} // catch		
	} // doEliminarTemporal
	
	private void liberarImage() throws IOException{
		if(this.image!= null){
			this.image.getStream().close();
			this.image= null;
		}	// if	
	} // liberarImage
	
	private void toMessageImage() {
		FacesMessage msg= null;
		String detail   = null;
		try {
			detail= toDetailMessage();
			msg=new FacesMessage("Actualización de versiones", detail);
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} // try
		catch (Exception e) {			
			throw e;
		} // catch				     
	} // toMessageImage
	
	private String toDetailMessage() {
		StringBuilder regresar= new StringBuilder();
		regresar.append("Nombre: ");
		regresar.append(this.importado.getName());
		regresar.append("\nTamaño: ");
		regresar.append(this.importado.getSize());
		regresar.append(this.importado.getMedicion());
		regresar.append(" \nContenido: ");
		regresar.append(this.importado.getContent());
		regresar.append("\nEl archivo fue importado con éxito.");
		return regresar.toString();
	} // toDetailMessage	
	
	public void doAceptar(){
		Transaccion transaccion= null;
		try {
			if(this.selecteds.length > 0){
				liberarImage();
				transaccion= new Transaccion(this.selecteds, this.importado);
				if(transaccion.ejecutar(EAccion.ASIGNAR)){
					JsfBase.addMessage("Asignar imagen a articulos.", "Se realizó la asignacion de la imagen de forma correcta.", ETipoMensaje.ERROR);
					doLoad();
				} // if
				else
					JsfBase.addMessage("Asignar imagen a articulos.", "Ocurrió un error al realizar la asignación de la imagen.", ETipoMensaje.ERROR);
			} // if
			else
				JsfBase.addMessage("Asignar imagen a articulos.", "Es necesario seleccionar al menos un articulo.", ETipoMensaje.ERROR);
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
	} // doAceptar
	
	public String doCancelar(){
		String regresar= null;
		try {
			doEliminarTemporal();
			JsfBase.setFlashAttribute("idNotaEntrada", this.attrs.get("idNotaEntrada"));
			regresar= this.attrs.get("retorno").toString().concat(Constantes.REDIRECIONAR);
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
		return regresar;
	} // doCancelar
	
	public void doDeleteFile(){		
		try {						
			liberarImage();
			deleteFile();																			
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
	} // deleteFile
	
	public void deleteFile(){
		String genericPath= null;		
		File image        = null;
		File imageContent = null;
		try {
			if (this.importado != null && !Cadena.isVacio(this.importado.getName())) {
				genericPath= Configuracion.getInstance().getPropiedadSistemaServidor("path.image").concat(JsfBase.getAutentifica().getEmpresa().getIdEmpresa().toString()).concat("/");
				image= new File(genericPath.concat(this.importado.getName()));
				if(image.exists())
					image.delete();
				imageContent= new File(genericPath.concat(this.importado.getContent()));
				if(imageContent.exists())
					imageContent.delete();
				this.importado= new Importado();
			} // if			
		} // try
		catch (Exception e) {			
			Error.mensaje(e);
			throw e;
		} // catch		
		finally{						
			image= null;			
			imageContent= null;			
		} // finally
	} // doDeleteFile
}