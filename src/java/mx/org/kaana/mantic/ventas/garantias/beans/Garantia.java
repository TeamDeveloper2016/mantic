package mx.org.kaana.mantic.ventas.garantias.beans;

import java.io.Serializable;
import mx.org.kaana.mantic.ventas.beans.TicketVenta;
import mx.org.kaana.mantic.ventas.caja.beans.VentaFinalizada;

public class Garantia extends VentaFinalizada implements Serializable {

	private TicketVenta garantia;
	private PagoGarantia pagoGarantia;
	
	public Garantia() {
		this(new TicketVenta(), new PagoGarantia());
	}

	public Garantia(TicketVenta garantia, PagoGarantia pagoGarantia) {
		super();
		this.garantia    = garantia;
		this.pagoGarantia= pagoGarantia;
	}

	public TicketVenta getGarantia() {
		return garantia;
	}

	public void setGarantia(TicketVenta garantia) {
		this.garantia = garantia;
	}	

	public PagoGarantia getPagoGarantia() {
		return pagoGarantia;
	}

	public void setPagoGarantia(PagoGarantia pagoGarantia) {
		this.pagoGarantia = pagoGarantia;
	}	
}
