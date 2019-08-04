package mx.org.kaana.mantic.explorar.backing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.formato.Global;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.recurso.LoadImages;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.compras.ordenes.reglas.Descuentos;
import mx.org.kaana.mantic.explorar.comun.Pedido;
import mx.org.kaana.mantic.inventarios.almacenes.beans.AdminKardex;
import mx.org.kaana.mantic.inventarios.almacenes.reglas.Transaccion;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.primefaces.model.StreamedContent;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 26/05/2018
 *@time 02:19:46 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

@Named(value= "manticExplorarAccion")
@ViewScoped
public class Accion extends Pedido implements Serializable {

	private static final long serialVersionUID=-6770709196941718388L;
	private static final Log LOG=LogFactory.getLog(Accion.class);

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
		this.adminKardex= new AdminKardex(-1L, false);
		Entity articulo= (Entity)JsfBase.getFlashAttribute("articulo");
    this.attrs.put("retorno", JsfBase.getFlashAttribute("retorno")== null? "filtro": JsfBase.getFlashAttribute("retorno"));
		this.attrs.put("articulo", articulo);
		if(articulo!= null && !articulo.isEmpty()) {
			this.updateArticulo();
		} // if	
	}
	
	private void updateArticulo() {
		Entity articulo= (Entity)this.attrs.get("articulo");
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
					//UIBackingUtilities.execute("jsKardex.callback("+ solicitado +");");
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
				this.attrs.put("articulo", null);
				this.adminKardex.getTiposVentas().clear();
			} // if	
		} // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
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
	
	public String doAceptar() {
    Transaccion transaccion= null;
		EAccion eaccion        = EAccion.MODIFICAR;
    try {			
			Entity articulo= (Entity)this.attrs.get("articulo");
			// if (transaccion.ejecutar(eaccion)) {
			if (true) {
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
	
  public String	doCancelar() {
    return ((String)this.attrs.get("retorno")).concat(Constantes.REDIRECIONAR);
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

	public String doBusqueda(){
		String regresar               = null;
		String criterio               = null;
		List<UISelectEntity> articulos= null;
		try {			
			if(this.attrs.get("nombre")!= null && ((UISelectEntity)this.attrs.get("nombre")).getKey()> 0L){
				articulos= (List<UISelectEntity>) this.attrs.get("articulosFiltro");
				criterio= articulos.get(articulos.indexOf((UISelectEntity)this.attrs.get("nombre"))).toString("nombre");						
			} // if
  		else if(!Cadena.isVacio(this.attrs.get("nombreHidden"))) 
				criterio= this.attrs.get("nombreHidden").toString();		  			
			JsfBase.setFlashAttribute("criterio", criterio!= null ? criterio.toUpperCase() : criterio);
			regresar= "filtro".concat(Constantes.REDIRECIONAR);
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
		return regresar;
	} // doBusqueda
}
