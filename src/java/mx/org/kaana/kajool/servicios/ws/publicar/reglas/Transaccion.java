package mx.org.kaana.kajool.servicios.ws.publicar.reglas;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 21-sep-2015
 *@time 21:29:02
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import com.sun.jersey.core.util.Base64;
import java.sql.Timestamp;
import java.util.Calendar;
import org.hibernate.Session;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.dto.TcJanalMuestrasDto;
import mx.org.kaana.kajool.db.dto.TrJanalBitacoraMovilDto;
import mx.org.kaana.kajool.db.dto.TrJanalMovimientosDto;
import mx.org.kaana.kajool.db.dto.TrJanalPersonasDto;
import mx.org.kaana.kajool.db.dto.TrJanalVisitasDto;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.procesos.beans.Cuestionario;
import mx.org.kaana.kajool.procesos.enums.EEstatus;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.kajool.servicios.ws.publicar.beans.RespuestaMovil;
import mx.org.kaana.kajool.servicios.ws.publicar.beans.VisitasMovil;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.libs.pagina.JsfBase;
import org.apache.log4j.Logger;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.json.Decoder;

public class Transaccion extends IBaseTnx {

  private static final Logger LOG = Logger.getLogger(Transaccion.class);
	private String idMuestra;
	private String cuenta;
	private String cuestionario;
	private String visitas;	
	private Cuestionario cuestionarioMovil;
	private TrJanalVisitasDto[] visitasMovil;
	private TrJanalBitacoraMovilDto bitacora;
	private RespuestaMovil respuestaMovil;
	
	public Transaccion(String idMuestra, String cuenta, String cuestionario, String visitas) {
		this.idMuestra   = idMuestra;
		this.cuenta      = cuenta;
		this.cuestionario= cuestionario;
		this.visitas     = visitas;
	}	
	
	public Transaccion(TrJanalBitacoraMovilDto bitacora) {		
		this.bitacora         = bitacora;
	}

	public RespuestaMovil getRespuestaMovil() {
		return respuestaMovil;
	}

	public TrJanalBitacoraMovilDto getBitacora() {
		return bitacora;
	}
 
