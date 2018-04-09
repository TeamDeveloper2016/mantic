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

public class RespuestaMenu implements Serializable{

  private static final long serialVersionUID = -5216105230130850321L;
  
  private String indicador;
  private String descripcion;
  private String html;
  private List<BeanMenu> menu;

  public RespuestaMenu(String indicador, String descripcion) {
    this.indicador   = indicador;
    this.descripcion = descripcion;
  }
  
  public RespuestaMenu(String indicador, String descripcion, List<BeanMenu> menu) {
    this.indicador   = indicador;
    this.descripcion = descripcion;
    this.menu        = menu;
  }

  public RespuestaMenu(String indicador, String descripcion, String html) {
    this.indicador = indicador;
    this.descripcion = descripcion;
    this.html = html;
  }

  public String getHtml() {
    return html;
  }

  public void setHtml(String html) {
    this.html = html;
  }

  public String getIndicador() {
    return indicador;
  }

  public void setIndicador(String indicador) {
    this.indicador = indicador;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public List<BeanMenu> getMenu() {
    return menu;
  }

  public void setMenu(List<BeanMenu> menu) {
    this.menu = menu;
  }  
}
