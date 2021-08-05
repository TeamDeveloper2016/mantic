package mx.org.kaana.kajool.procesos.acceso.beans;

import java.io.Serializable;
import java.util.Objects;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 30/07/2021
 *@time 08:23:40 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public class Producto implements Serializable {

  private static final long serialVersionUID = 5375962589862983620L;

  private Long id;
  private String acronimo;
  private String nombre;
  private String descripcion;
  private String imagen;
  private String url;
  private String datos;

  public Producto(Long id) {
    this(id, null, null, null, null, null, null);
  }

  public Producto(Long id, String acronimo, String nombre, String descripcion, String imagen, String url, String datos) {
    this.id = id;
    this.acronimo = acronimo;
    this.nombre = nombre;
    this.descripcion = descripcion;
    this.imagen = imagen;
    this.url = url;
    this.datos = datos;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getAcronimo() {
    return acronimo;
  }

  public void setAcronimo(String acronimo) {
    this.acronimo = acronimo;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public String getImagen() {
    return imagen;
  }

  public void setImagen(String imagen) {
    this.imagen = imagen;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getDatos() {
    return datos;
  }

  public void setDatos(String datos) {
    this.datos = datos;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 71 * hash + Objects.hashCode(this.id);
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
    final Producto other = (Producto) obj;
    return true;
  }

  @Override
  public String toString() {
    return "Producto{" + "id=" + id + ", acronimo=" + acronimo + ", nombre=" + nombre + ", descripcion=" + descripcion + ", imagen=" + imagen + ", url=" + url + ", datos=" + datos + '}';
  }
  
}
