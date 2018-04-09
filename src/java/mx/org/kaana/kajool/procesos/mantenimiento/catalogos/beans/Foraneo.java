package mx.org.kaana.kajool.procesos.mantenimiento.catalogos.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.pagina.UISelect;
import mx.org.kaana.libs.pagina.UISelectItem;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.criterios.Criterio;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 1/10/2015
 * @time 08:08:40 AM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public class Foraneo implements Serializable {

  private static final long serialVersionUID = 3443018469704591760L;

  private String process;
  private String idXml;
  private String display;
  private String condition;

  public Foraneo() {
    this("kajjol", "row", "descripcion", Constantes.SQL_VERDADERO);
  }

  public Foraneo(String process, String idXml, String display, String condition) {
    this.process = process;
    this.idXml = idXml;
    this.display = display;
    this.condition = condition;
  }

  public String getProcess() {
    return process;
  }

  public void setProcess(String process) {
    this.process = process;
  }

  public String getIdXml() {
    return idXml;
  }

  public void setIdXml(String idXml) {
    this.idXml = idXml;
  }

  public String getDisplay() {
    return display;
  }

  public void setDisplay(String display) {
    this.display = display;
  }

  public String getCondition() {
    return condition;
  }

  public void setCondition(String condition) {
    this.condition = Cadena.isVacio(condition)? Constantes.SQL_VERDADERO: condition;
  }

  @Override
  public int hashCode() {
    int hash = 3;
    hash = 37 * hash + Objects.hashCode(this.process);
    hash = 37 * hash + Objects.hashCode(this.idXml);
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Foraneo other = (Foraneo) obj;
    if (!Objects.equals(this.process, other.process)) {
      return false;
    }
    if (!Objects.equals(this.idXml, other.idXml)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "Foraneo{" + "process=" + process + ", idXml=" + idXml + ", display=" + display + ", condition=" + condition + '}';
  }

  public List<UISelectItem> toItems(Map<String, Object> params, boolean selection) {
    StringBuilder sql= new StringBuilder();
    sql.append(this.toApplyParams(params));
    if(sql.length()== 0) {
      if(params.containsKey(Criterio.FOREIGN_PARAM))
        sql.append(params.get(Criterio.FOREIGN_PARAM));
    } // if
    else
      if(params.containsKey(Criterio.FOREIGN_PARAM))
        sql.append(" and ").append(params.get(Criterio.FOREIGN_PARAM));
    params.put(Constantes.SQL_CONDICION, sql.toString());
    List<UISelectItem> regresar= new ArrayList<>();
    if(selection)
      regresar.add(new UISelectItem(-1L, "Seleccione"));
    regresar.addAll(UISelect.build(this.process, this.idXml, params, this.display, " - ", EFormatoDinamicos.MAYUSCULAS));
    return regresar;
  }

  public List<UISelectItem> toItems(Map<String, Object> params) {
    return this.toItems(params, true);
  }

  private String toApplyParams(Map<String, Object> params) {
    // Aqui se tiene que aplicar los cambios de los parametros para la precondicion en caso de existir
    return Cadena.replaceParams(this.condition, params);
  }

}
