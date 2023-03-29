package mx.org.kaana.kalan.catalogos.pacientes.citas.beans;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.libs.formato.Fecha;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 28/03/2023
 *@time 10:04:39 PM 
 *@company KAANA
 *@project KAJOOL (Control system polls)
 */

public class Citado implements Serializable {

  private static final long serialVersionUID = -1995134888079417723L;
  
  private Long idKey;
  private String nombre;
  private Timestamp fecha;
  private List<Entity> servicios;

  public Citado() {
    this(1L, "Alejandro Jimenez Garcia", new Timestamp(Calendar.getInstance().getTimeInMillis()), Collections.EMPTY_LIST);
  }

  public Citado(Long idKey, String nombre, Timestamp fecha, List<Entity> servicios) {
    this.idKey = idKey;
    this.nombre = nombre;
    this.fecha = fecha;
    this.servicios = servicios;
  }

  public Long getIdKey() {
    return idKey;
  }

  public String getNombre() {
    return nombre;
  }

  public Timestamp getFecha() {
    return fecha;
  }

  public List<Entity> getServicios() {
    return servicios;
  }

  public String toFecha() {
    return Fecha.formatear(Fecha.FECHA_CORTA, this.fecha);
  }
  
  public String toHora() {
    return Fecha.formatear(Fecha.HORA_CORTA, this.fecha);
  }
  
  public String toDia() {
    return Fecha.formatear(Fecha.DIA_FECHA_HORA_CORTA, this.fecha);
  }
  
  @Override
  public int hashCode() {
    int hash = 5;
    hash = 23 * hash + Objects.hashCode(this.idKey);
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
    final Citado other = (Citado) obj;
    if (!Objects.equals(this.idKey, other.idKey)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "Agenda{" + "idKey=" + idKey + ", nombre=" + nombre + ", fecha=" + fecha + ", servicios=" + servicios + '}';
  }
 
}
