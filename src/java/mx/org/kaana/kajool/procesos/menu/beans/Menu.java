package mx.org.kaana.kajool.procesos.menu.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date Aug 31, 2015
 *@time 10:20:40 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Menu implements Serializable{

	private static final long serialVersionUID=6061079778618711760L;
	
	private String label;
	private String claseIcono;
	private List<Submenu> subMenu;
	
	public Menu(){
		this("", "");
	}

	public Menu(String label, String claseIcono) {
		this.label= label;
		this.claseIcono=claseIcono;
		this.subMenu= new ArrayList<>();
	}

	public String getClaseIcono() {
		return claseIcono;
	}

	public void setClaseIcono(String claseIcono) {
		this.claseIcono=claseIcono;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label=label;
	}

	public List<Submenu> getSubMenu() {
		return subMenu;
	}

	public void setSubMenu(List<Submenu> subMenu) {
		this.subMenu=subMenu;
	}
	
}
