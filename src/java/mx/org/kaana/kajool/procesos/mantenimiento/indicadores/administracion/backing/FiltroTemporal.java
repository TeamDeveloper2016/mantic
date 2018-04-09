package mx.org.kaana.kajool.procesos.mantenimiento.indicadores.administracion.backing;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date Mar 25, 2015
 *@time 2:56:34 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.IBaseFilter;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatLazyModel;
import mx.org.kaana.kajool.db.dto.TrJanalBitacorasAvancesDto;
import mx.org.kaana.kajool.procesos.mantenimiento.indicadores.administracion.reglas.TransaccionTablaTemporal;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@ManagedBean(name="kajoolMantenimientoIndicadoresAdministracionFiltroTemporal")
@ViewScoped
public class FiltroTemporal extends IBaseFilter implements Serializable {

	private static final Log LOG = LogFactory.getLog(FiltroTemporal.class);
	private static final long serialVersionUID=6424821442050242798L;

@Override
@PostConstruct
	protected void init() {
    try {
			this.attrs.put("nombreIndicador", JsfBase.getFlashAttribute("nombreIndicador"));
			this.attrs.put("idDefinicionAvance", JsfBase.getFlashAttribute("idKey"));
			this.attrs.put("idGrupo", JsfBase.getFlashAttribute("idGrupo"));
      doLoad();
    } // try
    catch(Exception e) {
			JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
	} // init

	@Override
	public void doLoad() {
		List<Columna> campos= null;
		try {
			campos= new ArrayList<>();
			campos.add(new Columna("nombreTabla", EFormatoDinamicos.MAYUSCULAS));
			campos.add(new Columna("campos", EFormatoDinamicos.MAYUSCULAS));
			this.lazyModel= new FormatLazyModel("TcJanalTablasTempAvaDto", "definicionAvanceFormato", this.attrs, campos);
		} // try
		catch (Exception e) {
			throw e;
		} // catch
		finally{
			Methods.clean(campos);
		} // finally
	}
	
	public String doAgregar(){
		JsfBase.setFlashAttribute("accion", EAccion.AGREGAR);
		JsfBase.setFlashAttribute("idDefinicionAvance", this.attrs.get("idDefinicionAvance"));
		JsfBase.setFlashAttribute("idGrupo", this.attrs.get("idGrupo"));
		JsfBase.setFlashAttribute("nombreIndicador", this.attrs.get("nombreIndicador"));
		return "agregarTemporal".concat(Constantes.REDIRECIONAR);
	}
	
	public String doModificar(){
		JsfBase.setFlashAttribute("accion", EAccion.MODIFICAR);
		JsfBase.setFlashAttribute("idDefinicionAvance", this.attrs.get("idDefinicionAvance"));
		JsfBase.setFlashAttribute("idKey", ((Entity)this.attrs.get("seleccionado")).getKey());
		JsfBase.setFlashAttribute("idGrupo", this.attrs.get("idGrupo"));
		JsfBase.setFlashAttribute("nombreIndicador", this.attrs.get("nombreIndicador"));
		return "agregarTemporal".concat(Constantes.REDIRECIONAR);
	}
	
	public void doEliminar(){
		Map<String, Object> params= null;
		TransaccionTablaTemporal transaccion		= null;
		try {
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, "id_definicion_avance= ".concat(((Entity)this.attrs.get("seleccionado")).getKey().toString()));
			if(DaoFactory.getInstance().size(TrJanalBitacorasAvancesDto.class, params)== 0){
				transaccion= new TransaccionTablaTemporal(((Entity)this.attrs.get("seleccionado")).getKey());
				if(transaccion.ejecutar(EAccion.ELIMINAR))
					JsfBase.addMessage("El registro se eliminó correctamente.", ETipoMensaje.INFORMACION);
				else
					JsfBase.addMessage("Ocurrio un error al intentar eliminar los registro.", ETipoMensaje.ERROR);
			} // if
			else
				JsfBase.addMessage("Registros encontrador", "No se puede eliminar la tabla temporal, el indicador tiene avances registrados.", ETipoMensaje.ERROR);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
		finally{
			Methods.clean(params);
		} // finally
	}

	public String doRegresar() {
		JsfBase.setFlashAttribute("idGrupo", this.attrs.get("idGrupo"));
    return "indicadores".concat(Constantes.REDIRECIONAR);
  } // doRegresar

}
