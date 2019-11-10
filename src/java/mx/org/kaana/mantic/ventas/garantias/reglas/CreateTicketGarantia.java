package mx.org.kaana.mantic.ventas.garantias.reglas;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.enums.ETiposContactos;
import mx.org.kaana.mantic.ventas.beans.ArticuloVenta;
import mx.org.kaana.mantic.ventas.beans.TicketVenta;
import mx.org.kaana.mantic.ventas.caja.beans.Pago;
import mx.org.kaana.mantic.ventas.caja.reglas.CreateTicket;
import mx.org.kaana.mantic.ventas.garantias.beans.Garantia;

public class CreateTicketGarantia extends CreateTicket{
	
	private TicketVenta ticket;		
	private Garantia garantia;
	private List<ArticuloVenta> articulos;
	private boolean isGarantia;

	public CreateTicketGarantia(Pago pago, String tipo, boolean isGarantia) {
		super(pago, tipo);
		super.init();
		this.isGarantia= isGarantia;
	} // CreateTicketGarantia

	public TicketVenta getTicket() {
		return ticket;
	}

	public void setTicket(TicketVenta ticket) {
		this.ticket = ticket;
	}			

	public Garantia getGarantia() {
		return garantia;
	}

	public void setGarantia(Garantia garantia) {
		this.garantia = garantia;
	}	
	
	@Override
	public String toHtml() throws Exception{
		StringBuilder sb= new StringBuilder();		
		this.pago= this.garantia.getTotales();
		this.ticket= this.garantia.getGarantia();		
		this.articulos= this.garantia.getArticulosGarantia();
		sb.append(toHeader());
		sb.append(toBlackBar());		
		sb.append(toNoTicket());
		sb.append(toTipoTransaccion());
		sb.append(toFecha());
		sb.append(toTable());			
		sb.append(toHeaderTable());
		sb.append(toFinishTable());
		sb.append(toArticulos());
		sb.append(toPagos());
		sb.append(toFinishTable());	
		sb.append(toVendedor());
		sb.append(toCajero());
		sb.append(toFooter());							
		return sb.toString();
	} // toHtml
	
	@Override
	protected String toTipoTransaccion(){
		StringBuilder regresar= new StringBuilder();
		regresar.append(this.isGarantia ? "GARANTIA" : this.tipo).append("<br>");		
		return regresar.toString();
	} // toTipoVenta
	
	@Override
	protected String toFindDomicilio() throws Exception{
		Entity domicilio         = null;
		String regresar          = null;
		Map<String, Object>params= null;
		try {
			params= new HashMap<>();
			params.put("idEmpresa", this.ticket.getIdEmpresa());
			domicilio= (Entity) DaoFactory.getInstance().toEntity("VistaInformacionEmpresas", "datosEmpresa", params);
			regresar= domicilio.toString("empresaDireccion").concat(" C.P. ").concat(domicilio.toString("codigoPostal")).concat("<br> COLONIA. ").concat(domicilio.toString("colonia")).concat("<br> TEL.").concat(toTelefono());
		} // try
		finally{
			Methods.clean(params);
		} // finally
		return regresar;
	} // toFindDomicilio
	
	private String toTelefono() throws Exception{
		String regresar          = "";
		Map<String, Object>params= null;
		Entity telefono          = null;
		try {
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, "id_empresa=" + this.ticket.getIdEmpresa() + " and id_tipo_contacto=" + ETiposContactos.TELEFONO.getKey());
			telefono= (Entity) DaoFactory.getInstance().toEntity("TrManticEmpresaTipoContactoDto", "row", params);
			if(telefono!= null)
				regresar= telefono.toString("valor");
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
		return regresar;
	} // toTelefono
	
	private String toNoTicket(){		
		StringBuilder	regresar= new StringBuilder();
		String descripcionTicket= this.ticket.getTicket();
		regresar.append("<p style=\"width: 290px;text-align: center;align-content: center;font-family: sans-serif;font-size: 15px;font-weight: bold\">");
		regresar.append(this.isGarantia ? "GARANTIA: " : "DEVOLUCION: ");
		regresar.append(this.principal.getClave()).append("-").append(descripcionTicket).append("<br>");		
		return regresar.toString();
	} // toNoTicket
	
