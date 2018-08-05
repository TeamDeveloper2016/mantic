package mx.org.kaana.mantic.compras.requisiciones.backing;

import java.io.Serializable;
import java.util.Date;
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
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.ventas.reglas.MotorBusqueda;
import mx.org.kaana.mantic.compras.ordenes.beans.Articulo;
import mx.org.kaana.mantic.compras.requisiciones.benas.Requisicion;
import mx.org.kaana.mantic.ventas.beans.TicketVenta;
import mx.org.kaana.mantic.compras.requisiciones.reglas.Transaccion;
import mx.org.kaana.mantic.ventas.reglas.AdminTickets;
import mx.org.kaana.mantic.comun.IBaseArticulos;
import mx.org.kaana.mantic.db.dto.TcManticArticulosDto;
import mx.org.kaana.mantic.db.dto.TcManticRequisicionesDto;
import org.primefaces.context.RequestContext;

@Named(value= "manticComprasRequisicionesAccion")
@ViewScoped
public class Accion extends IBaseArticulos implements Serializable {

  private static final long serialVersionUID = 327393488565639367L;
	private Requisicion requisicion;

	public Accion() {
		super("menudeo");
	}

	public Requisicion getRequisicion() {
		return requisicion;
	}

	public void setRequisicion(Requisicion requisicion) {
		this.requisicion = requisicion;
	}
	
	@PostConstruct
  @Override
  protected void init() {		
    try {
      this.attrs.put("accion", JsfBase.getFlashAttribute("accion")== null? EAccion.AGREGAR: JsfBase.getFlashAttribute("accion"));
      this.attrs.put("idRequisicion", JsfBase.getFlashAttribute("idVenta")== null? -1L: JsfBase.getFlashAttribute("idRequisicion"));
			this.attrs.put("retorno", JsfBase.getFlashAttribute("retorno")== null? null: JsfBase.getFlashAttribute("retorno"));
      this.attrs.put("isPesos", false);
			this.attrs.put("sinIva", false);
			this.attrs.put("buscaPorCodigo", false);
			this.attrs.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
			this.attrs.put("isMatriz", JsfBase.isAdminEncuestaOrAdmin());					
			this.attrs.put("nombreEmpresa", JsfBase.getAutentifica().getEmpresa().getNombre());
			this.attrs.put("solicita", JsfBase.getAutentifica().getPersona().getNombreCompleto());
			doLoad();
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
					this.requisicion= new Requisicion();
					this.requisicion.setPedido(new Date(Calendar.getInstance().getTimeInMillis()));
					this.requisicion.setEntrega(new Date(Calendar.getInstance().getTimeInMillis()));
          this.setAdminOrden(new AdminTickets(new TicketVenta(-1L)));
          break;
        case MODIFICAR:			
        case CONSULTAR:	
					this.requisicion= (Requisicion) DaoFactory.getInstance().findById(TcManticRequisicionesDto.class, Long.valueOf(this.attrs.get("idRequisicion").toString()));
          this.setAdminOrden(new AdminTickets((TicketVenta)DaoFactory.getInstance().toEntity(TicketVenta.class, "TcManticVentasDto", "detalle", this.attrs)));				
          break;
      } // switch
			this.attrs.put("consecutivo", "");
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
			loadOrdenVenta();
			eaccion= (EAccion) this.attrs.get("accion");						
			transaccion = new Transaccion(this.requisicion, this.getAdminOrden().getArticulos());
			this.getAdminOrden().toAdjustArticulos();
			if (transaccion.ejecutar(eaccion)) {
				if(eaccion.equals(EAccion.AGREGAR)) {
 				  regresar = this.attrs.get("retorno")!= null ? this.attrs.get("retorno").toString().concat(Constantes.REDIRECIONAR) : null;
    			RequestContext.getCurrentInstance().execute("jsArticulos.back('gener\\u00F3 requisición', '"+ this.requisicion.getConsecutivo()+ "');");
					this.init();
				} // if	
				JsfBase.addMessage("Se ".concat(eaccion.equals(EAccion.AGREGAR) ? "agregó" : "modificó").concat(" el ticket de venta."), ETipoMensaje.INFORMACION);
  			JsfBase.setFlashAttribute("idRequisicion", this.requisicion.getIdRequisicion());
				RequestContext.getCurrentInstance().execute("userUpdate();");
			} // if
			else 
				JsfBase.addMessage("Ocurrió un error al registrar la requisición de compra.", ETipoMensaje.ERROR);      			
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    return regresar;
  } // doAccion

