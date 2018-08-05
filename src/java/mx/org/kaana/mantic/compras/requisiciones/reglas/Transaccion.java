package mx.org.kaana.mantic.compras.requisiciones.reglas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.compras.ordenes.beans.Articulo;
import mx.org.kaana.mantic.db.dto.TcManticRequisicionesBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticRequisicionesDetallesDto;
import mx.org.kaana.mantic.db.dto.TcManticRequisicionesDto;
import mx.org.kaana.mantic.db.dto.TcManticVentasBitacoraDto;
import mx.org.kaana.mantic.enums.EEstatusVentas;
import mx.org.kaana.mantic.ventas.beans.ClienteVenta;
import org.apache.log4j.Logger;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 7/05/2018
 *@time 03:29:13 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Transaccion extends IBaseTnx {

  private static final Logger LOG  = Logger.getLogger(Transaccion.class);
	private static final String VENTA= "VENTA";
	private TcManticRequisicionesBitacoraDto bitacora;
	private TcManticRequisicionesDto requisicion;	
	private List<Articulo> articulos;
	private String messageError;	
	private String justificacion;
	private Long idClienteNuevo;

	public Transaccion(TcManticRequisicionesBitacoraDto bitacora) {
		this.bitacora= bitacora;
	} // Transaccion
	
	public Transaccion(TcManticRequisicionesDto requisicion) {
		this(requisicion, "");
	}
	
	public Transaccion(TcManticRequisicionesDto requisicion, String justificacion) {
		this(requisicion, new ArrayList<Articulo>(), justificacion);
	} // Transaccion

	public Transaccion(TcManticRequisicionesDto requisicion, List<Articulo> articulos) {
		this(requisicion, articulos, "");
	}
	
	public Transaccion(TcManticRequisicionesDto requisicion, List<Articulo> articulos, String justificacion) {
		this.requisicion= requisicion;		
		this.articulos  = articulos;
	} // Transaccion

	public String getMessageError() {
		return messageError;
	} // Transaccion

	public void setMessageError(String messageError) {
		this.messageError = messageError;
	}	

	public Long getIdClienteNuevo() {
		return idClienteNuevo;
	}

	public TcManticRequisicionesDto getRequisicion() {
		return requisicion;
	}
	
	@Override
	protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {		
		boolean regresar          = false;
		Map<String, Object> params= null;
		Long idRequisicionEstatus       = null;
		try {
			idRequisicionEstatus= EEstatusVentas.ELABORADA.getIdEstatusVenta();
			params= new HashMap<>();
			if(this.requisicion!= null)
				params.put("idVenta", this.requisicion.getIdRequisicion());
			this.messageError= "Ocurrio un error en ".concat(accion.name().toLowerCase()).concat(" el ticket de venta.");
			switch(accion) {
				case AGREGAR:
				case REGISTRAR:			
					idRequisicionEstatus= accion.equals(EAccion.AGREGAR) ? EEstatusVentas.ABIERTA.getIdEstatusVenta() : idRequisicionEstatus;
					regresar= this.requisicion.getIdRequisicion()!= null && !this.requisicion.getIdRequisicion().equals(-1L) ? actualizarVenta(sesion, idRequisicionEstatus) : registrarVenta(sesion, idRequisicionEstatus);					
					break;
				case MODIFICAR:
					regresar= actualizarVenta(sesion, EEstatusVentas.ABIERTA.getIdEstatusVenta());					
					break;				
				case ELIMINAR:
					idRequisicionEstatus= EEstatusVentas.CANCELADA.getIdEstatusVenta();
					this.requisicion.setIdRequisicionEstatus(idRequisicionEstatus);
					if(DaoFactory.getInstance().update(sesion, this.requisicion)>= 1L)
						regresar= registraBitacora(sesion, this.requisicion.getIdRequisicion(), idRequisicionEstatus, this.justificacion);					
					break;
				case JUSTIFICAR:
					if(DaoFactory.getInstance().insert(sesion, this.bitacora)>= 1L){
						this.requisicion= (TcManticRequisicionesDto) DaoFactory.getInstance().findById(sesion, TcManticRequisicionesDto.class, this.bitacora.getIdRequisicion());
						this.requisicion.setIdRequisicionEstatus(this.bitacora.getIdRequisicionEstatus());
						regresar= DaoFactory.getInstance().update(sesion, this.requisicion)>= 1L;
					} // if
					break;												
				case REPROCESAR:
				case COPIAR:
					regresar= actualizarVenta(sesion, accion.equals(EAccion.REPROCESAR) ? EEstatusVentas.PAGADA.getIdEstatusVenta() : EEstatusVentas.CREDITO.getIdEstatusVenta());				
					break;				
			} // switch
			if(!regresar)
        throw new Exception("");
		} // try
		catch (Exception e) {		
			Error.mensaje(e);
			throw new Exception(this.messageError.concat("\n\n")+ e.getMessage());
		} // catch		
		if(this.requisicion!= null)
			LOG.info("Se genero de forma correcta la orden: "+ this.requisicion.getConsecutivo());
		return regresar;
	}	// ejecutar

	private boolean registrarVenta(Session sesion, Long idRequisicionEstatus) throws Exception{
		boolean regresar= false;
		Long consecutivo= -1L;
		try {
			consecutivo= this.toSiguiente(sesion);
			this.requisicion.setConsecutivo(consecutivo.toString());
			this.requisicion.setOrden(consecutivo);
			this.requisicion.setIdRequisicionEstatus(idRequisicionEstatus);
			this.requisicion.setEjercicio(new Long(Fecha.getAnioActual()));
			this.requisicion.setIdSolicita(JsfBase.getAutentifica().getPersona().getIdPersona());
			this.requisicion.setIdUsuario(JsfBase.getIdUsuario());
			this.requisicion.setIdEmpresa(JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
			regresar= DaoFactory.getInstance().insert(sesion, this.requisicion)>= 1L;
			regresar= registraBitacora(sesion, this.requisicion.getIdRequisicion(), idRequisicionEstatus, "");
			toFillArticulos(sesion);
		} // try
		catch (Exception e) {			
			throw e;
		} // catch				
		return regresar;
	} // registrarVenta
	
	private boolean actualizarVenta(Session sesion, Long idRequisicionEstatus) throws Exception{
		boolean regresar         = false;
		Map<String, Object>params= null;
		try {						
			this.requisicion.setIdRequisicionEstatus(idRequisicionEstatus);						
			regresar= DaoFactory.getInstance().update(sesion, this.requisicion)>= 1L;
			if(registraBitacora(sesion, this.requisicion.getIdRequisicion(), idRequisicionEstatus, "")){
				params= new HashMap<>();
				params.put("idRequisicion", this.requisicion.getIdRequisicion());
				regresar= DaoFactory.getInstance().deleteAll(sesion, TcManticRequisicionesDetallesDto.class, params)>= 1;
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
	} // actualizarVenta
	
	protected boolean registraBitacora(Session sesion, Long idRequisicion, Long idRequisicionEstatus, String justificacion) throws Exception{
		boolean regresar                         = false;
		TcManticRequisicionesBitacoraDto bitVenta= null;
		try {
			bitVenta= new TcManticRequisicionesBitacoraDto(-1L, justificacion, JsfBase.getIdUsuario(), idRequisicionEstatus, idRequisicion);
			regresar= DaoFactory.getInstance().insert(sesion, bitVenta)>= 1L;
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // registrarBitacora
	
	private void toFillArticulos(Session sesion) throws Exception {
		List<Articulo> todos= (List<Articulo>)DaoFactory.getInstance().toEntitySet(sesion, Articulo.class, "TcManticRequisicionesDetallesDto", "detalle", this.requisicion.toMap());
		for (Articulo item: todos) 
			if(this.articulos.indexOf(item)< 0)
				DaoFactory.getInstance().delete(sesion, item);
		for (Articulo articulo: this.articulos) {
			TcManticRequisicionesDetallesDto item= articulo.toRequisicionDetalle();
			item.setIdRequisicion(this.requisicion.getIdRequisicion());
			if(DaoFactory.getInstance().findIdentically(sesion, TcManticRequisicionesDetallesDto.class, item.toMap())== null) 
				DaoFactory.getInstance().insert(sesion, item);
		} // for
	} // toFillArticulos
	
	private Long toSiguiente(Session sesion) throws Exception {
		Long regresar             = 1L;
		Map<String, Object> params= null;
		try {
			params=new HashMap<>();
			params.put("ejercicio", Fecha.getAnioActual());
			params.put("idEmpresa", this.requisicion.getIdEmpresa());
			Value next= DaoFactory.getInstance().toField(sesion, "TcManticRequisicionesDto", "siguiente", params, "siguiente");
			if(next.getData()!= null)
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
} 