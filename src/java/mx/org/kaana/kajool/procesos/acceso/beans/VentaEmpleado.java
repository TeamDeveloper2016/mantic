package mx.org.kaana.kajool.procesos.acceso.beans;

import java.io.Serializable;
import java.util.Random;

public class VentaEmpleado implements Serializable{

	private static final long serialVersionUID = -5849415278178158212L;	
	private Long idKey;
	private String nombre;
	private Long total;
	private Double porcentaje;
	private String style;
	
	public VentaEmpleado() {
		this(-1L, "", 0L, 0D);
	}
	
	public VentaEmpleado(Long idKey, String nombre, Long total, Double porcentaje) {
		this.idKey     = idKey;
		this.nombre    = nombre;
		this.total     = total;
		this.porcentaje= porcentaje;
	}

	public Long getIdKey() {
		return idKey;
	}

	public void setIdKey(Long idKey) {
		this.idKey = idKey;
	}	
	
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public Double getPorcentaje() {
		return porcentaje;
	}

	public void setPorcentaje(Double porcentaje) {
		this.porcentaje = porcentaje;
	}	

	public String getStyle() {
		return String.valueOf(new Random().nextInt((10-1)+1)+1);
	}	
}