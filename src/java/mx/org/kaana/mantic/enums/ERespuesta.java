package mx.org.kaana.mantic.enums;

public enum ERespuesta {

	CORRECTO       ("01", "Correcto"),
	SIN_USUARIOS   ("02", "No se tienen usuarios registrados y/o nuevos"),
	SIN_PRODUCTOS  ("03", "No se tienen productos registrados y/o nuevos"),
	SIN_EMPRESAS   ("04", "No se tienen empresas registradas y/o nuevas"),
	SIN_ALMACENES  ("05", "No se tienen almacenes registrados y/o nuevos"),
	SIN_UBICACIONES("06", "No se tienen ubicaciones registrados y/o nuevos"),
	SIN_INVENTARIOS("07", "No se tienen inventarios registrados y/o nuevos"),
	SIN_RESPALDO   ("08", "No se encontro ningun respaldo"),
	USUARIO_ERROR      ("09", "El usuario no esta activo"),
	ENROLAMIENTO_ERROR ("10", "No se puedo enrolar el dispositivo"),
	APLICACION_ERROR   ("11", "Esta aplicación no existe"),
	DISPOSITIVO_ERROR  ("12", "Este dispositivo no existe"),
	NO_ACTIVO          ("13", "Este dispositivo no esta activo"),
  SIN_CONTEOS        ("14", "No se tienen conteos registrados y/o nuevos"),
  SIN_TRANSFERENCIAS ("15", "No se tienen transferencias registradas y/o nuevas"),
	TOKEN              ("98", "Token invalido"),
	ERROR              ("99", "Error");	
	
	private String codigo;
	private String descripcion;

	private ERespuesta(String codigo, String descripcion) {
		this.codigo     = codigo;
		this.descripcion= descripcion;
	}	
	
	public Long getIdRespuesta(){
		return this.ordinal()+ 1L;
	} 

	public String getCodigo() {
		return codigo;
	}	

	public String getDescripcion() {
		return descripcion;
	}	
  
}
