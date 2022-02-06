package mx.org.kaana.kajool.control.backing;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reportes.FileSearch;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 21/08/2015
 * @time 12:27:03 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
@ViewScoped
@Named(value = "kajoolControlInicio")
public class Inicio extends BaseMenu implements Serializable {

  private static final Log LOG = LogFactory.getLog(Inicio.class);
  private static final long serialVersionUID = 5323749709626263801L;
  
  private String path;
  private List<String> images;

	public String getPath() {
    return path;
  }
  
	public String getBrand() {
    return path.concat("1/marcas/");
  }

  public List<String> getImages() {
    return images;
  }
  
  @Override
  @PostConstruct
  protected void init() {
    super.init();
    String dns = Configuracion.getInstance().getPropiedadServidor("sistema.dns");
    this.path  = dns.substring(0, dns.lastIndexOf("/")+ 1).concat(Configuracion.getInstance().getEtapaServidor().name().toLowerCase()).concat("/portal/");      
  }
  
  public void doLoadGallery() {
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
    } // catch	
  }
  
}