package mx.org.kaana.mantic.inventarios.devoluciones.backing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
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
import mx.org.kaana.mantic.inventarios.devoluciones.reglas.Transaccion;
import mx.org.kaana.mantic.comun.IBaseArticulos;
import mx.org.kaana.mantic.db.dto.TcManticNotasEntradasDto;
import mx.org.kaana.mantic.inventarios.devoluciones.beans.Devolucion;
import mx.org.kaana.mantic.inventarios.devoluciones.reglas.AdminDevoluciones;
import org.primefaces.event.TabChangeEvent;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 7/05/2018
 *@time 03:29:13 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

@Named(value= "manticInventariosDevolucionesAccion")
@ViewScoped
public class Accion extends IBaseArticulos implements Serializable {

  private static final long serialVersionUID = 327393488565639361L;

	private FormatLazyModel lazyArticulos;
	private EAccion accion;
	private boolean aplicar;

	public FormatLazyModel getLazyArticulos() {
		return lazyArticulos;
	}

	public boolean isAplicar() {
		return aplicar;
	}

	public void setAplicar(boolean aplicar) {
		this.aplicar=aplicar;
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
	
  @Override
	@PostConstruct
  protected void init() {		
    try {
			this.attrs.put("xcodigo", JsfBase.getFlashAttribute("xcodigo"));	
			if(JsfBase.getFlashAttribute("accion")== null)
				UIBackingUtilities.execute("janal.isPostBack('cancelar')");
			this.aplicar= false;
      this.accion = JsfBase.getFlashAttribute("accion")== null? EAccion.AGREGAR: (EAccion)JsfBase.getFlashAttribute("accion");
      this.attrs.put("idDevolucion", JsfBase.getFlashAttribute("idDevolucion")== null? -1L: JsfBase.getFlashAttribute("idDevolucion"));
      this.attrs.put("idNotaEntrada", JsfBase.getFlashAttribute("idNotaEntrada")== null? -1L: JsfBase.getFlashAttribute("idNotaEntrada"));
			this.attrs.put("retorno", JsfBase.getFlashAttribute("retorno")== null? "filtro": JsfBase.getFlashAttribute("retorno"));
			doLoad();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

	@Override
  public void doLoad() {
    try {
      this.attrs.put("nombreAccion", Cadena.letraCapital(this.accion.name()));
			TcManticNotasEntradasDto nota= this.attrs.get("idNotaEntrada").equals(-1L)? new TcManticNotasEntradasDto(): (TcManticNotasEntradasDto)DaoFactory.getInstance().findById(TcManticNotasEntradasDto.class, (Long)this.attrs.get("idNotaEntrada"));
      switch (this.accion) {
        case AGREGAR:											
          this.setAdminOrden(new AdminDevoluciones(new Devolucion(-1L, (Long)this.attrs.get("idNotaEntrada")), nota.getTipoDeCambio(), nota.getIdSinIva(), this.accion));
          break;
        case MODIFICAR:					
        case CONSULTAR:					
          this.setAdminOrden(new AdminDevoluciones((Devolucion)DaoFactory.getInstance().toEntity(Devolucion.class, "TcManticDevolucionesDto", "detalle", this.attrs), nota.getTipoDeCambio(), nota.getIdSinIva(), this.accion));
          break;
      } // switch
			this.toLoadCatalog();
			this.doFilterRows();
			if(this.getAdminOrden().getFiltrados().isEmpty())
				UIBackingUtilities.execute("janal.alert('Ya no existen articulos que devolver asociados a esta nota de entrada !');janal.isPostBack('cancelar')");
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // doLoad

  public String doAceptar() {  
    Transaccion transaccion= null;
    String regresar        = null;
    try {			
			// this.getAdminOrden().toCheckTotales();
			((Devolucion)this.getAdminOrden().getOrden()).setDescuentos(this.getAdminOrden().getTotales().getDescuentos());
			((Devolucion)this.getAdminOrden().getOrden()).setImpuestos(this.getAdminOrden().getTotales().getIva());
			((Devolucion)this.getAdminOrden().getOrden()).setSubTotal(this.getAdminOrden().getTotales().getSubTotal());
			((Devolucion)this.getAdminOrden().getOrden()).setTotal(this.getAdminOrden().getTotales().getTotal());
			this.getAdminOrden().toAdjustArticulos();
			transaccion = new Transaccion(((Devolucion)this.getAdminOrden().getOrden()), this.getAdminOrden().getArticulos(), this.aplicar);
			if (transaccion.ejecutar(this.accion)) {
				if(this.accion.equals(EAccion.AGREGAR)) {
 				  regresar = this.attrs.get("retorno").toString().concat(Constantes.REDIRECIONAR);
   			  UIBackingUtilities.execute("jsArticulos.back('gener\\u00F3 la devolución ', '"+ ((Devolucion)this.getAdminOrden().getOrden()).getConsecutivo()+ "');");
				} // if	
 				if(!this.accion.equals(EAccion.CONSULTAR)) 
  				JsfBase.addMessage("Se ".concat(this.accion.equals(EAccion.AGREGAR) ? "agregó" : this.accion.equals(EAccion.COMPLETO) ? "aplicó": "modificó").concat(" la devolución de la nota de entrada."), ETipoMensaje.INFORMACION);
  			JsfBase.setFlashAttribute("idDevolucion", ((Devolucion)this.getAdminOrden().getOrden()).getIdDevolucion());
			} // if
			else 
				JsfBase.addMessage("Ocurrió un error al registrar la devolución de la nota de entrada.", ETipoMensaje.ERROR);      			
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    return regresar;
  } // doAccion
	
	public String doAplicar() {  
  	this.aplicar= true;
		return this.doAceptar();
	}

  public String doCancelar() {   
	  JsfBase.setFlashAttribute("idDevolucion", ((Devolucion)this.getAdminOrden().getOrden()).getIdDevolucion());
		JsfBase.setFlashAttribute("xcodigo", this.attrs.get("xcodigo"));	
     return this.attrs.get("retorno")== null? "filtro": (String)this.attrs.get("retorno");
  } // doCancelar

	private void toLoadCatalog() {
		List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
    try {
			columns= new ArrayList<>();
      columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("razonSocial", EFormatoDinamicos.MAYUSCULAS));
			params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
			params.put("idNotaEntrada", this.attrs.get("idNotaEntrada"));
			if(this.attrs.get("idNotaEntrada")!= null) {
        columns.add(new Columna("total", EFormatoDinamicos.MONEDA_SAT_DECIMALES));
        this.attrs.put("notas", UIEntity.build("VistaDevolucionesDto", "notas", params, columns));
			  List<UISelectEntity> notas= (List<UISelectEntity>)this.attrs.get("notas");
			  if(!notas.isEmpty()) 
				  ((Devolucion)this.getAdminOrden().getOrden()).setIkNotaEntrada(notas.get(0));
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

	public void doTabChange(TabChangeEvent event) {
		if(event.getTab().getTitle().equals("Costo actual")) 
      this.loadCostoActual();
		else
  		if(event.getTab().getTitle().equals("Costo anterior")) 
        this.loadCostoAnterior();
	}

	private void loadCostoActual() {
    List<Columna> columns= null;
    try {
      columns = new ArrayList<>();
      columns.add(new Columna("propio", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("codigo", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("cantidad", EFormatoDinamicos.NUMERO_CON_DECIMALES));      
      columns.add(new Columna("costo", EFormatoDinamicos.MONEDA_SAT_DECIMALES));      
      columns.add(new Columna("menudeo", EFormatoDinamicos.MONEDA_SAT_DECIMALES));
      columns.add(new Columna("medioMayoreo", EFormatoDinamicos.MONEDA_SAT_DECIMALES));
      columns.add(new Columna("mayoreo", EFormatoDinamicos.MONEDA_SAT_DECIMALES));
      columns.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA));
      this.attrs.put("sortOrder", "order by tc_mantic_notas_detalles.nombre");
      this.lazyArticulos= new FormatCustomLazy("VistaDevolucionesDto", "articulos", this.attrs, columns);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			throw e;
		} // catch
		finally {
			Methods.clean(columns);
		} // finally
	}
		
	private void loadCostoAnterior() {
    List<Columna> columns= null;
    try {
      columns = new ArrayList<>();
      columns.add(new Columna("propio", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("codigo", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("cantidad", EFormatoDinamicos.NUMERO_CON_DECIMALES));      
      columns.add(new Columna("costo", EFormatoDinamicos.MONEDA_SAT_DECIMALES));      
      columns.add(new Columna("menudeo", EFormatoDinamicos.MONEDA_SAT_DECIMALES));
      columns.add(new Columna("medioMayoreo", EFormatoDinamicos.MONEDA_SAT_DECIMALES));
      columns.add(new Columna("mayoreo", EFormatoDinamicos.MONEDA_SAT_DECIMALES));
      columns.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA));
      this.attrs.put("sortOrder", "order by tc_mantic_notas_detalles.nombre");
      this.lazyModel = new FormatCustomLazy("VistaDevolucionesDto", "bitacora", this.attrs, columns);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			throw e;
		} // catch
		finally {
			Methods.clean(columns);
		} // finally
	}

}