package mx.org.kaana.mantic.ventas.garantias.beans;

import java.io.Serializable;

public class PagoGarantia implements Serializable{

	private static final long serialVersionUID= 6058115499284027134L;	
	private Long idTipoVenta;
	private Long idBanco;
	private String transferencia;
	private Long idTipoPago;
	private Long tipoDevolucion;

	public PagoGarantia() {
		this(-1L, null, -1L, 1L);
	}

	public PagoGarantia(Long idBanco, String transferencia, Long idTipoPago, Long idTipoVenta) {
		this(idBanco, transferencia, idTipoPago, idTipoVenta, 1L);
	}
	
	public PagoGarantia(Long idBanco, String transferencia, Long idTipoPago, Long idTipoVenta, Long tipoDevolucion) {
		this.idBanco       = idBanco;
		this.transferencia = transferencia;
		this.idTipoPago    = idTipoPago;
		this.tipoDevolucion= tipoDevolucion;
	}

	public Long getIdBanco() {
		return idBanco;
	}

	public void setIdBanco(Long idBanco) {
		this.idBanco = idBanco;
	}

	public String getTransferencia() {
		return transferencia;
	}

	public void setTransferencia(String transferencia) {
		this.transferencia = transferencia;
	}

	public Long getIdTipoPago() {
		return idTipoPago;
	}

	public void setIdTipoPago(Long idTipoPago) {
		this.idTipoPago = idTipoPago;
	}	

	public Long getIdTipoVenta() {
		return idTipoVenta;
	}

	public void setIdTipoVenta(Long idTipoVenta) {
		this.idTipoVenta = idTipoVenta;
	}	

	public Long getTipoDevolucion() {
		return tipoDevolucion;
	}

	public void setTipoDevolucion(Long tipoDevolucion) {
		this.tipoDevolucion = tipoDevolucion;
	}	
}