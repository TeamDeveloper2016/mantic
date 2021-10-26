package mx.org.kaana.mantic.catalogos.empresas.saldar.beans;

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
 *@date 25/10/2021
 *@time 06:02:16 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Egreso extends TcManticEgresosNotasDto implements Serializable {

  private static final long serialVersionUID = -5856612920598759747L;
  
  private Long idPivote;
  private String consecutivo;
  private String descripcion;
  private Date fecha;
  private Double importe;
  private ESql accion;

  public Egreso() {
    this(new Long((int)(Math.random()* -10000)));
  }

  public Egreso(Long key) {
    super(key);
    this.idPivote= key;
    this.accion  = ESql.INSERT;
  }

  public Egreso(Long idEgreso, Long idNotaEntrada, String consecutivo, String descripcion, Date fecha, Double importe) {
    super(idEgreso, JsfBase.getIdUsuario(), -1L, null, idNotaEntrada);
    this.idPivote   = idEgreso;
    this.consecutivo= consecutivo;
    this.descripcion= descripcion;
    this.fecha      = fecha;
    this.importe    = importe;
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

  public String getDescripcion() {
    return descripcion;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public Date getFecha() {
    return fecha;
  }

  public void setFecha(Date fecha) {
    this.fecha = fecha;
  }

  public Double getImporte() {
    return importe;
  }

  public void setImporte(Double importe) {
    this.importe = importe;
  }

  public String getDia() {
    return Global.format(EFormatoDinamicos.FECHA_CORTA, this.fecha);
  }

  public String getCantidad() {
    return Global.format(EFormatoDinamicos.MILES_CON_DECIMALES, this.importe);
  }

  public ESql getAccion() {
    return accion;
  }

  public void setAccion(ESql accion) {
    this.accion = accion;
  }

  @Override
  public int hashCode() {
    int hash = 5;
    hash = 79 * hash + Objects.hashCode(this.idPivote);
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
    final Egreso other = (Egreso) obj;
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
