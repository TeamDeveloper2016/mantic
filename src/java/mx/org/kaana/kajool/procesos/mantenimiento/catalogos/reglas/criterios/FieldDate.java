package mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.criterios;

import java.text.MessageFormat;
import java.util.List;
import javax.faces.component.UIComponentBase;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.kajool.enums.ETipoDato;
import mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.FormatoCampo;
import mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.OperadorRelacional;
import mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.componentes.KajoolDate;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 22/09/2015
 *@time 03:54:12 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class FieldDate extends Criterio implements ICriterio {

  private static final long serialVersionUID = -3383226234857942805L;

  public FieldDate(String nombre, String titulo) {
    this(nombre, titulo, null);
  }

  public FieldDate(String nombre, String titulo, Object value) {
    super(nombre, titulo, OperadorRelacional.getInstance().toFieldDate(), FormatoCampo.getInstance().toFormatDate(), 1L, 80L, ETipoDato.DATE, new Integer(0), value, 0L);
  }

  @Override
  public List<UIComponentBase> create(String value) {
    return super.clone(new KajoolDate(Cadena.toBeanName(this.getNombre()), null).create(), value);
  }

  @Override
  public String toSql() {
    return this.getNombre().concat(" ".concat(MessageFormat.format(OperadorRelacional.getInstance().toFieldDate().get(Integer.valueOf(this.getOperador().toString())).getCode(), this.getValue().toString())));
  }

}
