package mx.org.kaana.libs.facturama.models.response.catalogs.cfdi;

import mx.org.kaana.libs.facturama.models.response.catalogs.Catalog;

public class PostalCode extends Catalog {

	private String StateCode;
	private String MunicipalityCode;
	private String LocationCode;

	public String getStateCode() {
		return StateCode;
	}

	public void setStateCode(String StateCode) {
		this.StateCode = StateCode;
	}

	public String getMunicipalityCode() {
		return MunicipalityCode;
	}

	public void setMunicipalityCode(String MunicipalityCode) {
		this.MunicipalityCode = MunicipalityCode;
	}

	public String getLocationCode() {
		return LocationCode;
	}

	public void setLocationCode(String LocationCode) {
		this.LocationCode = LocationCode;
	}

}
