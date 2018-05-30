package mx.org.kaana.mantic.compras.ordenes.beans;

import java.io.Serializable;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.libs.formato.Global;
import mx.org.kaana.libs.formato.Numero;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 10/05/2018
 *@time 01:29:41 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public final class Totales implements Serializable {

	private static final long serialVersionUID=-724230632673749688L;

	private int articulos;
	private double importe;
	private double iva;
	private double sinIva;
	private double descuento;
	private double extra;
	private double subTotal;
	private double total;

	public Totales() {
		this(0, 0D, 0D, 0D, 0D, 0D, 0D);
	}

	public Totales(int articulos, double importe, double iva, double descuento, double extra, double subTotal, double total) {
		this.articulos=articulos;
		this.importe= importe; 
		this.iva=iva;
		this.descuento=descuento;
		this.extra=extra;
		this.subTotal=subTotal;
		this.total=total;
	}

	public int getArticulos() {
		return articulos;
	}

	public void setArticulos(int articulos) {
		this.articulos=articulos;
	}

	public double getImporte() {
		return Numero.toRedondear(importe);
	}

	public String getImporte$() {
		return Global.format(EFormatoDinamicos.MILES_CON_DECIMALES, Numero.toRedondear(importe));
	}

	public void setImporte(double importe) {
		this.importe=importe;
	}
	
	public double getIva() {
		return Numero.toRedondear(iva);
	}

	public String getIva$() {
		return Global.format(EFormatoDinamicos.MILES_CON_DECIMALES, Numero.toRedondear(iva));
	}

	public void setIva(double iva) {
		this.iva=iva;
	}

	public double getDescuento() {
		return Numero.toRedondear(descuento);
	}

	public String getDescuento$() {
		return Global.format(EFormatoDinamicos.MILES_CON_DECIMALES, Numero.toRedondear(descuento));
	}

	public void setDescuento(double descuento) {
		this.descuento=descuento;
	}

	public double getExtra() {
		return Numero.toRedondear(extra);
	}

	public String getExtra$() {
		return Global.format(EFormatoDinamicos.MILES_CON_DECIMALES, Numero.toRedondear(extra));
	}

	public void setExtra(double extra) {
		this.extra=extra;
	}

	public double getSubTotal() {
		return Numero.toRedondear(subTotal);
	}

	public String getSubTotal$() {
		return Global.format(EFormatoDinamicos.MILES_CON_DECIMALES, Numero.toRedondear(subTotal));
	}

	public void setSubTotal(double subTotal) {
		this.subTotal=subTotal;
	}

	public double getTotal() {
		return Numero.toRedondear(total);
	}

	public String getTotal$() {
		return Global.format(EFormatoDinamicos.MILES_CON_DECIMALES, Numero.toRedondear(total));
	}

	public void setTotal(double total) {
		this.total=total;
	}

	public double getDescuentos() {
		return Numero.toRedondear(this.descuento+ this.extra);
	}

	public void addImporte(double importe) {
		this.importe+= importe;
	}
	
	public void addDescuento(double descuento) {
		this.descuento+= descuento;
	}
	
	public void addExtra(double extra) {
		this.extra+= extra;
	}
	
	public void addIva(double iva) {
		this.iva+= iva;
	}
	
	public void addSubTotal(double subTotal) {
		this.subTotal+= subTotal;
	}
	
	public void addTotal(double total) {
		this.total+= total;
	}

	public void addArticulo(Long idArticulo) {
		this.articulos+= idArticulo> 0? 1: 0;
	}

	public void reset() {
	  this.articulos= 0;
		this.descuento= 0;
		this.extra    = 0;
		this.importe  = 0;
		this.iva      = 0;
		this.subTotal = 0;
		this.total    = 0;
	}
	
	@Override
	public String toString() {
		return "Totales{"+"articulos="+articulos+", importe="+importe+", iva="+iva+", descuento="+descuento+", extra="+extra+", subTotal="+subTotal+", total="+total+'}';
	}
	
}
