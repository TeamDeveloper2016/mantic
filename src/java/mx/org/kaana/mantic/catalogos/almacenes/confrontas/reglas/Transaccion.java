package mx.org.kaana.mantic.catalogos.almacenes.confrontas.reglas;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.reglas.beans.Siguiente;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.almacenes.transferencias.reglas.ComunInventarios;
import mx.org.kaana.mantic.compras.ordenes.beans.Articulo;
import mx.org.kaana.mantic.db.dto.TcManticArticulosDto;
import mx.org.kaana.mantic.db.dto.TcManticConfrontasDto;
import mx.org.kaana.mantic.db.dto.TcManticTransferenciasBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticConfrontasDetallesDto;
import mx.org.kaana.mantic.db.dto.TcManticFaltantesDto;
import mx.org.kaana.mantic.db.dto.TcManticTransferenciasDetallesDto;
import mx.org.kaana.mantic.db.dto.TcManticTransferenciasDto;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;

public class Transaccion extends ComunInventarios implements Serializable {

	private static final Log LOG= LogFactory.getLog(Transaccion.class);
  private static final long serialVersionUID = -623150336470893500L;

  private TcManticConfrontasDto dto;
  private TcManticTransferenciasDto transferencia;
	private boolean afectoAlmacenDestino;

	public Transaccion(TcManticConfrontasDto dto, List<Articulo> articulos) {
		this.dto= dto;
		this.articulos= articulos;
		this.afectoAlmacenDestino= false;
	}
	
