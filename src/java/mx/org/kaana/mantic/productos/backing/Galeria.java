package mx.org.kaana.mantic.productos.backing;

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
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelect;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.pagina.UISelectItem;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.productos.beans.Caracteristica;
import mx.org.kaana.mantic.productos.beans.Partida;
import mx.org.kaana.mantic.productos.beans.Producto;
import org.primefaces.extensions.model.fluidgrid.FluidGridItem;


@Named(value = "manticProductosGaleria")
@ViewScoped
public class Galeria extends IBaseAttribute implements Serializable {

  private static final long serialVersionUID = 327393488565639363L;
	private Entity producto;
	private String categoria;
  private String path;
  private UISelectEntity ikEmpresa;
  private List<FluidGridItem> productos;

  public String getCategoria() {
    return categoria;
  }

  public void setCategoria(String categoria) {
    this.categoria = categoria;
  }
  
	public String getPath() {
    return path;
  }

  public UISelectEntity getIkEmpresa() {
    return ikEmpresa;
  }

  public void setIkEmpresa(UISelectEntity ikEmpresa) {
    this.ikEmpresa = ikEmpresa;
  }

  public List<FluidGridItem> getProductos() {
    return productos;
  }
  
  @PostConstruct
  @Override
  protected void init() {		
    try {
      //if(JsfBase.getFlashAttribute("producto")== null)
			//	UIBackingUtilities.execute("janal.isPostBack('cancelar')");
      this.attrs.put("isMatriz", Boolean.FALSE);
      //this.producto = (Entity)JsfBase.getFlashAttribute("producto");
      this.producto = new Entity(-1L);
      this.producto.add("idEmpresa", 1L);
      this.producto.add("idProducto", 1L);
      this.producto.add("categoria", "TORNILLOS");
      this.categoria= this.producto.toString("categoria");
      this.ikEmpresa= new UISelectEntity(this.producto.toLong("idEmpresa"));
			this.attrs.put("retorno", JsfBase.getFlashAttribute("retorno"));
      String dns= Configuracion.getInstance().getPropiedadServidor("sistema.dns");
      this.path = dns.substring(0, dns.lastIndexOf("/")+ 1).concat(Configuracion.getInstance().getEtapaServidor().name().toLowerCase()).concat("/galeria/");      
      this.attrs.put("continuar", Boolean.FALSE);
      this.attrs.put("particular", Boolean.FALSE);
      this.toLoadCatalog();
      this.attrs.put("continuar", Boolean.TRUE);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

  public void doLoad() {
    Map<String, Object> params= new HashMap<>();
    try {
      params.put("sortOrder", "order by tc_mantic_productos.orden");
      params.put("idEmpresa", this.ikEmpresa.getKey());
      params.put("categoria", this.categoria);
      List<Entity> items= (List<Entity>)DaoFactory.getInstance().toEntitySet("VistaProductosDto", "galeria", params, Constantes.SQL_TODOS_REGISTROS);
      this.productos= new ArrayList<>();
      if(items!= null && !items.isEmpty())
        for (Entity item: items) {
          this.productos.add(new FluidGridItem(new Producto(item.getKey())));    
        } // for
		} // try
	  catch (Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
    finally {
      Methods.clean(params);
    } // finally        
  } // doLoad

	private void toLoadCatalog() {
		List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
    try {
			columns= new ArrayList<>();
			if(JsfBase.getAutentifica().getEmpresa().isMatriz())
        params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresaDepende());
			else
				params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
			params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
			params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
      columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      this.attrs.put("sucursales", UIEntity.build("TcManticEmpresasDto", "empresas", params, columns));			
      this.doLoadCategorias();
    } // try
    catch (Exception e) {
      throw e;
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    } // finally
	}

	public void doLoadCategorias() {
		List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
    try {
			columns= new ArrayList<>();
      params.put("idEmpresa", this.ikEmpresa.getKey());
      List<UISelectItem> categorias= (List<UISelectItem>)UISelect.free("TcManticProductosDto", "categorias", params, "categoria", Constantes.SEPARADOR, EFormatoDinamicos.MAYUSCULAS, "categoria");
      this.attrs.put("categorias", categorias);			
      if((Boolean)this.attrs.get("continuar"))
        if(categorias!= null && !categorias.isEmpty())
          this.categoria= (String)categorias.get(0).getValue();
        else
          this.categoria= "";
      this.doLoad();
    } // try
    catch (Exception e) {
      throw e;
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    } // finally
	}
  
  public String doAceptar() {  
    return null;
  } // doAccion

  public String doCancelar() {   
		JsfBase.setFlashAttribute("idProducto", this.producto.getKey());
    return this.attrs.get("retorno")== null? "filtro".concat(Constantes.REDIRECIONAR): ((String)this.attrs.get("retorno")).concat(Constantes.REDIRECIONAR);
  } // doCancelar

  public String toColorPartida(Partida partida) {
    return ""; // Objects.equals(partida.getAction(), ESql.DELETE)? "janal-display-none": "";
  }
 
  public String doLetraCapital(Caracteristica caracteristica) {
    return Cadena.letraCapital(caracteristica.getDescripcion());
  }  
  
  public String doCheckCategoria(Producto producto) {
    if(producto!= null)
      this.attrs.put("categoria", producto.getCategoria());
    return "";
  }  
  
}