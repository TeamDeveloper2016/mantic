package mx.org.kaana.libs.facturama.models.request;

import java.util.List;

public class Payment {

	private List<RelatedDocument> RelatedDocument;
	private List<Tax> Taxes;
	private String Date;
	private String PaymentForm;
	private String Currency;
	private Double ExchangeRate;
	private Double Amount;
	private String OperationNumber;
	private String RfcIssuerPayerAccount;
	private String ForeignAccountNamePayer;
	private String PayerAccount;
	private String RfcReceiverBeneficiaryAccount;
	private String BeneficiaryAccount;

	public List<RelatedDocument> getRelatedDocument() {
		return RelatedDocument;
	}

	public void setRelatedDocument(List<RelatedDocument> RelatedDocument) {
		this.RelatedDocument = RelatedDocument;
	}

	public List<Tax> getTaxes() {
		return Taxes;
	}

	public void setTaxes(List<Tax> Taxes) {
		this.Taxes = Taxes;
	}

	public String getDate() {
		return Date;
	}

	public void setDate(String Date) {
		this.Date = Date;
	}

	public String getPaymentForm() {
		return PaymentForm;
	}

	public void setPaymentForm(String PaymentForm) {
		this.PaymentForm = PaymentForm;
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

	public double getAmount() {
		return Amount;
	}

	public void setAmount(double Amount) {
		this.Amount = Amount;
	}

	public String getOperationNumber() {
		return OperationNumber;
	}

	public void setOperationNumber(String OperationNumber) {
		this.OperationNumber = OperationNumber;
	}

	public String getRfcIssuerPayerAccount() {
		return RfcIssuerPayerAccount;
	}

	public void setRfcIssuerPayerAccount(String RfcIssuerPayerAccount) {
		this.RfcIssuerPayerAccount = RfcIssuerPayerAccount;
	}

	public String getForeignAccountNamePayer() {
		return ForeignAccountNamePayer;
	}

	public void setForeignAccountNamePayer(String ForeignAccountNamePayer) {
		this.ForeignAccountNamePayer = ForeignAccountNamePayer;
	}

	public String getPayerAccount() {
		return PayerAccount;
	}

	public void setPayerAccount(String PayerAccount) {
		this.PayerAccount = PayerAccount;
	}

	public String getRfcReceiverBeneficiaryAccount() {
		return RfcReceiverBeneficiaryAccount;
	}

	public void setRfcReceiverBeneficiaryAccount(String RfcReceiverBeneficiaryAccount) {
		this.RfcReceiverBeneficiaryAccount = RfcReceiverBeneficiaryAccount;
	}

	public String getBeneficiaryAccount() {
		return BeneficiaryAccount;
	}

	public void setBeneficiaryAccount(String BeneficiaryAccount) {
		this.BeneficiaryAccount = BeneficiaryAccount;
	}

}
