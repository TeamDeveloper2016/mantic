package mx.org.kaana.kajool.enums;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 10/04/2015
 *@time 11:19:02 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public enum EEncabezadosCargaAlumno {
	CLAVE_PLANTEL       ("Clave del plantel"),
	MATRICULA           ("Matrícula"),
	NOMBRES             ("Nombre completo"),
	PRIMER_APELLIDO     ("Primer apellido"),
	SEGUNDO_APELLIDO    ("Segundo apellido"),
  CURP                ("CURP"),
	SEXO                ("Sexo"),
	FECHA_NACIMIENTO    ("Fecha de nacimiento"),
	LUGAR_NACIMIENTO    ("Lugar de nacimiento"),
	ENTIDAD             ("Entidad"),
	MUNICICPIO          ("Municipio"),
	C_P                 ("C.P."),
  CORREO_ELECTRONICO  ("Correo electrónico"),
	NUMERO_TELEFONICO   ("Número de teléfono"),
	LENGUA_INDIGENA     ("Lengua indígena"),
	DISCAPACIDAD        ("Tipo de discapacidad"),
	PLAN_DE_ESTUDIOS    ("Plan de estudios al que está inscrito"),
	MES_INSCRIPCION     ("Mes"),
	ANIO_INSCRIPCION    ("Año de ingreso"),
	MATERIAS            ("Materias"),
	ESTATUS_ASIGNATURAS ("Estatus de las asignaturas"),
	CALIFICACION        ("Calificaciones"),
	BECA                ("Recibe beca")
	;
	
	String encabezado;

	private EEncabezadosCargaAlumno(String encabezado) {
		this.encabezado=encabezado;
	}

	public String getEncabezado() {
		return encabezado;
	}
}
