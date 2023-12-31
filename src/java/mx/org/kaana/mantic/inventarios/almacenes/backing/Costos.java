package mx.org.kaana.mantic.inventarios.almacenes.backing;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ESql;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.pagina.IBaseFilter;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.inventarios.almacenes.beans.Utilidad;

@Named(value= "manticInventariosAlmacenesCostos")
@ViewScoped
public class Costos extends IBaseFilter implements Serializable {

  private static final long serialVersionUID = 8793667741599428332L;
	
  private List<Utilidad> articulos;

  public List<Utilidad> getArticulos() {
    return articulos;
  }

  public void setArticulos(List<Utilidad> articulos) {
    this.articulos = articulos;
  }
  
  @PostConstruct
  @Override
  protected void init() {
    try {
      this.attrs.put("isMatriz", JsfBase.getAutentifica().getEmpresa().isMatriz());
			this.attrs.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
			this.attrs.put("utilidad", 100D);
			this.toLoadCatalog();      
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init
 
  @Override
  public void doLoad() {
		Map<String, Object> params= this.toPrepare();
    try {
      Double utilidad= Objects.equals((Double)this.attrs.get("utilidad"), null)? 0D: (Double)this.attrs.get("utilidad");
      if(utilidad<= 0)
			  params.put("sortOrder", "order by (tc_mantic_articulos.menudeo- (tc_mantic_articulos.precio* 1.16))* 100/ (tc_mantic_articulos.precio* 1.16) asc");
      else
			  params.put("sortOrder", "order by (tc_mantic_articulos.menudeo- (tc_mantic_articulos.precio* 1.16))* 100/ (tc_mantic_articulos.precio* 1.16) desc");
      this.articulos = (List<Utilidad>)DaoFactory.getInstance().toEntitySet(Utilidad.class, "VistaConsultasDto", "costos", params, new Long(Constantes.REGISTROS_TOPE_PAGINA));
      UIBackingUtilities.resetDataTable();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    finally {
      Methods.clean(params);
    } // finally		
  } // doLoad
	
	protected Map<String, Object> toPrepare() {
	  Map<String, Object> regresar= new HashMap<>();	
		StringBuilder sb= new StringBuilder();
	  UISelectEntity codigo         = (UISelectEntity)this.attrs.get("codigo");
		List<UISelectEntity>codigos   = (List<UISelectEntity>)this.attrs.get("codigos");
	  UISelectEntity articulo       = (UISelectEntity)this.attrs.get("articulo");
		List<UISelectEntity>articulos = (List<UISelectEntity>)this.attrs.get("articulos");
	  UISelectEntity proveedor      = (UISelectEntity)this.attrs.get("proveedor");
		List<UISelectEntity>provedores= (List<UISelectEntity>)this.attrs.get("proveedores");
  	if(!Cadena.isVacio(this.attrs.get("consecutivo")))
  		sb.append("(tc_mantic_notas_entradas.consecutivo like '%").append(this.attrs.get("consecutivo")).append("%') and ");
		
		if(codigos!= null && codigo!= null && codigos.indexOf(codigo)>= 0) 
			sb.append("(tc_mantic_articulos_codigos.id_articulo= ").append(codigo.getKey()).append(") and ");			
		else 
			if(!Cadena.isVacio(JsfBase.getParametro("codigo_input")))
  			sb.append("(upper(tc_mantic_articulos_codigos.codigo) like upper('%").append(JsfBase.getParametro("codigo_input")).append("%')) and");									
		
		if(articulos!= null && articulo!= null && articulos.indexOf(articulo)>= 0) 
			sb.append("(tc_mantic_articulos.id_articulo= ").append(articulo.getKey()).append(") and ");			
		else 
			if(!Cadena.isVacio(JsfBase.getParametro("articulo_input")))
  			sb.append("(upper(tc_mantic_articulos.nombre) like upper('%").append(JsfBase.getParametro("articulo_input")).append("%')) and");									
		
		if(provedores!= null && proveedor!= null && provedores.indexOf(proveedor)>= 0) 
			sb.append("(tc_mantic_proveedores.id_proveedor= ").append(proveedor.getKey()).append(") and ");			
		else 
			if(!Cadena.isVacio(JsfBase.getParametro("proveedor_input")))
  			sb.append("(upper(tc_mantic_proveedores.razon_social) like upper('%").append(JsfBase.getParametro("proveedor_input")).append("%')").append(" or upper(tc_mantic_proveedores.rfc) like upper('%").append(JsfBase.getParametro("proveedor_input")).append("%') or upper(tc_mantic_proveedores.clave) like upper('%").append(JsfBase.getParametro("proveedor_input")).append("%')) and");									
		
		if(!Cadena.isVacio(this.attrs.get("fechaInicio")))
		  sb.append("(date_format(tc_mantic_notas_entradas.registro, '%Y%m%d')>= '").append(Fecha.formatear(Fecha.FECHA_ESTANDAR, (Date)this.attrs.get("fechaInicio"))).append("') and");
		if(!Cadena.isVacio(this.attrs.get("fechaTermino")))
		  sb.append("(date_format(tc_mantic_notas_entradas.registro, '%Y%m%d')<= '").append(Fecha.formatear(Fecha.FECHA_ESTANDAR, (Date)this.attrs.get("fechaTermino"))).append("') and");
    
    Double utilidad= Objects.equals((Double)this.attrs.get("utilidad"), null)? 0D: (Double)this.attrs.get("utilidad");
    if(utilidad<= 0)
      sb.append("((tc_mantic_articulos.menudeo- (tc_mantic_articulos.precio* 1.16))* 100/ (tc_mantic_articulos.precio* 1.16)<= ").append(utilidad).append(") and");
    else
      sb.append("((tc_mantic_articulos.menudeo- (tc_mantic_articulos.precio* 1.16))* 100/ (tc_mantic_articulos.precio* 1.16)>= ").append(utilidad).append(") and");
		if(!Cadena.isVacio(this.attrs.get("idEmpresa")) && !this.attrs.get("idEmpresa").toString().equals("-1"))
		  regresar.put("idEmpresa", this.attrs.get("idEmpresa"));
		else
		  regresar.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getSucursales());
		if(sb.length()== 0)
		  regresar.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
		else	
		  regresar.put(Constantes.SQL_CONDICION, sb.substring(0, sb.length()- 4));
		return regresar;		
	} // toPrepare
	
	protected void toLoadCatalog() {
		List<Columna> columns     = new ArrayList<>();
    Map<String, Object> params= new HashMap<>();
    try {
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
    } // finally
	}

	public List<UISelectEntity> doCompleteCodigo(String query) {
		List<Columna> columns       = new ArrayList<>();
    Map<String, Object> params  = new HashMap<>();
		List<UISelectEntity> codigos= null;
    try {
      columns.add(new Columna("propio", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
			String search= query; 
			search= !Cadena.isVacio(search) ? search.toUpperCase().replaceAll(Constantes.CLEAN_SQL, "").trim(): "WXYZ";
			params.put("idAlmacen", JsfBase.getAutentifica().getEmpresa().getIdAlmacen());
			if(!Cadena.isVacio(this.attrs.get("idEmpresa")) && !this.attrs.get("idEmpresa").toString().equals("-1"))
				params.put("sucursales", this.attrs.get("idEmpresa"));
			else
				params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
  		params.put("idProveedor", -1L);			
  		params.put("codigo", search);			
			codigos= (List<UISelectEntity>) UIEntity.build("VistaOrdenesComprasDto", "porCodigo", params, columns, 20L);
      this.attrs.put("codigos", codigos);
		} // try
	  catch (Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    } // finally
		return (List<UISelectEntity>)codigos;
	}	// doCompleteCodigo

	public List<UISelectEntity> doCompleteArticulo(String query) {
		List<Columna> columns     = new ArrayList<>();
    Map<String, Object> params= new HashMap<>();
		List<UISelectEntity> items= null;
    try {
      columns.add(new Columna("propio", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
  		params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
  		params.put("idProveedor", -1L);
			String search= query; 
			if(!Cadena.isVacio(search)) {
  			search= search.replaceAll(Constantes.CLEAN_SQL, "").trim().toUpperCase().replaceAll("(,| |\\t)+", ".*");			
        if(Cadena.isVacio(search))
          search= "WXYZ";        
      } // if  
			else
				search= "WXYZ";
  		params.put("codigo", search);			        
      items= (List<UISelectEntity>) UIEntity.build("VistaOrdenesComprasDto", "porNombreTipoArticulo", params, columns, 40L);
      this.attrs.put("articulos", items);
		} // try
	  catch (Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    }// finally
		return (List<UISelectEntity>)items;
	}	// doCompleteArticulo
	
	public List<UISelectEntity> doCompleteProveedor(String query) {
 		List<Columna> columns     = new ArrayList<>();
    Map<String, Object> params= new HashMap<>();
		boolean buscaPorCodigo    = Boolean.FALSE;
    try {
      columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("rfc", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("razonSocial", EFormatoDinamicos.MAYUSCULAS));
			params.put("idAlmacen", JsfBase.getAutentifica().getEmpresa().getIdAlmacen());
  		params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
			String search= query; 
			if(!Cadena.isVacio(search)) {
  			search= search.replaceAll(Constantes.CLEAN_SQL, "").trim();
				buscaPorCodigo= search.startsWith(".");
				if(buscaPorCodigo)
					search= search.trim().substring(1);
				search= search.toUpperCase().replaceAll("(,| |\\t)+", ".*");
			} // if	
			else
				search= "WXYZ";
  		params.put("codigo", search);
			if(buscaPorCodigo)
        this.attrs.put("proveedores", UIEntity.build("TcManticProveedoresDto", "porCodigo", params, columns, 40L));
			else
        this.attrs.put("proveedores", UIEntity.build("TcManticProveedoresDto", "porNombre", params, columns, 40L));
		} // try
	  catch (Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    }// finally
		return (List<UISelectEntity>)this.attrs.get("proveedores");
	}	

  public void doChangeRowCostoMenudeo(Utilidad row) {
    row.setSql(ESql.UPDATE);
    row.setPorcentajeMenudeo((row.getMenudeo()- (row.getPrecio()* 1.16))* 100/ (row.getPrecio()* 1.16));
  }
  
  public void doChangeRowCostoMedioMayoreo(Utilidad row) {
    row.setSql(ESql.UPDATE);
    row.setPorcentajeMedioMayoreo((row.getMedioMayoreo()- (row.getPrecio()* 1.16))* 100/ (row.getPrecio()* 1.16));
  }
  
  public void doChangeRowCostoMayoreo(Utilidad row) {
    row.setSql(ESql.UPDATE);
    row.setPorcentajeMayoreo((row.getMayoreo()- (row.getPrecio()* 1.16))* 100/ (row.getPrecio()* 1.16));
  }
  
  public void doChangeRowPorcentajeMenudeo(Utilidad row) {
    row.setSql(ESql.UPDATE);
    row.setMenudeo(((row.getPorcentajeMenudeo()/100)+1)* (row.getPrecio()* 1.16));
  }
  
  public void doChangeRowPorcentajeMedioMayoreo(Utilidad row) {
    row.setSql(ESql.UPDATE);
    row.setMedioMayoreo(((row.getPorcentajeMedioMayoreo()/100)+1)* (row.getPrecio()* 1.16));
  }
  
  public void doChangeRowPorcentajeMayoreo(Utilidad row) {
    row.setSql(ESql.UPDATE);
    row.setMayoreo(((row.getPorcentajeMayoreo()/100)+ 1)* (row.getPrecio()* 1.16));
  }
  
  public void doUpdateChanges(Utilidad row) {
    try {      
      if(Objects.equals(row.getSql(), ESql.UPDATE)) {
        Boolean regresar= DaoFactory.getInstance().update(row)> 0L;
        if(regresar) {
          JsfBase.addMessage("Info", "Se actualizó el articulo de forma correcta !");
          row.setSql(ESql.SELECT);
        } // if  
        else
          JsfBase.addMessage("Error", "No se actualizó el articulo, intente de nuevo");
      } // if  
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);      
    } // catch	
  }
  
}
