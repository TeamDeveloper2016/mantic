package mx.org.kaana.mantic.catalogos.iva.reglas;

import java.util.HashMap;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.mantic.db.dto.TcManticArticulosDto;
import mx.org.kaana.mantic.db.dto.TcManticHistorialIvaDto;
import org.hibernate.Session;

public class Transaccion extends IBaseTnx {

	private TcManticHistorialIvaDto iva;	
	private String messageError;
	private Boolean aplicar;

	public Transaccion(TcManticHistorialIvaDto iva) {
		this(iva, false);
	}

	public Transaccion(TcManticHistorialIvaDto iva, Boolean aplicar) {
		this.iva    = iva;		
		this.aplicar= aplicar;
	} // Transaccion

	public String getMessageError() {
		return messageError;
	}

	@Override
	protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {		
		boolean regresar          = false;
		Map<String, Object> params= new HashMap<>();
		try {
			params.put("iva", this.iva.getImporte());
			params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
			this.messageError= "Ocurrio un error al ".concat(accion.name().toLowerCase()).concat(" el registro del IVA");
			switch(accion){
				case AGREGAR:
					regresar= DaoFactory.getInstance().insert(sesion, this.iva)>= 1L;
					break;
				case MODIFICAR:
					regresar= DaoFactory.getInstance().update(sesion, this.iva)>= 1L;
					break;				
				case ELIMINAR:
					regresar= DaoFactory.getInstance().delete(sesion, this.iva)>= 1L;
					break;
			} // switch
			if(this.aplicar)
				regresar= DaoFactory.getInstance().updateAll(sesion, TcManticArticulosDto.class, params)>= 0L;
			if(!regresar)
        throw new Exception("");
		} // try
		catch (Exception e) {			
			throw new Exception(this.messageError.concat("<br/>")+ e);
		} // catch		
		return regresar;
	}	// ejecutar
	
}