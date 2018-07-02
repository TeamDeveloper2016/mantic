package mx.org.kaana.mantic.libs.factura.beans;

import java.io.Serializable;

public class Emisor extends FiguraFiscal implements Serializable{

	private static final long serialVersionUID = 1548623531085601312L;
	private String regimenFiscal;

	public Emisor() {
		this(null);
	}

	public Emisor(String regimenFiscal) {
		this.regimenFiscal = regimenFiscal;
	}	

	public String getRegimenFiscal() {
		return regimenFiscal;
	}

	public void setRegimenFiscal(String regimenFiscal) {
		this.regimenFiscal = regimenFiscal;
	}		

	@Override
	public String toString() {
		return "Emisor{"+ super.toString()+ ", regimenFiscal="+regimenFiscal+'}';
	}
	
	
}
