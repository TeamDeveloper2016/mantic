package mx.org.kaana.libs.facturama.models.request.complements.terceros;

public class ThirdPartyAccountTax {

	private String Name;
	private Double Rate;
	private double Amount;

	public String getName() {
		return Name;
	}

	public void setName(String Name) {
		this.Name = Name;
	}

	public Double getRate() {
		return Rate;
	}

	public void setRate(Double Rate) {
		this.Rate = Rate;
	}

	public double getAmount() {
		return Amount;
	}

	public void setAmount(double Amount) {
		this.Amount = Amount;
	}
}
