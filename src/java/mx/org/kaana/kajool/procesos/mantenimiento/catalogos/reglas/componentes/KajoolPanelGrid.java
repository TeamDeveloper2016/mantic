package mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.componentes;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import org.primefaces.component.panelgrid.PanelGrid;
import java.util.UUID;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 14/09/2015
 *@time 10:29:33 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class KajoolPanelGrid extends PanelGrid implements IComponent{

  private static String render = "org.primefaces.component.PanelGridRenderer";
  private String id;
  private int columns;

  public KajoolPanelGrid() {
    this(UUID.randomUUID().toString());
  }

  public KajoolPanelGrid(String id) {
    this(id, 1);
  }

  public KajoolPanelGrid(String id, int columns) {
    this.id = id;
    this.columns = columns;
  }

  @Override
  public UIComponentBase create() {
    PanelGrid regresar = (PanelGrid) FacesContext.getCurrentInstance().getApplication().createComponent(FacesContext.getCurrentInstance(), PanelGrid.COMPONENT_TYPE, this.render);
    regresar.setId(this.id);
    regresar.setColumns(this.columns);
    return regresar;
  }

}
