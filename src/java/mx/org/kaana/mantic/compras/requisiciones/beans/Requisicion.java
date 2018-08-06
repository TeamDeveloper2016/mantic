package mx.org.kaana.mantic.compras.requisiciones.beans;

import java.io.Serializable;
import java.util.Date;
import mx.org.kaana.mantic.db.dto.TcManticRequisicionesDto;

public class Requisicion extends TcManticRequisicionesDto implements Serializable{

	private static final long serialVersionUID = -4646811548830622174L;	
	private Date pedido;
	private Date entrega;

	public Requisicion() {
		this(new Date(), new Date());
	}

	public Requisicion(Long key) {
		this(key, new Date(), new Date());
	}

	public Requisicion(Date pedido, Date entrega) {
		this(-1L, new Date(), new Date());
	}
	
	public Requisicion(Long key, Date pedido, Date entrega) {
		super(key);
		this.pedido  = pedido;
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