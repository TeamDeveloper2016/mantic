package mx.org.kaana.mantic.productos.backing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.pagina.IBaseFilter;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 16/10/2021
 *@time 08:44:23 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public abstract class Contenedor extends IBaseFilter implements Serializable {

  private static final long serialVersionUID = -4986338657389855301L;

  private TreeNode root;

  public TreeNode getRoot() {
    return root;
  }

  public void doLoad(String father, Long level, TreeNode dad) {
    List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
    try {
      columns = new ArrayList<>();
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      params.put("nivel", level);
      params.put("padre", father);
      List<UISelectEntity> items= (List<UISelectEntity>)UIEntity.build("TcManticProductosCategoriasDto", "hijos", params, columns);
      if(dad== null) {
        if(items!= null && !items.isEmpty()) {
          this.root= new DefaultTreeNode(items.get(0), null); 
          this.doLoad(items.get(0).toString("nombre").concat(Constantes.SEPARADOR), items.get(0).toLong("nivel")+ 1L, this.root);
        } // if
      } // if
      else {
        if(items!= null && !items.isEmpty()) 
          for (UISelectEntity item : items) {
            TreeNode child= new DefaultTreeNode(item, dad);
            this.doLoad(item.toString("padre").concat(item.toString("nombre")).concat(Constantes.SEPARADOR), item.toLong("nivel")+ 1L, child);
          } // for
      } // else
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    finally {
      Methods.clean(columns);
    } // finally		
  } // doLoad
  
}
