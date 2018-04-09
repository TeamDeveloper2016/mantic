package mx.org.kaana.libs.pagina;

import java.io.Serializable;
import mx.org.kaana.kajool.reglas.comun.FormatLazyModel;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 17-feb-2014
 * @time 16:19:07
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public abstract class IBaseFilter extends IBaseAttribute implements Serializable {

	private static final long serialVersionUID=-5324353121363296376L;
	protected FormatLazyModel lazyModel;
	
/**
 * Obtiene el Objeto lazyModel de tipo FormatLazyModel
 * @return
 */	
	public FormatLazyModel getLazyModel() {
		return lazyModel;
	}
	/**
   * Metodo abstracto para implementar para la carga de la consulta
   */
	public abstract void doLoad();	
	
}
