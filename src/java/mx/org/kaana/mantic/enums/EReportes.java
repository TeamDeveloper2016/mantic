package mx.org.kaana.mantic.enums;

import mx.org.kaana.kajool.enums.EFormatos;
import mx.org.kaana.libs.reportes.IReportAttribute;

public enum EReportes implements IReportAttribute{

	ORDEN_COMPRA   ("VistaReportesOrdenesComprasDto", "reporteOrden", "Orden de compra", "/Paginas/Mantic/Compras/Ordenes/Reporte/orden", EFormatos.PDF, "orden_de_compra", "/Paginas/Mantic/Compras/filtro"),
	ORDENES_COMPRA ("VistaReportesOrdenesComprasDto", "ordenesCompra", "Ordenes de compra", "/Paginas/Mantic/Compras/Ordenes/Reporte/ordenesCompra", EFormatos.PDF, "ordenes_de_compra", "/Paginas/Mantic/Compras/filtro"),
	ORDEN_DETALLE  ("VistaReportesOrdenesComprasDto", "ordenDetalle", "Orden de compra detalle", "/Paginas/Mantic/Compras/Ordenes/Reporte/ordenDetalle", EFormatos.PDF, "orden_de_compra_detalle", "/Paginas/Mantic/Compras/filtro"),
	TICKET_VENTA   ("VistaTicketVentaDto", "ticket", "ticket", "/Paginas/Mantic/Ventas/Caja/Reportes/ticketVenta", EFormatos.PDF, "ticket_venta", "/Paginas/Mantic/Ventas/Caja/accion");
  
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
