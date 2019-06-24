package mx.org.kaana.mantic.enums;

public enum ERespuesta {

	CORRECTO    ("01", "Correcto"),
	SIN_RESPALDO("02", "No se encontro ningun respaldo."),
	ERROR       ("99", "Error");	
	
	private String codigo;
	private String descripcion;

	private ERespuesta(String codigo, String descripcion) {
		this.codigo     = codigo;
		this.descripcion= descripcion;
	}	
	
	public Long getIdRespuesta(){
		return this.ordinal() + 1L;
	} 

	public String getCodigo() {
		return codigo;
	}	

	public String getDescripcion() {
		return descripcion;
	}	
}
