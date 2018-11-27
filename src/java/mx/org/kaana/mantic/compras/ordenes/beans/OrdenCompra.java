package mx.org.kaana.mantic.compras.ordenes.beans;

import java.io.Serializable;
import java.sql.Date;
import java.util.Calendar;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.mantic.db.dto.TcManticOrdenesComprasDto;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 8/05/2018
 *@time 10:29:26 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class OrdenCompra extends TcManticOrdenesComprasDto implements Serializable {

	private static final long serialVersionUID=3088884892456452488L;
	
	private UISelectEntity ikEmpresa;
	private UISelectEntity ikAlmacen;
	private UISelectEntity ikCliente;
	private UISelectEntity ikProveedor;
	private UISelectEntity ikProveedorPago;

	public OrdenCompra() {
		this(-1L);
	}

	public OrdenCompra(Long key) {
		this(-1L, 0D, -1L, -1L, "0.00", -1L, "0.00", new Long(Calendar.getInstance().get(Calendar.YEAR)), Calendar.getInstance().get(Calendar.YEAR)+ "00000", 2L, 0D, 1L, new Date(Calendar.getInstance().getTimeInMillis()), 1L, -1L, 0D, 0D, 1D, 2L, "", -1L, 1L, 0D);
	}

	public OrdenCompra(Long idProveedorPago, Double descuentos, Long idProveedor, Long idCliente, String descuento, Long idOrdenCompra, String extras, Long ejercicio, String consecutivo, Long idGasto, Double total, Long idOrdenEstatus, Date entregaEstimada, Long idUsuario, Long idAlmacen, Double impuestos, Double subTotal, Double tipoDeCambio, Long idSinIva, String observaciones, Long idEmpresa, Long orden, Double excedentes) {
		super(idProveedorPago, descuentos, idProveedor, idCliente, descuento, idOrdenCompra, extras, ejercicio, consecutivo, idGasto, total, idOrdenEstatus, entregaEstimada, idUsuario, idAlmacen, impuestos, subTotal, tipoDeCambio, idSinIva, observaciones, idEmpresa, orden, excedentes);
	}

	public UISelectEntity getIkEmpresa() {
		return ikEmpresa;
	}

	public void setIkEmpresa(UISelectEntity ikEmpresa) {
		this.ikEmpresa=ikEmpresa;
		if(this.ikEmpresa!= null)
		  this.setIdEmpresa(this.ikEmpresa.getKey());
	}

	public UISelectEntity getIkAlmacen() {
		return ikAlmacen;
	}

	public void setIkAlmacen(UISelectEntity ikAlmacen) {
		this.ikAlmacen=ikAlmacen;
		if(this.ikAlmacen!= null)
		  this.setIdAlmacen(this.ikAlmacen.getKey());
	}

	public UISelectEntity getIkCliente() {
		return ikCliente;
	}

	public void setIkCliente(UISelectEntity ikCliente) {
		this.ikCliente=ikCliente;
		if(this.ikCliente!= null)
		  this.setIdCliente(this.ikCliente.getKey());
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
	
	@Override
	public Class toHbmClass() {
		return TcManticOrdenesComprasDto.class;
	}
	
}
