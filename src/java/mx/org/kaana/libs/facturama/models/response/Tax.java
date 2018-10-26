package mx.org.kaana.libs.facturama.models.response;

public class Tax {

	private double Total;
	private String Name;
	private Double Rate;
	private String Type;

	public double getTotal() {
		return Total;
	}

	public void setTotal(double Total) {
		this.Total = Total;
	}

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

	public String getType() {
		return Type;
	}

	public void setType(String Type) {
		this.Type = Type;
	}

}
