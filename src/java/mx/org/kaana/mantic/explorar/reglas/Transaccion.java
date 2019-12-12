package mx.org.kaana.mantic.explorar.reglas;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.db.comun.sql.Value;
import org.hibernate.Session;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.reglas.beans.Siguiente;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.formato.Variables;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.db.dto.TcManticPedidosBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticPedidosDetallesDto;
import mx.org.kaana.mantic.db.dto.TcManticPedidosDto;
import mx.org.kaana.mantic.enums.EEstatusPedidos;
import org.apache.log4j.Logger;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 7/05/2018
 *@time 03:29:13 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Transaccion extends mx.org.kaana.mantic.inventarios.almacenes.reglas.Transaccion {

  private static final Logger LOG = Logger.getLogger(Transaccion.class);
 	
	private Entity articulo;
	private TcManticPedidosDto pedido;
	private TcManticPedidosDetallesDto detalle;	

	public Transaccion() {
		this(new Entity());
	} 
	
	public Transaccion(Entity articulo) {
		this.articulo = articulo;
	} // Transaccion

	public Transaccion(TcManticPedidosDto pedido) {
		this.pedido = pedido;
	} // Transaccion

	public Transaccion(TcManticPedidosDetallesDto detalle) {
		this.detalle  = detalle;
	} // Transaccion

	@Override
	protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {		
		boolean regresar                        = false;
		Map<String, Object> params              = null;		
		TcManticPedidosDetallesDto pivoteDetalle= null;		
		try {
			initPedido(sesion, accion);
			this.messageError= "Ocurrio un error en ".concat(accion.name().toLowerCase()).concat(" el pedido a realizar.");						
			switch(accion) {
				case AGREGAR:
					params= new HashMap<>();
					params.put("idUsuario", JsfBase.getIdUsuario());
					params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
					this.pedido= (TcManticPedidosDto)DaoFactory.getInstance().findFirst(TcManticPedidosDto.class, "abierto", params);
					if(this.pedido== null) {
						this.pedido= loadPedido(sesion);												
						DaoFactory.getInstance().insert(sesion, this.pedido);
  					this.toGlobalEstatus(sesion);
					} // else
					this.detalle= new TcManticPedidosDetallesDto(
						this.articulo.toDouble("descuentos"), // Double descuentos, 
						-1L, // Long idPedidoDetalle, 
						this.articulo.toString("propio"), // String codigo, 
						"", // String unidadMedida, 
						this.articulo.toString("descuento"), // String descuento, 
						this.articulo.toString("sat"), // String sat, 
						this.articulo.toString("extra"), // String extra, 
						this.articulo.toString("nombre"), // String nombre, 
						this.articulo.toDouble("importe"), // Double importe, 
						this.articulo.toDouble("precio"), // Double precio, 
						this.articulo.toDouble("iva"), // Double iva, 
						this.articulo.toDouble("impuestos"), // Double impuestos, 
						this.articulo.toDouble("precio"), // Double unitarioSinIva, 
						this.articulo.toDouble("subTotal"), // Double subTotal, 
						this.articulo.toDouble("cantidad"), // Double cantidad, 
						this.articulo.toLong("idArticulo"), // Long idArticulo, 
						this.pedido.getIdPedido() // Long idPedido
					);
					this.detalle.setIdPedido(this.pedido.getIdPedido());
					regresar= DaoFactory.getInstance().insert(sesion, this.detalle)> 0L;
					this.toUpdateTotal(sesion);
					break;				
				case ELIMINAR:										
					regresar= DaoFactory.getInstance().delete(sesion, this.detalle)> 0L;
					this.toUpdateTotal(sesion);					
					break;				
				case MODIFICAR:
					pivoteDetalle= toArticuloDetalle(sesion, this.detalle.getIdArticulo(), this.detalle.getCantidad());
					pivoteDetalle.setIdPedido(this.detalle.getIdPedido());
					pivoteDetalle.setIdPedidoDetalle(this.detalle.getIdPedidoDetalle());
					regresar= DaoFactory.getInstance().update(sesion, pivoteDetalle)> 0L;
					this.toUpdateTotal(sesion);
					this.toUpdateEstatus(sesion);
					break;				
				case DEPURAR:
					params= new HashMap<>();
					params.put("idPedido", this.pedido.getIdPedido());
					regresar= DaoFactory.getInstance().deleteAll(sesion, TcManticPedidosDetallesDto.class, params)> 0L;
					this.pedido= (TcManticPedidosDto)DaoFactory.getInstance().findById(sesion, TcManticPedidosDto.class, this.pedido.getIdPedido());
					this.pedido.setTotal(0D);
					this.pedido.setSubTotal(0D);
      		this.pedido.setImpuestos(0D);
					this.pedido.setDescuentos(0D);
					this.pedido.setExcedentes(0D);
					this.pedido.setIdPedidoEstatus(EEstatusPedidos.INICIALIZADO.getIdEstatus());
					regresar= DaoFactory.getInstance().update(sesion, this.pedido)> 0L;
					this.toGlobalEstatus(sesion);
					break;				
				case DESACTIVAR:
					this.pedido.setIdPedidoEstatus(EEstatusPedidos.CANCELADO.getIdEstatus());
					regresar= DaoFactory.getInstance().update(sesion, this.pedido)> 0L;
					this.toGlobalEstatus(sesion);
					break;
				case PROCESAR:
					this.pedido.setIdPedidoEstatus(EEstatusPedidos.CERRADO.getIdEstatus());
					regresar= DaoFactory.getInstance().update(sesion, this.pedido)> 0L;
					this.toGlobalEstatus(sesion);
					break;
				case ACTIVAR:
					this.pedido= loadPedido(sesion);												
					regresar= DaoFactory.getInstance().insert(sesion, this.pedido)>= 1L;
					this.toGlobalEstatus(sesion);
					break;
			} // switch
			if(!regresar)
        throw new Exception("");
		} // try
		catch (Exception e) {
      Error.mensaje(e);			
			throw new Exception(this.messageError.concat("<br/>")+ e);
		} // catch		
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	}	// ejecutar

	private void initPedido(Session sesion, EAccion accion) throws Exception{
		try {
			switch(accion) {
				case ELIMINAR:
				case MODIFICAR:
					this.pedido= (TcManticPedidosDto)DaoFactory.getInstance().findById(sesion, TcManticPedidosDto.class, this.detalle.getIdPedido());
					break;
				case PROCESAR:
					this.pedido= (TcManticPedidosDto)DaoFactory.getInstance().findById(sesion, TcManticPedidosDto.class, this.pedido.getKey());
					break;
			} // switch
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
	} // initPedido
	
	private TcManticPedidosDto loadPedido(Session sesion) throws Exception{
		Siguiente consecutivo= this.toSiguiente(sesion);
		TcManticPedidosDto regresar= new TcManticPedidosDto(
			0D, // Double descuentos, 
			"0", // String descuento, 
			"0", //	String extras, 
			new Long(Calendar.getInstance().get(Calendar.YEAR)), //	Long ejercicio, 
			1L, //	Long idPedidoEstatus, 
			consecutivo.getConsecutivo(), // String consecutivo, 
			0D, // Double total, 
			JsfBase.getIdUsuario(), // Long idUsuario, 
			0D, // Double excedentes, 
			0D, // Double impuestos, 
			0D, // Double subTotal, 
			"", // String observaciones, 
			JsfBase.getAutentifica().getEmpresa().getIdEmpresa(), //	Long idEmpresa, 
			consecutivo.getOrden(), // Long orden, 
			-1L //	Long idPedido							
		 );
		return regresar;
	} // loadPedido
	
	private Siguiente toSiguiente(Session sesion) throws Exception {
		Siguiente regresar        = null;
		Map<String, Object> params= null;
		try {
			params=new HashMap<>();
			params.put("idUsuario", JsfBase.getIdUsuario());
			params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
			params.put("ejercicio", this.getCurrentYear());
			Value next= DaoFactory.getInstance().toField(sesion, "TcManticPedidosDto", "siguiente", params, "siguiente");
			if(next.getData()!= null)
			  regresar= new Siguiente(next.toLong());
			else
			  regresar= new Siguiente(Configuracion.getInstance().isEtapaDesarrollo()? 900001L: 1L);
		} // try
		catch (Exception e) {
			throw e;
		} // catch
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	}	// toSiguiente
	
	private void toUpdateEstatus(Session sesion) throws Exception {
		if(this.pedido.getIdPedidoEstatus().equals(EEstatusPedidos.ABIERTO.getIdEstatus()) || this.pedido.getIdPedidoEstatus().equals(EEstatusPedidos.INICIALIZADO.getIdEstatus())) {
			this.pedido.setIdPedidoEstatus(EEstatusPedidos.ACTUALIZADO.getIdEstatus());
			DaoFactory.getInstance().update(sesion, this.pedido);
			this.toGlobalEstatus(sesion);
		} // if	
	}	// toUpdateEstatus

	private void toUpdateTotal(Session sesion) throws Exception {
		List<TcManticPedidosDetallesDto> detalles= DaoFactory.getInstance().toEntitySet(sesion, TcManticPedidosDetallesDto.class, Variables.toMap(Constantes.SQL_CONDICION.concat("~id_pedido=").concat(this.pedido.getIdPedido().toString())));
		this.pedido.setTotal(0D);
		this.pedido.setSubTotal(0D);
		this.pedido.setImpuestos(0D);
		this.pedido.setDescuentos(0D);		
		if(!detalles.isEmpty()){
			for(TcManticPedidosDetallesDto recordDetalle: detalles){
				this.pedido.setTotal(this.pedido.getTotal() + recordDetalle.getImporte());
				this.pedido.setSubTotal(this.pedido.getSubTotal() + recordDetalle.getSubTotal());
				this.pedido.setImpuestos(this.pedido.getImpuestos()+ recordDetalle.getImpuestos());
				this.pedido.setDescuentos(this.pedido.getDescuentos()+ recordDetalle.getDescuentos());				
			} // for
		} // if
		DaoFactory.getInstance().update(sesion, this.pedido);
	} // toUpdateTotal
	
	private void toGlobalEstatus(Session sesion) throws Exception {
		TcManticPedidosBitacoraDto bitacora= new TcManticPedidosBitacoraDto(
			JsfBase.getIdUsuario(), // Long idUsuario, 
			"", // String observaciones, 
			-1L, // Long idPedidoBitacora, 
			this.pedido.getIdPedido(), // Long idPedido, 
			this.pedido.getIdPedidoEstatus() // Long idPedidoEstatus
		);
		DaoFactory.getInstance().insert(sesion, bitacora);
	}	// toGlobalEstatus
} 