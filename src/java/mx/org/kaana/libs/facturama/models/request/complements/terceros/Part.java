package mx.org.kaana.libs.facturama.models.request.complements.terceros;

import java.util.List;

public class Part {

	private double Quantity;
	private String Unit;
	private String IdentificationNumber;
	private String Description;
	private Double UnitPrce;
	private Double Amount;
	private List<CustomsInformation> CustomsInformation;

	public double getQuantity() {
		return Quantity;
	}

	public void setQuantity(double Quantity) {
		this.Quantity = Quantity;
	}

	public String getUnit() {
		return Unit;
	}

	public void setUnit(String Unit) {
		this.Unit = Unit;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String Description) {
		this.Description = Description;
	}

	public Double getUnitPrce() {
		return UnitPrce;
	}

	public void setUnitPrce(Double UnitPrce) {
		this.UnitPrce = UnitPrce;
	}

	public Double getAmount() {
		return Amount;
	}

	public void setAmount(Double Amount) {
		this.Amount = Amount;
	}

	public List<CustomsInformation> getCustomsInformation() {
		return CustomsInformation;
	}

	public void setCustomsInformation(List<CustomsInformation> CustomsInformation) {
		this.CustomsInformation = CustomsInformation;
	}

}
