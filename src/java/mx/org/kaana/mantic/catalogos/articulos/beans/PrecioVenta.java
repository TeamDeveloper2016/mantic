package mx.org.kaana.mantic.catalogos.articulos.beans;

import java.io.Serializable;
import mx.org.kaana.libs.formato.Numero;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 19/09/2019
 *@time 09:02:26 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class PrecioVenta implements Serializable {

	private static final Log LOG=LogFactory.getLog(PrecioVenta.class);
	private static final long serialVersionUID=-5902307046480075898L;

	private Double costo;
	private Double precio;
	private Double utilidad;
	private Double iva;
	private Long idRedondear;

	public PrecioVenta() {
		this(1D, 2L);
	}

	public PrecioVenta(Double costo, Long idRedondear) {
		this(costo, idRedondear, costo);
	}

	public PrecioVenta(Double costo, Long idRedondear, Double precio) {
		this(costo, idRedondear, precio, 50D);
	}

	public PrecioVenta(Double costo, Long idRedondear, Double precio, Double utilidad) {
		this(costo, idRedondear, precio, utilidad, 16D);
	}

	public PrecioVenta(Double costo, Long idRedondear, Double precio, Double utilidad, Double iva) {
		this.costo=costo;
		this.precio=precio;
		this.utilidad=utilidad;
		this.iva=iva;
		this.idRedondear=idRedondear;
		this.precio();
	}

	public Double getCosto() {
		return costo;
	}

	public void setCosto(Double costo) {
		this.costo=costo;
	}

	public Double getPrecio() {
		return precio;
	}

	public void setPrecio(Double precio) {
		this.precio=precio;
	}

	public Double getUtilidad() {
		return utilidad;
	}

	public void setUtilidad(Double utilidad) {
		this.utilidad=utilidad;
	}

	public Double getIva() {
		return iva;
	}

	public void setIva(Double iva) {
		this.iva=iva;
	}
	
	protected Double precio() {
		Double base= this.costo* (1+ (this.iva/ 100));
		this.precio= Numero.toAjustarDecimales(base* (1+ (this.utilidad/ 100)), this.idRedondear.equals(1L));
		return this.precio;
	}
	
	protected Double precio(Double utilidad) { 
		this.utilidad= Numero.toRedondearSat(utilidad);
		return this.precio();
	}

	protected Double utilidad(Double precio) {
		this.precio= Numero.toRedondearSat(precio);
		Double base= this.costo* (1+ (this.iva/ 100));
		this.utilidad= Numero.toRedondearSat((this.precio- base)* 100/ base);
		return this.utilidad;
	}
	
	protected Double especial(Double costo) {
		Double base= this.costo* (1+ (this.iva/ 100));
		this.utilidad= Numero.toRedondearSat((this.precio- base)* 100/ base);
		return Numero.toAjustarDecimales((costo* (1+ (this.iva/ 100)))* (1+ (this.utilidad/ 100)), this.idRedondear.equals(1L));
	}
	
  public static void main(String ... args) {
     PrecioVenta pv= new PrecioVenta(100D, 2L);
		 LOG.info("costo 100: "+ pv.precio());
		 LOG.info("utilidad 173.99999999999997: "+ pv.utilidad(173.99999999999997));
		 LOG.info("precio 100: "+ pv.precio(30D));
		 LOG.info("especial 200: "+ pv.precio(200D));
	}	
	
}
