package mx.org.kaana.mantic.enums;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 19/06/2018
 *@time 07:51:53 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public enum ETipoMovimiento {
  
	ORDENES_COMPRAS("orden(es) de compra(s)", "ordenes", "idOrdenCompra", "VistaOrdenesComprasDto"), 
	NOTAS_ENTRADAS("nota(s) de entrada(s)", "notas", "idNotaEntrada", "VistaNotasEntradasDto"), 
	VENTAS("venta(s)", "ventas", "idVenta", "VistaVentasDto"), 
	SERVICIOS("servicio(s)", "servicios", "idServicio", "VistaTallerServiciosDto"), 
	DEVOLUCIONES("devolucion(es)", "devoluciones", "idDevolucion", "VistaDevolucionesDto"),
	NOTAS_CREDITOS("nota(s) de credito(s)", "creditos_notas", "idCreditoNota", "VistaNotasCreditosDto"),
	CIERRES_CAJA("cierre(s) de caja", "cierres", "idCierre", "VistaCierresCajasDto"),
	FACTURAS_FICTICIAS("factura(s)", "ficticias", "idFicticia", "VistaFicticiasDto"),
	TRANSFERENCIAS("transferencia(s)", "transferencias", "idTransferencia", "VistaAlmacenesTransferenciasDto");
	 
	private final String title;
	private final String table;
	private final String idKey;
	private final String proceso;
	
	private ETipoMovimiento(String title, String table, String idKey, String proceso) {
		this.title= title;
		this.table= table;
		this.idKey= idKey;
		this.proceso= proceso;
	}
	
	public String getTitle() {
		return "Movimientos generados de las ".concat(this.title);
	}
	
	public String getTable() {
		return "tc_mantic_".concat(this.table);
	}
	
	public String getIdKey() {
		return this.idKey;
	}
	
	public String getProceso() {
		return proceso;
	}
	
}
