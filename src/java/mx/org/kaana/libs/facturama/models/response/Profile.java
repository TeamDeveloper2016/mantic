package mx.org.kaana.libs.facturama.models.response;

import mx.org.kaana.libs.facturama.models.Address;
import mx.org.kaana.libs.facturama.models.Csd;

public class Profile {

	private String UserName;
	private String Email;
	private String ContactPhone;
	private boolean HasRegistered;
	private String LoginProvider;
	private String FiscalRegime;
	private String Rfc;
	private String TaxName;
	private Address TaxAddress;

	public String getUserName() {
		return UserName;
	}

	public void setUserName(String UserName) {
		this.UserName = UserName;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String Email) {
		this.Email = Email;
	}

	public String getContactPhone() {
		return ContactPhone;
	}

	public void setContactPhone(String ContactPhone) {
		this.ContactPhone = ContactPhone;
	}

	public boolean getHasRegistered() {
		return HasRegistered;
	}

	public void setHasRegistered(boolean HasRegistered) {
		this.HasRegistered = HasRegistered;
	}

	public String getLoginProvider() {
		return LoginProvider;
	}

	public void setLoginProvider(String LoginProvider) {
		this.LoginProvider = LoginProvider;
	}

	public String getFiscalRegime() {
		return FiscalRegime;
	}

	public void setFiscalRegime(String FiscalRegime) {
		this.FiscalRegime = FiscalRegime;
	}

	public String getTaxName() {
		return TaxName;
	}

	public void setTaxName(String TaxName) {
		this.TaxName = TaxName;
	}

	public Address getTaxAddress() {
		return TaxAddress;
	}

	public void setTaxAddress(Address TaxAddress) {
		this.TaxAddress = TaxAddress;
	}

}
