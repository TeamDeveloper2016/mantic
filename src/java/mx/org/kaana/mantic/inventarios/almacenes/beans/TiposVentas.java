package mx.org.kaana.mantic.inventarios.almacenes.beans;

import java.io.Serializable;
import mx.org.kaana.libs.formato.Numero;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 27/05/2018
 *@time 10:28:11 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class TiposVentas implements Serializable {

	private static final long serialVersionUID=4887769036868092654L;

	private Integer index;
	private String nombre;
	private double costo;
	private double utilidad;
	private double precio;
	private double iva;
	private double importe;
	private long limite;
	private double impuesto;

	public TiposVentas(Integer index) {
		this(index, "", 0D, 0D, 16D, 0L);
	}

	
	public TiposVentas(Integer index, String nombre, double costo, double precio, double iva, long limite) {
		this.index= index;
		this.nombre=nombre;
		this.costo=costo;
		this.utilidad=0D;
		this.precio=precio;
		this.iva=iva;
		this.importe=0D;
		this.limite=limite;
		this.impuesto= 0D;
		this.toCalculate();
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre=nombre;
	}

	public double getCosto() {
		return costo;
	}

	public void setCosto(double costo) {
		this.costo=costo;
	}

	public double getUtilidad() {
		return utilidad;
	}

	public void setUtilidad(double utilidad) {
		this.utilidad=utilidad;
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio=precio;
	}

	public double getIva() {
		return iva;
	}

	public void setIva(double iva) {
		this.iva=iva;
	}

	public double getImporte() {
		return importe;
	}

	public void setImporte(double importe) {
		this.importe=importe;
	}

	public long getLimite() {
		return limite;
	}

	public void setLimite(long limite) {
		this.limite=limite;
	}

	public double getImpuesto() {
		return impuesto;
	}

	@Override
	public int hashCode() {
		int hash=5;
		hash=19*hash+(int) (this.index^(this.index>>>32));
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
		final TiposVentas other=(TiposVentas) obj;
		if (this.index!=other.index) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "TiposVentas{"+"index="+index+", nombre="+nombre+", costo="+costo+", utilidad="+utilidad+", precio="+precio+", iva="+iva+", importe="+importe+", limite="+limite+", impuesto="+impuesto+'}';
	}

	public void toUpdateUtilidad(double utilidadad) {
		 this.precio= Numero.toRedondearSat(((utilidadad/ 100)+ 1)* this.costo);
		 this.toCalculate();
	}
	
  public void toCalculate() {
		this.precio  = Numero.toRedondearSat(this.precio);
		this.utilidad= Numero.toRedondearSat((this.precio*100/(this.costo<= 0? 1: this.costo))- 100);
	  this.impuesto= Numero.toRedondearSat((this.precio* ((this.iva/100)+ 1))- this.precio);
		this.importe = Numero.toRedondearSat(this.precio+ this.impuesto);
	}	
	
}
