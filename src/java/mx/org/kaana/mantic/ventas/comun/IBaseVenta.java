package mx.org.kaana.mantic.ventas.comun;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import mx.org.kaana.kajool.reglas.comun.FormatLazyModel;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.formato.Global;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.compras.ordenes.beans.Articulo;
import mx.org.kaana.mantic.compras.ordenes.reglas.Descuentos;
import mx.org.kaana.mantic.comun.IBaseCliente;
import mx.org.kaana.mantic.db.dto.TcManticApartadosDto;
import mx.org.kaana.mantic.db.dto.TcManticArticulosDto;
import mx.org.kaana.mantic.enums.EEstatusVentas;
import mx.org.kaana.mantic.ventas.beans.ArticuloVenta;
import mx.org.kaana.mantic.ventas.beans.SaldoCliente;
import mx.org.kaana.mantic.ventas.beans.TicketVenta;
import mx.org.kaana.mantic.ventas.reglas.AdminTickets;
import mx.org.kaana.mantic.ventas.reglas.CambioUsuario;
import mx.org.kaana.mantic.ventas.reglas.MotorBusqueda;
import mx.org.kaana.mantic.ventas.caja.beans.Pago;
import org.primefaces.event.SelectEvent;


public abstract class IBaseVenta extends IBaseCliente implements Serializable {

	private static final long serialVersionUID = 4853975930464243369L;
	protected static final String INDIVIDUAL   = "1";
	protected static final String GLOBAL       = "0";	
	protected static final String MEDIO_MAYOREO= "2";	
	protected static final String MAYOREO      = "3";	
	protected static final String MENUDEO      = "4";	
	protected FormatLazyModel lazyCuentasAbiertas;
	protected FormatLazyModel lazyCotizaciones;
	protected FormatLazyModel lazyApartados;
	protected SaldoCliente saldoCliente;
	private FormatLazyModel almacenes;
  private boolean costoLibre;
	
	
	public IBaseVenta(String precio) {
		this(precio, false);
	}
	
	public IBaseVenta(String precio, boolean costoLibre) {
		super(precio);
		this.costoLibre= costoLibre;
	}
	
	public FormatLazyModel getLazyCuentasAbiertas() {
		return lazyCuentasAbiertas;
	}		

	public FormatLazyModel getLazyCotizaciones() {
		return lazyCotizaciones;
	}		

	public FormatLazyModel getLazyApartados() {
		return lazyApartados;
	}		
	
	public SaldoCliente getSaldoCliente() {
		return saldoCliente;
	}	

	public void setSaldoCliente(SaldoCliente saldoCliente) {
		this.saldoCliente = saldoCliente;
	}
	
	public FormatLazyModel getAlmacenes() {
		return almacenes;
	}	

	public boolean isCostoLibre() {
		return costoLibre;
	}	
	
	public String doCancelar() {   
  	JsfBase.setFlashAttribute("idVenta", ((TicketVenta)this.getAdminOrden().getOrden()).getIdVenta());
		JsfBase.setFlashAttribute("xcodigo", this.attrs.get("xcodigo"));	
    return this.attrs.get("retorno") != null ? (String)this.attrs.get("retorno") : "filtro";
  } // doCancelar
	
