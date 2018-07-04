package mx.org.kaana.mantic.catalogos.articulos.beans;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.kajool.enums.EFormatos;
import mx.org.kaana.kajool.enums.ESql;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.mantic.catalogos.articulos.reglas.MotorBusqueda;
import mx.org.kaana.mantic.db.dto.TcManticArticulosDto;
import mx.org.kaana.mantic.db.dto.TrManticEmpaqueUnidadMedidaDto;
import org.primefaces.event.FileUploadEvent;

public class RegistroArticulo implements Serializable{

	private static final long serialVersionUID= 2173243980013765240L;
	private static final int BUFFER_SIZE      = 6124;
	private static final String BYTES         = " Bytes";	
	private static final String K_BYTES       = " Kb";	
	
	private Long idArticulo;
	private Long idEmpaque;
	private String observaciones;
	private TcManticArticulosDto articulo;
	private List<ArticuloCodigo> articulosCodigos;
	private ArticuloCodigo articulosCodigosSeleccion;
	private List<Especificacion> especificaciones;
	private Especificacion especificacionSeleccion;
	private List<Descuento> articulosDescuentos;
	private Descuento articuloDescuentoSeleccion;
	private List<DescuentoEspecial> clientesDescuentos;
	private DescuentoEspecial clienteDescuentoSeleccion;
	private List<PrecioSugerido> preciosSugeridos;
	private PrecioSugerido precioSugeridoSeleccion;
	private List<ArticuloProveedor> articulosProveedores;
	private ArticuloProveedor articuloProveedorSeleccion;
	private List<TipoVenta> articulosTiposVenta;
	private TipoVenta articuloTiposVentaSeleccion;
	private ArticuloDimencion articuloDimencion;
	private boolean redondear;
	private ContadoresListas contadores;
	private Long countIndice;
	private List<IBaseDto> deleteList;
	private Importado importado;

	public RegistroArticulo() {
		this(-1L, 
				new TcManticArticulosDto(),
				new ArrayList<ArticuloCodigo>(),
				new ArrayList<Especificacion>(),
				new ArrayList<Descuento>(),
				new ArrayList<DescuentoEspecial>(),
				new ArrayList<PrecioSugerido>(),
				new ArrayList<ArticuloProveedor>(),
				new ArrayList<TipoVenta>(),
				-1L, null, false, null, -1L,
				new ArticuloDimencion()
				);
	} // RegistroArticulo
	
	public RegistroArticulo(Long idArticulo) {
		this.idArticulo  = idArticulo;
		this.contadores  = new ContadoresListas();
		this.countIndice = 0L;
		this.deleteList  = new ArrayList<>();
		this.importado   = new Importado();
		init();		
	}
	
	public RegistroArticulo(Long idArticulo, TcManticArticulosDto articulo, List<ArticuloCodigo> articulosCodigos, List<Especificacion> especificaciones, List<Descuento> articulosDescuentos, List<DescuentoEspecial> clientesDescuentos, List<PrecioSugerido> preciosSugeridos, List<ArticuloProveedor> articulosProveedores, List<TipoVenta> articulosTiposVenta, Long idEmpaque, String obervaciones, boolean redondear, String codigo, Long idProveedor, ArticuloDimencion articuloDimencion) {
		this.idArticulo          = idArticulo;
		this.articulo            = articulo;
		this.articulosCodigos    = articulosCodigos;
		this.especificaciones    = especificaciones;
		this.articulosDescuentos = articulosDescuentos;
		this.clientesDescuentos  = clientesDescuentos;
		this.preciosSugeridos    = preciosSugeridos;
		this.articulosProveedores= articulosProveedores;
		this.articulosTiposVenta = articulosTiposVenta;
		this.idEmpaque           = idEmpaque;
		this.redondear           = redondear;		
		this.contadores          = new ContadoresListas();
		this.countIndice         = 0L;
		this.deleteList          = new ArrayList<>();
		this.importado           = new Importado();
		this.articuloDimencion   = articuloDimencion;
	}

	public Long getIdArticulo() {
		return idArticulo;
	}

	public void setIdArticulo(Long idArticulo) {
		this.idArticulo = idArticulo;
	}
	
	public TcManticArticulosDto getArticulo() {
		return articulo;
	}

	public List<ArticuloCodigo> getArticulosCodigos() {
		return articulosCodigos;
	}

	public List<Especificacion> getEspecificaciones() {
		return especificaciones;
	}

	public List<Descuento> getArticulosDescuentos() {
		return articulosDescuentos;
	}

	public List<DescuentoEspecial> getClientesDescuentos() {
		return clientesDescuentos;
	}

