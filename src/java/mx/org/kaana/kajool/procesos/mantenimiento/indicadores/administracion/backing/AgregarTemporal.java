package mx.org.kaana.kajool.procesos.mantenimiento.indicadores.administracion.backing;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date Mar 23, 2015
 *@time 9:58:25 AM
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
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.db.dto.TcJanalTablasTempAvaDto;
import mx.org.kaana.kajool.procesos.mantenimiento.indicadores.administracion.reglas.TransaccionTablaTemporal;

@ManagedBean(name="kajoolMantenimientoIndicadoresAdministracionAgregarTemporal")
@ViewScoped
public class AgregarTemporal extends IBaseAttribute implements Serializable {
	
	private static final long serialVersionUID=4069829877252308510L;
	private TcJanalTablasTempAvaDto dtoTablaTemporal;
	private EAccion accion;	
	private List<String> listaCampos;

	public TcJanalTablasTempAvaDto getDtoTablaTemporal() {
		return dtoTablaTemporal;
	}

	public List<String> getListaCampos() {
		return listaCampos;
	}
	

  @PostConstruct
	@Override
	protected void init() {
    try {
      this.accion = (EAccion) JsfBase.getFlashAttribute("accion");
			this.attrs.put("idDefinicionAvance", (Long)JsfBase.getFlashAttribute("idDefinicionAvance"));
			this.attrs.put("idGrupo", JsfBase.getFlashAttribute("idGrupo"));
			this.attrs.put("nombreIndicador", JsfBase.getFlashAttribute("nombreIndicador"));
			this.listaCampos= new ArrayList<>();
			switch(this.accion){
				case AGREGAR:
					this.dtoTablaTemporal= new TcJanalTablasTempAvaDto();
					this.dtoTablaTemporal.setOrden(1L);
					this.dtoTablaTemporal.setIdDefinicionAvance((Long) this.attrs.get("idDefinicionAvance"));					
					this.attrs.put("tablaPrincipal", true);
					camposTablaPrincipal(this.attrs);
				break;
				case MODIFICAR:
					this.dtoTablaTemporal= (TcJanalTablasTempAvaDto) DaoFactory.getInstance().findById(TcJanalTablasTempAvaDto.class, (Long) JsfBase.getFlashAttribute("idKey"));
					if(this.dtoTablaTemporal.getPrincipal().equals(1L)){
						camposTablaPrincipal(this.attrs);
						this.attrs.put("tablaPrincipal", true);
					} // if
					else{
						for(String item: this.dtoTablaTemporal.getCampos().split(Constantes.SEPARADOR_SPLIT))
							this.listaCampos.add(Cadena.reemplazarCaracter(item.split(Constantes.TILDE)[0], '_', ' '));
						this.attrs.put("tablaPrincipal", false);
					} // else
				break;
			} // switch
    } // try
    catch(Exception e) {
			JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
	}
	
	private void camposTablaPrincipal(Map<String, Object> params) throws Exception{
		Entity entity= null;
		try {
			entity= (Entity) DaoFactory.getInstance().toEntity("TcJanalDefinicionesAvancesDto", "definicionAvance", params);
			this.listaCampos.addAll(Arrays.asList(entity.toString("estatusAplican").split(Constantes.SEPARADOR_SPLIT)));
		} // try
		catch (Exception e) {
			throw e;
		} // catch
	}
	
	private String formatearCampos(){
		String regresar= "";
		if((Boolean) this.attrs.get("tablaPrincipal")){
			regresar= "ID_ORGANIZACION~NUMBER(11)|ID_ENTIDAD~NUMBER(11)|ID_MUNICIPIO~NUMBER(11)|ID_ORGANIZACION-GRUPO~NUMBER(11)|";
			for(String item: this.listaCampos){
				item= item.replaceAll(" ", "_").toUpperCase();
				regresar= regresar.concat(item).concat(Constantes.TILDE).concat("NUMBER(11)").concat(Constantes.SEPARADOR)
					.concat("PORCENTAJE_").concat(item).concat(Constantes.TILDE).concat("NUMBER(11,2)").concat(Constantes.SEPARADOR)
					.concat("ESTATUS_").concat(item).concat(Constantes.TILDE).concat("NUMBER(11)").concat(Constantes.SEPARADOR);
			} // for
		} // if
		else{
			for(String item: this.listaCampos){
				item= item.replaceAll(" ", "_").toUpperCase();
				regresar= regresar.concat(item).concat(Constantes.TILDE).concat("NUMBER(11)").concat(Constantes.SEPARADOR);
			} // for
		} // else
		regresar= regresar.substring(0, regresar.length()- 1);
		return regresar;
	}

	public String doAceptar() {
    TransaccionTablaTemporal transaccion= null;
		String regresar											= null;
		Map<String, Object> params					= null;
		try {
			if(!this.listaCampos.isEmpty()){
				params= new HashMap<>();
				params.put(Constantes.SQL_CONDICION, "id_definicion_avance="+ this.attrs.get("idDefinicionAvance").toString()+ " and principal= 1");
				if((Boolean) this.attrs.get("tablaPrincipal") && DaoFactory.getInstance().findFirst(TcJanalTablasTempAvaDto.class, params)!= null)
					JsfBase.addMessage("Ya existe una tabla principal registrada.", ETipoMensaje.ERROR);
				else{
					this.dtoTablaTemporal.setNombreTabla(this.dtoTablaTemporal.getNombreTabla().replaceAll(" ", "_").toUpperCase());
					this.dtoTablaTemporal.setCampos(formatearCampos());
					this.dtoTablaTemporal.setPrincipal((Boolean) this.attrs.get("tablaPrincipal")? 1L: 0L);
					transaccion= new TransaccionTablaTemporal(this.dtoTablaTemporal);
					if(transaccion.ejecutar(this.accion)){
						JsfBase.setFlashAttribute("idKey", this.attrs.get("idDefinicionAvance"));
						JsfBase.addMessage(this.accion== EAccion.AGREGAR?"El registro se agregó correctamente.": "El registro se modificó correctamente.", ETipoMensaje.INFORMACION);
						regresar= "filtroTemporal".concat(Constantes.REDIRECIONAR);
					} // if
					else
						JsfBase.addMessage("Ocurrio un error al intentar agregar el registro.", ETipoMensaje.ERROR);
				} // else
			} // if
			else
				JsfBase.addMessage("Tabla vacia", "La tabla no puede estar vacia.", ETipoMensaje.ERROR);
			JsfBase.setFlashAttribute("idGrupo", this.attrs.get("idGrupo"));
			JsfBase.setFlashAttribute("nombreIndicador", this.attrs.get("nombreIndicador"));
    } // try
    catch(Exception e) {
			JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
		finally{
			Methods.clean(params);
		} // finally
		return regresar;
	}

	public String doRegresar(){
		JsfBase.setFlashAttribute("idKey", this.attrs.get("idDefinicionAvance"));
		JsfBase.setFlashAttribute("idGrupo", this.attrs.get("idGrupo"));
		JsfBase.setFlashAttribute("nombreIndicador", this.attrs.get("nombreIndicador"));
		return "filtroTemporal".concat(Constantes.REDIRECIONAR);
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		Methods.clean(this.listaCampos);
	}

}
