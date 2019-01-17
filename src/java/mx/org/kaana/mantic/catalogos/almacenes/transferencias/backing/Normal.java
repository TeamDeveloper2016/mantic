package mx.org.kaana.mantic.catalogos.almacenes.transferencias.backing;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
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
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.formato.Global;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.almacenes.transferencias.beans.Transferencia;
import mx.org.kaana.mantic.catalogos.almacenes.transferencias.reglas.AdminTransferencias;
import mx.org.kaana.mantic.compras.ordenes.reglas.Transaccion;
import mx.org.kaana.mantic.compras.ordenes.beans.Articulo;
import mx.org.kaana.mantic.compras.ordenes.enums.EOrdenes;
import mx.org.kaana.mantic.comun.IBaseArticulos;
import mx.org.kaana.mantic.comun.IBaseStorage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.primefaces.context.RequestContext;
import org.primefaces.event.TabChangeEvent;


@Named(value= "manticCatalogosAlmacenesTransferenciasNormal")
@ViewScoped
public class Normal extends IBaseArticulos implements IBaseStorage, Serializable {

	private static final Log LOG              = LogFactory.getLog(Normal.class);
  private static final long serialVersionUID= 327393488565639367L;
	private EAccion accion;
	private EOrdenes tipoOrden;

	public String getTitulo() {
		return "(".concat(tipoOrden.name()).concat(")");
	}

	public EOrdenes getTipoOrden() {
		return tipoOrden;
	}
	
	public String getAgregar() {
		return this.accion.equals(EAccion.AGREGAR)? "none": "";
	}
	
