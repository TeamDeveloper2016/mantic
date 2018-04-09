package mx.org.kaana.kajool.procesos.reportes.beans;

import java.io.Serializable;
import java.util.Map;
import mx.org.kaana.kajool.procesos.reportes.beans.Modelo;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 14/11/2016
 *@time 11:22:37 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public class Definicion extends Modelo implements Serializable {

  private static final long serialVersionUID=-4176902962330581834L;
  private Map<String, Object> parametros;
  private String jrxml;
	private Long consecutivo;

  public Definicion(Map<String, Object> params, Map<String, Object> parametros, String proceso, String idXml, String jrxml) {
    super(params,proceso,idXml);
    this.parametros=parametros;
    this.jrxml=jrxml;
  }

	public Definicion(Map<String, Object> params, Map<String, Object> parametros, String proceso, String idXml, String jrxml, Long consecutivo) {
    super(params,proceso,idXml);
    this.parametros=parametros;
    this.jrxml=jrxml;
		this.consecutivo= consecutivo;
  }
	
  public Map<String, Object> getParametros() {
    return parametros;
  }

  public String getJrxml() {
    return jrxml;
  }
  
	@Override
  public Map<String, Object> getParams() {
    return super.getParams();
  }

	@Override
  public String getProceso() {
    return super.getProceso();
  }

	@Override
  public String getIdXml() {
    return super.getIdXml();
  }

	public Long getConsecutivo() {
		return consecutivo;
	}
  
}
