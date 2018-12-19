package mx.org.kaana.libs.facturama.reglas;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.facturas.beans.ArticuloFactura;
import mx.org.kaana.mantic.facturas.beans.ClienteFactura;
import org.hibernate.Session;

public class CFDIGestor implements Serializable{

	private static final long serialVersionUID = 7197603923593328319L;
	private Long idComodin;

	public CFDIGestor() {
		this(-1L);
	} // // CFDIGestor
	
	public CFDIGestor(Long idComodin) {
		this.idComodin= idComodin;
	} // CFDIGestor	
	
	public ClienteFactura toClienteFactura() throws Exception{
		ClienteFactura regresar  = null;
		Map<String, Object>params= null;
		try {
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, "tc_mantic_clientes.id_cliente=" + this.idComodin);
			regresar= (ClienteFactura) DaoFactory.getInstance().toEntity(ClienteFactura.class, "VistaClientesDto", "facturama", params);
		} // try
		catch (Exception e) {
			throw e;
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
		return regresar;
	} // toClienteFactura
	
	public ClienteFactura toClienteFactura(Session sesion) throws Exception{
		ClienteFactura regresar  = null;
		Map<String, Object>params= null;
		try {
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, "tc_mantic_clientes.id_cliente=" + this.idComodin);
			regresar= (ClienteFactura) DaoFactory.getInstance().toEntity(sesion, ClienteFactura.class, "VistaClientesDto", "facturama", params);
		} // try
		catch (Exception e) {
			throw e;
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
		return regresar;
	} // toClienteFactura
	
	public ClienteFactura toClienteCfdiVenta() throws Exception{
		ClienteFactura regresar  = null;
		Map<String, Object>params= null;
		try {
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, "tc_mantic_ventas.id_venta=" + this.idComodin);
			regresar= (ClienteFactura) DaoFactory.getInstance().toEntity(ClienteFactura.class, "VistaClientesDto", "facturamaCfdiVentas", params);
		} // try
		catch (Exception e) {
			throw e;
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
		return regresar;
	} // toClienteFactura
	
	public ClienteFactura toClienteCfdiVenta(Session sesion) throws Exception{
		ClienteFactura regresar  = null;
		Map<String, Object>params= null;
		try {
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, "tc_mantic_ventas.id_venta=" + this.idComodin);
			regresar= (ClienteFactura) DaoFactory.getInstance().toEntity(sesion, ClienteFactura.class, "VistaClientesDto", "facturamaCfdiVentas", params);
		} // try
		catch (Exception e) {
			throw e;
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
		return regresar;
	} // toClienteFactura
	
	public ClienteFactura toClienteCfdiFicticia() throws Exception{
		ClienteFactura regresar  = null;
		Map<String, Object>params= null;
		try {
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, "tc_mantic_ficticias.id_ficticia=" + this.idComodin);
			regresar= (ClienteFactura) DaoFactory.getInstance().toEntity(ClienteFactura.class, "VistaClientesDto", "cfdiFicticia", params);
		} // try
		catch (Exception e) {
			throw e;
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
		return regresar;
	} // toClienteFactura
	
	public ClienteFactura toClienteCfdiFicticia(Session sesion) throws Exception{
		ClienteFactura regresar  = null;
		Map<String, Object>params= null;
		try {
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, "tc_mantic_ficticias.id_ficticia=" + this.idComodin);
			regresar= (ClienteFactura) DaoFactory.getInstance().toEntity(sesion, ClienteFactura.class, "VistaClientesDto", "cfdiFicticia", params);
		} // try
		catch (Exception e) {
			throw e;
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
		return regresar;
	} // toClienteFactura
	
