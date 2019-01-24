package mx.org.kaana.mantic.facturas.reglas;

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
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.facturama.reglas.CFDIFactory;
import mx.org.kaana.libs.facturama.reglas.CFDIGestor;
import mx.org.kaana.libs.facturama.reglas.TransaccionFactura;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.KajoolBaseException;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.clientes.beans.ClienteTipoContacto;
import mx.org.kaana.mantic.compras.ordenes.beans.Articulo;
import mx.org.kaana.mantic.db.dto.TcManticFacturasDto;
import mx.org.kaana.mantic.db.dto.TcManticFicticiasBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticFicticiasDto;
import mx.org.kaana.mantic.db.dto.TcManticFicticiasDetallesDto;
import mx.org.kaana.mantic.db.dto.TrManticClienteTipoContactoDto;
import mx.org.kaana.mantic.enums.EEstatusFicticias;
import mx.org.kaana.mantic.enums.ETiposContactos;
import mx.org.kaana.mantic.facturas.beans.Correo;
import org.apache.log4j.Logger;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 7/05/2018
 *@time 03:29:13 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Transaccion extends IBaseTnx {

  private static final Logger LOG    = Logger.getLogger(Transaccion.class);
	private static final Long TIMBRADA = 3L;
	private static final Long CANCELADA= 5L;
	private TcManticFicticiasBitacoraDto bitacora;
	private TcManticFicticiasDto orden;	
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
					if(DaoFactory.getInstance().insert(sesion, this.bitacora)>= 1L){
						this.orden= (TcManticFicticiasDto) DaoFactory.getInstance().findById(sesion, TcManticFicticiasDto.class, this.bitacora.getIdFicticia());
						this.orden.setIdFicticiaEstatus(this.bitacora.getIdFicticiaEstatus());						
						regresar= DaoFactory.getInstance().update(sesion, this.orden)>= 1L;
						if(this.bitacora.getIdFicticiaEstatus().equals(TIMBRADA) && this.checkTotal(sesion)) {
							params= new HashMap<>();
							params.put("idFicticia", this.orden.getIdFicticia());
							factura= (TcManticFacturasDto) DaoFactory.getInstance().toEntity(sesion, TcManticFacturasDto.class, "TcManticFacturasDto", "detalle", params);
							if(factura!= null){
								params= new HashMap<>();
								params.put("correos", this.correos);
								params.put("comentarios", this.comentarios);								
								params.put("timbrado", new Timestamp(Calendar.getInstance().getTimeInMillis()));								
								DaoFactory.getInstance().update(sesion, TcManticFacturasDto.class, factura.getIdFactura(), params);
								generarTimbradoFactura(sesion, this.orden.getIdFicticia(), factura.getIdFactura(), this.correos);
							} // 
						} // if
						else 
							if(this.bitacora.getIdFicticiaEstatus().equals(CANCELADA)) {
								params= new HashMap<>();
								params.put("idFicticia", this.orden.getIdFicticia());
								factura= (TcManticFacturasDto) DaoFactory.getInstance().toEntity(sesion, TcManticFacturasDto.class, "TcManticFacturasDto", "detalle", params);
								if(factura!= null && factura.getIdFacturama()!= null)
									CFDIFactory.getInstance().cfdiRemove(factura.getIdFacturama());
								else
									throw new Exception("No fue posible cancelar la factura, por favor vuelva a intentarlo !");															
							} // else if
					} // if
					break;								
				case REPROCESAR:
					regresar= actualizarFicticia(sesion, EEstatusFicticias.PAGADA.getIdEstatusFicticia());				
					break;		
				case NO_APLICA:
					params= new HashMap<>();
					params.put("idFicticia", this.orden.getIdFicticia());
					if(DaoFactory.getInstance().deleteAll(sesion, TcManticFicticiasBitacoraDto.class, params)>= 0) {
						if(DaoFactory.getInstance().deleteAll(sesion, TcManticFicticiasDetallesDto.class, params)>= 0)
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
		if(this.orden!= null)
			LOG.info("Se genero de forma correcta la orden: "+ this.orden.getConsecutivo());
		return regresar;
	}	// ejecutar
	
	private boolean registrarFicticia(Session sesion, Long idEstatusFicticia) throws Exception {
		boolean regresar         = false;
		Long consecutivo         = -1L;
		Long idFactura           = -1L;
		Map<String, Object>params= null;
		try {									
			idFactura= registrarFactura(sesion);										
			if(idFactura>= 1L){
				consecutivo= this.toSiguiente(sesion);			
				this.orden.setConsecutivo(Fecha.getAnioActual() + Cadena.rellenar(consecutivo.toString(), 5, '0', true));			
				this.orden.setOrden(consecutivo);
				this.orden.setIdFicticiaEstatus(idEstatusFicticia);
				this.orden.setEjercicio(new Long(Fecha.getAnioActual()));						
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
	
	private boolean actualizarFicticia(Session sesion, Long idEstatusFicticia) throws Exception{
		boolean regresar           = false;
		Map<String, Object>params  = null;
		TcManticFacturasDto factura=null;
		try {						
			this.orden.setIdFicticiaEstatus(idEstatusFicticia);						
			regresar= DaoFactory.getInstance().update(sesion, this.orden)>= 1L;
			params= new HashMap<>();
			params.put("idFicticia", this.orden.getIdFicticia());
			factura= (TcManticFacturasDto) DaoFactory.getInstance().toEntity(sesion, TcManticFacturasDto.class, "TcManticFacturasDto", "detalle", params);
			factura.setIdVenta(null);
			factura.setObservaciones(this.justificacion);
			if(DaoFactory.getInstance().update(sesion, factura)>= 1L){
				if(registraBitacora(sesion, this.orden.getIdFicticia(), idEstatusFicticia, "")){
					params= new HashMap<>();
					params.put("idFicticia", this.orden.getIdFicticia());
					regresar= DaoFactory.getInstance().deleteAll(sesion, TcManticFicticiasDetallesDto.class, params)>= 1;
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
	
	private Long toSiguiente(Session sesion) throws Exception {
		Long regresar             = 1L;
		Map<String, Object> params= null;
		try {
			params=new HashMap<>();
			params.put("ejercicio", Fecha.getAnioActual());
			params.put("idEmpresa", this.orden.getIdEmpresa());
			Value next= DaoFactory.getInstance().toField(sesion, "TcManticFicticiasDto", "siguiente", params, "siguiente");
			if(next!= null && next.getData()!= null)
				regresar= next.toLong();
		} // try
		catch (Exception e) {
			throw e;
		} // catch
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
		catch (Exception e) {			
			throw e;
		} // catch		
		finally{
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
		catch (Exception e) {		
			throw e;
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
		return regresar;
	} // toClientesTipoContacto
	
	private void generarTimbradoFactura(Session sesion, Long idFicticia, Long idFactura, String correos) throws Exception {
		TransaccionFactura factura= null;
		CFDIGestor gestor         = null;
		try {
			gestor= new CFDIGestor(idFicticia);			
			factura= new TransaccionFactura();
			factura.setArticulos(gestor.toDetalleCfdiFicticia(sesion));
			factura.setCliente(gestor.toClienteCfdiFicticia(sesion));
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
			Error.mensaje(e);
			this.messageError= "";
			throw e;
		} // catch				
	} // generarTimbradoFactura

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
			Long consecutivo= this.toSiguiente(sesion);			
			params.put("idFicticia", this.orden.getKey());
			this.orden.setKey(-1L);
			this.orden.setConsecutivo(Fecha.getAnioActual() + Cadena.rellenar(consecutivo.toString(), 5, '0', true));			
			this.orden.setOrden(consecutivo);
			this.orden.setIdFicticiaEstatus(idEstatusFicticia);
			this.orden.setEjercicio(new Long(Fecha.getAnioActual()));						
			this.orden.setIdUsuario(JsfBase.getIdUsuario());
			this.orden.setObservaciones("");
			regresar= DaoFactory.getInstance().insert(sesion, this.orden)> 0L;
			if(regresar) {
				TcManticFacturasDto factura= (TcManticFacturasDto)DaoFactory.getInstance().toEntity(TcManticFacturasDto.class, "TcManticFacturasDto", "detalle", params);
				factura.setIdFicticia(this.orden.getKey());
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
  			regresar= DaoFactory.getInstance().insert(sesion, factura)> 0L;
				if(regresar) {
					regresar= this.registraBitacora(sesion, this.orden.getIdFicticia(), idEstatusFicticia, "");
					if(regresar) {
	      		this.articulos= (List<Articulo>)DaoFactory.getInstance().toEntitySet(sesion, Articulo.class, "TcManticFicticiasDetallesDto", "detalle", params);
						for (Articulo articulo: this.articulos) 
							articulo.setIdComodin(-1L);
				  	this.toFillArticulos(sesion);
					} // if	
				} // if	
			} // if
		} // try
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	}
	
} 