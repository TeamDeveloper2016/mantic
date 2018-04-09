package mx.org.kaana.kajool.enums;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 28/04/2015
 *@time 05:57:26 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public enum ETipoAccion {
	
	CARGA(1L),
	ALTA_MOVIL(2L),
	ELIMINADO(3L),
	ALTA_WEB(4L);

	private Long key;

	private ETipoAccion(Long key) {
		this.key=key;
	}

	public Long getKey() {
		return key;
	}
	
}
