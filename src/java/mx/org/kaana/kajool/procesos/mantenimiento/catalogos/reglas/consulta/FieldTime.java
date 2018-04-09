package mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.consulta;

import java.sql.Time;
import java.util.List;
import javax.faces.component.UIComponentBase;
import mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.accion.IValue;
import mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.criterios.ICriterio;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 22/09/2015
 *@time 03:50:28 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class FieldTime extends mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.criterios.FieldTime implements ICriterio, IValue {

  private static final long serialVersionUID = 3324567893314098634L;

  public FieldTime(String nombre, String titulo) {
    super(nombre, titulo, new Time(java.util.Calendar.getInstance().getTimeInMillis()));
  }

  @Override
  public List<UIComponentBase> create(String value) {
    return super.clone(value);
  }

  @Override
  public Object getValue(){
    if(super.getValue()!= null)
      return super.getValue();
    return new Time(java.util.Calendar.getInstance().getTimeInMillis());
  }

}
