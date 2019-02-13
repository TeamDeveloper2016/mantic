package mx.org.kaana.mantic.catalogos.almacenes.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EBooleanos;
import mx.org.kaana.kajool.enums.ESql;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.pagina.UISelectItem;
import mx.org.kaana.mantic.catalogos.clientes.beans.Domicilio;
import mx.org.kaana.mantic.catalogos.almacenes.reglas.MotorBusqueda;
import mx.org.kaana.mantic.catalogos.almacenes.reglas.Transaccion;
import mx.org.kaana.mantic.db.dto.TcManticAlmacenesDto;
import org.primefaces.context.RequestContext;
import org.primefaces.event.TabChangeEvent;

public class RegistroAlmacen implements Serializable{
	
	private static final long serialVersionUID = -360101383355243032L;
	private Long idAlmacen;
	private TcManticAlmacenesDto almacen;
	private List<AlmacenDomicilio> almacenDomicilio;
	private AlmacenDomicilio almacenDomicilioSelecion;
	private List<AlmacenArticulo> almacenArticulo;
	private AlmacenArticulo almacenArticuloSeleccion;
	private AlmacenArticulo almacenArticuloPivote;
	private List<AlmacenTipoContacto> almacenTiposContacto;
	private AlmacenTipoContacto almacenTipoContactoSeleccion;
	private List<AlmacenUbicacion> ubicaciones;
	private List<UISelectItem> ubicacionesItems;
	private AlmacenUbicacion almacenUbicacionSeleccion;
	private UISelectItem almacenUbicacionItem;
	private Long idUbicacion;
	private List<IBaseDto> deleteList;
	private ContadoresListas contadores;
	private Long countIndice;
	private Domicilio domicilio;
	private Domicilio domicilioPivote;
	private UISelectEntity resultadoBusquedaArticulo;
	private Boolean idPrincipal;

	public RegistroAlmacen() {
		this(-1L, new TcManticAlmacenesDto(), new ArrayList<AlmacenDomicilio>(), new ArrayList<AlmacenTipoContacto>(), new ArrayList<AlmacenUbicacion>(), new Domicilio(), new ArrayList<AlmacenArticulo>(), new AlmacenArticulo());
	}
	
	public RegistroAlmacen(Long idAlmacen) {
		this.idAlmacen  = idAlmacen;
		this.contadores = new ContadoresListas();
		this.countIndice= 0L;
		this.deleteList = new ArrayList<>();
		this.domicilio  = new Domicilio();
		this.domicilioPivote= new Domicilio();
		this.almacenArticuloSeleccion= new AlmacenArticulo();
		this.almacenArticuloPivote= new AlmacenArticulo();
		init();		
	}
	
	public RegistroAlmacen(Long idAlmacen, TcManticAlmacenesDto almacen, List<AlmacenDomicilio> almacenDomicilio, List<AlmacenTipoContacto> almacenTiposContacto, List<AlmacenUbicacion> ubicaciones, Domicilio domicilio, List<AlmacenArticulo>almacenArticulo, AlmacenArticulo almacenArticuloSeleccion) {
		this.idAlmacen           = idAlmacen;
		this.almacen             = almacen;
		this.almacenDomicilio    = almacenDomicilio;
		this.almacenTiposContacto= almacenTiposContacto;
		this.ubicaciones         = ubicaciones;
		this.deleteList          = new ArrayList<>();
		this.contadores          = new ContadoresListas();
		this.countIndice         = 0L;
		this.domicilio           = domicilio;
		this.domicilioPivote     = domicilio;
		this.almacenArticulo     = almacenArticulo;
		this.almacenArticuloSeleccion= almacenArticuloSeleccion;
		this.almacenArticuloPivote= almacenArticuloSeleccion;
		this.ubicacionesItems    = new ArrayList<>();
	}

	public Long getIdAlmacen() {
		return idAlmacen;
	}

	public void setIdAlmacen(Long idAlmacen) {
		this.idAlmacen = idAlmacen;
	}

	public TcManticAlmacenesDto getAlmacen() {
		return almacen;
	}

	public void setAlmacen(TcManticAlmacenesDto almacen) {
		this.almacen = almacen;
	}

	public List<AlmacenDomicilio> getAlmacenDomicilio() {
		return almacenDomicilio;
	}

	public void setAlmacenDomicilio(List<AlmacenDomicilio> almacenDomicilio) {
		this.almacenDomicilio = almacenDomicilio;
	}