	protected void loadSucursales(){
		List<UISelectEntity> sucursales= null;
		Map<String, Object>params      = null;
		List<Columna> columns          = null;
		try {
			columns= new ArrayList<>();
			params= new HashMap<>();
			params.put("sucursales", JsfBase.isAdminEncuestaOrAdmin() ? JsfBase.getAutentifica().getEmpresa().getSucursales() : JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
			columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
			sucursales=(List<UISelectEntity>) UIEntity.build("TcManticEmpresasDto", "empresas", params, columns);
			this.attrs.put("sucursales", sucursales);
			this.attrs.put("idEmpresa", sucursales.get(0));
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
	} // loadSucursales
	
	protected void loadSucursalesPerfil(){
		List<UISelectEntity> sucursales= null;
		Map<String, Object>params      = null;
		List<Columna> columns          = null;
		try {
			columns= new ArrayList<>();
			params= new HashMap<>();
			params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
			columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
			sucursales=(List<UISelectEntity>) UIEntity.build("TcManticEmpresasDto", "empresas", params, columns);
			this.attrs.put("sucursales", sucursales);
			this.attrs.put("idEmpresa", sucursales.get(0));
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
	} // loadSucursales
	
	public void doAlmacenesArticulo(Long idArticulo, Integer index) {
		Map<String, Object>params= null;
		List<Columna>columns     = null;
		try {
			if(idArticulo!= null){
				params= new HashMap<>();
				params.put("idArticulo", idArticulo);
				params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getDependencias());
				columns= new ArrayList<>();
				columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
				columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
				columns.add(new Columna("stock", EFormatoDinamicos.NUMERO_SIN_DECIMALES));
				columns.add(new Columna("minimo", EFormatoDinamicos.NUMERO_SIN_DECIMALES));
				columns.add(new Columna("maximo", EFormatoDinamicos.NUMERO_SIN_DECIMALES));
				columns.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA));
				this.almacenes= new FormatLazyModel("VistaKardexDto", "almacenesDetalle", params, columns);
				UIBackingUtilities.resetDataTable("almacenes");
				UIBackingUtilities.execute("PF('dlgAlmacenes').show();");				
			} // if
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
	} // doDetailArticulo
	
	public void doLoadTicketAbiertos(){	
		Map<String, Object>params= null;
		List<Columna> campos     = null;
		try {
			campos= new ArrayList<>();
			params= new HashMap<>();
			params.put("sortOrder", "");
			params.put("idEmpresa", this.attrs.get("idEmpresa"));			
			campos.add(new Columna("cuenta", EFormatoDinamicos.MAYUSCULAS));
			campos.add(new Columna("cliente", EFormatoDinamicos.MAYUSCULAS));
			params.put(Constantes.SQL_CONDICION, toCondicion());
			this.lazyCuentasAbiertas= new FormatLazyModel("VistaVentasDto", "lazy", params, campos);			
			UIBackingUtilities.execute("PF('dlgOpenTickets').show();");			
			UIBackingUtilities.resetDataTable("tablaTicketsAbiertos");
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
	} // doLoadTicketAbiertos		
	
	public void doLoadCotizaciones(){		
		Map<String, Object>params      = null;		
		List<Columna> campos           = null;
		try {			
			params= new HashMap<>();
			params.put("sortOrder", "");
			params.put("idEmpresa", this.attrs.get("idEmpresa"));
			params.put(Constantes.SQL_CONDICION, toCondicion(true));
			campos= new ArrayList<>();
			campos.add(new Columna("cliente", EFormatoDinamicos.MAYUSCULAS));
			campos.add(new Columna("cuenta", EFormatoDinamicos.MAYUSCULAS));
			campos.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA_CORTA));
			campos.add(new Columna("vigencia", EFormatoDinamicos.FECHA_HORA_CORTA));
			this.lazyCotizaciones= new FormatLazyModel("VistaVentasDto", "lazy", params, campos);			
			UIBackingUtilities.execute("PF('dlgCotizaciones').show();");			
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
		finally{
			Methods.clean(params);
			Methods.clean(campos);
		} // finally
	} // doLoadCotizaciones
	
	public void doLoadApartados(){		
		Map<String, Object>params      = null;		
		List<Columna> campos           = null;
		try {			
			params= new HashMap<>();
			params.put("sortOrder", "");
			params.put("idEmpresa", this.attrs.get("idEmpresa"));
			params.put(Constantes.SQL_CONDICION, toCondicionApartados());
			campos= new ArrayList<>();
			campos.add(new Columna("cliente", EFormatoDinamicos.MAYUSCULAS));
			campos.add(new Columna("cuenta", EFormatoDinamicos.MAYUSCULAS));
			campos.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA_CORTA));
			campos.add(new Columna("vigencia", EFormatoDinamicos.FECHA_HORA_CORTA));
			this.lazyApartados= new FormatLazyModel("VistaVentasDto", "lazyApartados", params, campos);			
			UIBackingUtilities.execute("PF('dlgApartados').show();");			
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
		finally{
			Methods.clean(params);
			Methods.clean(campos);
		} // finally
	} // doLoadCotizaciones
	
	private String toCondicion() {
		return toCondicion(false);
	} // toCondicion
	