	public List<PrecioSugerido> getPreciosSugeridos() {
		return preciosSugeridos;
	}

	public List<ArticuloProveedor> getArticulosProveedores() {
		return articulosProveedores;
	}

	public List<TipoVenta> getArticulosTiposVenta() {
		return articulosTiposVenta;
	}	

	public Long getIdEmpaque() {
		return idEmpaque;
	}

	public void setIdEmpaque(Long idEmpaque) {
		this.idEmpaque = idEmpaque;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public ArticuloCodigo getArticulosCodigosSeleccion() {
		return articulosCodigosSeleccion;
	}

	public void setArticulosCodigosSeleccion(ArticuloCodigo articulosCodigosSeleccion) {
		this.articulosCodigosSeleccion = articulosCodigosSeleccion;
	}

	public boolean isRedondear() {
		return redondear;
	}

	public void setRedondear(boolean redondear) {
		this.redondear = redondear;
	}

	public Especificacion getEspecificacionSeleccion() {
		return especificacionSeleccion;
	}

	public void setEspecificacionSeleccion(Especificacion especificacionSeleccion) {
		this.especificacionSeleccion = especificacionSeleccion;
	}

	public Descuento getArticuloDescuentoSeleccion() {
		return articuloDescuentoSeleccion;
	}

	public void setArticuloDescuentoSeleccion(Descuento articuloDescuentoSeleccion) {
		this.articuloDescuentoSeleccion = articuloDescuentoSeleccion;
	}

	public DescuentoEspecial getClienteDescuentoSeleccion() {
		return clienteDescuentoSeleccion;
	}

	public void setClienteDescuentoSeleccion(DescuentoEspecial clienteDescuentoSeleccion) {
		this.clienteDescuentoSeleccion = clienteDescuentoSeleccion;
	}

	public Importado getImportado() {
		return importado;
	}

	public void setImportado(Importado importado) {
		this.importado = importado;
	}		

	public PrecioSugerido getPrecioSugeridoSeleccion() {
		return precioSugeridoSeleccion;
	}

	public void setPrecioSugeridoSeleccion(PrecioSugerido precioSugeridoSeleccion) {
		this.precioSugeridoSeleccion = precioSugeridoSeleccion;
	}

	public ArticuloProveedor getArticuloProveedorSeleccion() {
		return articuloProveedorSeleccion;
	}

	public void setArticuloProveedorSeleccion(ArticuloProveedor articuloProveedorSeleccion) {
		this.articuloProveedorSeleccion = articuloProveedorSeleccion;
	}

	public ArticuloDimencion getArticuloDimencion() {
		return articuloDimencion;
	}

	public void setArticuloDimencion(ArticuloDimencion articuloDimencion) {
		this.articuloDimencion = articuloDimencion;
	}

	public TipoVenta getArticuloTiposVentaSeleccion() {
		return articuloTiposVentaSeleccion;
	}

	public void setArticuloTiposVentaSeleccion(TipoVenta articuloTiposVentaSeleccion) {
		this.articuloTiposVentaSeleccion = articuloTiposVentaSeleccion;
	}

	public List<IBaseDto> getDeleteList() {
		return deleteList;
	}

	public void setDeleteList(List<IBaseDto> deleteList) {
		this.deleteList = deleteList;
	}
		
	private void init(){
		MotorBusqueda motorBusqueda                = null;
		TrManticEmpaqueUnidadMedidaDto unidadMedida= null;
		try {
			motorBusqueda= new MotorBusqueda(this.idArticulo);
			this.articulo= motorBusqueda.toArticulo();
			this.redondear= this.articulo.getIdRedondear()== 1L;
			unidadMedida= motorBusqueda.toEmpaqueUnidadMedida(this.articulo.getIdEmpaqueUnidadMedida());
			this.idEmpaque= unidadMedida.getIdEmpaque();
			initCollections(motorBusqueda);
		} // try
		catch (Exception e) {			
			JsfBase.addMessageError(e);
			Error.mensaje(e);
		} // catch		
	} // init
	
	private void initCollections(MotorBusqueda motor) throws Exception{
		try {
			this.articulosCodigos= motor.toArticulosCodigos();
			this.articuloDimencion= motor.toArticuloDimencion();
			this.articulosDescuentos= motor.toArticulosDescuentos();
			this.articulosProveedores= motor.toArticulosProveedor();
			this.articulosTiposVenta= motor.toArticulosTipoVenta();
			this.clientesDescuentos= motor.toArticulosDescuentosEspeciales();
			this.especificaciones= motor.toArticulosEspecificaciones();
			this.importado= motor.toArticuloImagen(this.articulo.getIdImagen());
			this.preciosSugeridos= motor.toArticulosPreciosSugeridos();
			if(!this.articulosCodigos.isEmpty())
				this.observaciones= this.articulosCodigos.get(0).getObservaciones();
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
			throw e;
		} // catch		
	} // initCollections
	
	public void doAgregarArticuloCodigo(){
		ArticuloCodigo articuloCodigo= null;
		try {					
			articuloCodigo= new ArticuloCodigo(this.contadores.getTotalArticulosCodigos() + this.countIndice, ESql.INSERT, true);				
			this.articulosCodigos.add(articuloCodigo);			
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
		finally{			
			this.countIndice++;
		} // finally
	} // doAgregarArticuloCodigo
	
	public void doEliminarArticuloCodigo(){
		try {			
			if(this.articulosCodigos.remove(this.articulosCodigosSeleccion)){
				if(!this.articulosCodigosSeleccion.getNuevo())
					addDeleteList(this.articulosCodigosSeleccion);
				JsfBase.addMessage("Se eliminó correctamente el código", ETipoMensaje.INFORMACION);
			} // if
			else
				JsfBase.addMessage("No fue porsible eliminar el código", ETipoMensaje.INFORMACION);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch			
	} // doEliminarArticuloCodigo
	
	public void doAgregarEspecificacion(){
		Especificacion especificacion= null;
		try {					
			especificacion= new Especificacion(this.contadores.getTotalEspecificaciones() + this.countIndice, ESql.INSERT, true);				
			this.especificaciones.add(especificacion);			
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
		finally{			
			this.countIndice++;
		} // finally
	} // doAgregarEspecificacion
	
	public void doEliminarEspecificacion(){
		try {			
			if(this.especificaciones.remove(this.especificacionSeleccion)){
				if(!this.especificacionSeleccion.getNuevo())
					addDeleteList(this.especificacionSeleccion);
				JsfBase.addMessage("Se eliminó correctamente el código", ETipoMensaje.INFORMACION);
			} // if
			else
				JsfBase.addMessage("No fue porsible eliminar el código", ETipoMensaje.INFORMACION);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch			
	} // doEliminarEspecificacion
	
	public void doAgregarDescuento(){
		Descuento descuento= null;
		try {					
			descuento= new Descuento(this.contadores.getTotalDescuentos() + this.countIndice, ESql.INSERT, true);				
			this.articulosDescuentos.add(descuento);			
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
		finally{			
			this.countIndice++;
		} // finally
	} // doAgregarEspecificacion
	
	public void doEliminarDescuento(){
		try {			
			if(this.articulosDescuentos.remove(this.articuloDescuentoSeleccion)){
				if(!this.articuloDescuentoSeleccion.getNuevo())
					addDeleteList(this.articuloDescuentoSeleccion);
				JsfBase.addMessage("Se eliminó correctamente el código", ETipoMensaje.INFORMACION);
			} // if
			else
				JsfBase.addMessage("No fue porsible eliminar el código", ETipoMensaje.INFORMACION);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch			
	} // doEliminarEspecificacion
	
	public void doAgregarDescuentoEspecial(){
		DescuentoEspecial descuentoEspecial= null;
		try {					
			descuentoEspecial= new DescuentoEspecial(this.contadores.getTotalDescuentosEspeciales() + this.countIndice, ESql.INSERT, true);				
			this.clientesDescuentos.add(descuentoEspecial);			
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
		finally{			
			this.countIndice++;
		} // finally
	} // doAgregarEspecificacion
	
	public void doEliminarDescuentoEspecial(){
		try {			
			if(this.clientesDescuentos.remove(this.clienteDescuentoSeleccion)){
				if(!this.clienteDescuentoSeleccion.getNuevo())
					addDeleteList(this.clienteDescuentoSeleccion);
				JsfBase.addMessage("Se eliminó correctamente el código", ETipoMensaje.INFORMACION);
			} // if
			else
				JsfBase.addMessage("No fue porsible eliminar el código", ETipoMensaje.INFORMACION);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch			
	} // doEliminarEspecificacion
	
	public void doAgregarPrecioSugerido(){
		PrecioSugerido precioSugerido= null;
		try {					
			precioSugerido= new PrecioSugerido(this.contadores.getTotalPreciosSugeridos() + this.countIndice, ESql.INSERT, true);				
			this.preciosSugeridos.add(precioSugerido);			
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
		finally{			
			this.countIndice++;
		} // finally
	} // doAgregarEspecificacion
	
	public void doEliminarPrecioSugerido(){
		try {			
			if(this.preciosSugeridos.remove(this.precioSugeridoSeleccion)){
				if(!this.precioSugeridoSeleccion.getNuevo())
					addDeleteList(this.precioSugeridoSeleccion);
				JsfBase.addMessage("Se eliminó correctamente el código", ETipoMensaje.INFORMACION);
			} // if
			else
				JsfBase.addMessage("No fue porsible eliminar el código", ETipoMensaje.INFORMACION);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch			
	} // doEliminarEspecificacion
	
	public void doAgregarArticuloProveedor(){
		ArticuloProveedor articuloProveedor= null;
		try {					
			articuloProveedor= new ArticuloProveedor(this.contadores.getTotalArticulosProveedor() + this.countIndice, ESql.INSERT, true);				
			this.articulosProveedores.add(articuloProveedor);			
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
		finally{			
			this.countIndice++;
		} // finally
	} // doAgregarEspecificacion
	
	public void doEliminarArticuloProveedor(){
		try {			
			if(this.articulosProveedores.remove(this.articuloProveedorSeleccion)){
				if(!this.articuloProveedorSeleccion.getNuevo())
					addDeleteList(this.articuloProveedorSeleccion);
				JsfBase.addMessage("Se eliminó correctamente el código", ETipoMensaje.INFORMACION);
			} // if
			else
				JsfBase.addMessage("No fue porsible eliminar el código", ETipoMensaje.INFORMACION);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch			
	} // doEliminarEspecificacion	
	
	public void doAgregarTipoVenta(){
		TipoVenta tipoVenta= null;
		try {					
			tipoVenta= new TipoVenta(this.contadores.getTotalTiposVentas() + this.countIndice, ESql.INSERT, true);				
			this.articulosTiposVenta.add(tipoVenta);			
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
		finally{			
			this.countIndice++;
		} // finally
	} // doAgregarEspecificacion
	
	public void doEliminarTipoVenta(){
		try {			
			if(this.articulosTiposVenta.remove(this.articuloTiposVentaSeleccion)){
				if(!this.articuloTiposVentaSeleccion.getNuevo())
					addDeleteList(this.articuloTiposVentaSeleccion);
				JsfBase.addMessage("Se eliminó correctamente el código", ETipoMensaje.INFORMACION);
			} // if
			else
				JsfBase.addMessage("No fue porsible eliminar el código", ETipoMensaje.INFORMACION);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch			
	} // doEliminarEspecificacion
	
	public void doFileUpload(FileUploadEvent event) {
		String genericPath= null;  
    File result       = null;		
		Long fileSize     = 0L;
		File filePath     = null;
		try {
			if (this.importado != null && !Cadena.isVacio(this.importado.getName())) 
				doCancelar();
			genericPath= Configuracion.getInstance().getPropiedadSistemaServidor("path.image").concat(JsfBase.getAutentifica().getEmpresa().getIdEmpresa().toString()).concat(File.separator);
			result= new File(genericPath.concat(event.getFile().getFileName()));		
			filePath= new File(genericPath);
			if (!filePath.exists())
				filePath.mkdirs();
			if (result.exists())
				result.delete();			      
			toWriteFile(result, event.getFile().getInputstream());
			fileSize= event.getFile().getSize();
			this.importado= new Importado(event.getFile().getFileName(), event.getFile().getContentType(), EFormatos.FREE, event.getFile().getSize(), fileSize.equals(0L) ? fileSize : fileSize/1024, event.getFile().equals(0L) ? BYTES : K_BYTES, genericPath);      
			toMessageImage();			
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessage("Importar archivo", "El archivo no pudo ser importado.", ETipoMensaje.ERROR);
		} // catch
	} // doFileUpload
	
	private void toWriteFile(File result, InputStream fileImport) throws Exception{
		FileOutputStream fileOutputStream= null;
		InputStream inputStream          = null;
		try {
			fileOutputStream= new FileOutputStream(result);
      byte[] buffer= new byte[BUFFER_SIZE];
      int bulk;
      inputStream= fileImport;
      while (true){
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
	
	private void toMessageImage(){
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
	
	private String toDetailMessage(){
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
	
	private void addDeleteList(IBaseDto dto){
		try {
			this.deleteList.add(dto);
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
	} // addDeleteList
	
	public void doCancelar(){
		File result= null;
		try {
			if (this.importado != null && !Cadena.isVacio(this.importado.getName())) {
				result= new File(JsfBase.getRealPath().concat("Temporal").concat(File.separator).concat("Img").concat(File.separator).concat(this.importado.getName()));
				if (result.exists())
					result.delete();
			} // if						
		} // try
		catch (Exception e) {		
			throw e;
		} // catch		
	} // doCancelar
}
