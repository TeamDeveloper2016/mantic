package mx.org.kaana.libs.facturama.models;

public class Csd {

	private String Rfc;
	private String Certificate;
	private String PrivateKey;
	private String PrivateKeyPassword;

	public String getCertificate() {
		return Certificate;
	}

	public void setCertificate(String Certificate) {
		this.Certificate = Certificate;
	}

	public String getRfc() {
		return Rfc;
	}

	public void setRfc(String Rfc) {
		this.Rfc = Rfc;
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

}
