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
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.procesos.acceso.backing.Indice;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.productos.beans.Caracteristica;
import mx.org.kaana.mantic.productos.beans.Partida;
import mx.org.kaana.mantic.productos.beans.Producto;
import org.primefaces.extensions.model.fluidgrid.FluidGridItem;

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

 private static final Log LOG = LogFactory.getLog(Indice.class);
  private static final long serialVersionUID = 5323749709626263801L;
  
	private Integer salt;
  private List<FluidGridItem> productos;
  private String categoria;
  private String path;

	public Integer getSalt() {
		return salt;
	}

	public String getPath() {
    return path;
  }
  
	public String getBrand() {
    return path.concat("1/marcas/");
  }

  public List<FluidGridItem> getProductos() {
    return productos;
  }

  public String getCategoria() {
    return categoria;
  }

  @Override
  @PostConstruct
  protected void init() {
		this.attrs.put("opcion", 0);
    this.categoria = "";
		this.salt      = (int)(Math.random()* 10000);
    this.productos = new ArrayList<>();
    String dns= Configuracion.getInstance().getPropiedadServidor("sistema.dns");
    this.path = dns.substring(0, dns.lastIndexOf("/")+ 1).concat(Configuracion.getInstance().getEtapaServidor().name().toLowerCase()).concat("/galeria/");      
    this.doLoad();
  }

  public void doOpcion(Integer opcion) {
    this.attrs.put("opcion", opcion);
    UIBackingUtilities.update("tabla");
    UIBackingUtilities.update("catalogo");
    UIBackingUtilities.update("productos");
    if(this.productos!= null)
      this.productos.clear();
  }
 
  public void doUpdateMarcas(String descripcion) {
    Map<String, Object> params= new HashMap<>();
    try {
      this.attrs.put("categoria", "");
      params.put("sortOrder", "order by concat(tc_mantic_productos_categorias.padre, tc_mantic_productos_categorias.nombre), tc_mantic_productos.orden");
      params.put("idEmpresa", 1L);
      params.put("marca", descripcion);
      this.attrs.put("particular", Boolean.FALSE);
      List<Entity> items= (List<Entity>)DaoFactory.getInstance().toEntitySet("VistaProductosDto", "marca", params, Constantes.SQL_TODOS_REGISTROS);
      if(this.productos!= null)
        this.productos.clear();
      if(items!= null && !items.isEmpty()) 
        for (Entity item: items) {
          this.productos.add(new FluidGridItem(new Producto(item.getKey(), "menudeo")));    
        } // for
      UIBackingUtilities.update("tabla");
		} // try
	  catch (Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
    finally {
      Methods.clean(params);
    } // finally     
  }
 
  public String doCheckCategoria(Producto producto) {
    String regresar= "none";
    if(producto!= null) {
      String temporal= producto.getProducto().getCategoria().replaceAll("[|]", "»");
      temporal= temporal.substring(temporal.indexOf("»")+ 1);
      if(!Objects.equals((String)this.attrs.get("categoria"), temporal))
        regresar= "";
      this.attrs.put("categoria", temporal);
      this.attrs.put("links", this.toProcessLinks(temporal));
    } // if  
    return regresar;
  }  
  
  private String toProcessLinks(String categoria) {
    StringBuilder regresar= new StringBuilder();
    StringBuilder link    = new StringBuilder("LINK-");
    regresar.append("<span id=\"LINK-").append(Cadena.eliminar(categoria, ' ').replace('»', '-')).append("\" class=\"ui-panel-title Fs18\">");
    String[] items= categoria.split("[»]");
    for (String item: items) {
      if(!Cadena.isVacio(item)) {
        link.append(item);
        regresar.append("<a onclick=\"movePage('").append(link.toString()).append("');\" style=\"cursor:pointer;\">").append(item).append("</a>").append("  »  ");
        link.append("-");
      } // if  
    } // for
    if(regresar.toString().endsWith("»  "))
      regresar.delete(regresar.length()- 5, regresar.length());
    regresar.append("</span>");
    return regresar.toString();
  }
 
  public String doLetraCapital(Caracteristica caracteristica) {
    return Cadena.letraCapital(caracteristica.getDescripcion());
  }  

  public String toColorPartida(Partida partida) {
    return ""; 
  }

  public void doSearchMain(String codigo) {
    this.doOpcion(3);
    Map<String, Object> params= new HashMap<>();
    try {
      this.attrs.put("categoria", "");
      params.put("sortOrder", "order by concat(tc_mantic_productos_categorias.padre, tc_mantic_productos_categorias.nombre), tc_mantic_productos.orden");
      params.put("idEmpresa", 1L);
      codigo= codigo.toUpperCase().replaceAll(Constantes.CLEAN_SQL, "").trim();
      if(codigo.length()> 1) {
        params.put("codigo", codigo.replaceAll("(,| |\\t)+", ".*.*"));
        this.attrs.put("particular", Boolean.FALSE);
        List<Entity> items= (List<Entity>)DaoFactory.getInstance().toEntitySet("VistaProductosDto", "codigo", params, 30L);
        if(items!= null && !items.isEmpty()) 
          for (Entity item: items) {
            this.productos.add(new FluidGridItem(new Producto(item.getKey(), "menudeo")));    
          } // for
      } // if  
      UIBackingUtilities.update("catalogo");
		} // try
	  catch (Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
    finally {
      Methods.clean(params);
    } // finally   
  }
  
  public void doSearchItem() {
    this.doOpcion(3);
    Map<String, Object> params= new HashMap<>();
    try {
      this.attrs.put("categoria", "");
      params.put("sortOrder", "order by concat(tc_mantic_productos_categorias.padre, tc_mantic_productos_categorias.nombre), tc_mantic_productos.orden");
      params.put("idEmpresa", 1L);
      String codigo= this.attrs.get("codigo")!= null? (String)this.attrs.get("codigo"): "";
      codigo= codigo.toUpperCase().replaceAll(Constantes.CLEAN_SQL, "").trim();
      if(codigo.length()> 1) {
        params.put("codigo", codigo.replaceAll("(,| |\\t)+", ".*.*"));
        this.attrs.put("particular", Boolean.FALSE);
        List<Entity> items= (List<Entity>)DaoFactory.getInstance().toEntitySet("VistaProductosDto", "codigo", params, 30L);
        if(items!= null && !items.isEmpty()) 
          for (Entity item: items) {
            this.productos.add(new FluidGridItem(new Producto(item.getKey(), "menudeo")));    
          } // for
      } // if  
      UIBackingUtilities.update("catalogo");
		} // try
	  catch (Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
    finally {
      Methods.clean(params);
    } // finally          
  }
 
  public void doUpdateCategorias(String descripcion) {
    Map<String, Object> params= new HashMap<>();
    try {
      this.attrs.put("categoria", "");
      params.put("sortOrder", "order by concat(tc_mantic_productos_categorias.padre, tc_mantic_productos_categorias.nombre), tc_mantic_productos.orden");
      params.put("idEmpresa", 1L);
      params.put("categoria", descripcion);
      this.attrs.put("particular", Boolean.FALSE);
      List<Entity> items= (List<Entity>)DaoFactory.getInstance().toEntitySet("VistaProductosDto", "galeria", params, Constantes.SQL_TODOS_REGISTROS);
      if(this.productos!= null)
        this.productos.clear();
      if(items!= null && !items.isEmpty()) 
        for (Entity item: items) {
          this.productos.add(new FluidGridItem(new Producto(item.getKey(), "menudeo")));    
        } // for
      UIBackingUtilities.update("productos");
		} // try
	  catch (Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
    finally {
      Methods.clean(params);
    } // finally     
  }
 
  public void doCleanProductos(String decripcion) {
     if(this.productos!= null)
      this.productos.clear();
  }
   
}