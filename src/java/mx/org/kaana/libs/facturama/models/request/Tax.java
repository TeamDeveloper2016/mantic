package mx.org.kaana.libs.facturama.models.request;

public class Tax {

	private Double Total;
	private String Name;
	private Double Base;
	private Double Rate;
	private boolean IsRetention;
	private Boolean IsQuota;

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

	public double getBase() {
		return Base;
	}

	public void setBase(double Base) {
		this.Base = Base;
	}

	public Double getRate() {
		return Rate;
	}

	public void setRate(Double Rate) {
		this.Rate = Rate;
	}

	public boolean getIsRetention() {
		return IsRetention;
	}

	public void setIsRetention(boolean IsRetention) {
		this.IsRetention = IsRetention;
	}

	public Boolean getIsQuota() {
		return IsQuota;
	}

	public void setIsQuota(Boolean IsQuota) {
		this.IsQuota = IsQuota;
	}

}
