package mx.org.kaana.kalan.catalogos.pacientes.citas.beans;

import java.io.Serializable;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.mantic.db.dto.TcManticClientesDto;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 22/03/2023
 *@time 06:40:50 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Paciente extends TcManticClientesDto implements Serializable {

  private static final long serialVersionUID = 933154433403709712L;

  private UISelectEntity ikAtendio;
  
  public Paciente() {
    this(-1L);
  }

  public Paciente(Long key) {
    super(key);
  }

  public UISelectEntity getIkAtendio() {
    return ikAtendio;
  }

  public void setIkAtendio(UISelectEntity ikAtendio) {
    this.ikAtendio = ikAtendio;
  }
  
}
