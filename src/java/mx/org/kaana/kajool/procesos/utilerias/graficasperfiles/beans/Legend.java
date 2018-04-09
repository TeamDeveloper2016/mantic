package mx.org.kaana.kajool.procesos.utilerias.graficasperfiles.beans;

import java.io.Serializable;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 17/10/2016
 *@time 01:07:47 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Legend implements Serializable{

  private static final long serialVersionUID= -1360495546974179864L;
  private String layout;
  private String align;
  private String verticalAlign;
  private int x;
  private int y;
  private boolean floating;
  private int borderWidth;
  private String backgroundColor;
  private boolean shadow;

  public Legend(String layout, String align, String verticalAlign, int x, int y, boolean floating, int borderWidth, String backgroundColor, boolean shadow) {
    this.layout         = layout;
    this.align          = align;
    this.verticalAlign  = verticalAlign;
    this.x              = x;
    this.y              = y;
    this.floating       = floating;
    this.borderWidth    = borderWidth;
    this.backgroundColor= backgroundColor;
    this.shadow         = shadow;
  }

  public String getLayout() {
    return layout;
  }

  public void setLayout(String layout) {
    this.layout = layout;
  }

  public String getAlign() {
    return align;
  }

  public void setAlign(String align) {
    this.align = align;
  }

  public String getVerticalAlign() {
    return verticalAlign;
  }

  public void setVerticalAlign(String verticalAlign) {
    this.verticalAlign = verticalAlign;
  }

  public int getX() {
    return x;
  }

  public void setX(int x) {
    this.x = x;
  }

  public int getY() {
    return y;
  }

  public void setY(int y) {
    this.y = y;
  }

  public boolean isFloating() {
    return floating;
  }

  public void setFloating(boolean floating) {
    this.floating = floating;
  }

  public int getBorderWidth() {
    return borderWidth;
  }

  public void setBorderWidth(int borderWidth) {
    this.borderWidth = borderWidth;
  }

  public String getBackgroundColor() {
    return backgroundColor;
  }

  public void setBackgroundColor(String backgroundColor) {
    this.backgroundColor = backgroundColor;
  }

  public boolean isShadow() {
    return shadow;
  }

  public void setShadow(boolean shadow) {
    this.shadow = shadow;
  }
}
