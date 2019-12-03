package mx.org.kaana.mantic.inventarios.almacenes.backing;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
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
import mx.org.kaana.mantic.compras.ordenes.reglas.Descuentos;
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
  	this.attrs.put("retorno", JsfBase.getFlashAttribute("retorno")== null? "/Paginas/Mantic/Ventas/accion": JsfBase.getFlashAttribute("retorno"));
  	this.attrs.put("buscaPorCodigo", false);
		this.attrs.put("costoMayorMenor", 0);
  	this.attrs.put("redondear", false);
  	this.attrs.put("sat", Constantes.CODIGO_SAT);
  	this.attrs.put("ultimoCosto", 0.0D);
		this.adminKardex= new AdminKardex(-1L, false);
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
      columns.add(new Columna("registro", EFormatoDinamicos.DIA_FECHA_HORA_CORTA));
      columns.add(new Columna("actualizado", EFormatoDinamicos.DIA_FECHA_HORA_CORTA));
			this.attrs.put("idArticulo", null);
			if(articulo.size()> 1) {
				this.image= LoadImages.getImage(articulo.toLong("idArticulo"));
  			this.attrs.put("idArticulo", articulo.toLong("idArticulo"));
				Entity solicitado= (Entity)DaoFactory.getInstance().toEntity("VistaKardexDto", "row", this.attrs);
				if(solicitado!= null) {
					UIBackingUtilities.toFormatEntity(solicitado, columns);
					this.attrs.put("articulo", solicitado);
					this.attrs.put("redondear", solicitado.toLong("idRedondear")== 1L);
        	this.attrs.put("sat", solicitado.toString("sat"));
        	this.attrs.put("ultimo", solicitado.toString("actualizado"));
					Periodo periodo= new Periodo();
			    periodo.addMeses(-6);
			    this.attrs.put("registro", periodo.toString());
					Value ultimoCosto= DaoFactory.getInstance().toField("VistaKardexDto", "historial", this.attrs, "costo");
					if(ultimoCosto!= null && ultimoCosto.getData()!= null) 
						this.attrs.put("ultimoCosto", ultimoCosto.toDouble());
					else
					  this.attrs.put("ultimoCosto", solicitado.toDouble("precio"));
					this.attrs.put("precio", solicitado.toDouble("precio"));
					this.attrs.put("costoMayorMenor", this.getCostoMayorMenor(solicitado.toDouble("value"), solicitado.toDouble("precio")));
					Value ultimo= (Value)DaoFactory.getInstance().toField("TcManticArticulosBitacoraDto", "ultimo", this.attrs, "registro");
					if(ultimo!= null)
					  this.attrs.put("ultimo", Global.format(EFormatoDinamicos.DIA_FECHA_HORA_CORTA, ultimo.toTimestamp()));
					UIBackingUtilities.execute("jsKardex.callback("+ solicitado +");");
      		this.adminKardex= new AdminKardex(
						articulo.toLong("idArticulo"), 
						solicitado.toDouble("precio"), 
						solicitado.toDouble("iva"), 
						solicitado.toDouble("menudeo"), 
						solicitado.toDouble("medioMayoreo"), 
						solicitado.toDouble("mayoreo"), 
						solicitado.toLong("limiteMedioMayoreo"),
						solicitado.toLong("limiteMayoreo"),
						solicitado.toLong("idRedondear").equals(1L)
					);
					this.toUpdatePrecioVenta(false);
				} // if	
			} // if
			else {
				this.attrs.put("existe", "<span class='janal-color-orange'>EL ARTICULO NO EXISTE EN EL CATALOGO !</span>");
				this.attrs.put("articulo", null);
				this.attrs.put("redondear", false);
      	this.attrs.put("sat", Constantes.CODIGO_SAT);
       	this.attrs.put("ultimo", "");
				this.attrs.put("ultimoCosto", 0.0D);
				this.adminKardex.getTiposVentas().clear();
			} // if	
			this.toInventario();
			this.toLoadCodigos();
			this.toLoadAlmacenes();
			this.toLoadMovimientos();
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
			Entity articulo= (Entity)this.attrs.get("articulo");
			transaccion = new Transaccion((Long)this.attrs.get("idArticulo"), (Double)this.attrs.get("precio"), articulo.toString("descuento"), articulo.toString("extra"), this.adminKardex.getTiposVentas(), (String)this.attrs.get("sat"));
			if (transaccion.ejecutar(eaccion)) {
				JsfBase.addMessage("Se modificaron los precios de tipos de ventas del articulo.", ETipoMensaje.INFORMACION);
   			UIBackingUtilities.execute("jsKardex.callback('"+ this.adminKardex.getTiposVentas()+ "');");
				this.attrs.put("ultimo", Global.format(EFormatoDinamicos.DIA_FECHA_HORA_CORTA, new Timestamp(Calendar.getInstance().getTimeInMillis())));
			}	// if
			else 
				JsfBase.addMessage("Ocurrió un error al registrar los precios de los tipos de ventas del articulo.", ETipoMensaje.ERROR);      			
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
		return null;
	}
	
	public void doTabChange(TabChangeEvent event) {
		if(event.getTab().getTitle().equals("Códigos")) 
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
			this.attrs.put("seleccionado", articulo);
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
      columns.add(new Columna("stock", EFormatoDinamicos.NUMERO_CON_DECIMALES));
      columns.add(new Columna("minimo", EFormatoDinamicos.NUMERO_CON_DECIMALES));
      columns.add(new Columna("maximo", EFormatoDinamicos.NUMERO_CON_DECIMALES));
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
		this.toUpdatePrecioVenta(true);
	}
	
	public void doUpdateCosto(Double precio, Boolean keep) {
		Entity articulo= (Entity)this.attrs.get("articulo");
		double value   = articulo.toDouble("value");
		this.attrs.put("costoMayorMenor", this.getCostoMayorMenor(value, precio));
		articulo.getValue("calculado").setData(precio);
		articulo.getValue("precio").setData(precio);
		for (TiposVentas item: this.adminKardex.getTiposVentas()) {
			// ¿Quieres manter el porcentaje de utilidad?
  		if(!keep) {
				double costo   = Numero.toAjustarDecimales(item.getPrecio(), item.isRounded());
				double calculo = Numero.toRedondearSat((precio* ((item.getIva()/100)+ 1)));
				// al precio de neto se le quita el costo+ iva y lo que queda se calcula la utilidad bruta 
				item.setUtilidad(Numero.toRedondearSat((costo- calculo)* 100/ calculo));
			} // if	
			double calculo= Numero.toRedondearSat((precio* ((item.getIva()/100)+ 1)));
			item.setPrecio(Numero.toRedondearSat(((1+ (item.getUtilidad()/ 100))* calculo)));
			item.setCosto(precio);
  		item.toCalculate();
			this.toUpdatePrecioVenta(true);
		} // for
	}
	
	public void doCalculate(Integer index) {
		this.adminKardex.toCalculate(index);
		Entity articulo= (Entity)this.attrs.get("articulo");
		articulo.getValue("calculado").setData(this.adminKardex.getTiposVentas().get(0).getPrecio());
		this.toUpdatePrecioVenta(true);
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
			params.put("idAlmacen", JsfBase.getAutentifica().getEmpresa().getIdAlmacen());
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
			params.put("idAlmacen", JsfBase.getAutentifica().getEmpresa().getIdAlmacen());
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
			if(this.attrs.get("seleccionado")!= null) {
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
		UISelectEntity consecutivo= (UISelectEntity)this.attrs.get("consecutivo");
		List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
		List<UISelectEntity> documento= null;
    try {
			columns= new ArrayList<>();
      columns.add(new Columna("razonSocial", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("cantidad", EFormatoDinamicos.NUMERO_CON_DECIMALES));
      columns.add(new Columna("impuestos", EFormatoDinamicos.MONEDA_SAT_DECIMALES));
      columns.add(new Columna("precio", EFormatoDinamicos.MONEDA_SAT_DECIMALES));
      columns.add(new Columna("importe", EFormatoDinamicos.MONEDA_SAT_DECIMALES));
      columns.add(new Columna("total", EFormatoDinamicos.MONEDA_SAT_DECIMALES));
      columns.add(new Columna("fecha", EFormatoDinamicos.FECHA_HORA));
			switch(consecutivo.toLong("idTipoMovimiento").intValue()) {
				case 1: // ENTRADAS
					Long idNotaEntrada= this.toFindIdKey(consecutivo.toString("consecutivo"), "TcManticNotasEntradasDto", "consecutivo");
      		params.put("idNotaEntrada", idNotaEntrada);
					documento= (List<UISelectEntity>) UIEntity.build("VistaKardexDto", "notaEntrada", params, columns, Constantes.SQL_TODOS_REGISTROS);
          this.attrs.put("documentos", documento);
          this.attrs.put("tipoDocumento", "de la nota de entrada");
					break;
				case 2: // VENTAS
					Long idVenta= this.toFindIdKey(consecutivo.toString("consecutivo"), "TcManticVentasDto", "ticket");
      		params.put("idVenta", idVenta);
					documento= (List<UISelectEntity>) UIEntity.build("VistaKardexDto", "venta", params, columns, Constantes.SQL_TODOS_REGISTROS);
          this.attrs.put("documentos", documento);
          this.attrs.put("tipoDocumento", "de la venta");
					break;
				case 3: // DEVOLUCIONES
					Long idDevolucion= this.toFindIdKey(consecutivo.toString("consecutivo"), "TcManticDevolucionesDto", "consecutivo");
      		params.put("idDevolucion", idDevolucion);
					documento= (List<UISelectEntity>) UIEntity.build("VistaKardexDto", "devolucion", params, columns, Constantes.SQL_TODOS_REGISTROS);
          this.attrs.put("documentos", documento);
          this.attrs.put("tipoDocumento", "de la devolución");
					break;
				case 4: // TRASPASOS
          columns.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA));
          columns.add(new Columna("origen", EFormatoDinamicos.MAYUSCULAS));
          columns.add(new Columna("destino", EFormatoDinamicos.MAYUSCULAS));
					Long idTransferencia= this.toFindIdKey(consecutivo.toString("consecutivo"), "TcManticTransferenciasDto", "consecutivo");
      		params.put("idTransferencia", idTransferencia);
					documento= (List<UISelectEntity>) UIEntity.build("VistaKardexDto", "traspaso", params, columns, Constantes.SQL_TODOS_REGISTROS);
          this.attrs.put("documentos", documento);
          this.attrs.put("tipoDocumento", "del traspaso");
					break;
				case 5: // GARANTIAS
					Long idGarantia= this.toFindIdKey(consecutivo.toString("consecutivo"), "TcManticGarantiasDto", "consecutivo");
      		params.put("idGarantia", idGarantia);
					documento= (List<UISelectEntity>) UIEntity.build("VistaKardexDto", "garantia", params, columns, Constantes.SQL_TODOS_REGISTROS);
          this.attrs.put("documentos", documento);
          this.attrs.put("tipoDocumento", "de la garantía");
					break;
				case 6: // CONTEOS
          columns.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA));
          columns.add(new Columna("inicial", EFormatoDinamicos.NUMERO_CON_DECIMALES));
          columns.add(new Columna("salidas", EFormatoDinamicos.NUMERO_CON_DECIMALES));
          columns.add(new Columna("stock", EFormatoDinamicos.NUMERO_CON_DECIMALES));
					Long idArticulo= consecutivo.toLong("idArticulo");
      		params.put("idArticulo", idArticulo);
					documento= (List<UISelectEntity>) UIEntity.build("VistaKardexDto", "conteo", params, columns, Constantes.SQL_TODOS_REGISTROS);
          this.attrs.put("documentos", documento);
          this.attrs.put("tipoDocumento", "del conteo");
					break;
			} // switch
			if(documento!= null && !documento.isEmpty()) {
				documento.get(0).put("articulos", new Value("articulos", documento.size()));
        this.attrs.put("documento", documento.get(0));
			} // if	
      this.attrs.put("idTipoDocumento", consecutivo.toLong("idTipoMovimiento").intValue());
		} // try
	  catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
		finally {
      Methods.clean(columns);
      Methods.clean(params);
    } // finally
		return null;
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
						JsfBase.addMessage("Aviso", "Se cambió con éxito la ubicación del articulo !", ETipoMensaje.INFORMACION);
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
  				JsfBase.addMessage("Aviso", "Se agregó con éxito la ubicación del articulo !", ETipoMensaje.INFORMACION);
			} // if
			this.toLoadPosicion();
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

	private void toUpdatePrecioVenta(boolean keep) {
		Entity articulo= (Entity)this.attrs.get("articulo");
		if(keep) {
  		articulo.getValue("descuento").setData("0");
	  	articulo.getValue("extra").setData("0");
		} // if
    Descuentos descuentos= new Descuentos(articulo.toDouble("calculado"), articulo.toString("descuento"));
		double calculado= descuentos.getImporte();
		if(descuentos.getFactor()!= 0D)
		  calculado= descuentos.getImporte()* (1+ (1- descuentos.getFactor()));
		articulo.getValue("calculado").setData(Numero.toAjustarDecimales(calculado, this.adminKardex.getTiposVentas().get(0).isRounded()));
		articulo.getValue("menudeo").setData(this.adminKardex.getTiposVentas().get(0).getPrecio());
		articulo.getValue("utilidad").setData(this.adminKardex.getTiposVentas().get(0).getUtilidad());
	}
	
	public void doChangeRedondear() {
    Transaccion transaccion= null;
		EAccion eaccion        = EAccion.PROCESAR;
    try {			
			transaccion = new Transaccion((Long)this.attrs.get("idArticulo"), (Boolean)this.attrs.get("redondear")? 1L: 2L);
			if (transaccion.ejecutar(eaccion)) {
				JsfBase.addMessage("Se modificaron el tipo de redondeo del articulo.", ETipoMensaje.INFORMACION);
				for (TiposVentas item: this.adminKardex.getTiposVentas()) {
					item.setRounded((Boolean)this.attrs.get("redondear"));
				} // for
				this.doUpdateCosto((Double)this.attrs.get("precio"), true);
			}	// if
			else 
				JsfBase.addMessage("Ocurrió un error al hacer el cambio del tipo de redondeo.", ETipoMensaje.ERROR);      			
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
	}

	public void doChangeCodigoSat() {
    Transaccion transaccion= null;
		EAccion eaccion        = EAccion.COMPLEMENTAR;
    try {			
			transaccion = new Transaccion((Long)this.attrs.get("idArticulo"), (String)this.attrs.get("sat"));
			if (transaccion.ejecutar(eaccion)) 
				JsfBase.addMessage("Se modificó el código del SAT ["+ (String)this.attrs.get("sat")+ "] del articulo.", ETipoMensaje.INFORMACION);
			else 
				JsfBase.addMessage("Ocurrió un error al hacer el cambio del código del SAT.", ETipoMensaje.ERROR);      			
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
	}

	public void doAddCodigoAuxiliar() {
    Transaccion transaccion= null;
		EAccion eaccion        = EAccion.ASIGNAR;
    try {			
			transaccion = new Transaccion((Long)this.attrs.get("idArticulo"), (String)this.attrs.get("auxiliar"));
			if (transaccion.ejecutar(eaccion)) {
				JsfBase.addMessage("Se agregó el código auxiliar ["+ (String)this.attrs.get("auxiliar")+ "] al articulo.", ETipoMensaje.INFORMACION);
				this.toLoadCodigos();
				this.attrs.put("auxiliar", "");
			} // if	
			else 
				JsfBase.addMessage("Ocurrió un error al agregar el código auxiliar al articulo.", ETipoMensaje.ERROR);      			
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
	}
 
	public void doDeleteCodigoAlterno(UISelectEntity depurar) {
    Transaccion transaccion= null;
		EAccion eaccion        = EAccion.DEPURAR;
    try {			
			transaccion = new Transaccion(depurar.getKey(), "");
			if (transaccion.ejecutar(eaccion)) {
				JsfBase.addMessage("Se eliminó el código auxiliar ["+ depurar.toString("codigo")+ "] del articulo.", ETipoMensaje.INFORMACION);
				this.toLoadCodigos();
			} // if	
			else 
				JsfBase.addMessage("Ocurrió un error al eliminar el código auxiliar del articulo.", ETipoMensaje.ERROR);      			
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
	}

	public void doLookForCodigo(String id, String codigo, Long index) {
	  Map<String, Object> params= null;
		int count                 = 0;
		try {
			params=new HashMap<>();
			if(!Cadena.isVacio(codigo)) {
			  params.put("codigo", codigo.toUpperCase());
				params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
				params.put(Constantes.SQL_CONDICION, " tc_mantic_articulos_codigos.id_articulo!="+ this.attrs.get("idArticulo"));
			  List<Entity> values= (List<Entity>)DaoFactory.getInstance().toEntitySet("VistaArticulosDto", "existeCodigo", params);
				if(values!= null && values.size()> 0) {
					StringBuilder sb= new StringBuilder();
					sb.append("<br/>");
					for (Entity item : values) {
						if(item.toLong("idKey")!= null && item.toLong("idKey")>0){
							count++;
							sb.append("  [");
							sb.append(item.toString("codigo"));
							sb.append("]  ");
							sb.append(item.toString("nombre"));
							sb.append(" como  ");
							sb.append(item.toString("principal"));
							sb.append(".<br/>");
						} // if
					} // for
					if(count>0)
						JsfBase.addAlert("El código esta siendo utilizado por los siguientes articulos:".concat("<br/>").concat(sb.toString()), ETipoMensaje.ALERTA);
					//id= id.replaceAll("[:]+", "\\\\:").replaceAll("[:]+", "\\\\:");
					//UIBackingUtilities.execute("$('#"+ id+ "').val('');$('#"+ id+ "').focus();");
				} // if	
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
	
  public String	doRegresar() {
    return ((String)this.attrs.get("retorno")).concat(Constantes.REDIRECIONAR);
	}
	
	public void doAlmacen() {
    Transaccion transaccion= null;
    try {			
  	  UISelectEntity almacen= (UISelectEntity)this.attrs.get("identificado");
			almacen.put("min", new Value("min", this.attrs.get("min")));
			almacen.put("max", new Value("max", this.attrs.get("max")));
			transaccion = new Transaccion(almacen);
			if (transaccion.ejecutar(EAccion.MOVIMIENTOS)) {
				JsfBase.addMessage("Se modificaron los umbrales del articulo.", ETipoMensaje.INFORMACION);
  			this.toLoadAlmacenes();
			} // if
			else 
				JsfBase.addMessage("Ocurrió un error al registrar los umbrales del articulo.", ETipoMensaje.ERROR);      			
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
	}

	public void doUpdateUmbrales() {
 	  UISelectEntity almacen= (UISelectEntity)this.attrs.get("identificado");
		this.attrs.put("min", almacen.toDouble("min"));
		this.attrs.put("max", almacen.toDouble("max"));
	}

}
