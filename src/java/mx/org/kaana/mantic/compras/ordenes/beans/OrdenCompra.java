package mx.org.kaana.mantic.compras.ordenes.beans;

import java.io.Serializable;
import java.sql.Date;
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
	
	private UISelectEntity ikAlmacen;
	private UISelectEntity ikCliente;
	private UISelectEntity ikProveedor;
	private UISelectEntity ikProveedorPago;

	public OrdenCompra() {
	}

	public OrdenCompra(Long key) {
		super(key);
	}

	public OrdenCompra(Long idProveedor, Long idProveedorPago, Long idCliente, String descuento, Long idOrdenCompra, String extras, String consecutivo, Long idTipoCompra, Long idGasto, Double total, Date entregaEstimada, Long idUsuario, Long idAlmacen, Double subtotal, Double impuestos, Double tipoDeCambio, String observaciones, Long idEmpresa, Long idEstatus) {
		super(idProveedor, idProveedorPago, idCliente, descuento, idOrdenCompra, extras, consecutivo, idTipoCompra, idGasto, total, entregaEstimada, idUsuario, idAlmacen, subtotal, impuestos, tipoDeCambio, observaciones, idEmpresa, idEstatus);
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
