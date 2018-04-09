package mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.accion;

import java.util.List;
import javax.faces.component.UIComponentBase;
import mx.org.kaana.libs.formato.Cadena;
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
    return super.create(value);
  }

  @Override
  public Object getValue(){
    if(Cadena.isVacio(super.getValue()))
      return new Double(0.0);
    else
      return super.getValue();
  }

}
