package mx.org.kaana.mantic.ventas.caja.cierres.reglas;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.db.dto.TcManticCierresBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticCierresCajasDto;
import mx.org.kaana.mantic.db.dto.TcManticCierresRetirosDto;
import org.apache.log4j.Logger;
import org.hibernate.Session;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 7/08/2018
 *@time 01:09:18 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Transaccion extends IBaseTnx implements Serializable  {

	private static final long serialVersionUID=-2843960976905038609L;
  private static final Logger LOG = Logger.getLogger(Transaccion.class);

	private TcManticCierresRetirosDto retiro;
	private Long idCierre;
	private String messageError;
	
	public Transaccion(Long idCierre, TcManticCierresRetirosDto retiro) {
		this.idCierre= idCierre;
		this.retiro= retiro;
	}
	
	@Override
	protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {
		boolean regresar= false;
		TcManticCierresCajasDto caja       = null;
		TcManticCierresBitacoraDto bitacora= null;
		try {
			this.messageError= "Ocurrio un error en ".concat(accion.name().toLowerCase()).concat(" el retiro de caja.");
			switch(accion) {
				case AGREGAR:
					Long consecutivo= this.toSiguiente(sesion);
					this.retiro.setConsecutivo(Fecha.getAnioActual()+ Cadena.rellenar(consecutivo.toString(), 5, '0', true));
					this.retiro.setOrden(consecutivo);
					this.retiro.setEjercicio(new Long(Fecha.getAnioActual()));
					regresar= DaoFactory.getInstance().insert(sesion, this.retiro)>= 1L;
					bitacora= new TcManticCierresBitacoraDto("RETIRO DE EFECTIVO", -1L, idCierre, JsfBase.getIdUsuario(), 2L);
					regresar= DaoFactory.getInstance().insert(sesion, bitacora)>= 1L;
					caja= (TcManticCierresCajasDto)DaoFactory.getInstance().findById(TcManticCierresCajasDto.class, this.idCierre);
					caja.setSaldo(Numero.toRedondearSat(caja.getSaldo()- this.retiro.getImporte()));
					regresar= DaoFactory.getInstance().update(sesion, caja)>= 1L;
					break;
				case ELIMINAR:
					this.retiro.setObservaciones("ESTE RETIRO FUE CANCELADO :"+ this.retiro.getImporte()+ " con fecha de "+ Fecha.getHoy());
					this.retiro.setImporte(0D);
					regresar= DaoFactory.getInstance().update(sesion, this.retiro)>= 1L;
					bitacora= new TcManticCierresBitacoraDto("REINTEGRO DE EFECTIVO", -1L, idCierre, JsfBase.getIdUsuario(), 2L);
					regresar= DaoFactory.getInstance().insert(sesion, bitacora)>= 1L;
					caja= (TcManticCierresCajasDto)DaoFactory.getInstance().findById(TcManticCierresCajasDto.class, this.idCierre);
					caja.setSaldo(Numero.toRedondearSat(caja.getSaldo()+ this.retiro.getImporte()));
					regresar= DaoFactory.getInstance().update(sesion, caja)>= 1L;
					break;
			} // switch
		  if(!regresar)
        throw new Exception("");
		} // try
		catch (Exception e) {
      Error.mensaje(e);			
			throw new Exception(this.messageError.concat("\n\n")+ e.getMessage());
		} // catch		
		LOG.info("Se genero de forma correcta el registro del retiro de caja: "+ this.retiro.getConsecutivo());
		return regresar;
	}

	private Long toSiguiente(Session sesion) throws Exception {
		Long regresar= 1L;
		Map<String, Object> params=null;
		try {
			params=new HashMap<>();
			params.put("ejercicio", Fecha.getAnioActual());
			params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
			Value next= DaoFactory.getInstance().toField(sesion, "TcManticCreditosNotasDto", "siguiente", params, "siguiente");
			if(next.getData()!= null)
			  regresar= next.toLong();
		} // try
		catch (Exception e) {
			throw e;
		} // catch
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	}	
			
}