	public AlmacenDomicilio getAlmacenDomicilioSelecion() {
		return almacenDomicilioSelecion;
	}

	public void setAlmacenDomicilioSelecion(AlmacenDomicilio almacenDomicilioSelecion) {
		this.almacenDomicilioSelecion = almacenDomicilioSelecion;
	}	

	public List<AlmacenTipoContacto> getAlmacenTiposContacto() {
		return almacenTiposContacto;
	}

	public void setAlmacenTiposContacto(List<AlmacenTipoContacto> almacenTiposContacto) {
		this.almacenTiposContacto = almacenTiposContacto;
	}

	public AlmacenTipoContacto getAlmacenTipoContactoSeleccion() {
		return almacenTipoContactoSeleccion;
	}

	public void setAlmacenTipoContactoSeleccion(AlmacenTipoContacto almacenTipoContactoSeleccion) {
		this.almacenTipoContactoSeleccion = almacenTipoContactoSeleccion;
	}

	public List<AlmacenUbicacion> getUbicaciones() {
		return ubicaciones;
	}

	public void setUbicaciones(List<AlmacenUbicacion> ubicaciones) {
		this.ubicaciones = ubicaciones;
	}

	public AlmacenUbicacion getAlmacenUbicacionSeleccion() {
		return almacenUbicacionSeleccion;
	}

	public void setAlmacenUbicacionSeleccion(AlmacenUbicacion almacenUbicacionSeleccion) {
		this.almacenUbicacionSeleccion = almacenUbicacionSeleccion;
	}

	public List<IBaseDto> getDeleteList() {
		return deleteList;
	}

	public void setDeleteList(List<IBaseDto> deleteList) {
		this.deleteList = deleteList;
	}		

	public Domicilio getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(Domicilio domicilio) {
		this.domicilio = domicilio;
	}		

	public Domicilio getDomicilioPivote() {
		return domicilioPivote;
	}

	public void setDomicilioPivote(Domicilio domicilioPivote) {
		this.domicilioPivote = domicilioPivote;
	}

	public List<AlmacenArticulo> getAlmacenArticulo() {
		return almacenArticulo;
	}

	public void setAlmacenArticulo(List<AlmacenArticulo> almacenArticulo) {
		this.almacenArticulo = almacenArticulo;
	}

	public AlmacenArticulo getAlmacenArticuloSeleccion() {
		return almacenArticuloSeleccion;
	}

	public void setAlmacenArticuloSeleccion(AlmacenArticulo almacenArticuloSeleccion) {
		this.almacenArticuloSeleccion = almacenArticuloSeleccion;
	}

	public AlmacenArticulo getAlmacenArticuloPivote() {
		return almacenArticuloPivote;
	}

	public void setAlmacenArticuloPivote(AlmacenArticulo almacenArticuloPivote) {
		this.almacenArticuloPivote = almacenArticuloPivote;
	}

	public List<UISelectItem> getUbicacionesItems() {
		return ubicacionesItems;
	}

	public void setUbicacionesItems(List<UISelectItem> ubicacionesItems) {
		this.ubicacionesItems = ubicacionesItems;
	}

	public UISelectItem getAlmacenUbicacionItem() {
		return almacenUbicacionItem;
	}

	public void setAlmacenUbicacionItem(UISelectItem almacenUbicacionItem) {
		this.almacenUbicacionItem = almacenUbicacionItem;
	}

	public Long getIdUbicacion() {
		return idUbicacion;
	}

	public void setIdUbicacion(Long idUbicacion) {
		this.idUbicacion = idUbicacion;
	}

	public UISelectEntity getResultadoBusquedaArticulo() {
		return resultadoBusquedaArticulo;
	}

	public void setResultadoBusquedaArticulo(UISelectEntity resultadoBusquedaArticulo) {
		this.resultadoBusquedaArticulo = resultadoBusquedaArticulo;
	}

	public Boolean getIdPrincipal() {
		return idPrincipal;
	}

	public void setIdPrincipal(Boolean idPrincipal) {
		this.idPrincipal = idPrincipal;
		if(this.almacen!= null)
			this.almacen.setIdPrincipal(idPrincipal ? EBooleanos.SI.getIdBooleano() : EBooleanos.NO.getIdBooleano());
	}	
	
