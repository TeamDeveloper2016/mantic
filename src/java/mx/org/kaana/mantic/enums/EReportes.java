package mx.org.kaana.mantic.enums;

import mx.org.kaana.kajool.enums.EFormatos;
import mx.org.kaana.libs.reportes.IReportAttribute;

public enum EReportes implements IReportAttribute{

	ORDEN_COMPRA        ("VistaReportesOrdenesComprasDto", "reporteOrden", "Orden de compra", "/Paginas/Mantic/Compras/Ordenes/Reporte/orden", EFormatos.PDF, "orden_de_compra", "/Paginas/Mantic/Compras/filtro"),
	ORDENES_COMPRA      ("VistaReportesOrdenesComprasDto", "ordenesCompra", "Ordenes de compra", "/Paginas/Mantic/Compras/Ordenes/Reporte/ordenesCompra", EFormatos.PDF, "ordenes_de_compra", "/Paginas/Mantic/Compras/filtro"),
	ORDEN_DETALLE       ("VistaReportesOrdenesComprasDto", "ordenDetalle", "Orden de compra detalle", "/Paginas/Mantic/Compras/Ordenes/Reporte/ordenDetalle", EFormatos.PDF, "orden_de_compra_detalle", "/Paginas/Mantic/Compras/filtro"),
	TICKET_VENTA        ("VistaTicketVentaDto", "ticket", "Ticket de venta", "/Paginas/Mantic/Ventas/Caja/Reportes/ticketVenta", EFormatos.XLS, "ticket_venta", "/Paginas/Mantic/Ventas/Caja/accion"),
	NOTAS_ENTRADA       ("VistaNotasEntradasDto", "lazy", "Notas de Entrada", "/Paginas/Mantic/Inventarios/Entradas/Reportes/notasEntrada", EFormatos.PDF, "ordenes_de_entrada", "/Paginas/Mantic/Inventarios/Entradas/filtro"),
	NOTA_ENTRADA_DETALLE("VistaReporteNotaEntrada", "detalleNotaEntrada", "Nota de Entrada", "/Paginas/Mantic/Inventarios/Entradas/Reportes/notaEntradaDetalle", EFormatos.PDF, "orden_de_entrada_detalle", "/Paginas/Mantic/Inventarios/Entradas/filtro"),
	DEVOLUCIONES        ("VistaDevolucionesDto", "lazy", "Devoluciones", "/Paginas/Mantic/Inventarios/Devoluciones/Reportes/devoluciones", EFormatos.PDF, "devoluciones", "/Paginas/Mantic/Inventarios/Devoluciones/filtro"),
	DEVOLUCIONES_DETALLE("VistaReporteDevoluciones", "detalleDevolucion", "Devolución", "/Paginas/Mantic/Inventarios/Devoluciones/Reportes/devolucionDetalle", EFormatos.PDF, "devolucion_detalle", "/Paginas/Mantic/Inventarios/Devoluciones/filtro"),
	NOTA_CREDITO        ("VistaCreditosNotasDto", "lazy", "Notas de credito", "/Paginas/Mantic/Inventarios/Creditos/Reportes/notasCredito", EFormatos.PDF, "notas_de_credito", "/Paginas/Mantic/Inventarios/Caja/Creditos/filtro"),
	NOTA_CREDITO_DETALLE("VistaReporteNotasCredito", "detalleNostasCredito", "Nota de crédito", "/Paginas/Mantic/Inventarios/Creditos/Reportes/notaCreditoDetalle", EFormatos.PDF, "nota_de_credito_detalle", "/Paginas/Mantic/Inventarios/Caja/Creditos/filtro");
  
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
