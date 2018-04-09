package mx.org.kaana.kajool.procesos.acceso.menu;

import com.coolservlets.beans.TreeBean;
import com.coolservlets.beans.tree.Tree;
import java.io.Serializable;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.kajool.enums.EMenus;
import static mx.org.kaana.kajool.enums.EMenus.ENCABEZADO;
import static mx.org.kaana.kajool.enums.EMenus.MMENU;
import static mx.org.kaana.kajool.enums.EMenus.SENTINEL;
import mx.org.kaana.kajool.procesos.acceso.menu.motor.Mmenu;
import mx.org.kaana.kajool.procesos.acceso.menu.motor.Sentinel;
import mx.org.kaana.kajool.procesos.acceso.menu.motor.SentinelLateral;
import mx.org.kaana.kajool.procesos.acceso.menu.reglas.IBaseMenu;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date Sep 2, 2015
 * @time 8:58:38 AM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public class FactoryMenu implements Serializable {

  private static final long serialVersionUID = 395257279676083941L;
  private static final Log LOG = LogFactory.getLog(FactoryMenu.class);

  public IBaseMenu toMenu(EMenus menu) throws Exception {
    IBaseMenu regresar = null;
    Tree treebean = null;
    try {
      LOG.info("Procesando la carga del menu solicitado [".concat(menu.name()).concat("]"));
      if (JsfBase.getSession().getAttribute("tree") != null) {
        treebean = ((TreeBean) JsfBase.getSession().getAttribute("tree")).getRoot();
      }
      switch (menu) {
        case SENTINEL:
          regresar = new SentinelLateral(treebean);
          break;
        case ENCABEZADO:
          if (JsfBase.getSession().getAttribute("treeEncabezado") != null) {
            treebean = ((TreeBean) JsfBase.getSession().getAttribute("treeEncabezado")).getRoot();
          }
          regresar = new Sentinel(treebean);
          break;
        case MMENU:
          regresar = new Mmenu(treebean);
          break;
      }// switch
    }// try
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  }
}
