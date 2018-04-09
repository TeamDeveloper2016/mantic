package mx.org.kaana.kajool.enums;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 13/04/2015
 *@time 12:05:12 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public enum ETipoCemesCarga {
	ALUMNOS ("Alumnos", 1L),
	PERSONAL("Personal", 2L);
	
	String descripcion;
	Long idKajoolTipoCarga;

	private ETipoCemesCarga(String descripcion, Long idKajoolTipoCarga) {
		this.descripcion= descripcion;
		this.idKajoolTipoCarga= idKajoolTipoCarga;
	}

	public Long getIdCemesTipoCarga() {
		return idKajoolTipoCarga;
	}

	public String getDescripcion() {
		return descripcion;
	}
}
