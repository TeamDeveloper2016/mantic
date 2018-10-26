package mx.org.kaana.libs.facturama.models.response;

public class CfdiSearchResult {

	private String Id;
	private String Folio;
	private String Serie;
	private String TaxName;
	private String Rfc;
	private String Date;
	private Double Total;
	private String Uuid;
	private String Email;
	private Boolean IsActive;
	private Boolean EmailSent;

	public String getId() {
		return Id;
	}

	public void setId(String Id) {
		this.Id = Id;
	}

	public String getFolio() {
		return Folio;
	}

	public void setFolio(String Folio) {
		this.Folio = Folio;
	}

	public String getSerie() {
		return Serie;
	}

	public void setSerie(String Serie) {
		this.Serie = Serie;
	}

	public String getTaxName() {
		return TaxName;
	}

	public void setTaxName(String TaxName) {
		this.TaxName = TaxName;
	}

	public String getRfc() {
		return Rfc;
	}

	public void setRfc(String Rfc) {
		this.Rfc = Rfc;
	}

	public String getDate() {
		return Date;
	}

	public void setDate(String Date) {
		this.Date = Date;
	}

	public Double getTotal() {
		return Total;
	}

	public void setTotal(double Total) {
		this.Total = Total;
	}

	public String getUuid() {
		return Uuid;
	}

	public void setUuid(String Uuid) {
		this.Uuid = Uuid;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String Email) {
		this.Email = Email;
	}

	public Boolean getIsActive() {
		return IsActive;
	}

	public void setIsActive(boolean IsActive) {
		this.IsActive = IsActive;
	}

	public Boolean getEmailSent() {
		return EmailSent;
	}

	public void setEmailSent(Boolean EmailSent) {
		this.EmailSent = EmailSent;
	}
}
