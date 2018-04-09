package mx.org.kaana.kajool.enums;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 24/03/2015
 *@time 03:08:44 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public enum EValidaciones {
	REQUERIDO									 ("REQUERIDO","requerido", "general"),
	ENTERO										 ("ENTERO","entero","numero"),
	ENTERO_SIGNO							 ("ENTERO-SIGNO","entero-signo", "numero"),
	FLOTANTE									 ("FLOTANTE","flotante", "numero"),
	FLOTANTE_SIGNO						 ("FLOTANTE-SIGNO","flotante-signo", "numero"),
	MAYUSCULAS								 ("MAYUSCULAS","mayusculas", "cadena"),
	MINUSCULAS								 ("MINUSCULAS","minusculas", "cadena"),
	RANGO											 ("RANGO","rango({\"min\":2,\"max\":5})", "numero"),
	HORA											 ("HORA","hora", "fecha"),
	LETRAS										 ("LETRAS","letras", "cadena"),
	TEXTO											 ("TEXTO","texto", "cadena"),
	CURP											 ("CURP","curp", "cadena"),
	RFC												 ("RFC","rfc", "cadena"),
	TEXTO_ESPECIAL						 ("TEXTO-ESPECIAL","texto-especial", "cadena"),
	BOLEANO										 ("BOLEANO","boleano","cadena"),
	FECHA											 ("FECHA","fecha","fecha"),
	MIN_CARACTERES						 ("MIN-CARACTERES","min-caracteres({\"cuantos\":4})", "cadena"),
	MAX_CARACTERES						 ("MAX-CARACTERES","max-caracteres({\"cuantos\":5})", "cadena"),
	MIN_VALOR									 ("MIN-VALOR","min-valor({\"cuanto\":5})", "numero"),
	MAX_VALOR									 ("MAX-VALOR","max-valor({\"cuanto\":5})", "numero"),
	COMODIN										 ("COMODIN","comodin({\"expresion\":\"2=2\"})", "cadena"),
	REGISTRO									 ("REGISTRO","registro","fecha"),
	HORA_COMPLETA							 ("HORA-COMPLETA",	"hora-completa","fecha"),
	MONEDA										 ("MONEDA", "moneda", "numero"),
	MONEDA_DECIMAL						 ("MONEDA-DECIMAL", "moneda-decimal", "numero"),
	ASTERISCO									 ("ASTERISCO", "asterisco", "cadena"),
	NO_PERMITIR								 ("NO-PERMITIR", "no-permitir({\"valor\":5})","numero"),
	VALOR_SIMPLE							 ("VALOR_SIMPLE", "valor-simple", "cadena"),
	SECUENCIA_PALABRA					 ("SECUENCIA-PALABRA","secuencia-palabra", "cadena"),
	TELEFONO								   ("TELEFONO","telefono", "cadena"),
	CONTIENE_A								 ("CONTIENE-A", "contiene-a", "cadena"),
	VOCALES										 ("VOCALES", "vocales", "cadena"),
	LONGITUD								   ("LONGITUD", "longitud({\"min\":5\"max\":10})", "cadena"),
	IGUAL_A										 ("IGUAL-A", "igual-a({\"cual\":\"comparativo\"})", "numero"),
	MENOR_A										 ("MENOR-A", "menor-a({\"cual\":\"comparativo\"})", "numero"),
	MAYOR_A										 ("MAYOR-A", "mayor-a({\"cual\":\"comparativo\"})", "numero"),
	FECHA_MENOR								 ("FECHA-MENOR", "fecha-menor({\"cual\":\"fecha-mayor\"})","fecha"),
	FECHA_MAYOR								 ("FECHA-MAYOR", "fecha-mayor({\"cual\":\"fecha-menor\"})","fecha"),
	IPV4											 ("IPV4", "ipv4", "cadena"),
	IPV6											 ("IPV6", "ipv6", "cadena"),
	RESULTADO_ENTREVISTA_MODULO("RESULTADO-ENTREVISTA-MODULO", "resultado-entrevista-modulo", "cadena"),
	RESULTADO_ENTREVISTA_BASICO("RESULTADO-ENTREVISTA-BASICO", "resultado-entrevista-basico", "cadena"),
	ESTA_EN										 ("ESTA_EN", "esta-en({\"cuantos\":\"1,2,3,4-7,8\"})","numero");
	
	private String descripcion;
	private String sintaxis;
	private String tipo;

	private EValidaciones(String descripcion, String sintaxis, String tipo) {
		this.descripcion=descripcion;
		this.sintaxis=sintaxis;
		this.tipo=tipo;
	}

	public String getSintaxis() {
		return sintaxis;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public String getTipo() {
		return tipo;
	}
	
}
