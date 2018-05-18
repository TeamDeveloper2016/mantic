package mx.org.kaana.mantic.libs.factura.beans;

import java.io.Serializable;

public class Concepto extends Articulo implements Serializable{

	private static final long serialVersionUID = 4868374301878977376L;	
	private String claveProdServ;
	private String noIdentificacion;
	private String cantidad;
	private String claveUnidad;
	private String unidad;
	private String descripcion;
	private String valorUnitario;	
	private Traslado traslado;
	private InformacionAduanera informacionAduanera;

	public Concepto() {
		this(new Traslado(), new InformacionAduanera(), null, null, null, null, null, null, null);
	}

	public Concepto(Traslado traslado, InformacionAduanera informacionAduanera, String claveProdServ, String noIdentificacion, String cantidad, String claveUnidad, String unidad, String descripcion, String valorUnitario) {
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
}