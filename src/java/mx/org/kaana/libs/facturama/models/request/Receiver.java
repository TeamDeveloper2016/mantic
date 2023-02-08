package mx.org.kaana.libs.facturama.models.request;

public class Receiver {

  private String Id;
  private String Rfc;
  private String Name;
  private String CfdiUse;
  private String FiscalRegime;
  private String TaxZipCode;
  private String TaxResidence;
  private String TaxRegistrationNumber;

  public String getId() {
    return Id;
  }

  public void setId(String Id) {
    this.Id = Id;
  }

  public String getRfc() {
    return Rfc;
  }

  public void setRfc(String Rfc) {
    this.Rfc = Rfc;
  }

  public String getName() {
    return Name;
  }

  public void setName(String Name) {
    this.Name = Name;
  }

  public String getCfdiUse() {
    return CfdiUse;
  }

  public void setCfdiUse(String CfdiUse) {
    this.CfdiUse = CfdiUse;
  }

  public String getFiscalRegime() {
    return FiscalRegime;
  }

  public void setFiscalRegime(String FiscalRegime) {
    this.FiscalRegime = FiscalRegime;
  }

  public String getTaxZipCode() {
    return TaxZipCode;
  }

  public void setTaxZipCode(String TaxZipCode) {
    this.TaxZipCode = TaxZipCode;
  }

  public String getTaxResidence() {
    return TaxResidence;
  }

  public void TaxResidence(String TaxResidence) {
    this.TaxResidence = TaxResidence;
  }

  public String getTaxRegistrationNumber() {
    return TaxRegistrationNumber;
  }

  public void setTaxRegistrationNumber(String TaxRegistrationNumber) {
    this.TaxRegistrationNumber = TaxRegistrationNumber;
  }

}
