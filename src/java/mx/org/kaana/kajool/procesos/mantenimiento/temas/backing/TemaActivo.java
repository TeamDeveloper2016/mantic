package mx.org.kaana.kajool.procesos.mantenimiento.temas.backing;

import java.io.Serializable;
import java.util.Map;
import javax.enterprise.context.SessionScoped;

import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 10/09/2015
 * @time 11:04:26 AM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
@Named(value = "kajoolTemaActivo")
@SessionScoped
public class TemaActivo implements Serializable {

  private static final long serialVersionUID = -7297184858511753453L;

  private String name;

  public TemaActivo() {
    this.name = "sentinel";
  }

  public String getName() {
    Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
    if (params.containsKey("name")) {
      name = ((String) params.get("name"));
    } // if
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    TemaActivo other = (TemaActivo) obj;
    if (name == null) {
      if (other.name != null) {
        return false;
      }
    } else if (!name.equals(other.name)) {
      return false;
    }
    return true;
  }

}
