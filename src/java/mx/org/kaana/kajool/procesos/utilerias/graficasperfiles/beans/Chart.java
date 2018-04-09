package mx.org.kaana.kajool.procesos.utilerias.graficasperfiles.beans;

import java.io.Serializable;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 17/10/2016
 *@time 12:31:39 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Chart implements Serializable{

  private static final long serialVersionUID= 7076571439846775360L;
  private static final String FONT          = "Dosis, sans-serif";
  private String type;
  private String backgroundColor;
  private Style style;
  private String height;

  public Chart(String type) {
    this(type, "350");
  }
  
  public Chart(String type, String height) {
    this(type, null, new Style(FONT), height);
  }

  public Chart(String type, String backgroundColor, String height) {
    this(type, backgroundColor, new Style(FONT), height);
  }

  public Chart(String type, String backgroundColor, Style style, String height) {
    this.type           = type;
    this.backgroundColor= backgroundColor;
    this.style          = style;
    this.height         = height;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getBackgroundColor() {
    return backgroundColor;
  }

  public void setBackgroundColor(String backgroundColor) {
    this.backgroundColor = backgroundColor;
  }

  public Style getStyle() {
    return style;
  }

  public void setStyle(Style style) {
    this.style = style;
  }

  public String getHeight() {
    return height;
  }

  public void setHeight(String height) {
    this.height = height;
  }
}
