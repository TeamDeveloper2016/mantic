package mx.org.kaana.mantic.catalogos.proveedores.backing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelect;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.pagina.UISelectItem;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.clientes.beans.Domicilio;
import mx.org.kaana.mantic.catalogos.clientes.reglas.MotorBusqueda;
import mx.org.kaana.mantic.catalogos.proveedores.beans.ProveedorContactoAgente;
import mx.org.kaana.mantic.catalogos.proveedores.beans.RegistroProveedor;
import mx.org.kaana.mantic.catalogos.proveedores.reglas.Transaccion;
import mx.org.kaana.mantic.db.dto.TcManticDomiciliosDto;
import mx.org.kaana.mantic.enums.ETiposContactos;
import mx.org.kaana.mantic.enums.ETiposDomicilios;

@Named(value = "manticCatalogosProveedoresAccion")
@ViewScoped
public class Accion extends IBaseAttribute implements Serializable {

  private static final long serialVersionUID = 327393488565639367L;
  private RegistroProveedor registroProveedor;
	private UISelectEntity domicilioBusqueda;

	public RegistroProveedor getRegistroProveedor() {
		return registroProveedor;
	}

	public void setRegistroProveedor(RegistroProveedor registroProveedor) {
		this.registroProveedor = registroProveedor;
	}

	public UISelectEntity getDomicilioBusqueda() {
		return domicilioBusqueda;
	}

	public void setDomicilioBusqueda(UISelectEntity domicilioBusqueda) {
		this.domicilioBusqueda = domicilioBusqueda;
	}	
	
