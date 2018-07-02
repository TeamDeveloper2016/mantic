package mx.org.kaana.mantic.libs.factura.beans;

import java.io.Serializable;

public class Articulo implements Serializable{
	
	private static final long serialVersionUID = -7390954845655449945L;
	private String importe;

	public Articulo() {
		this(null);
	}

	public Articulo(String importe) {
		this.importe = importe;
	}

	public String getImporte() {
		return importe;
	}

	public void setImporte(String importe) {
		this.importe = importe;
	}	

	@Override
	public String toString() {
		return "Articulo{"+"importe="+importe+'}';
	}
	
}
