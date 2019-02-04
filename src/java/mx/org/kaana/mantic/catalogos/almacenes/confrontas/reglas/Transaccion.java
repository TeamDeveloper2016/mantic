package mx.org.kaana.mantic.catalogos.almacenes.confrontas.reglas;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.compras.ordenes.beans.Articulo;
import mx.org.kaana.mantic.db.dto.TcManticAlmacenesArticulosDto;
import mx.org.kaana.mantic.db.dto.TcManticAlmacenesUbicacionesDto;
import mx.org.kaana.mantic.db.dto.TcManticArticulosDto;
import mx.org.kaana.mantic.db.dto.TcManticConfrontasDto;
import mx.org.kaana.mantic.db.dto.TcManticInventariosDto;
import mx.org.kaana.mantic.db.dto.TcManticTransferenciasBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticConfrontasDetallesDto;
import mx.org.kaana.mantic.db.dto.TcManticTransferenciasDetallesDto;
import mx.org.kaana.mantic.db.dto.TcManticTransferenciasDto;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;

public class Transaccion extends IBaseTnx {

	private static final Log LOG= LogFactory.getLog(Transaccion.class);

  private TcManticConfrontasDto dto;
  private TcManticTransferenciasDto transferencia;
	private List<Articulo> articulos;
  private TcManticTransferenciasBitacoraDto bitacora;
  private String messageError;

	public Transaccion(TcManticConfrontasDto dto, List<Articulo> articulos) {
		this.dto= dto;
		this.articulos= articulos;
	}
	
  @Override
  protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {
    boolean regresar= false;
    try {			
    	this.messageError= "Ocurrio un error en ".concat(accion.name().toLowerCase()).concat(" para transferencia de articulos.");
  		Long consecutivo  = 0L;
			this.transferencia= (TcManticTransferenciasDto)DaoFactory.getInstance().findById(TcManticTransferenciasDto.class, this.dto.getIdTransferencia());
      switch (accion) {
				case AGREGAR:
					consecutivo= this.toSiguiente(sesion);
					this.dto.setConsecutivo(this.dto.getEjercicio()+ Cadena.rellenar(consecutivo.toString(), 5, '0', true));
					this.dto.setOrden(consecutivo);
          regresar= DaoFactory.getInstance().insert(sesion, this.dto).intValue()> 0;
					this.toFillArticulos(sesion, accion);
					this.bitacora= new TcManticTransferenciasBitacoraDto(-1L, "", JsfBase.getIdUsuario(), null, 5L, this.dto.getIdTransferencia());
					if(regresar)
            regresar= DaoFactory.getInstance().insert(sesion, this.bitacora).intValue()> 0;
					this.transferencia.setIdTransferenciaEstatus(bitacora.getIdTransferenciaEstatus());
					if(regresar) 
            regresar= DaoFactory.getInstance().update(sesion, this.transferencia).intValue()> 0;
          break;
				case PROCESAR:
				case MODIFICAR:
          break;
      } // switch
      if(!regresar) 
        throw new Exception("");
    } // try
    catch (Exception e) {
      throw new Exception(this.messageError.concat("<br/>")+ e.getMessage());
    } // catch		
    return regresar;
  } // ejecutar

