package mx.org.kaana.mantic.inventarios.almacenes.beans;

import java.io.Serializable;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.mantic.inventarios.almacenes.enums.ETiposVentas;
import static mx.org.kaana.mantic.inventarios.almacenes.enums.ETiposVentas.MAYOREO;
import static mx.org.kaana.mantic.inventarios.almacenes.enums.ETiposVentas.MEDIO_MAYOREO;
import static mx.org.kaana.mantic.inventarios.almacenes.enums.ETiposVentas.MENUDEO;

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
	private double limite;
	private double impuesto;
	private double pivote;
	private boolean rounded;

	public TiposVentas(Integer index) {
		this(index, "", 0D, 0D, 16D, 0L, 0D, false);
	}
	
	public TiposVentas(Integer index, String nombre, double costo, double precio, double iva, double limite, double pivote, boolean rounded) {
		this.index=index;
		this.nombre=nombre;
		this.costo=costo;
		this.utilidad=0D;
		this.iva=iva;
		this.importe=0D;
		this.limite=limite;
		this.impuesto=0D;
		this.pivote=pivote;
		this.rounded=rounded;
		this.precio= precio;
		this.toCalculate(false);
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index=index;
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

	public double getLimite() {
		return limite;
	}

	public void setLimite(double limite) {
		this.limite=limite;
	}

	public double getImpuesto() {
		return impuesto;
	}

	public double getPivote() {
		return pivote;
	}

	public void setPivote(double pivote) {
		this.pivote=pivote;
	}

	public void setRounded(boolean rounded) {
		this.rounded=rounded;
	}
	
	public boolean isRounded() {
		return rounded;
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
		return "TiposVentas{"+"index="+index+", nombre="+nombre+", costo="+costo+", utilidad="+utilidad+", precio="+precio+", iva="+iva+", importe="+importe+", limite="+limite+", impuesto="+impuesto+ ", pivote="+pivote+'}';
	}

	public void toUpdateUtilidad(double utilidadad) {
		double calculo= Numero.toRedondearSat((this.costo* ((this.iva/100)+ 1)));
		this.precio   = Numero.toRedondearSat(((utilidadad/ 100)+ 1)* calculo);
		this.toCalculate(this.rounded);
	}
	
  public void toCalculate() {
	  this.toCalculate(this.rounded);	
	}
	
  public void toCalculate(boolean round) {
		this.precio   = Numero.toAjustarDecimales(this.precio, round);
	  this.impuesto = Numero.toRedondearSat((this.precio* ((this.iva/100)+ 1))- this.precio);
		this.importe  = Numero.toRedondearSat(this.precio- this.impuesto);
		double calculo= Numero.toRedondearSat((this.costo* ((this.iva/100)+ 1)));
		// al precio de neto se le quita el costo+ iva y lo que queda se calcula la utilidad bruta 
		this.utilidad = Numero.toRedondearSat((this.precio- calculo)* 100/ calculo);
	}	
	
	public ETiposVentas toEnum() {
		 ETiposVentas regresar= MENUDEO;
		 switch(this.index) {
			 case 1:
				 regresar= MEDIO_MAYOREO;
				 break;
			 case 2:
				 regresar= MAYOREO;
				 break;
		 } // switch
		 return regresar;
  }

}
