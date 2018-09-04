package mx.org.kaana.mantic.ventas.garantias.beans;

import java.io.Serializable;

public class PagoGarantia implements Serializable{
	
	private Long idBanco;
	private String transferencia;
	private Long idTipoPago;

	public PagoGarantia() {
		this(-1L, null, -1L);
	}

	public PagoGarantia(Long idBanco, String transferencia, Long idTipoPago) {
		this.idBanco      = idBanco;
		this.transferencia= transferencia;
		this.idTipoPago   = idTipoPago;
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
}