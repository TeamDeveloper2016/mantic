package mx.org.kaana.mantic.catalogos.conteos.reglas;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.hibernate.Session;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.db.dto.TcManticDispositivosDto;
import mx.org.kaana.mantic.db.dto.TcManticVersionesDto;
import org.apache.log4j.Logger;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 03/02/2024
 *@time 13:29:04 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Monitoreo extends IBaseTnx implements Serializable {

  private static final Logger LOG= Logger.getLogger(Monitoreo.class);
  private static final long serialVersionUID= -3186367186737177671L;
 
	private Long idDispositivo;	
	private Long idActivo;
	private String messageError;

	public Monitoreo(Long idDispositivo) {
    this(idDispositivo, 0L);
	} // Transaccion
  
	public Monitoreo(Long idDispositivo, Long idActivo) {
    this.idDispositivo= idDispositivo;
		this.idActivo     = idActivo;
  	this.messageError= "Ocurrio un error en el dispositivo";
	} // Transaccion
	
	public String getMessageError() {
		return messageError;
	} 

	@Override
	protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {		
		boolean regresar= Boolean.FALSE;
		try {
			switch(accion) {
				case PROCESAR:
          regresar= this.toActivar(sesion);
    			break;
				case ELIMINAR:
          regresar= this.toEliminar(sesion);
					break;
				case DEPURAR:
          regresar= this.toDepurar(sesion);
					break;
			} // switch
			if(!regresar)
        throw new Exception("Error al procesar el dispositivo");
		} // try
		catch (Exception e) {		
			Error.mensaje(e);
      this.messageError= "Error: ["+ e+ "]";
			throw new Exception(this.messageError.concat("<br/>")+ e);
		} // catch		
		return regresar;
	}	
  
  private boolean toEliminar(Session sesion) throws Exception {
    Boolean regresar= Boolean.FALSE;
    try {   
      regresar= DaoFactory.getInstance().delete(sesion, TcManticDispositivosDto.class, this.idDispositivo)> 0L;
    } // try
    catch (Exception e) {
      throw e;
    } // catch	
    return regresar;    
  }
  
  private boolean toActivar(Session sesion) throws Exception {
    Boolean regresar          = Boolean.FALSE;
    Map<String, Object> params= new HashMap<>();
    try {   
      params.put("idActivo", this.idActivo);      
      regresar= DaoFactory.getInstance().update(sesion, TcManticDispositivosDto.class, this.idDispositivo, params)> 0L;
    } // try
    catch (Exception e) {
      throw e;
    } // catch	
    finally {
      Methods.clean(params);
    } // finally
    return regresar;    
  }
  
  private boolean toDepurar(Session sesion) throws Exception {
    Boolean regresar= Boolean.FALSE;
    try {   
      regresar= DaoFactory.getInstance().delete(sesion, TcManticVersionesDto.class, this.idDispositivo)> 0L;
    } // try
    catch (Exception e) {
      throw e;
    } // catch	
    return regresar;    
  }
  
} 