  @Override
  protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {		
    Long muestraDesencriptada                      = null;
		String cuentaDesencriptada                     = null;
    String cuestionarioDesencriptado               = null;
    String visitasDesencriptado                    = null;
    boolean regresar	                             = true;
		Long idCaratula                                = -1L;
		Long idMovimiento                              = -1L;
		TcJanalMuestrasDto tcJanalMuestraDto           = null;
		TrJanalMovimientosDto movimiento               = null;
		TrJanalMovimientosDto movimientoAnterior       = null;
    try {			
      switch(accion) {
        case AGREGAR :					          
					this.bitacora            = new TrJanalBitacoraMovilDto();			
          muestraDesencriptada		 = Long.valueOf(Base64.base64Decode((this.idMuestra)));
					cuentaDesencriptada      = Base64.base64Decode(this.cuenta);
          cuestionarioDesencriptado= Base64.base64Decode(this.cuestionario);
          visitasDesencriptado		 = Base64.base64Decode(this.visitas);
					this.bitacora.setIdMuestra(muestraDesencriptada);
          this.bitacora.setVisitas(visitasDesencriptado);
					this.bitacora.setCuestionario(cuestionarioDesencriptado);			
					this.bitacora.setCuenta(cuentaDesencriptada);					
					this.bitacora.setRegistroIntegracion(null);
					this.bitacora.setIdEstatus(EEstatus.ASIGNADO.getKey());
					if(DaoFactory.getInstance().insert(sesion, this.bitacora)> 0) {
						regresar= true;
						this.respuestaMovil= new RespuestaMovil("01", "Se registro correctamente el cuestionario y visitas");						
					} // if
					else
						this.respuestaMovil= new RespuestaMovil("99", "Error al registrar el cuestionario y visitas");											
					break;
        case PROCESAR:
					this.cuestionarioMovil= (Cuestionario) Decoder.toSerialSqllite(Cuestionario.class, bitacora.getCuestionario());
					this.visitasMovil     = (TrJanalVisitasDto[]) Decoder.toSerialSqllite(TrJanalVisitasDto[].class, bitacora.getVisitas());
					tcJanalMuestraDto = (TcJanalMuestrasDto) DaoFactory.getInstance().findById(sesion, TcJanalMuestrasDto.class, this.cuestionarioMovil.getTrJanalCaratulaDto().getIdMuestra());          					
          movimientoAnterior= (TrJanalMovimientosDto) DaoFactory.getInstance().findById(sesion, TrJanalMovimientosDto.class, tcJanalMuestraDto.getIdMovimiento());
          if(!movimientoAnterior.getIdEstatus().equals(EEstatus.ASIGNADO.getKey())) {
						this.respuestaMovil= new RespuestaMovil("97", "No es posible integrar el cuestionario, porque ya tiene información capturada.");
					} // if
					else {						
						this.cuestionarioMovil.getTrJanalCaratulaDto().setIdCaratula(null);
						this.cuestionarioMovil.getTrJanalCaratulaDto().setStatus("C");
						this.cuestionarioMovil.getTrJanalCaratulaDto().setTcuest(1L);
						this.cuestionarioMovil.getTrJanalCaratulaDto().setFuente(2L);
						idCaratula= DaoFactory.getInstance().insert(sesion, this.cuestionarioMovil.getTrJanalCaratulaDto());
						for(TrJanalPersonasDto persona: this.cuestionarioMovil.getPersonas()) {
							persona.setIdPersona(null);
							persona.setActivo("3");
							persona.setIdCaratula(idCaratula);
							DaoFactory.getInstance().insert(sesion, persona);
						} // for
						this.cuestionarioMovil.getTrJanalFamiliasDto().setIdFamilia(null);
						DaoFactory.getInstance().insert(sesion, this.cuestionarioMovil.getTrJanalFamiliasDto());
						this.cuestionarioMovil.getTrJanalModuloDto().setIdModulo(null);
						DaoFactory.getInstance().insert(sesion, this.cuestionarioMovil.getTrJanalModuloDto());
						for(TrJanalVisitasDto visita: this.visitasMovil) {
							visita.setIdVisita(null);						
							visita.setIdResultado(visita.getIdResultado()+ 1L);
							DaoFactory.getInstance().insert(sesion, visita);
						} // for					
						movimiento= new TrJanalMovimientosDto();
						movimiento.setIdMuestra(movimientoAnterior.getIdMuestra());
						movimiento.setIdUsuario(movimientoAnterior.getIdUsuario());
						movimiento.setIdSupervisa(movimientoAnterior.getIdSupervisa());
						movimiento.setIdEstatus(EEstatus.COMPLETO.getKey());
						idMovimiento= DaoFactory.getInstance().insert(sesion, movimiento);
						tcJanalMuestraDto= (TcJanalMuestrasDto) DaoFactory.getInstance().findById(sesion, TcJanalMuestrasDto.class, this.cuestionarioMovil.getTrJanalCaratulaDto().getIdMuestra());
						tcJanalMuestraDto.setIdMovimiento(idMovimiento);
						DaoFactory.getInstance().update(tcJanalMuestraDto);
						bitacora.setIdEstatus(EEstatus.COMPLETO.getKey());
						bitacora.setRegistroIntegracion(new Timestamp(Calendar.getInstance().getTimeInMillis()));
						if(DaoFactory.getInstance().update(sesion, this.bitacora)> 0) {
							regresar= true;
							this.respuestaMovil= new RespuestaMovil("02", "Se integro correctamente el cuestionario y visitas");						
						} // if
						else {
							this.respuestaMovil= new RespuestaMovil("98", "Error al integrar el cuestionario y visitas");						
						} // else
					} // else
        break;        
      } // switch
      LOG.info("Transaccion ".concat(accion.name()).concat(" para ? ["+ regresar+ "]."));
    } // try   
    catch (Exception e) {
			JsfBase.addMessageError(e);
      Error.mensaje(e);
      regresar= false;
    } // catch
    return regresar;
  }
} 