package mx.org.kaana.mantic.compras.ordenes.backing;

import java.io.Serializable;
import java.sql.Date;
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
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.compras.ordenes.beans.OrdenCompra;
import mx.org.kaana.mantic.compras.ordenes.reglas.Transaccion;
import mx.org.kaana.mantic.compras.ordenes.beans.Articulo;
import mx.org.kaana.mantic.compras.ordenes.reglas.AdminOrdenes;


@Named(value= "manticComprasOrdendesAccion")
@ViewScoped
public class Accion extends IBaseAttribute implements Serializable {

  private static final long serialVersionUID = 327393488565639367L;
  private AdminOrdenes adminOrden;

	public AdminOrdenes getAdminOrden() {
		return adminOrden;
	}

	public void setAdminOrden(AdminOrdenes adminOrden) {
		this.adminOrden=adminOrden;
	}
	
	@PostConstruct
  @Override
  protected void init() {		
    try {
      this.attrs.put("isPesos", false);
      this.attrs.put("accion", JsfBase.getFlashAttribute("accion")== null? EAccion.AGREGAR: JsfBase.getFlashAttribute("accion"));
      this.attrs.put("idOrdenCompra", JsfBase.getFlashAttribute("idOrdenCompra")== null? -1L: JsfBase.getFlashAttribute("idOrdenCompra"));
			this.attrs.put("retorno", JsfBase.getFlashAttribute("retorno")== null? "filtro": JsfBase.getFlashAttribute("retorno"));
			this.attrs.put("cantidad", 1);
			this.attrs.put("precio", 10);
			this.attrs.put("sinIva", false);
			this.attrs.put("diferencia", "0");
			doLoad();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

  public void doLoad() {
    EAccion eaccion= null;
    try {
      eaccion= (EAccion) this.attrs.get("accion");
      this.attrs.put("nombreAccion", Cadena.letraCapital(eaccion.name()));
      switch (eaccion) {
        case AGREGAR:											
          this.adminOrden= new AdminOrdenes(new OrdenCompra(-1L));
          break;
        case MODIFICAR:					
          this.adminOrden= new AdminOrdenes((OrdenCompra)DaoFactory.getInstance().findById(OrdenCompra.class, (Long)this.attrs.get("idOrdenCompra")));
    			this.attrs.put("sinIva", this.adminOrden.getOrden().getIdSinIva().equals(1L));
          break;
      } // switch
			toLoadCatalog();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // doLoad

  public String doAceptar() {  
    Transaccion transaccion= null;
    String regresar        = null;
		EAccion eaccion        = null;
    try {			
			eaccion= (EAccion) this.attrs.get("accion");
			this.adminOrden.getOrden().setDescuentos(this.adminOrden.getTotales().getDescuentos());
			this.adminOrden.getOrden().setImpuestos(this.adminOrden.getTotales().getIva());
			this.adminOrden.getOrden().setSubtotal(this.adminOrden.getTotales().getSubTotal());
			this.adminOrden.getOrden().setTotal(this.adminOrden.getTotales().getTotal());
			transaccion = new Transaccion(this.adminOrden.getOrden(), this.adminOrden.getArticulos());
			if (transaccion.ejecutar(eaccion)) {
				regresar = this.attrs.get("retorno").toString().concat(Constantes.REDIRECIONAR);
				JsfBase.addMessage("Se ".concat(eaccion.equals(EAccion.AGREGAR) ? "agregó" : "modificó").concat(" la orden de compra."), ETipoMensaje.INFORMACION);
			} // if
			else 
				JsfBase.addMessage("Ocurrió un error al registrar la orden de compra.", ETipoMensaje.ERROR);      			
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    return regresar;
  } // doAccion

  public String doCancelar() {   
    return (String)this.attrs.get("retorno");
  } // doCancelar

	private void toLoadCatalog() {
		List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
    try {
			columns= new ArrayList<>();
			params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
			params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
      this.attrs.put("almacenes", (List<UISelectEntity>) UIEntity.build("TcManticAlmacenesDto", "almacenes", params, columns));
      columns.remove(0);
			columns.add(new Columna("razonSocial", EFormatoDinamicos.MAYUSCULAS));
      this.attrs.put("clientes", (List<UISelectEntity>) UIEntity.build("TcManticClientesDto", "sucursales", params, columns));
      this.attrs.put("proveedores", (List<UISelectEntity>) UIEntity.build("VistaOrdenesComprasDto", "moneda", params, columns));
			List<UISelectEntity> proveedores= (List<UISelectEntity>)this.attrs.get("proveedores");
			if(!proveedores.isEmpty()) 
				toLoadCondiciones(proveedores.get(0));
    } // try
    catch (Exception e) {
			JsfBase.addMessageError(e);
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    }// finally
	}

	private void toLoadCondiciones(UISelectEntity proveedor) {
		List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
    try {
			columns= new ArrayList<>();
      columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
  		params.put("idProveedor", proveedor.getKey());
      this.attrs.put("condiciones", (List<UISelectEntity>) UIEntity.build("VistaOrdenesComprasDto", "condiciones", params, columns));
			List<UISelectEntity> condiciones= (List<UISelectEntity>) this.attrs.get("condiciones");
			if(!condiciones.isEmpty())
				this.adminOrden.getOrden().setDescuento(condiciones.get(0).toString("descuento"));
			this.attrs.put("proveedor", proveedor);
			Calendar fechaEstimada= Calendar.getInstance();
			int tipoDia= proveedor.toInteger("idTipoDia");
			int dias   = proveedor.toInteger("dias");
	    fechaEstimada.set(Calendar.DATE, fechaEstimada.get(Calendar.DATE)+ dias);
			if(tipoDia== 2) {
		    fechaEstimada.add(Calendar.DATE, ((int)(dias/5)* 2));
			  int dia= fechaEstimada.get(Calendar.DAY_OF_WEEK);
			  dias= dia== Calendar.SUNDAY? 1: dia== Calendar.SATURDAY? 2: 0;
		    fechaEstimada.add(Calendar.DATE, dias);
			} // if
			this.adminOrden.getOrden().setEntregaEstimada(new Date(fechaEstimada.getTimeInMillis()));
    } // try
    catch (Exception e) {
			JsfBase.addMessageError(e);
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    }// finally
	}
	
	public void doUpdateProveedor() {
		try {
			List<UISelectEntity> proveedores= (List<UISelectEntity>)this.attrs.get("proveedores");
			toLoadCondiciones(proveedores.get(proveedores.indexOf((UISelectEntity)this.adminOrden.getOrden().getIkProveedor())));
		}	
	  catch (Exception e) {
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
  		params.put("idProveedor", ((UISelectEntity)this.attrs.get("proveedor")).getKey());
  		params.put("codigo", this.attrs.get("codigo"));
      this.attrs.put("articulos", (List<UISelectEntity>) UIEntity.build("VistaOrdenesComprasDto", "codigos", params, columns));
	    this.doUpdatePrecio(UIBackingUtilities.toFirstKeySelectEntity((List<UISelectEntity>)this.attrs.get("articulos")));
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
 
  private UISelectEntity toArticulo() {
  	List<UISelectEntity> articulos= (List<UISelectEntity>)this.attrs.get("articulos");
	  UISelectEntity articulo= (UISelectEntity)this.attrs.get("articulo");
		return articulos== null? new UISelectEntity(new Entity(-1L)): articulos.indexOf(articulo)>= 0? articulos.get(articulos.indexOf(articulo)): articulos.isEmpty()? new UISelectEntity(new Entity(-1L)): articulos.get(0);
	}
	
	public void doAddArticulo() {
		try {
			UISelectEntity seleccionado= toArticulo();
			Long idOrdenDetalle= new Long((int)(Math.random()*10000));
			Articulo item= new Articulo(
				(Boolean)this.attrs.get("sinIva"),
				this.adminOrden.getOrden().getTipoDeCambio(),
				seleccionado.toString("nombre"), 
				seleccionado.toString("codigo"),
				(Double)this.attrs.get("precio"), 
        this.adminOrden.getOrden().getDescuento(), 
				-1L,
				this.adminOrden.getOrden().getExtras(), 
				0D,
				seleccionado.toString("codigo"),
				seleccionado.toDouble("iva"), 
				0D,
				0D,
				(Long)this.attrs.get("cantidad"), 
				-1* idOrdenDetalle, 
				seleccionado.toLong("idArticulo"), 
				0.0);
			this.adminOrden.add(item);
		} // try
	  catch (Exception e) {
			JsfBase.addMessageError(e);
    } // catch   
	}
	
	public void doRemoveArticulo() {
		try {
   		this.adminOrden.remove((Articulo)this.attrs.get("seleccionado"));
		} // try
	  catch (Exception e) {
			JsfBase.addMessageError(e);
    } // catch   
	}

  public void doUpdatePrecio() {
		this.doUpdatePrecio(toArticulo());
	} 
	
  public void doUpdatePrecio(UISelectEntity seleccionado) {
		try {
   		this.attrs.put("precio", seleccionado.toDouble("precio"));
			this.doUpdateDiferencia();
		} // try
	  catch (Exception e) {
			JsfBase.addMessageError(e);
    } // catch   
	} 
	
	public void doUpdateIvaTipoDeCambio() {
		boolean sinIva= (boolean)this.attrs.get("sinIva");
		this.adminOrden.getOrden().setIdSinIva(sinIva? 1L: 2L);
		this.adminOrden.toCalculate(true, sinIva, this.adminOrden.getOrden().getTipoDeCambio());
	} 

  public void doUpdateDiferencia() {
		double precio    = (Double)this.attrs.get("precio");
		double diferencia= 0.0; 
		double costo     = precio== 0? 1: precio; 
		UISelectEntity articulo= this.toArticulo();
		if(articulo!= null && articulo.size()> 1)
		  costo= articulo.toDouble("precio");
    diferencia= precio* 100/ costo;
  	this.attrs.put("diferencia", Numero.toRedondear(diferencia- 100));
	} 
	
}