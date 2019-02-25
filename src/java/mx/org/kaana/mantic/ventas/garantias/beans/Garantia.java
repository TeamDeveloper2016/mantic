package mx.org.kaana.mantic.ventas.garantias.beans;

import java.io.Serializable;
import mx.org.kaana.mantic.enums.ETiposGarantias;
import mx.org.kaana.mantic.ventas.beans.TicketVenta;
import mx.org.kaana.mantic.ventas.caja.beans.VentaFinalizada;

public class Garantia extends VentaFinalizada implements Serializable {

	private static final long serialVersionUID = -4603022684128624529L;
	private TicketVenta garantia;
	private PagoGarantia pagoGarantia;
	private ETiposGarantias tipoGarantia;
	
	public Garantia() {
		this(new TicketVenta(), new PagoGarantia());
	}

	public Garantia(TicketVenta garantia, PagoGarantia pagoGarantia) {
		this(garantia, pagoGarantia, ETiposGarantias.RECIBIDA);
	}
	
	public Garantia(TicketVenta garantia, PagoGarantia pagoGarantia, ETiposGarantias tipoGarantia) {
		super();
		this.garantia    = garantia;
		this.pagoGarantia= pagoGarantia;
		this.tipoGarantia= tipoGarantia;
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

	public ETiposGarantias getTipoGarantia() {
		return tipoGarantia;
	}

	public void setTipoGarantia(ETiposGarantias tipoGarantia) {
		this.tipoGarantia = tipoGarantia;
	}	
}
