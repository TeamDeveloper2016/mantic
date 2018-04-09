/*
 * Clase: Numero.java
 *
 * Creado: 21 de mayo de 2007, 12:16 AM
 *
 * Write by: alejandro.jimenez
 */

package mx.org.kaana.libs.formato;

import java.lang.reflect.Constructor;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public final class Numero {

  public static final int MONEDA_CON_DECIMALES= 1;
  public static final int MILES_CON_DECIMALES = 2;
  public static final int MILES_SIN_DECIMALES = 3;
  public static final int MONEDA_SIN_DECIMALES= 4;
  public static final int NUMERO_CON_DECIMALES= 5;
  public static final int NUMERO_SIN_DECIMALES= 6;

  private Numero() {
  }

  public static String formatear(String patron, double valor) {
    NumberFormat formateo= NumberFormat.getCurrencyInstance(Locale.US);
    if (formateo instanceof DecimalFormat) {
      ((DecimalFormat) formateo).setDecimalSeparatorAlwaysShown(true);
      ((DecimalFormat) formateo).applyPattern(patron);
    }; // if
    return formateo.format(valor);
  }; // formatear

  public static String formatear(int patron, double valor) {
    String regresar= null;
    switch(patron) {
      case MONEDA_CON_DECIMALES: // formato moneda
        regresar= formatear("$ ###,##0.00", valor);
        break;
      case MILES_CON_DECIMALES: // separacion de miles
        regresar= formatear("###,##0.00", valor);
        break;
      case MILES_SIN_DECIMALES: // separacion de miles sin decimales
        regresar= formatear("###,##0", valor);
        break;
      case MONEDA_SIN_DECIMALES: // formato moneda
        regresar= formatear("$ ###,##0", valor);
        break;
      case NUMERO_CON_DECIMALES: // formato numero con decimales
        regresar= formatear("########0.00", valor);
        break;
      case NUMERO_SIN_DECIMALES: // formato numoer sin decimales
        regresar= formatear("########0", valor);
        break;
    } // switch
    return regresar;
  } // formatear

  public static String redondea(double valor, int decimales) {
    NumberFormat numberFormatUS = NumberFormat.getNumberInstance(Locale.US);
    numberFormatUS.setGroupingUsed(false);
    numberFormatUS.setMaximumFractionDigits(decimales);
    String numberString = numberFormatUS.format(valor);
    return numberString;
  }; // redondea

  public static String redondear(double valor){
    int operador= valor< 0? -1: 1;
    valor= operador* (Math.floor(Math.abs(valor)*100+ 0.5001)/100.0);
    return String.valueOf(valor);
  };// redondear

  private static Number getNumber(Class objeto, String value) {
    Number regresar= null;
    try  {
      Constructor constructor= objeto.getConstructor(String.class);
      regresar= (Number)constructor.newInstance(value);
    }
    catch (Exception e) {
      regresar= null;
  //    Error.mensaje(e);
    } // try
    return regresar;
  }

  public static Double getDouble(String value, Double defecto) {
    Double regresar= (Double)getNumber(Double.class, value);
    return regresar!= null? regresar: defecto;
  }

  public static Double getDouble(String value) {
    return getDouble(value,new Double(0));
  }

  public static Integer getInteger(String value, Integer defecto) {
    Integer regresar= (Integer)getNumber(Integer.class, value);
    return regresar!= null? regresar: defecto;
  }

  public static Integer getInteger(String value) {
    return getInteger(value, new Integer(0));
  }

  public static Long getLong(String value, Long defecto) {
    Long regresar= (Long)getNumber(Long.class, value);
    return regresar!= null? regresar: defecto;
  }

  public static Long getLong(String value) {
    return getLong(value,new Long(0));
  }

  public Short getShort(String value,Short defecto) {
    Short regresar= (Short)getNumber(Short.class, value);
    return regresar!= null? regresar: defecto;
  }

  public Short getShort(String value) {
    short number  = 0;
    return getShort(value,new Short(number));
  }

  public static Byte getByte(String value,Byte defecto) {
    Byte regresar= (Byte)getNumber(Byte.class, value);
    return regresar!= null? regresar: defecto;
  }

  public static Byte getByte(String value) {
    byte number  = 0;
    return getByte(value, new Byte(number));
  }

  public static Integer redondeaEnteroProximo(Double numero){
    Integer numeroEntero = numero.intValue();
    if (numero>numeroEntero)
      numeroEntero++;
    return numeroEntero;
  }

}; // Numero
