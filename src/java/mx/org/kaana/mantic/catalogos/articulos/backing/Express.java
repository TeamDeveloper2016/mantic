package mx.org.kaana.mantic.catalogos.articulos.backing;

import java.io.Serializable;
import java.util.ArrayList;
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
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelect;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.pagina.UISelectItem;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.articulos.beans.ArticuloCodigo;
import mx.org.kaana.mantic.catalogos.articulos.beans.RegistroArticulo;
import mx.org.kaana.mantic.catalogos.articulos.reglas.Transaccion;
import org.primefaces.event.SelectEvent;

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
			this.attrs.put("menudeo", 50D);				
			this.attrs.put("medioMayoreo", 40D);				
			this.attrs.put("mayoreo", 40D);				
      this.doLoad();
      this.loadProveedores();
      this.loadCategorias();
      this.loadEmpaques();
      this.doLoadUnidadesMedidas();
      this.loadGrupos();
      this.loadTiposVentas();
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
			this.prepareRegistro();
      transaccion = new Transaccion(this.registroArticulo, 0D);
      if (transaccion.ejecutar(EAccion.COPIAR)) 
				JsfBase.addMessage("Se registro el artículo de forma correcta.", ETipoMensaje.INFORMACION);
      else
				JsfBase.addMessage("Ocurrió un error al registrar el artículo", ETipoMensaje.ERROR);						
			this.registroArticulo= new RegistroArticulo();
			this.attrs.put("codigoExpress", null);
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
			codigo.setCodigo((String) JsfBase.getParametro("codigoDialog_input"));
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
	
	public void doActualizaPrecios() {
		try {
			if(this.registroArticulo.getArticulo().getPrecio()!= null) {
				boolean redondear= this.registroArticulo.getArticulo().getIdRedondear().equals(1L);
				double total= this.registroArticulo.getArticulo().getPrecio()* (1+ (this.registroArticulo.getArticulo().getIva()/ 100));
				this.registroArticulo.getArticulo().setMenudeo(Numero.toAjustarDecimales(total+ (total* ((Double)this.attrs.get("menudeo")/ 100)), redondear));
				this.registroArticulo.getArticulo().setMedioMayoreo(Numero.toAjustarDecimales(total+ (total* ((Double)this.attrs.get("medioMayoreo")/ 100)), redondear));
				this.registroArticulo.getArticulo().setMayoreo(Numero.toAjustarDecimales(total+ (total* ((Double)this.attrs.get("mayoreo")/ 100)), redondear));
			} // if
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
	}
	
	public void doUpdateArticuloExpress() {
    double costo  = 0D;
	  double calculo= 0D;
		if(this.attrs.get("seleccionado")!= null && ((Entity)this.attrs.get("seleccionado")).size()> 1) {
			Entity entity= (Entity)this.attrs.get("seleccionado");
			if(entity.containsKey("idListaPrecio")) {
				this.attrs.put("codigo", entity.toString("codigo"));
				this.registroArticulo.getArticulo().setDescripcion(entity.toString("descripcion"));
				this.registroArticulo.getArticulo().setNombre(entity.toString("descripcion"));
				this.registroArticulo.getArticulo().setPrecio(entity.toDouble("costo"));
				this.registroArticulo.getArticulo().setIva(16D);
				this.registroArticulo.getArticulo().setIdRedondear(2L);
				calculo = Numero.toRedondearSat((this.registroArticulo.getArticulo().getPrecio()* ((this.registroArticulo.getArticulo().getIva()/100)+ 1)));
			  // al precio de neto se le quita el costo+ iva y lo que queda se calcula la utilidad bruta 
				this.registroArticulo.getArticulo().setMenudeo(calculo+ (calculo* 0.5));
				this.registroArticulo.getArticulo().setMedioMayoreo(calculo+ (calculo* 0.4));
				this.registroArticulo.getArticulo().setMayoreo(calculo+ (calculo* 0.3));
				this.attrs.put("menudeo", 50D);				
				this.attrs.put("medioMayoreo", 40D);				
				this.attrs.put("mayoreo", 30D);			
				this.doActualizaPrecios();
			} // if
			else {
				this.attrs.put("codigo", entity.toString("propio"));
				this.registroArticulo.getArticulo().setSat(Cadena.isVacio(entity.toString("sat"))? Constantes.CODIGO_SAT: entity.toString("sat"));
				this.registroArticulo.getArticulo().setDescripcion(entity.toString("descripcion"));
				this.registroArticulo.getArticulo().setNombre(entity.toString("nombre"));
				this.registroArticulo.getArticulo().setPrecio(entity.toDouble("precio"));
				this.registroArticulo.getArticulo().setMenudeo(entity.toDouble("menudeo"));
				this.registroArticulo.getArticulo().setMedioMayoreo(entity.toDouble("medioMayoreo"));
				this.registroArticulo.getArticulo().setMayoreo(entity.toDouble("mayoreo"));
				this.registroArticulo.getArticulo().setIva(entity.toDouble("iva"));
				this.registroArticulo.getArticulo().setIdRedondear(entity.toLong("idRedondear"));
				calculo = Numero.toRedondearSat((this.registroArticulo.getArticulo().getPrecio()* ((this.registroArticulo.getArticulo().getIva()/100)+ 1)));
				this.attrs.put("menudeo", Numero.toRedondearSat((calculo- this.registroArticulo.getArticulo().getMenudeo())* 100/ calculo));				
				this.attrs.put("medioMayoreo", Numero.toRedondearSat((calculo- this.registroArticulo.getArticulo().getMedioMayoreo())* 100/ calculo));				
				this.attrs.put("mayoreo", Numero.toRedondearSat((calculo- this.registroArticulo.getArticulo().getMayoreo())* 100/ calculo));			
				this.doActualizaPrecios();
			} // else	
		} // if
	}	
	
	public void doUpdateNombre() {
		if(this.getRegistroArticulo().getArticulo().getNombre()!= null && Cadena.isVacio(this.getRegistroArticulo().getArticulo().getDescripcion())) 
		  this.getRegistroArticulo().getArticulo().setDescripcion(this.getRegistroArticulo().getArticulo().getNombre().toUpperCase());
	}	

	public List<UISelectEntity> doCompleteCodigo(String query) {
		List<Columna> columns     = null;
    Map<String, Object> params= null;
    try {
			params= new HashMap<>();
			columns= new ArrayList<>();
      columns.add(new Columna("propio", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
			String search= !Cadena.isVacio(query)? query.toUpperCase().replaceAll(Constantes.CLEAN_SQL, "").trim(): "WXYZ";
			params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
  		params.put("idProveedor", -1L);			
  		params.put("codigo", search);			
      this.attrs.put("codigosExpress", (List<UISelectEntity>) UIEntity.build("VistaOrdenesComprasDto", "porCodigo", params, columns, 20L));
		} // try
	  catch (Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    } // finally
		return (List<UISelectEntity>)this.attrs.get("codigosExpress");
	}	// doCompleteCodigo

	public void doAsignaCodigo(SelectEvent event) {
		UISelectEntity seleccion    = null;
		List<UISelectEntity> codigos= null;
		try {
			codigos= (List<UISelectEntity>) this.attrs.get("codigosExpress");
			seleccion= codigos.get(codigos.indexOf((UISelectEntity)event.getObject()));
			this.attrs.put("codigoExpressSeleccion", seleccion);			
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
	} // doAsignaCodigo		
}
