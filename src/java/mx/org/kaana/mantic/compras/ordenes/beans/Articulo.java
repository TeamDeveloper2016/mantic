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
	private String nombre;
	private Totales importes;
	private double costo;
	private boolean sinIva;
	private double tipoDeCambio;
	private UISelectEntity idEntity;

	public Articulo() {
		this(-1L);
	}

	public Articulo(Long key) {
		this(true, 1.0, "", "", 0.0, "0", -1L, "0", 0D, "", 16D, 0D, 0D, 1L, -1L, -1L, 0D);
	}

	public Articulo(boolean conIva, double tipoDeCambio, String nombre, String codigo, Double costo, String descuento, Long idOrdenCompra, String extras, Double importe, String propio, Double iva, Double totalImpuesto, Double subTotal, Long cantidad, Long idOrdenDetalle, Long idArticulo, Double totalDescuentos) {
		super(codigo, costo, descuento, idOrdenCompra, extras, importe, propio, iva, totalImpuesto, subTotal, cantidad, idOrdenDetalle, idArticulo, totalDescuentos);
		this.idEntity    = new UISelectEntity(new Entity(-1L));
		this.nombre      = nombre;
		this.idArticulo  = idArticulo;
		this.sinIva      = conIva;
		this.importes    = new Totales();
		this.costo       = costo;
		this.tipoDeCambio= tipoDeCambio;
		toCalculate();
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre=nombre;
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
		double costoModena= this.costo* this.tipoDeCambio;
		double costoReal  = this.getCantidad()* costoModena;
		if(this.sinIva) {
			this.setCosto(Numero.toRedondear(costoModena* (1- (this.getIva()/ 100))));
		  this.importes.setImporte(Numero.toRedondear(this.getCantidad()* this.getCosto()));
		} // if
		else
			this.importes.setImporte(Numero.toRedondear(this.getCantidad()* costoModena));
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
	
}
