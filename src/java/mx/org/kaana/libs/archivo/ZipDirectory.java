package mx.org.kaana.libs.archivo;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.zip.Adler32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import mx.org.kaana.libs.formato.Error;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 23/06/2015
 *@time 06:41:53 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class ZipDirectory extends Zip implements Serializable {
	
	private static final long serialVersionUID= 5152112748767969003L;
  private static final Log LOG              = LogFactory.getLog(ZipDirectory.class);	
	
	private ZipOutputStream out;
	private String pathInicial;
	private String directorioRaiz;
	private boolean raiz;
	
	/**
	 * Constructor default de la clase
	 */
	
	public ZipDirectory() {
		this(null, false);
	} // ZipDirectory

	/**
	 * Constructor
	 * @param directorioRaiz Nombre del directorio inicial para el compactado de los archivos.
	 * @param raiz Identifica si el al compactar los archivos se incluira el directorio raiz.
	 */
	
	public ZipDirectory(String directorioRaiz, boolean raiz) {
		super();
		this.directorioRaiz= directorioRaiz;
		this.raiz          = raiz;		
	} // ZipDirectory
	
	/**
	 * Inicializa los atributos heredados por la clase Zip.	
	 * @param nombre Path donde se depositara el archivo compactado.
	 * @param directorio Path donde se encuentran los archivos a compactar.
	 * @param patron Especifica el tipo de archivos a compactar.
	 */
	
	private void loadAttrs(String nombre, String directorio, String patron){
		setNombre(nombre);
		setDirectorio(directorio);
		setPatron(patron);
	} // loadAttrs
	
	/**
	 * Realiza el compactado de los archivos encontrados en el directorio.	
	 * @param nombre Path donde se depositara el archivo compactado.
	 * @param directorio Path donde se encuentran los archivos a compactar.
	 * @param patron Especifica el tipo de archivos a compactar.
	 * @throws Exception
	 */
	
	@Override
	public void compactar(String nombre, String directorio, String patron) throws Exception {		
    File rutaArchivo            = null;
		FileOutputStream destino    = null;
		CheckedOutputStream checksum= null;		
		File file                   = null;			
		try {
			loadAttrs(nombre, directorio, patron);
			rutaArchivo= new File(nombre);
      if (nombre.contains("\\"))
        rutaArchivo = new File(nombre.substring(0, nombre.lastIndexOf("\\")));
      else if (nombre.contains("/"))
        rutaArchivo = new File(nombre.substring(0, nombre.lastIndexOf("/")));
      if (!rutaArchivo.exists())
        rutaArchivo.mkdirs();      		
      destino = new FileOutputStream(nombre);
      checksum= new CheckedOutputStream(destino, new Adler32());
      this.out= new ZipOutputStream(new BufferedOutputStream(checksum));
      file= new File(directorio);
			this.pathInicial= file.getPath();
      recurseFiles(file);
      this.out.close();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      throw e;
    }// try
	}	// compactar
	
	/**
	 * Metodo recursivo, el cual hace el barrido del directorio para localizar los archivos y compactarlos.
	 * @param file Directorio donde se encuentran depositados los archivos.
	 * @throws Exception
	 */
	
	private void recurseFiles(File file) throws Exception{
		File fileValidate         = null;		
		File fileClear            = null;		
		FileInputStream fi        = null;
		ZipEntry entry            = null;		
		BufferedInputStream origen= null;
		String path               = null;
		String pathCompress       = null;		
		try {
			String files[]= file.list();
			byte data[]= new byte[BUFFER];
      for (int i =0; files != null && i<files.length; i++) {
        if (acceptFile(getPatron(), files[i], true)) {
					path= file.getPath().concat(File.separator).concat(files[i]);
					fileValidate= new File(path);
					if(!fileValidate.isDirectory()){
						fi= new FileInputStream(path);
						origen= new BufferedInputStream(fi, BUFFER);												
						pathCompress= this.raiz ? path.substring(path.indexOf(this.directorioRaiz), path.length()) : path.substring(this.pathInicial.length() + 1, path.length());
						entry= new ZipEntry(pathCompress);
						this.out.putNextEntry(entry);
						int count;
						while ((count = origen.read(data, 0, BUFFER)) != -1)
							this.out.write(data, 0, count);
						origen.close();
						if (isEliminar()) {
							fileClear= new File(path);
							fileClear.delete();
						} // if
					} // if
					else						
						recurseFiles(new File(path));										
        } // if
      } // for
		} // try
		catch (Exception e) {						
			throw e;
		} // catch		
	} // recurseFiles
}
