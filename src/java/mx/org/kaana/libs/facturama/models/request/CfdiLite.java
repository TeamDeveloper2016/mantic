package mx.org.kaana.libs.facturama.models.request;

import mx.org.kaana.libs.facturama.models.request.complements.Complements;
import java.util.List;

public class CfdiLite {

	private String NameId;
	private String Date;
	private String Serie;
	private String PaymentAccountNumber;
	private Double CurrencyExchangeRate;
	private String Currency;
	private String ExpeditionPlace;
	private String PaymentConditions;
	private CfdiRelations Relations;
	private String Folio;
	private String CfdiType;
	private String PaymentForm;
	private String PaymentMethod;
	private Issuer Issuer;
	private Receiver Receiver;
	private List<Item> Items;
	private Complements Complements;

	public String getNameId() {
		return NameId;
	}

	public void setNameId(String NameId) {
		this.NameId = NameId;
	}

	public String getDate() {
		return Date;
	}

	public void setDate(String Date) {
		this.Date = Date;
	}

	public String getSerie() {
		return Serie;
	}

	public void setSerie(String Serie) {
		this.Serie = Serie;
	}

	public String getPaymentAccountNumber() {
		return PaymentAccountNumber;
	}

	public void setPaymentAccountNumber(String PaymentAccountNumber) {
		this.PaymentAccountNumber = PaymentAccountNumber;
	}

	public Double getCurrencyExchangeRate() {
		return CurrencyExchangeRate;
	}

	public void setCurrencyExchangeRate(Double CurrencyExchangeRate) {
		this.CurrencyExchangeRate = CurrencyExchangeRate;
	}

	public String getCurrency() {
		return Currency;
	}

	public void setCurrency(String Currency) {
		this.Currency = Currency;
	}

	public String getExpeditionPlace() {
		return ExpeditionPlace;
	}

	public void setExpeditionPlace(String ExpeditionPlace) {
		this.ExpeditionPlace = ExpeditionPlace;
	}

	public String getPaymentConditions() {
		return PaymentConditions;
	}

	public void setPaymentConditions(String PaymentConditions) {
		this.PaymentConditions = PaymentConditions;
	}

	public CfdiRelations getRelations() {
		return Relations;
	}

	public void setRelations(CfdiRelations Relations) {
		this.Relations = Relations;
	}

	public String getFolio() {
		return Folio;
	}

	public void setFolio(String Folio) {
		this.Folio = Folio;
	}

	public String getCfdiType() {
		return CfdiType;
	}

	public void setCfdiType(String CfdiType) {
		this.CfdiType = CfdiType;
	}

	public String getPaymentForm() {
		return PaymentForm;
	}

	public void setPaymentForm(String PaymentForm) {
		this.PaymentForm = PaymentForm;
	}

	public String getPaymentMethod() {
		return PaymentMethod;
	}

	public void setPaymentMethod(String PaymentMethod) {
		this.PaymentMethod = PaymentMethod;
	}

	public Issuer getIssuer() {
		return Issuer;
	}

	public void setIssuer(Issuer Issuer) {
		this.Issuer = Issuer;
	}

	public Receiver getReceiver() {
		return Receiver;
	}

	public void setReceiver(Receiver Receiver) {
		this.Receiver = Receiver;
	}

	public List<Item> getItems() {
		return Items;
	}

	public void setItems(List<Item> Items) {
		this.Items = Items;
	}

	public Complements getComplements() {
		return Complements;
	}

	public void setComplements(Complements Complements) {
		this.Complements = Complements;
	}

}
