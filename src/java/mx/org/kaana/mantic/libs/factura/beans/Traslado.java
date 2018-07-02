package mx.org.kaana.mantic.libs.factura.beans;

import java.io.Serializable;

public class Traslado extends Articulo implements Serializable {
	
	private static final long serialVersionUID = 8520191941719301263L;
	private String base;
	private String impuesto;
	private String tipoFactor;
	private String tasaCuota;

	public Traslado() {
		this(null, null, null, null);
	}

	public Traslado(String base, String impuesto, String tipoFactor, String tasaCuota) {
		super();
		this.base      = base;
		this.impuesto  = impuesto;
		this.tipoFactor= tipoFactor;
		this.tasaCuota = tasaCuota;
	}

	public String getBase() {
		return base;
	}

	public void setBase(String base) {
		this.base = base;
	}

	public String getImpuesto() {
		return impuesto;
	}

	public void setImpuesto(String impuesto) {
		this.impuesto = impuesto;
	}

	public String getTipoFactor() {
		return tipoFactor;
	}

	public void setTipoFactor(String tipoFactor) {
		this.tipoFactor = tipoFactor;
	}

	public String getTasaCuota() {
		return tasaCuota;
	}

	public void setTasaCuota(String tasaCuota) {
		this.tasaCuota = tasaCuota;
	}	

	@Override
	public String toString() {
		return "Traslado{"+"base="+base+", impuesto="+impuesto+", tipoFactor="+tipoFactor+", tasaCuota="+tasaCuota+'}';
	}
	
}