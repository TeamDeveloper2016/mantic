package mx.org.kaana.kajool.procesos.utilerias.graficasperfiles.beans;

import java.io.Serializable;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 17/10/2016
 *@time 11:46:48 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class JsonChart implements Serializable{

  private static final long serialVersionUID= 6585810675924627558L;
  private String id;
  private String titulo;
  private String json;

  public JsonChart(String id, String titulo, String json) {
    this.id    = id;
    this.titulo= titulo;
    this.json  = json;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTitulo() {
    return titulo;
  }

  public void setTitulo(String titulo) {
    this.titulo = titulo;
  }

  public String getJson() {
    return json;
  }

  public void setJson(String json) {
    this.json = json;
  }
}
