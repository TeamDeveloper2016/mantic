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
import mx.org.kaana.kajool.db.comun.sql.Value;
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
import mx.org.kaana.mantic.catalogos.almacenes.transferencias.reglas.Transaccion;
import mx.org.kaana.mantic.compras.ordenes.beans.Articulo;
import mx.org.kaana.mantic.compras.ordenes.enums.EOrdenes;
import mx.org.kaana.mantic.comun.IBaseArticulos;
import mx.org.kaana.mantic.comun.IBaseStorage;
import mx.org.kaana.mantic.db.dto.TcManticTransferenciasDto;
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
			transaccion = new Transaccion((TcManticTransferenciasDto)this.getAdminOrden().getOrden(), this.getAdminOrden().getArticulos());
			this.getAdminOrden().toAdjustArticulos();
			if (transaccion.ejecutar(this.accion)) {
				if(this.accion.equals(EAccion.AGREGAR)) {
   			  RequestContext.getCurrentInstance().execute("janal.back(' gener\\u00F3 la transferencia ', '"+ ((Transferencia)this.getAdminOrden().getOrden()).getConsecutivo()+ "');");
		  		JsfBase.addMessage("Se registró la transferencia de correcta", ETipoMensaje.INFORMACION);
 				  regresar = ((String)this.attrs.get("retorno")).concat(Constantes.REDIRECIONAR);
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
				if(this.accion.equals(EAccion.AGREGAR))
  				((Transferencia)this.getAdminOrden().getOrden()).setIkEmpresa(empresas.get(0));
			  else
				  ((Transferencia)this.getAdminOrden().getOrden()).setIkEmpresa(empresas.get(empresas.indexOf(((Transferencia)this.getAdminOrden().getOrden()).getIkEmpresa())));
				this.attrs.put("idPedidoSucursal", ((Transferencia)this.getAdminOrden().getOrden()).getIkEmpresa());
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
			if(personas!= null && !this.accion.equals(EAccion.AGREGAR) && ((Transferencia)this.getAdminOrden().getOrden()).getIdSolicito()!= null && ((Transferencia)this.getAdminOrden().getOrden()).getIdSolicito()> 0L) 
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
		List<UISelectEntity> destinos= (List<UISelectEntity>)((ArrayList<UISelectEntity>)this.attrs.get("almacenes")).clone();
    this.attrs.put("destinos", destinos);
		int index = destinos.indexOf(((Transferencia)this.getAdminOrden().getOrden()).getIkAlmacen());
		if(index>= 0)
      destinos.remove(index);
		if(destinos.isEmpty())
      ((Transferencia)this.getAdminOrden().getOrden()).setIkDestino(destinos.get(0));
		this.getAdminOrden().getArticulos().clear();
		this.getAdminOrden().getArticulos().add(new Articulo(-1L));
		this.getAdminOrden().toCalculate();
	}

	public void doTabChange(TabChangeEvent event) {
		if(event.getTab().getTitle().equals("Articulos")) {
			if(this.attrs.get("articulos")== null) 
				this.toLoadArticulos("almacen");
		} // if	
		else 
			if(event.getTab().getTitle().equals("Faltantes almacen") && this.attrs.get("faltantes")== null) 
        this.doLoadFaltantes();
			else 
			  if(event.getTab().getTitle().equals("Ventas perdidas") && this.attrs.get("perdidos")== null) 
          this.doLoadPerdidas(((Transferencia)this.getAdminOrden().getOrden()).getIdDestino()== null? -1L: ((Transferencia)this.getAdminOrden().getOrden()).getIdDestino());
	}

	@Override
	public void doLoadFaltantes() {
		List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
    try {
			params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getDependencias());
			params.put("idAlmacen", ((Transferencia)this.getAdminOrden().getOrden()).getIdDestino());
			columns= new ArrayList<>();
      columns.add(new Columna("propio", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("estatus", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("costo", EFormatoDinamicos.MONEDA_SAT_DECIMALES));
      columns.add(new Columna("stock", EFormatoDinamicos.NUMERO_CON_DECIMALES));
      columns.add(new Columna("minimo", EFormatoDinamicos.NUMERO_SIN_DECIMALES));
      columns.add(new Columna("maximo", EFormatoDinamicos.NUMERO_SIN_DECIMALES));
      this.attrs.put("faltantes", UIEntity.build("VistaAlmacenesTransferenciasDto", "faltantes", params, columns, Constantes.SQL_TODOS_REGISTROS));
    } // try
    catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
    finally {
      Methods.clean(params);
      Methods.clean(columns);
    }// finally
	}
	
	public void doLoadFaltantesAlmacenDestino() {
    this.doLoadPerdidas(((Transferencia)this.getAdminOrden().getOrden()).getIdDestino()== null? -1L: ((Transferencia)this.getAdminOrden().getOrden()).getIdDestino());
	}
	
	public void toLoadArticulos(String idXml) {
		List<Articulo> articulos = null;
    Map<String, Object> params= new HashMap<>();
		try {
			params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
			params.putAll(((Transferencia)this.getAdminOrden().getOrden()).toMap());
			articulos= (List<Articulo>)DaoFactory.getInstance().toEntitySet(Articulo.class, "VistaOrdenesComprasDto", idXml, params);
      if(articulos!= null && this.getAdminOrden().getArticulos().isEmpty())
				for (Articulo articulo : articulos) 
					this.getAdminOrden().add(articulo);
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
  protected void toMoveData(UISelectEntity articulo, Integer index) throws Exception {
		Articulo temporal= this.getAdminOrden().getArticulos().get(index);
		Map<String, Object> params= new HashMap<>();
		try {
			if(articulo.size()> 1) {
				this.doSearchArticulo(articulo.toLong("idArticulo"), index);
				params.put("idArticulo", articulo.toLong("idArticulo"));
				params.put("idAlmacen", this.getAdminOrden().getIdAlmacen());
				temporal.setKey(articulo.toLong("idArticulo"));
				temporal.setIdArticulo(articulo.toLong("idArticulo"));
				temporal.setIdProveedor(-1L);
				temporal.setIdRedondear(articulo.toLong("idRedondear"));
				temporal.setCodigo(articulo.toString("propio"));
				temporal.setPropio(articulo.toString("propio"));
				temporal.setNombre(articulo.toString("nombre"));
				temporal.setOrigen(articulo.toString("origen"));
				temporal.setPrecio(articulo.toDouble("precio"));				
				temporal.setIva(articulo.toDouble("iva"));				
				temporal.setSat("");				
				temporal.setDescuento("");
				temporal.setExtras("");				
				temporal.setUnidadMedida(articulo.toString("unidadMedida"));
				// recuperar el stock de articulos en el almacen origen
				Value origen= (Value)DaoFactory.getInstance().toField("TcManticInventariosDto", "stock", params, "stock");
				temporal.setStock(origen== null? 0D: origen.toDouble());
				// recuperar el stock de articulos en el almacen destino
				params.put("idAlmacen", ((TcManticTransferenciasDto)this.getAdminOrden().getOrden()).getIdDestino());
				origen= (Value)DaoFactory.getInstance().toField("TcManticInventariosDto", "stock", params, "stock");
				temporal.setValor(origen== null? 0D: origen.toDouble());
				origen= (Value)DaoFactory.getInstance().toField("TcManticAlmacenesArticulosDto", "umbral", params, "maximo");
				temporal.setCosto(origen== null? 0D: origen.toDouble());
				// calcular el valor sugerido para la cantidad
				if(temporal.getCantidad()<= 1D && temporal.getStock()> 0D && temporal.getCosto()> 0D) 
					if((temporal.getCosto()- temporal.getValor())> temporal.getStock())
						temporal.setCantidad(temporal.getStock()<= 0? 1D: temporal.getStock());
					else
						if(temporal.getValor()< temporal.getCosto())
						  temporal.setCantidad(temporal.getCosto()- temporal.getValor());
				// el stock del almacen destino es superior al maximo permitido en el almacen
				temporal.setUltimo(temporal.getValor()> temporal.getCosto());
				// el almacen origen no tiene conteo 
				temporal.setSolicitado(temporal.getStock()<= 0D);
				// el almacen destino no tiene conteo
				temporal.setCostoLibre(temporal.getValor()<= 0D);
				if(index== this.getAdminOrden().getArticulos().size()- 1) {
					this.getAdminOrden().getArticulos().add(new Articulo(-1L));
					RequestContext.getCurrentInstance().execute("jsArticulos.update("+ (this.getAdminOrden().getArticulos().size()- 1)+ ");");
				} // if	
				RequestContext.getCurrentInstance().execute("jsArticulos.callback('"+ articulo.toMap()+ "');");
				this.getAdminOrden().toCalculate();
			} // if	
			else
				temporal.setNombre("<span class='janal-color-orange'>EL ARTICULO NO EXISTE EN EL CATALOGO !</span>");
		} // try
		finally {
			Methods.clean(params);
		} // finally
	}
	
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
			transaccion = new Transaccion((TcManticTransferenciasDto)this.getAdminOrden().getOrden(), this.getAdminOrden().getArticulos());
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