package mx.org.kaana.kajool.procesos.acceso.menu.motor;

import com.coolservlets.beans.TreeBean;
import com.coolservlets.beans.menu.MenuModel;
import com.coolservlets.beans.tree.Tree;
import mx.org.kaana.kajool.procesos.acceso.menu.reglas.IBaseMenu;
import java.io.Serializable;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.formato.Error;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date Sep 1, 2015
 *@time 3:47:36 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class SentinelLateral extends IBaseMenu implements Serializable{
	
	private static final long serialVersionUID= -2994876049728627670L;
	
	public SentinelLateral(Tree tree){
		super(tree);
	} // SentinelLateral

	@Override
	public Object toBuild() {
		MenuModel regresar= null;
		try {
			if(JsfBase.getAutentifica().getCredenciales().isMenuEncabezado() && getRoot()!= null){
				getRoot().loadMenu();
				regresar= ((TreeBean)JsfBase.getSession().getAttribute("tree")).getRoot().getModel();
			} // if
		} // try
		catch (Exception e) {
			Error.mensaje(e);
		} // catch
		return regresar;
	} // toBuild
}
