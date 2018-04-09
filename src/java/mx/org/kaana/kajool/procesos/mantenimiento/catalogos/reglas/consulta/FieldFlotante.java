package mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.consulta;

import java.util.List;
import javax.faces.component.UIComponentBase;
import mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.accion.IValue;
import mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.criterios.ICriterio;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 22/09/2015
 *@time 03:49:44 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class FieldFlotante extends mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.criterios.FieldFlotante implements ICriterio, IValue {

  private static final long serialVersionUID = 3536201624102841455L;

  public FieldFlotante(String nombre, String titulo, Long size) {
    super(nombre, titulo, size, new Double(0.0));
  }

  @Override
  public List<UIComponentBase> create(String value) {
    return super.clone(value);
  }

  @Override
  public Object getValue(){
    if(super.getValue()!= null && !super.getValue().equals(""))
      return new Double((String)super.getValue());
    return new Double(0.0);
  }

}
