package mx.org.kaana.kajool.procesos.mantenimiento.indicadores.administracion.backing;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date Mar 12, 2015
 *@time 1:34:47 PM
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
import mx.org.kaana.kajool.procesos.mantenimiento.indicadores.administracion.reglas.Transaccion;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatLazyModel;
import mx.org.kaana.kajool.db.dto.TrJanalBitacorasAvancesDto;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.mozilla.javascript.edu.emory.mathcs.backport.java.util.Arrays;
import org.primefaces.model.DualListModel;

@ManagedBean(name="kajoolMantenimientoIndicadoresAdministracionIndicadores")
@ViewScoped
public class Indicadores extends IBaseFilter implements Serializable {
	
	private static final Log LOG = LogFactory.getLog(Indicadores.class);
	private static final long serialVersionUID=8070511579207957186L;
	private Entity seleccionado;
	private DualListModel<String> model;

	public Entity getSeleccionado() {
		return seleccionado;
	}

	public void setSeleccionado(Entity seleccionado) {
		this.seleccionado=seleccionado;
	}

	public DualListModel<String> getModel() {
		return model;
	}
	
	public void setModel(DualListModel<String> model) {
		this.model= model;
	}

  @Override
	@PostConstruct
	protected void init() {
    try {
			this.attrs.put("idGrupo", JsfBase.getFlashAttribute("idGrupo"));
			this.attrs.put("nombreGrupo", JsfBase.getFlashAttribute("nombreGrupo"));
			this.model= new DualListModel<>();
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
			this.lazyModel= new FormatLazyModel("TcJanalDefinicionesAvancesDto", this.attrs, campos);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
		finally{
			Methods.clean(campos);
		} // finally
	}
	
	public String doAgregar(){
		JsfBase.setFlashAttribute("accion", EAccion.AGREGAR);
		JsfBase.setFlashAttribute("idGrupo", this.attrs.get("idGrupo"));
		JsfBase.setFlashAttribute("nombreGrupo", this.attrs.get("nombreGrupo"));
		return "agregar".concat(Constantes.REDIRECIONAR);
	}
	
	public String doTablaTemporal(){
		JsfBase.setFlashAttribute("idKey", this.seleccionado.getKey());
		JsfBase.setFlashAttribute("nombreIndicador", this.seleccionado.get("descripcion"));
		JsfBase.setFlashAttribute("idGrupo", this.attrs.get("idGrupo"));
		return "filtroTemporal".concat(Constantes.REDIRECIONAR);
	}
	
	public String doModificar(){
		JsfBase.setFlashAttribute("accion", EAccion.MODIFICAR);
		JsfBase.setFlashAttribute("idKey", this.seleccionado.getKey());
		JsfBase.setFlashAttribute("idGrupo", this.attrs.get("idGrupo"));
		return "agregar".concat(Constantes.REDIRECIONAR);
	}
	
	public void doConsultar(){
		List<Entity> estatus= null;
		Entity campos= null;
		try {
			this.model.getSource().clear();
			this.model.getTarget().clear();
			this.attrs.put("idDefinicionAvance", this.seleccionado.getKey());
			estatus= DaoFactory.getInstance().toEntitySet("VistaIndicadoresDirectivos", "estatusAplican", this.attrs);
			for(Entity entity: estatus)
				this.model.getSource().add(entity.get("descripcion").toString().toUpperCase());
			campos= (Entity) DaoFactory.getInstance().toEntity("TcJanalDefinicionesAvancesDto", "definicionAvance", this.attrs);
			this.model.getTarget().addAll(Arrays.asList(campos.get("estatusAplican").toString().split(Constantes.SEPARADOR_SPLIT)));
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
		finally{
			Methods.clean(estatus);
		} // finally
	}
	
	public void doEliminar(){
		Map<String, Object> params= null;
		Transaccion transaccion		= null;
		try {
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, "id_definicion_avance= ".concat(this.seleccionado.getKey().toString()));
			if(DaoFactory.getInstance().size(TrJanalBitacorasAvancesDto.class, params)== 0){
				transaccion= new Transaccion(this.seleccionado.getKey());
				if(transaccion.ejecutar(EAccion.ELIMINAR))
					JsfBase.addMessage("Se eliminaron correctamente todos los registro del indicador.", ETipoMensaje.INFORMACION);
				else
					JsfBase.addMessage("Ocurrio un error al intentar eliminar los registro.", ETipoMensaje.ERROR);
			} // if
			else
				JsfBase.addMessage("Registros encontrador", "No se puede eliminar el indicador, el indicador tiene avances registrados.", ETipoMensaje.ERROR);
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
    return "filtro".concat(Constantes.REDIRECIONAR);
  } // doRegresar

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		Methods.clean(this.model);
	}

}
