package mx.org.kaana.kajool.enums;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 10/04/2015
 *@time 11:19:02 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public enum EEncabezadosCargaPersonal {
	CLAVE_PLANTEL       ("Clave del plantel"),
  RFC                 ("RFC"),
  CURP                ("CURP"),
	NOMBRES             ("Nombre completo"),
	PRIMER_APELLIDO     ("Primer apellido"),
	SEGUNDO_APELLIDO    ("Segundo apellido"),
  SEXO                ("Sexo"),
	FECHA_NACIMIENTO    ("Fecha de nacimiento"),
	LUGAR_NACIMIENTO    ("Lugar de nacimiento"),
  ENTIDAD             ("Entidad"),
  MUNICIPIO           ("Municipio"),
  C_P                 ("C.P."),
	CORREO_ELECTRONICO  ("Correo electrónico"),
	NUMERO_TELEFONICO   ("Número de teléfono"),
	NIVEL_ESTUDIOS      ("Nivel máximo de estudios"),
	ANIO_GRADO          ("Año o grado"),
	ESTATUS             ("Estatus"),
	TURNO               ("Turno"),
	RELACION_CONTRACTUAL("Relación contractual"),
	TIEMPO_DE_JORNADA   ("Tiempo de jornada"),
	FUNCION             ("Función"),
	PLAN_DE_ESTUDIOS    ("Plan de estudios")
	;
	
	String encabezado;

	private EEncabezadosCargaPersonal(String encabezado) {
		this.encabezado=encabezado;
	}

	public String getEncabezado() {
		return encabezado;
	}
}
