package mx.org.kaana.libs.facturama.models.request.complements;

import java.util.List;
import mx.org.kaana.libs.facturama.models.request.Payment;
import mx.org.kaana.libs.facturama.models.request.complements.foreingtrade.ForeingTrade;
import mx.org.kaana.libs.facturama.models.request.complements.donat.Donat;
import mx.org.kaana.libs.facturama.models.request.complements.ine.Ine;

public class Complements {

  private List<Payment> Payments;
  private ForeingTrade ForeignTrade;
  private Donat Donation;
  private Ine Ine;

  /**
   * Complemento de pago, se pueden representar varios pagos en un solo CFDI Referencia: https://apisandbox.facturama.mx/guias/api-web/cfdi/complemento-pago
   *
   * @return
   */
  public List<Payment> getPayments() {
    return Payments;
  }

  /**
   * Complemento de pago, se pueden representar varios pagos en un solo CFDI Referencia: https://apisandbox.facturama.mx/guias/api-web/cfdi/complemento-pago
   *
   * @return
   */
  public void setPayments(List<Payment> payments) {
    this.Payments = payments;
  }

  /**
   * Complemento de comercio exterior Referencia: https://apisandbox.facturama.mx/guias/api-web/cfdi/complemento-exterior
   *
   * @return
   */
  public ForeingTrade getForeingTrades() {
    return ForeignTrade;
  }

  /**
   * Complemento de comercio exterior Referencia: https://apisandbox.facturama.mx/guias/api-web/cfdi/complemento-exterior
   *
   * @return
   */
  public void setForeingTrades(ForeingTrade foreingTrades) {
    this.ForeignTrade = foreingTrades;
  }

  /**
   * Complemento de donativos Referencia: https://apisandbox.facturama.mx/guias/api-web/cfdi/complemento-donativo
   *
   * @return
   */
  public Donat getDonation() {
    return Donation;
  }

  /**
   * Complemento de donativos Referencia: https://apisandbox.facturama.mx/guias/api-web/cfdi/complemento-donativo
   *
   * @return
   */
  public void setDonation(Donat Donation) {
    this.Donation = Donation;
  }

  public Ine getIne() {
    return Ine;
  }

  public void setIne(Ine Ine) {
    this.Ine = Ine;
  }

}
