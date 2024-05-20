package mx.org.kaana.mantic.ventas.comun;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.pagina.IBaseFilter;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.mantic.enums.EEstatusVentas;
import mx.org.kaana.mantic.enums.ETipoMediosPago;
import mx.org.kaana.mantic.ventas.beans.TicketVenta;
import mx.org.kaana.mantic.ventas.caja.beans.Pago;
import mx.org.kaana.mantic.ventas.caja.reglas.CreateTicket;
import mx.org.kaana.mantic.ventas.reglas.AdminTickets;

public abstract class IBaseTicket extends IBaseFilter implements Serializable {
	
	private static final long serialVersionUID = -2088985265691847994L;
	
	public void doTicket() {
		Entity seleccionado      = (Entity) this.attrs.get("seleccionado");
		Map<String, Object>params= new HashMap<>();
		CreateTicket ticket      = null;
		AdminTickets adminTicket = null;
    String       cliente     = "";
		try {			
			params.put("idVenta", seleccionado.toLong("idVenta"));
      if(seleccionado.containsKey("cliente") && !Objects.equals(Constantes.VENTA_AL_PUBLICO_GENERAL, seleccionado.toString("cliente"))) 
        cliente= seleccionado.toString("cliente");
			adminTicket= new AdminTickets((TicketVenta)DaoFactory.getInstance().toEntity(TicketVenta.class, "TcManticVentasDto", "detalle", params));			
			ticket= new CreateTicket(adminTicket, this.toPago(adminTicket, seleccionado.toLong("idVenta")), this.toTipoTransaccion(seleccionado.toLong("idVentaEstatus")), cliente);
			UIBackingUtilities.execute("jsTicket.imprimirTicket('" + ticket.getPrincipal().getClave()  + "-" + toConsecutivoTicket(seleccionado.toLong("idVentaEstatus"), adminTicket) + "','" + ticket.toHtml() + "');");
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);
		} // catch		
	} // doTicket
	
	private String toTipoTransaccion(Long idEstatus) {
		String regresar       = "VENTA DE MOSTRADOR";
		EEstatusVentas estatus= null;		
		try {
			if(idEstatus<= EEstatusVentas.EN_CAPTURA.getIdEstatusVenta() || idEstatus.equals(EEstatusVentas.TIMBRADA.getIdEstatusVenta())) {
				estatus= EEstatusVentas.fromIdTipoPago(idEstatus);
				switch(estatus) {
					case PAGADA:
					case TIMBRADA:
					case TERMINADA:
						regresar= "VENTA DE MOSTRADOR";
					break;
					case COTIZACION:
						regresar= "COTIZACIÓN";
						break;
					case APARTADOS:
						regresar= "APARTADO";
						break;
					case CREDITO:
						regresar= "VENTA A CREDITO";
						break;
				} // switch			
			} // if			
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toTipoTransaccion
	
	private String toConsecutivoTicket(Long idEstatus, AdminTickets ticket) {
		String regresar= null;		
		try {
			if(idEstatus.equals(EEstatusVentas.COTIZACION.getIdEstatusVenta()))
				regresar= ((TicketVenta)(ticket.getOrden())).getCotizacion();							
			else
				regresar= ((TicketVenta)(ticket.getOrden())).getTicket();			
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toTipoTransaccion
	
	private Pago toPago(AdminTickets adminTicket, Long idVenta) throws Exception {
		Pago regresar            = null;
		List<Entity> detallePago = null;
		Map<String, Object>params= new HashMap<>();
		ETipoMediosPago medioPago= null;
    EEstatusVentas venta     = EEstatusVentas.ABIERTA;
		try {
			regresar= new Pago(adminTicket.getTotales());
			params.put("idVenta", idVenta);
      // SON LOS ABONOS QUE SE HAN REALIZADO POR EL CLIENTE SI ES UN APARTADO
      detallePago= DaoFactory.getInstance().toEntitySet("VistaApartadosDto", "abonos", params, Constantes.SQL_TODOS_REGISTROS);
      if(detallePago!= null && !detallePago.isEmpty()) {
        for (Entity pago : detallePago) {
          medioPago= ETipoMediosPago.fromIdTipoPago(pago.toLong("idTipoMedioPago"));
          if(ETipoMediosPago.EFECTIVO.equals(medioPago))
            regresar.addAbono(pago.toDouble("importe"), pago.toTimestamp("registro"), pago.toString("referencia"));
          else
            regresar.addAbono(pago.toDouble("importe"), pago.toTimestamp("registro"), "(REF "+ pago.toString("referencia")+ ")");
        } // for
        venta= EEstatusVentas.APARTADOS;
      } // if
      switch (venta) {
        case APARTADOS:
          break;
        default:  
    			detallePago= DaoFactory.getInstance().toEntitySet("TrManticVentaMedioPagoDto", "ticket", params, Constantes.SQL_TODOS_REGISTROS);
      } // switch
			if(!detallePago.isEmpty()) {
				for(Entity pago: detallePago) {
					medioPago= ETipoMediosPago.fromIdTipoPago(pago.toLong("idTipoMedioPago"));
					switch(medioPago) {
						case EFECTIVO:
							regresar.setEfectivo(regresar.getEfectivo()+ pago.toDouble("importe"));							
							break;
						case TARJETA_DEBITO:
							regresar.setDebito(regresar.getDebito()+ pago.toDouble("importe"));							
							regresar.setReferenciaDebito(pago.toString("referencia"));							
							break;
						case TARJETA_CREDITO:
							regresar.setCredito(regresar.getCredito()+ pago.toDouble("importe"));							
							regresar.setReferenciaCredito(pago.toString("referencia"));							
							break;
						case CHEQUE:
							regresar.setCheque(regresar.getCheque()+ pago.toDouble("importe"));							
							regresar.setReferenciaCheque(pago.toString("referencia"));							
							break;
						case TRANSFERENCIA:
							regresar.setTransferencia(regresar.getTransferencia()+ pago.toDouble("importe"));							
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
	} 
  
}