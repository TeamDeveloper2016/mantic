package mx.org.kaana.kajool.enums;

import net.sf.dynamicreports.report.constant.HorizontalAlignment;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date Aug 22, 2012
 *@time 8:51:49 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public enum EAlineacion {
	
  IZQUIERDA("Izquierda", HorizontalAlignment.LEFT), CENTRAR("Centrar", HorizontalAlignment.CENTER), DERECHA("Derecha", HorizontalAlignment.RIGHT), JUSITIFICADO("Justificado", HorizontalAlignment.JUSTIFIED);
	
	private String name;
	private HorizontalAlignment align;

	private EAlineacion(String name, HorizontalAlignment align) {
		this.name=name;
		this.align=align;
	}

	public HorizontalAlignment getAlign() {
		return align;
	}

	public String getName() {
		return name;
	}
	
}
