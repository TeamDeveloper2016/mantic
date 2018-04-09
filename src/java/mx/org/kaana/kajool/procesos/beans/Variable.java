package mx.org.kaana.kajool.procesos.beans;

import java.io.Serializable;
import java.util.Objects;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;

/**
 *@company KAANA
 *@project  KAJOOL (Control system polls)
 *@date 29/11/2016
 *@time 07:31:29 PM
 *@author One Developer 2016 <one.developer@kaana.org.mx>
*/

public class Variable implements Serializable {
  
  private static final long serialVersionUID = 6673640290063344816L;

  private String header;
  private String title;
  private String property;
  private String css;
  private EFormatoDinamicos format;

  public Variable(String property) {
    this(property, property, property, "", EFormatoDinamicos.LIBRE);
  } // Variable

  public Variable(String property, String header, String title, String css, EFormatoDinamicos format) {
    this.header  = header;
    this.title   = title;
    this.property= property;
    this.css     = css;
    this.format  = format;
  } // Variable

  public String getHeader() {
    return header;
  }

  public String getTitle() {
    return title;
  }

  public String getProperty() {
    return property;
  }

  public String getCss() {
    return css;
  }

  public EFormatoDinamicos getFormat() {
    return format;
  }

  @Override
  public int hashCode() {
    int hash = 3;
    hash = 29 * hash + Objects.hashCode(this.property);
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Variable other = (Variable) obj;
    if (!Objects.equals(this.property, other.property)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "Variable{" + "header=" + header + ", title=" + title + ", property=" + property + ", css=" + css + ", format=" + format + '}';
  }
}
