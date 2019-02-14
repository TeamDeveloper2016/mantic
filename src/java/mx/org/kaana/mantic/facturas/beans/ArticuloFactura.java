package mx.org.kaana.mantic.facturas.beans;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;

public class ArticuloFactura implements Serializable, IBaseDto {

	private static final long serialVersionUID = -4697872800407924572L;	
	private Long id;
	private String idFacturama;
	private String codigo;
	private String codigoHacienda;
	private String unidad;
	private String identificador;
	private String nombre;
	private String descripcion;
	private double precio;
	private double iva;
	private double cantidad;
	private double subtotal;
	private double total;
	private double base;
	private double impuestos;

	public ArticuloFactura() {
		this(-1L);
	}
	
	public ArticuloFactura(Long id) {
		this.id = id;
	}

	public ArticuloFactura(Long id, String idFacturama, String codigo, String unidad, String identificador, String nombre, String descripcion, double precio, String codigoHacienda, double iva) {
		this(id, idFacturama, codigo, unidad, identificador, nombre, descripcion, precio, codigoHacienda, iva, 0D, 0D, 0D, 100D);
	}
	
	public ArticuloFactura(Long id, String idFacturama, String codigo, String unidad, String identificador, String nombre, String descripcion, double precio, String codigoHacienda, double iva, double cantidad,	double subtotal, double total, double base) {
		this.id            = id;
		this.idFacturama   = idFacturama;
		this.codigo        = codigo;
		this.unidad        = unidad;
		this.identificador = identificador;
		this.nombre        = nombre;
		this.descripcion   = descripcion;
		this.precio        = precio;
		this.codigoHacienda= codigoHacienda;
		this.iva           = iva;
		this.cantidad      = cantidad;
		this.subtotal      = subtotal;
		this.total         = total;
		this.base          = base;
		this.impuestos     = 0D;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	public String getCodigoHacienda() {
		return codigoHacienda;
	}

	public void setCodigoHacienda(String codigoHacienda) {
		this.codigoHacienda = codigoHacienda;
	}	

	public double getIva() {
		return iva;
	}

	public void setIva(double iva) {
		this.iva = iva;
	}	

	public double getCantidad() {
		return cantidad;
	}

	public void setCantidad(double cantidad) {
		this.cantidad = cantidad;
	}

	public double getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public double getBase() {
		return base;
	}

	public void setBase(double base) {
		this.base = base;
	}

	public double getImpuestos() {
		return impuestos;
	}

	public void setImpuestos(double impuestos) {
		this.impuestos=impuestos;
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

	@Override
	public Long getKey() {
		if(id!= null)
			return id;
		else
			return -1L;
	}

	@Override
	public void setKey(Long key) {
		this.id= key;
	}

	@Override
	public Map<String, Object> toMap() {
		return null;
	}

	@Override
	public Object[] toArray() {
		return null;
	}

	@Override
	public boolean isValid() {
		return this.id!= null && Long.valueOf(this.id) > -1L;
	}

	@Override
	public Object toValue(String name) {
		return null;
	}

	@Override
	public String toAllKeys() {
		return null;
	}

	@Override
	public String toKeys() {
		return null;
	}

	@Override
	public Class toHbmClass() {
		return ArticuloFactura.class;
	}
}