  @PostConstruct
  @Override
  public void init() {
    try {
      this.attrs.put("accion", JsfBase.getFlashAttribute("accion"));
      this.attrs.put("idProveedor", JsfBase.getFlashAttribute("idProveedor"));
			this.attrs.put("admin", JsfBase.isAdminEncuestaOrAdmin());
      doLoad(); 
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // initload
  
	private void loadCollections(){
		loadBancos();
		loadTiposProveedores();
		loadTipoPago();
		loadTiposContactos();
		loadTiposDomicilios();	
		loadDomicilios();
		loadEntidades();
		toAsignaEntidad();
		loadMunicipios();
		toAsignaMunicipio();
		loadLocalidades();
		toAsignaLocalidad();
		loadCodigosPostales();      
		toAsignaCodigoPostal();
	}
	
  public void doLoad() {
    EAccion eaccion = null;
    Long idProveedor = -1L;
    try {
      eaccion = (EAccion) this.attrs.get("accion");
      this.attrs.put("nombreAccion", Cadena.letraCapital(eaccion.name()));
      switch (eaccion) {
        case AGREGAR:
          this.registroProveedor = new RegistroProveedor();
					loadCollections();
          break;
        case MODIFICAR:
        case CONSULTAR:
          idProveedor = Long.valueOf(this.attrs.get("idProveedor").toString());
          this.registroProveedor = new RegistroProveedor(idProveedor);
					loadCollections();
					if(!this.registroProveedor.getProveedoresDomicilio().isEmpty()){
						this.registroProveedor.setProveedorDomicilioSeleccion(this.registroProveedor.getProveedoresDomicilio().get(0));
						doConsultarProveedorDomicilio();
					} // if
					if(!this.registroProveedor.getPersonasTiposContacto().isEmpty()){
						this.registroProveedor.setPersonaTipoContacto(this.registroProveedor.getPersonasTiposContacto().get(0));
						this.registroProveedor.doConsultarAgente();
					} // if
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
      transaccion= new Transaccion(this.registroProveedor);
			this.registroProveedor.getProveedor().setIdTipoProveedor(((UISelectEntity)this.attrs.get("tipoProveedor")).getKey());
      if (transaccion.ejecutar((EAccion) this.attrs.get("accion"))) {
        regresar = "filtro".concat(Constantes.REDIRECIONAR);
        JsfBase.addMessage("Se registro el proveedor de forma correcta.", ETipoMensaje.INFORMACION);
      } // if
      else 
        JsfBase.addMessage("Ocurrió un error al registrar el proveedor", ETipoMensaje.ERROR);      
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    return regresar;
  } // doAccion

  public String doCancelar() {
    return "filtro";
  } // doAccion
	
	private void loadTiposContactos() {
    List<UISelectItem> tiposContactos = null;
    try {
      tiposContactos = new ArrayList<>();
      for (ETiposContactos tipoContacto : ETiposContactos.values()) 
        tiposContactos.add(new UISelectItem(tipoContacto.getKey(), Cadena.reemplazarCaracter(tipoContacto.name(), '_', ' ')));      
      this.attrs.put("tiposContactos", tiposContactos);
    } // try
    catch (Exception e) {
      throw e;
    } // catch		    
  } // loadTiposContactos

  private void loadTiposDomicilios() {
    List<UISelectItem> tiposDomicilios = null;
    try {
      tiposDomicilios = new ArrayList<>();
      for (ETiposDomicilios tipoDomicilio : ETiposDomicilios.values()) 
        tiposDomicilios.add(new UISelectItem(tipoDomicilio.getKey(), Cadena.reemplazarCaracter(tipoDomicilio.name(), '_', ' ')));      
      this.attrs.put("tiposDomicilios", tiposDomicilios);
    } // try
    catch (Exception e) {
      throw e;
    } // catch		    
  } // loadTiposDomicilios
	
  private void loadTipoPago(){
    List<UISelectItem> tiposPago= null;
		Map<String, Object>params   = null;
    try {
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
			tiposPago= UISelect.build("TcManticTiposPagosDto", "row", params, "nombre", " ", EFormatoDinamicos.MAYUSCULAS);
      this.attrs.put("tiposPago", tiposPago);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
		finally{
			Methods.clean(params);
		} // finally
  } // loadTipoPago
	
	private void loadTiposProveedores(){
		List<UISelectEntity> tiposProveedores= null;
    List<Columna> formatos               = null;
    Map<String, Object> params           = null;
    try {
      params = new HashMap();
      params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
      formatos = new ArrayList<>();      
      formatos.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      formatos.add(new Columna("dias", EFormatoDinamicos.NUMERO_SIN_DECIMALES));
      tiposProveedores= UIEntity.build("TcManticTiposProveedoresDto", params, formatos);
			this.attrs.put("tiposProveedores", tiposProveedores);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    finally {
      Methods.clean(params);
      Methods.clean(formatos);
    } // finally
	} // loadTiposProveedores
	
	private void loadEntidades() {
    List<UISelectEntity> entidades= null;
		List<Columna>campos= null;
    Map<String, Object> params= null;
    try {
      params = new HashMap<>();
      params.put("idPais", 1);
      params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
			campos= new ArrayList<>();
			campos.add(new Columna("descripcion", EFormatoDinamicos.MAYUSCULAS));
      entidades = UIEntity.build("TcJanalEntidadesDto", "comboEntidades", params, campos, Constantes.SQL_TODOS_REGISTROS);
      this.attrs.put("entidades", entidades);
      this.registroProveedor.getDomicilio().setIdEntidad(entidades.get(0));
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    finally {
      Methods.clean(params);
    } // finally
  } // loadEntidades
	
	private void toAsignaEntidad(){
		Entity domicilio= null;
		List<Entity>entidades= null;
		try {
			if(!this.registroProveedor.getDomicilio().getIdDomicilio().equals(-1L)){
				domicilio= this.registroProveedor.getDomicilio().getDomicilio();
				entidades= (List<Entity>) this.attrs.get("entidades");
				for(Entity entidad: entidades){
					if(entidad.getKey().equals(domicilio.toLong("idEntidad")))
						this.registroProveedor.getDomicilio().setIdEntidad(entidad);
				} // for
			} // if
			else
				this.registroProveedor.getDomicilio().setIdEntidad(new Entity(-1L));
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
	} // toAsignaEntidad

  private void loadMunicipios() {
    List<UISelectEntity> municipios= null;
    Map<String, Object> params= null;
		List<Columna>campos= null;
    try {
			if(!this.registroProveedor.getDomicilio().getIdEntidad().getKey().equals(-1L)){
				params = new HashMap<>();
				params.put("idEntidad", this.registroProveedor.getDomicilio().getIdEntidad().getKey());
				campos= new ArrayList<>();
				campos.add(new Columna("descripcion", EFormatoDinamicos.MAYUSCULAS));
				municipios = UIEntity.build("TcJanalMunicipiosDto", "comboMunicipios", params, campos, Constantes.SQL_TODOS_REGISTROS);
				this.attrs.put("municipios", municipios);
				this.registroProveedor.getDomicilio().setIdMunicipio(municipios.get(0));
			} // if
			else
				this.registroProveedor.getDomicilio().setIdMunicipio(new Entity(-1L));
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    finally {
      Methods.clean(params);
    } // finally
  } // loadMunicipios
	
	private void toAsignaMunicipio(){
		Entity domicilio= null;
		List<Entity>municipios= null;
		try {
			if(!this.registroProveedor.getDomicilio().getIdMunicipio().getKey().equals(-1L)){
				domicilio= this.registroProveedor.getDomicilio().getDomicilio();
				municipios= (List<Entity>) this.attrs.get("municipios");
				for(Entity municipio: municipios){
					if(municipio.getKey().equals(domicilio.toLong("idMunicipio")))
						this.registroProveedor.getDomicilio().setIdMunicipio(municipio);
				} // for
			} // if
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
	} // toAsignaMunicipio

  private void loadLocalidades() {
    List<UISelectEntity> localidades= null;
    Map<String, Object> params= null;
		List<Columna>campos= null;
    try {
			if(!this.registroProveedor.getDomicilio().getIdMunicipio().getKey().equals(-1L)){
				params = new HashMap<>();
				params.put("idMunicipio", this.registroProveedor.getDomicilio().getIdMunicipio().getKey());
				campos= new ArrayList<>();
				campos.add(new Columna("descripcion", EFormatoDinamicos.MAYUSCULAS));
				localidades = UIEntity.build("TcJanalLocalidadesDto", "comboLocalidades", params, campos, Constantes.SQL_TODOS_REGISTROS);
				this.attrs.put("localidades", localidades);
				this.registroProveedor.getDomicilio().setLocalidad(localidades.get(0));
				this.registroProveedor.getDomicilio().setIdLocalidad(localidades.get(0).getKey());
			} // if
			else{
				this.registroProveedor.getDomicilio().setLocalidad(new Entity(-1L));
				this.registroProveedor.getDomicilio().setIdLocalidad(-1L);
			} // else
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    finally {
      Methods.clean(params);
    } // finally
  } // loadLocalidades
	
	private void toAsignaLocalidad(){
		Entity domicilio= null;
		List<Entity>localidades= null;
		try {
			if(!this.registroProveedor.getDomicilio().getIdDomicilio().equals(-1L)){
				domicilio= this.registroProveedor.getDomicilio().getDomicilio();
				localidades= (List<Entity>) this.attrs.get("localidades");
				for(Entity localidad: localidades){
					if(localidad.getKey().equals(domicilio.toLong("idLocalidad"))){
						this.registroProveedor.getDomicilio().setIdLocalidad(localidad.getKey());
						this.registroProveedor.getDomicilio().setLocalidad(localidad);
					} // if
				} // for
			} // if			
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
	} // toAsignaLocalidad

  private void loadCodigosPostales() {
    List<UISelectItem> codigosPostales = null;
    Map<String, Object> params = null;
    try {
			if(!this.registroProveedor.getDomicilio().getIdEntidad().getKey().equals(-1L)){
				params = new HashMap<>();
				params.put(Constantes.SQL_CONDICION, "id_entidad=" + this.registroProveedor.getDomicilio().getIdEntidad().getKey());
				codigosPostales = UISelect.build("TcManticCodigosPostalesDto", "row", params, "codigo", EFormatoDinamicos.MAYUSCULAS, Constantes.SQL_TODOS_REGISTROS);
				this.attrs.put("codigosPostales", codigosPostales);
				if (!codigosPostales.isEmpty()) {
					this.registroProveedor.getDomicilio().setCodigoPostal(codigosPostales.get(0).getLabel());
					this.registroProveedor.getDomicilio().setIdCodigoPostal((Long) codigosPostales.get(0).getValue());
					this.registroProveedor.getDomicilio().setNuevoCp(true);
				} // if
				else 
					this.registroProveedor.getDomicilio().setNuevoCp(false);				
			} // if
			else
				this.registroProveedor.getDomicilio().setNuevoCp(false);				
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    finally {
      Methods.clean(params);
    } // finally
  } // loadCodigosPostales

	private void toAsignaCodigoPostal(){
		Entity domicilio= null;
		List<UISelectItem>codigos= null;
		int count=0;
		try {
			if(!this.registroProveedor.getDomicilio().getIdDomicilio().equals(-1L)){
				domicilio= this.registroProveedor.getDomicilio().getDomicilio();
				codigos= (List<UISelectItem>) this.attrs.get("codigosPostales");
				for(UISelectItem codigo: codigos){
					if(codigo.getLabel().equals(domicilio.toString("codigoPostal"))){
						this.registroProveedor.getDomicilio().setCodigoPostal(codigo.getLabel());
						this.registroProveedor.getDomicilio().setIdCodigoPostal((Long) codigo.getValue());
						this.registroProveedor.getDomicilio().setNuevoCp(true);
						count++;
					} // if
				} // for
				if(count==0){
					this.registroProveedor.getDomicilio().setNuevoCp(false);
					this.registroProveedor.getDomicilio().setIdCodigoPostal(-1L);
					this.registroProveedor.getDomicilio().setCodigoPostal("");
				} // if
			} // if
			else{
				this.registroProveedor.getDomicilio().setNuevoCp(false);
				this.registroProveedor.getDomicilio().setIdCodigoPostal(-1L);
				this.registroProveedor.getDomicilio().setCodigoPostal("");
			} // else
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
	} // toAsignaCodigoPostal
	
  public void doLoadDomicilios() {
    try {
      updateCodigoPostal();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // doLoadDomicilios

  private void loadDomicilios() {
		List<UISelectEntity> domicilios= null;
		try {
			domicilios= new ArrayList<>();
			this.attrs.put("domicilios", domicilios);     
			this.registroProveedor.getDomicilio().setDomicilio(new Entity(-1L));
      this.registroProveedor.getDomicilio().setIdDomicilio(-1L);
		} // try
		catch (Exception e) {		
			throw e;
		} // catch		
	} // loadDomicilios
	
  public void doBusquedaDomicilios() {
    List<UISelectEntity> domicilios= null;
    Map<String, Object> params= null;
		List<Columna>campos= null;
    try {
      params = new HashMap<>();      
      params.put(Constantes.SQL_CONDICION, "upper(calle) like upper('%".concat(this.attrs.get("calle").toString()).concat("%')"));
			campos= new ArrayList<>();
			campos.add(new Columna("calle", EFormatoDinamicos.MAYUSCULAS));
			campos.add(new Columna("numeroExterior", EFormatoDinamicos.MAYUSCULAS));
			campos.add(new Columna("numeroInterior", EFormatoDinamicos.MAYUSCULAS));
			campos.add(new Columna("asentamiento", EFormatoDinamicos.MAYUSCULAS));
			campos.add(new Columna("entidad", EFormatoDinamicos.MAYUSCULAS));
			campos.add(new Columna("municipio", EFormatoDinamicos.MAYUSCULAS));
			campos.add(new Columna("domicilio", EFormatoDinamicos.MAYUSCULAS));
      domicilios = UIEntity.build("VistaDomiciliosCatalogosDto", "domicilios", params, campos, Constantes.SQL_TODOS_REGISTROS);
      this.registroProveedor.getDomicilio().setDomicilio(new Entity(-1L));
      this.registroProveedor.getDomicilio().setIdDomicilio(-1L);
			this.attrs.put("domiciliosBusqueda", domicilios);      
			this.attrs.put("resultados", domicilios.size());      
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    finally {
      Methods.clean(params);
    } // finally
  } // doLoadDomicilios

	public void doAsignaDomicilio(){
		List<UISelectEntity> domicilios        = null;
		List<UISelectEntity> domiciliosBusqueda= null;
		UISelectEntity domicilio               = null;
		try {
			domiciliosBusqueda=(List<UISelectEntity>) this.attrs.get("domiciliosBusqueda");
			domicilio= domiciliosBusqueda.get(domiciliosBusqueda.indexOf(this.domicilioBusqueda));
			domicilios= new ArrayList<>();
			domicilios.add(domicilio);
			this.attrs.put("domicilios", domicilios);			
			this.registroProveedor.getDomicilio().setDomicilio(domicilio);
      this.registroProveedor.getDomicilio().setIdDomicilio(domicilio.getKey());
			toAsignaEntidad();
			loadMunicipios();
			toAsignaMunicipio();
			loadLocalidades();
			toAsignaLocalidad();
			loadCodigosPostales();      
			toAsignaCodigoPostal();
			loadAtributosComplemento();						
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
	} // doAsignaDomicilio
	
  private void updateCodigoPostal() {
    List<UISelectItem> codigosPostales = null;
    try {
      if (this.registroProveedor.getDomicilio().getIdCodigoPostal().equals(-1L)) {
        this.registroProveedor.getDomicilio().setCodigoPostal("");
        this.registroProveedor.getDomicilio().setNuevoCp(false);
      } // if
      else {
        codigosPostales = (List<UISelectItem>) this.attrs.get("codigosPostales");
        for (UISelectItem codigo : codigosPostales) {
          if (codigo.getValue().equals(this.registroProveedor.getDomicilio().getIdCodigoPostal())) {
            this.registroProveedor.getDomicilio().setCodigoPostal(codigo.getLabel());
            this.registroProveedor.getDomicilio().setNuevoCp(true);
          } // if
        } // for
      } // else
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
  } // updateCodigoPostal

  public void doActualizaMunicipios() {
    try {
      loadMunicipios();
      loadLocalidades();
      loadCodigosPostales();      
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // doActualizaMunicipios

  public void doActualizaLocalidades() {
    try {
      loadLocalidades();
      loadCodigosPostales();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // doActualizaMunicipios

  public void doActualizaCodigosPostales() {
    try {
      loadCodigosPostales();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // doActualizaMunicipios

  public void doLoadAtributos() {
		doLoadAtributos(true);
	} // doLoadAtributos
	
  public void doLoadAtributos(boolean all) {    
		List<Entity> domicilios= null;
    try {
			if(all){
				if(!this.registroProveedor.getDomicilio().getDomicilio().getKey().equals(-1L)){
					domicilios= (List<Entity>) this.attrs.get("domicilios");
					this.registroProveedor.getDomicilio().setDomicilio(domicilios.get(domicilios.indexOf(this.registroProveedor.getDomicilio().getDomicilio())));
					this.registroProveedor.getDomicilio().setIdDomicilio(domicilios.get(domicilios.indexOf(this.registroProveedor.getDomicilio().getDomicilio())).getKey());
				} // if
				else{
					this.registroProveedor.getDomicilio().setDomicilio(new Entity(-1L));
					this.registroProveedor.getDomicilio().setIdDomicilio(-1L);
				} // else					
				toAsignaEntidad();
				loadMunicipios();
				toAsignaMunicipio();
				loadLocalidades();
				toAsignaLocalidad();
				loadCodigosPostales();      
				toAsignaCodigoPostal();
			} // if
      loadAtributosComplemento();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
  } // doLoadAtributos

	private void loadAtributosComplemento() throws Exception{
		MotorBusqueda motor            = null;
		TcManticDomiciliosDto domicilio= null;
		try {
			if (!this.registroProveedor.getDomicilio().getIdDomicilio().equals(-1L)) {
        motor = new MotorBusqueda(this.registroProveedor.getIdProveedor());
        domicilio = motor.toDomicilio(this.registroProveedor.getDomicilio().getIdDomicilio());
        this.registroProveedor.getDomicilio().setNumeroExterior(domicilio.getNumeroExterior());
        this.registroProveedor.getDomicilio().setNumeroInterior(domicilio.getNumeroInterior());
        this.registroProveedor.getDomicilio().setCalle(domicilio.getCalle());
        this.registroProveedor.getDomicilio().setAsentamiento(domicilio.getAsentamiento());
        this.registroProveedor.getDomicilio().setEntreCalle(domicilio.getEntreCalle());
        this.registroProveedor.getDomicilio().setYcalle(domicilio.getYcalle());
      } // if
      else {
        clearAtributos();
      } // else
		} // try
		catch (Exception e) {			
			throw e;
		} // catch				
	} // loadAtributosComplemento
	
  public void clearAtributos() {
    try {
      this.registroProveedor.getDomicilio().setNumeroExterior("");
      this.registroProveedor.getDomicilio().setNumeroInterior("");
      this.registroProveedor.getDomicilio().setCalle("");
      this.registroProveedor.getDomicilio().setAsentamiento("");
      this.registroProveedor.getDomicilio().setEntreCalle("");
      this.registroProveedor.getDomicilio().setYcalle("");
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
  } // doLoadAtributos
	
	public void doAgregarProveedor() {
    try {
      this.registroProveedor.doAgregarProveedorDomicilio();
      this.registroProveedor.setDomicilio(new Domicilio());
      loadDomicilios();
      doLoadAtributos(true);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // doAgregarProveedor

  public void doConsultarProveedorDomicilio() {
    Domicilio domicilio = null;
    List<UISelectItem> codigos = null;
    try {
      this.registroProveedor.doConsultarProveedorDomicilio();
			domicilio = this.registroProveedor.getDomicilioPivote();
      this.registroProveedor.getDomicilio().setIdDomicilio(domicilio.getIdDomicilio());
      this.registroProveedor.getDomicilio().setDomicilio(domicilio.getDomicilio());      			
      this.registroProveedor.getDomicilio().setIdEntidad(domicilio.getIdEntidad());	
			this.registroProveedor.getDomicilio().getDomicilio().put("idEntidad", new Value("idEntidad", domicilio.getIdEntidad().getKey()));
      toAsignaEntidad();
			loadMunicipios();
      this.registroProveedor.getDomicilio().setIdMunicipio(domicilio.getIdMunicipio());			
			this.registroProveedor.getDomicilio().getDomicilio().put("idMunicipio", new Value("idMunicipio", domicilio.getIdMunicipio().getKey()));
      toAsignaMunicipio();
			loadLocalidades();
      this.registroProveedor.getDomicilio().setLocalidad(domicilio.getLocalidad());			
      this.registroProveedor.getDomicilio().setIdLocalidad(domicilio.getIdLocalidad());			
			this.registroProveedor.getDomicilio().getDomicilio().put("idLocalidad", new Value("idLocalidad", domicilio.getLocalidad().getKey()));
      toAsignaLocalidad();
			loadCodigosPostales();
      codigos = (List<UISelectItem>) this.attrs.get("codigosPostales");
      for (UISelectItem codigo : codigos) {
        if (codigo.getLabel().equals(domicilio.getCodigoPostal())) {
          this.registroProveedor.getDomicilio().setIdCodigoPostal((Long) codigo.getValue());
          this.registroProveedor.getDomicilio().setCodigoPostal(codigo.getLabel());
					this.registroProveedor.getDomicilio().setNuevoCp(true);
        } // if
      }	// for      
      this.registroProveedor.getDomicilio().setCalle(domicilio.getCalle());
      this.registroProveedor.getDomicilio().setNumeroExterior(domicilio.getNumeroExterior());
      this.registroProveedor.getDomicilio().setNumeroInterior(domicilio.getNumeroInterior());
      this.registroProveedor.getDomicilio().setAsentamiento(domicilio.getAsentamiento());
      this.registroProveedor.getDomicilio().setEntreCalle(domicilio.getEntreCalle());
      this.registroProveedor.getDomicilio().setYcalle(domicilio.getYcalle());
      this.registroProveedor.getDomicilio().setIdTipoDomicilio(domicilio.getIdTipoDomicilio());
      this.registroProveedor.getDomicilio().setPrincipal(domicilio.getPrincipal());
			this.registroProveedor.getDomicilio().setNuevoCp(domicilio.isNuevoCp());
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // doEliminarArticuloCodigo	
	
	public void doActualizaDomicilio(){
		try {
			this.registroProveedor.doActualizarProveedorDomicilio();
			this.registroProveedor.setDomicilio(new Domicilio());
      loadDomicilios();
      doLoadAtributos(true);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
	} // doActualizaDomicilio
	
	public void doEliminarDomicilio(){
		try {
			this.registroProveedor.doEliminarProveedorDomicilio();
			this.registroProveedor.setDomicilio(new Domicilio());
      loadDomicilios();
      doLoadAtributos(true);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
	} // doActualizaDomicilio
	
	public void doAgregarProveedorAgente(){
		List<UISelectItem> representantes = null;
		try {
			representantes= (List<UISelectItem>) this.attrs.get("representantes");
			if(!representantes.isEmpty())
				this.registroProveedor.doAgregarAgenteContacto();
			else
				JsfBase.addMessage("Agregar representante", "No hay representantes registrados", ETipoMensaje.INFORMACION);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
	} // doAgregarProveedorRepresentante
	
	public void doAgregarAgente() {
    try {
      this.registroProveedor.doAgregarAgente();
      this.registroProveedor.setPersonaTipoContactoPivote(new ProveedorContactoAgente());      
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // doAgregarProveedor
	
	public void doActualizaAgente(){
		try {
			this.registroProveedor.doActualizaAgente();
      this.registroProveedor.setPersonaTipoContactoPivote(new ProveedorContactoAgente());      
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
	} // doActualizaRepresentante
	
	public void doEliminarAgente(){
		try {
      this.registroProveedor.doEliminarAgente();
      this.registroProveedor.setPersonaTipoContactoPivote(new ProveedorContactoAgente());      
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
	} // doEliminarRepresentante
	
	private void loadBancos(){
		List<UISelectItem> bancos= null;
		Map<String, Object>params= null;
		List<String> campos= null;
		try {
			params= new HashMap<>();
			params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
			campos= new ArrayList<>();
			campos.add("nombre");
			bancos= UISelect.build("TcManticBancosDto", "row", params, campos, " ", EFormatoDinamicos.MAYUSCULAS, Constantes.SQL_TODOS_REGISTROS);
			this.attrs.put("bancos", bancos);
		} // try
		catch (Exception e) {
			throw e;
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
	}
}