	@PostConstruct
  @Override
  protected void init() {		
    try {
      this.accion   = JsfBase.getFlashAttribute("accion")== null? EAccion.AGREGAR: (EAccion)JsfBase.getFlashAttribute("accion");
			this.tipoOrden= EOrdenes.NORMAL;
      this.attrs.put("idTransferencia", JsfBase.getFlashAttribute("idTransferencia")== null? -1L: JsfBase.getFlashAttribute("idTransferencia"));
			this.attrs.put("retorno", JsfBase.getFlashAttribute("retorno")== null? "filtro": JsfBase.getFlashAttribute("retorno"));
      this.attrs.put("isPesos", false);
      this.attrs.put("cantidad", 0D);
			this.attrs.put("buscaPorCodigo", false);
			this.attrs.put("seleccionado", null);
			doLoad();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

	@Override
  public void doLoad() {
    try {
      this.attrs.put("nombreAccion", Cadena.letraCapital(this.accion.name()));
      switch (this.accion) {
        case AGREGAR:											
          this.setAdminOrden(new AdminTransferencias(new Transferencia(-1L, 2L)));
    			this.attrs.put("sinIva", false);
          break;
        case MODIFICAR:			
        case CONSULTAR:											
          this.setAdminOrden(new AdminTransferencias((Transferencia)DaoFactory.getInstance().toEntity(Transferencia.class, "TcManticTransferenciasDto", "detalle", this.attrs)));
    			this.attrs.put("sinIva", this.getAdminOrden().getIdSinIva().equals(1L));
          break;
      } // switch
			this.toLoadCatalog();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // doLoad

  public String doAceptar() {  
    Transaccion transaccion= null;
    String regresar        = null;
    try {			
			// transaccion = new Transaccion(((Transferencia)this.getAdminOrden().getOrden()), this.getAdminOrden().getArticulos());
			this.getAdminOrden().toAdjustArticulos();
			if (transaccion.ejecutar(this.accion)) {
				if(this.accion.equals(EAccion.AGREGAR)) {
 				  regresar = this.attrs.get("retorno").toString().concat(Constantes.REDIRECIONAR);
    			RequestContext.getCurrentInstance().execute("jsArticulos.back('gener\\u00F3 orden de compra', '"+ ((Transferencia)this.getAdminOrden().getOrden()).getConsecutivo()+ "');");
				} // if	
 				if(!this.accion.equals(EAccion.CONSULTAR)) 
    			JsfBase.addMessage("Se ".concat(this.accion.equals(EAccion.AGREGAR) ? "agregó" : "modificó").concat(" la transferencia de articulos."), ETipoMensaje.INFORMACION);
  			JsfBase.setFlashAttribute("idTransferencia", ((Transferencia)this.getAdminOrden().getOrden()).getIdTransferencia());
			} // if
			else 
				JsfBase.addMessage("Ocurrió un error al registrar la orden de compra.", ETipoMensaje.ALERTA);      			
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    return regresar;
  } // doAccion

  public String doCancelar() {   
  	JsfBase.setFlashAttribute("idTransferencia", ((Transferencia)this.getAdminOrden().getOrden()).getIdTransferencia());
    return (String)this.attrs.get("retorno");
  } // doCancelar

	private void toLoadCatalog() {
		List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
    try {
			columns= new ArrayList<>();
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
			if(JsfBase.getAutentifica().getEmpresa().isMatriz())
        params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresaDepende());
			else
				params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
			params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
      this.attrs.put("empresas", (List<UISelectEntity>) UIEntity.build("TcManticEmpresasDto", "empresas", params, columns));
 			List<UISelectEntity> empresas= (List<UISelectEntity>)this.attrs.get("empresas");
			if(!empresas.isEmpty()) {
				this.attrs.put("idPedidoSucursal", empresas.get(0));
				if(this.accion.equals(EAccion.AGREGAR))
  				((Transferencia)this.getAdminOrden().getOrden()).setIkEmpresa(empresas.get(0));
			  else
				  ((Transferencia)this.getAdminOrden().getOrden()).setIkEmpresa(empresas.get(empresas.indexOf(((Transferencia)this.getAdminOrden().getOrden()).getIkEmpresa())));
			} // if	
      this.attrs.put("almacenes", UIEntity.build("TcManticAlmacenesDto", "almacenes", params, columns));
 			List<UISelectEntity> almacenes= (List<UISelectEntity>)this.attrs.get("almacenes");
			if(!almacenes.isEmpty()) {
				List<UISelectEntity> destinos= (List<UISelectEntity>)((ArrayList<UISelectEntity>)almacenes).clone();
				if(this.accion.equals(EAccion.AGREGAR))
				  ((Transferencia)this.getAdminOrden().getOrden()).setIkAlmacen(almacenes.get(0));
				else 
				  ((Transferencia)this.getAdminOrden().getOrden()).setIkAlmacen(almacenes.get(almacenes.indexOf(((Transferencia)this.getAdminOrden().getOrden()).getIkAlmacen())));
        this.attrs.put("destinos", destinos);
  			int index = destinos.indexOf(((Transferencia)this.getAdminOrden().getOrden()).getIkAlmacen());
  			if(index>= 0)
	  			destinos.remove(index);
				if(this.accion.equals(EAccion.AGREGAR))
				  ((Transferencia)this.getAdminOrden().getOrden()).setIkDestino(destinos.get(0));
				else 
				  ((Transferencia)this.getAdminOrden().getOrden()).setIkDestino(destinos.get(destinos.indexOf(((Transferencia)this.getAdminOrden().getOrden()).getIkDestino())));
			} // if
			columns.clear();
      columns.add(new Columna("nombres", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("materno", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("paterno", EFormatoDinamicos.MAYUSCULAS));
      List<UISelectEntity> personas= UIEntity.build("VistaAlmacenesTransferenciasDto", "solicito", params, columns);
      this.attrs.put("personas", personas);
			if(personas!= null && !this.accion.equals(EAccion.AGREGAR)) 
				((Transferencia)this.getAdminOrden().getOrden()).setIkSolicito(personas.get(personas.indexOf(new UISelectEntity(((Transferencia)this.getAdminOrden().getOrden()).getIdSolicito()))));
    } // try
    catch (Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    } // finally
	}

	public void doUpdateAlmacen() {
    this.attrs.put("destinos", ((ArrayList<UISelectEntity>)this.attrs.get("almacenes")).clone());
		int index = ((List<UISelectEntity>)this.attrs.get("destinos")).indexOf(((Transferencia)this.getAdminOrden().getOrden()).getIkAlmacen());
		if(index>= 0)
      ((List<UISelectEntity>)this.attrs.get("destinos")).remove(index);
		this.getAdminOrden().getArticulos().clear();
		this.getAdminOrden().toCalculate();
	}

	public void doTabChange(TabChangeEvent event) {
		if(event.getTab().getTitle().equals("Articulos")) {
			if(this.attrs.get("articulos")== null) 
				this.toLoadArticulos("almacen");
		} // if	
		else 
			if(event.getTab().getTitle().equals("Faltantes") && this.attrs.get("faltantes")== null) 
        this.doLoadFaltantes();
			else 
			  if(event.getTab().getTitle().equals("Ventas perdidas") && this.attrs.get("perdidos")== null) 
           this.doLoadPerdidas();
	}
  
	public void toLoadArticulos(String idXml) {
		List<Articulo> articulos  = null;
    Map<String, Object> params= new HashMap<>();
		try {
			params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
			params.putAll(((Transferencia)this.getAdminOrden().getOrden()).toMap());
			articulos= (List<Articulo>)DaoFactory.getInstance().toEntitySet(Articulo.class, "VistaOrdenesComprasDto", idXml, params);
      if(articulos!= null && this.getAdminOrden().getArticulos().isEmpty())
				for (Articulo articulo : articulos) {
					this.getAdminOrden().add(articulo);
				} // for
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
    finally {
      Methods.clean(params);
    } // finally
	}

	public void doEliminarPerdido() {
		Transaccion transaccion= null;
		try {
			UISelectEntity perdido= (UISelectEntity)this.attrs.get("perdidoRemove");   		
			transaccion= new Transaccion(perdido.getKey());
			if(transaccion.ejecutar(EAccion.DEPURAR)){
				List<UISelectEntity> perdidos= (List<UISelectEntity>)this.attrs.get("perdidos");
				perdidos.remove(perdidos.indexOf(perdido));
				this.attrs.put("perdidos", perdidos);
			} // if
		} // try
	  catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
	}	
	
	public void doRecoverArticulo(Integer index) {
		try {
			if(index>= 0 && index< this.getAdminOrden().getArticulos().size()) {
				this.attrs.put("seleccionado", this.getAdminOrden().getArticulos().get(index).toEntity());
				Object backing= JsfBase.ELAsObject("manticCatalogosArticulosExpress");
				if(backing!= null)
					((IBaseAttribute)backing).getAttrs().put("seleccionado", this.attrs.get("seleccionado"));
			} // if	
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch				
	} // doRecoveryArticulo
	
	@Override
  public void doFindArticulo(Integer index) {
		super.doFindArticulo(index);
		List<UISelectEntity> articulos= (List<UISelectEntity>)this.attrs.get("articulos");
		int position= articulos.indexOf((UISelectEntity)this.attrs.get("articulo"));
		if(position>= 0)
      this.attrs.put("seleccionado", articulos.get(position));
	}

	@Override
	public void toSaveRecord() {
    Transaccion transaccion= null;
    try {			
			//transaccion = new Transaccion(((Transferencia)this.getAdminOrden().getOrden()), this.getAdminOrden().getArticulos());
			this.getAdminOrden().toAdjustArticulos();
			if (transaccion.ejecutar(EAccion.MOVIMIENTOS)) {
   			RequestContext.getCurrentInstance().execute("jsArticulos.back('guard\\u00F3 orden de compra', '"+ ((Transferencia)this.getAdminOrden().getOrden()).getConsecutivo()+ "');");
				this.accion= EAccion.MODIFICAR;
				this.getAdminOrden().getArticulos().add(new Articulo(-1L));
				this.attrs.put("autoSave", Global.format(EFormatoDinamicos.FECHA_HORA, Fecha.getRegistro()));
			} // if	
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
	}

	public void doGlobalEvent(Boolean isViewException) {
		LOG.error("ESTO ES UN MENSAJE GLOBAL INVOCADO POR UNA EXCEPCION QUE NO FUE ATRAPADA");
		if(isViewException && this.getAdminOrden().getArticulos().size()> 0)
		  this.toSaveRecord();
    //RequestContext.getCurrentInstance().execute("alert('ESTO ES UN MENSAJE GLOBAL INVOCADO POR UNA EXCEPCION QUE NO FUE ATRAPADA');");
	}
	
	public void doLoadAlmacenes() {
		List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
    try {
			columns= new ArrayList<>();
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
			params.put("sucursales", ((Transferencia)this.getAdminOrden().getOrden()).getIdEmpresa());
      this.attrs.put("almacenes", UIEntity.build("TcManticAlmacenesDto", "almacenes", params, columns));
 			List<UISelectEntity> almacenes= (List<UISelectEntity>)this.attrs.get("almacenes");
			if(!almacenes.isEmpty()) 
			  ((Transferencia)this.getAdminOrden().getOrden()).setIkAlmacen(almacenes.get(0));
   } // try
    catch (Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    } // finally
	}
	
}