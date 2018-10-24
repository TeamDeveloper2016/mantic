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
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.clientes.beans.ClienteTipoContacto;
import mx.org.kaana.mantic.catalogos.comun.MotorBusquedaCatalogos;
import mx.org.kaana.mantic.db.dto.TcManticArticulosDto;
import mx.org.kaana.mantic.db.dto.TcManticVentasDto;
import mx.org.kaana.mantic.enums.ETiposContactos;

public class MotorBusqueda extends MotorBusquedaCatalogos implements Serializable{

	private static final long serialVersionUID= -1476191556651225342L;	
	private static final String VENTA         = "VENTA";
	private Long idArticulo;
	
	public MotorBusqueda(Long idArticulo) {
		this(idArticulo, null);
	}	// MotorBusqueda

	public MotorBusqueda(Long idArticulo, Long idCliente) {
		super(idCliente);
		this.idArticulo= idArticulo;
	}	
	
	public TcManticArticulosDto toArticulo() throws Exception {
		TcManticArticulosDto regresar= null;
		try {
			regresar= (TcManticArticulosDto) DaoFactory.getInstance().findById(TcManticArticulosDto.class, this.idArticulo);
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
			params.put("idArticulo", this.idArticulo);
			params.put("idCliente", this.idCliente);
			params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
			regresar= (Entity) DaoFactory.getInstance().toEntity("VistaVentasDto", "descuentoGrupoVigente", params);
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toDescuentoGrupo
	
	public Entity toDescuentoArticulo() throws Exception{
		Entity regresar          = null;
		Map<String, Object>params= null;
		try {
			params= new HashMap<>();
			params.put("idArticulo", this.idArticulo);			
			regresar= (Entity) DaoFactory.getInstance().toEntity("VistaVentasDto", "descuentoArticuloVigente", params);
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toDescuentoGrupo
	
	public Entity toCliente() throws Exception{
		Entity regresar          = null;
		Map<String, Object>params= null;
		try {
			params= new HashMap<>();
			params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
			params.put(Constantes.SQL_CONDICION, "tc_mantic_clientes.id_cliente=" + this.idCliente);
			regresar= (Entity) DaoFactory.getInstance().toEntity("VistaClientesDto", "findRazonSocial", params);
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toCliente
	
	public Entity toDetalleArticulo() throws Exception{
		Entity regresar          = null;
		Map<String, Object>params= null;
		try {
			params= new HashMap<>();			
			params.put("idArticulo", this.idArticulo);
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
		return regresar;
	} // toDeudaCliente
	
	public Entity toClienteDefault() throws Exception{
		Entity regresar          = null;
		Map<String, Object>params= null;
		try {
			params= new HashMap<>();
			params.put("clave", VENTA);
			params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
			regresar= (Entity) DaoFactory.getInstance().toEntity("VistaClientesDto", "clienteDefault", params);			
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toClienteDefault	
	
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
}
