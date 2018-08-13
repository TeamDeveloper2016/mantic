package mx.org.kaana.mantic.ventas.caja.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import mx.org.kaana.mantic.catalogos.clientes.beans.ClienteTipoContacto;
import mx.org.kaana.mantic.catalogos.clientes.beans.Domicilio;
import mx.org.kaana.mantic.compras.ordenes.beans.Articulo;
import mx.org.kaana.mantic.db.dto.TcManticClientesDto;
import mx.org.kaana.mantic.ventas.beans.TicketVenta;

public class VentaFinalizada implements Serializable{

	private static final long serialVersionUID = 6598582355031809104L;
	private TicketVenta ticketVenta;
	private List<ClienteTipoContacto> correosContacto;
	private ClienteTipoContacto telefono;
	private ClienteTipoContacto celular;
	private Pago totales;
	private Domicilio domicilio;
	private TcManticClientesDto cliente;
	private Boolean facturar;
	private Boolean credito;
	private List<Articulo> articulos;
	private Long idCaja;

	public VentaFinalizada() {
		this(new TicketVenta(), new ArrayList<ClienteTipoContacto>(), new ClienteTipoContacto(), new ClienteTipoContacto(), new Pago(), new Domicilio(), new TcManticClientesDto(), false, false, new ArrayList<Articulo>(), -1L);
	}
	
	public VentaFinalizada(TicketVenta ticketVenta, List<ClienteTipoContacto> correosContacto, ClienteTipoContacto telefono, ClienteTipoContacto celular, Pago totales, Domicilio domicilio, TcManticClientesDto cliente, Boolean facturar, Boolean credito, List<Articulo> articulos, Long idCaja) {
		this.ticketVenta    = ticketVenta;
		this.correosContacto= correosContacto;
		this.telefono       = telefono;
		this.celular        = celular;
		this.totales        = totales;
		this.domicilio      = domicilio;
		this.cliente        = cliente;
		this.facturar       = facturar;
		this.credito        = credito;
		this.articulos      = articulos;
		this.idCaja         = idCaja;
	}

	public TicketVenta getTicketVenta() {
		return ticketVenta;
	}

	public void setTicketVenta(TicketVenta ticketVenta) {
		this.ticketVenta = ticketVenta;
	}

	public List<ClienteTipoContacto> getCorreosContacto() {
		return correosContacto;
	}

	public void setCorreosContacto(List<ClienteTipoContacto> correosContacto) {
		this.correosContacto = correosContacto;
	}

	public ClienteTipoContacto getTelefono() {
		return telefono;
	}

	public void setTelefono(ClienteTipoContacto telefono) {
		this.telefono = telefono;
	}

	public ClienteTipoContacto getCelular() {
		return celular;
	}

	public void setCelular(ClienteTipoContacto celular) {
		this.celular = celular;
	}

	public Pago getTotales() {
		return totales;
	}

	public void setTotales(Pago totales) {
		this.totales = totales;
	}

	public Domicilio getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(Domicilio domicilio) {
		this.domicilio = domicilio;
	}

	public TcManticClientesDto getCliente() {
		return cliente;
	}

	public void setCliente(TcManticClientesDto cliente) {
		this.cliente = cliente;
	}

	public boolean isFacturar() {
		return facturar;
	}

	public void setFacturar(Boolean facturar) {
		this.facturar = facturar;
	}

	public boolean isCredito() {
		return credito;
	}

	public void setCredito(Boolean credito) {
		this.credito = credito;
	}	

	public List<Articulo> getArticulos() {
		return articulos;
	}

	public void setArticulos(List<Articulo> articulos) {
		this.articulos = articulos;
	}	

	public Long getIdCaja() {
		return idCaja;
	}

	public void setIdCaja(Long idCaja) {
		this.idCaja = idCaja;
	}	
}
