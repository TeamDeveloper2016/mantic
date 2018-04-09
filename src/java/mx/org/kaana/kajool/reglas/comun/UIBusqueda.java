package mx.org.kaana.kajool.reglas.comun;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date May 24, 2012
 * @time 1:30:13 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.hibernate.UtilDto;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.UISelectItem;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.xml.Dml;

public class UIBusqueda<T extends IBaseDto> implements Serializable {

  private static final long serialVersionUID = 3196410649837731358L;

  private String titulo;
  private String proceso;
  private String idXml;
  private List<String> disenio;
  private List<String> desplegados;
  private Map<String, Object> criteria;
  private boolean multiple;
  private String operador;
  private String idKey;
  private List<UISelectItem> resultado;
  private Long total;
  private UISelectItem seleccionado;

  public UIBusqueda(String titulo, Class dto, List<String> disenio, boolean multiple) {
    this(titulo, dto, disenio, new ArrayList<String>(), multiple);
  }

  public UIBusqueda(String titulo, Class dto, List<String> disenio, List<String> desplegados, boolean multiple) {
    this(titulo, dto, disenio, desplegados, multiple, Collections.EMPTY_MAP, "and");
  }

  public UIBusqueda(String titulo, Class dto, List<String> disenio, List<String> desplegados, boolean multiple, String operador) {
    this(titulo, dto, disenio, desplegados, multiple, Collections.EMPTY_MAP, operador);
  }

  public UIBusqueda(String titulo, Class dto, List<String> disenio, List<String> desplegados, boolean multiple, Map<String, Object> criteria) {
    this(titulo, dto, disenio, desplegados, multiple, criteria, "and");
  }

  public UIBusqueda(String titulo, String proceso, String idKey, List<String> disenio, boolean multiple) {
    this(titulo, proceso, idKey, disenio, new ArrayList<String>(), multiple);
  }

  public UIBusqueda(String titulo, String proceso, String idKey, List<String> disenio, List<String> desplegados, boolean multiple) {
    this(titulo, proceso, idKey, disenio, desplegados, multiple, Collections.EMPTY_MAP, "and");
  }

  public UIBusqueda(String titulo, String proceso, String idKey, List<String> disenio, List<String> desplegados, boolean multiple, String operador) {
    this(titulo, proceso, idKey, disenio, desplegados, multiple, Collections.EMPTY_MAP, operador);
  }

  public UIBusqueda(String titulo, String proceso, String idKey, List<String> disenio, List<String> desplegados, boolean multiple, Map<String, Object> criteria) {
    this(titulo, proceso, idKey, disenio, desplegados, multiple, criteria, "and");
  }

  public UIBusqueda(String titulo, Class dto, List<String> disenio, List<String> desplegados, boolean multiple, Map<String, Object> criteria, String operador) {
    this(titulo, dto.getSimpleName(), "", disenio, desplegados, multiple, criteria, operador);
    this.idKey = Methods.toKeyName(dto);
  }

  public UIBusqueda(String titulo, String proceso, String idKey, List<String> disenio, List<String> desplegados, boolean multiple, Map<String, Object> criteria, String operador) {
    this(titulo, proceso, Constantes.DML_SELECT, idKey, disenio, desplegados, multiple, criteria, operador);
  }

  public UIBusqueda(String titulo, String proceso, String idXml, String idKey, List<String> disenio, List<String> desplegados, boolean multiple, Map<String, Object> criteria, String operador) {
    this.proceso = proceso;
    this.idXml = idXml;
    this.idKey = idKey;
    this.disenio = disenio;
    this.multiple = multiple;
    this.desplegados = desplegados;
    this.titulo = titulo;
    this.criteria = criteria;
    this.total = 0L;
    this.operador = operador;
    this.resultado = new ArrayList<>();
    init();
  }

  private void init() {
    buscar("");
    this.seleccionado = new UISelectItem(-1L, "");
  }

  public String getProceso() {
    return proceso;
  }

  public List<String> getDisenio() {
    return disenio;
  }

  public boolean isMultiple() {
    return multiple;
  }

  public List<UISelectItem> getResultado() {
    return resultado;
  }

  public List<String> getDesplegados() {
    return desplegados;
  }