	@Override
	protected String toFecha(){
		StringBuilder regresar= new StringBuilder();
		regresar.append("Fecha:").append(Fecha.formatear(Fecha.FECHA_HORA_CORTA, this.ticket.getCobro()));		
		regresar.append("</p>");		
		return regresar.toString();
	} // toFecha
	
	private String toArticulos(){				
		StringBuilder regresar= new StringBuilder();			
		for(ArticuloVenta articulo : this.articulos){
			if(articulo.isValid() && articulo.getCantidad() > 0D) {				
				regresar.append(toTable());
				regresar.append("<tbody>");
				regresar.append("<tr style=\"border-top: 1px solid black;border-collapse: collapse;\">");
				regresar.append("<td style=\"font-family: sans-serif;font-size: 12px;width: 80px; max-width: 80px;border-top: 1px solid black;border-collapse: collapse;\">").append(articulo.getNombre().length()> 35 ? articulo.getNombre().substring(0, 35) : articulo.getNombre()).append("</td>");
				regresar.append("</tr>");
				regresar.append("</tbody>");
				regresar.append(toFinishTable());
				regresar.append("<table style=\"width: 290px;\">");
				regresar.append("<tbody>");
				regresar.append("<tr>");
				regresar.append("<td style=\"font-family: sans-serif;font-size: 12px;width: 80px;max-width: 80px;\">").append("</td>");
				regresar.append("<td style=\"font-family: sans-serif;font-size: 12px;width: 35px;max-width: 35px;word-break: break-all;text-align: center\">").append(articulo.getCantidad()).append("</td>");
				regresar.append("<td style=\"font-family: sans-serif;font-size: 12px;width: 35px;max-width: 35px;word-break: break-all;text-align: right\">").append(Numero.formatear(Numero.NUMERO_CON_DECIMALES, articulo.getCosto())).append("</td>");
				regresar.append("<td style=\"font-family: sans-serif;font-size: 12px;width: 55px;max-width: 55px;word-break: break-all;padding-right: 10px;text-align: right\">").append(articulo.getImporte()).append("</td>");
				regresar.append("</tr>");
				regresar.append("</tbody>");
				regresar.append(toFinishTable());
			} // if
		} // for
		regresar.append(toTable());
		regresar.append("<tbody>");
		regresar.append("<tr style=\"height: 15px;border-top: 1px solid black;border-collapse: collapse;\"><td></td><td></td><td></td><td></td></tr>");			
		regresar.append("</tbody>");			
		return regresar.toString();
	} // toArticulos
	
