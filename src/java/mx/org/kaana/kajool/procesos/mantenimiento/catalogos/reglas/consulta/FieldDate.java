package mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.consulta;

import java.sql.Date;
import java.util.List;
import javax.faces.component.UIComponentBase;
import mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.accion.IValue;
import mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.criterios.ICriterio;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 22/09/2015
 *@time 03:54:12 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class FieldDate extends mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.criterios.FieldDate implements ICriterio, IValue {

  private static final long serialVersionUID = -6359739535507787622L;

  public FieldDate(String nombre, String titulo) {
    super(nombre, titulo, new Date(java.util.Calendar.getInstance().getTimeInMillis()));
  }

  @Override
  public List<UIComponentBase> create(String value) {
    return super.clone(value);
  }

  @Override
  public Object getValue(){
    if(super.getValue()!= null)
      return super.getValue();
    return new Date(java.util.Calendar.getInstance().getTimeInMillis());
  }

}
