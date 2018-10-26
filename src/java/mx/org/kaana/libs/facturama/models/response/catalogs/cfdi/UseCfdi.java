package mx.org.kaana.libs.facturama.models.response.catalogs.cfdi;

import mx.org.kaana.libs.facturama.models.response.catalogs.Catalog;

public class UseCfdi extends Catalog {

	private boolean Natural;
	private boolean Moral;

	public boolean getNatural() {
		return Natural;
	}

	public void setNatural(boolean Natural) {
		this.Natural = Natural;
	}

	public boolean getMoral() {
		return Moral;
	}

	public void setMoral(boolean Moral) {
		this.Moral = Moral;
	}

}
