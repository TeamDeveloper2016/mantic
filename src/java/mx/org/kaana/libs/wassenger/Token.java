package mx.org.kaana.libs.wassenger;

import java.io.Serializable;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 16/05/2022
 *@time 08:23:39 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Token implements Serializable {

  private static final long serialVersionUID = 6050598876143507397L;

  private Boolean valid;
  private String message;

  public Token() {
  }

  public Token(Boolean valid, String message) {
    this.valid = valid;
    this.message = message;
  }

  public Boolean getValid() {
    return valid;
  }

  public void setValid(Boolean valid) {
    this.valid = valid;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  @Override
  public String toString() {
    return "Token{" + "valid=" + valid + ", message=" + message + '}';
  }
  
}
