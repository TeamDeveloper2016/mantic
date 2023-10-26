package mx.org.kaana.mantic.inventarios.almacenes.beans;

import java.io.Serializable;
import java.sql.Timestamp;
import mx.org.kaana.kajool.enums.ESql;
import mx.org.kaana.mantic.db.dto.TcManticArticulosDto;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 23/10/2023
 *@time 03:44:54 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Utilidad extends TcManticArticulosDto implements Serializable {

  private static final long serialVersionUID = -3909033197948172590L;

  private String notaEntrada;
  private String propio;
  private Double porcentajeMenudeo;
  private Double porcentajeMedioMayoreo;
  private Double porcentajeMayoreo;
  private Timestamp fechaNotaEntrada;
  private ESql sql;

  public Utilidad() {
    this(-1L);
  }

  public Utilidad(Long key) {
    super(key);
    this.sql= ESql.SELECT;
  }

  public String getNotaEntrada() {
    return notaEntrada;
  }

  public void setNotaEntrada(String notaEntrada) {
    this.notaEntrada = notaEntrada;
  }

  public String getPropio() {
    return propio;
  }

  public void setPropio(String propio) {
    this.propio = propio;
  }

  public Double getPorcentajeMenudeo() {
    return porcentajeMenudeo;
  }

  public void setPorcentajeMenudeo(Double porcentajeMenudeo) {
    this.porcentajeMenudeo = porcentajeMenudeo;
  }

  public Double getPorcentajeMedioMayoreo() {
    return porcentajeMedioMayoreo;
  }

  public void setPorcentajeMedioMayoreo(Double porcentajeMedioMayoreo) {
    this.porcentajeMedioMayoreo = porcentajeMedioMayoreo;
  }

  public Double getPorcentajeMayoreo() {
    return porcentajeMayoreo;
  }

  public void setPorcentajeMayoreo(Double porcentajeMayoreo) {
    this.porcentajeMayoreo = porcentajeMayoreo;
  }

  public Timestamp getFechaNotaEntrada() {
    return fechaNotaEntrada;
  }

  public void setFechaNotaEntrada(Timestamp fechaNotaEntrada) {
    this.fechaNotaEntrada = fechaNotaEntrada;
  }

  public ESql getSql() {
    return sql;
  }

  public void setSql(ESql sql) {
    this.sql = sql;
  }
  
}
