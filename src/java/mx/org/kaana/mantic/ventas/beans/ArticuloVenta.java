package mx.org.kaana.mantic.ventas.beans;

import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Global;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.mantic.compras.ordenes.beans.Articulo;
import mx.org.kaana.mantic.compras.ordenes.reglas.Descuentos;

public class ArticuloVenta extends Articulo{	
	
	private static final long serialVersionUID = -7272868284456340705L;

	public ArticuloVenta() {
		super();
	} // ArticuloCaja

	public ArticuloVenta(Long key) {
		super(key);
	} // ArticuloCaja
	
	public ArticuloVenta(boolean sinIva, double tipoDeCambio, String nombre, String codigo, Double costo, String descuento, Long idOrdenCompra, String extras, Double importe, String propio, Double iva, Double totalImpuesto, Double subTotal, Double cantidad, Long idOrdenDetalle, Long idArticulo, Double totalDescuentos, Long idProveedor, boolean ultimo, boolean solicitado, double stock, Double excedentes, String sat, String unidadMedida) {
		super(sinIva, tipoDeCambio, nombre, codigo, costo, descuento, idOrdenCompra, extras, importe, propio, iva, totalImpuesto, subTotal, cantidad, idOrdenDetalle, idArticulo, totalDescuentos, idProveedor, ultimo, solicitado, stock, excedentes, sat, unidadMedida);
	} // ArticuloCaja

	public ArticuloVenta(boolean sinIva, double tipoDeCambio, String nombre, String codigo, Double costo, String descuento, Long idOrdenCompra, String extras, Double importe, String propio, Double iva, Double totalImpuesto, Double subTotal, Double cantidad, Long idOrdenDetalle, Long idArticulo, Double totalDescuentos, Long idProveedor, boolean ultimo, boolean solicitado, double stock, Double excedentes, String sat, String unidadMedida, Long idAplicar) {
		super(sinIva, tipoDeCambio, nombre, codigo, costo, descuento, idOrdenCompra, extras, importe, propio, iva, totalImpuesto, subTotal, cantidad, idOrdenDetalle, idArticulo, totalDescuentos, idProveedor, ultimo, solicitado, stock, excedentes, sat, unidadMedida, idAplicar);
	} // ArticuloCaja
	
	@Override
	public Double getImporte() {
    return Numero.toRedondear(super.getImporte());
  }
	
	@Override
	public String getImporte$() {
		return Global.format(EFormatoDinamicos.MILES_CON_DECIMALES, this.getImporte()); 
	}
	
	@Override
	public void toCalculate() {
		double porcentajeIva = this.getIva()/ 100;       
		double costoMoneda   = this.getCosto()* getTipoDeCambio();
		double costoReal     = this.getCantidad()* costoMoneda;
		double utilidad      = (this.getCosto()*this.getCantidad()) - (this.getPrecio()*this.getCantidad());
		getImportes().setImporte(Numero.toRedondear(costoReal));
		
		Descuentos descuentos= new Descuentos(getImportes().getImporte(), this.getDescuento().concat(",").concat(this.getExtras()));
		double temporal= descuentos.toImporte();
		getImportes().setSubTotal(Numero.toRedondear(temporal<= 0? getImportes().getImporte(): temporal));
	  
		temporal= descuentos.toImporte(this.getDescuento());
		getImportes().setDescuento(Numero.toRedondear(temporal> 0? getImportes().getImporte()- temporal: 0D));
		
		temporal= descuentos.toImporte(this.getExtras());
		getImportes().setExtra(Numero.toRedondear(temporal> 0? (getImportes().getImporte()- getImportes().getSubTotal())- getImportes().getDescuento(): 0D));

		if(isSinIva()) {
	  	getImportes().setIva(Numero.toRedondear(getImportes().getSubTotal()- (getImportes().getSubTotal()/(1+ porcentajeIva))));
	  	getImportes().setSubTotal(Numero.toRedondear(getImportes().getSubTotal()- getImportes().getIva()));
		} // if	
		else {
	  	getImportes().setIva(Numero.toRedondear((getImportes().getSubTotal()* (1+ porcentajeIva))- getImportes().getSubTotal()));
		} // else
		getImportes().setTotal(Numero.toRedondear(getImportes().getSubTotal() + getImportes().getIva()));
		this.setSubTotal(getImportes().getSubTotal());
		this.setImpuestos(getImportes().getIva());
		this.setDescuentos(getImportes().getDescuento());		
		this.setDescuentoDescripcion(!Cadena.isVacio(this.getDescuento()) && !this.getDescuento().equals("0") ? this.getDescuento().concat("% [ $").concat(String.valueOf(getImportes().getDescuento())).concat(" ] ") : "0");
		this.setExcedentes(getImportes().getExtra());
		this.setImporte(Numero.toRedondear(getImportes().getTotal()));
		this.setUtilidad(utilidad);
		this.toDiferencia();
	}
	
	@Override
	public double getDiferenciaCosto() {
		return Numero.toRedondear(getDiferenciaReal());
	}
		
	@Override
	public void toDiferencia() {
		Descuentos descuentos= new Descuentos(this.getCosto(), this.getDescuento());
		double value= descuentos.toImporte();
		setCalculado(Numero.toRedondear(value== 0? this.getCosto(): value));
		descuentos= new Descuentos(this.getCosto(), this.getDescuento().concat(",").concat(this.getExtras()));
		value= descuentos.toImporte();
		setReal(Numero.toRedondear(value== 0? this.getCosto(): value));
		value= Numero.toRedondear((value== 0? this.getCosto(): value)- this.getValor()); 
  	setDiferencia(this.getValor()== 0? 0: Numero.toRedondear(value* 100/ this.getValor()));
	}	// toDiferencia
}
