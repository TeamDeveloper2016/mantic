package mx.org.kaana.libs.facturama.models.request;

import mx.org.kaana.libs.facturama.models.Address;

public class TaxEnity {

	private String FiscalRegime;
	private String ComercialName;
	private String Rfc;
	private String TaxName;
	private String Email;
	private String Phone;
	private Address TaxAddress;
	private String PasswordSat;

	public String getFiscalRegime() {
		return FiscalRegime;
	}

	public void set(String FiscalRegime) {
		this.FiscalRegime = FiscalRegime;
	}

	public String getComercialName() {
		return ComercialName;
	}

	public void setComercialName(String ComercialName) {
		this.ComercialName = ComercialName;
	}

	public String getRfc() {
		return Rfc;
	}

	public void setRfc(String Rfc) {
		this.Rfc = Rfc;
	}

	public String getTaxName() {
		return TaxName;
	}

	public void setTaxName(String TaxName) {
		this.TaxName = TaxName;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String Email) {
		this.Email = Email;
	}

	public String getPhone() {
		return Phone;
	}

	public void setPhone(String Phone) {
		this.Phone = Phone;
	}

	public Address getTaxAddress() {
		return TaxAddress;
	}

	public void setTaxAddress(Address TaxAddress) {
		this.TaxAddress = TaxAddress;
	}

	public String getPasswordSat() {
		return PasswordSat;
	}

	public void setPasswordSat(String PasswordSat) {
		this.PasswordSat = PasswordSat;
	}

}
