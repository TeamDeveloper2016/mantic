package mx.org.kaana.kajool.db.comun.hibernate;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Error;
import org.reflections.Reflections;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date Jan 14, 2013
 * @time 12:52:58 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public final class ClassFinder {

  private ClassFinder () {

  }

  public static List<Class<?>> findClasses(Package pkg) throws IOException {
    return findClasses(pkg.getName());
  }

  public static List<Class<?>> findClasses(String packageName) throws IOException {
    List<Class<?>> classes=new ArrayList<Class<?>>();
    ClassLoader classLoader=Thread.currentThread().getContextClassLoader();
    String path=packageName.replace('.', File.separatorChar );
    Enumeration<URL> resources=classLoader.getResources(path);
    while (resources.hasMoreElements()) {
      URL resource=resources.nextElement();
      if (resource.getProtocol().equals("file")) {
        classes.addAll(findClassesInDir(new File(resource.getFile()), packageName));
      }
      else {
        if (resource.getProtocol().toLowerCase().equals("jar") || resource.getProtocol().toLowerCase().equals("zip")) {
          String rf = resource.getFile();
          File file = new File(rf.substring(0, rf.indexOf("!")));
          classes.addAll(findClassesInJar(file, packageName));
        } // if
      } // else
    } // while
    return classes;
  }

  public static List<Class<?>> findClassesInDir(File directory, String packageName) {
    List<Class<?>> classes = new ArrayList<Class<?>>();
    File[] files           = null;
    String name            = null;
    if (!directory.exists()) {
      return classes;
    } // if
    files = directory.listFiles();
    for (File file : files) {
      if (file.isDirectory()) {
        classes.addAll(findClassesInDir(file, packageName+"."+file.getName()));
      }
      else
        if (file.getName().endsWith(".class")) {
          try {
            name = packageName.concat(file.getName().substring(0, file.getName().length()-6));
            name = Cadena.reemplazarCaracter(name,File.separatorChar, '.');
            classes.add(Class.forName(name));
          } // try
          catch (ClassNotFoundException e) {
            Error.mensaje(e);
          } // catch
        } // if
     } // for
     return classes;
  }

  public static List<Class<?>> findClassesInJar(File jarFile, String packageName) throws IOException {
    List<Class<?>> classes   = new ArrayList<Class<?>>();
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
      name = name.replace('/', File.separatorChar);
      if (name.startsWith(path)&&name.endsWith(".class")) {
        try {
          classes.add(Class.forName(name.replace(File.separator , ".").substring(0, name.length()-6)));
        } // try
        catch (ClassNotFoundException e) {
          //do nothing
        }// catch
      } // if
    }
    return classes;
  }

	public static List<Class<?>> findClassesMapping(String packageName) throws IOException {
	  List<Class<?>> regresar  = new ArrayList<Class<?>>();
		String changePackage =  Cadena.reemplazarCaracter(packageName,File.separatorChar,'.' );
		Reflections reflections  = new Reflections(changePackage);
		Set<Class<?>> classes    = reflections.getTypesAnnotatedWith(javax.persistence.Entity.class);
		if (classes!= null && !classes.isEmpty()) {
		  regresar = new ArrayList<Class<?>>(classes);
		}	
		return  regresar;
	}	
	
}
