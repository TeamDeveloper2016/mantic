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
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Error;
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
import mx.org.kaana.mantic.inventarios.almacenes.beans.AdminKardex;
import mx.org.kaana.mantic.inventarios.almacenes.beans.TiposVentas;
import mx.org.kaana.mantic.inventarios.almacenes.reglas.Transaccion;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.StreamedContent;

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

	private AdminKardex adminKardex;
	private StreamedContent image;

	public AdminKardex getAdminKardex() {
		return adminKardex;
	}

	public void setAdminKardex(AdminKardex adminKardex) {
		this.adminKardex=adminKardex;
	}

	public StreamedContent getImage() {
		return image;
	}
	
	@Override
	@PostConstruct
	protected void init() {
  	this.attrs.put("buscaPorCodigo", false);
		this.attrs.put("costoMayorMenor", 0);
		this.adminKardex= new AdminKardex(-1L);
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
				this.image= LoadImages.getImage(articulo.toString("idArticulo"));
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
					RequestContext.getCurrentInstance().execute("jsKardex.callback("+ solicitado +");");
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
   			RequestContext.getCurrentInstance().execute("jsKardex.callback('"+ this.adminKardex.getTiposVentas()+ "');");
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
			if(event.getTab().getTitle().equals("Almacenes")) 
				this.toLoadAlmacenes();
	    else	
			  if(event.getTab().getTitle().equals("Historial")) 
				  this.toLoadHistorial();
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
			columns= new ArrayList<>();
      columns.add(new Columna("persona", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("costo", EFormatoDinamicos.NUMERO_SAT_DECIMALES));
      columns.add(new Columna("menudeo", EFormatoDinamicos.NUMERO_SAT_DECIMALES));
      columns.add(new Columna("medioMayoreo", EFormatoDinamicos.NUMERO_SAT_DECIMALES));
      columns.add(new Columna("mayoreo", EFormatoDinamicos.NUMERO_SAT_DECIMALES));
      columns.add(new Columna("limiteMedioMayoreo", EFormatoDinamicos.NUMERO_SAT_DECIMALES));
      columns.add(new Columna("limiteMayoreo", EFormatoDinamicos.NUMERO_SAT_DECIMALES));
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

	public void doUpdateUtilidad(Integer index, Double value) {
		this.adminKardex.toUpdateUtilidad(index, value);
	}
	
	public void doUpdateCosto(Double precio) {
		double value= ((Entity)this.attrs.get("articulo")).toDouble("value");
		this.attrs.put("costoMayorMenor", this.getCostoMayorMenor(value, precio));
		for (TiposVentas item : this.adminKardex.getTiposVentas()) {
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

}
