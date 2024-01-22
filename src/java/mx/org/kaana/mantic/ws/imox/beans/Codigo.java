package mx.org.kaana.mantic.ws.imox.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Objects;
import mx.org.kaana.libs.formato.Fecha;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 15/01/2024
 * @time 11:37:08 AM 
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Codigo implements Serializable {

  private static final long serialVersionUID = 7827342035308515585L;

  private Long idCodigo;
  private String numero;
  private Long idPrincipal;
  private String registro;

  public Codigo() {
  }

  public Codigo(Long idCodigo, String numero, Long idPrincipal) {
    this.idCodigo = idCodigo;
    this.numero = numero;
    this.idPrincipal = idPrincipal;
    this.registro = Fecha.toRegistro();
  }

  public Long getIdCodigo() {
    return idCodigo;
  }

  public void setIdCodigo(Long idCodigo) {
    this.idCodigo = idCodigo;
  }

  public String getNumero() {
    return numero;
  }

  public void setNumero(String numero) {
    this.numero = numero;
  }

  public Long getIdPrincipal() {
    return idPrincipal;
  }

  public void setIdPrincipal(Long idPrincipal) {
    this.idPrincipal = idPrincipal;
  }

  public String getRegistro() {
    return registro;
  }

  public void setRegistro(String registro) {
    this.registro = registro;
  }

  @Override
  public int hashCode() {
    int hash = 5;
    hash = 89 * hash + Objects.hashCode(this.idCodigo);
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
    final Codigo other = (Codigo) obj;
    if (!Objects.equals(this.idCodigo, other.idCodigo)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "Codigo{" + "idCodigo=" + idCodigo + ", numero=" + numero + ", idPrincipal=" + idPrincipal + ", registro=" + registro + '}';
  }

}
