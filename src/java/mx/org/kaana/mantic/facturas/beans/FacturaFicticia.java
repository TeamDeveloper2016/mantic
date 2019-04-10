package mx.org.kaana.mantic.facturas.beans;

import java.io.Serializable;
import java.sql.Date;
import java.util.Calendar;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.mantic.db.dto.TcManticFicticiasDto;

public class FacturaFicticia extends TcManticFicticiasDto implements Serializable {

	private static final long serialVersionUID = -4493162062949623588L;	
	
	public FacturaFicticia() {
		this(-1L);
	}

	public FacturaFicticia(Long key) {
		this(0D, null, -1L, key, "0", 0D, 0D, 1L, 1D, -1L, -1L, -1L, -1L, "0", null, Long.valueOf(Fecha.getAnioActual()), "", -1L, 0D, 2L, 2L, 0D, "", -1L, new Date(Calendar.getInstance().getTimeInMillis()), "");
	}
	
	public FacturaFicticia(Double descuentos, Long idFactura, Long idTipoPago, Long idFicticia, String extras, Double global, Double total, Long idFicticiaEstatus, Double tipoDeCambio, Long orden, Long idTipoMedioPago, Long idCliente, Long idClienteDomicilio, String descuento, Long idBanco, Long ejercicio, String consecutivo, Long idUsuario, Double impuestos, Long idUsoCfdi, Long idSinIva, Double subTotal, String observaciones, Long idEmpresa, Date dia, String referencia) {		
		super(descuentos, idTipoPago, idFicticia, extras, global, total, idFicticiaEstatus, tipoDeCambio, orden, idTipoMedioPago, idCliente, idClienteDomicilio, descuento, idBanco, ejercicio, consecutivo, idUsuario, impuestos, idUsoCfdi, idSinIva, subTotal, observaciones, idEmpresa, dia, referencia, idFactura);		
	}	
	
	@Override
	public Class toHbmClass() {
		return TcManticFicticiasDto.class;
	}			
}
