package mx.org.kaana.mantic.enums;

import mx.org.kaana.kajool.enums.EFormatos;
import mx.org.kaana.libs.reportes.IReportAttribute;

public enum EReportes implements IReportAttribute{

	ORDEN_COMPRA         ("VistaReportesOrdenesComprasDto", "reporteOrden", "Orden de compra", "/Paginas/Mantic/Compras/Ordenes/Reportes/orden", EFormatos.PDF, "orden_de_compra", "/Paginas/Mantic/Compras/Ordenes/filtro"),
	ORDENES_COMPRA       ("VistaReportesOrdenesComprasDto", "ordenesCompra", "Ordenes de compra", "/Paginas/Mantic/Compras/Ordenes/Reportes/ordenesCompra", EFormatos.PDF, "ordenes_de_compra", "/Paginas/Mantic/Compras/Ordenes/filtro"),
	ORDEN_DETALLE        ("VistaReportesOrdenesComprasDto", "ordenDetalle", "Orden de compra", "/Paginas/Mantic/Compras/Ordenes/Reportes/ordenDetalle", EFormatos.PDF, "orden_de_compra_detalle", "/Paginas/Mantic/Compras/Ordenes/filtro"),
	ORDEN_DETALLE_DIF    ("VistaReportesOrdenesComprasDto", "diferenciasOrdenCompra", "Orden de compra", "/Paginas/Mantic/Compras/Ordenes/Reportes/ordenDetalleDiferencias", EFormatos.PDF, "orden_de_compra_diferencias_detalle", "/Paginas/Mantic/Compras/Ordenes/filtro"),
	ORDEN_DETALLE_DIF_DIF("VistaReportesOrdenesComprasDto", "diferenciasOrdenCompra", "Orden de compra", "/Paginas/Mantic/Compras/Ordenes/Reportes/ordenDetalleDiferencias", EFormatos.PDF, "orden_de_compra_diferencias_detalle", "/Paginas/Mantic/Compras/Ordenes/diferencias"),
	ORDEN_DETALLES_COMP  ("VistaReportesOrdenesComprasDto", "detalleCompletoOrdenCompra", "Orden de compra", "/Paginas/Mantic/Compras/Ordenes/Reportes/detCompletoOrdenCompra", EFormatos.PDF, "orden_de_compra_detalles_completos", "/Paginas/Mantic/Compras/Ordenes/filtro"),
	ORDEN_DETALLES_COMP2 ("VistaReportesOrdenesComprasDto", "detalleCompletoOrdenCompra", "Orden de compra", "/Paginas/Mantic/Compras/Ordenes/Reportes/detalleCompletoOrdenCompra", EFormatos.PDF, "orden_de_compra_detalles_completos", "/Paginas/Mantic/Compras/Ordenes/filtro"),
	TICKET_VENTA         ("VistaTicketVentaDto", "ticket", "Ticket de venta", "/Paginas/Mantic/Ventas/Caja/Reportes/ticketVenta", EFormatos.XLS, "ticket_venta", "/Paginas/Mantic/Ventas/Caja/accion"),
	NOTAS_ENTRADA        ("VistaNotasEntradasDto", "lazy", "Notas de Entrada", "/Paginas/Mantic/Inventarios/Entradas/Reportes/notasEntrada", EFormatos.PDF, "ordenes_de_entrada", "/Paginas/Mantic/Inventarios/Entradas/filtro"),
	NOTA_ENTRADA_DETALLE ("VistaReporteNotaEntrada", "detalleNotaEntrada", "Nota de Entrada", "/Paginas/Mantic/Inventarios/Entradas/Reportes/notaEntradaDetalle", EFormatos.PDF, "orden_de_entrada_detalle", "/Paginas/Mantic/Inventarios/Entradas/filtro"),
	NOTA_ENTRADA_DETALLE_D("VistaReporteNotaEntrada", "detalleNotaEntradaDif", "Nota de Entrada", "/Paginas/Mantic/Inventarios/Entradas/Reportes/notaEntradaDetalleDif", EFormatos.PDF, "orden_de_entrada_detalle_diferencias", "/Paginas/Mantic/Inventarios/Entradas/filtro"),
	DEVOLUCIONES         ("VistaDevolucionesDto", "lazy", "Devoluciones", "/Paginas/Mantic/Inventarios/Devoluciones/Reportes/devoluciones", EFormatos.PDF, "devoluciones", "/Paginas/Mantic/Inventarios/Devoluciones/filtro"),
	DEVOLUCIONES_DETALLE ("VistaReporteDevoluciones", "detalleDevolucion", "Devolución", "/Paginas/Mantic/Inventarios/Devoluciones/Reportes/devolucionDetalle", EFormatos.PDF, "devolucion_detalle", "/Paginas/Mantic/Inventarios/Devoluciones/filtro"),
	NOTAS_CREDITO        ("VistaCreditosNotasDto", "lazy", "Notas de credito", "/Paginas/Mantic/Inventarios/Creditos/Reportes/notasCredito", EFormatos.PDF, "notas_de_credito", "/Paginas/Mantic/Inventarios/Caja/Creditos/filtro"),
	NOTA_CREDITO_DETALLE ("VistaReporteNotasCredito", "detalleNostasCredito", "Nota de crédito", "/Paginas/Mantic/Inventarios/Creditos/Reportes/notaCreditoDetalle", EFormatos.PDF, "nota_de_credito_detalle", "/Paginas/Mantic/Inventarios/Caja/Creditos/filtro"),
	CUENTAS_POR_PAGAR    ("VistaEmpresasDto", "cuentasBusqueda", "Cuentas por pagar", "/Paginas/Mantic/Catalogos/Empresas/Cuentas/Reportes/cuentasPorPagar", EFormatos.PDF, "cuentas_por_pagar", "/Paginas/Mantic/Catalogos/Empresas/Cuentas/saldos"),
	CUENTA_PAGAR_DETALLE ("VistaReporteCuentaPorPagarDetalle", "pagosDeuda", "Cuenta por pagar", "/Paginas/Mantic/Catalogos/Empresas/Cuentas/Reportes/cuentaPorPagarDetalle", EFormatos.PDF, "cuenta_por_pagar_detalle", "/Paginas/Mantic/Catalogos/Empresas/Cuentas/saldos"),
	CUENTAS_POR_COBRAR   ("VistaClientesDto", "cuentasBusqueda", "Cuentas por cobrar", "/Paginas/Mantic/Catalogos/Clientes/Cuentas/Reportes/cuentasPorCobrar", EFormatos.PDF, "cuentas_por_cobrar", "/Paginas/Mantic/Catalogos/Clientes/Cuentas/saldos"),
	CUENTA_COBRAR_DETALLE("VistaReporteCuentaPorCobrarDetalle", "cobroDeuda", "Cuenta por pagar", "/Paginas/Mantic/Catalogos/Clientes/Cuentas/Reportes/cuentaPorCobrarDetalle", EFormatos.PDF, "cuenta_por_cobrar_detalle", "/Paginas/Mantic/Catalogos/Clientes/Cuentas/saldos");
  
	private final String proceso;
  private final String idXml;
  private final String titulo; 
  private final String jrxml;
  private final EFormatos formato;
  private final String nombre;
  private final String regresar;

	private EReportes(String proceso, String idXml, String titulo, String jrxml, EFormatos formato, String nombre, String regresar) {
    this.proceso = proceso;
    this.idXml   = idXml;
    this.titulo  = titulo;
    this.jrxml   = jrxml;
    this.formato = formato;
    this.nombre  = nombre;
    this.regresar= regresar;
  }

	@Override
  public String getIdentificador() {
    return EReportes.class.getName();
  }

  @Override
  public String getProceso() {
    return proceso;
  }

  @Override
  public String getIdXml() {
    return idXml;
  }

  @Override
  public String getJrxml() {
    return jrxml;
  }

  @Override
  public EFormatos getFormato() {
    return formato;
  }

  @Override
  public String getNombre() {
    return nombre;
  }

  @Override
  public String getTitulo() {
    return titulo;
  }

  @Override
  public String getRegresar() {
    return regresar;
  }
  
  public Boolean getAutomatico() {
    return Boolean.TRUE;
	}
}
