package mx.org.kaana.kajool.procesos.menu.reglas;


import com.coolservlets.beans.menu.MenuModel;
import java.io.Serializable;
import java.util.List;
import mx.org.kaana.kajool.procesos.menu.beans.DetalleSubmenu;
import mx.org.kaana.kajool.procesos.menu.beans.Menu;
import mx.org.kaana.kajool.procesos.menu.beans.Submenu;
import org.primefaces.component.menuitem.UIMenuItem;
import org.primefaces.component.submenu.UISubmenu;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date Aug 31, 2015
 *@time 10:22:25 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Generador implements Serializable{

	private static final long serialVersionUID=3453324242200577894L;
	
	public MenuModel construirLeftMenu(List<Menu> listaMenu){
		MenuModel regresar= new MenuModel();
		try {
			for(Menu elemento: listaMenu){
				if(elemento.getSubMenu().isEmpty()){
					UIMenuItem padre= new UIMenuItem();
					padre.setValue(elemento.getLabel());
					padre.setIcon(elemento.getClaseIcono());
					regresar.addElement(padre);
				} // if
				else{
					UISubmenu padre= new UISubmenu();
					padre.setLabel(elemento.getLabel());
					padre.setIcon(elemento.getClaseIcono());
					for(Submenu item: elemento.getSubMenu()){
						UIMenuItem hijo= new UIMenuItem();
						hijo.setValue(item.getLabel());
						hijo.setIcon(item.getClaseIcono());
						padre.getChildren().add(hijo);
					} // for
					regresar.addElement(padre);
				} // else
			} // for
		} // try
		catch (Exception e) {
			throw e;
		} // catch
		return regresar;
	}
	
	public String generarMenu(List<Menu> listaMenu){
		StringBuilder regresar= new StringBuilder();
		try {
			for(Menu menu: listaMenu){
				regresar.append("<li class=\"Fleft BordRadHalf TexAlCenter\"><i class=\"");
				regresar.append(menu.getClaseIcono());
				regresar.append("\"></i>");
				regresar.append(generarSubMenu(menu.getSubMenu()));
				regresar.append("</li>");
			} // for Menu
		} // try
		catch (Exception e) {
			throw e;
		} // catch
		return regresar.toString();
	}
	
	private String generarSubMenu(List<Submenu> listaSubMenu){
		StringBuilder regresar= new StringBuilder();
		try {
			regresar.append("<ul class=\"layout-header-widgets-submenu BordRad5 shadows white Animated05\">");
			for(Submenu subMenu: listaSubMenu){
				regresar.append("<li class=\"Animated05\">");
				if(!subMenu.getDetalle().isEmpty()){
					regresar.append("<a class=\"white Unselectable\" onclick=\"");
					regresar.append(subMenu.getOnStart());
					regresar.append("\"><i class=\"");
					regresar.append(subMenu.getClaseIcono());
					regresar.append(" Fs26 OvHidden\"></i>");
					regresar.append(generarDetalleSubMenu(subMenu.getDetalle()));
					regresar.append("</a></li>");
				} // if
				else{
					regresar.append("<a href=\"");
					regresar.append(subMenu.getHref());
					regresar.append("\" class=\"white Unselectable ");
					regresar.append(subMenu.getClaseAlinear());
					regresar.append("\" onclick=\"");
					regresar.append(subMenu.getOnStart());
					regresar.append("\">");
					regresar.append("<i class=\"");
					regresar.append(subMenu.getClaseIcono());
					regresar.append("\"></i>");
					regresar.append(subMenu.getLabel());
					regresar.append("</a></li>");
				} // else
			} // for subMenu
			regresar.append("</ul>");
		} // try
		catch (Exception e) {
			throw e;
		} // catch
		return regresar.toString();
	}
	
	private String generarDetalleSubMenu(List<DetalleSubmenu> listaDetalle){
		StringBuilder regresar= new StringBuilder();
		try {
			for(DetalleSubmenu detalle: listaDetalle){
				regresar.append("<span class=\"");
				regresar.append(detalle.getClaseFont());
				regresar.append("\">");
				regresar.append(detalle.getLabel());
				regresar.append("</span><br/>");
			} // for
		} // try
		catch (Exception e) {
			throw e;
		} // catch
		return regresar.toString();
	}
	
}
