package mx.org.kaana.mantic.catalogos.almacenes.confrontas.reglas;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.almacenes.transferencias.reglas.ComunInventarios;
import mx.org.kaana.mantic.compras.ordenes.beans.Articulo;
import mx.org.kaana.mantic.db.dto.TcManticArticulosDto;
import mx.org.kaana.mantic.db.dto.TcManticConfrontasDto;
import mx.org.kaana.mantic.db.dto.TcManticTransferenciasBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticConfrontasDetallesDto;
import mx.org.kaana.mantic.db.dto.TcManticTransferenciasDetallesDto;
import mx.org.kaana.mantic.db.dto.TcManticTransferenciasDto;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;

public class Transaccion extends ComunInventarios {

	private static final Log LOG= LogFactory.getLog(Transaccion.class);

  private TcManticConfrontasDto dto;
  private TcManticTransferenciasDto transferencia;

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
				case ACTIVAR:
				case AGREGAR:
					consecutivo= this.toSiguiente(sesion);
					this.dto.setConsecutivo(this.dto.getEjercicio()+ Cadena.rellenar(consecutivo.toString(), 5, '0', true));
					this.dto.setOrden(consecutivo);
          regresar= DaoFactory.getInstance().insert(sesion, this.dto).intValue()> 0;
					this.toFillArticulos(sesion, accion);
					this.bitacora= new TcManticTransferenciasBitacoraDto(-1L, "", JsfBase.getIdUsuario(), null, 5L, this.dto.getIdTransferencia());
					if(regresar)
            regresar= DaoFactory.getInstance().insert(sesion, this.bitacora).intValue()> 0;
					this.toCheckOrden(sesion, accion);
          break;
				case PROCESAR:
				case MODIFICAR:
          this.dto.setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
					this.toFillArticulos(sesion, accion);
          regresar= DaoFactory.getInstance().update(sesion, this.dto).intValue()> 0;
					this.bitacora= new TcManticTransferenciasBitacoraDto(-1L, "", JsfBase.getIdUsuario(), null, this.transferencia.getIdTransferenciaEstatus(), this.dto.getIdTransferencia());
					if(regresar)
            regresar= DaoFactory.getInstance().insert(sesion, this.bitacora).intValue()> 0;
					this.toCheckOrden(sesion, accion);
          break;
				case CALCULAR:
					this.toCheckArticulos(sesion);
					this.transferencia.setIdTransferenciaEstatus(9L);
					DaoFactory.getInstance().update(sesion, this.transferencia);
					this.bitacora= new TcManticTransferenciasBitacoraDto(-1L, "", JsfBase.getIdUsuario(), null, this.transferencia.getIdTransferenciaEstatus(), this.dto.getIdTransferencia());
					DaoFactory.getInstance().insert(sesion, this.bitacora);
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
					case ACTIVAR: // RECIBIR
					case PROCESAR: // INCOMPLETA
						if(this.transferencia.getIdTransferenciaEstatus()== 6L || this.transferencia.getIdTransferenciaEstatus()== 7L) {
							if(articulo.getInicial()- item.getCantidad()!= 0L)
						    this.toMovimientosAlmacenDestino(sesion, this.transferencia.getIdDestino(), articulo, umbrales, articulo.getInicial()- articulo.getCantidad());
						} // if	
						else
						  this.toMovimientosAlmacenDestino(sesion, this.transferencia.getIdDestino(), articulo, umbrales, articulo.getCantidad());
						break;
					case DEPURAR: // AUTORIZAR
						break;
				} // switch
			} // if
		} // for
	}

	private void toAffectTransferenciaDetalle(Session sesion, Articulo articulo) throws Exception {
		if(articulo.getIdOrdenDetalle()!= null && articulo.getIdOrdenDetalle()> 0L) {
			TcManticTransferenciasDetallesDto detalle= (TcManticTransferenciasDetallesDto)DaoFactory.getInstance().findById(sesion, TcManticTransferenciasDetallesDto.class, articulo.getIdOrdenDetalle());
      detalle.setCantidades(detalle.getCantidad()- articulo.getCantidad());
			DaoFactory.getInstance().update(sesion, detalle);
		} // if
	}

	private void toCheckOrden(Session sesion, EAccion accion) throws Exception {
		sesion.flush();
		Value errors= DaoFactory.getInstance().toField(sesion, "VistaConfrontasDto", "errores", this.transferencia.toMap(), "total");
		if(errors.toLong()!= null && errors.toLong()== 0) 
			if(this.transferencia.getIdTransferenciaEstatus()== 3L || this.transferencia.getIdTransferenciaEstatus()== 5L || this.transferencia.getIdTransferenciaEstatus()== 7L)
				this.transferencia.setIdTransferenciaEstatus(8L); // TERMINADA
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
	}

	private void toCheckArticulos(Session sesion) throws Exception {
		for (Articulo articulo: this.articulos) {
  		TcManticArticulosDto umbrales= (TcManticArticulosDto)DaoFactory.getInstance().findById(TcManticArticulosDto.class, articulo.getIdArticulo());
			TcManticConfrontasDetallesDto item= articulo.toConfrontasDetalle();
			item.setIdConfronta(this.dto.getIdConfronta());
			double diferencia= articulo.getCantidad();
			articulo.setCantidad(Math.abs(articulo.getCuantos()));
			switch(item.getIdAplicar().intValue()) {
				case 1: // IGNORAR CAMBIOS
					break;
				case 2: // RESTAR ORIGEN
					this.toMovimientosAlmacenOrigen(sesion, this.transferencia.getIdAlmacen(), articulo, umbrales, 4L);
					break;
				case 3: // AFECTAR ORIGEN
					this.toMovimientosAlmacenOrigen(sesion, this.transferencia.getIdAlmacen(), articulo, umbrales, this.transferencia.getIdTransferenciaEstatus());
					break;
				case 4: // AFECTAR DESTINO
					this.toMovimientosAlmacenDestino(sesion, this.transferencia.getIdDestino(), articulo, umbrales, articulo.getCantidad());
					break;
				case 5: // REGRESAR ORIGEN
					this.toMovimientosAlmacenOrigen(sesion, this.transferencia.getIdAlmacen(), articulo, umbrales, this.transferencia.getIdTransferenciaEstatus());
					break;
				case 6: // SUMAR DESTINO
					this.toMovimientosAlmacenDestino(sesion, this.transferencia.getIdDestino(), articulo, umbrales, articulo.getCantidad());
					break;
			} // switch
			articulo.setCantidad(diferencia);
		} // for
	}
	
}
