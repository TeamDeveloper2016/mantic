package mx.org.kaana.libs.facturama.models.request.complements;

import mx.org.kaana.libs.facturama.models.request.complements.foreingtrade.ForeingTrade;
import mx.org.kaana.libs.facturama.models.request.Payment;
import mx.org.kaana.libs.facturama.models.request.complements.donat.Donat;
import java.util.List;

public class Complements {

	private List<Payment> payments;
	private ForeingTrade foreingTrades;
	private Donat Donation;

	public List<Payment> getPayments() {
		return payments;
	}

	public void setPayments(List<Payment> payments) {
		this.payments = payments;
	}

	public ForeingTrade getForeingTrades() {
		return foreingTrades;
	}

	public void setForeingTrades(ForeingTrade foreingTrades) {
		this.foreingTrades = foreingTrades;
	}

	public Donat getDonation() {
		return Donation;
	}

	public void setDonation(Donat Donation) {
		this.Donation = Donation;
	}

}
