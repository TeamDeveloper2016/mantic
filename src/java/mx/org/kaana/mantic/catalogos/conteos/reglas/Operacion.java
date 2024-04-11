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
import mx.org.kaana.mantic.compras.ordenes.beans.Articulo;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.almacenes.transferencias.reglas.ComunInventarios;
import mx.org.kaana.mantic.db.dto.TcManticArticulosDto;
import mx.org.kaana.mantic.db.dto.TcManticTransferenciasBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticTransferenciasDetallesDto;
import mx.org.kaana.mantic.db.dto.TcManticTransferenciasDto;
import org.apache.log4j.Logger;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 03/02/2024
 *@time 13:29:04 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Operacion extends ComunInventarios implements Serializable {

  private static final Logger LOG= Logger.getLogger(Operacion.class);
	private static final long serialVersionUID= -3186367186737677671L;
 
	private TcManticTransferenciasDto transferencia;	
	private TcManticTransferenciasBitacoraDto bitacora;
	private String messageError;

	public Operacion(TcManticTransferenciasDto transferencia) {
    this(transferencia, null);
	} // Transaccion
  
	public Operacion(TcManticTransferenciasDto transferencia, TcManticTransferenciasBitacoraDto bitacora) {
    this.transferencia= transferencia;
		this.bitacora     = bitacora;
  	this.messageError = "Ocurrio un error en el conteo";
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
					this.transferencia.setIdTransferenciaEstatus(2L);
					regresar= DaoFactory.getInstance().update(sesion, this.transferencia)>= 1L;
					break;
				case JUSTIFICAR:
					if(DaoFactory.getInstance().insert(sesion, this.bitacora)>= 1L) {
						this.transferencia.setIdTransferenciaEstatus(this.bitacora.getIdTransferenciaEstatus());
						regresar= DaoFactory.getInstance().update(sesion, this.transferencia)>= 1L;
					} // if
					break;
				case DEPURAR:
          params.put("idTransferencia", this.transferencia.getIdTransferencia());
          DaoFactory.getInstance().deleteAll(TcManticTransferenciasBitacoraDto.class, params);
          DaoFactory.getInstance().deleteAll(TcManticTransferenciasDetallesDto.class, params);
          regresar= DaoFactory.getInstance().deleteAll(TcManticTransferenciasDto.class, params)>= 1L;
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
      this.transferencia.setIdTransferenciaEstatus(10L); // ERRORES
      DaoFactory.getInstance().update(sesion, this.transferencia);
      this.bitacora= new TcManticTransferenciasBitacoraDto(
        -1L, // Long idTransferenciaBitacora, 
        this.messageError, // String justificacion, 
        this.transferencia.getIdUsuario(), // Long idUsuario, 
        null, // Long idTransporto, 
        this.transferencia.getIdTransferenciaEstatus(), // Long idTransferenciaEstatus, 
        this.transferencia.getIdTransferencia() // Long idTransferencia
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
      params.put("idTransferencia", this.transferencia.getIdTransferencia());      
      this.transferencia.setIdTransferenciaEstatus(3L); // TRANSITO
      DaoFactory.getInstance().update(sesion, this.transferencia);
      this.bitacora= new TcManticTransferenciasBitacoraDto(
        -1L, // Long idTransferenciaBitacora, 
        this.messageError, // String justificacion, 
        this.transferencia.getIdUsuario(), // Long idUsuario, 
        null, // Long idTransporto, 
        this.transferencia.getIdTransferenciaEstatus(), // Long idTransferenciaEstatus, 
        this.transferencia.getIdTransferencia() // Long idTransferencia
      );
      regresar= DaoFactory.getInstance().insert(sesion, bitacora)> 0L;
      List<TcManticTransferenciasDetallesDto> items= (List<TcManticTransferenciasDetallesDto>)DaoFactory.getInstance().toEntitySet(sesion, TcManticTransferenciasDetallesDto.class, "TcManticTransferenciasDetallesDto", "detalle", params);
      for (TcManticTransferenciasDetallesDto item: items) {
        LOG.error("Articulo: "+ item.getIdArticulo()+ " - "+ item.getCantidad()+ " ["+ item.getProcesado()+ "]");  
        if(Objects.equals(item.getProcesado(), null)) {
          regresar= this.toArticulo(sesion, item);
          item.setProcesado(new Timestamp(Calendar.getInstance().getTimeInMillis()));
          DaoFactory.getInstance().update(sesion, item);
        } // if
        else
          regresar= Boolean.TRUE;
      } // for
      this.transferencia.setProcesado(new Timestamp(Calendar.getInstance().getTimeInMillis()));
      this.transferencia.setIdTransferenciaEstatus(8L); // TERMINADA
      DaoFactory.getInstance().update(sesion, this.transferencia);
      this.bitacora= new TcManticTransferenciasBitacoraDto(
        -1L, // Long idTransferenciaBitacora, 
        this.messageError, // String justificacion, 
        this.transferencia.getIdUsuario(), // Long idUsuario, 
        null, // Long idTransporto, 
        this.transferencia.getIdTransferenciaEstatus(), // Long idTransferenciaEstatus, 
        this.transferencia.getIdTransferencia() // Long idTransferencia
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

  private boolean toArticulo(Session sesion, TcManticTransferenciasDetallesDto item) throws Exception {
    Boolean regresar          = Boolean.FALSE;
    Map<String, Object> params= new HashMap<>();
    try {
			TcManticArticulosDto umbrales= (TcManticArticulosDto)DaoFactory.getInstance().findById(TcManticArticulosDto.class, item.getIdArticulo());
      Articulo articulo= new Articulo(item.getIdArticulo());
      articulo.setCantidad(item.getCantidad());
      articulo.setSolicitados(item.getCantidad());
			this.toMovimientosAlmacenOrigen(sesion,  this.transferencia.getConsecutivo(), this.transferencia.getIdAlmacen(), articulo, umbrales, this.transferencia.getIdTransferenciaEstatus());
  		this.toMovimientosAlmacenDestino(sesion, this.transferencia.getConsecutivo(), this.transferencia.getIdDestino(), articulo, umbrales, articulo.getCantidad());
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