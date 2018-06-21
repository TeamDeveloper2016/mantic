package mx.org.kaana.mantic.inventarios.devoluciones.reglas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Value;
import org.hibernate.Session;
import mx.org.kaana.kajool.enums.EAccion;
import static mx.org.kaana.kajool.enums.EAccion.AGREGAR;
import static mx.org.kaana.kajool.enums.EAccion.ELIMINAR;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.compras.ordenes.beans.Articulo;
import mx.org.kaana.mantic.db.dto.TcManticArticulosBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticArticulosDto;
import mx.org.kaana.mantic.db.dto.TcManticDevolucionesBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticDevolucionesDetallesDto;
import mx.org.kaana.mantic.db.dto.TcManticDevolucionesDto;
import mx.org.kaana.mantic.db.dto.TcManticInventariosDto;
import mx.org.kaana.mantic.db.dto.TcManticNotasBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticNotasDetallesDto;
import mx.org.kaana.mantic.db.dto.TcManticNotasEntradasDto;
import org.apache.log4j.Logger;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 7/05/2018
 *@time 03:29:13 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Transaccion extends IBaseTnx implements Serializable {

  private static final Logger LOG = Logger.getLogger(Transaccion.class);
	private static final long serialVersionUID=-6069204157451117543L;
 
	private TcManticDevolucionesDto orden;	
	private List<Articulo> articulos;
	private String messageError;
	private TcManticDevolucionesBitacoraDto bitacora;
	private boolean aplicar;

	public Transaccion(TcManticDevolucionesDto orden, TcManticDevolucionesBitacoraDto bitacora) {
		this(orden);
		this.bitacora= bitacora;
	}
	
	public Transaccion(TcManticDevolucionesDto orden) {
		this(orden, new ArrayList<Articulo>(), true);
	}

	public Transaccion(TcManticDevolucionesDto orden, List<Articulo> articulos, boolean aplicar) {
		this.orden    = orden;		
		this.articulos= articulos;
		this.aplicar  = aplicar;
	} 

	public String getMessageError() {
		return messageError;
	}

	@Override
	protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {		
		boolean regresar          = false;
		TcManticDevolucionesBitacoraDto bitacoraNota= null;
		Map<String, Object> params= null;
		try {
			this.messageError= "Ocurrio un error en ".concat(accion.name().toLowerCase()).concat(" la devolución de entrada.");
			if(this.orden!= null) {
				params= new HashMap<>();
				params.put("idNotaEntrada", this.orden.getIdNotaEntrada());
				params.put("idDevolucion", this.orden.getIdDevolucion());
			} // if
			switch(accion) {
				case AGREGAR:
					Long consecutivo= this.toSiguiente(sesion);
					this.orden.setConsecutivo(Fecha.getAnioActual()+ Cadena.rellenar(consecutivo.toString(), 5, '0', true));
					this.orden.setOrden(consecutivo);
					this.orden.setEjercicio(new Long(Fecha.getAnioActual()));
  				this.orden.setIdDevolucionEstatus(3L);
					regresar= DaoFactory.getInstance().insert(sesion, this.orden)>= 1L;
					bitacoraNota= new TcManticDevolucionesBitacoraDto(this.orden.getIdDevolucionEstatus(), "", this.orden.getIdDevolucion(), JsfBase.getIdUsuario(), -1L, this.orden.getConsecutivo(), this.orden.getTotal());
					regresar= DaoFactory.getInstance().insert(sesion, bitacoraNota)>= 1L;
					this.toFillArticulos(sesion);
					this.toCheckOrden(sesion);
					break;				
				case MODIFICAR:
  				if(this.aplicar) {
						this.orden.setIdDevolucionEstatus(3L);
  					bitacoraNota= new TcManticDevolucionesBitacoraDto(this.orden.getIdDevolucionEstatus(), "", this.orden.getIdDevolucion(), JsfBase.getIdUsuario(), -1L, this.orden.getConsecutivo(), this.orden.getTotal());
	  				regresar= DaoFactory.getInstance().insert(sesion, bitacoraNota)>= 1L;
					} // if	
					regresar= DaoFactory.getInstance().update(sesion, this.orden)>= 1L;
					this.toRemoveOrdenDetalle(sesion);
					this.toFillArticulos(sesion);
					this.toCheckOrden(sesion);
					break;
				case ELIMINAR:
					this.toRemoveOrdenDetalle(sesion);
					regresar= DaoFactory.getInstance().deleteAll(sesion, TcManticDevolucionesDetallesDto.class, params)>= 1L;
					regresar= DaoFactory.getInstance().delete(sesion, this.orden)>= 1L;
					this.orden.setIdDevolucionEstatus(2L);
					bitacoraNota= new TcManticDevolucionesBitacoraDto(this.orden.getIdDevolucionEstatus(), "", this.orden.getIdDevolucion(), JsfBase.getIdUsuario(), -1L, this.orden.getConsecutivo(), this.orden.getTotal());
					regresar= DaoFactory.getInstance().insert(sesion, bitacoraNota)>= 1L;
					this.toCheckOrden(sesion);
					break;
				case JUSTIFICAR:
					if(DaoFactory.getInstance().insert(sesion, this.bitacora)>= 1L) {
						this.orden.setIdDevolucionEstatus(this.bitacora.getIdDevolucionEstatus());
						regresar= DaoFactory.getInstance().update(sesion, this.orden)>= 1L;
						if(this.bitacora.getIdDevolucionEstatus().equals(2L)) {
							this.toRemoveOrdenDetalle(sesion);
							regresar= DaoFactory.getInstance().deleteAll(sesion, TcManticDevolucionesDetallesDto.class, params)>= 1L;
							regresar= DaoFactory.getInstance().delete(sesion, this.orden)>= 1L;
							this.orden.setIdDevolucionEstatus(2L);
							bitacoraNota= new TcManticDevolucionesBitacoraDto(this.orden.getIdDevolucionEstatus(), "", this.orden.getIdDevolucion(), JsfBase.getIdUsuario(), -1L, this.orden.getConsecutivo(), this.orden.getTotal());
							regresar= DaoFactory.getInstance().insert(sesion, bitacoraNota)>= 1L;
              this.toCheckOrden(sesion);
						} // if	
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
		LOG.info("Se genero de forma correcta la devolución de entrada: "+ this.orden.getConsecutivo());
		return regresar;
	}	// ejecutar
	
	private Long toSiguiente(Session sesion) throws Exception {
		Long regresar= 1L;
		Map<String, Object> params=null;
		try {
			params=new HashMap<>();
			params.put("ejercicio", Fecha.getAnioActual());
			params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
			Value next= DaoFactory.getInstance().toField(sesion, "TcManticDevolucionesDto", "siguiente", params, "siguiente");
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
	}	
	
	private void toFillArticulos(Session sesion) throws Exception {
		List<Articulo> todos= (List<Articulo>)DaoFactory.getInstance().toEntitySet(sesion, Articulo.class, "TcManticDevolucionesDetallesDto", "detalle", this.orden.toMap());
  	TcManticNotasEntradasDto nota= (TcManticNotasEntradasDto)DaoFactory.getInstance().findById(TcManticNotasEntradasDto.class, this.orden.getIdNotaEntrada());
		for (Articulo item: todos) 
			if(this.articulos.indexOf(item)< 0) {
				this.toAffectOrdenDetalle(sesion, item);
				DaoFactory.getInstance().delete(sesion, item);
			} // if
		for (Articulo articulo: this.articulos) {
			TcManticDevolucionesDetallesDto item= articulo.toDevolucionDetalle();
			if(item.getCantidad()> 0) {
				item.setIdDevolucion(this.orden.getIdDevolucion());
				if(DaoFactory.getInstance().findIdentically(sesion, TcManticDevolucionesDetallesDto.class, item.toMap())== null) {
					this.toAffectOrdenDetalle(sesion, articulo);
					if(item.isValid())
						DaoFactory.getInstance().update(sesion, item);
					else
						DaoFactory.getInstance().insert(sesion, item);
					this.toAffectAlmacenes(sesion, this.orden.getIdNotaEntrada(), articulo, nota.getIdAlmacen());
				} // if
		  } // if
		} // for
	}

	private void toAffectOrdenDetalle(Session sesion, Articulo articulo) throws Exception {
		if(articulo.getIdOrdenDetalle()!= null && articulo.getIdOrdenDetalle()> 0L) {
			TcManticNotasDetallesDto detalle= (TcManticNotasDetallesDto)DaoFactory.getInstance().findById(sesion, TcManticNotasDetallesDto.class, articulo.getIdOrdenDetalle());
      detalle.setCantidades(detalle.getCantidades()- articulo.getCantidad());
			DaoFactory.getInstance().update(sesion, detalle);
		} // if
	}
	
	private void toRemoveOrdenDetalle(Session sesion) throws Exception {
		List<Articulo> todos= (List<Articulo>)DaoFactory.getInstance().toEntitySet(sesion, Articulo.class, "TcManticDevolucionesDetallesDto", "detalle", this.orden.toMap());
		for (Articulo articulo: todos) {
			if(articulo.getIdOrdenDetalle()!= null && articulo.getIdOrdenDetalle()> 0L) {
				TcManticNotasDetallesDto detalle= (TcManticNotasDetallesDto)DaoFactory.getInstance().findById(sesion, TcManticNotasDetallesDto.class, articulo.getIdOrdenDetalle());
				detalle.setCantidades(detalle.getCantidades()+ articulo.getCantidad());
				DaoFactory.getInstance().update(sesion, detalle);
			} // if
		} // for
	}
	
	protected void toAffectAlmacenes(Session sesion, Long idNotaEntrada, Articulo item, Long idAlmacen) throws Exception {
		Map<String, Object> params= null;
		try {
			params=new HashMap<>();
			params.put("idArticulo", item.getIdArticulo());
			params.put("idNotaEntrada", idNotaEntrada);
			
			// registar el cambio de precios en la bitacora de articulo 
			TcManticArticulosDto global= (TcManticArticulosDto)DaoFactory.getInstance().findById(sesion, TcManticArticulosDto.class, item.getIdArticulo());
			
			// afectar el inventario general de articulos dentro del almacen
			TcManticInventariosDto inventario= (TcManticInventariosDto)DaoFactory.getInstance().findFirst(sesion, TcManticInventariosDto.class, "inventario", params);
			if(inventario== null)
				DaoFactory.getInstance().insert(sesion, new TcManticInventariosDto(JsfBase.getIdUsuario(), idAlmacen, item.getCantidad(), -1L, item.getIdArticulo(), 0D, 0D, item.getCantidad(), new Long(Calendar.getInstance().get(Calendar.YEAR))));
			else {
				inventario.setEntradas(inventario.getEntradas()- item.getCantidad());
				inventario.setStock(inventario.getStock()- item.getCantidad());
				DaoFactory.getInstance().update(sesion, inventario);
			} // else
			
			// afectar los precios del catalogo de articulos
			if(this.aplicar) {
  			TcManticArticulosBitacoraDto movimiento= new TcManticArticulosBitacoraDto(global.getIva(), JsfBase.getIdUsuario(), global.getMayoreo(), -1L, global.getMenudeo(), global.getCantidad()* -1L, global.getIdArticulo(), idNotaEntrada, global.getMedioMayoreo(), global.getPrecio(), global.getLimiteMedioMayoreo(), global.getLimiteMayoreo());
	  		DaoFactory.getInstance().insert(sesion, movimiento);
				TcManticArticulosBitacoraDto ultimo= (TcManticArticulosBitacoraDto)DaoFactory.getInstance().findFirst(sesion, TcManticArticulosBitacoraDto.class, "ultimo", params);
				if(ultimo!= null) {
					global.setPrecio(ultimo.getCosto());
					global.setMenudeo(ultimo.getMenudeo());
					global.setMedioMayoreo(ultimo.getMedioMayoreo());
					global.setMayoreo(ultimo.getMayoreo());
				} // if	
			} // if
			global.setStock(global.getStock()- item.getCantidad());
			DaoFactory.getInstance().update(sesion, global);
		} // try
		catch (Exception e) {
			throw e;
		} // catch
		finally {
			Methods.clean(params);
		} // finally
	}	
	
	private void toCheckOrden(Session sesion) throws Exception {
		try {
			sesion.flush();
			TcManticNotasEntradasDto notaEntrada= (TcManticNotasEntradasDto)DaoFactory.getInstance().findById(sesion, TcManticNotasEntradasDto.class, this.orden.getIdNotaEntrada());
			Value errors= DaoFactory.getInstance().toField(sesion, "TcManticNotasDetallesDto", "errores", this.orden.toMap(), "total");
			if(errors.toLong()!= null && errors.toLong()== 0)
				notaEntrada.setIdNotaEstatus(6L); // SALDADA
			else {
				errors= DaoFactory.getInstance().toField(sesion, "TcManticNotasDetallesDto", "iguales", this.orden.toMap(), "total");
  			if(errors.toLong()!= null && errors.toLong()== 0)
				  notaEntrada.setIdNotaEstatus(5L); // PARCIALIZADA
			  else
				  notaEntrada.setIdNotaEstatus(3L); // TERMINADA
			} // if	
			DaoFactory.getInstance().update(sesion, notaEntrada);
			TcManticNotasBitacoraDto estatus= new TcManticNotasBitacoraDto(-1L, "", JsfBase.getIdUsuario(), notaEntrada.getIdNotaEntrada(), notaEntrada.getIdNotaEstatus(), notaEntrada.getConsecutivo(), this.orden.getTotal());
			DaoFactory.getInstance().insert(sesion, estatus);
		} // try
		catch (Exception e) {
			throw e;
		} // catch
	} 
	
} 