package mx.org.kaana.kajool.procesos.reportes.reglas;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 12/10/2016
 *@time 02:46:37 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public interface IBaseExporter {
  
  public String getProceso();
	public String[] getIdXml();
	public String getDescripcion();
	public String[] getDbfFile();
	public String getPaginaRegreso();
	public String getArchivoZip();
	public String getPatron();

}
