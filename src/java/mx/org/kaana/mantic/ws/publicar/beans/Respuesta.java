package mx.org.kaana.mantic.ws.publicar.beans;

import java.io.Serializable;
import mx.org.kaana.libs.formato.Fecha;

public class Respuesta implements Serializable{
	
	private static final long serialVersionUID = -7431056257630809729L;
	private String codigo;
	private String descripcion;
	private String nombre;
	private String ruta;
	private String fecha;

	public Respuesta(String codigo, String descripcion) {
		this(codigo, descripcion, "");
	}

	public Respuesta(String codigo, String descripcion, String nombre) {
		this(codigo, descripcion, nombre, "");
	}
	
	public Respuesta(String codigo, String descripcion, String nombre, String ruta) {
		this(codigo, descripcion, nombre, ruta, Fecha.formatear(Fecha.FECHA_HORA_CORTA));
	}
	
	public Respuesta(String codigo, String descripcion, String nombre, String ruta, String fecha) {
		this.codigo     = codigo;
		this.descripcion= descripcion;
		this.fecha      = fecha;
		this.nombre     = nombre;
		this.ruta       = ruta;
	}
	
	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}		

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}	

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}	

	public String getRuta() {
		return ruta;
	}

	public void setRuta(String ruta) {
		this.ruta = ruta;
	}	
}
