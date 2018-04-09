package mx.org.kaana.kajool.beans;

/**
 *@company INEGI
 *@project IKTAN (Sistema de Seguimiento y Control de proyectos estadísticos)
 *@date 14/06/2016
 *@time 12:00:00 PM 
 *@author Edward Aarón Vázquez Nájera <edward.vazquez@inegi.gob.mx>
 */

import java.io.Serializable;
import java.util.List;

public class BeanMenu implements Serializable{

  private static final long serialVersionUID = -4037256764037974722L;
  
  private String name;
  private String url;
  private String clave;
  private String icono;
  private List<BeanMenu> submenu;

  public BeanMenu(String name, String url, List<BeanMenu> submenu, String clave, String icono) {
    this.name   = name;
    this.url    = url;
    this.submenu= submenu;
    this.clave  = clave;
    this.icono  = icono;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public List<BeanMenu> getSubmenu() {
    return submenu;
  }

  public void setSubmenu(List<BeanMenu> submenu) {
    this.submenu = submenu;
  }

  public String getClave() {
    return clave;
  }

  public void setClave(String clave) {
    this.clave = clave;
  }

  public String getIcono() {
    return icono;
  }

  public void setIcono(String icono) {
    this.icono = icono;
  }
  
}
