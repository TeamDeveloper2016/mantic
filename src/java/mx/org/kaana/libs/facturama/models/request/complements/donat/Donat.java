package mx.org.kaana.libs.facturama.models.request.complements.donat;

public class Donat {

	private String AuthorizationNumber;
	private String AuthorizationDate;
	private String Legend;

	public String getAuthorizationNumber() {
		return AuthorizationNumber;
	}

	public void setAuthorizationNumber(String AuthorizationNumber) {
		this.AuthorizationNumber = AuthorizationNumber;
	}

	public String getAuthorizationDate() {
		return AuthorizationDate;
	}

	public void setAuthorizationDate(String AuthorizationDate) {
		this.AuthorizationDate = AuthorizationDate;
	}

	public String getLegend() {
		return Legend;
	}

	public void setLegend(String Legend) {
		this.Legend = Legend;
	}

}
