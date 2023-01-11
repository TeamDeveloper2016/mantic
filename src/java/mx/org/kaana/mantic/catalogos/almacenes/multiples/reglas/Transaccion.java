package mx.org.kaana.mantic.catalogos.almacenes.multiples.reglas;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.reglas.beans.Siguiente;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.compras.ordenes.beans.Articulo;
import mx.org.kaana.mantic.db.dto.TcManticTransferenciasDetallesDto;
import mx.org.kaana.mantic.db.dto.TcManticTransferenciasDto;
import mx.org.kaana.mantic.db.dto.TcManticTransferenciasMultiplesBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticTransferenciasMultiplesDetallesDto;
import mx.org.kaana.mantic.db.dto.TcManticTransferenciasMultiplesDto;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;

public class Transaccion extends ComunInventarios {

	private static final Log LOG=LogFactory.getLog(Transaccion.class);
	
  private TcManticTransferenciasMultiplesDto dto;
	private Long idTransferenciaMultiplesEstatus;

	public Transaccion(TcManticTransferenciasMultiplesDto dto) {
		this(dto, 1L);
	}
	
	public Transaccion(TcManticTransferenciasMultiplesDto dto, TcManticTransferenciasMultiplesBitacoraDto bitacora) {
		this(dto, bitacora.getIdTransferenciaMultipleEstatus());
		this.bitacora = bitacora;
	}
	
	public Transaccion(TcManticTransferenciasMultiplesDto dto, List<Articulo> articulos) {
		this(dto, dto.getIdTransferenciaMultipleEstatus());
		this.articulos= articulos;
	}
	
	public Transaccion(TcManticTransferenciasMultiplesDto dto, Long idTransferenciaEstatus) {
		this.dto= dto;
		if(this.dto.getIdSolicito()!= null && this.dto.getIdSolicito()< 0L)
		  this.dto.setIdSolicito(null);
		this.idTransferenciaMultiplesEstatus= idTransferenciaEstatus;
	}

