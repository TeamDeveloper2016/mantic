package mx.org.kaana.mantic.libs.factura.beans;

import java.io.Serializable;

public class InformacionAduanera implements Serializable{
	
	private static final long serialVersionUID = 3371561528227738285L;
	private String numeroPedimento;

	public InformacionAduanera() {
		this(null);
	}

	public InformacionAduanera(String numeroPedimento) {
		this.numeroPedimento = numeroPedimento;
	}

	public String getNumeroPedimento() {
		return numeroPedimento;
	}

	public void setNumeroPedimento(String numeroPedimento) {
		this.numeroPedimento = numeroPedimento;
	}	

	@Override
	public String toString() {
		return "InformacionAduanera{"+"numeroPedimento="+numeroPedimento+'}';
	}
	
}
