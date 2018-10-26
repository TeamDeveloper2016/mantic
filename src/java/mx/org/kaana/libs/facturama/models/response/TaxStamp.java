package mx.org.kaana.libs.facturama.models.response;

public class TaxStamp {

	private String Uuid;
	private String Date;
	private String CfdiSign;
	private String SatCertNumber;
	private String SatSign;

	public String getUuid() {
		return Uuid;
	}

	public void setUuid(String Uuid) {
		this.Uuid = Uuid;
	}

	public String getDate() {
		return Date;
	}

	public void setDate(String Date) {
		this.Date = Date;
	}

	public String getCfdiSign() {
		return CfdiSign;
	}

	public void setCfdiSign(String CfdiSign) {
		this.CfdiSign = CfdiSign;
	}

	public String getSatCertNumber() {
		return SatCertNumber;
	}

	public void setSatCertNumber(String SatCertNumber) {
		this.SatCertNumber = SatCertNumber;
	}

	public String getSatSign() {
		return SatSign;
	}

	public void setSatSign(String SatSign) {
		this.SatSign = SatSign;
	}
}
