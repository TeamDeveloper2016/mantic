package mx.org.kaana.mantic.catalogos.empresas.backing;

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
import mx.org.kaana.mantic.catalogos.empresas.reglas.Transaccion;
import mx.org.kaana.mantic.catalogos.empresas.reglas.MotorBusqueda;
import mx.org.kaana.mantic.catalogos.empresas.beans.RegistroEmpresa;
import mx.org.kaana.mantic.db.dto.TcManticDomiciliosDto;
import mx.org.kaana.mantic.enums.ETipoEmpresa;
import mx.org.kaana.mantic.enums.ETiposContactos;
import mx.org.kaana.mantic.enums.ETiposDomicilios;

@Named(value = "manticCatalogosEmpresasAccion")
@ViewScoped
public class Accion extends IBaseAttribute implements Serializable {

  private static final long serialVersionUID = -7668104942302148046L;
  private RegistroEmpresa registroEmpresa;
	private UISelectEntity domicilioBusqueda;

	public RegistroEmpresa getRegistroEmpresa() {
		return registroEmpresa;
	}

	public void setRegistroEmpresa(RegistroEmpresa registroEmpresa) {
		this.registroEmpresa = registroEmpresa;
	}  

	public UISelectEntity getDomicilioBusqueda() {
		return domicilioBusqueda;
	}

	public void setDomicilioBusqueda(UISelectEntity domicilioBusqueda) {
		this.domicilioBusqueda = domicilioBusqueda;
	}

