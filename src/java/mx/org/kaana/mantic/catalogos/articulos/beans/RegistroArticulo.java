package mx.org.kaana.mantic.catalogos.articulos.beans;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.kajool.enums.EFormatos;
import mx.org.kaana.kajool.enums.ESql;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.archivo.Archivo;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.mantic.catalogos.articulos.reglas.MotorBusqueda;
import mx.org.kaana.mantic.db.dto.TcManticArticulosDto;
import mx.org.kaana.mantic.db.dto.TrManticEmpaqueUnidadMedidaDto;
import org.primefaces.event.FileUploadEvent;

public class RegistroArticulo implements Serializable {

	private static final long serialVersionUID= 2173143980013765240L;
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
	private Long idTipoArticulo;
	private Boolean idBarras;
	private Boolean idVigente;
	private Boolean idDescontinuado;
  private List<ArticuloImagen> imagenes;
	private Boolean imagen;

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
				new ArticuloDimencion(), 1L, false, true, false
				);
	} // RegistroArticulo
	
	public RegistroArticulo(Long idArticulo) {
		this.idArticulo  = idArticulo;
		this.contadores  = new ContadoresListas();
		this.countIndice = 0L;
		this.deleteList  = new ArrayList<>();
    this.imagenes    = new ArrayList<>();
		this.init();		
	}
	
	public RegistroArticulo(Long idArticulo, TcManticArticulosDto articulo, List<ArticuloCodigo> articulosCodigos, List<Especificacion> especificaciones, List<Descuento> articulosDescuentos, List<DescuentoEspecial> clientesDescuentos, List<PrecioSugerido> preciosSugeridos, List<ArticuloProveedor> articulosProveedores, List<TipoVenta> articulosTiposVenta, Long idEmpaque, String obervaciones, boolean redondear, String codigo, Long idProveedor, ArticuloDimencion articuloDimencion, Long idTipoArticulo, Boolean idBarras, Boolean idVigente, Boolean idDescontinuado) {
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
		this.articuloDimencion   = articuloDimencion;
		this.articulo.setSat(Constantes.CODIGO_SAT);
		this.idTipoArticulo      = idTipoArticulo;
		this.idBarras            = idBarras;
		this.idVigente           = idVigente;
		this.idDescontinuado     = idDescontinuado;
    this.imagenes            = new ArrayList<>();
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

	public Long getIdTipoArticulo() {
		return idTipoArticulo;
	}

	public void setIdTipoArticulo(Long idTipoArticulo) {
		this.idTipoArticulo = idTipoArticulo;
	}	

	public Boolean getIdBarras() {
		return idBarras;
	}

	public void setIdBarras(Boolean idBarras) {
		this.idBarras = idBarras;
		if(this.articulo!= null)
			this.articulo.setIdBarras(idBarras? 1L : 2L);
	}

	public Boolean getIdVigente() {
		return idVigente;
	}

	public void setIdVigente(Boolean idVigente) {
		this.idVigente = idVigente;
		if(this.articulo!= null)
			this.articulo.setIdVigente(idVigente? 1L : 2L);
	}

	public Boolean getIdDescontinuado() {
		return idDescontinuado;
	}

	public void setIdDescontinuado(Boolean idDescontinuado) {
		this.idDescontinuado = idDescontinuado;
		if(this.articulo!= null)
			this.articulo.setIdDescontinuado(idDescontinuado? 1L : 2L);
	}

  public List<ArticuloImagen> getImagenes() {
    return imagenes;
  }

  public void setImagenes(List<ArticuloImagen> imagenes) {
    this.imagenes = imagenes;
  }

  public Boolean getImagen() {
    return imagen;
  }

  public void setImagen(Boolean imagen) {
    this.imagen = imagen;
  }
	
	private void init() {
		MotorBusqueda motorBusqueda                = null;
		TrManticEmpaqueUnidadMedidaDto unidadMedida= null;
		try {
			motorBusqueda= new MotorBusqueda(this.idArticulo);
			this.articulo= motorBusqueda.toArticulo();
			if(this.articulo!= null) {			
				this.idBarras = this.articulo.getIdBarras().equals(1L);
			  this.redondear= this.articulo.getIdRedondear()== 1L;
			  this.idVigente= this.articulo.getIdVigente()== 1L;
			} // if	
			unidadMedida= motorBusqueda.toEmpaqueUnidadMedida(this.articulo.getIdEmpaqueUnidadMedida());
			this.idEmpaque= unidadMedida.getIdEmpaque();
			this.initCollections(motorBusqueda);
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
			this.preciosSugeridos= motor.toArticulosPreciosSugeridos();
			if(!this.articulosCodigos.isEmpty())
				this.observaciones= this.articulosCodigos.get(0).getObservaciones();
      motor.toArticuloImagenes(this.imagenes);
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
		String path    = null;  
		String nameFile= Archivo.toFormatNameFile(event.getFile().getFileName().toUpperCase());
    File result    = null;		
		Long fileSize  = 0L;
		File filePath  = null;
		try {
			path= Configuracion.getInstance().getPropiedadSistemaServidor("path.image").concat(JsfBase.getAutentifica().getEmpresa().getIdEmpresa().toString()).concat("/");
			result  = new File(path.concat(nameFile));		
			filePath= new File(path);
			if (!filePath.exists())
				filePath.mkdirs();
			if (result.exists())
				result.delete();			      
			Archivo.toWriteFile(result, event.getFile().getInputstream());
			fileSize= event.getFile().getSize();
			Importado importado= new Importado(nameFile, event.getFile().getContentType(), EFormatos.JPG, event.getFile().getSize(), fileSize.equals(0L) ? fileSize : fileSize/1024, event.getFile().equals(0L) ? BYTES : K_BYTES, path, path.concat(nameFile), event.getFile().getFileName().toUpperCase());      
			this.toMessageImage(importado);		
			this.articulo.setIdImagen(-1L);
      this.addImagen(importado);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessage("Importar archivo", "El archivo no pudo ser importado.", ETipoMensaje.ERROR);
		} // catch
	} // doFileUpload
	
	public boolean validaImagenComun() {
		boolean regresar   = false;
		MotorBusqueda motor= null;		
		try {
			motor= new MotorBusqueda(this.articulo.getIdImagen());
			regresar= motor.deleteImage();
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
		return regresar;
	} // validaImagenComun 	
	
	private void toMessageImage(Importado importado) {
		String detail   = null;
		try {
			detail= this.toDetailMessage(importado);
      JsfBase.addMessage("Actualización de versiones", detail);
		} // try
		catch (Exception e) {			
			throw e;
		} // catch				     
	} // toMessageImage
	
	private String toDetailMessage(Importado importado) {
		StringBuilder regresar= new StringBuilder();
		regresar.append("Nombre: ");
		regresar.append(importado.getName());
		regresar.append(" Tamaño: ");
		regresar.append(importado.getSize());
		regresar.append(importado.getMedicion());
		regresar.append(" Contenido: ");
		regresar.append(importado.getContent());
		regresar.append("; importado con éxito.");
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
	
	public void doSeleccionarPrincipal(ArticuloCodigo principal) {
		try {
			for(ArticuloCodigo articuloCodigo: this.articulosCodigos) {
				if(!articuloCodigo.equals(principal))
					articuloCodigo.setIdPrincipal(2L);
			} // for
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
	}
  
  public void addImagen(Importado importado) {
    this.imagenes.add(new ArticuloImagen(importado));
    int count= 0;
    long max = 0;
    for (ArticuloImagen item: this.imagenes) {
      if(!item.getSqlAccion().equals(ESql.DELETE) && Objects.equals(item.getIdPrincipal(), 1L)) 
        count++;
      if(max< item.getOrden()) 
        max= item.getOrden();
    } // for
    this.imagenes.get(this.imagenes.size()- 1).setOrden(max+ 1L);
    this.imagenes.get(this.imagenes.size()- 1).setModelo("MODELO_"+ (max+ 1L));
    this.imagenes.get(this.imagenes.size()- 1).setNombre("MODELO_"+ (max+ 1L));
    if(count== 0)
      this.toSetImagePrincipal();
  }
  
  public void toUpdatePrincipal(ArticuloImagen row) {
    for (ArticuloImagen item : this.imagenes) {
      if(!Objects.equals(row.getId(), item.getId()) && Objects.equals(item.getIdPrincipal(), 1L)) {
        item.setPrincipal(false);
        item.setIdPrincipal(2L);
        if(!Objects.equals(item.getSqlAccion(), ESql.INSERT))
          item.setSqlAccion(ESql.UPDATE);
      } // if
    } // for
    if(!Objects.equals(row.getSqlAccion(), ESql.INSERT))
      row.setSqlAccion(ESql.UPDATE);
    row.setIdPrincipal(row.getPrincipal()? 1L: 2L);
    if(!row.getPrincipal()) 
      this.toSetImagePrincipal();
  }
  
  public void toSetImagePrincipal() {
    if(this.imagenes!= null && this.imagenes.size()> 0) {
      for (ArticuloImagen item : this.imagenes) {
        if(!item.getSqlAccion().equals(ESql.DELETE)) {
          item.setPrincipal(true);
          item.setIdPrincipal(1L);
          if(!Objects.equals(item.getSqlAccion(), ESql.INSERT))
            item.setSqlAccion(item.getAnterior());
          break;
        } // if  
      } // for
    } // if
  }
  
  public void doDeleteImage(ArticuloImagen row) {
    if(row.getSqlAccion().equals(ESql.INSERT)) {
      // eliminar fisicamente el archivo porque no ya no es necesario que este en el servidor
      this.doDeleteFile(row.getImportado());
      this.imagenes.remove(row);
      if(Objects.equals(row.getIdPrincipal(), 1L))
        this.toSetImagePrincipal();
    } // if  
    else {
      row.setSqlAccion(ESql.DELETE);
      if(Objects.equals(row.getIdPrincipal(), 1L)) {
        this.toSetImagePrincipal();
        row.setPrincipal(false);
        row.setIdPrincipal(2L);
      } // if  
    } // else
  } 

  public void doRecoverImage(ArticuloImagen row) {
    row.setSqlAccion(row.getAnterior());
    int count= 0;
    for (ArticuloImagen item : this.imagenes) {
      if(!item.getSqlAccion().equals(ESql.DELETE) && Objects.equals(item.getIdPrincipal(), 1L)) 
        count++;
    } // for
    if(count== 0)
      this.toSetImagePrincipal();
  } 

 	public void doDeleteFile(Importado item) {
		String path = null;		
		File image  = null;
		try {
			if (item != null && !Cadena.isVacio(item.getName())) {
				path= Configuracion.getInstance().getPropiedadSistemaServidor("path.image").concat(JsfBase.getAutentifica().getEmpresa().getIdEmpresa().toString()).concat("/");
				image= new File(path.concat(item.getName()));
				if(image.exists())
					image.delete();
			} // if			
		} // try
		catch (Exception e) {			
			Error.mensaje(e);
			throw e;
		} // catch		
		finally{						
			image  = null;			
		} // finally
	} // doDeleteFile

  public void toCleanImages() {
    if(this.imagenes!= null && this.imagenes.size()> 0) {
      // eliminar fisicamente el archivo porque no ya no es necesario que este en el servidor
      for (ArticuloImagen item: this.imagenes)
        if(Objects.equals(item.getSqlAccion(), ESql.INSERT))
          this.doDeleteFile(item.getImportado());
    } // if  
  }          
  
}
