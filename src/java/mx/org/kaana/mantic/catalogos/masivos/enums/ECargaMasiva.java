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
	
	ARTICULOS(1L, "CODIGO|CODIGOAUXILIAR|NOMBRE|COSTOS/IVA|MENUDEONETO|MEDIONETO|MAYOREONETO|UNIDADMEDIDA|IVA|LIMITEMENUDEO|LIMITEMAYOREO|STOCKMINIMO|STOCKMAXIMO|SAT|CODIGOFABRICANTE", "CODIGO,CODIGO,AUXILIAR,NOMBRE,COSTO S/IVA,MENUDEO NETO,MEDIO NETO,MAYOREO NETO,UNIDAD MEDIDA,IVA,LIMITE MENUDEO,LIMITE MAYOREO,STOCK MINIMO,STOCK MAXIMO,SAT,CODIGO FABRICANTE", 15, 1000, "masivo-articulos.xls"), 
	CLIENTES(2L, "RFC|CLAVE|RAZONSOCIAL|USOCFDI|TELEFONO|CORREO1|CORREO2|ENTIDAD|MUNICIPIO|COLONIA|CALLE|NUMERO|CODIGOPOSTAL", "RFC,CLAVE,RAZON SOCIAL,USO CFDI,TELEFONO,CORREO 1,CORREO 2,ENTIDAD,MUNICIPIO,COLONIA,CALLE,NUMERO,CODIGOPOSTAL", 13, 50, "masivo-clientes.xls"), 
	PROVEEDORES(3L, "RFC|CLAVE|RAZONSOCIAL|TELEFONO|CORREO1|CORREO2|ENTIDAD|MUNICIPIO|COLONIA|CALLE|NUMERO|CODIGOPOSTAL", "RFC,CLAVE,RAZON SOCIAL,TELEFONO,CORREO 1,CORREO 2,ENTIDAD,MUNICIPIO,COLONIA,CALLE,NUMERO,CODIGOPOSTAL", 12, 50, "masivo-proveedores.xls"),
	REFACCIONES(4L, "CODIGO|NOMBRE|HERRAMIENTA|COSTO|PRECIOMENUDEO|IVA|SAT|RFC|CLAVEPROVEEDOR", "CODIGO,NOMBRE,HERRAMIENTA,COSTO,PRECIO MENUDEO,IVA,SAT,RFC,CLAVE PROVEEDOR", 9, 500, "masivo-refacciones.xls"),
	SERVICIOS(5L, "CODIGO|NOMBRE|PRECIOMENUDEO|IVA|SAT|LINEA", "CODIGO,NOMBRE,PRECIO MENUDEO,IVA,SAT,LINEA", 6, 100, "masivo-servicios.xls"),
	EGRESOS(6L, "FECHA|DESCRIPCION|IMPORTE", "FECHA,DESCRIPCION,IMPORTE", 3, 500, "masivo-egresos.xls"),
	CONTEOS(7L, "ALMACEN|CODIGO|NOMBRE|STOCKMINIMO|STOCKMAXIMO|STOCK", "ALMACEN,CODIGO,NOMBRE,STOCK MINIMO,STOCK MAXIMO,STOCK", 6, 500, "masivo-conteos.xls"),
	CODIGOS(8L, "PROVEEDOR|CODIGOPROPIO|CODIGOPROVEEDOR|MULTIPLO", "PROVEEDOR,CODIGO PROPIO,CODIGO PROVEEDOR,MULTIPLO", 4, 500, "masivo-codigos.xls");
	
	private final Long id;
	private final String fields;
	private final String titles;
	private final Integer columns;
	private final Integer tuplas;
	private final String file;
	
	private ECargaMasiva(Long id, String fields, String titles, Integer columns, Integer tuplas, String file) {
		this.id= id;
		this.fields= fields;
		this.titles= titles;
		this.columns= columns;
		this.tuplas = tuplas;
		this.file = file;
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

	public String getFile() {
		return file;
	}
	
}
