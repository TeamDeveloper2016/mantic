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
@Table(name="tc_mantic_ventas_detalles")
public class TcManticFicticiasDetallesDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="descuentos")
  private Double descuentos;
  @Column (name="codigo")
  private String codigo;
  @Column (name="unidad_medida")
  private String unidadMedida;
  @Column (name="costo")
  private Double costo;
  @Column (name="id_venta")
  private Long idFicticia;
  @Column (name="descuento")
  private String descuento;
  @Column (name="sat")
  private String sat;
  @Column (name="extras")
  private String extras;
  @Column (name="utilidad")
  private Double utilidad;
  @Column (name="nombre")
  private String nombre;
  @Column (name="importe")
  private Double importe;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_venta_detalle")
  private Long idFicticiaDetalle;
  @Column (name="registro")
  private Timestamp registro;
  @Column (name="precio")
  private Double precio;
  @Column (name="iva")
  private Double iva;
  @Column (name="impuestos")
  private Double impuestos;
  @Column (name="sub_total")
  private Double subTotal;
  @Column (name="cantidad")
  private Double cantidad;
  @Column (name="id_articulo")
  private Long idArticulo;
  @Column (name="unitario_sin_iva")
  private Double unitarioSinIva;

  public TcManticFicticiasDetallesDto() {
    this(new Long(-1L));
  }

  public TcManticFicticiasDetallesDto(Long key) {
    this(null, null, null, null, null, null, null, null, null, null, null, new Long(-1L), null, null, null, null, null, null, 0D);
    setKey(key);
  }

  public TcManticFicticiasDetallesDto(Double descuentos, String codigo, String unidadMedida, Double costo, Long idFicticia, String descuento, String sat, String extras, Double utilidad, String nombre, Double importe, Long idFicticiaDetalle, Double precio, Double iva, Double impuestos, Double subTotal, Double cantidad, Long idArticulo, Double unitarioSinIva) {
    setDescuentos(descuentos);
    setCodigo(codigo);
    setUnidadMedida(unidadMedida);
    setCosto(costo);
    setIdFicticia(idFicticia);
    setDescuento(descuento);
    setSat(sat);
    setExtras(extras);
    setUtilidad(utilidad);
    setNombre(nombre);
    setImporte(importe);
    setIdFicticiaDetalle(idFicticiaDetalle);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
    setPrecio(precio);
    setIva(iva);
    setImpuestos(impuestos);
    setSubTotal(subTotal);
    setCantidad(cantidad);
    setIdArticulo(idArticulo);
		this.unitarioSinIva= unitarioSinIva;
  }
	
  public void setDescuentos(Double descuentos) {
    this.descuentos = descuentos;
  }

  public Double getDescuentos() {
    return descuentos;
  }

  public void setCodigo(String codigo) {
    this.codigo = codigo;
  }

  public String getCodigo() {
    return codigo;
  }

  public void setUnidadMedida(String unidadMedida) {
    this.unidadMedida = unidadMedida;
  }

  public String getUnidadMedida() {
    return unidadMedida;
  }

  public void setCosto(Double costo) {
    this.costo = costo;
  }

  public Double getCosto() {
    return costo;
  }

  public void setIdFicticia(Long idFicticia) {
    this.idFicticia = idFicticia;
  }

  public Long getIdFicticia() {
    return idFicticia;
  }

  public void setIdVenta(Long idFicticia) {
    this.idFicticia = idFicticia;
  }

  public Long getIdVenta() {
    return idFicticia;
  }

  public void setDescuento(String descuento) {
    this.descuento = descuento;
  }

  public String getDescuento() {
    return descuento;
  }

  public void setSat(String sat) {
    this.sat = sat;
  }

  public String getSat() {
    return sat;
  }

  public void setExtras(String extras) {
    this.extras = extras;
  }

  public String getExtras() {
    return extras;
  }

  public void setUtilidad(Double utilidad) {
    this.utilidad = utilidad;
  }

  public Double getUtilidad() {
    return utilidad;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getNombre() {
    return nombre;
  }

  public void setImporte(Double importe) {
    this.importe = importe;
  }

  public Double getImporte() {
    return importe;
  }

  public void setIdFicticiaDetalle(Long idFicticiaDetalle) {
    this.idFicticiaDetalle = idFicticiaDetalle;
  }

  public Long getIdFicticiaDetalle() {
    return idFicticiaDetalle;
  }

  public void setIdVentaDetalle(Long idFicticiaDetalle) {
    this.idFicticiaDetalle = idFicticiaDetalle;
  }

  public Long getIdVentaDetalle() {
    return idFicticiaDetalle;
  }

  public void setRegistro(Timestamp registro) {
    this.registro = registro;
  }

  public Timestamp getRegistro() {
    return registro;
  }

  public void setPrecio(Double precio) {
    this.precio = precio;
  }

  public Double getPrecio() {
    return precio;
  }

  public void setIva(Double iva) {
    this.iva = iva;
  }

  public Double getIva() {
    return iva;
  }

  public void setImpuestos(Double impuestos) {
    this.impuestos = impuestos;
  }

  public Double getImpuestos() {
    return impuestos;
  }

  public void setSubTotal(Double subTotal) {
    this.subTotal = subTotal;
  }

  public Double getSubTotal() {
    return subTotal;
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

	public Double getUnitarioSinIva() {
		return unitarioSinIva;
	}

	public void setUnitarioSinIva(Double unitarioSinIva) {
		this.unitarioSinIva=unitarioSinIva;
	}

  @Transient
  @Override
  public Long getKey() {
  	return getIdFicticiaDetalle();
  }

  @Override
  public void setKey(Long key) {
  	this.idFicticiaDetalle = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getDescuentos());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCodigo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getUnidadMedida());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCosto());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdFicticia());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getDescuento());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getSat());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getExtras());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getUtilidad());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getNombre());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getImporte());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdFicticiaDetalle());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getPrecio());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIva());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getImpuestos());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getSubTotal());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCantidad());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdArticulo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getUnitarioSinIva());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("descuentos", getDescuentos());
		regresar.put("codigo", getCodigo());
		regresar.put("unidadMedida", getUnidadMedida());
		regresar.put("costo", getCosto());
		regresar.put("idFicticia", getIdFicticia());
		regresar.put("descuento", getDescuento());
		regresar.put("sat", getSat());
		regresar.put("extras", getExtras());
		regresar.put("utilidad", getUtilidad());
		regresar.put("nombre", getNombre());
		regresar.put("importe", getImporte());
		regresar.put("idFicticiaDetalle", getIdFicticiaDetalle());
		regresar.put("registro", getRegistro());
		regresar.put("precio", getPrecio());
		regresar.put("iva", getIva());
		regresar.put("impuestos", getImpuestos());
		regresar.put("subTotal", getSubTotal());
		regresar.put("cantidad", getCantidad());
		regresar.put("idArticulo", getIdArticulo());
		regresar.put("unitarioSinIva", getUnitarioSinIva());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getDescuentos(), getCodigo(), getUnidadMedida(), getCosto(), getIdFicticia(), getDescuento(), getSat(), getExtras(), getUtilidad(), getNombre(), getImporte(), getIdFicticiaDetalle(), getRegistro(), getPrecio(), getIva(), getImpuestos(), getSubTotal(), getCantidad(), getIdArticulo(), getUnitarioSinIva()
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
    regresar.append("idFicticiaDetalle~");
    regresar.append(getIdFicticiaDetalle());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdFicticiaDetalle());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticFicticiasDetallesDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdFicticiaDetalle()!= null && getIdFicticiaDetalle()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticFicticiasDetallesDto other = (TcManticFicticiasDetallesDto) obj;
    if (getIdFicticiaDetalle() != other.idFicticiaDetalle && (getIdFicticiaDetalle() == null || !getIdFicticiaDetalle().equals(other.idFicticiaDetalle))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdFicticiaDetalle() != null ? getIdFicticiaDetalle().hashCode() : 0);
    return hash;
  }

}


