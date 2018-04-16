package mx.org.kaana.kajool.db.comun.hibernate;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.xml.Modulos;
import mx.org.kaana.xml.Modulos.Paths;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.cfg.Configuration;

public class CfgFile {

  private static final Log LOG = LogFactory.getLog(CfgFile.class);
  private static final String PACKAGE_DEFAULT = "mx.org.kaana.kajool.db";

  private String pathFile;
  private String nameFile;

  public CfgFile() throws Exception {
    init();
  }

  public String getPathFile() {
    return pathFile;
  }

  public String getNameFile() {
    return nameFile;
  }

  private void init() throws Exception {
    this.nameFile = Configuracion.getInstance().getHibernateCustomFile().trim();
    LOG.info("Archivo de configuración [".concat(this.nameFile).concat("]"));
    try {
      this.pathFile = this.getClass().getResource(this.nameFile).getPath();
      if (Configuracion.getInstance().isEtapaDesarrollo() && pathFile.substring(0, 1).contains("/")) 
        this.pathFile = this.pathFile.substring(1, this.pathFile.length());      
      LOG.info("Path archivo de conguracion [".concat(this.pathFile).concat("]"));
    } // try
    catch (Exception e) {
      LOG.info("Favor de verificar path archivo de configuarción [".concat(this.nameFile).concat("]"));
      throw e;
    } // catch
  }

  public List<File> getPackageContent(String packageName) throws IOException {
    List<File> regresar = new ArrayList<File>();
    Enumeration<URL> urls = Thread.currentThread().getContextClassLoader().getResources(packageName);
    File[] files = null;
    File dir = null;
    URL url = null;
    while (urls.hasMoreElements()) {
      url = urls.nextElement();
      dir = new File(url.getFile());
      files = toFilesDirectory(dir);
      for (File clase : files) {
        regresar.add(clase);
      } // for
    } // while
    return regresar;
  }

  private List toClasses(String paquete) throws IOException {
    List regresar = new ArrayList();
    regresar.addAll(ClassFinder.findClassesMapping(paquete));
    return regresar;
  }

  public void toBuildMetaData(Configuration metaData) {
    List<String> listPackage = new ArrayList<String>();
    List clases = null;
    Modulos modulos = null;
    try {
      listPackage.add(PACKAGE_DEFAULT.concat(".view.dto"));
      modulos = new Modulos(Configuracion.getInstance().toFileModule());
      modulos.load(listPackage, PACKAGE_DEFAULT, Paths.PACKAGE);
      for (String paquete : listPackage) {
        if (paquete != null) {
          clases = toClasses(paquete);
          if (clases != null) {
            for (int i = 0; i < clases.size(); i++) {
              try {
                metaData.addAnnotatedClass((Class) clases.get(i));
              } // try
              catch (Exception e) {
                //LOG.error(e.getMessage());
              } // catch
            } // for
          } // if
        } // if	
      } // for
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
  }

  private File[] toFilesDirectory(File directory) {
    File[] files = null;
    if (directory.isDirectory()) {
      files = directory.listFiles(new FileFilter() {
        public boolean accept(File file) {
          return file.getName().endsWith(".class");
        }
      });
    }
    return files;
  }
}
