package mx.org.kaana.mantic.inventarios.creditos.reglas;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Value;
import org.hibernate.Session;
import mx.org.kaana.kajool.enums.EAccion;
import static mx.org.kaana.kajool.enums.EAccion.AGREGAR;
import static mx.org.kaana.kajool.enums.EAccion.ELIMINAR;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.db.dto.TcManticCreditosBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticCreditosNotasDto;
import mx.org.kaana.mantic.db.dto.TcManticDevolucionesBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticDevolucionesDto;
import mx.org.kaana.mantic.db.dto.TcManticNotasEntradasDto;
import org.apache.log4j.Logger;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 7/05/2018
 *@time 03:29:13 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Transaccion extends IBaseTnx implements Serializable {

  private static final Logger LOG = Logger.getLogger(Transaccion.class);
	private static final long serialVersionUID=-6069204157451117543L;
 
	private TcManticCreditosNotasDto orden;	
	private Double importe;
	private String messageError;
	private TcManticCreditosBitacoraDto bitacora;

	public Transaccion(TcManticCreditosNotasDto orden, TcManticCreditosBitacoraDto bitacora) {
		this(orden, 0D);
		this.bitacora= bitacora;
	}
	
	public Transaccion(TcManticCreditosNotasDto orden, Double importe) {
		this.orden  = orden;		
		this.importe= importe;
	} 

	public String getMessageError() {
		return messageError;
	}

	@Override
	protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {		
		boolean regresar          = false;
		TcManticCreditosBitacoraDto bitacoraNota= null;
		try {
			this.messageError= "Ocurrio un error en ".concat(accion.name().toLowerCase()).concat(" el nota de crédito.");
			switch(accion) {
				case AGREGAR:
					Long consecutivo= this.toSiguiente(sesion);
					this.orden.setConsecutivo(Fecha.getAnioActual()+ Cadena.rellenar(consecutivo.toString(), 5, '0', true));
					this.orden.setOrden(consecutivo);
					this.orden.setEjercicio(new Long(Fecha.getAnioActual()));
  				this.orden.setIdCreditoEstatus(3L);
					switch(this.orden.getIdTipoCreditoNota().intValue()) {
						case 1:
							this.orden.setIdNotaEntrada(null);
							this.orden.setIdProveedor(null);
							break;
						case 2:
							this.orden.setIdDevolucion(null);
							this.orden.setIdProveedor(null);
							break;
						case 3:
							this.orden.setIdDevolucion(null);
							this.orden.setIdNotaEntrada(null);
							break;
					} // switch
					regresar= DaoFactory.getInstance().insert(sesion, this.orden)>= 1L;
					bitacoraNota= new TcManticCreditosBitacoraDto(this.orden.getConsecutivo(), "", this.orden.getIdCreditoEstatus(), -1L, JsfBase.getIdUsuario(), this.orden.getIdCreditoNota(), this.orden.getImporte());
					regresar= DaoFactory.getInstance().insert(sesion, bitacoraNota)>= 1L;
					if(this.orden.getIdTipoCreditoNota().equals(1L))
						this.toCheckOrdenDevolucion(sesion);
					break;				
				case MODIFICAR:
					TcManticCreditosNotasDto anterior= (TcManticCreditosNotasDto)DaoFactory.getInstance().findById(TcManticCreditosNotasDto.class, this.orden.getIdCreditoNota());
					this.orden.setIdCreditoEstatus(3L);
					bitacoraNota= new TcManticCreditosBitacoraDto(this.orden.getConsecutivo(), "", this.orden.getIdCreditoEstatus(), -1L, JsfBase.getIdUsuario(), this.orden.getIdCreditoNota(), this.orden.getImporte());
					regresar= DaoFactory.getInstance().insert(sesion, bitacoraNota)>= 1L;
					regresar= DaoFactory.getInstance().update(sesion, this.orden)>= 1L;
					
					this.importe+= (this.orden.getImporte()- anterior.getImporte());
					if(this.orden.getIdTipoCreditoNota().equals(1L))
						this.toCheckOrdenDevolucion(sesion);
					break;
				case ELIMINAR:
					regresar= DaoFactory.getInstance().delete(sesion, this.orden)>= 1L;
					this.orden.setIdCreditoEstatus(2L);
					bitacoraNota= new TcManticCreditosBitacoraDto(this.orden.getConsecutivo(), "", this.orden.getIdCreditoEstatus(), -1L, JsfBase.getIdUsuario(), this.orden.getIdCreditoNota(), this.orden.getImporte());
					regresar= DaoFactory.getInstance().insert(sesion, bitacoraNota)>= 1L;
					this.importe-= this.orden.getImporte();
					if(this.orden.getIdTipoCreditoNota().equals(1L)) 
						this.toCheckOrdenDevolucion(sesion);
					break;
			} // switch
			if(!regresar)
        throw new Exception("");
		} // try
		catch (Exception e) {
      Error.mensaje(e);			
			throw new Exception(this.messageError.concat("\n\n")+ e.getMessage());
		} // catch		
		LOG.info("Se genero de forma correcta la nota de crédito: "+ this.orden.getConsecutivo());
		return regresar;
	}	// ejecutar
	
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
	
	private void toCheckOrdenDevolucion(Session sesion) throws Exception {
		try {
			sesion.flush();
			TcManticDevolucionesDto devolucion= (TcManticDevolucionesDto)DaoFactory.getInstance().findById(sesion, TcManticDevolucionesDto.class, this.orden.getIdDevolucion());
			if(this.orden.getImporte().equals(devolucion.getTotal()))
				devolucion.setIdDevolucionEstatus(5L); // TERMINADA
			else 
  			if(this.importe.equals(this.orden.getImporte()))
  				devolucion.setIdDevolucionEstatus(6L); // SALDADA
				else
  				devolucion.setIdDevolucionEstatus(4L); // PARCIALIZADA
			DaoFactory.getInstance().update(sesion, devolucion);
			TcManticDevolucionesBitacoraDto estatus= new TcManticDevolucionesBitacoraDto(devolucion.getIdDevolucionEstatus(), "", this.orden.getIdDevolucion(), JsfBase.getIdUsuario(), -1L, devolucion.getConsecutivo(), devolucion.getTotal());
			DaoFactory.getInstance().insert(sesion, estatus);
		} // try
		catch (Exception e) {
			throw e;
		} // catch
	} 
	
} 