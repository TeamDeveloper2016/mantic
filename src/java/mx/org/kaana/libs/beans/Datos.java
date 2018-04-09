package mx.org.kaana.libs.beans;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 14/04/2015
 *@time 12:26:42 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Datos {
	private String curp;
	private boolean verdadero;
	private String texto;
	private String cadena;

	public Datos() {
	}

	public Datos(String curp, boolean verdadero, String texto, String cadena) {
		this.curp=curp;
		this.verdadero=verdadero;
		this.texto=texto;
		this.cadena=cadena;
	}

	public String getCurp() {
		return curp;
	}

	public void setCurp(String curp) {
		this.curp=curp;
	}

	public boolean isVerdadero() {
		return verdadero;
	}

	public void setVerdadero(boolean verdadero) {
		this.verdadero=verdadero;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto=texto;
	}

	public String getCadena() {
		return cadena;
	}

	public void setCadena(String cadena) {
		this.cadena=cadena;
	}
}
