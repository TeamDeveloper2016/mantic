package mx.org.kaana.kajool.procesos.tableros.beans;


import java.io.Serializable;
import java.util.Map;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date Mar 27, 2013
 *@time 3:32:29 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class GraficaConsulta implements Serializable {

  private static final long serialVersionUID=-7630104180935307626L;

  private String proceso;
  private String idXml;
  private Map params;

  public GraficaConsulta(String proceso, String idXml, Map params) {
    this.proceso=proceso;
    this.idXml=idXml;
    this.params=params;
  }

  public String getProceso() {
    return proceso;
  }

  public String getIdXml() {
    return idXml;
  }

  public Map getParams() {
    return params;
  }

}
