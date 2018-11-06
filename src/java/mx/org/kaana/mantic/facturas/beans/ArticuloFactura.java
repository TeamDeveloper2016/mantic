package mx.org.kaana.mantic.facturas.beans;

import java.io.Serializable;
import java.util.Objects;

public class ArticuloFactura implements Serializable{

	private static final long serialVersionUID = -4697872800407924572L;	
	private String id;
	private String idFacturama;
	private String codigo;
	private String unidad;
	private String identificador;
	private String nombre;
	private String descripcion;
	private double precio;

	public ArticuloFactura() {
		this(null);
	}
	
	public ArticuloFactura(String id) {
		this.id = id;
	}

	public ArticuloFactura(String id, String idFacturama, String codigo, String unidad, String identificador, String nombre, String descripcion, double precio) {
		this.id           = id;
		this.idFacturama  = idFacturama;
		this.codigo       = codigo;
		this.unidad       = unidad;
		this.identificador= identificador;
		this.nombre       = nombre;
		this.descripcion  = descripcion;
		this.precio       = precio;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIdFacturama() {
		return idFacturama;
	}

	public void setIdFacturama(String idFacturama) {
		this.idFacturama = idFacturama;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getUnidad() {
		return unidad;
	}

	public void setUnidad(String unidad) {
		this.unidad = unidad;
	}

	public String getIdentificador() {
		return identificador;
	}

	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}	
	
	@Override
	public int hashCode() {
		int hash=7;
		hash=11*hash+Objects.hashCode(this.identificador);
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
		final ArticuloFactura other=(ArticuloFactura) obj;
		if (!Objects.equals(this.identificador, other.identificador)) {
			return false;
		}
		return true;
	}
}
