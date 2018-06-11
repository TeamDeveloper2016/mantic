package mx.org.kaana.mantic.ventas.beans;

import java.io.Serializable;
import java.sql.Date;
import java.util.Calendar;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.mantic.db.dto.TcManticVentasDto;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 8/05/2018
 *@time 10:29:26 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class TicketVenta extends TcManticVentasDto implements Serializable {

	private static final long serialVersionUID=3088884892456452488L;
	
	private UISelectEntity ikAlmacen;
	private UISelectEntity ikCliente;

	public TicketVenta() {
		this(-1L);
	}

	public TicketVenta(Long key) {
		super(0D, null, "0", 0D, -1L, 1D, 1L, "", null, -1L, "0", new Long(Calendar.getInstance().get(Calendar.YEAR)), 1L, -1L, 0D, 2L, 0D, "", 0D, "", -1L, -1L, new Date(Calendar.getInstance().getTimeInMillis()), 1L);
	}

	public TicketVenta(Double descuentos, Long idFactura, String extras, Double total, Long idAlmacen, Double tipoDeCambio, Long orden, String tarjeta, Long idTipoMedioPago, Long idCliente, String descuento, Long ejercicio, Long consecutivo, Long idUsuario, Double impuestos, Long idSinIva, Double subTotal, String tipoTarjeta, Double efectivo, String observaciones, Long idEmpresa, Long idVenta, Date dia, Long idVentaEstatus) {
		super(descuentos, idFactura, extras, total, idAlmacen, tipoDeCambio, orden, tarjeta, idTipoMedioPago, idCliente, descuento, ejercicio, consecutivo, idUsuario, impuestos, idSinIva, subTotal, tipoTarjeta, efectivo, observaciones, idEmpresa, idVenta, dia, idVentaEstatus);
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

	public void setIkProveedor(UISelectEntity ikCliente) {
		this.ikCliente=ikCliente;
		if(this.ikCliente!= null)
		  this.setIdCliente(this.ikCliente.getKey());
	}

	@Override
	public Class toHbmClass() {
		return TcManticVentasDto.class;
	}
	
}
