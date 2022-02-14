package mx.org.kaana.kajool.control.backing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.faces.component.html.HtmlGraphicImage;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
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
import org.primefaces.component.graphicimage.GraphicImage;
import org.primefaces.model.menu.DefaultMenuColumn;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

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

  private MenuModel megaCategorias;

  private String codigo;
  private EBusqueda busqueda;
  private String pathImage;
  
	public String getPathImage() {
		return pathImage;
	}
  
  public MenuModel getMegaCategorias() {
    return this.megaCategorias;
  }
  
  @Override
  @PostConstruct
  protected void init() {
    super.init();
    this.codigo  = JsfBase.getFlashAttribute("codigo")== null? "": (String)JsfBase.getFlashAttribute("codigo");
    this.busqueda= JsfBase.getFlashAttribute("busqueda")== null? EBusqueda.CATEGORIA: (EBusqueda)JsfBase.getFlashAttribute("busqueda");
    String dns= Configuracion.getInstance().getPropiedadServidor("sistema.dns");
    this.pathImage= dns.substring(0, dns.lastIndexOf("/")+ 1).concat(Configuracion.getInstance().getEtapaServidor().name().toLowerCase()).concat("/galeria/");
    if(!Cadena.isVacio(this.codigo))
      this.doLoadArticulos(this.codigo);
    this.toLoadMegaCategorias();
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
  
  private void toLoadMegaCategorias() {
    List<Entity> categorias   = null;
    DefaultSubMenu categoria  = null;
    Map<String, Object> params= new HashMap<>();
    try {      
      params.put("nivel", 2);      
      params.put("nombre", "null");      
      // MENU DINAMICO
      this.megaCategorias= new DefaultMenuModel();
      categorias= (List<Entity>)DaoFactory.getInstance().toEntitySet("VistaProductosDto", "categorias", params);
      if(categorias!= null && !categorias.isEmpty())
        for (Entity item: categorias) {
          String nombre= Cadena.letraCapital(item.toString("categoria"));
          categoria    = new DefaultSubMenu(nombre);
          categoria.setIcon("fa ".concat(item.toString("icon")));
          categoria.addElement(this.toLoadMarcas(item));
          categoria.addElement(this.toLoadDivisiones(item));
          categoria.addElement(this.toLoadImagen(item));
          this.megaCategorias.addElement(categoria);
        } // for
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch	
    finally {
      Methods.clean(params);
      Methods.clean(categorias);
    } // finally    
  } 
  
  public DefaultMenuColumn toLoadMarcas(Entity categoria) {
    DefaultMenuColumn regresar= new DefaultMenuColumn();
    List<Entity> marcas       = null;
    Map<String, Object> params= null;
    try {      
      params= new HashMap<>();      
      params.put("categoria", categoria.toString("nombre"));      
      marcas= (List<Entity>)DaoFactory.getInstance().toEntitySet("VistaProductosDto", "marcas", params);
      DefaultSubMenu subMenu= new DefaultSubMenu("MARCA");
      subMenu.setIcon("fa fa-language");
      if(marcas!= null && !marcas.isEmpty()) {
        int count= 0;
        for (Entity item: marcas) {      
          DefaultMenuItem marca= new DefaultMenuItem(item.toString("marca"));
          marca.setIcon("fa fa-picture-o");
          marca.setOncomplete("galeriaPrincipal('".concat(item.toString("marca")).concat("', 'MARCA');"));
          subMenu.addElement(marca);
          if(count++> 9)
            break;
        } // for
        DefaultMenuItem marca= new DefaultMenuItem("Ver mas ... ");
        marca.setIcon("fa fa-picture-o");
        marca.setOncomplete("marcasPrincipal('VER_MAS', 'MARCA');");
        subMenu.addElement(marca);
      } // if
      regresar.addElement(subMenu);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);      
    } // catch	
    finally {
      Methods.clean(params);
      Methods.clean(marcas);
    } // finally
    return regresar;
  }
  
  public DefaultMenuColumn toLoadDivisiones(Entity categoria) {
    DefaultMenuColumn regresar= new DefaultMenuColumn();
    List<Entity> divisiones   = null;
    Map<String, Object> params= new HashMap<>();
    try {      
      params.put("categoria", categoria.toString("nombre").concat(Constantes.SEPARADOR));      
      divisiones= (List<Entity>)DaoFactory.getInstance().toEntitySet("VistaProductosDto", "divisiones", params);
      DefaultSubMenu subMenu= new DefaultSubMenu("DIVISION");
      subMenu.setIcon("fa fa-language");
      if(divisiones!= null && !divisiones.isEmpty()) {
        int count= 0;
        for (Entity item: divisiones) {      
          DefaultMenuItem division= new DefaultMenuItem(item.toString("categoria"));
          division.setIcon("fa fa-picture-o");
          division.setOncomplete("galeriaPrincipal('".concat(item.toString("categoria")).concat("', 'CATEGORIA');"));
          subMenu.addElement(division);
          if(count++> 9)
            break;
        } // for
        DefaultMenuItem division= new DefaultMenuItem("Ver mas ...");
        division.setIcon("fa fa-picture-o");
        division.setOncomplete("divisionesPrincipal('VER_MAS', 'CATEGORIA');");
        subMenu.addElement(division);
      } // if  
      regresar.addElement(subMenu);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);      
    } // catch	
    finally {
      Methods.clean(params);
      Methods.clean(divisiones);
    } // finally
    return regresar;
  }
  
  public DefaultMenuColumn toLoadImagen(Entity categoria) {
    DefaultMenuColumn regresar= new DefaultMenuColumn();
    try {      
      DefaultSubMenu subMenu= new DefaultSubMenu(categoria.toString("categoria"));
      subMenu.setIcon("fa fa-language");
      regresar.addElement(subMenu);
      HtmlGraphicImage picture= new HtmlGraphicImage();
      picture.setUrl(this.pathImage.concat("1/categorias/").concat(categoria.toString("imagen")));
      picture.setWidth("400px");
      picture.setHeight("280px");
      subMenu.addElement(new DefaultMenuItem(picture));
      regresar.getElements().add(new DefaultMenuItem(picture));
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);      
    } // catch	
    return regresar;
  }
    
}