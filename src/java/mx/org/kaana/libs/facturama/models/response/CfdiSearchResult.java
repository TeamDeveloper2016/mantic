package mx.org.kaana.libs.facturama.models.response;

import java.util.Calendar;
import java.util.Objects;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Fecha;

public class CfdiSearchResult implements Comparable<CfdiSearchResult> {

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
	private Calendar timbrado;

	public CfdiSearchResult() {
		this("");
	}

	public CfdiSearchResult(String Id) {
		this.Id=Id;
	}

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
		if(!Cadena.isVacio(Date))
			this.timbrado= Fecha.toCalendar(Date.substring(0, 10), Date.substring(11, 19));
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

	public Calendar getTimbrado() {
		return timbrado;
	}

	public void setTimbrado(Calendar timbrado) {
		this.timbrado=timbrado;
	}

	@Override
	public int hashCode() {
		int hash=3;
		hash=97*hash+Objects.hashCode(this.Id);
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
		final CfdiSearchResult other=(CfdiSearchResult) obj;
		if (!Objects.equals(this.Id, other.Id)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "CfdiSearchResult{"+"Id="+Id+", Folio="+Folio+", Serie="+Serie+", TaxName="+TaxName+", Rfc="+Rfc+", Date="+Date+", Total="+Total+", Uuid="+Uuid+", Email="+Email+", IsActive="+IsActive+", EmailSent="+EmailSent+'}';
	}

	@Override
	public int compareTo(CfdiSearchResult compare) {
		if(this.timbrado== null)
			this.timbrado= Fecha.toCalendar(this.getDate().substring(0, 10), this.getDate().substring(11, 19));
		if(compare.timbrado== null)
			compare.timbrado= Fecha.toCalendar(compare.getDate().substring(0, 10), compare.getDate().substring(11, 19));
		//ascending order
		// return  compare.getTimbrado().compareTo(this.getTimbrado());
		
		//descending order
		return compare.getDate().compareTo(this.getDate());
	}
	
}
