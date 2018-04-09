package mx.org.kaana.kajool.db.comun.metadata;

import com.linuxense.javadbf.DBFField;

/**
 * Clase que extiende la funcionalidad de DBFField, y que
 * sobrescribe los metodos getName y SetName para que pueda
 * aecptar mas de 10 caracteres como nombre de campo
 */
public class Field extends DBFField {

  int nameNullIndex = 0;
  String fieldName = "";

  public Field() {
    super();
  }

  public void setName(String value) {
    if (value == null)
      throw new IllegalArgumentException("El campo no puede ser nulo");
    fieldName=value;
  }

  public String getName() {
    return fieldName;
  }

  public boolean equals(Object o) {
    boolean result = false;
    if ((o != null) && (o instanceof DBFField)) {
      Field d = (Field)o;
      if ((fieldName == d.fieldName) && (super.getDataType() == d.getDataType()) &&
          (getFieldLength() == d.getFieldLength()) && (getDecimalCount() == d.getDecimalCount())) {
        result=true;
      }
    }
    return result;
  }

}
