package mx.org.kaana.kajool.db.comun.hibernate;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import mx.org.kaana.libs.formato.Error;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date Jan 14, 2013
 * @time 12:52:58 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public final class FileFinder {

  private FileFinder () {

  }

  public static File findFileXml(String packageName, String nameXml) throws IOException {
    File regresar = null;
    ClassLoader classLoader=Thread.currentThread().getContextClassLoader();
    String path=packageName.replace('.', File.separatorChar );
    Enumeration<URL> resources=classLoader.getResources(path);
    while (resources.hasMoreElements()) {
      URL resource=resources.nextElement();
      if (resource.getProtocol().equals("file")) {
        regresar= findFileInDir(new File(resource.getFile()), packageName,nameXml);
      }
      else {
        if (resource.getProtocol().toLowerCase().equals("jar") || resource.getProtocol().toLowerCase().equals("zip")) {
          String rf = resource.getFile();
          File file = new File(rf.substring(0, rf.indexOf("!")));
          regresar = findFileInJar(file, packageName,nameXml);
        } // if
      } // else
    } // while
    return regresar;
  }

  public static File findFileInDir(File directory, String packageName, String nameXml) {
    File  regresar=null;
    File[] files           = null;
    files = directory.listFiles();
    for (File file : files) {
      if (file.isDirectory()) {
        regresar= findFileInDir(file, packageName+"."+file.getName(), nameXml);
      }
      else
        if (file.getName().endsWith(".xml" ) && file.getName().startsWith(nameXml)) {
          try {
             regresar =  file;
          } // try
          catch (Exception e) {
            Error.mensaje(e);
          } // catch
        } // if
     } // for
     return regresar;
  }

  public static File findFileInJar(File jarFile, String packageName, String nameXml) throws IOException {
    File  regresar= null;
		String pathJar = jarFile.getPath();
		if (pathJar.indexOf("file:")>=0) {
		  pathJar = jarFile.getPath().substring(5,jarFile.getPath().length());
		}			
    JarFile jar               =  new JarFile(pathJar);
    Enumeration<JarEntry> enu = jar.entries();
    String path=packageName.replace(".", File.separator);
    while (enu.hasMoreElements()) {
      JarEntry je=enu.nextElement();
      String name=je.getName();
      if (name.startsWith(path)&&name.endsWith(".xml") && name.indexOf(nameXml)>=0) {
        try {
          regresar = new File(name);
        } // try
        catch (Exception e) {
          //do nothing
        }// catch
      } // if
    }
    return regresar;
  }


  public static File findFilexml(String packageName, String nameXml) throws IOException {
    File regresar = null;
    ClassLoader classLoader=Thread.currentThread().getContextClassLoader();
    String path=packageName.replace('.', File.separatorChar );
    Enumeration<URL> resources=classLoader.getResources(path);
    while (resources.hasMoreElements()) {
      URL resource=resources.nextElement();
      if (resource.getProtocol().equals("file")) {
        regresar = findFileInDir(new File(resource.getFile()), packageName,nameXml);
      }
      else {
        if (resource.getProtocol().toLowerCase().equals("jar") || resource.getProtocol().toLowerCase().equals("zip")) {
          String rf = resource.getFile();
          File file = new File(rf.substring(0, rf.indexOf("!")));
          regresar = findFileInJar(file, packageName,nameXml);
        } // if
      } // else
    } // while
    return regresar;
  }
	
}
