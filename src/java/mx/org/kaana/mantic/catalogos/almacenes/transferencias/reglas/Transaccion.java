package mx.org.kaana.mantic.catalogos.almacenes.transferencias.reglas;

import java.util.HashMap;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.db.dto.TcManticTransferenciasBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticTransferenciasDto;
import org.hibernate.Session;

public class Transaccion extends IBaseTnx {

  private TcManticTransferenciasDto dto;
  private TcManticTransferenciasBitacoraDto dtoBitacora;
  private String messageError;
  private String nombreSolicito;

	public Transaccion(TcManticTransferenciasDto dto, String nombreSolicito) {
		this.dto = dto;
    this.nombreSolicito = nombreSolicito;
	}

  public Transaccion(TcManticTransferenciasBitacoraDto dtoBitacora) {
    this.dtoBitacora = dtoBitacora;
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
        case JUSTIFICAR:
          regresar= DaoFactory.getInstance().insert(sesion, this.dtoBitacora).intValue()> 0;
          if(DaoFactory.getInstance().insert(sesion, this.dtoBitacora)>= 1L){
						this.dto= (TcManticTransferenciasDto) DaoFactory.getInstance().findById(sesion, TcManticTransferenciasDto.class, this.dtoBitacora.getIdTransferencia());
						this.dto.setIdTransferenciaEstatus(this.dtoBitacora.getIdTransferenciaEstatus());
						regresar= DaoFactory.getInstance().update(sesion, this.dto)>= 1L;
					} // if
          break;
        case ELIMINAR:
          regresar= eliminarTransferencia(sesion);
          break;
      } // switch
      if (!regresar) 
        throw new Exception("");
    } // try
    catch (Exception e) {
      throw new Exception(this.messageError.concat("\n\n")+ e.getMessage());
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
      regresar= DaoFactory.getInstance().insert(sesion, dtoBitacora).intValue()> 0;
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

}
