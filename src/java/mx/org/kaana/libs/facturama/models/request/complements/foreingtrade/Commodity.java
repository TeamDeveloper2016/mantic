package mx.org.kaana.libs.facturama.models.request.complements.foreingtrade;

import java.util.List;

public class Commodity {

	private List<SpecificDescriptions> SpecificDescriptions;
	private String IdentificationNumber;
	private String TariffFraction;
	private Double CustomsQuantity;
	private String CustomsUnit;
	private Double CustomsUnitValue;
	private double ValueInDolar;

	public List<SpecificDescriptions> getSpecificDescriptions() {
		return SpecificDescriptions;
	}

	public void setSpecificDescriptions(List<SpecificDescriptions> SpecificDescriptions) {
		this.SpecificDescriptions = SpecificDescriptions;
	}

	public String getIdentificationNumber() {
		return IdentificationNumber;
	}

	public void setIdentificationNumber(String IdentificationNumber) {
		this.IdentificationNumber = IdentificationNumber;
	}

	public String getTariffFraction() {
		return TariffFraction;
	}

	public void setTariffFraction(String TariffFraction) {
		this.TariffFraction = TariffFraction;
	}

	public Double getCustomsQuantity() {
		return CustomsQuantity;
	}

	public void setCustomsQuantity(Double CustomsQuantity) {
		this.CustomsQuantity = CustomsQuantity;
	}

	public String getCustomsUnit() {
		return CustomsUnit;
	}

	public void setCustomsUnit(String CustomsUnit) {
		this.CustomsUnit = CustomsUnit;
	}

	public Double getCustomsUnitValue() {
		return CustomsUnitValue;
	}

	public void setCustomsUnitValue(Double CustomsUnitValue) {
		this.CustomsUnitValue = CustomsUnitValue;
	}

	public double getValueInDolar() {
		return ValueInDolar;
	}

	public void setValueInDolar(double ValueInDolar) {
		this.ValueInDolar = ValueInDolar;
	}
}
