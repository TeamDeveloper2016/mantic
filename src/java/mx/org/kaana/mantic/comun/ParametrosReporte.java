package mx.org.kaana.mantic.comun;

import java.io.Serializable;
import java.util.Map;
import mx.org.kaana.kajool.enums.EFormatos;
import mx.org.kaana.kajool.procesos.reportes.reglas.IReporte;
import mx.org.kaana.libs.reportes.IReportAttribute;
import mx.org.kaana.mantic.enums.EReportes;

public class ParametrosReporte implements Serializable, IReporte {

	private static final long serialVersionUID=631768500919598939L;

	private EReportes reporte;
	private Map<String, Object> params;
	private Map<String, Object> parametros;

	public ParametrosReporte(EReportes reporte, Map<String, Object> params, Map<String, Object> parametros) {
		this.reporte   = reporte;
		this.params    = params;
		this.parametros= parametros;
	}
	
	public IReportAttribute getEnum() {
		return this.reporte;
	}  
  
	@Override
	public String getJrxml() {
		return this.reporte.getJrxml();
	}

	@Override
	public Map<String, Object> getParametros() {
		return this.parametros;
	}

	@Override
	public String getProceso() {
		return this.reporte.getProceso();
	}

	@Override
	public String getIdXml() {
		return this.reporte.getIdXml();
	}

	@Override
	public Map<String, Object> getParams() {
		return this.params;
	}

	@Override
	public String getNombre() {
		return this.reporte.getNombre();
	}

	@Override
	public EFormatos getFormato() {
		return this.reporte.getFormato();
	}

	@Override
	public Long getTitulo() {
		return 0L;
	}

	@Override
	public Boolean getAutomatico() {
		return Boolean.TRUE;
	}

	@Override
	public String getRegresar() {
		return this.reporte.getRegresar();
	}

	@Override
	public Boolean getComprimir() {
		return Boolean.TRUE;
	}
    
  public void setParams(Map<String, Object> params) {
    this.params= params;
  }	  
}
