package mx.org.kaana.mantic.compras.entradas.reglas;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.kajool.enums.EAccion;
import static mx.org.kaana.kajool.enums.EAccion.AGREGAR;
import static mx.org.kaana.kajool.enums.EAccion.ELIMINAR;
import static mx.org.kaana.kajool.enums.EAccion.MODIFICAR;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.compras.ordenes.beans.Articulo;
import mx.org.kaana.mantic.db.dto.TcManticAlmacenesArticulosDto;
import mx.org.kaana.mantic.db.dto.TcManticAlmacenesUbicacionesDto;
import mx.org.kaana.mantic.db.dto.TcManticArticulosBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticArticulosCodigosDto;
import mx.org.kaana.mantic.db.dto.TcManticArticulosDto;
import mx.org.kaana.mantic.db.dto.TcManticInventariosDto;
import mx.org.kaana.mantic.db.dto.TcManticNotasBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticNotasEntradasDto;
import mx.org.kaana.mantic.db.dto.TcManticNotasDetallesDto;
import mx.org.kaana.mantic.db.dto.TcManticOrdenesBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticOrdenesComprasDto;
import mx.org.kaana.mantic.db.dto.TcManticOrdenesDetallesDto;
import org.apache.log4j.Logger;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 7/05/2018
 *@time 03:29:13 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Transaccion extends IBaseTnx {

  private static final Logger LOG = Logger.getLogger(Transaccion.class);
 
	private TcManticNotasEntradasDto orden;	
	private List<Articulo> articulos;
	private boolean aplicar;
	private String messageError;
	private TcManticNotasBitacoraDto bitacora;

	public Transaccion(TcManticNotasBitacoraDto bitacora) {
		this.bitacora= bitacora;
	}
	
	public Transaccion(TcManticNotasEntradasDto orden) {
		this(orden, new ArrayList<Articulo>(), false);
	}

	public Transaccion(TcManticNotasEntradasDto orden, List<Articulo> articulos, boolean aplicar) {
		this.orden    = orden;		
		this.articulos= articulos;
		this.aplicar  = aplicar;
	} // Transaccion

	public String getMessageError() {
		return messageError;
	}

	@Override
	protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {		
		boolean regresar                     = false;
		TcManticNotasBitacoraDto bitacoraNota= null;
		Map<String, Object> params           = null;
		try {
			if(this.orden!= null) {
				params= new HashMap<>();
				params.put("idNotaEntrada", this.orden.getIdNotaEntrada());
				params.put("idOrdenCompra", this.orden.getIdOrdenCompra());
			} // if
			this.messageError= "Ocurrio un error en ".concat(accion.name().toLowerCase()).concat(" la nota de entrada.");
			switch(accion) {
				case AGREGAR:
					Long consecutivo= this.toSiguiente(sesion);
					this.orden.setConsecutivo(Fecha.getAnioActual()+ Cadena.rellenar(consecutivo.toString(), 5, '0', true));
					this.orden.setOrden(consecutivo);
					this.orden.setEjercicio(new Long(Fecha.getAnioActual()));
					if(this.orden.getIdDirecta().equals(1L))
						this.orden.setIdOrdenCompra(null);
  				if(this.aplicar)
						this.orden.setIdNotaEstatus(3L);
					regresar= DaoFactory.getInstance().insert(sesion, this.orden)>= 1L;
					bitacoraNota= new TcManticNotasBitacoraDto(-1L, "", JsfBase.getIdUsuario(), this.orden.getIdNotaEntrada(), this.orden.getIdNotaEstatus());
					regresar= DaoFactory.getInstance().insert(sesion, bitacoraNota)>= 1L;
					this.toFillArticulos(sesion);
					this.toCheckOrden(sesion);
					break;
				case MODIFICAR:
  				if(this.aplicar) {
						this.orden.setIdNotaEstatus(3L);
  					bitacoraNota= new TcManticNotasBitacoraDto(-1L, "", JsfBase.getIdUsuario(), this.orden.getIdNotaEntrada(), this.orden.getIdNotaEstatus());
	  				regresar= DaoFactory.getInstance().insert(sesion, bitacoraNota)>= 1L;
					} // if	
					regresar= DaoFactory.getInstance().update(sesion, this.orden)>= 1L;
					this.toRemoveOrdenDetalle(sesion);
					this.toFillArticulos(sesion);
					this.toCheckOrden(sesion);
					break;				
				case ELIMINAR:
					regresar= this.toNotExistsArticulosBitacora(sesion);
					if(regresar) {
//						this.toRemoveOrdenDetalle(sesion);
//						regresar= DaoFactory.getInstance().deleteAll(sesion, TcManticNotasDetallesDto.class, params)>= 1L;
//						regresar= DaoFactory.getInstance().delete(sesion, this.orden)>= 1L;
            this.orden.setIdNotaEstatus(2L);
						regresar= DaoFactory.getInstance().update(sesion, this.orden)>= 1L;
						bitacoraNota= new TcManticNotasBitacoraDto(-1L, "", JsfBase.getIdUsuario(), this.orden.getIdNotaEntrada(), 2L);
						regresar= DaoFactory.getInstance().insert(sesion, bitacoraNota)>= 1L;
//						this.toCheckOrden(sesion);
					}
					else
       			this.messageError= "No se puede eliminar la nota de entrada porque ya fue aplicada en los precios de los articulos.";
					break;
				case JUSTIFICAR:
					if(DaoFactory.getInstance().insert(sesion, this.bitacora)>= 1L) {
						this.orden= (TcManticNotasEntradasDto) DaoFactory.getInstance().findById(sesion, TcManticNotasEntradasDto.class, this.bitacora.getIdNotaEntrada());
						this.orden.setIdNotaEstatus(this.bitacora.getIdNotaEstatus());
						regresar= DaoFactory.getInstance().update(sesion, this.orden)>= 1L;
						if(this.bitacora.getIdNotaEstatus().equals(2L) || this.bitacora.getIdNotaEstatus().equals(4L)) {
							this.toRemoveOrdenDetalle(sesion);
              this.toCheckOrden(sesion);
						} // if	
						else
  						if(this.bitacora.getIdNotaEstatus().equals(3L)) {
            		for (Articulo articulo: this.articulos)
									this.toAffectAlmacenes(sesion, articulo.toNotaDetalle(), articulo);
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
		LOG.info("Se genero de forma correcta la nota de entrada: "+ this.orden.getConsecutivo());
		return regresar;
	}	// ejecutar

	private void toFillArticulos(Session sesion) throws Exception {
		List<Articulo> todos= (List<Articulo>)DaoFactory.getInstance().toEntitySet(sesion, Articulo.class, "TcManticNotasDetallesDto", "detalle", this.orden.toMap());
		for (Articulo item: todos) 
			if(this.articulos.indexOf(item)< 0) {
				this.toAffectOrdenDetalle(sesion, item);
				DaoFactory.getInstance().delete(sesion, item);
			} // if
		for (Articulo articulo: this.articulos) {
			TcManticNotasDetallesDto item= articulo.toNotaDetalle();
			item.setIdNotaEntrada(this.orden.getIdNotaEntrada());
			if(DaoFactory.getInstance().findIdentically(sesion, TcManticNotasDetallesDto.class, item.toMap())== null) {
				this.toAffectOrdenDetalle(sesion, articulo);
				if(item.isValid())
			    DaoFactory.getInstance().update(sesion, item);
				else
			    DaoFactory.getInstance().insert(sesion, item);
				if(this.aplicar)
				  this.toAffectAlmacenes(sesion, item, articulo);
			} // if
		} // for
	}

	private void toRemoveOrdenDetalle(Session sesion) throws Exception {
		List<Articulo> todos= (List<Articulo>)DaoFactory.getInstance().toEntitySet(sesion, Articulo.class, "TcManticNotasDetallesDto", "detalle", this.orden.toMap());
		for (Articulo articulo: todos) {
			if(articulo.getIdOrdenDetalle()!= null && articulo.getIdOrdenDetalle()> 0L) {
				TcManticOrdenesDetallesDto detalle= (TcManticOrdenesDetallesDto)DaoFactory.getInstance().findById(sesion, TcManticOrdenesDetallesDto.class, articulo.getIdOrdenDetalle());
				detalle.setCantidades(detalle.getCantidades()+ articulo.getCantidad());
				detalle.setImportes(Numero.toRedondearSat(detalle.getImportes()+ articulo.getImporte()));
				detalle.setPrecios(Numero.toRedondearSat(detalle.getCosto()- articulo.getCosto()));
				DaoFactory.getInstance().update(sesion, detalle);
				//DaoFactory.getInstance().delete(sesion, TcManticOrdenesDetallesDto.class, articulo.getIdComodin());
			} // if
		} // for
	}
	
	private void toAffectOrdenDetalle(Session sesion, Articulo articulo) throws Exception {
		if(articulo.getIdOrdenDetalle()!= null && articulo.getIdOrdenDetalle()> 0L) {
			TcManticOrdenesDetallesDto detalle= (TcManticOrdenesDetallesDto)DaoFactory.getInstance().findById(sesion, TcManticOrdenesDetallesDto.class, articulo.getIdOrdenDetalle());
      detalle.setCantidades(detalle.getCantidades()- articulo.getCantidad());
      detalle.setImportes(Numero.toRedondearSat(detalle.getImportes()- articulo.getImporte()));
      detalle.setPrecios(Numero.toRedondearSat(articulo.getValor()- articulo.getCosto()));
			DaoFactory.getInstance().update(sesion, detalle);
		} // if
	}
	
	private Long toSiguiente(Session sesion) throws Exception {
		Long regresar= 1L;
		Map<String, Object> params=null;
		try {
			params=new HashMap<>();
			params.put("ejercicio", Fecha.getAnioActual());
			params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
			Value next= DaoFactory.getInstance().toField(sesion, "TcManticNotasEntradasDto", "siguiente", params, "siguiente");
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
	
	private void toAffectAlmacenes(Session sesion, TcManticNotasDetallesDto item, Articulo codigos) throws Exception {
		Map<String, Object> params= null;
		try {
			params=new HashMap<>();
			params.put("idAlmacen", this.orden.getIdAlmacen());
			params.put("idArticulo", item.getIdArticulo());
			params.put("idProveedor", this.orden.getIdProveedor());
			TcManticAlmacenesArticulosDto ubicacion= (TcManticAlmacenesArticulosDto)DaoFactory.getInstance().findFirst(sesion, TcManticAlmacenesArticulosDto.class,  params, "ubicacion");
			if(ubicacion== null) {
			  TcManticAlmacenesUbicacionesDto general= (TcManticAlmacenesUbicacionesDto)DaoFactory.getInstance().findFirst(sesion, TcManticAlmacenesUbicacionesDto.class, params, "general");
				if(general== null) {
  				general= new TcManticAlmacenesUbicacionesDto("GENERAL", "", "GENERAL", "", "", JsfBase.getAutentifica().getPersona().getIdUsuario(), this.orden.getIdAlmacen(), -1L);
					DaoFactory.getInstance().insert(sesion, general);
				} // if	
			  Entity entity= (Entity)DaoFactory.getInstance().toEntity(sesion, "TcManticArticulosDto", "inventario", params);
				TcManticAlmacenesArticulosDto articulo= new TcManticAlmacenesArticulosDto(entity.toLong("minimo"), -1L, general.getIdUsuario(), general.getIdAlmacen(), entity.toLong("maximo"), general.getIdAlmacenUbicacion(), item.getIdArticulo(), item.getCantidad());
				DaoFactory.getInstance().insert(sesion, articulo);
		  } // if
			else { 
				ubicacion.setStock(ubicacion.getStock()+ item.getCantidad());
				DaoFactory.getInstance().update(sesion, ubicacion);
			} // if
			// registar el cambio de precios en la bitacora de articulo 
			TcManticArticulosDto global= (TcManticArticulosDto)DaoFactory.getInstance().findById(sesion, TcManticArticulosDto.class, item.getIdArticulo());
			TcManticArticulosBitacoraDto movimiento= new TcManticArticulosBitacoraDto(global.getIva(), JsfBase.getIdUsuario(), global.getMayoreo(), -1L, global.getMenudeo(), global.getCantidad(), global.getIdArticulo(), this.orden.getIdNotaEntrada(), global.getMedioMayoreo(), global.getPrecio(), global.getLimiteMedioMayoreo(), global.getLimiteMayoreo());
			DaoFactory.getInstance().insert(sesion, movimiento);
			
			// afectar el inventario general de articulos dentro del almacen
			TcManticInventariosDto inventario= (TcManticInventariosDto)DaoFactory.getInstance().findFirst(TcManticInventariosDto.class, "inventario", params);
			if(inventario== null)
				DaoFactory.getInstance().insert(sesion, new TcManticInventariosDto(JsfBase.getIdUsuario(), this.orden.getIdAlmacen(), item.getCantidad(), -1L, item.getIdArticulo(), 0L, item.getCantidad(), 0L, new Long(Calendar.getInstance().get(Calendar.YEAR))));
			else {
				inventario.setEntradas(inventario.getEntradas()+ item.getCantidad());
				inventario.setStock(inventario.getStock()+ item.getCantidad());
				DaoFactory.getInstance().update(sesion, inventario);
			} // else
			
			// afectar los precios del catalogo de articulos
			if(!Cadena.isVacio(codigos.getSat()))
			  global.setSat(codigos.getSat());
			global.setPrecio(Numero.toRedondear(item.getCosto()));
			global.setMenudeo(Numero.toRedondear(item.getCosto()+ Constantes.PORCENTAJE_MENUDEO));
			global.setMedioMayoreo(Numero.toRedondear(item.getCosto()+ Constantes.PORCENTAJE_MEDIO_MAYOREO));
			global.setMayoreo(Numero.toRedondear(item.getCosto()+ Constantes.PORCENTAJE_MAYOREO));
			global.setStock(global.getStock()+ item.getCantidad());
			DaoFactory.getInstance().update(sesion, global);
			
			// afectar el catalogo de codigos del proveedor
			TcManticArticulosCodigosDto remplazo= (TcManticArticulosCodigosDto)DaoFactory.getInstance().findFirst(TcManticArticulosCodigosDto.class, "codigo", params);
			if(remplazo== null) {
				Value next= DaoFactory.getInstance().toField(sesion, "TcManticArticulosCodigosDto", "siguiente", params, "siguiente");
				if(next.getData()== null)
					next.setData(1L);
				DaoFactory.getInstance().insert(sesion, new TcManticArticulosCodigosDto(codigos.getCodigo(), this.orden.getIdProveedor(), JsfBase.getIdUsuario(), 2L, "", -1L, next.toLong(), codigos.getIdArticulo()));
			} // if	
			else {
				remplazo.setCodigo(codigos.getCodigo());
				DaoFactory.getInstance().update(sesion, remplazo);
			} // else	
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
			if(this.orden.getIdDirecta().equals(2L)) {
        TcManticOrdenesComprasDto ordenCompra= (TcManticOrdenesComprasDto)DaoFactory.getInstance().findById(sesion, TcManticOrdenesComprasDto.class, this.orden.getIdOrdenCompra());
  		  Value errors= DaoFactory.getInstance().toField(sesion, "VistaNotasEntradasDto", "errores", this.orden.toMap(), "total");
			  if(errors.toLong()!= null && errors.toLong()== 0) {
				  ordenCompra.setIdOrdenEstatus(7L); // CONFRONTADA
					TcManticNotasBitacoraDto registro= new TcManticNotasBitacoraDto(-1L, "", JsfBase.getIdUsuario(), this.orden.getIdNotaEntrada(), 3L);
					DaoFactory.getInstance().insert(sesion, registro);
				} // if	
				else
					ordenCompra.setIdOrdenEstatus(5L); // INCOMPLETA
				DaoFactory.getInstance().update(sesion, ordenCompra);
				TcManticOrdenesBitacoraDto estatus= new TcManticOrdenesBitacoraDto(ordenCompra.getIdOrdenEstatus(), "", JsfBase.getIdUsuario(), this.orden.getIdOrdenCompra(), -1L);
				DaoFactory.getInstance().insert(sesion, estatus);
			} // if
		} // try
		catch (Exception e) {
			throw e;
		} // catch
	} 
	
	private boolean toNotExistsArticulosBitacora(Session sesion) throws Exception {
		boolean regresar= true;
		Value total= DaoFactory.getInstance().toField(sesion, "TcManticArticulosBitacoraDto", "existe", this.orden.toMap(), "total");
		if(total.getData()!= null)
		  regresar= total.toLong()<= 0;
		return regresar;
	}
	
	private void toApplyNotaEntrada(Session sesion) throws Exception {
		for (Articulo articulo: this.articulos) {
			TcManticNotasDetallesDto item= articulo.toNotaDetalle();
			item.setIdNotaEntrada(this.orden.getIdNotaEntrada());
		  this.toAffectAlmacenes(sesion, item, articulo);
		} // for
		this.orden.setIdNotaEstatus(3L);
		TcManticNotasBitacoraDto nota= new TcManticNotasBitacoraDto(-1L, "", JsfBase.getIdUsuario(), this.orden.getIdNotaEntrada(), this.orden.getIdNotaEstatus());
		DaoFactory.getInstance().insert(sesion, nota);
	}
	
} 