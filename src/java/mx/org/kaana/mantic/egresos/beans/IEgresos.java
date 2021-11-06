package mx.org.kaana.mantic.egresos.beans;

import mx.org.kaana.kajool.enums.ESql;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 26/10/2021
 *@time 05:40:14 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public interface IEgresos {
  
  public Long getIdEgreso();
  public Long getIdNotaEntrada();
  public ESql getAccion();
  public Class toHbmClass();
  
}