	private Long toSiguiente(Session sesion) throws Exception {
		Long regresar= 1L;
		Map<String, Object> params=null;
		try {
			params=new HashMap<>();
			params.put("ejercicio", Fecha.getAnioActual());
			params.put("idEmpresa", this.transferencia.getIdEmpresa());
			Value next= DaoFactory.getInstance().toField(sesion, "VistaConfrontasDto", "siguiente", params, "siguiente");
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

	private void toFillArticulos(Session sesion, EAccion accion) throws Exception {
		List<Articulo> todos=(List<Articulo>) DaoFactory.getInstance().toEntitySet(sesion, Articulo.class, "VistaConfrontasDto", "detalle", this.dto.toMap());
		for (Articulo item: todos) 
			if (this.articulos.indexOf(item)< 0) 
				DaoFactory.getInstance().delete(sesion, item.toConfrontasDetalle());
		for (Articulo articulo: this.articulos) {
			TcManticConfrontasDetallesDto item= articulo.toConfrontasDetalle();
			item.setIdConfronta(this.dto.getIdConfronta());
			if (DaoFactory.getInstance().findIdentically(sesion, TcManticConfrontasDetallesDto.class, item.toMap())== null) 
				DaoFactory.getInstance().insert(sesion, item);
			else 
				DaoFactory.getInstance().update(sesion, item);
			this.toAffectTransferenciaDetalle(sesion, articulo);
			if(!EAccion.AGREGAR.equals(accion)) {
				TcManticArticulosDto umbrales= (TcManticArticulosDto)DaoFactory.getInstance().findById(TcManticArticulosDto.class, articulo.getIdArticulo());
				switch(accion) {
					case MODIFICAR: // RECIBIR
					case PROCESAR: // INCOMPLETA
						this.toMovimientosAlmacenDestino(sesion, item, umbrales);
						break;
					case DEPURAR: // AUTORIZAR
						break;
				} // switch
			} // if
		} // for
	}

	private void toMovimientosAlmacenDestino(Session sesion, TcManticConfrontasDetallesDto articulo, TcManticArticulosDto umbrales) throws Exception {
		Map<String, Object> params= null;
		try {
			//Afectar el almacen destino sumando los articulos que fueron agregados
			params=new HashMap<>();
			params.put("idAlmacen", this.transferencia.getIdDestino());
			params.put("idArticulo", articulo.getIdArticulo());
			TcManticAlmacenesArticulosDto origen= (TcManticAlmacenesArticulosDto)DaoFactory.getInstance().findIdentically(TcManticAlmacenesArticulosDto.class, params);
			if(origen== null) 
				origen= this.toCreateAlmacenArticulo(sesion, articulo, this.transferencia.getIdDestino(), umbrales);
			origen.setStock(Numero.toRedondearSat(origen.getStock()+ articulo.getCantidad()));
			DaoFactory.getInstance().update(sesion, origen);
			TcManticInventariosDto inventario= (TcManticInventariosDto)DaoFactory.getInstance().toEntity(TcManticInventariosDto.class, "TcManticInventariosDto", "inventario", params);
			if(inventario== null)
			  this.toCreateInvetario(sesion, articulo, this.transferencia.getIdDestino(), true);
			else {
				inventario.setSalidas(Numero.toRedondearSat(inventario.getSalidas()+ articulo.getCantidad()));
				inventario.setStock(Numero.toRedondearSat(inventario.getStock()- articulo.getCantidad()));
  			DaoFactory.getInstance().update(sesion, inventario);
			} // if
		} // try
		finally {
			Methods.clean(params);
		} // finally
	}
	
	private TcManticAlmacenesArticulosDto toCreateAlmacenArticulo(Session sesion,  TcManticConfrontasDetallesDto articulo, Long idAlmacen, TcManticArticulosDto umbrales) throws Exception {
    TcManticAlmacenesArticulosDto regresar= new TcManticAlmacenesArticulosDto(
			umbrales.getMinimo(), // Double minimo, 
			-1L, // Long idAlmacenArticulo, 
			JsfBase.getIdUsuario(), // Long idUsuario, 
			idAlmacen, // Long idAlmacen, 
			umbrales.getMaximo(), // Double maximo, 
			this.toUbicacion(sesion, idAlmacen, articulo.getIdArticulo()), // Long idAlmacenUbicacion, 
			articulo.getIdArticulo(), // Long idArticulo, 
			0D // Double stock
		);
		DaoFactory.getInstance().insert(sesion, regresar);
		return regresar;
	}
		
	private TcManticInventariosDto toCreateInvetario(Session sesion,  TcManticConfrontasDetallesDto articulo, Long idAlmacen, boolean inicial) throws Exception {
    TcManticInventariosDto regresar= new TcManticInventariosDto(
			JsfBase.getIdUsuario(), // Long idUsuario, 
			idAlmacen, // Long idAlmacen, 
			0D, // Double entradas, 
			-1L, // Long idInventario, 
			articulo.getIdArticulo(), // Long idArticulo, 
			inicial? articulo.getCantidad(): articulo.getCantidad()* -1D, // Double inicial, 
			inicial? articulo.getCantidad(): articulo.getCantidad()* -1D, // Double stock, 
			articulo.getCantidad(), // Double salidas, 
			new Long(Fecha.getAnioActual()), // Long ejercicio, 
			1L // Long idAutomatico
		);
		DaoFactory.getInstance().insert(sesion, regresar);
		return regresar;
	}

	private Long toUbicacion(Session sesion, Long idAlmacen, Long idArticulo) throws Exception {
		Long regresar= -1L;
		Map<String, Object> params= null;
		try {
			params=new HashMap<>();
			params.put("idAlmacen", idAlmacen);
			params.put("idArticulo", idArticulo);
			TcManticAlmacenesArticulosDto ubicacion= (TcManticAlmacenesArticulosDto)DaoFactory.getInstance().findFirst(sesion, TcManticAlmacenesArticulosDto.class, params, "ubicacion");
			if(ubicacion== null) {
				TcManticAlmacenesUbicacionesDto general= (TcManticAlmacenesUbicacionesDto)DaoFactory.getInstance().findFirst(sesion, TcManticAlmacenesUbicacionesDto.class, params, "general");
				if(general== null) {
					general= new TcManticAlmacenesUbicacionesDto("GENERAL", "", "GENERAL", "", "", JsfBase.getAutentifica().getPersona().getIdUsuario(), this.transferencia.getIdDestino(), -1L);
					DaoFactory.getInstance().insert(sesion, general);
				} // if	
				regresar= general.getIdAlmacenUbicacion();
			} // if		
		} // try
		catch (Exception e) {
			throw e;
		} // catch
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	}
	
	private void toAffectTransferenciaDetalle(Session sesion, Articulo articulo) throws Exception {
		if(articulo.getIdOrdenDetalle()!= null && articulo.getIdOrdenDetalle()> 0L) {
			TcManticTransferenciasDetallesDto detalle= (TcManticTransferenciasDetallesDto)DaoFactory.getInstance().findById(sesion, TcManticTransferenciasDetallesDto.class, articulo.getIdOrdenDetalle());
      detalle.setCantidades(detalle.getCantidades()- articulo.getCantidad());
			DaoFactory.getInstance().update(sesion, detalle);
		} // if
	}


}