  @Override
  protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {
    boolean regresar= Boolean.FALSE;
    try {			
    	this.messageError    = "Ocurrio un error en ".concat(accion.name().toLowerCase()).concat(" para transferencia multiple de articulos.");
  		Siguiente consecutivo= null;
      switch (accion) {
        case AGREGAR:
					consecutivo= this.toSiguiente(sesion);
					this.dto.setConsecutivo(consecutivo.getConsecutivo());
					this.dto.setEjercicio(consecutivo.getEjercicio());
					this.dto.setOrden(consecutivo.getOrden());
          regresar= DaoFactory.getInstance().insert(sesion, this.dto).intValue()> 0;
					this.toFillArticulos(sesion);
					this.bitacora= new TcManticTransferenciasMultiplesBitacoraDto(-1L, "", JsfBase.getIdUsuario(), null, this.dto.getIdTransferenciaMultipleEstatus(), this.dto.getIdTransferenciaMultiple());
					if(regresar)
            regresar= DaoFactory.getInstance().insert(sesion, bitacora).intValue()> 0;
					break;
        case MODIFICAR:
          this.dto.setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
					this.toFillArticulos(sesion);
          regresar= DaoFactory.getInstance().update(sesion, this.dto).intValue()> 0;
          break;
        case ELIMINAR:
					this.dto.setIdTransferenciaMultipleEstatus(2L);
          regresar= DaoFactory.getInstance().update(sesion, this.dto)>= 1L;
					this.bitacora= new TcManticTransferenciasMultiplesBitacoraDto(-1L, "", JsfBase.getIdUsuario(), null, this.dto.getIdTransferenciaMultipleEstatus(), this.dto.getIdTransferenciaMultiple());
					if(regresar)
            regresar= DaoFactory.getInstance().insert(sesion, bitacora).intValue()> 0;
          break;
        case REGISTRAR:
					this.dto.setIdTransferenciaMultipleEstatus(this.idTransferenciaMultiplesEstatus);
					if(DaoFactory.getInstance().update(sesion, this.dto).intValue()> 0) {
            regresar= DaoFactory.getInstance().insert(sesion, this.bitacora).intValue()> 0;
  					if(Objects.equals(this.idTransferenciaMultiplesEstatus, 3L))
              this.toTransferir(sesion);
          } // if  
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
		Map<String, Object> params= new HashMap<>();
		try {
			params.put("ejercicio", this.getCurrentYear());
			params.put("idEmpresa", this.dto.getIdEmpresa());
			params.put("operador", this.getCurrentSign());
			Value next= DaoFactory.getInstance().toField(sesion, "TcManticTransferenciasMultiplesDto", "siguiente", params, "siguiente");
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

	private void toFillArticulos(Session sesion) throws Exception {
		List<Articulo> todos=(List<Articulo>) DaoFactory.getInstance().toEntitySet(sesion, Articulo.class, "VistaTransferenciasMultiplesDto", "detalle", this.dto.toMap());
		for (Articulo item: todos) {
      int index= this.toIndexOf(item);
			if (index< 0) 
				DaoFactory.getInstance().delete(sesion, item.toTransferenciaDetalle());
    } // if  
		for (Articulo articulo: this.articulos) {
			TcManticTransferenciasMultiplesDetallesDto item= articulo.toTransferenciaMultipleDetalle();
			item.setIdTransferenciaMultiple(this.dto.getIdTransferenciaMultiple());
			if (DaoFactory.getInstance().findIdentically(sesion, TcManticTransferenciasMultiplesDetallesDto.class, item.toMap())== null) 
				DaoFactory.getInstance().insert(sesion, item);
			else 
				DaoFactory.getInstance().update(sesion, item);
		} // for
	}
  
  private int toIndexOf(Articulo articulo) {
    int regresar= -1;
    if(articulos!= null && !this.articulos.isEmpty()) {
      regresar= 0;
      while (regresar< this.articulos.size()) {
        if(Objects.equals(this.articulos.get(regresar).getIdArticulo(), articulo.getIdArticulo()) && 
           Objects.equals(this.articulos.get(regresar).getIdAlmacen(), articulo.getIdAlmacen()))
          break;
        regresar++;
      } // while 
      regresar= regresar>= articulos.size()? -1: regresar;
    } // if
    return regresar;
  }  

 	private void toTransferir(Session sesion) throws Exception {
    Map<String, Object> params                              = new HashMap<>();
    List<TcManticTransferenciasMultiplesDetallesDto> items  = null;
    List<TcManticTransferenciasMultiplesDetallesDto> detalle= new ArrayList<>();
    try {   
      params.put("idTransferenciaMultiple", this.dto.getIdTransferenciaMultiple());
      items= (List<TcManticTransferenciasMultiplesDetallesDto>)DaoFactory.getInstance().findViewCriteria(sesion, TcManticTransferenciasMultiplesDetallesDto.class, params);
      Long idAlmacen= -1L;
      for (TcManticTransferenciasMultiplesDetallesDto item: items) {
        if(!Objects.equals(idAlmacen, item.getIdAlmacen())) {
          if(!Objects.equals(idAlmacen, -1L) & !Objects.equals(idAlmacen, item.getIdAlmacen())) 
            this.toTransferencia(sesion, idAlmacen, detalle);
          idAlmacen= item.getIdAlmacen();
          detalle.clear();
        } // if
        detalle.add(item);
      } // for
      if(!Objects.equals(idAlmacen, -1L)) 
        this.toTransferencia(sesion, idAlmacen, detalle);
    } // try
    catch (Exception e) {
      throw e;
    } // catch	
    finally {
      Methods.clean(params);
      Methods.clean(items);
      Methods.clean(detalle);
    } // finally
  }

 	private void toTransferencia(Session sesion, Long idDestino, List<TcManticTransferenciasMultiplesDetallesDto> detalles) throws Exception {
    try {      
      Siguiente consecutivo= this.toContinua(sesion);
      TcManticTransferenciasDto transferencia= new TcManticTransferenciasDto(
        this.dto.getIdSolicito(), // Long idSolicito, 
        1L, // Long idTransferenciaEstatus, 
        2L, // Long idTransferenciaTipo, 
        consecutivo.getEjercicio(), // Long ejercicio, 
        consecutivo.getConsecutivo(), // String consecutivo, 
        JsfBase.getIdUsuario(), // Long idUsuario,
        this.dto.getIdAlmacen(), // Long idAlmacen, 
        this.dto.getObservaciones(), // String observaciones, 
        idDestino, // Long idDestino, 
        this.dto.getIdEmpresa(), // Long idEmpresa, 
        consecutivo.getOrden(), // Long orden, 
        -1L // Long idTransferencia              
      );
      DaoFactory.getInstance().insert(sesion, transferencia);
      for (TcManticTransferenciasMultiplesDetallesDto item: detalles) {
        TcManticTransferenciasDetallesDto detalle= new TcManticTransferenciasDetallesDto(
          item.getCodigo(), // String codigo, 
          0D, // Double cantidades, 
          -1L, // Long idTransferenciaDetalle, 
          item.getCantidad(), // Double cantidad, 
          item.getIdArticulo(), // Long idArticulo, 
          transferencia.getIdTransferencia(), // Long idTransferencia, 
          item.getNombre(), // String nombre, 
          item.getCaja() // Long caja
        );
        DaoFactory.getInstance().insert(sesion, detalle);
      } // for
    } // try
    catch (Exception e) {
      throw e;
    } // catch	
  }

	private Siguiente toContinua(Session sesion) throws Exception {
		Siguiente regresar        = null;
		Map<String, Object> params= new HashMap<>();
		try {
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
  
}
