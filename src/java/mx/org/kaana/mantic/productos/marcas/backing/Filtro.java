package mx.org.kaana.mantic.productos.marcas.backing;

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
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.articulos.beans.Importado;
import mx.org.kaana.mantic.productos.marcas.reglas.Transaccion;
import mx.org.kaana.mantic.productos.marcas.beans.Marca;

@Named(value = "manticProductosMarcasFiltro")
@ViewScoped
public class Filtro extends IBaseFilter implements Serializable {

  private static final long serialVersionUID = 8793667741599428369L;

  private String path;
  
	public String getPath() {
    return path;
  }
  
  @PostConstruct
  @Override
  protected void init() {
    try {
      this.attrs.put("nombre", "");
      this.attrs.put("descripcion", "");
      this.attrs.put("idProductoMarca", JsfBase.getFlashAttribute("idProductoMarca"));
      if(!Cadena.isVacio(this.attrs.get("idProductoMarca")))
        this.doLoad();
      String dns= Configuracion.getInstance().getPropiedadServidor("sistema.dns");
      this.path = dns.substring(0, dns.lastIndexOf("/")+ 1).concat(Configuracion.getInstance().getEtapaServidor().name().toLowerCase()).concat("/galeria/").concat(String.valueOf(JsfBase.getAutentifica().getEmpresa().getIdEmpresaDepende())).concat("/marcas/");      
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init
 
  @Override
  public void doLoad() {
    List<Columna> columns     = null;
		Map<String, Object> params= null;
		try {
			params= this.toPrepare();
      params.put("sortOrder", "order by tc_mantic_productos_marcas.nombre");
      columns = new ArrayList<>();
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("descripcion", EFormatoDinamicos.MAYUSCULAS));      
      columns.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA));      
      this.lazyModel = new FormatCustomLazy("TcManticProductosMarcasDto", "lazy", params, columns);
      UIBackingUtilities.resetDataTable();
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

	private Map<String, Object> toPrepare() {
	  Map<String, Object> regresar= new HashMap<>();	
		StringBuilder sb= new StringBuilder();
		if(!Cadena.isVacio(this.attrs.get("idProductoMarca")) && !this.attrs.get("idProductoMarca").toString().equals("-1"))
  		sb.append("(tc_mantic_productos_marcas.id_producto_marca= ").append(this.attrs.get("idProductoMarca")).append(") and ");
		if(!Cadena.isVacio(this.attrs.get("nombre")))
  		sb.append("(tc_mantic_productos_marcas.nombre like '%").append(this.attrs.get("nombre")).append("%') and ");
		if(!Cadena.isVacio(this.attrs.get("descripcion")))
  		sb.append("(tc_mantic_productos_marcas.descripcion like '%").append(this.attrs.get("descripcion")).append("%') and ");
		if(sb.length()> 0) 
		  regresar.put(Constantes.SQL_CONDICION, sb.substring(0, sb.length()- 4));
    else
		  regresar.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
    this.attrs.put("idProductoMarca", null);
		return regresar;		
	}
  
  public String doAccion(String accion) {
    EAccion eaccion= null;
		try {
			eaccion= EAccion.valueOf(accion.toUpperCase());
			JsfBase.setFlashAttribute("accion", eaccion);		
			JsfBase.setFlashAttribute("retorno", "/Paginas/Mantic/Productos/Marcas/filtro");		
			JsfBase.setFlashAttribute("idProductoMarca", (eaccion.equals(EAccion.MODIFICAR) || eaccion.equals(EAccion.CONSULTAR)) ? ((Entity)this.attrs.get("seleccionado")).getKey() : -1L);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch
		return "/Paginas/Mantic/Productos/Marcas/accion".concat(Constantes.REDIRECIONAR);
  } // doAccion  
	
  public void doEliminar() {
		Transaccion transaccion= null;
		Entity seleccionado    = null;
		Marca marca            = null;
		Map<String, Object> params= null;
		try {
			params=new HashMap<>();
			seleccionado= (Entity) this.attrs.get("seleccionado");			
      params.put("idProductoMarca", seleccionado.getKey());
      marca= (Marca)DaoFactory.getInstance().toEntity(Marca.class, "TcManticProductosMarcasDto", "existe", params);
      if(marca!= null) {
        if(marca.getIdProductoMarcaArchivo()!= null) {
          params.put("idProductoMarcaArchivo", marca.getIdProductoMarcaArchivo());
          marca.setImportado((Importado)DaoFactory.getInstance().toEntity(Importado.class, "TcManticProductosMarcasArchivosDto", "igual", params));
        } // if  
        transaccion= new Transaccion(marca);
        if(transaccion.ejecutar(EAccion.ELIMINAR))
          JsfBase.addMessage("Eliminar", "La marca se ha eliminado correctamente.", ETipoMensaje.ERROR);
        else
          JsfBase.addMessage("Eliminar", "Ocurrió un error al eliminar la marca.", ETipoMensaje.ERROR);								
      } // if  
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch			
    finally {
      Methods.clean(params);
    } // finally
  } // doEliminar	
  
}