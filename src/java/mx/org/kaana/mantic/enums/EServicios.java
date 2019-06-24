package mx.org.kaana.mantic.enums;

public enum EServicios {

	ARGENTINA ("argentina", "Verificar conexi�n"),
	MEXICO    ("mexico"   , "Verifica �ltimo respaldo");
	
	private String nombre;
	private String descripcion;

	private EServicios(String nombre, String descripcion) {
		this.nombre     = nombre;
		this.descripcion= descripcion;
	}

	public String getNombre() {
		return nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}	
}
