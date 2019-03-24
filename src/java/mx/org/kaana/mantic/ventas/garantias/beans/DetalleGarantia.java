package mx.org.kaana.mantic.ventas.garantias.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.mantic.ventas.caja.beans.VentaFinalizada;

public class DetalleGarantia extends VentaFinalizada implements Serializable {

	private static final long serialVersionUID= -4603022684128624529L;
	private List<Garantia> garantias;
	private PagoGarantia pagoGarantia;
	private Long idVenta;
	private Long idCliente;
	private EAccion accionCredito;
	private Double pagoCredito;
	private Double devolucionCredito;
	
	public DetalleGarantia() {
		this(new ArrayList<Garantia>(), new PagoGarantia(), null, null, EAccion.COMPLETO, 0D, 0D);
	}

	public DetalleGarantia(List<Garantia> garantias, PagoGarantia pagoGarantia, Long idVenta, Long idCliente, EAccion accionCredito, Double pagoCredito, Double devolucionCredito) {
		super();
		this.garantias    = garantias;
		this.pagoGarantia = pagoGarantia;		
		this.idVenta      = idVenta;
		this.idCliente    = idCliente;
		this.accionCredito= accionCredito;
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

	public Long getIdVenta() {
		return idVenta;
	}

	public void setIdVenta(Long idVenta) {
		this.idVenta = idVenta;
	}

	public Long getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Long idCliente) {
		this.idCliente = idCliente;
	}

	public EAccion getAccionCredito() {
		return accionCredito;
	}

	public void setAccionCredito(EAccion accionCredito) {
		this.accionCredito = accionCredito;
	}	

	public Double getPagoCredito() {
		return pagoCredito;
	}

	public void setPagoCredito(Double pagoCredito) {
		this.pagoCredito = pagoCredito;
	}

	public Double getDevolucionCredito() {
		return devolucionCredito;
	}

	public void setDevolucionCredito(Double devolucionCredito) {
		this.devolucionCredito = devolucionCredito;
	}	
}