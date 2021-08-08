package mx.org.kaana.mantic.ventas.beans;

import java.util.Objects;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 7/08/2021
 *@time 11:09:33 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public class Imagen {

  private Long id;
  private String name;
  private String alias;

  public Imagen() {
  }

  public Imagen(Long id, String name, String alias) {
    this.id = id;
    this.name = name;
    this.alias = alias;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAlias() {
    return alias;
  }

  public void setAlias(String alias) {
    this.alias = alias;
  }

  @Override
  public int hashCode() {
    int hash = 5;
    hash = 41 * hash + Objects.hashCode(this.id);
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
    final Imagen other = (Imagen) obj;
    if (!Objects.equals(this.id, other.id)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "Imagen{" + "id=" + id + ", name=" + name + ", alias=" + alias + '}';
  }
  
}
