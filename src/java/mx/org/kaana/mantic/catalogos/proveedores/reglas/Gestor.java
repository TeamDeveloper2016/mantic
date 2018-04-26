/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.kaana.mantic.catalogos.proveedores.reglas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ESql;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelect;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.pagina.UISelectItem;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.proveedores.beans.CondicionPago;
import mx.org.kaana.mantic.catalogos.proveedores.beans.Domicilio;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class Gestor implements Serializable {

  private static final Log LOG = LogFactory.getLog(Gestor.class);
  private static final long serialVersionUID = 4918891922274546780L;

  private List<UISelectEntity> tiposProveedores;
  private List<UISelectEntity> entidades;
  private List<UISelectEntity> municipios;
  private List<UISelectEntity> localidades;
  private List<UISelectEntity> codigosPostales;
  private List<Domicilio> direcciones;

  public Gestor() {
    this.tiposProveedores = new ArrayList();
    this.entidades = new ArrayList();
    this.municipios = new ArrayList();
    this.localidades = new ArrayList();
    this.codigosPostales = new ArrayList();
    this.direcciones = new ArrayList();
  }

  public List<UISelectEntity> getEntidades() {
    return entidades;
  }

  public List<UISelectEntity> getMunicipios() {
    return municipios;
  }

  public List<UISelectEntity> getLocalidades() {
    return localidades;
  }

  public List<UISelectEntity> getCodigosPostales() {
    return codigosPostales;
  }

  public List<UISelectEntity> getTiposProveedores() {
    return tiposProveedores;
  }

  public List<Domicilio> getDirecciones() {
    return direcciones;
  } 

  public void loadTiposProveedores() throws Exception {
    try {
      loadTiposProveedores(true);
    } // try
    catch (Exception e) {
      throw e;
    } // catch   
  }

  public void loadTiposProveedores(boolean incluyeItemTodos) throws Exception {
    Entity entityDefault = null;
    List<Columna> formatos = null;
    Map<String, Object> params = null;
    try {
      params = new HashMap();
      params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
      entityDefault = new Entity();
      formatos = new ArrayList<>();
      if (incluyeItemTodos) {
        entityDefault.put("idKey", new Value("idKey", -1L, "id_key"));
        entityDefault.put("nombre", new Value("nombre", "TODOS", "nombre"));
        this.tiposProveedores.add(0, new UISelectEntity(entityDefault));
      } // if
      formatos.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      formatos.add(new Columna("dias", EFormatoDinamicos.NUMERO_SIN_DECIMALES));
      this.tiposProveedores.addAll(UIEntity.build("TcManticTiposProveedoresDto", params, formatos));
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    finally {
      Methods.clean(params);
      Methods.clean(formatos);
    } // finally
  }

  public void loadEntidades(boolean includeItemSeleccione) {
    Map<String, Object> params = null;
    Entity entityDefault = null;
    try {
      params = new HashMap();
      params.put(Constantes.SQL_CONDICION, "id_pais=1");
      params.put("sortOrder", "order by clave");
      this.entidades.addAll(UIEntity.build("TcJanalEntidadesDto", "mto", params));
      if (includeItemSeleccione){
         entityDefault = new Entity();
         entityDefault.put("idKey", new Value("idKey", -1L, "id_key"));
         entityDefault.put("descripcion", new Value("descripcion", "SELECCIONE", "descripcion"));
         entityDefault.put("clave", new Value("clave", "00", "clave"));
        this.entidades.add(0, new UISelectEntity(entityDefault));
      }
        
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    finally {
      Methods.clean(params);
    }// finally
  }
  
  public void loadMunicipios(Long idEntidad) {
    Map<String, Object> params = null;
    try {
      params = new HashMap<>();
      params.put(Constantes.SQL_CONDICION, "id_entidad=".concat(idEntidad.toString()));
      this.municipios.addAll(UIEntity.build("TcJanalMunicipiosDto", "row", params));
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    finally {
      Methods.clean(params);
    }// finally
  }

  public void loadLocalidades(Long idMunicipio) {
    Map<String, Object> params = null;
    try {
      params = new HashMap();
      params.put(Constantes.SQL_CONDICION, "id_municipio=".concat(idMunicipio.toString()));
      this.localidades.addAll(UIEntity.build("TcJanalLocalidadesDto", "row", params));
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    finally {
      Methods.clean(params);
    }// finally
  }

  public void loadCodigosPostales(Long idLocalidad) {
    Map<String, Object> params = null;
    Entity  entityDefault = null;
    try {
      params = new HashMap();
      params.put(Constantes.SQL_CONDICION, "id_localidad=".concat(idLocalidad.toString()));
      this.codigosPostales.addAll(UIEntity.build("TcManticDomiciliosDto", "row", params));
       entityDefault = new Entity();
       entityDefault.put("idKey", new Value("idKey", -1L, "id_key"));
       entityDefault.put("calle", new Value("calle", "NUEVO", "calle"));
       entityDefault.put("codigoPostal", new Value("codigoPostal", "00", "codigoPostal"));  
       this.codigosPostales.add(0,new UISelectEntity(entityDefault));
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    finally {
      Methods.clean(params);
    }// finally
  }

  public void loadDirecciones(Long idProvedor) throws Exception {
    Map<String,Object> params =  null;
    List<Domicilio>  domiciliosActuales= null;
    try {
      params = new HashMap<>();
      params.put("idProveedor", idProvedor);
      domiciliosActuales = DaoFactory.getInstance().toEntitySet(Domicilio.class,"TcManticDomiciliosDto","row",params);
      if(domiciliosActuales!= null && !domiciliosActuales.isEmpty())
        this.direcciones.addAll(domiciliosActuales);
      else {
        this.direcciones.add(new Domicilio(ESql.SELECT));
      }
    } // try
    catch (Exception e) {
      throw e;
    }// catch
    finally {
      Methods.clean(params);
    }// finally
  }
  
  public List<UISelectItem> toTiposPagos () throws Exception {
    List<UISelectItem> regresar = null;
    try {     
      regresar = UISelect.build("VistaProveedoresDto", "proveedorCondicionPago",Collections.emptyMap(),Cadena.toList("nombrePago|clave|nombre")," ", EFormatoDinamicos.MAYUSCULAS);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  }
  
  
  public List<CondicionPago>  toCondicionesPagoProveedor(Long idProveedor) throws Exception {
    List<CondicionPago> regresar = null;
    Map<String,Object> params = new HashMap<>();
    try {
      params.put(Constantes.SQL_CONDICION, "id_proveedor".concat(idProveedor.toString()));
      regresar = DaoFactory.getInstance().toEntitySet(CondicionPago.class,"TrManticProveedorPagoDto","row",params);
    } // try
    catch (Exception e) {
      throw e;
    }// catch
    finally {
      Methods.clean(params);
    } // finally
    return regresar;
  }
  
}