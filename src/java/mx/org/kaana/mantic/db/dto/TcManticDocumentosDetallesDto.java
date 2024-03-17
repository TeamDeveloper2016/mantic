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
@Table(name="tc_mantic_documentos_detalles")
public class TcManticDocumentosDetallesDto implements IBaseDto, Serializable {
		
  private static final long serialVersionUID=1L;
  @Column (name="id_documento")
  private Long idDocumento;
  @Column (name="descripcion")
  private String descripcion;
  @Column (name="tipo")
  private String tipo;
  @Column (name="no_identificacion")
  private String noIdentificacion;
  @Column (name="archivo")
  private String archivo;
  @Column (name="descuento")
  private String descuento;
  @Column (name="clave_producto")
  private String claveProducto;
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column (name="id_documento_detalle")
  private Long idDocumentoDetalle;
  @Column (name="registro")
  private Timestamp registro;
  @Column (name="unidad")
  private String unidad;
  @Column (name="precio_unitario")
  private String precioUnitario;
  @Column (name="fecha")
  private String fecha;
  @Column (name="total")
  private String total;
  @Column (name="tasa_impuesto")
  private String tasaImpuesto;
  @Column (name="clave_unidad")
  private String claveUnidad;
  @Column (name="iva")
  private String iva;
  @Column (name="subtotal")
  private String subtotal;
  @Column (name="folio")
  private String folio;
  @Column (name="proveedor")
  private String proveedor;
  @Column (name="cantidad")
  private String cantidad;

  public TcManticDocumentosDetallesDto() {
    this(new Long(-1L));
  }

  public TcManticDocumentosDetallesDto(Long key) {
    this(null, null, null, null, null, null, null, new Long(-1L), null, null, null, null, null, null, null, null, null, null, null);
    setKey(key);
  }

  public TcManticDocumentosDetallesDto(Long idDocumento, String descripcion, String tipo, String noIdentificacion, String archivo, String descuento, String claveProducto, Long idDocumentoDetalle, String unidad, String precioUnitario, String fecha, String total, String tasaImpuesto, String claveUnidad, String iva, String subtotal, String folio, String proveedor, String cantidad) {
    setIdDocumento(idDocumento);
    setDescripcion(descripcion);
    setTipo(tipo);
    setNoIdentificacion(noIdentificacion);
    setArchivo(archivo);
    setDescuento(descuento);
    setClaveProducto(claveProducto);
    setIdDocumentoDetalle(idDocumentoDetalle);
    setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
    setUnidad(unidad);
    setPrecioUnitario(precioUnitario);
    setFecha(fecha);
    setTotal(total);
    setTasaImpuesto(tasaImpuesto);
    setClaveUnidad(claveUnidad);
    setIva(iva);
    setSubtotal(subtotal);
    setFolio(folio);
    setProveedor(proveedor);
    setCantidad(cantidad);
  }
	
  public void setIdDocumento(Long idDocumento) {
    this.idDocumento = idDocumento;
  }

