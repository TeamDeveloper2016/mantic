package mx.org.kaana.libs.facturama.models.request;

import mx.org.kaana.libs.facturama.models.request.complements.Complements;
import java.util.*;

public class Cfdi {

	private String NameId;
	private String Date;
	private String Serie;
	private String PaymentAccountNumber;
	private Double CurrencyExchageRate;
	private String Currency;
	private String ExpeditionPlace;
	private String PaymentConditions;
	private CfdiRelation Relations;
	private String CfdiType;
	private String PaymentForm;
	private String PaymentMethod;
	private Receiver Receiver;
	private List<Item> Items;
	private String Observations;
	private String OrderNumber;
	private String PaymentBankName;
	private Complements Complements;

	public String getNameId() {
		return NameId;
	}

	/**
	 * Establece el identificador del nombre para el PDF
	 */
	public void setNameId(String NameId) {
		this.NameId = NameId;
	}

	/*public void setUnitCode(Integer NameId)
   {
      this.NameId = NameId;
   }*/
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

	public Double getCurrencyExchageRate() {
		return CurrencyExchageRate;
	}

	public void setCurrencyExchageRate(Double CurrencyExchageRate) {
		this.CurrencyExchageRate = CurrencyExchageRate;
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

	public CfdiRelation getRelations() {
		return Relations;
	}

	public void setRelations(CfdiRelation Relations) {
		this.Relations = Relations;
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

	public String getObservations() {
		return Observations;
	}

	public void setObservations(String Observations) {
		this.Observations = Observations;
	}

	public String getOrderNumber() {
		return OrderNumber;
	}

	public void setOrderNumber(String OrderNumber) {
		this.OrderNumber = OrderNumber;
	}

	public String getPaymentBankName() {
		return PaymentBankName;
	}

	public void setPaymentBankName(String PaymentBankName) {
		this.PaymentBankName = PaymentBankName;
	}

	public Complements getComplements() {
		return Complements;
	}

	public void setComplements(Complements Complements) {
		this.Complements = Complements;
	}

}
