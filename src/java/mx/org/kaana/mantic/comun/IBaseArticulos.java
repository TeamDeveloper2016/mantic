package mx.org.kaana.mantic.comun;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import mx.org.kaana.kajool.reglas.comun.FormatLazyModel;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.formato.Global;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.libs.pagina.JsfUtilities;
import mx.org.kaana.libs.recurso.LoadImages;
import mx.org.kaana.mantic.compras.ordenes.beans.Articulo;
import mx.org.kaana.mantic.compras.ordenes.beans.OrdenCompra;
import mx.org.kaana.mantic.compras.ordenes.reglas.Descuentos;
import mx.org.kaana.mantic.db.dto.TcManticArticulosDto;
import mx.org.kaana.mantic.db.dto.TrManticArticuloPrecioSugeridoDto;
import mx.org.kaana.mantic.enums.EPrecioArticulo;
import mx.org.kaana.mantic.inventarios.comun.IBaseImportar;
import mx.org.kaana.mantic.inventarios.entradas.beans.NotaEntrada;
import mx.org.kaana.mantic.ventas.caja.reglas.Transaccion;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.StreamedContent;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 18/05/2018
 *@time 07:29:56 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public abstract class IBaseArticulos extends IBaseImportar implements Serializable {

	private static final long serialVersionUID=-7378726801437171894L;
	private static final Log LOG=LogFactory.getLog(IBaseArticulos.class);
	
  private IAdminArticulos adminOrden;
	private StreamedContent detailImage;
	private String precio;

	public IBaseArticulos() {
		this("precio");
		this.attrs.put("paginator", false); 
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

	public StreamedContent getDetailImage() {
		return detailImage;
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
				temporal.setCodigo(codigo== null? articulo.containsKey("codigo")? articulo.toString("codigo"): "": codigo.toString());
				if(Cadena.isVacio(articulo.toString("propio")))
					LOG.warn("El articulo ["+ articulo.toLong("idArticulo")+" ] no tiene codigo asignado '"+ articulo.toString("nombre")+ "'");
				temporal.setPropio(articulo.toString("propio"));
				temporal.setNombre(articulo.toString("nombre"));
				temporal.setOrigen(articulo.toString("origen"));
				temporal.setValor(articulo.toDouble(this.precio));
				// SI VIENE EN LA CONSULTA EL CAMPO DE PORCENTAJE ES LA SUMA DE LA COLUMNA DE DESCUENTO Y EXTRA SEPARADA POR COMA
				// ASIGNARLA PARA CALCULAR EL COSTO REAL DEL ARTICULO
				if(articulo.containsKey("porcentajes")) {
					temporal.setMorado(articulo.toString("morado"));
					temporal.setPorcentajes(articulo.toString("porcentajes"));
				} // if	
				// SI VIENE DE IMPORTAR EL ARTICULO DE UN XML ENTONCES CONSIDERAR EL COSTO DE LA FACTURA CON RESPECTO AL DEL CATALOGOD E ARTICULOS
				if(articulo.containsKey("costo")) 
  				temporal.setCosto(articulo.toDouble("costo"));
			  else
				  temporal.setCosto(articulo.toDouble(this.precio));
				temporal.setIva(articulo.toDouble("iva"));				
				temporal.setSat(articulo.get("sat").getData()!= null ? articulo.toString("sat") : "");				
				temporal.setDescuento(this.adminOrden.getDescuento());
				temporal.setExtras(this.adminOrden.getExtras());				
				// SON ARTICULOS QUE ESTAN EN LA FACTURA MAS NO EN LA ORDEN DE COMPRA
				if(articulo.containsKey("descuento")) 
				  temporal.setDescuento(articulo.toString("descuento"));
				if(articulo.containsKey("cantidad")) {
				  temporal.setCantidad(articulo.toDouble("cantidad"));
				  temporal.setSolicitados(articulo.toDouble("cantidad"));
				} // if	
				if(temporal.getCantidad()< 1D)					
					temporal.setCantidad(1D);
				temporal.setCuantos(0D);
				temporal.setUltimo(this.attrs.get("ultimo")!= null);
				temporal.setSolicitado(this.attrs.get("solicitado")!= null);
				temporal.setUnidadMedida(articulo.toString("unidadMedida"));
				temporal.setPrecio(articulo.toDouble("precio"));				
				
				Value stock= (Value)DaoFactory.getInstance().toField("TcManticInventariosDto", "stock", params, "stock");
				temporal.setStock(stock== null? 0D: stock.toDouble());

				// Esto es para cuando se agregan articulos de forma directa del archivo XML
				if(articulo.containsKey("disponible")) 
  				temporal.setDisponible(articulo.toBoolean("disponible"));
				if(index== this.adminOrden.getArticulos().size()- 1) {
					this.adminOrden.getArticulos().add(new Articulo(-1L));
  				this.adminOrden.toAddUltimo(this.adminOrden.getArticulos().size()- 1);
					UIBackingUtilities.execute("jsArticulos.update("+ (this.adminOrden.getArticulos().size()- 1)+ ");");
				} // if	
				if(articulo.containsKey("facturado")) 
					temporal.setFacturado(true);
				UIBackingUtilities.execute("jsArticulos.callback('"+ articulo.getKey()+ "');");
				this.adminOrden.toCalculate(index);
				if(this.attrs.get("paginator")== null || !(boolean)this.attrs.get("paginator"))
				  this.attrs.put("paginator", this.adminOrden.getArticulos().size()> Constantes.REGISTROS_LOTE_TOPE);
				//if(this instanceof IBaseStorage)
 				//	((IBaseStorage)this).toSaveRecord();
			} // if	
			else
				temporal.setNombre("<span class='janal-color-orange'>EL ARTICULO NO EXISTE EN EL CATALOGO !</span>");
		} // try
		finally {
			Methods.clean(params);
		}
	}
	
  protected void toMoveArticulo(Articulo articulo, Integer index) throws Exception {
		Articulo temporal               = new Articulo(articulo.getKey());
		TcManticArticulosDto artTemporal= null;
		Map<String, Object> params      = new HashMap<>();
		EPrecioArticulo eprecioArticulo = null;
		try {			
			this.doSearchArticulo(articulo.getIdArticulo(), index);
			params.put("idArticulo", articulo.getIdArticulo());
			params.put("idProveedor", this.adminOrden.getIdProveedor());
			params.put("idAlmacen", this.adminOrden.getIdAlmacen());
			temporal.setKey(articulo.getIdArticulo());
			temporal.setIdArticulo(articulo.getIdArticulo());
			temporal.setIdProveedor(this.adminOrden.getIdProveedor());
			temporal.setIdRedondear(articulo.getIdRedondear());
			Value codigo= (Value)DaoFactory.getInstance().toField("TcManticArticulosCodigosDto", "codigo", params, "codigo");
			temporal.setCodigo(codigo== null? "": codigo.toString());
			temporal.setPropio(articulo.getPropio());
			temporal.setNombre(articulo.getNombre());
			eprecioArticulo= EPrecioArticulo.fromNombre(this.precio.toUpperCase());
			artTemporal= (TcManticArticulosDto) DaoFactory.getInstance().findById(TcManticArticulosDto.class, articulo.getIdArticulo());
			if(artTemporal!= null) {
				switch(eprecioArticulo) {
					case MAYOREO:
						temporal.setValor(artTemporal.getMayoreo());
						temporal.setCosto(artTemporal.getMayoreo());
						break;
					case MEDIO_MAYOREO:
						temporal.setValor(artTemporal.getMedioMayoreo());
						temporal.setCosto(artTemporal.getMedioMayoreo());
						break;
					case MENUDEO:
						temporal.setValor(artTemporal.getMenudeo());
						temporal.setCosto(artTemporal.getMenudeo());
						break;
				}	// switch	 	
			} // if
			temporal.setIva(articulo.getIva());
			temporal.setSat(articulo.getSat());
			temporal.setDescuento(this.adminOrden.getDescuento());
			temporal.setExtras(this.adminOrden.getExtras());										
			temporal.setCantidad(articulo.getCantidad());
			temporal.setCuantos(0D);
			temporal.setUltimo(this.attrs.get("ultimo")!= null);
			temporal.setSolicitado(this.attrs.get("solicitado")!= null);
			temporal.setUnidadMedida(articulo.getUnidadMedida());
			temporal.setPrecio(articulo.getPrecio());				
			Value stock= (Value)DaoFactory.getInstance().toField("TcManticInventariosDto", "stock", params, "stock");
			temporal.setStock(stock== null? 0D: stock.toDouble());				
			this.adminOrden.getArticulos().add(temporal);
			this.adminOrden.toAddUltimo(this.adminOrden.getArticulos().size()- 1);
			UIBackingUtilities.execute("jsArticulos.update("+ (this.adminOrden.getArticulos().size()- 1)+ ");");				
			UIBackingUtilities.execute("jsArticulos.callback('"+ articulo.getKey()+ "');");
			this.adminOrden.toAddArticulo(this.adminOrden.getArticulos().size()- 1);		
			if(this.attrs.get("paginator")== null || !(boolean)this.attrs.get("paginator"))
  			this.attrs.put("paginator", this.adminOrden.getArticulos().size()> Constantes.REGISTROS_LOTE_TOPE);
		} // try
		finally {
			Methods.clean(params);
		} // finally
	} // toMoveDataArt
	
	public void doUpdateArticulo(String codigo, Integer index) {
		List<Columna> columns         = null;
    Map<String, Object> params    = new HashMap<>();
		List<UISelectEntity> articulos= null;
		boolean buscaPorCodigo        = false;
    try {
			codigo= new String(codigo).replaceAll(Constantes.CLEAN_SQL, "").trim();
			columns= new ArrayList<>();
      columns.add(new Columna("propio", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
			params.put("idAlmacen", JsfBase.getAutentifica().getEmpresa().getIdAlmacen());
  		params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getDependencias());
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
						UIBackingUtilities.execute("jsArticulos.exists('"+ articulo.toString("propio")+ "', '"+ articulo.toString("nombre")+ "', "+ position+ ");");
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

	public void doDeleteArticulo(Integer index, Boolean isCantidad) {
    try {
			if(this.adminOrden.getArticulos().size()== 1 || index.equals(this.adminOrden.getArticulos().size()- 1)) {			
				Articulo temporal= this.adminOrden.getArticulos().get(index);
				temporal.setKey(-1L);
				temporal.setIdArticulo(-1L);
				temporal.setIdProveedor(-1L);
				temporal.setCodigo("");
				temporal.setPropio("");
				temporal.setNombre("");
				temporal.setValor(0.0);
				temporal.setIva(0.0);
				temporal.setCantidad(0D);
  			if(!isCantidad)
	  		  this.adminOrden.toRemoveArticulo(index);
			} // if
			else {
  			if(!isCantidad)
  				this.adminOrden.toRemoveArticulo(index);
  			this.adminOrden.getArticulos().remove(index.intValue());
			} // 
			if(isCantidad)
			  this.adminOrden.toCantidad();
			UIBackingUtilities.execute("jsArticulos.update("+ (this.adminOrden.getArticulos().size()- 1)+ ");");
			//if(this instanceof IBaseStorage)
			//	((IBaseStorage)this).toSaveRecord();
		} // try
	  catch (Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
	}	
	
	public void doDeleteArticulo(Integer index) {
		this.doDeleteArticulo(index, Boolean.FALSE);
	}

	public void doCalculate(Integer index) {
		this.adminOrden.toCalculate(index);
	}	

	public void doUpdatePorcentaje() {
		this.adminOrden.toUpdatePorcentajes();
	} 

	public void doUpdateDescuento() {
		this.adminOrden.toUpdateDescuento();
	} 

	public void doUpdateExtras() {
		this.adminOrden.toUpdateExtras();
	} 

	public void doUpdateIvaTipoDeCambio() {
		boolean sinIva= (boolean)this.attrs.get("sinIva");
		this.adminOrden.setIdSinIva(sinIva? 1L: 2L);
		this.adminOrden.toCalculate(true);
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
      columns.add(new Columna("costo", EFormatoDinamicos.MONEDA_SAT_DECIMALES));
      columns.add(new Columna("cantidad", EFormatoDinamicos.NUMERO_CON_DECIMALES));
      columns.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA_CORTA));
			Entity solicitado= (Entity)DaoFactory.getInstance().toEntity("VistaOrdenesComprasDto", "solicitado", this.attrs);
			if(solicitado!= null) {
		     Descuentos descuentos= new Descuentos(solicitado.toDouble("costo"), solicitado.toString("descuento").concat(",").concat("extra"));
		     Value value= new Value("real", Numero.toRedondearSat(descuentos.toImporte()== 0? solicitado.toDouble("costo"):  descuentos.toImporte()));
				 solicitado.put("real", value);
		     descuentos= new Descuentos(solicitado.toDouble("costo"), solicitado.toString("descuento"));
		     value     = new Value("calculado", Numero.toRedondearSat(descuentos.toImporte()== 0? solicitado.toDouble("costo"):  descuentos.toImporte()));
				 solicitado.put("calculado", value);
			} // if
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
			if(ultimoPrecio!= null && !ultimoPrecio.isEmpty()) {
//				Descuentos descuentos= new Descuentos(ultimoPrecio.toDouble("costo"), ultimoPrecio.toString("descuento").concat(",").concat("extra"));
//				Value calculo= new Value("real", Numero.toRedondearSat(descuentos.toImporte()== 0? ultimoPrecio.toDouble("costo"):  descuentos.toImporte()));
//				ultimoPrecio.put("real", calculo);
//				descuentos= new Descuentos(ultimoPrecio.toDouble("costo"), ultimoPrecio.toString("descuento"));
//				calculo   = new Value("calculado", Numero.toRedondearSat(descuentos.toImporte()== 0? ultimoPrecio.toDouble("costo"):  descuentos.toImporte()));
//				ultimoPrecio.put("calculado", calculo);
				for (Value value : ultimoPrecio.values()) {
				  if("|costo|".indexOf(value.getName())> 0)
						value.setData(Global.format(EFormatoDinamicos.MILES_SAT_DECIMALES, Numero.toRedondearSat(value.toDouble())));
					else
            if("|registro|".indexOf(value.getName())> 0) 
					    value.setData(Global.format(EFormatoDinamicos.FECHA_HORA_CORTA, value.toTimestamp()));						
  					else
              if("|stock|".indexOf(value.getName())> 0) 
		  			    value.setData(Global.format(EFormatoDinamicos.NUMERO_CON_DECIMALES, value.toDouble()));						
				} // for
			} // if	
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
		  this.attrs.put("sugeridos", UIEntity.build("VistaOrdenesComprasDto", "sugerido", this.attrs, columns, Constantes.SQL_TODOS_REGISTROS));
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
		int buscarCodigoPor       = 2;
    try {
			columns= new ArrayList<>();
      columns.add(new Columna("propio", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
			params.put("idAlmacen", JsfBase.getAutentifica().getEmpresa().getIdAlmacen());
  		params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getDependencias());
  		params.put("idProveedor", this.attrs.get("proveedor")== null? new UISelectEntity(new Entity(-1L)): ((UISelectEntity)this.attrs.get("proveedor")).getKey());
			String search= (String)this.attrs.get("codigo"); 
			if(!Cadena.isVacio(search)) {
				if((boolean)this.attrs.get("buscaPorCodigo"))
			    buscarCodigoPor= 1;
				if(search.startsWith("."))
					buscarCodigoPor= 1;
				else 
					if(search.startsWith(":"))
						buscarCodigoPor= 0;
				if(search.startsWith(".") || search.startsWith(":"))
					search= search.trim().substring(1);				
				search= search.toUpperCase().replaceAll(Constantes.CLEAN_SQL, "").trim().replaceAll("(,| |\\t)+", ".*.*");
			} // if	
			else
				search= "WXYZ";
  		params.put("codigo", search);	
			switch(buscarCodigoPor) {      
				case 0: 
					this.attrs.put("articulos", (List<UISelectEntity>) UIEntity.build("VistaOrdenesComprasDto", "porCodigoIgual", params, columns, 20L));
					break;
				case 1: 
					this.attrs.put("articulos", (List<UISelectEntity>) UIEntity.build("VistaOrdenesComprasDto", "porCodigo", params, columns, 20L));
					break;
				case 2:
          this.attrs.put("articulos", (List<UISelectEntity>) UIEntity.build("VistaOrdenesComprasDto", "porNombre", params, columns, 20L));
          break;
			} // switch
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
	
	public void doUpdateDialogArticulos(String codigo) {
		List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
		boolean buscaPorCodigo    = false;
    try {
			columns= new ArrayList<>();
      columns.add(new Columna("propio", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("original", EFormatoDinamicos.MONEDA_CON_DECIMALES));
			params.put("idAlmacen", JsfBase.getAutentifica().getEmpresa().getIdAlmacen());
  		params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getDependencias());
  		params.put("idProveedor", this.attrs.get("proveedor")== null? new UISelectEntity(new Entity(-1L)): ((UISelectEntity)this.attrs.get("proveedor")).getKey());
			if(!Cadena.isVacio(codigo)) {
				buscaPorCodigo= codigo.startsWith(".");
				if(buscaPorCodigo)
					codigo= codigo.trim().substring(1);
			} // if	
			else
				codigo= "WXYZ";
			params.put("codigo", codigo.toUpperCase().replaceAll("(,| |\\t)+", ".*.*"));
			if(buscaPorCodigo)
        this.attrs.put("lazyModel", new FormatCustomLazy("VistaOrdenesComprasDto", "porCodigo", params, columns));
			else
        this.attrs.put("lazyModel", new FormatCustomLazy("VistaOrdenesComprasDto", "porNombre", params, columns));
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
 
	public void doCleanArticulos() {
		List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
		try {
			columns= new ArrayList<>();
  		params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getDependencias());
  		params.put("idProveedor", this.attrs.get("proveedor")== null? new UISelectEntity(new Entity(-1L)): ((UISelectEntity)this.attrs.get("proveedor")).getKey());
			params.put("codigo", "WXYZ");
      this.attrs.put("lazyModel", new FormatCustomLazy("VistaOrdenesComprasDto", "porLikeNombre", params, columns));
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
 
	public void doFindOutArticulos(String codigo) {
		List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
		boolean buscaPorCodigo    = false;
    try {
			columns= new ArrayList<>();
      columns.add(new Columna("propio", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
			params.put("idAlmacen", JsfBase.getAutentifica().getEmpresa().getIdAlmacen());
  		params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getDependencias());
  		params.put("idProveedor", this.attrs.get("proveedor")== null? new UISelectEntity(new Entity(-1L)): ((UISelectEntity)this.attrs.get("proveedor")).getKey());
			if(!Cadena.isVacio(codigo)) {
  			codigo= new String(codigo).replaceAll(Constantes.CLEAN_SQL, "").trim();
				buscaPorCodigo= codigo.startsWith(".");
				if(buscaPorCodigo)
					codigo= codigo.trim().substring(1);
				codigo= codigo.toUpperCase().replaceAll("(,| |\\t)+", ".*.*");
			} // if	
			else
				codigo= "WXYZ";
			params.put("codigo", codigo.toUpperCase());
			if((boolean)this.attrs.get("buscaPorCodigo") || buscaPorCodigo)
        this.attrs.put("lazyModel", new FormatCustomLazy("VistaOrdenesComprasDto", "porCodigo", params, columns));
			else
        this.attrs.put("lazyModel", new FormatCustomLazy("VistaOrdenesComprasDto", "porNombre", params, columns));
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
	    UISelectEntity articulo       = (UISelectEntity)this.attrs.get("articulo");
	    UISelectEntity encontrado     = (UISelectEntity)this.attrs.get("encontrado");
			if(encontrado!= null) {
				articulo= encontrado;
				this.attrs.remove("encontrado");
			} // else
			else 
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
					if(index!= position) {
						if(this.attrs.get("omitirMensaje")!= null)
							this.attrs.remove("omitirMensaje");
						else
						  UIBackingUtilities.execute("jsArticulos.exists('"+ articulo.toString("propio")+ "', '"+ articulo.toString("nombre")+ "', "+ position+ ","+ Constantes.REGISTROS_POR_LOTE+ ","+ (this.attrs.get("paginator")== null? false: this.attrs.get("paginator"))+ ");");
					} // if	
				} // if	
				else
					this.toMoveData(articulo, index);
			} // else
			else 
					this.toMoveData(articulo, index);
			DataTable dataTable= (DataTable)JsfUtilities.findComponent("contenedorGrupos:tabla");
			if (dataTable!= null) 
				dataTable.setRows((boolean)this.attrs.get("paginator") || this.getAdminOrden().getTotales().getArticulos()>  Constantes.REGISTROS_LOTE_TOPE? Constantes.REGISTROS_POR_LOTE: 10000);		
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
				this.adminOrden.getIdProveedor(),
				this.attrs.get("ultimo")!= null,
				this.attrs.get("solicitado")!= null,
				stock== null? 0D: stock.toDouble(),
				0D,
				"",
				"",
				1L
			);
			int position= this.getAdminOrden().getArticulos().indexOf(item);
			if(this.getAdminOrden().getArticulos().size()> 1 && position>= 0) {
				Articulo articulo= this.getAdminOrden().getArticulos().get(position);
				articulo.setCantidad(articulo.getCantidad()+ item.getCantidad());
				UIBackingUtilities.execute("jsArticulos.exists('"+ articulo.getPropio()+ "', '"+ articulo.getNombre()+ "', "+ position+ ");");
			} // if
			else {
				if(this.getAdminOrden().add(item))
					UIBackingUtilities.execute("jsArticulos.update("+ (this.adminOrden.getArticulos().size()- 1)+ ");");
				UIBackingUtilities.execute("jsArticulos.callback('"+ item.getKey()+ "');");
			}   // if
			this.getAdminOrden().toCalculate();
		} // try
		finally {
			Methods.clean(params);
		} // finally
	}
	
	public void toAddFaltante(Articulo seleccionado) throws Exception {
		Long idOrdenDetalle= new Long((int)(Math.random()*10000));
  	this.doSearchArticulo(seleccionado.getIdArticulo(), 0);
		Map<String, Object> params= null;
		Value stock               = null;
		try {
			params=new HashMap<>();
			params.put("idArticulo", seleccionado.getIdArticulo());
			params.put("idProveedor", this.attrs.get("idProveedor"));
			params.put("idAlmacen", this.attrs.get("idAlmacen"));
  		stock = (Value)DaoFactory.getInstance().toField("TcManticInventariosDto", "stock", params, "stock");
			Articulo item= new Articulo(
				(Boolean)this.attrs.get("sinIva"),
				this.getAdminOrden().getTipoDeCambio(),
				seleccionado.getNombre(), 
				seleccionado.getCodigo(),
				seleccionado.getCosto(),
				this.getAdminOrden().getDescuento(), 
				-1L,
				this.getAdminOrden().getExtras(), 
				0D,
				"",
				seleccionado.getIva(), 
				0D,
				0D,
				seleccionado.getCantidad(), 
				-1* idOrdenDetalle, 
				seleccionado.getIdArticulo(), 
				0D,
				(Long)this.attrs.get("idProveedor"),
				this.attrs.get("ultimo")!= null,
				this.attrs.get("solicitado")!= null,
				stock== null? 0D: stock.toDouble(),
				0D,
				seleccionado.getSat(),
				seleccionado.getUnidadMedida(),
				1L
			);
			if(this.getAdminOrden().add(item))
				UIBackingUtilities.execute("jsArticulos.update("+ (this.adminOrden.getArticulos().size()- 1)+ ");");
			UIBackingUtilities.execute("jsArticulos.callback('"+ item.getKey()+ "');");
		} // try
		finally {
			Methods.clean(params);
		} // finally
	}

	public void doFaltanteArticulo() {
		try {
			UISelectEntity faltante= (UISelectEntity)this.attrs.get("faltante");
   		this.toAddArticulo(faltante);
		  List<UISelectEntity> faltantes= (List<UISelectEntity>)this.attrs.get("faltantes");
			faltantes.remove(faltantes.indexOf(faltante));
		} // try
	  catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
	}

	public void doAgregarPerdido() {
		try {
			UISelectEntity perdido= (UISelectEntity)this.attrs.get("perdido");
   		this.toAddArticulo(perdido);
		  List<UISelectEntity> perdidos= (List<UISelectEntity>)this.attrs.get("perdidos");
			perdidos.remove(perdidos.indexOf(perdido));
		} // try
	  catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
	}

	public void doLoadFaltantes() {
		List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
    try {
			Long before   = (Long)this.attrs.get("before");
			Long idAlmacen= this.getAdminOrden().getIdAlmacen();
			if(before== null || idAlmacen== null || !before.equals(idAlmacen)) {
				params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getDependencias());
				params.put("idAlmacen", this.getAdminOrden().getIdAlmacen());
				params.put("idProveedor", this.getAdminOrden().getIdProveedor());
				columns= new ArrayList<>();
				columns.add(new Columna("propio", EFormatoDinamicos.MAYUSCULAS));
				columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
				columns.add(new Columna("estatus", EFormatoDinamicos.MAYUSCULAS));
				columns.add(new Columna("costo", EFormatoDinamicos.MONEDA_SAT_DECIMALES));
				columns.add(new Columna("stock", EFormatoDinamicos.NUMERO_CON_DECIMALES));
				columns.add(new Columna("minimo", EFormatoDinamicos.NUMERO_SIN_DECIMALES));
				columns.add(new Columna("maximo", EFormatoDinamicos.NUMERO_SIN_DECIMALES));
				this.attrs.put("faltantes", UIEntity.build("VistaOrdenesComprasDto", "faltantes", params, columns, Constantes.SQL_TODOS_REGISTROS));
			  this.attrs.put("before", this.attrs.get("idAlmacen"));
			} // 
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
	
	public void doLoadPerdidas() {
		this.doLoadPerdidas(this.attrs.get("idPedidoSucursal")== null? -1L: ((UISelectEntity)this.attrs.get("idPedidoSucursal")).getKey());
	}
	
	public void doLoadPerdidas(Long idSucursal) {
		List<Columna> columns= null;
    try {
			this.attrs.put("idSucursal", idSucursal);
			columns= new ArrayList<>();
      columns.add(new Columna("codigo", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("usuario", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA_CORTA));
      this.attrs.put("perdidos", UIEntity.build("VistaOrdenesComprasDto", "registrados", this.attrs, columns, Constantes.SQL_TODOS_REGISTROS));
    } // try
    catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
    finally {
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
	
	public void doChangeBuscado() {
		if(this.attrs.get("encontrado")== null) {
			FormatCustomLazy list= (FormatCustomLazy)this.attrs.get("lazyModel");
			if(list!= null) {
				List<Entity> items= (List<Entity>)list.getWrappedData();
				if(items.size()> 0)
					this.attrs.put("encontrado", new UISelectEntity(items.get(0)));
			} // if
		} // if
	}

	public void doDetailArticulo(Long idArticulo, Integer index) {
		Map<String, Object>params= null;
		List<Columna>columns     = null;
		try {
			if(idArticulo!= null) {
				params= new HashMap<>();
				params.put("idArticulo", idArticulo);
				params.put("idAlmacen", JsfBase.getAutentifica().getEmpresa().getIdAlmacen());				
				columns= new ArrayList<>();
				this.attrs.put("detailArticulo", DaoFactory.getInstance().toEntity("VistaArticulosDto", "informativo", params));
				columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
				columns.add(new Columna("valor", EFormatoDinamicos.MAYUSCULAS));
				params.put(Constantes.SQL_CONDICION, "id_articulo=" + idArticulo);
				this.attrs.put("lazyEspecificaciones", new FormatLazyModel("TcManticArticulosEspecificacionesDto", "row", params, columns));
				UIBackingUtilities.resetDataTable("lazyEspecificaciones");
				columns.clear();
				columns.add(new Columna("porcentaje", EFormatoDinamicos.NUMERO_CON_DECIMALES));
				columns.add(new Columna("vigenciaInicial", EFormatoDinamicos.FECHA_HORA_CORTA));
				columns.add(new Columna("vigenciaFinal", EFormatoDinamicos.FECHA_HORA_CORTA));
				columns.add(new Columna("observaciones", EFormatoDinamicos.MAYUSCULAS));
				this.attrs.put("lazyDescuentos", new FormatLazyModel("TcManticArticulosDescuentosDto", "row", params, columns));
				UIBackingUtilities.resetDataTable("lazyDescuentos");
				this.detailImage= LoadImages.getImage(idArticulo);
				this.toLoadExistencias(idArticulo);
			} // if
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
	} 

  public void doRowDblselect(SelectEvent event) {
		this.attrs.put("encontrado", new UISelectEntity((Entity)event.getObject()));
	}	
	
	public void doResetDataTable() {
    DataTable dataTable= (DataTable)JsfUtilities.findComponent("contenedorGrupos:tabla");
    if (dataTable!= null) {
			dataTable.reset();
      dataTable.setFirst(0);		
      dataTable.setRows((boolean)this.attrs.get("paginator") || this.getAdminOrden().getTotales().getArticulos()>  Constantes.REGISTROS_LOTE_TOPE? Constantes.REGISTROS_POR_LOTE: 10000);		
		}	// if
	}
	
  public int getRows() {
	  return this.attrs.get("paginator")== null || this.getAdminOrden()== null || (boolean)this.attrs.get("paginator") || this.getAdminOrden().getTotales().getArticulos()> Constantes.REGISTROS_LOTE_TOPE? 10000: Constantes.REGISTROS_POR_LOTE;
  }
	
	public String getRecordCount() {
		return (this.attrs.get("perdidos")== null || ((List<UISelectEntity>)this.attrs.get("perdidos")).isEmpty()? 100: ((List<UISelectEntity>)this.attrs.get("perdidos")).size())+ ",".concat(Constantes.REGISTROS_POR_CADA_PAGINA);
	}

	public void unlockVenta(){
		Transaccion transaccion= null;
		try {
			if(this.attrs.get("ticketLock")!= null){
				transaccion= new Transaccion(-1L, (Long)this.attrs.get("ticketLock"));
				transaccion.ejecutar(EAccion.DESACTIVAR);
			} // if
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
	} // unlockVenta	
	
	protected void unlockVentaExtends(Long block, Long active){
		Transaccion transaccion= null;
		try {			
			transaccion= new Transaccion(block, active);
			transaccion.ejecutar(EAccion.DESACTIVAR);
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
	} // unlockVenta	
	
	public void doRecoverArticulo(Integer index) {
		try {
			if(index>= 0 && index< this.getAdminOrden().getArticulos().size()) {
				this.attrs.put("seleccionado", this.getAdminOrden().getArticulos().get(index).toEntity());
				Object backing= JsfBase.ELAsObject("manticCatalogosArticulosExpress");
				if(backing!= null)
					((IBaseAttribute)backing).getAttrs().put("seleccionado", this.attrs.get("seleccionado"));
			} // if	
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch				
	} // doRecoveryArticulo	
	
  private void toLoadExistencias(Long idArticulo) {
		List<Columna> columns     = null;
		Map<String, Object> params= null;
		try {
			columns= new ArrayList<>();
      columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("ubicacion", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("stock", EFormatoDinamicos.NUMERO_SIN_DECIMALES));
			params=new HashMap<>();
			params.put("idArticulo", idArticulo);
			this.attrs.put("existencias", UIEntity.build("VistaKardexDto", "localizado", params, columns));
		} // try
		catch (Exception e) {
			Error.mensaje(e);
		} // catch
		finally {
			Methods.clean(params);
			Methods.clean(columns);
		} // finally
  }	

}
