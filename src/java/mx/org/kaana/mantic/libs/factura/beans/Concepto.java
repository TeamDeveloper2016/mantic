package mx.org.kaana.mantic.libs.factura.beans;

import java.io.Serializable;
import java.util.Objects;

public class Concepto extends Articulo implements Serializable{

	private static final long serialVersionUID = 4868374301878977376L;	
	private String claveProdServ;
	private String noIdentificacion;
	private String cantidad;
	private String claveUnidad;
	private String unidad;
	private String descripcion;
	private String descuento;
	private String valorUnitario;	
	private Traslado traslado;
	private InformacionAduanera informacionAduanera;

	public Concepto() {
		this(new Traslado(), new InformacionAduanera(), null, null, null, null, null, null, null, "0");
	}

	public Concepto(Traslado traslado, InformacionAduanera informacionAduanera, String claveProdServ, String noIdentificacion, String cantidad, String claveUnidad, String unidad, String descripcion, String valorUnitario, String descuento) {
		super();
		this.traslado        = traslado;
		this.informacionAduanera= informacionAduanera;
		this.claveProdServ   = claveProdServ;
		this.noIdentificacion= noIdentificacion;
		this.cantidad        = cantidad;
		this.claveUnidad     = claveUnidad;
		this.unidad          = unidad;
		this.descripcion     = descripcion;
		this.valorUnitario   = valorUnitario;
		this.descuento       = descuento;
	}

	public String getClaveProdServ() {
		return claveProdServ;
	}

	public void setClaveProdServ(String claveProdServ) {
		this.claveProdServ = claveProdServ;
	}

	public String getNoIdentificacion() {
		return noIdentificacion;
	}

	public void setNoIdentificacion(String noIdentificacion) {
		this.noIdentificacion = noIdentificacion;
	}

	public String getCantidad() {
		return cantidad;
	}

	public void setCantidad(String cantidad) {
		this.cantidad = cantidad;
	}

	public String getClaveUnidad() {
		return claveUnidad;
	}

	public void setClaveUnidad(String claveUnidad) {
		this.claveUnidad = claveUnidad;
	}

	public String getUnidad() {
		return unidad;
	}

	public void setUnidad(String unidad) {
		this.unidad = unidad;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getValorUnitario() {
		return valorUnitario;
	}

	public void setValorUnitario(String valorUnitario) {
		this.valorUnitario = valorUnitario;
	}	

	public Traslado getTraslado() {
		return traslado;
	}

	public void setTraslado(Traslado traslado) {
		this.traslado = traslado;
	}

	public InformacionAduanera getInformacionAduanera() {
		return informacionAduanera;
	}

	public void setInformacionAduanera(InformacionAduanera informacionAduanera) {
		this.informacionAduanera = informacionAduanera;
	}	

	public String getDescuento() {
		return descuento;
	}

	public void setDescuento(String descuento) {
		this.descuento=descuento;
	}

	@Override
	public String toString() {
		return "Concepto{"+"claveProdServ="+claveProdServ+", noIdentificacion="+noIdentificacion+", cantidad="+cantidad+", claveUnidad="+claveUnidad+", unidad="+unidad+", descripcion="+descripcion+", valorUnitario="+valorUnitario+", importe="+ this.getImporte()+", traslado="+traslado+", informacionAduanera="+informacionAduanera+'}';
	}

	@Override
	public int hashCode() {
		int hash=5;
		hash=23*hash+Objects.hashCode(this.cantidad);
		hash=23*hash+Objects.hashCode(this.descripcion);
		hash=23*hash+Objects.hashCode(this.valorUnitario);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this==obj) {
			return true;
		}
		if (obj==null) {
			return false;
		}
		if (getClass()!=obj.getClass()) {
			return false;
		}
		final Concepto other=(Concepto) obj;
		if (!Objects.equals(this.cantidad, other.cantidad)) {
			return false;
		}
		if (!Objects.equals(this.descripcion, other.descripcion)) {
			return false;
		}
		if (!Objects.equals(this.valorUnitario, other.valorUnitario)) {
			return false;
		}
		return true;
	}
	
	
	
}