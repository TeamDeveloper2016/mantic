package mx.org.kaana.mantic.taller.beans;

import java.io.Serializable;

public class ContactoCliente implements Serializable{

	private static final long serialVersionUID = -3029857560161510151L;
	private String telefono;
	private String email;

	public ContactoCliente() {
		this(null, null);
	} // ContactoCliente

	public ContactoCliente(String telefono, String email) {
		this.telefono= telefono;
		this.email   = email;
	}	// ContactoCliente

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}	
}