	private String toCondicion(boolean cotizacion) {
		StringBuilder regresar= null;
		Date fecha            = null;
		try {
			regresar= new StringBuilder();
			if(cotizacion){
				regresar.append("tc_mantic_ventas.id_venta_estatus in (");
				regresar.append(EEstatusVentas.COTIZACION.getIdEstatusVenta());
				regresar.append(") and vigencia is not null");
				regresar.append(" and tc_mantic_ventas.candado= 2 ");
				if(this.attrs.get("fecha")!= null){
					fecha= (Date) this.attrs.get("fecha");			
					regresar.append(" and date_format (tc_mantic_ventas.registro, '%Y%m%d')=".concat(Fecha.formatear(Fecha.FECHA_ESTANDAR, fecha)));
				} // if
				if(this.attrs.get("busquedaCotizacion")!= null && !Cadena.isVacio(this.attrs.get("busquedaCotizacion"))){
					regresar.append(" and (upper(tc_mantic_personas.cuenta) like upper('%");
					regresar.append(this.attrs.get("busquedaCotizacion"));
					regresar.append("%') or upper(tc_mantic_clientes.razon_social)");
					regresar.append(" like upper('%");
					regresar.append(this.attrs.get("busquedaCotizacion"));
					regresar.append("%'))");
				} // if
			} // if
			else{
				regresar.append(" date_format(tc_mantic_ventas.registro, '%Y%m%d')= date_format(SYSDATE(), '%Y%m%d')");
				regresar.append(" and tc_mantic_ventas.candado= 2 ");				
				regresar.append(" and tc_mantic_ventas.id_venta_estatus in (");				
				regresar.append(EEstatusVentas.ELABORADA.getIdEstatusVenta());
				regresar.append(" , ");			
				regresar.append(EEstatusVentas.ABIERTA.getIdEstatusVenta());
				regresar.append(")");				
				if(this.attrs.get("busquedaTicketAbierto")!= null && !Cadena.isVacio(this.attrs.get("busquedaTicketAbierto"))){
					regresar.append(" and (upper(tc_mantic_personas.cuenta) like upper('%");
					regresar.append(this.attrs.get("busquedaTicketAbierto"));
					regresar.append("%') or upper(tc_mantic_clientes.razon_social)");
					regresar.append(" like upper('%");
					regresar.append(this.attrs.get("busquedaTicketAbierto"));
					regresar.append("%'))");
				} // if
			} // else			
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar.toString();
	} // toCondicion	
	
	private String toCondicionApartados() {
		StringBuilder regresar= null;
		Date fecha            = null;
		try {
			regresar= new StringBuilder();			
			regresar.append("tc_mantic_ventas.id_venta_estatus in (");
			regresar.append(EEstatusVentas.APARTADOS.getIdEstatusVenta());
			regresar.append(") and vigencia is not null");
			regresar.append(" and tc_mantic_ventas.candado= 2 ");
			if(this.attrs.get("fecha")!= null){
				fecha= (Date) this.attrs.get("fecha");			
				regresar.append(" and date_format (tc_mantic_ventas.registro, '%Y%m%d')=".concat(Fecha.formatear(Fecha.FECHA_ESTANDAR, fecha)));
			} // if
			if(this.attrs.get("busquedaApartados")!= null && !Cadena.isVacio(this.attrs.get("busquedaApartados"))){
				regresar.append(" and (upper(tc_mantic_personas.cuenta) like upper('%");
				regresar.append(this.attrs.get("busquedaApartados"));
				regresar.append("%') or upper(tc_mantic_clientes.razon_social)");
				regresar.append(" like upper('%");
				regresar.append(this.attrs.get("busquedaApartados"));
				regresar.append("%'))");
			} // if			
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar.toString();
	} // toCondicion	
	
	public void doAsignaTicketAbierto(){
		Map<String, Object>params = null;
		try {
			params= new HashMap<>();			
			params.put("idVenta", ((Entity)this.attrs.get("selectedCuentaAbierta")).get("idVenta"));
			this.setAdminOrden(new AdminTickets((TicketVenta)DaoFactory.getInstance().toEntity(TicketVenta.class, "TcManticVentasDto", "detalle", params)));
    	unlockVentaExtends(Long.valueOf(params.get("idVenta").toString()), (Long)this.attrs.get("ticketLock"));
			this.attrs.put("ticketLock", Long.valueOf(params.get("idVenta").toString()));
			this.attrs.put("sinIva", this.getAdminOrden().getIdSinIva().equals(1L));
			this.attrs.put("consecutivo", ((TicketVenta)this.getAdminOrden().getOrden()).getConsecutivo());
			toLoadCatalog();
			doAsignaClienteTicketAbierto();
			UIBackingUtilities.execute("jsArticulos.initArrayArt(" + String.valueOf(getAdminOrden().getArticulos().size()-1) + ");");
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
	} // doAsignaTicketAbierto
	
	public void doAsignaCotizacion(){
		Map<String, Object>params = null;
		Date actual               = null;
		try {
			params= new HashMap<>();
			params.put("idVenta", this.attrs.get("cotizacion"));
			this.setAdminOrden(new AdminTickets((TicketVenta)DaoFactory.getInstance().toEntity(TicketVenta.class, "TcManticVentasDto", "detalle", params)));
			unlockVentaExtends(Long.valueOf(params.get("idVenta").toString()), (Long)this.attrs.get("ticketLock"));
			this.attrs.put("ticketLock", Long.valueOf(params.get("idVenta").toString()));
			actual= new Date(Calendar.getInstance().getTimeInMillis());
			if(actual.after(((TicketVenta)getAdminOrden().getOrden()).getVigencia()))
				generateNewVenta();    	
			toLoadCatalog();
			doAsignaClienteTicketAbierto();
			UIBackingUtilities.execute("jsArticulos.initArrayArt(" + String.valueOf(getAdminOrden().getArticulos().size()-1) + ");");
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
	} // doAsignaTicketAbierto	
	
	public void doAsignaApartado(){
		Map<String, Object>params = null;
		Date actual               = null;
		try {
			params= new HashMap<>();
			params.put("idVenta", this.attrs.get("apartados"));
			this.setAdminOrden(new AdminTickets((TicketVenta)DaoFactory.getInstance().toEntity(TicketVenta.class, "TcManticVentasDto", "detalle", params)));
			unlockVentaExtends(Long.valueOf(params.get("idVenta").toString()), (Long)this.attrs.get("ticketLock"));
			this.attrs.put("ticketLock", Long.valueOf(params.get("idVenta").toString()));
			asignaAbonoApartado();
			actual= new Date(Calendar.getInstance().getTimeInMillis());
			if(actual.after(((TicketVenta)getAdminOrden().getOrden()).getVigencia()))
				generateNewVenta();    				
			toLoadCatalog();
			doAsignaClienteTicketAbierto();
			UIBackingUtilities.execute("jsArticulos.initArrayArt(" + String.valueOf(getAdminOrden().getArticulos().size()-1) + ");");
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
	} // doAsignaApartado
	
	protected void generateNewVenta() throws Exception{
		List<ArticuloVenta> articulosPivote= null;
		try {
			articulosPivote= new ArrayList<>();
			for(Articulo vigente: getAdminOrden().getArticulos())				
				articulosPivote.add((ArticuloVenta)vigente);			
			getAdminOrden().getArticulos().clear();
			for(ArticuloVenta addArticulo : articulosPivote)
				this.toMoveArticulo(addArticulo, -1);			
		} // try
		catch (Exception e) {			
			throw e; 
		} // catch		
	} // generateNewVenta
	
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
	
	private void doAsignaClienteTicketAbierto() throws Exception{		
		MotorBusqueda motorBusqueda           = null;
		UISelectEntity seleccion              = null;
		List<UISelectEntity> clientesSeleccion= null;
		Entity clienteDefault                 = null;
		try {
			motorBusqueda= new MotorBusqueda(-1L, ((TicketVenta)this.getAdminOrden().getOrden()).getIdCliente());
			seleccion= new UISelectEntity(motorBusqueda.toCliente());
			clientesSeleccion= new ArrayList<>();
			clientesSeleccion.add(seleccion);
			clienteDefault= motorBusqueda.toClienteDefault();
			clientesSeleccion.add(0, new UISelectEntity(clienteDefault));
			this.attrs.put("mostrarCorreos", seleccion.getKey().equals(-1L) || seleccion.getKey().equals(clienteDefault.getKey()));
			this.attrs.put("clientesSeleccion", clientesSeleccion);
			this.attrs.put("clienteSeleccion", seleccion);
			setPrecio(Cadena.toBeanNameEspecial(seleccion.toString("tipoVenta")));
			doReCalculatePreciosArticulos(seleccion.getKey());	
			doLoadSaldos(seleccion.getKey());
		} // try
		catch (Exception e) {	
			throw e;
		} // catch		
	} // doAsignaClienteTicketAbierto
	
	public void doReCalculatePreciosArticulos(Long idCliente){
		doReCalculatePreciosArticulos(true, idCliente);
	} // doReCalculatePreciosArticulos
	
	public void doReCalculatePreciosArticulos(boolean descuentoVigente, Long idCliente){
		MotorBusqueda motor          = null;
		TcManticArticulosDto articulo= null;
		String descuento             = null;
		String sinDescuento          = "0";
		try {			
			if(!getAdminOrden().getArticulos().isEmpty()){
				for(Articulo beanArticulo: getAdminOrden().getArticulos()){
					if(beanArticulo.getIdArticulo()!= null && !beanArticulo.getIdArticulo().equals(-1L)){
						motor= new MotorBusqueda(beanArticulo.getIdArticulo());
						articulo= motor.toArticulo();						
						if(!beanArticulo.getCosto().equals(articulo.getMayoreo()) && !beanArticulo.getCosto().equals(articulo.getMedioMayoreo())){
							beanArticulo.setValor((Double) articulo.toValue(getPrecio()));
							beanArticulo.setCosto((Double) articulo.toValue(getPrecio()));
						} // if		
						else
							((ArticuloVenta)beanArticulo).setDescuentoAsignado(true);						
						if(descuentoVigente){
							descuento= toDescuentoVigente(beanArticulo.getIdArticulo(), idCliente);
							if(descuento!= null)
								beanArticulo.setDescuento(descuento);							
						} // if
						else
							beanArticulo.setDescuento(sinDescuento);
					} // if
				} // for					
				if(getAdminOrden().getArticulos().size()>1){					
					getAdminOrden().toCalculate();
					getAdminOrden().cleanPrecioDescuentoArticulo();
					UIBackingUtilities.update("@(.filas) @(.recalculo) @(.informacion)");
				} // if
			} // if			
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
	} // doReCalculatePreciosArticulos
	
	protected void doLoadSaldos(Long idCliente) throws Exception{
		Entity cliente     = null;
		MotorBusqueda motor= null;
		this.saldoCliente  = null;
		try {			
			this.saldoCliente= new SaldoCliente();
			if(!idCliente.equals(-1L)){
				motor= new MotorBusqueda(null, idCliente);
				cliente= motor.toCliente();			
				this.saldoCliente.setIdCliente(idCliente);
				this.saldoCliente.setTotalCredito(cliente.toDouble("limiteCredito"));
				this.saldoCliente.setTotalDeuda(motor.toDeudaCliente());
				this.saldoCliente.setTotalVenta(getAdminOrden().getTotales().getTotal());
			} // if
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
	} // doLoadSaldos
	
	private String toDescuentoVigente(Long idArticulo, Long idCliente) throws Exception{
		MotorBusqueda motorBusqueda= null;
		Entity descuentoVigente    = null;
		String regresar            = null;
		try {
			motorBusqueda= new MotorBusqueda(idArticulo, idCliente);
			descuentoVigente= motorBusqueda.toDescuentoGrupo();
			if(descuentoVigente!= null)
				regresar= descuentoVigente.toString("porcentaje");
			else{
				descuentoVigente= motorBusqueda.toDescuentoArticulo();
				if(descuentoVigente!= null)
					regresar= descuentoVigente.toString("porcentaje");
			} // else
		} // try
		catch (Exception e) {			
			throw e;			
		} // catch		
		return regresar;
	} // toDescuentoVigente
	
	public void doAplicarDescuento() {
		doAplicarDescuento(-1);
	} // doAplicarDescuento
	
	public void doAplicarDescuento(Integer index) {
		Boolean isIndividual       = false;
		Boolean isGlobal           = false;
		Boolean isMedioMayoreo     = false;
		Boolean isMayoreo          = false;
		CambioUsuario cambioUsuario= null;
		String cuenta              = null;
		String contrasenia         = null;
		Double global              = 0D;
		Boolean recalculate        = false;
		try {
			if(!getAdminOrden().getArticulos().isEmpty()){
				cuenta       = (String)this.attrs.get("usuarioDescuento");
				contrasenia  = (String)this.attrs.get("passwordDescuento");
				cambioUsuario= new CambioUsuario(cuenta, contrasenia);
				if(cambioUsuario.validaPrivilegiosDescuentos()){
					this.attrs.put("decuentoAutorizadoActivo", true);				
					((ArticuloVenta)getAdminOrden().getArticulos().get(index)).setDescuentoActivo(true);
					isIndividual= Boolean.valueOf(this.attrs.get("isIndividual").toString());
					isGlobal= Boolean.valueOf(this.attrs.get("isGlobal").toString());
					isMedioMayoreo= Boolean.valueOf(this.attrs.get("isMedioMayoreo").toString());
					isMayoreo= Boolean.valueOf(this.attrs.get("isMayoreo").toString());
					if(isIndividual){
						this.attrs.put("tipoDecuentoAutorizadoActivo", INDIVIDUAL);
						getAdminOrden().getArticulos().get(index).setDescuento(this.attrs.get("descuentoIndividual").toString());
						if(getAdminOrden().getArticulos().get(index).autorizedDiscount())
							UIBackingUtilities.execute("jsArticulos.divDiscount('".concat(this.attrs.get("descuentoIndividual").toString()).concat("');"));
						else
							JsfBase.addMessage("No es posble aplicar el descuento, el descuento es superior a la utilidad", ETipoMensaje.ERROR);
					} // if
					else if (isGlobal){		
						this.attrs.put("tipoDecuentoAutorizadoActivo", GLOBAL);
						global= Double.valueOf(this.attrs.get("descuentoGlobal").toString());
						getAdminOrden().toCalculate();
						if(global < getAdminOrden().getTotales().getUtilidad()){
							getAdminOrden().getTotales().setGlobal(global);							
							getAdminOrden().toCalculate();
						} // if
						else
							JsfBase.addMessage("No es posble aplicar el descuento, el descuento es superior a la utilidad", ETipoMensaje.ERROR);
					} // else if
					else if (isMedioMayoreo){
						this.attrs.put("tipoDecuentoAutorizadoActivo", MEDIO_MAYOREO);
						setPrecio("medioMayoreo");
						getAdminOrden().getArticulos().get(index).setCosto(getAdminOrden().getArticulos().get(index).toEntity().toDouble("medioMayoreo"));
						((ArticuloVenta)getAdminOrden().getArticulos().get(index)).setDescripcionPrecio("medioMayoreo");
						((ArticuloVenta)getAdminOrden().getArticulos().get(index)).setDescuentoAsignado(true);
						((ArticuloVenta)getAdminOrden().getArticulos().get(index)).toCalculate();
						recalculate= true;
					} // else if
					else if (isMayoreo){
						this.attrs.put("tipoDecuentoAutorizadoActivo", MAYOREO);
						setPrecio("mayoreo");
						getAdminOrden().getArticulos().get(index).setCosto(getAdminOrden().getArticulos().get(index).toEntity().toDouble("mayoreo"));
						((ArticuloVenta)getAdminOrden().getArticulos().get(index)).setDescripcionPrecio("mayoreo");
						((ArticuloVenta)getAdminOrden().getArticulos().get(index)).setDescuentoAsignado(true);
						((ArticuloVenta)getAdminOrden().getArticulos().get(index)).toCalculate();
						recalculate= true;
					} // else if
					else{
						this.attrs.put("decuentoAutorizadoActivo", false);					
						this.attrs.put("tipoDecuentoAutorizadoActivo", MENUDEO);
						setPrecio("menudeo");
						((ArticuloVenta)getAdminOrden().getArticulos().get(index)).setDescuentoActivo(false);
						getAdminOrden().getArticulos().get(index).setCosto(getAdminOrden().getArticulos().get(index).toEntity().toDouble("menudeo"));
						((ArticuloVenta)getAdminOrden().getArticulos().get(index)).setDescripcionPrecio("menudeo");
						((ArticuloVenta)getAdminOrden().getArticulos().get(index)).setDescuentoAsignado(false);
						((ArticuloVenta)getAdminOrden().getArticulos().get(index)).toCalculate();
					} // else
				} // if
				else
					JsfBase.addMessage("El usuario no tiene privilegios o el usuario y la contraseña son incorrectos", ETipoMensaje.ERROR);
			} // if
			if(recalculate){
				getAdminOrden().toCalculate();
				UIBackingUtilities.update("@(.filas) @(.recalculo) @(.informacion)");
			} // if
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
		finally{			
			this.attrs.put("descuentoIndividual", 0);
			this.attrs.put("descuentoGlobal", 0);
			this.attrs.put("usuarioDescuento", "");
			this.attrs.put("passwordDescuento", "");
			this.attrs.put("tipoDescuento", 1L);
		} // finally
	} // doAplicarDescuento
	
	public boolean doVerificaVigenciaCotizacion(Long idKey){
		boolean regresar           = false;
		MotorBusqueda motorBusqueda= null;		
		try {
			motorBusqueda= new MotorBusqueda(-1L);
			regresar= motorBusqueda.doVerificaVigenciaCotizacion(idKey);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
		return regresar;
	} // doVerificaVigenciaCotizacion
	
	public void doActivarDescuento(){
		String tipoDescuento= null;		
		try {
			tipoDescuento= this.attrs.get("tipoDescuento").toString();
			this.attrs.put("isIndividual", tipoDescuento.equals(INDIVIDUAL));
			this.attrs.put("isGlobal", tipoDescuento.equals(GLOBAL));
			this.attrs.put("isMedioMayoreo", tipoDescuento.equals(MEDIO_MAYOREO));
			this.attrs.put("isMayoreo", tipoDescuento.equals(MAYOREO));
			this.attrs.put(tipoDescuento.equals(INDIVIDUAL)  ? "descuentoGlobal" : "descuentoIndividual", 0);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
	} // doActivarDescuento
	
	@Override
	protected void toMoveData(UISelectEntity articulo, Integer index) throws Exception {
		ArticuloVenta temporal= (ArticuloVenta) getAdminOrden().getArticulos().get(index);
		Map<String, Object> params= new HashMap<>();
		try {
			if(articulo.size()> 1) {
				this.doSearchArticulo(articulo.toLong("idArticulo"), index);
				params.put("idArticulo", articulo.toLong("idArticulo"));
				params.put("idProveedor", getAdminOrden().getIdProveedor());
				params.put("idAlmacen", getAdminOrden().getIdAlmacen());
				temporal.setKey(articulo.toLong("idArticulo"));
				temporal.setIdArticulo(articulo.toLong("idArticulo"));
				temporal.setIdProveedor(getAdminOrden().getIdProveedor());
				temporal.setIdRedondear(articulo.toLong("idRedondear"));
				Value codigo= (Value)DaoFactory.getInstance().toField("TcManticArticulosCodigosDto", "codigo", params, "codigo");
				temporal.setCodigo(codigo== null? "": codigo.toString());
				temporal.setPropio(articulo.toString("propio"));
				temporal.setNombre(articulo.toString("nombre"));
				temporal.setValor(articulo.toDouble(getPrecio()));
				temporal.setCosto(articulo.toDouble(getPrecio()));
				temporal.setIva(articulo.toDouble("iva"));				
				temporal.setSat(articulo.get("sat").getData()!= null ? articulo.toString("sat") : "");				
				temporal.setDescuento(getAdminOrden().getDescuento());
				temporal.setExtras(getAdminOrden().getExtras());				
				// SON ARTICULOS QUE ESTAN EN LA FACTURA MAS NO EN LA ORDEN DE COMPRA
				if(articulo.containsKey("descuento")) 
				  temporal.setDescuento(articulo.toString("descuento"));
				if(articulo.containsKey("cantidad")) {
				  temporal.setCantidad(articulo.toDouble("cantidad"));
				  temporal.setSolicitados(articulo.toDouble("cantidad"));
				} // if	
				if(temporal.getCantidad()<= 0D)					
					temporal.setCantidad(1D);
				temporal.setDescripcionPrecio(getPrecio());
				temporal.setMenudeo(articulo.toDouble("menudeo"));				
 				temporal.setDescuentoActivo((Boolean)this.attrs.get("decuentoAutorizadoActivo"));
				temporal.setUltimo(this.attrs.get("ultimo")!= null);
				temporal.setSolicitado(this.attrs.get("solicitado")!= null);
				temporal.setUnidadMedida(articulo.toString("unidadMedida"));
				temporal.setPrecio(articulo.toDouble("precio"));				
				Value stock= (Value)DaoFactory.getInstance().toField("TcManticInventariosDto", "stock", params, "stock");
				temporal.setStock(stock== null? 0D: stock.toDouble());
				if(index== getAdminOrden().getArticulos().size()- 1) {
					this.getAdminOrden().getArticulos().add(new ArticuloVenta(-1L, this.costoLibre));
					this.getAdminOrden().toAddUltimo(this.getAdminOrden().getArticulos().size()- 1);
					UIBackingUtilities.execute("jsArticulos.update("+ (getAdminOrden().getArticulos().size()- 1)+ ");");
				} // if	
				UIBackingUtilities.execute("jsArticulos.callback('"+ articulo.getKey()+ "');");
				getAdminOrden().toCalculate();
			} // if	
			else
				temporal.setNombre("<span class='janal-color-orange'>EL ARTICULO NO EXISTE EN EL CATALOGO !</span>");
		} // try
		finally {
			Methods.clean(params);
		}
	}
	
	@Override
	public void doUpdateSolicitado(Long idArticulo) {
		List<Columna> columns= null;
    try {
			if(idArticulo!= null)
  			this.attrs.put("idArticulo", idArticulo);
			columns= new ArrayList<>();
      columns.add(new Columna("razonSocial", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("moneda", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("costo", EFormatoDinamicos.MONEDA_CON_DECIMALES));
      columns.add(new Columna("cantidad", EFormatoDinamicos.NUMERO_CON_DECIMALES));
      columns.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA_CORTA));
			Entity solicitado= (Entity)DaoFactory.getInstance().toEntity("VistaOrdenesComprasDto", "solicitado", this.attrs);
			if(solicitado!= null) {
		     Descuentos descuentos= new Descuentos(solicitado.toDouble("costo"), solicitado.toString("descuento").concat(",").concat("extra"));
		     Value value= new Value("real", Numero.toRedondear(descuentos.toImporte()== 0? solicitado.toDouble("costo"):  descuentos.toImporte()));
				 solicitado.put("real", value);
		     descuentos= new Descuentos(solicitado.toDouble("costo"), solicitado.toString("descuento"));
		     value     = new Value("calculado", Numero.toRedondear(descuentos.toImporte()== 0? solicitado.toDouble("costo"):  descuentos.toImporte()));
				 solicitado.put("calculado", value);
			} // if
      this.attrs.put("solicitado", solicitado!= null? UIBackingUtilities.toFormatEntity(solicitado, columns): null);
    } // try
    catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
    finally {
      Methods.clean(columns);
    }// finally
	}
	
	@Override
	public void doUpdateInformacion(Long idArticulo) {
    try {
			if(idArticulo!= null)
			  this.attrs.put("idArticulo", idArticulo);
			Entity ultimoPrecio= (Entity)DaoFactory.getInstance().toEntity("VistaOrdenesComprasDto", "ultimo", this.attrs);
			if(ultimoPrecio!= null && !ultimoPrecio.isEmpty()) {
				Descuentos descuentos= new Descuentos(ultimoPrecio.toDouble("costo"), ultimoPrecio.toString("descuento").concat(",").concat("extra"));
				Value calculo= new Value("real", Numero.toRedondear(descuentos.toImporte()== 0? ultimoPrecio.toDouble("costo"):  descuentos.toImporte()));
				ultimoPrecio.put("real", calculo);
				descuentos= new Descuentos(ultimoPrecio.toDouble("costo"), ultimoPrecio.toString("descuento"));
				calculo   = new Value("calculado", Numero.toRedondear(descuentos.toImporte()== 0? ultimoPrecio.toDouble("costo"):  descuentos.toImporte()));
				ultimoPrecio.put("calculado", calculo);
				for (Value value : ultimoPrecio.values()) {
				  if("|costo|".indexOf(value.getName())> 0)
						value.setData(Global.format(EFormatoDinamicos.MILES_CON_DECIMALES, Numero.toRedondear(value.toDouble())));
					else
            if("|registro|".indexOf(value.getName())> 0) 
					    value.setData(Global.format(EFormatoDinamicos.FECHA_HORA_CORTA, value.toTimestamp()));						
  					else
              if("|stock|".indexOf(value.getName())> 0) 
		  			    value.setData(Global.format(EFormatoDinamicos.NUMERO_CON_DECIMALES, value.toDouble()));						
				} // for
			} // if	
 		  this.attrs.put("ultimo", ultimoPrecio);
    } // try
    catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
	}
	
	@Override
	public void doLoadFaltantes() {
		List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
    try {
			params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
			params.put("idAlmacen", this.getAdminOrden().getIdAlmacen());
			params.put("idProveedor", this.getAdminOrden().getIdProveedor());
			columns= new ArrayList<>();
      columns.add(new Columna("propio", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("estatus", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("costo", EFormatoDinamicos.MONEDA_CON_DECIMALES));
      columns.add(new Columna("stock", EFormatoDinamicos.NUMERO_CON_DECIMALES));
      columns.add(new Columna("minimo", EFormatoDinamicos.NUMERO_SIN_DECIMALES));
      columns.add(new Columna("maximo", EFormatoDinamicos.NUMERO_SIN_DECIMALES));
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
	
	@Override
  public void doFindArticulo(Integer index) {
		try {
    	List<UISelectEntity> articulos= (List<UISelectEntity>)this.attrs.get("articulos");
	    UISelectEntity articulo  = (UISelectEntity)this.attrs.get("articulo");
	    UISelectEntity encontrado= (UISelectEntity)this.attrs.get("encontrado");
			if(encontrado!= null) {
				articulo= encontrado;
				this.attrs.remove("encontrado");
			} // else
			else 
				if(articulo== null)
					articulo= new UISelectEntity(new Entity(-1L));
				else
					if(articulos.indexOf(articulo)>= 0) 
						articulo= articulos.get(articulos.indexOf(articulo));
					else
						articulo= articulos.get(0);
			if(articulo.size()> 1) {
				int position= this.getAdminOrden().getArticulos().indexOf(new ArticuloVenta(articulo.toLong("idArticulo"), this.costoLibre));
				if(articulo.size()> 1 && position>= 0) {
					if(index!= position)
						UIBackingUtilities.execute("jsArticulos.exists('"+ articulo.toString("propio")+ "', '"+ articulo.toString("nombre")+ "', "+ position+ ");");
				} // if	
				else
					this.toMoveData(articulo, index);
			} // else
			else 
					this.toMoveData(articulo, index);
		} // try
	  catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
	} 
	
	public void doCleanClientes() {
		List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
		try {
			columns= new ArrayList<>();
  		params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getSucursales());
  		params.put(Constantes.SQL_CONDICION, Constantes.SQL_FALSO);
      this.attrs.put("lazyModelClientes", new FormatCustomLazy("VistaClientesDto", "findRazonSocial", params, columns));
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
	
  public void doAsignaCliente(SelectEvent event) {
		UISelectEntity seleccion     = null;
		List<UISelectEntity> clientes= null;
		try {
			clientes= (List<UISelectEntity>) this.attrs.get("clientes");
			seleccion= clientes.get(clientes.indexOf((UISelectEntity)event.getObject()));
			this.toFindCliente(seleccion);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
	} // doAsignaCliente
	
  private void toFindCliente(UISelectEntity seleccion) {
		List<UISelectEntity> clientesSeleccion= null;
		MotorBusqueda motorBusqueda           = null;
		Entity clienteDefault                 = null;
		try {
			clientesSeleccion= new ArrayList<>();
			clientesSeleccion.add(seleccion);
			motorBusqueda= new MotorBusqueda(-1L);
			clienteDefault= motorBusqueda.toClienteDefault();
			clientesSeleccion.add(0, new UISelectEntity(clienteDefault));
			this.attrs.put("clientesSeleccion", clientesSeleccion);
			this.attrs.put("clienteSeleccion", seleccion);
			this.attrs.put("mostrarCorreos", seleccion.getKey().equals(-1L) || seleccion.getKey().equals(clienteDefault.getKey()));
			setPrecio(Cadena.toBeanNameEspecial(seleccion.toString("tipoVenta")));
			doReCalculatePreciosArticulos(seleccion.getKey());		
			doLoadSaldos(seleccion.getKey());
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
	}
 
	public void doChangeCliente() {
		if(this.attrs.get("comprador")== null) {
			FormatCustomLazy list= (FormatCustomLazy)this.attrs.get("lazyModelClientes");
			if(list!= null) {
				List<Entity> items= (List<Entity>)list.getWrappedData();
				if(items.size()> 0)
					this.toFindCliente(new UISelectEntity(items.get(0)));
			} // if
		} // else
	  else
      this.toFindCliente((UISelectEntity)this.attrs.get("comprador"));
	}

  public void doRowDblCliente(SelectEvent event) {
		this.toFindCliente(new UISelectEntity((Entity)event.getObject()));
	}	
	
	public void doUpdateDialogClientes(String codigo) {
		List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
		boolean buscaPorCodigo    = false;
    try {
			columns= new ArrayList<>();
      columns.add(new Columna("rfc", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("razonSocial", EFormatoDinamicos.MAYUSCULAS));
  		params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getSucursales());
			if(!Cadena.isVacio(codigo)) {
				buscaPorCodigo= codigo.startsWith(".");
				if(buscaPorCodigo)
					codigo= codigo.trim().substring(1);
			} // if	
			else
				codigo= "WXYZ";
			if(buscaPorCodigo)
    		params.put(Constantes.SQL_CONDICION, "upper(tc_mantic_clientes.rfc) like '".concat(codigo.toUpperCase()).concat("%'"));			
			else
    		params.put(Constantes.SQL_CONDICION, "upper(tc_mantic_clientes.razon_social) like '".concat(codigo.toUpperCase()).concat("%'"));
      this.attrs.put("lazyModelClientes", new FormatCustomLazy("VistaClientesDto", "findRazonSocial", params, columns));
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

	public List<UISelectEntity> doCompleteCliente(String query) {
		this.attrs.put("codigoCliente", query);
    this.doUpdateClientes();		
		return (List<UISelectEntity>)this.attrs.get("clientes");
	}	// doCompleteCliente
	
	public void doUpdateClientes() {
		List<Columna> columns     = null;
    Map<String, Object> params= null;
    try {
			params= new HashMap<>();
			columns= new ArrayList<>();
      columns.add(new Columna("rfc", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("razonSocial", EFormatoDinamicos.MAYUSCULAS));
  		params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getDependencias());
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
	
	public String toColor(Entity row) {
		return doVerificaVigenciaCotizacion(row.getKey()) ? "janal-tr-yellow" : "";
	} // toColor
	
	protected void asignaAbonoApartado() throws Exception{
		TcManticApartadosDto apartado= null;
		Map<String, Object>params    = null;
		try {
			params= new HashMap<>();
			params.put("idVenta", ((TicketVenta)this.getAdminOrden().getOrden()).getIdVenta());
			apartado= (TcManticApartadosDto) DaoFactory.getInstance().toEntity(TcManticApartadosDto.class, "TcManticApartadosDto", "detalle", params);
			if(apartado.isValid())
				((Pago) this.attrs.get("pago")).setAbono(apartado.getAbonado());								
		} // try
		catch (Exception e) {			
			throw e; 
		} // catch
		finally{
			Methods.clean(params);
		} // finally
	} // asignaAbonoApartado
	
	protected void asignaFechaApartado() throws Exception{
		TcManticApartadosDto apartado= null;
		Map<String, Object>params    = null;
		try {
			params= new HashMap<>();
			params.put("idVenta", ((TicketVenta)this.getAdminOrden().getOrden()).getIdVenta());
			apartado= (TcManticApartadosDto) DaoFactory.getInstance().toEntity(TcManticApartadosDto.class, "TcManticApartadosDto", "detalle", params);
			if(apartado.isValid())
				((TicketVenta)this.getAdminOrden().getOrden()).setVigencia(apartado.getVencimiento());
		} // try
		catch (Exception e) {			
			throw e; 
		} // catch
		finally{
			Methods.clean(params);
		} // finally
	} // asignaAbonoApartado

	@Override
	public void doUpdateArticulos() {
		List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
		int buscarCodigoPor       = 1;
    try {
			columns= new ArrayList<>();
      columns.add(new Columna("propio", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
			params.put("idAlmacen", JsfBase.getAutentifica().getEmpresa().getIdAlmacen());
  		params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getDependencias());
  		params.put("idProveedor", this.attrs.get("proveedor")== null? new UISelectEntity(new Entity(-1L)): ((UISelectEntity)this.attrs.get("proveedor")).getKey());
			String search= (String)this.attrs.get("codigo"); 
			if(!Cadena.isVacio(search)) {
				if((boolean)this.attrs.get("buscaPorCodigo"))
			    buscarCodigoPor= 1;
				if(search.startsWith("."))
					buscarCodigoPor= 2;
				else 
					if(search.startsWith(":"))
						buscarCodigoPor= 0;
				if(search.startsWith(".") || search.startsWith(":"))
					search= search.trim().substring(1);				
				search= search.toUpperCase().replaceAll(Constantes.CLEAN_SQL, "").trim().replaceAll("(,| |\\t)+", ".*.*");
			} // if	
			else
				search= "WXYZ";
  		params.put("codigo", search);						
			switch(buscarCodigoPor) {      
				case 0: 
					this.attrs.put("articulos", (List<UISelectEntity>) UIEntity.build("VistaOrdenesComprasDto", "porCodigo", params, columns, 20L));
					break;
				case 1: 
					this.attrs.put("articulos", (List<UISelectEntity>) UIEntity.build("VistaOrdenesComprasDto", "porCodigoIgual", params, columns, 20L));
					break;
				case 2:
          this.attrs.put("articulos", (List<UISelectEntity>) UIEntity.build("VistaOrdenesComprasDto", "porNombre", params, columns, 20L));
          break;
			} // switch
		} // try
	  catch (Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    } // finally
	} // doUpdateArticulos	
}