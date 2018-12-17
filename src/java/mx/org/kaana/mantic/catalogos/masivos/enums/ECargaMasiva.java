package mx.org.kaana.mantic.catalogos.masivos.enums;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 13/12/2018
 *@time 04:38:57 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public enum ECargaMasiva {
	
	ARTICULOS(1L, "CODIGO|CODIGOAUXILIAR|NOMBRE|COSTOS/IVA|MENUDEONETO|MEDIONETO|MAYOREONETO|UNIDADMEDIDA|IVA|LIMITEMENUDEO|LIMITEMAYOREO|STOCKMINIMO|STOCKMAXIMO", "CODIGO,CODIGO,AUXILIAR,NOMBRE,COSTO S/IVA,MENUDEO NETO,MEDIO NETO,MAYOREO NETO,UNIDAD MEDIDA,IVA,LIMITE MENUDEO,LIMITE MAYOREO,STOCK MINIMO,STOCK MAXIMO", 13), 
	CLIENTES(2L, "RFC|CLAVE|RAZONSOCIAL|USOCFDI|TELEFONO|CORREO1|CORREO2|ENTIDAD|MUNICIPIO|COLONIA|CALLE|NUMERO|CODIGOPOSTAL", "RFC,CLAVE,RAZON SOCIAL,USO CFDI,TELEFONO,CORREO 1,CORREO 2,ENTIDAD,MUNICIPIO,COLONIA,CALLE,NUMERO,CODIGOPOSTAL", 13), 
	PROVEEDORES(3L, "RFC|CLAVE|RAZONSOCIAL|TELEFONO|CORREO1|CORREO2|ENTIDAD|MUNICIPIO|COLONIA|CALLE|NUMERO|CODIGOPOSTAL", "RFC,CLAVE,RAZON SOCIAL,TELEFONO,CORREO 1,CORREO 2,ENTIDAD,MUNICIPIO,COLONIA,CALLE,NUMERO,CODIGOPOSTAL", 12);
	
	private Long id;
	private String fields;
	private String titles;
	private Integer columns;
	
	private ECargaMasiva(Long id, String fields, String titles, Integer columns) {
		this.id= id;
		this.fields= fields;
		this.titles= titles;
		this.columns= columns;
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
	
}
