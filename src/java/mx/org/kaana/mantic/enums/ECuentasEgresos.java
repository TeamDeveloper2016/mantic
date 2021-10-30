package mx.org.kaana.mantic.enums;

public enum ECuentasEgresos {

	EGRESO       ("exportar", "Egresos", 1),
	DOCUMENTO    ("extraer", "NotasDeEntradas", 2),
	NOTA_ENTRADA ("filesEgresosNotasEntradas", "NotasDeEntrada", 3),
	CREDITO_NOTA ("filesEgresosCreditosNotas", "NotasDeCredito", 3),
	EMPRESA_PAGO ("filesEgresosEmpresasPagos", "EmpresasPagos", 3),
	NOTA         ("filesEgresos", "OrdenesDePago", 3);
	
	private String idXml;
	private String title;
	private int group;

	private ECuentasEgresos(String idXml, String title, int group) {
		this.idXml = idXml;
		this.title = title;
    this.group= group;
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

  public int getGroup() {
    return group;
  }
  
}
