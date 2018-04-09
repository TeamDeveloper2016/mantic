package mx.org.kaana.kajool.mantenimiento.contadores.backing;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.JsfUtilities;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.db.dto.TcJanalContadorAyudasDto;
import mx.org.kaana.kajool.procesos.mantenimiento.contadores.reglas.Transaccion;
import mx.org.kaana.libs.formato.Error;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 2/12/2014
 * @time 05:22:50 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
@ManagedBean(name="janalMantenimientoContadoresAyudas")
@ViewScoped
public class Ayudas implements Serializable {

  @PostConstruct
  protected void init() {
  }

  public void doCount(Long idAyuda) {
    Transaccion transaccion     = null;
    TcJanalContadorAyudasDto dto= null;
    try {
      dto= new TcJanalContadorAyudasDto(idAyuda, JsfBase.getAutentifica().getEmpleado().getIdUsuario(), -1L);
      transaccion= new Transaccion(dto);
      transaccion.ejecutar(EAccion.AGREGAR);
    } // try
    catch(Exception e) {
      JsfUtilities.addMessageError(e);
			Error.mensaje(e);
    }// catch
  }

}
