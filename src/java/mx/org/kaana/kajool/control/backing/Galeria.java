package mx.org.kaana.kajool.control.backing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import jdk.nashorn.internal.runtime.regexp.joni.ast.ConsAltNode;
import mx.org.kaana.kajool.control.enums.EBusqueda;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
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
@Named(value = "kajoolControlGaleria")
public class Galeria extends BaseMenu implements Serializable {

  private static final Log LOG = LogFactory.getLog(Galeria.class);
  private static final long serialVersionUID = 5323749709626263802L;
  
  private String codigo;
  private EBusqueda busqueda;
  private String pathImage;
  
	public String getPathImage() {
		return pathImage;
	}
  
  @Override
  @PostConstruct
  protected void init() {
    super.init();
    this.codigo  = JsfBase.getFlashAttribute("codigo")== null? "": (String)JsfBase.getFlashAttribute("codigo");
    this.busqueda= JsfBase.getFlashAttribute("busqueda")== null? EBusqueda.CATEGORIA: (EBusqueda)JsfBase.getFlashAttribute("busqueda");
    // this.codigo  = "TORNILLERÍA";
    // this.busqueda= EBusqueda.CATEGORIA;
    String dns= Configuracion.getInstance().getPropiedadServidor("sistema.dns");
    this.pathImage= dns.substring(0, dns.lastIndexOf("/")+ 1).concat(Configuracion.getInstance().getEtapaServidor().name().toLowerCase()).concat("/galeria/");
    if(!Cadena.isVacio(this.codigo))
      this.doLoadArticulos(this.codigo);
  }

  @Override
  public void doLoad() {
  }
  
  public void doLoadArticulos(String codigo) {
    List<Columna> columns     = null;
		Map<String, Object> params= new HashMap<>();
    try {
			columns= new ArrayList<>();
      columns.add(new Columna("propio", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
  		params.put("codigo", codigo.toUpperCase().trim());		
      params.put("sortOrder", "order by tc_mantic_articulos.nombre, tc_mantic_articulos.actualizado");
      if(!Cadena.isVacio(codigo)) {
        this.lazyModel= new FormatCustomLazy("VistaOrdenesComprasDto", "galeria", params, columns);
        if(Objects.equals(EBusqueda.CATEGORIA, this.busqueda)) 
          this.attrs.put("links", this.toProcessLinks(this.codigo));
        else {
          Entity entity = (Entity)DaoFactory.getInstance().toEntity("VistaOrdenesComprasDto", "galeria", params);
          if(entity!= null && !entity.isEmpty())
            this.attrs.put("links", this.toProcessLinks(entity.toString("categoria")));
          else
            this.attrs.put("links", null);
        } // else
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
  
  public void doLoadCategoria(String codigo) {
    this.busqueda= EBusqueda.CATEGORIA;
    this.codigo  = codigo;
    this.doLoadArticulos(codigo);
  }
  
  public String doLoadDetalle() {
    Entity row= (Entity)this.attrs.get("seleccionado");
    JsfBase.setFlashAttribute("idProducto", row.toLong("idProducto"));
    JsfBase.setFlashAttribute("codigo", this.codigo);
    JsfBase.setFlashAttribute("busqueda", this.busqueda);
    return "/Control/individual.jsf".concat(Constantes.REDIRECIONAR);
  }
  
  private String toProcessLinks(String categoria) {
    StringBuilder regresar= new StringBuilder();
    StringBuilder link    = new StringBuilder();
    String[] items= categoria.split("[|]");
    for (String item: items) {
      if(!Cadena.isVacio(item)) {
        link.append(item);
        regresar.append("<span class=\"ui-panel-title Fs16\"><a onclick=\"busquedaCategoria('").append(link.toString()).
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