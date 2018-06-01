package mx.org.kaana.kajool.procesos.acceso.menu.motor;

import com.coolservlets.beans.tree.Tree;
import com.coolservlets.beans.tree.TreeNode;
import com.coolservlets.beans.tree.TreeObject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.kajool.procesos.acceso.menu.reglas.IBaseMenu;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date Sep 2, 2015
 *@time 10:29:08 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Mmenu extends IBaseMenu implements Serializable{
	
	private static final long serialVersionUID=-4403467129541932635L;
	
	public Mmenu(Tree root){
		super(root);
	} // Mmenu

	@Override
	public Object toBuild() {
		try {
			if(JsfBase.getAutentifica().getCredenciales().isMenuEncabezado()){
				getMenu().append("<ul>");
				generar(getRoot()!= null? getRoot().loadChildren(): new ArrayList<>());
				getMenu().append("</ul>");
			} // if
		} // try
		catch (Exception e) {
			mx.org.kaana.libs.formato.Error.mensaje(e);
		} // catch
		return getMenu().toString();
	}
	
	private void generar(List<TreeObject> list) throws Exception {
		try {
			for (TreeObject treeObject : list) {
				if (treeObject.getType()==Tree.LEAF) {					
					getMenu().append("<li><a style=\"font-size: 18px;font-family: titillium_webregular;\" href= \"");
					getMenu().append(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath());
					getMenu().append(treeObject.getUrl());
					getMenu().append("\"><i class=\"");
					getMenu().append(treeObject.getIcono());
					getMenu().append(" janal-mmenu-click\">&nbsp;&nbsp;</i>");
					getMenu().append(treeObject.getName());
					getMenu().append("</a></li>");
				} // if
				else {
					getMenu().append("<li><a style=\"font-size: 18px;font-family: titillium_webregular;\"><i class=\"");
					getMenu().append(treeObject.getIcono());
					getMenu().append("\">&nbsp;&nbsp;</i>");
					getMenu().append(treeObject.getName());
					getMenu().append("</a><ul>");
					TreeNode treeNode=(TreeNode) treeObject;
					generar(getRoot().getChildren(treeNode.getChildren()));
					getMenu().append("</ul></li>");
				} // else
			} // for
		} // try
		catch (Exception e) {
			throw e;
		} // catch
	} // generar
}