	private String toPagos(){
		StringBuilder regresar= new StringBuilder();
		regresar.append("<table style=\"width: 290px;\">");
		regresar.append("<tbody>");
		regresar.append("<tr style=\"border-collapse: collapse;\">");						
		regresar.append("<td style=\"font-family: sans-serif;font-size: 12px;width: 220px;max-width: 220px;word-break: break-all;border-collapse: collapse;text-align: right;\">SUBTOTAL:</td>");			
		regresar.append("<td style=\"font-family: sans-serif;font-size: 12px;width: 70px;max-width: 70px;word-break: break-all;border-collapse: collapse;text-align: right\">").append(this.pago.getTotales().getSubTotalDosDecimales$()).append("</td>");			
		regresar.append("</tr>");			
		regresar.append("<tr style=\"border-collapse: collapse;\">");						
		regresar.append("<td style=\"font-family: sans-serif;font-size: 12px;width: 220px;max-width: 220px;word-break: break-all;border-collapse: collapse;text-align: right;\">IVA:</td>");			
		regresar.append("<td style=\"font-family: sans-serif;font-size: 12px;width: 70px;max-width: 70px;word-break: break-all;border-collapse: collapse;text-align: right\">").append(this.pago.getTotales().getIvaDosDecimales$()).append("</td>");			
		regresar.append("</tr>");			
		regresar.append("<tr style=\"border-collapse: collapse;\">");						
		regresar.append("<td style=\"font-family: sans-serif;font-size: 14px;width: 220px;max-width: 220px;word-break: break-all;border-collapse: collapse;text-align: right;font-weight: bold;\">TOTAL:</td>");			
		regresar.append("<td style=\"font-family: sans-serif;font-size: 14px;width: 70px;max-width: 70px;word-break: break-all;border-collapse: collapse;text-align: right;font-weight: bold;\">$").append(this.pago.getTotales().getTotalDosDecimales$()).append("</td>");			
		regresar.append("</tr>");			
		regresar.append("<tr style=\"height: 15px;\"><td></td><td></td><td></td><td></td></tr>");	
		if(this.tipo.equals("APARTADO")){			
			regresar.append("<tr style=\"border-collapse: collapse;\">");				
			regresar.append("<td style=\"font-family: sans-serif;font-size: 14px;width: 220px;max-width: 220px;word-break: break-all;border-collapse: collapse;text-align: right;font-weight: bold;\">ABONO:</td>");			
			regresar.append("<td style=\"font-family: sans-serif;font-size: 12px;width: 70px;max-width: 70px;word-break: break-all;border-collapse: collapse;text-align: right;\">").append("</td>");
			regresar.append("</tr>");
		} // if
		if(!this.tipo.equals("APARTADO") && this.pago.getAbono() > 0){			
			regresar.append("<tr style=\"border-collapse: collapse;\">");				
			regresar.append("<td style=\"font-family: sans-serif;font-size: 14px;width: 220px;max-width: 220px;word-break: break-all;border-collapse: collapse;text-align: right;\">ABONO:</td>");			
			regresar.append("<td style=\"font-family: sans-serif;font-size: 12px;width: 70px;max-width: 70px;word-break: break-all;border-collapse: collapse;text-align: right;\">").append(this.pago.getAbono()).append("</td>");
			regresar.append("</tr>");
		} // if
		if(this.pago.getEfectivo() > 0){			
			regresar.append("<tr style=\"border-collapse: collapse;\">");				
			regresar.append("<td style=\"font-family: sans-serif;font-size: 12px;width: 220px;max-width: 220px;word-break: break-all;border-collapse: collapse;text-align: right;\">EFECTIVO:</td>");
			regresar.append("<td style=\"font-family: sans-serif;font-size: 12px;width: 70px;max-width: 70px;word-break: break-all;border-collapse: collapse;text-align: right;\">").append(this.pago.getEfectivo()).append("</td>");
			regresar.append("</tr>");
		} // if
		if(this.pago.getDebito()> 0){			
			regresar.append("<tr style=\"border-collapse: collapse;\">");
			regresar.append("<td style=\"font-family: sans-serif;font-size: 12px;width: 220px;max-width: 220px;word-break: break-all;border-collapse: collapse;text-align: right;\">DEBITO (REF ").append(this.pago.getReferenciaDebito()).append("):").append("</td>");
			regresar.append("<td style=\"font-family: sans-serif;font-size: 12px;width: 70px;max-width: 70px;word-break: break-all;border-collapse: collapse;text-align: right;\">").append(this.pago.getDebito()).append("</td>");
			regresar.append("</tr>");
		} // if
		if(this.pago.getCredito()> 0){			
			regresar.append("<tr style=\"border-collapse: collapse;\">");
			regresar.append("<td style=\"font-family: sans-serif;font-size: 12px;width: 220px;max-width: 220px;word-break: break-all;border-collapse: collapse;text-align: right;\">CREDITO (REF ").append(this.pago.getReferenciaCredito()).append("):").append("</td>");
			regresar.append("<td style=\"font-family: sans-serif;font-size: 12px;width: 70px;max-width: 70px;word-break: break-all;border-collapse: collapse;text-align: right;\">").append(this.pago.getCredito()).append("</td>");
			regresar.append("</tr>");
		} // if
		if(this.pago.getTransferencia()> 0){			
			regresar.append("<tr style=\"border-collapse: collapse;\">");
			regresar.append("<td style=\"font-family: sans-serif;font-size: 12px;width: 220px;max-width: 220px;word-break: break-all;border-collapse: collapse;text-align: right;\">TRANSFERENCIA (REF ").append(this.pago.getReferenciaTransferencia()).append("):").append("</td>");
			regresar.append("<td style=\"font-family: sans-serif;font-size: 12px;width: 70px;max-width: 70px;word-break: break-all;border-collapse: collapse;text-align: right;\">").append(this.pago.getTransferencia()).append("</td>");
			regresar.append("</tr>");
		} // if
		if(this.pago.getCheque()> 0){			
			regresar.append("<tr style=\"border-collapse: collapse;\">");
			regresar.append("<td style=\"font-family: sans-serif;font-size: 12px;width: 220px;max-width: 220px;word-break: break-all;border-collapse: collapse;text-align: right;\">CHEQUE (REF ").append(this.pago.getReferenciaCheque()).append("):").append("</td>");
			regresar.append("<td style=\"font-family: sans-serif;font-size: 12px;width: 70px;max-width: 70px;word-break: break-all;border-collapse: collapse;text-align: right;\">").append(this.pago.getCheque()).append("</td>");
			regresar.append("</tr>");
		} // if
		regresar.append("<tr style=\"border-collapse: collapse;\">");
		regresar.append("<td style=\"font-family: sans-serif;font-size: 14px;width: 220px;max-width: 220px;word-break: break-all;border-collapse: collapse;text-align: right;font-weight: bold;\">CAMBIO:</td>");
		regresar.append("<td style=\"font-family: sans-serif;font-size: 14px;width: 70px;max-width: 70px;word-break: break-all;border-collapse: collapse;text-align: right;font-weight: bold;\">").append(this.pago.getCambio$()).append("</td>");
		regresar.append("</tr>");
		if(this.tipo.equals("APARTADO")){			
			regresar.append("<tr style=\"height: 15px;\"><td></td><td></td><td></td><td></td></tr>");	
			regresar.append("<tr style=\"border-collapse: collapse;\">");				
			regresar.append("<td style=\"font-family: sans-serif;font-size: 14px;width: 220px;max-width: 220px;word-break: break-all;border-collapse: collapse;text-align: right;font-weight: bold;\">RESTANTE:</td>");
			regresar.append("<td style=\"font-family: sans-serif;font-size: 14px;width: 70px;max-width: 70px;word-break: break-all;border-collapse: collapse;text-align: right;font-weight: bold;\">$").append(Numero.formatear(Numero.NUMERO_CON_DECIMALES, this.pago.getDifEfectivo())).append("</td>");
			regresar.append("</tr>");
		} // if
		regresar.append("</tbody></table>");					
		return regresar.toString();
	} // toPagos

