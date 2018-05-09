package mx.org.kaana.mantic.compras.ordenes.beans;

import java.io.Serializable;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.libs.pagina.UISelectEntity;
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

	private String nombre;
	private Double subTotal;
	private Double importe;
	private UISelectEntity idEntity;

	public Articulo() {
		this(-1L);
	}

	public Articulo(Long key) {
		super(key);
		this.idEntity= new UISelectEntity(new Entity(-1L));
		this.subTotal= 0D;
		this.importe = 0D;
	}

	public Articulo(Double costo, Double iva, String descuento, Long idOrdenCompra, String extras, Long cantidad, Long idOrdenDetalle, Long idArticulo, String codigo, String sat, Double importe) {
		super(costo, iva, descuento, idOrdenCompra, extras, cantidad, idOrdenDetalle, idArticulo, codigo, sat, importe);
		this.subTotal= 0D;
		this.importe = 0D;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre=nombre;
	}

	public Double getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(Double subTotal) {
		this.subTotal=subTotal;
	}

	public Double getImporte() {
		return importe;
	}

	public void setImporte(Double importe) {
		this.importe=importe;
	}

	public UISelectEntity getIdEntity() {
		return idEntity;
	}

	public void setIdEntity(UISelectEntity idEntity) {
		this.idEntity=idEntity;
	}

	@Override
	public Class toHbmClass() {
		return TcManticOrdenesDetallesDto.class;
	}
	
}
