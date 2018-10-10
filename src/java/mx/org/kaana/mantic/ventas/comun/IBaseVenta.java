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
import mx.org.kaana.kajool.reglas.comun.FormatLazyModel;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.formato.Global;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelect;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.pagina.UISelectItem;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.compras.ordenes.beans.Articulo;
import mx.org.kaana.mantic.compras.ordenes.reglas.Descuentos;
import mx.org.kaana.mantic.comun.IAdminArticulos;
import mx.org.kaana.mantic.comun.IBaseCliente;
import mx.org.kaana.mantic.db.dto.TcManticArticulosDto;
import mx.org.kaana.mantic.enums.EEstatusVentas;
import mx.org.kaana.mantic.ventas.beans.ArticuloVenta;
import mx.org.kaana.mantic.ventas.beans.SaldoCliente;
import mx.org.kaana.mantic.ventas.beans.TicketVenta;
import mx.org.kaana.mantic.ventas.reglas.AdminTickets;
import mx.org.kaana.mantic.ventas.reglas.CambioUsuario;
import mx.org.kaana.mantic.ventas.reglas.MotorBusqueda;
import org.primefaces.context.RequestContext;


public abstract class IBaseVenta extends IBaseCliente implements Serializable{

	private static final long serialVersionUID = 4853975930464243369L;
	protected static final String INDIVIDUAL= "1";
	protected SaldoCliente saldoCliente;
	private FormatLazyModel almacenes;
	
