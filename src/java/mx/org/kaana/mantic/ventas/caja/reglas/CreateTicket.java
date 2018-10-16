package mx.org.kaana.mantic.ventas.caja.reglas;

import java.util.HashMap;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.procesos.acceso.beans.Sucursal;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.mantic.compras.ordenes.beans.Articulo;
import mx.org.kaana.mantic.ventas.beans.ArticuloVenta;
import mx.org.kaana.mantic.ventas.beans.TicketVenta;
import mx.org.kaana.mantic.ventas.caja.beans.Pago;
import mx.org.kaana.mantic.ventas.reglas.AdminTickets;

public class CreateTicket {

	private AdminTickets ticket;
	private Pago pago;
	private Sucursal principal;
	private String tipoTransaccion;
	
	public CreateTicket(AdminTickets ticket, Pago pago, String tipoTransaccion) {
		this.ticket         = ticket;
		this.pago           = pago;
		this.tipoTransaccion= tipoTransaccion;
		init();
	}
	
	private void init(){		
		try {
			for(Sucursal sucursal: JsfBase.getAutentifica().getSucursales()){
				if(sucursal.isMatriz())
					this.principal= sucursal;
			} // for
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
	}

	public Sucursal getPrincipal() {
		return principal;
	}
	
	public String toHtml() throws Exception{
		StringBuilder sb= null;
		try {
			sb= new StringBuilder();
			sb.append(toHeader());
			sb.append(toBlackBar());
			sb.append(toDomicilio());
			sb.append(toNoTicket());
			sb.append(toTipoTransaccion());
			sb.append(toFecha());
			sb.append(toTable());
			sb.append(toHeaderTable());
			sb.append(toArticulos());
			sb.append(toPagos());
			sb.append(toFinishTable());
			sb.append(toVendedor());
			sb.append(toCajero());
			sb.append(toFooter());
		} // try
		catch (Exception e) {			
			throw e; 
		} // catch		
		return sb.toString();
	} // toHtml
	
	private String toHeader(){
		StringBuilder regresar= null;
		try {
			regresar= new StringBuilder("<div id=\"ticket\" style=\"width: 90px; max-width: 80px;\">");
			regresar.append("<table style=\"width: 200px;\"><tr>");
			regresar.append("<td><img src=\"data:image/svg+xml;base64,PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiIHN0YW5kYWxvbmU9Im5vIj8+CjxzdmcKICAgeG1sbnM6ZGM9Imh0dHA6Ly9wdXJsLm9yZy9kYy9lbGVtZW50cy8xLjEvIgogICB4bWxuczpjYz0iaHR0cDovL2NyZWF0aXZlY29tbW9ucy5vcmcvbnMjIgogICB4bWxuczpyZGY9Imh0dHA6Ly93d3cudzMub3JnLzE5OTkvMDIvMjItcmRmLXN5bnRheC1ucyMiCiAgIHhtbG5zOnN2Zz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciCiAgIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyIKICAgeG1sbnM6c29kaXBvZGk9Imh0dHA6Ly9zb2RpcG9kaS5zb3VyY2Vmb3JnZS5uZXQvRFREL3NvZGlwb2RpLTAuZHRkIgogICB4bWxuczppbmtzY2FwZT0iaHR0cDovL3d3dy5pbmtzY2FwZS5vcmcvbmFtZXNwYWNlcy9pbmtzY2FwZSIKICAgdmVyc2lvbj0iMS4wIgogICB3aWR0aD0iMzAyLjAwMDAwMHB0IgogICBoZWlnaHQ9IjUwNC4wMDAwMDBwdCIKICAgdmlld0JveD0iMCAwIDMwMi4wMDAwMDAgNTA0LjAwMDAwMCIKICAgcHJlc2VydmVBc3BlY3RSYXRpbz0ieE1pZFlNaWQgbWVldCIKICAgaWQ9InN2ZzIiCiAgIGlua3NjYXBlOnZlcnNpb249IjAuOTEgcjEzNzI1IgogICBzb2RpcG9kaTpkb2NuYW1lPSJib25hbnphLnN2ZyI+CiAgPGRlZnMKICAgICBpZD0iZGVmczI0IiAvPgogIDxzb2RpcG9kaTpuYW1lZHZpZXcKICAgICBwYWdlY29sb3I9IiNmZmZmZmYiCiAgICAgYm9yZGVyY29sb3I9IiM2NjY2NjYiCiAgICAgYm9yZGVyb3BhY2l0eT0iMSIKICAgICBvYmplY3R0b2xlcmFuY2U9IjEwIgogICAgIGdyaWR0b2xlcmFuY2U9IjEwIgogICAgIGd1aWRldG9sZXJhbmNlPSIxMCIKICAgICBpbmtzY2FwZTpwYWdlb3BhY2l0eT0iMCIKICAgICBpbmtzY2FwZTpwYWdlc2hhZG93PSIyIgogICAgIGlua3NjYXBlOndpbmRvdy13aWR0aD0iMTAzMCIKICAgICBpbmtzY2FwZTp3aW5kb3ctaGVpZ2h0PSI2NjYiCiAgICAgaWQ9Im5hbWVkdmlldzIyIgogICAgIHNob3dncmlkPSJmYWxzZSIKICAgICBpbmtzY2FwZTp6b29tPSIwLjM3NDYwMzE3IgogICAgIGlua3NjYXBlOmN4PSIxODguNzUiCiAgICAgaW5rc2NhcGU6Y3k9IjMxNSIKICAgICBpbmtzY2FwZTp3aW5kb3cteD0iMTQ2MyIKICAgICBpbmtzY2FwZTp3aW5kb3cteT0iMjAyIgogICAgIGlua3NjYXBlOndpbmRvdy1tYXhpbWl6ZWQ9IjAiCiAgICAgaW5rc2NhcGU6Y3VycmVudC1sYXllcj0ic3ZnMiIgLz4KICA8bWV0YWRhdGEKICAgICBpZD0ibWV0YWRhdGE0Ij4KQ3JlYXRlZCBieSBwb3RyYWNlIDEuMTUsIHdyaXR0ZW4gYnkgUGV0ZXIgU2VsaW5nZXIgMjAwMS0yMDE3CjxyZGY6UkRGPgogIDxjYzpXb3JrCiAgICAgcmRmOmFib3V0PSIiPgogICAgPGRjOmZvcm1hdD5pbWFnZS9zdmcreG1sPC9kYzpmb3JtYXQ+CiAgICA8ZGM6dHlwZQogICAgICAgcmRmOnJlc291cmNlPSJodHRwOi8vcHVybC5vcmcvZGMvZGNtaXR5cGUvU3RpbGxJbWFnZSIgLz4KICA8L2NjOldvcms+CjwvcmRmOlJERj4KPC9tZXRhZGF0YT4KICA8cGF0aAogICAgIHN0eWxlPSJmaWxsOiNmNDAwMDA7c3Ryb2tlOm5vbmU7ZmlsbC1vcGFjaXR5OjEiCiAgICAgaW5rc2NhcGU6Y29ubmVjdG9yLWN1cnZhdHVyZT0iMCIKICAgICBpZD0icGF0aDgiCiAgICAgZD0ibSAxNDYuMywxMy42IGMgLTI3LjIsNC42IC01MC42LDIyIC02My4yLDQ3LjEgLTQuMSw4LjIgLTcuOCwyMS41IC04LjcsMzAuOCBsIC0wLjcsNy41IDg4LjMsMCA4OC4zLDAgLTAuNywtNy41IEMgMjQ4LjcsODIuMiAyNDUsNjguOSAyNDAuOSw2MC43IDIyOC40LDM1LjkgMjAxLjksMTYuNCAxNzYuNSwxMy40IGwgLTUuNSwtMC43IDAsMTUuNyAwLDE1LjYgLTguNSwwIC04LjUsMCAtMC4yLC0xNS44IC0wLjMsLTE1LjggLTcuMiwxLjIgeiBtIDI3LjYsMzggYyAyLjEsMS43IDMuMSwzLjUgMy4xLDUuMyAwLDUuMyAtNC4yLDguOCAtMTQuMywxMS44IGwgLTUuMSwxLjUgMy44LDEuMyBjIDUuNiwyIDksNS42IDguMiw4LjkgLTEuMyw1LjIgLTEzLjEsMTAuNiAtMjMuMSwxMC42IC0xMCwwIC0xMy43LC02LjggLTcuNiwtMTQgbCAyLjgsLTMuNCAtMi4xLC0xLjggYyAtMi4xLC0xLjggLTIuMSwtMS44IC0wLjIsLTMuMyAxLjEsLTAuOCAzLjIsLTEuNSA0LjgsLTEuNSAxLjgsMCA0LjQsLTEuNSA3LjgsLTQuNSA1LjIsLTQuNiA5LC01LjggOSwtMi44IDAsMC45IC0wLjgsMi41IC0xLjcsMy41IC0xLjcsMS45IC0xLjYsMS45IDEuMiwxLjIgMy43LC0wLjggOS41LC00LjIgOS41LC01LjUgMCwtMC41IC0xLjEsLTEuNCAtMi40LC0xLjkgLTMuNiwtMS4zIC0xMi40LDAuNCAtMTkuOCwzLjggLTguNSw0IC0xMS43LDQuNyAtMTIuNSwyLjcgLTIuNCwtNi41IDEyLjcsLTE0LjMgMjcuOSwtMTQuNCA2LjgsLTAuMSA4LDAuMiAxMC43LDIuNSB6IiAvPgogIDxwYXRoCiAgICAgc3R5bGU9ImZpbGw6IzAwMDBjNztzdHJva2U6bm9uZTtmaWxsLW9wYWNpdHk6MSIKICAgICBpbmtzY2FwZTpjb25uZWN0b3ItY3VydmF0dXJlPSIwIgogICAgIGlkPSJwYXRoMTAiCiAgICAgZD0ibSA0MS41LDg2LjkgYyAtMy41LDIuMSAtNy45LDcgLTEwLDExLjEgLTIsMy45IC00LjcsMjAuMiAtMTAsNjAgLTIuNSwxOSAtNC43LDM1LjUgLTUsMzYuNiAtMC40LDEuOCAxLjUsMy41IDEwLjgsMTAuMiAzMywyMy42IDUxLjUsMzIuNiA3Mi41LDM1LjQgbCA5LjQsMS4zIDE4LjIsMzUuNyBjIDEwLDE5LjcgMTguNSwzNiAxOC44LDM2LjMgMC4zLDAuNCA5LjMsLTMuNyAxOS45LC05LjEgbCAxOS40LC05LjYgMTIuNSwtMjIuNCBjIDYuOSwtMTIuMyAxMi44LC0yMi4zIDEzLjEsLTIyLjQgMC44LDAgMTcuOSwyNS43IDE3LjksMjYuOSAwLDAuNiAtMy45LDkuMyAtOC42LDE5LjUgbCAtOC43LDE4LjUgLTMuMSwtMi40IGMgLTEuNywtMS4zIC01LC0yLjkgLTcuNCwtMy42IC02LC0xLjcgLTEzLjgsMC43IC0xOC4yLDUuOCBsIC0zLjIsMy42IDAuNCwxMC42IGMgMS4xLDIzLjYgMTAuMSwzMi4yIDI1LjYsMjQuNSA4LjgsLTQuNCAxOS4xLC0xNS4yIDQyLjgsLTQ1LjIgMjUuNywtMzIuNCAyNCwtMjguNiAxOS44LC00NC4yIC0zLjYsLTEzLjIgLTEwLjEsLTI4LjggLTE5LjcsLTQ3IC05LjIsLTE3LjQgLTEzLjYsLTIzLjggLTE5LjQsLTI4IC01LjMsLTMuOSAtMTAuNSwtNC45IC0yNSwtNSBsIC0xMi4zLDAgMCwtNS4zIGMgMCwtMi44IDAuOSwtMTkuNiAyLC0zNy4yIDEuMSwtMTcuNiAyLC0zMy45IDIsLTM2LjMgbCAwLC00LjIgLTM0LjUsMCBjIC0yNS4zLDAgLTM0LjUsMC4zIC0zNC41LDEuMSAwLDIuNCA1LDcyLjYgNS41LDc3LjcgbCAwLjUsNS4zIC00LjYsLTIuNyBjIC04LjMsLTQuOSAtMjIsLTQuMiAtMzMuNSwxLjYgbCAtMy42LDEuOSAtMy43LC00LjUgYyAtNS43LC02LjggLTEyLjMsLTkuNyAtMjIuMSwtOS44IC00LjQsLTAuMSAtOS45LDAuMyAtMTIuMiwwLjcgbCAtNC4zLDAuOSAwLC02LjUgYyAwLC0zLjUgMC4zLC0xMy4yIDAuNywtMjEuNSBsIDAuNiwtMTUuMiA0LjEsMCBjIDkuMywwIDE5LjIsLTcuNyAyMC4zLC0xNS44IDAuOCwtNi4xIC0xLjMsLTEwLjggLTcuOSwtMTcuNCBDIDU1LjYsODUuNiA0OC4zLDgyLjcgNDEuNSw4Ni45IFogbSAxMDkuMywyNy42IGMgMy42LDQuMSA1LjIsOS4xIDUuMSwxNiBsIC0wLjEsNSAtMC44LC0zLjkgYyAtMS42LC04LjIgLTkuNiwtMTEuMiAtMTUuMSwtNS43IC0zLjksNCAtMy45LDguMiAwLDEyLjIgMy45LDMuOSA4LjgsNCAxMi42LDAuMSBsIDIuOCwtMi43IC0wLjgsMi41IGMgLTEuMiw0LjEgLTQuMiw3LjYgLTcuNSw5IC02LjgsMi44IC0xMy42LC00LjMgLTE0LjcsLTE1LjQgLTAuNywtNi43IDEsLTEyLjggNC44LC0xNy4xIDIuNSwtMi44IDMuOCwtMy41IDYuOSwtMy41IDMuMSwwIDQuNCwwLjcgNi44LDMuNSB6IG0gMzQuMSwwIGMgNy45LDkgNi4yLDI2LjkgLTMuMiwzMiAtMy40LDEuOCAtMy43LDEuOSAtNy4xLDAuMyAtMi4yLC0xLjEgLTQuMywtMy4yIC01LjYsLTUuNyAtMi44LC01LjUgLTIuNSwtNi41IDAuOSwtMyAzLjksMy44IDguOCw0IDEyLjUsMC4zIDMuNywtMy43IDMuNSwtOC42IC0wLjMsLTEyLjUgLTUuMywtNS4zIC0xMy4zLC0yLjYgLTE1LjEsNS4xIGwgLTAuOCwzLjUgLTAuMSwtNC41IGMgLTAuMSwtNi40IDEuNSwtMTEuNSA1LjEsLTE1LjUgMi40LC0yLjggMy43LC0zLjUgNi44LC0zLjUgMy4xLDAgNC40LDAuNyA2LjksMy41IHogbSAtNS45LDQ4LjQgYyAwLDEuMiAtNi40LDYuNyAtOS44LDguNSAtNy4xLDMuNiAtMTIuMiwyLjggLTE4LC0yLjcgLTcuMSwtNi44IC03LjIsLTYuNyAxMS4zLC02LjcgOS4xLDAgMTYuNSwwLjQgMTYuNSwwLjkgeiIgLz4KICA8cGF0aAogICAgIHN0eWxlPSJmaWxsOiMwMDAwYzc7c3Ryb2tlOm5vbmU7ZmlsbC1vcGFjaXR5OjEiCiAgICAgaW5rc2NhcGU6Y29ubmVjdG9yLWN1cnZhdHVyZT0iMCIKICAgICBpZD0icGF0aDEyIgogICAgIGQ9Im0gMTYyLjYsMzA5LjIgLTE1LjgsOC4xIDAuNywxMC42IGMgMC40LDUuOCAxLDExLjEgMS40LDExLjggMC40LDAuNyA1LjUsLTIuNCAxNC41LC05LjEgMTEsLTggMTQuMSwtMTAuOCAxNC43LC0xMy4xIDEsLTQuMyAxLjgsLTE2LjUgMSwtMTYuNSAtMC4zLDAgLTcuNywzLjcgLTE2LjUsOC4yIHoiIC8+CiAgPHBhdGgKICAgICBzdHlsZT0iZmlsbDojMDAwMGM3O3N0cm9rZTpub25lO2ZpbGwtb3BhY2l0eToxIgogICAgIGlua3NjYXBlOmNvbm5lY3Rvci1jdXJ2YXR1cmU9IjAiCiAgICAgaWQ9InBhdGgxNCIKICAgICBkPSJtIDE2Mi44LDMzNS41IC0xMy43LDEwLjUgMC42LDcuMiBjIDAuMyw0IDAuOSw3LjcgMS4zLDguMiAwLjUsMC40IDYuMywtMy4xIDEyLjksLTggbCAxMiwtOC44IDAuNiwtNS41IGMgMC4zLC0zLjEgMC44LC03LjUgMSwtOS45IDAuMywtMi4zIDAuMSwtNC4yIC0wLjMsLTQuMSAtMC40LDAgLTYuOSw0LjcgLTE0LjQsMTAuNCB6IiAvPgogIDxwYXRoCiAgICAgc3R5bGU9ImZpbGw6IzAwMDBjNztzdHJva2U6bm9uZTtmaWxsLW9wYWNpdHk6MSIKICAgICBpbmtzY2FwZTpjb25uZWN0b3ItY3VydmF0dXJlPSIwIgogICAgIGlkPSJwYXRoMTYiCiAgICAgZD0ibSAxNjIuOCwzNTguOCAtMTEuNyw4LjcgMC42LDcuNSBjIDAuOSwxMiAwLjQsMTEuOCAxMiwzLjMgbCAxMC4xLC03LjUgMC43LC03LjYgYyAwLjMsLTQuMyAwLjgsLTguOSAxLC0xMC41IDAuMywtMS41IDAuMSwtMi43IC0wLjMsLTIuNyAtMC40LDAgLTYsNCAtMTIuNCw4LjggeiIgLz4KICA8cGF0aAogICAgIHN0eWxlPSJmaWxsOiMwMDAwYzc7c3Ryb2tlOm5vbmU7ZmlsbC1vcGFjaXR5OjEiCiAgICAgaW5rc2NhcGU6Y29ubmVjdG9yLWN1cnZhdHVyZT0iMCIKICAgICBpZD0icGF0aDE4IgogICAgIGQ9Im0gMTYyLjcsMzgzLjYgYyAtOC44LDYuOCAtOS44LDcuOCAtOS4zLDEwLjIgMC4zLDEuNSAwLjgsNi42IDEuMiwxMS40IDAuMyw0LjkgMC45LDguOCAxLjEsOC44IDEuNSwwIDE1LjIsLTExLjggMTUuNiwtMTMuNSAwLjcsLTIuNiAyLjEsLTI0LjUgMS42LC0yNC40IC0wLjIsMCAtNC44LDMuNCAtMTAuMiw3LjUgeiIgLz4KICA8cGF0aAogICAgIHN0eWxlPSJmaWxsOiMwMDAwYzc7c3Ryb2tlOm5vbmU7ZmlsbC1vcGFjaXR5OjEiCiAgICAgaW5rc2NhcGU6Y29ubmVjdG9yLWN1cnZhdHVyZT0iMCIKICAgICBpZD0icGF0aDIwIgogICAgIGQ9Im0gMTYyLjcsNDEzLjYgYyAtMy43LDMgLTYuNyw2LjEgLTYuNyw2LjcgMCwzLjQgNi4yLDcwLjYgNi43LDcyLjcgMC45LDMuNiAxLDMuMSA0LjMsLTM4IDEuNiwtMjAuNiAzLjIsLTM5LjYgMy41LC00Mi4zIDAuMywtMi42IDAuMiwtNC43IC0wLjIsLTQuNyAtMC41LDAgLTMuOSwyLjUgLTcuNiw1LjYgeiIgLz4KPC9zdmc+Cg==\" alt=\"Logotipo\" width=\"35px\"/></td>");
			regresar.append("<td><p style=\"text-align: center;align-content: center;font-size: 10px;\">").append(this.principal.getTitulo()).append("<br/>").append(this.principal.getTicket()).append("</p></td>");
			regresar.append("</tr></table>");
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar.toString();
	} // toEncabezado;
	
	private String toBlackBar(){
		StringBuilder regresar= null;
		try {
			regresar= new StringBuilder();
			regresar.append("<p style=\"width: 200px;text-align: center;font-size: 9px;font-weight: bold;background: black;color: white\">CENTRO DE SERVICIO DEWALT Y B&amp;D</p>");
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar.toString();
	} // toBlackBar
	
	private String toDomicilio() throws Exception{
		StringBuilder regresar= null;
		try {
			regresar= new StringBuilder();
			regresar.append("<p style=\"width: 200px;text-align: center;font-size: 11px;\">").append(toFindDomicilio()).append("</p>");
		} // try
		catch (Exception e) {			
			throw e;  
		} // catch		
		return regresar.toString();
	} // toDomicilio
	
	private String toFindDomicilio() throws Exception{
		Entity domicilio         = null;
		String regresar          = null;
		Map<String, Object>params= null;
		try {
			params= new HashMap<>();
			params.put("idEmpresa", this.principal.getIdEmpresa());
			domicilio= (Entity) DaoFactory.getInstance().toEntity("VistaInformacionEmpresas", "datosEmpresa", params);
			regresar= domicilio.toString("empresaDireccion").concat(" C.P. ").concat(domicilio.toString("codigoPostal")).concat(" Colonia. ").concat(domicilio.toString("colonia"));
		} // try
		catch (Exception e) {			
			throw e; 
		} // catch		
		return regresar;
	} // toFindDomicilio
	
	private String toNoTicket(){
		StringBuilder regresar  = null;
		String descripcionTicket= null;
		try {			
			regresar= new StringBuilder();
			descripcionTicket= this.tipoTransaccion.equals("COTIZACIÓN") ? ((TicketVenta)this.ticket.getOrden()).getCotizacion(): ((TicketVenta)this.ticket.getOrden()).getTicket();
			regresar.append("<p style=\"width: 200px;text-align: center;align-content: center;font-size: 12px;font-weight: bold\">TICKET No: ").append(this.principal.getClave()).append("-").append(descripcionTicket).append("</p>");
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar.toString();
	} // toNoTicket
	
	private String toTipoTransaccion(){
		StringBuilder regresar= null;
		try {
			regresar= new StringBuilder();
			regresar.append("<p style=\"width: 200px;text-align: center;align-content: center;font-size: 9px;font-weight: bold\">").append(this.tipoTransaccion).append("</p>");
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar.toString();
	} // toTipoVenta
	
	private String toFecha(){
		StringBuilder regresar= null;
		try {
			regresar= new StringBuilder();
			regresar.append("<p style=\"width: 200px;text-align: center;align-content: center;font-size: 11px;\">Fecha:").append(Fecha.formatear(Fecha.FECHA_HORA_CORTA, ((TicketVenta)this.ticket.getOrden()).getRegistro())).append("</p>");
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar.toString();
	} // toFecha
	
	private String toTable(){
		StringBuilder regresar= null;
		try {
			regresar= new StringBuilder();
			regresar.append("<table style=\"width: 200px;border-top: 1px solid black;border-collapse: collapse;\">");
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar.toString();
	} // toTable
	
	private String toHeaderTable(){
		StringBuilder regresar= null;
		try {
			regresar= new StringBuilder();
			regresar.append("<thead>");
			regresar.append("<tr style=\"border-top: 1px solid black;border-collapse: collapse;\">");
			regresar.append("<th style=\"font-size: 10px;width: 80px; max-width: 80px;border-top: 1px solid black;border-collapse: collapse;text-align: left\">CONCEPTO</th>");
			regresar.append("<th style=\"font-size: 10px;width: 35px;max-width: 35px;word-break: break-all;border-top: 1px solid black;border-collapse: collapse;text-align: center\">CANT</th>");
			regresar.append("<th style=\"font-size: 10px;width: 35px;max-width: 45px;word-break: break-all;border-top: 1px solid black;border-collapse: collapse;\">NETO</th>");
			regresar.append("<th style=\"font-size: 10px;width: 55px;max-width: 45px;word-break: break-all;border-top: 1px solid black;border-collapse: collapse;\">IMPORTE</th>");
			regresar.append("</tr></thead>");
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar.toString();
	} // toNoTicket
	
	private String toArticulos(){
		StringBuilder regresar= null;
		ArticuloVenta pivote  = null;
		try {
			regresar= new StringBuilder();
			regresar.append("<tbody>");
			for(Articulo articulo : this.ticket.getArticulos()){
				pivote= (ArticuloVenta) articulo;
				regresar.append("<tr style=\"border-top: 1px solid black;border-collapse: collapse;\">");
				regresar.append("<td style=\"font-size: 8px;width: 80px; max-width: 80px;border-top: 1px solid black;border-collapse: collapse;\">").append(pivote.getNombre()).append("</td>");
				regresar.append("<td style=\"font-size: 8px;width: 35px;max-width: 35px;word-break: break-all;border-top: 1px solid black;border-collapse: collapse;text-align: center\">").append(pivote.getCantidad()).append("</td>");
				regresar.append("<td style=\"font-size: 8px;width: 35px;max-width: 45px;word-break: break-all;border-top: 1px solid black;border-collapse: collapse;text-align: right\">").append(pivote.getPrecio()).append("</td>");
				regresar.append("<td style=\"font-size: 8px;width: 55px;max-width: 45px;word-break: break-all;border-top: 1px solid black;border-collapse: collapse;text-align: right\">").append(pivote.getImporte()).append("</td>");
				regresar.append("</tr>");
			} // for
			regresar.append("<tr style=\"height: 15px;border-top: 1px solid black;border-collapse: collapse;\"><td></td><td></td><td></td><td></td></tr>");
			regresar.append("</tbody>");
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar.toString();
	} // toArticulos
	
	private String toPagos(){
		StringBuilder regresar= null;
		try {
			regresar= new StringBuilder();
			regresar.append("<table style=\"width: 200px;\">");
			regresar.append("<tbody>");
			
			regresar.append("<tr style=\"border-collapse: collapse;\">");			
			regresar.append("<td style=\"font-size: 8px;width: 70px;max-width: 35px;word-break: break-all;border-collapse: collapse;\"></td>");			
			regresar.append("<td style=\"font-size: 8px;width: 35px; max-width: 80px;border-collapse: collapse;\"></td>");			
			regresar.append("<td style=\"font-size: 8px;width: 55px;max-width: 45px;word-break: break-all;border-collapse: collapse;text-align: right;\">SUBTOTAL:</td>");			
			regresar.append("<td style=\"font-size: 8px;width: 45px;max-width: 45px;word-break: break-all;border-collapse: collapse;text-align: right\">").append(this.ticket.getTotales().getSubTotalDosDecimales$()).append("</td>");			
			regresar.append("</tr>");			
			
			regresar.append("<tr style=\"border-collapse: collapse;\">");			
			regresar.append("<td style=\"font-size: 8px;width: 70px;max-width: 35px;word-break: break-all;border-collapse: collapse;\"></td>");			
			regresar.append("<td style=\"font-size: 8px;width: 35px; max-width: 80px;border-collapse: collapse;\"></td>");			
			regresar.append("<td style=\"font-size: 8px;width: 55px;max-width: 45px;word-break: break-all;border-collapse: collapse;text-align: right;\">IVA:</td>");			
			regresar.append("<td style=\"font-size: 8px;width: 45px;max-width: 45px;word-break: break-all;border-collapse: collapse;text-align: right\">").append(this.ticket.getTotales().getIvaDosDecimales$()).append("</td>");			
			regresar.append("</tr>");			
			
			regresar.append("<tr style=\"border-collapse: collapse;\">");			
			regresar.append("<td style=\"font-size: 8px;width: 70px;max-width: 35px;word-break: break-all;border-collapse: collapse;\"></td>");			
			regresar.append("<td style=\"font-size: 8px;width: 35px; max-width: 80px;border-collapse: collapse;\"></td>");			
			regresar.append("<td style=\"font-size: 10px;width: 55px;max-width: 45px;word-break: break-all;border-collapse: collapse;text-align: right;font-weight: bold;\">TOTAL:</td>");			
			regresar.append("<td style=\"font-size: 10px;width: 45px;max-width: 45px;word-break: break-all;border-collapse: collapse;text-align: right;font-weight: bold;\">$").append(this.ticket.getTotales().getTotalDosDecimales$()).append("</td>");			
			regresar.append("</tr>");			
			
			regresar.append("<tr style=\"height: 15px;\"><td></td><td></td><td></td><td></td></tr>");	
			
			if(this.pago.getEfectivo() > 0){			
				regresar.append("<tr style=\"border-collapse: collapse;\">");
				regresar.append("<td style=\"font-size: 8px;width: 70px;max-width: 35px;word-break: break-all;border-collapse: collapse;\"></td>");
				regresar.append("<td style=\"font-size: 8px;width: 35px; max-width: 80px;border-collapse: collapse;\"></td>");
				regresar.append("<td style=\"font-size: 8px;width: 55px;max-width: 45px;word-break: break-all;border-collapse: collapse;text-align: right;\">EFECTIVO:</td>");
				regresar.append("<td style=\"font-size: 8px;width: 45px;max-width: 45px;word-break: break-all;border-collapse: collapse;text-align: right;\">").append(this.pago.getEfectivo()).append("</td>");
				regresar.append("</tr>");
			} // if
			
			if(this.pago.getCredito()> 0){			
				regresar.append("<tr style=\"border-collapse: collapse;\">");
				regresar.append("<td style=\"font-size: 8px;width: 70px;max-width: 35px;word-break: break-all;border-collapse: collapse;\"></td>");
				regresar.append("<td style=\"font-size: 8px;width: 35px; max-width: 80px;border-collapse: collapse;\"></td>");
				regresar.append("<td style=\"font-size: 8px;width: 55px;max-width: 45px;word-break: break-all;border-collapse: collapse;text-align: right;\">CREDITO:<br/>REF ").append(this.pago.getReferenciaCredito()).append("</td>");
				regresar.append("<td style=\"font-size: 8px;width: 45px;max-width: 45px;word-break: break-all;border-collapse: collapse;text-align: right;\">").append(this.pago.getCredito()).append("</td>");
				regresar.append("</tr>");
			} // if
			
			if(this.pago.getTransferencia()> 0){			
				regresar.append("<tr style=\"border-collapse: collapse;\">");
				regresar.append("<td style=\"font-size: 8px;width: 70px;max-width: 35px;word-break: break-all;border-collapse: collapse;\"></td>");
				regresar.append("<td style=\"font-size: 8px;width: 35px; max-width: 80px;border-collapse: collapse;\"></td>");
				regresar.append("<td style=\"font-size: 8px;width: 55px;max-width: 45px;word-break: break-all;border-collapse: collapse;text-align: right;\">TRANSF:<br/>REF ").append(this.pago.getReferenciaTransferencia()).append("</td>");
				regresar.append("<td style=\"font-size: 8px;width: 45px;max-width: 45px;word-break: break-all;border-collapse: collapse;text-align: right;\">").append(this.pago.getTransferencia()).append("</td>");
				regresar.append("</tr>");
			} // if
			
			if(this.pago.getCheque()> 0){			
				regresar.append("<tr style=\"border-collapse: collapse;\">");
				regresar.append("<td style=\"font-size: 8px;width: 70px;max-width: 35px;word-break: break-all;border-collapse: collapse;\"></td>");
				regresar.append("<td style=\"font-size: 8px;width: 35px; max-width: 80px;border-collapse: collapse;\"></td>");
				regresar.append("<td style=\"font-size: 8px;width: 55px;max-width: 45px;word-break: break-all;border-collapse: collapse;text-align: right;\">CHEQUE:<br/>REF ").append(this.pago.getReferenciaCheque()).append("</td>");
				regresar.append("<td style=\"font-size: 8px;width: 45px;max-width: 45px;word-break: break-all;border-collapse: collapse;text-align: right;\">").append(this.pago.getCheque()).append("</td>");
				regresar.append("</tr>");
			} // if
			
			regresar.append("<tr style=\"border-collapse: collapse;\">");
			regresar.append("<td style=\"font-size: 8px;width: 70px;max-width: 35px;word-break: break-all;border-collapse: collapse;\"></td>");
			regresar.append("<td style=\"font-size: 8px;width: 35px; max-width: 80px;border-collapse: collapse;\"></td>");
			regresar.append("<td style=\"font-size: 10px;width: 55px;max-width: 45px;word-break: break-all;border-collapse: collapse;text-align: right;font-weight: bold;\">CAMBIO:</td>");
			regresar.append("<td style=\"font-size: 10px;width: 45px;max-width: 45px;word-break: break-all;border-collapse: collapse;text-align: right;font-weight: bold;\">").append(this.pago.getCambio$()).append("</td>");
			regresar.append("</tr></tbody></table>");			
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar.toString();
	} // toPagos
	
	private String toFinishTable(){
		StringBuilder regresar= null;
		try {
			regresar= new StringBuilder("</table>");
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar.toString();
	} // toArticulos

	private String toVendedor() throws Exception{
		StringBuilder regresar= null;
		try {
			regresar= new StringBuilder("<br/>");
			regresar.append("<p style=\"width: 200px;font-size: 9px;border-top: 1px solid black;border-collapse: collapse;\">");
			regresar.append("<br/><strong>VENDEDOR:</strong>").append(toUsuario()).append("<br/>");
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar.toString();
	} // toArticulos

	private String toUsuario() throws Exception{
		String regresar          = null;
		Entity usuario           = null;
		Map<String, Object>params= null;
		try {
			params= new HashMap<>();
			params.put("idUsuario", ((TicketVenta)this.ticket.getOrden()).getIdUsuario());
			usuario= (Entity) DaoFactory.getInstance().toEntity("VistaUsuariosDto", "perfilUsuario", params);
			regresar= usuario.toString("nombreCompleto");
		} // try
		catch (Exception e) {
			throw e;
		} // catch		
		return regresar;
	} // toUsuario
	
	private String toCajero(){
		StringBuilder regresar= null;
		try {
			regresar= new StringBuilder("<strong>COBRO:</strong>").append(JsfBase.getAutentifica().getPersona().getNombreCompleto());
			regresar.append("</p>");
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar.toString();
	} // toArticulos
	
	private String toFooter(){
		StringBuilder regresar= null;
		String descripcion    = null;
		try {
			descripcion= this.tipoTransaccion.equals("COTIZACIÓN") ? "GRACIAS POR SU PREFERENCIA" : "GRACIAS POR SU COMPRA";
			regresar= new StringBuilder();
			regresar.append("<p style=\"width: 200px;text-align: center;align-content: center;font-size: 11px;border-top: 1px solid black;border-collapse: collapse;\">");
			regresar.append("<br/>¡").append(descripcion).append("!");
			regresar.append("</p>");
			regresar.append("<p style=\"width: 200px;text-align: center;align-content: center;font-size: 8px;\">");
			regresar.append("PARA CUALQUIER ACLARACION, MANTENER SU TICKET");
			regresar.append("</p>");
			//regresar.append("<svg id=\"barcode\"></svg>");
			regresar.append("</div>");
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar.toString();
	} // toFooter	
}