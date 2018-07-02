package mx.org.kaana.mantic.libs.factura.beans;

import java.io.Serializable;

public class Impuesto implements Serializable{
	
	private static final long serialVersionUID = 1577909823552675210L;
	private String totalImpuestosTrasladados;
	private Traslado traslado;
	
	public Impuesto() {
		this(new Traslado(), null);
	}

	public Impuesto(Traslado traslado, String totalImpuestosTrasladados) {
		this.traslado = traslado;
		this.totalImpuestosTrasladados = totalImpuestosTrasladados;
	}

	public String getTotalImpuestosTrasladados() {
		return totalImpuestosTrasladados;
	}

	public void setTotalImpuestosTrasladados(String totalImpuestosTrasladados) {
		this.totalImpuestosTrasladados = totalImpuestosTrasladados;
	}	

	public Traslado getTraslado() {
		return traslado;
	}

	public void setTraslado(Traslado traslado) {
		this.traslado = traslado;
	}	

	@Override
	public String toString() {
		return "Impuesto{"+"totalImpuestosTrasladados="+totalImpuestosTrasladados+", traslado="+traslado+'}';
	}
	
}
