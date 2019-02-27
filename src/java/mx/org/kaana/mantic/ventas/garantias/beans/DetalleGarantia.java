package mx.org.kaana.mantic.ventas.garantias.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import mx.org.kaana.mantic.ventas.caja.beans.VentaFinalizada;

public class DetalleGarantia extends VentaFinalizada implements Serializable {

	private static final long serialVersionUID = -4603022684128624529L;
	private List<Garantia> garantias;
	private PagoGarantia pagoGarantia;	
	
	public DetalleGarantia() {
		this(new ArrayList<Garantia>(), new PagoGarantia());
	}

	public DetalleGarantia(List<Garantia> garantias, PagoGarantia pagoGarantia) {
		super();
		this.garantias   = garantias;
		this.pagoGarantia= pagoGarantia;		
	}			

	public List<Garantia> getGarantias() {
		return garantias;
	}

	public void setGarantias(List<Garantia> garantias) {
		this.garantias = garantias;
	}
	
	public PagoGarantia getPagoGarantia() {
		return pagoGarantia;
	}

	public void setPagoGarantia(PagoGarantia pagoGarantia) {
		this.pagoGarantia = pagoGarantia;
	}
}
