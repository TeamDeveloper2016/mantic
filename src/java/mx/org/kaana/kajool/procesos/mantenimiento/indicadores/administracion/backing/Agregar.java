package mx.org.kaana.kajool.procesos.mantenimiento.indicadores.administracion.backing;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date Mar 12, 2015
 *@time 3:23:37 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UISelect;
import mx.org.kaana.libs.pagina.UISelectItem;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.beans.SelectionItem;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.kajool.procesos.mantenimiento.indicadores.administracion.reglas.Transaccion;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.db.dto.TcJanalDefinicionesAvancesDto;
import mx.org.kaana.kajool.procesos.mantenimiento.indicadores.administracion.beans.BeanIndicadorAvance;

import org.primefaces.event.TransferEvent;


@ManagedBean(name="kajoolMantenimientoIndicadoresAdministracionAgregar")
@ViewScoped
public class Agregar implements Serializable {
	
	private static final long serialVersionUID=368648697594613689L;
  private TcJanalDefinicionesAvancesDto dto;
	private EAccion accion;
	private BeanIndicadorAvance beanIndicador;
	private Long idGrupo;
	private String nombreGrupo;

	public TcJanalDefinicionesAvancesDto getDto() {
		return dto;
	}

	public void setDto(TcJanalDefinicionesAvancesDto dto) {
		this.dto=dto;
	}

	public BeanIndicadorAvance getBeanIndicador() {
		return beanIndicador;
	}

