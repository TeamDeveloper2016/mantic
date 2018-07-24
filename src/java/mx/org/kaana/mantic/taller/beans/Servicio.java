package mx.org.kaana.mantic.taller.beans;

import java.io.Serializable;
import mx.org.kaana.mantic.db.dto.TcManticServiciosDto;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 8/05/2018
 *@time 10:29:26 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Servicio extends TcManticServiciosDto implements Serializable {

	private static final long serialVersionUID=3088884892456452488L;
	
	public Servicio() {
		super(-1L);
	}

	@Override
	public Class toHbmClass() {
		return TcManticServiciosDto.class;
	}
	
}
