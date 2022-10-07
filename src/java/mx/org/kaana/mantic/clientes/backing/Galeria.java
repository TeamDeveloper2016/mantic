package mx.org.kaana.mantic.clientes.backing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.control.backing.BaseMenu;
import mx.org.kaana.kajool.control.enums.EBusqueda;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
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
@Named(value = "manticClientesGaleria")
public class Galeria extends BaseMenu implements Serializable {

  private static final Log LOG = LogFactory.getLog(Galeria.class);
  private static final long serialVersionUID = 5323749709626263802L;

  private String codigo;
  private String categoria;
  private EBusqueda busqueda;
  private String pathImage;
  private Entity cliente;
  
  
	public String getPathImage() {
		return pathImage;
	}
  
  public String getHoy() {
    return Fecha.getHoyCorreo().concat("; ").concat(Fecha.getHoraExtendida());
  }
  
  @Override
  @PostConstruct
  protected void init() {
    super.init();
    try {
			this.attrs.put("isGerente", JsfBase.isAdminEncuestaOrAdmin());
      this.codigo = JsfBase.getFlashAttribute("codigo")== null? "": (String)JsfBase.getFlashAttribute("codigo");
      this.categoria= JsfBase.getFlashAttribute("categoria")== null? "": (String)JsfBase.getFlashAttribute("categoria");
      this.busqueda = JsfBase.getFlashAttribute("busqueda")== null? EBusqueda.CATEGORIA: (EBusqueda)JsfBase.getFlashAttribute("busqueda");
      String dns= Configuracion.getInstance().getPropiedadServidor("sistema.dns");
      this.pathImage= dns.substring(0, dns.lastIndexOf("/")+ 1).concat(Configuracion.getInstance().getEtapaServidor().name().toLowerCase()).concat("/galeria/");
      if(!Cadena.isVacio(this.codigo))
        this.doLoadArticulos();
      this.toLoadCatalog();
      this.doLoad();
		} // try
	  catch (Exception e) {
      Error.mensaje(e);
    } // catch   
  }

  @Override
  public void doLoad() {
    Map<String, Object> params = new HashMap<>();
    try {      
      if (!Cadena.isVacio(this.attrs.get("idCliente")) && !this.attrs.get("idCliente").toString().equals("-1")) 
        params.put("idUsuario", this.attrs.get("idCliente"));
      else 
        params.put("idUsuario", JsfBase.getIdUsuario());     
      this.cliente= (Entity)DaoFactory.getInstance().toEntity("VistaClientesRepresentantesDto", "cliente", params);
      if(this.cliente== null) {
        this.cliente= new Entity(JsfBase.getIdUsuario());
        this.cliente.put("especial", new Value("especial", -1D));
        this.cliente.put("idUsuario", new Value("idUsuario", -1L));
      } // if  
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);      
    } // catch	
    finally {
      Methods.clean(params);
    } // finally
  }
  
	private void toLoadCatalog() {
		List<Columna> columns     = new ArrayList<>();
    Map<String, Object> params= new HashMap<>();
    try {
			params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
      columns.add(new Columna("nombreCompleto", EFormatoDinamicos.MAYUSCULAS));
      this.attrs.put("clientes", (List<UISelectEntity>) UIEntity.build("VistaClientesRepresentantesDto", "todos", params, columns));
			this.attrs.put("idCliente", new UISelectEntity("-1"));
    } // try
    catch (Exception e) {
      throw e;
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    }// finally
	}
  
  public void doLoadArticulos() {
    List<Columna> columns     = new ArrayList<>();
		Map<String, Object> params= new HashMap<>();
    try {
      if (!Cadena.isVacio(this.attrs.get("idCliente")) && !this.attrs.get("idCliente").toString().equals("-1") && !Objects.equals(this.cliente.toLong("idUsuario"), new Long(this.attrs.get("idCliente").toString()))) {
        params.put("idUsuario", this.attrs.get("idCliente"));
        this.cliente= (Entity)DaoFactory.getInstance().toEntity("VistaClientesRepresentantesDto", "cliente", params);
      } // if
      else 
        if (!Cadena.isVacio(this.attrs.get("idCliente")) && this.attrs.get("idCliente").toString().equals("-1") && !Objects.equals(this.cliente.toDouble("especial"), -1D)) {
          this.cliente.get("especial").setData(-1D);
          this.cliente.get("idUsuario").setData(-1L);
        } // if  
  		params.put("porcentaje", this.cliente.toDouble("especial"));
      columns.add(new Columna("propio", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("menudeo", EFormatoDinamicos.MILES_CON_DECIMALES));
      columns.add(new Columna("medioMayoreo", EFormatoDinamicos.MILES_CON_DECIMALES));
      columns.add(new Columna("mayoreo", EFormatoDinamicos.MILES_CON_DECIMALES));
      columns.add(new Columna("especial", EFormatoDinamicos.MILES_CON_DECIMALES));
      params.put("sortOrder", "order by tc_mantic_productos.nombre, tc_mantic_productos_detalles.orden");
      if(Cadena.isVacio(this.categoria))
    		params.put("codigo", this.codigo);		
      else 
    		params.put("codigo", this.categoria.toUpperCase().trim());		
      this.attrs.put("subs", null);
      if(!Cadena.isVacio(codigo)) {
        this.lazyModel= new FormatCustomLazy("VistaOrdenesComprasDto", "exportar", params, columns);
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
    this.doLoadArticulos();
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
    this.toLoadSubcategoria(categoria);
    return regresar.toString();
  }
 
  public void doLoadProducto(String codigo, String busqueda) {
    this.codigo   = codigo;
    this.busqueda= EBusqueda.valueOf(busqueda);
    this.doLoadArticulos();
  }
  
  public void toLoadSubcategoria(String categoria) {
    List<Columna> columns     = new ArrayList<>();
		Map<String, Object> params= new HashMap<>();
    StringBuilder regresar    = new StringBuilder();
    try {
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      params.put("categoria", categoria);
      List<Entity> items= (List<Entity>)DaoFactory.getInstance().toEntitySet("VistaProductosDto", "hijos", params);
      if(items!= null && items.size()> 0) {
        regresar.append(" <br></br><span>Subcategorías: </span>");
        for (Entity item: items) {
          regresar.append("[ <span class=\"ui-panel-title Fs16\"><a onclick=\"busquedaCategoria('").append(categoria).append(Constantes.SEPARADOR).append(item.toString("nombre")).
                   append("');\" class=\"janal-move-element\" style=\"color: orange; cursor:pointer;\">").
                   append(Cadena.letraCapital(item.toString("nombre")).trim()).append("</a> ]</span>").append("<span class=\"ui-panel-title janal-color-black Fs18\">   »   </span>");
        } // for
        regresar.delete(regresar.length()- 66, regresar.length());
        this.attrs.put("subs", regresar.toString());
      } // if
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
  
}