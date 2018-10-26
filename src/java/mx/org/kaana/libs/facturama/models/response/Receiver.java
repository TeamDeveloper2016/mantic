package mx.org.kaana.libs.facturama.models.response;

import mx.org.kaana.libs.facturama.models.Address;

public class Receiver {

	private Address Address;
	private String Rfc;
	private String Name;
	private String Email;

	public String getRfc() {
		return Rfc;
	}

	public void setRfc(String Rfc) {
		this.Rfc = Rfc;
	}

	public String getName() {
		return Name;
	}

	public void setName(String Name) {
		this.Name = Name;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String Email) {
		this.Email = Email;
	}

	public Address getAddress() {
		return Address;
	}

	public void setAddress(Address Address) {
		this.Address = Address;
	}

}
