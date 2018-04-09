package mx.org.kaana.kajool.mantenimiento.gestion.asistente.filtros.beans;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 22/12/2014
 *@time 03:11:54 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Parametro {

	private String nombre;
	private Object valor;

	public Parametro() {}

	public Parametro(String nombre) {
		this.nombre=nombre;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre=nombre;
	}

	public Object getValor() {
		return valor;
	}

	public void setValor(Object valor) {
		this.valor=valor;
	}
}