	public List<ClienteFactura> toAllClientesFactura() throws Exception{
		List<ClienteFactura> regresar= null;
		Map<String, Object>params    = null;
		try {
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
			regresar= DaoFactory.getInstance().toEntitySet(ClienteFactura.class, "VistaClientesDto", "facturama", params, Constantes.SQL_TODOS_REGISTROS);
		} // try
		catch (Exception e) {
			throw e;
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
		return regresar;
	} // toClienteFactura
	
	public List<ClienteFactura> toAllClientesFactura(Session sesion) throws Exception{
		List<ClienteFactura> regresar= null;
		Map<String, Object>params    = null;
		try {
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
			regresar= DaoFactory.getInstance().toEntitySet(sesion, ClienteFactura.class, "VistaClientesDto", "facturama", params, Constantes.SQL_TODOS_REGISTROS);
		} // try
		catch (Exception e) {
			throw e;
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
		return regresar;
	} // toClienteFactura
	
	public ArticuloFactura toArticuloFactura() throws Exception{
		ArticuloFactura regresar  = null;
		Map<String, Object>params= null;
		try {
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, "tc_mantic_articulos.id_articulo=" + this.idComodin);
			regresar= (ArticuloFactura) DaoFactory.getInstance().toEntity(ArticuloFactura.class, "VistaArticulosDto", "facturama", params);
		} // try
		catch (Exception e) {
			throw e;
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
		return regresar;
	} // toClienteFactura
	
	public ArticuloFactura toArticuloFactura(Session sesion) throws Exception{
		ArticuloFactura regresar = null;
		Map<String, Object>params= null;
		try {
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, "tc_mantic_articulos.id_articulo=" + this.idComodin);
			regresar= (ArticuloFactura) DaoFactory.getInstance().toEntity(sesion, ArticuloFactura.class, "VistaArticulosDto", "facturama", params);			
		} // try
		catch (Exception e) {
			throw e;
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
		return regresar;
	} // toClienteFactura
	
	public List<ArticuloFactura> toAllArticulosFactura() throws Exception{
		List<ArticuloFactura> regresar= null;
		Map<String, Object>params     = null;
		try {
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
			regresar= DaoFactory.getInstance().toEntitySet(ArticuloFactura.class, "VistaArticulosDto", "facturama", params, Constantes.SQL_TODOS_REGISTROS);
		} // try
		catch (Exception e) {
			throw e;
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
		return regresar;
	} // toClienteFactura
	
	public List<ArticuloFactura> toAllArticulosFactura(Session sesion) throws Exception{
		List<ArticuloFactura> regresar= null;
		Map<String, Object>params     = null;
		try {
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
			regresar= DaoFactory.getInstance().toEntitySet(sesion, ArticuloFactura.class, "VistaArticulosDto", "facturama", params, Constantes.SQL_TODOS_REGISTROS);
		} // try
		catch (Exception e) {
			throw e;
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
		return regresar;
	} // toClienteFactura
	
	public List<ArticuloFactura> toDetalleCfdiVentas() throws Exception{
		List<ArticuloFactura> regresar= null;
		Map<String, Object>params     = null;
		try {
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, "tc_mantic_ventas_detalles.id_venta=" + this.idComodin);
			regresar= DaoFactory.getInstance().toEntitySet(ArticuloFactura.class, "VistaArticulosDto", "facturamaCfdiVenta", params, Constantes.SQL_TODOS_REGISTROS);
		} // try
		catch (Exception e) {
			throw e;
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
		return regresar;
	} // toClienteFactura
	
	public List<ArticuloFactura> toDetalleCfdiVentas(Session sesion) throws Exception{
		List<ArticuloFactura> regresar= null;
		Map<String, Object>params     = null;
		try {
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, "tc_mantic_ventas_detalles.id_venta=" + this.idComodin);
			regresar= DaoFactory.getInstance().toEntitySet(sesion, ArticuloFactura.class, "VistaArticulosDto", "facturamaCfdiVenta", params, Constantes.SQL_TODOS_REGISTROS);
		} // try
		catch (Exception e) {
			throw e;
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
		return regresar;
	} // toClienteFactura
	
	public List<ArticuloFactura> toDetalleCfdiFicticia() throws Exception{
		List<ArticuloFactura> regresar= null;
		Map<String, Object>params     = null;
		try {
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, "tc_mantic_ficticias_detalles.id_ficticia=" + this.idComodin);
			regresar= DaoFactory.getInstance().toEntitySet(ArticuloFactura.class, "VistaArticulosDto", "detalleCfdi", params, Constantes.SQL_TODOS_REGISTROS);
		} // try
		catch (Exception e) {
			throw e;
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
		return regresar;
	} // toClienteFactura
	
	public List<ArticuloFactura> toDetalleCfdiFicticia(Session sesion) throws Exception{
		List<ArticuloFactura> regresar= null;
		Map<String, Object>params     = null;
		try {
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, "tc_mantic_ficticias_detalles.id_ficticia=" + this.idComodin);
			regresar= DaoFactory.getInstance().toEntitySet(sesion, ArticuloFactura.class, "VistaArticulosDto", "detalleCfdi", params, Constantes.SQL_TODOS_REGISTROS);
		} // try
		catch (Exception e) {
			throw e;
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
		return regresar;
	} // toClienteFactura
}