  @PostConstruct
  @Override
  protected void init() {
    try {
      this.attrs.put("accion", JsfBase.getFlashAttribute("accion"));
      this.attrs.put("idEmpresa", JsfBase.getFlashAttribute("idEmpresa"));
			this.attrs.put("admin", JsfBase.isAdminEncuestaOrAdmin());
			this.attrs.put("isMatriz", false);
      doLoad();      					
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

	private void loadCollections(){
		loadTiposEmpresas();
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
    Long idEmpresa = -1L;
    try {
      eaccion = (EAccion) this.attrs.get("accion");
      this.attrs.put("nombreAccion", Cadena.letraCapital(eaccion.name()));
      switch (eaccion) {
        case AGREGAR:
          this.registroEmpresa= new RegistroEmpresa();
					loadCollections();
          break;
        case MODIFICAR:
        case CONSULTAR:
          idEmpresa = Long.valueOf(this.attrs.get("idEmpresa").toString());
          this.registroEmpresa = new RegistroEmpresa(idEmpresa);
					loadCollections();
					if(!this.registroEmpresa.getEmpresasDomicilio().isEmpty()){
						this.registroEmpresa.setEmpresaDomicilioSelecion(this.registroEmpresa.getEmpresasDomicilio().get(0));
						doConsultarEmpresaDomicilio();
					} // if					
          break;
      } // switch 			
			if(this.registroEmpresa.getEmpresa().getIdTipoEmpresa().equals(ETipoEmpresa.SUCURSAL.getIdTipoEmpresa())){
				doLoadMatrices();
				this.attrs.put("isMatriz", true);
			} // if
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // doLoad

  public String doAceptar() {
    Transaccion transaccion = null;
    String regresar = null;
    try {
      transaccion = new Transaccion(this.registroEmpresa);
      if (transaccion.ejecutar((EAccion) this.attrs.get("accion"))) {
        regresar = "filtro".concat(Constantes.REDIRECIONAR);
        JsfBase.addMessage("Se registro la empresa de forma correcta.", ETipoMensaje.INFORMACION);
      } // if
      else {
        JsfBase.addMessage("Ocurrió un error al registrar la empresa", ETipoMensaje.ERROR);
      }
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    return regresar;
  } // doAccion

  public String doCancelar() {
    return "filtro".concat(Constantes.REDIRECIONAR);
  } // doAccion

  private void loadTiposContactos() {
    List<UISelectItem> tiposContactos = null;
    try {
      tiposContactos = new ArrayList<>();
      for (ETiposContactos tipoContacto : ETiposContactos.values()) {
        tiposContactos.add(new UISelectItem(tipoContacto.getKey(), Cadena.reemplazarCaracter(tipoContacto.name(), '_', ' ')));
      }
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
      for (ETiposDomicilios tipoDomicilio : ETiposDomicilios.values()) {
        tiposDomicilios.add(new UISelectItem(tipoDomicilio.getKey(), Cadena.reemplazarCaracter(tipoDomicilio.name(), '_', ' ')));
      }
      this.attrs.put("tiposDomicilios", tiposDomicilios);
    } // try
    catch (Exception e) {
      throw e;
    } // catch		    
  } // loadTiposDomicilios

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
      this.registroEmpresa.getDomicilio().setIdEntidad(entidades.get(0));
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
			if(!this.registroEmpresa.getDomicilio().getIdDomicilio().equals(-1L)){
				domicilio= this.registroEmpresa.getDomicilio().getDomicilio();
				entidades= (List<Entity>) this.attrs.get("entidades");
				for(Entity entidad: entidades){
					if(entidad.getKey().equals(domicilio.toLong("idEntidad")))
						this.registroEmpresa.getDomicilio().setIdEntidad(entidad);
				} // for
			} // if
			else
				this.registroEmpresa.getDomicilio().setIdEntidad(new Entity(-1L));
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
			if(!this.registroEmpresa.getDomicilio().getIdEntidad().getKey().equals(-1L)){
				params = new HashMap<>();
				params.put("idEntidad", this.registroEmpresa.getDomicilio().getIdEntidad().getKey());
				campos= new ArrayList<>();
				campos.add(new Columna("descripcion", EFormatoDinamicos.MAYUSCULAS));
				municipios = UIEntity.build("TcJanalMunicipiosDto", "comboMunicipios", params, campos, Constantes.SQL_TODOS_REGISTROS);
				this.attrs.put("municipios", municipios);
				this.registroEmpresa.getDomicilio().setIdMunicipio(municipios.get(0));
			} // if
			else
				this.registroEmpresa.getDomicilio().setIdMunicipio(new Entity(-1L));
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
			if(!this.registroEmpresa.getDomicilio().getIdMunicipio().getKey().equals(-1L)){
				domicilio= this.registroEmpresa.getDomicilio().getDomicilio();
				municipios= (List<Entity>) this.attrs.get("municipios");
				for(Entity municipio: municipios){
					if(municipio.getKey().equals(domicilio.toLong("idMunicipio")))
						this.registroEmpresa.getDomicilio().setIdMunicipio(municipio);
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
			if(!this.registroEmpresa.getDomicilio().getIdMunicipio().getKey().equals(-1L)){
				params = new HashMap<>();
				params.put("idMunicipio", this.registroEmpresa.getDomicilio().getIdMunicipio().getKey());
				campos= new ArrayList<>();
				campos.add(new Columna("descripcion", EFormatoDinamicos.MAYUSCULAS));
				localidades = UIEntity.build("TcJanalLocalidadesDto", "comboLocalidades", params, campos, Constantes.SQL_TODOS_REGISTROS);
				this.attrs.put("localidades", localidades);
				this.registroEmpresa.getDomicilio().setLocalidad(localidades.get(0));
				this.registroEmpresa.getDomicilio().setIdLocalidad(localidades.get(0).getKey());
			} // if
			else{
				this.registroEmpresa.getDomicilio().setLocalidad(new Entity(-1L));
				this.registroEmpresa.getDomicilio().setIdLocalidad(-1L);
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
			if(!this.registroEmpresa.getDomicilio().getIdDomicilio().equals(-1L)){
				domicilio= this.registroEmpresa.getDomicilio().getDomicilio();
				localidades= (List<Entity>) this.attrs.get("localidades");
				for(Entity localidad: localidades){
					if(localidad.getKey().equals(domicilio.toLong("idLocalidad"))){
						this.registroEmpresa.getDomicilio().setIdLocalidad(localidad.getKey());
						this.registroEmpresa.getDomicilio().setLocalidad(localidad);
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
			if(!this.registroEmpresa.getDomicilio().getIdEntidad().getKey().equals(-1L)){
				params = new HashMap<>();
				params.put(Constantes.SQL_CONDICION, "id_entidad=" + this.registroEmpresa.getDomicilio().getIdEntidad().getKey());
				codigosPostales = UISelect.build("TcManticCodigosPostalesDto", "row", params, "codigo", EFormatoDinamicos.MAYUSCULAS, Constantes.SQL_TODOS_REGISTROS);
				this.attrs.put("codigosPostales", codigosPostales);
				if (!codigosPostales.isEmpty()) {
					this.registroEmpresa.getDomicilio().setCodigoPostal(codigosPostales.get(0).getLabel());
					this.registroEmpresa.getDomicilio().setIdCodigoPostal((Long) codigosPostales.get(0).getValue());
					this.registroEmpresa.getDomicilio().setNuevoCp(true);
				} // if
				else 
					this.registroEmpresa.getDomicilio().setNuevoCp(false);				
			} // if
			else
				this.registroEmpresa.getDomicilio().setNuevoCp(false);				
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
			if(!this.registroEmpresa.getDomicilio().getIdDomicilio().equals(-1L)){
				domicilio= this.registroEmpresa.getDomicilio().getDomicilio();
				codigos= (List<UISelectItem>) this.attrs.get("codigosPostales");
				for(UISelectItem codigo: codigos){
					if(codigo.getLabel().equals(domicilio.toString("codigoPostal"))){
						this.registroEmpresa.getDomicilio().setCodigoPostal(codigo.getLabel());
						this.registroEmpresa.getDomicilio().setIdCodigoPostal((Long) codigo.getValue());
						this.registroEmpresa.getDomicilio().setNuevoCp(true);
						count++;
					} // if
				} // for
				if(count==0){
					this.registroEmpresa.getDomicilio().setNuevoCp(false);
					this.registroEmpresa.getDomicilio().setIdCodigoPostal(-1L);
					this.registroEmpresa.getDomicilio().setCodigoPostal("");
				} // if
			} // if
			else{
				this.registroEmpresa.getDomicilio().setNuevoCp(false);
				this.registroEmpresa.getDomicilio().setIdCodigoPostal(-1L);
				this.registroEmpresa.getDomicilio().setCodigoPostal("");
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
			this.registroEmpresa.getDomicilio().setDomicilio(new Entity(-1L));
      this.registroEmpresa.getDomicilio().setIdDomicilio(-1L);
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
      this.registroEmpresa.getDomicilio().setDomicilio(new Entity(-1L));
      this.registroEmpresa.getDomicilio().setIdDomicilio(-1L);
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
			this.registroEmpresa.getDomicilio().setDomicilio(domicilio);
      this.registroEmpresa.getDomicilio().setIdDomicilio(domicilio.getKey());
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
		finally {
			
		} // finally
	}
	
  private void updateCodigoPostal() {
    List<UISelectItem> codigosPostales = null;
    try {
      if (this.registroEmpresa.getDomicilio().getIdCodigoPostal().equals(-1L)) {
        this.registroEmpresa.getDomicilio().setCodigoPostal("");
        this.registroEmpresa.getDomicilio().setNuevoCp(false);
      } // if
      else {
        codigosPostales = (List<UISelectItem>) this.attrs.get("codigosPostales");
        for (UISelectItem codigo : codigosPostales) {
          if (codigo.getValue().equals(this.registroEmpresa.getDomicilio().getIdCodigoPostal())) {
            this.registroEmpresa.getDomicilio().setCodigoPostal(codigo.getLabel());
            this.registroEmpresa.getDomicilio().setNuevoCp(true);
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
				if(!this.registroEmpresa.getDomicilio().getDomicilio().getKey().equals(-1L)){
					domicilios= (List<Entity>) this.attrs.get("domicilios");
					this.registroEmpresa.getDomicilio().setDomicilio(domicilios.get(domicilios.indexOf(this.registroEmpresa.getDomicilio().getDomicilio())));
					this.registroEmpresa.getDomicilio().setIdDomicilio(domicilios.get(domicilios.indexOf(this.registroEmpresa.getDomicilio().getDomicilio())).getKey());
				} // if
				else{
					this.registroEmpresa.getDomicilio().setDomicilio(new Entity(-1L));
					this.registroEmpresa.getDomicilio().setIdDomicilio(-1L);
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
			if (!this.registroEmpresa.getDomicilio().getIdDomicilio().equals(-1L)) {
        motor = new MotorBusqueda(this.registroEmpresa.getIdEmpresa());
        domicilio = motor.toDomicilio(this.registroEmpresa.getDomicilio().getIdDomicilio());
        this.registroEmpresa.getDomicilio().setNumeroExterior(domicilio.getNumeroExterior());
        this.registroEmpresa.getDomicilio().setNumeroInterior(domicilio.getNumeroInterior());
        this.registroEmpresa.getDomicilio().setCalle(domicilio.getCalle());
        this.registroEmpresa.getDomicilio().setAsentamiento(domicilio.getAsentamiento());
        this.registroEmpresa.getDomicilio().setEntreCalle(domicilio.getEntreCalle());
        this.registroEmpresa.getDomicilio().setYcalle(domicilio.getYcalle());
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
      this.registroEmpresa.getDomicilio().setNumeroExterior("");
      this.registroEmpresa.getDomicilio().setNumeroInterior("");
      this.registroEmpresa.getDomicilio().setCalle("");
      this.registroEmpresa.getDomicilio().setAsentamiento("");
      this.registroEmpresa.getDomicilio().setEntreCalle("");
      this.registroEmpresa.getDomicilio().setYcalle("");
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
  } // doLoadAtributos

  public void doAgregarDomicilio() {
    try {
      this.registroEmpresa.doAgregarEmpresaDomicilio();
      this.registroEmpresa.setDomicilio(new Domicilio());
      loadDomicilios();
      doLoadAtributos(true);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // doAgregarDomicilio

  public void doConsultarEmpresaDomicilio() {
    Domicilio domicilio = null;
    List<UISelectItem> codigos = null;
    try {
      this.registroEmpresa.doConsultarEmpresaDomicilio();
			domicilio = this.registroEmpresa.getDomicilioPivote();
      this.registroEmpresa.getDomicilio().setIdDomicilio(domicilio.getIdDomicilio());
      this.registroEmpresa.getDomicilio().setDomicilio(domicilio.getDomicilio());      			
      this.registroEmpresa.getDomicilio().setIdEntidad(domicilio.getIdEntidad());	
			this.registroEmpresa.getDomicilio().getDomicilio().put("idEntidad", new Value("idEntidad", domicilio.getIdEntidad().getKey()));
      toAsignaEntidad();
			loadMunicipios();
      this.registroEmpresa.getDomicilio().setIdMunicipio(domicilio.getIdMunicipio());			
			this.registroEmpresa.getDomicilio().getDomicilio().put("idMunicipio", new Value("idMunicipio", domicilio.getIdMunicipio().getKey()));
      toAsignaMunicipio();
			loadLocalidades();
      this.registroEmpresa.getDomicilio().setLocalidad(domicilio.getLocalidad());			
      this.registroEmpresa.getDomicilio().setIdLocalidad(domicilio.getIdLocalidad());			
			this.registroEmpresa.getDomicilio().getDomicilio().put("idLocalidad", new Value("idLocalidad", domicilio.getLocalidad().getKey()));
      toAsignaLocalidad();
			loadCodigosPostales();
      codigos = (List<UISelectItem>) this.attrs.get("codigosPostales");
      for (UISelectItem codigo : codigos) {
        if (codigo.getLabel().equals(domicilio.getCodigoPostal())) {
          this.registroEmpresa.getDomicilio().setIdCodigoPostal((Long) codigo.getValue());
          this.registroEmpresa.getDomicilio().setCodigoPostal(codigo.getLabel());
					this.registroEmpresa.getDomicilio().setNuevoCp(true);
        } // if
      }	// for      
      this.registroEmpresa.getDomicilio().setCalle(domicilio.getCalle());
      this.registroEmpresa.getDomicilio().setNumeroExterior(domicilio.getNumeroExterior());
      this.registroEmpresa.getDomicilio().setNumeroInterior(domicilio.getNumeroInterior());
      this.registroEmpresa.getDomicilio().setAsentamiento(domicilio.getAsentamiento());
      this.registroEmpresa.getDomicilio().setEntreCalle(domicilio.getEntreCalle());
      this.registroEmpresa.getDomicilio().setYcalle(domicilio.getYcalle());
      this.registroEmpresa.getDomicilio().setIdTipoDomicilio(domicilio.getIdTipoDomicilio());
      this.registroEmpresa.getDomicilio().setPrincipal(domicilio.getPrincipal());
			this.registroEmpresa.getDomicilio().setNuevoCp(domicilio.isNuevoCp());
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // doEliminarArticuloCodigo	
	
	public void doActualizaDomicilio(){
		try {
			this.registroEmpresa.doActualizarEmpresaDomicilio();
			this.registroEmpresa.setDomicilio(new Domicilio());
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
			this.registroEmpresa.doEliminarEmpresaDomicilio();;
			this.registroEmpresa.setDomicilio(new Domicilio());
      loadDomicilios();
      doLoadAtributos(true);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
	} // doActualizaDomicilio
	
	public void loadTiposEmpresas(){
		List<UISelectItem> tiposEmpresas= null;
		try {
			tiposEmpresas= new ArrayList<>();
			for(ETipoEmpresa tipoEmpresa: ETipoEmpresa.values())
				tiposEmpresas.add(new UISelectItem(tipoEmpresa.getIdTipoEmpresa(), tipoEmpresa.name().toUpperCase()));
			this.attrs.put("tiposEmpresas", tiposEmpresas);
			this.registroEmpresa.getEmpresa().setIdTipoEmpresa(Long.valueOf(tiposEmpresas.get(0).getValue().toString()));
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
	} // loadTiposEmpresas
	
	public void doLoadMatrices(){
		List<UISelectItem> matrices= null;
		Map<String, Object> params = null;
		List<String> campos       = null;
		try {
			if(this.registroEmpresa.getEmpresa().getIdTipoEmpresa().equals(ETipoEmpresa.SUCURSAL.getIdTipoEmpresa())){
				params= new HashMap<>();
				campos= new ArrayList<>();
				params.put(Constantes.SQL_CONDICION, "id_tipo_empresa=".concat(ETipoEmpresa.MATRIZ.getIdTipoEmpresa().toString()));
				campos.add("nombre");
				matrices= UISelect.build("TcManticEmpresasDto", "row", params, campos, " ", EFormatoDinamicos.MAYUSCULAS);
				this.attrs.put("matrices", matrices);
				this.attrs.put("isMatriz", true);
			} // if
			else
				this.attrs.put("isMatriz", false);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
		finally{
			Methods.clean(params);
			Methods.clean(campos);
		} // finally
	} // doLoadMatrices
}
