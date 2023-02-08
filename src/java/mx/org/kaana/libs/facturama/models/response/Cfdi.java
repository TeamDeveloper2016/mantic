package mx.org.kaana.libs.facturama.models.response;

import java.util.List;
import mx.org.kaana.libs.facturama.models.request.complements.Complements;

public class Cfdi {

  private String Id;//ya
  private String CfdiType;
  private String Serie;
  private String Folio;
  private String Date;
  private String CertNumber;
  private String PaymentTerms;
  private String PaymentConditions;
  private String PaymentMethod;
  private String PaymentAccountNumber;
  private String ExpeditionPlace;
  private Double ExchangeRate;
  private String Currency;
  private double Subtotal;
  private Double Discount;
  private double Total;
  private String Observations;
  private Issuer Issuer;
  private Receiver Receiver;
  private List<Item> Items;
  private List<Tax> Taxes;
  private Complement Complement;
  private Complements Complements;
  private String Status;
  private String OriginalString;
  // Datos no fiscales (se muestran en el PDF)   
  private String OrderNumber;
  private String PaymentBankName;

  public String getId() {
    return Id;
  }

  public void setId(String Id) {
    this.Id = Id;
  }

  public String getCfdiType() {
    return CfdiType;
  }

  public void setCfdiType(String CfdiType) {
    this.CfdiType = CfdiType;
  }

  public String getSerie() {
    if ("".equals(Serie)) {
      return null;
    }
    return Serie;
  }

  public void setSerie(String Serie) {
    this.Serie = Serie;
  }

  public String getFolio() {
    if ("".equals(Folio)) {
      return null;
    }
    return Folio;
  }

  public void setFolio(String Folio) {
    this.Folio = Folio;
  }

  public String getDate() {
    return Date;
  }

  public void setDate(String Date) {
    this.Date = Date;
  }

  public String getCertNumber() {
    return CertNumber;
  }

  public void setCertNumber(String CertNumber) {
    this.CertNumber = CertNumber;
  }

  public String getPaymentTerms() {
    return PaymentTerms;
  }

  public void setPaymentTerms(String PaymentTerms) {
    this.PaymentTerms = PaymentTerms;
  }

  public String getPaymentConditions() {
    return PaymentConditions;
  }

  public void setPaymentConditions(String PaymentConditions) {
    this.PaymentConditions = PaymentConditions;
  }

  public String getPaymentMethod() {
    return PaymentMethod;
  }

  public void setPaymentMethod(String PaymentMethod) {
    this.PaymentMethod = PaymentMethod;
  }

  public String getPaymentAccountNumber() {
    return PaymentAccountNumber;
  }

  public void setPaymentAccountNumber(String PaymentAccountNumber) {
    this.PaymentAccountNumber = PaymentAccountNumber;
  }

  public String getExpeditionPlace() {
    return ExpeditionPlace;
  }

  public void setExpeditionPlace(String ExpeditionPlace) {
    this.ExpeditionPlace = ExpeditionPlace;
  }

  public Double getExchangeRate() {
    return ExchangeRate;
  }

  public void setExchangeRate(Double ExchangeRate) {
    this.ExchangeRate = ExchangeRate;
  }

  public String getCurrency() {
    return Currency;
  }

  public void setCurrency(String Currency) {
    this.Currency = Currency;
  }

  public double getSubtotal() {
    return Subtotal;
  }

  public void setSubtotal(double Subtotal) {
    this.Subtotal = Subtotal;
  }

  public Double getDiscount() {
    return Discount;
  }

  public void setDiscount(Double Discount) {
    this.Discount = Discount;
  }

  public double getTotal() {
    return Total;
  }

  public void setTotal(double Total) {
    this.Total = Total;
  }

  public String getObservations() {
    return Observations;
  }

  public void setObservations(String Observations) {
    this.Observations = Observations;
  }

  public List<Tax> getTaxes() {
    return Taxes;
  }

  public void setTaxes(List<Tax> Taxes) {
    this.Taxes = Taxes;
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

  public Issuer getIssuer() {
    return Issuer;
  }

  public void setIssuer(Issuer Issuer) {
    this.Issuer = Issuer;
  }

  public Complement getComplement() {
    return Complement;
  }

  public void setComplement(Complement Complement) {
    this.Complement = Complement;
  }

  public Complements getComplements() {
    return Complements;
  }

  public void setComplement(Complements Complements) {
    this.Complements = Complements;
  }

  public String getStatus() {
    return Status;
  }

  public void setStatus(String Status) {
    this.Status = Status;
  }

  public String getOriginalString() {
    return OriginalString;
  }

  public void setOriginalString(String OriginalString) {
    this.OriginalString = OriginalString;
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

}
