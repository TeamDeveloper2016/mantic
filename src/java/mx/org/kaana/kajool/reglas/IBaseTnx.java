package mx.org.kaana.kajool.reglas;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date May 2, 2012
 * @time 1:53:33 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
import mx.org.kaana.kajool.db.comun.hibernate.SessionFactoryFacade;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.reglas.beans.Siguiente;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.recurso.Configuracion;
import org.hibernate.Session;
import org.hibernate.Transaction;

public abstract class IBaseTnx {

	private	Transaction transaction;
	private Long idFuenteDato;

	public IBaseTnx() {
		this(-1L);
	}

	public IBaseTnx(Long idFuenteDato) {
		this.idFuenteDato= idFuenteDato;
	}

	protected abstract boolean ejecutar(Session sesion, EAccion accion) throws Exception;

	public final boolean ejecutar(EAccion accion) throws Exception {
		boolean regresar= false;
		Session session = null;
		try {
			session= SessionFactoryFacade.getInstance().getSession(this.idFuenteDato);
			this.transaction= session.beginTransaction();
			session.clear();
			regresar= ejecutar(session, accion);
			this.transaction.commit();
		} // try
		catch (Exception e) {
			if (this.transaction!= null) {
				this.transaction.rollback();
			} // if
			throw e;
		} // catch
		finally {
			if (session!= null) {
				session.close();
			} // if
			this.transaction= null;
			session    = null;
		} // finally
		return regresar;
	}
	
	protected void commit() throws Exception {
  	if (this.transaction!= null && this.transaction.isActive()) {
			this.transaction.commit();
			this.transaction.begin();
		} // if	
	}

	protected Siguiente toNextItem(Long orden) {
		return new Siguiente(orden);
	}

  protected int getCurrentYear() {
		//return Configuracion.getInstance().isEtapaDesarrollo()? Fecha.getAnioActual()* 10+ 1: Fecha.getAnioActual();
		return Fecha.getAnioActual();
	}	
	
  protected String getCurrentSign() {
		return Configuracion.getInstance().isEtapaDesarrollo()? ">": "<=";
	}	
	
}
