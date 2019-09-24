package mx.org.kaana.mantic.catalogos.articulos.beans;

import java.io.Serializable;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 23/09/2019
 *@time 09:02:26 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public final class Menudeo extends PrecioVenta implements Serializable {

	private static final long serialVersionUID=-5902307046480075898L;

	public Menudeo(Double costo, Long idRedondear, Double precio) {
		super(costo, idRedondear, precio, 50D);
	}
	
}
