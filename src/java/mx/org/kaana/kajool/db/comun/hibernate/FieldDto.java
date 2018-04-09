package mx.org.kaana.kajool.db.comun.hibernate;

import java.lang.reflect.Field;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Map;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;

public final class FieldDto {

  private FieldDto() {
  }

  // este metodo seria reemplazado por el actual
  // Recibe el dto y el mapa de valores actualizar y solo recorre el mapa
  public static String updateFields(IBaseDto dto, Map fields) {
    Object value=null;
    Field field=null;
    Class typeField=null;
    StringBuilder regresar=new StringBuilder();
    for (Object key : fields.keySet()) {
      try {
        if (!key.toString().equals("key")) {
          value=fields.get(key);
          field=dto.getClass().getDeclaredField(key.toString());
          typeField=field.getType();
          Methods.setValue(dto, key.toString(), new Class[]{typeField}, new Object[]{value});
        }
      } // try
      catch (Exception e) {
        regresar.append("|".concat((key.toString())));
      } // end catch
    } // end for
    return regresar.toString().length()>0 ? "Los atributos ".concat(regresar.toString().concat("| no existen en la clase [".concat(dto.getClass().getSimpleName()).concat("]"))) : "";
  }

  public static Object formatType(Object value) {
    if (value!=null&&value.getClass().getSimpleName().equals("BigDecimal")) {
      return ((Number) value).longValue();
    }
    else {
      return value;
    }
  }

  public static boolean isSameType(Class typeField, Object value) {
    return typeField.getName().equals(value.getClass().getName());
  }

  public static Object formatType(Class typeField, Object value) {
    Object regresar=null;
    String type=typeField.getSimpleName();
    try {
      if (value!=null) {
        if (!isSameType(typeField, value)) {
          if (type.equals("Long")) {
            regresar=new Long(value.toString());
          }
          else if (type.equals("Date")) {
            regresar=new Date(Fecha.getFechaCalendar(value.toString()).getTimeInMillis());
          }
          else if (type.equals("Timestamp")) {
            regresar=new Timestamp(Fecha.getFechaHora(value.toString()).getTimeInMillis());
          }
          else if (type.equals("String")) {
            regresar=value.toString();
          }
          else if (type.equals("Double")) {
            regresar=new Double(value.toString());
          }
          else {
            regresar=value;
          }
        } // if
        else {
          regresar=value;
        } // else
      } // if
    } // end try
    catch (Exception e) {
      Error.mensaje(e);
      regresar=null;
    } // end catch
    return regresar;
  } // formarType
}
