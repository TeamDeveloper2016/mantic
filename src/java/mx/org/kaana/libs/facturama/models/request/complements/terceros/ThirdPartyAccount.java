package mx.org.kaana.libs.facturama.models.request.complements.terceros;

import java.util.List;

public class ThirdPartyAccount {

	private String Rfc;
	private String Name;
	private Address ThirdTaxInformation;
	private CustomsInformation CustomsInformation;
	private List<Part> Parts;
	private String PropertyTaxNumber;
	private List<ThirdPartyAccountTax> Taxes;

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

	public Address getThirdTaxInformation() {
		return ThirdTaxInformation;
	}

	public void setThirdTaxInformation(Address ThirdTaxInformation) {
		this.ThirdTaxInformation = ThirdTaxInformation;
	}

	public CustomsInformation getCustomsInformation() {
		return CustomsInformation;
	}

	public void setCustomsInformation(CustomsInformation CustomsInformation) {
		this.CustomsInformation = CustomsInformation;
	}

	public List<Part> getParts() {
		return Parts;
	}

	public void setParts(List<Part> Parts) {
		this.Parts = Parts;
	}

	public String PropertyTaxNumber() {
		return PropertyTaxNumber;
	}

	public void setPropertyTaxNumber(String PropertyTaxNumber) {
		this.PropertyTaxNumber = PropertyTaxNumber;
	}

	public List<ThirdPartyAccountTax> getTaxes() {
		return Taxes;
	}

	public void setTaxes(List<ThirdPartyAccountTax> Taxes) {
		this.Taxes = Taxes;
	}

}
