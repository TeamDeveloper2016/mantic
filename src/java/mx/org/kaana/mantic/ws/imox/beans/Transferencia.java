package mx.org.kaana.mantic.ws.imox.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 16/01/2024
 * @time 12:06:20 AM 
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Transferencia extends Conteo implements Serializable {

  private static final long serialVersionUID = -8330660760447754833L;

  private Long idFuente;

  public Transferencia() {
    super();
  }

  public Transferencia(Long idConteo, Long idUsuario, String nombre, String registro, Long idEmpresa, Long idAlmacen, String semilla, String version, Long idFuente) {
    super(idConteo, idUsuario, nombre, registro, idEmpresa, idAlmacen, semilla, version);
    this.setProductos(new ArrayList<>());
    this.setIdFuente(idFuente);
  }

  public Long getIdFuente() {
    return idFuente;
  }

  public void setIdFuente(Long idFuente) {
    this.idFuente = idFuente;
  }
  
  @Override
  public int hashCode() {
    int hash = 5;
    hash = 71 * hash + Objects.hashCode(this.getIdConteo());
    hash = 71 * hash + Objects.hashCode(this.getIdUsuario());
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
    final Transferencia other = (Transferencia) obj;
    if (!Objects.equals(this.getIdConteo(), other.getIdConteo())) {
      return false;
    }
    if (!Objects.equals(this.getIdUsuario(), other.getIdUsuario())) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "Transferencia{" + "idFuente=" + idFuente + ','+ super.toString()+ '}';
  }
  
}
