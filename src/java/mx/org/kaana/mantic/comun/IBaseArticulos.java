package mx.org.kaana.mantic.comun;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.formato.Global;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.mantic.compras.ordenes.beans.Articulo;
import mx.org.kaana.mantic.db.dto.TrManticArticuloPrecioSugeridoDto;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.primefaces.context.RequestContext;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 18/05/2018
 *@time 07:29:56 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public abstract class IBaseArticulos extends IBaseAttribute implements Serializable {

	private static final long serialVersionUID=-7378726801437171894L;
	private static final Log LOG=LogFactory.getLog(IBaseArticulos.class);
	
  private IAdminArticulos adminOrden;
	private String precio;

	public IBaseArticulos() {
		this("precio");
		this.attrs.put("filterName", "");
		this.attrs.put("filterCode", "");
	}

	public IBaseArticulos(String precio) {
		this.precio= precio;
	}
	
	public IAdminArticulos getAdminOrden() {
		return adminOrden;
	}

	public void setAdminOrden(IAdminArticulos adminOrden) {
		this.adminOrden=adminOrden;
	}

	public String getPrecio() {
		return precio;
	}

	public void setPrecio(String precio) {
		this.precio=precio;
	}

  protected void toMoveData(UISelectEntity articulo, Integer index) throws Exception {
		Articulo temporal= this.adminOrden.getArticulos().get(index);
		Map<String, Object> params= new HashMap<>();
		try {
			if(articulo.size()> 1) {
				this.doSearchArticulo(articulo.toLong("idArticulo"), index);
				params.put("idArticulo", articulo.toLong("idArticulo"));
				params.put("idProveedor", this.adminOrden.getIdProveedor());
				params.put("idAlmacen", this.adminOrden.getIdAlmacen());
				temporal.setKey(articulo.toLong("idArticulo"));
				temporal.setIdArticulo(articulo.toLong("idArticulo"));
				temporal.setIdProveedor(this.adminOrden.getIdProveedor());
				temporal.setIdRedondear(articulo.toLong("idRedondear"));
				Value codigo= (Value)DaoFactory.getInstance().toField("TcManticArticulosCodigosDto", "codigo", params, "codigo");
				temporal.setCodigo(codigo== null? "": codigo.toString());
				temporal.setPropio(articulo.toString("propio"));
				temporal.setNombre(articulo.toString("nombre"));
				temporal.setValor(articulo.toDouble(this.precio));
				temporal.setCosto(articulo.toDouble(this.precio));
				temporal.setIva(articulo.toDouble("iva"));
				temporal.setDescuento(this.adminOrden.getDescuento());
				temporal.setExtras(this.adminOrden.getExtras());
				temporal.setCantidad(1D);
				temporal.setUltimo(this.attrs.get("ultimo")!= null);
				temporal.setSolicitado(this.attrs.get("solicitado")!= null);
				Value stock= (Value)DaoFactory.getInstance().toField("TcManticInventariosDto", "stock", params, "stock");
				temporal.setStock(stock== null? 0L: stock.toLong());
				if(index== this.adminOrden.getArticulos().size()- 1) {
					this.adminOrden.getArticulos().add(new Articulo(-1L));
					RequestContext.getCurrentInstance().execute("jsArticulos.update("+ (this.adminOrden.getArticulos().size()- 1)+ ");");
				} // if	
				RequestContext.getCurrentInstance().execute("jsArticulos.callback('"+ articulo.toMap()+ "');");
				this.adminOrden.toCalculate();
			} // if	
			else
				temporal.setNombre("<span class='janal-color-orange'>EL ARTICULO NO EXISTE EN EL CATALOGO !</span>");
		} // try
		finally {
			Methods.clean(params);
		}
	}
	
	public void doUpdateArticulo(String codigo, Integer index) {
		List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
		List<UISelectEntity> articulos= null;
		boolean buscaPorCodigo    = false;
    try {
			columns= new ArrayList<>();
      columns.add(new Columna("propio", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
  		params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
  		params.put("idProveedor", this.attrs.get("proveedor")== null? new UISelectEntity(new Entity(-1L)): ((UISelectEntity)this.attrs.get("proveedor")).getKey());
			buscaPorCodigo= codigo.startsWith(".");
			if(buscaPorCodigo)
				codigo= codigo.trim().substring(1);
			codigo= codigo.toUpperCase().replaceAll("(,| |\\t)+", ".*.*");
			params.put("codigo", codigo);
			if((boolean)this.attrs.get("buscaPorCodigo") || buscaPorCodigo)
        articulos= (List<UISelectEntity>) UIEntity.build("VistaOrdenesComprasDto", "porCodigo", params, columns, 20L);
			else
        articulos= (List<UISelectEntity>) UIEntity.build("VistaOrdenesComprasDto", "porNombre", params, columns, 20L);
			UISelectEntity articulo= UIBackingUtilities.toFirstKeySelectEntity(articulos);
			if(articulo.size()> 1) {
				int position= this.getAdminOrden().getArticulos().indexOf(new Articulo(articulo.toLong("idArticulo")));
				if(articulo.size()> 1 && position>= 0) {
					if(index!= position)
						RequestContext.getCurrentInstance().execute("jsArticulos.exists("+ position+ ");");
				} // if	
				else
					this.toMoveData(articulo, index);
			} // if	
  		else
	  		this.toMoveData(articulo, index);
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

	public void doDeleteArticulo(Integer index) {
    try {
			if(this.adminOrden.getArticulos().size()== 1 || index.equals(this.adminOrden.getArticulos().size()- 1)) {			
				Articulo temporal= this.adminOrden.getArticulos().get(index.intValue());
				temporal.setKey(-1L);
				temporal.setIdArticulo(-1L);
				temporal.setIdProveedor(-1L);
				temporal.setCodigo("");
				temporal.setPropio("");
				temporal.setNombre("");
				temporal.setValor(0.0);
				temporal.setIva(0.0);
				temporal.setCantidad(0D);
			} // if
			else 
  			this.adminOrden.getArticulos().remove(index.intValue());
			this.adminOrden.toCalculate();
			RequestContext.getCurrentInstance().execute("jsArticulos.update("+ (this.adminOrden.getArticulos().size()- 1)+ ");");
		} // try
	  catch (Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
	}	

	public void doCalculate(Integer index) {
		this.adminOrden.toCalculate();
	}	

	public void doUpdatePorcentaje() {
		this.adminOrden.toUpdatePorcentajes();
	} 

	public void doUpdateIvaTipoDeCambio() {
		boolean sinIva= (boolean)this.attrs.get("sinIva");
		this.adminOrden.setIdSinIva(sinIva? 1L: 2L);
		this.adminOrden.toCalculate();
	} 
	
	public void doAdjustArticulos(Integer index) {
		this.adminOrden.toAdjustArticulos();
	}
	
	public void doSearchArticulo(Long idArticulo, Integer index) {
		this.doUpdateSolicitado(idArticulo);
		this.doUpdateInformacion(idArticulo);
		this.doUpdateSugeridos(idArticulo);
	}

	public void doUpdateSolicitado(Long idArticulo) {
		List<Columna> columns= null;
    try {
			if(idArticulo!= null)
  			this.attrs.put("idArticulo", idArticulo);
			columns= new ArrayList<>();
      columns.add(new Columna("razonSocial", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("moneda", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("costo", EFormatoDinamicos.MONEDA_CON_DECIMALES));
      columns.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA_CORTA));
			Entity solicitado= (Entity)DaoFactory.getInstance().toEntity("VistaOrdenesComprasDto", "solicitado", this.attrs);
      this.attrs.put("solicitado", solicitado!= null? UIBackingUtilities.toFormatEntity(solicitado, columns): null);
    } // try
    catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
    finally {
      Methods.clean(columns);
    }// finally
	}
	
	public void doUpdateInformacion(Long idArticulo) {
    try {
			if(idArticulo!= null)
			  this.attrs.put("idArticulo", idArticulo);
			Entity ultimoPrecio= (Entity)DaoFactory.getInstance().toEntity("VistaOrdenesComprasDto", "ultimo", this.attrs);
			if(ultimoPrecio!= null && !ultimoPrecio.isEmpty())
				ultimoPrecio.values().stream().map((value) -> {
					if("|costo|".indexOf(value.getName())> 0)
						value.setData(Numero.toRedondearSat(value.toDouble()));
					return value;
				}).filter((value) -> ("|registro|".indexOf(value.getName())> 0)).forEachOrdered((value) -> {
					value.setData(Global.format(EFormatoDinamicos.FECHA_HORA_CORTA, value.toTimestamp()));
				}); // for
 		  this.attrs.put("ultimo", ultimoPrecio);
    } // try
    catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
	}
	
	public void doUpdateSugeridos(Long idArticulo) {
		List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
    try {
			if(idArticulo!= null)
			  this.attrs.put("idArticulo", idArticulo);
			if(this.attrs.get("sugerido")!= null && ((UISelectEntity)this.attrs.get("sugerido")).getKey()!= -1L) {
			  TrManticArticuloPrecioSugeridoDto sugerido= new TrManticArticuloPrecioSugeridoDto(((UISelectEntity)this.attrs.get("sugerido")).getKey());
				params.put("idLeido", 1L);
				DaoFactory.getInstance().update(sugerido, params);
				this.attrs.put("sugerido", new UISelectEntity(new Entity(-1L)));
			} // if	
			columns= new ArrayList<>();
      columns.add(new Columna("proveedor", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("precio", EFormatoDinamicos.MONEDA_CON_DECIMALES));
      columns.add(new Columna("descuento", EFormatoDinamicos.NUMERO_CON_DECIMALES));
		  this.attrs.put("sugeridos", UIEntity.build("VistaOrdenesComprasDto", "sugerido", this.attrs, columns));
    } // try
    catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
    finally {
      Methods.clean(params);
      Methods.clean(columns);
    }// finally
	}

	public void doUpdateArticulos() {
		List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
		boolean buscaPorCodigo    = false;
    try {
			columns= new ArrayList<>();
      columns.add(new Columna("propio", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
  		params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
  		params.put("idProveedor", this.attrs.get("proveedor")== null? new UISelectEntity(new Entity(-1L)): ((UISelectEntity)this.attrs.get("proveedor")).getKey());
			String search= (String)this.attrs.get("codigo"); 
			if(!Cadena.isVacio(search)) {
				buscaPorCodigo= search.startsWith(".");
				if(buscaPorCodigo)
					search= search.trim().substring(1);
				search= search.toUpperCase().replaceAll("(,| |\\t)+", ".*.*");
			} // if	
			else
				search= "WXYZ";
  		params.put("codigo", search);
			if((boolean)this.attrs.get("buscaPorCodigo") || buscaPorCodigo)
        this.attrs.put("articulos", (List<UISelectEntity>) UIEntity.build("VistaOrdenesComprasDto", "porCodigo", params, columns, 20L));
			else
        this.attrs.put("articulos", (List<UISelectEntity>) UIEntity.build("VistaOrdenesComprasDto", "porNombre", params, columns, 20L));
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
 
  public void doFindArticulo(Integer index) {
		try {
    	List<UISelectEntity> articulos= (List<UISelectEntity>)this.attrs.get("articulos");
	    UISelectEntity articulo= (UISelectEntity)this.attrs.get("articulo");
			if(articulo== null)
			  articulo= new UISelectEntity(new Entity(-1L));
			else
				if(articulos.indexOf(articulo)>= 0) 
					articulo= articulos.get(articulos.indexOf(articulo));
			  else
			    articulo= articulos.get(0);
			if(articulo.size()> 1) {
				int position= this.getAdminOrden().getArticulos().indexOf(new Articulo(articulo.toLong("idArticulo")));
				if(articulo.size()> 1 && position>= 0) {
					if(index!= position)
						RequestContext.getCurrentInstance().execute("jsArticulos.exists("+ position+ ");");
				} // if	
				else
					this.toMoveData(articulo, index);
			} // else
			else 
					this.toMoveData(articulo, index);
		} // try
	  catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
	} 
	
	public void doRemoveArticulo() {
		try {
   		this.getAdminOrden().remove((Articulo)this.attrs.get("seleccionado"));
		} // try
	  catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
	}

	public void toAddArticulo(UISelectEntity seleccionado) throws Exception {
		Long idOrdenDetalle= new Long((int)(Math.random()*10000));
  	this.doSearchArticulo(seleccionado.toLong("idArticulo"), 0);
		Map<String, Object> params= null;
		Value codigo, stock       = null;
		try {
			params=new HashMap<>();
			params.put("idArticulo", seleccionado.toLong("idArticulo"));
			params.put("idProveedor", this.adminOrden.getIdProveedor());
			params.put("idAlmacen", this.adminOrden.getIdAlmacen());
			codigo= (Value)DaoFactory.getInstance().toField("TcManticArticulosCodigosDto", "codigo", params, "codigo");
  		stock = (Value)DaoFactory.getInstance().toField("TcManticInventariosDto", "stock", params, "stock");
			Articulo item= new Articulo(
				(Boolean)this.attrs.get("sinIva"),
				this.getAdminOrden().getTipoDeCambio(),
				seleccionado.toString("nombre"), 
				codigo== null? "": codigo.toString(),
				seleccionado.toDouble(this.precio),
				this.getAdminOrden().getDescuento(), 
				-1L,
				this.getAdminOrden().getExtras(), 
				0D,
				seleccionado.toString("propio"),
				seleccionado.toDouble("iva"), 
				0D,
				0D,
				seleccionado.toDouble("cantidad"), 
				-1* idOrdenDetalle, 
				seleccionado.toLong("idArticulo"), 
				0D,
				((UISelectEntity)this.attrs.get("idProveedor")).getKey(),
				this.attrs.get("ultimo")!= null,
				this.attrs.get("solicitado")!= null,
				stock== null? 0D: stock.toDouble(),
				0D,
				"",
				""
			);
			if(this.getAdminOrden().add(item))
				RequestContext.getCurrentInstance().execute("jsArticulos.update("+ (this.adminOrden.getArticulos().size()- 1)+ ");");
			RequestContext.getCurrentInstance().execute("jsArticulos.callback('"+ item.toMap()+ "');");
		} // try
		finally {
			Methods.clean(params);
		} // finally
	}
	
	public void doFaltanteArticulo() {
		try {
			UISelectEntity faltante= (UISelectEntity)this.attrs.get("faltante");
   		toAddArticulo(faltante);
		  List<UISelectEntity> faltantes= (List<UISelectEntity>)this.attrs.get("faltantes");
			faltantes.remove(faltantes.indexOf(faltante));
		} // try
	  catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
	}

	public void toLoadFaltantes() {
		List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
    try {
			params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
			params.put("idAlmacen", this.getAdminOrden().getIdAlmacen());
			params.put("idProveedor", this.getAdminOrden().getIdProveedor());
			columns= new ArrayList<>();
      columns.add(new Columna("propio", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("estatus", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("costo", EFormatoDinamicos.MONEDA_CON_DECIMALES));
      columns.add(new Columna("stock", EFormatoDinamicos.NUMERO_SIN_DECIMALES));
      columns.add(new Columna("minimo", EFormatoDinamicos.NUMERO_SIN_DECIMALES));
      columns.add(new Columna("maximo", EFormatoDinamicos.NUMERO_SIN_DECIMALES));
      this.attrs.put("faltantes", UIEntity.build("VistaOrdenesComprasDto", "faltantes", params, columns));
    } // try
    catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
    finally {
      Methods.clean(params);
      Methods.clean(columns);
    }// finally
	}

	public List<UISelectEntity> doCompleteArticulo(String query) {
		this.attrs.put("codigo", query);
    this.doUpdateArticulos();		
		return (List<UISelectEntity>)this.attrs.get("articulos");
	}	

	private boolean isInsideArticulo(String code, String name) {
		String filterCode= (String)this.attrs.get("filterCode");
		String filterName= (String)this.attrs.get("filterName");
	  return (Cadena.isVacio(filterCode) || code.contains(filterCode.toUpperCase())) && (Cadena.isVacio(filterName) || name.contains(filterName.toUpperCase()));	
	}
	
	public void doFilterRows() {
		LOG.info("doFilterRows: "+ this.attrs.get("filterCode")+ " => "+ this.attrs.get("filterName"));
		this.attrs.put("filterCode", this.attrs.get("filterCode")== null? "": ((String)this.attrs.get("filterCode")).trim());
		this.attrs.put("filterName", this.attrs.get("filterName")== null? "": ((String)this.attrs.get("filterName")).trim());
		this.getAdminOrden().getFiltrados().clear();
		for (Articulo articulo : this.getAdminOrden().getArticulos()) {
			if(this.isInsideArticulo("|"+ articulo.getCodigo()+ "|"+ articulo.getPropio()+ "|", articulo.getNombre()))
				this.getAdminOrden().getFiltrados().add(articulo);
		} // for
	}
	

}
