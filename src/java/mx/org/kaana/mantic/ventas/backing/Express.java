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
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.ventas.reglas.MotorBusqueda;
import mx.org.kaana.mantic.ventas.beans.TicketVenta;
import mx.org.kaana.mantic.ventas.reglas.Transaccion;
import mx.org.kaana.mantic.enums.EEstatusVentas;
import mx.org.kaana.mantic.ventas.comun.IBaseVenta;
import org.primefaces.event.SelectEvent;

@Named(value= "manticVentasExpress")
@ViewScoped
public class Express extends IBaseVenta implements Serializable {

  private static final long serialVersionUID = 327393488565639367L;  
	private TicketVenta venta;
	private boolean aplicar;

	public Express() {
		super("menudeo");
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
      this.attrs.put("idCliente", JsfBase.getFlashAttribute("idCliente"));
      this.attrs.put("accion", JsfBase.getFlashAttribute("accion")== null? EAccion.AGREGAR : JsfBase.getFlashAttribute("accion"));
      this.attrs.put("idVenta", JsfBase.getFlashAttribute("idVenta")== null? -1L: JsfBase.getFlashAttribute("idVenta"));
			this.attrs.put("retorno", JsfBase.getFlashAttribute("retorno")== null? null: JsfBase.getFlashAttribute("retorno"));      
			this.attrs.put("ticketLock", -1L);
			this.attrs.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
			isMatriz= JsfBase.getAutentifica().getEmpresa().isMatriz();
			this.attrs.put("isMatriz", isMatriz);
			if(isMatriz)
				this.loadSucursales();
			this.doLoad();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

  @Override
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
					this.venta.setIdAlmacen(JsfBase.getAutentifica().getEmpresa().getIdAlmacen());
					this.venta.setIdManual(1L);
					this.venta.setEjercicio(2020L);
          this.venta.setGlobal(0D);
          this.venta.setUtilidad(0D);
          this.venta.setIdTipoMedioPago(1L);
          this.venta.setIdTipoPago(2L);
          break;
        case MODIFICAR:			
        case CONSULTAR:			          
					params= new HashMap<>();
					params.put("idVenta", Long.valueOf(this.attrs.get("idVenta").toString()));
					this.venta= (TicketVenta) DaoFactory.getInstance().toEntity(TicketVenta.class, "TcManticVentasDto", "detalle", params);
					this.attrs.put("idEmpresa", this.venta.getIdEmpresa());
					if(this.venta.getIdCliente()!= null && !this.venta.getIdCliente().equals(-1L)) {
						this.doAsignaClienteInicial(this.venta.getIdCliente());
					} // if
          break;
      } // switch		
			this.toLoadClienteDefualt();
			this.toLoadCatalog();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
		finally{
			Methods.clean(params);
		} // finally
  } // doLoad

	public String doAplicar() {
		String regresar= null;
		try {
			this.aplicar= true;
			regresar= this.doAceptar();
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
        JsfBase.setFlashAttribute("idCliente", this.venta.getIdCliente());		
 				if(this.aplicar)
          regresar= "filtro".concat(Constantes.REDIRECIONAR);
				else	
					regresar= "/Paginas/Mantic/Catalogos/Clientes/Cuentas/saldos".concat(Constantes.REDIRECIONAR);
    		UIBackingUtilities.execute("jsArticulos.back('gener\\u00F3', '"+ this.venta.getTicket()+ "');");													
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
				this.venta.setIkAlmacen(almacenes.get(0));
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
  		// params.put("idEmpresa", this.attrs.get("idEmpresa"));
  		params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getSucursales());
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
	public void doAsignaCliente(SelectEvent event) {
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
	
	public void doAsignaClienteInicial(Long idCliente) {
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
	
	private void toLoadClienteDefualt() {
		UISelectEntity seleccion              = null;
		List<UISelectEntity> clientesSeleccion= null;
		MotorBusqueda motorBusqueda           = null;
		try {
      motorBusqueda= new MotorBusqueda(-1L, (Long)this.attrs.get("idCliente"));
      if(Cadena.isVacio(this.attrs.get("idCliente"))) 
			  seleccion= new UISelectEntity(motorBusqueda.toClienteDefault());
      else
			  seleccion= new UISelectEntity(motorBusqueda.toClienteFuente());
			clientesSeleccion= new ArrayList<>();
			clientesSeleccion.add(seleccion);			
			this.attrs.put("clientesSeleccion", clientesSeleccion);
			this.attrs.put("clienteSeleccion", seleccion);			
      this.venta.setIdCliente(seleccion.toLong("idCliente"));
      this.venta.setIdClienteDomicilio(seleccion.toLong("idClienteDomicilio"));
      this.venta.setIkCliente(new UISelectEntity(seleccion));
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
	} // loadClienteDefault	
	
	public void doUpdateForEmpresa() {
		try {
			this.toLoadClienteDefualt();
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
	} // doUpdateForEmpresa	
	
	@Override
	public String doCancelar() {    
    JsfBase.setFlashAttribute("idCliente", this.attrs.get("idCliente"));		
		if(!this.aplicar)
			return "/Paginas/Mantic/Catalogos/Clientes/Cuentas/saldos".concat(Constantes.REDIRECIONAR);
		else	
			return this.attrs.get("regreso")== null? "filtro".concat(Constantes.REDIRECIONAR): (String)this.attrs.get("regreso");
  } // doCancelar

  public void doCalculate() {
    this.venta.setImpuestos(Numero.toRedondearSat(this.venta.getTotal()* 0.16));
    this.venta.setSubTotal(Numero.toRedondearSat(this.venta.getTotal()- this.venta.getImpuestos()));
  }
  
}
