package mx.org.kaana.mantic.catalogos.masivos.enums;

import java.io.Serializable;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 13/12/2018
 *@time 04:38:57 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public enum ECargaMasiva implements Serializable {
	
	ARTICULOS(1L, "CODIGO|CODIGOAUXILIAR|NOMBRE|COSTOS/IVA|MENUDEONETO|MEDIONETO|MAYOREONETO|UNIDADMEDIDA|IVA|LIMITEMENUDEO|LIMITEMAYOREO|STOCKMINIMO|STOCKMAXIMO", "CODIGO,CODIGO,AUXILIAR,NOMBRE,COSTO S/IVA,MENUDEO NETO,MEDIO NETO,MAYOREO NETO,UNIDAD MEDIDA,IVA,LIMITE MENUDEO,LIMITE MAYOREO,STOCK MINIMO,STOCK MAXIMO", 13, 1000), 
	CLIENTES(2L, "RFC|CLAVE|RAZONSOCIAL|USOCFDI|TELEFONO|CORREO1|CORREO2|ENTIDAD|MUNICIPIO|COLONIA|CALLE|NUMERO|CODIGOPOSTAL", "RFC,CLAVE,RAZON SOCIAL,USO CFDI,TELEFONO,CORREO 1,CORREO 2,ENTIDAD,MUNICIPIO,COLONIA,CALLE,NUMERO,CODIGOPOSTAL", 13, 50), 
	PROVEEDORES(3L, "RFC|CLAVE|RAZONSOCIAL|TELEFONO|CORREO1|CORREO2|ENTIDAD|MUNICIPIO|COLONIA|CALLE|NUMERO|CODIGOPOSTAL", "RFC,CLAVE,RAZON SOCIAL,TELEFONO,CORREO 1,CORREO 2,ENTIDAD,MUNICIPIO,COLONIA,CALLE,NUMERO,CODIGOPOSTAL", 12, 50),
	REFACCIONES(4L, "CODIGO|CODIGOAUXILIAR|NOMBRE|HERRAMIENTA|COSTOS/IVA|IVA", "CODIGO,CODIGO AUXILIAR,NOMBRE,HERRAMIENTA,COSTO S/IVA,IVA", 6, 500);
	
	private final Long id;
	private final String fields;
	private final String titles;
	private final Integer columns;
	private final Integer tuplas;
	
	private ECargaMasiva(Long id, String fields, String titles, Integer columns, Integer tuplas) {
		this.id= id;
		this.fields= fields;
		this.titles= titles;
		this.columns= columns;
		this.tuplas = tuplas;
	}

	public Long getId() {
		return id;
	}

	public String getFields() {
		return fields;
	}

	public String getTitles() {
		return titles;
	}

	public Integer getColumns() {
		return columns;
	}

	public Integer getTuplas() {
		return tuplas;
	}
	
}
