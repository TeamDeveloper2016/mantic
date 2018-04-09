package mx.org.kaana.kajool.procesos.captura.reglas;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 13/10/2016
 *@time 12:16:53 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import java.util.HashMap;
import java.util.Map;
import org.hibernate.Session;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.kajool.db.dto.TcJanalMuestrasDto;
import mx.org.kaana.kajool.db.dto.TrJanalMovimientosDto;
import mx.org.kaana.kajool.db.dto.TrJanalPersonasDto;
import mx.org.kaana.kajool.db.dto.TrJanalVisitasDto;
import mx.org.kaana.kajool.procesos.beans.Cuestionario;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.reflection.Methods;
import org.apache.log4j.Logger;

public class Transaccion extends IBaseTnx {

  private static final Logger LOG = Logger.getLogger(Transaccion.class);

  private TrJanalMovimientosDto trJanalMovimientoDto;
	private TrJanalVisitasDto trJanalVisitaDto;
	private Cuestionario cuestionario;	
	private boolean finalizar;

	public Transaccion (boolean finalizar, TrJanalMovimientosDto trJanalMovimientoDto, TrJanalVisitasDto trJanalVisitaDto, Cuestionario cuestionario) {
		this.trJanalMovimientoDto= trJanalMovimientoDto;
		this.trJanalVisitaDto    = trJanalVisitaDto;
		this.cuestionario        = cuestionario;
		this.finalizar           = finalizar;
  } // Transaccion

