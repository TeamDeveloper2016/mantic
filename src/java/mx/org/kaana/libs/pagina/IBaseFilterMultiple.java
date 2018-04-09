package mx.org.kaana.libs.pagina;

import java.io.Serializable;
import mx.org.kaana.kajool.db.comun.sql.Entity;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 20-feb-2014
 *@time 15:20:21
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public abstract class IBaseFilterMultiple extends IBaseFilter implements Serializable {

  private static final long serialVersionUID = -5324353121363296348L;

  protected Entity[] selecteds;

	public Entity[] getSelecteds() {
		return selecteds;
	}

	public void setSelecteds(Entity[] selecteds) {
		this.selecteds=selecteds;
	}

}
