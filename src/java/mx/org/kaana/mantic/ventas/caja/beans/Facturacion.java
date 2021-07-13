package mx.org.kaana.mantic.ventas.caja.beans;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Calendar;

public class Facturacion implements Serializable{
	
	private static final long serialVersionUID = 2652025374897469982L;
	private Long idVenta;
	private Long idCliente;
	private String correos;
	private String razonSocial;	
	private Long idTipoPago;
	private Long idFactura;
	private String idFacturama;
	private String selloSat;
	private Long idClienteDomicilio;
	private Long idUsuario;
	private String nombreEmpresa;
	private Long idEmpresa;
	private Long idTipoDocumento;
	private Long intentos;
	private String ticket;
	private Timestamp timbrado;

	public Facturacion() {
		this(-1L, -1L, "", "", -1L, -1L, "", "", -1L);
	} // Facturacion

	public Facturacion(Long idVenta, Long idCliente, String correos, String razonSocial, Long idTipoPago, Long idFactura, String idFacturama, String selloSat, Long idClienteDomicilio) {
		this(idVenta, idCliente, correos, razonSocial, idTipoPago, idFactura, idFacturama, selloSat, idClienteDomicilio, -1L, "", -1L);
	} // Facturacion
	
	public Facturacion(Long idVenta, Long idCliente, String correos, String razonSocial, Long idTipoPago, Long idFactura, String idFacturama, String selloSat, Long idClienteDomicilio, Long idUsuario, String nombreEmpresa, Long idEmpresa) {
		this(idVenta, idCliente, correos, razonSocial, idTipoPago, idFactura, idFacturama, selloSat, idClienteDomicilio, idUsuario, nombreEmpresa, idEmpresa, -1L, 0L);
	}
	
	public Facturacion(Long idVenta, Long idCliente, String correos, String razonSocial, Long idTipoPago, Long idFactura, String idFacturama, String selloSat, Long idClienteDomicilio, Long idUsuario, String nombreEmpresa, Long idEmpresa, Long idTipoDocumento, Long intentos) {
		this.idVenta           = idVenta;
		this.idCliente         = idCliente;
		this.correos           = correos;
		this.razonSocial       = razonSocial;
		this.idTipoPago        = idTipoPago;
		this.idFactura         = idFactura;
		this.idFacturama       = idFacturama;
		this.selloSat          = selloSat;
		this.idClienteDomicilio= idClienteDomicilio;
		this.idUsuario         = idUsuario;
		this.nombreEmpresa     = nombreEmpresa;
		this.idEmpresa         = idEmpresa;
		this.idTipoDocumento   = idTipoDocumento;
		this.intentos          = intentos;
    this.ticket            = "";
    this.timbrado          = new Timestamp(Calendar.getInstance().getTimeInMillis());
	}

	public Long getIdVenta() {
		return idVenta;
	}

	public void setIdVenta(Long idVenta) {
		this.idVenta = idVenta;
	}

	public Long getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Long idCliente) {
		this.idCliente = idCliente;
	}

	public String getCorreos() {
		return correos;
	}

	public void setCorreos(String correos) {
		this.correos = correos;
	}

	public String getRazonSocial() {
		return razonSocial;
	}

	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}	

	public Long getIdTipoPago() {
		return idTipoPago;
	}

	public void setIdTipoPago(Long idTipoPago) {
		this.idTipoPago = idTipoPago;
	}

	public Long getIdFactura() {
		return idFactura;
	}

	public void setIdFactura(Long idFactura) {
		this.idFactura = idFactura;
	}

	public String getIdFacturama() {
		return idFacturama;
	}

	public void setIdFacturama(String idFacturama) {
		this.idFacturama = idFacturama;
	}

	public String getSelloSat() {
		return selloSat;
	}

	public void setSelloSat(String selloSat) {
		this.selloSat = selloSat;
	}

	public Long getIdClienteDomicilio() {
		return idClienteDomicilio;
	}

	public void setIdClienteDomicilio(Long idClienteDomicilio) {
		this.idClienteDomicilio = idClienteDomicilio;
	}

	public Long getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}	

	public String getNombreEmpresa() {
		return nombreEmpresa;
	}

	public void setNombreEmpresa(String nombreEmpresa) {
		this.nombreEmpresa = nombreEmpresa;
	}	

	public Long getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}	

	public Long getIdTipoDocumento() {
		return idTipoDocumento;
	}

	public void setIdTipoDocumento(Long idTipoDocumento) {
		this.idTipoDocumento = idTipoDocumento;
	}	

	public Long getIntentos() {
		return intentos;
	}

	public void setIntentos(Long intentos) {
		this.intentos = intentos;
	}	

  public String getTicket() {
    return ticket;
  }

  public void setTicket(String ticket) {
    this.ticket = ticket;
  }

  public Timestamp getTimbrado() {
    return timbrado;
  }

  public void setTimbrado(Timestamp timbrado) {
    this.timbrado = timbrado;
  }
  
}
