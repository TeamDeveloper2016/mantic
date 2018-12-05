package mx.org.kaana.libs.facturama.models.request;

import static java.lang.reflect.Array.get;
import java.math.BigDecimal;
import java.util.*;

public class ProductTax {

	private String Name;
	private Double Rate;
	private boolean IsRetention;
	private boolean IsFederalTax;
	private Boolean IsQuota;
	private Double Total;

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

	public boolean getIsRetention() {
		return IsRetention;
	}

	public void setIsRetention(boolean IsRetention) {
		this.IsRetention = IsRetention;
	}

	public boolean getIsFederalTax() {
		return IsFederalTax;
	}

	public void setIsFederalTax(boolean IsFederalTax) {
		this.IsFederalTax = IsFederalTax;
	}

	public Boolean getIsQuota() {
		return IsQuota;
	}

	public void setIsQuota(Boolean IsQuota) {
		this.IsQuota = IsQuota;
	}

	public Double getTotal() {
		return Total;
	}

	public void setTotal(Double Total) {
		this.Total = Total;
	}

	@Override
	public String toString() {
		return "ProductTax{"+"Name="+Name+", Rate="+Rate+", IsRetention="+IsRetention+", IsFederalTax="+IsFederalTax+", IsQuota="+IsQuota+", Total="+Total+'}';
	}

}
