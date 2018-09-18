package mx.org.kaana.mantic.compras.requisiciones.beans;

import java.io.Serializable;
import java.sql.Date;
import java.util.Calendar;
import mx.org.kaana.mantic.db.dto.TcManticRequisicionesDto;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 8/05/2018
 *@time 10:29:26 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class TicketRequisicion extends TcManticRequisicionesDto implements Serializable {

	private static final long serialVersionUID = 1445893582080951078L;

	public TicketRequisicion() {
		this(-1L);
	}

	public TicketRequisicion(Long key) {
		this(-1L, "", -1L, 1L, 1L, -1L);
	}
	
	public TicketRequisicion(Long idUsuario, String observaciones, Long idEmpresa, Long idRequisicionEstatus, Long orden, Long ejercicio) {
		super("", new Date(Calendar.getInstance().getTimeInMillis()), idUsuario, observaciones, idEmpresa, idRequisicionEstatus, new Date(Calendar.getInstance().getTimeInMillis()), orden, -1L, -1L, ejercicio);
	}

	@Override
	public Class toHbmClass() {
		return TcManticRequisicionesDto.class;
	}
}