	private void init(){
		MotorBusqueda motorBusqueda= null;
		try {
			motorBusqueda= new MotorBusqueda(this.idAlmacen);
			this.almacen= motorBusqueda.toAlmacen();		
			this.idPrincipal= this.almacen.getIdPrincipal().equals(EBooleanos.SI.getIdBooleano());
			initCollections(motorBusqueda);
		} // try
		catch (Exception e) {			
			JsfBase.addMessageError(e);
			Error.mensaje(e);
		} // catch		
	} // init
	
	private void initCollections(MotorBusqueda motor) throws Exception{
		int count= 0;
		try {
			this.ubicacionesItems= new ArrayList<>();
			this.almacenDomicilio= motor.toAlmacenesDomicilio(true);
			for(AlmacenDomicilio almacenDomicilio: this.almacenDomicilio){
				count++;
				almacenDomicilio.setConsecutivo(Long.valueOf(count));
			} // for				
			this.almacenTiposContacto= motor.toAlmacenesTipoContacto();
			this.ubicaciones= motor.toAlmacenUbicacion();
			if(!this.ubicaciones.isEmpty()){
				for(AlmacenUbicacion item: this.ubicaciones)
					this.ubicacionesItems.add(new UISelectItem(item.getKey(), item.getUbicacion()));
			} // if			
			this.almacenArticulo= motor.toAlmacenArticulos();
		} // try
		catch (Exception e) {
			Error.mensaje(e);			
			throw e;
		} // catch		
	} // initCollections
	
