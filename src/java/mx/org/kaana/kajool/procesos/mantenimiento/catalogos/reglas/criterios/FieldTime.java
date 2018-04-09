package mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.criterios;

import java.text.MessageFormat;
import java.util.List;
import javax.faces.component.UIComponentBase;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.kajool.enums.ETipoDato;
import mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.FormatoCampo;
import mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.OperadorRelacional;
import mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.componentes.KajoolTime;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 22/09/2015
 *@time 03:50:28 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class FieldTime extends Criterio implements ICriterio {

  private static final long serialVersionUID = 500379635714774080L;

  public FieldTime(String nombre, String titulo) {
    this(nombre, titulo, null);
  }


  public FieldTime(String nombre, String titulo, Object value) {
    super(nombre, titulo, OperadorRelacional.getInstance().toFieldTime(), FormatoCampo.getInstance().toFormatDate(), 1L, 80L, ETipoDato.TIME, new Integer(0), value, 0L);
  }

  @Override
  public List<UIComponentBase> create(String value) {
    return super.clone(new KajoolTime(Cadena.toBeanName(this.getNombre()), null).create(), value);
  }

  @Override
  public String toSql() {
    return this.getNombre().concat(" ".concat(MessageFormat.format(OperadorRelacional.getInstance().toFieldTime().get(Integer.valueOf(this.getOperador().toString())).getCode(), this.getValue().toString())));
  }

}
