package mx.org.kaana.mantic.ventas.beans;

import java.io.Serializable;
import mx.org.kaana.libs.formato.Numero;

public class SaldoCliente implements Serializable{

	private static final long serialVersionUID = 5429344596542874600L;
	private Long idCliente;
	private Double totalDeuda;
	private Double totalCredito;
	private Double totalVenta;

	public SaldoCliente() {
		this(3515L, 0D, 0D, 0D);
	}

	public SaldoCliente(Long idCliente, Double totalDeuda, Double totalCredito, Double totalVenta) {
		this.idCliente   = idCliente;
		this.totalDeuda  = totalDeuda;
		this.totalCredito= totalCredito;
		this.totalVenta  = totalVenta;
	}

	public Long getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Long idCliente) {
		this.idCliente = idCliente;
	}

	public Double getTotalDeuda() {
		return totalDeuda;
	}

	public void setTotalDeuda(Double totalDeuda) {
		this.totalDeuda = totalDeuda;
	}

	public Double getTotalCredito() {
		return totalCredito;
	}

	public void setTotalCredito(Double totalCredito) {
		this.totalCredito = totalCredito;
	}

	public Double getTotalSaldo() {
		return this.totalCredito - (this.totalDeuda + this.totalVenta);
	}	

	public Double getTotalVenta() {
		return totalVenta;
	}

	public void setTotalVenta(Double totalVenta) {
		this.totalVenta = totalVenta;
	}	
	
	public boolean isDeudor(){
		boolean regresar= false;
		if(this.totalCredito > 0){
			Double totalSaldo= this.totalCredito - (this.totalDeuda + this.totalVenta);
			regresar= totalSaldo < 0;
		} // if
		return regresar; 
	} // isDeudor
	
	public String getMensaje(){
		return isDeudor() ? "CRÉDITO SUPERADO Y/O PLAZO VENCIDO. CONSULTAR CON CRÉDITO Y COBRANZA" : "";
	}
}
