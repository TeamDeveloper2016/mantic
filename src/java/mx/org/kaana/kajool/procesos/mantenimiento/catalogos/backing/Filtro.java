package mx.org.kaana.kajool.procesos.mantenimiento.catalogos.backing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.JsfUtilities;
import mx.org.kaana.libs.pagina.UISelect;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import mx.org.kaana.kajool.procesos.mantenimiento.catalogos.beans.Busqueda;
import mx.org.kaana.kajool.procesos.mantenimiento.catalogos.beans.Column;
import mx.org.kaana.kajool.procesos.mantenimiento.catalogos.beans.Dinamicos;
import mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.componentes.KajoolPanelGrid;
import mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.criterios.Criterio;
import mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.criterios.ICriterio;
import org.primefaces.component.panelgrid.PanelGrid;
import org.primefaces.context.RequestContext;

/**
 *
 * @project KAJOOL (Control system polls)
 * @company KAANA
 * @date 10/09/2015
 * @time 13:08:51 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
@ManagedBean(name = "kajoolDinamicosFiltro")
@ViewScoped
public class Filtro extends UIBase implements Serializable {

  private static final long serialVersionUID = 1004519096922738596L;
  private PanelGrid grid;
  private PanelGrid detalle;
  private List<Criterio> detalles;
  private List<Column> columns;
  private boolean eliminar;

  @PostConstruct
  @Override
  protected void init() {
    Long idCatalogo= JsfBase.getFlashAttribute("idCatalogo")!= null? (Long)JsfBase.getFlashAttribute("idCatalogo"): -1L;
    this.eliminar= false;
    this.attrs.put("showCatalogos", Cadena.isVacio(JsfBase.getFlashAttribute("idCatalogo")));
    this.attrs.put("idCatalogo", idCatalogo);
    this.attrs.put("descripcion", "");
    this.attrs.put("catalogos", UISelect.build("TcJanalCatalogosDto", this.attrs, "descripcion"));
    this.grid = (PanelGrid) new KajoolPanelGrid("grid", 4).create();
    this.grid.setColumnClasses("janal-wid-10, janal-wid-10");
    this.detalle = (PanelGrid) new KajoolPanelGrid("detalle", 2).create();
    this.detalle.setColumnClasses("janal-wid-15");
    this.attrs.put("buscar", new Busqueda(new Criterio("")));
    if(idCatalogo.intValue()> 0)
      this.doRefresh();
  } // init();

  public PanelGrid getGrid() {
    return grid;
  }

  public void setGrid(PanelGrid grid) {
    this.grid= grid;
  }

  public List<Column> getColumns() {
    return columns;
  }

  public PanelGrid getDetalle() {
    return detalle;
  }

  public void setDetalle(PanelGrid detalle) {
    this.detalle = detalle;
  }

  public boolean isEliminar() {
    return eliminar;
  }

  @Override
  public void doLoad() {
    List<Columna> columnas = new ArrayList();
    try {
      this.attrs.put(Constantes.SQL_CONDICION, this.toSql());
      this.columns = this.getDinamico().getMostrar();
      for (Column column: columns)
        columnas.add(new Columna(column.getProperty(), column.getFormat()));
      this.lazyModel = new FormatCustomLazy(this.getDinamico().getNombre().substring(this.getDinamico().getNombre().lastIndexOf(".")+ 1), "mto", this.attrs, columnas);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfUtilities.addMessageError(e);
    } // catch
    finally {
      Methods.clean(columnas);
    } // finally
  } // doLoad

  public void doRefresh() {
    Long idCatalogo= (Long)this.attrs.get("idCatalogo");
    try {
      if(idCatalogo!= -1) {
        this.setDinamico(Dinamicos.getInstance().add(idCatalogo));
        this.attrs.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
        this.setCriterios(this.getDinamico().getCriterios());
        this.detalles = this.getDinamico().getVerMas();
        this.toCreate(this.grid, this.getCriterios());
        this.toDisplay(this.detalle, this.detalles);
        RequestContext.getCurrentInstance().addCallbackParam("mascaras", this.getDinamico().toFiltro());
        this.lazyModel= null;
        Methods.clean(this.columns);
      } // if
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfUtilities.addMessageError(e);
    } // catch
  } // doRefresh

  private void toCreate(PanelGrid container, List<Criterio> items) {
    container.getChildren().clear();
    int count = 0;
    for (Criterio item: items) {
      container.getChildren().addAll(((ICriterio)item).create(Criterio.VALUE_FILTER.replace("index", String.valueOf(count))));
      count++;
    } // for
  }

  private void toDisplay(PanelGrid container, List<Criterio> items) {
    container.getChildren().clear();
    for (Criterio item: items) {
      container.getChildren().addAll(((ICriterio)item).create(Criterio.VALUE_DISPLAY.replace("name", item.getId())));
    } // for
  }

  private String toSql() {
    StringBuilder regresar= new StringBuilder();
    for (Criterio criterio: this.getCriterios()) {
      if (!Cadena.isVacio(criterio.getValue()) && criterio.isValid()) {
        regresar.append(((ICriterio)criterio).toSql());
        regresar.append(" and ");
      } // if
    } // for
    if(regresar.length()== 0)
      if(Cadena.isVacio(this.getDinamico().getPrecondicion()))
        regresar.append(Constantes.SQL_VERDADERO);
      else
        regresar.append(this.toApplyParams(this.getDinamico().getPrecondicion()));
    else
      if(Cadena.isVacio(this.getDinamico().getPrecondicion()))
        regresar.delete(regresar.length()- 4, regresar.length());
      else
        regresar.append(this.toApplyParams(this.getDinamico().getPrecondicion()));
    return regresar.toString();
  } // toSql

  public void doAccion(boolean eliminar) {
    this.eliminar= eliminar;
  }

}
