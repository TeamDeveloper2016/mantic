/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 21/05/2015
 * @time 10:28:10 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */

package mx.org.kaana.libs.formato;

import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.math.RoundingMode;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import mx.org.kaana.libs.Constantes;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class Numero {
	
  private static final Log LOG=LogFactory.getLog(Numero.class);
	
  public static final int MONEDA_CON_DECIMALES= 1;
  public static final int MILES_CON_DECIMALES = 2;
  public static final int MILES_SIN_DECIMALES = 3;
  public static final int MONEDA_SIN_DECIMALES= 4;
  public static final int NUMERO_CON_DECIMALES= 5;
  public static final int NUMERO_SIN_DECIMALES= 6;
  public static final int MONEDA_SAT_DECIMALES= 7;
  public static final int NUMERO_SAT_DECIMALES= 8;
  public static final int MILES_SAT_DECIMALES = 9;

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
      case NUMERO_SAT_DECIMALES: // formato numero con decimales
        regresar= formatear("########0.0000", valor);
        break;
      case NUMERO_SIN_DECIMALES: // formato numoer sin decimales
        regresar= formatear("########0", valor);
        break;
      case MONEDA_SAT_DECIMALES: // formato moneda
        regresar= formatear("$ ###,##0.0000", valor);
        break;
      case MILES_SAT_DECIMALES: // separacion de miles
        regresar= formatear("###,##0.0000", valor);
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

  public static String redondear(double valor) {
    int operador= valor< 0? -1: 1;
    valor= operador* (Math.floor(Math.abs(valor)*100+ 0.5001)/100.0);
    return String.valueOf(valor);
  } // redondear

  public static String redondearSat(double valor) {
    int operador= valor< 0? -1: 1;
    valor= operador* (Math.floor(Math.abs(valor)*100000+ 0.5000001)/100000.0);
    return String.valueOf(valor);
  } // redondear

  public static String toTruncate(double valor, int decimals) {
    int operador= valor< 0? -1: 1;
    valor= operador* (Math.floor(Math.abs(valor)*100000+ 0.5000001)/100000.0);
		String regresar= String.valueOf(valor);
    return regresar.indexOf(".")> 0? regresar.substring(0, regresar.indexOf(".")+ decimals+ 1): regresar;
  } // redondear

  public static String toTruncate(double valor) {
		return toTruncate(valor, 2);
	}

  public static double toRedondear(double valor) {
    int operador= valor< 0? -1: 1;
    return operador* (Math.floor(Math.abs(valor)*100+ 0.5001)/100.0);
  } // redondear

  public static double toRedondearSat(double valor) {
    int operador= valor< 0? -1: 1;
    return operador* (Math.floor(Math.abs(valor)*100000+ 0.50000001)/100000.0);
  } // redondear

  public static double toAjustarDecimales(double valor) {
		return toAjustarDecimales(valor, false);
	}
	
  public static double toAjustarDecimales(double valor, boolean rounded) {
		valor= toRedondearSat(valor);
		try {
			if(rounded) {
				BigDecimal value = new BigDecimal(String.valueOf(valor));
				BigDecimal ivalue= new BigDecimal(value.toBigInteger());
				BigDecimal dvalue= value.remainder(BigDecimal.ONE);
				//BigDecimal avalue= value.subtract(value.setScale(0, RoundingMode.FLOOR)).movePointRight(value.scale());		
				if(dvalue.doubleValue()> 0.5)
					valor= ivalue.doubleValue()+ 1;
				else
					if(dvalue.doubleValue()>= 0.01 && dvalue.doubleValue()< 0.5)
						valor= ivalue.doubleValue()+ 0.5;
			} // if
		} // try
		catch(Exception e) {
			Error.mensaje(e);
		} // catch
    return valor;
  } // redondear

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
    return getInteger(value, 0);
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
    return getShort(value, number);
  }

  public static Byte getByte(String value,Byte defecto) {
    Byte regresar= (Byte)getNumber(Byte.class, value);
    return regresar!= null? regresar: defecto;
  }

  public static Byte getByte(String value) {
    byte number  = 0;
    return getByte(value, number);
  }

  public static Integer redondeaEnteroProximo(Double numero){
    Integer numeroEntero = numero.intValue();
    if (numero>numeroEntero)
      numeroEntero++;
    return numeroEntero;
  }

	public static void main(String ... args) {
//    LOG.info(10*100.0);		
//    LOG.info(Numero.toRedondearSat(10*100.0));		
//    LOG.info(10D/3D);		
//    LOG.info(Numero.toRedondearSat(3.123455));		
//    LOG.info(Numero.toRedondearSat(10D/3D));		
//    LOG.info(Numero.toTruncate(10D/3D));		
//    LOG.info(Numero.toTruncate(3.123455, 3));		
    LOG.info("3.5: "+ Numero.toAjustarDecimales(3.50, true));		
    LOG.info("3.123455: "+ Numero.toAjustarDecimales(3.123455, true));		
    LOG.info("3.123455: "+ Numero.toAjustarDecimales(3.123455, false));		
    LOG.info("3.5123: "+ Numero.toAjustarDecimales(3.5123, true));		
    LOG.info("3.1999: "+ Numero.toAjustarDecimales(3.1999, true));		
	}	

} // Numero
