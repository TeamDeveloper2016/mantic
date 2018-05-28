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
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.formato.Global;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.inventarios.almacenes.beans.AdminKardex;
import org.primefaces.context.RequestContext;
import org.primefaces.event.TabChangeEvent;

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

	public AdminKardex getAdminKardex() {
		return adminKardex;
	}

	public void setAdminKardex(AdminKardex adminKardex) {
		this.adminKardex=adminKardex;
	}
	
	@Override
	@PostConstruct
	protected void init() {
		this.adminKardex= new AdminKardex(-1L, 1D, 16D, 0D, 0D, 0D, 10L, 20L);
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
      columns.add(new Columna("utilidad", EFormatoDinamicos.NUMERO_CON_DECIMALES));
      columns.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA));
			if(articulo.size()> 1) {
  			this.attrs.put("idArticulo", articulo.toLong("idArticulo"));
				Entity solicitado= (Entity)DaoFactory.getInstance().toEntity("VistaKardexDto", "row", this.attrs);
				if(solicitado!= null) {
					UIBackingUtilities.toFormatEntity(solicitado, columns);
					this.attrs.put("articulo", solicitado);
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
					this.toLoadCodigos();
					this.toLoadAlmacenes();
				} // if	
			} // if
			else
				this.attrs.put("existe", "<span class='janal-color-orange'>EL ARTICULO NO EXISTE EN EL CATALOGO !</span>");
		} // try
		finally {
      Methods.clean(columns);
    }// finally
	}
	
	public String doMoneyFormat(Double value) {
		return value== null? "": Global.format(EFormatoDinamicos.MONEDA_CON_DECIMALES, value);
	}
	
	public String doMoneyFormat(Value value) {
		return value== null? "": this.doMoneyFormat(value.toDouble());
	}
	
	public String doPercenageFormat(Value value) {
		return value== null? "": Global.format(EFormatoDinamicos.NUMERO_CON_DECIMALES, value.toDouble())+ "%";
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
    try {
			columns= new ArrayList<>();
      columns.add(new Columna("codigo", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
  		params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
  		params.put("idProveedor", -1L);
			String search= (String)this.attrs.get("codigo"); 
			if(!Cadena.isVacio(search)) 
				search= search.trim().toUpperCase().replaceAll("(,| |\\t)+", ".*.*");
			else
				search= "WXYZ";
  		params.put("codigo", search);
      this.attrs.put("articulos", (List<UISelectEntity>) UIEntity.build("VistaOrdenesComprasDto", "codigos", params, columns, 20L));
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
		return null;
	}
	
	public String doCancelar() {
		return null;
	}
	
	public void doTabChange(TabChangeEvent event) {
		if(event.getTab().getTitle().equals("Códigos")) 
			this.toLoadCodigos();
		else
			if(event.getTab().getTitle().equals("Almacenes")) 
				this.toLoadAlmacenes();
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
	
}
