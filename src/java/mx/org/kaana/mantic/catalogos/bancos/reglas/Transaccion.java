package mx.org.kaana.mantic.catalogos.bancos.reglas;

import java.util.HashMap;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.mantic.db.dto.TcManticBancosDto;
import org.hibernate.Session;

public class Transaccion extends IBaseTnx {

	private TcManticBancosDto banco;	
	private String messageError;

	public Transaccion(TcManticBancosDto banco) {		
		this.banco= banco;		
	} // Transaccion

	public String getMessageError() {
		return messageError;
	}

	@Override
	protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {		
		boolean regresar          = false;
		Map<String, Object> params= new HashMap<>();
		try {
			params.put("idBanco", this.banco.getIdBanco());
			this.messageError= "Ocurrio un error al ".concat(accion.name().toLowerCase()).concat(" el banco");
			switch(accion){
				case AGREGAR:
					this.banco.setIdUsuario(JsfBase.getIdUsuario());
					regresar= DaoFactory.getInstance().insert(sesion, this.banco)>= 1L;
					break;
				case MODIFICAR:
					this.banco.setIdUsuario(JsfBase.getIdUsuario());
					regresar= DaoFactory.getInstance().update(sesion, this.banco)>= 1L;
					break;				
				case ELIMINAR:
					regresar= DaoFactory.getInstance().delete(sesion, this.banco)>= 1L;
					break;
			} // switch
			if(!regresar)
        throw new Exception("");
		} // try
		catch (Exception e) {			
			throw new Exception(this.messageError.concat("<br/>")+ e);
		} // catch		
		return regresar;
	}	// ejecutar	
}