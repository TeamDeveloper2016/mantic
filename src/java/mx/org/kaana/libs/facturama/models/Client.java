package mx.org.kaana.libs.facturama.models;

import java.util.Objects;

public class Client {

	private String Id;
	private String Email;
	private String EmailOp1;
	private String EmailOp2;
	private Address Address;
	private String Rfc;
	private String Name;
	private String CfdiUse;
	private String TaxResidence;
	private String NumRegIdTrib;

	public Client() {
		this("");
	}

	public Client(String Rfc) {
		this.Rfc=Rfc;
	}

	public String getId() {
		return Id;
	}

	public void setId(String Id) {
		this.Id = Id;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String Email) {
		this.Email = Email;
	}

	public String getEmailOp1() {
		return EmailOp1;
	}

	public void setEmailOp1(String EmailOp1) {
		this.EmailOp1 = EmailOp1;
	}

	public String getEmailOp2() {
		return EmailOp2;
	}

	public void setEmailOp2(String EmailOp2) {
		this.EmailOp2 = EmailOp2;
	}

	public Address getAddress() {
		return Address;
	}

	public void setAddress(Address Address) {
		this.Address = Address;
	}

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

	public String getCfdiUse() {
		return CfdiUse;
	}

	public void setCfdiUse(String CfdiUse) {
		this.CfdiUse = CfdiUse;
	}

	public String getTaxResidence() {
		return TaxResidence;
	}

	public void setTaxResidence(String TaxResidence) {
		this.TaxResidence = TaxResidence;
	}

	public String getNumRegIdTrib() {
		return NumRegIdTrib;
	}

	public void setNumRegIdTrib(String NumRegIdTrib) {
		this.NumRegIdTrib = NumRegIdTrib;
	}

	@Override
	public int hashCode() {
		int hash=5;
		hash=29*hash+Objects.hashCode(this.Rfc);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this==obj) {
			return true;
		}
		if (obj==null) {
			return false;
		}
		if (getClass()!=obj.getClass()) {
			return false;
		}
		final Client other=(Client) obj;
		if (!Objects.equals(this.Rfc, other.Rfc)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Client{"+"Id="+Id+", Email="+Email+", EmailOp1="+EmailOp1+", EmailOp2="+EmailOp2+", Address="+Address+", Rfc="+Rfc+", Name="+Name+", CfdiUse="+CfdiUse+", TaxResidence="+TaxResidence+", NumRegIdTrib="+NumRegIdTrib+'}';
	}

}
