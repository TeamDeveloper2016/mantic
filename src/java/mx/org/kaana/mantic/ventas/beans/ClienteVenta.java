package mx.org.kaana.mantic.ventas.beans;

import java.util.List;
import mx.org.kaana.mantic.catalogos.clientes.beans.Domicilio;
import mx.org.kaana.mantic.db.dto.TcManticClientesDto;
import mx.org.kaana.mantic.db.dto.TrManticClienteTipoContactoDto;

public class ClienteVenta {

	private TcManticClientesDto cliente;
	private Domicilio domicilio;
	private List<TrManticClienteTipoContactoDto> contacto;

	public ClienteVenta() {
		this(null, null, null);
	}
	
	public ClienteVenta(TcManticClientesDto cliente, Domicilio domicilio, List<TrManticClienteTipoContactoDto> contacto) {
		this.cliente = cliente;
		this.domicilio = domicilio;
		this.contacto = contacto;
	}

	public TcManticClientesDto getCliente() {
		return cliente;
	}

	public void setCliente(TcManticClientesDto cliente) {
		this.cliente = cliente;
	}

	public Domicilio getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(Domicilio domicilio) {
		this.domicilio = domicilio;
	}

	public List<TrManticClienteTipoContactoDto> getContacto() {
		return contacto;
	}

	public void setContacto(List<TrManticClienteTipoContactoDto> contacto) {
		this.contacto = contacto;
	}	
}