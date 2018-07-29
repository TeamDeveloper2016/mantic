package mx.org.kaana.mantic.catalogos.almacenes.transferencias.backing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
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
import mx.org.kaana.mantic.catalogos.almacenes.transferencias.reglas.Transaccion;
import mx.org.kaana.mantic.db.dto.TcManticAlmacenesArticulosDto;
import mx.org.kaana.mantic.db.dto.TcManticTransferenciasBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticTransferenciasDto;
import mx.org.kaana.mantic.taller.beans.RegistroServicio;

@Named(value = "manticCatalogosAlmacenesTransferenciasFiltro")
@ViewScoped
public class Filtro extends Comun implements Serializable {

  private static final long serialVersionUID = 8793667741599428879L;

  @PostConstruct
  @Override
  protected void init() {
    try {
			this.attrs.put("clave", "");
			this.attrs.put("nombre", "");
      this.attrs.put("codigoArticulo", "");
			this.attrs.put("nombreArticulo", "");
      this.attrs.put("sortOrder", "order by tc_mantic_transferencias.registro");
      this.attrs.put("idPrincipal", 1L);
      this.attrs.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
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
      campos.add(new Columna("nombreOrigen", EFormatoDinamicos.MAYUSCULAS));
      campos.add(new Columna("nombreDestino", EFormatoDinamicos.MAYUSCULAS));
      campos.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      campos.add(new Columna("cantidad", EFormatoDinamicos.NUMERO_CON_DECIMALES));
      campos.add(new Columna("observaciones", EFormatoDinamicos.MAYUSCULAS));
      campos.add(new Columna("estatus", EFormatoDinamicos.MAYUSCULAS));
      campos.add(new Columna("registro", EFormatoDinamicos.FECHA_CORTA));
      campos.add(new Columna("solicito", EFormatoDinamicos.MAYUSCULAS));
      this.lazyModel = new FormatCustomLazy("VistaAlmacenesTransferenciasDto", "row", this.attrs, campos);
      UIBackingUtilities.resetDataTable();
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
			JsfBase.setFlashAttribute("idTransferencia", (eaccion.equals(EAccion.MODIFICAR)||eaccion.equals(EAccion.CONSULTAR)) ? ((Entity)this.attrs.get("seleccionado")).getKey() : -1L);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch
		return "accion".concat(Constantes.REDIRECIONAR);
  } // doAccion
  
  public void doLoadEstatus(){
		Entity seleccionado          = null;
		Map<String, Object>params    = null;
		List<UISelectItem> allEstatus= null;
		try {
			seleccionado= (Entity)this.attrs.get("seleccionado");
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, "id_transferencias_estatus in (".concat(seleccionado.toString("estatusAsociados")).concat(")"));
			allEstatus= UISelect.build("TcManticTransferenciasEstatusDto", params, "nombre", EFormatoDinamicos.MAYUSCULAS);			
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
		Transaccion transaccion                   = null;
		TcManticTransferenciasBitacoraDto bitacora= null;
		Entity seleccionado                       = null;
    TcManticAlmacenesArticulosDto origen      = null;
    Map<String, Object>params                 = null;
    Double nuevoStock                         = 0D;
		try {
      params= new HashMap<>();
			seleccionado= (Entity)this.attrs.get("seleccionado");
			bitacora= new TcManticTransferenciasBitacoraDto();
			bitacora.setIdTransferencia(seleccionado.getKey());
			bitacora.setIdTransferenciaEstatus(Long.valueOf(this.attrs.get("estatusAsigna").toString()));
			bitacora.setJustificacion((String) this.attrs.get("justificacion"));
			bitacora.setIdUsuario(JsfBase.getIdUsuario());
			transaccion= new Transaccion(bitacora);
      if(Long.valueOf(this.attrs.get("estatusAsigna").toString()).equals(5L)){//estatus entregado
          params.put("idAlmacen", seleccionado.get("idAlmacen").toLong());
          params.put("idArticulo", seleccionado.get("idArticulo").toLong());
          origen = (TcManticAlmacenesArticulosDto) DaoFactory.getInstance().findFirst(TcManticAlmacenesArticulosDto.class, "almacenArticulo", params);
          nuevoStock = origen.getStock()- Double.valueOf(seleccionado.get("cantidad").toString());
          origen.setStock(nuevoStock);
          transaccion.setAlmanecArticuloOrigen(origen);
          transaccion.setIdAlmacenDestino(seleccionado.get("idDestino").toLong());
          transaccion.setCantidad(seleccionado.get("cantidad").toString());
      }
			if(transaccion.ejecutar(EAccion.REGISTRAR)){
				JsfBase.addMessage("Cambio estatus", "Se realizo el cambio de estatus de forma correcta", ETipoMensaje.INFORMACION);
      }
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

  public void doEliminar() {
		Transaccion transaccion           = null;
		Entity seleccionado               = null;
		TcManticTransferenciasDto registro= null;
    try {
			seleccionado= (Entity) this.attrs.get("seleccionado");
      registro = (TcManticTransferenciasDto) DaoFactory.getInstance().findById(TcManticTransferenciasDto.class, seleccionado.getKey());
			transaccion= new Transaccion(registro, "");
			if(transaccion.ejecutar(EAccion.ELIMINAR))
				JsfBase.addMessage("Eliminar transferencia", "La transferencia de artículos se ha eliminado correctamente.", ETipoMensaje.INFORMACION);
			else
				JsfBase.addMessage("Eliminar transferencia", "Ocurrió un error al eliminar la transferencia de artículos.", ETipoMensaje.ERROR);								
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // doEliminar
  
  public void doConfirmaEntrega() {
		Transaccion transaccion              = null;
		Entity seleccionado                  = null;
		TcManticAlmacenesArticulosDto origen = null;
    Map<String, Object>params            = null;
    Double nuevoStock                    = 0D;
    try {
      params= new HashMap<>();
			seleccionado= (Entity) this.attrs.get("seleccionado");
      params.put("idAlmacen", seleccionado.get("idAlmacen").toLong());
      params.put("idArticulo", seleccionado.get("idArticulo").toLong());
      origen = (TcManticAlmacenesArticulosDto) DaoFactory.getInstance().findViewCriteria(TcManticAlmacenesArticulosDto.class, params, "almacenArticulo");
      nuevoStock = origen.getStock()- Double.valueOf(seleccionado.get("cantidad").toString());
      origen.setStock(nuevoStock);
      transaccion= new Transaccion(origen, seleccionado.get("cantidad").toString(), seleccionado.get("idDestino").toLong());
			if(transaccion.ejecutar(EAccion.COMPLEMENTAR))
				JsfBase.addMessage("Complementar entrega transferencia", "La transferencia de artículos se ha concretado correctamente.", ETipoMensaje.INFORMACION);
			else
				JsfBase.addMessage("Complementar entrega transferencia", "Ocurrió un error al concretar la transferencia de artículos.", ETipoMensaje.ERROR);								
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
    	finally {
			Methods.clean(params);
		} // finally
  } // doConfirmaEntrega
  
}
