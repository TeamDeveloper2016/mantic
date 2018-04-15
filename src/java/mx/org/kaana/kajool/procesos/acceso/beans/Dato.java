
package mx.org.kaana.kajool.procesos.acceso.beans;

import java.io.Serializable;

public class Dato implements Serializable{
	
	public Long id;
	public String nombre;
	public Long clientes;

	public Dato(Long id, String nombre, Long clientes) {
		this.id = id;
		this.nombre = nombre;
		this.clientes = clientes;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Long getClientes() {
		return clientes;
	}

	public void setClientes(Long clientes) {
		this.clientes = clientes;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
      return false;
    }
    if (this.getClass() != obj.getClass()) {
      return false;
    }
    Dato other = (Dato) obj;
    if (this.id == null ? other.id != null : !this.id.equals(other.id)) {
      return false;
    }
    return true;
	}

	@Override
	public int hashCode() {
		int hash = 7;
    hash = 59 * hash + (this.id != null ? this.id.hashCode() : 0);
    return hash;
	}
	
	
}
