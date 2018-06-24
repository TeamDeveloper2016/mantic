package mx.org.kaana.mantic.inventarios.entradas.backing;

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
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Cifrar;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.inventarios.entradas.beans.NotaEntrada;
import mx.org.kaana.mantic.inventarios.entradas.reglas.AdminNotas;
import mx.org.kaana.mantic.inventarios.entradas.reglas.Transaccion;
import mx.org.kaana.mantic.compras.ordenes.enums.EOrdenes;
import mx.org.kaana.mantic.comun.IBaseArticulos;
import mx.org.kaana.mantic.db.dto.TcManticOrdenesComprasDto;
import org.primefaces.context.RequestContext;
import org.primefaces.event.TabChangeEvent;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 7/05/2018
 *@time 03:29:13 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

@Named(value= "manticInventariosEntradasAccion")
@ViewScoped
public class Accion extends IBaseArticulos implements Serializable {

  private static final long serialVersionUID = 327393488565639367L;
	private EAccion accion;	
	private EOrdenes tipoOrden;
	private boolean aplicar;

	public String getValidacion() {
		return this.tipoOrden.equals(EOrdenes.NORMAL)? "libre": "requerido";
	}
	
	public String getTitulo() {
		return this.tipoOrden.equals(EOrdenes.NORMAL)? "(DIRECTA)": "";
	}

