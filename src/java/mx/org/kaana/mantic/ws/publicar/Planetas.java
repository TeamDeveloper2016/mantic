package mx.org.kaana.mantic.ws.publicar;

import com.google.gson.Gson;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.hibernate.SessionFactoryFacade;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.libs.formato.BouncyEncryption;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.json.Decoder;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.db.dto.TcManticConteosDetallesDto;
import mx.org.kaana.mantic.db.dto.TcManticConteosBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticConteosDto;
import mx.org.kaana.mantic.enums.ERespuesta;
import mx.org.kaana.mantic.ws.imox.beans.Almacen;
import mx.org.kaana.mantic.ws.imox.beans.Articulo;
import mx.org.kaana.mantic.ws.imox.beans.Cantidad;
import mx.org.kaana.mantic.ws.imox.beans.Conteo;
import mx.org.kaana.mantic.ws.imox.beans.Empresa;
import mx.org.kaana.mantic.ws.imox.beans.Inventario;
import mx.org.kaana.mantic.ws.imox.beans.Producto;
import mx.org.kaana.mantic.ws.imox.beans.Ubicacion;
import mx.org.kaana.mantic.ws.imox.beans.Usuario;
import mx.org.kaana.mantic.ws.publicar.beans.Respuesta;
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

  private static final long serialVersionUID = 8944954886601403304L;
  private static final String TEXT_TOKEN= "/Eb1AjylyNNfQBLodn6Jf6Stb8NM7Hw2";
  private static final Long USER_SUPER  = 1976L;
  private static final Long USER_ADMIN  = 1991L;
  

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
        ArrayList<Inventario> inventarios= (ArrayList<Inventario>)DaoFactory.getInstance().toEntitySet(Inventario.class, "VistaPlanetasDto", "pluton", params, -1L);
        if(inventarios!= null && !inventarios.isEmpty()) 
          regresar= Decoder.toJson(new Respuesta(ERespuesta.CORRECTO.getCodigo(), Decoder.cleanJson(inventarios)));
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
  
  // SERVICIO WEB PARA LOS PRODUCTOS / ARTICULOS
  public String sol(Long radio, String densidad, String atmosfera) throws Exception {
    String regresar           = Decoder.toJson(new Respuesta(ERespuesta.CORRECTO.getCodigo(), "Proceso correcto")); 
    Map<String, Object> params= new HashMap<>();
    Respuesta respuesta       = null;
    try {
      respuesta= this.toRespueta(this.neptuno(atmosfera, radio));
      if(Objects.equals(respuesta.getCodigo(), ERespuesta.CORRECTO.getCodigo())) {
        params.put("fecha", densidad);
        ArrayList<Articulo> articulos= (ArrayList<Articulo>)DaoFactory.getInstance().toEntitySet(Articulo.class, "VistaPlanetasDto", "sol", params, -1L);
        if(articulos!= null && !articulos.isEmpty()) 
          regresar= Decoder.toJson(new Respuesta(ERespuesta.CORRECTO.getCodigo(), Decoder.cleanJson(articulos)));
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
  
  private String toAplicarConteo(Long idUsuario, String densidad) throws Exception {
    String id                 = Cadena.rellenar(String.valueOf(idUsuario), 3, '0', true);
    String regresar           = id.concat(Fecha.toRegistro());
    Map<String, Object> params= new HashMap<>();
    Transaction transaction   = null;
    Session session           = null;
		try {
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
        items.getNombre(), // String nombre, 
        1L, // Long idConteoEstatus, 
        regresar, // String token
        items.getIdEmpresa(), // Long idEmpresa
        items.getIdAlmacen() // Long idAlmacen
      );
      params.put("idReferencia", conteo.getIdReferencia());
      params.put("nombre", conteo.getNombre());
      TcManticConteosDto existe= (TcManticConteosDto)DaoFactory.getInstance().toEntity(TcManticConteosDto.class, "TcManticConteosDto", "igual", params);
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
          conteo.getIdConteo(), // Long idConteo, 
          -1L, // Long idConteoBitacora, 
          conteo.getIdConteoEstatus() // Long idConteoEstatus
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
        TcManticConteosDetallesDto copia= (TcManticConteosDetallesDto)DaoFactory.getInstance().toEntity(TcManticConteosDetallesDto.class, "TcManticConteosDetallesDto", "igual", params);
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
			if (transaction!= null) {
				transaction.rollback();
			} // if
			throw e;
		} // catch
		finally {
      Methods.clean(params);
			if (session!= null) {
				session.close();
			} // if
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
  
}
