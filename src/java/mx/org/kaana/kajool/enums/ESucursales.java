/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.kaana.kajool.enums;

import mx.org.kaana.libs.formato.Cadena;

/**
 *
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public enum ESucursales {

	SUCURSAL_A(30000L, "FERRETERIA BONANZA", 1560L, "PILAR BLANCO"),
	SUCURSAL_B(21500L, "FERRETERIA BONANZA, SUC. JESÚS MARÍA", 1200L, "JESÚS MARÍA");
	
	private Long utilidad;
	private Long ventas;
	private String nombre;
	private String titulo;
	private static final Long total= 51500L; 

	private ESucursales(Long utilidad, String nombre, Long ventas, String titulo) {
		this.utilidad= utilidad;
		this.nombre  = nombre;
		this.ventas  = ventas;
		this.titulo  = titulo;
	}

	public Long getIdKey(){
		return this.ordinal() + 1L;
	}
	
	public String getSucursal() {
		return Cadena.letraCapital(Cadena.reemplazarCaracter(this.name(), '_', ' '));
	}
	
	public String getNombreSucursal() {
		return this.nombre;
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

	public Long getVentas() {
		return ventas;
	}

	public String getTitulo() {
		return titulo;
	}
}
