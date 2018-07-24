package mx.org.kaana.mantic.catalogos.almacenes.transferencias.reglas;

import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.mantic.db.dto.TcManticTransferenciasBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticTransferenciasDto;
import org.hibernate.Session;

public class Transaccion extends IBaseTnx {

  private TcManticTransferenciasDto dto;
  private String messageError;
  private String nombreSolicito;

	public Transaccion(TcManticTransferenciasDto dto, String nombreSolicito) {
		this.dto = dto;
    this.nombreSolicito = nombreSolicito;
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
      dtoBitacora.setIdUsuario(this.dto.getIdUsuario());
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

}
