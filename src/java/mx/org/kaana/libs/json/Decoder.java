package mx.org.kaana.libs.json;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Serializable;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date Jul 23, 2012
 *@time 1:12:32 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */



public final class Decoder {
  /**
   * Obtiene un json de un objeto serializable
   * @param serializable Objeto a obtener el json
   * @return regresa un json del objeto
   * @throws Exception
   */
  public static String toJson(Serializable serializable) throws Exception {
    String regresar = null;
    Gson gson       = null;
    try {
      gson     = new Gson();
      regresar = gson.toJson(serializable);
    } // try
    catch(Exception e) {
      throw e;
    } // catch
    return regresar;
  }
/**
 * Regresa el objeto serializado de un json
 * @param serializable el Objeto que encapsular la informacion del json
 * @param json  json a encapsular
 * @return regresa el objeto serializado
 * @throws Exception
 */

  public static  Serializable toSerializar(Class serializable,String json) throws Exception {
    Serializable regresar   = null;
    Gson gson               = null;
    try {
      gson = new Gson();
      regresar = (Serializable)gson.fromJson(json, serializable);
    } // try
    catch(Exception e ) {
      throw e;
    } // catch
     return (Serializable)regresar;
  }

  public static  Serializable toSerialSqllite(Class serializable, String json) throws Exception {
    Serializable regresar   = null;
    Gson gson               = null;
    try {
      gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
      regresar = (Serializable)gson.fromJson(json, serializable);
    } // try
    catch(Exception e ) {
      throw e;
    } // catch
     return (Serializable)regresar;
  }

  /**
   * Recuperar un objeto leyendo un archivo json
   * @param serializable. el Objeto que encapsular� la informacion del archivo
   * @param file. ruta + nombre del archivo,
   * @return regresa el objeto serializado
   * @throws Exception
   */
  public Serializable fromFileJson(Class serializable, String file) throws Exception {
    Serializable regresar   = null;
    Gson gson               = null;
    String json             = null;
    StringBuilder jsonBuilder = new StringBuilder();
    FileReader fileReader   = null;
    BufferedReader br       = null;
    File f                  = null;
    try {
      gson= new Gson();
      f   = new File(file);
      //Verificar existencia de archivo
      if (! f.exists())
        throw new RuntimeException("Archivo ".concat(file).concat(" no existe"));
      fileReader = new FileReader(f);
      br = new BufferedReader(fileReader);
      while((json = br.readLine()) != null) {
        jsonBuilder.append(json);
      } // while
      regresar = (Serializable)gson.fromJson(jsonBuilder.toString(), serializable);
    } // try
    catch(Exception e) {
      throw e;
    } // catch
    finally {
      if (br != null)
        br.close();
      if (fileReader != null)
       fileReader.close();
      br        = null;
      fileReader= null;
      f         = null;
    } // finally
    return regresar;
  }

}