	public IBaseVenta(String precio) {
		super(precio);
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

	public String doCancelar() {   
  	JsfBase.setFlashAttribute("idVenta", ((TicketVenta)this.getAdminOrden().getOrden()).getIdVenta());
    return this.attrs.get("retorno") != null ? (String)this.attrs.get("retorno") : "filtro";
  } // doCancelar
	
	protected void loadSucursales(){
		List<UISelectEntity> sucursales= null;
		Map<String, Object>params      = null;
		List<Columna> columns          = null;
		try {
			columns= new ArrayList<>();
			params= new HashMap<>();
			params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
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
				columns= new ArrayList<>();
				columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
				columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
				columns.add(new Columna("stock", EFormatoDinamicos.NUMERO_SIN_DECIMALES));
				columns.add(new Columna("minimo", EFormatoDinamicos.NUMERO_SIN_DECIMALES));
				columns.add(new Columna("maximo", EFormatoDinamicos.NUMERO_SIN_DECIMALES));
				columns.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA));
				this.almacenes= new FormatLazyModel("VistaKardexDto", "almacenes", params, columns);
				UIBackingUtilities.resetDataTable("almacenes");
				RequestContext.getCurrentInstance().execute("PF('dlgAlmacenes').show();");				
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
		List<UISelectItem> ticketsAbiertos= null;
		Map<String, Object>params         = null;
		List<String> fields               = null;
		try {
			fields= new ArrayList<>();
			params= new HashMap<>();
			params.put("sortOrder", "");
			params.put("idEmpresa", this.attrs.get("idEmpresa"));
			fields.add("consecutivo");
			fields.add("cuenta");
			fields.add("precioTotal");
			fields.add("cliente");
			params.put(Constantes.SQL_CONDICION, toCondicion());
			ticketsAbiertos= UISelect.build("VistaVentasDto", "lazy", params, fields, " - ", EFormatoDinamicos.MAYUSCULAS, Constantes.SQL_TODOS_REGISTROS);
			if(!ticketsAbiertos.isEmpty()){
				this.attrs.put("ticketsAbiertos", ticketsAbiertos);
				if(!ticketsAbiertos.isEmpty())
					this.attrs.put("ticketAbierto", UIBackingUtilities.toFirstKeySelectItem(ticketsAbiertos));
				RequestContext.getCurrentInstance().execute("PF('dlgOpenTickets').show();");
			} // if
			else{
				JsfBase.addMessage("Cuentas", "Actualmente no hay cuentas abiertas", ETipoMensaje.INFORMACION);
				RequestContext.getCurrentInstance().execute("janal.desbloquear();");
			} // else
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
		List<UISelectItem> cotizaciones= null;
		Map<String, Object>params      = null;
		List<String> fields            = null;
		MotorBusqueda motorBusqueda    = null;
		try {
			fields= new ArrayList<>();
			params= new HashMap<>();
			params.put("sortOrder", "");
			params.put("idEmpresa", this.attrs.get("idEmpresa"));
			fields.add("consecutivo");
			fields.add("cuenta");
			fields.add("precioTotal");
			fields.add("cliente");
			params.put(Constantes.SQL_CONDICION, toCondicion(true));
			cotizaciones= UISelect.build("VistaVentasDto", "lazy", params, fields, " - ", EFormatoDinamicos.MAYUSCULAS, Constantes.SQL_TODOS_REGISTROS);
			if(!cotizaciones.isEmpty()){
				this.attrs.put("cotizaciones", cotizaciones);
				if(!cotizaciones.isEmpty()){
					motorBusqueda= new MotorBusqueda(-1L);
					this.attrs.put("cotizacion", UIBackingUtilities.toFirstKeySelectItem(cotizaciones));
					this.attrs.put("expirada", motorBusqueda.doVerificaVigenciaCotizacion(Long.valueOf(this.attrs.get("cotizacion").toString())));
				}
				RequestContext.getCurrentInstance().execute("PF('dlgCotizaciones').show();");
			} // if
			else{
				JsfBase.addMessage("Cuentas", "Actualmente no hay cotizaciones abiertas", ETipoMensaje.INFORMACION);
				RequestContext.getCurrentInstance().execute("janal.desbloquear();");
			} // else
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
	} // doLoadCotizaciones
	
	private String toCondicion() {
		return toCondicion(false);
	} // toCondicion
	
	private String toCondicion(boolean cotizacion) {
		StringBuilder regresar= null;
		try {
			regresar= new StringBuilder();
			if(cotizacion){
				regresar.append("tc_mantic_ventas.id_venta_estatus in (");
				regresar.append(EEstatusVentas.COTIZACION.getIdEstatusVenta());
				regresar.append(") and vigencia is not null");
			} // if
			else{
				regresar.append(" date_format(tc_mantic_ventas.registro, '%Y%m%d')= date_format(SYSDATE(), '%Y%m%d')");
				regresar.append(" and tc_mantic_ventas.id_venta_estatus in (");
				regresar.append(EEstatusVentas.ELABORADA.getIdEstatusVenta());
				regresar.append(" , ");			
				regresar.append(EEstatusVentas.ABIERTA.getIdEstatusVenta());
				regresar.append(")");				
			} // else
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
			params.put("idVenta", this.attrs.get("ticketAbierto"));
			this.setAdminOrden(new AdminTickets((TicketVenta)DaoFactory.getInstance().toEntity(TicketVenta.class, "TcManticVentasDto", "detalle", params)));
    	this.attrs.put("sinIva", this.getAdminOrden().getIdSinIva().equals(1L));
			this.attrs.put("consecutivo", ((TicketVenta)this.getAdminOrden().getOrden()).getConsecutivo());
			toLoadCatalog();
			doAsignaClienteTicketAbierto();
			RequestContext.getCurrentInstance().execute("jsArticulos.initArrayArt(" + String.valueOf(getAdminOrden().getArticulos().size()-1) + ");");
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
			actual= new Date(Calendar.getInstance().getTimeInMillis());
			if(actual.after(((TicketVenta)getAdminOrden().getOrden()).getVigencia()))
				generateNewVenta();
    	this.attrs.put("sinIva", this.getAdminOrden().getIdSinIva().equals(1L));
			this.attrs.put("consecutivo", ((TicketVenta)this.getAdminOrden().getOrden()).getConsecutivo());
			toLoadCatalog();
			doAsignaClienteTicketAbierto();
			RequestContext.getCurrentInstance().execute("jsArticulos.initArrayArt(" + String.valueOf(getAdminOrden().getArticulos().size()-1) + ");");
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
	} // doAsignaTicketAbierto
	
	private void generateNewVenta() throws Exception{
		IAdminArticulos adminTicketPivote= null;
		try {
			adminTicketPivote= getAdminOrden();
			setAdminOrden(new AdminTickets(new TicketVenta(-1L)));
			((TicketVenta)getAdminOrden().getOrden()).setIkProveedor(((TicketVenta)adminTicketPivote.getOrden()).getIkCliente());
			((TicketVenta)getAdminOrden().getOrden()).setIkAlmacen(((TicketVenta)adminTicketPivote.getOrden()).getIkAlmacen());
			for(Articulo addArticulo : adminTicketPivote.getArticulos()){
				if(addArticulo.isValid())
					toMoveDataArt(addArticulo, -1);			
			}	// for
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
		try {
			motorBusqueda= new MotorBusqueda(-1L, ((TicketVenta)this.getAdminOrden().getOrden()).getIdCliente());
			seleccion= new UISelectEntity(motorBusqueda.toCliente());
			clientesSeleccion= new ArrayList<>();
			clientesSeleccion.add(seleccion);
			clientesSeleccion.add(0, new UISelectEntity(motorBusqueda.toClienteDefault()));
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
						beanArticulo.setValor((Double) articulo.toValue(getPrecio()));
						beanArticulo.setCosto((Double) articulo.toValue(getPrecio()));
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
					RequestContext.getCurrentInstance().update("@(.filas) @(.recalculo) @(.informacion)");
				} // if
			} // if			
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
	} // doReCalculatePreciosArticulos
	
	private void doLoadSaldos(Long idCliente) throws Exception{
		Entity cliente     = null;
		MotorBusqueda motor= null;
		this.saldoCliente  = null;
		try {
			motor= new MotorBusqueda(null, idCliente);
			cliente= motor.toCliente();
			this.saldoCliente= new SaldoCliente();
			this.saldoCliente.setIdCliente(idCliente);
			this.saldoCliente.setTotalCredito(cliente.toDouble("limiteCredito"));
			this.saldoCliente.setTotalDeuda(motor.toDeudaCliente());
			this.saldoCliente.setTotalVenta(getAdminOrden().getTotales().getTotal());
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
		} // try
		catch (Exception e) {			
			throw e;			
		} // catch		
		return regresar;
	} // toDescuentoVigente
	
	public void doAplicarDescuento(){
		doAplicarDescuento(-1);
	} // doAplicarDescuento
	
	public void doAplicarDescuento(Integer index){
		Boolean isIndividual       = false;
		CambioUsuario cambioUsuario= null;
		String cuenta              = null;
		String contrasenia         = null;
		Double global              = 0D;
		try {
			if(!getAdminOrden().getArticulos().isEmpty()){
				cuenta= this.attrs.get("usuarioDescuento").toString();
				contrasenia= this.attrs.get("passwordDescuento").toString();
				cambioUsuario= new CambioUsuario(cuenta, contrasenia);
				if(cambioUsuario.validaPrivilegiosDescuentos()){
					isIndividual= Boolean.valueOf(this.attrs.get("isIndividual").toString());
					if(isIndividual){
						getAdminOrden().getArticulos().get(index).setDescuento(this.attrs.get("descuentoIndividual").toString());
						if(getAdminOrden().getArticulos().get(index).autorizedDiscount())
							RequestContext.getCurrentInstance().execute("jsArticulos.divDiscount('".concat(this.attrs.get("descuentoIndividual").toString()).concat("');"));
						else
							JsfBase.addMessage("No es posble aplicar el descuento, el descuento es superior a la utilidad", ETipoMensaje.ERROR);
					} // if
					else{		
						global= Double.valueOf(this.attrs.get("descuentoGlobal").toString());
						getAdminOrden().toCalculate();
						if(global < getAdminOrden().getTotales().getUtilidad()){
							getAdminOrden().getTotales().setGlobal(global);							
							getAdminOrden().toCalculate();
						} // if
						else
							JsfBase.addMessage("No es posble aplicar el descuento, el descuento es superior a la utilidad", ETipoMensaje.ERROR);
					} // else
				} // if
				else
					JsfBase.addMessage("El usuario no tiene privilegios o el usuario y la contraseña son incorrectos", ETipoMensaje.ERROR);
			} // if
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
		finally{			
			this.attrs.put("isIndividual", true);
			this.attrs.put("descuentoIndividual", 0);
			this.attrs.put("descuentoGlobal", 0);
			this.attrs.put("tipoDescuento", INDIVIDUAL);
			this.attrs.put("usuarioDescuento", "");
			this.attrs.put("passwordDescuento", "");
		} // finally
	} // doAplicarDescuento
	
	public void doVerificaVigenciaCotizacion(){
		MotorBusqueda motorBusqueda= null;		
		try {
			motorBusqueda= new MotorBusqueda(-1L);
			this.attrs.put("expirada", motorBusqueda.doVerificaVigenciaCotizacion(Long.valueOf(this.attrs.get("cotizacion").toString())));
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
	} // doVerificaVigenciaCotizacion
	
	public void doActivarDescuento(){
		String tipoDescuento= null;		
		try {
			tipoDescuento= this.attrs.get("tipoDescuento").toString();
			this.attrs.put("isIndividual", tipoDescuento.equals(INDIVIDUAL));
			this.attrs.put(tipoDescuento.equals(INDIVIDUAL) ? "descuentoGlobal" : "descuentoIndividual", 0);
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
				if(temporal.getCantidad()< 1D)					
					temporal.setCantidad(1D);
				temporal.setUltimo(this.attrs.get("ultimo")!= null);
				temporal.setSolicitado(this.attrs.get("solicitado")!= null);
				temporal.setUnidadMedida(articulo.toString("unidadMedida"));
				temporal.setPrecio(articulo.toDouble("precio"));				
				Value stock= (Value)DaoFactory.getInstance().toField("TcManticInventariosDto", "stock", params, "stock");
				temporal.setStock(stock== null? 0D: stock.toDouble());
				if(index== getAdminOrden().getArticulos().size()- 1) {
					getAdminOrden().getArticulos().add(new ArticuloVenta(-1L));
					RequestContext.getCurrentInstance().execute("jsArticulos.update("+ (getAdminOrden().getArticulos().size()- 1)+ ");");
				} // if	
				RequestContext.getCurrentInstance().execute("jsArticulos.callback('"+ articulo.toMap()+ "');");
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
	public void toLoadFaltantes() {
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
}
