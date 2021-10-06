package mx.org.kaana.mantic.productos.backing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.sql.Entity;
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


@Named(value = "manticProductosOrdena")
@ViewScoped
public class Ordena extends IBaseFilter implements Serializable {

  private static final long serialVersionUID = 327393488565639363L;
	private Entity producto;
	private String categoria;
  private String path;
  private UISelectEntity ikEmpresa;

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
      List<UISelectItem> categorias= (List<UISelectItem>)UISelect.free("TcManticProductosDto", "categorias", params, "categoria", "|", EFormatoDinamicos.MAYUSCULAS, "categoria");
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

	public void doSubirPartida(Partida partida) {
    Transaccion transaccion= null;
    try {			
			transaccion = new Transaccion(this.producto, partida);
			if (transaccion.ejecutar(EAccion.SUBIR)) 
				JsfBase.addMessage("Se actualizó el registro del producto", ETipoMensaje.INFORMACION);
			else 
				JsfBase.addMessage("Ocurrió un error al registrar el producto", ETipoMensaje.ERROR);      			
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
	}
  
	public void doBajarPartida(Partida partida) {
    Transaccion transaccion= null;
    try {			
			transaccion = new Transaccion(this.producto, partida);
			if (transaccion.ejecutar(EAccion.BAJAR)) 
				JsfBase.addMessage("Se actualizó el registro del producto", ETipoMensaje.INFORMACION);
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
  
}