  @Override
  protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {
    boolean regresar= false;		
    try {
      switch(accion) {
        case AGREGAR :					
					regresar= guardarCaratula(sesion) && guardarPersonas(sesion) && guardarFamilias(sesion) && guardarModulo(sesion) && guardarVisitas(sesion) && guardarMovimiento(sesion);												
        break;
				case JUSTIFICAR:
					regresar= guardarVisitas(sesion) && guardarMovimiento(sesion);
				break;					
      } // switch
			if(!regresar)
				throw new RuntimeException("No se modifico ningun registro");
      LOG.info("Transaccion ".concat(accion.name()).concat(" para ? ["+ regresar+ "]."));
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  } // ejecutar

	private boolean guardarCaratula(Session sesion) throws Exception {
		boolean regresar= false;
		try {
			if(this.cuestionario.getTrJanalCaratulaDto().getIdCaratula().equals(Constantes.SQL_TODOS_REGISTROS)) 
				regresar= DaoFactory.getInstance().insert(sesion, this.cuestionario.getTrJanalCaratulaDto())> 0;		
			else
				regresar= DaoFactory.getInstance().update(sesion, this.cuestionario.getTrJanalCaratulaDto())> 0;							
		} // try
		catch (Exception e) {
      throw e;
    } // catch
		return regresar;
	}

	private boolean guardarPersonas(Session sesion) throws Exception {
		boolean regresar= false;
		try {
			for(TrJanalPersonasDto persona: this.cuestionario.getPersonas()) {				
				if(persona.getIdPersona().equals(Constantes.SQL_TODOS_REGISTROS)) {
					persona.setFolioCuest(this.cuestionario.getTrJanalCaratulaDto().getFolioCuest());
					persona.setConsecutiv(this.cuestionario.getTrJanalCaratulaDto().getConsecutiv());
					persona.setCveUpm(this.cuestionario.getTrJanalCaratulaDto().getCveUpm());
					persona.setIdMuestra(this.cuestionario.getTrJanalCaratulaDto().getIdMuestra());
					persona.setIdCaratula(this.cuestionario.getTrJanalCaratulaDto().getIdCaratula());
					persona.setIdUsuario(this.cuestionario.getTrJanalCaratulaDto().getIdUsuario());					
					regresar= DaoFactory.getInstance().insert(sesion, persona)> 0;							
				} // if
				else
					regresar= DaoFactory.getInstance().update(sesion, persona)> 0;											
			} // for
		} // try
		catch (Exception e) {
      throw e;
    } // catch
		return regresar;
	}

	private boolean guardarFamilias(Session sesion) throws Exception {
		boolean regresar= false;
		try {			
			if((this.cuestionario.getPersonas().size()> 0)) {
				if(this.cuestionario.getTrJanalFamiliasDto().getIdFamilia().equals(Constantes.SQL_TODOS_REGISTROS)) {
					this.cuestionario.getTrJanalFamiliasDto().setFolioCuest(this.cuestionario.getTrJanalCaratulaDto().getFolioCuest());
					this.cuestionario.getTrJanalFamiliasDto().setCveUpm(this.cuestionario.getTrJanalCaratulaDto().getCveUpm());
					this.cuestionario.getTrJanalFamiliasDto().setConsecutiv(this.cuestionario.getTrJanalCaratulaDto().getConsecutiv());
					this.cuestionario.getTrJanalFamiliasDto().setIdMuestra(this.cuestionario.getTrJanalCaratulaDto().getIdMuestra());		
					this.cuestionario.getTrJanalFamiliasDto().setIdUsuario(this.cuestionario.getTrJanalCaratulaDto().getIdUsuario());		
					regresar= DaoFactory.getInstance().insert(sesion, this.cuestionario.getTrJanalFamiliasDto())> 0;
				} // if
				else
					regresar= DaoFactory.getInstance().update(sesion, this.cuestionario.getTrJanalFamiliasDto())>0;		
			} // if
			else
				regresar= true;
		} // try
		catch (Exception e) {
      throw e;
    } // catch
		return regresar;
	}

	private boolean guardarModulo(Session sesion) throws Exception {
		boolean regresar= false;
		try {
			if((this.cuestionario.getTrJanalModuloDto().getGrApo()!= null)) {
				if(this.cuestionario.getTrJanalModuloDto().getIdModulo().equals(Constantes.SQL_TODOS_REGISTROS)) {
					this.cuestionario.getTrJanalModuloDto().setFolioCuest(this.cuestionario.getTrJanalCaratulaDto().getFolioCuest());
					this.cuestionario.getTrJanalModuloDto().setIdUsuario(this.cuestionario.getTrJanalCaratulaDto().getIdUsuario());
					this.cuestionario.getTrJanalModuloDto().setCveUpm(this.cuestionario.getTrJanalCaratulaDto().getCveUpm());
					this.cuestionario.getTrJanalModuloDto().setConsecutiv(this.cuestionario.getTrJanalCaratulaDto().getConsecutiv());
					this.cuestionario.getTrJanalModuloDto().setIdMuestra(this.cuestionario.getTrJanalCaratulaDto().getIdMuestra());		
					this.cuestionario.getTrJanalModuloDto().setIdCaratula(this.cuestionario.getTrJanalCaratulaDto().getIdCaratula());		
					regresar= DaoFactory.getInstance().insert(sesion, this.cuestionario.getTrJanalModuloDto())> 0;	
				} // if
				else
					regresar= DaoFactory.getInstance().update(sesion, this.cuestionario.getTrJanalModuloDto())> 0;
			} // if
			else
				regresar= true;
		} // try
		catch (Exception e) {
      throw e;
    } // catch
		return regresar;
	}
	
	private boolean guardarVisitas(Session sesion) throws Exception {		
		boolean regresar= false;
		try {			
			if(finalizar)
				regresar= DaoFactory.getInstance().insert(sesion, this.trJanalVisitaDto)> 0;
			else
				regresar= !finalizar;
		} // try
		catch (Exception e) {
      throw e;
    } // catch
		return regresar;
	}

	private boolean guardarMovimiento(Session sesion) throws Exception {
		TrJanalMovimientosDto movimiento= null;
		boolean regresar                = false;		
		try {
			if(finalizar) {
				movimiento= new TrJanalMovimientosDto();
				movimiento.setIdMuestra(this.trJanalVisitaDto.getIdMuestra());
				movimiento.setIdSupervisa(this.trJanalMovimientoDto.getIdSupervisa());
				movimiento.setIdUsuario(this.trJanalMovimientoDto.getIdUsuario());
				movimiento.setObservaciones(this.trJanalMovimientoDto.getObservaciones());
				movimiento.setIdEstatus(this.trJanalMovimientoDto.getIdEstatus());
				DaoFactory.getInstance().insert(sesion, movimiento);
				regresar= actualizarMuestra(sesion, movimiento.getIdMovimiento());
			} // if
			else
				regresar= !finalizar;
		} // try
		catch (Exception e) {
      throw e;
    } // catch
		return regresar;
	}
	
	private boolean actualizarMuestra(Session sesion, Long idMovimiento) throws Exception {
		boolean regresar         = false;
    Map<String, Object>params= null;
    try {
			params= new HashMap<>();
			if(finalizar) {				
				params.put("idMovimiento", idMovimiento);							
				regresar= DaoFactory.getInstance().update(sesion, TcJanalMuestrasDto.class, this.trJanalMovimientoDto.getIdMuestra(), params) > 0L;			
			} // if
			else
				regresar= !finalizar;
    } // try // try
    catch (Exception e) {
      throw e;
    } // catch
		finally {
			Methods.clean(params);
		} // finally
    return regresar;
	}
}
