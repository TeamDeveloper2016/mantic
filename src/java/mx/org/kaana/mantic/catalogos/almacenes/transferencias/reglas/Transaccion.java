package mx.org.kaana.mantic.catalogos.almacenes.transferencias.reglas;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.db.dto.TcManticAlmacenesArticulosDto;
import mx.org.kaana.mantic.db.dto.TcManticAlmacenesUbicacionesDto;
import mx.org.kaana.mantic.db.dto.TcManticArticulosDto;
import mx.org.kaana.mantic.db.dto.TcManticTransferenciasBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticTransferenciasDto;
import org.hibernate.Session;

public class Transaccion extends IBaseTnx {

  private TcManticTransferenciasDto dto;
	private Long idTransferenciaEstatus;
  private String messageError;

	public Transaccion(TcManticTransferenciasDto dto) {
		this(dto, 1L);
	}

	public Transaccion(TcManticTransferenciasDto dto, Long idTransferenciaEstatus) {
		this.dto = dto;
		this.idTransferenciaEstatus= idTransferenciaEstatus;
	}

  @Override
  protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {
    boolean regresar = false;
    try {			
    	this.messageError= "Ocurrio un error en ".concat(accion.name().toLowerCase()).concat(" para transferencia de articulos.");
      TcManticTransferenciasBitacoraDto bitacora = null;
      TcManticAlmacenesArticulosDto origen= null;			
      switch (accion) {
        case AGREGAR:
					//Afectar el almacen original restando los articulos que fueron extraidos
					origen= (TcManticAlmacenesArticulosDto)DaoFactory.getInstance().findById(TcManticAlmacenesArticulosDto.class, this.dto.getIdAlmacen());
					if(origen== null || origen.getStock()< this.dto.getCantidad())
						throw new Exception("No existen suficientes articulos en el stock del almacen !");
					else {
						origen.setStock(Numero.toRedondearSat(origen.getStock()- this.dto.getCantidad()));
						regresar= DaoFactory.getInstance().update(sesion, origen).intValue()> 0;
					} // if
          regresar= DaoFactory.getInstance().insert(sesion, this.dto).intValue()> 0;
					bitacora= new TcManticTransferenciasBitacoraDto(-1L, this.dto.getIdTransferencia(), JsfBase.getIdUsuario(), this.dto.getIdTransferenciaEstatus(), "");
          regresar= DaoFactory.getInstance().insert(sesion, bitacora).intValue()> 0;
					//Afectar el almacen destino sumando los articulos que fueron agregados
					origen= (TcManticAlmacenesArticulosDto)DaoFactory.getInstance().findById(TcManticAlmacenesArticulosDto.class, this.dto.getIdDestino());
					if(origen== null) {
						TcManticArticulosDto articulo= (TcManticArticulosDto)DaoFactory.getInstance().findById(TcManticArticulosDto.class, this.dto.getIdArticulo());
						origen= new TcManticAlmacenesArticulosDto(
							articulo.getMinimo(), // Double minimo, 
							-1L, // Long idAlmacenArticulo, 
							JsfBase.getIdUsuario(), // Long idUsuario, 
							this.dto.getIdDestino(), // Long idAlmacen, 
							articulo.getMaximo(), // Double maximo, 
							this.toUbicacion(sesion), // Long idAlmacenUbicacion, 
							articulo.getIdArticulo(), // Long idArticulo, 
							this.dto.getCantidad() // Double stock
						);
						regresar= DaoFactory.getInstance().insert(sesion, origen).intValue()> 0;
					} // if	
					else {
						origen.setStock(Numero.toRedondearSat(origen.getStock()+ this.dto.getCantidad()));
						regresar= DaoFactory.getInstance().update(sesion, origen).intValue()> 0;
					} // else
          break;
        case MODIFICAR:
          this.dto.setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
          regresar= DaoFactory.getInstance().update(sesion, this.dto).intValue()> 0;
          break;
        case ELIMINAR:
          if (DaoFactory.getInstance().deleteAll(sesion, TcManticTransferenciasBitacoraDto.class, this.dto.toMap())> -1L) 
            regresar= DaoFactory.getInstance().delete(sesion, TcManticTransferenciasDto.class, this.dto.getIdTransferencia())>= 1L;
          break;
        case REGISTRAR:
					this.dto.setIdTransferencia(this.idTransferenciaEstatus);
				  switch(this.idTransferenciaEstatus.intValue()) {
						case 3: // TRANSITO
							origen= (TcManticAlmacenesArticulosDto)DaoFactory.getInstance().findById(TcManticAlmacenesArticulosDto.class, this.dto.getIdAlmacen());
							if(origen== null || origen.getStock()< this.dto.getCantidad())
								throw new Exception("No existen suficientes articulos en el stock del almacen !");
							else {
								origen.setStock(Numero.toRedondearSat(origen.getStock()- this.dto.getCantidad()));
								regresar= DaoFactory.getInstance().update(sesion, origen).intValue()> 0;
							} // if
							break;
						case 4: // CANCELAR
							origen= (TcManticAlmacenesArticulosDto)DaoFactory.getInstance().findById(TcManticAlmacenesArticulosDto.class, this.dto.getIdAlmacen());
							if(origen== null)
								throw new Exception("El almacen ya no cuenta con este articulo !");
							else {
								origen.setStock(Numero.toRedondearSat(origen.getStock()+ this.dto.getCantidad()));
								regresar= DaoFactory.getInstance().update(sesion, origen).intValue()> 0;
							} // if
							break;
						case 5: // ENTREGADO
							origen= (TcManticAlmacenesArticulosDto)DaoFactory.getInstance().findById(TcManticAlmacenesArticulosDto.class, this.dto.getIdDestino());
							if(origen== null) {
  							TcManticArticulosDto articulo= (TcManticArticulosDto)DaoFactory.getInstance().findById(TcManticArticulosDto.class, this.dto.getIdArticulo());
								origen= new TcManticAlmacenesArticulosDto(
									articulo.getMinimo(), // Double minimo, 
									-1L, // Long idAlmacenArticulo, 
									JsfBase.getIdUsuario(), // Long idUsuario, 
									this.dto.getIdDestino(), // Long idAlmacen, 
									articulo.getMaximo(), // Double maximo, 
									this.toUbicacion(sesion), // Long idAlmacenUbicacion, 
									articulo.getIdArticulo(), // Long idArticulo, 
									this.dto.getCantidad() // Double stock
								);
							  regresar= DaoFactory.getInstance().insert(sesion, origen).intValue()> 0;
							} // if	
							else {
							  origen.setStock(Numero.toRedondearSat(origen.getStock()+ this.dto.getCantidad()));
							  regresar= DaoFactory.getInstance().update(sesion, origen).intValue()> 0;
					    } // else
							break;
					} // switch
 					bitacora= new TcManticTransferenciasBitacoraDto(-1L, this.dto.getIdTransferencia(), JsfBase.getIdUsuario(), this.dto.getIdTransferenciaEstatus(), "");
          regresar= DaoFactory.getInstance().insert(sesion, bitacora).intValue()> 0;
          break;
      } // switch
      if (!regresar) 
        throw new Exception("");
    } // try
    catch (Exception e) {
      throw new Exception(this.messageError.concat("<br/>")+ e.getMessage());
    } // catch		
    return regresar;
  } // ejecutar

	private Long toUbicacion(Session sesion) throws Exception {
		Long regresar= -1L;
		Map<String, Object> params= null;
		try {
			params=new HashMap<>();
			params.put("idAlmacen", this.dto.getIdDestino());
			params.put("idArticulo", this.dto.getIdArticulo());
			TcManticAlmacenesArticulosDto ubicacion= (TcManticAlmacenesArticulosDto)DaoFactory.getInstance().findFirst(sesion, TcManticAlmacenesArticulosDto.class, params, "ubicacion");
			if(ubicacion== null) {
				TcManticAlmacenesUbicacionesDto general= (TcManticAlmacenesUbicacionesDto)DaoFactory.getInstance().findFirst(sesion, TcManticAlmacenesUbicacionesDto.class, params, "general");
				if(general== null) {
					general= new TcManticAlmacenesUbicacionesDto("GENERAL", "", "GENERAL", "", "", JsfBase.getAutentifica().getPersona().getIdUsuario(), this.dto.getIdDestino(), -1L);
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
	


}
