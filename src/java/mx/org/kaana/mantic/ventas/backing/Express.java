package mx.org.kaana.mantic.ventas.backing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Cifrar;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.ventas.reglas.MotorBusqueda;
import mx.org.kaana.mantic.ventas.beans.TicketVenta;
import mx.org.kaana.mantic.ventas.reglas.Transaccion;
import mx.org.kaana.mantic.compras.ordenes.enums.EOrdenes;
import mx.org.kaana.mantic.enums.EEstatusVentas;
import mx.org.kaana.mantic.ventas.comun.IBaseVenta;
import org.primefaces.event.SelectEvent;

@Named(value= "manticVentasExpress")
@ViewScoped
public class Express extends IBaseVenta implements Serializable {

  private static final long serialVersionUID = 327393488565639367L;  
	private TicketVenta venta;
	private EOrdenes tipoOrden;	
	private boolean aplicar;

	public Express() {
		super("menudeo");
	}
	
	public String getTitulo() {
		return "(".concat(tipoOrden.name()).concat(")");
	}

	public EOrdenes getTipoOrden() {
		return tipoOrden;
	}	

	public TicketVenta getVenta() {
		return venta;
	}

	public void setVenta(TicketVenta venta) {
		this.venta = venta;
	}	
	
	@PostConstruct
  @Override
  protected void init() {		
		boolean isMatriz= false;
		this.aplicar    = false;
    try {
			this.attrs.put("isAplicar", JsfBase.isAdminEncuestaOrAdmin());
			this.tipoOrden= JsfBase.getParametro("zOyOxDwIvGuCt")== null? EOrdenes.NORMAL: EOrdenes.valueOf(Cifrar.descifrar(JsfBase.getParametro("zOyOxDwIvGuCt")));
      this.attrs.put("accion", JsfBase.getFlashAttribute("accion")== null? EAccion.AGREGAR : JsfBase.getFlashAttribute("accion"));
      this.attrs.put("idVenta", JsfBase.getFlashAttribute("idVenta")== null? -1L: JsfBase.getFlashAttribute("idVenta"));
			this.attrs.put("retorno", JsfBase.getFlashAttribute("retorno")== null? null: JsfBase.getFlashAttribute("retorno"));      
			this.attrs.put("ticketLock", -1L);
			loadClienteDefault();
			this.attrs.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
			isMatriz= JsfBase.getAutentifica().getEmpresa().isMatriz();
			this.attrs.put("isMatriz", isMatriz);
			if(isMatriz)
				loadSucursales();
			doLoad();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

  public void doLoad() {
    EAccion eaccion          = null;
		Map<String, Object>params= null;
    try {			
      eaccion= (EAccion) this.attrs.get("accion");
			this.attrs.put("disabled", eaccion.equals(EAccion.CONSULTAR));
      this.attrs.put("nombreAccion", Cadena.letraCapital(eaccion.name()));
      switch (eaccion) {
        case AGREGAR:					
					this.venta= new TicketVenta();
					this.venta.setIdManual(1L);
					this.venta.setIdAlmacen(JsfBase.getAutentifica().getEmpresa().getIdAlmacen());
          break;
        case MODIFICAR:			
        case CONSULTAR:			          
					params= new HashMap<>();
					params.put("idVenta", Long.valueOf(this.attrs.get("idVenta").toString()));
					this.venta= (TicketVenta) DaoFactory.getInstance().toEntity(TicketVenta.class, "TcManticVentasDto", "detalle", params);
					this.attrs.put("idEmpresa", this.venta.getIdEmpresa());
					if(this.venta.getIdCliente()!= null && !this.venta.getIdCliente().equals(-1L)){
						doAsignaClienteInicial(this.venta.getIdCliente());
					} // if
          break;
      } // switch			
			toLoadCatalog();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
		finally{
			Methods.clean(params);
		} // finally
  } // doLoad

	public String doAplicar(){
		String regresar= null;
		try {
			this.aplicar= true;
			regresar= doAceptar();
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);		
		} // catch		
		return regresar;
	} // doAplicar
	
  public String doAceptar() {  
    Transaccion transaccion= null;
    String regresar        = null;
    try {
			this.venta.setIdEmpresa(((UISelectEntity) this.attrs.get("idEmpresa")).getKey());
			this.venta.setIdCliente(((UISelectEntity) this.attrs.get("clienteSeleccion")).getKey());
			if(this.aplicar)
				this.venta.setIdVentaEstatus(EEstatusVentas.PAGADA.getIdEstatusVenta());
			else
				this.venta.setIdVentaEstatus(EEstatusVentas.CREDITO.getIdEstatusVenta());
			transaccion = new Transaccion(this.venta);
			transaccion.setAplicar(this.aplicar);
			if (transaccion.ejecutar(EAccion.PROCESAR)) {
 				if(!this.aplicar)
					regresar= "/Paginas/Mantic/Catalogos/Clientes/Cuentas/saldos".concat(Constantes.REDIRECIONAR);
				else	
					regresar= "filtro".concat(Constantes.REDIRECIONAR);
    		UIBackingUtilities.execute("jsArticulos.back('gener\\u00F3 la cuenta ', '"+ this.venta.getConsecutivo()+ "');");													
				// JsfBase.addMessage("Se ".concat(eaccion.equals(EAccion.AGREGAR) ? "agregó" : "modificó").concat(" la cuenta."), ETipoMensaje.INFORMACION);  			
			} // if
			else 
				JsfBase.addMessage("Ocurrió un error al registrar el ticket.", ETipoMensaje.ERROR);      			
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    return regresar;
  } // doAccion  

	private void toLoadCatalog() {
		List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
    try {
			columns= new ArrayList<>();
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
			params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
      this.attrs.put("almacenes", UIEntity.build("TcManticAlmacenesDto", "almacenes", params, columns));
 			List<UISelectEntity> almacenes= (List<UISelectEntity>)this.attrs.get("almacenes");
			if(!almacenes.isEmpty()) 
				((TicketVenta)this.getAdminOrden().getOrden()).setIkAlmacen(almacenes.get(0));
      columns.remove(0);
			columns.add(new Columna("razonSocial", EFormatoDinamicos.MAYUSCULAS));
      this.attrs.put("clientes", UIEntity.build("TcManticClientesDto", "sucursales", params, columns));
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
  
	@Override
	public List<UISelectEntity> doCompleteCliente(String query) {
		this.attrs.put("codigoCliente", query);
    this.doUpdateClientes();		
		return (List<UISelectEntity>)this.attrs.get("clientes");
	}	// doCompleteCliente

	@Override
	public void doUpdateClientes() {
		List<Columna> columns     = null;
    Map<String, Object> params= null;
    try {
			params= new HashMap<>();
			columns= new ArrayList<>();
      columns.add(new Columna("rfc", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("razonSocial", EFormatoDinamicos.MAYUSCULAS));
  		params.put("idEmpresa", this.attrs.get("idEmpresa"));
			String search= (String) this.attrs.get("codigoCliente"); 
			search= !Cadena.isVacio(search) ? search.toUpperCase().replaceAll(Constantes.CLEAN_SQL, "").trim().replaceAll("(,| |\\t)+", ".*.*") : "WXYZ";
  		params.put(Constantes.SQL_CONDICION, "upper(tc_mantic_clientes.razon_social) regexp '.*".concat(search).concat(".*'").concat(" or upper(tc_mantic_clientes.rfc) regexp '.*".concat(search).concat(".*'")));			
      this.attrs.put("clientes", (List<UISelectEntity>) UIEntity.build("VistaClientesDto", "findRazonSocial", params, columns, 20L));
		} // try
	  catch (Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    } // finally
	}	// doUpdateClientes
	
	@Override
	public void doAsignaCliente(SelectEvent event){
		UISelectEntity seleccion              = null;
		List<UISelectEntity> clientes         = null;
		List<UISelectEntity> clientesSeleccion= null;
		MotorBusqueda motorBusqueda           = null;
		try {
			clientes= (List<UISelectEntity>) this.attrs.get("clientes");
			seleccion= clientes.get(clientes.indexOf((UISelectEntity)event.getObject()));
			clientesSeleccion= new ArrayList<>();
			clientesSeleccion.add(seleccion);
			motorBusqueda= new MotorBusqueda(-1L);
			clientesSeleccion.add(0, new UISelectEntity(motorBusqueda.toClienteDefault()));
			this.attrs.put("clientesSeleccion", clientesSeleccion);
			this.attrs.put("clienteSeleccion", seleccion);			
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
	} // doAsignaCliente
	
	public void doAsignaClienteInicial(Long idCliente){
		UISelectEntity seleccion              = null;
		List<UISelectEntity> clientesSeleccion= null;
		MotorBusqueda motorBusqueda           = null; 
		try {
			motorBusqueda= new MotorBusqueda(null, idCliente);
			seleccion= new UISelectEntity(motorBusqueda.toCliente());
			clientesSeleccion= new ArrayList<>();
			clientesSeleccion.add(seleccion);
			clientesSeleccion.add(0, new UISelectEntity(motorBusqueda.toClienteDefault()));
			this.attrs.put("clientesSeleccion", clientesSeleccion);
			this.attrs.put("clienteSeleccion", seleccion);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
	} // doAsignaCliente	
	
	private void loadClienteDefault(){
		UISelectEntity seleccion              = null;
		List<UISelectEntity> clientesSeleccion= null;
		MotorBusqueda motorBusqueda           = null;
		try {
			motorBusqueda= new MotorBusqueda(-1L);
			seleccion= new UISelectEntity(motorBusqueda.toClienteDefault());
			clientesSeleccion= new ArrayList<>();
			clientesSeleccion.add(seleccion);			
			this.attrs.put("clientesSeleccion", clientesSeleccion);
			this.attrs.put("clienteSeleccion", seleccion);			
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
	} // loadClienteDefault	
	
	public void doUpdateForEmpresa(){
		try {
			loadClienteDefault();
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
	} // doUpdateForEmpresa	
	
	@Override
	public String doCancelar() {     	
		if(!this.aplicar)
			return "/Paginas/Mantic/Catalogos/Clientes/Cuentas/saldos".concat(Constantes.REDIRECIONAR);
		else	
			return "filtro".concat(Constantes.REDIRECIONAR);
  } // doCancelar
}
