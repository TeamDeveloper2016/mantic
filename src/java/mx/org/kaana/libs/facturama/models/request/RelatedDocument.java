package mx.org.kaana.libs.facturama.models.request;

public class RelatedDocument {

	private String Uuid;
	private String Serie;
	private String Folio;
	private String Currency;
	private Double ExchangeRate;
	private String PaymentMethod;
	private Integer PartialityNumber;
	private Double PreviousBalanceAmount;
	private Double AmountPaid;

	public String getUuid() {
		return Uuid;
	}

	public void setUuid(String Uuid) {
		this.Uuid = Uuid;
	}

	public String getSerie() {
		return Serie;
	}

	public void setSerie(String Serie) {
		this.Serie = Serie;
	}

	public String getFolio() {
		return Folio;
	}

	public void setFolio(String Folio) {
		this.Folio = Folio;
	}

	public String getCurrency() {
		return Currency;
	}

	public void setCurrency(String Currency) {
		this.Currency = Currency;
	}

	public Double getExchangeRate() {
		return ExchangeRate;
	}

	public void setExchageRate(Double ExchangeRate) {
		this.ExchangeRate = ExchangeRate;
	}

	public String getPaymentMethod() {
		return PaymentMethod;
	}

	public void setPaymentMethod(String PaymentMethod) {
		this.PaymentMethod = PaymentMethod;
	}

	public Integer getPartialityNumber() {
		return PartialityNumber;
	}

	public void setPartialityNumber(Integer PartialityNumber) {
		this.PartialityNumber = PartialityNumber;
	}

	public Double getPreviousBalanceAmount() {
		return PreviousBalanceAmount;
	}

	public void setPreviousBalanceAmount(Double PreviousBalanceAmount) {
		this.PreviousBalanceAmount = PreviousBalanceAmount;
	}

	public Double getAmountPaid() {
		return AmountPaid;
	}

	public void setAmountPaid(Double AmountPaid) {
		this.AmountPaid = AmountPaid;
	}

}
