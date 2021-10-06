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
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.pagina.IBaseFilter;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelect;
import mx.org.kaana.libs.pagina.UISelectItem;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.productos.reglas.Transaccion;
import mx.org.kaana.mantic.productos.beans.Caracteristica;
import mx.org.kaana.mantic.productos.beans.Partida;
import mx.org.kaana.mantic.productos.beans.Producto;


@Named(value = "manticProductosAccion")
@ViewScoped
public class Accion extends IBaseFilter implements Serializable {

  private static final long serialVersionUID = 327393488565639367L;
	private Producto producto;
  private String path;

  public Producto getProducto() {
    return producto;
  }

  public void setProducto(Producto producto) {
    this.producto = producto;
  }

	public String getPath() {
    return path;
  }
  
  @PostConstruct
  @Override
  protected void init() {		
    try {
     if(JsfBase.getFlashAttribute("accion")== null)
				UIBackingUtilities.execute("janal.isPostBack('cancelar')");
      this.attrs.put("isMatriz", JsfBase.getAutentifica().getEmpresa().isMatriz());
      this.attrs.put("accion", JsfBase.getFlashAttribute("accion")== null? EAccion.AGREGAR: JsfBase.getFlashAttribute("accion"));
      this.attrs.put("idProducto", JsfBase.getFlashAttribute("idProducto")== null? -1L: JsfBase.getFlashAttribute("idProducto"));
			this.attrs.put("retorno", JsfBase.getFlashAttribute("retorno"));
      this.attrs.put("codigo", "");
      this.attrs.put("buscaPorCodigo", false);
  		this.attrs.put("total", 0);
      String dns= Configuracion.getInstance().getPropiedadServidor("sistema.dns");
      this.path = dns.substring(0, dns.lastIndexOf("/")+ 1).concat(Configuracion.getInstance().getEtapaServidor().name().toLowerCase()).concat("/galeria/");      
      this.toLoadCatalog();
			this.doLoad();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

  @Override
  public void doLoad() {
    EAccion eaccion= null;
    try {
      eaccion= (EAccion) this.attrs.get("accion");
      this.attrs.put("nombreAccion", Cadena.letraCapital(eaccion.name()));
      switch (eaccion) {
        case AGREGAR:											
          this.producto= new Producto();
          break;
        case MODIFICAR:					
        case CONSULTAR:					
          this.producto= new Producto((Long)this.attrs.get("idProducto"));
          break;
      } // switch
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
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
      List<UISelectItem> categorias= UISelect.seleccioneFree("TcManticProductosDto", "categorias", params, "categoria", "|", EFormatoDinamicos.MAYUSCULAS, "categoria");
      this.attrs.put("categorias", categorias);			
      List<UISelectItem> marcas= UISelect.seleccioneFree("TcManticProductosDto", "marcas", params, "marca", "|", EFormatoDinamicos.MAYUSCULAS, "marca");
      if(marcas!= null)
        marcas.add(new UISelectItem("OTRA", "OTRA"));
      this.attrs.put("marcas", marcas);			
    } // try
    catch (Exception e) {
      throw e;
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    }// finally
	}
    
  public String doAceptar() {  
    Transaccion transaccion= null;
    String regresar        = null;
		EAccion eaccion        = null;
    try {			
			eaccion= (EAccion) this.attrs.get("accion");
			transaccion = new Transaccion(this.producto);
			if (transaccion.ejecutar(eaccion)) {
				regresar = ((String)this.attrs.get("retorno")).concat(Constantes.REDIRECIONAR);
				JsfBase.addMessage("Se ".concat(eaccion.equals(EAccion.AGREGAR) ? "agregó" : "modificó").concat(" el registro del producto."), ETipoMensaje.INFORMACION);
			} // if
			else 
				JsfBase.addMessage("Ocurrió un error al registrar el producto", ETipoMensaje.ERROR);      			
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    return regresar;
  } // doAccion

  public String doCancelar() {   
		JsfBase.setFlashAttribute("idProducto", this.producto.getProducto().getIdProducto());
    return ((String)this.attrs.get("retorno")).concat(Constantes.REDIRECIONAR);
  } // doAccion

	public void doLoadPartidas() {
		List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
		boolean buscaPorCodigo    = false;
    try {
			columns= new ArrayList<>();
      columns.add(new Columna("propio", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
			params.put("idAlmacen", JsfBase.getAutentifica().getEmpresa().getIdAlmacen());
  		params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
  		params.put("idProveedor", -1L);
			String search= (String)this.attrs.get("codigo"); 
			if(!Cadena.isVacio(search)) {
  			search= search.replaceAll(Constantes.CLEAN_SQL, "").trim();
				buscaPorCodigo= search.startsWith(".");
				if(buscaPorCodigo)
					search= search.trim().substring(1);
				search= search.toUpperCase().replaceAll("(,| |\\t)+", ".*.*");
			} // if	
			else
				search= "WXYZ";
  		params.put("codigo", search);
			if((boolean)this.attrs.get("buscaPorCodigo") || buscaPorCodigo)
        this.lazyModel= new FormatCustomLazy("VistaOrdenesComprasDto", "porCodigo", params, columns);
			else
        this.lazyModel= new FormatCustomLazy("VistaOrdenesComprasDto", "porNombre", params, columns);
      UIBackingUtilities.resetDataTable("encontrados");
		} // try
	  catch (Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    }// finally    
	}
 
	public void doAgregarPartida(Entity articulo) {
		try {
      Partida partida= new Partida(articulo.toLong("idArticulo"), articulo.toString("codigo"), articulo.toString("propio"), articulo.toString("nombre"), articulo.toLong("idImagen"), articulo.toString("archivo"));
			if(this.producto.getArticulos().indexOf(partida)>= 0) 
        this.attrs.put("existe", "<span class='janal-color-orange'>EL ARTICULO YA ESTA EN LA LISTA</span>");
      this.producto.addPartida(partida);
			UIBackingUtilities.execute("jsKardex.cursor.top= "+ (this.producto.getArticulos().size()- 1)+"; jsKardex.callback("+ articulo+");");
			this.attrs.put("total", this.producto.getArticulos().size());
	  } // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
	}

	public void doEliminarPartida(Partida partida) {
		try {
      this.producto.removePartida(partida);
	  } // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
		this.attrs.put("total", this.producto.getArticulos().size());
	}
  
  public void doRecuperarPartida(Partida partida) {
 		try {
      this.producto.doRecoverPartida(partida);
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
  } 
  
	public void doSubirPartida(Partida partida) {
		try {
      this.producto.upPartida(partida);
	  } // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
	}
  
	public void doBajarPartida(Partida partida) {
		try {
      this.producto.downPartida(partida);
	  } // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
	}
  
  public String toColorPartida(Partida partida) {
    return ""; // Objects.equals(partida.getAction(), ESql.DELETE)? "janal-display-none": "";
  }
  
  public String toColorCaracteristica(Caracteristica caracteristica) {
    return ""; // Objects.equals(caracteristica.getAction(), ESql.DELETE)? "janal-display-none": "";
  }

	public void doAgregarCaracteristica() {
		try {
      this.producto.addCaracteristica(new Caracteristica("CARACTERISTICA_"+ (this.producto.getCaracteristicas().size()+ 1)));
	  } // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
	}
  
	public void doEliminarCaracteristica(Caracteristica caracteristica) {
		try {
      this.producto.removeCaracteristica(caracteristica);
	  } // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
	}
 
  public void doRecuperarCaracteristica(Caracteristica caracteristica) {
 		try {
      this.producto.doRecoverCaracteristica(caracteristica);
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
  } 
  
  
  public void doSubirCaracteristica(Caracteristica caracteristica) {
		try {
      this.producto.upCaracteristica(caracteristica); 
	  } // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
	}

  public void doBajarCaracteristica(Caracteristica caracteristica) {
		try {
      this.producto.downCaracteristica(caracteristica);  
	  } // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
	}

  public void doUpdatePrinicipal(Partida partida) {
 		try {
      this.producto.toUpdatePrincipal(partida);
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
  }
 
  public void doConcatCategoria() {
    StringBuilder sb= new StringBuilder();
    if(this.getProducto().getIkCategoria()!= null && !Objects.equals(this.getProducto().getIkCategoria(), "-1"))
      sb.append(this.getProducto().getIkCategoria().trim());
    if(this.getProducto().getProducto().getCategoria()!= null && !Cadena.isVacio(this.getProducto().getProducto().getCategoria().trim())) {
      String categoria= this.getProducto().getProducto().getCategoria().trim().toUpperCase();
      String completo = Constantes.SEPARADOR.concat(sb.toString()).concat(Constantes.SEPARADOR);
      if(categoria.startsWith(Constantes.SEPARADOR))
        this.getProducto().getProducto().setCategoria(categoria.substring(1));
      if(categoria.endsWith(Constantes.SEPARADOR))
        this.getProducto().getProducto().setCategoria(this.getProducto().getProducto().getCategoria().substring(0, this.getProducto().getProducto().getCategoria().length()- 1));
      String contiene= this.getProducto().getProducto().getCategoria().trim().toUpperCase();
      if(sb.length()> 0 && !completo.contains(Constantes.SEPARADOR.concat(contiene).concat(Constantes.SEPARADOR)))
        sb.append(Constantes.SEPARADOR).append(contiene);
      else
        this.getProducto().getProducto().setCategoria("");        
    } // if  
    this.getProducto().setCategoria(sb.toString());
  }
  
  public void doConcatMarca() {
    if(this.getProducto().getIkMarca()!= null && !Objects.equals(this.getProducto().getIkMarca(), "-1"))
      this.getProducto().getProducto().setMarca(this.getProducto().getIkMarca().trim());
    else
      if(this.getProducto().getProducto().getMarca()!= null && !Cadena.isVacio(this.getProducto().getProducto().getMarca()))
        this.getProducto().getProducto().setMarca(this.getProducto().getProducto().getMarca().trim().toUpperCase());
    this.getProducto().setMarca(this.getProducto().getProducto().getMarca());
  }
  
}