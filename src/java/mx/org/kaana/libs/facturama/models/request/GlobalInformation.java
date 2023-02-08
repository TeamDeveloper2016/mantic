package mx.org.kaana.libs.facturama.models.request;

public class GlobalInformation {

  private String Periodicity;
  private String Months;
  private String Year;

  public String getPeriodicity() {
    return Periodicity;
  }

  public void setPeriodicity(String Periodicity) {
    this.Periodicity = Periodicity;
  }

  public String getMonths() {
    return Months;
  }

  public void setMonths(String Months) {
    this.Months = Months;
  }

  public String getYear() {
    return Year;
  }

  public void setYear(String Year) {
    this.Year = Year;
  }
  
}
