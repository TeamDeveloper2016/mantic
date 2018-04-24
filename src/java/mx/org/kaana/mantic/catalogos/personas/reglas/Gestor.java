/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.kaana.mantic.catalogos.personas.reglas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Gestor implements Serializable {

  private static final Log LOG = LogFactory.getLog(Gestor.class);
  private static final long serialVersionUID = 4918891922274546780L;

  private List<UISelectEntity> tiposPersonas;

  public Gestor() {
    this.tiposPersonas = new ArrayList();
  }

  public List<UISelectEntity> getTiposPersonas() {
    return tiposPersonas;
  }

  public void loadTiposPersonas() throws Exception {
    Entity entityDefault = null;
    List<Columna> formatos = null;
    Map<String,Object>  params = null;
    try {
      params =  new HashMap();
      params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
      entityDefault = new Entity();
      formatos      = new ArrayList<>();
      entityDefault.put("idKey", new Value("idKey", -1L, "id_key"));
      entityDefault.put("nombre", new Value("nombre", "TODOS", "nombre"));
      formatos.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));      
      this.tiposPersonas.add(0, new UISelectEntity(entityDefault));
      this.tiposPersonas.addAll(UIEntity.build("TcManticTiposPersonasDto",params, formatos));
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    finally {
      Methods.clean(params);
      Methods.clean(formatos);
    } // finally
  }  
}
