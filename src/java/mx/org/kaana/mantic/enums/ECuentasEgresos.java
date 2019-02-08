package mx.org.kaana.mantic.enums;

public enum ECuentasEgresos {

	NOTA_ENTRADA ("filesEgresosNotasEntradas", "NotasEntrada"),
	CREDITO_NOTA ("filesEgresosCreditosNotas", "CreditosNotas"),
	EMPRESA_PAGO ("filesEgresosEmpresasPagos", "EmpresasPagos"),
	NOTA         ("filesEgresos", "Egresos");
	
	private String idXml;
	private String title;

	private ECuentasEgresos(String idXml, String title) {
		this.idXml = idXml;
		this.title = title;
	}	
	
	public Long getKey(){
		return this.ordinal() + 1L;
	} // getKey

	public String getIdXml() {
		return idXml;
	} // getIdXml

	public String getTitle() {
		return title;
	}	// getTitle
}
