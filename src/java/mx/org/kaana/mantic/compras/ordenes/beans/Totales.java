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
	private double utilidad;
	private double global;
	private double cantidad;

	public Totales() {
		this(0, 0D, 0D, 0D, 0D, 0D, 0D, 0D, 0D, 0D);
	}

	public Totales(int articulos, double importe, double iva, double descuento, double extra, double subTotal, double total, double utilidad, double global, double cantidad) {
		this.articulos= articulos;
		this.importe  = importe; 
		this.iva      = iva;
		this.descuento= descuento;
		this.extra    = extra;
		this.subTotal = subTotal;
		this.total    = total;
		this.utilidad = utilidad;
		this.global   = global;
		this.cantidad = cantidad;
	}

	public int getArticulos() {
		return articulos;
	}

	public int getReales() {
		return articulos- 1;
	}

	public void setArticulos(int articulos) {
		this.articulos=articulos;
	}

	public double getImporte() {
		return Numero.toRedondearSat(importe);
	}

	public String getImporte$() {
		return Global.format(EFormatoDinamicos.MONEDA_SAT_DECIMALES, Numero.toRedondearSat(importe));
	}

	public void setImporte(double importe) {
		this.importe=importe;
	}
	
	public double getIva() {
		return Numero.toRedondearSat(iva);
	}

	public String getIva$() {
		return Global.format(EFormatoDinamicos.MILES_SAT_DECIMALES, Numero.toRedondearSat(iva));
	}

	public void setIva(double iva) {
		this.iva=iva;
	}

	public double getDescuento() {
		return Numero.toRedondearSat(descuento);
	}

	public String getDescuento$() {
		return Global.format(EFormatoDinamicos.MILES_SAT_DECIMALES, Numero.toRedondearSat(descuento));
	}

	public void setDescuento(double descuento) {
		this.descuento=descuento;
	}

	public double getExtra() {
		return Numero.toRedondearSat(extra);
	}

	public String getExtra$() {
		return Global.format(EFormatoDinamicos.MILES_SAT_DECIMALES, Numero.toRedondearSat(extra));
	}

	public void setExtra(double extra) {
		this.extra=extra;
	}

	public double getSubTotal() {
		return Numero.toRedondearSat(subTotal);
	}

	public String getSubTotal$() {
		return Global.format(EFormatoDinamicos.MILES_SAT_DECIMALES, Numero.toRedondearSat(subTotal));
	}

	public void setSubTotal(double subTotal) {
		this.subTotal=subTotal;
	}

	public double getTotal() {
		return Numero.toRedondearSat(total);
	}

	public String getTotal$() {
		return Global.format(EFormatoDinamicos.MILES_SAT_DECIMALES, Numero.toRedondearSat(total));
	}

	public void setTotal(double total) {
		this.total=total;
	}

	public double getDescuentos() {
		return Numero.toRedondearSat(this.descuento+ this.extra);
	}

  public void addArticulo(Long idArticulo, double cantidad) {
		this.articulos+= idArticulo> 0? 1: 0;
		if(idArticulo> 0)
		  this.cantidad+= cantidad;
	}
	
	public void addArticulo(Articulo articulo) {
		this.importe+= articulo.getImportes().getImporte();
		this.descuento+= articulo.getImportes().getDescuento();
		this.extra+= articulo.getImportes().getExtra();
		this.iva+= articulo.getImportes().getIva();
		this.subTotal+= articulo.getImportes().getSubTotal();		
		this.total+= articulo.getImportes().getTotal();
		this.utilidad+= articulo.getUtilidad();
    this.cantidad+= articulo.getCantidad();
  	this.articulos+= 1;
	}
	
	public void removeUltimo(Articulo articulo) {
		articulo.setCuantos(0D);
	  this.cantidad-= articulo.getCantidad();
	}
	
	public void removeArticulo(Articulo articulo) {
		this.importe-= articulo.getImportes().getImporte();
		this.descuento-= articulo.getImportes().getDescuento();
		this.extra-= articulo.getImportes().getExtra();
		this.iva-= articulo.getImportes().getIva();
		this.subTotal-= articulo.getImportes().getSubTotal();
		this.total-= articulo.getImportes().getTotal();
		this.utilidad-= articulo.getUtilidad();
	  this.cantidad-= articulo.getCuantos();
	  this.articulos-= 1;
	}
	
	public void removeTotal() {
		if(this.global> 0D)
  		if(this.utilidad> this.global)
    		this.total-= this.global;
		  else
			  this.global= 0D;
	}
	
	public String getGlobal$() {
		return Global.format(EFormatoDinamicos.MILES_SAT_DECIMALES, Numero.toRedondearSat(global));
	}
	
	public double getGlobal() {
		return global;
	}

	public void setGlobal(double global) {
		this.global = global;
	}	

	public double getUtilidad() {
		return utilidad;
	}

	public void setUtilidad(double utilidad) {
		this.utilidad = utilidad;
	}
	
	public double getCantidad() {
		return cantidad;
	}

	public void setCantidad(double cantidad) {
		this.cantidad=cantidad;
	}

	public double getImporteDosDecimales() {
		return Numero.toRedondear(importe);
	}

	public String getImporteDosDecimales$() {
		return Global.format(EFormatoDinamicos.MILES_CON_DECIMALES, Numero.toRedondear(importe));
	}
	
	public void setImporteDosDecimales(double importe) {
		this.importe=importe;
	}
	
	public double getIvaDosDecimales() {
		return Numero.toRedondear(iva);
	}

	public String getIvaDosDecimales$() {
		return Global.format(EFormatoDinamicos.MILES_CON_DECIMALES, Numero.toRedondear(iva));
	}

	public void setIvaDosDecimales(double iva) {
		this.iva=iva;
	}
	
	public double getDescuentoDosDecimales() {
		return Numero.toRedondear(descuento);
	}

	public String getDescuentoDosDecimales$() {
		return Global.format(EFormatoDinamicos.MILES_CON_DECIMALES, Numero.toRedondear(descuento));
	}

	public void setDescuentoDosDecimales(double descuento) {
		this.descuento=descuento;
	}
	
	public double getExtraDosDecimales() {
		return Numero.toRedondear(extra);
	}

	public String getExtraDosDecimales$() {
		return Global.format(EFormatoDinamicos.MILES_CON_DECIMALES, Numero.toRedondear(extra));
	}

	public void setExtraDosDecimales(double extra) {
		this.extra=extra;
	}
	
	public double getSubTotalDosDecimales() {
		return Numero.toRedondear(subTotal);
	}

	public String getSubTotalDosDecimales$() {
		return Global.format(EFormatoDinamicos.MILES_CON_DECIMALES, Numero.toRedondear(subTotal));
	}

	public void setSubTotalDosDecimales(double subTotal) {
		this.subTotal=subTotal;
	}
	
	public double getTotalDosDecimales() {
		return Numero.toRedondear(total);
	}

	public String getTotalDosDecimales$() {
		return Global.format(EFormatoDinamicos.MILES_CON_DECIMALES, Numero.toRedondear(total));
	}

	public void setTotalDosDecimales(double total) {
		this.total=total;
	}
	
	public double getDescuentosDosDecimales() {
		return Numero.toRedondear(this.descuento+ this.extra);
	}
	
	public String getGlobalDosDecimales$() {
		return Global.format(EFormatoDinamicos.MILES_CON_DECIMALES, Numero.toRedondear(global));
	}

	public void setGlobalDosDecimales(double global) {
		this.global = global;
	}
	
	public void reset() {
	  this.articulos= 0;
		this.descuento= 0;
		this.extra    = 0;
		this.importe  = 0;
		this.iva      = 0;
		this.subTotal = 0;
		this.total    = 0;
		this.utilidad = 0;
		this.cantidad = 0;
	}

	@Override
	public String toString() {
		return "Totales{"+"articulos="+articulos+", importe="+importe+", iva="+iva+", sinIva="+sinIva+", descuento="+descuento+", extra="+extra+", subTotal="+subTotal+", total="+total+", utilidad="+utilidad+", global="+global+", cantidad="+cantidad+'}';
	}
	
}
