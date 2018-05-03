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
@Table(name="tc_mantic_ordenes_detalles")
public class TcManticOrdenesDetallesDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="costo")
  private Double costo;
  @Column (name="descuento")
  private Double descuento;
  @Column (name="id_orden_compra")
  private Long idOrdenCompra;
  @Column (name="cantidad")
  private Long cantidad;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_orden_detalle")
  private Long idOrdenDetalle;
  @Column (name="id_articulo")
  private Long idArticulo;
  @Column (name="importe")
  private Double importe;
  @Column (name="registro")
  private Timestamp registro;

  public TcManticOrdenesDetallesDto() {
    this(new Long(-1L));
  }

  public TcManticOrdenesDetallesDto(Long key) {
    this(null, null, null, null, new Long(-1L), null, null);
    setKey(key);
  }

  public TcManticOrdenesDetallesDto(Double costo, Double descuento, Long idOrdenCompra, Long cantidad, Long idOrdenDetalle, Long idArticulo, Double importe) {
    setCosto(costo);
    setDescuento(descuento);
    setIdOrdenCompra(idOrdenCompra);
    setCantidad(cantidad);
    setIdOrdenDetalle(idOrdenDetalle);
    setIdArticulo(idArticulo);
    setImporte(importe);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  }
	
  public void setCosto(Double costo) {
    this.costo = costo;
  }

  public Double getCosto() {
    return costo;
  }

  public void setDescuento(Double descuento) {
    this.descuento = descuento;
  }

  public Double getDescuento() {
    return descuento;
  }

  public void setIdOrdenCompra(Long idOrdenCompra) {
    this.idOrdenCompra = idOrdenCompra;
  }

  public Long getIdOrdenCompra() {
    return idOrdenCompra;
  }

  public void setCantidad(Long cantidad) {
    this.cantidad = cantidad;
  }

  public Long getCantidad() {
    return cantidad;
  }

  public void setIdOrdenDetalle(Long idOrdenDetalle) {
    this.idOrdenDetalle = idOrdenDetalle;
  }

  public Long getIdOrdenDetalle() {
    return idOrdenDetalle;
  }

  public void setIdArticulo(Long idArticulo) {
    this.idArticulo = idArticulo;
  }

  public Long getIdArticulo() {
    return idArticulo;
  }

  public void setImporte(Double importe) {
    this.importe = importe;
  }

  public Double getImporte() {
    return importe;
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
  	return getIdOrdenDetalle();
  }

  @Override
  public void setKey(Long key) {
  	this.idOrdenDetalle = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getCosto());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getDescuento());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdOrdenCompra());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCantidad());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdOrdenDetalle());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdArticulo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getImporte());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("costo", getCosto());
		regresar.put("descuento", getDescuento());
		regresar.put("idOrdenCompra", getIdOrdenCompra());
		regresar.put("cantidad", getCantidad());
		regresar.put("idOrdenDetalle", getIdOrdenDetalle());
		regresar.put("idArticulo", getIdArticulo());
		regresar.put("importe", getImporte());
		regresar.put("registro", getRegistro());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getCosto(), getDescuento(), getIdOrdenCompra(), getCantidad(), getIdOrdenDetalle(), getIdArticulo(), getImporte(), getRegistro()
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
    regresar.append("idOrdenDetalle~");
    regresar.append(getIdOrdenDetalle());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdOrdenDetalle());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticOrdenesDetallesDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdOrdenDetalle()!= null && getIdOrdenDetalle()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticOrdenesDetallesDto other = (TcManticOrdenesDetallesDto) obj;
    if (getIdOrdenDetalle() != other.idOrdenDetalle && (getIdOrdenDetalle() == null || !getIdOrdenDetalle().equals(other.idOrdenDetalle))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdOrdenDetalle() != null ? getIdOrdenDetalle().hashCode() : 0);
    return hash;
  }

}


