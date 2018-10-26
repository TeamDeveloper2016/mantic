package mx.org.kaana.libs.facturama.models;

import java.util.Date;

public class TaxEnityCsd {

	public String Rfc;
	public String Certificate;
	public String PrivateKey;
	public String PrivateKeyPassword;
	public Date UploadDate;

	public String getRfc() {
		return Rfc;
	}

	public void setRfc(String Rfc) {
		this.Rfc = Rfc;
	}

	public String getCertificate() {
		return Certificate;
	}

	public void setCertificate(String Certificate) {
		this.Certificate = Certificate;
	}

	public String getPrivateKey() {
		return PrivateKey;
	}

	public void setPrivateKey(String PrivateKey) {
		this.PrivateKey = PrivateKey;
	}

	public String getPrivateKeyPassword() {
		return PrivateKeyPassword;
	}

	public void setPrivateKeyPassword(String PrivateKeyPassword) {
		this.PrivateKeyPassword = PrivateKeyPassword;
	}

	public Date getUploadDate() {
		return UploadDate;
	}

	public void setUploadDate(Date UploadDate) {
		this.UploadDate = UploadDate;
	}

}
