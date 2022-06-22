package mx.org.kaana.mantic.archivos.beans;

import java.io.Serializable;
import java.util.Objects;
import java.util.Random;
import mx.org.kaana.mantic.catalogos.articulos.beans.Importado;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 20/06/2022
 *@time 02:20:06 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Documento implements Serializable {

  private static final long serialVersionUID = -4217515929009677092L;
  
  private Long id;
  private String original;
  private String name;
  private String path;
  private Importado xml; 

  public Documento() {
    this(new Random().nextLong(), "HOLIX", "", "", new Importado());
  }

  public Documento(String original) {
    this(new Random().nextLong(), original, "", "", new Importado());
  }

  public Documento(String original, String name, String path, Importado xml) {
    this(new Random().nextLong(), original, name, path, xml);
  }
  
  public Documento(Long id, String original, String name, String path, Importado xml) {
    this.id = id;
    this.original = original;
    this.name = name;
    this.path = path;
    this.xml = xml;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getOriginal() {
    return original;
  }

  public void setOriginal(String original) {
    this.original = original;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public Importado getXml() {
    return xml;
  }

  public void setXml(Importado xml) {
    this.xml = xml;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 53 * hash + Objects.hashCode(this.original);
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
    final Documento other = (Documento) obj;
    if (!Objects.equals(this.original, other.original)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "Documento{" + "id=" + id + ", original=" + original + ", name=" + name + ", path=" + path + '}';
  }
  
}
