package mx.org.kaana.mantic.enums;

public enum ECuentasEgresos {

	NOTA_ENTRADA,
	CREDITO_NOTA,
	EMPRESA_PAGO,
	NOTA;
	
	public Long getKey(){
		return this.ordinal() + 1L;
	} // getKey
}
