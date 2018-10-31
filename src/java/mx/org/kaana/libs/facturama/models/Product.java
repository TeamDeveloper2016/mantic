package mx.org.kaana.libs.facturama.models;

import mx.org.kaana.libs.facturama.models.request.ProductTax;
import java.util.List;
import java.util.Objects;

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

	@Override
	public int hashCode() {
		int hash=7;
		hash=11*hash+Objects.hashCode(this.IdentificationNumber);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this==obj) {
			return true;
		}
		if (obj==null) {
			return false;
		}
		if (getClass()!=obj.getClass()) {
			return false;
		}
		final Product other=(Product) obj;
		if (!Objects.equals(this.IdentificationNumber, other.IdentificationNumber)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Product{"+"Id="+Id+", UnitCode="+UnitCode+", Unit="+Unit+", IdentificationNumber="+IdentificationNumber+", Name="+Name+", Description="+Description+", Price="+Price+", CodeProdServ="+CodeProdServ+", CuentaPredial="+CuentaPredial+", Taxes="+Taxes+'}';
	}
	
	
}
