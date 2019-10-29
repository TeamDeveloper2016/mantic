package mx.org.kaana.mantic.catalogos.articulos.backing;

import java.io.Serializable;
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
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UISelect;
import mx.org.kaana.libs.pagina.UISelectItem;
import mx.org.kaana.libs.recurso.LoadImages;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.articulos.beans.ArticuloCodigo;
import mx.org.kaana.mantic.catalogos.articulos.beans.Importado;
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
      this.attrs.put("goKardex", ((Long)JsfBase.getFlashAttribute("idArticulo"))> 0L);
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

  public void doLoad() {
    EAccion eaccion= null;
    Long idArticulo= -1L;
    try {
      eaccion= (EAccion) this.attrs.get("accion");
			this.attrs.put("activeClon", eaccion.equals(EAccion.ACTIVAR) || eaccion.equals(EAccion.MODIFICAR));
      this.attrs.put("nombreAccion", Cadena.letraCapital(eaccion.name()));
      switch (eaccion) {
        case AGREGAR:
          this.registroArticulo = new RegistroArticulo();
					this.image= LoadImages.getImage(-1L);
          break;
        case MODIFICAR:
        case CONSULTAR:
        case COPIAR:
        case ACTIVAR:
          idArticulo = (Long)(this.attrs.get("idArticulo"));
          this.registroArticulo = new RegistroArticulo(idArticulo);
					this.image= LoadImages.getImage(idArticulo);
					this.registroArticulo.setIdTipoArticulo(this.registroArticulo.getArticulo().getIdArticuloTipo());
          break;
      } // switch
			this.attrs.put("precio", this.registroArticulo.getArticulo().getPrecio());
			this.doUpdatePrecio();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // doLoad

  public String doAceptar(String accion) {
    Transaccion transaccion= null;
    String regresar        = null;
		EAccion eaccion        = null;
    try {
			eaccion= EAccion.valueOf(accion.toUpperCase());
      transaccion = new Transaccion(this.registroArticulo, (Double)this.attrs.get("precio"));
			if(this.image!= null) {
				this.image.getStream().close();
				this.image= null;
			} // if
      if (transaccion.ejecutar(eaccion)) {
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
  } // doCancelar

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
	
	public String doKardex() {
		JsfBase.setFlashAttribute("idArticulo", this.attrs.get("idArticulo"));
		if(!this.registroArticulo.getArticulosCodigos().isEmpty())
		  JsfBase.setFlashAttribute("xcodigo", this.registroArticulo.getArticulosCodigos().get(0).getCodigo());
		return "/Paginas/Mantic/Inventarios/Almacenes/kardex".concat(Constantes.REDIRECIONAR);
	}
	
  public void doUpdatePrecio() {
		double precio    = this.registroArticulo.getArticulo().getPrecio();
		boolean redondear= this.registroArticulo== null || this.registroArticulo.isRedondear();
		if(((EAccion)this.attrs.get("accion")).equals(EAccion.AGREGAR)) {
  	  this.attrs.put("precio", precio);
			double calculo= precio* (1+ (this.registroArticulo.getArticulo().getIva()/ 100));
			this.registroArticulo.getArticulo().setMenudeo(Numero.toAjustarDecimales(calculo* 1.5, redondear));
			this.registroArticulo.getArticulo().setMedioMayoreo(Numero.toAjustarDecimales(calculo* 1.4, redondear));
			this.registroArticulo.getArticulo().setMayoreo(Numero.toAjustarDecimales(calculo* 1.3, redondear));
			this.attrs.put("menudeo", 50D);
			this.attrs.put("medioMayoreo", 40D);
			this.attrs.put("mayoreo", 30D);
		} // if	
		else {
			double calculo= (Double)this.attrs.get("precio")* (1+ (this.registroArticulo.getArticulo().getIva()/ 100));
			double total  = precio* (1+ (this.registroArticulo.getArticulo().getIva()/ 100));
      double factor = Numero.toRedondearSat(this.registroArticulo.getArticulo().getMenudeo()/ calculo);
			this.attrs.put("menudeo", Numero.toRedondearSat((factor- 1)* 100));
			this.registroArticulo.getArticulo().setMenudeo(Numero.toAjustarDecimales(total* factor, redondear));
      factor = Numero.toRedondearSat(this.registroArticulo.getArticulo().getMedioMayoreo()/ calculo);
			this.attrs.put("medioMayoreo", Numero.toRedondearSat((factor- 1)* 100));
			this.registroArticulo.getArticulo().setMedioMayoreo(Numero.toAjustarDecimales(total* factor, redondear));
      factor = Numero.toRedondearSat(this.registroArticulo.getArticulo().getMayoreo()/ calculo);
			this.attrs.put("mayoreo", Numero.toRedondearSat((factor- 1)* 100));
			this.registroArticulo.getArticulo().setMayoreo(Numero.toAjustarDecimales(total* factor, redondear));
  	  this.attrs.put("precio", precio);
		} // if	
	}	 // doUpdatePrecio
	
  public void doUpdatePreciosVenta() {
		double precio    = this.registroArticulo.getArticulo().getPrecio();
		boolean redondear= this.registroArticulo== null || this.registroArticulo.isRedondear();
		double calculo= (Double)this.attrs.get("precio")* (1+ (this.registroArticulo.getArticulo().getIva()/ 100));
		double total  = precio* (1+ (this.registroArticulo.getArticulo().getIva()/ 100));
		double factor = Numero.toRedondearSat(this.registroArticulo.getArticulo().getMenudeo()/ calculo);
		this.attrs.put("menudeo", Numero.toRedondearSat((factor- 1)* 100));
		this.registroArticulo.getArticulo().setMenudeo(Numero.toAjustarDecimales(total* factor, redondear));
		factor = Numero.toRedondearSat(this.registroArticulo.getArticulo().getMedioMayoreo()/ calculo);
		this.attrs.put("medioMayoreo", Numero.toRedondearSat((factor- 1)* 100));
		this.registroArticulo.getArticulo().setMedioMayoreo(Numero.toAjustarDecimales(total* factor, redondear));
		factor = Numero.toRedondearSat(this.registroArticulo.getArticulo().getMayoreo()/ calculo);
		this.attrs.put("mayoreo", Numero.toRedondearSat((factor- 1)* 100));
		this.registroArticulo.getArticulo().setMayoreo(Numero.toAjustarDecimales(total* factor, redondear));
		this.attrs.put("precio", precio);
	}

	public void doDeleteFile() {
		Transaccion transaccion= null;
		EAccion accion         = null;
		try {
			accion= (EAccion)this.attrs.get("accion");			
			if(accion.equals(EAccion.AGREGAR) || (this.registroArticulo.getArticulo().getIdImagen()== null || this.registroArticulo.getArticulo().getIdImagen() < 1L)) 
				this.registroArticulo.doDeleteFile();										
			if (this.registroArticulo.validaImagenComun()){
				transaccion= new Transaccion(this.registroArticulo, 0D);
				if(transaccion.ejecutar(EAccion.DEPURAR)){
					if(this.image!= null){
						this.image.getStream().close();
						this.image= null;
					}	// if				
					this.registroArticulo.doDeleteFile();		
				} // if
			} // else			
			this.registroArticulo.getArticulo().setIdImagen(null);
			this.registroArticulo.setImportado(new Importado());
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
	} // doDeleteFile
	
	public void doLookForCodigo(String id, String codigo, Long index) {
	  Map<String, Object> params=null;
		try {
			params=new HashMap<>();
			if(!Cadena.isVacio(codigo)) {
				ArticuloCodigo proveedor= this.registroArticulo.getArticulosCodigos().get(index.intValue());
				if(proveedor.getIdProveedor()!= null)
			    params.put("idProveedor", proveedor.getIdProveedor());
			  params.put("codigo", codigo.toUpperCase());
				params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
				if(!((EAccion)this.attrs.get("accion")).equals(EAccion.AGREGAR))
					params.put(Constantes.SQL_CONDICION, " tc_mantic_articulos_codigos.id_articulo!="+ this.attrs.get("idArticulo"));
			  List<Entity> values= (List<Entity>)DaoFactory.getInstance().toEntitySet("VistaArticulosDto", "existeCodigo", params);
				if(values!= null && values.size()> 0 && !Cadena.isVacio(values.get(0).toString("codigo"))) {
					StringBuilder sb= new StringBuilder();
					sb.append("<br/>");
					for (Entity item : values) {
						sb.append("  [");
						sb.append(item.toString("codigo"));
						sb.append("]  ");
						sb.append(item.toString("nombre"));
						sb.append(" como  ");
						sb.append(item.toString("principal"));
						sb.append(".<br/>");
					} // for
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
	
}
