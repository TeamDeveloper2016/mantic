package mx.org.kaana.kajool.procesos.plantillas.formularios.backing;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date Jul 3, 2014
 *@time 4:25:28 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import java.util.List;
import java.io.Serializable;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.IBaseFilter;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatLazyModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@ManagedBean(name="kajoolPlantillasFormulariosTabla")
@ViewScoped
public class Tabla extends IBaseFilter implements Serializable {

	private static final Log LOG = LogFactory.getLog(Tabla.class);
	private static final long serialVersionUID=-5239207897666578101L;

  @PostConstruct
	@Override
	protected void init() {
    try {
			LOG.debug(JsfBase.getFacesContext().getCurrentPhaseId());
			this.attrs.put("cadena", "");
      doLoad();
    } // try
    catch(Exception e) {
			JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
	} // init

	@Override
  public void doLoad() {
		List<Columna> columnas	= new ArrayList<Columna>();
    try {
			columnas.add(new Columna("cadena", EFormatoDinamicos.LIBRE));
			columnas.add(new Columna("entero", EFormatoDinamicos.NUMERO_SIN_DECIMALES));
			columnas.add(new Columna("flotante", EFormatoDinamicos.NUMERO_CON_DECIMALES));
			columnas.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA));
			this.attrs.put(Constantes.SQL_CONDICION, "cadena like '".concat(this.attrs.get("cadena").toString()).concat("%'"));
      this.lazyModel= new FormatLazyModel("TcJanalTiposDatosDto","row", this.attrs, columnas);
			UIBackingUtilities.resetDataTable();
    } // try
    catch(Exception e) {
			JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
		finally {
			Methods.clean(columnas);
		}//finally
  } // doLoad

	public String doEvento(String evento) {
		EAccion	accion	= null;
		try {
			accion	= EAccion.valueOf(evento.toUpperCase());
			switch (accion) {
				case MODIFICAR:
				case ELIMINAR:
					JsfBase.setFlashAttribute("seleccionado", ((Entity)this.attrs.get("selected")));
				break;
			} // switch
			JsfBase.setFlashAttribute("accion", accion);
    } // try
    catch(Exception e) {
			JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
		return "agregar";
	} // doEvento

}