  @Override
  protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {
    boolean regresar= false;
    try {			
    	this.messageError = "Ocurrio un error en ".concat(accion.name().toLowerCase()).concat(" para transferencia de articulos.");
  		Siguiente consecutivo= null;
			this.transferencia= (TcManticTransferenciasDto)DaoFactory.getInstance().findById(sesion, TcManticTransferenciasDto.class, this.dto.getIdTransferencia());
      this.idUsuario= JsfBase.getIdUsuario();
      this.idEmpresa= JsfBase.getAutentifica().getEmpresa().getIdEmpresa();
      switch (accion) {
				case ACTIVAR:
				case AGREGAR:
					consecutivo= this.toSiguiente(sesion);
					this.dto.setConsecutivo(consecutivo.getConsecutivo());
					this.dto.setOrden(consecutivo.getOrden());
          regresar= DaoFactory.getInstance().insert(sesion, this.dto).intValue()> 0;
					this.toFillArticulos(sesion, accion);
					this.bitacora= new TcManticTransferenciasBitacoraDto(-1L, "", JsfBase.getIdUsuario(), null, this.transferencia.getIdTransferenciaEstatus(), this.dto.getIdTransferencia());
					this.toCheckOrden(sesion, accion);
          break;
				case PROCESAR:
				case MODIFICAR:
          this.dto.setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
					this.toFillArticulos(sesion, accion);
          regresar= DaoFactory.getInstance().update(sesion, this.dto).intValue()> 0;
					this.bitacora= new TcManticTransferenciasBitacoraDto(-1L, "", JsfBase.getIdUsuario(), null, this.transferencia.getIdTransferenciaEstatus(), this.dto.getIdTransferencia());
					this.toCheckOrden(sesion, accion);
          break;
				case CALCULAR:
					this.toCheckArticulos(sesion);
					this.transferencia.setIdTransferenciaEstatus(9L);
					regresar= DaoFactory.getInstance().update(sesion, this.transferencia).intValue()> 0;
					this.bitacora= new TcManticTransferenciasBitacoraDto(-1L, "", JsfBase.getIdUsuario(), null, this.transferencia.getIdTransferenciaEstatus(), this.dto.getIdTransferencia());
					if(regresar)
					  regresar= DaoFactory.getInstance().insert(sesion, this.bitacora).intValue()> 0;
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
			params.put("idEmpresa", this.transferencia.getIdEmpresa());
			params.put("operador", this.getCurrentSign());
			Value next= DaoFactory.getInstance().toField(sesion, "VistaConfrontasDto", "siguiente", params, "siguiente");
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

	private void toMarkFaltantes(Session sesion, Articulo articulo) throws Exception {
		Map<String, Object> params= new HashMap<>();
		try {
			// QUITAR DE LAS VENTAS PERDIDAS LOS ARTICULOS QUE FUERON YA SURTIDOS EN EL ALMACEN
			params.put("idArticulo", articulo.getIdArticulo());
			Long idEmpresa= this.transferencia.getIdEmpresa();
			params.put("idAlmacen", this.transferencia.getIdDestino());
			Value empresa= DaoFactory.getInstance().toField(sesion, "TcManticAlmacenesDto", "empresa", params, "idEmpresa");
			if(empresa.getData()!= null)
				idEmpresa= empresa.toLong();
			params.put("idEmpresa", idEmpresa);
			params.put("observaciones", "ESTE ARTICULO FUE SURTIDO CON NO. CONFRONTA "+ this.dto.getConsecutivo()+ " EL DIA "+ Fecha.getHoyExtendido());
			DaoFactory.getInstance().updateAll(sesion, TcManticFaltantesDto.class, params);
		} // try
		catch (Exception e) {
			throw e;
		} // catch
		finally {
			Methods.clean(params);
		} // finally
	}
	
	private void toFillArticulos(Session sesion, EAccion accion) throws Exception {
		Map<String, Object> params= new HashMap<>();
		List<Articulo> todos      = (List<Articulo>) DaoFactory.getInstance().toEntitySet(sesion, Articulo.class, "VistaConfrontasDto", "detalle", this.dto.toMap());
		try {
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
					TcManticArticulosDto umbrales= (TcManticArticulosDto)DaoFactory.getInstance().findById(sesion, TcManticArticulosDto.class, articulo.getIdArticulo());
					switch(accion) {
						case ACTIVAR: // RECIBIR
						case PROCESAR: // INCOMPLETA
							if(this.transferencia.getIdTransferenciaEstatus()== 3L || this.transferencia.getIdTransferenciaEstatus()== 5L || this.transferencia.getIdTransferenciaEstatus()== 6L || this.transferencia.getIdTransferenciaEstatus()== 7L) {
								if(item.getDiferencia()== 0L) {
									this.toMovimientosAlmacenDestino(sesion, this.transferencia.getConsecutivo(), this.transferencia.getIdDestino(), articulo, umbrales, articulo.getCantidad());
									this.toMarkFaltantes(sesion, articulo);
                  this.afectoAlmacenDestino= true;
								} // if
							} // if	
							break;
						case DEPURAR: // AUTORIZAR
							break;
					} // switch
				} // if
			} // for
		} // try
		catch (Exception e) {
			throw e;
		} // catch
		finally {
			Methods.clean(todos);
			Methods.clean(params);
		} // finally
	}

	private void toAffectTransferenciaDetalle(Session sesion, Articulo articulo) throws Exception {
		if(articulo.getIdOrdenDetalle()!= null && articulo.getIdOrdenDetalle()> 0L) {
			TcManticTransferenciasDetallesDto detalle= (TcManticTransferenciasDetallesDto)DaoFactory.getInstance().findById(sesion, TcManticTransferenciasDetallesDto.class, articulo.getIdOrdenDetalle());
      detalle.setCantidades(detalle.getCantidad()- articulo.getCantidad());
			DaoFactory.getInstance().update(sesion, detalle);
		} // if
	}
	
	private void toApplyMovimientos(Session sesion) throws Exception {
		Map<String, Object> params= new HashMap<>();
		try {
			for (Articulo articulo: this.articulos) {
				TcManticConfrontasDetallesDto item= articulo.toConfrontasDetalle();
				TcManticArticulosDto umbrales     = (TcManticArticulosDto)DaoFactory.getInstance().findById(TcManticArticulosDto.class, articulo.getIdArticulo());
				if(item.getDiferencia()== 0L) {
					this.toMovimientosAlmacenDestino(sesion, this.transferencia.getConsecutivo(), this.transferencia.getIdDestino(), articulo, umbrales, articulo.getCantidad());
					this.toMarkFaltantes(sesion, articulo);
				} // if
			} // for
		} // try
		catch (Exception e) {
			throw e;
		} // catch
		finally {
			Methods.clean(params);
		} // finally
	}

	private void toCheckOrden(Session sesion, EAccion accion) throws Exception {
		sesion.flush();
		try {
			Value errors= DaoFactory.getInstance().toField(sesion, "VistaConfrontasDto", "errores", this.transferencia.toMap(), "total");
			if(errors.toLong()!= null && errors.toLong()== 0) 
				if(this.transferencia.getIdTransferenciaEstatus()== 3L || this.transferencia.getIdTransferenciaEstatus()== 5L) {
					if(!this.afectoAlmacenDestino)
						this.toApplyMovimientos(sesion);
					this.transferencia.setIdTransferenciaEstatus(8L); // TERMINADA
				} // if
				else
					this.transferencia.setIdTransferenciaEstatus(9L); // ACEPTADA
			else 
				if(this.transferencia.getIdTransferenciaEstatus()== 3L && !EAccion.ACTIVAR.equals(accion) && !EAccion.PROCESAR.equals(accion))
					this.transferencia.setIdTransferenciaEstatus(5L); // RECIBIR
				else
					if((this.transferencia.getIdTransferenciaEstatus()== 3L || this.transferencia.getIdTransferenciaEstatus()== 5L) && (EAccion.ACTIVAR.equals(accion) || EAccion.PROCESAR.equals(accion)))
						this.transferencia.setIdTransferenciaEstatus(6L); // INCOMPLETA
					else
						if(this.transferencia.getIdTransferenciaEstatus()== 6L)
							this.transferencia.setIdTransferenciaEstatus(7L); // AUTORIZAR
			if(!this.bitacora.getIdTransferenciaEstatus().equals(this.transferencia.getIdTransferenciaEstatus())) {
				DaoFactory.getInstance().update(sesion, this.transferencia);
				this.bitacora= new TcManticTransferenciasBitacoraDto(-1L, "", JsfBase.getIdUsuario(), null, this.transferencia.getIdTransferenciaEstatus(), this.dto.getIdTransferencia());
				DaoFactory.getInstance().insert(sesion, this.bitacora);
			} // if
		} // try
   	catch (Exception e) {
			throw e;
		} // catch
	}

	private void toCheckArticulos(Session sesion) throws Exception {
		try {
			for (Articulo articulo: this.articulos) {
				TcManticArticulosDto umbrales= (TcManticArticulosDto)DaoFactory.getInstance().findById(sesion, TcManticArticulosDto.class, articulo.getIdArticulo());
				TcManticConfrontasDetallesDto item= articulo.toConfrontasDetalle();
				item.setIdConfronta(this.dto.getIdConfronta());
				articulo.setCantidad(articulo.getCuantos());
				switch(articulo.getIdRedondear().intValue()) {
					case 1: // IGNORAR CAMBIOS
						break;
					case 2: // RESTAR ORIGEN
						// this.toMovimientosAlmacenOrigen(sesion, this.transferencia.getConsecutivo(), this.transferencia.getIdAlmacen(), articulo, umbrales, this.transferencia.getIdTransferenciaEstatus());
						break;
					case 3: // AFECTAR ORIGEN
						// this.toMovimientosAlmacenOrigen(sesion, this.transferencia.getConsecutivo(), this.transferencia.getIdAlmacen(), articulo, umbrales, this.transferencia.getIdTransferenciaEstatus());
						break;
					case 4: // AFECTAR DESTINO
						// this.toMovimientosAlmacenDestino(sesion, this.transferencia.getConsecutivo(), this.transferencia.getIdDestino(), articulo, umbrales, articulo.getCantidad());
						// this.toMarkFaltantes(sesion, articulo);
						break;
					case 5: // AFECTAR AMBOS
						this.toAutorizarAlmacenOrigen(sesion, this.transferencia.getConsecutivo(), this.transferencia.getIdAlmacen(), articulo, umbrales, this.transferencia.getIdTransferenciaEstatus());
            if(articulo.getDescuentos()!= 0D) {
              articulo.setCantidad(articulo.getDescuentos());
						  this.toMovimientosAlmacenDestino(sesion, this.transferencia.getConsecutivo(), this.transferencia.getIdDestino(), articulo, umbrales, articulo.getCantidad());
            } // if
						this.toMarkFaltantes(sesion, articulo);
						break;
					case 6: // REGRESAR ORIGEN
						// this.toMovimientosAlmacenOrigen(sesion, this.transferencia.getConsecutivo(), this.transferencia.getIdAlmacen(), articulo, umbrales, 4L);
						break;
					case 7: // SUMAR DESTINO
						// this.toMovimientosAlmacenDestino(sesion, this.transferencia.getConsecutivo(), this.transferencia.getIdDestino(), articulo, umbrales, articulo.getCantidad());
						// this.toMarkFaltantes(sesion, articulo);
						break;
				} // switch
				DaoFactory.getInstance().update(sesion, item);
			} // for
//      throw new RuntimeException("Estoy probando que este bien este proceso.");
		} // try
   	catch (Exception e) {
			throw e;
		} // catch
	}
	
}
