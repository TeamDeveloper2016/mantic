package mx.org.kaana.mantic.catalogos.clientes.backing;

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
import mx.org.kaana.libs.pagina.UISelectItem;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.clientes.reglas.Transaccion;
import mx.org.kaana.mantic.catalogos.clientes.beans.RegistroCliente;
import mx.org.kaana.mantic.catalogos.masivos.enums.ECargaMasiva;
import mx.org.kaana.mantic.facturas.beans.ClienteFactura;

@Named(value = "manticCatalogosClientesFiltro")
@ViewScoped
public class Filtro extends IBaseFilter implements Serializable {

  private static final long serialVersionUID = 8793667741599428879L;

  @PostConstruct
  @Override
  protected void init() {
		Object puntoVenta= null;		
    try {
			puntoVenta= JsfBase.getFlashAttribute("puntoVenta");			
			this.attrs.put("puntoVenta", puntoVenta!= null);
      this.attrs.put("sortOrder", "order by tc_mantic_clientes.razon_social");
      this.attrs.put("idPrincipal", 1L);
      this.attrs.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());     
			this.loadCreditos();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

	private void loadCreditos() {
		List<UISelectItem>creditos= null;
		try {
			creditos= new ArrayList<>();
			creditos.add(new UISelectItem("1,2", "TODOS"));
			creditos.add(new UISelectItem("1", "SI"));
			creditos.add(new UISelectItem("2", "NO"));
			this.attrs.put("creditos", creditos);
			this.attrs.put("credito", UIBackingUtilities.toFirstKeySelectItem(creditos));
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
	} // loadCreditos
	
  @Override
  public void doLoad() {
    List<Columna> campos     = null;		
		Map<String, Object>params= null;
    try {
			params= new HashMap<>();
      campos= new ArrayList<>();
      campos.add(new Columna("razonSocial", EFormatoDinamicos.MAYUSCULAS));      
      campos.add(new Columna("rfc", EFormatoDinamicos.MAYUSCULAS));    
			params.put(Constantes.SQL_CONDICION, toCondicion());
			params.put("idPrincipal", 1L);
			params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getDependencias());			
			params.put("credito", this.attrs.get("credito"));			
      this.lazyModel = new FormatCustomLazy("VistaClientesDto", "row", params, campos);
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

	private String toCondicion() {
		String regresar             = null;
		StringBuilder condicion     = null;
		UISelectEntity cliente      = null;
		List<UISelectEntity>clientes= null;
		try {
			condicion= new StringBuilder("");
			cliente= (UISelectEntity)this.attrs.get("cliente");
			clientes= (List<UISelectEntity>)this.attrs.get("clientes");
			if(clientes!= null && cliente!= null && clientes.indexOf(cliente)>= 0) 
				condicion.append("tc_mantic_clientes.razon_social regexp '.*").append(clientes.get(clientes.indexOf(cliente)).toString("razonSocial").replaceAll(Constantes.CLEAN_SQL, "").trim().replaceAll("(,| |\\t)+", ".*.*")).append(".*' and ");				
			else 
				if(!Cadena.isVacio(JsfBase.getParametro("razonSocial_input"))) 
					condicion.append("tc_mantic_clientes.razon_social regexp '.*").append(JsfBase.getParametro("razonSocial_input").replaceAll(Constantes.CLEAN_SQL, "").trim().replaceAll("(,| |\\t)+", ".*.*")).append(".*' and ");				
			if(!Cadena.isVacio(this.attrs.get("rfc")))
				condicion.append("tc_mantic_clientes.rfc like '%").append(this.attrs.get("rfc")).append("%' and ");
			if(!Cadena.isVacio(this.attrs.get("clave")))
				condicion.append("tc_mantic_clientes.clave like '%").append(this.attrs.get("clave")).append("%' and ");
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
 		List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
		boolean buscaPorCodigo    = false;
    try {
			columns= new ArrayList<>();
      columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("rfc", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("razonSocial", EFormatoDinamicos.MAYUSCULAS));
  		params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
			if(!Cadena.isVacio(codigo)) {
  			codigo= new String(codigo).replaceAll(Constantes.CLEAN_SQL, "").trim();
				buscaPorCodigo= codigo.startsWith(".");
				if(buscaPorCodigo)
					codigo= codigo.trim().substring(1);
				codigo= codigo.toUpperCase().replaceAll("(,| |\\t)+", ".*.*");
			} // if	
			else
				codigo= "WXYZ";
  		params.put("codigo", codigo);
			if(buscaPorCodigo)
        this.attrs.put("clientes", UIEntity.build("TcManticClientesDto", "porCodigo", params, columns, 40L));
			else
        this.attrs.put("clientes", UIEntity.build("TcManticClientesDto", "porNombre", params, columns, 40L));
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
	
	public void doPublicarFacturama(){
		TransaccionFactura transaccion= null;
		CFDIGestor gestor             = null;
		ClienteFactura cliente        = null;
		try {
			gestor= new CFDIGestor(((Entity)this.attrs.get("seleccionado")).getKey());
			cliente= gestor.toClienteFactura();			
			transaccion= new TransaccionFactura(cliente);
			if(transaccion.ejecutar(EAccion.ACTIVAR))
				JsfBase.addMessage("Registrar cliente en facturama", "Se registro de forma correcta.");
			else
				JsfBase.addMessage("Registrar cliente en facturama", "Ocurrio un error al registrar el cliente en facturama.");			
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
	} // doPublicarFacturama

  public String doMasivo() {
    JsfBase.setFlashAttribute("retorno", "/Paginas/Mantic/Catalogos/Clientes/filtro");
    JsfBase.setFlashAttribute("idTipoMasivo", ECargaMasiva.CLIENTES.getId());
    return "/Paginas/Mantic/Catalogos/Masivos/importar".concat(Constantes.REDIRECIONAR);
	}
	
}
