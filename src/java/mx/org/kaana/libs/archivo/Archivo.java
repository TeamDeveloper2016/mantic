package mx.org.kaana.libs.archivo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.util.Calendar;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.formato.Fecha;

public final class Archivo {

  /**
   * Verifica si la ruta existe, si no la crea
   * @param ruta path a verificar
   * @return regresa si se cre� con exito, panda una excepci�n si no se puede crear
   */

  public static boolean verificaRuta(String ruta){
    File dir = new File(ruta.substring(0,ruta.lastIndexOf(File.separator)));
    boolean exito = false;
    if (!(dir.exists())) {
      exito = dir.mkdirs();
    } // if
    if (!exito && !dir.exists()) {
      try {
        throw new Exception("No se pudo crear la ruta ".concat(ruta));
      } // try
      catch (Exception e){
        Error.mensaje(e);
      }// catch
    }// id
    return exito;
  }

  /**
   *
   * @param file Archivo Blob que se va a leer
   * @param fileName nombre del archivo que se va a crear el Blob
   * @return regresa true si se ley� correctamente
   */

  public static boolean writeBlob(Blob file, String fileName){
    InputStream inputStream= null;
    FileOutputStream fw    = null;
    boolean regresar       = true;
    try  {
      inputStream = file.getBinaryStream();
      fw = new FileOutputStream(fileName);
      // Bucle de lectura del blob y escritura en el fichero, de 1024
      // en 1024 bytes.
      byte bytes[] = new byte[1024];
      int leidos = inputStream.read(bytes);
      while (leidos > 0) {
         fw.write(bytes);
         leidos = inputStream.read(bytes);
      } // while
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      regresar= false;
    } // catch
    finally {
      try  {
        if (fw!=null)
          fw.close();
        if (inputStream!=null)
          inputStream.close();
      } // try
      catch (Exception e) {
        Error.mensaje(e);
      } // catch
    } // finally
    return regresar;
  }
	
	public static String toNormalizerName(String name) {
		String regresar= name.toUpperCase();
		try {
		  regresar= new String(name.toUpperCase().getBytes(), "UTF-8");
		} // catch
		catch(Exception e) {
			Error.mensaje(e);
		} // catch
	  return Cadena.toNormalizer(regresar);	
	}


  /**
   * Obtiene el estandar del nombre de los archivos creados en
   * @param nameFile Sufijo a concatenar para obtener el nombre del archivo
   * @return Devuelve el nombre del archivo Ej: K20150126123324122_nombreArchivo
   */
  public static String toFormatNameFile(String nameFile) {
    StringBuilder regresar = new StringBuilder();
    regresar.append(Constantes.ARCHIVO_PATRON_NOMBRE);
    regresar.append(Constantes.ARCHIVO_PATRON_SEPARADOR);
    regresar.append(Fecha.formatear("yyyyMMddhhmmssS", Calendar.getInstance().getTime()));
    regresar.append(Constantes.ARCHIVO_PATRON_SEPARADOR);
    regresar.append(toNormalizerName(nameFile));
    return regresar.toString();
  }
	
/**
 * Realiza una copia de un archivo
 * @param source Path completo del archivo que se quiere copiar
 * @param target Path completo del archivo que se quiere crear
 * @param delete
 */
	public static void copy(String source, String target, boolean delete) {
    InputStream inStream  = null;
    OutputStream outStream= null;
    try {
      File afile= new File(source);
      File bfile= new File(target);
      inStream  = new FileInputStream(afile);
      outStream = new FileOutputStream(bfile);
      byte[] buffer=new byte[1024];
      int length= 0;
      while ((length= inStream.read(buffer))> 0) {
        outStream.write(buffer, 0, length);
      } // while
      inStream.close();
      outStream.close();
    } // try
    catch (IOException e) {
      Error.mensaje(e);
    } // catch
  }
	
	public static void copyDeleteSource(String source, String target, boolean delete) {
    InputStream inStream  = null;
    OutputStream outStream= null;
		File afile            = null;
		File bfile            = null;
    try {
      afile= new File(source);
      bfile= new File(target);
      inStream  = new FileInputStream(afile);
      outStream = new FileOutputStream(bfile);
      byte[] buffer=new byte[1024];
      int length= 0;
      while ((length= inStream.read(buffer))> 0) {
        outStream.write(buffer, 0, length);
      } // while
      inStream.close();
      outStream.close();
			inStream= null;
			outStream= null;
    } // try
    catch (IOException e) {
      Error.mensaje(e);
    } // catch
		finally{
			if(afile!= null)
				afile.delete();			
		} // finally
  }

  /**
   * Realiza una copia de un archivo
   * @param source Path completo del archivo que se quiere copiar
   * @param target Path completo del archivo que se quiere crear
   */

	public static void copy(String source, String target) {
    copy(source, target, false);
  }
  /**
   *
   * @param ruta Ruta del archivo que se quiere eliminar
   * @return Si existe el archivo lo borra y regresa un valor verdadero
   */

  public static boolean delete(String ruta) {
    boolean regresar= true;
    File dir = new File(ruta.substring(0,ruta.lastIndexOf(File.separator)));
    if(dir.exists())
      regresar=dir.delete();
    return regresar;
  }
	
	public static String toFormatNameFile(String nameFile, String prefijo) {
    StringBuilder regresar = new StringBuilder();
    regresar.append(Cadena.isVacio(prefijo)? Constantes.ARCHIVO_PATRON_NOMBRE: prefijo.toUpperCase());
    regresar.append(Constantes.ARCHIVO_PATRON_SEPARADOR);
    regresar.append(Fecha.formatear("yyyyMMddhhmmssS", Calendar.getInstance().getTime()));
    regresar.append(Constantes.ARCHIVO_PATRON_SEPARADOR);
    regresar.append(nameFile);
    return regresar.toString();    
  }

	public static void toWriteFile(File result, InputStream inputStream) throws Exception {
		FileOutputStream fileOutputStream= new FileOutputStream(result);
		byte[] buffer                    = new byte[Constantes.BUFFER_SIZE];
		int bulk;
		while(true) {
			bulk= inputStream.read(buffer);
			if (bulk < 0) 
				break;        
			fileOutputStream.write(buffer, 0, bulk);
			fileOutputStream.flush();
		} // while
		fileOutputStream.close();
		inputStream.close();
	} // toWriteFile
	
}