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
public enum ESucursales {

	SUCURSAL_A(50L),
	SUCURSAL_B(30L),
	SUCURSAL_C(20L),
	SUCURSAL_D(70L),
	SUCURSAL_E(100L),
	SUCURSAL_F(10L),
	SUCURSAL_G(46L),
	SUCURSAL_H(25L),
	SUCURSAL_I(20L),
	SUCURSAL_J(80L),
	SUCURSAL_K(30L);
	
	private Long clientes;
	private static final Long total= 500L; 

	private ESucursales(Long clientes) {
		this.clientes = clientes;
	}

	public Long getIdKey(){
		return this.ordinal() + 1L;
	}
	
	public String getSucursal() {
		return Cadena.letraCapital(Cadena.reemplazarCaracter(this.name(), '_', ' '));
	}

	public Long getClientes() {
		return this.clientes;
	}

	public static Long getTotal() {
		return total;
	}
	
	public Double getPorcentaje(){
		return new Double(String.valueOf((this.clientes * 100L) / total));
	}
}
