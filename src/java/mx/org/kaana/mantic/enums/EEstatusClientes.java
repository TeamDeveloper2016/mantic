package mx.org.kaana.mantic.enums;

public enum EEstatusClientes {

	INICIADA,
	PARCIALIZADA,
	FINALIZADA;
	
	public Long getIdEstatus(){
		return this.ordinal() + 1L;
	} 
}
