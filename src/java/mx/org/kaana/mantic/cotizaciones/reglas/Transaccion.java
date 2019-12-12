package mx.org.kaana.mantic.cotizaciones.reglas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.reglas.beans.Siguiente;
import mx.org.kaana.libs.facturama.reglas.TransaccionFactura;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.compras.ordenes.beans.Articulo;
import mx.org.kaana.mantic.db.dto.TcManticFicticiasBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticFicticiasDto;
import mx.org.kaana.mantic.db.dto.TcManticFicticiasDetallesDto;
import mx.org.kaana.mantic.enums.EEstatusCotizaciones;
import org.apache.log4j.Logger;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 7/05/2018
 *@time 03:29:13 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Transaccion extends TransaccionFactura {

  private static final Logger LOG= Logger.getLogger(Transaccion.class);		
	private TcManticFicticiasDto orden;	
	private List<Articulo> articulos;
	private String messageError;	
	private String justificacion;		
	
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
		try {
			idEstatusFactura= EEstatusCotizaciones.ELABORADA.getIdEstatusFicticia();
			params= new HashMap<>();
			if(this.orden!= null)
				params.put("idFicticia", this.orden.getIdFicticia());
			this.messageError= "Ocurrio un error en ".concat(accion.name().toLowerCase()).concat(" la factura.");
			switch(accion) {								
				case AGREGAR:
				case REGISTRAR:	
				case DESACTIVAR:
					idEstatusFactura= accion.equals(EAccion.AGREGAR) ? EEstatusCotizaciones.ELABORADA.getIdEstatusFicticia() : (accion.equals(EAccion.DESACTIVAR) ? this.orden.getIdFicticiaEstatus() : idEstatusFactura);
					regresar= this.orden.getIdFicticia()!= null && !this.orden.getIdFicticia().equals(-1L) ? actualizarFicticia(sesion, idEstatusFactura) : registrarFicticia(sesion, idEstatusFactura);					
					break;
				case MODIFICAR:
					regresar= actualizarFicticia(sesion, EEstatusCotizaciones.ELABORADA.getIdEstatusFicticia());					
					break;				
				case ELIMINAR:
					idEstatusFactura= EEstatusCotizaciones.CANCELADA.getIdEstatusFicticia();
					this.orden= (TcManticFicticiasDto) DaoFactory.getInstance().findById(sesion, TcManticFicticiasDto.class, this.orden.getIdFicticia());
					this.orden.setIdFicticiaEstatus(idEstatusFactura);					
					if(DaoFactory.getInstance().update(sesion, this.orden)>= 1L)
						regresar= registraBitacora(sesion, this.orden.getIdFicticia(), idEstatusFactura, this.justificacion);					
					break;															
				case NO_APLICA:
					params.put("idFicticia", this.orden.getIdFicticia());
					if(DaoFactory.getInstance().deleteAll(sesion, TcManticFicticiasBitacoraDto.class, params)>= 0) {
						if(DaoFactory.getInstance().deleteAll(sesion, TcManticFicticiasDetallesDto.class, params)>= 0)
							regresar= DaoFactory.getInstance().delete(sesion, this.orden)>= 1L;
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
		boolean regresar     = false;
		Siguiente consecutivo= null;
		Siguiente cuenta     = null;				
		Siguiente siguiente  = null;		
		try {						
			siguiente= this.toSiguienteCotizacion(sesion);
			this.orden.setCcotizacion(siguiente.getOrden());
			this.orden.setCotizacion(siguiente.getConsecutivo());
			consecutivo= this.toSiguiente(sesion);			
			this.orden.setTicket(consecutivo.getConsecutivo());			
			this.orden.setCticket(consecutivo.getOrden());			
			cuenta= this.toSiguienteCuenta(sesion);			
			this.orden.setConsecutivo(cuenta.toConsecutivo());			
			this.orden.setOrden(cuenta.getOrden());
			this.orden.setIdFicticiaEstatus(idEstatusFicticia);
			this.orden.setEjercicio(new Long(Fecha.getAnioActual()));									
			this.orden.setObservaciones(this.justificacion);
			if(DaoFactory.getInstance().insert(sesion, this.orden)>= 1L) {				
				regresar= registraBitacora(sesion, this.orden.getIdFicticia(), idEstatusFicticia, "");
				this.toFillArticulos(sesion);				
			} // if		
		} // try
		catch (Exception e) {			
			throw e;
		} // catch				
		return regresar;
	} // registrarFicticia
	
	private Siguiente toSiguienteCotizacion(Session sesion) throws Exception {
		Siguiente regresar        = null;
		Map<String, Object> params= null;
		try {
			params=new HashMap<>();
			params.put("ejercicio", this.getCurrentYear());
			params.put("idEmpresa", this.orden.getIdEmpresa());
			params.put("operador", this.getCurrentSign());
			Value next= DaoFactory.getInstance().toField(sesion, "TcManticVentasDto", "siguienteCotizacion", params, "siguiente");
			if(next.getData()!= null)
				regresar= new Siguiente(next.toLong());
			else
				regresar= new Siguiente(Configuracion.getInstance().isEtapaDesarrollo()? 900001L: 1L);
		} // try		
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	} // toSiguiente
	
	private boolean actualizarFicticia(Session sesion, Long idEstatusFicticia) throws Exception{
		boolean regresar         = false;
		Map<String, Object>params= null;		
		try {						
			this.orden.setObservaciones(this.justificacion);
			this.orden.setIdFicticiaEstatus(idEstatusFicticia);						
			regresar= DaoFactory.getInstance().update(sesion, this.orden)>= 1L;
			params= new HashMap<>();
			params.put("idVenta", this.orden.getIdVenta());						
			if(registraBitacora(sesion, this.orden.getIdFicticia(), idEstatusFicticia, "")) {
				params= new HashMap<>();
				params.put("idFicticia", this.orden.getIdFicticia());
				regresar= DaoFactory.getInstance().deleteAll(sesion, TcManticFicticiasDetallesDto.class, params)>= 0;
				toFillArticulos(sesion);
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
} 