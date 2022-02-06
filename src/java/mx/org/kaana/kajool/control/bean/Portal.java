package mx.org.kaana.kajool.control.bean;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.reportes.FileSearch;
import org.mozilla.javascript.edu.emory.mathcs.backport.java.util.Collections;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 6/02/2022
 *@time 01:04:30 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Portal implements Serializable {

  private static final long serialVersionUID = 6003054262706290104L;

  private static Portal instance;
  private static Object mutex;

  private List<String> images;

  static {
    mutex = new Object();
  }

  private Portal() {
    this.doLoad();
  }

  public static Portal getInstance() {
    synchronized (mutex) {
      if (instance == null) {
        instance = new Portal();
      }
    } // if
    return instance;
  }

  public List<String> getImages() {
    return Collections.unmodifiableList(this.images);
  }
  
  public void doLoad() {
    try {
      this.images= new ArrayList<>();
      String portal= Configuracion.getInstance().getPropiedadSistemaServidor("portal");
      FileSearch fileSearch = new FileSearch();
      fileSearch.searchDirectory(new File(portal), "*");
      if(fileSearch.getResult().size()> 0)
        for (String matched: fileSearch.getResult()) {
          String name= matched.substring((matched.lastIndexOf("/")< 0? matched.lastIndexOf("\\"): matched.lastIndexOf("/"))+ 1);
          this.images.add(name);
        } // for 
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch	
  }
  
}
