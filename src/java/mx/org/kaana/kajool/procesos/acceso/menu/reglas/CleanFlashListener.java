package mx.org.kaana.kajool.procesos.acceso.menu.reglas;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date Oct 22, 2012
 *@time 3:13:54 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import mx.org.kaana.libs.pagina.JsfBase;

public class CleanFlashListener implements ActionListener {

  @Override
  public void processAction(ActionEvent ae) throws AbortProcessingException {
    JsfBase.cleanFlashParams();
  }

}
