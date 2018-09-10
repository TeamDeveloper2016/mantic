/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.kaana.kajool.enums;

import mx.org.kaana.libs.formato.Cadena;

/**
 *
 * @author JORGE.VAZQUEZSER
 */
public enum ECajas {

	CAJA_1(12000L),
	CAJA_2(7500L),
	CAJA_3(10500L);
	
	private Long utilidad;
	private static final Long total= 30000L; 

	private ECajas(Long utilidad) {
		this.utilidad= utilidad;
	}

	public Long getIdKey(){
		return this.ordinal() + 1L;
	}
	
	public String getCaja() {
		return Cadena.letraCapital(Cadena.reemplazarCaracter(this.name(), '_', ' '));
	}

	public Long getUtilidad() {
		return this.utilidad;
	}

	public static Long getTotal() {
		return total;
	}
	
	public Double getPorcentaje(){
		return new Double(String.valueOf((this.utilidad * 100L) / total));
	}
}
