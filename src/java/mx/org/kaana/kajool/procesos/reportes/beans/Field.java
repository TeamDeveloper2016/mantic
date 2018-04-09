package mx.org.kaana.kajool.procesos.reportes.beans;

import java.util.Objects;
import mx.org.kaana.kajool.procesos.reportes.enums.ETypeField;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 30/11/2016
 *@time 12:57:27 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public class Field {

  private String name;
  private ETypeField type;
  private int length;
  private int scale;

  public Field(String name) {
    this(name, ETypeField.VARCHAR, 1, 0);
  }

  public Field(String name, ETypeField type, int length, int scale) {
    this.name = name;
    this.type = type;
    this.length = length;
    this.scale = scale;
  }

  public String getName() {
    return name;
  }

  public ETypeField getType() {
    return type;
  }

  public int getLength() {
    return length;
  }

  public int getScale() {
    return scale;
  }

  @Override
  public int hashCode() {
    int hash = 5;
    hash = 59 * hash + Objects.hashCode(this.name);
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Field other = (Field) obj;
    if (!Objects.equals(this.name, other.name)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "Field{" + "name=" + name + ", type=" + type + ", length=" + length + ", scale=" + scale + '}';
  }
  
}
