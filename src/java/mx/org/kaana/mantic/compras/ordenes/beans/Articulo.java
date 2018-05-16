package mx.org.kaana.mantic.compras.ordenes.beans;

import java.io.Serializable;
import java.util.Objects;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.mantic.compras.ordenes.reglas.Descuentos;
import mx.org.kaana.mantic.db.dto.TcManticOrdenesDetallesDto;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 8/05/2018
 *@time 03:09:42 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Articulo extends TcManticOrdenesDetallesDto implements Serializable {

	private static final long serialVersionUID=329661715469035396L;

  private Long idArticulo;	
  private Long idProveedor;	
	private Totales importes;
	private boolean sinIva;
	private double tipoDeCambio;
	private UISelectEntity idEntity;

	public Articulo() {
		this(-1L);
	}

	public Articulo(Long key) {
		this(true, 1.0, "", "", 0.0, "0", -1L, "0", 0D, "", 16D, 0D, 0D, 1L, -1L, -1L, 0D, -1L);
	}

	public Articulo(boolean conIva, double tipoDeCambio, String nombre, String codigo, Double costo, String descuento, Long idOrdenCompra, String extras, Double importe, String propio, Double iva, Double totalImpuesto, Double subTotal, Long cantidad, Long idOrdenDetalle, Long idArticulo, Double totalDescuentos, Long idProveedor) {
		super(codigo, costo, descuento, idOrdenCompra, extras, importe, propio, iva, totalImpuesto, subTotal, cantidad, idOrdenDetalle, idArticulo, totalDescuentos, nombre);
		this.idEntity    = new UISelectEntity(new Entity(-1L));
		this.idArticulo  = idArticulo;
		this.idProveedor = idProveedor;
		this.sinIva      = conIva;
		this.importes    = new Totales();
		this.tipoDeCambio= tipoDeCambio;
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

	public Long getIdArticulo() {
		return idArticulo;
	}

	public Long getIdProveedor() {
		return idProveedor;
	}

	public void toPrepare(boolean conIva, Double tipoDeCambio, Long idProvedores) {
		this.idArticulo  = super.getIdArticulo();
		this.sinIva      = conIva;
		this.tipoDeCambio= tipoDeCambio;
		this.idProveedor = idProvedores;
	}
	
	@Override
	public Class toHbmClass() {
		return TcManticOrdenesDetallesDto.class;
	}

	@Override
	public int hashCode() {
		int hash=3;
		hash=19*hash+Objects.hashCode(this.idArticulo);
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
		final Articulo other=(Articulo) obj;
		if (!Objects.equals(this.idArticulo, other.idArticulo)) {
			return false;
		}
		return true;
	}

	private void toCalculate() {
		double costoMoneda= this.getCosto()* this.tipoDeCambio;
		double costoReal  = this.getCantidad()* costoMoneda;
		double costoSinIva= Numero.toRedondear(this.getCosto()* (1- (this.getIva()/ 100)));
		if(this.sinIva) 
		  this.importes.setImporte(Numero.toRedondear(this.getCantidad()* costoSinIva* this.tipoDeCambio));
		else
			this.importes.setImporte(Numero.toRedondear(costoReal));
		this.importes.setIva(Numero.toRedondear((this.getIva()/ 100)* (this.sinIva? costoReal: this.importes.getImporte())));
		Descuentos descuentos= new Descuentos(this.importes.getImporte());
		this.importes.setDescuento(Numero.toRedondear(descuentos.toImporte(this.getDescuento())));
		this.importes.setExtra(Numero.toRedondear(descuentos.toImporte(this.getExtras())));
		this.importes.setSubTotal(Numero.toRedondear(this.importes.getImporte()- this.importes.getDescuento()- this.importes.getExtra()));
		this.importes.setTotal(Numero.toRedondear(this.importes.getSubTotal()+ this.importes.getIva()));
		this.setImporte(Numero.toRedondear(this.importes.getTotal()));
		
		this.setTotalImpuesto(this.importes.getIva());
		this.setTotalDescuentos(this.importes.getDescuentos());
		this.setSubTotal(this.importes.getSubTotal());
	}

	public void toCalculate(boolean sinIva, double tipoDeCambio) {
		this.sinIva      = sinIva;
		this.tipoDeCambio= tipoDeCambio;
		this.toCalculate();
	}

	public static void main(String ... args) {
		String search= "hola   como estas    ";
		System.out.println(search.replaceAll("( |\\t)+", ".*.*"));
	}	
	
}
