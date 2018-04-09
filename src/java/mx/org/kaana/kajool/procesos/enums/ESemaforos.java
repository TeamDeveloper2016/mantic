package mx.org.kaana.kajool.procesos.enums;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 12/10/2016
 *@time 03:27:04 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public enum ESemaforos {

  AMARILLO ("circulo-amarillo"),
	AZUL     ("circulo-azul"),	
	CAFE     ("circulo-cafe"),
	GRIS     ("circulo-gris"),
	LILA     ("circulo-lila"),
	NARANJA  ("circulo-naranja"),
	ROJO     ("circulo-rojo"),
	VERDE    ("circulo-verde"),;
	
	private String nombre;
	
	private ESemaforos(String nombre) {
		this.nombre= nombre;
	}	

	public Long getKey(){
		return new Long(this.ordinal() + 1);
	}
	
	public String getNombre() {
		return nombre;
	}	
}
