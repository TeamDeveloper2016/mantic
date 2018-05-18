package mx.org.kaana.mantic.compras.entradas.backing;

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
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Cifrar;
import mx.org.kaana.libs.formato.Global;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.compras.ordenes.beans.OrdenCompra;
import mx.org.kaana.mantic.compras.ordenes.reglas.Transaccion;
import mx.org.kaana.mantic.compras.ordenes.beans.Articulo;
import mx.org.kaana.mantic.compras.ordenes.enums.EOrdenes;
import mx.org.kaana.mantic.compras.ordenes.reglas.AdminOrdenes;
import mx.org.kaana.mantic.db.dto.TrManticArticuloPrecioSugeridoDto;
import org.primefaces.event.TabChangeEvent;


@Named(value= "manticComprasEntradasAccion")
@ViewScoped
public class Accion extends IBaseAttribute implements Serializable {

  private static final long serialVersionUID = 327393488565639367L;
  private AdminOrdenes adminOrden;
	private EOrdenes tipoOrden;

	public AdminOrdenes getAdminOrden() {
		return adminOrden;
	}

	public void setAdminOrden(AdminOrdenes adminOrden) {
		this.adminOrden=adminOrden;
	}

	@PostConstruct
  @Override
  protected void init() {		
    try {
      this.attrs.put("isPesos", false);
      this.attrs.put("accion", JsfBase.getFlashAttribute("accion")== null? EAccion.AGREGAR: JsfBase.getFlashAttribute("accion"));
      this.attrs.put("idOrdenCompra", JsfBase.getFlashAttribute("idOrdenCompra")== null? -1L: JsfBase.getFlashAttribute("idOrdenCompra"));
			this.attrs.put("retorno", JsfBase.getFlashAttribute("retorno")== null? "filtro": JsfBase.getFlashAttribute("retorno"));
			this.attrs.put("cantidad", 1);
			this.attrs.put("precio", 0);
			this.attrs.put("sinIva", false);
			this.attrs.put("diferencia", "0");
			this.attrs.put("solicitado", new Entity(-1L));
			doLoad();
			List<UISelectEntity> almacenes= (List<UISelectEntity>)this.attrs.get("almacenes");
			if(!almacenes.isEmpty()) 
				this.adminOrden.getOrden().setIkAlmacen(almacenes.get(0));
			this.tipoOrden= JsfBase.getParametro("zOyOxDwIvGuCt")== null? EOrdenes.NORMAL: EOrdenes.valueOf(Cifrar.descifrar(JsfBase.getParametro("zOyOxDwIvGuCt")));
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

  public void doLoad() {
    EAccion eaccion= null;
    try {
      eaccion= (EAccion) this.attrs.get("accion");
      this.attrs.put("nombreAccion", Cadena.letraCapital(eaccion.name()));
      switch (eaccion) {
        case AGREGAR:											
          this.adminOrden= new AdminOrdenes(new OrdenCompra(-1L));
          break;
        case MODIFICAR:					
          this.adminOrden= new AdminOrdenes((OrdenCompra)DaoFactory.getInstance().toEntity(OrdenCompra.class, "TcManticOrdenesComprasDto", "detalle", this.attrs));
    			this.attrs.put("sinIva", this.adminOrden.getOrden().getIdSinIva().equals(1L));
          break;
      } // switch
			toLoadCatalog();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // doLoad

  public String doAceptar() {  
    Transaccion transaccion= null;
    String regresar        = null;
		EAccion eaccion        = null;
    try {			
			eaccion= (EAccion) this.attrs.get("accion");
			this.adminOrden.getOrden().setDescuentos(this.adminOrden.getTotales().getDescuentos());
			this.adminOrden.getOrden().setImpuestos(this.adminOrden.getTotales().getIva());
			this.adminOrden.getOrden().setSubtotal(this.adminOrden.getTotales().getSubTotal());
			this.adminOrden.getOrden().setTotal(this.adminOrden.getTotales().getTotal());
			transaccion = new Transaccion(this.adminOrden.getOrden(), this.adminOrden.getArticulos());
			if (transaccion.ejecutar(eaccion)) {
				if(eaccion.equals(EAccion.AGREGAR))
 				  regresar = this.attrs.get("retorno").toString().concat(Constantes.REDIRECIONAR);
				JsfBase.addMessage("Se ".concat(eaccion.equals(EAccion.AGREGAR) ? "agregó" : "modificó").concat(" la orden de compra."), ETipoMensaje.INFORMACION);
  			JsfBase.setFlashAttribute("idOrdenCompra", this.adminOrden.getOrden().getIdOrdenCompra());
			} // if
			else 
				JsfBase.addMessage("Ocurrió un error al registrar la orden de compra.", ETipoMensaje.ERROR);      			
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    return regresar;
  } // doAccion

  public String doCancelar() {   
  	JsfBase.setFlashAttribute("idOrdenCompra", this.adminOrden.getOrden().getIdOrdenCompra());
    return (String)this.attrs.get("retorno");
  } // doCancelar

	private void toLoadCatalog() {
		List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
    try {
			columns= new ArrayList<>();
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
			params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
      this.attrs.put("almacenes", UIEntity.build("TcManticAlmacenesDto", "almacenes", params, columns));
      columns.remove(0);
			columns.add(new Columna("razonSocial", EFormatoDinamicos.MAYUSCULAS));
      this.attrs.put("clientes", UIEntity.build("TcManticClientesDto", "sucursales", params, columns));
      this.attrs.put("proveedores", UIEntity.build("VistaOrdenesComprasDto", "moneda", params, columns));
			List<UISelectEntity> proveedores= (List<UISelectEntity>)this.attrs.get("proveedores");
			if(!proveedores.isEmpty()) { 
				this.adminOrden.getOrden().setIkProveedor(proveedores.get(0));
				toLoadCondiciones(proveedores.get(0));
			} // if	
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

	private void toLoadCondiciones(UISelectEntity proveedor) {
		List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
    try {
			columns= new ArrayList<>();
      columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
  		params.put("idProveedor", proveedor.getKey());
      this.attrs.put("condiciones", UIEntity.build("VistaOrdenesComprasDto", "condiciones", params, columns));
			List<UISelectEntity> condiciones= (List<UISelectEntity>) this.attrs.get("condiciones");
			if(!condiciones.isEmpty())
				this.adminOrden.getOrden().setDescuento(condiciones.get(0).toString("descuento"));
			this.attrs.put("proveedor", proveedor);
			Calendar fechaEstimada= Calendar.getInstance();
			int tipoDia= proveedor.toInteger("idTipoDia");
			int dias   = proveedor.toInteger("dias");
	    fechaEstimada.set(Calendar.DATE, fechaEstimada.get(Calendar.DATE)+ dias);
			if(tipoDia== 2) {
		    fechaEstimada.add(Calendar.DATE, ((int)(dias/5)* 2));
			  int dia= fechaEstimada.get(Calendar.DAY_OF_WEEK);
			  dias= dia== Calendar.SUNDAY? 1: dia== Calendar.SATURDAY? 2: 0;
		    fechaEstimada.add(Calendar.DATE, dias);
			} // if
			this.adminOrden.getOrden().setEntregaEstimada(new Date(fechaEstimada.getTimeInMillis()));
    } // try
    catch (Exception e) {
			JsfBase.addMessageError(e);
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    }// finally
	}
	
	public void doUpdateProveedor() {
		try {
			if(this.tipoOrden.equals(EOrdenes.PROVEEDOR)) {
				this.adminOrden.getArticulos().clear();
				this.adminOrden.toCalculate();
			} // if	
			List<UISelectEntity> proveedores= (List<UISelectEntity>)this.attrs.get("proveedores");
			toLoadCondiciones(proveedores.get(proveedores.indexOf((UISelectEntity)this.adminOrden.getOrden().getIkProveedor())));
		}	
	  catch (Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
	} 
	
	public void doUpdateArticulos() {
		List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
    try {
			columns= new ArrayList<>();
      columns.add(new Columna("codigo", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
  		params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
  		params.put("idProveedor", this.attrs.get("proveedor")== null? new UISelectEntity(new Entity(-1L)): ((UISelectEntity)this.attrs.get("proveedor")).getKey());
			String search= (String)this.attrs.get("codigo"); 
			if(!Cadena.isVacio(search)) 
				search= search.trim().toUpperCase().replaceAll("(,| |\\t)+", ".*.*");
			else
				search= "WXYZ";
  		params.put("codigo", search);
      this.attrs.put("articulos", (List<UISelectEntity>) UIEntity.build("VistaOrdenesComprasDto", "codigos", params, columns));
	    this.doUpdatePrecio(UIBackingUtilities.toFirstKeySelectEntity((List<UISelectEntity>)this.attrs.get("articulos")));
		} // try
	  catch (Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    }// finally
	}	
 
  private UISelectEntity toArticulo() {
  	List<UISelectEntity> articulos= (List<UISelectEntity>)this.attrs.get("articulos");
	  UISelectEntity articulo= (UISelectEntity)this.attrs.get("articulo");
		return articulos== null? new UISelectEntity(new Entity(-1L)): articulos.indexOf(articulo)>= 0? articulos.get(articulos.indexOf(articulo)): articulos.isEmpty()? new UISelectEntity(new Entity(-1L)): articulos.get(0);
	}
	
	public void doAddArticulo() {
		try {
			UISelectEntity articulo= toArticulo(); 
			((Value)articulo.get("precio")).setData(this.attrs.get("precio"));
			toAddArticulo(articulo);
		} // try
	  catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
	}

	public void toAddArticulo(UISelectEntity seleccionado) {
		try {
			Long idOrdenDetalle= new Long((int)(Math.random()*10000));
			Articulo item= new Articulo(
				(Boolean)this.attrs.get("sinIva"),
				this.adminOrden.getOrden().getTipoDeCambio(),
				seleccionado.toString("nombre"), 
				seleccionado.toString("codigo"),
				seleccionado.toDouble("precio"),
        this.adminOrden.getOrden().getDescuento(), 
				-1L,
				this.adminOrden.getOrden().getExtras(), 
				0D,
				seleccionado.toString("propio"),
				seleccionado.toDouble("iva"), 
				0D,
				0D,
				(Long)this.attrs.get("cantidad"), 
				-1* idOrdenDetalle, 
				seleccionado.toLong("idArticulo"), 
				0.0,
			  this.adminOrden.getOrden().getIdProveedor());
			this.adminOrden.add(item);
			this.attrs.put("cantidad", 1);
			this.attrs.put("precio", 0);
			this.attrs.put("codigo", "");
			this.attrs.put("articulos", null);
		} // try
	  catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
	}

	public void doRemoveArticulo() {
		try {
   		this.adminOrden.remove((Articulo)this.attrs.get("seleccionado"));
		} // try
	  catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
	}

  public void doUpdatePrecio() {
		this.doUpdatePrecio(toArticulo());
	} 
	
  public void doUpdatePrecio(UISelectEntity seleccionado) {
		try {
   		this.attrs.put("precio", seleccionado.containsKey("precio")? seleccionado.toDouble("precio"): 0D);
   		this.attrs.put("idArticulo", seleccionado.getKey());
			this.doUpdateDiferencia();
			this.doUpdateInformacion();
			this.doUpdateSolicitado();
		} // try
	  catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
	} 
	
	public void doUpdateIvaTipoDeCambio() {
		boolean sinIva= (boolean)this.attrs.get("sinIva");
		this.adminOrden.getOrden().setIdSinIva(sinIva? 1L: 2L);
		this.adminOrden.toCalculate();
	} 

  public void doUpdateDiferencia() {
		double precio    = (Double)this.attrs.get("precio");
		double diferencia= 0.0; 
		double costo     = precio== 0? 1: precio; 
		UISelectEntity articulo= this.toArticulo();
		if(articulo!= null && articulo.size()> 1)
		  costo= articulo.toDouble("precio");
    diferencia= precio* 100/ costo;
  	this.attrs.put("diferencia", Numero.toRedondear(diferencia- 100));
	} 

	private void doUpdateInformacion() {
    try {
			Entity ultimoPrecio= (Entity)DaoFactory.getInstance().toEntity("VistaOrdenesComprasDto", "ultimo", this.attrs);
			if(ultimoPrecio!= null && !ultimoPrecio.isEmpty())
				ultimoPrecio.values().stream().map((value) -> {
					if("|costo|".indexOf(value.getName())> 0)
						value.setData(Numero.toRedondear(value.toDouble()));
					return value;
				}).filter((value) -> ("|registro|".indexOf(value.getName())> 0)).forEachOrdered((value) -> {
					value.setData(Global.format(EFormatoDinamicos.FECHA_HORA_CORTA, value.toTimestamp()));
				}); // for
 		  this.attrs.put("ultimo", ultimoPrecio);
			doUpdateSugeridos();
    } // try
    catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
	}
	
	public void doUpdateSugeridos() {
		List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
    try {
			if(this.attrs.get("sugerido")!= null && ((UISelectEntity)this.attrs.get("sugerido")).getKey()!= -1L) {
			  TrManticArticuloPrecioSugeridoDto sugerido= new TrManticArticuloPrecioSugeridoDto(((UISelectEntity)this.attrs.get("sugerido")).getKey());
				params.put("idLeido", 1L);
				DaoFactory.getInstance().update(sugerido, params);
				this.attrs.put("sugerido", new UISelectEntity(new Entity(-1L)));
			} // if	
			columns= new ArrayList<>();
      columns.add(new Columna("proveedor", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("precio", EFormatoDinamicos.MONEDA_CON_DECIMALES));
      columns.add(new Columna("descuento", EFormatoDinamicos.NUMERO_CON_DECIMALES));
		  this.attrs.put("sugeridos", UIEntity.build("VistaOrdenesComprasDto", "sugerido", this.attrs, columns));
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
	
	public void doUpdateSolicitado() {
		List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
    try {
			columns= new ArrayList<>();
      columns.add(new Columna("razonSocial", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("moneda", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("costo", EFormatoDinamicos.MONEDA_CON_DECIMALES));
      columns.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA_CORTA));
			Entity solicitado= (Entity)DaoFactory.getInstance().toEntity("VistaOrdenesComprasDto", "solicitado", this.attrs);
      this.attrs.put("solicitado", solicitado!= null? UIBackingUtilities.toFormatEntity(solicitado, columns): new Entity(-1L));
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

  public void doUpdatePorcentaje() {
		this.adminOrden.toUpdatePorcentajes();
	} 

	public void doTabChange(TabChangeEvent event) {
		//JsfBase.addMessage("Tab changed active tab: "+ event.getTab().getId());
		if(event.getTab().getTitle().equals("Articulos")) {
			if(this.attrs.get("proveedores")== null) 
  	  	JsfBase.addMessage("No se selecciono ningun proveedor !", ETipoMensaje.INFORMACION);
			if(this.attrs.get("articulos")== null) {
				switch(this.tipoOrden) {
					case NORMAL:
						break;
					case ALMACEN: 
						this.toLoadArticulos("almacen");
						break;
					case PROVEEDOR:
						this.toLoadArticulos("proveedor");
						break;
				} // switch
			} // if
		} // if	
		else 
			if(event.getTab().getTitle().equals("Faltantes") && this.attrs.get("faltantes")== null) 
        toLoadFaltantes();
	}
  
	public void toLoadArticulos(String idXml) {
		List<Articulo> articulos  = null;
    Map<String, Object> params= new HashMap<>();
		try {
			params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
			params.putAll(this.adminOrden.getOrden().toMap());
			articulos= (List<Articulo>)DaoFactory.getInstance().toEntitySet(Articulo.class, "VistaOrdenesComprasDto", idXml, params);
      if(articulos!= null && this.adminOrden.getArticulos().isEmpty())
				for (Articulo articulo : articulos) {
					articulo.toPrepare(
						(Boolean)this.attrs.get("sinIva"), 
						this.adminOrden.getOrden().getTipoDeCambio(), 
						this.adminOrden.getOrden().getIdProveedor()
					);
					this.adminOrden.add(articulo);
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

	public void doUpdateAlmacen() {
		if(this.tipoOrden.equals(EOrdenes.ALMACEN)) {
  		this.adminOrden.getArticulos().clear();
			this.adminOrden.toCalculate();
		} // if	
	}
	
	public void doFaltanteArticulo() {
		try {
			UISelectEntity faltante= (UISelectEntity)this.attrs.get("faltante");
   		toAddArticulo(faltante);
		  List<UISelectEntity> faltantes= (List<UISelectEntity>)this.attrs.get("faltantes");
			faltantes.remove(faltantes.indexOf(faltante));
		} // try
	  catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
	}

	public void toLoadFaltantes() {
		List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
    try {
			params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
			params.put("idAlmacen", this.adminOrden.getOrden().getIdAlmacen());
			params.put("idProveedor", this.adminOrden.getOrden().getIdProveedor());
			columns= new ArrayList<>();
      columns.add(new Columna("propio", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("estatus", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("costo", EFormatoDinamicos.MONEDA_CON_DECIMALES));
      columns.add(new Columna("stock", EFormatoDinamicos.NUMERO_SIN_DECIMALES));
      columns.add(new Columna("minimo", EFormatoDinamicos.NUMERO_SIN_DECIMALES));
      columns.add(new Columna("maximo", EFormatoDinamicos.NUMERO_SIN_DECIMALES));
      columns.add(new Columna("cantidad", EFormatoDinamicos.NUMERO_SIN_DECIMALES));
      this.attrs.put("faltantes", UIEntity.build("VistaOrdenesComprasDto", "faltantes", params, columns));
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
	
}