package mx.org.kaana.kajool.enums;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 1/09/2015
 * @time 10:35:35 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public enum EReporte {

	USUARIO("VistaGruposTrabajoUsuariosDto", "row", "Usuarios", "Usuarios", "/Paginas/Usuarios/Reportes/gruposTrabajo", EFormatos.PDF, "/Paginas/Usuarios/filtro", Boolean.TRUE, Boolean.TRUE),
	CUESTIONARIO("Cuestionarios", "cuestionarioPrincipal", "ENCUESTA DEPERCEPCIÓN Y SATISFACCIÓN DE LAS FAMILIAS BENEFICIARIAS DE PROESPA SOBRE SU PARTICIPACIÓN EN EL PROGRAMA", "Cuestionario", "/Paginas/Cuestionarios/Reportes/principal", EFormatos.PDF, "/Paginas/Cuestionarios/filtro", Boolean.TRUE, Boolean.TRUE),
	CUESTIONARIO_NACIONAL("Cuestionarios", "cuestionarioPrincipal", "ENCUESTA DEPERCEPCIÓN Y SATISFACCIÓN DE LAS FAMILIAS BENEFICIARIAS DE PROESPA SOBRE SU PARTICIPACIÓN EN EL PROGRAMA", "Cuestionario", "/Paginas/Cuestionarios/Reportes/principal", EFormatos.PDF, "/Paginas/Cuestionarios/nacional", Boolean.TRUE, Boolean.TRUE),
	INTEGRANTES("Cuestionarios", "cuestionarioIntegrantes", "Integrantes", "Integrantes", "/Paginas/Cuestionarios/Reportes/integrantes.jasper", EFormatos.PDF, "/Paginas/Cuestionarios/filtro", Boolean.TRUE, Boolean.TRUE),
  CONTENIDO("Cuestionarios", "cuestionarioIntegrantes", "Integrantes", "Integrantes", "/Paginas/Cuestionarios/Reportes/contenido", EFormatos.PDF, "/Paginas/Cuestionarios/filtro", Boolean.TRUE, Boolean.TRUE);
  
	private final String proceso;
	private final String idXml;
	private final String titulo;
	private final String nombre;
	private final String jasper;
	private final EFormatos formato;
	private final String paginaRegreso;
	private final Boolean automatico;
	private final Boolean comprimir;

	private EReporte(String proceso, String idXml, String titulo, String nombre, String jasper, EFormatos formato, String paginaRegreso, Boolean automatico, Boolean comprimir) {
		this.proceso=proceso;
		this.idXml=idXml;
		this.titulo=titulo;
		this.nombre=nombre;
		this.jasper=jasper;
		this.formato=formato;
		this.paginaRegreso=paginaRegreso;
		this.automatico=automatico;
		this.comprimir=comprimir;
	} //GruposTrabajo

	public Boolean getAutomatico() {
		return automatico;
	}

	public Boolean getComprimir() {
		return comprimir;
	}

	public EFormatos getFormato() {
		return formato;
	}

	public String getIdXml() {
		return idXml;
	}

	public String getJasper() {
		return jasper;
	}

	public String getNombre() {
		return nombre;
	}

	public String getPaginaRegreso() {
		return paginaRegreso;
	}

	public String getProceso() {
		return proceso;
	}

	public String getTitulo() {
		return titulo;
	}
  
}
