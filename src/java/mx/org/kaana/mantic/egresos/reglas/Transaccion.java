package mx.org.kaana.mantic.egresos.reglas;

import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.mantic.catalogos.articulos.beans.Importado;
import mx.org.kaana.mantic.db.dto.TcManticCreditosNotasDto;
import mx.org.kaana.mantic.db.dto.TcManticEgresosNotasDto;
import mx.org.kaana.mantic.db.dto.TcManticEmpresasPagosDto;
import mx.org.kaana.mantic.db.dto.TcManticNotasEntradasDto;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;

public class Transaccion extends IBaseTnx {

	private static final Log LOG= LogFactory.getLog(Transaccion.class);
	private String messageError;
	private Long idEgreso;
	private Long idTipoEgreso;
	private EAccion accionTipoEgreso;
	private String nota;

	public Transaccion(Long idEgreso, String nota) {
		this.idEgreso= idEgreso;
		this.nota    = nota;
	}

	public Transaccion(Long idTipoEgreso, EAccion accionTipoEgreso) {
		this.idTipoEgreso    = idTipoEgreso;
		this.accionTipoEgreso= accionTipoEgreso;
	}	
	
	public String getMessageError() {
		return messageError;
	}
			
	@Override
	protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {
		boolean regresar= false;
		try {
			switch(accion){
				case JUSTIFICAR:
					regresar= registrarNota(sesion);
					break;				
				case ELIMINAR:
					regresar= procesarDocumento(sesion);
					break;				
			} // switch
			if(!regresar)
        throw new Exception("");
		} // try
		catch (Exception e) {			
			throw new Exception(this.messageError.concat("<br/>")+ e.getMessage());
		} // catch		
		return regresar;
	} // ejecutar		
	
	private boolean registrarNota(Session sesion) throws Exception{
		boolean regresar            = false;
		TcManticEgresosNotasDto nota= null;
		try {
			nota= new TcManticEgresosNotasDto(this.idEgreso, JsfBase.getIdUsuario(), -1L, this.nota);
			regresar= DaoFactory.getInstance().insert(sesion, nota)>= 1L;
		} // try
		catch (Exception e) {			
			throw e; 
		} // catch		
		return regresar;
	} // registrarNota	
	
	private boolean procesarDocumento(Session sesion) throws Exception{
		boolean regresar                    = false;
		TcManticNotasEntradasDto notaEntrada= null;
		TcManticCreditosNotasDto creditoNota= null;
		TcManticEmpresasPagosDto empresaPago= null;
		TcManticEgresosNotasDto nota        = null;
		try {
			switch(this.accionTipoEgreso){
				case ACTIVAR:
					notaEntrada= (TcManticNotasEntradasDto) DaoFactory.getInstance().findById(sesion, TcManticNotasEntradasDto.class, this.idTipoEgreso);
					notaEntrada.setIdEgreso(null);
					regresar= DaoFactory.getInstance().update(sesion, notaEntrada)>= 1L;
					break;
				case AGREGAR: 
					creditoNota= (TcManticCreditosNotasDto) DaoFactory.getInstance().findById(sesion, TcManticCreditosNotasDto.class, this.idTipoEgreso);
					creditoNota.setIdEgreso(null);
					regresar= DaoFactory.getInstance().update(sesion, creditoNota)>= 1L;
					break;
				case ASIGNAR: 
					empresaPago= (TcManticEmpresasPagosDto) DaoFactory.getInstance().findById(sesion, TcManticEmpresasPagosDto.class, this.idTipoEgreso);
					empresaPago.setIdEgreso(null);
					regresar= DaoFactory.getInstance().update(sesion, empresaPago)>= 1L;
					break;
				case BAJAR:
					regresar= DaoFactory.getInstance().delete(sesion, TcManticEgresosNotasDto.class, this.idTipoEgreso)>= 1L;
					break;
			} // switch
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // procesarDocumento
}