package mx.org.kaana.mantic.catalogos.articulos.backing;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UISelect;
import mx.org.kaana.libs.pagina.UISelectItem;
import mx.org.kaana.libs.recurso.LoadImages;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.articulos.beans.RegistroArticulo;
import mx.org.kaana.mantic.catalogos.articulos.reglas.Transaccion;
import org.primefaces.model.StreamedContent;

@Named(value = "manticCatalogosArticulosAccion")
@ViewScoped
public class Accion extends IBaseAttribute implements Serializable {

  private static final long serialVersionUID = 327393488565639367L;
  private RegistroArticulo registroArticulo;
	private StreamedContent image;

  public RegistroArticulo getRegistroArticulo() {
    return registroArticulo;
  }

  public void setRegistroArticulo(RegistroArticulo registroArticulo) {
    this.registroArticulo = registroArticulo;
  }

	public StreamedContent getImage() {
		return image;
	}
	
  @PostConstruct
  @Override
  protected void init() {
    try {
      this.attrs.put("accion", JsfBase.getFlashAttribute("accion"));
      this.attrs.put("idArticulo", JsfBase.getFlashAttribute("idArticulo"));
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

  public void doLoad() {
    EAccion eaccion= null;
    Long idArticulo= -1L;
    try {
      eaccion= (EAccion) this.attrs.get("accion");
      this.attrs.put("nombreAccion", Cadena.letraCapital(eaccion.name()));
      switch (eaccion) {
        case AGREGAR:
          this.registroArticulo = new RegistroArticulo();
					this.image= LoadImages.getImage("-1");
          break;
        case MODIFICAR:
        case CONSULTAR:
        case COPIAR:
          idArticulo = Long.valueOf(this.attrs.get("idArticulo").toString());
          this.registroArticulo = new RegistroArticulo(idArticulo);
					this.image= LoadImages.getImage(this.registroArticulo.getArticulo().getIdEmpresa().toString(), Cadena.isVacio(this.registroArticulo.getImportado().getName()) ? "" : this.registroArticulo.getImportado().getName().substring(0, this.registroArticulo.getImportado().getName().lastIndexOf(".")));
          break;
      } // switch
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
      transaccion = new Transaccion(this.registroArticulo);
      if (transaccion.ejecutar((EAccion) this.attrs.get("accion"))) {
        regresar = "filtro".concat(Constantes.REDIRECIONAR);
        JsfBase.addMessage("Se registro el artículo de forma correcta.", ETipoMensaje.INFORMACION);
      } // if
      else 
        JsfBase.addMessage("Ocurrió un error al registrar el artículo", ETipoMensaje.ERROR);      
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    return regresar;
  } // doAccion

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
      if (eaccion.equals(EAccion.AGREGAR)) 
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
}
