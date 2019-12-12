package mx.org.kaana.mantic.catalogos.almacenes.transferencias.reglas;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.reglas.beans.Siguiente;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.compras.ordenes.beans.Articulo;
import mx.org.kaana.mantic.db.dto.TcManticArticulosDto;
import mx.org.kaana.mantic.db.dto.TcManticFaltantesDto;
import mx.org.kaana.mantic.db.dto.TcManticTransferenciasBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticTransferenciasDetallesDto;
import mx.org.kaana.mantic.db.dto.TcManticTransferenciasDto;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;

public class Transaccion extends ComunInventarios {

	private static final Log LOG=LogFactory.getLog(Transaccion.class);
	
  private TcManticTransferenciasDto dto;
	private Long idTransferenciaEstatus;
	private Long idFaltante;

	public Transaccion(Long idFaltante) {
		this(new TcManticTransferenciasDto(-1L));
		this.idFaltante= idFaltante;
	}
	
	public Transaccion(TcManticTransferenciasDto dto) {
		this(dto, 1L);
	}
	
	public Transaccion(TcManticTransferenciasDto dto, TcManticTransferenciasBitacoraDto bitacora) {
		this(dto, bitacora.getIdTransferenciaEstatus());
		this.bitacora = bitacora;
	}
	
	public Transaccion(TcManticTransferenciasDto dto, List<Articulo> articulos) {
		this(dto, dto.getIdTransferenciaEstatus());
		this.articulos= articulos;
	}
	
	public Transaccion(TcManticTransferenciasDto dto, Long idTransferenciaEstatus) {
		this.dto= dto;
		if(this.dto.getIdSolicito()!= null && this.dto.getIdSolicito()< 0L)
		  this.dto.setIdSolicito(null);
		this.idTransferenciaEstatus= idTransferenciaEstatus;
	}

  @Override
  protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {
    boolean regresar= false;
    try {			
    	this.messageError    = "Ocurrio un error en ".concat(accion.name().toLowerCase()).concat(" para transferencia de articulos.");
  		Siguiente consecutivo= null;
      switch (accion) {
        case ACTIVAR:
        case AGREGAR:
					consecutivo= this.toSiguiente(sesion);
					this.dto.setConsecutivo(consecutivo.getConsecutivo());
					this.dto.setOrden(consecutivo.getOrden());
          regresar= DaoFactory.getInstance().insert(sesion, this.dto).intValue()> 0;
					this.toFillArticulos(sesion, accion);
					this.bitacora= new TcManticTransferenciasBitacoraDto(-1L, "", JsfBase.getIdUsuario(), null, this.dto.getIdTransferenciaEstatus(), this.dto.getIdTransferencia());
					if(regresar)
            regresar= DaoFactory.getInstance().insert(sesion, bitacora).intValue()> 0;
					break;
        case MODIFICAR:
          this.dto.setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
					this.toFillArticulos(sesion, accion);
          regresar= DaoFactory.getInstance().update(sesion, this.dto).intValue()> 0;
          break;
        case ELIMINAR:
          //if (DaoFactory.getInstance().deleteAll(sesion, TcManticTransferenciasBitacoraDto.class, this.dto.toMap())> -1L) 
					this.dto.setIdTransferenciaEstatus(2L);
          regresar= DaoFactory.getInstance().update(sesion, this.dto)>= 1L;
					this.bitacora= new TcManticTransferenciasBitacoraDto(-1L, "", JsfBase.getIdUsuario(), null, this.dto.getIdTransferenciaEstatus(), this.dto.getIdTransferencia());
					if(regresar)
            regresar= DaoFactory.getInstance().insert(sesion, bitacora).intValue()> 0;
          break;
				case DEPURAR:
					regresar= DaoFactory.getInstance().delete(sesion, TcManticFaltantesDto.class, this.idFaltante)>= 1L;
					break;
        case REGISTRAR:
      		this.articulos= (List<Articulo>)DaoFactory.getInstance().toEntitySet(Articulo.class, "VistaAlmacenesTransferenciasDto", "detalle", dto.toMap());
					this.dto.setIdTransferenciaEstatus(this.idTransferenciaEstatus);
					if(this.idTransferenciaEstatus!= 1L)
					  this.toFillArticulos(sesion, accion);
					regresar= DaoFactory.getInstance().update(sesion, this.dto).intValue()> 0;
					if(regresar)
            regresar= DaoFactory.getInstance().insert(sesion, this.bitacora).intValue()> 0;
          // throw new RuntimeException("ERROR PROVACADO INTENCIONALMENTE");
          break;
      } // switch
      if(!regresar) 
        throw new Exception("");
    } // try
    catch (Exception e) {
      throw new Exception(this.messageError.concat("<br/>")+ e);
    } // catch		
    return regresar;
  } // ejecutar

	private Siguiente toSiguiente(Session sesion) throws Exception {
		Siguiente regresar        = null;
		Map<String, Object> params= null;
		try {
			params=new HashMap<>();
			params.put("ejercicio", this.getCurrentYear());
			params.put("idEmpresa", this.dto.getIdEmpresa());
			params.put("operador", this.getCurrentSign());
			Value next= DaoFactory.getInstance().toField(sesion, "TcManticTransferenciasDto", "siguiente", params, "siguiente");
			if(next.getData()!= null)
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
	}

	private void toFillArticulos(Session sesion, EAccion accion) throws Exception {
		List<Articulo> todos=(List<Articulo>) DaoFactory.getInstance().toEntitySet(sesion, Articulo.class, "VistaAlmacenesTransferenciasDto", "detalle", this.dto.toMap());
		for (Articulo item: todos) 
			if (this.articulos.indexOf(item)< 0) 
				DaoFactory.getInstance().delete(sesion, item.toTransferenciaDetalle());
		for (Articulo articulo: this.articulos) {
			TcManticTransferenciasDetallesDto item= articulo.toTransferenciaDetalle();
			item.setIdTransferencia(this.dto.getIdTransferencia());
			if (DaoFactory.getInstance().findIdentically(sesion, TcManticTransferenciasDetallesDto.class, item.toMap())== null) 
				DaoFactory.getInstance().insert(sesion, item);
			else 
				DaoFactory.getInstance().update(sesion, item);
			if(EAccion.ACTIVAR.equals(accion)) 
				this.toMovimientos(sesion, this.dto.getConsecutivo(), this.dto.getIdAlmacen(), this.dto.getIdDestino(), articulo, this.idTransferenciaEstatus);
			else
  			if(EAccion.REGISTRAR.equals(accion)) {
					TcManticArticulosDto umbrales= (TcManticArticulosDto)DaoFactory.getInstance().findById(TcManticArticulosDto.class, articulo.getIdArticulo());
					switch(this.idTransferenciaEstatus.intValue()) {
						case 3: // TRANSITO
    					this.toMovimientosAlmacenOrigen(sesion, this.dto.getConsecutivo(), this.dto.getIdAlmacen(), articulo, umbrales, this.idTransferenciaEstatus);
							break;
						case 4: // CANCELAR
    					this.toMovimientosAlmacenOrigen(sesion, this.dto.getConsecutivo(), this.dto.getIdAlmacen(), articulo, umbrales, this.idTransferenciaEstatus);
							break;
						case 5: // RECEPCION
    					this.toMovimientosAlmacenDestino(sesion, this.dto.getConsecutivo(), this.dto.getIdDestino(), articulo, umbrales, articulo.getCantidad());
							break;
					} // switch
				} // if
		} // for
	}
		
}
