package mx.org.kaana.mantic.archivos.beans;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Random;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.libs.formato.Global;
import mx.org.kaana.mantic.db.dto.TcManticDocumentosDetallesDto;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 20/06/2022
 *@time 03:18:45 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Concepto extends TcManticDocumentosDetallesDto implements Serializable {

  private static final long serialVersionUID = 6070990482487756522L;

  private Long id;

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
    this.setFolio(folio);
    this.setTipo(tipo);
    this.setClaveProducto(claveProducto);
    this.setNoIdentificacion(noIdentificacion);
    this.setDescripcion(descripcion);
    this.setClaveUnidad(claveUnidad);
    this.setUnidad(unidad);
    this.setPrecioUnitario(precioUnitario);
    this.setCantidad(cantidad);
    this.setTasaImpuesto(tasaImpuesto);
    this.setSubtotal(subtotal);
    this.setDescuento(descuento);
    this.setIva(iva);
    this.setTotal(total);
    this.setArchivo(archivo);
    this.setFecha(fecha);
    this.setProveedor(proveedor);
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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
    return "Concepto{" + "id=" + id + ", "+ super.toString()+ '}';
  }

  @Override
  public Class toHbmClass() {
    return TcManticDocumentosDetallesDto.class;
  }
  
}
