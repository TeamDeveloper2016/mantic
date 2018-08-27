package mx.org.kaana.mantic.facturas.beans;

import java.io.Serializable;
import java.sql.Date;
import java.util.Calendar;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.mantic.db.dto.TcManticFicticiasDto;

public class FacturaFicticia extends TcManticFicticiasDto implements Serializable{

	private static final long serialVersionUID = -4493162062949623588L;

	public FacturaFicticia() {
		this(-1L);
	}

	public FacturaFicticia(Long key) {
		super(0D,	2L, key, "", "", 0D, Long.valueOf(Fecha.getAnioActual()), -1L, 0D, 1L, -1L, 0D, 2L, 1D, 2L, 0D, "", -1L, -1L, new Date(Calendar.getInstance().getTimeInMillis()));
	}

	public FacturaFicticia(Double descuentos, Long idFactura, Long idFicticia, String descuento, String extras, Double global, Long ejercicio, Long consecutivo, Double total, Long idFicticiaEstatus, Long idUsuario, Double impuestos, Long idUsoCfdi, Double tipoDeCambio, Long idSinIva, Double subTotal, String observaciones, Long idEmpresa, Long orden, Date dia) {
		super(descuentos, idFactura, idFicticia, descuento, extras, global, ejercicio, consecutivo, total, idFicticiaEstatus, idUsuario, impuestos, idUsoCfdi, tipoDeCambio, idSinIva, subTotal, observaciones, idEmpresa, orden, dia);
	}
	
	@Override
	public Class toHbmClass() {
		return TcManticFicticiasDto.class;
	}	
}