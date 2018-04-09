package mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.criterios;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.el.ValueExpression;
import javax.faces.component.UIComponentBase;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.pagina.UISelectItem;
import mx.org.kaana.libs.pagina.convertidores.entity.Registro;
import mx.org.kaana.kajool.enums.ETipoDato;
import mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.Operador;
import mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.accion.IValue;
import mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.componentes.KajoolInputText;
import mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.componentes.KajoolOutputLabel;
import mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.componentes.KajoolOutputText;
import mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.componentes.KajoolSelectOneMenu;
import mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.componentes.KajoolSpacer;
import org.primefaces.component.inputtext.InputText;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 21/09/2015
 *@time 04:27:13 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Criterio implements Serializable {

  private static final long serialVersionUID = -4273727614824922112L;

  public static final String VALUE_FILTER = "#{kajoolDinamicosFiltro.criterios[index].value}";
  public static final String VALUE_DISPLAY= "#{kajoolDinamicosFiltro.attrs.selected.name}";
  public static final String VALUE_ACTION = "#{kajoolDinamicosAccion.criterios[index].value}";
  public static final String FOREIGN_PARAM= "kajoolParamDinamico";

  private String id;
  private String nombre;
	private String titulo;
	private List<Operador> operadores;
  private List<Operador> formatos;
	private Long longitud;
	private ETipoDato tipo;
  private Integer operador;
	private Object value;
  private Long orden;
  private boolean foraneo;

  public Criterio(String id) {
    this(id, id, Collections.EMPTY_LIST, Collections.EMPTY_LIST, 0L, 0L, ETipoDato.LONG, 0, null, 0L);
  }

  public Criterio(String nombre, String titulo, List<Operador> operadores, List<Operador> formatos, Long normal, Long longitud, ETipoDato tipo, Integer operador, Object value, Long orden) {
    this(nombre, titulo, operadores, formatos, normal, longitud, tipo, operador, value, orden, false);
  }

  public Criterio(String nombre, String titulo, List<Operador> operadores, List<Operador> formatos, Long normal, Long longitud, ETipoDato tipo, Integer operador, Object value, Long orden, boolean foraneo) {
    this.nombre = nombre;
    this.titulo = titulo;
    this.operadores = operadores;
    this.formatos = formatos;
    this.longitud = longitud;
    this.tipo = tipo;
    this.operador = operador;
    this.value = value;
    this.orden = orden;
    this.foraneo = foraneo;
    this.id = Cadena.toBeanName(nombre);
  }

  public String getId() {
    return id;
  }

  public String getNombre() {
    return nombre;
  }

  public String getTitulo() {
    return titulo;
  }

  protected List<Operador> getOperadores() {
    return operadores;
  }

  protected List<Operador> getFormatos() {
    return formatos;
  }

  protected Long getLongitud() {
    return longitud;
  }

  protected ETipoDato getTipo() {
    return tipo;
  }

  public Integer getOperador() {
    return operador;
  }

  public Object getValue() {
    return value;
  }

  public Long getOrden() {
    return orden;
  }

  public void setOperador(Integer operador) {
    this.operador = operador;
  }

  public void setValue(Object value) {
    this.value = value;
  }

  protected List<UISelectItem> toOperator() {
    List<UISelectItem> regresar = new ArrayList();
    for(Operador item: this.getOperadores()) {
      regresar.add(new UISelectItem(item.getIndex(), item.getLabel()));
    } // for
    return regresar;
  }

  protected UIComponentBase toSelectOneMenu(String value) {
    ValueExpression expression = FacesContext.getCurrentInstance().getApplication().getExpressionFactory().createValueExpression(FacesContext.getCurrentInstance().getELContext(), value, Object.class);
    return new KajoolSelectOneMenu("o".concat(Cadena.toBeanName(this.getNombre())), expression, toOperator()).create();
  }

  protected UIComponentBase toOutputLabel() {
    return new KajoolOutputLabel("x".concat(Cadena.toBeanName(this.getNombre())), this.getTitulo().concat(": "), Cadena.toBeanName(this.getNombre())).create();
  }

  protected UIComponentBase toLabel() {
    String name= Cadena.toBeanName(this.getNombre());
    return new KajoolOutputLabel("y".concat(name), this.getTitulo().concat(": "), "z".concat(name)).create();
  }

  protected UIComponentBase toOutput(ValueExpression value) {
    return new KajoolOutputText("z".concat(Cadena.toBeanName(this.getNombre())), value).create();
  }

  protected List<UIComponentBase> clone(String value) {
    List<UIComponentBase> regresar = new ArrayList();
    regresar.add(this.toLabel());
    ValueExpression expression= FacesContext.getCurrentInstance().getApplication().getExpressionFactory().createValueExpression(FacesContext.getCurrentInstance().getELContext(), value, Object.class);
    UIComponentBase component = this.toOutput(expression);
    switch (this.tipo) {
      case DATE:
      case TIME:
      case TIMESTAMP:
        ((HtmlOutputText)component).setConverter(new Registro());
        break;
    } // switch
    regresar.add(component);
    return regresar;
  }

  // Lista de campos a crear cuando se va a pintar los criterios de seleccion
  public List<UIComponentBase> clone(UIComponentBase component, String value) {
    List<UIComponentBase> regresar = new ArrayList();
    regresar.add(this.toOutputLabel());
    if(!(this instanceof IValue))
      regresar.add(this.toSelectOneMenu(value.replace(".value", ".operador")));
    ValueExpression expression= FacesContext.getCurrentInstance().getApplication().getExpressionFactory().createValueExpression(FacesContext.getCurrentInstance().getELContext(), value, Object.class);
    component.setValueExpression("value", expression);
    regresar.add(component);
    regresar.add(new KajoolSpacer().create());
    return regresar;
  }

  protected UIComponentBase sizeInput(Converter converter) {
    UIComponentBase regresar= this.sizeInput();
    ((InputText)regresar).setConverter(converter);
    return regresar;
  }

  protected UIComponentBase sizeInput() {
    UIComponentBase regresar= new KajoolInputText(Cadena.toBeanName(this.nombre), null).create();
    ((InputText)regresar).setSize(this.longitud.intValue()> 79? 80: this.longitud.intValue()+ 3);
    ((InputText)regresar).setMaxlength(this.longitud.intValue());
    return regresar;
  }

  public boolean isValid() {
    return !this.foraneo || ((Long)this.getValue()).longValue()!= -1L;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 97 * hash + Objects.hashCode(this.id);
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
    final Criterio other = (Criterio) obj;
    if (!Objects.equals(this.id, other.id)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "Criterio{" + "id=" + id + ", nombre=" + nombre + ", titulo=" + titulo + ", longitud=" + longitud + ", tipo=" + tipo + ", value=" + value + ", orden=" + orden + ", foraneo=" + foraneo + '}';
  }

}
