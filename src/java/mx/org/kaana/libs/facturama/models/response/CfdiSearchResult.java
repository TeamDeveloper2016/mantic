package mx.org.kaana.libs.facturama.models.response;

import java.util.Calendar;
import java.util.Objects;
import mx.org.kaana.libs.formato.Fecha;

public class CfdiSearchResult implements Comparable<CfdiSearchResult> {

  private String Id;
  private String CfdiType;
  private String Type;
  private String Folio;
  private String Serie;
  private String TaxName;
  private String Rfc;
  private String RfcIssuer;
  private String Date;
  private Double Subtotal;
  private Double Total;
  private String Uuid;
  private Boolean IsActive;
  private String PaymentMethod;
  private String OrderNumber;
  private String Status;
  private String Email;
  private Boolean EmailSent;
  private Calendar timbrado;

  public CfdiSearchResult() {
    this("");
  }

  public CfdiSearchResult(String Id) {
    this.Id = Id;
  }

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

  public String getType() {
    return Type;
  }

  public void setType(String Type) {
    this.Type = Type;
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

  public String getRfcIssuer() {
    return RfcIssuer;
  }

  public void setRfcIssuer(String RfcIssuer) {
    this.RfcIssuer = RfcIssuer;
  }

  public String getDate() {
    return Date;
  }

  public void setDate(String Date) {
    this.Date = Date;
  }

  public Double getSubtotal() {
    return Subtotal;
  }

  public void setSubtotal(double Subtotal) {
    this.Subtotal = Subtotal;
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

  public String getPaymentMethod() {
    return PaymentMethod;
  }

  public void setPaymentMethod(String PaymentMethod) {
    this.PaymentMethod = PaymentMethod;
  }

  public String getOrderNumber() {
    return OrderNumber;
  }

  public void setOrderNumber(String OrderNumber) {
    this.OrderNumber = OrderNumber;
  }

  public String getStatus() {
    return Status;
  }

  public void setStatus(String Status) {
    this.Status = Status;
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
    this.timbrado = timbrado;
  }

  @Override
  public int hashCode() {
    int hash = 3;
    hash = 97 * hash + Objects.hashCode(this.Id);
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final CfdiSearchResult other = (CfdiSearchResult) obj;
    if (!Objects.equals(this.Id, other.Id)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "CfdiSearchResult{" + "Id=" + Id + ", Folio=" + Folio + ", Serie=" + Serie + ", TaxName=" + TaxName + ", Rfc=" + Rfc + ", Date=" + Date + ", Total=" + Total + ", Uuid=" + Uuid + ", Email=" + Email + ", IsActive=" + IsActive + ", EmailSent=" + EmailSent + '}';
  }

  @Override
  public int compareTo(CfdiSearchResult compare) {
    if (this.timbrado == null) {
      this.timbrado = Fecha.toCalendar(this.getDate().substring(0, 10), this.getDate().substring(11, 19));
    }
    if (compare.timbrado == null) {
      compare.timbrado = Fecha.toCalendar(compare.getDate().substring(0, 10), compare.getDate().substring(11, 19));
    }
    //ascending order
    // return  compare.getTimbrado().compareTo(this.getTimbrado());

    //descending order
    return compare.getDate().compareTo(this.getDate());
  }
  
}
