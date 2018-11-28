package mx.org.kaana.mantic.catalogos.articulos.backing;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ESql;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UISelect;
import mx.org.kaana.libs.pagina.UISelectItem;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.articulos.beans.ArticuloCodigo;
import mx.org.kaana.mantic.catalogos.articulos.beans.RegistroArticulo;
import mx.org.kaana.mantic.catalogos.articulos.reglas.Transaccion;

@Named(value = "manticCatalogosArticulosExpress")
@ViewScoped
public class Express extends IBaseAttribute implements Serializable {

  private static final long serialVersionUID = 327393488565639367L;
  private RegistroArticulo registroArticulo;		
	
  public RegistroArticulo getRegistroArticulo() {
    return registroArticulo;
  }

  public void setRegistroArticulo(RegistroArticulo registroArticulo) {
    this.registroArticulo = registroArticulo;
  }	
	
  @PostConstruct
  @Override
  protected void init() {		
    try {
			this.attrs.put("seleccionado", new Entity(-1L));				
			this.attrs.put("idArticulo", JsfBase.getFlashAttribute("idArticulo")== null? -1L: JsfBase.getFlashAttribute("idArticulo"));
			this.attrs.put("accion", JsfBase.getFlashAttribute("accion")== null || JsfBase.getFlashAttribute("idArticulo")== null? EAccion.AGREGAR: JsfBase.getFlashAttribute("accion"));				
      doLoad();
      loadProveedores();
      loadCategorias();
      loadEmpaques();
      doLoadUnidadesMedidas();
      loadGrupos();
      loadTiposVentas();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

	public void doPrepare(String idArticulo){
		try {
			this.attrs.put("idArticulo", idArticulo);
			this.attrs.put("accion", EAccion.MODIFICAR);
			doLoad();
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
	} // doPrepare
	
  public void doLoad() {
    EAccion eaccion= null;
    try {			
      eaccion= (EAccion) this.attrs.get("accion");
      this.attrs.put("nombreAccion", Cadena.letraCapital(eaccion.name()));
      switch (eaccion) {
        case AGREGAR:
          this.registroArticulo = new RegistroArticulo();
          break;
        case MODIFICAR:
        case CONSULTAR:
        case COPIAR:
          this.registroArticulo = new RegistroArticulo((Long)this.attrs.get("idArticulo"));
          break;
      } // switch
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // doLoad

  public void doAceptar() {
    Transaccion transaccion= null;
    try {
			prepareRegistro();
      transaccion = new Transaccion(this.registroArticulo);
      if (transaccion.ejecutar(EAccion.COPIAR)) 
				JsfBase.addMessage("Se registro el artículo de forma correcta.", ETipoMensaje.INFORMACION);
      else
				JsfBase.addMessage("Ocurrió un error al registrar el artículo", ETipoMensaje.ERROR);						
			this.registroArticulo= new RegistroArticulo();
			this.attrs.put("codigo", "");
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch    
  } // doAccion

	private void prepareRegistro() {	
		ArticuloCodigo codigo= null;
		try {			
			codigo= new ArticuloCodigo(-1L, ESql.INSERT, true);
			codigo.setCodigo((String) this.attrs.get("codigo"));
			codigo.setIdPrincipal(1L);
			codigo.setIdUsuario(JsfBase.getIdUsuario());
			codigo.setOrden(1L);
			this.registroArticulo.getArticulosCodigos().clear();
			this.registroArticulo.getArticulosCodigos().add(codigo);
			this.registroArticulo.getArticulo().setIdEmpresa(JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
			this.registroArticulo.getArticulo().setIdEmpaqueUnidadMedida(1L);
			this.registroArticulo.getArticulo().setIdRedondear(2L);
			this.registroArticulo.getArticulo().setLimiteMayoreo(20D);
			this.registroArticulo.getArticulo().setLimiteMedioMayoreo(10D);			
			this.registroArticulo.getArticulo().setIdVigente(1L);					
		} // try
		catch (Exception e) {			
			throw e;
		} // catch	
	} // prepareRegistro
	
  public String doCancelar() {
    if (((EAccion) this.attrs.get("accion")).equals(EAccion.AGREGAR)) 
      this.registroArticulo.doCancelar();    
    return "filtro";
  } // doAccion

  private void loadEmpaques() {
    List<UISelectItem> empaques= null;
    Map<String, Object> params = null;
    EAccion eaccion = null;
    try {
      params = new HashMap<>();
      params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
      empaques = UISelect.build("TcManticEmpaquesDto", "row", params, "nombre", EFormatoDinamicos.LIBRE, Constantes.SQL_TODOS_REGISTROS);
      this.attrs.put("empaques", empaques);
      eaccion = (EAccion) this.attrs.get("accion");
      if (eaccion.equals(EAccion.AGREGAR)) 
        this.registroArticulo.setIdEmpaque((Long) UIBackingUtilities.toFirstKeySelectItem(empaques));      
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    finally {
      Methods.clean(params);
    } // finally
  } // loadEmpaques

  public void doLoadUnidadesMedidas() {
    List<UISelectItem> unidadesMedidas= null;
    Map<String, Object> params        = null;
    EAccion eaccion                   = null;
    try {
      params = new HashMap<>();
      params.put("idEmpaque", this.registroArticulo.getIdEmpaque());
      eaccion = (EAccion) this.attrs.get("accion");
      unidadesMedidas = UISelect.build("VistaEmpaqueUnidadMedidaDto", "empaqueUnidadMedida", params, "nombre", EFormatoDinamicos.LIBRE, Constantes.SQL_TODOS_REGISTROS);
      this.attrs.put("unidadesMedidas", unidadesMedidas);
      if (eaccion.equals(EAccion.AGREGAR)) 
        this.registroArticulo.getArticulo().setIdEmpaqueUnidadMedida((Long) UIBackingUtilities.toFirstKeySelectItem(unidadesMedidas));      
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    finally {
      Methods.clean(params);
    } // finally
  } // doLoadUnidadesMedidas

  private void loadCategorias() {
    List<UISelectItem> categorias= null;
    Map<String, Object> params   = null;
    EAccion eaccion              = null;
    try {
      params = new HashMap<>();
      params.put(Constantes.SQL_CONDICION, "id_empresa=" + JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
      categorias = UISelect.build("TcManticCategoriasDto", "row", params, "traza", EFormatoDinamicos.LIBRE, Constantes.SQL_TODOS_REGISTROS);
      this.attrs.put("categorias", categorias);
      eaccion = (EAccion) this.attrs.get("accion");
      if (eaccion.equals(EAccion.AGREGAR) && !categorias.isEmpty()) 
        this.registroArticulo.getArticulo().setIdCategoria((Long) UIBackingUtilities.toFirstKeySelectItem(categorias));
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    finally {
      Methods.clean(params);
    } // finally
  } // loadCategorias

  private void loadProveedores() {
    List<UISelectItem> proveedores= null;
    Map<String, Object> params    = null;
    try {
      params = new HashMap<>();
      params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
      proveedores = UISelect.build("TcManticProveedoresDto", "sucursales", params, "razonSocial", EFormatoDinamicos.MAYUSCULAS, Constantes.SQL_TODOS_REGISTROS);
      this.attrs.put("proveedoresGeneral", proveedores);
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    finally {
      Methods.clean(params);
    } // finally
  } // loadProveedores

  private void loadGrupos() {
    List<UISelectItem> grupos= null;
    Map<String, Object> params = null;
    try {
      params = new HashMap<>();
      params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
      grupos = UISelect.build("TcManticGruposDto", "row", params, "nombre", EFormatoDinamicos.MAYUSCULAS, Constantes.SQL_TODOS_REGISTROS);
      this.attrs.put("gruposGeneral", grupos);
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    finally {
      Methods.clean(params);
    } // finally
  } // loadClientes

  private void loadTiposVentas() {
    List<UISelectItem> tiposVentas= null;
    Map<String, Object> params    = null;
    try {
      params = new HashMap<>();
      params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
      tiposVentas = UISelect.build("TcManticTiposVentasDto", "row", params, "nombre", EFormatoDinamicos.MAYUSCULAS, Constantes.SQL_TODOS_REGISTROS);
      this.attrs.put("tiposVentasGeneral", tiposVentas);
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    finally {
      Methods.clean(params);
    } // finally
  } // loadClientes	
	
	public void doActualizaPrecios(){
		try {
			if(this.registroArticulo.getArticulo().getPrecio()!= null){
				this.registroArticulo.getArticulo().setMenudeo(this.registroArticulo.getArticulo().getPrecio() + (this.registroArticulo.getArticulo().getPrecio() * .5));
				this.registroArticulo.getArticulo().setMedioMayoreo(this.registroArticulo.getArticulo().getPrecio() + (this.registroArticulo.getArticulo().getPrecio() * .4));
				this.registroArticulo.getArticulo().setMayoreo(this.registroArticulo.getArticulo().getPrecio() + (this.registroArticulo.getArticulo().getPrecio() * .3));
			} // if
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
	}
	
	public void doUpdateArticuloExpress() {
		if(this.attrs.get("seleccionado")!= null) {
			Entity entity= (Entity)this.attrs.get("seleccionado");
			if(entity.containsKey("idListaPrecio")) {
				this.attrs.put("codigo", entity.toString("codigo"));
				this.registroArticulo.getArticulo().setDescripcion(entity.toString("descripcion"));
				this.registroArticulo.getArticulo().setNombre(entity.toString("descripcion"));
				this.registroArticulo.getArticulo().setPrecio(entity.toDouble("costo"));
				this.doActualizaPrecios();
			} // if
			else {
				this.attrs.put("codigo", entity.toString("propio"));
				this.registroArticulo.getArticulo().setSat(entity.toString("sat"));
				this.registroArticulo.getArticulo().setDescripcion(entity.toString("descripcion"));
				this.registroArticulo.getArticulo().setNombre(entity.toString("nombre"));
				this.registroArticulo.getArticulo().setPrecio(entity.toDouble("precio"));
				this.registroArticulo.getArticulo().setMenudeo(entity.toDouble("menudeo"));
				this.registroArticulo.getArticulo().setMedioMayoreo(entity.toDouble("medioMayoreo"));
				this.registroArticulo.getArticulo().setMayoreo(entity.toDouble("mayoreo"));
			} // else	
		} // if
	}	
}
