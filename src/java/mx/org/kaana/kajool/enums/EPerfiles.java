package mx.org.kaana.kajool.enums;

/**
 *@company KAANA
 *@project  (Sistema de Seguimiento y Control de proyectos estadísticos)
 *@date 23/04/2015
 *@time 04:39:22 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public enum EPerfiles {
	ADMINISTRADOR_ENCUESTA         ("ADMINISTRADOR DE ENCUESTA", 1L),
	ADMINISTRADOR                  ("ADMINISTRADOR", 2L),
  RESPONSABLE_ESTATAL            ("RESPONSABLE ESTATAL", 3L),
  CAPTURISTA                     ("CAPTURISTA", 4L),
  CONSULTOR                      ("CONSULTOR", 5L),
	
	PERSONAL ("PERSONAL", 6L),
	ALUMNO ("ALUMNO", 7L);

  private String descripcion;
	private Long idPerfil;

  private EPerfiles(String descripcion, Long idPerfil) {
    this.descripcion = descripcion;
		this.idPerfil    = idPerfil;
  }

  public String getDescripcion() {
    return descripcion;
  }
	
	public Long getIdPerfil() {
		return idPerfil;
	}

}
