package mx.org.kaana.kajool.procesos.menu.beans;

import java.io.Serializable;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date Aug 31, 2015
 *@time 10:17:47 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class DetalleSubmenu implements Serializable{

	private static final long serialVersionUID=-6500512512231486493L;
	private String label;
	private String claseFont;
	
	public DetalleSubmenu(){
		this("", "");
	}

	public DetalleSubmenu(String label, String claseFont) {
		this.label=label;
		this.claseFont=claseFont;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label=label;
	}

	public String getClaseFont() {
		return claseFont;
	}

	public void setClaseFont(String claseFont) {
		this.claseFont=claseFont;
	}
	
}
