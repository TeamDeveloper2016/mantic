package mx.org.kaana.mantic.inventarios.entradas.beans;

import java.io.Serializable;
import java.sql.Date;
import java.util.Calendar;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.mantic.db.dto.TcManticNotasEntradasDto;
import mx.org.kaana.mantic.db.dto.TcManticOrdenesComprasDto;

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
  private UISelectEntity ikProveedorPago;
	private UISelectEntity ikOrdenCompra;

	public NotaEntrada() throws Exception {
		this(1L, null);
	}

	public NotaEntrada(Long key, Long idOrdenCompra) throws Exception {
		super(0D, null, "0.00", idOrdenCompra, 1L, new Date(Calendar.getInstance().getTimeInMillis()), "0.00", key, new Date(Calendar.getInstance().getTimeInMillis()), 1L, new Long(Calendar.getInstance().get(Calendar.YEAR)), Calendar.getInstance().get(Calendar.YEAR)+ "00000", 0D, "", 1L, -1L, 0D, 0D, 1D, 2L, "", -1L, 1L, 0D, 30L, new Date(Calendar.getInstance().getTimeInMillis()), 0D, -1L, 0D);
		if(!Cadena.isVacio(idOrdenCompra) && idOrdenCompra> 0L) {
		  TcManticOrdenesComprasDto compra= (TcManticOrdenesComprasDto)DaoFactory.getInstance().findById(TcManticOrdenesComprasDto.class, idOrdenCompra);
		  super.setIdProveedor(compra.getIdProveedor());
			super.setIdProveedorPago(compra.getIdProveedorPago());
			super.setIdAlmacen(compra.getIdAlmacen());
		} // if
	}

	public NotaEntrada(Double descuentos, Long idProveedor, String descuento, Long idOrdenCompra, Long idDirecta, Date fechaRecepcion, String extras, Long idNotaEntrada, Date fechaFactura, Long idNotaEstatus, Long ejercicio, String consecutivo, Double total, String factura, Long idUsuario, Long idAlmacen, Double subTotal, Double impuestos, Double tipoDeCambio, Long idSinIva, String observaciones, Long idEmpresa, Long orden, Double excedentes, Long idProveedorPago) {
		super(descuentos, idProveedor, descuento, idOrdenCompra, idDirecta, fechaRecepcion, extras, idNotaEntrada, fechaFactura, idNotaEstatus, ejercicio, consecutivo, total, factura, idUsuario, idAlmacen, subTotal, impuestos, tipoDeCambio, idSinIva, observaciones, idEmpresa, orden, excedentes, 30L, new Date(Calendar.getInstance().getTimeInMillis()), 0D, idProveedorPago, 0D);
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

	public UISelectEntity getIkProveedorPago() {
		return ikProveedorPago;
	}

	public void setIkProveedorPago(UISelectEntity ikProveedorPago) {
		this.ikProveedorPago=ikProveedorPago;
		if(this.ikProveedorPago!= null)
		  this.setIdProveedorPago(this.ikProveedorPago.getKey());
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
