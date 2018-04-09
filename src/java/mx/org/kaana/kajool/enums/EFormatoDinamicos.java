package mx.org.kaana.kajool.enums;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date May 30, 2012
 *@time 8:55:09 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public enum EFormatoDinamicos {

	LIBRE(""),
	MAYUSCULAS("MAYUSCULAS"),
	MINUSCULAS("minusculas"),
	LETRA_CAPITAL("Letra capital"),
	NOMBRE_DE_PERSONA("Nombre Persona"),
	MILES_CON_DECIMALES("###,##0.00"),
	MILES_SIN_DECIMALES("###,##0"),
	MONEDA_CON_DECIMALES("$ ###,##0.00"),
	MONEDA_SIN_DECIMALES("$ ###,##0"),
	NUMERO_SIN_DECIMALES("#####0"),
	NUMERO_CON_DECIMALES("#####0.00"),
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
	FECHA_HORA_ANTERIOR("dd/MM/yyyy HH:mm:ss");
	
	private String patron;

	private EFormatoDinamicos(String patron) {
		this.patron=patron;
	}

	public String getPatron() {
		return patron;
	}
	
}
