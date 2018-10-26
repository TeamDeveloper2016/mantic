package mx.org.kaana.libs.facturama.models.request.complements.terceros;

public class Address {

	private String Street;
	private String ExteriorNumber;
	private String InteriorNumber;
	private String Neighborhood;
	private String Locality;
	private String Reference;
	private String Municipality;
	private String State;
	private String Country;
	private String PostalCode;

	public String getStreet() {
		return Street;
	}

	public void setStreet(String Street) {
		this.Street = Street;
	}

	public String getExteriorNumber() {
		return ExteriorNumber;
	}

	public void setExteriorNumber(String ExteriorNumber) {
		this.ExteriorNumber = ExteriorNumber;
	}

	public String getInteriorNumber() {
		return InteriorNumber;
	}

	public void setInteriorNumber(String InteriorNumber) {
		this.InteriorNumber = InteriorNumber;
	}

	public String getNeighborhood() {
		return Neighborhood;
	}

	public void setNeighborhood(String Neighborhood) {
		this.Neighborhood = Neighborhood;
	}

	public String getReference() {
		return Reference;
	}

	public void setReference(String Reference) {
		this.Reference = Reference;
	}

	public String getLocality() {
		return Locality;
	}

	public void setLocality(String Locality) {
		this.Locality = Locality;
	}

	public String getMunicipality() {
		return Municipality;
	}

	public void setMunicipality(String Municipality) {
		this.Municipality = Municipality;
	}

	public String getState() {
		return State;
	}

	public void setState(String State) {
		this.State = State;
	}

	public String getCountry() {
		return Country;
	}

	public void setCountry(String Country) {
		this.Country = Country;
	}

	public String getPostalCode() {
		return PostalCode;
	}

	public void setPostalCode(String PostalCode) {
		this.PostalCode = PostalCode;
	}

}
