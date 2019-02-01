package mx.org.kaana.mantic.catalogos.almacenes.confrontas.backing;

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
import mx.org.kaana.mantic.catalogos.almacenes.confrontas.beans.Confronta;
import mx.org.kaana.mantic.catalogos.almacenes.confrontas.reglas.AdminConfronta;
import mx.org.kaana.mantic.catalogos.almacenes.transferencias.reglas.Transaccion;
import mx.org.kaana.mantic.compras.ordenes.beans.Articulo;
import mx.org.kaana.mantic.comun.IBaseArticulos;
import mx.org.kaana.mantic.comun.IBaseStorage;
import mx.org.kaana.mantic.db.dto.TcManticTransferenciasDto;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.primefaces.context.RequestContext;
import org.primefaces.event.TabChangeEvent;


@Named(value= "manticCatalogosAlmacenesConfrontasAccion")
@ViewScoped
public class Accion extends IBaseArticulos implements IBaseStorage, Serializable {

	private static final Log LOG              = LogFactory.getLog(Accion.class);
  private static final long serialVersionUID= 327393488565639367L;
	private EAccion accion;

	public String getAgregar() {
		return this.accion.equals(EAccion.AGREGAR)? "none": "";
	}
	
	@PostConstruct
  @Override
  protected void init() {		
    try {
			if(JsfBase.getFlashAttribute("accion")== null)
				RequestContext.getCurrentInstance().execute("janal.isPostBack('cancelar')");
      this.accion= JsfBase.getFlashAttribute("accion")== null? EAccion.AGREGAR: (EAccion)JsfBase.getFlashAttribute("accion");
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
          this.setAdminOrden(new AdminConfronta(new Confronta(-1L, 2L)));
          break;
        case MODIFICAR:			
        case CONSULTAR:											
          this.setAdminOrden(new AdminConfronta((Confronta)DaoFactory.getInstance().toEntity(Confronta.class, "TcManticConfrontasDto", "detalle", this.attrs)));
          break;
      } // switch
 			this.attrs.put("sinIva", false);
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
				if(this.accion.equals(EAccion.PROCESAR)) {
   			  RequestContext.getCurrentInstance().execute("janal.back(' gener\\u00F3 la confronta ', '"+ ((Confronta)this.getAdminOrden().getOrden()).getConsecutivo()+ "');");
		  		JsfBase.addMessage("Se registró la transferencia de correcta", ETipoMensaje.INFORMACION);
 				  regresar = ((String)this.attrs.get("retorno")).concat(Constantes.REDIRECIONAR);
				} // if	
 				if(!this.accion.equals(EAccion.CONSULTAR)) 
    			JsfBase.addMessage("Se ".concat(this.accion.equals(EAccion.AGREGAR) ? "agregó" : "modificó").concat(" la confronta de articulos."), ETipoMensaje.INFORMACION);
  			JsfBase.setFlashAttribute("idTransferencia", ((Confronta)this.getAdminOrden().getOrden()).getIdTransferencia());
			} // if
			else 
				JsfBase.addMessage("Ocurrió un error al registrar la confronta de articulos.", ETipoMensaje.ALERTA);      			
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    return regresar;
  } // doAccion

  public String doCancelar() {   
  	JsfBase.setFlashAttribute("idTransferencia", ((Confronta)this.getAdminOrden().getOrden()).getIdTransferencia());
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
			  ((Confronta)this.getAdminOrden().getOrden()).setIkEmpresa(empresas.get(empresas.indexOf(((Confronta)this.getAdminOrden().getOrden()).getIkEmpresa())));
				this.attrs.put("idPedidoSucursal", ((Confronta)this.getAdminOrden().getOrden()).getIkEmpresa());
			} // if	
      this.attrs.put("almacenes", UIEntity.build("TcManticAlmacenesDto", "almacenes", params, columns));
 			List<UISelectEntity> almacenes= (List<UISelectEntity>)this.attrs.get("almacenes");
			if(!almacenes.isEmpty()) {
				List<UISelectEntity> destinos= (List<UISelectEntity>)((ArrayList<UISelectEntity>)almacenes).clone();
			  ((Confronta)this.getAdminOrden().getOrden()).setIkAlmacen(almacenes.get(almacenes.indexOf(((Confronta)this.getAdminOrden().getOrden()).getIkAlmacen())));
        this.attrs.put("destinos", destinos);
  			int index = destinos.indexOf(((Confronta)this.getAdminOrden().getOrden()).getIkAlmacen());
  			if(index>= 0)
	  			destinos.remove(index);
  		  ((Confronta)this.getAdminOrden().getOrden()).setIkDestino(destinos.get(destinos.indexOf(((Confronta)this.getAdminOrden().getOrden()).getIkDestino())));
			} // if
			columns.clear();
      columns.add(new Columna("nombres", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("materno", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("paterno", EFormatoDinamicos.MAYUSCULAS));
      List<UISelectEntity> personas= UIEntity.build("VistaAlmacenesTransferenciasDto", "solicito", params, columns);
      this.attrs.put("personas", personas);
			if(personas!= null && !this.accion.equals(EAccion.AGREGAR) && ((Confronta)this.getAdminOrden().getOrden()).getTransferencia().getIdSolicito()!= null && ((Confronta)this.getAdminOrden().getOrden()).getTransferencia().getIdSolicito()> 0L) 
				((Confronta)this.getAdminOrden().getOrden()).setIkSolicito(personas.get(personas.indexOf(new UISelectEntity(((Confronta)this.getAdminOrden().getOrden()).getTransferencia().getIdSolicito()))));
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

	public void doTabChange(TabChangeEvent event) {
		if(event.getTab().getTitle().equals("Articulos")) {
			
		} // if	
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
					this.getAdminOrden().toAddUltimo(this.getAdminOrden().getArticulos().size()- 1);
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
   			RequestContext.getCurrentInstance().execute("jsArticulos.back('guard\\u00F3 confronta de articulos', '"+ ((Confronta)this.getAdminOrden().getOrden()).getConsecutivo()+ "');");
				this.accion= EAccion.PROCESAR;
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
	
}