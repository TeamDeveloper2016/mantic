package mx.org.kaana.mantic.catalogos.conteos.reglas;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.hibernate.Session;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.inventarios.beans.ArticuloAlmacen;
import mx.org.kaana.mantic.db.dto.TcManticAlmacenesUbicacionesDto;
import mx.org.kaana.mantic.db.dto.TcManticArticulosDto;
import mx.org.kaana.mantic.db.dto.TcManticConteosBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticConteosDetallesDto;
import mx.org.kaana.mantic.db.dto.TcManticConteosDto;
import mx.org.kaana.mantic.db.dto.TcManticFaltantesDto;
import mx.org.kaana.mantic.db.dto.TcManticInventariosDto;
import mx.org.kaana.mantic.db.dto.TcManticMovimientosDto;
import org.apache.log4j.Logger;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 03/02/2024
 *@time 13:29:04 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Transaccion extends IBaseTnx implements Serializable {

  private static final Logger LOG= Logger.getLogger(Transaccion.class);
	private static final long serialVersionUID= -3186367186737677671L;
 
	private TcManticConteosDto conteo;	
	private TcManticConteosBitacoraDto bitacora;
	private String messageError;

	public Transaccion(TcManticConteosDto conteo) {
    this(conteo, null);
	} // Transaccion
  
	public Transaccion(TcManticConteosDto conteo, TcManticConteosBitacoraDto bitacora) {
    this.conteo      = conteo;
		this.bitacora    = bitacora;
  	this.messageError= "Ocurrio un error en el conteo";
	} // Transaccion
	
	public String getMessageError() {
		return messageError;
	} 

	@Override
	protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {		
		boolean regresar          = Boolean.FALSE;
		Map<String, Object> params= new HashMap<>();
		try {
			switch(accion) {
				case PROCESAR:
          regresar= this.toProcesar(sesion);
					break;
				case DESACTIVAR:
          regresar= this.toDesactivar(sesion);
					break;
				case ELIMINAR:
					this.conteo.setIdConteoEstatus(4L);
					regresar= DaoFactory.getInstance().update(sesion, this.conteo)>= 1L;
					break;
				case JUSTIFICAR:
					if(DaoFactory.getInstance().insert(sesion, this.bitacora)>= 1L) {
						this.conteo.setIdConteoEstatus(this.bitacora.getIdConteoEstatus());
						regresar= DaoFactory.getInstance().update(sesion, this.conteo)>= 1L;
					} // if
					break;
				case DEPURAR:
          params.put("idConteo", this.conteo.getIdConteo());
          DaoFactory.getInstance().deleteAll(TcManticConteosBitacoraDto.class, params);
          DaoFactory.getInstance().deleteAll(TcManticConteosDetallesDto.class, params);
          regresar= DaoFactory.getInstance().deleteAll(TcManticConteosDto.class, params)>= 1L;
					break;
			} // switch
			if(!regresar)
        throw new Exception("Error al procesar el conteo");
		} // try
		catch (Exception e) {		
			Error.mensaje(e);
      this.messageError= "Error: ["+ e+ "]";
			throw new Exception(this.messageError.concat("<br/>")+ e);
		} // catch		
		return regresar;
	}	
  
  private boolean toDesactivar(Session sesion) throws Exception {
    Boolean regresar= Boolean.FALSE;
    try {   
      this.conteo.setIdConteoEstatus(6L);
      DaoFactory.getInstance().update(sesion, this.conteo);
      this.bitacora= new TcManticConteosBitacoraDto(
        this.messageError, // String justificacion, 
        this.conteo.getIdUsuario(), // Long idUsuario, 
        this.conteo.getIdConteo(), // Long idConteo, 
        -1L, // Long idConteoBitacora, 
        this.conteo.getIdConteoEstatus()// Long idConteoEstatus
      );          
      regresar= DaoFactory.getInstance().insert(sesion, bitacora)> 0L;
    } // try
    catch (Exception e) {
      throw e;
    } // catch	
    return regresar;    
  }
  
  private boolean toProcesar(Session sesion) throws Exception {
    Boolean regresar          = Boolean.FALSE;
    Map<String, Object> params= new HashMap<>();
    try {      
      params.put("idConteo", this.conteo.getIdConteo());      
      this.conteo.setIdConteoEstatus(2L);
      DaoFactory.getInstance().update(sesion, this.conteo);
      this.bitacora= new TcManticConteosBitacoraDto(
        null, // String justificacion, 
        this.conteo.getIdUsuario(), // Long idUsuario, 
        this.conteo.getIdConteo(), // Long idConteo, 
        -1L, // Long idConteoBitacora, 
        this.conteo.getIdConteoEstatus()// Long idConteoEstatus
      );          
      DaoFactory.getInstance().insert(sesion, bitacora);
      List<TcManticConteosDetallesDto> items= (List<TcManticConteosDetallesDto>)DaoFactory.getInstance().toEntitySet(sesion, TcManticConteosDetallesDto.class, "TcManticConteosDetallesDto", "detalle", params);
      for (TcManticConteosDetallesDto item: items) {
        if(Objects.equals(item.getProcesado(), null)) {
          regresar= this.toArticulo(sesion, item);
          item.setProcesado(new Timestamp(Calendar.getInstance().getTimeInMillis()));
          DaoFactory.getInstance().update(sesion, item);
          LOG.error(item);
        } // if
        else
          regresar= Boolean.TRUE;
      } // for
      this.conteo.setProcesado(new Timestamp(Calendar.getInstance().getTimeInMillis()));
      this.conteo.setIdConteoEstatus(3L);
      DaoFactory.getInstance().update(sesion, this.conteo);
      this.bitacora= new TcManticConteosBitacoraDto(
        null, // String justificacion, 
        this.conteo.getIdUsuario(), // Long idUsuario, 
        this.conteo.getIdConteo(), // Long idConteo, 
        -1L, // Long idConteoBitacora, 
        this.conteo.getIdConteoEstatus() // Long idConteoEstatus
      );          
      regresar= DaoFactory.getInstance().insert(sesion, this.bitacora)> 0L;
    } // try
    catch (Exception e) {
      throw e;
    } // catch	
    finally {
      Methods.clean(params);
    } // finally
    return regresar;    
  }

  private boolean toArticulo(Session sesion, TcManticConteosDetallesDto item) throws Exception {
    Boolean regresar          = Boolean.FALSE;
    Map<String, Object> params= new HashMap<>();
    try {
      params.put("idArticulo", item.getIdArticulo());      
      params.put("idAlmacen", this.conteo.getIdAlmacen());      
      ArticuloAlmacen almacen= (ArticuloAlmacen)DaoFactory.getInstance().toEntity(sesion, ArticuloAlmacen.class, "TcManticAlmacenesArticulosDto", "almacenArticulo", params);
      if(Objects.equals(almacen, null)) {
        TcManticArticulosDto articulo= (TcManticArticulosDto)DaoFactory.getInstance().findById(sesion, TcManticArticulosDto.class, item.getIdArticulo());
        almacen= new ArticuloAlmacen(
          articulo.getMinimo(), // Long minimo, 
          -1L, // Long idAlmacenArticulo, 
          this.conteo.getIdUsuario(), // Long idUsuario, 
          this.conteo.getIdAlmacen(), // Long idAlmacen,
          articulo.getMaximo(), // Long maximo, 
          -1L, // Long idAlmacenUbicacion, 
          item.getIdArticulo(), // Long idArticulo, 
          item.getCantidad(), // Double stock
          articulo.getIdVerificado(), // Long idVerificado
          -1L // Long idInventario
        );
        almacen.setIdAlmacenUbicacion(this.toLoadIdUbicacion(sesion));
      } // if	
      TcManticInventariosDto inventario= (TcManticInventariosDto)DaoFactory.getInstance().findById(sesion, TcManticInventariosDto.class, almacen.getIdInventario());
      if(Objects.equals(inventario, null) || !inventario.getEntradas().equals(0D) || !inventario.getSalidas().equals(0D)) {
        inventario= new TcManticInventariosDto(-1L);
        inventario.setIdAlmacen(this.conteo.getIdAlmacen());
        inventario.setIdArticulo(item.getIdArticulo());
        inventario.setEjercicio(new Long(Fecha.getAnioActual()));
        inventario.setInicial(item.getCantidad());
        inventario.setEntradas(0D);
        inventario.setSalidas(0D);
        inventario.setStock(0D);
        inventario.setIdUsuario(this.conteo.getIdUsuario());
        inventario.setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
      } // if
      else {
        inventario.setInicial(item.getCantidad());
        inventario.setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
      } // else  
      almacen.setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
			almacen.setStock(inventario.getInicial());
			if(almacen.isValid()) 
				DaoFactory.getInstance().update(sesion, almacen);
			else 
				DaoFactory.getInstance().insert(sesion, almacen);
			// QUITAR DE LAS VENTAS PERDIDAS LOS ARTICULOS QUE FUERON YA SURTIDOS EN EL ALMACEN
			params.put("idEmpresa", this.conteo.getIdEmpresa());
			params.put("idAlmacen", almacen.getIdAlmacen());
			params.put("idArticulo", inventario.getIdArticulo());
			params.put("observaciones", "ESTE ARTICULO FUE CONTADO EL DIA "+ Fecha.getHoyExtendido());
			DaoFactory.getInstance().updateAll(sesion, TcManticFaltantesDto.class, params);
			// afectar el stock global del articulo basado en las diferencias que existian en el almacen origen
			TcManticArticulosDto global= (TcManticArticulosDto)DaoFactory.getInstance().findById(sesion, TcManticArticulosDto.class, inventario.getIdArticulo());
			if(global!= null) {
				global.setStock(this.toSumAlmacenArticulo(sesion, almacen.getIdArticulo()));
        global.setIdVerificado(almacen.getIdVerificado());
  			DaoFactory.getInstance().update(sesion, global);
			} // if
			else
				LOG.error("El articulos ["+ inventario.getIdArticulo()+ "] no existe hay que verificarlo !");
      // generar un registro en la bitacora de movimientos de los articulos 
      TcManticMovimientosDto movimiento= new TcManticMovimientosDto(
        "REM", // String consecutivo, 
        8L, // Long idTipoMovimiento, 
        this.conteo.getIdUsuario(), // Long idUsuario, 
        almacen.getIdAlmacen(), // Long idAlmacen, 
        -1L, // Long idMovimiento, 
        0D, // Double cantidad, 
        inventario.getIdArticulo(), // Long idArticulo, 
        inventario.getInicial(), // Double stock, 
        Numero.toRedondearSat(inventario.getInicial()), // Double calculo
        null // String observaciones
      );
      DaoFactory.getInstance().insert(sesion, movimiento);
      inventario.setStock(inventario.getInicial());
      if(inventario.isValid())
        regresar= DaoFactory.getInstance().update(sesion, inventario)> 0L;
      else
        regresar= DaoFactory.getInstance().insert(sesion, inventario)> 0L;
    } // try
    catch (Exception e) {
      throw e;
    } // catch	
    finally {
      Methods.clean(params);
    } // finally
    return regresar;    
  }
  
  private Long toLoadIdUbicacion(Session sesion) throws Exception {
    Long regresar             = -1L;
    List<Entity> ubicaciones  = null;
    Map<String, Object> params= new HashMap<>();
    try {      
      params.put("idAlmacen", this.conteo.getIdAlmacen());
			ubicaciones= DaoFactory.getInstance().toEntitySet(sesion, "VistaKardexDto", "ubicaciones", params);
      // SI NO HAY UBICACIONES INSERTAR UNA UBICACION POR DEFECTO
      if(Objects.equals(ubicaciones, null) || ubicaciones.isEmpty()) {
        TcManticAlmacenesUbicacionesDto general= new TcManticAlmacenesUbicacionesDto("GENERAL", "", "GENERAL", "", "", this.conteo.getIdUsuario(), this.conteo.getIdAlmacen(), -1L);
				DaoFactory.getInstance().insert(sesion, general);
        regresar= general.getIdAlmacenUbicacion();
      } // if
      else
        regresar= ubicaciones.get(0).toLong("idAlmacenUbicacion");
    } // try
    catch (Exception e) {
      throw e;      
    } // catch	
    finally {
      Methods.clean(params);
    } // finally
    return regresar;
  }
  
  private Double toSumAlmacenArticulo(Session sesion, Long idArticulo) throws Exception {
		Double regresar           = 0D;
		Map<String, Object> params= new HashMap<>();
		try {
			params.put("sucursales", this.conteo.getIdEmpresa());
			params.put("idArticulo", idArticulo);
			Value value= DaoFactory.getInstance().toField(sesion, "VistaKardexDto", "existencias", params, "total");
			if(value!= null && value.getData()!= null)
				regresar= value.toDouble();
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