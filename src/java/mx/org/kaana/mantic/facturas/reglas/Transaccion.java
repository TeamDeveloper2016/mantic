package mx.org.kaana.mantic.facturas.reglas;

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
import mx.org.kaana.mantic.db.dto.TcManticFicticiasBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticFicticiasDto;
import mx.org.kaana.mantic.db.dto.TcManticFicticiasDetallesDto;
import mx.org.kaana.mantic.enums.EEstatusFicticias;
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
	private TcManticFicticiasBitacoraDto bitacora;
	private TcManticFicticiasDto orden;	
	private List<Articulo> articulos;
	private String messageError;	
	private String justificacion;

	public Transaccion(TcManticFicticiasBitacoraDto bitacora) {
		this.bitacora= bitacora;
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
		boolean regresar          = false;
		Map<String, Object> params= null;
		Long idEstatusVenta       = null;
		try {
			idEstatusVenta= EEstatusFicticias.ELABORADA.getIdEstatusFicticia();
			params= new HashMap<>();
			if(this.orden!= null)
				params.put("idFicticia", this.orden.getIdFicticia());
			this.messageError= "Ocurrio un error en ".concat(accion.name().toLowerCase()).concat(" la factura.");
			switch(accion) {				
				case AGREGAR:
				case REGISTRAR:			
					idEstatusVenta= accion.equals(EAccion.AGREGAR) ? EEstatusFicticias.ABIERTA.getIdEstatusFicticia() : idEstatusVenta;
					regresar= this.orden.getIdFicticia()!= null && !this.orden.getIdFicticia().equals(-1L) ? actualizarVenta(sesion, idEstatusVenta) : registrarVenta(sesion, idEstatusVenta);					
					break;
				case MODIFICAR:
					regresar= actualizarVenta(sesion, EEstatusFicticias.ABIERTA.getIdEstatusFicticia());					
					break;				
				case ELIMINAR:
					idEstatusVenta= EEstatusFicticias.CANCELADA.getIdEstatusFicticia();
					this.orden= (TcManticFicticiasDto) DaoFactory.getInstance().findById(sesion, TcManticFicticiasDto.class, this.orden.getIdFicticia());
					this.orden.setIdFicticiaEstatus(idEstatusVenta);					
					if(DaoFactory.getInstance().update(sesion, this.orden)>= 1L)
						regresar= registraBitacora(sesion, this.orden.getIdFicticia(), idEstatusVenta, this.justificacion);					
					break;
				case JUSTIFICAR:
					if(DaoFactory.getInstance().insert(sesion, this.bitacora)>= 1L){
						this.orden= (TcManticFicticiasDto) DaoFactory.getInstance().findById(sesion, TcManticFicticiasDto.class, this.bitacora.getIdFicticia());
						this.orden.setIdFicticiaEstatus(this.bitacora.getIdFicticiaEstatus());
						regresar= DaoFactory.getInstance().update(sesion, this.orden)>= 1L;
					} // if
					break;								
				case REPROCESAR:
					regresar= actualizarVenta(sesion, EEstatusFicticias.PAGADA.getIdEstatusFicticia());				
					break;		
				case NO_APLICA:
					params= new HashMap<>();
					params.put("idFicticia", this.orden.getIdFicticia());
					if(DaoFactory.getInstance().deleteAll(sesion, TcManticFicticiasBitacoraDto.class, params)>= 0){
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
			throw new Exception(this.messageError.concat("\n\n")+ e.getMessage());
		} // catch		
		if(this.orden!= null)
			LOG.info("Se genero de forma correcta la orden: "+ this.orden.getConsecutivo());
		return regresar;
	}	// ejecutar
	
	private boolean registrarVenta(Session sesion, Long idEstatusVenta) throws Exception{
		boolean regresar          = false;
		Long consecutivo          = -1L;
		try {						
			consecutivo= this.toSiguiente(sesion);			
			this.orden.setConsecutivo(consecutivo);			
			this.orden.setOrden(consecutivo);
			this.orden.setIdFicticiaEstatus(idEstatusVenta);
			this.orden.setEjercicio(new Long(Fecha.getAnioActual()));			
			regresar= DaoFactory.getInstance().insert(sesion, this.orden)>= 1L;
			regresar= registraBitacora(sesion, this.orden.getIdFicticia(), idEstatusVenta, "");
			toFillArticulos(sesion);
		} // try
		catch (Exception e) {			
			throw e;
		} // catch				
		return regresar;
	} // registrarVenta
	
	private boolean actualizarVenta(Session sesion, Long idEstatusVenta) throws Exception{
		boolean regresar         = false;
		Map<String, Object>params= null;
		try {						
			this.orden.setIdFicticiaEstatus(idEstatusVenta);						
			regresar= DaoFactory.getInstance().update(sesion, this.orden)>= 1L;
			if(registraBitacora(sesion, this.orden.getIdFicticia(), idEstatusVenta, "")){
				params= new HashMap<>();
				params.put("idFicticia", this.orden.getIdFicticia());
				regresar= DaoFactory.getInstance().deleteAll(sesion, TcManticFicticiasDetallesDto.class, params)>= 1;
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
	
	protected boolean registraBitacora(Session sesion, Long idFicticia, Long idFicticaEstatus, String justificacion) throws Exception{
		boolean regresar                  = false;
		TcManticFicticiasBitacoraDto bitVenta= null;
		try {
			bitVenta= new TcManticFicticiasBitacoraDto(-1L, justificacion, JsfBase.getIdUsuario(), idFicticia, idFicticaEstatus, this.orden.getConsecutivo(), this.orden.getTotal());
			regresar= DaoFactory.getInstance().insert(sesion, bitVenta)>= 1L;
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // registrarBitacora
	
	private void toFillArticulos(Session sesion) throws Exception {
		List<Articulo> todos= (List<Articulo>)DaoFactory.getInstance().toEntitySet(sesion, Articulo.class, "TcManticFicticiasDetallesDto", "detalle", this.orden.toMap());
		for (Articulo item: todos) 
			if(this.articulos.indexOf(item)< 0)
				DaoFactory.getInstance().delete(sesion, item);
		for (Articulo articulo: this.articulos) {
			TcManticFicticiasDetallesDto item= articulo.toFicticiaDetalle();
			item.setIdFicticia(this.orden.getIdFicticia());
			if(DaoFactory.getInstance().findIdentically(sesion, TcManticFicticiasDetallesDto.class, item.toMap())== null) 
				DaoFactory.getInstance().insert(sesion, item);
			else
				DaoFactory.getInstance().update(sesion, item);
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