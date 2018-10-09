package mx.org.kaana.mantic.compras.ordenes.beans;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Calendar;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Global;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.mantic.compras.ordenes.reglas.Descuentos;
import mx.org.kaana.mantic.comun.beans.ArticuloDetalle;
import mx.org.kaana.mantic.db.dto.TcManticDevolucionesDetallesDto;
import mx.org.kaana.mantic.db.dto.TcManticFicticiasDetallesDto;
import mx.org.kaana.mantic.db.dto.TcManticGarantiasDetallesDto;
import mx.org.kaana.mantic.db.dto.TcManticNotasDetallesDto;
import mx.org.kaana.mantic.db.dto.TcManticOrdenesDetallesDto;
import mx.org.kaana.mantic.db.dto.TcManticRequisicionesDetallesDto;
import mx.org.kaana.mantic.db.dto.TcManticServiciosDetallesDto;
import mx.org.kaana.mantic.db.dto.TcManticVentasDetallesDto;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 8/05/2018
 *@time 03:09:42 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Articulo extends ArticuloDetalle implements Comparable<Articulo>, Serializable {

	private static final Log LOG              = LogFactory.getLog(ArticuloDetalle.class);
	private static final long serialVersionUID= 329661715469035396L;

  private Long idProveedor;	
	private Totales importes;
	private boolean sinIva;
	private double tipoDeCambio;
	private boolean ultimo;
	private boolean solicitado;
	private double stock;
	private UISelectEntity idEntity;
	private String observacion;
	private double diferencia;
	private double real;
	private double calculado;

	public Articulo() {
		this(-1L);
	}

	public Articulo(Long key) {
		this(false, 1.0, "", "", 0.0, "0", -1L, "0", 0D, "", 16D, 0D, 0D, 1D, -1L, key, 0D, -1L, false, false, 0L, 0D, "", "");
	}

	public Articulo(boolean sinIva, double tipoDeCambio, String nombre, String codigo, Double costo, String descuento, Long idOrdenCompra, String extras, Double importe, String propio, Double iva, Double totalImpuesto, Double subTotal, Double cantidad, Long idOrdenDetalle, Long idArticulo, Double totalDescuentos, Long idProveedor, boolean ultimo, boolean solicitado, double stock, Double excedentes, String sat, String unidadMedida) {
	  this(sinIva, tipoDeCambio, nombre, codigo, costo, descuento, idOrdenCompra, extras, importe, propio, iva, totalImpuesto, subTotal, cantidad, idOrdenDetalle, idArticulo, totalDescuentos, idProveedor, ultimo, solicitado, stock, excedentes, sat, unidadMedida, 1L);
	}
	
	public Articulo(boolean sinIva, double tipoDeCambio, String nombre, String codigo, Double costo, String descuento, Long idOrdenCompra, String extras, Double importe, String propio, Double iva, Double totalImpuesto, Double subTotal, Double cantidad, Long idOrdenDetalle, Long idArticulo, Double totalDescuentos, Long idProveedor, boolean ultimo, boolean solicitado, double stock, Double excedentes, String sat, String unidadMedida, Long idAplicar) {
		super(idArticulo, codigo, costo, descuento, extras, importe, new Timestamp(Calendar.getInstance().getTimeInMillis()), propio, iva, totalImpuesto, subTotal, cantidad, totalDescuentos, nombre, sat, unidadMedida, excedentes, idAplicar);
		this.idEntity    = new UISelectEntity(new Entity(-1L));
		this.idProveedor = idProveedor;
		this.sinIva      = sinIva;
		this.importes    = new Totales();
		this.tipoDeCambio= tipoDeCambio;
		this.ultimo      = ultimo;
		this.solicitado  = solicitado;
    this.stock       = stock;
		this.observacion = "";
		this.diferencia  = 0D;
		this.real        = costo;
		this.calculado   = costo;
	}

	public UISelectEntity getIdEntity() {
		return idEntity;
	}

	public void setIdEntity(UISelectEntity idEntity) {
		this.idEntity=idEntity;
	}

	public boolean isConIva() {
		return sinIva;
	}

	public Totales getImportes() {
		return importes;
	}

	@Override
	public Long getIdArticulo() {
		return super.getIdArticulo();
	}

	@Override
	public void setIdArticulo(Long idArticulo) {
		super.setIdArticulo(idArticulo);
	}

	@Override
	public Long getKey() {
		return super.getIdArticulo();
	}

	@Override
	public boolean isValid() {
		return this.getKey()!= null &&  this.getKey()> 0;
	}

	public Long getIdProveedor() {
		return idProveedor;
	}

	public void setIdProveedor(Long idProveedor) {
		this.idProveedor=idProveedor;
	}

	public boolean isUltimo() {
		return ultimo;
	}

	public void setUltimo(boolean ultimo) {
		this.ultimo=ultimo;
	}

	public boolean isSolicitado() {
		return solicitado;
	}

	public void setSolicitado(boolean solicitado) {
		this.solicitado=solicitado;
	}

	public double getStock() {
		return stock;
	}

	public void setStock(double stock) {
		this.stock=stock;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion=observacion;
	}
	
	public String getImporte$() {
		return Global.format(EFormatoDinamicos.MILES_SAT_DECIMALES, this.getImporte());
	}

	public String getDiferencia() {
		String color     = this.diferencia< -5? "janal-color-orange": Numero.toRedondear(this.diferencia)> 5? "janal-color-blue": "janal-color-green";
		return "<span class='".concat(color).concat("' style='float:left;'>[").concat(String.valueOf(Numero.toRedondear(this.diferencia))).concat("%]</span>");
	}

	public double getReal() {
		return real;
	}

	public double getCalculado() {
		return calculado;
	}

	public String getCostoMayorMenor() {
		String color     = this.diferencia< -5? "janal-color-orange": this.diferencia> 5? "janal-color-blue": "janal-color-green";
		boolean display  = this.diferencia!= 0D;
		return "<i class='fa fa-fw fa-question-circle ".concat(color).concat("' style='float:right; display:").concat(display? "": "").concat("' title='")
		.concat("Costo: ").concat(Global.format(EFormatoDinamicos.MONEDA_CON_DECIMALES, this.getCosto())
		).concat("\nCosto real: ").concat(Global.format(EFormatoDinamicos.MONEDA_CON_DECIMALES, this.real)
		).concat("\nCosto calculado: ").concat(Global.format(EFormatoDinamicos.MONEDA_CON_DECIMALES, this.calculado)
		).concat("\n\nIVA: ").concat(Global.format(EFormatoDinamicos.NUMERO_CON_DECIMALES, this.getIva())
		).concat("%\n\nCosto anterior: ").concat(Global.format(EFormatoDinamicos.MONEDA_CON_DECIMALES, this.getValor())
		).concat("\nCosto digitado: ").concat(Global.format(EFormatoDinamicos.MONEDA_CON_DECIMALES, Math.abs(this.real))
		).concat("\nDiferencia: ").concat(String.valueOf(this.diferencia)).concat("%'></i>");
	}

	public String getCantidadMayorMenor() {
		double difieren  = this.getCantidad()- this.getSolicitados();
		String color     = difieren< -5? "janal-color-orange": difieren> 5? "janal-color-blue": "janal-color-green";
		boolean display  = difieren!= 0D;
		return "<i class='fa fa-fw fa-question-circle ".concat(color).concat("' style='float:right; display:").concat(display? "": "none").concat("' title='Cantidad solicitada: ").concat(
			Global.format(EFormatoDinamicos.NUMERO_SIN_DECIMALES, this.getSolicitados())
		).concat("\n\nDiferencia: ").concat(Global.format(EFormatoDinamicos.NUMERO_SIN_DECIMALES, difieren)).concat("'></i>");
	}
	
	public String getEstaSolicitado() {
		String color= "janal-color-green";
		return "<i class='fa fa-fw fa-question-circle ".concat(color).concat("' style='float:right; display:").concat(this.solicitado? "": "none").concat("' title='El articulo esta solicitado en una orden de compra vigente !'></i>");
	}
	
	public String getPrecioSugerido() {
		String color= "janal-color-orange";
		return "<i class='fa fa-fw fa-question-circle ".concat(color).concat("' style='float:right; display:").concat(this.ultimo? "": "none").concat("' title='El articulo tiene un precio especial de un proveedor !'></i>");
	}
	
	public void toPrepare(boolean sinIva, Double tipoDeCambio, Long idProvedores) {
		this.sinIva      = sinIva;
		this.tipoDeCambio= tipoDeCambio;
		this.idProveedor = idProvedores;
	}
	
	private void toCalculate() {
		double porcentajeIva = 1+ (this.getIva()/ 100);
		double costoMoneda   = this.getCosto()* this.tipoDeCambio;
		double costoReal     = this.getCantidad()* costoMoneda;
		double utilidad      = (this.getCosto()*this.getCantidad()) - (this.getPrecio()*this.getCantidad());
		this.importes.setImporte(Numero.toRedondearSat(costoReal));
		
		Descuentos descuentos= new Descuentos(this.importes.getImporte(), this.getDescuento().concat(",").concat(this.getExtras()));
		double temporal= descuentos.toImporte();
		this.importes.setSubTotal(Numero.toRedondearSat(temporal<= 0? this.importes.getImporte(): temporal));
	  
		temporal= descuentos.toImporte(this.getDescuento());
		this.importes.setDescuento(Numero.toRedondearSat(temporal> 0? this.importes.getImporte()- temporal: 0D));
		
		temporal= descuentos.toImporte(this.getExtras());
		this.importes.setExtra(Numero.toRedondearSat(temporal> 0? (this.importes.getImporte()- this.importes.getSubTotal())- this.importes.getDescuento(): 0D));

		if(this.sinIva) {
	  	this.importes.setIva(Numero.toRedondearSat(this.importes.getSubTotal()- (this.importes.getSubTotal()/ porcentajeIva)));
	  	this.importes.setSubTotal(Numero.toRedondearSat(this.importes.getSubTotal()- this.importes.getIva()));
		} // if	
		else {
	  	this.importes.setIva(Numero.toRedondearSat((this.importes.getSubTotal()* porcentajeIva)- this.importes.getSubTotal()));
		} // else
		this.importes.setTotal(Numero.toRedondearSat(this.importes.getSubTotal()+ this.importes.getIva()));
		
		// esto es para ajustar los importes quitando el descuento extra que se añade porque no debe de afecta el importe total de la factura
		this.importes.setIva(Numero.toRedondearSat(this.importes.getIva()+ (this.importes.getExtra()* porcentajeIva- this.importes.getExtra())));
		this.importes.setSubTotal(Numero.toRedondearSat(this.importes.getSubTotal()+ this.importes.getExtra()));
		this.importes.setTotal(Numero.toRedondearSat(this.importes.getTotal()+ (this.importes.getExtra()* porcentajeIva)));
    // termina aqui los ajustes de los descuentos extras que se asignaron a los articulos 		
		
		this.setSubTotal(this.importes.getSubTotal());
		this.setImpuestos(this.importes.getIva());
		this.setDescuentos(this.importes.getDescuento());		
		this.setDescuentoDescripcion(!Cadena.isVacio(this.getDescuento()) && !this.getDescuento().equals("0") ? this.getDescuento().concat("% [ $").concat(String.valueOf(this.importes.getDescuento())).concat(" ] ") : "0");
		this.setExcedentes(this.importes.getExtra());
		this.setImporte(Numero.toRedondearSat(this.importes.getTotal()));
		this.setUtilidad(utilidad);
		this.toDiferencia();
	}

	public void toCalculate(boolean sinIva, double tipoDeCambio) {
		this.sinIva      = sinIva;
		this.tipoDeCambio= tipoDeCambio;
		this.toCalculate();
	}

	public boolean autorizedDiscount(){
		boolean regresar= false;
		Double utilidad = (this.getCosto()*this.getCantidad()) - (this.getPrecio()*this.getCantidad());
		Descuentos descuentos= new Descuentos(this.importes.getImporte(), this.getDescuento().concat(",").concat(this.getExtras()));
		regresar= (this.importes.getImporte() - descuentos.toImporte()) < utilidad;
		if(!regresar)
			setDescuento("");
		return regresar;
	} // autorizedDiscount
	
	@Override
	public int compareTo(Articulo current) {
		int x= this.getNombre().compareTo(current.getNombre());
		if (x!= 0) 
      return x;
		else 
			return current.getCosto().compareTo(this.getCosto());
	}
	
	public double getDiferenciaCosto() {
		return Numero.toRedondearSat(this.diferencia);
	}
		
	private void toDiferencia() {
		Descuentos descuentos= new Descuentos(this.getCosto(), this.getDescuento());
		double value  = descuentos.toImporte();
		this.calculado= Numero.toRedondearSat(value== 0? this.getCosto(): value);
		descuentos    = new Descuentos(this.getCosto(), this.getDescuento().concat(",").concat(this.getExtras()));
		value         = descuentos.toImporte();
		this.real     = Numero.toRedondearSat(value== 0? this.getCosto(): value);
		value= Numero.toRedondearSat((value== 0? this.getCosto(): value)- this.getValor()); 
  	this.diferencia= this.getValor()== 0? 0: Numero.toRedondearSat(value* 100/ this.getValor());
	}	

	public TcManticNotasDetallesDto toNotaDetalle() {
		return new TcManticNotasDetallesDto(
			this.getCodigo(), 
			this.getUnidadMedida(), 
			this.getCosto(), 
			this.getDescuento(), 
			this.getSat(), 
			this.getExtras(), 
			-1L, /*idNotaEntrada, */
			this.getNombre(), 
			this.getImporte(), 
			this.getIva(), 
			this.getIdComodin(), /*idNotaDetalle, */
			this.getSubTotal(), 
			this.getCantidad(), 
			this.getIdArticulo(), 
			this.getDescuentos(), 
			this.getImpuestos(),
			this.getIdOrdenDetalle(),
			this.getCantidad(),
			this.getExcedentes(),
			this.getIdAplicar(),
			this.getIdOrdenDetalle()== null? this.getSolicitados(): 0D,
			this.getIdOrdenDetalle()== null? this.getCantidad()- this.getSolicitados(): 0D,
			this.real,
			this.calculado
		);	
	}

	public TcManticOrdenesDetallesDto toOrdenDetalle() {
		return new TcManticOrdenesDetallesDto(
			this.getImporte(),
			this.getDescuentos(), 
			this.getCodigo(), 
			this.getCosto(), 
			this.getDescuento(),
			-1L, /*idOrdenCompra, */
			this.getExtras(), 
			this.getNombre(), 
			this.getImporte(),
			this.getCosto(),
			this.getPropio(), 
			this.getCantidad(),
			this.getIva(), 
			this.getImpuestos(), 
			this.getSubTotal(), 
			this.getCantidad(), 
			this.getIdComodin(), /*idOrdenDetalle, */
			this.getIdArticulo(),
		  this.getExcedentes(),
			this.real,
			this.calculado
		);
	}
	
	public TcManticVentasDetallesDto toVentaDetalle() {
		return new TcManticVentasDetallesDto(
			this.getDescuentos(),
			Cadena.isVacio(this.getCodigo())? this.getPropio(): this.getCodigo(), 
			this.getUnidadMedida(),
			this.getCosto(), 
			this.getDescuento(), 
			this.getSat(),
			this.getExtras(), 
			this.getNombre(), 
			this.getImporte(), 
      this.getIdComodin(), /*idVentaDetalle, */
			this.getIva(), 
			this.getImpuestos(), 
			this.getSubTotal(), 
			this.getCantidad(), 
			this.getIdArticulo(),
			-1L, /*idVenta, */
			this.getPrecio(),
			this.getUtilidad()
		);
	}
	
	public TcManticGarantiasDetallesDto toGarantiaDetalle() {		
		return new TcManticGarantiasDetallesDto(
			-1L, // idGarantia, 
			this.getDescuentos(), // descuentos, 
			this.getIdProveedor(), // idProveedor, 
			this.getDescuento(), // descuento, 
			this.getExtras(), // extras, 
			this.getUtilidad(), // utilidad, 
			this.getImporte(), // importe, 
			-1L, // idGarantiaDetalle, 
			this.getIdComodin(), // idVentaDetalle, 
			this.getIva(), // iva, 
			this.getImpuestos(), // impuestos, 
			this.getSubTotal(), // subTotal, 
			this.getCantidad(), // cantidad, 
			2L// idReparacion
		);
	}
	
	public TcManticFicticiasDetallesDto toFicticiaDetalle() {
		return new TcManticFicticiasDetallesDto(
			this.getDescuentos(),
			Cadena.isVacio(this.getCodigo())? this.getPropio(): this.getCodigo(), 
			this.getUnidadMedida(),
			this.getCosto(), 
			-1L, // idFicticia
			this.getDescuento(), 
			this.getSat(),
			this.getExtras(), 
			this.getUtilidad(),
			this.getNombre(), 
			this.getImporte(), 
      this.getIdComodin(), /*idFicticiaDetalle, */
			this.getPrecio(),
			this.getIva(), 
			this.getImpuestos(), 
			this.getSubTotal(), 
			this.getCantidad(), 
			this.getIdArticulo()			
		);
	}
	
	public TcManticRequisicionesDetallesDto toRequisicionDetalle() {
		return new TcManticRequisicionesDetallesDto(			
			-1L, /*idVenta, */
			this.getPropio(),
			this.getUnidadMedida(),
			this.getCantidad(),
			this.getIdArticulo(),
			-1L,
			this.getNombre()
		);
	}
	
	public TcManticServiciosDetallesDto toServicioDetalle() {
		return new TcManticServiciosDetallesDto(
			this.getCodigo(), 
			this.getCosto(), 
			this.getDescuento(), 
			-1L, 
			-1L, 
			this.getImporte(), 
			this.getPropio(), 
			this.getIva(), 
			-1L, 
			this.getImpuestos(), 
			-1L, 
			"", 
			this.getSubTotal(), 
			this.getCantidad().longValue(), 
			this.getIdArticulo()
		);			
	}

	public TcManticDevolucionesDetallesDto toDevolucionDetalle() {
	  return new TcManticDevolucionesDetallesDto(
			-1L, // idDevolucion, 
			this.getIdOrdenDetalle(),
			this.getCantidad(), 
			this.getIdComodin()// idDevolucionDetalle
		);
	}
	
	@Override
	public String toString() {
		return String.valueOf(this.getIdArticulo());
	}
	
	public static void main(String ... args) {
		Articulo articulo= new Articulo(-1L);
		articulo.setCosto(1131.63);
		articulo.setCantidad(10D);
		articulo.setDescuento("5");
		articulo.setExtras("5");
		articulo.setIva(16D);
		articulo.toCalculate();
		LOG.info(articulo);
	}		
}