	public void doAgregarAlmacenDomicilio(){
		AlmacenDomicilio domicilio= null;
		try {								
			domicilio= new AlmacenDomicilio(this.contadores.getTotalAlmacenesDomicilios() + this.countIndice, ESql.INSERT, true);	
			setValuesAlmacenDomicilio(domicilio, false);			
			this.almacenDomicilio.add(domicilio);			
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
		finally{			
			this.countIndice++;
		} // finally
	} // doAgregarAlmacenDomicilio
	
	public void doEliminarAlmacenDomicilio(){
		try {			
			if(this.almacenDomicilio.remove(this.almacenDomicilioSelecion)){
				if(!this.almacenDomicilioSelecion.getNuevo())
					addDeleteList(this.almacenDomicilioSelecion);
				JsfBase.addMessage("Se eliminó correctamente el domicilio", ETipoMensaje.INFORMACION);
			} // if
			else
				JsfBase.addMessage("No fue porsible eliminar el domicilio", ETipoMensaje.INFORMACION);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch			
	} // doEliminarAlmacenDomicilio	
	
	public void doConsultarAlmacenDomicilio(){
		AlmacenDomicilio pivote= null;
		try {			
			pivote= this.almacenDomicilio.get(this.almacenDomicilio.indexOf(this.almacenDomicilioSelecion));
			pivote.setModificar(true);
			this.domicilioPivote= new Domicilio();
			this.domicilioPivote.setIdTipoDomicilio(pivote.getIdTipoDomicilio());
			this.domicilioPivote.setPrincipal(pivote.getIdPrincipal().equals(1L));	
			this.domicilioPivote.setIdDomicilio(pivote.getDomicilio().getKey());
			this.domicilioPivote.setDomicilio(pivote.getDomicilio());
			this.domicilioPivote.setIdEntidad(pivote.getIdEntidad());
			this.domicilioPivote.setIdMunicipio(pivote.getIdMunicipio());
			this.domicilioPivote.setLocalidad(pivote.getIdLocalidad());
			this.domicilioPivote.setIdLocalidad(pivote.getIdLocalidad().getKey());
			this.domicilioPivote.setCodigoPostal(pivote.getCodigoPostal());
			this.domicilioPivote.setCalle(pivote.getCalle());
			this.domicilioPivote.setNumeroExterior(pivote.getExterior());
			this.domicilioPivote.setNumeroInterior(pivote.getInterior());
			this.domicilioPivote.setAsentamiento(pivote.getColonia());
			this.domicilioPivote.setEntreCalle(pivote.getEntreCalle());
			this.domicilioPivote.setYcalle(pivote.getyCalle());
			this.domicilioPivote.setNuevoCp(pivote.getCodigoPostal()!= null && !Cadena.isVacio(pivote.getCodigoPostal()));
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
	} // doConsultarAlmacenDomicilio
	
	public void doActualizarAlmacenDomicilio(){
		AlmacenDomicilio pivote= null;
		try {			
			pivote= this.almacenDomicilio.get(this.almacenDomicilio.indexOf(this.almacenDomicilioSelecion));			
			pivote.setModificar(false);
			setValuesAlmacenDomicilio(pivote, true);						
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch				
	} // doActualizarAlmacenDomicilio
	
	private void setValuesAlmacenDomicilio(AlmacenDomicilio almacenDomicilio, boolean actualizar) throws Exception{
		try {
			if(this.domicilio.getPrincipal()){
				for(AlmacenDomicilio record: this.almacenDomicilio)
					record.setIdPrincipal(0L);
			} // if
			almacenDomicilio.setIdPrincipal(this.domicilio.getPrincipal() ? 1L : 2L);
			almacenDomicilio.setDomicilio(this.domicilio.getDomicilio());
			almacenDomicilio.setIdDomicilio(this.domicilio.getDomicilio().getKey());
			almacenDomicilio.setIdUsuario(JsfBase.getIdUsuario());
			almacenDomicilio.setIdTipoDomicilio(this.domicilio.getIdTipoDomicilio());
			if(!actualizar)
				almacenDomicilio.setConsecutivo(this.almacenDomicilio.size() + 1L);
			almacenDomicilio.setIdEntidad(this.domicilio.getIdEntidad());
			almacenDomicilio.setIdMunicipio(this.domicilio.getIdMunicipio());
			almacenDomicilio.setIdLocalidad(this.domicilio.getLocalidad());
			almacenDomicilio.setCodigoPostal(this.domicilio.getCodigoPostal());
			almacenDomicilio.setCalle(this.domicilio.getCalle());
			almacenDomicilio.setExterior(this.domicilio.getNumeroExterior());
			almacenDomicilio.setInterior(this.domicilio.getNumeroInterior());
			almacenDomicilio.setEntreCalle(this.domicilio.getEntreCalle());
			almacenDomicilio.setyCalle(this.domicilio.getYcalle());
			almacenDomicilio.setColonia(this.domicilio.getAsentamiento());
			almacenDomicilio.setNuevoCp(this.domicilio.getCodigoPostal()!= null && !Cadena.isVacio(this.domicilio.getCodigoPostal()));
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
	} // setValuesAlmacenDomicilio
	
	public void doAgregarAlmacenTipoContacto(){
		AlmacenTipoContacto almacenTipoContacto= null;
		try {					
			almacenTipoContacto= new AlmacenTipoContacto(this.contadores.getTotalAlmacenesTipoContacto() + this.countIndice, ESql.INSERT, true);				
			almacenTipoContacto.setOrden(this.almacenTiposContacto.size() + 1L);
			this.almacenTiposContacto.add(almacenTipoContacto);			
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
		finally{			
			this.countIndice++;
		} // finally
	} // doAgregarAlmacenTipoContacto
	
	public void doEliminarAlamcenTipoContacto(){
		try {			
			if(this.almacenTiposContacto.remove(this.almacenTipoContactoSeleccion)){
				if(!this.almacenTipoContactoSeleccion.getNuevo())
					addDeleteList(this.almacenTipoContactoSeleccion);
				JsfBase.addMessage("Se eliminó correctamente el tipo de contacto", ETipoMensaje.INFORMACION);
			} // if
			else
				JsfBase.addMessage("No fue porsible eliminar el tipo de contacto", ETipoMensaje.INFORMACION);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch			
	} // doEliminarAlamcenTipoContacto
	
	public void doAgregarAlmacenUbicacion(){
		AlmacenUbicacion almacenUbicacion= null;
		try {					
			almacenUbicacion= new AlmacenUbicacion(this.contadores.getTotalAlmacenesUbicacion() + this.countIndice, ESql.INSERT, true);				
			this.ubicaciones.add(almacenUbicacion);			
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
		finally{			
			this.countIndice++;
		} // finally
	} // doAgregarAlmacenTipoContacto
	
	public void doEliminarAlmacenUbicacion(){
		try {			
			for(AlmacenArticulo recordoArt: this.almacenArticulo){
				if(recordoArt.getIdAlmacenUbicacion().equals(this.almacenUbicacionSeleccion.getIdAlmacenUbicacion())){
					recordoArt.setIdAlmacenUbicacion(-1L);
					recordoArt.setAnaquel(null);
					recordoArt.setPiso(null);
					recordoArt.setCharola(null);
					recordoArt.setCuarto(null);
				} // if
			} // for
			if(this.ubicaciones.remove(this.almacenUbicacionSeleccion)){				
				if(!this.almacenUbicacionSeleccion.getNuevo())
					addDeleteList(this.almacenUbicacionSeleccion);
				JsfBase.addMessage("Se eliminó correctamente el tipo de contacto", ETipoMensaje.INFORMACION);
			} // if
			else
				JsfBase.addMessage("No fue porsible eliminar el tipo de contacto", ETipoMensaje.INFORMACION);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch			
	} // doEliminarAlamcenTipoContacto
	
	public void doSeleccionarArticulo(){
		boolean existe= false;
		try {
			for(AlmacenArticulo articulo: this.almacenArticulo){
				if(articulo.getIdArticulo().equals(this.resultadoBusquedaArticulo.getKey())){
					this.almacenArticuloSeleccion= articulo;
					doConsultarAlmacenArticulo();
					existe= true;
				} // if
			} // for
			if(!existe){
				this.almacenArticuloPivote= new AlmacenArticulo();
				this.almacenArticuloSeleccion= new AlmacenArticulo();
			} // if
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);		
		} // catch
	} // doSeleccionarArticulo
	
	public void doSeleccionarArticuloPivote(){
		boolean existe= false;
		try {
			if(this.almacenArticuloPivote.getArticulo()!= null){
				for(AlmacenArticulo articulo: this.almacenArticulo){
					if(articulo.getIdArticulo().equals(this.almacenArticuloPivote.getArticulo().getKey())){
						this.almacenArticuloSeleccion= articulo;
						doConsultarAlmacenArticulo();
						existe= true;
					} // if
				} // for
				if(!existe){
					this.almacenArticuloPivote= new AlmacenArticulo();
				} // if
			} // if
			else
				this.almacenArticuloPivote= new AlmacenArticulo();
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);		
		} // catch
	} // doSeleccionarArticulo
	
	public void doAgregarAlmacenArticulo(){
		AlmacenArticulo articulo= null;
		AlmacenUbicacion ubicacion= null;
		try {					
			articulo= new AlmacenArticulo(this.contadores.getTotalAlmacenesArticulo() + this.countIndice, ESql.INSERT, true);							
			articulo.setIdArticulo(this.almacenArticuloPivote.getIdArticulo());
			articulo.setIdUsuario(JsfBase.getIdUsuario());
			articulo.setMaximo(this.almacenArticuloPivote.getMaximo());
			articulo.setMinimo(this.almacenArticuloPivote.getMinimo());
			articulo.setStock(this.almacenArticuloPivote.getStock());
			if(this.idUbicacion!= null && !this.idUbicacion.equals(-1L)){
				for(AlmacenUbicacion recordUbicacion: this.ubicaciones){
					if(this.idUbicacion.equals(recordUbicacion.getKey())){
						ubicacion= recordUbicacion;
						articulo.setIdAlmacenUbicacion(recordUbicacion.getKey());
					} // if
				} // for
			} // if
			if(ubicacion!= null){
				articulo.setAnaquel(ubicacion.getAnaquel());
				articulo.setPiso(ubicacion.getPiso());
				articulo.setCuarto(ubicacion.getCuarto());
				articulo.setCharola(ubicacion.getCharola());
			} // if
			this.almacenArticulo.add(articulo);		
			this.almacenArticuloPivote= new AlmacenArticulo();
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
		finally{			
			this.countIndice++;
		} // finally
	} // doAgregarAlmacenArticulo
	
	public void doEliminarAlmacenArticulo(){
		try {			
			if(this.almacenArticulo.remove(this.almacenArticuloSeleccion)){
				if(!this.almacenArticuloSeleccion.getNuevo())
					addDeleteList(this.almacenArticuloSeleccion);
				JsfBase.addMessage("Se eliminó correctamente el articulo", ETipoMensaje.INFORMACION);
			} // if
			else
				JsfBase.addMessage("No fue porsible eliminar el articulo", ETipoMensaje.INFORMACION);
			this.almacenArticuloPivote= new AlmacenArticulo();
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch			
	} // doEliminarAlmacenArticulo
	
	public void doActualizarAlmacenArticulo(){
		AlmacenArticulo articulo= null;
		AlmacenUbicacion ubicacion= null;
		try {					
			articulo= this.almacenArticulo.get(this.almacenArticulo.indexOf(this.almacenArticuloSeleccion));
			articulo.setModificar(false);			
			articulo.setIdArticulo(this.almacenArticuloPivote.getIdArticulo());
			articulo.setIdUsuario(JsfBase.getIdUsuario());
			articulo.setMaximo(this.almacenArticuloPivote.getMaximo());
			articulo.setMinimo(this.almacenArticuloPivote.getMinimo());
			articulo.setStock(this.almacenArticuloPivote.getStock());
			if(this.idUbicacion!= null && !this.idUbicacion.equals(-1L)){
				for(AlmacenUbicacion recordUbicacion: this.ubicaciones){
					if(recordUbicacion.getKey().equals(this.idUbicacion)){
						ubicacion= recordUbicacion;
						articulo.setIdAlmacenUbicacion(recordUbicacion.getKey());
					} // if
				} // for
				if(ubicacion!= null){
					articulo.setAnaquel(ubicacion.getAnaquel());
					articulo.setPiso(ubicacion.getPiso());
					articulo.setCuarto(ubicacion.getCuarto());
					articulo.setCharola(ubicacion.getCharola());
				} // if				
			} // if
			this.almacenArticuloPivote= new AlmacenArticulo();
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
		finally{			
			this.countIndice++;
		} // finally
	} // doAgregarAlmacenArticulo
	
	public void doConsultarAlmacenArticulo(){
		AlmacenArticulo articulo         = null;
		AlmacenUbicacion almacenUbicacion= null;
		AlmacenUbicacion ubicacionDefault= null;
		try {					
			articulo= this.almacenArticulo.get(this.almacenArticulo.indexOf(this.almacenArticuloSeleccion));			
			articulo.setModificar(true);
			this.almacenArticuloPivote.setModificar(true);
			this.almacenArticuloPivote.setIdArticulo(articulo.getIdArticulo());
			this.almacenArticuloPivote.setMaximo(articulo.getMaximo());
			this.almacenArticuloPivote.setMinimo(articulo.getMinimo());
			this.almacenArticuloPivote.setStock(articulo.getStock());
			this.almacenArticuloPivote.setArticulo(this.resultadoBusquedaArticulo);
			if(articulo.getIdAlmacenUbicacion()!= null){
				for(AlmacenUbicacion recordAlmacenUbicacion: this.ubicaciones){
					if(recordAlmacenUbicacion.getKey().equals(articulo.getIdAlmacenUbicacion()))
						almacenUbicacion= recordAlmacenUbicacion;				
					else if(recordAlmacenUbicacion.getPiso()!= null && recordAlmacenUbicacion.getPiso().equals("GENERAL"))
						ubicacionDefault= recordAlmacenUbicacion;			
				} // for
				if(almacenUbicacion== null)
					almacenUbicacion= ubicacionDefault;
				if(almacenUbicacion!= null){
					this.almacenArticuloPivote.setIdAlmacenUbicacion(almacenUbicacion.getIdAlmacenUbicacion());
					this.almacenArticuloPivote.setAnaquel(almacenUbicacion.getAnaquel());
					this.almacenArticuloPivote.setPiso(almacenUbicacion.getPiso());
					this.almacenArticuloPivote.setCuarto(almacenUbicacion.getCuarto());
					this.almacenArticuloPivote.setCharola(almacenUbicacion.getCharola());
					this.idUbicacion= almacenUbicacion.getIdAlmacenUbicacion();
				} // if
			} // if
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch			
	} // doEliminarAlmacenArticulo
	
	private void addDeleteList(IBaseDto dto) throws Exception{
		Transaccion transaccion= null;
		try {
			transaccion= new Transaccion(dto);
			transaccion.ejecutar(EAccion.DEPURAR);
			//this.deleteList.add(dto);
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
	} // addDeleteList	
	
	public void doLoadUbicaciones(){		
		try {
			this.ubicacionesItems= new ArrayList<>();
			for(AlmacenUbicacion item: this.ubicaciones)
				this.ubicacionesItems.add(new UISelectItem(item.getKey(), item.getUbicacion()));
			if(!this.ubicacionesItems.isEmpty()){
				this.almacenUbicacionItem= this.ubicacionesItems.get(0);
				this.idUbicacion= (Long) UIBackingUtilities.toFirstKeySelectItem(this.ubicacionesItems);
			} // if
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch			
	}
	
	public void doActualizarListaUbicaciones(TabChangeEvent event){		
		try {
			if(event.getTab().getTitle().equals("Articulos"))
				UIBackingUtilities.execute("widAgregarUbicacion();");
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
	} // doActualizarListaUbicaciones
}
