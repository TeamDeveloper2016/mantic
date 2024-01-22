package mx.org.kaana.mantic.ws.publicar;

import com.google.gson.Gson;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.libs.formato.BouncyEncryption;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.json.Decoder;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.enums.ERespuesta;
import mx.org.kaana.mantic.ws.imox.beans.Almacen;
import mx.org.kaana.mantic.ws.imox.beans.Empresa;
import mx.org.kaana.mantic.ws.imox.beans.Producto;
import mx.org.kaana.mantic.ws.imox.beans.Ubicacion;
import mx.org.kaana.mantic.ws.imox.beans.Usuario;
import mx.org.kaana.mantic.ws.publicar.beans.Respuesta;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 16/01/2024
 * @time 04:53:38 PM 
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Planetas implements Serializable {

  private static final long serialVersionUID = 8944954886601403304L;
  private static final String TEXT_TOKEN= "/Eb1AjylyNNfQBLodn6Jf6Stb8NM7Hw2";
  private static final Long USER_TOKEN  = 1976L;
  

  // SERVICIO WEB PARA LOS USUARIOS
  public String mercurio(Long radio, String densidad, String atmosfera) throws Exception {
    String regresar           = Decoder.toJson(new Respuesta(ERespuesta.CORRECTO.getCodigo(), "Proceso correcto")); 
    Map<String, Object> params= new HashMap<>();
    Respuesta respuesta       = null;
    try {
      respuesta= this.toParser(this.neptuno(atmosfera, radio));
      if(Objects.equals(respuesta.getCodigo(), ERespuesta.CORRECTO.getCodigo())) {
        params.put("fecha", densidad);
        ArrayList<Usuario> usuarios= (ArrayList<Usuario>)DaoFactory.getInstance().toEntitySet(Usuario.class, "VistaPlanetasDto", "mercurio", params, -1L);
        if(!Objects.equals(usuarios, null) && !usuarios.isEmpty()) {
          for (Usuario item: usuarios) {
            item.setContrasenia(BouncyEncryption.decrypt(item.getContrasenia()));
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
      respuesta= this.toParser(this.neptuno(atmosfera, radio));
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
      respuesta= this.toParser(this.neptuno(atmosfera, radio));
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
      respuesta= this.toParser(this.neptuno(atmosfera, radio));
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
      respuesta= this.toParser(this.neptuno(atmosfera, radio));
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
      regresar= Decoder.toJson(new Respuesta(ERespuesta.ERROR.getCodigo(), e.getMessage()));
    } // catch
    finally {
      Methods.clean(params);
    } // finally
    return regresar;
  }
  
  // SERVICIO WEB PARA REGISTRAR UN CONTEO INDIVIDUAL
  public String urano(Long radio, String distancia, String atmosfera) throws Exception {
    String regresar           = Decoder.toJson(new Respuesta(ERespuesta.CORRECTO.getCodigo(), "Proceso correcto")); 
    Map<String, Object> params= new HashMap<>();
    Respuesta respuesta       = null;
    try {
      respuesta= this.toParser(this.neptuno(atmosfera, radio));
      if(Objects.equals(respuesta.getCodigo(), ERespuesta.CORRECTO.getCodigo())) 
        regresar= Decoder.toJson(new Respuesta(ERespuesta.CORRECTO.getCodigo(), this.toAplicarConteo(radio, distancia)));
      else
        regresar= Decoder.toJson(respuesta);
    } // try
    catch (Exception e) {
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
        if(Objects.equals(radio, USER_TOKEN)) 
          regresar= Decoder.toJson(new Respuesta(ERespuesta.CORRECTO.getCodigo(), ERespuesta.CORRECTO.getDescripcion()));
        else {  
          params.put("idUsuario", radio);
          Entity usuario= (Entity)DaoFactory.getInstance().toEntity("VistaPlanetasDto", "neptuno", params);
          if(usuario!= null && !usuario.isEmpty()) 
            if(Objects.equals(radio, USER_TOKEN) || Objects.equals(usuario.toLong("activo"), 1L)) 
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
      regresar= Decoder.toJson(new Respuesta(ERespuesta.ERROR.getCodigo(), e.getMessage()));
    } // catch
    finally {
      Methods.clean(params);
    } // finally
    return regresar;
  }
  
  private String toAplicarConteo(Long idUsuario, String conteos) {
    String id= Cadena.rellenar(String.valueOf(idUsuario), 3, '0', true);
    String regresar= id.concat(Fecha.toRegistro());
    try {      
      // Entity entity = DaoFactory.getInstance().toEntity("", "", params);
      // INSERTAR EL REGISTRO DE LOS CONTEOS PARA DESPUES VERFICAR SI SE INTEGRARON
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch	
    return regresar;
  }

  private Respuesta toParser(String msg) {
    Gson gson = new Gson();
    return gson.fromJson(msg, Respuesta.class);
  }
  
  public static void main(String ... args) throws Exception {
    Planetas planetas= new Planetas();
    //System.out.println(planetas.mercurio(1L, "19000101", "/Eb1AjylyNNfQBLodn6Jf6Stb8NM7Hw2"));
    //System.out.println(planetas.marte(1L, "19000101", "/Eb1AjylyNNfQBLodn6Jf6Stb8NM7Hw2"));
    //System.out.println(planetas.jupiter(1L, "19000101", "/Eb1AjylyNNfQBLodn6Jf6Stb8NM7Hw2"));
    //System.out.println(planetas.saturno(1L, "19000101", "/Eb1AjylyNNfQBLodn6Jf6Stb8NM7Hw2"));
    //System.out.println(planetas.urano(1L, "Hola", "/Eb1AjylyNNfQBLodn6Jf6Stb8NM7Hw2"));
    //System.out.println(planetas.venus(1L, "19000101", "/Eb1AjylyNNfQBLodn6Jf6Stb8NM7Hw2"));
    System.out.println(planetas.neptuno("/Eb1AjylyNNfQBLodn6Jf6Stb8NM7Hw2", 1L));
  }
  
}