  public Long getIdDocumento() {
    return idDocumento;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setTipo(String tipo) {
    this.tipo = tipo;
  }

  public String getTipo() {
    return tipo;
  }

  public void setNoIdentificacion(String noIdentificacion) {
    this.noIdentificacion = noIdentificacion;
  }

  public String getNoIdentificacion() {
    return noIdentificacion;
  }

  public void setArchivo(String archivo) {
    this.archivo = archivo;
  }

  public String getArchivo() {
    return archivo;
  }

  public void setDescuento(String descuento) {
    this.descuento = descuento;
  }

  public String getDescuento() {
    return descuento;
  }

  public void setClaveProducto(String claveProducto) {
    this.claveProducto = claveProducto;
  }

  public String getClaveProducto() {
    return claveProducto;
  }

  public void setIdDocumentoDetalle(Long idDocumentoDetalle) {
    this.idDocumentoDetalle = idDocumentoDetalle;
  }

  public Long getIdDocumentoDetalle() {
    return idDocumentoDetalle;
  }

  public void setRegistro(Timestamp registro) {
    this.registro = registro;
  }

  public Timestamp getRegistro() {
    return registro;
  }

  public void setUnidad(String unidad) {
    this.unidad = unidad;
  }

  public String getUnidad() {
    return unidad;
  }

  public void setPrecioUnitario(String precioUnitario) {
    this.precioUnitario = precioUnitario;
  }

  public String getPrecioUnitario() {
    return precioUnitario;
  }

  public void setFecha(String fecha) {
    this.fecha = fecha;
  }

  public String getFecha() {
    return fecha;
  }

  public void setTotal(String total) {
    this.total = total;
  }

  public String getTotal() {
    return total;
  }

  public void setTasaImpuesto(String tasaImpuesto) {
    this.tasaImpuesto = tasaImpuesto;
  }

  public String getTasaImpuesto() {
    return tasaImpuesto;
  }

  public void setClaveUnidad(String claveUnidad) {
    this.claveUnidad = claveUnidad;
  }

  public String getClaveUnidad() {
    return claveUnidad;
  }

  public void setIva(String iva) {
    this.iva = iva;
  }

  public String getIva() {
    return iva;
  }

  public void setSubtotal(String subtotal) {
    this.subtotal = subtotal;
  }

  public String getSubtotal() {
    return subtotal;
  }

  public void setFolio(String folio) {
    this.folio = folio;
  }

  public String getFolio() {
    return folio;
  }

  public void setProveedor(String proveedor) {
    this.proveedor = proveedor;
  }

  public String getProveedor() {
    return proveedor;
  }

  public void setCantidad(String cantidad) {
    this.cantidad = cantidad;
  }

  public String getCantidad() {
    return cantidad;
  }

  @Transient
  @Override
  public Long getKey() {
  	return getIdDocumentoDetalle();
  }

  @Override
  public void setKey(Long key) {
  	this.idDocumentoDetalle = key;
  }

  @Override
  public String toString() {
    StringBuilder regresar= new StringBuilder();
    regresar.append("[");
		regresar.append(getIdDocumento());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getDescripcion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getTipo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getNoIdentificacion());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getArchivo());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getDescuento());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getClaveProducto());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdDocumentoDetalle());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getRegistro());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getUnidad());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getPrecioUnitario());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getFecha());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getTotal());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getTasaImpuesto());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getClaveUnidad());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIva());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getSubtotal());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getFolio());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getProveedor());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getCantidad());
    regresar.append("]");
  	return regresar.toString();
  }

  @Override
  public Map toMap() {
    Map regresar = new HashMap();
		regresar.put("idDocumento", getIdDocumento());
		regresar.put("descripcion", getDescripcion());
		regresar.put("tipo", getTipo());
		regresar.put("noIdentificacion", getNoIdentificacion());
		regresar.put("archivo", getArchivo());
		regresar.put("descuento", getDescuento());
		regresar.put("claveProducto", getClaveProducto());
		regresar.put("idDocumentoDetalle", getIdDocumentoDetalle());
		regresar.put("registro", getRegistro());
		regresar.put("unidad", getUnidad());
		regresar.put("precioUnitario", getPrecioUnitario());
		regresar.put("fecha", getFecha());
		regresar.put("total", getTotal());
		regresar.put("tasaImpuesto", getTasaImpuesto());
		regresar.put("claveUnidad", getClaveUnidad());
		regresar.put("iva", getIva());
		regresar.put("subtotal", getSubtotal());
		regresar.put("folio", getFolio());
		regresar.put("proveedor", getProveedor());
		regresar.put("cantidad", getCantidad());
  	return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{
    getIdDocumento(), getDescripcion(), getTipo(), getNoIdentificacion(), getArchivo(), getDescuento(), getClaveProducto(), getIdDocumentoDetalle(), getRegistro(), getUnidad(), getPrecioUnitario(), getFecha(), getTotal(), getTasaImpuesto(), getClaveUnidad(), getIva(), getSubtotal(), getFolio(), getProveedor(), getCantidad()
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
    regresar.append("idDocumentoDetalle~");
    regresar.append(getIdDocumentoDetalle());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar= new StringBuilder();
    regresar.append(getIdDocumentoDetalle());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return TcManticDocumentosDetallesDto.class;
  }

  @Override
  public boolean isValid() {
  	return getIdDocumentoDetalle()!= null && getIdDocumentoDetalle()!=-1L;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TcManticDocumentosDetallesDto other = (TcManticDocumentosDetallesDto) obj;
    if (getIdDocumentoDetalle() != other.idDocumentoDetalle && (getIdDocumentoDetalle() == null || !getIdDocumentoDetalle().equals(other.idDocumentoDetalle))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdDocumentoDetalle() != null ? getIdDocumentoDetalle().hashCode() : 0);
    return hash;
  }

}


