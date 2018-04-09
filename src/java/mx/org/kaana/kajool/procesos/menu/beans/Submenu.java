package mx.org.kaana.kajool.procesos.menu.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date Aug 31, 2015
 *@time 10:19:39 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Submenu implements Serializable{

	private static final long serialVersionUID=-6879912922140154640L;
	private String href;
	private String claseAlinear;
	private String claseIcono;
	private String label;
	private String onStart;
	private List<DetalleSubmenu> detalle;
	
	public Submenu(){
		this("#", "", "", "", "");
	}

	public Submenu(String href, String claseAlinear, String claseIcono, String label, String onStart) {
		this.href=href;
		this.claseAlinear=claseAlinear;
		this.claseIcono=claseIcono;
		this.label=label;
		this.onStart= onStart;
		this.detalle= new ArrayList<>();
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href=href;
	}

	public String getClaseAlinear() {
		return claseAlinear;
	}

	public void setClaseAlinear(String claseAlinear) {
		this.claseAlinear=claseAlinear;
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

	public String getOnStart() {
		return onStart;
	}

	public void setOnStart(String onStart) {
		this.onStart=onStart;
	}

	public List<DetalleSubmenu> getDetalle() {
		return detalle;
	}

	public void setDetalle(List<DetalleSubmenu> detalle) {
		this.detalle=detalle;
	}
	
}
