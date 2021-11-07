package mx.org.kaana.mantic.productos.backing;

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
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelect;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.pagina.UISelectItem;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.reportes.reglas.Parametros;
import mx.org.kaana.mantic.comun.ParametrosReporte;
import mx.org.kaana.mantic.enums.EReportes;
import mx.org.kaana.mantic.productos.beans.Caracteristica;
import mx.org.kaana.mantic.productos.beans.Partida;
import mx.org.kaana.mantic.productos.beans.Producto;
import org.primefaces.extensions.model.fluidgrid.FluidGridItem;
import mx.org.kaana.kajool.template.backing.Reporte;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import org.primefaces.context.RequestContext;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.TreeNode;

@Named(value = "manticProductosGaleria")
@ViewScoped
public class Galeria extends Contenedor implements Serializable {

  private static final long serialVersionUID = 327393488565639363L;
  
  private Reporte reporte;
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
      if(JsfBase.getFlashAttribute("producto")== null)
        this.producto = new Entity(-1L);
      else
        this.producto = (Entity)JsfBase.getFlashAttribute("producto");
      this.attrs.put("isMatriz", Boolean.FALSE);
      this.categoria= JsfBase.getFlashAttribute("categoria")== null? "": (String)JsfBase.getFlashAttribute("categoria");
      this.ikEmpresa= new UISelectEntity(JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
			this.attrs.put("retorno", JsfBase.getFlashAttribute("retorno"));
      String dns= Configuracion.getInstance().getPropiedadServidor("sistema.dns");
      this.path = dns.substring(0, dns.lastIndexOf("/")+ 1).concat(Configuracion.getInstance().getEtapaServidor().name().toLowerCase()).concat("/galeria/");      
      this.attrs.put("continuar", Boolean.FALSE);
      this.attrs.put("disenio", Boolean.FALSE);  
      this.attrs.put("cliente", new UISelectEntity(-1L));
      this.toLoadCatalog();
      this.attrs.put("continuar", Boolean.TRUE);
      this.doLoad("", 1L, null);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

  @Override
  public void doLoad() {
    Map<String, Object> params= new HashMap<>();
    try {
      this.attrs.put("categoria", "");
      params.put("sortOrder", "order by concat(tc_mantic_productos_categorias.padre, tc_mantic_productos_categorias.nombre), tc_mantic_productos.orden");
      params.put("idEmpresa", this.ikEmpresa.getKey());
      params.put("categoria", this.categoria);
      UISelectEntity cliente= (UISelectEntity)this.attrs.get("cliente");
      if(cliente== null)
        cliente= new UISelectEntity(-1L);
      else {
        List<UISelectEntity> clientes= (List<UISelectEntity>)this.attrs.get("clientes");
        if(clientes!= null && !clientes.isEmpty()) {
          int index= clientes.indexOf(cliente);
          if(index>= 0)
            cliente= clientes.get(index);
          else
            cliente= new UISelectEntity(-1L);
        } // if
      } // else
      this.attrs.put("particular", !Objects.equals(cliente.getKey(), -1L));
      List<Entity> items= (List<Entity>)DaoFactory.getInstance().toEntitySet("VistaProductosDto", "galeria", params, Constantes.SQL_TODOS_REGISTROS);
      this.productos= new ArrayList<>();
      if(items!= null && !items.isEmpty())
        for (Entity item: items) {
          String menudeo= Objects.equals(cliente.getKey(), -1L)? "menudeo": Cadena.toBeanName(cliente.toString("nombre"));
          this.productos.add(new FluidGridItem(new Producto(item.getKey(), menudeo)));    
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
      List<UISelectItem> categorias= (List<UISelectItem>)UISelect.free("TcManticProductosCategoriasDto", "categorias", params, "categoria", Constantes.SEPARADOR, EFormatoDinamicos.MAYUSCULAS, "categoria");
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
		JsfBase.setFlashAttribute("idProducto", this.producto!= null? this.producto.getKey(): -1L);
    return this.attrs.get("retorno")== null? "filtro".concat(Constantes.REDIRECIONAR): ((String)this.attrs.get("retorno")).concat(Constantes.REDIRECIONAR);
  } // doCancelar

  public String toColorPartida(Partida partida) {
    return ""; // Objects.equals(partida.getAction(), ESql.DELETE)? "janal-display-none": "";
  }
 
  public String doLetraCapital(Caracteristica caracteristica) {
    return Cadena.letraCapital(caracteristica.getDescripcion());
  }  
  
  public String doCheckCategoria(Producto producto) {
    String regresar= "none";
    if(producto!= null) {
      String temporal= producto.getCategoria().replaceAll("[|]", "»");
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
  
	public List<UISelectEntity> doCompleteCliente(String codigo) {
 		List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
		boolean buscaPorCodigo    = false;
    try {
			columns= new ArrayList<>();
      columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("rfc", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("razonSocial", EFormatoDinamicos.MAYUSCULAS));
  		params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
			if(!Cadena.isVacio(codigo)) {
  			codigo= new String(codigo).replaceAll(Constantes.CLEAN_SQL, "").trim();
				buscaPorCodigo= codigo.startsWith(".");
				if(buscaPorCodigo)
					codigo= codigo.trim().substring(1);
				codigo= codigo.toUpperCase().replaceAll("(,| |\\t)+", ".*.*");
			} // if	
			else
				codigo= "WXYZ";
  		params.put("codigo", codigo);
			if(buscaPorCodigo)
        this.attrs.put("clientes", UIEntity.build("VistaProductosDto", "porCodigo", params, columns, 40L));
			else
        this.attrs.put("clientes", UIEntity.build("VistaProductosDto", "porNombre", params, columns, 40L));
		} // try
	  catch (Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    }// finally
		return (List<UISelectEntity>)this.attrs.get("clientes");
	} // doCompleteCliente		
 
  public void doUpdateDisenio() {
    this.attrs.put("disenio", !(Boolean)this.attrs.get("disenio"));  
  }
 
  public void doReporte() {
		Map<String, Object>params    = null;
		Map<String, Object>parametros= null;
		EReportes reporteSeleccion   = EReportes.PRODUCTOS;
		try {		
      params= new HashMap<>();
      params.put("idEmpresa", this.ikEmpresa.getKey());
      params.put("categoria", this.categoria);
      params.put("tipoVenta", "menudeo");
      params.put("razonSocial", " ");
      UISelectEntity cliente= (UISelectEntity)this.attrs.get("cliente");
      if(cliente== null)
        cliente= new UISelectEntity(-1L);
      else {
        List<UISelectEntity> clientes= (List<UISelectEntity>)this.attrs.get("clientes");
        if(clientes!= null && !clientes.isEmpty()) {
          int index= clientes.indexOf(cliente);
          if(index>= 0) {
            cliente= clientes.get(index);
            params.put("tipoVenta", cliente.toString("nombre"));
            params.put("razonSocial", cliente.toString("razonSocial"));
          } // if  
          else
            cliente= new UISelectEntity(-1L);
        } // if
      } // else
      params.put("url", this.path);
      params.put("idCliente", cliente.getKey());
      this.reporte= JsfBase.toReporte();	
      Parametros comunes= new Parametros(JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
      parametros= comunes.getComunes();
      parametros.put("ENCUESTA", "LISTADO DE PRODUCTOS");
      parametros.put("NOMBRE_REPORTE", reporteSeleccion.getTitulo());
      parametros.put("REPORTE_ICON", JsfBase.getRealPath("").concat("resources/iktan/icon/acciones/"));			
      this.reporte.toAsignarReporte(new ParametrosReporte(reporteSeleccion, params, parametros));		
      this.doVerificarReporte();
      this.reporte.doAceptar();			
    } // try
    catch(Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);			
    } // catch	
  }
  
  public boolean doVerificarReporte() {
    boolean regresar = false;
		RequestContext rc= UIBackingUtilities.getCurrentInstance();
		if(this.reporte.getTotal()> 0L) {
			rc.execute("start(" + this.reporte.getTotal() + ")");		
      regresar = true;
    } // if
		else{
			rc.execute("generalHide();");		
			JsfBase.addMessage("Reporte", "No se encontraron registros para el reporte", ETipoMensaje.ERROR);
      regresar = false;
		} // else
    return regresar;
	} // doVerificarReporte	
  
  public void onSelectCategoria(NodeSelectEvent event) {
    this.attrs.put("seleccionado", event.getTreeNode());
    this.doGaleria();
  }  

  public void doGaleria() {
    UISelectEntity data= (UISelectEntity)((TreeNode)this.attrs.get("seleccionado")).getData();
    this.categoria= data.toString("padre").concat(data.toString("nombre"));
    this.doLoad();
  }  

}