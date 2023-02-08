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
  private String Exportation;
  private String PaymentForm;
  private String PaymentMethod;
  private GlobalInformation GlobalInformation;
  private Issuer Issuer;
  private Receiver Receiver;
  private List<Item> Items;
  private Complements Complemento;

  private String LogoUrl;

  // Datos no fiscales (se muestran en el PDF)
  private String Observations;
  private String OrderNumber;
  private String PaymentBankName;

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

  public String getLogoUrl() {
    return LogoUrl;
  }

  public void setLogoUrl(String LogoUrl) {
    this.LogoUrl = LogoUrl;
  }

  public String getCfdiType() {
    return CfdiType;
  }

  public void setCfdiType(String CfdiType) {
    this.CfdiType = CfdiType;
  }

  public String getExportation() {
    return Exportation;
  }

  public void setExportation(String Exportation) {
    this.Exportation = Exportation;
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
    return Complemento;
  }

  public void setComplements(Complements Complements) {
    this.Complemento = Complements;
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

  public GlobalInformation getGlobalInformation() {
    return GlobalInformation;
  }

  public void setGlobalInformation(GlobalInformation GlobalInformation) {
    this.GlobalInformation = GlobalInformation;
  }

}
