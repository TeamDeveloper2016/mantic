package mx.org.kaana.mantic.compras.ordenes.beans;

import java.io.Serializable;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.mantic.db.dto.TcManticArticulosDto;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 8/05/2018
 *@time 03:09:42 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Articulo extends TcManticArticulosDto implements Serializable {

	private static final long serialVersionUID=329661715469035396L;

	private UISelectEntity idEntity;

	public Articulo() {
		this.idEntity= new UISelectEntity(new Entity(-1L));
	}

	public UISelectEntity getIdEntity() {
		return idEntity;
	}

	public void setIdEntity(UISelectEntity idEntity) {
		this.idEntity=idEntity;
	}

	@Override
	public Class toHbmClass() {
		return TcManticArticulosDto.class;
	}
	
}
