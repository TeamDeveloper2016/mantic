package mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.criterios;

import java.text.MessageFormat;
import java.util.List;
import javax.faces.component.UIComponentBase;
import mx.org.kaana.kajool.enums.ETipoDato;
import mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.FormatoCampo;
import mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.OperadorRelacional;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 22/09/2015
 *@time 03:17:11 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class FieldText extends Criterio implements ICriterio {

  private static final long serialVersionUID = -4212682672794687214L;

  public FieldText(String nombre, String titulo) {
    this(nombre, titulo, 80L);
  }

  public FieldText(String nombre, String titulo, Long size) {
    this(nombre, titulo, size, null);
  }

  public FieldText(String nombre, String titulo, Long size, Object value) {
    super(nombre, titulo, OperadorRelacional.getInstance().toFieldText(), FormatoCampo.getInstance().toFormatText(), 1L, size, ETipoDato.TEXT, new Integer(0), value, 0L);
  }

  @Override
  public List<UIComponentBase> create(String value) {
    return super.clone(super.sizeInput(), value);
  }

  @Override
  public String toSql() {
    return this.getNombre().concat(" ".concat(MessageFormat.format(OperadorRelacional.getInstance().toFieldText().get(Integer.valueOf(this.getOperador().toString())).getCode(), this.getValue().toString())));
  }

}
