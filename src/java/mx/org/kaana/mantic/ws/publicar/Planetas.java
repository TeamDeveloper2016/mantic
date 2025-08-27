package mx.org.kaana.mantic.ws.publicar;

import com.google.gson.Gson;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.hibernate.SessionFactoryFacade;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.kajool.reglas.beans.Siguiente;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.BouncyEncryption;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.json.Decoder;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.libs.wassenger.Bonanza;
import mx.org.kaana.mantic.db.dto.TcManticContadoresBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticContadoresDetallesDto;
import mx.org.kaana.mantic.db.dto.TcManticContadoresDto;
import mx.org.kaana.mantic.db.dto.TcManticConteosDetallesDto;
import mx.org.kaana.mantic.db.dto.TcManticConteosBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticConteosDto;
import mx.org.kaana.mantic.db.dto.TcManticDispositivosDto;
import mx.org.kaana.mantic.db.dto.TcManticTransferenciasBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticTransferenciasDetallesDto;
import mx.org.kaana.mantic.db.dto.TcManticTransferenciasDto;
import mx.org.kaana.mantic.enums.ERespuesta;
import mx.org.kaana.mantic.ws.imox.beans.Almacen;
import mx.org.kaana.mantic.ws.imox.beans.Articulo;
import mx.org.kaana.mantic.ws.imox.beans.Cantidad;
import mx.org.kaana.mantic.ws.imox.beans.Contador;
import mx.org.kaana.mantic.ws.imox.beans.Conteo;
import mx.org.kaana.mantic.ws.imox.beans.Empresa;
import mx.org.kaana.mantic.ws.imox.beans.Inventario;
import mx.org.kaana.mantic.ws.imox.beans.Item;
import mx.org.kaana.mantic.ws.imox.beans.Items;
import mx.org.kaana.mantic.ws.imox.beans.Partida;
import mx.org.kaana.mantic.ws.imox.beans.Producto;
import mx.org.kaana.mantic.ws.imox.beans.Transferencia;
import mx.org.kaana.mantic.ws.imox.beans.Ubicacion;
import mx.org.kaana.mantic.ws.imox.beans.Usuario;
import mx.org.kaana.mantic.ws.publicar.beans.Respuesta;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 16/01/2024
 * @time 04:53:38 PM 
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Planetas implements Serializable {

  private static final Log LOG = LogFactory.getLog(Planetas.class);
  
  private static final long serialVersionUID = 8944954886601403304L;
  private static final String TEXT_TOKEN= "/Eb1AjylyNNfQBLodn6Jf6Stb8NM7Hw2";
  private static final Long USER_SUPER  = 1976L;
  private static final Long USER_ADMIN  = 1991L;

  // atmosfera: es el token unico de esta aplicación
  // radio: es el id_usiario que hace la solicutud
  // densidad: es la fecha para descargar la información
  
  // SERVICIO WEB PARA LOS USUARIOS
  public String mercurio(Long radio, String densidad, String atmosfera) throws Exception {
    String regresar           = Decoder.toJson(new Respuesta(ERespuesta.CORRECTO.getCodigo(), "Proceso correcto")); 
    Map<String, Object> params= new HashMap<>();
    Respuesta respuesta       = null;
    try {
      respuesta= this.toRespueta(this.neptuno(atmosfera, radio));
      if(Objects.equals(respuesta.getCodigo(), ERespuesta.CORRECTO.getCodigo())) {
        params.put("fecha", densidad);
        ArrayList<Usuario> usuarios= (ArrayList<Usuario>)DaoFactory.getInstance().toEntitySet(Usuario.class, "VistaPlanetasDto", "mercurio", params, -1L);
        if(!Objects.equals(usuarios, null) && !usuarios.isEmpty()) {
          for (Usuario item: usuarios) {
            item.setNombre(BouncyEncryption.encrypt(item.getNombre()));
            item.setCuenta(BouncyEncryption.encrypt(item.getCuenta()));
            // item.setToken(BouncyEncryption.decrypt(item.getToken()));
          } // for
          regresar= Decoder.toJson(new Respuesta(ERespuesta.CORRECTO.getCodigo(), Decoder.cleanJson(usuarios)));
        } // if
        else
          regresar= Decoder.toJson(new Respuesta(ERespuesta.SIN_USUARIOS.getCodigo(), ERespuesta.SIN_USUARIOS.getDescripcion()));
      } // if
      else
        regresar= Decoder.toJson(respuesta);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      regresar= Decoder.toJson(new Respuesta(ERespuesta.ERROR.getCodigo(), e.getMessage()));
    } // catch
    finally {
      Methods.clean(params);
    } // finally
    return regresar;
  }
  
  // SERVICIO WEB PARA EL CATALOGO DE PRODUCTOS
  public String venus(Long radio, String densidad, String atmosfera) throws Exception {
    String regresar           = Decoder.toJson(new Respuesta(ERespuesta.CORRECTO.getCodigo(), "Proceso correcto")); 
    Map<String, Object> params= new HashMap<>();
    Respuesta respuesta       = null;
    try {
      respuesta= this.toRespueta(this.neptuno(atmosfera, radio));
      if(Objects.equals(respuesta.getCodigo(), ERespuesta.CORRECTO.getCodigo())) {
        params.put("fecha", densidad);
        ArrayList<Producto> productos =(ArrayList<Producto>)DaoFactory.getInstance().toEntitySet(Producto.class, "VistaPlanetasDto", "venus", params, -1L);
        if(productos!= null && !productos.isEmpty()) 
          regresar= Decoder.toJson(new Respuesta(ERespuesta.CORRECTO.getCodigo(), Decoder.cleanJson(productos)));
        else
          regresar= Decoder.toJson(new Respuesta(ERespuesta.SIN_PRODUCTOS.getCodigo(), ERespuesta.SIN_PRODUCTOS.getDescripcion()));
      } // if
      else
        regresar= Decoder.toJson(respuesta);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      regresar= Decoder.toJson(new Respuesta(ERespuesta.ERROR.getCodigo(), e.getMessage()));
    } // catch
    finally {
      Methods.clean(params);
    } // finally
    return regresar;
  }
  
  // SERVICIO WEB PARA EL CATALOGO DE EMPRESAS
  public String marte(Long radio, String densidad, String atmosfera) throws Exception {
    String regresar           = Decoder.toJson(new Respuesta(ERespuesta.CORRECTO.getCodigo(), "Proceso correcto")); 
    Map<String, Object> params= new HashMap<>();
    Respuesta respuesta       = null;
    try {
      respuesta= this.toRespueta(this.neptuno(atmosfera, radio));
      if(Objects.equals(respuesta.getCodigo(), ERespuesta.CORRECTO.getCodigo())) {
        params.put("fecha", densidad);
        ArrayList<Empresa> empresas= (ArrayList<Empresa>)DaoFactory.getInstance().toEntitySet(Empresa.class, "VistaPlanetasDto", "marte", params, -1L);
        if(empresas!= null && !empresas.isEmpty()) 
          regresar= Decoder.toJson(new Respuesta(ERespuesta.CORRECTO.getCodigo(), Decoder.cleanJson(empresas)));
        else
          regresar= Decoder.toJson(new Respuesta(ERespuesta.SIN_EMPRESAS.getCodigo(), ERespuesta.SIN_EMPRESAS.getDescripcion()));
      } // if
      else
        regresar= Decoder.toJson(respuesta);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      regresar= Decoder.toJson(new Respuesta(ERespuesta.ERROR.getCodigo(), e.getMessage()));
    } // catch
    finally {
      Methods.clean(params);
    } // finally
    return regresar;
  }
  
  // SERVICIO WEB PARA LOS ALMACENCES
  public String jupiter(Long radio, String densidad, String atmosfera) throws Exception {
    String regresar           = Decoder.toJson(new Respuesta(ERespuesta.CORRECTO.getCodigo(), "Proceso correcto")); 
    Map<String, Object> params= new HashMap<>();
    Respuesta respuesta       = null;
    try {
      respuesta= this.toRespueta(this.neptuno(atmosfera, radio));
      if(Objects.equals(respuesta.getCodigo(), ERespuesta.CORRECTO.getCodigo())) {
        params.put("fecha", densidad);
        ArrayList<Almacen> almacenes= (ArrayList<Almacen>)DaoFactory.getInstance().toEntitySet(Almacen.class, "VistaPlanetasDto", "jupiter", params, -1L);
        if(almacenes!= null && !almacenes.isEmpty()) 
          regresar= Decoder.toJson(new Respuesta(ERespuesta.CORRECTO.getCodigo(), Decoder.cleanJson(almacenes)));
        else
          regresar= Decoder.toJson(new Respuesta(ERespuesta.SIN_ALMACENES.getCodigo(), ERespuesta.SIN_ALMACENES.getDescripcion()));
      } // if
      else
        regresar= Decoder.toJson(respuesta);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      regresar= Decoder.toJson(new Respuesta(ERespuesta.ERROR.getCodigo(), e.getMessage()));
    } // catch
    finally {
      Methods.clean(params);
    } // finally
    return regresar;
  }
  
  // SERVICIO WEB PARA LOS UBICACIONES
  public String saturno(Long radio, String densidad, String atmosfera) throws Exception {
    String regresar           = Decoder.toJson(new Respuesta(ERespuesta.CORRECTO.getCodigo(), "Proceso correcto")); 
    Map<String, Object> params= new HashMap<>();
    Respuesta respuesta       = null;
    try {
      respuesta= this.toRespueta(this.neptuno(atmosfera, radio));
      if(Objects.equals(respuesta.getCodigo(), ERespuesta.CORRECTO.getCodigo())) {
        params.put("fecha", densidad);
        ArrayList<Ubicacion> ubicaciones= (ArrayList<Ubicacion>)DaoFactory.getInstance().toEntitySet(Ubicacion.class, "VistaPlanetasDto", "saturno", params, -1L);
        if(ubicaciones!= null && !ubicaciones.isEmpty()) 
          regresar= Decoder.toJson(new Respuesta(ERespuesta.CORRECTO.getCodigo(), Decoder.cleanJson(ubicaciones)));
        else
          regresar= Decoder.toJson(new Respuesta(ERespuesta.SIN_UBICACIONES.getCodigo(), ERespuesta.SIN_UBICACIONES.getDescripcion()));
      } // if
      else
        regresar= Decoder.toJson(respuesta);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      regresar= Decoder.toJson(new Respuesta(ERespuesta.ERROR.getCodigo(), e.getMessage()));
    } // catch
    finally {
      Methods.clean(params);
    } // finally
    return regresar;
  }
  
  // SERVICIO WEB PARA EL CATALOGO DE CONTEOS DIRIGIDOS
  public String polaris(Long radio, String densidad, String atmosfera) throws Exception {
    String regresar           = Decoder.toJson(new Respuesta(ERespuesta.CORRECTO.getCodigo(), "Proceso correcto")); 
    Map<String, Object> params= new HashMap<>();
    Respuesta respuesta       = null;
    try {
      respuesta= this.toRespueta(this.neptuno(atmosfera, radio));
      if(Objects.equals(respuesta.getCodigo(), ERespuesta.CORRECTO.getCodigo())) {
        params.put("fecha", densidad);
        ArrayList<Contador> contadores= (ArrayList<Contador>)DaoFactory.getInstance().toEntitySet(Contador.class, "VistaPlanetasDto", "polaris", params, Constantes.SQL_TODOS_REGISTROS);
        if(contadores!= null && !contadores.isEmpty()) 
          regresar= Decoder.toJson(new Respuesta(ERespuesta.CORRECTO.getCodigo(), Decoder.cleanJson(contadores)));
        else
          regresar= Decoder.toJson(new Respuesta(ERespuesta.SIN_CONTEOS.getCodigo(), ERespuesta.SIN_CONTEOS.getDescripcion()));
      } // if
      else
        regresar= Decoder.toJson(respuesta);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      regresar= Decoder.toJson(new Respuesta(ERespuesta.ERROR.getCodigo(), e.getMessage()));
    } // catch
    finally {
      Methods.clean(params);
    } // finally
    return regresar;
  }
  
  // SERVICIO WEB PARA REGISTRAR UN CONTEO INDIVIDUAL
  public String canopus(Long radio, String densidad, String atmosfera) throws Exception {
    String regresar           = Decoder.toJson(new Respuesta(ERespuesta.CORRECTO.getCodigo(), "Proceso correcto")); 
    Map<String, Object> params= new HashMap<>();
    Respuesta respuesta       = null;
    try {
      respuesta= this.toRespueta(this.neptuno(atmosfera, radio));
      if(Objects.equals(respuesta.getCodigo(), ERespuesta.CORRECTO.getCodigo())) 
        regresar= Decoder.toJson(new Respuesta(ERespuesta.CORRECTO.getCodigo(), this.toConteoDirigido(radio, densidad)));
      else
        regresar= Decoder.toJson(respuesta);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      regresar= Decoder.toJson(new Respuesta(ERespuesta.ERROR.getCodigo(), e.getMessage()));
    } // catch
    finally {
      Methods.clean(params);
    } // finally
    return regresar;
  }
  
  // SERVICIO WEB PARA EL CATALOGO DE TRANSFERENCIAS / SOLICITUDES DIRIGIDAS
  public String antares(Long radio, String densidad, String atmosfera) throws Exception {
    String regresar           = Decoder.toJson(new Respuesta(ERespuesta.CORRECTO.getCodigo(), "Proceso correcto")); 
    Map<String, Object> params= new HashMap<>();
    Respuesta respuesta       = null;
    try {
      respuesta= this.toRespueta(this.neptuno(atmosfera, radio));
      if(Objects.equals(respuesta.getCodigo(), ERespuesta.CORRECTO.getCodigo())) {
        params.put("fecha", densidad);
        ArrayList<Producto> productos =(ArrayList<Producto>)DaoFactory.getInstance().toEntitySet(Producto.class, "VistaPlanetasDto", "venus", params, Constantes.SQL_TODOS_REGISTROS);
        if(productos!= null && !productos.isEmpty()) 
          regresar= Decoder.toJson(new Respuesta(ERespuesta.CORRECTO.getCodigo(), Decoder.cleanJson(productos)));
        else
          regresar= Decoder.toJson(new Respuesta(ERespuesta.SIN_TRANSFERENCIAS.getCodigo(), ERespuesta.SIN_TRANSFERENCIAS.getDescripcion()));
      } // if
      else
        regresar= Decoder.toJson(respuesta);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      regresar= Decoder.toJson(new Respuesta(ERespuesta.ERROR.getCodigo(), e.getMessage()));
    } // catch
    finally {
      Methods.clean(params);
    } // finally
    return regresar;
  }
  
  // SERVICIO WEB PARA REGISTRAR UN CONTEO INDIVIDUAL
  public String urano(Long radio, String densidad, String atmosfera) throws Exception {
    String regresar           = Decoder.toJson(new Respuesta(ERespuesta.CORRECTO.getCodigo(), "Proceso correcto")); 
    Map<String, Object> params= new HashMap<>();
    Respuesta respuesta       = null;
    try {
      respuesta= this.toRespueta(this.neptuno(atmosfera, radio));
      if(Objects.equals(respuesta.getCodigo(), ERespuesta.CORRECTO.getCodigo())) 
        regresar= Decoder.toJson(new Respuesta(ERespuesta.CORRECTO.getCodigo(), this.toAplicarConteo(radio, densidad)));
      else
        regresar= Decoder.toJson(respuesta);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      regresar= Decoder.toJson(new Respuesta(ERespuesta.ERROR.getCodigo(), e.getMessage()));
    } // catch
    finally {
      Methods.clean(params);
    } // finally
    return regresar;
  }
  
  // SERVICIO WEB PARA REGISTRAR UN CONTEO INDIVIDUAL
  public String solar(Long radio, String densidad, String atmosfera) throws Exception {
    String regresar           = Decoder.toJson(new Respuesta(ERespuesta.CORRECTO.getCodigo(), "Proceso correcto")); 
    Map<String, Object> params= new HashMap<>();
    Respuesta respuesta       = null;
    try {
      respuesta= this.toRespueta(this.neptuno(atmosfera, radio));
      if(Objects.equals(respuesta.getCodigo(), ERespuesta.CORRECTO.getCodigo())) 
        regresar= Decoder.toJson(new Respuesta(ERespuesta.CORRECTO.getCodigo(), this.toAplicarItems(radio, densidad)));
      else
        regresar= Decoder.toJson(respuesta);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      regresar= Decoder.toJson(new Respuesta(ERespuesta.ERROR.getCodigo(), e.getMessage()));
    } // catch
    finally {
      Methods.clean(params);
    } // finally
    return regresar;
  }
  
  // SERVICIO WEB PARA VERIFICAR QUE UN USUARIO ESTA ACTIVO
  public String neptuno(String atmosfera, Long radio) throws Exception {
    String regresar           = Decoder.toJson(new Respuesta(ERespuesta.CORRECTO.getCodigo(), "Proceso correcto")); 
    Map<String, Object> params= new HashMap<>();
    try {
      if(Objects.equals(BouncyEncryption.decrypt(TEXT_TOKEN), BouncyEncryption.decrypt(atmosfera))) {
        if(Objects.equals(radio, USER_SUPER) || Objects.equals(radio, USER_ADMIN)) 
          regresar= Decoder.toJson(new Respuesta(ERespuesta.CORRECTO.getCodigo(), ERespuesta.CORRECTO.getDescripcion()));
        else {  
          params.put("idUsuario", radio);
          Entity usuario= (Entity)DaoFactory.getInstance().toEntity("VistaPlanetasDto", "neptuno", params);
          if(usuario!= null && !usuario.isEmpty()) 
            if(Objects.equals(usuario.toLong("activo"), 1L)) 
              regresar= Decoder.toJson(new Respuesta(ERespuesta.CORRECTO.getCodigo(), ERespuesta.CORRECTO.getDescripcion()));
            else
              regresar= Decoder.toJson(new Respuesta(ERespuesta.USUARIO_ERROR.getCodigo(), ERespuesta.USUARIO_ERROR.getDescripcion()));
          else
            regresar= Decoder.toJson(new Respuesta(ERespuesta.SIN_USUARIOS.getCodigo(), ERespuesta.SIN_USUARIOS.getDescripcion()));
        } // if
      } // if
      else
        regresar= Decoder.toJson(new Respuesta(ERespuesta.TOKEN.getCodigo(), ERespuesta.TOKEN.getDescripcion()));
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      regresar= Decoder.toJson(new Respuesta(ERespuesta.ERROR.getCodigo(), e.getMessage()));
    } // catch
    finally {
      Methods.clean(params);
    } // finally
    return regresar;
  }
  
  // SERVICIO WEB PARA LOS INVENTARIOS
  public String pluton(Long radio, String densidad, String atmosfera) throws Exception {
    String regresar           = Decoder.toJson(new Respuesta(ERespuesta.CORRECTO.getCodigo(), "Proceso correcto")); 
    Map<String, Object> params= new HashMap<>();
    Respuesta respuesta       = null;
    try {
      respuesta= this.toRespueta(this.neptuno(atmosfera, radio));
      if(Objects.equals(respuesta.getCodigo(), ERespuesta.CORRECTO.getCodigo())) {
        params.put("fecha", densidad);
        ArrayList<Inventario> inventarios= (ArrayList<Inventario>)DaoFactory.getInstance().toEntitySet(Inventario.class, "VistaPlanetasDto", "pluton", params, Constantes.SQL_TODOS_REGISTROS);
        if(inventarios!= null && !inventarios.isEmpty()) 
          regresar= Decoder.toJson(new Respuesta(ERespuesta.CORRECTO.getCodigo(), Decoder.cleanJson(inventarios)));
        else
          regresar= Decoder.toJson(new Respuesta(ERespuesta.SIN_INVENTARIOS.getCodigo(), ERespuesta.SIN_INVENTARIOS.getDescripcion()));
      } // if
      else
        regresar= Decoder.toJson(respuesta);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      regresar= Decoder.toJson(new Respuesta(ERespuesta.ERROR.getCodigo(), e.getMessage()));
    } // catch
    finally {
      Methods.clean(params);
    } // finally
    return regresar;
  }
  
  // SERVICIO WEB PARA LOS PRODUCTOS / ARTICULOS
  public String sol(Long radio, String densidad, String atmosfera) throws Exception {
    String regresar           = Decoder.toJson(new Respuesta(ERespuesta.CORRECTO.getCodigo(), "Proceso correcto")); 
    Map<String, Object> params= new HashMap<>();
    Respuesta respuesta       = null;
    try {
      respuesta= this.toRespueta(this.neptuno(atmosfera, radio));
      if(Objects.equals(respuesta.getCodigo(), ERespuesta.CORRECTO.getCodigo())) {
        params.put("fecha", densidad);
        ArrayList<Articulo> articulos= (ArrayList<Articulo>)DaoFactory.getInstance().toEntitySet(Articulo.class, "VistaPlanetasDto", "sol", params, Constantes.SQL_TODOS_REGISTROS);
        if(articulos!= null && !articulos.isEmpty()) 
          regresar= Decoder.toJson(new Respuesta(ERespuesta.CORRECTO.getCodigo(), Decoder.cleanJson(articulos)));
        else
          regresar= Decoder.toJson(new Respuesta(ERespuesta.SIN_PRODUCTOS.getCodigo(), ERespuesta.SIN_PRODUCTOS.getDescripcion()));
      } // if
      else
        regresar= Decoder.toJson(respuesta);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      regresar= Decoder.toJson(new Respuesta(ERespuesta.ERROR.getCodigo(), e.getMessage()));
    } // catch
    finally {
      Methods.clean(params);
    } // finally
    return regresar;
  }

  // SERVICIO WEB PARA PARA ENROLAR DISPOSITIVOS
  public String luna(Long radio, String densidad, String atmosfera) throws Exception {
    String regresar           = Decoder.toJson(new Respuesta(ERespuesta.CORRECTO.getCodigo(), "Proceso correcto")); 
    Map<String, Object> params= new HashMap<>();
    Respuesta respuesta       = null;
    try {
      respuesta= this.toRespueta(this.neptuno(atmosfera, radio));
      if(Objects.equals(respuesta.getCodigo(), ERespuesta.CORRECTO.getCodigo())) {
        regresar= Decoder.toJson(new Respuesta(ERespuesta.CORRECTO.getCodigo(), this.toEnrolar(radio, densidad)));
      } // if  
      else
        regresar= Decoder.toJson(respuesta);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      regresar= Decoder.toJson(new Respuesta(ERespuesta.ENROLAMIENTO_ERROR.getCodigo(), e.getMessage()));
    } // catch
    finally {
      Methods.clean(params);
    } // finally
    return regresar;
  }

  // SERVICIO WEB PARA VERIFICAR LA VIGENCIA DE UNA APLICACIÓN MOVIL
  public String sistema(String atmosfera, String radio, String densidad) throws Exception {
    String regresar           = Decoder.toJson(new Respuesta(ERespuesta.CORRECTO.getCodigo(), "Proceso correcto")); 
    Map<String, Object> params= new HashMap<>();
    try {
      if(Objects.equals(BouncyEncryption.decrypt(TEXT_TOKEN), BouncyEncryption.decrypt(atmosfera))) {
        params.put("nombre", BouncyEncryption.decrypt(radio));
        params.put("version", densidad);
        Entity vigencia= (Entity)DaoFactory.getInstance().toEntity("TcManticVersionesDto", "row", params);
        if(vigencia!= null && !vigencia.isEmpty()) 
          regresar= Decoder.toJson(new Respuesta(ERespuesta.CORRECTO.getCodigo(), vigencia.toString("vigencia")));
        else
          regresar= Decoder.toJson(new Respuesta(ERespuesta.APLICACION_ERROR.getCodigo(), ERespuesta.APLICACION_ERROR.getDescripcion()));
      } // if
      else
        regresar= Decoder.toJson(new Respuesta(ERespuesta.TOKEN.getCodigo(), ERespuesta.TOKEN.getDescripcion()));
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      regresar= Decoder.toJson(new Respuesta(ERespuesta.APLICACION_ERROR.getCodigo(), e.getMessage()));
    } // catch
    finally {
      Methods.clean(params);
    } // finally
    return regresar;
  }

  // SERVICIO WEB PARA VERIFICAR SI UN DISPOSITOVO ESTA ACTIVO
  public String galaxia(String atmosfera, String radio, String densidad) throws Exception {
    String regresar           = Decoder.toJson(new Respuesta(ERespuesta.CORRECTO.getCodigo(), "Proceso correcto")); 
    Map<String, Object> params= new HashMap<>();
    try {
      if(Objects.equals(BouncyEncryption.decrypt(TEXT_TOKEN), BouncyEncryption.decrypt(atmosfera))) {
        params.put("imei", radio);
        params.put("semilla", densidad);
        Entity vigencia= (Entity)DaoFactory.getInstance().toEntity("TcManticDispositivosDto", "row", params);
        if(vigencia!= null && !vigencia.isEmpty()) 
          if(Objects.equals(vigencia.toLong("idActivo"), 1L))
            regresar= Decoder.toJson(new Respuesta(ERespuesta.CORRECTO.getCodigo(), ERespuesta.CORRECTO.getDescripcion()));
          else 
            regresar= Decoder.toJson(new Respuesta(ERespuesta.NO_ACTIVO.getCodigo(), ERespuesta.NO_ACTIVO.getDescripcion()));
        else
          regresar= Decoder.toJson(new Respuesta(ERespuesta.DISPOSITIVO_ERROR.getCodigo(), ERespuesta.DISPOSITIVO_ERROR.getDescripcion()));
      } // if
      else
        regresar= Decoder.toJson(new Respuesta(ERespuesta.TOKEN.getCodigo(), ERespuesta.TOKEN.getDescripcion()));
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      regresar= Decoder.toJson(new Respuesta(ERespuesta.DISPOSITIVO_ERROR.getCodigo(), e.getMessage()));
    } // catch
    finally {
      Methods.clean(params);
    } // finally
    return regresar;
  }
  
  // SERVICIO WEB PARA REGISTRAR UNA TRANSFERENCIA REMOTA ENTRE ALMACENES
  public String eclipse(Long radio, String densidad, String atmosfera) throws Exception {
    String regresar           = Decoder.toJson(new Respuesta(ERespuesta.CORRECTO.getCodigo(), "Proceso correcto")); 
    Map<String, Object> params= new HashMap<>();
    Respuesta respuesta       = null;
    try {
      respuesta= this.toRespueta(this.neptuno(atmosfera, radio));
      if(Objects.equals(respuesta.getCodigo(), ERespuesta.CORRECTO.getCodigo())) 
        regresar= Decoder.toJson(new Respuesta(ERespuesta.CORRECTO.getCodigo(), this.toAplicarTransferencia(radio, densidad)));
      else
        regresar= Decoder.toJson(respuesta);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      regresar= Decoder.toJson(new Respuesta(ERespuesta.ERROR.getCodigo(), e.getMessage()));
    } // catch
    finally {
      Methods.clean(params);
    } // finally
    return regresar;
  }
  
  private String toEnrolar(Long idUsuario, String densidad) throws Exception {
    String id                 = Cadena.rellenar(String.valueOf(idUsuario), 3, '0', true);
    String regresar           = id.concat(Fecha.toRegistro());
    Map<String, Object> params= new HashMap<>();
    Transaction transaction   = null;
    Session session           = null;
		try {
      LOG.error("ENROLAR: ["+ densidad+ "]");
			session    = SessionFactoryFacade.getInstance().getSession(-1L);
			transaction= session.beginTransaction();
			session.clear();
      TcManticDispositivosDto item= this.toDispositivo(densidad);
      if(item!= null) {
        item.setNombre(BouncyEncryption.decrypt(item.getNombre())); 
        item.setCuenta(BouncyEncryption.decrypt(item.getCuenta())); 
        Entity entity= (Entity)DaoFactory.getInstance().toEntity("TcManticVersionesDto", "identically", item.toMap());
        if(entity!= null && !entity.isEmpty()) {
          item.setIdVersion(entity.toLong("idVersion"));
        } // if
        item.setOriginal(densidad);
        item.setIdActivo(1L);
        DaoFactory.getInstance().insert(session, item);
      } // if
			transaction.commit();
		} // try
		catch (Exception e) {
      LOG.error("SOLICITUD: "+ densidad);
      Error.mensaje(e);
			if (transaction!= null) {
				transaction.rollback();
			} // if
			throw e;
		} // catch
		finally {
      Methods.clean(params);
			if (session!= null) 
				session.close();
			transaction= null;
			session    = null;
		} // finally    
    return regresar;
  }
  
  private String toAplicarConteo(Long idUsuario, String densidad) throws Exception {
    String id                 = Cadena.rellenar(String.valueOf(idUsuario), 3, '0', true);
    String regresar           = id.concat(Fecha.toRegistro());
    Map<String, Object> params= new HashMap<>();
    Transaction transaction   = null;
    Session session           = null;
		try {
      LOG.error("CONTEO: ["+ densidad+ "]");
			session    = SessionFactoryFacade.getInstance().getSession(-1L);
			transaction= session.beginTransaction();
			session.clear();
      Conteo items= this.toConteo(densidad);
      TcManticConteosDto conteo= new TcManticConteosDto(
        densidad, // String conteos, 
        items.getRegistro(), // String fecha, 
        new Long(items.getProductos().size()), // Long articulos, 
        items.getIdUsuario(), // Long idUsuario, 
        -1L, // Long idConteo, 
        null, // Timestamp procesado, 
        items.getIdConteo(), // Long idReferencia, 
        BouncyEncryption.decrypt(items.getNombre()), // String nombre, 
        1L, // Long idConteoEstatus, 
        regresar, // String token
        items.getIdEmpresa(), // Long idEmpresa
        items.getIdAlmacen(), // Long idAlmacen
        items.getSemilla(), // String semilla
        items.getVersion() // String version
      );
      params.put("semilla", conteo.getSemilla());
      params.put("idUsuario", conteo.getIdUsuario());
      params.put("idReferencia", conteo.getIdReferencia());
      params.put("nombre", conteo.getNombre());
      TcManticConteosDto existe= (TcManticConteosDto)DaoFactory.getInstance().toEntity(session, TcManticConteosDto.class, "TcManticConteosDto", "identically", params);
      if(Objects.equals(existe, null)) {
        // INSERTAR EL REGISTRO DE LOS CONTEOS PARA DESPUES VERFICAR SI SE INTEGRARON
        DaoFactory.getInstance().insert(session, conteo);
        TcManticConteosBitacoraDto bitacora= new TcManticConteosBitacoraDto(
          null, // String justificacion, 
          conteo.getIdUsuario(), // Long idUsuario, 
          conteo.getIdConteo(), // Long idConteo, 
          -1L, // Long idConteoBitacora, 
          conteo.getIdConteoEstatus() // Long idConteoEstatus
        );
        DaoFactory.getInstance().insert(session, bitacora);
      } // if
      else {
        regresar= existe.getToken();
        if(existe.getArticulos()!= items.getProductos().size()) {
          existe.setConteos(densidad);
          existe.setArticulos(new Long(items.getProductos().size()));
          DaoFactory.getInstance().update(session, existe);
        } // if
        conteo.setIdConteo(existe.getIdConteo());
        TcManticConteosBitacoraDto bitacora= new TcManticConteosBitacoraDto(
          "SE ENVIO UNA ACTUALIZACIÓN", // String justificacion, 
          conteo.getIdUsuario(), // Long idUsuario, 
          existe.getIdConteo(), // Long idConteo, 
          -1L, // Long idConteoBitacora, 
          existe.getIdConteoEstatus() // Long idConteoEstatus
        );
        DaoFactory.getInstance().insert(session, bitacora);
      } // if
      for (Cantidad item: items.getProductos()) {
        TcManticConteosDetallesDto detalle= new TcManticConteosDetallesDto(
          item.getRegistro(), // String fecha, 
          -1L, // Long idConteoDetalle, 
          conteo.getIdConteo(), // Long idConteo, 
          null, // Timestamp procesado, 
          item.getCantidad(), // Double cantidad, 
          item.getIdProducto(), // Long idArticulo, 
          item.getDescripcion(), // String nombre
          item.getCodigo() // String codigo
        );
        params.put("idConteo", conteo.getIdConteo());
        params.put("idArticulo", item.getIdProducto());
        TcManticConteosDetallesDto copia= (TcManticConteosDetallesDto)DaoFactory.getInstance().toEntity(session, TcManticConteosDetallesDto.class, "TcManticConteosDetallesDto", "identically", params);
        if(Objects.equals(copia, null)) 
          DaoFactory.getInstance().insert(session, detalle);
        else
          if(Objects.equals(copia.getProcesado(), null)) {
            copia.setCantidad(item.getCantidad());
            copia.setFecha(item.getRegistro());
            copia.setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
            DaoFactory.getInstance().update(session, copia);
          } // if  
      } // for
			transaction.commit();
		} // try
		catch (Exception e) {
      LOG.error("SOLICITUD: "+ densidad);
      Error.mensaje(e);
			if (transaction!= null) {
				transaction.rollback();
			} // if
			throw e;
		} // catch
		finally {
      Methods.clean(params);
			if (session!= null) 
				session.close();
			transaction= null;
			session    = null;
		} // finally    
    return regresar;
  }

  private String toConteoDirigido(Long idUsuario, String densidad) throws Exception {
    String id                 = Cadena.rellenar(String.valueOf(idUsuario), 3, '0', true);
    String regresar           = id.concat(Fecha.toRegistro());
    Map<String, Object> params= new HashMap<>();
    Transaction transaction   = null;
    Session session           = null;
		try {
      LOG.error("CONTEO: ["+ densidad+ "]");
			session    = SessionFactoryFacade.getInstance().getSession(-1L);
			transaction= session.beginTransaction();
			session.clear();
      Contador items= this.toContador(densidad);
      TcManticContadoresDto existe= (TcManticContadoresDto)DaoFactory.getInstance().findById(session, TcManticContadoresDto.class, items.getIdConteo());
      // SOLO SE ACTUALIZA SI EL ESTATUS ES DE CERRADO PARA CAMBIAR A INTEGRANDO
      if(!Objects.equals(existe, null) && Objects.equals(existe.getIdContadorEstatus(), 2L)) { // CERRADO
        existe.setConteos(densidad);
        existe.setSemilla(items.getSemilla());
        existe.setFecha(items.getRegistro());
        existe.setArticulos(new Long(items.getProductos().size()));
        existe.setToken(regresar);
        existe.setVersion(items.getVersion());
        existe.setIdReferencia(items.getIdConteo());
        existe.setIdContadorEstatus(3L); // INTEGRANDO
        DaoFactory.getInstance().update(session, existe);
        TcManticContadoresBitacoraDto bitacora= new TcManticContadoresBitacoraDto(
          "SE ENVIO UNA ACTUALIZACIÓN", // String justificacion, 
          existe.getIdUsuario(), // Long idUsuario, 
          existe.getIdContador(), // Long idContador, 
          -1L, // Long idContadorBitacora, 
          existe.getIdContadorEstatus() // Long idConteoEstatus
        );
        DaoFactory.getInstance().insert(session, bitacora);
        for (Cantidad item: items.getProductos()) {
          params.put("idContador", existe.getIdContador());
          params.put("idArticulo", item.getIdProducto());
          TcManticContadoresDetallesDto copia= (TcManticContadoresDetallesDto)DaoFactory.getInstance().toEntity(session, TcManticContadoresDetallesDto.class, "TcManticContadoresDetallesDto", "identically", params);
          if(!Objects.equals(copia, null) && Objects.equals(copia.getProcesado(), null)) { 
            copia.setCantidad(item.getCantidad());
            copia.setFecha(item.getRegistro());
            DaoFactory.getInstance().update(session, copia);
          } // if  
        } // for
        this.notificar(session, existe);
      } // if
			transaction.commit();
		} // try
		catch (Exception e) {
      LOG.error("SOLICITUD: "+ densidad);
      Error.mensaje(e);
			if (transaction!= null) 
				transaction.rollback();
			throw e;
		} // catch
		finally {
      Methods.clean(params);
			if (session!= null) 
				session.close();
			transaction= null;
			session    = null;
		} // finally    
    return regresar;
  }

  private String toAplicarItems(Long idUsuario, String densidad) throws Exception {
    String id                 = Cadena.rellenar(String.valueOf(idUsuario), 3, '0', true);
    String regresar           = id.concat(Fecha.toRegistro());
    Map<String, Object> params= new HashMap<>();
    Transaction transaction   = null;
    Session session           = null;
		try {
      LOG.error("CONTEO: ["+ densidad+ "]");
			session    = SessionFactoryFacade.getInstance().getSession(-1L);
			transaction= session.beginTransaction();
			session.clear();
      Items items= this.toItems(densidad);
      TcManticConteosDto conteo= new TcManticConteosDto(
        densidad, // String conteos, 
        items.getRegistro(), // String fecha, 
        new Long(items.getProductos().size()), // Long articulos, 
        items.getIdUsuario(), // Long idUsuario, 
        -1L, // Long idConteo, 
        null, // Timestamp procesado, 
        items.getIdConteo(), // Long idReferencia, 
        BouncyEncryption.decrypt(items.getNombre()), // String nombre, 
        1L, // Long idConteoEstatus, 
        regresar, // String token
        items.getIdEmpresa(), // Long idEmpresa
        items.getIdAlmacen(), // Long idAlmacen
        items.getSemilla(), // String semilla
        items.getVersion() // String version
      );
      params.put("semilla", conteo.getSemilla());
      params.put("idUsuario", conteo.getIdUsuario());
      params.put("idReferencia", conteo.getIdReferencia());
      params.put("nombre", conteo.getNombre());
      TcManticConteosDto existe= (TcManticConteosDto)DaoFactory.getInstance().toEntity(TcManticConteosDto.class, "TcManticConteosDto", "identically", params);
      if(Objects.equals(existe, null)) {
        // INSERTAR EL REGISTRO DE LOS CONTEOS PARA DESPUES VERFICAR SI SE INTEGRARON
        DaoFactory.getInstance().insert(session, conteo);
        TcManticConteosBitacoraDto bitacora= new TcManticConteosBitacoraDto(
          null, // String justificacion, 
          conteo.getIdUsuario(), // Long idUsuario, 
          conteo.getIdConteo(), // Long idConteo, 
          -1L, // Long idConteoBitacora, 
          conteo.getIdConteoEstatus() // Long idConteoEstatus
        );
        DaoFactory.getInstance().insert(session, bitacora);
      } // if
      else {
        regresar= existe.getToken();
        if(existe.getArticulos()!= items.getProductos().size()) {
          existe.setConteos(densidad);
          existe.setArticulos(new Long(items.getProductos().size()));
          DaoFactory.getInstance().update(session, existe);
        } // if
        conteo.setIdConteo(existe.getIdConteo());
        TcManticConteosBitacoraDto bitacora= new TcManticConteosBitacoraDto(
          "SE ENVIO UNA ACTUALIZACIÓN", // String justificacion, 
          conteo.getIdUsuario(), // Long idUsuario, 
          existe.getIdConteo(), // Long idConteo, 
          -1L, // Long idConteoBitacora, 
          existe.getIdConteoEstatus() // Long idConteoEstatus
        );
        DaoFactory.getInstance().insert(session, bitacora);
      } // if
      for (Item item: items.getProductos()) {
        TcManticConteosDetallesDto detalle= new TcManticConteosDetallesDto(
          item.getE(), // String fecha, 
          -1L, // Long idConteoDetalle, 
          conteo.getIdConteo(), // Long idConteo, 
          null, // Timestamp procesado, 
          item.getB(), // Double cantidad, 
          item.getA(), // Long idArticulo, 
          item.getD(), // String nombre
          item.getC() // String codigo
        );
        params.put("idConteo", conteo.getIdConteo());
        params.put("idArticulo", item.getA());
        TcManticConteosDetallesDto copia= (TcManticConteosDetallesDto)DaoFactory.getInstance().toEntity(TcManticConteosDetallesDto.class, "TcManticConteosDetallesDto", "identically", params);
        if(Objects.equals(copia, null)) 
          DaoFactory.getInstance().insert(session, detalle);
        else
          if(Objects.equals(copia.getProcesado(), null)) {
            copia.setCantidad(item.getB());
            copia.setFecha(item.getE());
            copia.setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
            DaoFactory.getInstance().update(session, copia);
          } // if  
      } // for
			transaction.commit();
		} // try
		catch (Exception e) {
      LOG.error("SOLICITUD: "+ densidad);
      Error.mensaje(e);
			if (transaction!= null) {
				transaction.rollback();
			} // if
			throw e;
		} // catch
		finally {
      Methods.clean(params);
			if (session!= null) 
				session.close();
			transaction= null;
			session    = null;
		} // finally    
    return regresar;
  }

  private String toAplicarTransferencia(Long idUsuario, String densidad) throws Exception {
    String id                 = Cadena.rellenar(String.valueOf(idUsuario), 3, '0', true);
    String regresar           = id.concat(Fecha.toRegistro());
    Map<String, Object> params= new HashMap<>();
    Transaction transaction   = null;
    Session session           = null;
    TcManticTransferenciasDto transferencia= null;
		try {
      LOG.error("TRANSFERENCIA: ["+ densidad+ "]");
			session    = SessionFactoryFacade.getInstance().getSession(-1L);
			transaction= session.beginTransaction();
			session.clear();
      Transferencia items= this.toTransferencia(densidad);
      Siguiente siguiente= this.toSiguiente(session, items.getIdEmpresa());
      String idRemoto    = items.getSemilla().concat(Constantes.SEPARADOR)+ items.getIdConteo()+ Constantes.SEPARADOR+ items.getIdUsuario()+ Constantes.SEPARADOR+ items.getVersion();
      params.put("idRemoto", idRemoto);
      transferencia= (TcManticTransferenciasDto)DaoFactory.getInstance().toEntity(TcManticTransferenciasDto.class, "TcManticTransferenciasDto", "remoto", params);
      if(Objects.equals(transferencia, null)) {
        // INSERTAR EL REGISTRO DE LAS TRANSFERENCIAS PARA DESPUES VERFICAR SI SE INTEGRARON
        transferencia= new TcManticTransferenciasDto(
          null, // Long idSolicito, 
          5L, // Long idTransferenciaEstatus, 
          3L, // Long idTransferenciaTipo, 
          siguiente.getEjercicio(), //  Long ejercicio, 
          siguiente.getConsecutivo(), // String consecutivo, 
          items.getIdUsuario(), // Long idUsuario, 
          items.getIdFuente(), //  Long idAlmacen, 
          BouncyEncryption.decrypt(items.getNombre()), // String observaciones, 
          items.getIdAlmacen(), // Long idDestino, 
          items.getIdEmpresa(), // Long idEmpresa, 
          siguiente.getOrden(), // Long orden, 
          -1L, // Long idTransferencia, 
          idRemoto, // String idRemoto
          null // Timestamp procesado
        );
        
        DaoFactory.getInstance().insert(session, transferencia);
        TcManticTransferenciasBitacoraDto bitacora= new TcManticTransferenciasBitacoraDto(
          -1L, // Long idTransferenciaBitacora, 
          null, // String justificacion, 
          items.getIdUsuario(), // Long idUsuario, 
          null, // Long idTransporto, 
          transferencia.getIdTransferenciaEstatus(), // Long idTransferenciaEstatus, 
          transferencia.getIdTransferencia() // Long idTransferencia
        );
        DaoFactory.getInstance().insert(session, bitacora);
      } // if
      else {
        TcManticTransferenciasBitacoraDto bitacora= new TcManticTransferenciasBitacoraDto(
          -1L, // Long idTransferenciaBitacora, 
          "SE ENVIO UNA ACTUALIZACIÓN", // String justificacion, 
          items.getIdUsuario(), // Long idUsuario, 
          null, // Long idTransporto, 
          transferencia.getIdTransferenciaEstatus(), // Long idTransferenciaEstatus, 
          transferencia.getIdTransferencia() // Long idTransferencia
        );
        DaoFactory.getInstance().insert(session, bitacora);
      } // if
      for (Item item: items.getProductos()) {
        LOG.error("DETALLE: ["+ item+ "]");
        params.put("idArticulo", item.getA());
        Entity articulo= (Entity)DaoFactory.getInstance().toEntity("TcManticArticulosDto", "remoto", params);
        params.put("idTransferencia", transferencia.getIdTransferencia());
        params.put("idArticulo", item.getA());
        TcManticTransferenciasDetallesDto copia= (TcManticTransferenciasDetallesDto)DaoFactory.getInstance().toEntity(TcManticTransferenciasDetallesDto.class, "TcManticTransferenciasDetallesDto", "remoto", params);
        if(Objects.equals(copia, null)) {
          TcManticTransferenciasDetallesDto detalle= new TcManticTransferenciasDetallesDto(
            articulo.toString("codigo"), // String codigo, 
            0D, // Double cantidades, 
            -1L, // Long idTransferenciaDetalle, 
            item.getB(), // Double cantidad, 
            item.getA(), // Long idArticulo, 
            transferencia.getIdTransferencia(), // Long idTransferencia, 
            articulo.toString("nombre"), // String nombre, 
            1L, // Long caja
            null // Timestamp procesado      
          );
          DaoFactory.getInstance().insert(session, detalle);
        } // if
        else
          if(Objects.equals(copia.getProcesado(), null)) {
            copia.setCantidad(item.getB());
            copia.setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
            DaoFactory.getInstance().update(session, copia);
          } // if  
      } // for
			transaction.commit();
		} // try
		catch (Exception e) {
      LOG.error("TRANSFERENCIA: "+ densidad);
      Error.mensaje(e);
			if (transaction!= null) {
				transaction.rollback();
			} // if
			throw e;
		} // catch
		finally {
      Methods.clean(params);
			if (session!= null) 
				session.close();
			transaction= null;
			session    = null;
		} // finally    
    return regresar;
  }
  
  private Respuesta toRespueta(String msg) {
    Gson gson = new Gson();
    return gson.fromJson(msg, Respuesta.class);
  }
  
  private Conteo toConteo(String msg) {
    Gson gson = new Gson();
    return gson.fromJson(msg, Conteo.class);
  }
  
  private Contador toContador(String msg) {
    Gson gson = new Gson();
    return gson.fromJson(msg, Contador.class);
  }
  
  private Transferencia toTransferencia(String msg) {
    Gson gson = new Gson();
    return gson.fromJson(msg, Transferencia.class);
  }
  
  private Items toItems(String msg) {
    Gson gson = new Gson();
    return gson.fromJson(msg, Items.class);
  }
  
  private TcManticDispositivosDto toDispositivo(String msg) {
    Gson gson = new Gson();
    return gson.fromJson(msg, TcManticDispositivosDto.class);
  }

	private Siguiente toSiguiente(Session sesion, Long idEmpresa) throws Exception {
		Siguiente regresar        = null;
		Map<String, Object> params= new HashMap<>();
		try {
			params.put("ejercicio", this.getCurrentYear());
			params.put("idEmpresa", idEmpresa);
			params.put("operador", this.getCurrentSign());
			Value next= DaoFactory.getInstance().toField(sesion, "TcManticTransferenciasDto", "siguiente", params, "siguiente");
			if(next.getData()!= null)
			  regresar= new Siguiente(next.toLong());
		  else
			  regresar= new Siguiente(Configuracion.getInstance().isEtapaDesarrollo()? 900001L: 1L);
		} // try
		catch (Exception e) {
			throw e;
		} // catch
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	}

  protected int getCurrentYear() {
		return Fecha.getAnioActual();
	}	
	
  protected String getCurrentSign() {
		return Configuracion.getInstance().isEtapaDesarrollo()? ">": "<=";
	}	

  // SERVICIO WEB PARA DESCARGAR LOS FALTANTES DE ALMACEN
  public String asteroide(Long radio, String densidad, String atmosfera, Long nucleo) throws Exception {
    String regresar           = Decoder.toJson(new Respuesta(ERespuesta.CORRECTO.getCodigo(), "Proceso correcto")); 
    Map<String, Object> params= new HashMap<>();
    Respuesta respuesta       = null;
    try {
      respuesta= this.toRespueta(this.neptuno(atmosfera, radio));
      if(Objects.equals(respuesta.getCodigo(), ERespuesta.CORRECTO.getCodigo())) {
        params.put("sucursales", densidad); // idEmpresa
        params.put("idAlmacen", nucleo); // idAlmacen
        ArrayList<Partida> productos= (ArrayList<Partida>)DaoFactory.getInstance().toEntitySet(Partida.class, "VistaPlanetasDto", "asteroide", params, -1L);
        if(productos!= null && !productos.isEmpty()) 
          regresar= Decoder.toJson(new Respuesta(ERespuesta.CORRECTO.getCodigo(), Decoder.clean(productos)));
        else
          regresar= Decoder.toJson(new Respuesta(ERespuesta.SIN_PRODUCTOS.getCodigo(), ERespuesta.SIN_PRODUCTOS.getDescripcion()));
      } // if
      else
        regresar= Decoder.toJson(respuesta);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      regresar= Decoder.toJson(new Respuesta(ERespuesta.ERROR.getCodigo(), e.getMessage()));
    } // catch
    finally {
      Methods.clean(params);
    } // finally
    return regresar;
  }
  
  // SERVICIO WEB PARA DESCARGAR LA VENTAS PERDIDAS
  public String cometa(Long radio, String densidad, String atmosfera) throws Exception {
    String regresar           = Decoder.toJson(new Respuesta(ERespuesta.CORRECTO.getCodigo(), "Proceso correcto")); 
    Map<String, Object> params= new HashMap<>();
    Respuesta respuesta       = null;
    try {
      respuesta= this.toRespueta(this.neptuno(atmosfera, radio));
      if(Objects.equals(respuesta.getCodigo(), ERespuesta.CORRECTO.getCodigo())) {
  			params.put("sucursales", densidad); // idEmpresa
        ArrayList<Partida> productos =(ArrayList<Partida>)DaoFactory.getInstance().toEntitySet(Partida.class, "VistaPlanetasDto", "cometa", params, -1L);
        if(productos!= null && !productos.isEmpty()) 
          regresar= Decoder.toJson(new Respuesta(ERespuesta.CORRECTO.getCodigo(), Decoder.clean(productos)));
        else
          regresar= Decoder.toJson(new Respuesta(ERespuesta.SIN_PRODUCTOS.getCodigo(), ERespuesta.SIN_PRODUCTOS.getDescripcion()));
      } // if
      else
        regresar= Decoder.toJson(respuesta);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      regresar= Decoder.toJson(new Respuesta(ERespuesta.ERROR.getCodigo(), e.getMessage()));
    } // catch
    finally {
      Methods.clean(params);
    } // finally
    return regresar;
  }

  private void notificar(Session session, TcManticContadoresDto conteo) {
    Bonanza bonanza           = null;
    List<Entity> celulares    = null;
    Map<String, Object> params= new HashMap<>();
    try {      
      if(Objects.equals(conteo.getIdContadorEstatus(), 2L)) {
        params.put("idUsuario", conteo.getIdUsuario());
        Entity fuente= (Entity)DaoFactory.getInstance().toEntity(session, "TcJanalUsuariosDto", "usuario", params);
        params.put("idUsuario", conteo.getIdTrabaja());
        Entity destino= (Entity)DaoFactory.getInstance().toEntity(session, "TcJanalUsuariosDto", "usuario", params);
        if(!Objects.equals(fuente, null) && !fuente.isEmpty() && !Objects.equals(destino, null) && !destino.isEmpty()) {
          params.put("idPersona", fuente.toLong("idPersona"));
          celulares= (List<Entity>)DaoFactory.getInstance().toEntitySet("TrManticPersonaTipoContactoDto", "celular", params);
          if(!Objects.equals(celulares, null) && !celulares.isEmpty()) {
            bonanza= new Bonanza(Cadena.letraCapital(fuente.toString("nombre")), "celular", String.valueOf(conteo.getArticulos()), conteo.getConsecutivo(), conteo.getNombre());
            for(Entity item: celulares) {
              bonanza.setCelular(item.toString("valor"));
              bonanza.doSendConteo(session, Cadena.letraCapital(destino.toString("nombre")));
            } // for
          } // if  
        } // if  
      } // if
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch	
    finally {
      Methods.clean(params);
    } // finally
  }
  
}
