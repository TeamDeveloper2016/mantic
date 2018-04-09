package mx.org.kaana.kajool.procesos.mantenimiento.indicadores.avance.beans;

import java.io.Serializable;
import java.util.List;
import mx.org.kaana.kajool.db.dto.TcJanalDefinicionesAvancesDto;
import mx.org.kaana.kajool.db.dto.TcJanalTablasTempAvaDto;


/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date Jul 24, 2014
 *@time 9:41:43 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class DefinicionTemporal implements Serializable{
	
	private static final long serialVersionUID=-6121704732833940608L;
	private TcJanalDefinicionesAvancesDto trDefinicionesAvancesTmpDto;
	private List<TcJanalTablasTempAvaDto> tablasTemporales;
	
	public DefinicionTemporal(TcJanalDefinicionesAvancesDto trDefinicionesAvancesTmpDto, List<TcJanalTablasTempAvaDto> tablasTemporales){
		this.trDefinicionesAvancesTmpDto= trDefinicionesAvancesTmpDto;
		this.tablasTemporales= tablasTemporales;
	}

	public TcJanalDefinicionesAvancesDto getTcJanalDefinicionesAvancesDto() {
		return trDefinicionesAvancesTmpDto;
	}

	public List<TcJanalTablasTempAvaDto> getTablasTemporales() {
		return tablasTemporales;
	}
	
}
