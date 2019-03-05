/*
 * Zip.java
 *
 * Created on 6 de julio de 2005, 01:51 PM
 */

package mx.org.kaana.libs.archivo;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import mx.org.kaana.libs.formato.Error;
import java.util.zip.*;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.mantic.egresos.beans.ZipEgreso;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/***
 *
 * @author  usuario final
 **/
public class Zip {
  protected static final int BUFFER = 2048;

  private String nombre = null;
  private String directorio = null;
  private String patron = null;
  private String salida = null;
  private boolean eliminar = false;
  private boolean debug = false;
  private static final Log LOG= LogFactory.getLog(Zip.class);

  /** Creates a new instance of Zip */
  public Zip() {
    nombre = "archivo.zip";
    patron = ".";
    salida = "/";
    eliminar = false;
  }

  /**
   * Getter for property nombre.
   * @return Value of property nombre.
   */
  public java.lang.String getNombre() {
    return nombre;
  }

  /**
   * Setter for property nombre.
   * @param nombre New value of property nombre.
   */
  public void setNombre(java.lang.String nombre) {
    this.nombre = nombre;
  }

  /**
   * Getter for property patron.
   * @return Value of property patron.
   */
  public java.lang.String getPatron() {
    return patron;
  }

  /**
   * Setter for property patron.
   * @param patron New value of property patron.
   */
  public void setPatron(java.lang.String patron) {
    this.patron = patron;
  }

  /**
   * Getter for property salida.
   * @return Value of property salida.
   */
  public java.lang.String getSalida() {
    return salida;
  }

  /**
   * Setter for property salida.
   * @param salida New value of property salida.
   */
  public void setSalida(java.lang.String salida) {
    this.salida = salida;
  }

  /**
   * Getter for property debug.
   * @return Value of property debug.
   */
  public boolean isDebug() {
    return debug;
  }

  /**
   * Setter for property debug.
   * @param debug New value of property debug.
   */
  public void setDebug(boolean debug) {
    this.debug = debug;
  }

  /**
   * Getter for property directorio.
   * @return Value of property directorio.
   */
  public java.lang.String getDirectorio() {
    return directorio;
  }

  public java.lang.String getDirectorio(boolean diagonal) {
    return directorio + (diagonal ? "/" : "");
  }

  /**
   * Setter for property directorio.
   * @param directorio New value of property directorio.
   */
  public void setDirectorio(java.lang.String directorio) {
    this.directorio = directorio;
  }

  /**
   * Getter for property eliminar.
   * @return Value of property eliminar.
   */
  public boolean isEliminar() {
    return eliminar;
  }

  /**
   * Setter for property eliminar.
   * @param eliminar New value of property eliminar.
   */
  public void setEliminar(boolean eliminar) {
    this.eliminar = eliminar;
  }

  protected boolean acceptFile(String patron, String name, boolean igual) {
    boolean comodin_ = patron.indexOf("?") >= 0;
    boolean _comodin = patron.indexOf("*") >= 0;
    String empieza = null;
    String termina = null;
    if (comodin_) {
      if (patron.indexOf("?")> 0)
        empieza= patron.substring(0, patron.indexOf("?"));
      if (patron.indexOf("?")< patron.length() - 1)
        termina= patron.substring(patron.indexOf("?") + 1, patron.length());
      comodin_= (empieza!= null? name.startsWith(empieza): true) && (termina!= null? name.endsWith(termina): true);
    }
    if (_comodin) {
      if (patron.indexOf("*")> 0)
        empieza= patron.substring(0, patron.indexOf("*"));
      if (patron.indexOf("*")< patron.length() - 1)
        termina= patron.substring(patron.indexOf("*") + 1, patron.length());
      _comodin= (empieza!= null? name.startsWith(empieza): true) && (termina!= null? name.endsWith(termina): true);
    } // if comodin
    return igual? name.equals(patron) || (comodin_ || _comodin) : name.startsWith(Constantes.ARCHIVO_PATRON_NOMBRE) && name.compareTo(patron) < 0;
  } // accept

/**
 * Compacta el Zip
 * @throws Exception
 */

