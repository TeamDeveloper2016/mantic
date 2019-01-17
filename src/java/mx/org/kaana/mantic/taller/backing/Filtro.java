package mx.org.kaana.mantic.taller.backing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.procesos.comun.Comun;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UISelect;
import mx.org.kaana.libs.pagina.UISelectItem;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.db.dto.TcManticServiciosBitacoraDto;
import mx.org.kaana.mantic.enums.ETipoMovimiento;
import mx.org.kaana.mantic.taller.beans.RegistroServicio;
import mx.org.kaana.mantic.taller.reglas.Transaccion;

@Named(value = "manticTallerFiltro")
@ViewScoped
public class Filtro extends Comun implements Serializable {

  private static final long serialVersionUID = 8793667741599428879L;

  @PostConstruct
  @Override
  protected void init() {
    try {
			this.attrs.put("herramienta", "");
			this.attrs.put("cliente", "");
			this.attrs.put("consecutivo", "");
      this.attrs.put("sortOrder", "order by tc_mantic_servicios.registro desc");
      this.attrs.put("idServicio", JsfBase.getFlashAttribute("idServicio"));
      this.attrs.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
			this.loadEstatusServicios();
      if(this.attrs.get("idServicio")!= null) 
			  this.doLoad();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

  @Override
  public void doLoad() {
    List<Columna> campos = null;
    try {
      campos = new ArrayList<>();
      campos.add(new Columna("herramienta", EFormatoDinamicos.MAYUSCULAS));
      campos.add(new Columna("consecutivo", EFormatoDinamicos.MAYUSCULAS));
      campos.add(new Columna("marca", EFormatoDinamicos.MAYUSCULAS));
      campos.add(new Columna("modelo", EFormatoDinamicos.MAYUSCULAS));      
      campos.add(new Columna("fechaEstimada", EFormatoDinamicos.FECHA_CORTA));      
      campos.add(new Columna("registro", EFormatoDinamicos.FECHA_CORTA));      
      campos.add(new Columna("total", EFormatoDinamicos.NUMERO_CON_DECIMALES));     			
      this.lazyModel = new FormatCustomLazy("VistaTallerServiciosDto", "principal", this.attrs, campos);
      UIBackingUtilities.resetDataTable();
			this.attrs.put("idServicio", null);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    finally {
      Methods.clean(campos);
    } // finally		
  } // doLoad

  public String doAccion(String accion) {
    EAccion eaccion= null;
		try {
			eaccion= EAccion.valueOf(accion.toUpperCase());
			JsfBase.setFlashAttribute("accion", eaccion);		
			JsfBase.setFlashAttribute("idServicio", (eaccion.equals(EAccion.MODIFICAR)||eaccion.equals(EAccion.CONSULTAR)) ? ((Entity)this.attrs.get("seleccionado")).getKey() : -1L);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch
		return "accion".concat(Constantes.REDIRECIONAR);
  } // doAccion

  public void doEliminar() {
		Transaccion transaccion  = null;
		Entity seleccionado      = null;
		RegistroServicio registro= null;
    try {
			seleccionado= (Entity) this.attrs.get("seleccionado");			
			registro= new RegistroServicio();
			registro.setIdServicio(seleccionado.getKey());
			transaccion= new Transaccion(registro);
			if(transaccion.ejecutar(EAccion.ELIMINAR))
				JsfBase.addMessage("Eliminar servicio", "El servicio de taller se ha eliminado correctamente.", ETipoMensaje.INFORMACION);
			else
				JsfBase.addMessage("Eliminar servicio", "Ocurrió un error al eliminar el servicio de taller.", ETipoMensaje.ERROR);								
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // doEliminar
	
	private void loadEstatusServicios(){
		List<UISelectItem> allEstatus= null;
		Map<String, Object>params    = null;
		List<String> campos          = null;
		String all                   = ""; 
		try {
			params= new HashMap<>();
			campos= new ArrayList<>();
			params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
			campos.add("nombre");
			allEstatus= UISelect.build("TcManticServiciosEstatusDto", "row", params, campos, " ", EFormatoDinamicos.MAYUSCULAS);
			for(UISelectItem record: allEstatus)
				all= all.concat(record.getValue().toString()).concat(",");
			allEstatus.add(0, new UISelectItem(all.substring(0, all.length()-1), "TODOS"));
			this.attrs.put("allEstatus", allEstatus);
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		finally{
			Methods.clean(params);
			Methods.clean(campos);
		} // finally
	} // loadEstatusServicios
	
	public void doLoadEstatus(){
		Entity seleccionado          = null;
		Map<String, Object>params    = null;
		List<UISelectItem> allEstatus= null;
		try {
			seleccionado= (Entity)this.attrs.get("seleccionado");
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, "id_servicio_estatus in (".concat(seleccionado.toString("estatusAsociados")).concat(")"));
			allEstatus= UISelect.build("TcManticServiciosEstatusDto", params, "nombre", EFormatoDinamicos.MAYUSCULAS);			
			this.attrs.put("allEstatusAsigna", allEstatus);
			this.attrs.put("estatusAsigna", allEstatus.get(0));
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
		finally {
			Methods.clean(params);
		} // finally
	} // doLoadEstatus
	
	public void doActualizarEstatus(){
		Transaccion transaccion              = null;
		TcManticServiciosBitacoraDto bitacora= null;
		Entity seleccionado                  = null;
		try {
			seleccionado= (Entity)this.attrs.get("seleccionado");
			bitacora= new TcManticServiciosBitacoraDto();
			bitacora.setIdServicio(seleccionado.getKey());
			bitacora.setIdServicioEstatus(Long.valueOf(this.attrs.get("estatusAsigna").toString()));
			bitacora.setSeguimiento((String) this.attrs.get("justificacion"));
			bitacora.setIdUsuario(JsfBase.getIdUsuario());
			transaccion= new Transaccion(bitacora);
			if(transaccion.ejecutar(EAccion.JUSTIFICAR))
				JsfBase.addMessage("Cambio estatus", "Se realizo el cambio de estatus de forma correcta", ETipoMensaje.INFORMACION);
			else
				JsfBase.addMessage("Cambio estatus", "Ocurrio un error al realizar el cambio de estatus", ETipoMensaje.ERROR);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
		finally{
			this.attrs.put("justificacion", "");
		} // finally
	}	// doActualizaEstatus
	
	public String doMovimientos() {
		JsfBase.setFlashAttribute("tipo", ETipoMovimiento.SERVICIOS);
		JsfBase.setFlashAttribute(ETipoMovimiento.SERVICIOS.getIdKey(), ((Entity)this.attrs.get("seleccionado")).getKey());
		JsfBase.setFlashAttribute("regreso", "/Paginas/Mantic/Taller/filtro");
		return "/Paginas/Mantic/Compras/Ordenes/movimientos".concat(Constantes.REDIRECIONAR);
	}
	
	public String doRefacciones() {
  	JsfBase.setFlashAttribute("accion", EAccion.MODIFICAR);		
		JsfBase.setFlashAttribute(ETipoMovimiento.SERVICIOS.getIdKey(), ((Entity)this.attrs.get("seleccionado")).getKey());
		JsfBase.setFlashAttribute("regreso", "/Paginas/Mantic/Taller/filtro");
		return "/Paginas/Mantic/Taller/detalle".concat(Constantes.REDIRECIONAR);
	}
	
}
