package mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.accion;

import java.sql.Date;
import java.util.List;
import javax.faces.component.UIComponentBase;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.criterios.ICriterio;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 22/09/2015
 *@time 03:54:12 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class FieldDate extends mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.criterios.FieldDate implements mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.criterios.ICriterio, mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.accion.IValue {

  private static final long serialVersionUID = -6359739535507787622L;

  public FieldDate(String nombre, String titulo) {
    super(nombre, titulo, new Date(java.util.Calendar.getInstance().getTimeInMillis()));
  }

  @Override
  public List<UIComponentBase> create(String value) {
    return super.create(value);
  }

  @Override
  public Object getValue(){
    if(Cadena.isVacio(super.getValue()))
      return new Date(java.util.Calendar.getInstance().getTimeInMillis());
    else
      return super.getValue();
  }

}
