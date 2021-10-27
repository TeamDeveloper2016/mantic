package mx.org.kaana.mantic.egresos.beans;

import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ESql;
import mx.org.kaana.libs.formato.Global;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.mantic.db.dto.TcManticEgresosNotasDto;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 26/10/2021
 *@time 05:40:14 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Nota extends TcManticEgresosNotasDto implements IEgresos, Serializable {

  private static final long serialVersionUID = -8800989266355341818L;

  private Long idPivote;
  private String consecutivo;
  private String factura;
  private Date fechaFactura;
  private Double total;
  private String proveedor;
  private ESql accion;

  public Nota() {
    this(new Long((int)(Math.random()* -10000)));
  }

  public Nota(Long key) {
    super(key);
    this.idPivote= key;
    this.accion  = ESql.INSERT;
  }

  public Nota(Long idNotaEntrada, Long idEgreso, String consecutivo, String factura, Date fechaFactura, Double total, String proveedor) {
    super(idEgreso, JsfBase.getIdUsuario(), -1L, null, idNotaEntrada);
    this.idPivote= idNotaEntrada;
    this.consecutivo= consecutivo;
    this.factura= factura;
    this.fechaFactura= fechaFactura;
    this.total= total;
    this.proveedor= proveedor;
    this.accion= ESql.INSERT;
  }

  public Long getIdPivote() {
    return idPivote;
  }

  public void setIdPivote(Long idPivote) {
    this.idPivote = idPivote;
  }

  public String getConsecutivo() {
    return consecutivo;
  }

  public void setConsecutivo(String consecutivo) {
    this.consecutivo = consecutivo;
  }

  public String getFactura() {
    return factura;
  }

  public void setFactura(String factura) {
    this.factura = factura;
  }

  public Date getFechaFactura() {
    return fechaFactura;
  }

  public void setFechaFactura(Date fechaFactura) {
    this.fechaFactura = fechaFactura;
  }

  public Double getTotal() {
    return total;
  }

  public void setTotal(Double total) {
    this.total = total;
  }

  public String getProveedor() {
    return proveedor;
  }

  public void setProveedor(String proveedor) {
    this.proveedor = proveedor;
  }

  public String getDia() {
    return Global.format(EFormatoDinamicos.FECHA_CORTA, this.fechaFactura);
  }

  public String getCantidad() {
    return Global.format(EFormatoDinamicos.MILES_CON_DECIMALES, this.total);
  }
  
  public ESql getAccion() {
    return accion;
  }

  public void setAccion(ESql accion) {
    this.accion = accion;
  }

  @Override
  public int hashCode() {
    int hash = 3;
    hash = 41 * hash + Objects.hashCode(this.idPivote);
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
    final Nota other = (Nota) obj;
    if (!Objects.equals(this.idPivote, other.idPivote)) {
      return false;
    }
    return true;
  }

  @Override
  public Class toHbmClass() {
    return TcManticEgresosNotasDto.class;
  }
  
}
