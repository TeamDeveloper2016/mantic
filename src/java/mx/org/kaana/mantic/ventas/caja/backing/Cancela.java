package mx.org.kaana.mantic.ventas.caja.backing;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Cifrar;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.compras.ordenes.beans.Articulo;
import mx.org.kaana.mantic.ventas.caja.reglas.Transaccion;
import mx.org.kaana.mantic.compras.ordenes.beans.Totales;
import mx.org.kaana.mantic.comun.IAdminArticulos;
import mx.org.kaana.mantic.db.dto.TcManticCierresDto;
import mx.org.kaana.mantic.db.dto.TcManticClientesDto;
import mx.org.kaana.mantic.db.dto.TcManticFacturasDto;
import mx.org.kaana.mantic.db.dto.TcManticVentasDto;
import mx.org.kaana.mantic.ventas.beans.ArticuloVenta;
import mx.org.kaana.mantic.ventas.beans.TicketVenta;
import mx.org.kaana.mantic.ventas.caja.beans.Pago;
import mx.org.kaana.mantic.ventas.reglas.AdminTickets;
import mx.org.kaana.mantic.ventas.reglas.CambioUsuario;
import mx.org.kaana.mantic.ventas.reglas.MotorBusqueda;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 2/04/2021
 *@time 12:35:18 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

@Named(value= "manticVentasCajaCancela")
@ViewScoped
public class Cancela extends IBaseAttribute implements Serializable {

  private static final long serialVersionUID= -317757402208690361L;
	protected static final String INDIVIDUAL  = "1";

  private TcManticVentasDto venta;
  private TcManticFacturasDto documento;
  private TcManticClientesDto cliente;
  private TcManticVentasDto ticket;
  private List<ArticuloVenta> articulos;
	private Totales totales;
	private IAdminArticulos orden;
  
  public TcManticVentasDto getVenta() {
    return venta;
  }

  public TcManticFacturasDto getDocumento() {
    return documento;
  }

  public TcManticClientesDto getCliente() {
    return cliente;
  }

  public Totales getTotales() {
    return totales;
  }
  
  public List<ArticuloVenta> getArticulos() {
    return articulos;
  }
  
