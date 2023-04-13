package mx.org.kaana.kajool.procesos.acceso.backing;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.control.backing.BaseMenu;
import mx.org.kaana.kajool.control.bean.Portal;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.recurso.Configuracion;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 21/08/2015
 * @time 12:27:03 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
@ViewScoped
@Named(value = "kajoolAccesoUbicacion")
public class Ubicacion extends IBaseAttribute implements Serializable {

  private static final long serialVersionUID = 5113749709626263801L;
  
  private String locate;

	public String getLocate() {
    return locate;
  }
  
  @Override
  @PostConstruct
  protected void init() {
    StringBuilder sb= new StringBuilder();
    switch(Configuracion.getInstance().getPropiedad("sistema.empresa.principal")) {
      case "mantic":
        sb.append("['<span class=janal-font-bold>").append(Configuracion.getInstance().getEmpresa("titulo")).append(" (Pilar Blanco)</span><br/><br/>Telefono:").append(Configuracion.getInstance().getEmpresa("telefono")).append("<br/>Whatsapp:").append(Configuracion.getInstance().getEmpresa("celular")).append("<br/><br/>Canario 8602, Pilar Blanco, C.P. 20289<br/>Aguascalientes, Ags.', 21.8472519, -102.2994786, 1],");
        sb.append("['<span class=janal-font-bold>").append(Configuracion.getInstance().getEmpresa("titulo")).append(" (Jesús María)</span><br/><br/>Telefono:").append(Configuracion.getInstance().getEmpresa("telefono")).append("<br/>Whatsapp:").append(Configuracion.getInstance().getEmpresa("celular")).append("<br/><br/>C. Hidalgo 112, Zona Centro, C.P. 20900<br/>Jesús María, Ags.', 21.960195, -102.3437393, 2]");
        this.locate= sb.toString();
        break;
      case "kalan":
        sb.append("['<span class=janal-font-bold>").append(Configuracion.getInstance().getEmpresa("titulo")).append(" (Matriz)</span><br/><br/>Telefono:").append(Configuracion.getInstance().getEmpresa("telefono")).append("<br/>Whatsapp:").append(Configuracion.getInstance().getEmpresa("celular")).append("<br/><br/>Poder Legislativo 127, Barrio de la Purísima, C.P. 20259<br/>Aguascalientes, Ags.', 21.884118, -102.2842101, 1]");
        this.locate= sb.toString();
        break;
      case "tsaak":
        sb.append("['<span class=janal-font-bold>").append(Configuracion.getInstance().getEmpresa("titulo")).append(" (Matriz)</span><br/><br/>Telefono:").append(Configuracion.getInstance().getEmpresa("telefono")).append("<br/>Whatsapp:").append(Configuracion.getInstance().getEmpresa("celular")).append("<br/><br/>Centro Comercial Agropecuario, Básico 93, Centro Comercial Agropecuario, C.P. 20135<br/>Aguascalientes, Ags.', 21.9047858, -102.2947087, 1],");
        sb.append("['<span class=janal-font-bold>").append(Configuracion.getInstance().getEmpresa("titulo")).append(" (Sucursal)</span><br/><br/>Telefono:").append(Configuracion.getInstance().getEmpresa("telefono")).append("<br/>Whatsapp:").append(Configuracion.getInstance().getEmpresa("celular")).append("<br/><br/>Ferrocarril 105, Desarrollo Especial Bulevar a Zacatecas, C.P. 20126 <br/>Aguascalientes, Ags.', 21.9138923, -102.2874127, 2]");
        this.locate= sb.toString();
        break;
      default:  
        sb.append("['<span class=janal-font-bold>").append(Configuracion.getInstance().getEmpresa("titulo")).append(" (Pilar Blanco)</span><br/><br/>Telefono:").append(Configuracion.getInstance().getEmpresa("telefono")).append("<br/>Whatsapp:").append(Configuracion.getInstance().getEmpresa("celular")).append("<br/><br/>Canario 8602, Pilar Blanco, C.P. 20289<br/>Aguascalientes, Ags.', 21.8472519, -102.2994786, 1],");
        sb.append("['<span class=janal-font-bold>").append(Configuracion.getInstance().getEmpresa("titulo")).append(" (Jesús María)</span><br/><br/>Telefono:").append(Configuracion.getInstance().getEmpresa("telefono")).append("<br/>Whatsapp:").append(Configuracion.getInstance().getEmpresa("celular")).append("<br/><br/>C. Hidalgo 112, Zona Centro, C.P. 20900<br/>Jesús María, Ags.', 21.960195, -102.3437393, 2]");
        this.locate= sb.toString();
    } // swtich
  }
 
}