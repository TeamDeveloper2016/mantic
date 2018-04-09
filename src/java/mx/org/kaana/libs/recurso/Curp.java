package mx.org.kaana.libs.recurso;

import mx.org.kaana.libs.beans.Datos;
import mx.org.kaana.kajool.enums.EEntidadesCurp;
import java.util.ArrayList;
import java.util.List;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.libs.reflection.Methods;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 14/04/2015
 * @time 12:22:50 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public final class Curp {

	private final String ALFABETO= "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private final String CONSONANTES= "BCDFGHJKLMNPQRSTVWXYZ";
	private final String NUMEROS= "0123456789";
  private static final Log LOG = LogFactory.getLog(Curp.class);
	private List<String> rfcData;
	private List<String> mariaJose;
	private List<String> preposiciones;
	private List<String> antisonante;
	private List<String> vocales;
	private List<String> vocalesAcentuadas;
	private List<String> caracterEspecial;
	private List<String> curpData;
	private List<String> contribuyenteData;
	private List<String> clasificacionCentroDeTrabajo;
	private List<String> valorData;
	private String p1,p2,p3,p4,p5,p6,p7,p8,p9,p10,p11,p12,p13,p14,p15,p16,p17,p18;

  public Curp() {
    loadData();
  }

  public void loadData() {
		inicializaRfcData();
		inicializaAntisonante();
		inicializaCaracterEspecial();
		inicializaClasificacionCentroDeTrabajo();
		inicializaCurpData();
		inicializaMariaJose();
		inicializaPreposiciones();
		inicializaVocales();
		inicializaValorData();
		inicializaVocalesAcentuadas();
		inicializaContribuyenteData();
	}

	protected void inicializaRfcData() {
		this.rfcData=new ArrayList();
		this.rfcData.add("0");
		this.rfcData.add("00");
		this.rfcData.add("1");
		this.rfcData.add("01");
		this.rfcData.add("2");
		this.rfcData.add("02");
		this.rfcData.add("3");
		this.rfcData.add("03");
		this.rfcData.add("4");
		this.rfcData.add("04");
		this.rfcData.add("5");
		this.rfcData.add("05");
		this.rfcData.add("6");
		this.rfcData.add("06");
		this.rfcData.add("7");
		this.rfcData.add("07");
		this.rfcData.add("8");
		this.rfcData.add("08");
		this.rfcData.add("9");
		this.rfcData.add("09");
		this.rfcData.add("&");
		this.rfcData.add("10");
		this.rfcData.add("A");
		this.rfcData.add("11");
		this.rfcData.add("B");
		this.rfcData.add("12");
		this.rfcData.add("C");
		this.rfcData.add("13");
		this.rfcData.add("D");
		this.rfcData.add("14");
		this.rfcData.add("E");
		this.rfcData.add("15");
		this.rfcData.add("F");
		this.rfcData.add("16");
		this.rfcData.add("G");
		this.rfcData.add("17");
		this.rfcData.add("H");
		this.rfcData.add("18");
		this.rfcData.add("I");
		this.rfcData.add("19");
		this.rfcData.add("J");
		this.rfcData.add("21");
		this.rfcData.add("K");
		this.rfcData.add("22");
		this.rfcData.add("L");
		this.rfcData.add("23");
		this.rfcData.add("M");
		this.rfcData.add("24");
		this.rfcData.add("N");
		this.rfcData.add("25");
		this.rfcData.add("O");
		this.rfcData.add("26");
		this.rfcData.add("P");
		this.rfcData.add("27");
		this.rfcData.add("Q");
		this.rfcData.add("28");
		this.rfcData.add("R");
		this.rfcData.add("29");
		this.rfcData.add("S");
		this.rfcData.add("32");
		this.rfcData.add("T");
		this.rfcData.add("33");
		this.rfcData.add("U");
		this.rfcData.add("34");
		this.rfcData.add("V");
		this.rfcData.add("35");
		this.rfcData.add("W");
		this.rfcData.add("36");
		this.rfcData.add("X");
		this.rfcData.add("37");
		this.rfcData.add("Y");
		this.rfcData.add("38");
		this.rfcData.add("Z");
		this.rfcData.add("39");
		this.rfcData.add(" ");
		this.rfcData.add("00");
		this.rfcData.add("Ñ");
		this.rfcData.add("40");
	}


	protected void inicializaContribuyenteData() {
    this.contribuyenteData=new ArrayList();
		this.contribuyenteData.add("0");
		this.contribuyenteData.add("00");
		this.contribuyenteData.add("1");
		this.contribuyenteData.add("01");
		this.contribuyenteData.add("2");
		this.contribuyenteData.add("02");
		this.contribuyenteData.add("3");
		this.contribuyenteData.add("03");
		this.contribuyenteData.add("4");
		this.contribuyenteData.add("04");
		this.contribuyenteData.add("5");
		this.contribuyenteData.add("05");
		this.contribuyenteData.add("6");
		this.contribuyenteData.add("06");
		this.contribuyenteData.add("7");
		this.contribuyenteData.add("07");
		this.contribuyenteData.add("8");
		this.contribuyenteData.add("08");
		this.contribuyenteData.add("9");
		this.contribuyenteData.add("09");
		this.contribuyenteData.add("A");
		this.contribuyenteData.add("10");
		this.contribuyenteData.add("B");
		this.contribuyenteData.add("11");
		this.contribuyenteData.add("C");
		this.contribuyenteData.add("12");
		this.contribuyenteData.add("D");
		this.contribuyenteData.add("13");
		this.contribuyenteData.add("E");
		this.contribuyenteData.add("14");
		this.contribuyenteData.add("F");
		this.contribuyenteData.add("15");
		this.contribuyenteData.add("G");
		this.contribuyenteData.add("16");
		this.contribuyenteData.add("H");
		this.contribuyenteData.add("17");
		this.contribuyenteData.add("I");
		this.contribuyenteData.add("18");
		this.contribuyenteData.add("J");
		this.contribuyenteData.add("19");
		this.contribuyenteData.add("K");
		this.contribuyenteData.add("20");
		this.contribuyenteData.add("L");
		this.contribuyenteData.add("21");
		this.contribuyenteData.add("M");
		this.contribuyenteData.add("22");
		this.contribuyenteData.add("N");
		this.contribuyenteData.add("23");
		this.contribuyenteData.add("&");
		this.contribuyenteData.add("24");
		this.contribuyenteData.add("O");
		this.contribuyenteData.add("25");
		this.contribuyenteData.add("P");
		this.contribuyenteData.add("26");
		this.contribuyenteData.add("Q");
		this.contribuyenteData.add("27");
		this.contribuyenteData.add("R");
		this.contribuyenteData.add("28");
		this.contribuyenteData.add("S");
		this.contribuyenteData.add("29");
		this.contribuyenteData.add("T");
		this.contribuyenteData.add("30");
		this.contribuyenteData.add("U");
		this.contribuyenteData.add("31");
		this.contribuyenteData.add("V");
		this.contribuyenteData.add("32");
		this.contribuyenteData.add("W");
		this.contribuyenteData.add("33");
		this.contribuyenteData.add("X");
		this.contribuyenteData.add("34");
		this.contribuyenteData.add("Y");
		this.contribuyenteData.add("35");
		this.contribuyenteData.add("Z");
		this.contribuyenteData.add("36");
		this.contribuyenteData.add(" ");
		this.contribuyenteData.add("37");
		this.contribuyenteData.add("Ñ");
		this.contribuyenteData.add("38");
  } // incializaContribuyenteData

	protected void inicializaMariaJose() {
		this.mariaJose=new ArrayList();
		mariaJose.add("MARIA");
		this.mariaJose.add("MA.");
		this.mariaJose.add("MA");
		this.mariaJose.add("M");
		this.mariaJose.add("JOSE");
		this.mariaJose.add("J.");
		this.mariaJose.add("J");
		this.mariaJose.add("J,");
		this.mariaJose.add("JO");
		this.mariaJose.add("M.");
	}

	protected void inicializaPreposiciones() {
		this.preposiciones=new ArrayList();
		this.preposiciones.add("DA");
		this.preposiciones.add("DAS");
		this.preposiciones.add("DE");
		this.preposiciones.add("DEL");
		this.preposiciones.add("DER");
		this.preposiciones.add("DI");
		this.preposiciones.add("DIE");
		this.preposiciones.add("DD");
		this.preposiciones.add("EL");
		this.preposiciones.add("LA");
		this.preposiciones.add("LOS");
		this.preposiciones.add("LAS");
		this.preposiciones.add("LE");
		this.preposiciones.add("LES");
		this.preposiciones.add("MAC");
		this.preposiciones.add("MC");
		this.preposiciones.add("VAN");
		this.preposiciones.add("VON");
		this.preposiciones.add("Y");
		//this.preposiciones.add("D");
		this.preposiciones.add("I");
		this.preposiciones.add("II");
		this.preposiciones.add("III");
		this.preposiciones.add("IV");
		this.preposiciones.add("V");
		this.preposiciones.add("VI");
		this.preposiciones.add("VII");
		this.preposiciones.add("VIII");
		//this.preposiciones.add("IX");

		this.preposiciones.add("JO");
		//this.preposiciones.add("M");
		this.preposiciones.add("J");
		//this.preposiciones.add("MA");
	}

	protected void inicializaAntisonante() {
		this.antisonante=new ArrayList();
		this.antisonante.add("BACA");
		this.antisonante.add("BAKA");
		this.antisonante.add("BUEI");
		this.antisonante.add("BUEY");
		this.antisonante.add("CACA");
		this.antisonante.add("CACO");
		this.antisonante.add("CAGA");
		this.antisonante.add("CAGO");
		this.antisonante.add("CAKA");
		this.antisonante.add("CAKO");
		this.antisonante.add("COGE");
		this.antisonante.add("COGI");
		this.antisonante.add("COJA");
		this.antisonante.add("COJE");
		this.antisonante.add("COJI");
		this.antisonante.add("COJO");
		this.antisonante.add("COLA");
		this.antisonante.add("CULO");
		this.antisonante.add("FALO");
		this.antisonante.add("FETO");
		this.antisonante.add("GETA");
		this.antisonante.add("GUEI");
		this.antisonante.add("GUEY");
		this.antisonante.add("JETA");
		this.antisonante.add("JOTO");
		this.antisonante.add("KACA");
		this.antisonante.add("KACO");
		this.antisonante.add("KAGA");
		this.antisonante.add("KAGO");
		this.antisonante.add("KAKA");
		this.antisonante.add("KAKO");
		this.antisonante.add("KOGE");
		this.antisonante.add("KOGI");
		this.antisonante.add("KOJA");
		this.antisonante.add("KOJE");
		this.antisonante.add("KOJI");
		this.antisonante.add("KOJO");
		this.antisonante.add("KOLA");
		this.antisonante.add("KULO");
		this.antisonante.add("LILO");
		this.antisonante.add("LOCA");
		this.antisonante.add("LOCO");
		this.antisonante.add("LOKA");
		this.antisonante.add("LOKO");
		this.antisonante.add("MAME");
		this.antisonante.add("MAMO");
		this.antisonante.add("MEAR");
		this.antisonante.add("MEAS");
		this.antisonante.add("MEON");
		this.antisonante.add("MIAR");
		this.antisonante.add("MION");
		this.antisonante.add("MOCO");
		this.antisonante.add("MOKO");
		this.antisonante.add("MULA");
		this.antisonante.add("MULO");
		this.antisonante.add("NACA");
		this.antisonante.add("NACO");
		this.antisonante.add("PEDA");
		this.antisonante.add("PEDO");
		this.antisonante.add("PENE");
		this.antisonante.add("PIPI");
		this.antisonante.add("PITO");
		this.antisonante.add("POPO");
		this.antisonante.add("PUTA");
		this.antisonante.add("PUTO");
		this.antisonante.add("QULO");
		this.antisonante.add("RATA");
		this.antisonante.add("ROBA");
		this.antisonante.add("ROBE");
		this.antisonante.add("ROBO");
		this.antisonante.add("RUIN");
		this.antisonante.add("SENO");
		this.antisonante.add("TETA");
		this.antisonante.add("VACA");
		this.antisonante.add("VAGA");
		this.antisonante.add("VAGO");
		this.antisonante.add("VAKA");
		this.antisonante.add("VUEI");
		this.antisonante.add("VUEY");
		this.antisonante.add("WUEI");
		this.antisonante.add("WUEY");
		this.antisonante.add("ORIN");
	}

	protected void inicializaVocales() {
		this.vocales=new ArrayList();
		this.vocales.add("A"); // char(192)
		this.vocales.add("A"); // char(193)
		this.vocales.add("A"); // char(196)
		this.vocales.add("E"); // char(200)
		this.vocales.add("E"); // char(201)
		this.vocales.add("E"); // char(203)
		this.vocales.add("I"); // char(204)
		this.vocales.add("I"); // char(205)
		this.vocales.add("I"); // char(207)
		this.vocales.add("O"); // char(210)
		this.vocales.add("O"); // char(211)
		this.vocales.add("O"); // char(214)
		this.vocales.add("U"); // char(217)
		this.vocales.add("U"); // char(218)
		this.vocales.add("U"); // char(220)
		this.vocales.add("a"); // char(224)
		this.vocales.add("a"); // char(225)
		this.vocales.add("a"); // char(228)
		this.vocales.add("e"); // char(232)
		this.vocales.add("e"); // char(233)
		this.vocales.add("e"); // char(235)
		this.vocales.add("i"); // char(236)
		this.vocales.add("i"); // char(237)
		this.vocales.add("i"); // char(239)
		this.vocales.add("o"); // char(242)
		this.vocales.add("o"); // char(243)
		this.vocales.add("o"); // char(246)
		this.vocales.add("u"); // char(249)
		this.vocales.add("u"); // char(250)
		this.vocales.add("u"); // char(252)
	}

	protected void inicializaVocalesAcentuadas() {
		this.vocalesAcentuadas=new ArrayList();
		this.vocalesAcentuadas.add("À"); // char(192)
		this.vocalesAcentuadas.add("Á"); // char(193)
		this.vocalesAcentuadas.add("Ä"); // char(196)
		this.vocalesAcentuadas.add("È"); // char(200)
		this.vocalesAcentuadas.add("É"); // char(201)
		this.vocalesAcentuadas.add("Ë"); // char(203)
		this.vocalesAcentuadas.add("Ì"); // char(204)
		this.vocalesAcentuadas.add("Í"); // char(205)
		this.vocalesAcentuadas.add("Ï"); // char(207)
		this.vocalesAcentuadas.add("Ò"); // char(210)
		this.vocalesAcentuadas.add("Ó"); // char(211)
		this.vocalesAcentuadas.add("Ö"); // char(214)
		this.vocalesAcentuadas.add("Ù"); // char(217)
		this.vocalesAcentuadas.add("Ú"); // char(218)
		this.vocalesAcentuadas.add("Ü"); // char(220)
		this.vocalesAcentuadas.add("à"); // char(224)
		this.vocalesAcentuadas.add("á"); // char(225)
		this.vocalesAcentuadas.add("ä"); // char(228)
		this.vocalesAcentuadas.add("è"); // char(232)
		this.vocalesAcentuadas.add("é"); // char(233)
		this.vocalesAcentuadas.add("ë"); // char(235)
		this.vocalesAcentuadas.add("ì"); // char(236)
		this.vocalesAcentuadas.add("í"); // char(237)
		this.vocalesAcentuadas.add("ï"); // char(239)
		this.vocalesAcentuadas.add("ò"); // char(242)
		this.vocalesAcentuadas.add("ó"); // char(243)
		this.vocalesAcentuadas.add("ö"); // char(246)
		this.vocalesAcentuadas.add("ù"); // char(249)
		this.vocalesAcentuadas.add("ú"); // char(250)
		this.vocalesAcentuadas.add("ü"); // char(252)
		this.vocalesAcentuadas.add("ñ"); // char(241)
		this.vocalesAcentuadas.add("Ñ"); // char(209)
	}

	protected void inicializaCaracterEspecial() {
		this.caracterEspecial=new ArrayList();
		this.caracterEspecial.add(""); // char(1)
		this.caracterEspecial.add(""); // char(2)
		this.caracterEspecial.add(""); // char(3)
		this.caracterEspecial.add(""); // char(4)
		this.caracterEspecial.add(""); // char(5)
		this.caracterEspecial.add(""); // char(6)
		this.caracterEspecial.add(""); // char(7)
		this.caracterEspecial.add(""); // char(8)
		this.caracterEspecial.add(""); // char(11)
		this.caracterEspecial.add(""); // char(12)
		this.caracterEspecial.add(""); // char(14)
		this.caracterEspecial.add(""); // char(15)
		this.caracterEspecial.add(""); // char(16)
		this.caracterEspecial.add(""); // char(17)
		this.caracterEspecial.add(""); // char(18)
		this.caracterEspecial.add(""); // char(19)
		this.caracterEspecial.add("");// char(20)
		this.caracterEspecial.add(""); // char(21)
		this.caracterEspecial.add(""); // char(22)
		this.caracterEspecial.add(""); // char(23)
		this.caracterEspecial.add(""); // char(24)
		this.caracterEspecial.add(""); // char(25)
		//si se pone el caracter special 26 no permite compilar la clase a?n comentando la linea, jcchg 12-ago-03
		this.caracterEspecial.add(""); // char(27)
//  this.caracterEspecial.add(""); // char(28)
//  this.caracterEspecial.add(""); // char(29)
//  this.caracterEspecial.add(""); // char(30)
//  this.caracterEspecial.add(""); // char(31)
		this.caracterEspecial.add("!"); // char(33)
		this.caracterEspecial.add("\""); // char(34)
		this.caracterEspecial.add("#"); // char(35)
		this.caracterEspecial.add("$"); // char(36)
		this.caracterEspecial.add("%"); // char(37)
		this.caracterEspecial.add("&"); // char(38)
		this.caracterEspecial.add("\\"); // char(39)
		this.caracterEspecial.add("("); // char(40)
		this.caracterEspecial.add(")"); // char(41)
		this.caracterEspecial.add("*"); // char(42)
		this.caracterEspecial.add("+"); // char(43)
		this.caracterEspecial.add(","); // char(44)
		this.caracterEspecial.add("-"); // char(45)
		this.caracterEspecial.add("."); // char(46)
		this.caracterEspecial.add("/"); // char(47)
		this.caracterEspecial.add(":"); // char(58)
		this.caracterEspecial.add(";"); // char(59)
		this.caracterEspecial.add("<"); // char(60)
		this.caracterEspecial.add("="); // char(61)
		this.caracterEspecial.add(">"); // char(62)
		this.caracterEspecial.add("?"); // char(63)
		this.caracterEspecial.add("@"); // char(64)
		this.caracterEspecial.add("["); // char(91)
		this.caracterEspecial.add("\\"); // char(92)
		this.caracterEspecial.add("]"); // char(93)
		this.caracterEspecial.add("^"); // char(94)
		this.caracterEspecial.add("_"); // char(95)
		this.caracterEspecial.add("`"); // char(96)
		this.caracterEspecial.add("{"); // char(123)
		this.caracterEspecial.add("|"); // char(124)
		this.caracterEspecial.add("}"); // char(125)
		this.caracterEspecial.add("~"); // char(126)
		this.caracterEspecial.add(""); // char(127)
	}

	protected void inicializaCurpData() {
		this.curpData=new ArrayList();
		this.curpData.add("0");
		this.curpData.add("0");
		this.curpData.add("1");
		this.curpData.add("1");
		this.curpData.add("2");
		this.curpData.add("2");
		this.curpData.add("3");
		this.curpData.add("3");
		this.curpData.add("4");
		this.curpData.add("4");
		this.curpData.add("5");
		this.curpData.add("5");
		this.curpData.add("6");
		this.curpData.add("6");
		this.curpData.add("7");
		this.curpData.add("7");
		this.curpData.add("8");
		this.curpData.add("8");
		this.curpData.add("9");
		this.curpData.add("9");
		this.curpData.add("A");
		this.curpData.add("10");
		this.curpData.add("B");
		this.curpData.add("11");
		this.curpData.add("C");
		this.curpData.add("12");
		this.curpData.add("D");
		this.curpData.add("13");
		this.curpData.add("E");
		this.curpData.add("14");
		this.curpData.add("F");
		this.curpData.add("15");
		this.curpData.add("G");
		this.curpData.add("16");
		this.curpData.add("H");
		this.curpData.add("17");
		this.curpData.add("I");
		this.curpData.add("18");
		this.curpData.add("J");
		this.curpData.add("19");
		this.curpData.add("K");
		this.curpData.add("20");
		this.curpData.add("L");
		this.curpData.add("21");
		this.curpData.add("M");
		this.curpData.add("22");
		this.curpData.add("N");
		this.curpData.add("23");
		this.curpData.add("Ñ");
		this.curpData.add("24");
		this.curpData.add("O");
		this.curpData.add("25");
		this.curpData.add("P");
		this.curpData.add("26");
		this.curpData.add("Q");
		this.curpData.add("27");
		this.curpData.add("R");
		this.curpData.add("28");
		this.curpData.add("S");
		this.curpData.add("29");
		this.curpData.add("T");
		this.curpData.add("30");
		this.curpData.add("U");
		this.curpData.add("31");
		this.curpData.add("V");
		this.curpData.add("32");
		this.curpData.add("W");
		this.curpData.add("33");
		this.curpData.add("X");
		this.curpData.add("34");
		this.curpData.add("Y");
		this.curpData.add("35");
		this.curpData.add("Z");
		this.curpData.add("36");
		this.curpData.add("-");
		this.curpData.add("37");
	}

	protected void inicializaClasificacionCentroDeTrabajo() {
		this.clasificacionCentroDeTrabajo=new ArrayList();
		this.clasificacionCentroDeTrabajo.add("1");
		this.clasificacionCentroDeTrabajo.add("2");
		this.clasificacionCentroDeTrabajo.add("3");
		this.clasificacionCentroDeTrabajo.add("4");
		this.clasificacionCentroDeTrabajo.add("5");
		this.clasificacionCentroDeTrabajo.add("D");
		this.clasificacionCentroDeTrabajo.add("E");
		this.clasificacionCentroDeTrabajo.add("N");
		this.clasificacionCentroDeTrabajo.add("O");
		this.clasificacionCentroDeTrabajo.add("K");
		this.clasificacionCentroDeTrabajo.add("U");
		this.clasificacionCentroDeTrabajo.add("S");
		this.clasificacionCentroDeTrabajo.add("P");
		this.clasificacionCentroDeTrabajo.add("R");
		this.clasificacionCentroDeTrabajo.add("A");
		this.clasificacionCentroDeTrabajo.add("B");
		this.clasificacionCentroDeTrabajo.add("C");
		this.clasificacionCentroDeTrabajo.add("F");
		this.clasificacionCentroDeTrabajo.add("H");
		this.clasificacionCentroDeTrabajo.add("T");
		this.clasificacionCentroDeTrabajo.add("M");
		this.clasificacionCentroDeTrabajo.add("I");
		this.clasificacionCentroDeTrabajo.add("Z");
		this.clasificacionCentroDeTrabajo.add("CC");
		this.clasificacionCentroDeTrabajo.add("JN");
		this.clasificacionCentroDeTrabajo.add("PB");
		this.clasificacionCentroDeTrabajo.add("PR");
		this.clasificacionCentroDeTrabajo.add("ES");
		this.clasificacionCentroDeTrabajo.add("ST");
		this.clasificacionCentroDeTrabajo.add("TV");
	}

	protected void inicializaValorData() {
		this.valorData=new ArrayList();
		this.valorData.add("1");
		this.valorData.add("0");
		this.valorData.add("2");
		this.valorData.add("1");
		this.valorData.add("3");
		this.valorData.add("2");
		this.valorData.add("4");
		this.valorData.add("3");
		this.valorData.add("5");
		this.valorData.add("4");
		this.valorData.add("6");
		this.valorData.add("5");
		this.valorData.add("7");
		this.valorData.add("6");
		this.valorData.add("8");
		this.valorData.add("7");
		this.valorData.add("9");
		this.valorData.add("8");
		this.valorData.add("A");
		this.valorData.add("9");
		this.valorData.add("B");
		this.valorData.add("10");
		this.valorData.add("C");
		this.valorData.add("11");
		this.valorData.add("D");
		this.valorData.add("12");
		this.valorData.add("E");
		this.valorData.add("13");
		this.valorData.add("F");
		this.valorData.add("14");
		this.valorData.add("G");
		this.valorData.add("15");
		this.valorData.add("H");
		this.valorData.add("16");
		this.valorData.add("I");
		this.valorData.add("17");
		this.valorData.add("J");
		this.valorData.add("18");
		this.valorData.add("K");
		this.valorData.add("19");
		this.valorData.add("L");
		this.valorData.add("20");
		this.valorData.add("M");
		this.valorData.add("21");
		this.valorData.add("N");
		this.valorData.add("22");
		this.valorData.add("O");
		this.valorData.add("0");
		this.valorData.add("P");
		this.valorData.add("23");
		this.valorData.add("Q");
		this.valorData.add("24");
		this.valorData.add("R");
		this.valorData.add("25");
		this.valorData.add("S");
		this.valorData.add("26");
		this.valorData.add("T");
		this.valorData.add("27");
		this.valorData.add("U");
		this.valorData.add("28");
		this.valorData.add("V");
		this.valorData.add("29");
		this.valorData.add("W");
		this.valorData.add("30");
		this.valorData.add("X");
		this.valorData.add("31");
		this.valorData.add("Y");
		this.valorData.add("32");
		this.valorData.add("Z");
		this.valorData.add("33");
	}
	
	public String generarCurp(String paterno, String materno, String nombre, String anio, String mes, String dia, String sexo, String entidad){
		String regresar						= null;
		Long sumaCURPData					= 0L;
    Long sumaRFCData					= 0L;
    Long varPreposicionPaterno= 0L;
    Long varPreposicionNombre = 0L;
    Long varPreposicionMaterno= 0L;
		String a									= null;
		String curp						    = null;
		String varNombre			    = "";
		String varPaterno			    = "";
		String varMaterno			    = "";
		String [] nombreLista     = null;
		String [] paternoLista    = null;
		String [] maternoLista    = null;
		Boolean band							= false;
		try {
			LOG.info("[Curp.generarCURP] generando curp con datos:" + paterno+ " "+ materno+ " "+ nombre+ " "+ anio+ " "+ mes+ " "+ dia+ " "+ sexo+ " "+ entidad);
			paterno= paterno.toUpperCase();
			materno= materno.toUpperCase();
			nombre = nombre.toUpperCase();
			sexo   = sexo.toUpperCase();
			entidad= entidad.toUpperCase();
      paterno= Cadena.sinAcentos(paterno);
      materno= Cadena.sinAcentos(materno);
      nombre = Cadena.sinAcentos(nombre);
      entidad= Cadena.sinAcentos(entidad);
			p1= p2= p3= p4= p5= p6= p7= p8= p9= p10= p11= p12= p13= p14= p15= p16= p17= p18= "";
			// obtener primeros dos caracteres.
      paternoLista = paterno.split(" ");
			for(int i= 0; i< paternoLista.length;i++){
				if(this.preposiciones.indexOf(paternoLista[i])>= 0 & paternoLista.length!= 1 & i== 0){
					p1= "X";
					p2= "X";
				} // if
				else if(this.preposiciones.indexOf(paternoLista[i])== -1 || paternoLista.length== 1){
					for(int j= 0; j< paternoLista[i].length(); j++){
						if (j== 0){
							if (vocalesAcentuadas.indexOf(paternoLista[i].substring(j,j+1))>= 0 || caracterEspecial.indexOf(paternoLista[i].substring(j,j+1))>= 0){
								p1= "X";
								p2= "X";
							} // if
							else{
								p1        = paternoLista[i].substring(j,j+1);
								p2        = "X";
								varPaterno= paternoLista[i];
								band      = true;
							} // else
						} // if
            else{
              if (vocales.indexOf(paternoLista[i].substring(j,j+1))>=0){
                p2= paternoLista[i].substring(j,j+1);
                varPaterno= paternoLista[i];
                band= true;
                break;
              } // if
            } // else
					} // for
          if(band)
            break;
				} // else if
			} // for
			// obtener primer letra apellido materno
      band= false;
      maternoLista= materno.split(" ");
      if(materno.equals("")){
        p3 = "X";
        p15= "X";
      } // if
      else{
        for(int i=0; i<maternoLista.length;i++){
          if(this.preposiciones.indexOf(maternoLista[i])>= 0 && maternoLista.length!= 1 && i== 0)
            p3=  "X";
          else if (this.preposiciones.indexOf(maternoLista[i])== -1 || maternoLista.length== 1){
            for(int j=0; j<maternoLista[i].length();j++){
              varMaterno= maternoLista[i];
              if (this.vocalesAcentuadas.indexOf(maternoLista[i].substring(j, j+1))>= 0 || this.caracterEspecial.indexOf(maternoLista[i].substring(j, j+1))>= 0){
                p3= "X";
                if (maternoLista.length== 1)
                  band= true;
                break;
              } // if
              else{
                p3= maternoLista[i].substring(j, j+1);
                band= true;
                break;
              }  // else
            } // for
          } // else if
          if(band)
            break;
        } // for
      } // else
      // obtener primer letra nombre
      band= false;
      nombreLista= nombre.split(" ");
      for(int i=0; i<nombreLista.length;i++){
        if (this.preposiciones.indexOf(nombreLista[i])>= 0 && nombreLista.length!= 1  && i== 0)
          p4=  "X";
        else if (this.mariaJose.indexOf(nombreLista[i])>= 0 && i== 0){
           p4=  nombreLista[i].substring(0,0);
           varNombre= nombreLista[i];
        } // else if
        else if (this.preposiciones.indexOf(nombreLista[i])== -1 || nombreLista.length== 1){
          for(int j=0;j<nombreLista.length;j++){
            if (this.vocalesAcentuadas.indexOf(nombreLista[i].substring(j, j+1))>= 0 || this.caracterEspecial.indexOf(nombreLista[i].substring(j, j+1))>= 0){
              p4       = "X";
              band     = true;
              varNombre= nombreLista[i];
              break;
            } // if
            else {
              p4       = nombreLista[i].substring(j, j+1);
              band     = true;
              varNombre= nombreLista[i];
              break;
            } // else
          } // for
          if(band)
            break;
        } // else if
      } // for
      // checa que las cuatro primeras letras de la curp on sean palabra antisonante
      if (this.antisonante.indexOf(p1+ p2+ p3+ p4)>= 0)
        p2= "X";
      // agrega caracteres con el año de nacimiento
      p5= anio.substring(2,3);
      p6= anio.substring(3,4);
      // agrega caracteres con el mes de nacimiento
      p7= mes.substring(0, 1);
      p8= mes.substring(1,2);
      //agrega caracteres con el dia de nacimiento
      p9 = dia.substring(0,1);
      p10= dia.substring(1,2);
      // agrega caracteres con el sexo
      p11= sexo;
      // agrega caracteres cla clave del estado
      p12= EEntidadesCurp.valueOf(entidad.replace(" ", "_")).getClave().substring(0,1);
      p13= EEntidadesCurp.valueOf(entidad.replace(" ", "_")).getClave().substring(1,2);
      // ciclo para recorrer letra por letra del Ap Paterno para insertar la primer consonante interna
      for (int i=1; i<varPaterno.length();i++){
        // checa cual es la primer letra interna consonante del Ap Paterno
        if (this.vocales.indexOf(varPaterno.substring(i, i+1))== -1 && this.vocalesAcentuadas.indexOf(varPaterno.substring(i, i+1))== -1 && this.caracterEspecial.indexOf(varPaterno.substring(i, i+1))== -1) {
          p14=  varPaterno.substring(i,i+1);
          break;
        } // if
        if (this.caracterEspecial.indexOf(varPaterno.substring(i, i+1))>= 0 || this.vocalesAcentuadas.indexOf(varPaterno.substring(i, i+1))>= 0){
          p14= "X";
          break;
        } // if
        // si no tiene consonantes en Ap Paterno inserta X
        if(i== varPaterno.length())
          p14= "X";
      } // for
      if (p14.equals("") ||  p14.equals(" ") || varPreposicionPaterno==varPaterno.length())
        p14= "X";

      // ciclo para recorrer letra por letra del Ap Materno para insertar la primer consonante interna
      for (int i=1; i<varMaterno.length();i++){
        // checa cual es la primer letra interna consonante del Ap Materno
        if (this.vocales.indexOf(varMaterno.substring(i, i+1))== -1 && this.vocalesAcentuadas.indexOf(varMaterno.substring(i, i+1))== -1 && this.caracterEspecial.indexOf(varMaterno.substring(i, i+1))== -1) {
          p15=  varMaterno.substring(i,i+1);
          break;
        } // if
        if (this.caracterEspecial.indexOf(varMaterno.substring(i, i+1))>= 0 || this.vocalesAcentuadas.indexOf(varMaterno.substring(i, i+1))>= 0){
          p15= "X";
          break;
        } // if
        // si no tiene consonantes en Ap Materno inserta X
        if(i== varMaterno.length())
          p15= "X";
      } // for
      if (p15.equals("") ||  p15.equals(" ") || varPreposicionMaterno==varMaterno.length())
        p15= "X";
      // ciclo para recorrer letra por letra del Nombre para insertar la primer consonante interna
      for (int i=1; i<varNombre.length();i++){
        // checa cual es la primer letra interna consonante del Ap Materno
        if (this.vocales.indexOf(varNombre.substring(i, i+1))== -1 && this.vocalesAcentuadas.indexOf(varNombre.substring(i, i+1))== -1 && this.caracterEspecial.indexOf(varNombre.substring(i, i+1))== -1) {
          p16=  varNombre.substring(i,i+1);
          break;
        } // if
        if (this.caracterEspecial.indexOf(varNombre.substring(i, i+1))>= 0 || this.vocalesAcentuadas.indexOf(varNombre.substring(i, i+1))>= 0){
          p16= "X";
          break;
        } // if
        // si no tiene consonantes en Ap Materno inserta X
        if(i== varNombre.length())
          p16= "X";
      } // for
      if (p16.equals("") ||  p16.equals(" ") || varPreposicionNombre==varNombre.length())
        p16= "X";
      // checa cual es el diferenciador de homonimia y siglo
      if (Numero.getLong(p5+p6)>=13)
        p17= "0";
      else
        p17= "A";

      // checa cual es el digito verificador
      curp= p1+ p2+ p3+ p4+ p5+ p6+ p7+ p8+ p9+ p10+ p11+ p12+ p13+ p14+ p15+ p16+ p17;
      for (int i=1;i<17;i++){
        if (this.curpData.indexOf(curp.substring(i-1,i))>0)
          sumaCURPData= sumaCURPData+ Long.parseLong(this.curpData.get((this.curpData.indexOf(curp.substring(i-1,i))+ 1)))* (19- i);
      } // for
      if((10-(sumaCURPData%10))!=10)
        p18= String.valueOf(10-(sumaCURPData%10));
      else
        p18= "0";
      regresar= p1+ p2+ p3+ p4+ p5+ p6+ p7+ p8+ p9+ p10+ p11+ p12+ p13+ p14+ p15+ p16+ p17+ p18;

		} // try
		catch (Exception e) {
			Error.mensaje(e);
		} // catch
    return regresar;
	} // generarCurp

  public Datos validarCurp(String curp, String paterno, String materno, String nombre, String anio, String mes, String dia, String sexo, String entidad) throws Exception{
    Datos regresar       = new Datos();
    String curpResultante= "";
    String curpDatos     = "";
    try{
      regresar.setVerdadero(true);
      LOG.info("[Curp.validarCURP] validando curp: "+ curp+ " con datos: "+ " "+ paterno+ " "+ materno+ " "+ nombre+ " "+ anio+ " "+ mes+ " "+ dia+ " "+ sexo+ " "+ entidad);
      curpDatos= generarCurp(paterno, materno, nombre,anio, mes, dia, sexo, entidad);
      for(int i=0; i<16; i++){
        if(!curp.substring(i,i+1).equals(curpDatos.substring(i,i+1))){
          regresar.setVerdadero(false);
          curpResultante= curpResultante.concat("[").concat(curp.substring(i,i+1)).concat("] ").concat(curpDatos.substring(i,i+1));
          switch(i){
            case 0:
            case 1:
              regresar.setCadena(String.valueOf(i+1));
              regresar.setTexto("No corresponde el apellido paterno en la CURP");
              break;
            case 2:
              regresar.setCadena(String.valueOf(i+1));
              regresar.setTexto("No corresponde el apellido materno en la CURP");
              break;
            case 3:
              regresar.setCadena(String.valueOf(i+1));
              regresar.setTexto("No corresponde el nombre en la CURP");
              break;
            case 4:
            case 5:
              regresar.setCadena(String.valueOf(i+1));
              regresar.setTexto("No corresponde el año en la fecha de la curp");
              break;
            case 6:
            case 7:
              regresar.setCadena(String.valueOf(i+1));
              regresar.setTexto("No corresponde el mes en la fecha de la curp");
              break;
            case 8:
            case 9:
              regresar.setCadena(String.valueOf(i+1));
              regresar.setTexto("No corresponde el dia en la fecha de la curp");
              break;
            case 10:
              regresar.setCadena(String.valueOf(i+1));
              regresar.setTexto("No corresponde el sexo en la fecha de la curp");
              break;
            case 11:
            case 12:
              regresar.setCadena(String.valueOf(i+1));
              regresar.setTexto("No corresponde la clave del estado en la curp");
              break;
            case 13:
              regresar.setCadena(String.valueOf(i+1));
              regresar.setTexto("No corresponde la consonante del apellido paterno en la curp");
              break;
            case 14:
              regresar.setCadena(String.valueOf(i+1));
              regresar.setTexto("No corresponde la consonante del apellido materno en la curp");
              break;
            case 15:
              regresar.setCadena(String.valueOf(i+1));
              regresar.setTexto("No corresponde la consonante del nombre en la curp");
              break;
          }
        } // if
        else{
          curpResultante= curpResultante + curp.substring(i, i+1);
          regresar.setVerdadero(true);
          regresar.setTexto("");
        } // switch
      } // for
    } // catch
    catch(Exception e){
      throw new Exception("Curp no válida");
    }
    return regresar;
  } // validarCurp

  public Datos validarRfc(String rfc, String paterno, String materno, String nombre, String anio, String mes, String dia){
    Datos regresar      = new Datos();
    String rfcResultante= "";
    String rfcDatos     = "";
    try{
      regresar.setVerdadero(true);
      LOG.info("[Curp.validarRfc] validando rfc: "+ rfc+ " con datos: "+ paterno+ " "+ materno+ " "+ nombre+ " "+ anio+ " "+ mes+ " "+ dia);
      rfcDatos= generarRfc(paterno, materno, nombre,anio, mes, dia);
      for(int i=0; i<10; i++){
        if(!rfc.substring(i,i+1).equals(rfcDatos.substring(i,i+1))){
          regresar.setVerdadero(false);
          rfcResultante= rfcResultante.concat("[").concat(rfc.substring(i,i+1)).concat("] ").concat(rfcDatos.substring(i,i+1));
          switch(i){
            case 0:
            case 1:
              regresar.setCadena(String.valueOf(i+1));
              regresar.setTexto("No corresponde el apellido paterno en el RFC");
              break;
            case 2:
              regresar.setCadena(String.valueOf(i+1));
              regresar.setTexto("No corresponde el apellido materno en el RFC");
              break;
            case 3:
              regresar.setCadena(String.valueOf(i+1));
              regresar.setTexto("No corresponde el nombre en el RFC");
              break;
            case 4:
            case 5:
              regresar.setCadena(String.valueOf(i+1));
              regresar.setTexto("No corresponde el año en la fecha de el RFC");
              break;
            case 6:
            case 7:
              regresar.setCadena(String.valueOf(i+1));
              regresar.setTexto("No corresponde el mes en la fecha de el RFC");
              break;
            case 8:
            case 9:
              regresar.setCadena(String.valueOf(i+1));
              regresar.setTexto("No corresponde el dia en la fecha de el RFC");
              break;
          } // switch
        } // if
        else{
          rfcResultante= rfcResultante + rfc.substring(i, i+1);
          regresar.setVerdadero(true);
          regresar.setTexto("");
        }
      } // for
    } // catch
    catch(Exception e){
      Error.mensaje(e);
    }
    return regresar;
  } // validarCurp

  public Datos verificarCurp(String curp){
    Datos regresar       = new Datos();
    String anio          = "";
    String texto         = "";
    String curpResultante= "";
    boolean bandera      = true;
    try{
      texto= "Ok";
      LOG.info("[Curp.verificarCurp] curp: "+ curp);
      for(int i=0; i<4;i++){
        if(ALFABETO.indexOf(curp.substring(i,i+1))== 0){
          curpResultante= curp.substring(0,i)+ '['+ curp.substring(i,i+1)+ ']'+ curp.substring(i+1,17-i);
          texto         = "Existe un carácter no alfabético en el nombre de la CURP";
          bandera       = false;
          regresar.setCadena(String.valueOf(i)); // '|P1|P2|P3|P4|';
          break;
        } // if
      } // for
      if(bandera){
        for(int i= 4; i<10;i++){
          if(NUMEROS.indexOf(curp.substring(i,i+1))==0){
            curpResultante= curp.substring(0,i)+ '['+ curp.substring(i,i+1)+ ']'+ curp.substring(i+1,17-i);
            texto         = "fecha incorrecta con caracter no numerico en la CURP";
            bandera       = false;
            regresar.setCadena(String.valueOf(i)); // '|P1|P2|P3|P4|';
            break;
          }
        } // for
      } // if
      if(bandera){
        for(int i=0; i<18; i++){
          if(Numero.getLong(curp.substring(6,8))> 12 && i>= 5 && i< 10){
            curpResultante= curp.substring(0,6)+ '['+ curp.substring(6,8)+ ']'+ curp.substring(8,10);
            texto= "Fecha incorrecta con más de 12 meses en la CURP";
            regresar.setCadena("7");
            break;
          } // if
          else{
            if(Numero.getLong(curp.substring(8,10))>31 && ((i>= 4) && (i< 10))){
              curpResultante= curp.substring(0,9)+ '['+ curp.substring(8,10)+ ']'+ curp.substring(10,17);
              texto= "Fecha incorrecta con más de 31 días en la CURP";
              regresar.setCadena("9");
              break;
            } // if
            else if (Numero.getLong(curp.substring(8,10))== 29L && (i>= 5 && i<= 10) && curp.substring(8,10).equals("02")){
              if(Numero.getLong(curp.substring(4,6))>=13)
                anio= "19".concat(curp.substring(4,6));
              else
                anio= "20".concat(curp.substring(4,6));
              if(!(Numero.getLong(anio)% 4== 0 && Numero.getLong(anio)% 100> 0) || Numero.getLong(anio)% 400== 0){
                curpResultante= curp.substring(0,8)+ '['+ curp.substring(8,10)+ ']'+ curp.substring(10,17);
                texto= "Fecha incorrecta el año no es bisiesto en la CURP";
                regresar.setCadena("5");
                break;
              } // if
            } // else if
            else if(!curp.substring(10,11).equals("H") && !curp.substring(10,11).equals("M") && i== 10){
              curpResultante= curp.substring(0,10)+ '['+ curp.substring(10,11)+ ']'+ curp.substring(11,17);
              texto= "clave de sexo incorrecta en la CURP";
              regresar.setCadena(String.valueOf(i));
              break;
            } // else if
            else if (!buscarClaveEstado(curp.substring(11,13))  && (i>= 11 & i<= 12)){
              curpResultante= curp.substring(0,11)+ '['+ curp.substring(11,13)+ ']'+ curp.substring(13,17);
              texto= "clave de estado incorrecta en la CURP";
              regresar.setCadena(String.valueOf(i));
              break;
            } // else if
            else if(CONSONANTES.indexOf(curp.substring(i,i+1))== 0 && (i>= 13 && i<= 14)){
              curpResultante= curp.substring(0,i-1)+ '['+ curp.substring(i,i+1)+ curp.substring(i+1,17);
              texto= "Existe un caracter no consonante en la CURP";
              regresar.setCadena(String.valueOf(i));
              break;
            } // else if
          } // else
        } // for
      } // if
      regresar.setVerdadero(texto.equals("Ok"));
      regresar.setCurp(curpResultante);
      regresar.setTexto(texto);
    } // try
    catch(Exception e){
      Error.mensaje(e);
    } // catch
    return regresar;
  } // verificarCurp

  protected boolean buscarClaveEstado(String clave){
    boolean regresar= false;
    for(int i= 0; i< EEntidadesCurp.values().length;i++){
      if(EEntidadesCurp.values()[i].getClave().equals(clave)){
        regresar= true;
        break;
      } // if
    } // for
    return regresar;
  }

  public String generarRfc(String paterno, String materno, String nombre, String anio, String mes, String dia){
		String regresar						= "";
    Long sumaCURPData         = 0L;
    Long multiplicaRFCCadena  = 0L;
    String rfc						    = "";
		String varNombre			    = "";
		String varPaterno			    = "";
		String varMaterno			    = "";
		String cadenaRFCData	    = "";
		String [] nombreLista     = null;
		String [] paternoLista    = null;
		String [] maternoLista    = null;
		Boolean band							= false;
		Boolean bandApellidoCorto	= false;
		Boolean bandApellidoNulo 	= false;
		try {
      loadData();
			LOG.info("[Curp.generarRfc] generando curp con datos:" + paterno+ " "+ materno+ " "+ nombre+ " "+ anio+ " "+ mes+ " "+ dia+ " ");
			paterno= paterno.toUpperCase();
			materno= materno.toUpperCase();
			nombre = nombre.toUpperCase();
      paterno= Cadena.sinAcentos(paterno);
      materno= Cadena.sinAcentos(materno);
      nombre = Cadena.sinAcentos(nombre);
			p1= p2= p3= p4= p5= p6= p7= p8= p9= p10= p11= p12= p13= "";
			paternoLista= paterno.split(" ");
      maternoLista= materno.split(" ");
      nombreLista= nombre.split(" ");
			if(paternoLista.length==1 && (paternoLista[0].length()==1 || paternoLista[0].length()==2)){
        for(int i= 0; i< paternoLista.length;i++){
          if(!(this.preposiciones.indexOf(paternoLista[i])>=0)){
            p1= paternoLista[i].substring(0,1);
            break;
          } // if
        } // for
        for(int i= 0; i< maternoLista.length;i++){
          if(!(this.preposiciones.indexOf(maternoLista[i])>=0)){
            p2= maternoLista[i].substring(0,1);
            break;
          } // if
        } // for
        if(nombreLista.length>1){
          if (!(this.mariaJose.indexOf(nombreLista[0])>= 0)){
            p3= nombre.substring(0,1);
            p4= nombre.substring(1,2);
          } // if
          else{
            p3= nombreLista[1].substring(0,1);
            p4= nombreLista[1].substring(1,2);
          } // else
        } // else
        else{
          p3= nombre.substring(0,1);
          p4= nombre.substring(1,2);
        } // else
        bandApellidoCorto= true;
      }
      if(paterno.equals("")){
        for(int i= 0; i< maternoLista.length;i++){
          if(!(this.preposiciones.indexOf(maternoLista[i])>=0)){
            p1= materno.substring(0,1);
            p2= materno.substring(1,2);;
            break;
          } // if
        }
        if(nombreLista.length>1){
          if (!(this.mariaJose.indexOf(nombreLista[0])>= 0)){
            p3= nombre.substring(0,1);
            p4= nombre.substring(1,2);
          } // if
          else{
            p3= nombreLista[1].substring(0,1);
            p4= nombreLista[1].substring(1,2);
          } // else
        } // else
        else{
          p3= nombre.substring(0,1);
          p4= nombre.substring(1,2);
        } // else
        bandApellidoNulo= true;
      } //
      if(materno.equals("")){
        for(int i= 0; i< paternoLista.length;i++){
          if(!(this.preposiciones.indexOf(paternoLista[i])>=0)){
            p1= paterno.substring(0,1);
            p2= paterno.substring(1,2);;
            break;
          } // if
        } // for
        if(nombreLista.length>1){
          if (!(this.mariaJose.indexOf(nombreLista[0])>= 0)){
            p3= nombre.substring(0,1);
            p4= nombre.substring(1,2);
          } // if
          else{
            p3= nombreLista[1].substring(0,1);
            p4= nombreLista[1].substring(1,2);
          } // else
        } // else
        else{
          p3= nombre.substring(0,1);
          p4= nombre.substring(1,2);
        } // else
        bandApellidoNulo= true;
      } //
			if(!bandApellidoCorto && !bandApellidoNulo){	
        for(int i= 0; i< paternoLista.length;i++){
          if(this.preposiciones.indexOf(paternoLista[i])>= 0 & paternoLista.length!= 1 & i== 0){
            p1= "X";
            p2= "X";
          } // if
          else if(this.preposiciones.indexOf(paternoLista[i])== -1 || paternoLista.length== 1){
            for(int j= 0; j< paternoLista[i].length(); j++){
              if (j== 0){
                if (vocalesAcentuadas.indexOf(paternoLista[i].substring(j,j+1))>= 0 || caracterEspecial.indexOf(paternoLista[i].substring(j,j+1))>= 0){
                  p1= "X";
                  p2= "X";
                } // if
                else{
                  p1        = paternoLista[i].substring(j,j+1);
                  p2        = "X";
                  varPaterno= paternoLista[i];
                  band      = true;
                } // else
              } // if
              else{
                if (vocales.indexOf(paternoLista[i].substring(j,j+1))>=0){
                  p2= paternoLista[i].substring(j,j+1);
                  varPaterno= paternoLista[i];
                  band= true;
                  break;
                } // if
              } // else
            } // for
            if(band)
              break;
          } // else if
        } // for
        band= false;
        maternoLista= materno.split(" ");
        if(materno.equals(""))
          p3 = "X";
        else{
          for(int i=0; i<maternoLista.length;i++){
            if(this.preposiciones.indexOf(maternoLista[i])>= 0 && maternoLista.length!= 1 && i== 0)
              p3=  "X";
            else if (this.preposiciones.indexOf(maternoLista[i])== -1 || maternoLista.length== 1){
              for(int j=0; j<maternoLista[i].length();j++){
                varMaterno= maternoLista[i];
                if (this.vocalesAcentuadas.indexOf(maternoLista[i].substring(j, j+1))>= 0 || this.caracterEspecial.indexOf(maternoLista[i].substring(j, j+1))>= 0){
                  p3= "X";
                  if (maternoLista.length== 1)
                    band= true;
                  break;
                } // if
                else{
                  p3= maternoLista[i].substring(j, j+1);
                  band= true;
                  break;
                }  // else
              } // for
            } // else if
            if(band)
              break;
          } // for
        } // else
        band= false;
        for(int i=0; i<nombreLista.length;i++){
          if (this.preposiciones.indexOf(nombreLista[i])>= 0 && nombreLista.length!= 1  && i== 0)
            p4=  "X";
          else if (this.mariaJose.indexOf(nombreLista[i])>= 0 && i== 0){
             p4=  nombreLista[i].substring(0,0);
             varNombre= nombreLista[i];
          } // else if
          else if (this.preposiciones.indexOf(nombreLista[i])== -1 || nombreLista.length== 1){
            for(int j=0;j<nombreLista.length;j++){
              if (this.vocalesAcentuadas.indexOf(nombreLista[i].substring(j, j+1))>= 0 || this.caracterEspecial.indexOf(nombreLista[i].substring(j, j+1))>= 0){
                p4       = "X";
                band     = true;
                varNombre= nombreLista[i];
                break;
              } // if
              else {
                p4       = nombreLista[i].substring(j, j+1);
                band     = true;
                varNombre= nombreLista[i];
                break;
              } // else
            } // for
            if(band)
              break;
          } // else if
        } // for
      } //if
      // checa que las cuatro primeras letras de la curp on sean palabra antisonante
      if (this.antisonante.indexOf(p1+ p2+ p3+ p4)>= 0)
        p4= "X";
      // agrega caracteres con el año de nacimiento
      p5= anio.substring(2,3);
      p6= anio.substring(3,4);
      // agrega caracteres con el mes de nacimiento
      p7= mes.substring(0, 1);
      p8= mes.substring(1,2);
      //agrega caracteres con el dia de nacimiento
      p9 = dia.substring(0,1);
      p10= dia.substring(1,2);
      cadenaRFCData= "0";
      for (int i= 0; i<paterno.length();i++)
        cadenaRFCData= cadenaRFCData+ rfcData.get(rfcData.indexOf(paterno.substring(i,i+1))+1);
      cadenaRFCData= cadenaRFCData+ "00";
      for (int i= 0;i<materno.length();i++)
        cadenaRFCData= cadenaRFCData+ rfcData.get(rfcData.indexOf(materno.substring(i,i+1))+1);
      cadenaRFCData= cadenaRFCData+ "00";
      for (int i= 0;i<nombre.length();i++)
        cadenaRFCData= cadenaRFCData+ rfcData.get(rfcData.indexOf(nombre.substring(i,i+1))+1);
      cadenaRFCData= cadenaRFCData+ "00";
      for(int i=0; i<cadenaRFCData.length()-1;i++){
        multiplicaRFCCadena= multiplicaRFCCadena + Numero.getLong(cadenaRFCData.substring(i,i+2)) * Numero.getLong(cadenaRFCData.substring(i+1,i+2));
      } // for
      p11= (valorData.get(valorData.lastIndexOf(String.valueOf((Numero.getLong(String.valueOf(multiplicaRFCCadena).substring(String.valueOf(multiplicaRFCCadena).length()-3, String.valueOf(multiplicaRFCCadena).length()))/34)))-1));
      p12= (valorData.get(valorData.lastIndexOf(String.valueOf((Numero.getLong(String.valueOf(multiplicaRFCCadena).substring(String.valueOf(multiplicaRFCCadena).length()-3, String.valueOf(multiplicaRFCCadena).length()))%34)))-1));
      // checa cual es el digito verificador
      rfc= p1+ p2+ p3+ p4+ p5+ p6+ p7+ p8+ p9+ p10+ p11+ p12;
      for(int i= 0; i<12; i++){
        if (contribuyenteData.indexOf(rfc.substring(i,i+1))> 0)
          sumaCURPData= sumaCURPData+ Numero.getLong(contribuyenteData.get(contribuyenteData.indexOf(rfc.substring(i,i+1))+ 1)) * (13- i);
      } // for
      p13= sumaCURPData % 11==0?"0":String.valueOf(11-(sumaCURPData % 11));
      if (p13.equals("10"))
        p13= "A";
      regresar= p1+ p2+ p3+ p4+ p5+ p6+ p7+ p8+ p9+ p10+ p11+ p12+ p13;
    } // try
    catch(Exception e){
      Error.mensaje(e);
    } // catch
    return regresar;
  }

  @Override
  protected void finalize() throws Throwable {
    Methods.clean(this.antisonante);
    Methods.clean(this.caracterEspecial);
    Methods.clean(this.clasificacionCentroDeTrabajo);
    Methods.clean(this.valorData);
    Methods.clean(this.curpData);
    Methods.clean(this.mariaJose);
  }

}