	@PostConstruct
  @Override
  protected void init() {
    try {      
      Long idVenta = JsfBase.getParametro("xyz")!= null? new Long(Cifrar.descifrar(JsfBase.getParametro("xyz"))): -1L;
      if(idVenta== -1L)
				UIBackingUtilities.execute("janal.isPostBack('cancelar')");
      this.totales  = new Totales();
      this.venta    = (TcManticVentasDto)DaoFactory.getInstance().findById(TcManticVentasDto.class, idVenta);
      if(!Cadena.isVacio(venta.getIdFactura()))
        this.documento= (TcManticFacturasDto)DaoFactory.getInstance().findById(TcManticFacturasDto.class, venta.getIdFactura());
      else
        this.documento= new TcManticFacturasDto();
      this.cliente  = (TcManticClientesDto)DaoFactory.getInstance().findById(TcManticClientesDto.class, venta.getIdCliente());
      this.attrs.put("articulos", 0D);
  		this.attrs.put("ok", Boolean.FALSE);
      this.toClonVenta(idVenta);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);      
    } // catch	
  }

	public String doCancelar() {
    return "/Paginas/Mantic/Ventas/Caja/filtro".concat(Constantes.REDIRECIONAR);
  } // doCancelar
  
  private void toClonVenta(Long idVenta) {
    Map<String, Object> params = null;
    try {      
      params = new HashMap<>();      
      params.put("idVenta", idVenta);     
      this.ticket   = (TcManticVentasDto)this.venta.clone();
      this.articulos= (List<ArticuloVenta>)DaoFactory.getInstance().toEntitySet(ArticuloVenta.class, "VistaGarantiasDto", "detalle", params);
      if(this.articulos!= null && !this.articulos.isEmpty()) {
        int count= 0;
        this.totales.reset();
        while(count< this.articulos.size()) {
          ArticuloVenta item= this.articulos.get(count);
          if(item.getCantidadGarantia().equals(0D))
            this.articulos.remove(count);
          else {
            item.setDescuentoAsignado(false);
            item.setViejosPrecios(true);
            item.setCantidad(item.getCantidadGarantia());
            item.toCalculate(this.ticket.getIdSinIva().equals(1L), this.ticket.getTipoDeCambio());
            ((Articulo)item).setModificado(true);
            this.totales.addArticulo((Articulo)item);
            count++;
          } // else
        } // while
        this.ticket.setIdVenta(-1L);
        this.ticket.setTotal(this.totales.getTotal());
        this.ticket.setImpuestos(this.totales.getIva());
        this.ticket.setSubTotal(this.totales.getSubTotal());
        this.ticket.setDescuentos(this.totales.getDescuentos());
        this.ticket.setUtilidad(this.totales.getUtilidad());
        this.ticket.setIdFactura(null);
        this.ticket.setIdFacturar(1L);
        this.ticket.setDia(new Date(Calendar.getInstance().getTimeInMillis()));
        this.ticket.setIdUsuario(JsfBase.getIdUsuario());
        this.ticket.setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
        this.ticket.setObservaciones((this.ticket.getObservaciones()!= null? "": this.ticket.getObservaciones().concat(", ")).concat("SE REFACTURO, TICKET ORIGINAL ["+ this.venta.getTicket()+"]"));
        this.attrs.put("articulos", new Double(this.articulos.size()));
      } // if
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);      
    } // catch	
    finally {
      Methods.clean(params);
    } // finally
  }

	public String doAceptar() {
		String regresar   = null;
		String cuenta     = (String)this.attrs.get("cuenta");
		String contrasenia= (String)this.attrs.get("contrasenia");
    Transaccion transaccion= null;
		try {
			CambioUsuario	usuario= new CambioUsuario(cuenta, contrasenia);			
			if(usuario.autorizaCancelacion()) {
        this.ticket.setObservaciones((String)this.attrs.get("justificacion"));
				this.attrs.put("cuenta", "");
				this.attrs.put("contrasenia", "");
				this.attrs.put("justificacion", "");
				this.attrs.put("ok", Boolean.FALSE);
        transaccion= new Transaccion(this.venta.getIdVenta(), this.documento, this.venta.getIdCliente(), (IBaseDto)this.ticket, new ArrayList<>(), (Long)this.toCierreCaja());
        if (transaccion.ejecutar(EAccion.RESTAURAR)) {
          String msg= "canceló ticket";
          if(this.documento.isValid())
            msg= "canceló ticket y factura";
          JsfBase.addMessage("Se ".concat(msg).concat(" con éxito"), ETipoMensaje.INFORMACION);
          UIBackingUtilities.execute("janal.back('"+ msg+ "', '"+ this.venta.getTicket()+ "');");
          regresar= "/Paginas/Mantic/Ventas/Caja/filtro".concat(Constantes.REDIRECIONAR);
        } // if
        else 
          JsfBase.addMessage("Ocurrió un error al intentar cancelar el ticket !", ETipoMensaje.ERROR);
				UIBackingUtilities.execute("PF('widgetDialogoAutorizacion').hide();");
			} // if
			else
				this.attrs.put("ok", Boolean.TRUE);
	  } // try
    catch (Exception e) {
      Error.mensaje(e);
			UIBackingUtilities.execute("PF('widgetDialogoAutorizacion').hide();");
      JsfBase.addMessageError(e);
    } // catch
		return regresar;
	}

  public void doUpdateCatalogos() {
		List<UISelectEntity> cajas = null;
    List<UISelectEntity> bancos= null;
    List<Columna> columns      = null;    
    Map<String, Object> params = null;
    try {      
      columns= new ArrayList<>();
      params = new HashMap<>();      
			params.put("idEmpresa", this.venta.getIdEmpresa());
			columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
			cajas=(List<UISelectEntity>) UIEntity.build("TcManticCajasDto", "cajas", params, columns);
			this.attrs.put("cajas", cajas);
			this.attrs.put("caja", cajas.get(0));
			params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
			columns.clear();
			columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
			columns.add(new Columna("razonSocial", EFormatoDinamicos.MAYUSCULAS));
			bancos= UIEntity.build("TcManticBancosDto", "row", params, columns, Constantes.SQL_TODOS_REGISTROS);
			this.attrs.put("bancos", bancos);
			this.attrs.put("banco", bancos.get(0));
			this.attrs.put("nombreCliente", this.cliente.getRazonSocial());
			this.attrs.put("referencia", "");
      params.clear();
      params.put("idVenta", this.venta.getIdVenta());
      this.orden= new AdminTickets((TicketVenta)DaoFactory.getInstance().toEntity(TicketVenta.class, "TcManticVentasDto", "detalle", params));
      this.attrs.put("pago", new Pago(this.orden.getTotales()));
			this.attrs.put("isEfectivo", true);			
			this.attrs.put("idVenta", this.venta.getIdVenta());
      this.loadMediosPago(this.venta.getIdVenta());
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);      
    } // catch	
    finally {
      Methods.clean(params);
      Methods.clean(columns);
    } // finally
  }  
  
  private Long toCierreCaja() {
    Long regresar             = -1L;
    Map<String, Object> params= null;
    try {      
			List<UISelectEntity> cajas= (List<UISelectEntity>)this.attrs.get("cajas");
      int index= cajas.indexOf((UISelectEntity)this.attrs.get("caja"));
      if(index>= 0)
			  this.attrs.put("caja", cajas.get(index));
      params = new HashMap<>();      
      params.put("estatusAbierto", "1, 2");
      params.put("idEmpresa", ((UISelectEntity)this.attrs.get("caja")).toLong("idEmpresa"));
      params.put("idCaja", ((UISelectEntity)this.attrs.get("caja")).getKey());			
      TcManticCierresDto cierre= (TcManticCierresDto) DaoFactory.getInstance().toEntity(TcManticCierresDto.class, "VistaCierresCajasDto", "cierreVigente", params);
      if(cierre!= null)
        regresar= cierre.getIdCierre();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);      
    } // catch	
    finally {
      Methods.clean(params);
    } // finally
    return regresar;
  }

  public void doActivarPago() {
		String tipoPago= null;				
		try {					
			tipoPago= this.attrs.get("tipoPago").toString();
			this.attrs.put("isEfectivo", tipoPago.equals(INDIVIDUAL));			
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
	} // doActivarPago
	
	private void loadMediosPago(Long idVenta){
		MotorBusqueda motor     = null;
		List<Entity> pagosVentas= null;
		try {
			motor= new MotorBusqueda(idVenta);
			pagosVentas= motor.pagosVenta();
			this.attrs.put("pagosVenta", pagosVentas);
			this.attrs.put("tipoPago", pagosVentas.get(0).toLong("idTipoMedioPago"));
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
	} // loadMediosPago
  
}
