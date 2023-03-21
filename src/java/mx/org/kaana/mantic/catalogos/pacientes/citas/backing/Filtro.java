package mx.org.kaana.mantic.catalogos.pacientes.citas.backing;

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
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.facturama.reglas.CFDIGestor;
import mx.org.kaana.libs.facturama.reglas.TransaccionFactura;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.pagina.IBaseFilter;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.clientes.reglas.Transaccion;
import mx.org.kaana.mantic.catalogos.clientes.beans.RegistroCliente;
import mx.org.kaana.mantic.catalogos.masivos.enums.ECargaMasiva;
import mx.org.kaana.mantic.facturas.beans.ClienteFactura;

@Named(value = "manticCatalogosPacientesCitasFiltro")
@ViewScoped
public class Filtro extends IBaseFilter implements Serializable {

  private static final long serialVersionUID = 8793667741599428879L;

  @PostConstruct
  @Override
  protected void init() {
    try {
      this.attrs.put("sortOrder", "order by tc_mantic_clientes.razon_social, tc_mantic_clientes.paterno, tc_mantic_clientes.materno");
      this.attrs.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());     
      if(JsfBase.getFlashAttribute("idClienteProcess")!= null) {
        this.attrs.put("idClienteProcess", JsfBase.getFlashAttribute("idClienteProcess"));
        this.doLoad();
        this.attrs.put("idClienteProcess", null);
      } // if
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

  @Override
  public void doLoad() {
    List<Columna> columns    = new ArrayList<>();		
		Map<String, Object>params= new HashMap<>();
    try {
      columns.add(new Columna("razonSocial", EFormatoDinamicos.MAYUSCULAS));      
      columns.add(new Columna("motivo", EFormatoDinamicos.MAYUSCULAS));    
      columns.add(new Columna("estatus", EFormatoDinamicos.MAYUSCULAS));    
			params.put(Constantes.SQL_CONDICION, this.toPrepare());
			params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getDependencias());			
      this.lazyModel = new FormatCustomLazy("VistaClientesCitasDto", params, columns);
      UIBackingUtilities.resetDataTable();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    finally {
      Methods.clean(columns);
    } // finally		
  } // doLoad

	private String toPrepare() {
		String regresar             = null;
		StringBuilder condicion     = null;
		UISelectEntity cliente      = null;
		List<UISelectEntity>clientes= null;
		try {
			condicion= new StringBuilder("");
			cliente= (UISelectEntity)this.attrs.get("cliente");
			clientes= (List<UISelectEntity>)this.attrs.get("clientes");
      if(!Cadena.isVacio(this.attrs.get("idClienteProcess")) && !this.attrs.get("idClienteProcess").toString().equals("-1")) 
        condicion.append("tc_mantic_clientes.id_cliente=").append(this.attrs.get("idClienteProcess")).append(" and ");
			if(clientes!= null && cliente!= null && clientes.indexOf(cliente)>= 0) 
				condicion.append("concat(tc_mantic_clientes.razon_social, ' ', ifnull(tc_mantic_clientes.paterno, ''), ' ', ifnull(tc_mantic_clientes.materno, '')) regexp '.*").append(clientes.get(clientes.indexOf(cliente)).toString("razonSocial").replaceAll(Constantes.CLEAN_SQL, "").trim().replaceAll("(,| |\\t)+", ".*.*")).append(".*' and ");				
			else 
				if(!Cadena.isVacio(JsfBase.getParametro("razonSocial_input"))) 
					condicion.append("concat(tc_mantic_clientes.razon_social, ' ', ifnull(tc_mantic_clientes.paterno, ''), ' ', ifnull(tc_mantic_clientes.materno, '')) regexp '.*").append(JsfBase.getParametro("razonSocial_input").replaceAll(Constantes.CLEAN_SQL, "").trim().replaceAll("(,| |\\t)+", ".*.*")).append(".*' and ");				
			if(Cadena.isVacio(condicion))
				regresar= Constantes.SQL_VERDADERO;
			else
				regresar= condicion.substring(0, condicion.length() -4);
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toPrepare
	
  public String doAccion(String accion) {
    EAccion eaccion= null;
		try {
			eaccion= EAccion.valueOf(accion.toUpperCase());
			JsfBase.setFlashAttribute("puntoVenta", this.attrs.get("puntoVenta"));		
			JsfBase.setFlashAttribute("accion", eaccion);		
			JsfBase.setFlashAttribute("idCliente", (eaccion.equals(EAccion.MODIFICAR) || eaccion.equals(EAccion.CONSULTAR)) ? ((Entity)this.attrs.get("seleccionado")).getKey() : -1L);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch
		return "accion".concat(Constantes.REDIRECIONAR);
  } // doAccion

  public void doEliminar() {
		Transaccion transaccion = null;
		Entity seleccionado     = null;
		RegistroCliente registro= null;
		try {
			seleccionado= (Entity) this.attrs.get("seleccionado");			
			registro= new RegistroCliente();
			registro.setIdCliente(seleccionado.getKey());
			transaccion= new Transaccion(registro);
			if(transaccion.ejecutar(EAccion.ELIMINAR))
				JsfBase.addMessage("Eliminar cliente", "El cliente se ha eliminado correctamente.", ETipoMensaje.INFORMACION);
			else
				JsfBase.addMessage("Eliminar cliente", "Ocurrió un error al eliminar el cliente.", ETipoMensaje.ERROR);								
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch			
  } // doEliminar
	
	public String doPuntoVenta(){
		return "/Paginas/Mantic/Ventas/accion.jsf".concat(Constantes.REDIRECIONAR);
	} // doPuntoVenta
	
	public String doImportar() {
		JsfBase.setFlashAttribute("retorno", "/Paginas/Mantic/Catalogos/Clientes/filtro");		
		JsfBase.setFlashAttribute("idCliente",((Entity)this.attrs.get("seleccionado")).getKey());
		return "importar".concat(Constantes.REDIRECIONAR);
	}
	
	public List<UISelectEntity> doCompleteCliente(String codigo) {
 		List<Columna> columns     = new ArrayList<>();
    Map<String, Object> params= new HashMap<>();
    try {
      columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("rfc", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("razonSocial", EFormatoDinamicos.MAYUSCULAS));
  		params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
			if(!Cadena.isVacio(codigo)) {
  			codigo= codigo.replaceAll(Constantes.CLEAN_SQL, "").trim();
				codigo= codigo.toUpperCase().replaceAll("(,| |\\t)+", ".*.*");
			} // if	
			else
				codigo= "WXYZ";
  		params.put("codigo", codigo);
      this.attrs.put("clientes", UIEntity.build("VistaClientesCitasDto", "nombre", params, columns, 40L));
		} // try
	  catch (Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    }// finally
		return (List<UISelectEntity>)this.attrs.get("clientes");
	}	
	
	public void doPublicarFacturama() {
		TransaccionFactura transaccion= null;
		CFDIGestor gestor             = null;
		ClienteFactura cliente        = null;
		try {
			gestor= new CFDIGestor(((Entity)this.attrs.get("seleccionado")).getKey());
			cliente= gestor.toClienteFactura();			
			transaccion= new TransaccionFactura(cliente);
			if(transaccion.ejecutar(EAccion.ACTIVAR))
				JsfBase.addMessage("Registrar cliente en facturama", "Se registro de forma correcta");
			else
				JsfBase.addMessage("Registrar cliente en facturama", "Ocurrio un error al registrar el cliente en facturama");
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
	} // doPublicarFacturama
	
}
