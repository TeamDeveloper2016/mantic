package mx.org.kaana.mantic.compras.entradas.beans;

import java.io.Serializable;
import java.sql.Date;
import java.util.Calendar;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.mantic.compras.ordenes.enums.EOrdenes;
import mx.org.kaana.mantic.db.dto.TcManticNotasEntradasDto;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 8/05/2018
 *@time 10:29:26 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class NotaEntrada extends TcManticNotasEntradasDto implements Serializable {

	private static final long serialVersionUID=3088884892456452488L;
	
	private UISelectEntity ikAlmacen;
	private UISelectEntity ikProveedor;
	private UISelectEntity ikOrdenCompra;

	public NotaEntrada() {
		this(null);
	}

	public NotaEntrada(Long idOrdenCompra) {
		this(-1L, idOrdenCompra);
	}

	public NotaEntrada(Long key, Long idOrdenCompra) {
		super(0D, null, "0", idOrdenCompra, 1L, new Date(Calendar.getInstance().getTimeInMillis()), "0", key, new Date(Calendar.getInstance().getTimeInMillis()), 1L, new Long(Calendar.getInstance().get(Calendar.YEAR)), Calendar.getInstance().get(Calendar.YEAR)+ "00000", 0D, "", 1L, -1L, 0D, 0D, 1D, 2L, "", -1L, 1L);
	}

	public NotaEntrada(Double descuentos, Long idProveedor, String descuento, Long idOrdenCompra, Long idDirecta, Date fechaRecepcion, String extras, Long idNotaEntrada, Date fechaFactura, Long idNotaEstatus, Long ejercicio, String consecutivo, Double total, String factura, Long idUsuario, Long idAlmacen, Double subTotal, Double impuestos, Double tipoDeCambio, Long idSinIva, String observaciones, Long idEmpresa, Long orden) {
		super(descuentos, idProveedor, descuento, idOrdenCompra, idDirecta, fechaRecepcion, extras, idNotaEntrada, fechaFactura, idNotaEstatus, ejercicio, consecutivo, total, factura, idUsuario, idAlmacen, subTotal, impuestos, tipoDeCambio, idSinIva, observaciones, idEmpresa, orden);
	}

	public UISelectEntity getIkAlmacen() {
		return ikAlmacen;
	}

	public void setIkAlmacen(UISelectEntity ikAlmacen) {
		this.ikAlmacen=ikAlmacen;
		if(this.ikAlmacen!= null)
		  this.setIdAlmacen(this.ikAlmacen.getKey());
	}

	public UISelectEntity getIkProveedor() {
		return ikProveedor;
	}

	public void setIkProveedor(UISelectEntity ikProveedor) {
		this.ikProveedor=ikProveedor;
		if(this.ikProveedor!= null)
		  this.setIdProveedor(this.ikProveedor.getKey());
	}

	public UISelectEntity getIkOrdenCompra() {
		return ikOrdenCompra;
	}

	public void setIkOrdenCompra(UISelectEntity ikOrdenCompra) {
		this.ikOrdenCompra=ikOrdenCompra;
		if(this.ikOrdenCompra!= null)
  	  this.setIdOrdenCompra(this.ikOrdenCompra.getKey());
	}

	
	@Override
	public Class toHbmClass() {
		return TcManticNotasEntradasDto.class;
	}
	
}
