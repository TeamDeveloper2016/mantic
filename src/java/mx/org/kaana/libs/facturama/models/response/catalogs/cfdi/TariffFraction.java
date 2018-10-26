package mx.org.kaana.libs.facturama.models.response.catalogs.cfdi;

import mx.org.kaana.libs.facturama.models.response.catalogs.Catalog;

public class TariffFraction extends Catalog {

	private String StartDate;
	private String EndDate;
	private String Crtierion;
	private String CustomUnit;

	public String getStartDate() {
		return StartDate;
	}

	public void setStartDate(String StartDate) {
		this.StartDate = StartDate;
	}

	public String getEndDate() {
		return EndDate;
	}

	public void setEndDate(String EndDate) {
		this.EndDate = EndDate;
	}

	public String getCrtierion() {
		return Crtierion;
	}

	public void setCrtierion(String Crtierion) {
		this.Crtierion = Crtierion;
	}

	public String getCustomUnit() {
		return CustomUnit;
	}

	public void setCustomUnit(String CustomUnit) {
		this.CustomUnit = CustomUnit;
	}
}
