package mx.org.kaana.mantic.inventarios.almacenes.backing;

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
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.formato.Global;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.libs.formato.Periodo;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.recurso.LoadImages;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.db.dto.TcManticAlmacenesArticulosDto;
import mx.org.kaana.mantic.db.dto.TcManticArticulosDto;
import mx.org.kaana.mantic.inventarios.almacenes.beans.AdminKardex;
import mx.org.kaana.mantic.inventarios.almacenes.beans.TiposVentas;
import mx.org.kaana.mantic.inventarios.almacenes.reglas.Transaccion;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.TreeNode;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 26/05/2018
 *@time 02:19:46 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

@Named(value= "manticInventariosAlmacenesKardex")
@ViewScoped
public class Kardex extends IBaseAttribute implements Serializable {

	private static final long serialVersionUID=-6770709196941718388L;
	private static final Log LOG=LogFactory.getLog(Kardex.class);

	private AdminKardex adminKardex;
	private StreamedContent image;
	private Integer tabPage;
	private TreeNode ubicaciones;

	public AdminKardex getAdminKardex() {
		return adminKardex;
	}

	public void setAdminKardex(AdminKardex adminKardex) {
		this.adminKardex=adminKardex;
	}

	public StreamedContent getImage() {
		return image;
	}

	public TreeNode getUbicaciones() {
		return ubicaciones;
	}
	
	@Override
	@PostConstruct
	protected void init() {
		this.tabPage= 0;
  	this.attrs.put("buscaPorCodigo", false);
		this.attrs.put("costoMayorMenor", 0);
		this.adminKardex= new AdminKardex(-1L);
		this.toLoadCatalog();
		if(JsfBase.getFlashAttribute("xcodigo")!= null) {
			this.doCompleteArticulo((String)JsfBase.getFlashAttribute("xcodigo"));
			List<UISelectEntity> articulos= (List<UISelectEntity>)this.attrs.get("articulos");
			if(articulos!= null && !articulos.isEmpty()) {
				this.attrs.put("custom", articulos.get(0));
				this.doFindArticulo();
			} // if	
		} // if	
		this.ubicaciones= new DefaultTreeNode("idKey", new UISelectEntity(-1L), null);	
	}
	
	private void toLoadCatalog() {
    List<UISelectEntity> almacenes= null;
		List<Columna> columns         = null;
    Map<String, Object> params    = new HashMap<>();
    try {
			columns= new ArrayList<>();
			params = new HashMap<>();
      params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
      columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      almacenes= (List<UISelectEntity>)UIEntity.build("TcManticAlmacenesDto", "almacenes", params, columns);
      this.attrs.put("depositos", almacenes);
			if(almacenes!= null) 
			  this.attrs.put("idAlmacen", almacenes.get(0));
    } // try
    catch (Exception e) {
      throw e;
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    }// finally
	}
	
