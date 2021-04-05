package mx.org.kaana.mantic.ventas.garantias.backing;

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
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cifrar;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.compras.ordenes.beans.Articulo;
import mx.org.kaana.mantic.ventas.caja.reglas.Transaccion;
import mx.org.kaana.mantic.compras.ordenes.beans.Totales;
import mx.org.kaana.mantic.db.dto.TcManticClientesDto;
import mx.org.kaana.mantic.db.dto.TcManticFacturasDto;
import mx.org.kaana.mantic.db.dto.TcManticVentasDto;
import mx.org.kaana.mantic.enums.ETipoMediosPago;
import mx.org.kaana.mantic.ventas.beans.ArticuloVenta;
import mx.org.kaana.mantic.ventas.beans.TicketVenta;
import mx.org.kaana.mantic.ventas.caja.beans.Facturacion;
import mx.org.kaana.mantic.ventas.caja.beans.Pago;
import mx.org.kaana.mantic.ventas.caja.reglas.CreateTicket;
import mx.org.kaana.mantic.ventas.reglas.AdminTickets;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 2/04/2021
 *@time 12:35:18 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

@Named(value= "manticVentasGarantiasCancela")
@ViewScoped
public class Cancela extends IBaseAttribute implements Serializable {

  private static final long serialVersionUID = -317757402208690362L;

  private TcManticVentasDto venta;
  private TcManticFacturasDto documento;
  private TcManticClientesDto cliente;
  private TcManticVentasDto ticket;
  private List<ArticuloVenta> articulos;
	private Totales totales;
  
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
  
	@PostConstruct
  @Override
  protected void init() {
    try {      
      Long idVenta = JsfBase.getParametro("xyz")!= null? new Long(Cifrar.descifrar(JsfBase.getParametro("xyz"))): -1L;
      Long idCierre= JsfBase.getParametro("zyx")!= null? new Long(Cifrar.descifrar(JsfBase.getParametro("zyx"))): -1L;
      if(idVenta== -1L || idCierre== -1L)
				UIBackingUtilities.execute("janal.isPostBack('cancelar')");
      this.totales  = new Totales();
      this.venta    = (TcManticVentasDto)DaoFactory.getInstance().findById(TcManticVentasDto.class, idVenta);
      this.documento= (TcManticFacturasDto)DaoFactory.getInstance().findById(TcManticFacturasDto.class, venta.getIdFactura());
      this.cliente  = (TcManticClientesDto)DaoFactory.getInstance().findById(TcManticClientesDto.class, venta.getIdCliente());
      this.attrs.put("articulos", 0D);
      this.attrs.put("idCierre", idCierre);
      this.toClonVenta(idVenta);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);      
    } // catch	
  }
  
	public String doCancelar() {     	
    return "/Paginas/Mantic/Ventas/Caja/accion".concat(Constantes.REDIRECIONAR);
  } // doCancelar
  
  public String doAceptar() {    
    Transaccion transaccion= null;
    CreateTicket voucher   = null;
    List<Articulo>items    = new ArrayList<>();
    Map<String, Object> params= null;
    try {      
      params = new HashMap<>();      
      for (ArticuloVenta item: this.articulos) {
        ArticuloVenta clon= (ArticuloVenta)item.clone();
        items.add((Articulo)clon);
      } // for
      transaccion= new Transaccion(this.venta.getIdVenta(), this.documento, this.venta.getIdCliente(), (IBaseDto)this.ticket, items, (Long)this.attrs.get("idCierre"));
      if (transaccion.ejecutar(EAccion.COPIAR)) {
        params.put("idVenta", this.ticket.getIdVenta());     
        AdminTickets comprobante= new AdminTickets((TicketVenta)DaoFactory.getInstance().toEntity(TicketVenta.class, "TcManticVentasDto", "detalle", params));
        voucher= new CreateTicket(comprobante, this.toPago(comprobante, this.ticket.getIdVenta()), "FACTURA", this.cliente.getRazonSocial());
        UIBackingUtilities.execute("jsTicket.imprimirTicket('" + voucher.getPrincipal().getClave()  + "-" + this.ticket.getTicket() + "','" + voucher.toHtml() + "');");
        UIBackingUtilities.execute("jsTicket.process('"+ JsfBase.getContext()+ "/Paginas/Mantic/Ventas/Caja/accion.jsf');");
        JsfBase.addMessage("Se finalizó la refacturación del ticket de venta", ETipoMensaje.INFORMACION);
      } // if
      else 
        JsfBase.addMessage("Ocurrió un error al intentar refacturar el ticket de venta", ETipoMensaje.ERROR);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);      
    } // catch	
    finally {
      Methods.clean(items);
      Methods.clean(params);
    } // finally 
    return "/Paginas/Mantic/Ventas/Caja/accion".concat(Constantes.REDIRECIONAR);
  }

  private void toClonVenta(Long idVenta) {
    Map<String, Object> params = null;
    try {      
      params = new HashMap<>();      
      params.put("idVenta", idVenta);     
      this.ticket   = (TcManticVentasDto)this.venta.clone();
      this.articulos= (List<ArticuloVenta>)DaoFactory.getInstance().toEntitySet(ArticuloVenta.class, "VistaTcManticGarantiasArticulosDto", "detalle", params);
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

	private Pago toPago(AdminTickets adminTicket, Long idVenta) throws Exception {
		Pago regresar            = null;
		List<Entity> detallePago = null;
		Map<String, Object>params= null;
		ETipoMediosPago medioPago= null;
		try {
			regresar= new Pago(adminTicket.getTotales());
			params= new HashMap<>();
			params.put("idVenta", idVenta);
			detallePago= DaoFactory.getInstance().toEntitySet("TrManticVentaMedioPagoDto", "ticket", params, Constantes.SQL_TODOS_REGISTROS);
			if(!detallePago.isEmpty()) {
				for(Entity pago: detallePago) {
					medioPago= ETipoMediosPago.fromIdTipoPago(pago.toLong("idTipoMedioPago"));
					switch(medioPago) {
						case EFECTIVO:
							regresar.setEfectivo(pago.toDouble("importe"));							
							break;
						case TARJETA_CREDITO:
							regresar.setCredito(pago.toDouble("importe"));							
							regresar.setReferenciaCredito(pago.toString("referencia"));							
							break;
						case TARJETA_DEBITO:
							regresar.setDebito(pago.toDouble("importe"));							
							regresar.setReferenciaDebito(pago.toString("referencia"));							
							break;
						case CHEQUE:
							regresar.setCheque(pago.toDouble("importe"));							
							regresar.setReferenciaCheque(pago.toString("referencia"));							
							break;
						case TRANSFERENCIA:
							regresar.setTransferencia(pago.toDouble("importe"));							
							regresar.setReferenciaTransferencia(pago.toString("referencia"));							
							break;
					} // switch
				} // for
			} // if
		} // try
		catch (Exception e) {			
			throw e; 
		} // catch		
		return regresar;
	} // toPago

}
