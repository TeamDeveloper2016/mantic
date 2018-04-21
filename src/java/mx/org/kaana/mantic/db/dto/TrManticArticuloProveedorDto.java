package mx.org.kaana.mantic.db.dto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 10/10/2016
 *@time 11:58:22 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

@Entity
@Table(name="tr_mantic_articulo_proveedor")
public class TrManticArticuloProveedorDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="id_proveedor")
  private Long idProveedor;
  @Column (name="precio")
  private Double precio; 
  @Column (name="id_usuario")
  private Long idUsuario;
  @Column (name="descuento")
  private Double descuento;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_articulo_proveedor")
  private Long idArticuloProveedor;
  @Column (name="fecha_compra")
  private Timestamp fechaCompra;
  @Column (name="observaciones")
  private String observaciones;
  @Column (name="cantidad")
  private Double cantidad;
  @Column (name="id_articulo")
  private Long idArticulo;
  @Column (name="registro")
  private Timestamp registro;

  public TrManticArticuloProveedorDto() {
    this(new Long(-1L));
  }

  public TrManticArticuloProveedorDto(Long key) {
    this(null, null, null, null, new Long(-1L), new Timestamp(Calendar.getInstance().getTimeInMillis()), null, null, null);
    setKey(key);
  }

  public TrManticArticuloProveedorDto(Long idProveedor, Double precio, Long idUsuario, Double descuento, Long idArticuloProveedor, Timestamp fechaCompra, String observaciones, Double cantidad, Long idArticulo) {
    setIdProveedor(idProveedor);
    setPrecio(precio);    
    setIdUsuario(idUsuario);
    setDescuento(descuento);
    setIdArticuloProveedor(idArticuloProveedor);
    setFechaCompra(fechaCompra);
    setObservaciones(observaciones);
    setCantidad(cantidad);
    setIdArticulo(idArticulo);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setIdProveedor(Long idProveedor) {
    this.idProveedor = idProveedor;
  }

  public Long getIdProveedor() {
    return idProveedor;
  }

  public void setPrecio(Double precio) {
    this.precio = precio;
  }

  public Double getPrecio() {
    return precio;
  } 

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setDescuento(Double descuento) {
    this.descuento = descuento;
  }

  public Double getDescuento() {
    return descuento;
  }

  public void setIdArticuloProveedor(Long idArticuloProveedor) {
    this.idArticuloProveedor = idArticuloProveedor;
  }

  public Long getIdArticuloProveedor() {
    return idArticuloProveedor;
  }

  public void setFechaCompra(Timestamp fechaCompra) {
    this.fechaCompra = fechaCompra;
  }

  public Timestamp getFechaCompra() {
    return fechaCompra;
  }

  public void setObservaciones(String observaciones) {
    this.observaciones = observaciones;
  }

  public String getObservaciones() {
    return observaciones;
  }

  public void setCantidad(Double cantidad) {
    this.cantidad = cantidad;
  }

  public Double getCantidad() {
    return cantidad;
  }

  public void setIdArticulo(Long idArticulo) {
    this.idArticulo = idArticulo;
  }

  public Long getIdArticulo() {
    return idArticulo;
  }

  public void setRegistro(Timestamp registro) {
    this.registro = registro;
  }

  public Timestamp getRegistro() {
    return registro;
  }

  @Transient
  @Override
  public Long getKey() {
  	return getIdArticuloProveedor();
  }

  @Override
  public void setKey(Long key) {
  	this.idArticuloProveedor = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdProveedor());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getPrecio());	
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdUsuario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getDescuento());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdArticuloProveedor());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getFechaCompra());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getObservaciones());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCantidad());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdArticulo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idProveedor", getIdProveedor());
		regresar.put("precio", getPrecio());
		regresar.put("idUsuario", getIdUsuario());
		regresar.put("descuento", getDescuento());
		regresar.put("idArticuloProveedor", getIdArticuloProveedor());
		regresar.put("fechaCompra", getFechaCompra());
		regresar.put("observaciones", getObservaciones());
		regresar.put("cantidad", getCantidad());
		regresar.put("idArticulo", getIdArticulo());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getIdProveedor(), getPrecio(), getIdUsuario(), getDescuento(), getIdArticuloProveedor(), getFechaCompra(), getObservaciones(), getCantidad(), getIdArticulo(), getRegistro()
    };
    return regresar;
  }

  @Override
  public Object toValue(String name) {
    return Methods.getValue(this, name);
  }

  @Override
  public String toAllKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("|");
    regresar.append("idArticuloProveedor~");
    regresar.append(getIdArticuloProveedor());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdArticuloProveedor());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TrManticArticuloProveedorDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdArticuloProveedor()!= null && getIdArticuloProveedor()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TrManticArticuloProveedorDto other = (TrManticArticuloProveedorDto) obj;
    if (getIdArticuloProveedor() != other.idArticuloProveedor && (getIdArticuloProveedor() == null || !getIdArticuloProveedor().equals(other.idArticuloProveedor))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdArticuloProveedor() != null ? getIdArticuloProveedor().hashCode() : 0);
    return hash;
  }

}


