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
import mx.org.kaana.mantic.db.dto.TcManticNotasDetallesDto;
import mx.org.kaana.mantic.db.dto.TcManticOrdenesDetallesDto;
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
	private Long idRedondear;
	private double valor;
	private boolean ultimo;
	private boolean solicitado;
	private UISelectEntity idEntity;

	public Articulo() {
		this(-1L);
	}

	public Articulo(Long key) {
		this(false, 1.0, "", "", 0.0, "0", -1L, "0", 0D, "", 16D, 0D, 0D, 1L, -1L, key, 0D, -1L, false, false);
	}

	public Articulo(boolean conIva, double tipoDeCambio, String nombre, String codigo, Double costo, String descuento, Long idOrdenCompra, String extras, Double importe, String propio, Double iva, Double totalImpuesto, Double subTotal, Long cantidad, Long idOrdenDetalle, Long idArticulo, Double totalDescuentos, Long idProveedor, boolean ultimo, boolean solicitado) {
		super(idArticulo, codigo, costo, descuento, extras, importe, new Timestamp(Calendar.getInstance().getTimeInMillis()), propio, iva, totalImpuesto, subTotal, cantidad, totalDescuentos, nombre, "", "");
		this.idEntity    = new UISelectEntity(new Entity(-1L));
		this.idProveedor = idProveedor;
		this.sinIva      = conIva;
		this.importes    = new Totales();
		this.tipoDeCambio= tipoDeCambio;
		this.valor       = costo;
		this.ultimo      = ultimo;
		this.solicitado  = solicitado;
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

	public Long getIdRedondear() {
		return idRedondear;
	}

	public void setIdRedondear(Long idRedondear) {
		this.idRedondear=idRedondear;
	}

	public void setValor(double valor) {
		this.valor=valor;
		super.setCosto(valor);
	}

	public double getValor() {
		return valor;
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
	
	public String getImporte$() {
		return Global.format(EFormatoDinamicos.MILES_CON_DECIMALES, this.getImporte());
	}


	public String getDiferencia() {
		double diferencia= this.toDiferencia();
		String color     = diferencia< -5? "janal-color-orange": diferencia> 5? "janal-color-blue": "janal-color-green";
		return "<span class='".concat(color).concat("' style='float:right;'>").concat(String.valueOf(diferencia)).concat("</span>");
	}

	public String getCostoMayorMenor() {
		double diferencia= this.toDiferencia();
		String color     = diferencia< -5? "janal-color-orange": diferencia> 5? "janal-color-blue": "janal-color-green";
		boolean display  = diferencia!= 0D;
		return "<i class='fa fa-fw fa-question-circle ".concat(color).concat("' style='float:right; display:").concat(display? "": "none").concat("' title='Diferencia: ").concat(String.valueOf(diferencia)).concat("%'></i>");
	}
	
	public String getEstaSolicitado() {
		String color= "janal-color-green";
		return "<i class='fa fa-fw fa-question-circle ".concat(color).concat("' style='float:right; display:").concat(this.solicitado? "": "none").concat("' title='El articulo esta solicitado en una orden de compra vigente !'></i>");
	}
	
	public String getPrecioSugerido() {
		String color= "janal-color-orange";
		return "<i class='fa fa-fw fa-question-circle ".concat(color).concat("' style='float:right; display:").concat(this.ultimo? "": "none").concat("' title='El articulo tiene un precio especial de un proveedor !'></i>");
	}
	
	public void toPrepare(boolean conIva, Double tipoDeCambio, Long idProvedores) {
		this.sinIva      = conIva;
		this.tipoDeCambio= tipoDeCambio;
		this.idProveedor = idProvedores;
	}
	
	private void toCalculate() {
		double porcentajeIva = this.getIva()/ 100;       
		double costoMoneda   = this.getCosto()* this.tipoDeCambio;
		double costoReal     = this.getCantidad()* costoMoneda;
		double costoSinIva   = Numero.toRedondear(this.getCosto()* (1- porcentajeIva));
		if(this.sinIva) 
		  this.importes.setImporte(Numero.toRedondear(this.getCantidad()* costoSinIva* this.tipoDeCambio));
		else
			this.importes.setImporte(Numero.toRedondear(costoReal));
		Descuentos descuentos= new Descuentos(this.importes.getImporte(), this.getDescuento().concat(",").concat(this.getExtras()));
		this.importes.setSubTotal(Numero.toRedondear(descuentos.toImporte()));
		double temporal= Numero.toRedondear(this.importes.getImporte()- this.importes.getSubTotal());
		this.importes.setDescuento(Numero.toRedondear(descuentos.toImporte(this.getDescuento())- this.importes.getSubTotal()));
		this.importes.setExtra(temporal- this.importes.getDescuento());
		this.importes.setIva(Numero.toRedondear((this.importes.getSubTotal()* (1+ porcentajeIva))- this.importes.getSubTotal()));
		this.importes.setTotal(Numero.toRedondear(this.importes.getSubTotal()+ this.importes.getIva()));
		this.setSubTotal(this.importes.getSubTotal());
		this.setTotalImpuesto(this.importes.getIva());
		this.setTotalDescuentos(this.importes.getDescuentos());
		this.setImporte(Numero.toRedondear(this.importes.getTotal()));
	}

	public void toCalculate(boolean sinIva, double tipoDeCambio) {
		this.sinIva      = sinIva;
		this.tipoDeCambio= tipoDeCambio;
		this.toCalculate();
	}

	@Override
	public int compareTo(Articulo current) {
		int x= this.getIdArticulo().compareTo(current.getIdArticulo());
		if (x!= 0) 
      return x;
		else 
			return current.getCosto().compareTo(this.getCosto());
	}
	
	private double toDiferencia() {
  	return this.valor> 0? Numero.toRedondear(this.getCosto()* 100/ this.valor- 100): 0D;
	}
	
	public TcManticNotasDetallesDto toNotaDetalle() {
		return new TcManticNotasDetallesDto(
			Cadena.isVacio(this.getCodigo())? this.getPropio(): this.getCodigo(), 
			this.getUnidadMedida(), 
			this.getCosto(), 
			this.getDescuento(), 
			this.getSat(), 
			this.getExtras(), 
			-1L, /*idNotaEntrada, */
			this.getNombre(), 
			this.getImporte(), 
			this.getIva(), 
			-1L, /*idNotaDetalle, */
			this.getSubTotal(), 
			this.getCantidad(), 
			this.getIdArticulo(), 
			this.getTotalDescuentos(), 
			this.getTotalImpuesto()
		);
	}

	public TcManticOrdenesDetallesDto toOrdenDetalle() {
		return new TcManticOrdenesDetallesDto(
			Cadena.isVacio(this.getCodigo())? this.getPropio(): this.getCodigo(), 
			this.getCosto(), 
			this.getDescuento(), 
			-1L, /*idOrdenCompra, */
			this.getExtras(), 
			this.getNombre(), 
			this.getImporte(), 
			this.getPropio(), 
			this.getIva(), 
			this.getTotalImpuesto(), 
			this.getSubTotal(), 
			this.getCantidad(), 
			-1L , /*idOrdenDetalle, */
			this.getIdArticulo(), 
			this.getTotalDescuentos()
		);
	}

	@Override
	public String toString() {
		return "Articulo{"+"idProveedor="+idProveedor+", importes="+importes+", sinIva="+sinIva+", tipoDeCambio="+tipoDeCambio+", idRedondear="+idRedondear+", valor="+valor+", ultimo="+ultimo+", solicitado="+solicitado+'}';
	}
	
	
	public static void main(String ... args) {
		Articulo articulo= new Articulo(-1L);
		articulo.setCosto(1131.63);
		articulo.setCantidad(10L);
		articulo.setDescuento("5");
		articulo.setExtras("5");
		articulo.setIva(16D);
		articulo.toCalculate();
		LOG.info(articulo);
	}	
	
}
