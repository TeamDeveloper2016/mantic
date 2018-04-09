package mx.org.kaana.kajool.enums;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 7/09/2015
 *@time 11:10:19 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public enum EPerfilesDatosGenerales {

	PERSONA(""),
	EMPLEADO("mx.org.kaana.personal.db.dto.TcJanalEmpleadosDto"),
	ALUMNO("mx.org.kaana.alumnos.db.dto.TcJanalAlumnosDto");
	
	private String ruta;

	private EPerfilesDatosGenerales(String ruta) {
		this.ruta=ruta;
	}

	public String getRuta() {
		return ruta;
	}

		
}
