package mx.org.kaana.kajool.db.comun.beans;

import java.io.Serializable;
import mx.org.kaana.kajool.db.comun.dto.IBaseLogDto;
import mx.org.kaana.kajool.enums.ESql;

/**
 *@company KAANA
 *@project (Sistema de Seguimiento y Control de proyectos estadísticos)
 *@date 27/02/2015
 *@time 07:27:37 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class AuditLog  implements Serializable {
	
	private static final long serialVersionUID=5811570182693932334L;

	private IBaseLogDto ibaseLogDto;
	private ESql  operation;	
	
	public AuditLog (IBaseLogDto ibaseLogDto, ESql operation) {
	  this.ibaseLogDto=ibaseLogDto;
		this.operation  = operation;
	}

	public IBaseLogDto getIbaseLogDto() {
		return ibaseLogDto;
	}

	public ESql getOperation() {
		return operation;
	}	
	
}
