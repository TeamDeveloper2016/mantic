package mx.org.kaana.kajool.procesos.mantenimiento.catalogos.backing;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.procesos.mantenimiento.catalogos.beans.Busqueda;
import mx.org.kaana.kajool.procesos.mantenimiento.catalogos.beans.Dinamicos;
import mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.Transaccion;
import mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.componentes.KajoolPanelGrid;
import mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.criterios.Criterio;
import mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.criterios.ICriterio;
import mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas.accion.IValue;
import org.primefaces.component.panelgrid.PanelGrid;

/**
 *
 * @project KAJOOL (Control system polls)
 * @company KAANA
 * @date 28/09/2015
 * @time 08:42:32 AM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
@ManagedBean(name = "kajoolDinamicosAccion")
@ViewScoped
public class Accion extends UIBase implements Serializable {

  private static final long serialVersionUID = -7485237769332813060L;

  private PanelGrid grid;
  private IBaseDto dto;
  private EAccion accion;

  @PostConstruct
  @Override
  protected void init() {
    try {
      this.grid = (PanelGrid) new KajoolPanelGrid("grid", 3).create();
      this.grid.setColumnClasses("janal-wid-10");
      this.attrs.put("idCatalogo", JsfBase.getFlashAttribute("idCatalogo"));
      this.attrs.put("idKey", JsfBase.getFlashAttribute("idKey"));
      this.accion   = (EAccion)JsfBase.getFlashAttribute("accion");
      this.attrs.put("buscar", new Busqueda(new Criterio("")));

//      this.attrs.put("idCatalogo", 1L);
//      this.attrs.put("idKey", 3531L);
//      this.accion   = EAccion.MODIFICAR;
      if(!Cadena.isVacio(this.attrs.get("idCatalogo"))) {
        this.setDinamico(Dinamicos.getInstance().add((Long)this.attrs.get("idCatalogo")));
        this.dto     = DaoFactory.getInstance().loadDTO(this.getDinamico().getNombre());
        switch (this.accion) {
          case AGREGAR:
            this.setCriterios(this.getDinamico().getCapturar());
            break;
          case MODIFICAR:
            this.setCriterios(this.getDinamico().getModificar());
            this.dto= DaoFactory.getInstance().findById(this.dto.getClass(), (Long)this.attrs.get("idKey"));
            for (Criterio criterio: this.getCriterios())
              criterio.setValue(this.dto.toValue(criterio.getId()));
            break;
        } // switch
        this.toCreate();
      } // if
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
  } // init();

  public PanelGrid getGrid() {
    return grid;
  }

  public void setGrid(PanelGrid grid) {
    this.grid = grid;
  }

  public String getFields() {
    return this.getDinamico().toAccion();
  }

  private void toCreate() {
    this.grid.getChildren().clear();
    int count = 0;
    for (Criterio criterio : this.getCriterios()) {
      this.grid.getChildren().addAll(((ICriterio)criterio).create(Criterio.VALUE_ACTION.replace("index", String.valueOf(count))));
      count++;
    } // for
  } // toCreate

  public void doAceptar() {
    // Esto es para remplazar los parametros que se vienen arrastrando
    //Map<String, Object> params= new HashMap();
    //BeanUtilsBean utilsBean   = null;
    try {
      //utilsBean= new BeanUtilsBean(new ConvertUtilsBean(), new PropertyUtilsBean());
      //utilsBean.populate(this.dto, params);
      for (Criterio criterio: this.getCriterios())
        Methods.setValue(this.dto, criterio.getId(), new Object[] {((IValue)criterio).getValue()});
      Transaccion transaccion= new Transaccion(dto);
      transaccion.ejecutar(accion);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    finally {
      //Methods.clean(params);
    } // finally
  }

  @Override
  public void doLoad() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

}
