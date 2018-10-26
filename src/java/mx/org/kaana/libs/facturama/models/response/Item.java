package mx.org.kaana.libs.facturama.models.response;

public class Item {

	private double Quantity;
	private String Unit;
	private String Description;
	private double UnitValue;
	private double Total;

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

	public double getUnitValue() {
		return UnitValue;
	}

	public void setUnitValue(double UnitValue) {
		this.UnitValue = UnitValue;
	}

	public double getTotal() {
		return Total;
	}

	public void setTotal(double Total) {
		this.Total = Total;
	}

}
