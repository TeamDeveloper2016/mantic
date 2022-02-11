package mx.org.kaana.mantic.productos.backing;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.pagina.IBaseFilter;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.iva.reglas.Transaccion;
import mx.org.kaana.mantic.db.dto.TcManticHistorialIvaDto;

@Named(value = "manticProductosFiltro")
@ViewScoped
public class Filtro extends IBaseFilter implements Serializable {

  private static final long serialVersionUID = 8793667741599428369L;

  @PostConstruct
  @Override
  protected void init() {
    try {
      this.attrs.put("isMatriz", JsfBase.getAutentifica().getEmpresa().isMatriz());
			this.attrs.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
      this.attrs.put("idActivo", -1L);
      this.attrs.put("idProducto", JsfBase.getFlashAttribute("idProducto"));
      this.toLoadCatalog();
      if(!Cadena.isVacio(this.attrs.get("idProducto")))
        this.doLoad();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init
 
  @Override
  public void doLoad() {
    List<Columna> columns     = null;
    Map<String, Object> params= this.toPrepare();
    try {
      params.put("sortOrder", "order by concat(tc_mantic_productos_categorias.padre, tc_mantic_productos_categorias.nombre), tc_mantic_productos.orden");
      columns = new ArrayList<>();
      columns.add(new Columna("categoria", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("marca", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("articulos", EFormatoDinamicos.NUMERO_SIN_DECIMALES));
      columns.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA));      
      this.lazyModel = new FormatCustomLazy("VistaProductosDto", params, columns);
      UIBackingUtilities.resetDataTable();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    finally {
      Methods.clean(columns);
    } // finally		
  } // doLoad

	private Map<String, Object> toPrepare() {
	  Map<String, Object> regresar= new HashMap<>();	
		StringBuilder sb= new StringBuilder();
		if(!Cadena.isVacio(this.attrs.get("idProducto")) && !this.attrs.get("idProducto").toString().equals("-1"))
  		sb.append("(tc_mantic_productos.id_producto= ").append(this.attrs.get("idProducto")).append(") and ");
		if(!Cadena.isVacio(this.attrs.get("categoria")))
  		sb.append("(concat(tc_mantic_productos_categorias.padre, tc_mantic_productos_categorias.nombre) like '%").append(this.attrs.get("categoria")).append("%') and ");
		if(!Cadena.isVacio(this.attrs.get("marca")))
  		sb.append("(tc_mantic_productos_marcas.nombre like '%").append(this.attrs.get("marca")).append("%') and ");
		if(!Cadena.isVacio(this.attrs.get("nombre")))
  		sb.append("(tc_mantic_productos.nombre like '%").append(this.attrs.get("nombre")).append("%') and ");
		if(!Cadena.isVacio(this.attrs.get("fechaInicio")))
		  sb.append("(date_format(tc_mantic_productos.registro, '%Y%m%d')>= '").append(Fecha.formatear(Fecha.FECHA_ESTANDAR, (Date)this.attrs.get("fechaInicio"))).append("') and ");	
		if(!Cadena.isVacio(this.attrs.get("fechaTermino")))
		  sb.append("(date_format(tc_mantic_productos.registro, '%Y%m%d')<= '").append(Fecha.formatear(Fecha.FECHA_ESTANDAR, (Date)this.attrs.get("fechaTermino"))).append("') and ");	
		if(!Cadena.isVacio(this.attrs.get("idActivo")) && !this.attrs.get("idActivo").toString().equals("-1"))
  		sb.append("(tc_mantic_productos.id_activo= ").append(this.attrs.get("idActivo")).append(") and ");
		if(!Cadena.isVacio(this.attrs.get("idEmpresa")) && !this.attrs.get("idEmpresa").toString().equals("-1"))
		  regresar.put("idEmpresa", this.attrs.get("idEmpresa"));
		else
		  regresar.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getSucursales());
		if(sb.length()> 0) 
		  regresar.put(Constantes.SQL_CONDICION, sb.substring(0, sb.length()- 4));
    else
		  regresar.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
    this.attrs.put("idProducto", null);
		return regresar;		
	}
  
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
      this.attrs.put("sucursales", (List<UISelectEntity>) UIEntity.build("TcManticEmpresasDto", "empresas", params, columns));			
			this.attrs.put("idEmpresa", this.toDefaultSucursal((List<UISelectEntity>)this.attrs.get("sucursales")));
    } // try
    catch (Exception e) {
      throw e;
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    }// finally
	}
  
  public String doAccion(String accion) {
    EAccion eaccion= null;
		try {
			eaccion= EAccion.valueOf(accion.toUpperCase());
			JsfBase.setFlashAttribute("accion", eaccion);		
			JsfBase.setFlashAttribute("retorno", "/Paginas/Mantic/Productos/filtro");		
			JsfBase.setFlashAttribute("idProducto", (eaccion.equals(EAccion.MODIFICAR) || eaccion.equals(EAccion.CONSULTAR)) ? ((Entity)this.attrs.get("seleccionado")).getKey() : -1L);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch
		return "/Paginas/Mantic/Productos/accion".concat(Constantes.REDIRECIONAR);
  } // doAccion  
	
  public String doOrdenar() {
		try {
			JsfBase.setFlashAttribute("retorno", "/Paginas/Mantic/Productos/filtro");		
			JsfBase.setFlashAttribute("producto", this.attrs.get("seleccionado"));
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch
		return "/Paginas/Mantic/Productos/ordena".concat(Constantes.REDIRECIONAR);
  } // doOrdenar
	
  public void doEliminar() {
		Transaccion transaccion = null;
		Entity seleccionado     = null;
		try {
			seleccionado= (Entity) this.attrs.get("seleccionado");			
			transaccion= new Transaccion(new TcManticHistorialIvaDto(seleccionado.getKey()));
			if(transaccion.ejecutar(EAccion.ELIMINAR))
				JsfBase.addMessage("Eliminar", "El productos se ha eliminado correctamente.", ETipoMensaje.ERROR);
			else
				JsfBase.addMessage("Eliminar", "Ocurrió un error al eliminar el registro del producto.", ETipoMensaje.ERROR);								
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch			
  } // doEliminar	
 
  public String doGaleria() {
		try {
			JsfBase.setFlashAttribute("retorno", "/Paginas/Mantic/Productos/filtro");		
			JsfBase.setFlashAttribute("producto", this.attrs.get("seleccionado"));
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch
		return "/Paginas/Mantic/Productos/galeria".concat(Constantes.REDIRECIONAR);
  } // doGaleria
  
}