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
	private String correos;
	private Long idServicio;
	
	public TicketVenta() {
		this(-1L);
	}

	public TicketVenta(Long key) {
		super(0D, null, 2L, "0", 0D, -1L, 1D, 1L, 2L, -1L, "0", new Long(Calendar.getInstance().get(Calendar.YEAR)), 1L, -1L, 0D, 3L, 2L, 0D, "", -1L, -1L, new Date(Calendar.getInstance().getTimeInMillis()), 1L, 2L);
		this.idServicio= -1L;
	}
	
	public TicketVenta(Double descuentos, Long idFactura, Long idCredito, String extras, Double total, Long idAlmacen, Double tipoDeCambio, Long orden, Long idCliente, String descuento, Long ejercicio, Long consecutivo, Long idUsuario, Double impuestos, Long idUsoCfdi, Long idSinIva, Double subTotal, String observaciones, Long idEmpresa, Long idVenta, Date dia, Long idVentaEstatus, Long idAutorizar, Long idFacturar) {
		this(descuentos, idFactura, idCredito, extras, total, idAlmacen, tipoDeCambio, orden, idCliente, descuento, ejercicio, consecutivo, idUsuario, impuestos, idUsoCfdi, idSinIva, subTotal, observaciones, idEmpresa, idVenta, dia, idVentaEstatus, idAutorizar, idFacturar, -1L);
	}
	
	public TicketVenta(Double descuentos, Long idFactura, Long idCredito, String extras, Double total, Long idAlmacen, Double tipoDeCambio, Long orden, Long idCliente, String descuento, Long ejercicio, Long consecutivo, Long idUsuario, Double impuestos, Long idUsoCfdi, Long idSinIva, Double subTotal, String observaciones, Long idEmpresa, Long idVenta, Date dia, Long idVentaEstatus, Long idAutorizar, Long idFacturar, Long idServicio) {
		super(descuentos, idFactura, idCredito, extras, total, idAlmacen, tipoDeCambio, orden, idAutorizar, idCliente, descuento, ejercicio, consecutivo, idUsuario, impuestos, idUsoCfdi, idSinIva, subTotal, observaciones, idEmpresa, idVenta, dia, idVentaEstatus, idFacturar);
		this.idServicio= idServicio;
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

	public String getCorreos() {
		return correos;
	}

	public void setCorreos(String correos) {
		this.correos = correos;
	}

	public Long getIdServicio() {
		return idServicio;
	}

	public void setIdServicio(Long idServicio) {
		this.idServicio = idServicio;
	}	
}