  @PostConstruct
	private void init() {
		Map<String, Object> params= null;
		List<Entity> listaPerfiles= null;
		String[] estatusAplican		= null;
		Integer i									= 0;
    try {
      this.accion = (EAccion) JsfBase.getFlashAttribute("accion");
			this.idGrupo= (Long) JsfBase.getFlashAttribute("idGrupo");
			this.nombreGrupo= (String) JsfBase.getFlashAttribute("nombreGrupo");
			this.beanIndicador= new BeanIndicadorAvance();
			switch(this.accion){
				case AGREGAR:
					this.beanIndicador.setDisableOrden(true);
					load();
				break;
				case MODIFICAR:
					params= new HashMap<>();
					this.dto= (TcJanalDefinicionesAvancesDto) DaoFactory.getInstance().findById(TcJanalDefinicionesAvancesDto.class, (Long)JsfBase.getFlashAttribute("idKey"));
					this.beanIndicador.setDisableOrden(false);
					cargarEstatusIndicadores();
					cargarPerfiles();
					estatusAplican= this.dto.getEstatusAplican().split(Constantes.SEPARADOR_SPLIT);
					params.put(Constantes.SQL_CONDICION, "id_definicion_avance= ".concat(this.dto.getIdDefinicionAvance().toString()));
					params.put("idDefinicionAvance", this.dto.getIdDefinicionAvance());
					listaPerfiles= DaoFactory.getInstance().toEntitySet("TrJanalPerfilesAvancesDto", params);
					this.beanIndicador.setPerfilesSeleccionados(new String[listaPerfiles.size()]);
					for(Entity entity: listaPerfiles){
						this.beanIndicador.getPerfilesSeleccionados()[i]= entity.get("idPerfil").toString();
						i++;
					} // for
					i= 0;
					for(Entity entity: (List<Entity>) DaoFactory.getInstance().toEntitySet("VistaIndicadoresDirectivos", "estatusAplican", params)){
						this.beanIndicador.getModel().getTarget().add(new SelectionItem(entity.get("idEstatusAvance").toString(), entity.get("descripcion").toString()));
						this.beanIndicador.getTargetTemporal().add(new SelectionItem(entity.get("idEstatusAvance").toString(), estatusAplican[i]));
						this.beanIndicador.getModel().getSource().remove(new SelectionItem(entity.get("idEstatusAvance").toString(), null));
						i++;
					} // for
					for(SelectionItem item: this.beanIndicador.getModel().getTarget()){
						if(item.getItem().equals(this.dto.getEstatusIndicador())){
							this.beanIndicador.setIdEstatusIndicador(Numero.getLong(item.getKey()));
							break;
						} // if
					} // for
				break;
			} // switch
    } // try
    catch(Exception e) {
			JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
		finally{
			Methods.clean(params);
			Methods.clean(listaPerfiles);
		} // finally
	}
	
	private void load() throws Exception{
		Map<String, Object> params= null;
		Value orden								= null;
		List<SelectionItem> lista	= null;
		try {
			params= new HashMap<>();
			params.put("idGrupo", this.idGrupo);
			this.dto= new TcJanalDefinicionesAvancesDto();
			this.dto.setCampo("ID_ESTATUS_AVANCE");
			this.dto.setTabla("TC_ESTATUS_AVANCES");
			this.dto.setIdGrupo(this.idGrupo);
			orden= DaoFactory.getInstance().toField("TcJanalDefinicionesAvancesDto", "ordenMaximo", params, "orden");
			this.dto.setOrden(orden.getData()== null? 1L: orden.toLong()+ 1L);
			cargarEstatusIndicadores();
			cargarPerfiles();
		} // try
		catch (Exception e) {
      throw e;
		} // catch
		finally{
			Methods.clean(params);
			Methods.clean(lista);
		} // finally
	}
	
	private void cargarEstatusIndicadores(){
		List<SelectionItem> lista = null;
		try {
			lista= new ArrayList<>();
			this.beanIndicador.setEstatusIndicadores(UISelect.build("TcJanalEstatusAvancesDto", "row", "descripcion"));
			for(UISelectItem item: this.beanIndicador.getEstatusIndicadores())
				lista.add(new SelectionItem(item.getValue().toString(), item.getLabel()));
			this.beanIndicador.getModel().getSource().addAll(lista);
			this.beanIndicador.getSourceTemporal().addAll(lista);
		} // try
		catch (Exception e) {
			throw e;
		} // catch
		finally{
			Methods.clean(lista);
		} // finally
	}
	
	private void cargarPerfiles(){
		Map<String, Object> params= null;
		try {
			params= new HashMap<>();
			params.put("idGrupo", this.idGrupo);
			this.beanIndicador.setPerfiles(UISelect.build("VistaGruposPrivilegiosPerfilesDto", "perfilesGrupo", params, "descripcion"));
		} // try
		catch (Exception e) {
			throw e;
		} // catch
		finally{
			Methods.clean(params);
		} // finally
	}

	public String doAceptar() {
		Transaccion transaccion		= null;
		String regresar						= null;
		Map<String, Object> params= null;
    try {
			System.out.println("Perfiles: "+ Arrays.toString(this.beanIndicador.getPerfilesSeleccionados()));
			if(!this.beanIndicador.getTargetTemporal().isEmpty()){
				params= new HashMap<>();
				params.put("idGrupo", this.idGrupo);
				params.put("idDefinicionAvance", this.dto.getIdDefinicionAvance());
				params.put("orden", this.dto.getOrden());
				if(DaoFactory.getInstance().findFirst(TcJanalDefinicionesAvancesDto.class, "orden", params)== null){
					for(UISelectItem item: this.beanIndicador.getEstatusIndicadores())
						if(((Long)item.getValue()).equals(this.beanIndicador.getIdEstatusIndicador())){
							this.dto.setEstatusIndicador(item.getLabel());
							break;
						} // if
					transaccion= new Transaccion(this.dto, this.beanIndicador.getTargetTemporal(), this.beanIndicador.getPerfilesSeleccionados());
					if(transaccion.ejecutar(this.accion)){
						JsfBase.setFlashAttribute("idGrupo", this.idGrupo);
						regresar= "indicadores".concat(Constantes.REDIRECIONAR);
						JsfBase.addMessage(this.accion== EAccion.AGREGAR?"El registro se agregó correctamente": "El registro se modificó correctamente.", ETipoMensaje.INFORMACION);
					} // if
				} // if
				else
					JsfBase.addMessage("Ya existe un indicador de avance con el mismo orden", ETipoMensaje.ERROR);
			} // if
			else
				JsfBase.addMessage("Tabla vacia", "La tabla no debe estar vacia", ETipoMensaje.ERROR);
    } // try
    catch(Exception e) {
			JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
		return regresar;
	}
	
	public void onTransfer(TransferEvent event) {				
		List<SelectionItem>transfers= (List<SelectionItem>) event.getItems();
		try {
			if(!transfers.isEmpty()){
				for(SelectionItem item: transfers){
					if(this.beanIndicador.getTargetTemporal().contains(item))
						this.beanIndicador.getTargetTemporal().remove(item);
					else
						this.beanIndicador.getTargetTemporal().add(this.beanIndicador.getSourceTemporal().get(this.beanIndicador.getSourceTemporal().indexOf(item)));
				} // for
			} // if
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
		finally{
			Methods.clean(transfers);
		} // finally
	}

	public String doRegresar(){
		JsfBase.setFlashAttribute("idGrupo", this.idGrupo);
		JsfBase.setFlashAttribute("nombreGrupo", this.nombreGrupo);
		return "indicadores".concat(Constantes.REDIRECIONAR);
	}
	

}
