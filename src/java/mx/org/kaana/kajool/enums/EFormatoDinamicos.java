package mx.org.kaana.kajool.enums;

import java.util.Calendar;
import java.util.GregorianCalendar;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.formato.Numero;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date May 30, 2012
 * @time 8:55:09 AM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public enum EFormatoDinamicos implements IFormatosKajool {

  LIBRE(""),
  MAYUSCULAS("MAYUSCULAS"),
  MINUSCULAS("minusculas"),
  LETRA_CAPITAL("Letra capital"),
  NOMBRE_DE_PERSONA("Nombre Persona"),
  MILES_SAT_DECIMALES("###,##0.0000"),
  MILES_CON_DECIMALES("###,##0.00"),
  MILES_SIN_DECIMALES("###,##0"),
  MONEDA_CON_DECIMALES("$ ###,##0.00"),
  MONEDA_SAT_DECIMALES("$ ###,##0.0000"),
  MONEDA_SIN_DECIMALES("$ ###,##0"),
  NUMERO_SIN_DECIMALES("#####0"),
  NUMERO_CON_DECIMALES("#####0.00"),
  NUMERO_SAT_DECIMALES("#####0.0000"),
  FECHA_CORTA("dd/MM/yyyy"),
  FECHA_NOMBRE_DIA("Dia, dd/MM/yyyy"),
  FECHA_EXTENDIDA("dd del Mes del yyyy"),
  FECHA_LARGA("Dia, dd del Mes del yyyy"),
  FECHA_NOMBRE_MES("dd/Mes/yyyy"),
  HORA_LARGA("HH:mm:ss"),
  HORA_CORTA("HH:mm"),
  FECHA_HORA("dd/MM/yyyy HH:mm:ss"),
  FECHA_HORA_CORTA("dd/MM/yyyy HH:mm"),
  DIA_FECHA_HORA("Dia, dd/MM/yyyy HH:mm:ss"),
  DIA_FECHA_HORA_CORTA("Dia, dd/MM/yyyy HH:mm"),
  FECHA_HORA_ANTERIOR("dd/MM/yyyy HH:mm:ss"),
  MEGAS("Megas"),
  ASTERISCO("*");

  private String patron;

  private EFormatoDinamicos(String patron) {
    this.patron = patron;
  }

  @Override
  public String getPatron() {
    return patron;
  }

  @Override
  public String execute(Object value) {
    String regresar = "";
    if (value != null) {
      regresar = value.toString();
      switch (this) {
        case LIBRE:
          break;
        case NOMBRE_DE_PERSONA:
          regresar = Cadena.nombrePersona(regresar);
          break;
        case LETRA_CAPITAL:
          regresar = Cadena.letraCapital(regresar);
          break;
        case MAYUSCULAS:
          regresar = Cadena.isVacio(regresar) ? regresar : regresar.toUpperCase();
          break;
        case MINUSCULAS:
          regresar = Cadena.isVacio(regresar) ? regresar : regresar.toLowerCase();
          break;
        case MILES_SIN_DECIMALES:
          regresar = Numero.formatear(Numero.MILES_SIN_DECIMALES, Numero.getDouble(regresar));
          break;
        case MILES_SAT_DECIMALES:
          regresar = Numero.formatear(Numero.MILES_SAT_DECIMALES, Numero.getDouble(regresar));
          break;
        case MILES_CON_DECIMALES:
          regresar = Numero.formatear(Numero.MILES_CON_DECIMALES, Numero.getDouble(regresar));
          break;
        case MONEDA_SIN_DECIMALES:
          regresar = Numero.formatear(Numero.MONEDA_SIN_DECIMALES, Numero.getDouble(regresar));
          break;
        case MONEDA_CON_DECIMALES:
          regresar = Numero.formatear(Numero.MONEDA_CON_DECIMALES, Numero.getDouble(regresar));
          break;
        case MONEDA_SAT_DECIMALES:
          regresar = Numero.formatear(Numero.MONEDA_SAT_DECIMALES, Numero.getDouble(regresar));
          break;
        case NUMERO_SIN_DECIMALES:
          regresar = Numero.formatear(Numero.NUMERO_SIN_DECIMALES, Numero.getDouble(regresar));
          break;
        case NUMERO_CON_DECIMALES:
          regresar = Numero.formatear(Numero.NUMERO_CON_DECIMALES, Numero.getDouble(regresar));
          break;
        case NUMERO_SAT_DECIMALES:
          regresar = Numero.formatear(Numero.NUMERO_SAT_DECIMALES, Numero.getDouble(regresar));
          break;
        case FECHA_CORTA:
          regresar = Fecha.formatear(Fecha.FECHA_CORTA, Fecha.getFechaHora(regresar));
          break;
        case FECHA_NOMBRE_DIA:
          regresar = Fecha.formatear(Fecha.FECHA_NOMBRE_DIA, Fecha.getFechaHora(regresar));
          break;
        case FECHA_EXTENDIDA:
          regresar = Fecha.formatear(Fecha.FECHA_EXTENDIDA, Fecha.getFechaHora(regresar));
          break;
        case FECHA_LARGA:
          regresar = Fecha.formatear(Fecha.FECHA_LARGA, Fecha.getFechaHora(regresar));
          break;
        case FECHA_NOMBRE_MES:
          regresar = Fecha.formatear(Fecha.FECHA_NOMBRE_MES, Fecha.getFechaHora(regresar));
          break;
        case HORA_CORTA:
          regresar = Fecha.formatear(Fecha.HORA_CORTA, Fecha.getFechaHora(regresar));
          break;
        case HORA_LARGA:
          regresar = Fecha.formatear(Fecha.HORA_LARGA, Fecha.getFechaHora(regresar));
          break;
        case FECHA_HORA:
          regresar = Fecha.formatear(Fecha.FECHA_HORA, Fecha.getFechaHora(regresar));
          break;
        case FECHA_HORA_CORTA:
          regresar = Fecha.formatear(Fecha.FECHA_HORA_CORTA, Fecha.getFechaHora(regresar));
          break;
        case DIA_FECHA_HORA:
          regresar = Fecha.formatear(Fecha.FECHA_HORA_EXTENDIDA, Fecha.getFechaHora(regresar));
          break;
        case DIA_FECHA_HORA_CORTA:
          regresar = Fecha.formatear(Fecha.DIA_FECHA_HORA_CORTA, Fecha.getFechaHora(regresar));
          break;
        case FECHA_HORA_ANTERIOR:
          Calendar anterior = Fecha.getFechaHora(regresar);
          anterior.add(GregorianCalendar.DATE, -1);
          regresar = Fecha.formatear(Fecha.FECHA_HORA, anterior);
          break;
        case MEGAS:
          regresar = Numero.formatear(Numero.NUMERO_CON_DECIMALES, Numero.getDouble(regresar) / 1024 / 1024);
          break;
        case ASTERISCO:
          regresar = Cadena.rellenar("", regresar.length(), '*', true);
          break;
      } // switch
    } // if	
    return regresar;
  }
}
