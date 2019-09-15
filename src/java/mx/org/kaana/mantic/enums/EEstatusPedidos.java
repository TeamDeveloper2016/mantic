package mx.org.kaana.mantic.enums;

public enum EEstatusPedidos {
	
	ABIERTO,
	ACTUALIZADO,
	CERRADO,
	INICIALIZADO,
	CANCELADO,
	SURTIDO,
	ENVIADO,
	RECIBIDO;
	
	public Long getIdEstatus(){
		return this.ordinal() + 1L;
	} // getIdEstatus
}
