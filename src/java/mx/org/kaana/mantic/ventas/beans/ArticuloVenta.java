package mx.org.kaana.mantic.ventas.beans;

import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.formato.Global;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.mantic.compras.ordenes.beans.Articulo;
import mx.org.kaana.mantic.compras.ordenes.reglas.Descuentos;
import mx.org.kaana.mantic.db.dto.TcManticArticulosDto;

public class ArticuloVenta extends Articulo {	
	
	private static final long serialVersionUID = -7272868284456340705L;

	public ArticuloVenta() {
	}

	public ArticuloVenta(Long key) {
		super(key);
	}

	@Override
	public Double getImporte() {
    return Numero.toRedondear(super.getImporte());
  }
	
	@Override
	public String getImporte$() {
		return Global.format(EFormatoDinamicos.MONEDA_SAT_DECIMALES, this.getImporte()); 
	}
	
	@Override
	public void toCalculate() {
		boolean asignar= this.getImportes().getTotal()<= 0D;
		if(!asignar)
			this.setTotal(this.getTotal());
		this.toCalculateCostoPorCantidad();
		double porcentajeIva = this.getIva()/ 100; 		
		double costoMoneda   = this.getCosto()* getTipoDeCambio();
		double costoReal     = this.getCantidad()* costoMoneda;
		this.getImportes().setImporte(Numero.toRedondear(costoReal));
		
		Descuentos descuentos= new Descuentos(this.getImportes().getImporte(), this.getDescuento().concat(",").concat(this.getExtras()));
		double temporal= descuentos.toImporte();
		this.getImportes().setSubTotal(Numero.toRedondear(temporal<= 0? this.getImportes().getImporte(): temporal));
	  
		// la utilidad es calculada tomando como base el costo menos los descuento y a eso quitarle el precio de lista
		double utilidad      = this.getImportes().getSubTotal()- (this.getPrecio()*this.getCantidad());
		
		temporal= descuentos.toImporte(this.getDescuento());
		this.getImportes().setDescuento(Numero.toRedondear(temporal> 0? this.getImportes().getImporte()- temporal: 0D));
		
		temporal= descuentos.toImporte(this.getExtras());
		this.getImportes().setExtra(Numero.toRedondear(temporal> 0? (this.getImportes().getImporte()- this.getImportes().getSubTotal())- this.getImportes().getDescuento(): 0D));

		if(isSinIva()) {
	  	this.getImportes().setIva(Numero.toRedondear(this.getImportes().getSubTotal()- (this.getImportes().getSubTotal()/(1+ porcentajeIva))));
	  	this.getImportes().setSubTotal(Numero.toRedondear(this.getImportes().getSubTotal()- this.getImportes().getIva()));
		} // if	
		else {
	  	this.getImportes().setIva(Numero.toRedondear((this.getImportes().getSubTotal()* (1+ porcentajeIva))- this.getImportes().getSubTotal()));
		} // else
		this.getImportes().setTotal(Numero.toRedondear(this.getImportes().getSubTotal() + this.getImportes().getIva()));
		this.setSubTotal(this.getImportes().getSubTotal());
		this.setImpuestos(this.getImportes().getIva());
		this.setDescuentos(this.getImportes().getDescuento());		
		this.setDescuentoDescripcion(!Cadena.isVacio(this.getDescuento()) && !this.getDescuento().equals("0") ? this.getDescuento().concat("% [ $").concat(String.valueOf(this.getImportes().getDescuento())).concat(" ] ") : "0");
		this.setExcedentes(this.getImportes().getExtra());
		this.setImporte(Numero.toRedondear(this.getImportes().getTotal()));	
		if(asignar)
			this.setTotal(Numero.toRedondear(this.getImportes().getTotal()));
		this.setUtilidad(utilidad);
		this.toDiferencia();
	}
	
	private void toCalculateCostoPorCantidad() {
		TcManticArticulosDto validate= null;
		try {
			validate= (TcManticArticulosDto) DaoFactory.getInstance().findById(TcManticArticulosDto.class, this.getIdArticulo());
			if(validate!= null && (this.getCosto().equals(validate.getMenudeo()) || this.getCosto().equals(validate.getMedioMayoreo()) || this.getCosto().equals(validate.getMayoreo()))) {
				if(this.getDescuentos()> 0D)
					this.setCosto(validate.getMenudeo());
				else{
					if(this.getCantidad() >= validate.getLimiteMedioMayoreo()) {
						if(this.getCantidad()>= validate.getLimiteMedioMayoreo() && this.getCantidad() < validate.getLimiteMayoreo())
							this.setCosto(validate.getMedioMayoreo());
						else if (this.getCantidad()>= validate.getLimiteMayoreo())
							this.setCosto(validate.getMayoreo());
						else
							this.setCosto(validate.getMenudeo());
					} // if
				} // else						
			} // if
		} // try
		catch (Exception e) {			
			Error.mensaje(e);
		} // catch		
	} // toCalculateCostoPorCantidad
	
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
