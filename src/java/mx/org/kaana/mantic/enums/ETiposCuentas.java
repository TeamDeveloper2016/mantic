package mx.org.kaana.mantic.enums;

public enum ETiposCuentas {

	TRANSFERENCIAS,
	SERVICIOS;
	
	public Long getKey(){
		return this.ordinal() + 1L;
	}
}
