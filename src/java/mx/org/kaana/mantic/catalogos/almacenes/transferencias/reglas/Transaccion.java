package mx.org.kaana.mantic.catalogos.almacenes.transferencias.reglas;

import java.sql.Timestamp;
import java.util.Calendar;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.mantic.db.dto.TcManticTransferenciasBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticTransferenciasDto;
import org.hibernate.Session;

public class Transaccion extends IBaseTnx {

  private TcManticTransferenciasDto dto;
  private String messageError;

	public Transaccion(TcManticTransferenciasDto dto) {
		this.dto = dto;
	}

  @Override
  protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {
    boolean regresar = false;
    try {			
    	this.messageError= "Ocurrio un error en ".concat(accion.name().toLowerCase()).concat(" para transferencia de articulos.");
      TcManticTransferenciasBitacoraDto bitacora = null;
      switch (accion) {
        case AGREGAR:
          regresar= DaoFactory.getInstance().insert(sesion, this.dto).intValue()> 0;
					bitacora= new TcManticTransferenciasBitacoraDto(-1L, this.dto.getIdTransferencia(), JsfBase.getIdUsuario(), this.dto.getIdTransferenciaEstatus(), "");
          regresar= DaoFactory.getInstance().insert(sesion, bitacora).intValue()> 0;
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
          regresar= DaoFactory.getInstance().insert(sesion, bitacora).intValue()> 0;
          if(regresar) {
						this.dto= (TcManticTransferenciasDto) DaoFactory.getInstance().findById(sesion, TcManticTransferenciasDto.class, bitacora.getIdTransferencia());
						this.dto.setIdTransferenciaEstatus(bitacora.getIdTransferenciaEstatus());
						regresar= DaoFactory.getInstance().update(sesion, this.dto)>= 1L;
            if(this.dto.getIdTransferenciaEstatus().equals(5L)) { // ESTATUS DE ENTREGADO
              regresar= DaoFactory.getInstance().update(this.dto)> 0L;
              if(regresar)
                agregaActualizaDestino(sesion);
            } // if
						else
              if(this.dto.getIdTransferenciaEstatus().equals(3L)) { // ESTATUS EN TRANSITO 
								
							}	// if						
					} // if
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

  private boolean agregaActualizaDestino(Session sesion) throws Exception {
    boolean regresar                        = false;
//    Map<String, Object> params              = null;
//    TcManticAlmacenesArticulosDto destino   = null;
//    TcManticAlmacenesUbicacionesDto general = null;
//    Double nuevoStock                       = 0D;
//    try {
//      params = new HashMap<>();
//      params.put("idTransferencia", this.dto.getIdTransferencia());
//      params.put("idAlmacen", this.idAlmacenDestino);
//      params.put("idArticulo", this.almanecArticuloOrigen.getIdArticulo());
//      destino = (TcManticAlmacenesArticulosDto) DaoFactory.getInstance().findFirst(TcManticAlmacenesArticulosDto.class, "almacenArticulo", params);
//      if(destino != null){
//        nuevoStock = destino.getStock()- this.cantidad;
//        destino.setStock(nuevoStock);
//        regresar = DaoFactory.getInstance().update(sesion, destino)> 0L;
//      }
//			else {
//        destino = new TcManticAlmacenesArticulosDto();
//        destino.setIdAlmacen(idAlmacenDestino);
//        destino.setIdArticulo(this.almanecArticuloOrigen.getIdArticulo());
//        destino.setIdUsuario(JsfBase.getIdUsuario());
//        destino.setStock(this.cantidad);
//        destino.setMinimo(this.almanecArticuloOrigen.getMinimo());
//        destino.setMaximo(this.almanecArticuloOrigen.getMaximo());
//        general= (TcManticAlmacenesUbicacionesDto)DaoFactory.getInstance().findFirst(sesion, TcManticAlmacenesUbicacionesDto.class, params, "general");
//				if(general== null) {
//  				general= new TcManticAlmacenesUbicacionesDto("GENERAL", "", "GENERAL", "", "", JsfBase.getAutentifica().getPersona().getIdUsuario(), this.idAlmacenDestino, -1L);
//					DaoFactory.getInstance().insert(sesion, general);
//        } // if
//        destino.setIdAlmacenUbicacion(general.getIdAlmacenUbicacion());
//        regresar = DaoFactory.getInstance().insert(sesion, destino) >= 0L;
//      }  
//    } // try 
//    catch (Exception e) {
//      throw e;
//    } // catch		
//    finally {
//      Methods.clean(params);
//    } // finally
    return regresar;
  } // eliminarTransferencia

}
