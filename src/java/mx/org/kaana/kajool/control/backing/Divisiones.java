package mx.org.kaana.kajool.control.backing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.control.enums.EBusqueda;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 21/08/2015
 * @time 12:27:03 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
@ViewScoped
@Named(value = "kajoolControlDivisiones")
public class Divisiones extends BaseMenu implements Serializable {

  private static final Log LOG = LogFactory.getLog(Divisiones.class);
  private static final long serialVersionUID = 5323749709626263802L;

  private String codigo;
  private String pathImage;
  
	public String getPathImage() {
		return pathImage;
	}
  
  @Override
  @PostConstruct
  protected void init() {
    super.init();
    this.codigo= JsfBase.getFlashAttribute("codigo")== null? "": (String)JsfBase.getFlashAttribute("codigo");
    String dns = Configuracion.getInstance().getPropiedadServidor("sistema.dns");
    this.pathImage= dns.substring(0, dns.lastIndexOf("/")+ 1).concat(Configuracion.getInstance().getEtapaServidor().name().toLowerCase()).concat("/galeria/1/categorias/");
    if(!Cadena.isVacio(this.codigo))
      this.doLoadDivisiones();
  }

  @Override
  public void doLoad() {
    
  }
  
  public void doLoadDivisiones() {
    List<Columna> columns     = null;
		Map<String, Object> params= new HashMap<>();
    try {
			columns= new ArrayList<>();
      columns.add(new Columna("categoria", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
  		params.put("categoria", this.codigo.toUpperCase().trim());		
      if(!Cadena.isVacio(this.codigo)) {
        this.lazyModel= new FormatCustomLazy("VistaProductosDto", "divisiones", params, columns);
        this.attrs.put("links", this.toProcessLinks(this.codigo));
      } // if  
      else
        this.attrs.put("links", null);
		} // try
	  catch (Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    } // finally    
  }
  
  public void doLoadArticulos(String codigo) {
    JsfBase.setFlashAttribute("codigo", codigo);
    JsfBase.setFlashAttribute("busqueda", EBusqueda.CATEGORIA);
  }
  
  public void doLoadCategoria(String codigo) {
    JsfBase.setFlashAttribute("codigo", codigo);
    JsfBase.setFlashAttribute("busqueda", EBusqueda.CATEGORIA);
  }
  
  public void doLoadLocal(String codigo) {
    this.codigo= codigo;
    this.doLoadDivisiones();
  }
  
  public String doLoadDetalle() {
    Entity row= (Entity)this.attrs.get("seleccionado");
    JsfBase.setFlashAttribute("codigo", row.toString("nombre"));
    JsfBase.setFlashAttribute("busqueda", EBusqueda.CATEGORIA);
    return "/Control/galeria".concat(Constantes.REDIRECIONAR);
  }
  
  private String toProcessLinks(String categoria) {
    StringBuilder regresar= new StringBuilder();
    StringBuilder link    = new StringBuilder();
    String[] items= categoria.split("[|]");
    for (String item: items) {
      if(!Cadena.isVacio(item)) {
        link.append(item);
        regresar.append("<span class=\"ui-panel-title Fs16\"><a onclick=\"busquedaLocal('").append(link.toString()).
                append("');\" class=\"janal-move-element janal-font-bold\" style=\"color: black; cursor:pointer;\">").
                append(Cadena.letraCapital(item)).append("</a></span>").append("<span class=\"ui-panel-title janal-color-blue Fs18\">   »   </span>");
        link.append(Constantes.SEPARADOR);
      } // if  
    } // for
    if(regresar.length()> 0)
      regresar.delete(regresar.length()- 66, regresar.length());
    return regresar.toString();
  }
  
}