package mx.org.kaana.kajool.procesos.acceso.menu.reglas;

import com.coolservlets.beans.tree.Tree;
import java.io.Serializable;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date Sep 1, 2015
 *@time 3:35:48 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public abstract class IBaseMenu implements Serializable{
	
	private static final long serialVersionUID=-6374080469200274429L;
	private Tree root;
	private StringBuilder menu;

	public Tree getRoot() {
		return root;
	}

	public void setRoot(Tree root) {
		this.root=root;
	}

	public StringBuilder getMenu() {
		return menu;
	}

	public void setMenu(StringBuilder menu) {
		this.menu=menu;
	}
	
	public IBaseMenu(Tree root){
		this.root= root;
		this.menu= new StringBuilder();
	}
	
	public abstract Object toBuild();

}
