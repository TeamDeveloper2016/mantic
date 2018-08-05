package mx.org.kaana.mantic.compras.requisiciones.benas;

import java.io.Serializable;
import java.util.Date;
import mx.org.kaana.mantic.db.dto.TcManticRequisicionesDto;

public class Requisicion extends TcManticRequisicionesDto implements Serializable{
	
	private Date pedido;
	private Date entrega;

	public Requisicion() {
		this(new Date(), new Date());
	}

	public Requisicion(Date pedido, Date entrega) {
		super();
		this.pedido = pedido;
		this.entrega = entrega;
	}

	public Date getPedido() {
		return pedido;
	}

	public void setPedido(Date pedido) {
		this.pedido = pedido;
		if(this.pedido!= null)
			setFechaPedido(new java.sql.Date(this.pedido.getTime()));
	}

	public Date getEntrega() {
		return entrega;
	}

	public void setEntrega(Date entrega) {
		this.entrega = entrega;
		if(this.entrega!= null)
			setFechaEntregada(new java.sql.Date(this.entrega.getTime()));
	}	
}