package mx.org.kaana.mantic.enums;

public enum ETiposDomicilios {

	FISICO,
	FISCAL;
	
	public Long getKey(){
		return this.ordinal() + 1L;
	}
}
