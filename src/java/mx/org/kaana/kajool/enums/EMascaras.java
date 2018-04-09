package mx.org.kaana.kajool.enums;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 24/03/2015
 *@time 04:16:12 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public enum EMascaras {
	  NO_APLICA									 ("no-aplica",""),
	  FECHA											 ("fecha","fecha"),
		FECHA_HORA								 ("fecha-hora","fecha"),
		REGISTRO								 	 ("registro","fecha"),
		HORA											 ("hora","fecha"),
		HORA_COMPLETA							 ("hora-completa","fecha"),
		TARJETA_CREDITO						 ("tarjeta-credito","numero"),
		DECIMAL										 ("decimal","numero"),
		DECIMAL_SIGNO							 ("decimal-signo","numero"),
		NUMERO										 ("numero","numero"),
		UN_DIGITO									 ("un-digito","numero"),
		DOS_DIGITOS								 ("dos-digitos","numero"),
		TRES_DIGITOS							 ("tres-digitos","numero"),
		CUATRO_DIGITOS						 ("cuatro-digitos","numero"),
		ENTERO										 ("entero","numero"),
		ENTERO_BLANCO							 ("entero-blanco","numero"),
		ENTERO_SIGNO							 ("entero-signo","numero"),
		ENTERO_SIN_SIGNO					 ("entero-sin-signo","numero"),
		FLOTANTE									 ("flotante","numero"),
		FLOTANTE_SIGNO						 ("flotante-signo","numero"),
		RFC												 ("rfc","cadena"),
		CURP											 ("curp","cadena"),
		MONEDA										 ("moneda","numero"),
		MONEDA_DECIMAL						 ("moneda-decimal","numero"),
		MAYUSCULAS								 ("mayusculas","cadena"),
		MINUSCULAS								 ("minusculas","cadena"),
		LETRAS										 ("letras","cadena"),
		CUENTA										 ("cuenta","cadena"),
		NUMEROS_LETRAS						 ("numeros-letras","cadena"),
		LIBRE											 ("libre",""),
		NOMBRE_DTO								 ("nombre-dto","cadena"),
		TEXTO											 ("texto","cadena"),
		SIETE_DIGITOS							 ("siete-digitos","numero"),
		CINCO_DIGITOS							 ("cinco-digitos","numero"),
		TRES_DIGITOS_DEFAULT			 ("tres-digitos-default","numero"),
		TELEFONO									 ("telefono","numero"),
		IP												 ("ip","cadena"),
		VERSION										 ("version","cadena"),
		RESULTADO_ENTREVISTA_MODULO("RESULTADO-ENTREVISTA-MODULO","cadena"),
		RESULTADO_ENTREVISTA_BASICO("RESULTADO-ENTREVISTA-BASICO","cadena"),
		CLAVE_CT_CALL_CENTER			 ("clave-ct-call-center","cadena"),
		CLAVE_CT									 ("clave-ct","cadena"),
		CLAVE_OPERATIVA						 ("clave-operativa","cadena");
	
	private String descripcion;
	private String tipo;
	
	private EMascaras(String descripcion, String tipo) {
		this.descripcion= descripcion;
		this.tipo       = tipo;
	}
	
	public String getDescripcion() {
		return descripcion;
	}
	
	public String getTipo() {
		return tipo;
	}
	
}
