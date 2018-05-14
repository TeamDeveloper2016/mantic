package mx.org.kaana.mantic.db.dto;

import java.io.Serializable;
import java.sql.Blob;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Lob;
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
  @Column (name="codigo")
  private String codigo;
  @Column (name="costo")
  private Double costo;
  @Column (name="descuento")
  private String descuento;
  @Column (name="id_orden_compra")
  private Long idOrdenCompra;
  @Column (name="extras")
  private String extras;
  @Column (name="importe")
  private Double importe;
  @Column (name="registro")
  private Timestamp registro;
  @Column (name="propio")
  private String propio;
  @Column (name="iva")
  private Double iva;
  @Column (name="total_impuesto")
  private Double totalImpuesto;
  @Column (name="sub_total")
  private Double subTotal;
  @Column (name="cantidad")
  private Long cantidad;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_orden_detalle")
  private Long idOrdenDetalle;
  @Column (name="id_articulo")
  private Long idArticulo;
  @Column (name="total_descuentos")
  private Double totalDescuentos;

  @Column (name="nombre")
  private String nombre;

  public TcManticOrdenesDetallesDto() {
    this(new Long(-1L));
  }

  public TcManticOrdenesDetallesDto(Long key) {
    this(null, null, null, null, null, null, null, null, null, null, null, new Long(-1L), null, null, null);
    setKey(key);
  }

  public TcManticOrdenesDetallesDto(String codigo, Double costo, String descuento, Long idOrdenCompra, String extras, Double importe, String propio, Double iva, Double totalImpuesto, Double subTotal, Long cantidad, Long idOrdenDetalle, Long idArticulo, Double totalDescuentos, String nombre) {
    setCodigo(codigo);
    setCosto(costo);
    setDescuento(descuento);
    setIdOrdenCompra(idOrdenCompra);
    setExtras(extras);
    setImporte(importe);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
    setPropio(propio);
    setIva(iva);
    setTotalImpuesto(totalImpuesto);
    setSubTotal(subTotal);
    setCantidad(cantidad);
    setIdOrdenDetalle(idOrdenDetalle);
    setIdArticulo(idArticulo);
    setTotalDescuentos(totalDescuentos);
    setNombre(nombre);
  }
	
  public void setCodigo(String codigo) {
    this.codigo = codigo;
  }

  public String getCodigo() {
    return codigo;
  }

  public void setCosto(Double costo) {
    this.costo = costo;
  }

  public Double getCosto() {
    return costo;
  }

  public void setDescuento(String descuento) {
    this.descuento = descuento;
  }

  public String getDescuento() {
    return descuento;
  }

  public void setIdOrdenCompra(Long idOrdenCompra) {
    this.idOrdenCompra = idOrdenCompra;
  }

  public Long getIdOrdenCompra() {
    return idOrdenCompra;
  }

  public void setExtras(String extras) {
    this.extras = extras;
  }

  public String getExtras() {
    return extras;
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

  public void setPropio(String propio) {
    this.propio = propio;
  }

  public String getPropio() {
    return propio;
  }

  public void setIva(Double iva) {
    this.iva = iva;
  }

  public Double getIva() {
    return iva;
  }

  public void setTotalImpuesto(Double totalImpuesto) {
    this.totalImpuesto = totalImpuesto;
  }

  public Double getTotalImpuesto() {
    return totalImpuesto;
  }

  public void setSubTotal(Double subTotal) {
    this.subTotal = subTotal;
  }

  public Double getSubTotal() {
    return subTotal;
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

  public void setTotalDescuentos(Double totalDescuentos) {
    this.totalDescuentos = totalDescuentos;
  }

  public Double getTotalDescuentos() {
    return totalDescuentos;
  }

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre=nombre;
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
		regresar.append(getCodigo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCosto());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getDescuento());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdOrdenCompra());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getExtras());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getImporte());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getPropio());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIva());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getTotalImpuesto());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getSubTotal());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCantidad());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdOrdenDetalle());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdArticulo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getTotalDescuentos());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getNombre());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("codigo", getCodigo());
		regresar.put("costo", getCosto());
		regresar.put("descuento", getDescuento());
		regresar.put("idOrdenCompra", getIdOrdenCompra());
		regresar.put("extras", getExtras());
		regresar.put("importe", getImporte());
		regresar.put("registro", getRegistro());
		regresar.put("propio", getPropio());
		regresar.put("iva", getIva());
		regresar.put("totalImpuesto", getTotalImpuesto());
		regresar.put("subTotal", getSubTotal());
		regresar.put("cantidad", getCantidad());
		regresar.put("idOrdenDetalle", getIdOrdenDetalle());
		regresar.put("idArticulo", getIdArticulo());
		regresar.put("totalDescuentos", getTotalDescuentos());
		regresar.put("nombre", getNombre());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getCodigo(), getCosto(), getDescuento(), getIdOrdenCompra(), getExtras(), getImporte(), getRegistro(), getPropio(), getIva(), getTotalImpuesto(), getSubTotal(), getCantidad(), getIdOrdenDetalle(), getIdArticulo(), getTotalDescuentos(), getNombre()
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


