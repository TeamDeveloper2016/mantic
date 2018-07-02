package mx.org.kaana.mantic.libs.factura.beans;

import java.io.Serializable;

public class Receptor extends FiguraFiscal implements Serializable{

	private static final long serialVersionUID = -7055221330561338607L;	
	private String usoCfdi;	

	public Receptor() {
		this(null);
	}

	public Receptor(String usoCfdi) {
		this.usoCfdi = usoCfdi;
	}

	public String getUsoCfdi() {
		return usoCfdi;
	}

	public void setUsoCfdi(String usoCfdi) {
		this.usoCfdi = usoCfdi;
	}			

	@Override
	public String toString() {
		return "Receptor{"+ super.toString()+ ", usoCfdi="+usoCfdi+'}';
	}
	
}
