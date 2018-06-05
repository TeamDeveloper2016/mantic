package mx.org.kaana.mantic.taller.beans;

import java.io.Serializable;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.mantic.db.dto.TcManticClientesDto;
import mx.org.kaana.mantic.db.dto.TcManticServiciosDto;
import mx.org.kaana.mantic.taller.reglas.MotorBusqueda;

public class RegistroServicio implements Serializable{

	private static final long serialVersionUID= 6388479874682387478L;
	private Long idServicio;
	private TcManticServiciosDto servicio;
	private TcManticClientesDto cliente;
	private ContactoCliente contactoCliente;

	public RegistroServicio() {
		this(-1L, new TcManticServiciosDto(), new TcManticClientesDto(), new ContactoCliente());
	} // RegistroServicio
	
	public RegistroServicio(Long idServicio) {
		this.idServicio= idServicio;
		init();		
	} // RegistroServicio

	public RegistroServicio(Long idServicio, TcManticServiciosDto servicio, TcManticClientesDto cliente, ContactoCliente contactoCliente) {
		this.idServicio     = idServicio;
		this.servicio       = servicio;
		this.cliente        = cliente;
		this.contactoCliente= contactoCliente;
	}

	public Long getIdServicio() {
		return idServicio;
	}

	public void setIdServicio(Long idServicio) {
		this.idServicio = idServicio;
	}

	public TcManticServiciosDto getServicio() {
		return servicio;
	}

	public void setServicio(TcManticServiciosDto servicio) {
		this.servicio = servicio;
	}

	public TcManticClientesDto getCliente() {
		return cliente;
	}

	public void setCliente(TcManticClientesDto cliente) {
		this.cliente = cliente;
	}

	public ContactoCliente getContactoCliente() {
		return contactoCliente;
	}

	public void setContactoCliente(ContactoCliente contactoCliente) {
		this.contactoCliente = contactoCliente;
	}
	
	private void init(){
		MotorBusqueda motor= null;
		try {
			motor= new MotorBusqueda(this.idServicio);
			this.servicio= motor.toServicio();
			this.cliente= motor.toCliente(this.servicio.getIdCliente());
			this.contactoCliente= motor.toContactoCliente(this.servicio.getIdCliente());
		} // try
		catch (Exception e) {
			Error.mensaje(e);
		} // catch
	} // init
}
