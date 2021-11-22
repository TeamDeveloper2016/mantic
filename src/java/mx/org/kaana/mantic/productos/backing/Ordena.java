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
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.pagina.IBaseFilter;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelect;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.pagina.UISelectItem;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.productos.beans.Partida;
import mx.org.kaana.mantic.productos.reglas.Transaccion;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.TreeNode;


@Named(value = "manticProductosOrdena")
@ViewScoped
public class Ordena extends Contenedor implements Serializable {

  private static final long serialVersionUID = 327393488565639363L;
	private Entity producto;
	private String categoria;
  private String path;
  private UISelectEntity ikEmpresa;
  private Long maximo;

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

  public Long getMaximo() {
    return maximo;
  }
  
  @PostConstruct
  @Override
  protected void init() {		
    try {
     if(JsfBase.getFlashAttribute("producto")== null)
				UIBackingUtilities.execute("janal.isPostBack('cancelar')");
      this.attrs.put("isMatriz", JsfBase.getAutentifica().getEmpresa().isMatriz());
      this.producto = (Entity)JsfBase.getFlashAttribute("producto");
      this.categoria= this.producto.toString("categoria");
      this.ikEmpresa= new UISelectEntity(this.producto.toLong("idEmpresa"));
			this.attrs.put("retorno", JsfBase.getFlashAttribute("retorno"));
      String dns= Configuracion.getInstance().getPropiedadServidor("sistema.dns");
      this.path = dns.substring(0, dns.lastIndexOf("/")+ 1).concat(Configuracion.getInstance().getEtapaServidor().name().toLowerCase()).concat("/galeria/");      
      this.attrs.put("continuar", Boolean.FALSE);
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
		List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
    try {
			columns= new ArrayList<>();
      columns.add(new Columna("marca", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("articulos", EFormatoDinamicos.MILES_SIN_DECIMALES));
      params.put("sortOrder", "order by tc_mantic_productos.orden");
      params.put("idEmpresa", this.ikEmpresa.getKey());
      params.put("categoria", this.categoria);
      this.lazyModel= new FormatCustomLazy("VistaProductosDto", "ordenar", params, columns);
      UIBackingUtilities.resetDataTable("tabla");
			Value next= DaoFactory.getInstance().toField("TcManticProductosDto", "maximo", params, "siguiente");
			if(next!= null && next.getData()!= null)
			  this.maximo= next.toLong();
			else
			  this.maximo= -1L;
		} // try
	  catch (Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
    finally {
      Methods.clean(columns);
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
      List<UISelectItem> categorias= (List<UISelectItem>)UISelect.free("TcManticProductosCategoriasDto", "categorias", params, "categoria", "|", EFormatoDinamicos.MAYUSCULAS, "categoria");
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
    return ((String)this.attrs.get("retorno")).concat(Constantes.REDIRECIONAR);
  } // doAccion

	public void doSubirPartida(Entity partida) {
    Transaccion transaccion= null;
    try {			
			transaccion = new Transaccion(partida);
			if (transaccion.ejecutar(EAccion.SUBIR)) {
				JsfBase.addMessage("Se actualizó el registro del producto", ETipoMensaje.INFORMACION);
        this.doLoad();
      } // if  
			else 
				JsfBase.addMessage("Ocurrió un error al registrar el producto", ETipoMensaje.ERROR);      			
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
	}
  
	public void doBajarPartida(Entity partida) {
    Transaccion transaccion= null;
    try {			
			transaccion = new Transaccion(partida);
			if (transaccion.ejecutar(EAccion.BAJAR)) {
				JsfBase.addMessage("Se actualizó el registro del producto", ETipoMensaje.INFORMACION);
        this.doLoad();
      } // if  
			else 
				JsfBase.addMessage("Ocurrió un error al registrar el producto", ETipoMensaje.ERROR);      			
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
	}
  
  public String toColorPartida(Partida partida) {
    return ""; // Objects.equals(partida.getAction(), ESql.DELETE)? "janal-display-none": "";
  }
 
  public void onSelectCategoria(NodeSelectEvent event) {
    this.attrs.put("data", event.getTreeNode());
    this.doGaleria();
  }

  public void doGaleria() {
    UISelectEntity data = (UISelectEntity) ((TreeNode) this.attrs.get("data")).getData();
    this.categoria= data.toString("padre").concat(data.toString("nombre"));
    this.doLoad();
  }  
  
}