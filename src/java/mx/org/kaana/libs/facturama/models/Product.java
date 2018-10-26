package mx.org.kaana.libs.facturama.models;

import mx.org.kaana.libs.facturama.models.request.ProductTax;
import java.util.List;

public class Product {

	private String Id;
	private String UnitCode;
	private String Unit;
	private String IdentificationNumber;
	private String Name;
	private String Description;
	private double Price;
	private String CodeProdServ;
	private String CuentaPredial;
	private List<ProductTax> Taxes;

	public String getId() {
		return Id;
	}

	public void setId(String Id) {
		this.Id = Id;
	}

	public String getUnitCode() {
		return UnitCode;
	}

	public void setUnitCode(String UnitCode) {
		this.UnitCode = UnitCode;
	}

	public String getUnit() {
		return Unit;
	}

	public void setUnit(String Unit) {
		this.Unit = Unit;
	}

	public String getIdentificationNumber() {
		return IdentificationNumber;
	}

	public void setIdentificationNumber(String IdentificationNumber) {
		this.IdentificationNumber = IdentificationNumber;
	}

	public String getName() {
		return Name;
	}

	public void setName(String Name) {
		this.Name = Name;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String Description) {
		this.Description = Description;
	}

	public double getPrice() {
		return Price;
	}

	public void setPrice(double Price) {
		this.Price = Price;
	}

	public String getCodeProdServ() {
		return CodeProdServ;
	}

	public void setCodeProdServ(String CodeProdServ) {
		this.CodeProdServ = CodeProdServ;
	}

	public String getCuentaPredial() {
		return CuentaPredial;
	}

	public void setCuentaPredial(String CuentaPredial) {
		this.CuentaPredial = CuentaPredial;
	}

	public List<ProductTax> getTaxes() {
		return Taxes;
	}

	public void setTaxes(List<ProductTax> Taxes) {
		this.Taxes = Taxes;
	}
}
