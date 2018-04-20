package mx.org.kaana.kajool.procesos.usuarios.reglas.beans;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 1/09/2015
 * @time 07:51:21 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import mx.org.kaana.libs.pagina.UISelectEntity;

public class CriteriosBusqueda implements Serializable {

  private static final long serialVersionUID = -4121538166956277598L;

  private UISelectEntity perfil;
  private List<UISelectEntity> listaPerfiles;
  private String nombre;

  public CriteriosBusqueda() {
    this.listaPerfiles = new ArrayList<>();
    this.nombre = "";
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public UISelectEntity getPerfil() {
    return perfil;
  }

  public void setPerfil(UISelectEntity perfil) {
    this.perfil = perfil;
  }

  public List<UISelectEntity> getListaPerfiles() {
    return listaPerfiles;
  }

  public void setListaPerfiles(List<UISelectEntity> listaPerfiles) {
    this.listaPerfiles = listaPerfiles;
  }

}
