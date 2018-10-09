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
  @Column (name="importes")
  private Double importes;
  @Column (name="descuentos")
  private Double descuentos;
  @Column (name="excedentes")
  private Double excedentes;
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
  @Column (name="nombre")
  private String nombre;
  @Column (name="importe")
  private Double importe;
  @Column (name="precios")
  private Double precios;
  @Column (name="registro")
  private Timestamp registro;
  @Column (name="propio")
  private String propio;
  @Column (name="cantidades")
  private Double cantidades;
  @Column (name="iva")
  private Double iva;
  @Column (name="impuestos")
  private Double impuestos;
  @Column (name="sub_total")
  private Double subTotal;
  @Column (name="cantidad")
  private Double cantidad;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_orden_detalle")
  private Long idOrdenDetalle;
  @Column (name="id_articulo")
  private Long idArticulo;
  @Column (name="costo_real")
  private Double costoReal;
  @Column (name="costo_calculado")
  private Double costoCalculado;

  public TcManticOrdenesDetallesDto() {
    this(new Long(-1L));
  }

  public TcManticOrdenesDetallesDto(Long key) {
    this(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, new Long(-1L), null, null, 0D, 0D);
    setKey(key);
  }

  public TcManticOrdenesDetallesDto(Double importes, Double descuentos, String codigo, Double costo, String descuento, Long idOrdenCompra, String extras, String nombre, Double importe, Double precios, String propio, Double cantidades, Double iva, Double impuestos, Double subTotal, Double cantidad, Long idOrdenDetalle, Long idArticulo, Double excedentes, Double costoReal, Double costoCalculado) {
    setImportes(importes);
    setDescuentos(descuentos);
    setExcedentes(excedentes);
    setCodigo(codigo);
    setCosto(costo);
    setDescuento(descuento);
    setIdOrdenCompra(idOrdenCompra);
    setExtras(extras);
    setNombre(nombre);
    setImporte(importe);
    setPrecios(precios);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
    setPropio(propio);
    setCantidades(cantidades);
    setIva(iva);
    setImpuestos(impuestos);
    setSubTotal(subTotal);
    setCantidad(cantidad);
    setIdOrdenDetalle(idOrdenDetalle);
    setIdArticulo(idArticulo);
		this.costoReal= costoReal;
		this.costoCalculado= costoCalculado;
  }
	
  public void setImportes(Double importes) {
    this.importes = importes;
  }

  public Double getImportes() {
    return importes;
  }

  public void setDescuentos(Double descuentos) {
    this.descuentos = descuentos;
  }

  public Double getDescuentos() {
    return descuentos;
  }

	public Double getExcedentes() {
		return excedentes;
	}

	public void setExcedentes(Double excedentes) {
		this.excedentes=excedentes;
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

  public void setPrecios(Double precios) {
    this.precios = precios;
  }

  public Double getPrecios() {
    return precios;
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

  public void setCantidades(Double cantidades) {
    this.cantidades = cantidades;
  }

  public Double getCantidades() {
    return cantidades;
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

	public Double getCostoReal() {
		return costoReal;
	}

	public void setCostoReal(Double costoReal) {
		this.costoReal=costoReal;
	}

	public Double getCostoCalculado() {
		return costoCalculado;
	}

	public void setCostoCalculado(Double costoCalculado) {
		this.costoCalculado=costoCalculado;
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
		regresar.append(getImportes());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getDescuentos());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCodigo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCosto());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getDescuento());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getExcedentes());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdOrdenCompra());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getExtras());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getNombre());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getImporte());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getPrecios());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getPropio());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCantidades());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIva());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getImpuestos());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getSubTotal());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCantidad());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdOrdenDetalle());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdArticulo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCostoReal());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCostoCalculado());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("importes", getImportes());
		regresar.put("descuentos", getDescuentos());
		regresar.put("excedentes", getExcedentes());
		regresar.put("codigo", getCodigo());
		regresar.put("costo", getCosto());
		regresar.put("descuento", getDescuento());
		regresar.put("idOrdenCompra", getIdOrdenCompra());
		regresar.put("extras", getExtras());
		regresar.put("nombre", getNombre());
		regresar.put("importe", getImporte());
		regresar.put("precios", getPrecios());
		regresar.put("registro", getRegistro());
		regresar.put("propio", getPropio());
		regresar.put("cantidades", getCantidades());
		regresar.put("iva", getIva());
		regresar.put("impuestos", getImpuestos());
		regresar.put("subTotal", getSubTotal());
		regresar.put("cantidad", getCantidad());
		regresar.put("idOrdenDetalle", getIdOrdenDetalle());
		regresar.put("idArticulo", getIdArticulo());
		regresar.put("costoReal", getCostoReal());
		regresar.put("costoCalculado", getCostoCalculado());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getImportes(), getDescuentos(), getCodigo(), getCosto(), getDescuento(), getIdOrdenCompra(), getExtras(), getNombre(), getImporte(), getPrecios(), getRegistro(), getPropio(), getCantidades(), getIva(), getImpuestos(), getSubTotal(), getCantidad(), getIdOrdenDetalle(), getIdArticulo(), getExcedentes(), getCostoReal(), getCostoCalculado()
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