	public Boolean getIsAplicar() {
		Boolean regresar= true;
		try {
			regresar= JsfBase.isAdminEncuestaOrAdmin();
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
		return regresar;
	}
	
	public String getAgregar() {
		return this.accion.equals(EAccion.AGREGAR)? "none": "";
	}
	
	@PostConstruct
  @Override
  protected void init() {		
    try {
			this.aplicar  =  false;
			this.tipoOrden= JsfBase.getParametro("zOyOxDwIvGuCt")== null || JsfBase.getFlashAttribute("idOrdenCompra")== null? EOrdenes.NORMAL: EOrdenes.valueOf(Cifrar.descifrar(JsfBase.getParametro("zOyOxDwIvGuCt")));
      this.accion   = JsfBase.getFlashAttribute("accion")== null? EAccion.AGREGAR: (EAccion)JsfBase.getFlashAttribute("accion");
      this.attrs.put("idNotaEntrada", JsfBase.getFlashAttribute("idNotaEntrada")== null? -1L: JsfBase.getFlashAttribute("idNotaEntrada"));
      this.attrs.put("idOrdenCompra", JsfBase.getFlashAttribute("idOrdenCompra")== null? 1L: JsfBase.getFlashAttribute("idOrdenCompra"));
			this.attrs.put("retorno", JsfBase.getFlashAttribute("retorno")== null? "filtro": JsfBase.getFlashAttribute("retorno"));
      this.attrs.put("isPesos", false);
			this.attrs.put("sinIva", false);
			this.attrs.put("buscaPorCodigo", false);
			doLoad();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

  public void doLoad() {
    try {
      this.attrs.put("nombreAccion", Cadena.letraCapital(this.accion.name()));
      switch (this.accion) {
        case AGREGAR:											
          this.setAdminOrden(new AdminNotas(new NotaEntrada(-1L, (Long)this.attrs.get("idOrdenCompra")), this.tipoOrden));
          TcManticOrdenesComprasDto ordenCompra= (TcManticOrdenesComprasDto)DaoFactory.getInstance().findById(TcManticOrdenesComprasDto.class, (Long)this.attrs.get("idOrdenCompra"));
					((NotaEntrada)this.getAdminOrden().getOrden()).setIkAlmacen(new UISelectEntity(new Entity(ordenCompra.getIdAlmacen())));
					((NotaEntrada)this.getAdminOrden().getOrden()).setIkProveedor(new UISelectEntity(new Entity(ordenCompra.getIdProveedor())));
          break;
        case MODIFICAR:					
        case CONSULTAR:					
          this.setAdminOrden(new AdminNotas((NotaEntrada)DaoFactory.getInstance().toEntity(NotaEntrada.class, "TcManticNotasEntradasDto", "detalle", this.attrs), this.tipoOrden));
    			this.attrs.put("sinIva", this.getAdminOrden().getIdSinIva().equals(1L));
          break;
      } // switch
			this.toLoadCatalog();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // doLoad

  public String doAplicar() {  
		this.aplicar= true;
		return this.doAceptar();
	}
	
  public String doAceptar() {  
    Transaccion transaccion= null;
    String regresar        = null;
    try {			
			((NotaEntrada)this.getAdminOrden().getOrden()).setDescuentos(this.getAdminOrden().getTotales().getDescuentos());
			((NotaEntrada)this.getAdminOrden().getOrden()).setImpuestos(this.getAdminOrden().getTotales().getIva());
			((NotaEntrada)this.getAdminOrden().getOrden()).setSubTotal(this.getAdminOrden().getTotales().getSubTotal());
			((NotaEntrada)this.getAdminOrden().getOrden()).setTotal(this.getAdminOrden().getTotales().getTotal());
			this.getAdminOrden().toAdjustArticulos();
			transaccion = new Transaccion(((NotaEntrada)this.getAdminOrden().getOrden()), this.getAdminOrden().getArticulos(), this.aplicar);
			if (transaccion.ejecutar(this.accion)) {
				if(this.accion.equals(EAccion.AGREGAR) || this.aplicar) {
 				  regresar = this.attrs.get("retorno").toString().concat(Constantes.REDIRECIONAR);
					if(this.accion.equals(EAccion.AGREGAR))
    			  RequestContext.getCurrentInstance().execute("jsArticulos.back('generó la nota de entrada', '"+ ((NotaEntrada)this.getAdminOrden().getOrden()).getConsecutivo()+ "');");
					else
   			    RequestContext.getCurrentInstance().execute("jsArticulos.back('aplicó la nota de entrada', '"+ ((NotaEntrada)this.getAdminOrden().getOrden()).getConsecutivo()+ "');");
				} // if	
 				if(!this.accion.equals(EAccion.CONSULTAR)) 
  				JsfBase.addMessage("Se ".concat(this.accion.equals(EAccion.AGREGAR) ? "agregó" : "modificó").concat(" la nota de entrada."), ETipoMensaje.INFORMACION);
  			JsfBase.setFlashAttribute("idNotaEntrada", ((NotaEntrada)this.getAdminOrden().getOrden()).getIdNotaEntrada());
			} // if
			else 
				JsfBase.addMessage("Ocurrió un error al registrar la nota de entrada.", ETipoMensaje.ERROR);      			
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    return regresar;
  } // doAccion

  public String doCancelar() {   
  	JsfBase.setFlashAttribute("idNotaEntrada", ((NotaEntrada)this.getAdminOrden().getOrden()).getIdNotaEntrada());
    return (String)this.attrs.get("retorno");
  } // doCancelar

	private void toLoadCatalog() {
		List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
    try {
			columns= new ArrayList<>();
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
			params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
			params.put("idOrdenCompra", this.attrs.get("idOrdenCompra"));
      this.attrs.put("almacenes", UIEntity.build("TcManticAlmacenesDto", "almacenes", params, columns));
 			List<UISelectEntity> almacenes= (List<UISelectEntity>)this.attrs.get("almacenes");
			if(!almacenes.isEmpty() && this.accion.equals(EAccion.AGREGAR)) 
				((NotaEntrada)this.getAdminOrden().getOrden()).setIkAlmacen(almacenes.get(0));
      columns.remove(0);
			columns.add(new Columna("razonSocial", EFormatoDinamicos.MAYUSCULAS));
      this.attrs.put("proveedores", UIEntity.build("VistaOrdenesComprasDto", "moneda", params, columns));
			List<UISelectEntity> proveedores= (List<UISelectEntity>)this.attrs.get("proveedores");
			if(!proveedores.isEmpty()) {
				int index= proveedores.indexOf(((NotaEntrada)this.getAdminOrden().getOrden()).getIkProveedor());
				((NotaEntrada)this.getAdminOrden().getOrden()).setIkProveedor(proveedores.get(index));
			  this.attrs.put("proveedor", proveedores.get(index));
			} // if	
			if(this.attrs.get("idOrdenCompra")!= null) {
        this.attrs.put("ordenes", UIEntity.build("VistaNotasEntradasDto", "ordenes", params, columns));
			  List<UISelectEntity> ordenes= (List<UISelectEntity>)this.attrs.get("ordenes");
			  if(!ordenes.isEmpty()) 
				  ((NotaEntrada)this.getAdminOrden().getOrden()).setIkOrdenCompra(ordenes.get(0));
			} // if	
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

	public void doUpdateProveedor() {
		try {
			if(this.tipoOrden.equals(EOrdenes.PROVEEDOR)) {
				this.getAdminOrden().getArticulos().clear();
				this.getAdminOrden().toCalculate();
			} // if	
			List<UISelectEntity> proveedores= (List<UISelectEntity>)this.attrs.get("proveedores");
			UISelectEntity proveedor= ((NotaEntrada)this.getAdminOrden().getOrden()).getIkProveedor();
			this.attrs.put("proveedor", proveedores.get(proveedores.indexOf(proveedor)));
		}	
	  catch (Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
	} 

	public void doUpdateAlmacen() {
		if(this.tipoOrden.equals(EOrdenes.ALMACEN)) {
  		this.getAdminOrden().getArticulos().clear();
			this.getAdminOrden().toCalculate();
		} // if	
	}
	
	public void doTabChange(TabChangeEvent event) {
		if(event.getTab().getTitle().equals("Faltantes")) 
      this.toLoadFaltantes();
	}
	
}