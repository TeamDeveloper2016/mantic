package mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.accion;

import java.sql.Timestamp;
import java.util.List;
import javax.faces.component.UIComponentBase;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.criterios.ICriterio;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 22/09/2015
 *@time 03:52:42 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class FieldTimestamp extends mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.criterios.FieldTimestamp implements ICriterio, IValue {

  private static final long serialVersionUID = 1327021033647921896L;

  public FieldTimestamp(String nombre, String titulo) {
    super(nombre, titulo, new Timestamp(java.util.Calendar.getInstance().getTimeInMillis()));
  }

  @Override
  public List<UIComponentBase> create(String value) {
    return super.create(value);
  }

  @Override
  public Object getValue() {
    if(Cadena.isVacio(super.getValue()))
      return new Timestamp(java.util.Calendar.getInstance().getTimeInMillis());
    else
      return super.getValue();
  }

}
