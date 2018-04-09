
package mx.org.kaana.libs.reportes;

import mx.org.kaana.kajool.enums.EFormatos;

/**
 *@company Instituto Nacional de Estadistica y Geografia
 *@project KAJOOL (Control system polls)
 *@date 9/05/2014
 *@time 11:10:47 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public interface IReportAttribute {

  public String getIdentificador();
  public String getProceso();
  public String getIdXml();
  public String getJrxml();
  public EFormatos getFormato();
  public String getNombre();
  public String getTitulo();
  public String getRegresar();

}
