package mx.org.kaana.kajool.enums;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 30/05/2014
 *@time 03:29:43 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public enum ECortes {
	
	CENTRAL(1L),
	REGIONAL(2L),
	ESTATAL(3L),
	MUNICIPIO(4L),
	LOCALIDAD(5L),
	OFICINA(6L);

	private Long ambito;
	
	private ECortes (Long ambito){
		this.ambito= ambito;
	}

	public Long getAmbito() {
		return ambito;
	}
	
}
