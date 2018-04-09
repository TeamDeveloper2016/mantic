package mx.org.kaana.kajool.procesos.mantenimiento.catalogos.beans;

import java.io.Serializable;
import java.util.Objects;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 18/09/2015
 *@time 01:10:35 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Column implements Serializable{
  private static final long serialVersionUID = 54587624741265101L;
  private String header;
  private String property;
  private EFormatoDinamicos format;
  private String align;

  public Column() {
    this("", "", EFormatoDinamicos.LIBRE);
  }

  public Column(String header, String property, EFormatoDinamicos format) {
    this(header, property, format, "TexAlLeft");
  }


  public Column(String header, String property, EFormatoDinamicos format, String align) {
    this.header = header;
    this.property = property;
    this.format = format;
    this.align = align;
  }

  public String getHeader() {
    return header;
  }

  public void setHeader(String header) {
    this.header = header;
  }

  public String getProperty() {
    return property;
  }

  public void setProperty(String property) {
    this.property = property;
  }

  public EFormatoDinamicos getFormat() {
    return format;
  }

  public void setFormat(EFormatoDinamicos format) {
    this.format = format;
  }

  public String getAlign() {
    switch(this.format){
      case LIBRE:
      case MAYUSCULAS:
      case MINUSCULAS:
      case LETRA_CAPITAL:
      case NOMBRE_DE_PERSONA:
        this.align = "TexAlLeft";
        break;
      case MILES_CON_DECIMALES:
      case MILES_SIN_DECIMALES:
      case NUMERO_CON_DECIMALES:
      case NUMERO_SIN_DECIMALES:
      case MONEDA_CON_DECIMALES:
      case MONEDA_SIN_DECIMALES:
        this.align = "TexAlRight";
        break;
      case DIA_FECHA_HORA:
      case DIA_FECHA_HORA_CORTA:
      case HORA_CORTA:
      case HORA_LARGA:
      case FECHA_CORTA:
      case FECHA_EXTENDIDA:
      case FECHA_HORA:
      case FECHA_HORA_ANTERIOR:
      case FECHA_HORA_CORTA:
      case FECHA_LARGA:
      case FECHA_NOMBRE_DIA:
      case FECHA_NOMBRE_MES:
        this.align = "TexAlCenter";
        break;
    }
    return align;
  }

  public void setAlign(String align) {
    this.align = align;
  }

  @Override
  public int hashCode() {
    int hash = 5;
    hash = 71 * hash + Objects.hashCode(this.header);
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
    final Column other = (Column) obj;
    if (!Objects.equals(this.header, other.header)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "Column{" + "header=" + header + ", property=" + property + '}';
  }
}
