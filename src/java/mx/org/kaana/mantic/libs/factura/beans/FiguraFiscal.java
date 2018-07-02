package mx.org.kaana.mantic.libs.factura.beans;

import java.io.Serializable;

public class FiguraFiscal implements Serializable {

	private static final long serialVersionUID = 1L;	
	private String rfc;
	private String nombre;

	public FiguraFiscal() {
		this(null, null);
	}

	public FiguraFiscal(String rfc, String nombre) {
		this.rfc   = rfc;
		this.nombre= nombre;
	}

	public String getRfc() {
		return rfc;
	}

	public void setRfc(String rfc) {
		this.rfc = rfc;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}		

	@Override
	public String toString() {
		return "FiguraFiscal{"+"rfc="+rfc+", nombre="+nombre+'}';
	}
	
}
