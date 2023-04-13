package mx.org.kaana.kajool.procesos.acceso.backing;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.control.backing.BaseMenu;
import mx.org.kaana.kajool.control.bean.Portal;
import mx.org.kaana.libs.recurso.Configuracion;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 21/08/2015
 * @time 12:27:03 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
@ViewScoped
@Named(value = "kajoolAccesoIndice")
public class Indice extends BaseMenu implements Serializable {

  private static final long serialVersionUID = 5323749709626263801L;
  
  private String path;

	public String getPath() {
    return path;
  }
  
  public List<String> getImages() {
    return Portal.getInstance().getImages();
  }
  
  public Boolean getActivar() {
    Boolean regresar= Boolean.TRUE;
    switch(Configuracion.getInstance().getPropiedad("sistema.empresa.principal")) {
      case "mantic":
        regresar= Boolean.TRUE;
        break;
      case "kalan":
        regresar= Boolean.FALSE;
        break;
      case "tsaak":
        regresar= Boolean.FALSE;
        break;
    } // swtich
    return regresar;
  }
  
  @Override
  @PostConstruct
  protected void init() {
    super.init();
    String dns = Configuracion.getInstance().getPropiedadServidor("sistema.dns");
    this.path  = dns.substring(0, dns.lastIndexOf("/")+ 1).concat(Configuracion.getInstance().getEtapaServidor().name().toLowerCase()).concat("/portal/");      
  }
  
  @Override
  public void doLoad() {
  }
  
  public void doLoadGallery() {
    
  }
 
}