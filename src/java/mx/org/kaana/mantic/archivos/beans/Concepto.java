package mx.org.kaana.mantic.archivos.beans;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Random;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.libs.formato.Global;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 20/06/2022
 *@time 03:18:45 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Concepto implements Serializable {

  private static final long serialVersionUID = 6070990482487756522L;

  private Long id;
  private String folio;
  private String tipo;
  private String claveProducto;
  private String noIdentificacion;
  private String descripcion;
  private String claveUnidad;
  private String unidad;
  private String precioUnitario;
  private String cantidad;
  private String tasaImpuesto;
  private String descuento;
  private String subtotal;
  private String iva;
  private String total;
  private String archivo;
  private String fecha;
  private String proveedor;

  public Concepto() {
    this(new Random().nextLong());
  }

  public Concepto(Long id) {
    this(id, "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", Global.format(EFormatoDinamicos.FECHA_HORA, LocalDate.now()), "");
  }
  
  public Concepto(String folio, String tipo, String claveProducto, String noIdentificacion, String descripcion, String claveUnidad, String unidad, String precioUnitario, String cantidad, String tasaImpuesto, String subtotal, String descuento, String iva, String total, String archivo, String fecha, String proveedor) {
    this(new Random().nextLong(), folio, tipo, claveProducto, noIdentificacion, descripcion, claveUnidad, unidad, precioUnitario, cantidad, tasaImpuesto, subtotal, descuento, iva, total, archivo, fecha, proveedor);
  }
  
  public Concepto(Long id, String folio, String tipo, String claveProducto, String noIdentificacion, String descripcion, String claveUnidad, String unidad, String precioUnitario, String cantidad, String tasaImpuesto, String subtotal, String descuento, String iva, String total, String archivo, String fecha, String proveedor) {
    this.id = id;
    this.folio = folio;
    this.tipo = tipo;
    this.claveProducto = claveProducto;
    this.noIdentificacion = noIdentificacion;
    this.descripcion = descripcion;
    this.claveUnidad = claveUnidad;
    this.unidad = unidad;
    this.precioUnitario = precioUnitario;
    this.cantidad = cantidad;
    this.tasaImpuesto = tasaImpuesto;
    this.subtotal = subtotal;
    this.descuento = descuento;
    this.iva = iva;
    this.total = total;
    this.archivo = archivo;
    this.fecha = fecha;
    this.proveedor = proveedor;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getFolio() {
    return folio;
  }

  public void setFolio(String folio) {
    this.folio = folio;
  }

  public String getTipo() {
    return tipo;
  }

  public void setTipo(String tipo) {
    this.tipo = tipo;
  }

  public String getClaveProducto() {
    return claveProducto;
  }

  public void setClaveProducto(String claveProducto) {
    this.claveProducto = claveProducto;
  }

  public String getNoIdentificacion() {
    return noIdentificacion;
  }

  public void setNoIdentificacion(String noIdentificacion) {
    this.noIdentificacion = noIdentificacion;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
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

  public String getPrecioUnitario() {
    return precioUnitario;
  }

  public void setPrecioUnitario(String precioUnitario) {
    this.precioUnitario = precioUnitario;
  }

  public String getCantidad() {
    return cantidad;
  }

  public void setCantidad(String cantidad) {
    this.cantidad = cantidad;
  }

  public String getTasaImpuesto() {
    return tasaImpuesto;
  }

  public void setTasaImpuesto(String tasaImpuesto) {
    this.tasaImpuesto = tasaImpuesto;
  }

  public String getSubtotal() {
    return subtotal;
  }

  public void setSubtotal(String subtotal) {
    this.subtotal = subtotal;
  }

  public String getDescuento() {
    return descuento;
  }

  public void setDescuento(String descuento) {
    this.descuento = descuento;
  }

  public String getIva() {
    return iva;
  }

  public void setIva(String iva) {
    this.iva = iva;
  }

  public String getTotal() {
    return total;
  }

  public void setTotal(String total) {
    this.total = total;
  }

  public String getArchivo() {
    return archivo;
  }

  public void setArchivo(String archivo) {
    this.archivo = archivo;
  }

  public String getFecha() {
    return fecha;
  }

  public void setFecha(String fecha) {
    this.fecha = fecha;
  }

  public String getProveedor() {
    return proveedor;
  }

  public void setProveedor(String proveedor) {
    this.proveedor = proveedor;
  }

  @Override
  public int hashCode() {
    int hash = 3;
    hash = 19 * hash + Objects.hashCode(this.id);
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Concepto other = (Concepto) obj;
    if (!Objects.equals(this.id, other.id)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "Concepto{" + "id=" + id + ", folio=" + folio + ", tipo=" + tipo + ", claveProducto=" + claveProducto + ", noIdentificacion=" + noIdentificacion + ", descripcion=" + descripcion + ", claveUnidad=" + claveUnidad + ", unidad=" + unidad + ", precioUnitario=" + precioUnitario + ", cantidad=" + cantidad + ", tasaImpuesto=" + tasaImpuesto + ", descuento=" + descuento + ", subtotal=" + subtotal + ", iva=" + iva + ", total=" + total + ", archivo=" + archivo + '}';
  }

}
