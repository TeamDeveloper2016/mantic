/*
 * Code write by user.development
 * Date 24/09/2008
 */

package mx.org.kaana.libs.formato;

import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import java.util.Calendar;
import java.util.GregorianCalendar;
import static mx.org.kaana.kajool.enums.EFormatoDinamicos.*;

/**
 *
 * @author alejandro.jimenez
 */
public final class Global {
	
  private Global() {
  }

	public static String format(EFormatoDinamicos type, Object value) {		
		String regresar= "";
    if(value!= null) {
			regresar= value.toString();
			switch (type) {
				case LIBRE:
					break;
				case NOMBRE_DE_PERSONA:
					regresar= Cadena.nombrePersona(regresar);
					break;
				case LETRA_CAPITAL:
					regresar= Cadena.letraCapital(regresar);
					break;
				case MAYUSCULAS:
					regresar= regresar.toUpperCase();
					break;
				case MINUSCULAS:
					regresar= regresar.toLowerCase();
					break;
				case MILES_SIN_DECIMALES:
					regresar= Numero.formatear(Numero.MILES_SIN_DECIMALES, Numero.getDouble(regresar));
					break;
				case MILES_CON_DECIMALES:
					regresar= Numero.formatear(Numero.MILES_CON_DECIMALES, Numero.getDouble(regresar));
					break;
				case MONEDA_SIN_DECIMALES:
					regresar= Numero.formatear(Numero.MONEDA_SIN_DECIMALES, Numero.getDouble(regresar));
					break;
				case MONEDA_CON_DECIMALES:
					regresar= Numero.formatear(Numero.MONEDA_CON_DECIMALES, Numero.getDouble(regresar));
					break;
				case NUMERO_SIN_DECIMALES:
					regresar= Numero.formatear(Numero.NUMERO_SIN_DECIMALES, Numero.getDouble(regresar));
					break;
				case NUMERO_CON_DECIMALES:
					regresar= Numero.formatear(Numero.NUMERO_CON_DECIMALES, Numero.getDouble(regresar));
					break;
				case FECHA_CORTA:
					regresar= Fecha.formatear(Fecha.FECHA_CORTA, Fecha.getFechaHora(regresar));
					break;
				case FECHA_NOMBRE_DIA:
					regresar= Fecha.formatear(Fecha.FECHA_NOMBRE_DIA, Fecha.getFechaHora(regresar));
					break;
				case FECHA_EXTENDIDA:
					regresar= Fecha.formatear(Fecha.FECHA_EXTENDIDA, Fecha.getFechaHora(regresar));
					break;
				case FECHA_LARGA:
					regresar= Fecha.formatear(Fecha.FECHA_LARGA, Fecha.getFechaHora(regresar));
					break;
				case FECHA_NOMBRE_MES:
					regresar= Fecha.formatear(Fecha.FECHA_NOMBRE_MES, Fecha.getFechaHora(regresar));
					break;
				case HORA_CORTA:
					regresar= Fecha.formatear(Fecha.HORA_CORTA, Fecha.getFechaHora(regresar));
					break;
				case HORA_LARGA:
					regresar= Fecha.formatear(Fecha.HORA_LARGA, Fecha.getFechaHora(regresar));
					break;
			  case FECHA_HORA:
			    regresar= Fecha.formatear(Fecha.FECHA_HORA, Fecha.getFechaHora(regresar));
			    break;
			  case FECHA_HORA_CORTA:
			    regresar= Fecha.formatear(Fecha.FECHA_HORA_CORTA, Fecha.getFechaHora(regresar));
			    break;
			  case DIA_FECHA_HORA:
			    regresar= Fecha.formatear(Fecha.FECHA_HORA_EXTENDIDA, Fecha.getFechaHora(regresar));
			    break;
			  case DIA_FECHA_HORA_CORTA:
			    regresar= Fecha.formatear(Fecha.FECHA_HORA_EXTENDIDA, Fecha.getFechaHora(regresar));
			    break;
				case FECHA_HORA_ANTERIOR:
					Calendar anterior = Fecha.getFechaHora(regresar);
					anterior.add(GregorianCalendar.DATE, -1);						
					regresar= Fecha.formatear(Fecha.FECHA_HORA,anterior);
				break;	
					
			} // switch
		} // if	
		return regresar;
	} // format

  public static String format(Criteria criteria, Object value) {		
		if(criteria.isBasic())
			return format(criteria.getFormat(), value);
		else
			return criteria.getCfg().format(value);
	}

}
