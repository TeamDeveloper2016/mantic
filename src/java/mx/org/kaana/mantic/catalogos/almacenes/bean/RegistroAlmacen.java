package mx.org.kaana.mantic.catalogos.almacenes.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.ESql;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.mantic.catalogos.clientes.bean.Domicilio;
import mx.org.kaana.mantic.catalogos.almacenes.reglas.MotorBusqueda;
import mx.org.kaana.mantic.catalogos.almacenes.reglas.Transaccion;
import mx.org.kaana.mantic.db.dto.TcManticAlmacenesDto;

public class RegistroAlmacen implements Serializable{
	
	private static final long serialVersionUID = -360101383355243032L;
	private Long idAlmacen;
	private TcManticAlmacenesDto almacen;
	private List<AlmacenDomicilio> almacenDomicilio;
	private AlmacenDomicilio almacenDomicilioSelecion;
	private List<AlmacenTipoContacto> almacenTiposContacto;
	private AlmacenTipoContacto almacenTipoContactoSeleccion;
	private AlmacenUbicacion almacenUbicacion;
	private List<IBaseDto> deleteList;
	private ContadoresListas contadores;
	private Long countIndice;
	private Domicilio domicilio;
	private Domicilio domicilioPivote;

	public RegistroAlmacen() {
		this(-1L, new TcManticAlmacenesDto(), new ArrayList<AlmacenDomicilio>(), new ArrayList<AlmacenTipoContacto>(), new AlmacenUbicacion(), new Domicilio());
	}
	
	public RegistroAlmacen(Long idAlmacen) {
		this.idAlmacen  = idAlmacen;
		this.contadores = new ContadoresListas();
		this.countIndice= 0L;
		this.deleteList = new ArrayList<>();
		this.domicilio  = new Domicilio();
		this.domicilioPivote= new Domicilio();
		init();		
	}
	
	public RegistroAlmacen(Long idAlmacen, TcManticAlmacenesDto almacen, List<AlmacenDomicilio> almacenDomicilio, List<AlmacenTipoContacto> almacenTiposContacto, AlmacenUbicacion almacenUbicacion, Domicilio domicilio) {
		this.idAlmacen           = idAlmacen;
		this.almacen             = almacen;
		this.almacenDomicilio    = almacenDomicilio;
		this.almacenTiposContacto= almacenTiposContacto;
		this.almacenUbicacion    = almacenUbicacion;
		this.deleteList          = new ArrayList<>();
		this.contadores          = new ContadoresListas();
		this.countIndice         = 0L;
		this.domicilio           = domicilio;
		this.domicilioPivote     = domicilio;
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

	public AlmacenUbicacion getAlmacenUbicacion() {
		return almacenUbicacion;
	}

	public void setAlmacenUbicacion(AlmacenUbicacion almacenUbicacion) {
		this.almacenUbicacion = almacenUbicacion;
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
	
	private void init(){
		MotorBusqueda motorBusqueda= null;
		try {
			motorBusqueda= new MotorBusqueda(this.idAlmacen);
			this.almacen= motorBusqueda.toAlmacen();									
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
			this.almacenDomicilio= motor.toAlmacenesDomicilio();
			for(AlmacenDomicilio almacenDomicilio: this.almacenDomicilio){
				count++;
				almacenDomicilio.setConsecutivo(Long.valueOf(count));
			} // for				
			this.almacenTiposContacto= motor.toAlmacenesTipoContacto();
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
			this.domicilioPivote.setIdDomicilio(pivote.getIdDomicilio());
			this.domicilioPivote.setIdEntidad(pivote.getIdEntidad());
			this.domicilioPivote.setIdMunicipio(pivote.getIdMunicipio());
			this.domicilioPivote.setIdLocalidad(pivote.getIdLocalidad());
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
			if(this.domicilio.getIdDomicilio().equals(-1L))
				almacenDomicilio.setIdDomicilio(registrarDomicilio());
			else
				almacenDomicilio.setIdDomicilio(this.domicilio.getIdDomicilio());
			almacenDomicilio.setIdUsuario(JsfBase.getIdUsuario());
			almacenDomicilio.setIdTipoDomicilio(this.domicilio.getIdTipoDomicilio());
			if(!actualizar)
				almacenDomicilio.setConsecutivo(this.almacenDomicilio.size() + 1L);
			almacenDomicilio.setIdEntidad(this.domicilio.getIdEntidad());
			almacenDomicilio.setIdMunicipio(this.domicilio.getIdMunicipio());
			almacenDomicilio.setIdLocalidad(this.domicilio.getIdLocalidad());
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
	
	private Long registrarDomicilio() throws Exception{
		Long regresar= -1L;
		try {
			this.domicilio.setIdUsuario(JsfBase.getIdUsuario());
			regresar= DaoFactory.getInstance().insert(this.domicilio);
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // registrarDomicilio	
}
