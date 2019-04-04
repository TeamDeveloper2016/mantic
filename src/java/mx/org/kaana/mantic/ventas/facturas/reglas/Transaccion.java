package mx.org.kaana.mantic.ventas.facturas.reglas;

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
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.facturama.reglas.CFDIFactory;
import mx.org.kaana.libs.facturama.reglas.CFDIGestor;
import mx.org.kaana.libs.facturama.reglas.TransaccionFactura;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.KajoolBaseException;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.clientes.beans.ClienteTipoContacto;
import mx.org.kaana.mantic.compras.ordenes.beans.Articulo;
import mx.org.kaana.mantic.db.dto.TcManticFacturasDto;
import mx.org.kaana.mantic.db.dto.TcManticVentasBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticVentasDto;
import mx.org.kaana.mantic.db.dto.TcManticVentasDetallesDto;
import mx.org.kaana.mantic.db.dto.TrManticClienteTipoContactoDto;
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
	private static final Long TIMBRADA = 3L;
	private static final Long CANCELADA= 5L;
	private TcManticVentasBitacoraDto bitacora;
	private TcManticVentasDto orden;	
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
	
	public Transaccion(TcManticVentasBitacoraDto bitacora) { 
		this(bitacora, "", "");
	} // Transaccion
	
	public Transaccion(TcManticVentasBitacoraDto bitacora, String correos, String comentarios) {
		this.bitacora   = bitacora;
		this.correos    = correos;
		this.comentarios= comentarios;
	} // Transaccion
	
	public Transaccion(TcManticVentasDto orden) {
		this(orden, "");
	}
	
	public Transaccion(TcManticVentasDto orden, String justificacion) {
		this(orden, new ArrayList<Articulo>(), justificacion);
	} // Transaccion

	public Transaccion(TcManticVentasDto orden, List<Articulo> articulos) {		
		this(orden, articulos, "");
	}
	
	public Transaccion(TcManticVentasDto orden, List<Articulo> articulos, String justificacion) { 		
		this.orden        = orden;		
		this.articulos    = articulos;
		this.justificacion= justificacion;
	} // Transaccion
	
	public String getMessageError() {
		return messageError;
	} // Transaccion

	public void setMessageError(String messageError) {
		this.messageError = messageError;
	}	
	
	public TcManticVentasDto getOrden() {
		return orden;
	}	
	
	@Override
	protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {		
		boolean regresar           = false;
		Map<String, Object> params = null;
		Long idEstatusFactura      = null;
		TcManticFacturasDto factura= null;
		try {
			idEstatusFactura= EEstatusVentas.ABIERTA.getIdEstatusVenta();
			params= new HashMap<>();
			if(this.orden!= null)
				params.put("idVenta", this.orden.getIdVenta());
			this.messageError= "Ocurrio un error en ".concat(accion.name().toLowerCase()).concat(" la factura.");
			switch(accion) {				
				case COPIAR:
					regresar= this.toClonarVenta(sesion, idEstatusFactura);
					break;
				case AGREGAR:
				case REGISTRAR:	
				case DESACTIVAR:
					idEstatusFactura= accion.equals(EAccion.AGREGAR) ? EEstatusVentas.ABIERTA.getIdEstatusVenta() : (accion.equals(EAccion.DESACTIVAR) ? this.orden.getIdVentaEstatus() : idEstatusFactura);
					regresar= this.orden.getIdVenta()!= null && !this.orden.getIdVenta().equals(-1L) ? actualizarVenta(sesion, idEstatusFactura) : registrarVenta(sesion, idEstatusFactura);					
					break;
				case MODIFICAR:
					regresar= actualizarVenta(sesion, EEstatusVentas.ABIERTA.getIdEstatusVenta());					
					break;				
				case ELIMINAR:
					idEstatusFactura= EEstatusVentas.CANCELADA.getIdEstatusVenta();
					this.orden= (TcManticVentasDto) DaoFactory.getInstance().findById(sesion, TcManticVentasDto.class, this.orden.getIdVenta());
					this.orden.setIdVentaEstatus(idEstatusFactura);					
					if(DaoFactory.getInstance().update(sesion, this.orden)>= 1L)
						regresar= registraBitacora(sesion, this.orden.getIdVenta(), idEstatusFactura, this.justificacion);					
					break;
				case JUSTIFICAR:		
					if(DaoFactory.getInstance().insert(sesion, this.bitacora)>= 1L) {
						this.orden= (TcManticVentasDto) DaoFactory.getInstance().findById(sesion, TcManticVentasDto.class, this.bitacora.getIdVenta());
						this.orden.setIdVentaEstatus(this.bitacora.getIdVentaEstatus());						
						regresar= DaoFactory.getInstance().update(sesion, this.orden)>= 1L;
						if(this.bitacora.getIdVentaEstatus().equals(TIMBRADA) && this.checkTotal(sesion)) {
							params.put("idVenta", this.orden.getIdVenta());
							factura= (TcManticFacturasDto) DaoFactory.getInstance().toEntity(sesion, TcManticFacturasDto.class, "TcManticFacturasDto", "detalle", params);
							if(factura!= null) {
								params.put("correos", this.correos);
								params.put("comentarios", this.comentarios);								
								params.put("timbrado", new Timestamp(Calendar.getInstance().getTimeInMillis()));								
								DaoFactory.getInstance().update(sesion, TcManticFacturasDto.class, factura.getIdFactura(), params);
								this.generarTimbradoFactura(sesion, this.orden.getIdVenta(), factura.getIdFactura(), this.correos);
							} // 
						} // if
						else 
							if(this.bitacora.getIdVentaEstatus().equals(CANCELADA)) {
								params.put("idVenta", this.orden.getIdVenta());
								factura= (TcManticFacturasDto) DaoFactory.getInstance().toEntity(sesion, TcManticFacturasDto.class, "TcManticFacturasDto", "detalle", params);
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
					//regresar= actualizarVenta(sesion, EEstatusVentas.TIMBRADA.getIdEstatusVenta());				
					break;		
				case NO_APLICA:
					params.put("idVenta", this.orden.getIdVenta());
					if(DaoFactory.getInstance().deleteAll(sesion, TcManticVentasBitacoraDto.class, params)>= 0) {
						if(DaoFactory.getInstance().deleteAll(sesion, TcManticVentasDetallesDto.class, params)>= 0)
							regresar= DaoFactory.getInstance().delete(sesion, this.orden)>= 1L;
					} // if					
					break;
				case COMPLEMENTAR: 
					regresar= agregarContacto(sesion);
					break;
			} // switch
			if(!regresar)
        throw new Exception("");
		} // try
		catch (Exception e) {		
			Error.mensaje(e);
			throw new Exception(this.messageError.concat("<br/>")+ e.getMessage());
		} // catch		
		finally {
			Methods.clean(params);
		} // finally
		if(this.orden!= null)
			LOG.info("Se genero de forma correcta la orden: "+ this.orden.getConsecutivo());
		return regresar;
	}	// ejecutar
	
	private boolean registrarVenta(Session sesion, Long idEstatusVenta) throws Exception {
		boolean regresar         = false;
		Long consecutivo         = -1L;
		Long idFactura           = -1L;
		Map<String, Object>params= null;
		try {									
			idFactura= registrarFactura(sesion);										
			if(idFactura>= 1L){
				consecutivo= this.toSiguiente(sesion);			
				this.orden.setConsecutivo(consecutivo);			
				this.orden.setOrden(consecutivo);
				this.orden.setIdVentaEstatus(idEstatusVenta);
				this.orden.setEjercicio(new Long(Fecha.getAnioActual()));						
				this.orden.setIdFactura(idFactura);
				if(DaoFactory.getInstance().insert(sesion, this.orden)>= 1L){					
					params= new HashMap<>();
					params.put("idVenta", this.orden.getIdVenta());
					if(DaoFactory.getInstance().update(sesion, TcManticFacturasDto.class, idFactura, params)>= 1L){					
						regresar= registraBitacora(sesion, this.orden.getIdVenta(), idEstatusVenta, "");
						this.toFillArticulos(sesion);
					} // if					
				} // if
			} // if
		} // try
		catch (Exception e) {			
			throw e;
		} // catch				
		return regresar;
	} // registrarVenta
	
	private boolean actualizarVenta(Session sesion, Long idEstatusVenta) throws Exception{
		boolean regresar           = false;
		Map<String, Object>params  = null;
		TcManticFacturasDto factura=null;
		try {						
			this.orden.setIdVentaEstatus(idEstatusVenta);						
			regresar= DaoFactory.getInstance().update(sesion, this.orden)>= 1L;
			params= new HashMap<>();
			params.put("idVenta", this.orden.getIdVenta());
			factura= (TcManticFacturasDto) DaoFactory.getInstance().toEntity(sesion, TcManticFacturasDto.class, "TcManticFacturasDto", "detalle", params);
			factura.setIdVenta(null);
			factura.setObservaciones(this.justificacion);
			if(DaoFactory.getInstance().update(sesion, factura)>= 1L){
				if(registraBitacora(sesion, this.orden.getIdVenta(), idEstatusVenta, "")){
					params= new HashMap<>();
					params.put("idVenta", this.orden.getIdVenta());
					regresar= DaoFactory.getInstance().deleteAll(sesion, TcManticVentasDetallesDto.class, params)>= 0;
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
	} // actualizarVenta
	
	protected boolean registraBitacora(Session sesion, Long idVenta, Long idVentaEstatus, String justificacion) throws Exception {
		TcManticVentasBitacoraDto bitVenta= new TcManticVentasBitacoraDto(this.orden.getConsecutivo(), justificacion, idVentaEstatus, JsfBase.getIdUsuario(), idVenta, -1L, this.orden.getTotal());
		return DaoFactory.getInstance().insert(sesion, bitVenta)>= 1L;
	} // registrarBitacora
	
	private void toFillArticulos(Session sesion) throws Exception {
		List<Articulo> todos= (List<Articulo>)DaoFactory.getInstance().toEntitySet(sesion, Articulo.class, "TcManticVentasDetallesDto", "detalle", this.orden.toMap());
		for (Articulo item: todos) 
			if(this.articulos.indexOf(item)< 0)
				DaoFactory.getInstance().delete(sesion, item);
		for (Articulo articulo: this.articulos) {
			if(articulo.isValid()) {
				TcManticVentasDetallesDto item= articulo.toVentaDetalle();
				item.setIdVenta(this.orden.getIdVenta());
				if(DaoFactory.getInstance().findIdentically(sesion, TcManticVentasDetallesDto.class, item.toMap())== null) 
					DaoFactory.getInstance().insert(sesion, item);
				else
					DaoFactory.getInstance().update(sesion, item);
			} // if
		} // for
	} // toFillArticulos
	
	private Long toSiguiente(Session sesion) throws Exception {
		Long regresar             = 1L;
		Map<String, Object> params= null;
		try {
			params=new HashMap<>();
			params.put("ejercicio", Fecha.getAnioActual());
			params.put("dia", Fecha.getHoyEstandar());
			params.put("idEmpresa", this.orden.getIdEmpresa());
			Value next= DaoFactory.getInstance().toField(sesion, "TcManticVentasDto", "siguiente", params, "siguiente");
			if(next.getData()!= null)
				regresar= next.toLong();
		} // try		
		finally {
			Methods.clean(params);
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
	
	private void generarTimbradoFactura(Session sesion, Long idVenta, Long idFactura, String correos) throws Exception {
		TransaccionFactura factura= null;
		CFDIGestor gestor         = null;
		try {
			actualizarClienteFacturama(sesion, idVenta);
			gestor= new CFDIGestor(idVenta);			
			factura= new TransaccionFactura();
			factura.setArticulos(gestor.toDetalleCfdiVentas(sesion));
			factura.setCliente(gestor.toClienteCfdiVenta(sesion));
			factura.getCliente().setIdFactura(idFactura);
			factura.generarCfdi(sesion);	
			try {
				CFDIFactory.getInstance().toSendMail(correos, factura.getIdFacturamaRegistro());
			} // try
			catch (Exception e) {				
				Error.mensaje(e);				
			} // catch						
		} // try
		catch (Exception e) {			
			this.messageError= "";
			throw e;
		} // catch				
	} // generarTimbradoFactura

	private void actualizarClienteFacturama(Session sesion, Long idVenta) throws Exception{		
		CFDIGestor gestor= new CFDIGestor(idVenta);
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
			params.put("idVenta", this.orden.getIdVenta());
			Value detalle= DaoFactory.getInstance().toField(sesion, "TcManticVentasDetallesDto", "total", params, "total");
			if(detalle!= null && detalle.getData()!= null)
				sumDetalle= detalle.toDouble();
			Value total= DaoFactory.getInstance().toField(sesion, "TcManticVentasDto", "total", params, "total");
			if(total!= null && total.getData()!= null)
				sumTotal= total.toDouble();
		} // try
		finally {
			Methods.clean(params);
		} // finally
		regresar= Objects.equals(sumTotal, sumDetalle);
		if(!regresar) {
			LOG.warn("Diferencias en los importes de la factura: "+ this.orden.getIdVenta()+ " verificar situacion, total ["+ sumTotal+ "] detalle["+ sumDetalle+ "]");
			throw new KajoolBaseException("No se puede timbrar porque el importe total difiere de los importes del detalle de la factura !");	
		} // if	
		return regresar;
	}
	
	private boolean toClonarVenta(Session sesion, Long idEstatusVenta) throws Exception { 
		boolean regresar          = false;
		Map<String, Object> params= null;
		try {
			params=new HashMap<>();
			Long consecutivo= this.toSiguiente(sesion);			
			params.put("idVenta", this.orden.getKey());
			this.orden.setKey(-1L);
			this.orden.setConsecutivo(consecutivo);			
			this.orden.setOrden(consecutivo);
			this.orden.setIdVentaEstatus(idEstatusVenta);
			this.orden.setEjercicio(new Long(Fecha.getAnioActual()));						
			this.orden.setIdUsuario(JsfBase.getIdUsuario());
			this.orden.setObservaciones("");
			this.orden.setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
			regresar= DaoFactory.getInstance().insert(sesion, this.orden)> 0L;
			if(regresar) {
				TcManticFacturasDto factura= (TcManticFacturasDto)DaoFactory.getInstance().toEntity(TcManticFacturasDto.class, "TcManticFacturasDto", "detalle", params);
				factura.setIdVenta(this.orden.getKey());
				factura.setIdVenta(null);
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
						regresar= this.registraBitacora(sesion, this.orden.getIdVenta(), idEstatusVenta, "");
						if(regresar) {
							this.articulos= (List<Articulo>)DaoFactory.getInstance().toEntitySet(sesion, Articulo.class, "TcManticVentasDetallesDto", "detalle", params);
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
	} // toClonarVenta
} 