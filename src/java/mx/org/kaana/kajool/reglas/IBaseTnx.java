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
import org.hibernate.Session;
import org.hibernate.Transaction;

public abstract class IBaseTnx {

	private Long idFuenteDato;

	public IBaseTnx() {
		this(-1L);
	}

	public IBaseTnx(Long idFuenteDato) {
		this.idFuenteDato= idFuenteDato;
	}

	protected abstract boolean ejecutar(Session sesion, EAccion accion) throws Exception;

	public final boolean ejecutar(EAccion accion) throws Exception {
		boolean regresar       = false;
		Session session        = null;
		Transaction transaction= null;
		try {
			session= SessionFactoryFacade.getInstance().getSession(this.idFuenteDato);
			transaction= session.beginTransaction();
			session.clear();
			regresar= ejecutar(session, accion);
			transaction.commit();
		} // try
		catch (Exception e) {
			if (transaction!= null) {
				transaction.rollback();
			} // if
			throw e;
		} // catch
		finally {
			if (session!= null) {
				session.close();
			} // if
			transaction= null;
			session    = null;
		} // finally
		return regresar;
	}
}
