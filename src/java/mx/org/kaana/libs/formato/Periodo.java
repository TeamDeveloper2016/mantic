package mx.org.kaana.libs.formato;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Periodo {


  private int dia;
  private int mes;
  private int anio;
  private String separador= null;

  /** Creates a new instance of Periodo */
  public Periodo() {
    setSeparador("/");
    this.dia = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    this.mes = Calendar.getInstance().get(Calendar.MONTH)+ 1;
    this.anio= Calendar.getInstance().get(Calendar.YEAR);
  }

  public Periodo(int dia, int mes, int anio) {
    this.setSeparador("/");
    this.dia = dia;
    this.mes = mes;
    this.anio= anio;
  }

  public Periodo(String valor, String separador) {
    this.setSeparador(separador);
    valor       = getFormatoValido(valor);
    this.dia    = getIntDia(valor.substring(6, 8));
    this.mes    = getIntMes(valor.substring(4, 6));
    this.anio   = getIntAnio(valor.substring(0, 4));
  }

  public Periodo(String valor) {
    this(valor, "/");
  }

  public Periodo(Periodo valor) {
    this(valor.toString(), valor.getSeparador());
  }

  private static int getIntDia(String dia) {
    try {
      int valor= Integer.parseInt(dia);
      return valor> 31 || valor< 0? Calendar.getInstance().get(Calendar.DAY_OF_MONTH): valor;
    }
    catch(Exception e) {
      Error.mensaje(e);
      return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }
  }

  private static int getIntMes(String mes) {
    try {
      int valor= Integer.parseInt(mes);
      return valor> 12 || valor< 0? Calendar.getInstance().get(Calendar.MONTH)+ 1: valor;
    }
    catch(Exception e) {
      Error.mensaje(e);
      return Calendar.getInstance().get(Calendar.MONTH)+ 1;
    }
  }

  private static int getIntAnio(String anio) {
    try {
      int valor= Integer.parseInt(anio);
      return valor> 9999 || valor< 1900? Calendar.getInstance().get(Calendar.YEAR): valor;
    }
    catch(Exception e) {
      Error.mensaje(e);
      return Calendar.getInstance().get(Calendar.YEAR);
    }
  }

  public int getDia() {
    return this.dia;
  }

  public void setDia(int dia) {
    this.dia= dia> 0 && dia<= getDiasEnElMes()? dia: getDia();
  }

  public int getMes() {
    return this.mes;
  }

  public int getAnio() {
    return this.anio;
  }

  private String getDosDigitos(int valor) {
    return valor< 10? "0"+ valor: String.valueOf(valor);
  }

  public String toString() {
    return anio+ getDosDigitos(mes)+ getDosDigitos(dia);
  }

  public String getSeparador() {
    return separador;
  }

  public void setSeparador(String separador) {
    this.separador = separador;
  }

  public String toPeriodo() {
    return getDosDigitos(dia)+ getSeparador()+ getDosDigitos(mes)+ getSeparador()+ anio;
  }

  private String getFormatoValido(String valor) {
    if (valor== null)
      return Calendar.getInstance().get(Calendar.YEAR)+ getDosDigitos(Calendar.getInstance().get(Calendar.MONTH)+ 1)+ getDosDigitos(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
    else
      if (valor.indexOf(getSeparador())>= 0)
        return valor.substring(6, 10)+ valor.substring(3, 5)+ valor.substring(0, 2);
      else
        return valor;
  }

  public int getDiasEnElMes() {
    GregorianCalendar calendario= new GregorianCalendar(getAnio(), getMes()- 1, getDia());
    return calendario.getActualMaximum(Calendar.DAY_OF_MONTH);
  }

  public String toProbar() {
    return "dias: ["+ getDiasEnElMes()+ "] fecha: "+ toPeriodo();
  }

  private Periodo getDiaComodin(int corrimiento) {
    GregorianCalendar calendario= new GregorianCalendar(getAnio(), getMes()- 1, getDia());
    calendario.add(calendario.DAY_OF_MONTH, corrimiento);
    return new Periodo(calendario.get(Calendar.DAY_OF_MONTH), calendario.get(Calendar.MONTH)+ 1, calendario.get(Calendar.YEAR));
  }

  public Periodo getDiaSiguiente() {
    return getDiaComodin(1);
  }

  public Periodo getDiaAnterior() {
    return getDiaComodin(-1);
  }

  public boolean isQuincenaImpar() {
    return getDia()< 16;
  }

  public Periodo getInicioQuincena() {
    return new Periodo(isQuincenaImpar()? 1: 16, getMes(), getAnio());
  }

  public Periodo getTerminoQuincena() {
    return new Periodo(isQuincenaImpar()? 15: getDiasEnElMes(), getMes(), getAnio());
  }

  public int getQuincena() {
    return (getMes()* 2- 1)+ (isQuincenaImpar()? 0: 1);
  }

  public Periodo getInicioMes() {
    return new Periodo(1, getMes(), getAnio());
  }

  public Periodo getTerminoMes() {
    return new Periodo(getDiasEnElMes(), getMes(), getAnio());
  }

  public Periodo getInicioAnio() {
    return new Periodo(1, 1, getAnio());
  }

  public Periodo getTerminoAnio() {
    return new Periodo(31, 12, getAnio());
  }

  public Periodo getInicioSemestre() {
    return new Periodo(1, getMes()< 7? 1: 7, getAnio());
  }

  public Periodo getTerminoSemestre() {
    return new Periodo(getMes()< 7? 30: 31, getMes()< 7? 6: 12, getAnio());
  }

  private int getDiaAnual() {
    GregorianCalendar calendario= new GregorianCalendar(getAnio(), getMes()- 1, getDia());
    return calendario.get(calendario.DAY_OF_YEAR);
  }

  public void addMeses(int meses) {
    GregorianCalendar calendario= new GregorianCalendar(getAnio(), getMes()- 1, getDia());
    calendario.add(GregorianCalendar.MONTH, meses);
    this.anio= calendario.get(GregorianCalendar.YEAR);
    this.mes = calendario.get(GregorianCalendar.MONTH)+ 1;
    this.dia = calendario.get(GregorianCalendar.DATE);
  }
	
  public void addDias(int dias) {
    GregorianCalendar calendario= new GregorianCalendar(getAnio(), getMes()- 1, getDia());
    calendario.add(GregorianCalendar.DATE, dias);
    this.anio= calendario.get(GregorianCalendar.YEAR);
    this.mes = calendario.get(GregorianCalendar.MONTH)+ 1;
    this.dia = calendario.get(GregorianCalendar.DATE);
  }


  public Periodo getPeriodoEspecial(int meses) {
    GregorianCalendar calendario= new GregorianCalendar(getAnio(), getMes()- 1, getDia());
    calendario.add(GregorianCalendar.MONTH, meses);
    return new Periodo(calendario.get(GregorianCalendar.DATE), calendario.get(GregorianCalendar.MONTH)+ 1, calendario.get(GregorianCalendar.YEAR));
  }

  public boolean equals(String fecha) {
    return toString().equals(fecha);
  }

  public boolean equals(Periodo fecha) {
    return equals(fecha.toString());
  }

}
