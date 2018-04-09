package mx.org.kaana.kajool.procesos.mantenimiento.contadores.backing;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.JsfUtilities;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.db.dto.TcJanalContadorAyudasDto;
import mx.org.kaana.kajool.procesos.mantenimiento.contadores.reglas.Transaccion;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 10/09/2015
 * @time 06:36:38 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
@ManagedBean(name="kajoolMantenimientoContadoresAyudas")
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
    } // try // try
    catch(Exception e) {
      JsfUtilities.addMessageError(e);
			mx.org.kaana.libs.formato.Error.mensaje(e);
    }// catch
  }

}
