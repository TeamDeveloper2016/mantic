package mx.org.kaana.mantic.ventas.caja.cierres.reglas;

import mx.org.kaana.mantic.ventas.caja.reglas.*;
import java.util.HashMap;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.db.dto.TcManticPersonasDto;

public class CreateCierre extends CreateTicket {
	
	private Double cantidad;
	private Long idAutorizo;
	
	public CreateCierre(Double cantidad, String tipo) {
		this(cantidad, tipo, null);
	}
	
	public CreateCierre(Double cantidad, String tipo, Long idAutorizo) {
		super(null, null, tipo);
		this.cantidad  = cantidad;
		this.idAutorizo= idAutorizo;
		super.init();
	}	
	
	@Override
	public String toHtml() throws Exception{
		StringBuilder sb= new StringBuilder();
		sb.append(toHeader());
		sb.append(toBlackBar());				
		sb.append(toTipoTransaccion());
		sb.append(toFecha());
		sb.append(toTable());					
		sb.append(toCantidad());				
		sb.append(toVendedor());		
		if(this.idAutorizo!= null)
			sb.append(toAutorizo());				
		return sb.toString();
	} // toHtml
	
	@Override
	public String toTipoTransaccion(){
		StringBuilder regresar= new StringBuilder();
		regresar.append("<p style=\"width: 290px;text-align: center;align-content: center;font-family: sans-serif;font-size: 14px;border-collapse: collapse;\">");
		regresar.append(this.tipo).append("<br>").append("<br>");		
		return regresar.toString();
	} // toTipoVenta			

	@Override
	public String toVendedor() throws Exception{
		StringBuilder regresar= new StringBuilder("<br/>");
		regresar.append("<p style=\"width: 290px;font-family: sans-serif;font-size: 13px;border-top: 1px solid black;border-collapse: collapse;\">");
		regresar.append("<br/><strong>USUARIO:</strong>").append(toUsuario()).append("<br/>");		
		return regresar.toString();
	} // toArticulos
	
	public String toAutorizo() throws Exception{
		StringBuilder regresar= new StringBuilder("<br/>");
		regresar.append("<p style=\"width: 290px;font-family: sans-serif;font-size: 13px;border-top: 1px solid black;border-collapse: collapse;\">");
		regresar.append("<br/><strong>AUTORIZO:</strong>").append(toPersona()).append("<br/>");		
		return regresar.toString();
	} // toArticulos

	@Override
	protected String toFindDomicilio() throws Exception{
		Entity domicilio         = null;
		String regresar          = null;
		Map<String, Object>params= null;
		try {
			params= new HashMap<>();
			params.put("idEmpresa", this.getPrincipal().getIdEmpresa());
			domicilio= (Entity) DaoFactory.getInstance().toEntity("VistaInformacionEmpresas", "datosEmpresa", params);
			regresar= domicilio.toString("empresaDireccion").concat(" C.P. ").concat(domicilio.toString("codigoPostal")).concat("<br> COLONIA. ").concat(domicilio.toString("colonia"));
		} // try
		finally{
			Methods.clean(params);
		} // finally
		return regresar;
	} // toFindDomicilio
	
	@Override
	public String toUsuario() throws Exception{
		String regresar          = null;
		Entity usuario           = null;
		Map<String, Object>params= null;
		try {
			params= new HashMap<>();
			params.put("idUsuario", JsfBase.getIdUsuario());
			usuario= (Entity) DaoFactory.getInstance().toEntity("VistaUsuariosDto", "perfilUsuario", params);
			regresar= usuario.toString("nombreCompleto");
		} // try
		finally{
			Methods.clean(params);
		} // finally
		return regresar;
	} // toUsuario
	
	public String toPersona() throws Exception{
		String regresar            = null;
		TcManticPersonasDto persona= (TcManticPersonasDto) DaoFactory.getInstance().findById(TcManticPersonasDto.class, this.idAutorizo);			
		regresar= persona.getNombres().concat(" ").concat(persona.getPaterno()).concat(" ").concat(persona.getMaterno());
		return regresar;
	} // toUsuario
	
	private String toCantidad(){
		StringBuilder regresar= new StringBuilder();
		regresar.append("<table style=\"width: 290px;\">");
		regresar.append("<tbody>");
		regresar.append("<tr style=\"border-collapse: collapse;\">");						
		regresar.append("<td style=\"font-family: sans-serif;font-size: 12px;width: 220px;max-width: 220px;word-break: break-all;border-collapse: collapse;text-align: right;\">TOTAL:</td>");			
		regresar.append("<td style=\"font-family: sans-serif;font-size: 12px;width: 70px;max-width: 70px;word-break: break-all;border-collapse: collapse;text-align: right\">").append(Numero.formatear(Numero.NUMERO_CON_DECIMALES, this.cantidad)).append("</td>");			
		regresar.append("</tr>");							
		regresar.append("<tr style=\"height: 15px;\"><td></td><td></td><td></td><td></td></tr>");					
		regresar.append("</tbody></table>");					
		return regresar.toString();
	} // toPagos
	
	@Override
	protected String toFecha(){
		StringBuilder regresar= new StringBuilder();
		regresar.append("Fecha:").append(Fecha.formatear(Fecha.FECHA_HORA_CORTA));		
		regresar.append("</p>");		
		return regresar.toString();
	} // toFecha
}