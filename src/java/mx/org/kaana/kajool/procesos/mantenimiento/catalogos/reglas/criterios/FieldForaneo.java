package mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.criterios;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.component.UIComponentBase;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.json.Decoder;
import mx.org.kaana.libs.pagina.UISelectItem;
import mx.org.kaana.libs.pagina.convertidores.EnteroLargo;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.enums.ETipoDato;
import mx.org.kaana.kajool.procesos.mantenimiento.catalogos.beans.Foraneo;
import mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.FormatoCampo;
import mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.OperadorRelacional;
import mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.componentes.KajoolSelectOneMenu;
import org.primefaces.component.selectonemenu.SelectOneMenu;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 1/10/2015
 * @time 08:08:40 AM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class FieldForaneo extends Criterio implements ICriterio {

  private static final long serialVersionUID = 8183199737628201456L;

  private Foraneo foraneo;
  private Map<String, Object> params;
  private List<UISelectItem> items;

  public FieldForaneo(String nombre, String titulo, String foraneo, Map<String, Object> params) throws Exception {
    super(nombre, titulo, OperadorRelacional.getInstance().toFieldNumeric(), FormatoCampo.getInstance().toFormatNumeric(), 1L, 80L, ETipoDato.LONG, new Integer(0), -1L, 0L, true);
    this.foraneo= (Foraneo) Decoder.toSerializar(Foraneo.class, foraneo);
    this.params = params;
  }

  public List<UISelectItem> getItems() {
    return items;
  }

  @Override
  public List<UIComponentBase> create(String value) {
    return this.create(value, false);
  }

  protected List<UIComponentBase> create(String value, boolean clean) {
    if(clean)
      this.items= new ArrayList<>();
    else
      this.items= toItems("");
    SelectOneMenu component= (SelectOneMenu)(new KajoolSelectOneMenu(Cadena.toBeanName(this.getNombre()), this.items).create());
    component.setConverter(new EnteroLargo());
    return super.clone(component, value);
  }

  @Override
  public String toSql() {
    return this.getNombre().concat(" ".concat(MessageFormat.format(OperadorRelacional.getInstance().toFieldNumeric().get(Integer.valueOf(this.getOperador().toString())).getCode(), this.getValue().toString())));
  }

  public List<UISelectItem> toItems(String condition) {
    Map<String, Object> elements= new HashMap<>(this.params);
    if(!Cadena.isVacio(condition))
      elements.put(Criterio.FOREIGN_PARAM, condition);
    List<UISelectItem> regresar= new ArrayList<>();
    try {
      regresar.addAll(this.foraneo.toItems(elements));
    } // try
    finally {
      Methods.clean(elements);
    } // finally
    return regresar;
  }

}
