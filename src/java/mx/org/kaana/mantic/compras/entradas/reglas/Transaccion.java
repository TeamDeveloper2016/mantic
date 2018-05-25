package mx.org.kaana.mantic.compras.entradas.reglas;

import java.util.ArrayList;
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
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.compras.ordenes.beans.Articulo;
import mx.org.kaana.mantic.db.dto.TcManticAlmacenesArticulosDto;
import mx.org.kaana.mantic.db.dto.TcManticAlmacenesUbicacionesDto;
import mx.org.kaana.mantic.db.dto.TcManticNotasEntradasDto;
import mx.org.kaana.mantic.db.dto.TcManticNotasDetallesDto;
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
		boolean regresar          = false;
		Map<String, Object> params= new HashMap<>();
		try {
			params.put("idNotaEntrada", this.orden.getIdNotaEntrada());
			params.put("idOrdenCompra", this.orden.getIdOrdenCompra());
			this.messageError= "Ocurrio un error en ".concat(accion.name().toLowerCase()).concat(" la nota de entrada");
			switch(accion) {
				case AGREGAR:
					Long consecutivo= this.toSiguiente(sesion);
					this.orden.setConsecutivo(Fecha.getAnioActual()+ Cadena.rellenar(consecutivo.toString(), 5, '0', true));
					this.orden.setOrden(consecutivo);
					this.orden.setEjercicio(new Long(Fecha.getAnioActual()));
					if(this.orden.getIdDirecta().equals(1L))
						this.orden.setIdOrdenCompra(null);
  				if(this.aplicar)
						this.orden.setIdNotaEstatus(6L);
					regresar= DaoFactory.getInstance().insert(sesion, this.orden)>= 1L;
					this.toFillArticulos(sesion);
					break;
				case MODIFICAR:
					regresar= DaoFactory.getInstance().update(sesion, this.orden)>= 1L;
  				if(this.aplicar)
						this.orden.setIdNotaEstatus(6L);
					this.toFillArticulos(sesion);
					break;				
				case ELIMINAR:
					regresar= DaoFactory.getInstance().deleteAll(sesion, TcManticNotasDetallesDto.class, params)>= 1L;
					regresar= regresar && DaoFactory.getInstance().delete(sesion, this.orden)>= 1L;
					break;
			} // switch
			if(!regresar)
        throw new Exception(this.messageError);
		} // try
		catch (Exception e) {
      Error.mensaje(e);			
			throw new Exception(this.messageError);
		} // catch		
		LOG.info("Se genero de forma correcta la nota de entrada: "+ this.orden.getConsecutivo());
		return regresar;
	}	// ejecutar

	private void toFillArticulos(Session sesion) throws Exception {
		List<Articulo> todos= (List<Articulo>)DaoFactory.getInstance().toEntitySet(sesion, Articulo.class, "TcManticNotasDetallesDto", "detalle", this.orden.toMap());
		for (Articulo item: todos) 
			if(this.articulos.indexOf(item)< 0) 
				DaoFactory.getInstance().delete(sesion, item);
		for (Articulo articulo: this.articulos) {
			TcManticNotasDetallesDto item= articulo.toNotaDetalle();
			item.setIdNotaEntrada(this.orden.getIdNotaEntrada());
			if(DaoFactory.getInstance().findIdentically(sesion, TcManticNotasDetallesDto.class, item.toMap())== null) {
				DaoFactory.getInstance().insert(sesion, item);
				if(this.aplicar)
				  this.toAffectAlmacenes(sesion, item);
			} // if
		} // for
	}
	
	private Long toSiguiente(Session sesion) throws Exception {
		Long regresar= 1L;
		Map<String, Object> params=null;
		try {
			params=new HashMap<>();
			params.put("ejercicio", Fecha.getAnioActual());
			params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
			regresar= DaoFactory.getInstance().toField(sesion, "TcManticNotasEntradasDto", "siguiente", params, "siguiente").toLong();
		} // try
		catch (Exception e) {
			throw e;
		} // catch
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	}
	
	private void toAffectAlmacenes(Session sesion, TcManticNotasDetallesDto item) throws Exception {
		Map<String, Object> params= null;
		try {
			params=new HashMap<>();
			params.put("idAlmacen", this.orden.getIdAlmacen());
			params.put("idArticulo", item.getIdArticulo());
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
				DaoFactory.getInstance().update(ubicacion);
			} // if
		} // try
		catch (Exception e) {
			throw e;
		} // catch
		finally {
			Methods.clean(params);
		} // finally
	}
	
} 