  public String doCancelar() {   
  	JsfBase.setFlashAttribute("idRequisicion", this.requisicion.getIdRequisicion());
    return this.attrs.get("retorno") != null ? (String)this.attrs.get("retorno") : "filtro";
  } // doCancelar			
	
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
							beanArticulo.setDescuento(descuento!= null ? descuento : sinDescuento);							
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

	@Override
	protected void toMoveData(UISelectEntity articulo, Integer index) throws Exception {
		UISelectEntity clienteSeleccion= null;		
		String descuentoPivote         = null;
		String descuentoVigente        = null;		
		try {
			clienteSeleccion= (UISelectEntity) this.attrs.get("clienteSeleccion");
			if(clienteSeleccion!= null && !clienteSeleccion.getKey().equals(-1L)){
				descuentoVigente= toDescuentoVigente(articulo.toLong("idArticulo"), clienteSeleccion.getKey());				
				if(descuentoVigente!= null){
					descuentoPivote= getAdminOrden().getDescuento();
					getAdminOrden().setDescuento(descuentoVigente);
					super.toMoveData(articulo, index);			
					getAdminOrden().setDescuento(descuentoPivote);
				} // if
				else
					super.toMoveData(articulo, index);				
			} // if
			else
				super.toMoveData(articulo, index);				
			RequestContext.getCurrentInstance().update("deudor");
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch	
	} // toMoveData
	
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
	
	private void loadOrdenVenta(){		
		UISelectEntity cliente = null;
		try {
			cliente= (UISelectEntity) this.attrs.get("clienteSeleccion");			
			((TicketVenta)this.getAdminOrden().getOrden()).setIdEmpresa(Long.valueOf(this.attrs.get("idEmpresa").toString()));
			((TicketVenta)this.getAdminOrden().getOrden()).setIdCliente(cliente.getKey());
			((TicketVenta)this.getAdminOrden().getOrden()).setDescuentos(this.getAdminOrden().getTotales().getDescuentos());
			((TicketVenta)this.getAdminOrden().getOrden()).setImpuestos(this.getAdminOrden().getTotales().getIva());
			((TicketVenta)this.getAdminOrden().getOrden()).setSubTotal(this.getAdminOrden().getTotales().getSubTotal());
			((TicketVenta)this.getAdminOrden().getOrden()).setTotal(this.getAdminOrden().getTotales().getTotal());
		} // try
		catch (Exception e) {		
			throw e;
		} // catch		
	} // loadOrdenVenta
	
	public void doDetailArticulo(Long idArticulo, Integer index) {
		MotorBusqueda motor= null;
		Entity detailArt   = null;
		try {
			if(idArticulo!= null){
				motor= new MotorBusqueda(idArticulo);
				detailArt= motor.toDetalleArticulo();
				this.attrs.put("detailArticulo", detailArt);
				RequestContext.getCurrentInstance().execute("PF('dlgDetalleArt').show();");
			} // if
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
	} // doDetailArticulo	
	
	@Override
	public void doUpdateArticulos() {
		List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
		boolean buscaPorCodigo    = false;
    try {
			columns= new ArrayList<>();
      columns.add(new Columna("propio", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
  		params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
  		params.put("idProveedor", this.attrs.get("proveedor")== null? new UISelectEntity(new Entity(-1L)): ((UISelectEntity)this.attrs.get("proveedor")).getKey());
			String search= (String)this.attrs.get("codigo"); 
			if(!Cadena.isVacio(search)) {
				buscaPorCodigo= search.startsWith(".");
				if(buscaPorCodigo)
					search= search.trim().substring(1);
				search= search.toUpperCase().replaceAll("(,| |\\t)+", ".*.*");
			} // if	
			else
				search= "WXYZ";
  		params.put("codigo", search);
			if((boolean)this.attrs.get("buscaPorCodigo") || buscaPorCodigo)
        this.attrs.put("articulos", (List<UISelectEntity>) UIEntity.buildImage("VistaOrdenesComprasDto", "porCodigo", params, columns, 20L));
			else
        this.attrs.put("articulos", (List<UISelectEntity>) UIEntity.buildImage("VistaOrdenesComprasDto", "porNombre", params, columns, 20L));
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
