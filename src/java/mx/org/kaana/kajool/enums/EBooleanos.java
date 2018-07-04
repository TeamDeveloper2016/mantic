package mx.org.kaana.kajool.enums;

public enum EBooleanos {
	
	SI,
	NO;
	
	public Long getIdBooleano(){
		return this.ordinal() + 1L;
	} // getIdBooleano
}
