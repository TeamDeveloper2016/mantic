package mx.org.kaana.mantic.facturas.reglas;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.hibernate.Session;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.reglas.beans.Siguiente;
import mx.org.kaana.libs.facturama.reglas.CFDIGestor;
import mx.org.kaana.libs.facturama.reglas.TransaccionFactura;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.KajoolBaseException;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.clientes.beans.ClienteTipoContacto;
import mx.org.kaana.mantic.ventas.reglas.MotorBusqueda;
import mx.org.kaana.mantic.catalogos.comun.MotorBusquedaCatalogos;
import mx.org.kaana.mantic.compras.ordenes.beans.Articulo;
import mx.org.kaana.mantic.db.dto.TcManticFacturasDto;
import mx.org.kaana.mantic.db.dto.TcManticFicticiasBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticFicticiasDto;
import mx.org.kaana.mantic.db.dto.TcManticFicticiasDetallesDto;
import mx.org.kaana.mantic.db.dto.TcManticVentasDto;
import mx.org.kaana.mantic.enums.EEstatusFacturas;
import mx.org.kaana.mantic.enums.EEstatusFicticias;
import mx.org.kaana.mantic.enums.ETiposContactos;
import mx.org.kaana.mantic.facturas.beans.ClienteFactura;
import org.apache.log4j.Logger;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 7/05/2018
 *@time 03:29:13 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class UnirFacturas extends TransaccionFactura {

  private static final Logger LOG= Logger.getLogger(UnirFacturas.class);
	private TcManticFicticiasDto orden;	
	private List<Articulo> articulos;
	private List<Entity> tickets;
	private Entity cliente;
	private String messageError;	
	private String justificacion;				
	
	public UnirFacturas(TcManticFicticiasDto orden, List<Articulo> articulos, String justificacion, List<Entity> tickets, Entity cliente) { 		
		this.orden        = orden;		
		this.articulos    = articulos;
		this.justificacion= justificacion;
		this.tickets      = tickets;
		this.cliente      = cliente;
	} // Transaccion
	
	public String getMessageError() {
		return messageError;
	} // Transaccion

	public void setMessageError(String messageError) {
		this.messageError = messageError;
	}	
	
	public TcManticFicticiasDto getOrden() {
		return orden;
	}	
	
	@Override
	protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {		
		boolean regresar          = false;
		Map<String, Object> params= null;		
		String correos            = null;
		Long idFicticiaEstatus    = EEstatusFicticias.ABIERTA.getIdEstatusFicticia();
		try {			
			this.messageError= "Ocurrio un error en ".concat(accion.name().toLowerCase()).concat(" la factura.");
			switch(accion) {												
				case REGISTRAR:																						
					if(registrarFicticia(sesion, idFicticiaEstatus)){
						if(actualizarVentas(sesion)){
							params= new HashMap<>();
							params.put("idFicticia", this.orden.getIdFicticia());
							idFicticiaEstatus= EEstatusFicticias.TIMBRADA.getIdEstatusFicticia();						
							if(registraBitacora(sesion, this.orden.getIdFicticia(), idFicticiaEstatus, "Finalización de venta con timbrado de factura.")) {							
								this.orden.setIdFicticiaEstatus(idFicticiaEstatus);						
								regresar= DaoFactory.getInstance().update(sesion, this.orden)>= 1L;
								if(this.checkTotal(sesion)) {																		
									correos= getCorreos(sesion, this.orden.getIdCliente());
									params.put("correos", correos);
									params.put("comentarios", this.justificacion);								
									params.put("timbrado", new Timestamp(Calendar.getInstance().getTimeInMillis()));								
									params.put("idFacturaEstatus", EEstatusFacturas.TIMBRADA.getIdEstatusFactura());								
									DaoFactory.getInstance().update(sesion, TcManticFacturasDto.class, this.orden.getIdFactura(), params);
									registrarBitacoraFactura(sesion, this.orden.getIdFactura(), EEstatusFacturas.TIMBRADA.getIdEstatusFactura(), this.justificacion);
									this.generarTimbradoFactura(sesion, this.orden.getIdFicticia(), this.orden.getIdFactura(), correos);
								} // if						
							} // if						
						} // if
					} // if
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
		if(this.orden!= null)
			LOG.info("Se genero de forma correcta la orden: "+ this.orden.getConsecutivo());
		return regresar;
	}	// ejecutar
	
	private boolean registrarFicticia(Session sesion, Long idEstatusFicticia) throws Exception {
		boolean regresar         = false;
		Siguiente consecutivo    = null;
		Siguiente cuenta         = null;
		Long idFactura           = -1L;
		Map<String, Object>params= null;
		try {									
			idFactura= registrarFactura(sesion);										
			if(idFactura>= 1L){								
				consecutivo= this.toSiguiente(sesion);			
				this.orden.setTicket(consecutivo.getConsecutivo());			
				this.orden.setCticket(consecutivo.getOrden());			
				cuenta= this.toSiguienteCuenta(sesion);			
				this.orden.setConsecutivo(cuenta.toConsecutivo());			
				this.orden.setOrden(cuenta.getOrden());				
				this.orden.setIdFicticiaEstatus(idEstatusFicticia);
				this.orden.setEjercicio(new Long(Fecha.getAnioActual()));						
				this.orden.setIdFactura(idFactura);
				if(DaoFactory.getInstance().insert(sesion, this.orden)>= 1L){					
					params= new HashMap<>();
					params.put("idFicticia", this.orden.getIdFicticia());
					if(DaoFactory.getInstance().update(sesion, TcManticFacturasDto.class, idFactura, params)>= 1L){					
						regresar= registraBitacora(sesion, this.orden.getIdFicticia(), idEstatusFicticia, "");
						this.toFillArticulos(sesion);
					} // if					
				} // if
			} // if
		} // try
		catch (Exception e) {			
			throw e;
		} // catch				
		return regresar;
	} // registrarFicticia
	
	protected boolean registraBitacora(Session sesion, Long idFicticia, Long idFicticaEstatus, String justificacion) throws Exception {
		TcManticFicticiasBitacoraDto bitFicticia= new TcManticFicticiasBitacoraDto(this.orden.getConsecutivo(), justificacion, idFicticaEstatus, JsfBase.getIdUsuario(), idFicticia, -1L, this.orden.getTotal());
		return DaoFactory.getInstance().insert(sesion, bitFicticia)>= 1L;
	} // registrarBitacora
	
	private void toFillArticulos(Session sesion) throws Exception {
		List<Articulo> todos= (List<Articulo>)DaoFactory.getInstance().toEntitySet(sesion, Articulo.class, "TcManticFicticiasDetallesDto", "detalle", this.orden.toMap());
		for (Articulo item: todos) 
			if(this.articulos.indexOf(item)< 0)
				DaoFactory.getInstance().delete(sesion, item);
		for (Articulo articulo: this.articulos) {
			if(articulo.isValid()) {
				TcManticFicticiasDetallesDto item= articulo.toFicticiaDetalle();
				item.setIdFicticia(this.orden.getIdFicticia());
				if(DaoFactory.getInstance().findIdentically(sesion, TcManticFicticiasDetallesDto.class, item.toMap())== null) 
					DaoFactory.getInstance().insert(sesion, item);
				else
					DaoFactory.getInstance().update(sesion, item);
			} // if
		} // for
	} // toFillArticulos
	
	private Siguiente toSiguienteCuenta(Session sesion) throws Exception {
		Siguiente regresar        = null;
		Map<String, Object> params= null;
		try {
			params=new HashMap<>();
			params.put("ejercicio", this.getCurrentYear());
			params.put("dia", Fecha.getHoyEstandar());
			params.put("idEmpresa", this.orden.getIdEmpresa());
			params.put("operador", this.getCurrentSign());
			Value next= DaoFactory.getInstance().toField(sesion, "TcManticFicticiasDto", "cuenta", params, "siguiente");
			if(next!= null && next.getData()!= null)
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
	} // toCuenta
	
	private Siguiente toSiguiente(Session sesion) throws Exception {
		Siguiente regresar        = null;
		Map<String, Object> params= null;
		try {
			params=new HashMap<>();
			params.put("ejercicio", this.getCurrentYear());
			params.put("idEmpresa", this.orden.getIdEmpresa());
			params.put("operador", this.getCurrentSign());
			Value next= DaoFactory.getInstance().toField(sesion, "TcManticFicticiasDto", "siguiente", params, "siguiente");
			if(next!= null && next.getData()!= null)
				regresar= new Siguiente(next.toLong());
			else
				regresar= new Siguiente(Configuracion.getInstance().isEtapaDesarrollo()? 900001L: 1L);
		} // try
		catch (Exception e) {
			throw e;
		} // catch
		finally {
			Methods.clean(params);
			this.messageError= "Error al generar el consecutivo.";		
		} // finally
		return regresar;
	} // toSiguiente
	
	private Long registrarFactura(Session sesion) throws Exception{
		Long regresar              = -1L;
		TcManticFacturasDto factura= null;
		try {			
			factura= new TcManticFacturasDto();
			factura.setIdUsuario(JsfBase.getIdUsuario());
			factura.setIntentos(0L);
			factura.setCorreos("");
			factura.setObservaciones(this.justificacion);
			factura.setIdFacturaEstatus(EEstatusFacturas.REGISTRADA.getIdEstatusFactura());
			regresar= DaoFactory.getInstance().insert(sesion, factura);
			registrarBitacoraFactura(sesion, factura.getIdFactura(), EEstatusFacturas.REGISTRADA.getIdEstatusFactura(), this.justificacion);
		} // try
		finally{
			this.messageError= "Error al registrar la factura.";
		} // finally
		return regresar;
	} // registrarFactura
	
	private void generarTimbradoFactura(Session sesion, Long idFicticia, Long idFactura, String correos) throws Exception {
		TransaccionFactura factura= null;
		CFDIGestor gestor         = null;
		try {
			actualizarClienteFacturama(sesion, idFicticia);
			gestor= new CFDIGestor(idFicticia);			
			factura= new TransaccionFactura();
			factura.setArticulos(gestor.toDetalleCfdiFicticia(sesion));
			factura.setCliente(gestor.toClienteCfdiFicticia(sesion));
			factura.getCliente().setIdFactura(idFactura);
			factura.generarCfdi(sesion);				
		} // try
		finally{
			this.messageError= "Error al generar el timbrado de la factura.";
		} // finally
	} // generarTimbradoFactura

	private void actualizarClienteFacturama(Session sesion, Long idFicticia) throws Exception{		
		CFDIGestor gestor= new CFDIGestor(idFicticia);
		ClienteFactura cliente= gestor.toClienteFacturaUpdate(sesion);
		setCliente(cliente);
		if(cliente.getIdFacturama()!= null)
			updateCliente(sesion);
		else
			super.procesarCliente(sesion);		
	} // actualizarArticuloFacturama
	
	private boolean checkTotal(Session sesion) throws Exception {
		boolean regresar = false;
		Double sumTotal  = 0D;
		Double sumDetalle= 0D;
		Map<String, Object> params= null;
		try {
			params=new HashMap<>();
			params.put("idFicticia", this.orden.getIdFicticia());
			Value detalle= DaoFactory.getInstance().toField(sesion, "TcManticFicticiasDetallesDto", "total", params, "total");
			if(detalle!= null && detalle.getData()!= null)
				sumDetalle= detalle.toDouble();
			Value total= DaoFactory.getInstance().toField(sesion, "TcManticFicticiasDto", "total", params, "total");
			if(total!= null && total.getData()!= null)
				sumTotal= total.toDouble();
		} // try
		finally {
			Methods.clean(params);
		} // finally
		regresar= Objects.equals(sumTotal, sumDetalle);
		if(!regresar) {
			LOG.warn("Diferencias en los importes de la factura: "+ this.orden.getIdFicticia()+ " verificar situacion, total ["+ sumTotal+ "] detalle["+ sumDetalle+ "]");
			throw new KajoolBaseException("No se puede timbrar porque el importe total difiere de los importes del detalle de la factura !");	
		} // if	
		return regresar;
	} // checkTotal
	
	private String getCorreos(Session sesion, Long idCliente) throws Exception{
		String regresar                   = null;
		StringBuilder correos             = null;
		MotorBusquedaCatalogos motor      = null; 
		List<ClienteTipoContacto>contactos= null;
		try {
			motor= new MotorBusqueda(-1L, idCliente);
			contactos= motor.toClientesTipoContacto(sesion);
			correos= new StringBuilder("");
			for(ClienteTipoContacto contacto: contactos){
				if(contacto.getIdTipoContacto().equals(ETiposContactos.CORREO.getKey()))
					correos.append(contacto.getValor()).append(",");
			} // for
			regresar= correos.substring(0, correos.length()-1);
		} // try		
		finally{
			this.messageError= "Error al recuperar los correos del cliente para enviar la factura.";
		} // finally
		return regresar;
	} // getCorreos
	
	private boolean actualizarVentas(Session sesion) throws Exception{
		boolean regresar         = false;
		Map<String, Object>params= null;
		int count                = 0;		
		try {
			params= new HashMap<>();
			params.put("idFactura", this.orden.getIdFactura());
			for(Entity venta: this.tickets){
				if(DaoFactory.getInstance().update(sesion, TcManticVentasDto.class, venta.getKey(), params)>= 1L)
					count++;
			} // for
			regresar= count==this.tickets.size();
		} // try
		finally{
			this.messageError= "Error al actualizar la factura en las ventas.";
		} // finally
		return regresar;
	} // actualizarVentas
} 