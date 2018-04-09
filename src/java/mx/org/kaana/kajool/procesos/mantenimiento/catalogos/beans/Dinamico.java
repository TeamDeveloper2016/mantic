package mx.org.kaana.kajool.procesos.mantenimiento.catalogos.beans;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import static mx.org.kaana.kajool.enums.ETipoDato.DATE;
import static mx.org.kaana.kajool.enums.ETipoDato.DOUBLE;
import static mx.org.kaana.kajool.enums.ETipoDato.LONG;
import static mx.org.kaana.kajool.enums.ETipoDato.TEXT;
import static mx.org.kaana.kajool.enums.ETipoDato.TIME;
import static mx.org.kaana.kajool.enums.ETipoDato.TIMESTAMP;
import mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.criterios.Criterio;
import mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.criterios.FieldDate;
import mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.criterios.FieldEntero;
import mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.criterios.FieldFlotante;
import mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.criterios.FieldForaneo;
import mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.criterios.FieldForaneoDialogo;
import mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.criterios.FieldText;
import mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.criterios.FieldTime;
import mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.criterios.FieldTimestamp;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 21/09/2015
 * @time 04:11:45 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public final class Dinamico implements IBaseDto, Serializable {

  private static final long serialVersionUID= -908492231693036605L;
  private static final String JAVASCRIPT    = "''{0}'': {4}\"validaciones\": ''{1}'', \"mascara\": ''{2}'' {3} {5},\n";

  private Long idCatalogo;
  private String descripcion;
  private String nombre;
  private Long tipoTabla;
  private String precondicion;
  private List<Campo> campos;
  private List<Criterio> criterios;
  private List<Column> mostrar;
  private List<Criterio> capturar;
  private List<Criterio> modificar;
  private List<Criterio> verMas;

  public Dinamico() {
    this(-1L);
  }

  public Dinamico(Long idCatalogo) {
    this.idCatalogo = idCatalogo;
  }

  public List<Campo> getCampos() {
    return campos;
  }

  public void setCampos(List<Campo> campos) {
    this.campos = campos;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public Long getIdCatalogo() {
    return idCatalogo;
  }

  public void setIdCatalogo(Long idCatalogo) {
    this.idCatalogo = idCatalogo;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getPrecondicion() {
    return precondicion;
  }

  public void setPrecondicion(String precondicion) {
    this.precondicion = precondicion;
  }

  public Long getTipoTabla() {
    return tipoTabla;
  }

  public void setTipoTabla(Long tipoTabla) {
    this.tipoTabla = tipoTabla;
  }

  public List<Criterio> getCriterios() {
    return criterios;
  }

  public List<Column> getMostrar() {
    return mostrar;
  }

  public List<Criterio> getCapturar() {
    return capturar;
  }

  public List<Criterio> getModificar() {
    return modificar;
  }

  public List<Criterio> getVerMas() {
    return verMas;
  }

  protected void init() throws Exception {
    Map<String, Object> params = new HashMap<>();
    this.mostrar  = new ArrayList();
    this.criterios= new ArrayList();
    this.capturar = new ArrayList();
    this.modificar= new ArrayList();
    this.verMas   = new ArrayList();
    try {
      params.put(Constantes.SQL_CONDICION, "id_catalogo= " + getIdCatalogo());
      this.campos= (List<Campo>) DaoFactory.getInstance().toEntitySet(Campo.class, "TcJanalCamposDto", params);
      for (Campo campo: campos) {
        if (campo.getBuscar() == Constantes.SI)
          this.toBuscar(this.criterios, campo);
        if (campo.getMostrar() == Constantes.SI)
          this.mostrar.add(new Column(campo.getDescripcion(), Cadena.toBeanName(campo.getNombre()), EFormatoDinamicos.valueOf(campo.getFormato())));
        if (campo.getCapturar() == Constantes.SI)
          this.toCapturar(this.capturar, campo);
        if (campo.getModificar()== Constantes.SI)
          this.toCapturar(this.modificar, campo);
        if (campo.getVerMas()== Constantes.SI)
          this.toConsultar(this.verMas, campo);
      } // for campo
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    finally {
      Methods.clean(params);
    } // finally
  }

  public String toKeyName() {
    String regresar = null;
    for (Campo campo : this.campos) {
      if (campo.getDuplicados() != null && Integer.parseInt(campo.getDuplicados()) == Constantes.SI) {
        regresar = Cadena.toBeanName(campo.getNombre());
        break;
      } // if
    } // for
    return regresar;
  }

  public String getProceso() throws Exception {
    return this.tipoTabla.equals(1L) ? Class.forName(this.nombre).getSimpleName() : this.nombre;
  }

  @Override
  public Class toHbmClass() {
    return this.getClass();
  }

  @Override
  public Long getKey() {
    return getIdCatalogo();
  }

  @Override
  public void setKey(Long key) {
    this.idCatalogo = key;
  }

  @Override
  public Map toMap() {
    Map<String, Object> regresar = new HashMap<>();
    regresar.put("idCatalogo", getIdCatalogo());
    regresar.put("descripcion", getDescripcion());
    regresar.put("nombre", getNombre());
    regresar.put("tipoTabla", getTipoTabla());
    regresar.put("precondicion", getPrecondicion());
    return regresar;
  }

  @Override
  public Object[] toArray() {
    Object[] regresar = new Object[]{getIdCatalogo(), getDescripcion(), getNombre(), getTipoTabla(), getPrecondicion()};
    return regresar;
  }

  @Override
  public boolean isValid() {
    return getIdCatalogo() != null;
  }

  @Override
  public Object toValue(String name) {
    return Methods.getValue(this, name);
  }

  @Override
  public String toAllKeys() {
    StringBuilder regresar = new StringBuilder();
    regresar.append("|");
    regresar.append("idCatalogo~");
    regresar.append(getIdCatalogo());
    regresar.append("|");
    return regresar.toString();
  }

  @Override
  public String toKeys() {
    StringBuilder regresar = new StringBuilder();
    regresar.append(getIdCatalogo());
    return regresar.toString();
  }

  @Override
  protected void finalize() throws Throwable {
    Methods.clean(this.campos);
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 29 * hash + Objects.hashCode(this.idCatalogo);
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    } // if
    if (getClass() != obj.getClass()) {
      return false;
    } // if
    final Dinamico other = (Dinamico) obj;
    if (!Objects.equals(this.idCatalogo, other.idCatalogo)) {
      return false;
    } // if
    return true;
  }

  @Override
  public String toString() {
    return "Dinamico{" + "idCatalogo=" + idCatalogo + ", descripcion=" + descripcion + ", nombre=" + nombre + ", tipoTabla=" + tipoTabla + ", precondicion=" + precondicion + ", campos=" + campos + '}';
  }

  private String toJsJanal(Campo campo, String validation, String mask) {
    return MessageFormat.format(JAVASCRIPT, new Object[] {Cadena.toBeanName(campo.getNombre()), validation, mask, campo.getError()== null? "": ", \"mensaje\": \"".concat(campo.getError()).concat("\""), "{","}"});
  }

  public String toFiltro() {
    StringBuilder regresar= new StringBuilder();
    for(Campo campo: this.getCampos()) {
      if(campo.getBuscar() == Constantes.SI)
        regresar.append(this.toJsJanal(campo, "libre", "no-aplica"));
    } // for
    if(regresar.length()> 0)
      regresar.deleteCharAt(regresar.length()- 2);
    return "{".concat(regresar.toString().replace("\n", "").replace("'", "\"")).concat("}");
  }

  public String toAccion() {
    StringBuilder regresar= new StringBuilder();
    for(Campo campo: this.getCampos()) {
      if(campo.getCapturar()== Constantes.SI)
        regresar.append(this.toJsJanal(campo, campo.getValidacion(), campo.getMascara()));
    } // for
    if(regresar.length()> 0)
      regresar.deleteCharAt(regresar.length()- 2);
    return "{".concat(regresar.toString().replace("\n", "").replace("'", "\"")).concat("}");
  }

  private void toCapturar(List<Criterio> actualizar, Campo campo) {
    switch (campo.getETipo()) {
      case TEXT:
        actualizar.add(new mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.accion.FieldText(campo.getNombre(), campo.getDescripcion(), campo.getLongitud()));
        break;
      case LONG:
        actualizar.add(new mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.accion.FieldEntero(campo.getNombre(), campo.getDescripcion(), campo.getLongitud()));
        break;
      case DOUBLE:
        actualizar.add(new mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.accion.FieldFlotante(campo.getNombre(), campo.getDescripcion(), campo.getLongitud()));
        break;
      case DATE:
        actualizar.add(new mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.accion.FieldDate(campo.getNombre(), campo.getDescripcion()));
        break;
      case TIME:
        actualizar.add(new mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.accion.FieldTime(campo.getNombre(), campo.getDescripcion()));
        break;
      case TIMESTAMP:
        actualizar.add(new mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.accion.FieldTimestamp(campo.getNombre(), campo.getDescripcion()));
        break;
    } // switch EtipoDato
  }

  private void toBuscar(List<Criterio> buscar, Campo campo) throws Exception {
    switch (campo.getETipo()) {
      case TEXT:
        buscar.add(new FieldText(campo.getNombre(), campo.getDescripcion(), campo.getLongitud()));
        break;
      case LONG:
        if(!Cadena.isVacio(campo.getForaneo()))
          switch(campo.getSeleccionar().intValue()) {
            case Constantes.SI:  // Este es el select one menu normal cuando el catalogo es pequeño
              buscar.add(new FieldForaneo(campo.getNombre(), campo.getDescripcion(), campo.getForaneo(), Collections.EMPTY_MAP));
              break;
            case Constantes.NO: // Este ese el select one menu con ventanda de dialogo cuando el catalogo es muy grande
              buscar.add(new FieldForaneoDialogo(campo.getNombre(), campo.getDescripcion(), campo.getForaneo(), Collections.EMPTY_MAP));
              break;
            default:
              buscar.add(new FieldEntero(campo.getNombre(), campo.getDescripcion(), campo.getLongitud()));
              break;
          } // switch
        break;
      case DOUBLE:
        buscar.add(new FieldFlotante(campo.getNombre(), campo.getDescripcion(), campo.getLongitud()));
        break;
      case DATE:
        buscar.add(new FieldDate(campo.getNombre(), campo.getDescripcion()));
        break;
      case TIME:
        buscar.add(new FieldTime(campo.getNombre(), campo.getDescripcion()));
        break;
      case TIMESTAMP:
        buscar.add(new FieldTimestamp(campo.getNombre(), campo.getDescripcion()));
        break;
    } // switch EtipoDato
  }

  private void toConsultar(List<Criterio> consultar, Campo campo) {
    switch (campo.getETipo()) {
      case TEXT:
        consultar.add(new mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.consulta.FieldText(campo.getNombre(), campo.getDescripcion(), campo.getLongitud()));
        break;
      case LONG:
        consultar.add(new mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.consulta.FieldEntero(campo.getNombre(), campo.getDescripcion(), campo.getLongitud()));
        break;
      case DOUBLE:
        consultar.add(new mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.consulta.FieldFlotante(campo.getNombre(), campo.getDescripcion(), campo.getLongitud()));
        break;
      case DATE:
        consultar.add(new mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.consulta.FieldDate(campo.getNombre(), campo.getDescripcion()));
        break;
      case TIME:
        consultar.add(new mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.consulta.FieldTime(campo.getNombre(), campo.getDescripcion()));
        break;
      case TIMESTAMP:
        consultar.add(new mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.consulta.FieldTimestamp(campo.getNombre(), campo.getDescripcion()));
        break;
    } // switch EtipoDato
  }

}
