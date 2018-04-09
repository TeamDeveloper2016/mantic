package mx.org.kaana.libs.recurso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.kajool.db.dto.TcJanalConfiguracionesDto;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;


public class TcConfiguraciones {

  private Map<String,String> propiedades;
  private static TcConfiguraciones instance;
  private static Object mutex;

  static {
    mutex=new Object();
  }

  private TcConfiguraciones() {
    setPropiedades(new HashMap<String, String>());
    init();
  }

  public static TcConfiguraciones getInstance() {
    if (instance == null || !Configuracion.getInstance().isEtapaProduccion()) {
      synchronized (mutex) {
        if (instance == null)
          instance=new TcConfiguraciones();
      } // synchronized
    } // if
    return instance;
  }

  private void setPropiedades(Map<String, String> propiedades) {
    this.propiedades = propiedades;
  }

  private Map<String, String> getPropiedades() {
    return propiedades;
  }


  private void init(){
    List<TcJanalConfiguracionesDto> listTcConfiguracionesDto= null;		
    try {
      listTcConfiguracionesDto= DaoFactory.getInstance().findAll(TcJanalConfiguracionesDto.class, Constantes.SQL_TODOS_REGISTROS);
      for(TcJanalConfiguracionesDto tcConfiguracionesDto: listTcConfiguracionesDto){
        getPropiedades().put(tcConfiguracionesDto.getLlave(), tcConfiguracionesDto.getValor());
      }// for			
    } // try
    catch (Exception e)  {
      Error.mensaje(e);
    } // catch
    finally{
      if (listTcConfiguracionesDto!= null) {
        listTcConfiguracionesDto.clear();
      }
      listTcConfiguracionesDto= null;
    } // finally
  }

  public String getPropiedad(String key) {
    return getPropiedades().get(key);
  }
	
	public boolean exist(String key) {
	  return getPropiedades().containsKey(key);
	}

  public Integer toInteger(String key) {
    return Numero.getInteger(getPropiedades().get(key));
  }

  public void reload() {
    try  {
      instance= new TcConfiguraciones();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
  }
}
