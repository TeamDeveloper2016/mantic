package mx.org.kaana.kalan.catalogos.pacientes.expedientes.reglas;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.kalan.catalogos.pacientes.expedientes.beans.Expediente;
import mx.org.kaana.kalan.db.dto.TcKalanExpedientesBitacoraDto;
import mx.org.kaana.kalan.db.dto.TcKalanExpedientesDto;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import org.hibernate.Session;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Transaccion extends IBaseTnx {

	private static final Log LOG=LogFactory.getLog(Transaccion.class);
	
	private Entity cliente;
  private Long idExpediente;
	private List<Expediente> documentos;
  private String messageError;

	public Transaccion(Long idExpediente) {
    this.idExpediente= idExpediente;
  }
  
	public Transaccion(Entity cliente, List<Expediente> documentos) {
		this.cliente   = cliente;
		this.documentos= documentos;
	}

  @Override
  protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {
    boolean regresar = false;
    try {
      switch (accion) {
				case REGISTRAR:
					regresar= this.toUpdateDeleteFile(sesion);
					break;
				case ELIMINAR:
					regresar= this.toDeleteFile(sesion);
					break;
      } // switch
      if (!regresar) {
        throw new Exception("");
      }
    } // try
    catch (Exception e) {
      throw new Exception(this.messageError.concat("<br/>")+ e);
    } // catch		
    return regresar;
  } // ejecutar

	protected Boolean toUpdateDeleteFile(Session sesion) throws Exception {
    Boolean regresar          = Boolean.FALSE;
		TcKalanExpedientesDto tmp = null;
    Map<String, Object> params= new HashMap<>();
    try {
      if(this.cliente.toLong("idCliente")!= -1L) {			
        for (Expediente documento: this.documentos) {
          tmp= new TcKalanExpedientesDto(
            this.cliente.toLong("idCliente"), // Long idCliente, 
            documento.getOriginal(), // String archivo, 
            documento.getRuta(), // String ruta, 
            -1L, // Long idExpediente, 
            null, // Long idCita, 
            documento.getName(), // String nombre, 
            documento.getFileSize(), // Long tamanio, 
            JsfBase.getIdUsuario(), // Long idUsuario, 
            documento.getIdComodin(), // Long idTipoArchivo, 
            1L, // Long idExpedienteEstatus, 
            documento.getObservaciones(), // String observaciones,
            Configuracion.getInstance().getPropiedadSistemaServidor("path.expedientes").concat(documento.getRuta()).concat(documento.getName()) // String alias     
          );
          params.put("nombre", documento.getName());
          TcKalanExpedientesDto exists= (TcKalanExpedientesDto)DaoFactory.getInstance().toEntity(TcKalanExpedientesDto.class, "TcKalanExpedientesDto", "identically", tmp.toMap());
          if(exists== null) 
            DaoFactory.getInstance().insert(sesion, tmp);
          else {
            tmp.setIdExpediente(exists.getIdExpediente());
            DaoFactory.getInstance().update(sesion, tmp);
          } // if  
          sesion.flush();
          this.toCheckDeleteFile(sesion, documento.getName());
        } // for
        regresar= Boolean.TRUE;
      } // if
    } // try
    catch (Exception e) {
      throw e;
    } // catch	      
    finally {
      Methods.clean(params);
    } // finally
    return regresar;
	} // toUpdateDeleteXml
 
	protected Boolean toDeleteFile(Session sesion) throws Exception {
    Boolean regresar= Boolean.FALSE;
    try {
      TcKalanExpedientesDto item= (TcKalanExpedientesDto)DaoFactory.getInstance().findById(TcKalanExpedientesDto.class, this.idExpediente);
      if(item!= null) {
        item.setIdExpedienteEstatus(2L); // ELIMINADA
        DaoFactory.getInstance().update(item);
        TcKalanExpedientesBitacoraDto bitacora= new TcKalanExpedientesBitacoraDto(
          JsfBase.getIdUsuario(), // Long idUsuario, 
          item.getIdExpedienteEstatus(), // Long idExpedienteEstatus, 
          item.getIdExpediente(), // Long idExpediente, 
          null, // String observaciones, 
          -1L // Long idExpedienteBitacora
        );
        DaoFactory.getInstance().insert(bitacora);
      } // if
      regresar= Boolean.TRUE;
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    return regresar;
  }  
  
}