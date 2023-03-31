package mx.org.kaana.kalan.catalogos.pacientes.expedientes.reglas;

import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.kalan.db.dto.TcKalanExpedientesBitacoraDto;
import mx.org.kaana.kalan.db.dto.TcKalanExpedientesDto;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.mantic.catalogos.articulos.beans.Importado;
import org.hibernate.Session;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Transaccion extends IBaseTnx {

	private static final Log LOG=LogFactory.getLog(Transaccion.class);
	
	private Entity cliente;
  private Long idExpediente;
	private Importado documento;
  private String messageError;

	public Transaccion(Long idExpediente) {
    this.idExpediente= idExpediente;
  }
  
	public Transaccion(Entity cliente, Importado documento) {
		this.cliente  = cliente;
		this.documento= documento;
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
    Boolean regresar         = Boolean.FALSE;
		TcKalanExpedientesDto tmp= null;
		if(this.cliente.toLong("idCliente")!= -1L) {			
			if(this.documento!= null) {
				tmp= new TcKalanExpedientesDto(
          this.cliente.toLong("idCliente"), // Long idCliente, 
          this.documento.getOriginal(), // String archivo, 
          this.documento.getRuta(), // String ruta, 
          -1L, // Long idExpediente, 
          null, // Long idCita, 
          this.documento.getName(), // String nombre, 
          this.documento.getFileSize(), // Long tamanio, 
          JsfBase.getIdUsuario(), // Long idUsuario, 
          this.documento.getIdComodin(), // Long idTipoArchivo, 
          1L, // Long idExpedienteEstatus, 
          this.documento.getObservaciones(), // String observaciones,
          Configuracion.getInstance().getPropiedadSistemaServidor("path.expedientes").concat(this.documento.getRuta()).concat(this.documento.getName()) // String alias     
        );
        TcKalanExpedientesDto exists= (TcKalanExpedientesDto)DaoFactory.getInstance().toEntity(TcKalanExpedientesDto.class, "TcKalanExpedientesDto", "identically", tmp.toMap());
				if(exists== null) 
					DaoFactory.getInstance().insert(sesion, tmp);
        else {
          tmp.setIdExpediente(exists.getIdExpediente());
					DaoFactory.getInstance().update(sesion, tmp);
        } // if  
				sesion.flush();
        this.toCheckDeleteFile(sesion, this.documento.getName());
        regresar= Boolean.TRUE;
			} // if	
  	} // if	
    return regresar;
	} // toUpdateDeleteXml
 
	protected Boolean toDeleteFile(Session sesion) throws Exception {
    Boolean regresar= Boolean.FALSE;
    try {
      TcKalanExpedientesDto item= (TcKalanExpedientesDto)DaoFactory.getInstance().findById(TcKalanExpedientesDto.class, this.idExpediente);
      if(item!= null) {
        item.setIdExpedienteEstatus(2L);
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