package mx.org.kaana.libs.cfg;

import java.util.List;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 26/08/2015
 *@time 03:57:42 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public interface IArbol {

  public List<? extends IBaseDto> toFather(String value) throws Exception;
  public List<? extends IBaseDto> toChildren(String value, int level) throws Exception;
  public boolean isChild(String value, int level) throws Exception;	
		
}
