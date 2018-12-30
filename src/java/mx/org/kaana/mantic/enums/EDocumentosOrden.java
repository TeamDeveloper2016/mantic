package mx.org.kaana.mantic.enums;

import mx.org.kaana.libs.Constantes;

public enum EDocumentosOrden {

	ORDEN_COMPRA ("entrada", "idOrdenCompra", "/Paginas/Mantic/Compras/Ordenes/accion.jsf", "idOrdenCompra"),
	NOTA_ENTRADA ("devolucion", "idNotaEntrada", "/Paginas/Mantic/Inventarios/Entradas/accion.jsf", "idNotaEntrada"),
	DEVOLUCION   ("credito", "idDevolucion", "/Paginas/Mantic/Inventarios/Devoluciones/accion.jsf", "idDevolucion"),
	NOTA_CREDITO ("", "idCreditoNota", "/Paginas/Mantic/Inventarios/Creditos/accion.jsf", "idCreditoNota");
	
	private String idXml;
	private String idKey;
	private String idParametro;
	private String ruta;

	private EDocumentosOrden(String idXml, String idParametro, String ruta, String idKey) {
		this.idXml = idXml;
		this.idParametro = idParametro;
		this.ruta = ruta;
		this.idKey= idKey;
	}

	public String getIdXml() {
		return idXml;
	}

	public String getIdParametro() {
		return idParametro;
	}	

	public String getRuta() {
		return ruta.concat(Constantes.REDIRECIONAR);
	}	

	public String getIdKey() {
		return idKey;
	}	
}
