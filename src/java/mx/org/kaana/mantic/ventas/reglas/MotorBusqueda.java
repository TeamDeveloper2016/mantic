package mx.org.kaana.mantic.ventas.reglas;

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
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.clientes.beans.ClienteTipoContacto;
import mx.org.kaana.mantic.catalogos.comun.MotorBusquedaCatalogos;
import mx.org.kaana.mantic.db.dto.TcManticArticulosDto;
import mx.org.kaana.mantic.db.dto.TcManticVentasDto;
import mx.org.kaana.mantic.enums.ETipoMediosPago;
import mx.org.kaana.mantic.enums.ETiposContactos;

public class MotorBusqueda extends MotorBusquedaCatalogos implements Serializable{

	private static final long serialVersionUID= -1476191556651225342L;		
	private Long idComodin;
	
	public MotorBusqueda(Long idComodin) {
		this(idComodin, null);
	}	// MotorBusqueda

	public MotorBusqueda(Long idComodin, Long idCliente) {
		super(idCliente);
		this.idComodin= idComodin;
	}	
	
	public TcManticArticulosDto toArticulo() throws Exception {
		TcManticArticulosDto regresar= null;
		try {
			regresar= (TcManticArticulosDto) DaoFactory.getInstance().findById(TcManticArticulosDto.class, this.idComodin);
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toArticulo
	
	public Entity toDescuentoGrupo() throws Exception{
		Entity regresar          = null;
		Map<String, Object>params= null;
		try {
			params= new HashMap<>();
			params.put("idArticulo", this.idComodin);
			params.put("idCliente", this.idCliente);
			params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getDependencias());
			regresar= (Entity) DaoFactory.getInstance().toEntity("VistaVentasDto", "descuentoGrupoVigente", params);
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
		return regresar;
	} // toDescuentoGrupo
	
	public Entity toDescuentoArticulo() throws Exception{
		Entity regresar          = null;
		Map<String, Object>params= null;
		try {
			params= new HashMap<>();
			params.put("idArticulo", this.idComodin);			
			regresar= (Entity) DaoFactory.getInstance().toEntity("VistaVentasDto", "descuentoArticuloVigente", params);
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
		return regresar;
	} // toDescuentoGrupo
	
	public Entity toCliente() throws Exception{
		Entity regresar          = null;
		Map<String, Object>params= null;
		try {
			params= new HashMap<>();
			params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getDependencias());
			params.put(Constantes.SQL_CONDICION, "tc_mantic_clientes.id_cliente=" + this.idCliente);
			regresar= (Entity) DaoFactory.getInstance().toEntity("VistaClientesDto", "findRazonSocial", params);
		} // try
		catch (Exception e) {			
			throw e;
		} // catch	
		finally{
			Methods.clean(params);
		} // finally
		return regresar;
	} // toCliente
	
	public Entity toDetalleArticulo() throws Exception{
		Entity regresar          = null;
		Map<String, Object>params= null;
		try {
			params= new HashMap<>();			
			params.put("idArticulo", this.idComodin);
			regresar= (Entity) DaoFactory.getInstance().toEntity("VistaArticulosDto", "detalle", params);
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
		return regresar;
	} // toDetalleArticulo
	
	public Double toDeudaCliente() throws Exception{
		Double regresar          = null;
		Map<String, Object>params= null;
		try {
			params= new HashMap<>();
			params.put("idCliente", this.idCliente);
			regresar= DaoFactory.getInstance().toField("TcManticClientesDeudasDto", "saldoCliente", params, "saldo").toDouble();
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
		return regresar;
	} // toDeudaCliente		
	
	public List<ClienteTipoContacto> toCorreosCliente() throws Exception {
		List<ClienteTipoContacto> regresar= null;
		List<ClienteTipoContacto> pivote  = null;
		try {
			regresar= new ArrayList<>();
			pivote= super.toClientesTipoContacto();
			if(!pivote.isEmpty()){
				for(ClienteTipoContacto contacto: pivote){
					if(contacto.getIdTipoContacto().equals(ETiposContactos.CORREO.getKey()) || contacto.getIdTipoContacto().equals(ETiposContactos.CORREO_NEGOCIO.getKey()) || contacto.getIdTipoContacto().equals(ETiposContactos.CORREO_PERSONAL.getKey()))
						regresar.add(contacto);
				} // for					
			} // if							
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toClientesTipoContacto
	
	public ClienteTipoContacto toTelefonoCliente() throws Exception {
		ClienteTipoContacto regresar    = null;
		List<ClienteTipoContacto> pivote= null;
		try {
			regresar= new ClienteTipoContacto();
			pivote= super.toClientesTipoContacto();
			if(!pivote.isEmpty()){
				for(ClienteTipoContacto contacto: pivote){
					if(contacto.getIdTipoContacto().equals(ETiposContactos.TELEFONO.getKey()))
						regresar= contacto;
				} // for					
			} // if							
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toClientesTipoContacto
	
	public ClienteTipoContacto toCelularCliente() throws Exception {
		ClienteTipoContacto regresar    = null;
		List<ClienteTipoContacto> pivote= null;
		try {
			regresar= new ClienteTipoContacto();
			pivote= super.toClientesTipoContacto();
			if(!pivote.isEmpty()){
				for(ClienteTipoContacto contacto: pivote){
					if(contacto.getIdTipoContacto().equals(ETiposContactos.CELULAR.getKey()))
						regresar= contacto;
				} // for					
			} // if							
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toClientesTipoContacto
	
	public TcManticVentasDto toVenta(Long idVenta) throws Exception{
		TcManticVentasDto regresar= null;
		try {
			regresar= (TcManticVentasDto) DaoFactory.getInstance().findById(TcManticVentasDto.class, idVenta);
		} // try
		catch (Exception e) {			
			throw e;
		} // catch				
		return regresar;
	} // toVenta
	
	public boolean doVerificaVigenciaCotizacion(Long idVenta) throws Exception{
		boolean regresar       = false;
		TcManticVentasDto venta= null;
		Date fecha             = null;
		Date vigencia          = null;
		try {
			venta= toVenta(idVenta);
			fecha= new Date(Calendar.getInstance().getTimeInMillis());
			vigencia= venta.getVigencia();
			regresar= fecha.after(vigencia);
		} // try
		catch (Exception e) {			
			throw e; 
		} // catch		
		return regresar;
	} // doVerificaVigenciaCotizacion
	
	public List<Entity> pagosVenta() throws Exception{
		List<Entity> regresar     = null;
		Map<String, Object> params= null;
		int count                 = 0;
		try {
			params= new HashMap<>();
			params.put("idVenta", this.idComodin);
			regresar= DaoFactory.getInstance().toEntitySet("VistaVentasDto", "pagosVenta", params, Constantes.SQL_TODOS_REGISTROS);
			for(Entity record: regresar){
				if(record.getKey().equals(ETipoMediosPago.EFECTIVO.getIdTipoMedioPago()))
					count++;
			} // for
			if(count== 0)
				regresar.add(0, toPagoEfectivo());
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
		return regresar;
	} // pagosVenta
	
	private Entity toPagoEfectivo(){
		Entity regresar= null;
		try {
			regresar= new Entity(ETipoMediosPago.EFECTIVO.getIdTipoMedioPago());
			regresar.put("idTipoMedioPago", new Value("idTipoMedioPago", ETipoMediosPago.EFECTIVO.getIdTipoMedioPago()));
			regresar.put("idVenta", new Value("idVenta", this.idComodin));
			regresar.put("nombre", new Value("nombre", ETipoMediosPago.EFECTIVO.name()));
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toPagoEfectivo
	
	public Entity toFactura() throws Exception{
		Entity regresar          = null;
		Map<String, Object>params= null;
		try {
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, "id_factura=" + this.idComodin + " and folio is not null");
			regresar= (Entity) DaoFactory.getInstance().toEntity("TcManticFacturasDto", "row", params);
		} // try
		catch (Exception e) {			
			throw e; 
		} // catch		
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	} // toFactura
}
