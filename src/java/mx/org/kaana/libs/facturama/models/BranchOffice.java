package mx.org.kaana.libs.facturama.models;

public class BranchOffice {

	private String Id;
	private String Name;
	private String Description;
	private Address Address;

	public String getId() {
		return Id;
	}

	public void setId(String Id) {
		this.Id = Id;
	}

	public String getName() {
		return Name;
	}

	public void setName(String Name) {
		this.Name = Name;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String Description) {
		this.Description = Description;
	}

	public Address getAddress() {
		return Address;
	}

	public void setAddress(Address Address) {
		this.Address = Address;
	}

}
