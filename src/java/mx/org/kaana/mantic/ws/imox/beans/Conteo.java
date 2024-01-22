package mx.org.kaana.mantic.ws.imox.beans;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 16/01/2024
 * @time 12:06:20 AM 
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Conteo implements Serializable {

  private static final long serialVersionUID = -8330660760447754833L;

  private Long idConteo;
  private Long idUsuario;
  private Date fecha;
  private String nombre;
  private List<Cantidad> productos;
  private Timestamp registro;

  public Conteo() {
  }

  public Conteo(Long idConteo, Long idUsuario, Date fecha, String nombre) {
    this.idConteo = idConteo;
    this.idUsuario = idUsuario;
    this.fecha = fecha;
    this.nombre = nombre;
    this.productos = new ArrayList<>();
    this.registro = new Timestamp(Calendar.getInstance().getTimeInMillis());
  }

  public Long getIdConteo() {
    return idConteo;
  }

  public void setIdConteo(Long idConteo) {
    this.idConteo = idConteo;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Date getFecha() {
    return fecha;
  }

  public void setFecha(Date fecha) {
    this.fecha = fecha;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public List<Cantidad> getProductos() {
    return productos;
  }

  public void setProductos(List<Cantidad> productos) {
    this.productos = productos;
  }

  public Timestamp getRegistro() {
    return registro;
  }

  public void setRegistro(Timestamp registro) {
    this.registro = registro;
  }

  @Override
  public int hashCode() {
    int hash = 5;
    hash = 71 * hash + Objects.hashCode(this.idConteo);
    hash = 71 * hash + Objects.hashCode(this.idUsuario);
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
    final Conteo other = (Conteo) obj;
    if (!Objects.equals(this.idConteo, other.idConteo)) {
      return false;
    }
    if (!Objects.equals(this.idUsuario, other.idUsuario)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "Conteo{" + "idConteo=" + idConteo + ", idUsuario=" + idUsuario + ", fecha=" + fecha + ", nombre=" + nombre + ", productos=" + productos + ", registro=" + registro + '}';
  }
  
}
