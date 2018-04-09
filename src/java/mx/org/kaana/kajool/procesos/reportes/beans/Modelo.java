package mx.org.kaana.kajool.procesos.reportes.beans;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 12/10/2016
 *@time 02:46:37 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Modelo implements Serializable {
  
  private static final long serialVersionUID = 5858589754327208710L;
  private Map<String, Object> params;
  private String proceso;
  private String idXml;
	private String nombre;
  private List<Field> campos;

  public Modelo(Map<String, Object> params, String proceso, String idXml) {
		this(params, proceso, idXml, "");
	}
	
  public Modelo(Map<String, Object> params, String proceso, String idXml, String nombre) {
		this(params, proceso, idXml, "", Collections.EMPTY_LIST);
  }
  
  public Modelo(Map<String, Object> params, String proceso, String idXml, String nombre, List<Field> campos) {
    this.params = params;
    this.proceso= proceso;
    this.idXml  = idXml;
		this.nombre = nombre;		
    this.campos = campos;
  }

  public String getIdXml() {
    return idXml;
  }

  public Map<String, Object> getParams() {
    return params;
  }

  public String getProceso() {
    return proceso;
  }

  public void setIdXml(String idXml) {
    this.idXml= idXml;
  }

  public void setParams(Map<String, Object> params) {
    this.params= params;
  }

  public void setProceso(String proceso) {
    this.proceso= proceso;
  }

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre= nombre;
	}

  public List<Field> getCampos() {
    return campos;
  }

  public void setCampos(List<Field> campos) {
    this.campos = campos;
  }
  
}