  public void compactar() throws Exception {
    try {
      BufferedInputStream origen = null;
      File rutaArchivo = null;
      if (getNombre().contains("\\"))
        rutaArchivo = new File(getNombre().substring(0,getNombre().lastIndexOf("\\")));
      else
        if (getNombre().contains("/"))
          rutaArchivo = new File(getNombre().substring(0,getNombre().lastIndexOf("/")));
      if (!(rutaArchivo.exists())) 
        rutaArchivo.mkdirs();
      FileOutputStream destino = new FileOutputStream(getNombre());
      if (this.debug)
        LOG.debug("nombre zip: " + getNombre());
      CheckedOutputStream checksum = new CheckedOutputStream(destino, new Adler32());
      ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(checksum));
      byte data[] = new byte[BUFFER];

      File f = new File(getDirectorio());
      if (this.debug)
        LOG.debug(" patron: " + getPatron() + " directorio: " + getDirectorio() + " f: " + f);

      String files[] = f.list();
      if (this.debug)
        LOG.debug("Archivos: " + (files != null ? 0 : files.length));
      for (int i= 0; files != null && i < files.length; i++) {
        if (this.debug)
          LOG.debug("Sumando: " + getDirectorio(true) + files[i]);
        if (acceptFile(getPatron(), files[i], true)) {
          FileInputStream fi = new FileInputStream(getDirectorio(true) + files[i]);
          origen = new BufferedInputStream(fi, BUFFER);
          ZipEntry entry = new ZipEntry(files[i]);
          out.putNextEntry(entry);
          int count;
          while ((count = origen.read(data, 0, BUFFER)) != -1) {
            out.write(data, 0, count);
          } // while
          origen.close();
          if (this.eliminar) {
            f = new File(getDirectorio(true) + files[i]);
            f.delete();
          } // if
        }// if zip
      }// for
      out.close();
    }
    catch (Exception e) {
      Error.mensaje(e);
      throw e;
    }// try
  }

  public void compactar(int token, String files[]) throws Exception {
    try {
      BufferedInputStream origen = null;
      File rutaArchivo = null;
      if (getNombre().contains("\\"))
        rutaArchivo = new File(getNombre().substring(0,getNombre().lastIndexOf("\\")));
      else
        if (getNombre().contains("/"))
          rutaArchivo = new File(getNombre().substring(0,getNombre().lastIndexOf("/")));
      if (!(rutaArchivo.exists())) 
        rutaArchivo.mkdirs();
      FileOutputStream destino = new FileOutputStream(getNombre());
      if (this.debug)
        LOG.debug("nombre zip: " + getNombre());
      CheckedOutputStream checksum = new CheckedOutputStream(destino, new Adler32());
      ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(checksum));
      byte data[] = new byte[BUFFER];
      if (this.debug)
        LOG.debug("Archivos: " + (files!= null? 0: files.length));
      for (String name: files) {
        if (this.debug)
          LOG.debug("Sumando: " + name);
				FileInputStream fi = new FileInputStream(name);
				origen = new BufferedInputStream(fi, BUFFER);
				ZipEntry entry = new ZipEntry(name.substring(token));
				out.putNextEntry(entry);
				int count;
				while ((count = origen.read(data, 0, BUFFER)) != -1) {
					out.write(data, 0, count);
				} // while
				origen.close();
				if (this.eliminar) {
					File file= new File(name);
					file.delete();
				} // if
      }// for
      out.close();
    }
    catch (Exception e) {
      Error.mensaje(e);
      throw e;
    }// try
  }
	
  public void compactar(String nombre, List<ZipEgreso> files) throws Exception {
		compactar(nombre, files, new ArrayList<>());
	} // compactar
	
  public void compactar(String nombre, List<ZipEgreso> files, List<String>notas) throws Exception {
    try {
			setNombre(nombre);						
			FileOutputStream destino = new FileOutputStream(getNombre());
			if (this.debug)
				LOG.debug("nombre zip: " + getNombre());
			CheckedOutputStream checksum = new CheckedOutputStream(destino, new Adler32());
			ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(checksum));
			byte data[] = new byte[BUFFER];
			for(ZipEgreso egreso: files){
				BufferedInputStream origen = null;				
				if (this.debug)
					LOG.debug("Archivos: " + (files!= null? 0: files.size()));
				for (String name: egreso.getFiles()) {
					if (this.debug)
						LOG.debug("Sumando: " + name);
					FileInputStream fi = new FileInputStream(name);
					origen = new BufferedInputStream(fi, BUFFER);
					ZipEntry entry = new ZipEntry(egreso.getCarpeta().substring(egreso.getCarpeta().lastIndexOf("/")+1, egreso.getCarpeta().length()).concat("/").concat(name.substring(name.lastIndexOf("/")+1, name.length())));
					out.putNextEntry(entry);
					int count;
					while ((count = origen.read(data, 0, BUFFER)) != -1) {
						out.write(data, 0, count);
					} // while
					origen.close();
					if (this.eliminar) {
						File file= new File(name);
						file.delete();
					} // if
				}// for								
			} // for			
			if(!notas.isEmpty()){
				String cadena = "";
				for(String line: notas)
					cadena= cadena.concat(line);									
				String namePath= Cadena.reemplazarCaracter(nombre,'/', File.separatorChar);
				String nameFile= namePath.substring(0, namePath.lastIndexOf(File.separator)+1).concat("notas.txt");
				FileOutputStream ops= new FileOutputStream(nameFile);
				BufferedWriter bw= new BufferedWriter(new OutputStreamWriter(ops));
				for(String line: notas){
					bw.write(line);
					bw.newLine();
				}	// for
				bw.close();				
				ops.close();
				FileInputStream fi = new FileInputStream(nameFile);
				BufferedInputStream origen = new BufferedInputStream(fi, BUFFER);
				ZipEntry entry= new ZipEntry("Notas".concat(File.separator).concat("Notas.txt"));				
				out.putNextEntry(entry);				
				int count;
				while ((count = origen.read(data, 0, BUFFER)) != -1) {
					out.write(data, 0, count);
				} // while
				origen.close();
				File file= new File(nameFile);
				if(file.exists())
					file.delete();				
			} // if
			out.close();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      throw e;
    }// try		
  } // compactar
	
  public void especial(String files[]) throws Exception {
    try {
      BufferedInputStream origen = null;
      File rutaArchivo = null;
      if (getNombre().contains("\\"))
        rutaArchivo = new File(getNombre().substring(0,getNombre().lastIndexOf("\\")));
      else
        if (getNombre().contains("/"))
          rutaArchivo = new File(getNombre().substring(0,getNombre().lastIndexOf("/")));
      if (!(rutaArchivo.exists())) 
        rutaArchivo.mkdirs();
      FileOutputStream destino = new FileOutputStream(getNombre());
      if (this.debug)
        LOG.debug("nombre zip: " + getNombre());
      CheckedOutputStream checksum = new CheckedOutputStream(destino, new Adler32());
      ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(checksum));
      byte data[] = new byte[BUFFER];
      if (this.debug)
        LOG.debug("Archivos: " + (files!= null? 0: files.length));
      for (String name: files) {
				String[] tokens= name.split("[|]");
        if (this.debug)
          LOG.debug("Sumando: " + tokens[2]);
				FileInputStream fi = new FileInputStream(tokens[2]);
				origen = new BufferedInputStream(fi, BUFFER);
				ZipEntry entry = new ZipEntry(tokens[1].concat("/").concat(tokens[2].substring(Numero.getInteger(tokens[0], 0))));
				out.putNextEntry(entry);
				int count;
				while ((count = origen.read(data, 0, BUFFER)) != -1) {
					out.write(data, 0, count);
				} // while
				origen.close();
				if (this.eliminar) {
					File file= new File(tokens[2]);
					file.delete();
				} // if
      } // for
      out.close();
    }
    catch (Exception e) {
      throw e;
    }// try
  }
	
