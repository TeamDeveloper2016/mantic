package mx.org.kaana.mantic.facturas.beans;

import java.io.Serializable;
import java.sql.Date;
import java.util.Calendar;
import java.util.Map;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.mantic.db.dto.TcManticFicticiasDto;

public class FacturaFicticia extends TcManticFicticiasDto implements Serializable {

	private static final long serialVersionUID = -4493162062949623588L;
	public Long idVentaPrincipal;
	
	public FacturaFicticia() {
		this(-1L);
	}

	public FacturaFicticia(Long key) {
		this(0D, null, -1L, key, "0", 0D, 0D, 1L, 1D, -1L, -1L, -1L, -1L, "0", null, Long.valueOf(Fecha.getAnioActual()), "", -1L, 0D, 2L, 2L, 0D, "", -1L, new Date(Calendar.getInstance().getTimeInMillis()), "", -1L);
	}
	
	public FacturaFicticia(Double descuentos, Long idFactura, Long idTipoPago, Long idFicticia, String extras, Double global, Double total, Long idFicticiaEstatus, Double tipoDeCambio, Long orden, Long idTipoMedioPago, Long idCliente, Long idClienteDomicilio, String descuento, Long idBanco, Long ejercicio, String consecutivo, Long idUsuario, Double impuestos, Long idUsoCfdi, Long idSinIva, Double subTotal, String observaciones, Long idEmpresa, Date dia, String referencia, Long idVentaPrincipal) {		
		super(descuentos, idTipoPago, idFicticia, extras, global, total, idFicticiaEstatus, tipoDeCambio, orden, idTipoMedioPago, idCliente, idClienteDomicilio, descuento, idBanco, ejercicio, consecutivo, idUsuario, impuestos, idUsoCfdi, idSinIva, subTotal, observaciones, idEmpresa, dia, referencia, idFactura);
		this.idVentaPrincipal= idVentaPrincipal;
	}

	public Long getIdVentaPrincipal() {
		return idVentaPrincipal;
	}

	public void setIdVentaPrincipal(Long idVentaPrincipal) {
		this.idVentaPrincipal = idVentaPrincipal;
	}	
	
	@Override
	public Class toHbmClass() {
		return TcManticFicticiasDto.class;
	}		

	@Override
	public Map toMap() {
		Map map= super.toMap();
		map.put("idVentaPrincipal", getIdVentaPrincipal());
		return map; //To change body of generated methods, choose Tools | Templates.
	}
}
