package mx.org.kaana.mantic.facturas.reglas;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.hibernate.Session;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.reglas.beans.Siguiente;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.facturama.reglas.CFDIFactory;
import mx.org.kaana.libs.facturama.reglas.CFDIGestor;
import mx.org.kaana.libs.facturama.reglas.TransaccionFactura;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.KajoolBaseException;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.clientes.beans.ClienteTipoContacto;
import mx.org.kaana.mantic.compras.ordenes.beans.Articulo;
import mx.org.kaana.mantic.db.dto.TcManticFacturasDto;
import mx.org.kaana.mantic.db.dto.TcManticFicticiasBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticFicticiasDto;
import mx.org.kaana.mantic.db.dto.TcManticFicticiasDetallesDto;
import mx.org.kaana.mantic.db.dto.TrManticClienteTipoContactoDto;
import mx.org.kaana.mantic.enums.EEstatusFacturas;
import mx.org.kaana.mantic.enums.EEstatusFicticias;
import mx.org.kaana.mantic.enums.EEstatusVentas;
import mx.org.kaana.mantic.enums.ETiposContactos;
import mx.org.kaana.mantic.facturas.beans.ClienteFactura;
import mx.org.kaana.mantic.facturas.beans.Correo;
import org.apache.log4j.Logger;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 7/05/2018
 *@time 03:29:13 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Transaccion extends TransaccionFactura {

  private static final Logger LOG    = Logger.getLogger(Transaccion.class);
	private TcManticFicticiasBitacoraDto bitacora;
	private TcManticFicticiasDto orden;	
	private Long idFicticia;
	private List<Articulo> articulos;
	private String messageError;	
	private String justificacion;
	private String correos;
	private String comentarios;	
	private Correo correo;
	private Long idCliente;

	public Transaccion(Correo correo, Long idCliente) {
		this.correo   = correo;
		this.idCliente= idCliente;
	}	// Transaccion
	
	public Transaccion(TcManticFicticiasBitacoraDto bitacora) { 
		this(bitacora, "", "");
	} // Transaccion
	
	public Transaccion(TcManticFicticiasBitacoraDto bitacora, String correos, String comentarios) {
		this.bitacora   = bitacora;
		this.correos    = correos;
		this.comentarios= comentarios;
	} // Transaccion
	
	public Transaccion(TcManticFicticiasDto orden) {
		this(orden, "");
	}
	
	public Transaccion(TcManticFicticiasDto orden, String justificacion) {
		this(orden, new ArrayList<Articulo>(), justificacion);
	} // Transaccion

	public Transaccion(TcManticFicticiasDto orden, List<Articulo> articulos) {		
		this(orden, articulos, "");
	}
	
	public Transaccion(TcManticFicticiasDto orden, List<Articulo> articulos, String justificacion) { 		
		this.orden        = orden;		
		this.articulos    = articulos;
		this.justificacion= justificacion;
	} // Transaccion
	
	public Transaccion(Long idFicticia) {
		this.idFicticia= idFicticia;
	}
	
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
		boolean regresar           = false;
		Map<String, Object> params = null;
		Long idEstatusFactura      = null;
		TcManticFacturasDto factura= null;
		try {
			idEstatusFactura= EEstatusFicticias.ABIERTA.getIdEstatusFicticia();
			params= new HashMap<>();
			if(this.orden!= null)
				params.put("idFicticia", this.orden.getIdFicticia());
			this.messageError= "Ocurrio un error en ".concat(accion.name().toLowerCase()).concat(" la factura.");
			switch(accion) {				
				case COPIAR:
					regresar= this.toClonarFiciticia(sesion, idEstatusFactura);
					break;
				case AGREGAR:
				case REGISTRAR:	
				case DESACTIVAR:
					idEstatusFactura= accion.equals(EAccion.AGREGAR) ? EEstatusFicticias.ABIERTA.getIdEstatusFicticia() : (accion.equals(EAccion.DESACTIVAR) ? this.orden.getIdFicticiaEstatus() : idEstatusFactura);
					regresar= this.orden.getIdFicticia()!= null && !this.orden.getIdFicticia().equals(-1L) ? actualizarFicticia(sesion, idEstatusFactura) : registrarFicticia(sesion, idEstatusFactura);					
					break;
				case MODIFICAR:
					regresar= actualizarFicticia(sesion, EEstatusFicticias.ABIERTA.getIdEstatusFicticia());					
					break;				
				case ELIMINAR:
					idEstatusFactura= EEstatusFicticias.CANCELADA.getIdEstatusFicticia();
					this.orden= (TcManticFicticiasDto) DaoFactory.getInstance().findById(sesion, TcManticFicticiasDto.class, this.orden.getIdFicticia());
					this.orden.setIdFicticiaEstatus(idEstatusFactura);					
					if(DaoFactory.getInstance().update(sesion, this.orden)>= 1L)
						regresar= registraBitacora(sesion, this.orden.getIdFicticia(), idEstatusFactura, this.justificacion);					
					break;
				case JUSTIFICAR:		
					if(DaoFactory.getInstance().insert(sesion, this.bitacora)>= 1L) {
						this.orden= (TcManticFicticiasDto) DaoFactory.getInstance().findById(sesion, TcManticFicticiasDto.class, this.bitacora.getIdFicticia());
						this.orden.setIdFicticiaEstatus(this.bitacora.getIdFicticiaEstatus());						
						regresar= DaoFactory.getInstance().update(sesion, this.orden)>= 1L;
						if((this.bitacora.getIdFicticiaEstatus().equals(EEstatusFicticias.TIMBRADA.getIdEstatusFicticia()) || this.bitacora.getIdFicticiaEstatus().equals(EEstatusVentas.TERMINADA.getIdEstatusVenta())) && this.checkTotal(sesion)) {
							params.put("idVenta", this.orden.getIdVenta());
							factura= (TcManticFacturasDto) DaoFactory.getInstance().toEntity(sesion, TcManticFacturasDto.class, "VistaFicticiasDto", "factura", params);
							if(factura!= null) {
								params.put("correos", this.correos);
								params.put("comentarios", this.comentarios);								
								params.put("timbrado", new Timestamp(Calendar.getInstance().getTimeInMillis()));		
								params.put("intentos", (factura.getIntentos()+1L));
								DaoFactory.getInstance().update(sesion, TcManticFacturasDto.class, factura.getIdFactura(), params);
								this.generarTimbradoFactura(sesion, this.orden.getIdFicticia(), factura.getIdFactura(), this.correos);
							} // 
						} // if
						else 
							if(this.bitacora.getIdFicticiaEstatus().equals(EEstatusFicticias.CANCELADA.getIdEstatusFicticia()) || this.bitacora.getIdFicticiaEstatus().equals(EEstatusVentas.ELIMINADA.getIdEstatusVenta())) {
								params.put("idVenta", this.orden.getIdVenta());
								factura= (TcManticFacturasDto) DaoFactory.getInstance().toEntity(sesion, TcManticFacturasDto.class, "VistaFicticiasDto", "factura", params);
								if(factura!= null && factura.getIdFacturama()!= null) {
									CFDIFactory.getInstance().cfdiRemove(factura.getIdFacturama());
									factura.setCancelada(new Timestamp(Calendar.getInstance().getTimeInMillis()));
									regresar= DaoFactory.getInstance().update(sesion, factura)>= 0;
								} // if
								else
									throw new Exception("No fue posible cancelar la factura, por favor vuelva a intentarlo !");															
							} // else if
					} // if
					break;								
				case REPROCESAR:
					regresar= actualizarFicticia(sesion, EEstatusFicticias.TIMBRADA.getIdEstatusFicticia());				
					break;		
				case NO_APLICA:
					params.put("idFicticia", this.orden.getIdFicticia());
					if(DaoFactory.getInstance().deleteAll(sesion, TcManticFicticiasBitacoraDto.class, params)>= 0) {
						if(DaoFactory.getInstance().deleteAll(sesion, TcManticFicticiasDetallesDto.class, params)>= 0)
							regresar= DaoFactory.getInstance().delete(sesion, this.orden)>= 1L;
					} // if					
					break;
				case COMPLEMENTAR: 
					regresar= agregarContacto(sesion);
					break;
				case DEPURAR:
					this.messageError= "Ocurrio un error al cancelar la factura.";
					params= new HashMap<>();
					params.put("idFactura", this.orden.getIdFactura());
					factura= (TcManticFacturasDto) DaoFactory.getInstance().toEntity(sesion, TcManticFacturasDto.class, "TcManticFacturasDto", "detalle", params);
					if(factura!= null && factura.getIdFacturama()!= null) {
						CFDIFactory.getInstance().cfdiRemove(factura.getIdFacturama());
						factura.setCancelada(new Timestamp(Calendar.getInstance().getTimeInMillis()));
						factura.setIdFacturaEstatus(EEstatusFacturas.CANCELADA.getIdEstatusFactura());
						regresar= DaoFactory.getInstance().update(sesion, factura)>= 0;
						registrarBitacoraFactura(sesion, factura.getIdFactura(), EEstatusFacturas.CANCELADA.getIdEstatusFactura(), "Cancelación de factura.".concat(this.justificacion));
					} // if
					else
						throw new Exception("No fue posible cancelar la factura, por favor vuelva a intentarlo !");															
				  break;
				case ACTIVAR:
					TcManticFicticiasDto ficticia= (TcManticFicticiasDto)DaoFactory.getInstance().findById(TcManticFicticiasDto.class, this.idFicticia);
					if(ficticia!= null) {
						ficticia.setIdTipoDocumento(1L);
						ficticia.setIdFicticiaEstatus(EEstatusFicticias.ABIERTA.getIdEstatusFicticia());
						regresar= DaoFactory.getInstance().update(sesion, ficticia)> 0;
						TcManticFicticiasBitacoraDto bitFicticia= new TcManticFicticiasBitacoraDto(ficticia.getTicket(), "Se cambio la cotización especial para timbrarse", EEstatusFicticias.ABIERTA.getIdEstatusFicticia(), JsfBase.getIdUsuario(), this.idFicticia, -1L, ficticia.getTotal());
						regresar= DaoFactory.getInstance().insert(sesion, bitFicticia)>= 1L;
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
				if(DaoFactory.getInstance().insert(sesion, this.orden)>= 1L) {
					params= new HashMap<>();
					// Este campo ya no se va a utilizar porque toda va a caer en venta, las facturas ficticias tienden a desaparecer
					params.put("idVenta", this.orden.getIdVenta());
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
	
	private boolean actualizarFicticia(Session sesion, Long idEstatusFicticia) throws Exception{
		boolean regresar           = false;
		Map<String, Object>params  = null;
		TcManticFacturasDto factura=null;
		try {						
			this.orden.setIdFicticiaEstatus(idEstatusFicticia);						
			regresar= DaoFactory.getInstance().update(sesion, this.orden)>= 1L;
			params= new HashMap<>();
			params.put("idVenta", this.orden.getIdVenta());
			factura= (TcManticFacturasDto) DaoFactory.getInstance().toEntity(sesion, TcManticFacturasDto.class, "VistaFicticiasDto", "factura", params);
			factura.setObservaciones(this.justificacion);
			if(DaoFactory.getInstance().update(sesion, factura)>= 1L){
				if(registraBitacora(sesion, this.orden.getIdFicticia(), idEstatusFicticia, "")) {
					params= new HashMap<>();
					params.put("idFicticia", this.orden.getIdFicticia());
					regresar= DaoFactory.getInstance().deleteAll(sesion, TcManticFicticiasDetallesDto.class, params)>= 0;
					toFillArticulos(sesion);
				} // if
			} // if
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		finally{
			Methods.clean(params);
		} // finally			
		return regresar;
	} // actualizarFicticia
	
	protected boolean registraBitacora(Session sesion, Long idFicticia, Long idFicticaEstatus, String justificacion) throws Exception {
		TcManticFicticiasBitacoraDto bitFicticia= new TcManticFicticiasBitacoraDto(this.orden.getTicket(), justificacion, idFicticaEstatus, JsfBase.getIdUsuario(), idFicticia, -1L, this.orden.getTotal());
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
		} // finally
		return regresar;
	} // toSiguiente
	
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
	
	private Long registrarFactura(Session sesion) throws Exception {
		Long regresar              = -1L;
		TcManticFacturasDto factura= null;
		try {			
			factura= new TcManticFacturasDto();
			factura.setIdUsuario(JsfBase.getIdUsuario());
			factura.setIntentos(0L);
			factura.setCorreos("");
			factura.setObservaciones(this.justificacion);
			regresar= DaoFactory.getInstance().insert(sesion, factura);
		} // try
		finally {
			setMessageError("Error al registrar la factura.");
		} // finally
		return regresar;
	} // registrarFactura
	
	private boolean agregarContacto(Session sesion) throws Exception{
		boolean regresar                       = true;
		List<ClienteTipoContacto> correos      = null;
		TrManticClienteTipoContactoDto contacto= null;
		int count                              = 0;
		Long records                           = 1L;
		try {
			correos= toClientesTipoContacto();
			if(!correos.isEmpty()){
				for(ClienteTipoContacto tipoContacto: correos){
					if(tipoContacto.getValor().equals(this.correo.getDescripcion()))
						count++;
				} // for				
				records= correos.size() + 1L;
			} // if
			if(count== 0){
				contacto= new TrManticClienteTipoContactoDto();
				contacto.setIdCliente(this.idCliente);
				contacto.setIdTipoContacto(ETiposContactos.CORREO.getKey());
				contacto.setIdUsuario(JsfBase.getIdUsuario());
				contacto.setValor(this.correo.getDescripcion());
				contacto.setOrden(records);
				regresar= DaoFactory.getInstance().insert(sesion, contacto)>= 1L;
			} // else
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // agregarContacto
	
	public List<ClienteTipoContacto> toClientesTipoContacto() throws Exception {
		List<ClienteTipoContacto> regresar= null;
		Map<String, Object>params    = null;
		try {
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, "id_cliente=" + this.idCliente + " and id_tipo_contacto=" + ETiposContactos.CORREO.getKey());
			regresar= DaoFactory.getInstance().toEntitySet(ClienteTipoContacto.class, "TrManticClienteTipoContactoDto", "row", params, Constantes.SQL_TODOS_REGISTROS);
		} // try
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	} // toClientesTipoContacto
	
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
			/*try {
				CFDIFactory.getInstance().toSendMail(correos, factura.getIdFacturamaRegistro());
			} // try
			catch (Exception e) {				
				Error.mensaje(e);				
			} // catch*/
		} // try
		catch (Exception e) {			
			this.messageError= "";
			throw e;
		} // catch				
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
	}
	
	private boolean toClonarFiciticia(Session sesion, Long idEstatusFicticia) throws Exception { 
		boolean regresar          = false;
		Map<String, Object> params= null;
		try {
			params=new HashMap<>();
			Siguiente cuenta     = this.toSiguienteCuenta(sesion);			
			Siguiente consecutivo= this.toSiguiente(sesion);			
			params.put("idVenta", this.orden.getKey());
			this.orden.setKey(-1L);
			this.orden.setDia(new Date(Calendar.getInstance().getTimeInMillis()));
			this.orden.setConsecutivo(cuenta.toConsecutivo());			
			this.orden.setOrden(cuenta.getOrden());
			this.orden.setTicket(consecutivo.getConsecutivo());			
			this.orden.setCticket(consecutivo.getOrden());
			this.orden.setIdFicticiaEstatus(idEstatusFicticia);
			this.orden.setEjercicio(new Long(Fecha.getAnioActual()));						
			this.orden.setIdUsuario(JsfBase.getIdUsuario());
			this.orden.setObservaciones("");
			this.orden.setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
			this.orden.setIdTipoDocumento(1L);
			regresar= DaoFactory.getInstance().insert(sesion, this.orden)> 0L;
			if(regresar) {
				TcManticFacturasDto factura= (TcManticFacturasDto)DaoFactory.getInstance().toEntity(TcManticFacturasDto.class, "VistaFicticiasDto", "factura", params);
				factura.setIdFactura(-1L);
				factura.setCadenaOriginal(null);
				factura.setCertificacion(null);
				factura.setCertificadoDigital(null);
				factura.setCertificadoSat(null);
				factura.setFolio(null);
				factura.setFolioFiscal(null);
				factura.setIdFacturama(null);
				factura.setSelloCfdi(null);
				factura.setSelloSat(null);
				factura.setUltimoIntento(null);
				factura.setTimbrado(null);
				factura.setIntentos(0L);
				factura.setIdUsuario(JsfBase.getIdUsuario());
				factura.setObservaciones(null);
				factura.setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
  			regresar= DaoFactory.getInstance().insert(sesion, factura)> 0L;
				if(regresar) {
					this.orden.setIdFactura(factura.getIdFactura());
					if(DaoFactory.getInstance().update(sesion, this.orden)>= 1L){
						regresar= this.registraBitacora(sesion, this.orden.getIdFicticia(), idEstatusFicticia, "");
						if(regresar) {
							this.articulos= (List<Articulo>)DaoFactory.getInstance().toEntitySet(sesion, Articulo.class, "TcManticFicticiasDetallesDto", "detalle", params);
							for (Articulo articulo: this.articulos) 
								articulo.setIdComodin(-1L);
							this.toFillArticulos(sesion);
						} // if	
					} // if	
				} // if	
			} // if
		} // try
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	} // toClonarFicticia
} 