package mx.org.kaana.kajool.procesos.mantenimiento.configuracion.reglas;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.dto.TcJanalConfiguracionesDto;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.libs.pagina.JsfBase;
import org.hibernate.Session;

public class Transaccion extends IBaseTnx {

	private Long idEmpresa;
	private String messageError;

	public Transaccion(Long idEmpresa) {		
		this.idEmpresa= idEmpresa;		
	} // Transaccion

	public String getMessageError() {
		return messageError;
	}

	@Override
	protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {		
		boolean regresar          = false;
		Map<String, Object> params= new HashMap<>();
		try {
			params.put("llave", "sucursal."+ JsfBase.getAutentifica().getCredenciales().getCuenta());
			this.messageError= "Ocurrio un error al ".concat(accion.name().toLowerCase()).concat(" actualizar a configuración.");
			TcJanalConfiguracionesDto configuracion= (TcJanalConfiguracionesDto)DaoFactory.getInstance().findIdentically(sesion, TcJanalConfiguracionesDto.class, params);
			if(configuracion!= null) {
				configuracion.setValor(String.valueOf(this.idEmpresa));
				regresar= DaoFactory.getInstance().update(sesion, configuracion).intValue()>= 0;
			} // if
			else {
			  configuracion= new TcJanalConfiguracionesDto(
					"sucursal."+ JsfBase.getAutentifica().getCredenciales().getCuenta(),
					-1L, 
					String.valueOf(this.idEmpresa),
					"SUCURSAL QUE SE TOMARA POR DEFECTO DENTRO DEL SITIO",
					new Timestamp(Calendar.getInstance().getTimeInMillis()),
					JsfBase.getIdUsuario()
				);
				regresar= DaoFactory.getInstance().insert(sesion, configuracion).intValue()>= 0;
			} // else
			if(!regresar)
        throw new Exception("");
		} // try
		catch (Exception e) {			
			throw new Exception(this.messageError.concat("<br/>")+ e);
		} // catch		
		return regresar;
	}	// ejecutar	
}