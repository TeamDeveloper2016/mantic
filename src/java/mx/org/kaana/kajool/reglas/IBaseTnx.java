package mx.org.kaana.kajool.reglas;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date May 2, 2012
 * @time 1:53:33 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.hibernate.SessionFactoryFacade;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.reglas.beans.Siguiente;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.db.dto.TcManticArchivosDto;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;

public abstract class IBaseTnx {

  private static final Log LOG = LogFactory.getLog(IBaseTnx.class);
  
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
	
  /*
   TcManticArchivosDto para el campo de idEliminar
   1 SIGNIFICA QUE EL ARCHIVO SE DEBE DE QUEDAR
   2 SIGNIFICA QUE EL ARCHIVO SE TIENE QUE ELIMINAR
   3 SIGNIFICA QUE EL ARCHIVO YA FUE ELIMINADO
   4 SIGNIFICA QUE EL ARCHIVO SE INTENTO ELIMINAR PERO NO EXISTE
  */
  protected boolean toCheckDeleteFile(Session sesion) throws Exception {
    return this.toCheckDeleteFile(sesion, "NO DEFINIDO");
  }
  
  protected boolean toCheckDeleteFile(Session sesion, String nombre) throws Exception {
    boolean regresar= false;
    Long idEliminado= 1L;
    Map<String, Object> params = null;
    try {      
      params = new HashMap<>();      
      params.put("nombre", nombre);      
      TcManticArchivosDto documento= (TcManticArchivosDto)DaoFactory.getInstance().toEntity(sesion, TcManticArchivosDto.class, "TcManticArchivosDto", "identically", params);
      if(documento!= null) {
				LOG.info("Documento: "+ documento.getAlias()+ " a ratificar su existencia en el servidor");
        documento.setIdEliminado(1L);
        regresar= DaoFactory.getInstance().update(sesion, documento)> 0L;
      } // if
      params.put("registro", Fecha.getFormatoHoras(-2));      
      List<TcManticArchivosDto> items= (List<TcManticArchivosDto>)DaoFactory.getInstance().toEntitySet(sesion, TcManticArchivosDto.class, "TcManticArchivosDto", "eliminar", params);
      if(items!= null && !items.isEmpty()) {
        for (TcManticArchivosDto item: items) {
   				LOG.info("Documento: "+ item.getAlias()+ " a ser eliminado del servidor");
          File archivo= new File(item.getAlias());
          if(archivo.exists()) {
            idEliminado= 3L;
            try {
              archivo.delete();
            } // try
            catch (Exception e) {
              idEliminado= 4L;
              LOG.error("No se pudo eliminar el documento: "+ item.getAlias()+ "-> "+ e);
            } // catch	
          } // if
          else
            idEliminado= 4L;
          item.setIdEliminado(idEliminado);
          regresar= DaoFactory.getInstance().update(sesion, item)> 0L;
        } // for
      } // if
      else
        regresar= true;
    } // try
    catch (Exception e) {
      throw e;
    } // catch	
    finally {
      Methods.clean(params);
    } // finally
    return regresar;
  }
  
  protected boolean toCheckFile(Session sesion, String nombre) throws Exception {
    return this.toCheckFile(sesion, nombre, null);
  }
  
  protected boolean toCheckFile(Session sesion, String nombre, String alias) throws Exception {
    boolean regresar= false;
    Long idEliminado= 1L;
    Map<String, Object> params = null;
    try {      
      params = new HashMap<>();      
      params.put("nombre", nombre);      
      TcManticArchivosDto documento= (TcManticArchivosDto)DaoFactory.getInstance().toEntity(sesion, TcManticArchivosDto.class, "TcManticArchivosDto", "identically", params);
      if(documento!= null) {
        LOG.info("Documento: "+ documento.getAlias()+ " a ser eliminado del servidor");
        File archivo= new File(documento.getAlias());
        if(archivo.exists()) {
          idEliminado= 3L;
          try {
            archivo.delete();
          } // try
          catch (Exception e) {
            idEliminado= 4L;
            LOG.error("No se pudo eliminar el documento: "+ documento.getAlias()+ "-> "+ e);
          } // catch	
        } // if
        else
          idEliminado= 4L;
        documento.setIdEliminado(idEliminado);
        regresar= DaoFactory.getInstance().update(sesion, documento)> 0L;
      } // if
      else {
        if(alias!= null) {
          File file= new File(alias);
          try {
            if(file.exists())
              file.delete();			
          } // try  
          catch (Exception e) {
            LOG.error("No se pudo eliminar el documento: "+ alias+ "-> "+ e);
          } // catch	
        } // if  
      } // else
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
