package mx.org.kaana.mantic.ventas.backing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.pagina.IBaseFilter;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.JsfUtilities;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.ventas.beans.Imagen;
import mx.org.kaana.mantic.ventas.enums.ECategorias;
import mx.org.kaana.mantic.ventas.enums.EOrdenarItems;

@Named(value= "manticVentasGaleria")
@ViewScoped
public class Galeria extends IBaseFilter implements Serializable {

  private static final long serialVersionUID = 8793667741599428311L;
	
  private ECategorias category;
  private EOrdenarItems sort;
  private List<Imagen> images;  
  private String path;
  private Entity product;
  private List<Entity> attributes;

  public List<Imagen> getImages() {
    return images;
  }
  
  public String getPath() {
    return path;
  }

  public ECategorias getCategory() {
    return category;
  }

  public Entity getProduct() {
    return product;
  }

  public List<Entity> getAttributes() {
    return attributes;
  }
  
  @PostConstruct
  @Override
  protected void init() {
    try {
      String dns= Configuracion.getInstance().getPropiedadServidor("sistema.dns");
      this.path = dns.substring(0, dns.lastIndexOf("/")+ 1).concat(Configuracion.getInstance().getEtapaServidor().name().toLowerCase()).concat("/galeria/1/");
      this.attrs.put("codigo", "");
      this.attrs.put("buscaPorCodigo", false);
      this.attrs.put("viewDetail", false);
      this.attrs.put("item", new Entity());
      this.attrs.put("cuantos", JsfUtilities.getBrowser().isMobile()? 1: 3);
      this.sort= EOrdenarItems.TEXT_ASC;
      this.category= ECategorias.NINGUNA;
      
      this.images = new ArrayList<>();
      for (int i= 1; i <= 10; i++) {
        this.images.add(new Imagen(new Long(i), "cabo_"+ i+ ".jpg", "CABO PARA MARTILLO "+ i));
      } // for
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init
 
  @Override
  public void doLoad() {
    List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
  	int buscarCodigoPor       = 2;
    try {
      columns = new ArrayList<>();
      columns.add(new Columna("propio", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("descripcion", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("menudeo", EFormatoDinamicos.MONEDA_CON_DECIMALES));
  		params.put(Constantes.SQL_CONDICION, this.category.getText());
			String search= (String) this.attrs.get("codigo"); 
			if(!Cadena.isVacio(search)) {
				if((boolean)this.attrs.get("buscaPorCodigo"))
			    buscarCodigoPor= 0;
				if(search.startsWith("."))
					buscarCodigoPor= 2;
				else 
					if(search.startsWith(":"))
						buscarCodigoPor= 1;
				if(search.startsWith(".") || search.startsWith(":"))
					search= search.trim().substring(1);				
				search= search.toUpperCase().replaceAll(Constantes.CLEAN_SQL, "").trim().replaceAll("(,| |\\t)+", ".*.*");
			} // if	
			else
				search= "";
  		params.put("codigo", search);	
			switch(buscarCodigoPor) {      
				case 0: 
          params.put("sortOrder", this.sort.getSort());
          this.lazyModel = new FormatCustomLazy("VistaGaleriaDto", "porCodigoIgual", params, columns);
					break;
				case 1: 
          params.put("sortOrder", this.sort.getSort());          
          this.lazyModel = new FormatCustomLazy("VistaGaleriaDto",  "porCodigo", params, columns);
					break;
				case 2:
    			params.put("sortOrder", this.sort.getSort());
          this.lazyModel = new FormatCustomLazy("VistaGaleriaDto", "porNombre", params, columns);
          break;
			} // switch
      UIBackingUtilities.resetDataGrid();
      this.attrs.put("viewDetail", false);
      this.attrs.put("item", new Entity());
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    finally {
      Methods.clean(params);
      Methods.clean(columns);
    } // finally		
  } // doLoad

  public void doViewDetail(Entity row) {
    this.attrs.put("viewDetail", true);
    this.attrs.put("item", row);
    List<Columna> columns = null;    
    Map<String, Object> params = null;
    try {      
      params = new HashMap<>();      
      params.put("idArticulo", row.toLong("idArticulo"));      
      columns = new ArrayList<>();
      columns.add(new Columna("mayoreo", EFormatoDinamicos.MONEDA_CON_DECIMALES));
      this.product = (Entity)DaoFactory.getInstance().toEntity("TcManticArticulosDto", "detalle", params);
      if(this.product!= null)
        UIBackingUtilities.toFormatEntity(this.product, columns);
      this.attributes= (List<Entity>)DaoFactory.getInstance().toEntitySet("TcManticArticulosEspecificacionesDto", "detalle", params);
      columns.clear();
      columns.add(new Columna("nombre", EFormatoDinamicos.LETRA_CAPITAL));
      columns.add(new Columna("valor", EFormatoDinamicos.MINUSCULAS));
      if(this.attributes== null)
        this.attributes= new ArrayList<>();
      else
        UIBackingUtilities.toFormatEntitySet(this.attributes, columns); 
      this.images.remove(this.images.size()- 1);
      this.images.add(new Imagen(new Long(this.images.size()), row.toString("archivo"), row.toString("nombre")));      
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

  public void doViewKeyup() {
    this.category= ECategorias.NINGUNA;
    this.doLoad();
  } 
  
  public void doViewSort(String sort) {
    this.sort= EOrdenarItems.valueOf(sort);
    this.doLoad();
  } 
  
  public void doViewSearch(String search) {
    this.category= ECategorias.valueOf(search);
    this.attrs.put("codigo", "");
    this.doLoad();
  }

  public void doViewMovil(Long cuantos) {
    this.attrs.put("cuantos", cuantos);
  } 
  
	@Override
	protected void finalize() throws Throwable {
    super.finalize();		
	}	// finalize
  
}
