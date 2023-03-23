package mx.org.kaana.kalan.catalogos.pacientes.citas.beans;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Objects;
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

  private Timestamp inicio;
  private Timestamp termino;
  private String celular;
  private String correo;
  
  private UISelectEntity ikAtendio;
  
  public Paciente() {
    this(-1L);
  }

  public Paciente(Long key) {
    super(key);
    this.celular= "";
    this.correo = "";
    this.init(Boolean.TRUE);
  }

  public Timestamp getInicio() {
    return inicio;
  }

  public void setInicio(Timestamp inicio) {
    this.inicio = inicio;
    this.init(Objects.equals(this.inicio, null));
  }

  public Timestamp getTermino() {
    return termino;
  }

  public void setTermino(Timestamp termino) {
    this.termino = termino;
  }

  public String getCelular() {
    return celular;
  }

  public void setCelular(String celular) {
    this.celular = celular;
  }

  public String getCorreo() {
    return correo;
  }

  public void setCorreo(String correo) {
    this.correo = correo;
  }

  public UISelectEntity getIkAtendio() {
    return ikAtendio;
  }

  public void setIkAtendio(UISelectEntity ikAtendio) {
    this.ikAtendio = ikAtendio;
  }
  
  private void init(Boolean clean) {
    if(clean)
      this.inicio = new Timestamp(Calendar.getInstance().getTimeInMillis());
    Calendar now= Calendar.getInstance();
    now.add(Calendar.MINUTE, 30);
    this.termino= new Timestamp(now.getTimeInMillis());
  }
  
}
