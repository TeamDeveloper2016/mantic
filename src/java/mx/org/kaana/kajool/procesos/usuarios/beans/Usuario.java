package mx.org.kaana.kajool.procesos.usuarios.beans;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 1/09/2015
 *@time 10:31:10 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import java.io.Serializable;
import java.util.Map;
import mx.org.kaana.kajool.enums.EFormatos;
import mx.org.kaana.kajool.enums.EReporte;
import mx.org.kaana.kajool.procesos.reportes.reglas.IReporte;

public class Usuario implements IReporte, Serializable {

	private static final long serialVersionUID=2645004033729508360L;
	private EReporte reporte;
	private Map<String, Object> params;

	public Usuario(EReporte reporte, Map<String, Object> params) {
		this.reporte=reporte;
		this.params=params;
	}

	@Override
	public String getJrxml() {
		return this.reporte.getJasper();
	}

	@Override
	public Map<String, Object> getParametros() {
		return this.params;
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
		return this.reporte.getAutomatico();
	}

	@Override
	public String getRegresar() {
		return this.reporte.getPaginaRegreso();
	}

	@Override
	public Boolean getComprimir() {
		return this.reporte.getComprimir();
	}
}
