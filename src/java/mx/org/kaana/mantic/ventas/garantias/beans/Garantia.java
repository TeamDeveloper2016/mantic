package mx.org.kaana.mantic.ventas.garantias.beans;

import java.io.Serializable;
import mx.org.kaana.mantic.ventas.beans.TicketVenta;
import mx.org.kaana.mantic.ventas.caja.beans.VentaFinalizada;

public class Garantia extends VentaFinalizada implements Serializable {

	private TicketVenta garantia;
	
	public Garantia() {
		this(new TicketVenta());
	}

	public Garantia(TicketVenta garantia) {
		super();
		this.garantia = garantia;
	}

	public TicketVenta getGarantia() {
		return garantia;
	}

	public void setGarantia(TicketVenta garantia) {
		this.garantia = garantia;
	}	
}