	@Override
	public String toUsuario() throws Exception{
		String regresar          = null;
		Entity usuario           = null;
		Map<String, Object>params= null;
		try {
			params= new HashMap<>();
			params.put("idUsuario", this.ticket.getIdUsuario());
			usuario= (Entity) DaoFactory.getInstance().toEntity("VistaUsuariosDto", "perfilUsuario", params);
			regresar= usuario.toString("nombreCompleto");
		} // try
		finally{
			Methods.clean(params);
		} // finally
		return regresar;
	} // toUsuario
	
	@Override
	protected String toFooter(){
		StringBuilder regresar= new StringBuilder();
		String descripcion= "GRACIAS POR SU PREFERENCIA";							
		regresar.append("<p style=\"width: 290px;text-align: center;align-content: center;font-family: sans-serif;font-size: 14px;border-top: 1px solid black;border-collapse: collapse;\">");				
		regresar.append("<br/>¡");
		regresar.append(descripcion);
		regresar.append("!</p>");
		regresar.append("<p style=\"width: 290px;text-align: center;align-content: center;font-family: sans-serif;font-size: 10px;\">");
		regresar.append("PARA CUALQUIER ACLARACION, MANTENER SU TICKET");
		regresar.append("</p>");	
		regresar.append("</div>");		
		return regresar.toString();
	} // toFooter		
	
	@Override
	protected String toCajero(){
		StringBuilder regresar=  new StringBuilder();
		regresar.append("<strong>Autorizó:</strong>");
		regresar.append(JsfBase.getAutentifica().getPersona().getNombreCompleto());
		regresar.append("</p>");		
		return regresar.toString();
	} // toArticulos
}