package mx.org.kaana.kajool.procesos.reportes.beans;

import java.io.Serializable;
import java.util.Map;
import mx.org.kaana.kajool.procesos.reportes.reglas.IExportacionXls;
import mx.org.kaana.kajool.procesos.reportes.reglas.IXls;

public class ExportarXls implements IXls, Serializable {

	private static final long serialVersionUID=-4097634443518939844L;
  private Modelo modelo;
  private IExportacionXls exportacionXls;
  private String campos;
	private Map<String, Object> parametros; 

  public ExportarXls(Modelo modelo, IExportacionXls exportacionXls) {
    this(modelo, exportacionXls, null);
  }
  
  public ExportarXls(Modelo modelo, IExportacionXls exportacionXls, String campos) {
    this.modelo = modelo;
    this.exportacionXls = exportacionXls;
    this.campos= campos;
  }
	
  public ExportarXls(Modelo modelo, IExportacionXls exportacionXls, String campos, Map<String, Object> parametrosRegreso) {
    this.modelo = modelo;
    this.exportacionXls = exportacionXls;
    this.campos= campos;
		this.parametros= parametrosRegreso;
  }

  @Override
  public Modelo getModelo() {
    return this.modelo;
  }
  
  @Override
  public String getNombre() {
    return this.exportacionXls.getNombreArchivo();
  }

  @Override
  public String getRegresar() {
    return this.exportacionXls.getPaginaRegreso();
  }

  @Override
  public Boolean getComprimir() {
    return Boolean.TRUE;
  }

  @Override
  public String getPatron() {
    return this.exportacionXls.getPatron();
  }

  @Override
  public String getCampos() {
    return this.campos;
  }

	@Override
	public Map<String, Object> getParametros() {
		return this.parametros;
	}

	@Override
	public void setParametros(Map<String, Object> parametros) {
		this.parametros= parametros;
	}

}
