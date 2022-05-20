package mx.org.kaana.libs.wassenger;

import java.io.Serializable;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 20/05/2022
 *@time 08:23:39 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Sat implements Serializable {

  private static final long serialVersionUID = 6050598876143507397L;

  private String Rfc;
  private Boolean FormatoCorrecto;
  private Boolean Activo;
  private Boolean Localizado;
  private String message;

  public Sat() {
  }

  public Sat(String Rfc, Boolean FormatoCorrecto, Boolean Activo, Boolean Localizado) {
    this.Rfc = Rfc;
    this.FormatoCorrecto = FormatoCorrecto;
    this.Activo = Activo;
    this.Localizado = Localizado;
  }

  public String getRfc() {
    return Rfc;
  }

  public void setRfc(String Rfc) {
    this.Rfc = Rfc;
  }

  public Boolean getFormatoCorrecto() {
    return FormatoCorrecto;
  }

  public void setFormatoCorrecto(Boolean FormatoCorrecto) {
    this.FormatoCorrecto = FormatoCorrecto;
  }

  public Boolean getActivo() {
    return Activo;
  }

  public void setActivo(Boolean Activo) {
    this.Activo = Activo;
  }

  public Boolean getLocalizado() {
    return Localizado;
  }

  public void setLocalizado(Boolean Localizado) {
    this.Localizado = Localizado;
  }

  @Override
  public String toString() {
    return "Sat{" + "Rfc=" + Rfc + ", FormatoCorrecto=" + FormatoCorrecto + ", Activo=" + Activo + ", Localizado=" + Localizado + ", message=" + message + '}';
  }
  
}