	private void updateArticulo(UISelectEntity articulo) throws Exception {
		List<Columna> columns= null;
    try {
			columns= new ArrayList<>();
      columns.add(new Columna("propio", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("empaque", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("unidadMedida", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("fecha", EFormatoDinamicos.FECHA_CORTA));
      columns.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA));
			this.attrs.put("idArticulo", null);
			if(articulo.size()> 1) {
				this.image= LoadImages.getImage(articulo.toLong("idArticulo"));
  			this.attrs.put("idArticulo", articulo.toLong("idArticulo"));
				Entity solicitado= (Entity)DaoFactory.getInstance().toEntity("VistaKardexDto", "row", this.attrs);
				if(solicitado!= null) {
					UIBackingUtilities.toFormatEntity(solicitado, columns);
					this.attrs.put("articulo", solicitado);
					this.attrs.put("precio", solicitado.toDouble("precio"));
					this.attrs.put("costoMayorMenor", this.getCostoMayorMenor(solicitado.toDouble("value"), solicitado.toDouble("precio")));
					Value ultimo= (Value)DaoFactory.getInstance().toField("TcManticArticulosBitacoraDto", "ultimo", this.attrs, "registro");
					if(ultimo!= null)
					  this.attrs.put("ultimo", Global.format(EFormatoDinamicos.FECHA_HORA, ultimo.toTimestamp()));
					UIBackingUtilities.execute("jsKardex.callback("+ solicitado +");");
      		this.adminKardex= new AdminKardex(
						articulo.toLong("idArticulo"), 
						solicitado.toDouble("precio"), 
						solicitado.toDouble("iva"), 
						solicitado.toDouble("menudeo"), 
						solicitado.toDouble("medioMayoreo"), 
						solicitado.toDouble("mayoreo"), 
						solicitado.toLong("limiteMedioMayoreo"),
						solicitado.toLong("limiteMayoreo")
					);
				} // if	
			} // if
			else {
				this.attrs.put("existe", "<span class='janal-color-orange'>EL ARTICULO NO EXISTE EN EL CATALOGO !</span>");
				this.attrs.put("articulo", null);
				this.adminKardex.getTiposVentas().clear();
			} // if	
			this.toInventario();
			this.toLoadCodigos();
			this.toLoadAlmacenes();
		} // try
		finally {
      Methods.clean(columns);
    }// finally
	}
	
	public String doMoneyValueFormat(Double value) {
		return value== null? "": Global.format(EFormatoDinamicos.MONEDA_SAT_DECIMALES, value);
	}
	
	public String doMoneyFormat(Value value) {
		return value== null? "": this.doMoneyValueFormat(value.toDouble());
	}
	
	public String doPercentageFormat(Value value) {
		return value== null? "": Global.format(EFormatoDinamicos.NUMERO_CON_DECIMALES, value.toDouble())+ "  %";
	}

	public String doPercentageValueFormat(Double value) {
		return value== null? "": Global.format(EFormatoDinamicos.NUMERO_CON_DECIMALES, value)+ "  %";
	}
	
	public String doNumberFormat(Value value) {
		return value== null? "": Global.format(EFormatoDinamicos.NUMERO_CON_DECIMALES, value.toDouble());
	}
	
	public void doUpdateArticulo(String codigo) {
    try {
  		List<UISelectEntity> articulos= this.doCompleteArticulo(codigo);
			UISelectEntity articulo= UIBackingUtilities.toFirstKeySelectEntity(articulos);
			this.updateArticulo(articulo);
		} // try
	  catch (Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
	}	

	public void doUpdateArticulos() {
		List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
		List<UISelectEntity> articulos= null;
		boolean buscaPorCodigo    = false;
    try {
			columns= new ArrayList<>();
      columns.add(new Columna("propio", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
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
        articulos= (List<UISelectEntity>) UIEntity.build("VistaOrdenesComprasDto", "porCodigo", params, columns, 40L);
			else
        articulos= (List<UISelectEntity>) UIEntity.build("VistaOrdenesComprasDto", "porNombre", params, columns, 40L);
      this.attrs.put("articulos", articulos);
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

	public List<UISelectEntity> doCompleteArticulo(String query) {
		this.attrs.put("existe", null);
		this.attrs.put("codigo", query);
    this.doUpdateArticulos();		
		return (List<UISelectEntity>)this.attrs.get("articulos");
	}	

	public String doAceptar() {
    Transaccion transaccion= null;
		EAccion eaccion        = EAccion.MODIFICAR;
    try {			
			transaccion = new Transaccion((Long)this.attrs.get("idArticulo"),  (Double)this.attrs.get("precio"), this.adminKardex.getTiposVentas());
			if (transaccion.ejecutar(eaccion)) {
				JsfBase.addMessage("Se modificaron los precios de tipos de ventas del articulo.", ETipoMensaje.INFORMACION);
   			UIBackingUtilities.execute("jsKardex.callback('"+ this.adminKardex.getTiposVentas()+ "');");
			}	// if
			else 
				JsfBase.addMessage("Ocurri� un error al registrar los precios de los tipos de ventas del articulo.", ETipoMensaje.ERROR);      			
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
		return null;
	}
	
	public void doTabChange(TabChangeEvent event) {
		if(event.getTab().getTitle().equals("C�digos")) 
			this.toLoadCodigos();
		else
			if(event.getTab().getTitle().equals("Almacenes") && this.attrs.get("idArticulo")!= null) 
				this.toLoadAlmacenes();
	    else	
			  if(event.getTab().getTitle().equals("Historial") && this.attrs.get("idArticulo")!= null) 
				  this.toLoadHistorial();
				else	
					if(event.getTab().getTitle().equals("Movimientos") && this.attrs.get("idArticulo")!= null) 
						this.toLoadMovimientos();
		      else
					  if(event.getTab().getTitle().equals("Ubicaciones") && this.attrs.get("idArticulo")!= null) 
						  this.toLoadUbicaciones();
	}

  public void doFindArticulo() {
		try {
    	List<UISelectEntity> articulos= (List<UISelectEntity>)this.attrs.get("articulos");
	    UISelectEntity articulo= (UISelectEntity)this.attrs.get("custom");
			if(articulo== null)
			  articulo= new UISelectEntity(new Entity(-1L));
			else
				if(articulos.indexOf(articulo)>= 0) 
					articulo= articulos.get(articulos.indexOf(articulo));
			  else
			    articulo= articulos.get(0);
			this.updateArticulo(articulo);
		} // try
	  catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
	} 

	public void doUpdateInventario(SelectEvent event) {
		UISelectEntity almacen= (UISelectEntity)event.getObject();
		this.attrs.put("idAlmacen", null);
		if(almacen!= null)
		  this.attrs.put("idAlmacen", almacen.toLong("idAlmacen"));
		this.toInventario();
	}
	
	private void toInventario() {
		List<Columna> columns= null;
    try {
			columns= new ArrayList<>();
      columns.add(new Columna("inicial", EFormatoDinamicos.NUMERO_SIN_DECIMALES));
      columns.add(new Columna("entradas", EFormatoDinamicos.NUMERO_SIN_DECIMALES));
      columns.add(new Columna("salidas", EFormatoDinamicos.NUMERO_SIN_DECIMALES));
      columns.add(new Columna("stock", EFormatoDinamicos.NUMERO_SIN_DECIMALES));
      columns.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA));
			Entity inventario= (Entity)DaoFactory.getInstance().toEntity("TcManticInventariosDto", "inventario", this.attrs);
			UIBackingUtilities.toFormatEntity(inventario, columns);
      this.attrs.put("inventario", inventario);
		} // try
	  catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
		finally {
      Methods.clean(columns);
    }// finally
	}
	
	private void toLoadCodigos() {
		List<Columna> columns= null;
    try {
			columns= new ArrayList<>();
      columns.add(new Columna("codigo", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("prefijo", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("razonSocial", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA));
      this.attrs.put("codigos", (List<UISelectEntity>) UIEntity.build("VistaKardexDto", "codigos", this.attrs, columns));
		} // try
	  catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
		finally {
      Methods.clean(columns);
    }// finally
	}
	
	private void toLoadAlmacenes() {
		List<Columna> columns= null;
    try {
			this.tabPage= 1;
			columns= new ArrayList<>();
      columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("stock", EFormatoDinamicos.NUMERO_SIN_DECIMALES));
      columns.add(new Columna("minimo", EFormatoDinamicos.NUMERO_SIN_DECIMALES));
      columns.add(new Columna("maximo", EFormatoDinamicos.NUMERO_SIN_DECIMALES));
      columns.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA));
      this.attrs.put("almacenes", (List<UISelectEntity>) UIEntity.build("VistaKardexDto", "almacenes", this.attrs, columns));
		} // try
	  catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
		finally {
      Methods.clean(columns);
    }// finally
	}

	private void toLoadHistorial() {
		List<Columna> columns= null;
    try {
			this.tabPage= 2;
			columns= new ArrayList<>();
      columns.add(new Columna("persona", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("razonSocial", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("costo", EFormatoDinamicos.MONEDA_SAT_DECIMALES));
      columns.add(new Columna("menudeo", EFormatoDinamicos.MONEDA_SAT_DECIMALES));
      columns.add(new Columna("medioMayoreo", EFormatoDinamicos.MONEDA_SAT_DECIMALES));
      columns.add(new Columna("mayoreo", EFormatoDinamicos.MONEDA_SAT_DECIMALES));
      columns.add(new Columna("limiteMedioMayoreo", EFormatoDinamicos.NUMERO_SIN_DECIMALES));
      columns.add(new Columna("limiteMayoreo", EFormatoDinamicos.NUMERO_SIN_DECIMALES));
      columns.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA));
			Periodo periodo= new Periodo();
			periodo.addMeses(-6);
			this.attrs.put("registro", periodo.toString());
      this.attrs.put("historial", (List<UISelectEntity>) UIEntity.build("VistaKardexDto", "historial", this.attrs, columns));
		} // try
	  catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
		finally {
      Methods.clean(columns);
    }// finally
	}

	private void toLoadMovimientos() {
		List<Columna> columns= null;
    try {
			this.tabPage= 3;
			columns= new ArrayList<>();
      columns.add(new Columna("almacen", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombreEmpresa", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("usuario", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("cantidad", EFormatoDinamicos.NUMERO_SAT_DECIMALES));
      columns.add(new Columna("stock", EFormatoDinamicos.NUMERO_SAT_DECIMALES));
      columns.add(new Columna("calculo", EFormatoDinamicos.NUMERO_SAT_DECIMALES));
      columns.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA));
			Periodo periodo= new Periodo();
			periodo.addMeses(-12);
			this.attrs.put("registro", periodo.toString());
			this.attrs.put("periodo", Fecha.formatear(Fecha.FECHA_NOMBRE_DIA, periodo.toString()));
      this.attrs.put("movimientos", (List<UISelectEntity>) UIEntity.build("VistaKardexDto", "movimientos", this.attrs, columns));
		} // try
	  catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
		finally {
      Methods.clean(columns);
    }// finally
	}

	public void doUpdateUtilidad(Integer index, Double value) {
		this.adminKardex.toUpdateUtilidad(index, value);
	}
	
	public void doUpdateCosto(Double precio, Boolean keep) {
		double value= ((Entity)this.attrs.get("articulo")).toDouble("value");
		this.attrs.put("costoMayorMenor", this.getCostoMayorMenor(value, precio));
		for (TiposVentas item: this.adminKardex.getTiposVentas()) {
			switch(item.toEnum()) {
				case MENUDEO:
					item.setUtilidad(50D);
					break;
				case MEDIO_MAYOREO:
					item.setUtilidad(40D);
					break;
				case MAYOREO:
					item.setUtilidad(30D);
					break;
			} // switch
			item.setPrecio(Numero.toRedondearSat((1+ (item.getUtilidad()/ 100))* precio));
			item.setCosto(precio);
  		item.toCalculate();
		} // for
	}
	
	public void doCalculate(Integer index) {
		this.adminKardex.toCalculate(index);
	}

	private String getCostoMayorMenor(double value, double precio) {
		double diferencia= precio- value;
		diferencia= value== 0? 0: Numero.toRedondearSat(diferencia* 100/ value);
		String color     = diferencia< -5? "janal-color-orange": diferencia> 5? "janal-color-blue": "janal-color-green";
		boolean display  = diferencia!= 0D;
		return "<i class='fa fa-fw fa-question-circle ".concat(color).concat("' style='float:right; display:").concat(display? "": "none").concat("' title='Costo anterior: ").concat(
			Global.format(EFormatoDinamicos.MONEDA_CON_DECIMALES, value)
		).concat("\n\nDiferencia: ").concat(String.valueOf(diferencia)).concat("%'></i>");
	}

	public void doCleanArticulos() {
		List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
		try {
			columns= new ArrayList<>();
  		params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
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
	
	public void doChangeBuscado() {
		try {
			if(this.attrs.get("encontrado")== null) {
				FormatCustomLazy list= (FormatCustomLazy)this.attrs.get("lazyModel");
				if(list!= null) {
					List<Entity> items= (List<Entity>)list.getWrappedData();
					if(items.size()> 0)
						this.updateArticulo(new UISelectEntity(items.get(0)));
				} // if
			} // else
			else
				this.updateArticulo((UISelectEntity)this.attrs.get("encontrado"));
		} // try
	  catch (Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
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
  		params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
  		params.put("idProveedor", this.attrs.get("proveedor")== null? new UISelectEntity(new Entity(-1L)): ((UISelectEntity)this.attrs.get("proveedor")).getKey());
			if(!Cadena.isVacio(codigo)) {
				buscaPorCodigo= codigo.startsWith(".");
				if(buscaPorCodigo)
					codigo= codigo.trim().substring(1);
			} // if	
			else
				codigo= "WXYZ";
			params.put("codigo", codigo.toUpperCase());
			if(buscaPorCodigo)
        this.attrs.put("lazyModel", new FormatCustomLazy("VistaOrdenesComprasDto", "porCodigo", params, columns));
			else
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

  public void doRowDblselect(SelectEvent event) {
		try {
			this.attrs.put("encontrado", new UISelectEntity((Entity)event.getObject()));
			this.updateArticulo((UISelectEntity)this.attrs.get("encontrado"));
		} // try
	  catch (Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
	}	

  public String	doCancelar() {
		if(this.attrs.get("custom")!= null) {
		  JsfBase.addMessage("Se cargaron de nueva cuenta los datos del articulo.", ETipoMensaje.INFORMACION);
		  this.doFindArticulo();
	  } // if	
		else
		  JsfBase.addMessage("No se ha seleccionado ningun articulo.", ETipoMensaje.ALERTA);
    return null;
	}

  public void doChangeBusquedas() {
		if(this.attrs.get("idArticulo")!= null) {
			switch(this.tabPage) {
				case 1: 
					this.toLoadAlmacenes();
					break;
				case 2: 
    			this.toLoadHistorial();
					break;
				case 3: 
    			this.toLoadMovimientos();
					break;
				case 4: 
    			this.toLoadUbicaciones();
					break;
			} // switch	
		} // if	
	}	

	public void doRecoverArticulo(Integer index) {
		try {
			if(this.attrs.get("idArticulo")!= null) {
				this.attrs.put("seleccionado", this.attrs.get("idArticulo"));
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

	private Long toFindIdKey(String consecutivo, String proceso, String idXml) {
		Long regresar= -1L;
		Map<String, Object> params=null;
		try {
			params=new HashMap<>();
			params.put("consecutivo", consecutivo);
			Entity entity= (Entity)DaoFactory.getInstance().toEntity(proceso, idXml, params);
			if(entity!= null && !entity.isEmpty())
				regresar= entity.getKey();
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	}
		
	public String doMoveSection() {
		String regresar= null;
		UISelectEntity consecutivo= (UISelectEntity)this.attrs.get("consecutivo");
		switch(consecutivo.toLong("idTipoMovimiento").intValue()) {
			case 1: // ENTRADAS
				Long idNotaEntrada= this.toFindIdKey(consecutivo.toString("consecutivo"), "TcManticNotasEntradasDto", "consecutivo");
    		JsfBase.setFlashAttribute("idNotaEntrada", idNotaEntrada);
    		JsfBase.setFlashAttribute("idOrdenCompra", this.toFindIdKey(String.valueOf(idNotaEntrada), "TcManticNotasEntradasDto", "orden"));
    		JsfBase.setFlashAttribute("accion", EAccion.CONSULTAR);
				regresar= "/Paginas/Mantic/Inventarios/Entradas/accion?zOyOxDwIvGuCt=zNyLxMwAvCuEtAs".concat(Constantes.REDIRECIONAR_AMPERSON);
				break;
			case 2: // VENTAS
				Long idVenta= this.toFindIdKey(consecutivo.toString("consecutivo"), "TcManticVentasDto", "consecutivo");
    		JsfBase.setFlashAttribute("idVenta", idVenta);
    		JsfBase.setFlashAttribute("idCliente", this.toFindIdKey(String.valueOf(idVenta), "TcManticVentasDto", "cliente"));
    		JsfBase.setFlashAttribute("accion", EAccion.CONSULTAR);
				regresar= "/Paginas/Mantic/Ventas/accion".concat(Constantes.REDIRECIONAR);
				break;
			case 3: // DEVOLUCIONES
				Long idDevolucion= this.toFindIdKey(consecutivo.toString("consecutivo"), "TcManticDevolucionesDto", "consecutivo");
    		JsfBase.setFlashAttribute("idDevolucion", idDevolucion);
    		JsfBase.setFlashAttribute("idNotaEntrada", this.toFindIdKey(String.valueOf(idDevolucion), "TcManticDevolucionesDto", "nota"));
    		JsfBase.setFlashAttribute("accion", EAccion.CONSULTAR);
				regresar= "/Paginas/Mantic/Inventarios/Devoluciones/accion".concat(Constantes.REDIRECIONAR);
				break;
			case 4: // TRASPASOS
    		JsfBase.setFlashAttribute("idTransferencia", this.toFindIdKey(consecutivo.toString("consecutivo"), "TcManticTransferenciasDto", "consecutivo"));
    		JsfBase.setFlashAttribute("accion", EAccion.CONSULTAR);
				regresar= "/Paginas/Mantic/Catalogos/Almacenes/Transferencias/normal".concat(Constantes.REDIRECIONAR);
				break;
			case 5: // GARANTIAS
				Long idGarantia= this.toFindIdKey(consecutivo.toString("consecutivo"), "TcManticGarantiasDto", "consecutivo");
    		JsfBase.setFlashAttribute("idGarantia", idGarantia);
    		JsfBase.setFlashAttribute("idVenta", this.toFindIdKey(String.valueOf(idGarantia), "TcManticGarantiasDto", "venta"));
    		JsfBase.setFlashAttribute("accion", EAccion.CONSULTAR);
				regresar= "/Paginas/Mantic/Ventas/Garantias/accion".concat(Constantes.REDIRECIONAR);
				break;
			case 6: // CONTEOS
    		JsfBase.setFlashAttribute("idArticulo", consecutivo.toLong("idArticulo"));
				regresar= "/Paginas/Mantic/Catalogos/Inventarios/conteos".concat(Constantes.REDIRECIONAR);
				break;
		} // switch
 		JsfBase.setFlashAttribute("xcodigo", consecutivo.toString("propio"));
 		JsfBase.setFlashAttribute("retorno", "/Paginas/Mantic/Inventarios/Almacenes/kardex");
		return regresar;
	}

	private int toLoadAnaqueles(int count, TreeNode root, String anaquel, List<UISelectEntity> list) {
		int regresar   = count;
		TreeNode pivote= null;
		if(Cadena.isVacio(anaquel))
			regresar++;
		else
			while(regresar< list.size() && anaquel.equals(list.get(regresar).toString("anaquel"))) {
				if(pivote== null)
					pivote= new DefaultTreeNode("anaquel", list.get(regresar), root);
				if(!Cadena.isVacio(list.get(regresar).toString("charola")))
					new DefaultTreeNode("charola", list.get(regresar), pivote);
				regresar++;
			} // while
		return regresar;
	}
	
	private int toLoadCuartos(int count, TreeNode root, String cuarto, List<UISelectEntity> list) {
		int regresar   = count;
		TreeNode pivote= null;
		if(Cadena.isVacio(cuarto))
			regresar++;
		else
			while(regresar< list.size() && cuarto.equals(list.get(regresar).toString("cuarto"))) {
				pivote  = new DefaultTreeNode("cuarto", list.get(regresar), root);
				regresar= this.toLoadAnaqueles(regresar, pivote, list.get(regresar).toString("anaquel"), list);
			} // while
		return regresar;
	}
	
	private void toLoadTreeAlmacen(TreeNode root, String piso, List<UISelectEntity> list) {
		int count      = 0;
		TreeNode pivote= null;
		while(count< list.size()) {
			while(count< list.size() && piso.equals(list.get(count).toString("piso"))) {
				pivote= new DefaultTreeNode("piso", list.get(count), root);
				count = this.toLoadCuartos(count, pivote, list.get(count).toString("cuarto"), list);
				if(count< list.size())
				  piso= list.get(count).toString("piso");
			} // while
		} // while
	}
	
  public void toLoadUbicaciones() {
		List<Columna> columns= null;
    try {
			this.tabPage= 4;
			columns= new ArrayList<>();
      columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("almacen", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("piso", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("cuarto", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("anaquel", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("charola", EFormatoDinamicos.MAYUSCULAS));
			List<UISelectEntity> list= (List<UISelectEntity>) UIEntity.build("VistaKardexDto", "ubicaciones", this.attrs, columns);
			if(list!= null && !list.isEmpty()) {
				UISelectEntity pivote= list.get(0);
        this.ubicaciones= new DefaultTreeNode(pivote, null);	
				this.toLoadTreeAlmacen(this.ubicaciones, pivote.toString("piso"), list);
			} // for
			this.toLoadPosicion();
		} // try
	  catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
		finally {
      Methods.clean(columns);
    }// finally		 
	}	
	
	public void doAssignUbicacion() {
		LOG.info("idUbicacion: "+ this.attrs.get("charola"));
		try {
			if(this.attrs.get("posicion")!= null) {
				TcManticAlmacenesArticulosDto articulo= (TcManticAlmacenesArticulosDto)DaoFactory.getInstance().findById(TcManticAlmacenesArticulosDto.class, ((UISelectEntity)this.attrs.get("posicion")).getKey());
				if(articulo!= null) {
					articulo.setIdAlmacenUbicacion(((UISelectEntity)this.attrs.get("charola")).getKey());
					if(DaoFactory.getInstance().update(articulo)> 0L)
						JsfBase.addMessage("Aviso", "Se cambi� con �xito la ubicaci�n del articulo !", ETipoMensaje.INFORMACION);
				} // if
			} // if
			else {
				TcManticArticulosDto global= (TcManticArticulosDto)DaoFactory.getInstance().findById(TcManticArticulosDto.class, (Long)this.attrs.get("idArticulo"));
				TcManticAlmacenesArticulosDto articulo= new TcManticAlmacenesArticulosDto(
					global.getMinimo(), //Double minimo, 
					-1L, // Long idAlmacenArticulo, 
					JsfBase.getIdUsuario(), // Long idUsuario, 
					((UISelectEntity)this.attrs.get("idAlmacen")).getKey(), // Long idAlmacen, 
					global.getMaximo(), // Double maximo, 
					((UISelectEntity)this.attrs.get("charola")).getKey(), // Long idAlmacenUbicacion, 
					(Long)this.attrs.get("idArticulo"), // Long idArticulo, 
					0D // Double stock
				);
				if(DaoFactory.getInstance().insert(articulo)> 0L)
  				JsfBase.addMessage("Aviso", "Se agreg� con �xito la ubicaci�n del articulo !", ETipoMensaje.INFORMACION);
				this.toLoadPosicion();
			} // if
		} // try
	  catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
	}
	
	private void toLoadPosicion() {
		List<Columna> columns= null;
    try {
			columns= new ArrayList<>();
      columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("stock", EFormatoDinamicos.NUMERO_SIN_DECIMALES));
      columns.add(new Columna("minimo", EFormatoDinamicos.NUMERO_SIN_DECIMALES));
      columns.add(new Columna("maximo", EFormatoDinamicos.NUMERO_SIN_DECIMALES));
      columns.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA));
			List<UISelectEntity> items= (List<UISelectEntity>) UIEntity.build("VistaKardexDto", "almacenes", this.attrs, columns);
			if(items!= null && !items.isEmpty())
        this.attrs.put("posicion", items.get(0));
		} // try
	  catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
		finally {
      Methods.clean(columns);
    }// finally
	}

}
