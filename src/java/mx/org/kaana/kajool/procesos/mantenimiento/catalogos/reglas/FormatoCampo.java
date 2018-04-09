package mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import mx.org.kaana.kajool.enums.ETipoDato;
import static mx.org.kaana.kajool.enums.ETipoDato.BLOB;
import static mx.org.kaana.kajool.enums.ETipoDato.DATE;
import static mx.org.kaana.kajool.enums.ETipoDato.DOUBLE;
import static mx.org.kaana.kajool.enums.ETipoDato.LONG;
import static mx.org.kaana.kajool.enums.ETipoDato.TEXT;
import static mx.org.kaana.kajool.enums.ETipoDato.TIME;
import static mx.org.kaana.kajool.enums.ETipoDato.TIMESTAMP;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 22/09/2015
 *@time 01:51:16 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public final class FormatoCampo implements Serializable {
	
  private static FormatoCampo instance;
  private static Object mutex;

	private static final List<Operador> texto;
	private static final List<Operador> numerico;
	private static final List<Operador> calendario;
	
  static {
    mutex = new Object();
		
		texto= new ArrayList<>();
    texto.add(new Operador(0, "Ninguno", "LIBRE"));
    texto.add(new Operador(2, "Mayusculas", "MAYUSCULAS"));
    texto.add(new Operador(3, "Minusculas", "MINUSCULAS"));
    texto.add(new Operador(4, "Letra-capital", "LETRA_CAPITAL"));
    texto.add(new Operador(5, "Letra-capital", "NOMBRE_DE_PERSONA"));
		
		numerico= new ArrayList<>();
    numerico.add(new Operador(0, "Ninguno", "LIBRE"));
    numerico.add(new Operador(1, "9,999.00", "MILES_CON_DECIMALES"));
    numerico.add(new Operador(2, "9,999", "MILES_SIN_DECIMALES"));
    numerico.add(new Operador(3, "$ 9,999.00", "MONEDA_CON_DECIMALES"));
    numerico.add(new Operador(4, "$ 9,999", "MONEDA_SIN_DECIMALES"));
    numerico.add(new Operador(5, "9999.00", "MONEDA_CON_DECIMALES"));
    numerico.add(new Operador(6, "9999", "MONEDA_SIN_DECIMALES"));

		calendario= new ArrayList<>();
    calendario.add(new Operador(0, "Ninguno", "LIBRE"));
    calendario.add(new Operador(1, "dd/MM/yyyy", "FECHA_CORTA"));
    calendario.add(new Operador(2, "dia, dd/MM/yyyy", "FECHA_NOMBRE_DIA"));
    calendario.add(new Operador(3, "dd del Mes del yyyy", "FECHA_LARGA"));
    calendario.add(new Operador(4, "Dia, dd de Mes del yyyy", "FECHA_EXTENDIDA"));
    calendario.add(new Operador(5, "dd/Mes/yyyy", "FECHA_NOMBRE_MES"));
    calendario.add(new Operador(6, "HH:mm:ss", "HORA_LARGA"));
    calendario.add(new Operador(7, "HH:mm", "HORA_CORTA"));
    calendario.add(new Operador(8, "dd/MM/yyyy HH:mm:ss", "FECHA_HORA"));
    calendario.add(new Operador(9, "dd/MM/yyyy HH:mm", "FECHA_HORA_CORTA"));
    calendario.add(new Operador(10, "dia, dd/MM/yyyy HH:mm:ss", "DIA_FECHA_HORA"));
    calendario.add(new Operador(11, "dia, dd/Mes/yyyy HH:mm", "DIA_FECHA_HORA_CORTA"));	
  }

	private FormatoCampo() {
	}

  public static FormatoCampo getInstance() {
    synchronized (mutex) {
      if (instance == null) {
        instance = new FormatoCampo();
      }
    } // if
    return instance;
  }

  public List<Operador> toFormatText() {
		return this.texto;
	}
	
  public List<Operador> toFormatNumeric() {
		return this.numerico;
	}
	
  public List<Operador> toFormatDate() {
		return this.calendario;
	}

	public List<Operador> toFormat(ETipoDato type) {
		List<Operador> regresar= null;
    switch(type) {
			case TEXT:
				regresar= toFormatText();
				break;
			case DOUBLE:
				regresar= toFormatNumeric();
				break;
			case LONG:
				regresar= toFormatNumeric();
				break;
			case BLOB:
				regresar= toFormatText();
				break;
			case DATE:
				regresar= toFormatDate();
				break;
			case TIME:
				regresar= toFormatDate();
				break;
			case TIMESTAMP:
				regresar= toFormatDate();
				break;
		} // switch
		return regresar;
	}
	
}
