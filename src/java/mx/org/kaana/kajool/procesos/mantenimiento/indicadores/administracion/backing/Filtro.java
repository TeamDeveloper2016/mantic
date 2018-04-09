package mx.org.kaana.kajool.procesos.mantenimiento.indicadores.administracion.backing;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date May 29, 2015
 *@time 11:06:00 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.IBaseFilter;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@ManagedBean(name="kajoolMantenimientoIndicadoresAdministracionFiltro")
@ViewScoped
public class Filtro extends IBaseFilter implements Serializable {

	private static final Log LOG = LogFactory.getLog(Filtro.class);
	private static final long serialVersionUID=3142747398495535480L;

@Override
@PostConstruct
	protected void init() {
    try {
			this.attrs.put("activo", 1);
			this.attrs.put("sortOrder", " order by tc_janal_grupos.clave");
      doLoad();
    } // try
    catch(Exception e) {
			JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
	} // init

	@Override
	public void doLoad() {
		List<Columna> columna= null;
		try {
			columna= new ArrayList<>();
			columna.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
			this.lazyModel= new FormatCustomLazy("VistaIndicadoresDirectivos", "indicadoresGrupos", this.attrs, columna);
		} // try
		catch (Exception e) {
			throw e;
		} // catch
	}
	
	public String doIndicador(){
		JsfBase.setFlashAttribute("idGrupo", ((Entity)this.attrs.get("seleccionado")).getKey());
		JsfBase.setFlashAttribute("nombreGrupo", ((Entity)this.attrs.get("seleccionado")).get("clave").toString());
		return "indicadores".concat(Constantes.REDIRECIONAR);
	}

}
