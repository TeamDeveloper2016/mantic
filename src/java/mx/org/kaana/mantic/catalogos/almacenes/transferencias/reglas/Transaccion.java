package mx.org.kaana.mantic.catalogos.almacenes.transferencias.reglas;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.db.dto.TcManticAlmacenesArticulosDto;
import mx.org.kaana.mantic.db.dto.TcManticAlmacenesUbicacionesDto;
import mx.org.kaana.mantic.db.dto.TcManticTransferenciasBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticTransferenciasDto;
import org.hibernate.Session;

public class Transaccion extends IBaseTnx {

  private TcManticTransferenciasDto dto;
  private TcManticTransferenciasBitacoraDto dtoBitacora;
  private TcManticAlmacenesArticulosDto almanecArticuloOrigen;
  private String messageError;
  private String nombreSolicito;
  private String cantidad;
  private Long idAlmacenDestino;

	public Transaccion(TcManticTransferenciasDto dto, String nombreSolicito) {
		this.dto = dto;
    this.nombreSolicito = nombreSolicito;
	}

  public Transaccion(TcManticTransferenciasBitacoraDto dtoBitacora) {
    this.dtoBitacora = dtoBitacora;
  }

  public Transaccion(TcManticAlmacenesArticulosDto almanecArticuloOrigen, String cantidad, Long idAlmacenDestino) {
    this.almanecArticuloOrigen = almanecArticuloOrigen;
    this.cantidad = cantidad;
    this.idAlmacenDestino = idAlmacenDestino;
  }

  public TcManticAlmacenesArticulosDto getAlmanecArticuloOrigen() {
    return almanecArticuloOrigen;
  }

  public void setAlmanecArticuloOrigen(TcManticAlmacenesArticulosDto almanecArticuloOrigen) {
    this.almanecArticuloOrigen = almanecArticuloOrigen;
  }

  public String getCantidad() {
    return cantidad;
  }

  public void setCantidad(String cantidad) {
    this.cantidad = cantidad;
  }

  public Long getIdAlmacenDestino() {
    return idAlmacenDestino;
  }

  public void setIdAlmacenDestino(Long idAlmacenDestino) {
    this.idAlmacenDestino = idAlmacenDestino;
  }
  
  @Override
  protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {
    boolean regresar = false;
    try {			
      switch (accion) {
        case AGREGAR:
          regresar= DaoFactory.getInstance().insert(sesion, this.dto).intValue()> 0;
          regresar = agregaBitacoraTransferencia(sesion);
          break;
        case REGISTRAR:
          regresar= DaoFactory.getInstance().insert(sesion, this.dtoBitacora).intValue()> 0;
          if(regresar){
						this.dto= (TcManticTransferenciasDto) DaoFactory.getInstance().findById(sesion, TcManticTransferenciasDto.class, this.dtoBitacora.getIdTransferencia());
						this.dto.setIdTransferenciaEstatus(this.dtoBitacora.getIdTransferenciaEstatus());
						regresar= DaoFactory.getInstance().update(sesion, this.dto)>= 1L;
            if(this.dto.getIdTransferenciaEstatus().equals(5L)){
              regresar= DaoFactory.getInstance().update(almanecArticuloOrigen)>0L;
              if(regresar)
                agregaActualizaDestino(sesion);
            }
					} // if
          break;
        case ELIMINAR:
          regresar= eliminarTransferencia(sesion);
          break;
        case MODIFICAR:
          this.dto.setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
          regresar= DaoFactory.getInstance().update(sesion, this.dto).intValue()> 0;
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

  private boolean agregaBitacoraTransferencia(Session sesion) throws Exception {
    boolean regresar = true;
    TcManticTransferenciasBitacoraDto dtoBitacora = new TcManticTransferenciasBitacoraDto();
    try {
      dtoBitacora.setIdTransferencia(this.dto.getIdTransferencia());
      dtoBitacora.setIdTransferenciaEstatus(this.dto.getIdTransferenciaEstatus());
      dtoBitacora.setIdUsuario(JsfBase.getAutentifica().getPersona().getIdPersona());
      dtoBitacora.setJustificacion("Transferencia solicitada por:  ".concat(nombreSolicito));
      regresar=DaoFactory.getInstance().insert(sesion, dtoBitacora).intValue()> 0;
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    finally {
      this.messageError = "Error al insertar bitacora transferencia";
    } // finally
    return regresar;
  } // eliminarRegistros
  
  private boolean eliminarTransferencia(Session sesion) throws Exception {
    boolean regresar = false;
    Map<String, Object> params = null;
    try {
      params = new HashMap<>();
      params.put("idTransferencia", this.dto.getIdTransferencia());
      if (DaoFactory.getInstance().deleteAll(sesion, TcManticTransferenciasBitacoraDto.class, params) > -1L) {
          regresar = DaoFactory.getInstance().delete(sesion, TcManticTransferenciasDto.class, this.dto.getIdTransferencia()) >= 1L;
      } // if
    } // try 
    catch (Exception e) {
      throw e;
    } // catch		
    finally {
      Methods.clean(params);
    } // finally
    return regresar;
  } // eliminarTransferencia
  
  private boolean agregaActualizaDestino(Session sesion) throws Exception {
    boolean regresar                        = false;
    Map<String, Object> params              = null;
    TcManticAlmacenesArticulosDto destino   = null;
    TcManticAlmacenesUbicacionesDto general = null;
    Double nuevoStock                       = 0D;
    try {
      params = new HashMap<>();
      params.put("idTransferencia", this.dto.getIdTransferencia());
      params.put("idAlmacen", this.idAlmacenDestino);
      params.put("idArticulo", this.almanecArticuloOrigen.getIdArticulo());
      general= (TcManticAlmacenesUbicacionesDto)DaoFactory.getInstance().findFirst(sesion, TcManticAlmacenesUbicacionesDto.class, params, "general");
      destino = (TcManticAlmacenesArticulosDto) DaoFactory.getInstance().findFirst(TcManticAlmacenesArticulosDto.class, "almacenArticulo", params);
      if(destino != null){
        nuevoStock = destino.getStock()- Double.valueOf(this.cantidad);
        destino.setStock(nuevoStock);
        regresar = DaoFactory.getInstance().update(sesion, destino)>0L;
      }
      else{
        destino = new TcManticAlmacenesArticulosDto();
        destino.setIdAlmacen(idAlmacenDestino);
        destino.setIdArticulo(this.almanecArticuloOrigen.getIdArticulo());
        destino.setIdUsuario(JsfBase.getIdUsuario());
        destino.setStock(Double.valueOf(this.cantidad));
        destino.setMinimo(this.almanecArticuloOrigen.getMinimo());
        destino.setMaximo(this.almanecArticuloOrigen.getMaximo());
        general= (TcManticAlmacenesUbicacionesDto)DaoFactory.getInstance().findFirst(sesion, TcManticAlmacenesUbicacionesDto.class, params, "general");
				if(general== null) {
  				general= new TcManticAlmacenesUbicacionesDto("GENERAL", "", "GENERAL", "", "", JsfBase.getAutentifica().getPersona().getIdUsuario(), this.idAlmacenDestino, -1L);
					DaoFactory.getInstance().insert(sesion, general);
        }
        destino.setIdAlmacenUbicacion(general.getIdAlmacenUbicacion());
        regresar = DaoFactory.getInstance().insert(sesion, destino) >= 0L;
      }  
    } // try 
    catch (Exception e) {
      throw e;
    } // catch		
    finally {
      Methods.clean(params);
    } // finally
    return regresar;
  } // eliminarTransferencia

}