/**
 * Compacta el zip con un nombre y un patron
 * @param nombre Nombre del archivo que se quiere poner al zip
 * @param archivos Es la lista de archivos que se van a compactar
 * @throws Exception
 */

  public void compactar(String nombre, int token, String[] archivos) throws Exception {
    setNombre(nombre);
    compactar(token, archivos);
  }

/**
 * Compacta el zip con un nombre y un patron
 * @param nombre Nombre del archivo que se quiere poner al zip
 * @param archivos Es la lista de archivos que se van a compactar
 * @throws Exception
 */

  public void especial(String nombre, String[] archivos) throws Exception {
    setNombre(nombre);
    especial(archivos);
  }

/**
 * Compacta el zip con un nombre y un patron
 * @param nombre Nombre del archivo que se quiere poner al zip
 * @param directorio Es la ruta del directorio que se quiere compactar
 * @param patron Es el patron de los archivos que se quieren agregar al Zip que estan dentro dedel direcotrio dado
 * @throws Exception
 */

  public void compactar(String nombre, String directorio, String patron) throws Exception {
    setNombre(nombre);
    setDirectorio(directorio);
    setPatron(patron);
    compactar();
  }

/**
 * Descompacta los archivos Zip
 * @throws Exception
 */

  public void descompactar() throws Exception {
    File directorio = null;
    try {
      // Crea un ZipInputStream para leer el archivo zip
      directorio = new File(getDirectorio());
      if (!directorio.exists())
        directorio.mkdirs();
      BufferedOutputStream dest = null;
      FileInputStream fis = new FileInputStream(getNombre());
      ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
      // Ciclo para recorrer las entradas del archivo zip
      int count;
      byte data[] = new byte[BUFFER];
      ZipEntry entry;
      while ((entry = zis.getNextEntry()) != null) {
        if (entry.isDirectory()) {
          new File(getDirectorio() + File.separator + entry.getName()).mkdirs();
        } // if
        else {
          String destFN = getDirectorio() + File.separator + entry.getName();
          FileOutputStream fos = null;
          //Escribe el archivo en el archivo de sistema
          try {
            fos = new FileOutputStream(destFN);
          } // try
          catch (FileNotFoundException e) {
            new File(destFN.substring(0, destFN.lastIndexOf(File.separator))).mkdirs();
          } // catch
          fos = new FileOutputStream(destFN);
          dest = new BufferedOutputStream(fos, BUFFER);
          while ((count = zis.read(data, 0, BUFFER)) != -1) {
            dest.write(data, 0, count);
          } // while
          dest.flush();
          dest.close();
        } // else
      } // while
      zis.close();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
  }

  /**
   * Descompacta un archivo Zip
   * @param nombre Nombre del Archivo a descompactar
   * @param directorio Directorio del archivo a descmpactar
   * @throws Exception
   */

  public void descompactar(String nombre, String directorio) throws Exception {
    descompactar(nombre, directorio, null);
  }

  /**
   * Descompacta un archivo Zip
   * @param nombre  Nombre del Archivo a descompactar
   * @param directorio Directorio del archivo a descmpactar
   * @param salida
   * @throws Exception
   */

  public void descompactar(String nombre, String directorio,String salida) throws Exception {
    setNombre(nombre);
    setPatron(salida);
    setDirectorio(directorio);
    descompactar();
  }

  public static void main(String[] args) {
    try {
      LOG.debug("zipFiles: Test Zip-Compress");
      Zip zips = new Zip();
      zips.setDebug(true);
      zips.setEliminar(true);
      zips.compactar("/home/kajool/Escritorio/prueba.zip", "/home/kajool/Escritorio", Constantes.ARCHIVO_PATRON_NOMBRE+ "ArchivoPrueba.slk");
      LOG.debug("zipFile.zip archivo generador Ok.");
			//"E:\Desarrollo\Plataforma\Netbeans\Web\mantic\Mantic24092018\mantic\build\web\Temporal\Zip\K_20190304115526210_EGRESO_TRANSFERENCIA_NO._234243545.zip"			
    }
    catch (Exception e) {
      Error.mensaje(e);
    }
  }
}

