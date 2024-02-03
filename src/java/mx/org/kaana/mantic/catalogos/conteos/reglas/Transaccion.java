package mx.org.kaana.mantic.catalogos.conteos.reglas;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.hibernate.Session;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.mantic.db.dto.TcManticConteosBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticConteosDetallesDto;
import mx.org.kaana.mantic.db.dto.TcManticConteosDto;
import org.apache.log4j.Logger;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 03/02/2024
 *@time 13:29:04 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Transaccion extends IBaseTnx implements Serializable {

  private static final Logger LOG = Logger.getLogger(Transaccion.class);
	private static final long serialVersionUID=-3186367186737677671L;
 
	private TcManticConteosDto conteo;	
	private String messageError;
	private TcManticConteosBitacoraDto bitacora;

	public Transaccion(TcManticConteosDto conteo) {
    this.conteo= conteo;
	} // Transaccion
  
	public Transaccion(TcManticConteosDto conteo, TcManticConteosBitacoraDto bitacora) {
    this.conteo= conteo;
		this.bitacora= bitacora;
	} // Transaccion
	
	public String getMessageError() {
		return messageError;
	} // getMessageError

	@Override
	protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {		
		boolean regresar          = Boolean.FALSE;
		Map<String, Object> params= new HashMap<>();
		try {
			this.messageError= "Ocurrio un error al ".concat(accion.name().toLowerCase()).concat(" el conteo");
			switch(accion) {
				case PROCESAR:
					break;
				case ELIMINAR:
					this.conteo.setIdConteoEstatus(4L);
					regresar= DaoFactory.getInstance().update(sesion, this.conteo)>= 1L;
					break;
				case JUSTIFICAR:
					if(DaoFactory.getInstance().insert(sesion, this.bitacora)>= 1L) {
						this.conteo.setIdConteoEstatus(this.bitacora.getIdConteoEstatus());
						regresar= DaoFactory.getInstance().update(sesion, this.conteo)>= 1L;
					} // if
					break;
				case DEPURAR:
          params.put("idConteo", this.conteo.getIdConteo());
          DaoFactory.getInstance().deleteAll(TcManticConteosBitacoraDto.class, params);
          DaoFactory.getInstance().deleteAll(TcManticConteosDetallesDto.class, params);
          regresar= DaoFactory.getInstance().deleteAll(TcManticConteosDto.class, params)>= 1L;
					break;
			} // switch
			if(!regresar)
        throw new Exception("");
		} // try
		catch (Exception e) {		
			Error.mensaje(e);
			throw new Exception(this.messageError.concat("<br/>")+ e);
		} // catch		
		return regresar;
	}	
  
} 