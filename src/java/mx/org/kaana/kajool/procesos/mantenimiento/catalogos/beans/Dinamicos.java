package mx.org.kaana.kajool.procesos.mantenimiento.catalogos.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 21/09/2015
 *@time 04:10:40 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public final class Dinamicos implements Serializable {

	private static Log LOG= LogFactory.getLog(Dinamicos.class);
	
  private static Dinamicos instance;
  private static Object mutex;

	private List<Dinamico> definiciones;
	
  static {
    mutex = new Object();
  }

  private Dinamicos() {
		this.definiciones= new ArrayList();
  }

  public static Dinamicos getInstance() {
    synchronized (mutex) {
      if (instance == null) {
        instance = new Dinamicos();
      }
    } // if
    return instance;
  }

	public List<Dinamico> getDefiniciones() {
		return Collections.unmodifiableList(definiciones);
	}

	public Dinamico add(Long idCatalogo, Boolean reload) throws Exception {
		Dinamico regresar         = null;
		Map<String, Object> params= null;
		try {
		  regresar = new Dinamico(idCatalogo);
			int index= getDefiniciones().indexOf(regresar);
			if((index< 0 && idCatalogo> 0) || reload) {
				 params= new HashMap<String, Object>();
				 params.put("idCatalogo", idCatalogo);
  		   regresar= (Dinamico)DaoFactory.getInstance().toEntity(Dinamico.class, "TcJanalCatalogosDto", "dinamicos", params);
				 if(regresar!= null) {
				   regresar.init();
					 if(index>= 0)
					   this.definiciones.remove(index);
           this.definiciones.add(regresar);
				 } // if
			} // if
			else
				regresar= getDefiniciones().get(index);
		} // try
		catch(Exception e) {
			JsfBase.addMessageError(e);
			throw e;
		} // catch
		finally {
			Methods.clean(params);
		}
		return regresar;
	}

	public Dinamico add(Long idCatalogo) throws Exception {
		return add(idCatalogo, Boolean.FALSE);
	}
	
	public Dinamico get(Long idCatalogo) {
		int index= getDefiniciones().indexOf(new Dinamico(idCatalogo));
	  return getDefiniciones().get(index);	
	}
	
	public static void main(String ... args) throws Exception {
		Dinamico dinamico= Dinamicos.getInstance().add(67L);
	  LOG.debug("-----------------------------------------");	
	  LOG.debug(dinamico);	
	  LOG.debug("-----------------------------------------");	
		for(Campo campo: dinamico.getCampos())
			LOG.debug(campo);
	}
}
