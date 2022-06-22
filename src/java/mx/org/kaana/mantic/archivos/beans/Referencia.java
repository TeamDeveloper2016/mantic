package mx.org.kaana.mantic.archivos.beans;

import java.io.Serializable;
import java.util.Objects;
import java.util.Random;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 20/06/2022
 *@time 03:13:46 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Referencia implements Serializable {

  private static final long serialVersionUID = 3027054165774572203L;
  
  private Long id;
  private String folio;
  private String rfc;
  private String emisor;
  private String receptor;
  private String uuid;
  private String fecha;
  private String tipo;
  private String total;
  private String archivo;

  public Referencia() {
    this(new Random().nextLong());
  }

  public Referencia(Long id) {
    this(id, "", "", "", "", "", "", "", "", "");
  }
  
  public Referencia(String folio, String rfc, String emisor, String uuid, String fecha, String tipo, String total, String receptor, String archivo) {
    this(new Random().nextLong(), folio, rfc, emisor, uuid, fecha, tipo, total, receptor, archivo);
  }
  
  public Referencia(Long id, String folio, String rfc, String emisor, String uuid, String fecha, String tipo, String total, String receptor, String archivo) {
    this.id = id;
    this.folio = folio;
    this.rfc = rfc;
    this.emisor = emisor;
    this.uuid = uuid;
    this.fecha = fecha;
    this.tipo = tipo;
    this.total = total;
    this.receptor = receptor;
    this.archivo = archivo;
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

  public String getRfc() {
    return rfc;
  }

  public void setRfc(String rfc) {
    this.rfc = rfc;
  }

  public String getEmisor() {
    return emisor;
  }

  public void setEmisor(String emisor) {
    this.emisor = emisor;
  }

  public String getReceptor() {
    return receptor;
  }

  public void setReceptor(String receptor) {
    this.receptor = receptor;
  }

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public String getFecha() {
    return fecha;
  }

  public void setFecha(String fecha) {
    this.fecha = fecha;
  }

  public String getTipo() {
    return tipo;
  }

  public void setTipo(String tipo) {
    this.tipo = tipo;
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

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 61 * hash + Objects.hashCode(this.id);
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
    final Referencia other = (Referencia) obj;
    if (!Objects.equals(this.id, other.id)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "Referencia{" + "id=" + id + ", folio=" + folio + ", rfc=" + rfc + ", emisor=" + emisor + ", receptor=" + receptor + ", uuid=" + uuid + ", fecha=" + fecha + ", tipo=" + tipo + ", total=" + total + ", archivo=" + archivo + '}';
  }

}