  public String getTitulo() {
    return titulo;
  }

  public Map<String, Object> getCriteria() {
    return criteria == null ? Collections.emptyMap() : this.criteria;
  }

  public Long getTotal() {
    return total;
  }

  public String getOperador() {
    return operador;
  }

  public UISelectItem getSeleccionado() {
    return seleccionado;
  }

  public void setSeleccionado(UISelectItem seleccionado) {
    this.seleccionado = seleccionado;
  }

  public String getIdKey() {
    return idKey;
  }

  private String toSqlTable(Map<String, Object> params) {
    StringBuilder sql = new StringBuilder();
    String regresar = null;
    this.idKey = "indice";
    sql.append(" select indice, table_name from (select id_tabla AS indice, descripcion AS table_name ");
    sql.append(" from tc_janal_tablas) as j ");
    sql.append(" where ");
    sql.append(" {condicion} ");
    sql.append(" order by ");
    sql.append(" table_name");
    regresar = Cadena.replaceParams(sql.toString(), params);
    return regresar;

  }

  public void buscar(String value, String operador) {
    List<T> list = null;
    Map<String, Object> params = new HashMap<String, Object>(getCriteria());
    Busqueda condicion = new Busqueda(getDisenio(), value);
    boolean exist = false;
    try {
      if (value != null && value.trim().length() > 0) {
        String where = (String) params.get(Constantes.SQL_CONDICION);
        if (where != null) {
          params.put(Constantes.SQL_CONDICION, where.concat(" ").concat(operador).concat(" (").concat(condicion.format()).concat(")"));
        } else {
          params.put(Constantes.SQL_CONDICION, condicion.format());
        }
        exist = Dml.getInstance().exists(getProceso(), this.idXml);
        if (exist) {
          list = DaoFactory.getInstance().toEntitySet(getProceso(), this.idXml, params);
        } else {
          list = DaoFactory.getInstance().toEntitySet(toSqlTable(params));
        }
        load(list, getDesplegados());
        if (exist) {
          this.total = DaoFactory.getInstance().toSize(proceso, idXml, params);
        } else {
          this.total = DaoFactory.getInstance().toSize(toSqlTable(params));
        }
      } // if
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
  }

  public void buscar(String value) {
    buscar(value, getOperador());
  }

  private void load(List<T> list, List<String> desplegados) {
    StringBuilder sb = new StringBuilder();
    for (IBaseDto item : list) {
      for (String attr : desplegados) {
        try {
          Object value = item.toValue(attr);
          if (value != null) {
            sb.append(UtilDto.getFormatType(value));
            sb.append(" ");
          } // if
        } // try
        catch (Exception e) {
          Error.mensaje(e);
        } // catch
      } // for
      if (sb.length() >= 100) {
        sb.delete(97, sb.length()).append("...");
      }
      getResultado().add(new UISelectItem(item.toValue(this.idKey), sb.toString()));
      sb.delete(0, sb.length());
    } // for
  }

  public String getAlias() {
    StringBuilder regresar = new StringBuilder();
    regresar.append(" por ");
    for (String campo : getDesplegados()) {
      regresar.append(Cadena.toSqlName(campo).replaceAll("_", " "));
      if (campo != null && campo.trim().length() > 0) {
        regresar.append(", ");
      }
    } // for
    return regresar.substring(0, regresar.length() - 2);
  }

  public Long getEncontrados() {
    return Integer.valueOf(getResultado().size()).longValue();
  }

  @Override
  public String toString() {
    return "UIBusqueda{" + "titulo=" + this.titulo + ", proceso=" + proceso + '}';
  }

  public void clear() {
    if (getResultado() != null) {
      getResultado().clear();
    }
    this.total = 0L;
  }

  public void update(String value) {
    this.seleccionado.setValue(new BigDecimal((String) this.seleccionado.getValue()));
    update();
  }

  public void update() {
    int index = this.resultado.indexOf(this.seleccionado);
    if (index >= 0) {
      this.seleccionado = new UISelectItem(((UISelectItem) this.resultado.get(index)).getValue(), ((UISelectItem) this.resultado.get(index)).getLabel());
    }
  }

}
