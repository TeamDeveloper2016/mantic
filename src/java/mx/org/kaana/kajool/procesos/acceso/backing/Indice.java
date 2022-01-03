package mx.org.kaana.kajool.procesos.acceso.backing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import mx.org.kaana.kajool.procesos.mantenimiento.temas.backing.TemaActivo;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.inventarios.comun.IBaseImportar;
import mx.org.kaana.mantic.productos.beans.Caracteristica;
import mx.org.kaana.mantic.productos.beans.Partida;
import mx.org.kaana.mantic.productos.beans.Producto;
import org.primefaces.extensions.model.fluidgrid.FluidGridItem;
import org.primefaces.model.StreamedContent;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 21/08/2015
 * @time 12:27:03 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
@ViewScoped
@Named(value = "kajoolAccesoIndice")
public class Indice extends IBaseImportar implements Serializable {

  private static final Log LOG = LogFactory.getLog(Indice.class);
  private static final long serialVersionUID = 5323749709626263801L;
  
  @Inject 
  private TemaActivo temaActivo;
	private Integer salt;
  private List<Entity> marcas;
  private List<Entity> menu;
  private Long indexMarca;    
  private List<FluidGridItem> productos;
  private String categoria;
  private String path;

  public TemaActivo getTemaActivo() {
    return temaActivo;
  }

  public void setTemaActivo(TemaActivo temaActivo) {
    this.temaActivo = temaActivo;
  }
	
  public Boolean getExistsXml() {
    return this.attrs.get("xmlFile")!= null;
  }

  public Boolean getExistsPdf() {
    return this.attrs.get("pdfFile")!= null;
  }

	public StreamedContent getPdfFileDownload() {
		return this.toPdfFileDownload((Entity)this.attrs.get("pdfFile")); 
	}
	
	public StreamedContent getXmlFileDownload() {
		return this.toXmlFileDownload((Entity)this.attrs.get("xmlFile")); 
	}

	public Integer getSalt() {
		return salt;
	}

  public List<Entity> getMarcas() {
    return marcas;
  }

  public List<Entity> getMenu() {
    return menu;
  }

  public Long getIndexMarca() {
    return indexMarca;
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
		this.attrs.put("rfc", "");
		this.attrs.put("folio", "");
		this.attrs.put("codigo", "");
		this.attrs.put("pdfFile", null);
		this.attrs.put("xmlFile", null);
		this.attrs.put("opcion", 0);
    this.categoria = "";
		this.salt      = (int)(Math.random()* 10000);
    this.indexMarca= -1L;
    this.productos = new ArrayList<>();
    String dns= Configuracion.getInstance().getPropiedadServidor("sistema.dns");
    this.path = dns.substring(0, dns.lastIndexOf("/")+ 1).concat(Configuracion.getInstance().getEtapaServidor().name().toLowerCase()).concat("/galeria/");      
    this.doLoad();
  }

  @Override
	public void doLoad() {
    List<Columna> columns     = null;    
    Map<String, Object> params= null;
    try {      
      params = new HashMap<>();      
      params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);      
      params.put("sortOrder", "order by tc_mantic_productos_marcas.nombre");
      columns = new ArrayList<>();
      columns.add(new Columna("descripcion", EFormatoDinamicos.MAYUSCULAS));
      this.marcas= (List<Entity>)DaoFactory.getInstance().toEntitySet("TcManticProductosMarcasDto", "lazy", params);
      this.menu  = (List<Entity>)DaoFactory.getInstance().toEntitySet("TcManticProductosCategoriasDto", "menu", params);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);      
    } // catch	
    finally {
      Methods.clean(params);
      Methods.clean(columns);
    } // finally
	}
  
  public void doRecoverTicket() {
		Map<String, Object> params= new HashMap<>();
		try {
			String codigo= this.toDecodeHash((String)this.attrs.get("codigo"));
			String encode= this.attrs.get("encode")== null? "": (String)this.attrs.get("encode");
			if(encode.equals(codigo)) {
				params.put("rfc", this.attrs.get("rfc"));
				params.put("folio", this.attrs.get("folio"));
				params.put("idTipoArchivo", "2");
				this.attrs.put("pdfFile", DaoFactory.getInstance().toEntity("VistaFicticiasDto", "descargas", params)); 			
				params.put("idTipoArchivo", "1");
				this.attrs.put("xmlFile", DaoFactory.getInstance().toEntity("VistaFicticiasDto", "descargas", params)); 			
				if(this.attrs.get("xmlFile")== null || this.attrs.get("pdfFile")== null) {
					JsfBase.addAlert("Error", "La factura no existe con los datos propocionados !", ETipoMensaje.ERROR);
				}	 // if
				else 
					UIBackingUtilities.execute("$(\"div[role='1']\").removeClass('Container100');$(\"div[role]\").addClass('Container50');$(\"div[role='2']\").show();$('#download').click();");
			} // if
			else 
				JsfBase.addAlert("Error", "El código de verificación, esta incorrecto !", ETipoMensaje.ERROR);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
		finally {
			Methods.clean(params);
		} // finally
		this.attrs.put("codigo", "");
  } 

	
  public void doViewPdfDocument() { 
		this.toCopyDocument(((Entity)this.attrs.get("pdfFile")).toString("alias"), ((Entity)this.attrs.get("pdfFile")).toString("archivo"));
	}

	public void doViewXmlDocument() {
		this.toViewFile(((Entity)this.attrs.get("xmlFile")).toString("alias"));
	}
	
  private String toDecodeHash(String value) {
		int hash = 5381;
		value = value.toUpperCase();
		for(int i = 0; i < value.length(); i++) {
			hash = ((hash << 5) + hash) + value.charAt(i);
		} // for
		return String.valueOf(hash);
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