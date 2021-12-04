package mx.org.kaana.mantic.catalogos.articulos.beans;

import java.io.Serializable;
import java.util.Objects;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 4/12/2021
 *@time 12:50:41 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Imagen implements Serializable {

  private static final long serialVersionUID = 1003409713313330507L;

  private Long idImage;
  private Long idProducto;
  private String articulos;
  private String original;

  public Imagen(Long idImage, Long idProducto, String articulos, String original) {
    this.idImage = idImage;
    this.idProducto = idProducto;
    this.articulos = articulos;
    this.original = original;
  }

  public Long getIdImage() {
    return idImage;
  }

  public void setIdImage(Long idImage) {
    this.idImage = idImage;
  }

  public Long getIdProducto() {
    return idProducto;
  }

  public void setIdProducto(Long idProducto) {
    this.idProducto = idProducto;
  }

  public String getArticulos() {
    return articulos;
  }

  public void setArticulos(String articulos) {
    this.articulos = articulos;
  }

  public String getOriginal() {
    return original;
  }

  public void setOriginal(String original) {
    this.original = original;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 79 * hash + Objects.hashCode(this.idImage);
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
    if (!Objects.equals(this.idImage, other.idImage)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "Imagen{" + "idImage=" + idImage + ", idProducto=" + idProducto + ", articulos=" + articulos + ", original=" + original + '}';
  }
  
}
