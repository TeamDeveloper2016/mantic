package mx.org.kaana.libs.facturama.models.response;

import java.util.Date;

public class Suscription {

	private String Plan;
	private String RefInvoiceId;
	private String CardId;
	private String CurrentFolios;
	private Date CreationDate;
	private Date ExpirationDate;
	private Double Amount;

	public String getPlan() {
		return Plan;
	}

	public void setPlan(String Plan) {
		this.Plan = Plan;
	}

	public String getRefInvoiceId() {
		return RefInvoiceId;
	}

	public void setRefInvoiceId(String RefInvoiceId) {
		this.RefInvoiceId = RefInvoiceId;
	}

	public String getCardId() {
		return CardId;
	}

	public void setCardId(String CardId) {
		this.CardId = CardId;
	}

	public String getCurrentFolios() {
		return CurrentFolios;
	}

	public void setCurrentFolios(String CurrentFolios) {
		this.CurrentFolios = CurrentFolios;
	}

	public Date getCreationDate() {
		return CreationDate;
	}

	public void setCreationDate(Date CreationDate) {
		this.CreationDate = CreationDate;
	}

	public Date getExpirationDate() {
		return ExpirationDate;
	}

	public void setExpirationDate(Date ExpirationDate) {
		this.ExpirationDate = ExpirationDate;
	}

	public Double getAmount() {
		return Amount;
	}

	public void setAmount(Double Amount) {
		this.Amount = Amount;
	}

}
