package mx.org.kaana.mantic.enums;

import mx.org.kaana.kajool.procesos.reportes.reglas.IExportacionXls;

public enum EExportacionXls implements IExportacionXls{

	ARTICULOS ("VistaArticulosDto", "exportar", "Articulos", "/Paginas/Mantic/Catalogos/Articulos/filtro", ""),
	CONTEOS ("VistaArticulosDto", "conteo", "Conteos", "/Paginas/Mantic/Inventarios/Almacenes/filtro", "");
  
  private final String proceso;
  private final String idXml;
  private final String nombreArchivo;
  private final String paginaRegreso;
  private final String patron;

  private EExportacionXls(String proceso, String idXml, String nombreArchivo, String paginaRegreso, String patron) {
    this.proceso      = proceso;
    this.idXml        = idXml;
    this.nombreArchivo= nombreArchivo;
    this.paginaRegreso= paginaRegreso;
    this.patron       = patron;
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
  public String getNombreArchivo() {
    return nombreArchivo;
  }

  @Override
  public String getPaginaRegreso() {
    return paginaRegreso;
  }

	@Override
  public String getPatron() {
    return patron;
  }	
}
