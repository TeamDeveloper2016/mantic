/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.kaana.kajool.procesos.mantenimiento.contadores.reglas;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.db.dto.TcJanalAyudasDto;

/**
 *
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public final class Ayudas {

  private static final Log LOG=LogFactory.getLog(Ayudas.class);
  private Map<Long, String> ayudas;
  private static Ayudas instance;
  private static Object mutex;

  static {
    mutex=new Object();
  }

  public Ayudas() {
    LOG.info("Instanciando clase de Ayudas");
    this.ayudas= new HashMap();
    load();
  }

  public static Ayudas getInstance() {
    synchronized (mutex) {
      if (instance==null) {
        instance= new Ayudas();
      }
    } // if
    return instance;
  }

  public void reload() {
    try  {
      instance= new Ayudas();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
  }

  public String value(Long idAyuda) {
    if(this.ayudas.containsKey(idAyuda))
      return this.ayudas.get(idAyuda).toString();
    else
      return "El texto de la ayuda no se encuentra definido!";
  }

  private void load() {
    List<TcJanalAyudasDto> listaAyudas= null;
    try {
      LOG.info("Cargando ayudas de tooltip");
      listaAyudas= DaoFactory.getInstance().findAll(TcJanalAyudasDto.class);
      for(TcJanalAyudasDto ayuda: listaAyudas) {
        this.ayudas.put(ayuda.getKey(), ayuda.getDescripcion());
      } // for
    } // try // try
    catch(Exception e) {
      JsfBase.addMessageError(e);
      Error.mensaje(e);
    }
    finally {
      Methods.clean(listaAyudas);
    } // finally
  }

  @Override
  public void finalize() {
    Methods.clean(this.ayudas);
  }

}
