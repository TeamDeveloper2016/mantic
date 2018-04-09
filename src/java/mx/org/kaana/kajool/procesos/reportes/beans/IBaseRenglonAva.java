package mx.org.kaana.kajool.procesos.reportes.beans;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 9/09/2015
 * @time 05:05:19 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public interface IBaseRenglonAva {

	public String toClaveNivel();
	public Long getNivel();
	public void setNivel(Long nivel);
	public String getOrClaveOperativa();
}
