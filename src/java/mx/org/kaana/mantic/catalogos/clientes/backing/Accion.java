package mx.org.kaana.mantic.catalogos.clientes.backing;

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
import mx.org.kaana.mantic.catalogos.clientes.beans.ClienteContactoRepresentante;
import mx.org.kaana.mantic.catalogos.clientes.beans.Domicilio;
import mx.org.kaana.mantic.catalogos.clientes.reglas.Transaccion;
import mx.org.kaana.mantic.catalogos.clientes.beans.RegistroCliente;
import mx.org.kaana.mantic.catalogos.clientes.reglas.MotorBusqueda;
import mx.org.kaana.mantic.db.dto.TcManticDomiciliosDto;
import mx.org.kaana.mantic.enums.ETipoPersona;
import mx.org.kaana.mantic.enums.ETipoVenta;
import mx.org.kaana.mantic.enums.ETiposContactos;
import mx.org.kaana.mantic.enums.ETiposDomicilios;
import org.primefaces.event.SelectEvent;

@Named(value = "manticCatalogosClientesAccion")
@ViewScoped
public class Accion extends IBaseAttribute implements Serializable {

  private static final long serialVersionUID = -7668104942302148046L;
  private RegistroCliente registroCliente;
	private UISelectEntity domicilioBusqueda;

  public RegistroCliente getRegistroCliente() {
    return registroCliente;
  }

  public void setRegistroCliente(RegistroCliente registroCliente) {
    this.registroCliente = registroCliente;
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
      this.attrs.put("puntoVenta", JsfBase.getFlashAttribute("puntoVenta"));
      this.attrs.put("accion", JsfBase.getFlashAttribute("accion"));
      this.attrs.put("idCliente", JsfBase.getFlashAttribute("idCliente"));
			this.attrs.put("admin", JsfBase.isAdminEncuestaOrAdmin());
			this.attrs.put("cpNuevo", false);
      doLoad();      					
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

	private void loadCollections(){
		loadRepresentantes();
		loadTiposContactos();
		loadTiposDomicilios();	
		loadTiposVentas();
		loadDomicilios();
		loadEntidades();
		toAsignaEntidad();
		loadMunicipios();
		toAsignaMunicipio();
		loadLocalidades();
		toAsignaLocalidad();
		//loadCodigosPostales();      
		//toAsignaCodigoPostal();
	}
	
  public void doLoad() {
    EAccion eaccion = null;
    Long idCliente = -1L;
    try {
      eaccion = (EAccion) this.attrs.get("accion");
      this.attrs.put("nombreAccion", Cadena.letraCapital(eaccion.name()));
      switch (eaccion) {
        case AGREGAR:
          this.registroCliente = new RegistroCliente();
					loadCollections();
          break;
        case MODIFICAR:
        case CONSULTAR:
					this.attrs.put("cpNuevo", true);
          idCliente = Long.valueOf(this.attrs.get("idCliente").toString());
          this.registroCliente = new RegistroCliente(idCliente);
					loadCollections();
					doCompleteCodigoPostal(this.registroCliente.getDomicilio().getCodigoPostal());
					asignaCodigoPostal();
					if(!this.registroCliente.getClientesDomicilio().isEmpty()){
						this.registroCliente.setClienteDomicilioSelecion(this.registroCliente.getClientesDomicilio().get(0));
						this.doConsultarClienteDomicilio();
					} // if
					if(!this.registroCliente.getPersonasTiposContacto().isEmpty()){
						this.registroCliente.setPersonaTipoContacto(this.registroCliente.getPersonasTiposContacto().get(0));
						this.registroCliente.doConsultarRepresentante();
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
    Transaccion transaccion = null;
    String regresar = null;
    try {
      transaccion = new Transaccion(this.registroCliente);
      if (transaccion.ejecutar((EAccion) this.attrs.get("accion"))) {
				JsfBase.setFlashAttribute("puntoVenta", this.attrs.get("puntoVenta"));
        regresar = "filtro".concat(Constantes.REDIRECIONAR);
        JsfBase.addMessage("Se registro el cliente de forma correcta.", ETipoMensaje.INFORMACION);
      } // if
      else {
        JsfBase.addMessage("Ocurrió un error al registrar el cliente", ETipoMensaje.ERROR);
      }
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    return regresar;
  } // doAccion

  public String doCancelar() {
		JsfBase.setFlashAttribute("puntoVenta", this.attrs.get("puntoVenta"));
    return "filtro".concat(Constantes.REDIRECIONAR);
  } // doAccion

  private void loadRepresentantes() {
    List<UISelectItem> representantes = null;
    Map<String, Object> params = null;
    try {
      params = new HashMap<>();
      params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
      params.put("idTipoPersona", ETipoPersona.REPRESENTANTE_LEGAL.getIdTipoPersona());
      representantes = UISelect.build("VistaPersonasDto", "tipoPersona", params, "nombre", EFormatoDinamicos.MAYUSCULAS, Constantes.SQL_TODOS_REGISTROS);
      this.attrs.put("representantes", representantes);
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    finally {
      Methods.clean(params);
    } // finally
  } // loadRepresentantes

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
      this.registroCliente.getDomicilio().setIdEntidad(entidades.get(0));
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
			if(!this.registroCliente.getDomicilio().getIdDomicilio().equals(-1L)){
				domicilio= this.registroCliente.getDomicilio().getDomicilio();
				entidades= (List<Entity>) this.attrs.get("entidades");
				for(Entity entidad: entidades){
					if(entidad.getKey().equals(domicilio.toLong("idEntidad")))
						this.registroCliente.getDomicilio().setIdEntidad(entidad);
				} // for
			} // if
			else
				this.registroCliente.getDomicilio().setIdEntidad(new Entity(-1L));
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
			if(!this.registroCliente.getDomicilio().getIdEntidad().getKey().equals(-1L)){
				params = new HashMap<>();
				params.put("idEntidad", this.registroCliente.getDomicilio().getIdEntidad().getKey());
				campos= new ArrayList<>();
				campos.add(new Columna("descripcion", EFormatoDinamicos.MAYUSCULAS));
				municipios = UIEntity.build("TcJanalMunicipiosDto", "comboMunicipios", params, campos, Constantes.SQL_TODOS_REGISTROS);
				this.attrs.put("municipios", municipios);
				this.registroCliente.getDomicilio().setIdMunicipio(municipios.get(0));
			} // if
			else
				this.registroCliente.getDomicilio().setIdMunicipio(new Entity(-1L));
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
			if(!this.registroCliente.getDomicilio().getIdMunicipio().getKey().equals(-1L)){
				domicilio= this.registroCliente.getDomicilio().getDomicilio();
				municipios= (List<Entity>) this.attrs.get("municipios");
				for(Entity municipio: municipios){
					if(municipio.getKey().equals(domicilio.toLong("idMunicipio")))
						this.registroCliente.getDomicilio().setIdMunicipio(municipio);
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
			if(!this.registroCliente.getDomicilio().getIdMunicipio().getKey().equals(-1L)){
				params = new HashMap<>();
				params.put("idMunicipio", this.registroCliente.getDomicilio().getIdMunicipio().getKey());
				campos= new ArrayList<>();
				campos.add(new Columna("descripcion", EFormatoDinamicos.MAYUSCULAS));
				localidades = UIEntity.build("TcJanalLocalidadesDto", "comboLocalidades", params, campos, Constantes.SQL_TODOS_REGISTROS);
				this.attrs.put("localidades", localidades);
				this.registroCliente.getDomicilio().setLocalidad(localidades.get(0));
				this.registroCliente.getDomicilio().setIdLocalidad(localidades.get(0).getKey());
			} // if
			else{
				this.registroCliente.getDomicilio().setLocalidad(new Entity(-1L));
				this.registroCliente.getDomicilio().setIdLocalidad(-1L);
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
			if(!this.registroCliente.getDomicilio().getIdDomicilio().equals(-1L)){
				domicilio= this.registroCliente.getDomicilio().getDomicilio();
				localidades= (List<Entity>) this.attrs.get("localidades");
				for(Entity localidad: localidades){
					if(localidad.getKey().equals(domicilio.toLong("idLocalidad"))){
						this.registroCliente.getDomicilio().setIdLocalidad(localidad.getKey());
						this.registroCliente.getDomicilio().setLocalidad(localidad);
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
			if(!this.registroCliente.getDomicilio().getIdEntidad().getKey().equals(-1L)){
				params = new HashMap<>();
				params.put(Constantes.SQL_CONDICION, "id_entidad=" + this.registroCliente.getDomicilio().getIdEntidad().getKey());
				codigosPostales = UISelect.build("TcManticCodigosPostalesDto", "row", params, "codigo", EFormatoDinamicos.MAYUSCULAS, Constantes.SQL_TODOS_REGISTROS);
				this.attrs.put("codigosPostales", codigosPostales);
				if (!codigosPostales.isEmpty()) {
					this.registroCliente.getDomicilio().setCodigoPostal(codigosPostales.get(0).getLabel());
					this.registroCliente.getDomicilio().setIdCodigoPostal((Long) codigosPostales.get(0).getValue());
					this.registroCliente.getDomicilio().setNuevoCp(true);
				} // if
				else 
					this.registroCliente.getDomicilio().setNuevoCp(false);				
			} // if
			else
				this.registroCliente.getDomicilio().setNuevoCp(false);				
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
			if(!this.registroCliente.getDomicilio().getIdDomicilio().equals(-1L)){
				domicilio= this.registroCliente.getDomicilio().getDomicilio();
				codigos= (List<UISelectItem>) this.attrs.get("codigosPostales");
				for(UISelectItem codigo: codigos){
					if(codigo.getLabel().equals(domicilio.toString("codigoPostal"))){
						this.registroCliente.getDomicilio().setCodigoPostal(codigo.getLabel());
						this.registroCliente.getDomicilio().setIdCodigoPostal((Long) codigo.getValue());
						this.registroCliente.getDomicilio().setNuevoCp(true);
						count++;
					} // if
				} // for
				if(count==0){
					this.registroCliente.getDomicilio().setNuevoCp(false);
					this.registroCliente.getDomicilio().setIdCodigoPostal(-1L);
					this.registroCliente.getDomicilio().setCodigoPostal("");
				} // if
			} // if
			else{
				this.registroCliente.getDomicilio().setNuevoCp(false);
				this.registroCliente.getDomicilio().setIdCodigoPostal(-1L);
				this.registroCliente.getDomicilio().setCodigoPostal("");
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
			this.registroCliente.getDomicilio().setDomicilio(new Entity(-1L));
      this.registroCliente.getDomicilio().setIdDomicilio(-1L);
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
      this.registroCliente.getDomicilio().setDomicilio(new Entity(-1L));
      this.registroCliente.getDomicilio().setIdDomicilio(-1L);
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
			this.registroCliente.getDomicilio().setDomicilio(domicilio);
      this.registroCliente.getDomicilio().setIdDomicilio(domicilio.getKey());
			toAsignaEntidad();
			loadMunicipios();
			toAsignaMunicipio();
			loadLocalidades();
			toAsignaLocalidad();
			//loadCodigosPostales();      
			//toAsignaCodigoPostal();
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
      if (this.registroCliente.getDomicilio().getIdCodigoPostal().equals(-1L)) {
        this.registroCliente.getDomicilio().setCodigoPostal("");
        this.registroCliente.getDomicilio().setNuevoCp(false);
      } // if
      else {
        codigosPostales = (List<UISelectItem>) this.attrs.get("codigosPostales");
        for (UISelectItem codigo : codigosPostales) {
          if (codigo.getValue().equals(this.registroCliente.getDomicilio().getIdCodigoPostal())) {
            this.registroCliente.getDomicilio().setCodigoPostal(codigo.getLabel());
            this.registroCliente.getDomicilio().setNuevoCp(true);
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
      //loadCodigosPostales();      
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // doActualizaMunicipios

  public void doActualizaLocalidades() {
    try {
      loadLocalidades();
      //loadCodigosPostales();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // doActualizaMunicipios

  public void doActualizaCodigosPostales() {
    try {
      //loadCodigosPostales();
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
				if(!this.registroCliente.getDomicilio().getDomicilio().getKey().equals(-1L)){
					domicilios= (List<Entity>) this.attrs.get("domicilios");
					this.registroCliente.getDomicilio().setDomicilio(domicilios.get(domicilios.indexOf(this.registroCliente.getDomicilio().getDomicilio())));
					this.registroCliente.getDomicilio().setIdDomicilio(domicilios.get(domicilios.indexOf(this.registroCliente.getDomicilio().getDomicilio())).getKey());
				} // if
				else{
					this.registroCliente.getDomicilio().setDomicilio(new Entity(-1L));
					this.registroCliente.getDomicilio().setIdDomicilio(-1L);
				} // else					
				toAsignaEntidad();
				loadMunicipios();
				toAsignaMunicipio();
				loadLocalidades();
				toAsignaLocalidad();
				//loadCodigosPostales();      
				//toAsignaCodigoPostal();
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
			if (!this.registroCliente.getDomicilio().getIdDomicilio().equals(-1L)) {
        motor = new MotorBusqueda(this.registroCliente.getIdCliente());
        domicilio = motor.toDomicilio(this.registroCliente.getDomicilio().getIdDomicilio());
        this.registroCliente.getDomicilio().setNumeroExterior(domicilio.getNumeroExterior());
        this.registroCliente.getDomicilio().setNumeroInterior(domicilio.getNumeroInterior());
        this.registroCliente.getDomicilio().setCalle(domicilio.getCalle());
        this.registroCliente.getDomicilio().setAsentamiento(domicilio.getAsentamiento());
        this.registroCliente.getDomicilio().setEntreCalle(domicilio.getEntreCalle());
        this.registroCliente.getDomicilio().setYcalle(domicilio.getYcalle());
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
      this.registroCliente.getDomicilio().setNumeroExterior("");
      this.registroCliente.getDomicilio().setNumeroInterior("");
      this.registroCliente.getDomicilio().setCalle("");
      this.registroCliente.getDomicilio().setAsentamiento("");
      this.registroCliente.getDomicilio().setEntreCalle("");
      this.registroCliente.getDomicilio().setYcalle("");
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
  } // doLoadAtributos

  public void doAgregarCliente() {
    try {
      this.registroCliente.doAgregarClienteDomicilio();
      this.registroCliente.setDomicilio(new Domicilio());
      loadDomicilios();
      doLoadAtributos(true);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // doAgregarCliente

  public void doConsultarClienteDomicilio() {
    Domicilio domicilio = null;
    List<UISelectItem> codigos = null;
    try {
      this.registroCliente.doConsultarClienteDomicilio();
			domicilio = this.registroCliente.getDomicilioPivote();
      this.registroCliente.getDomicilio().setIdDomicilio(domicilio.getIdDomicilio());
      this.registroCliente.getDomicilio().setDomicilio(domicilio.getDomicilio());      			
      this.registroCliente.getDomicilio().setIdEntidad(domicilio.getIdEntidad());	
			this.registroCliente.getDomicilio().getDomicilio().put("idEntidad", new Value("idEntidad", domicilio.getIdEntidad().getKey()));
      toAsignaEntidad();
			loadMunicipios();
      this.registroCliente.getDomicilio().setIdMunicipio(domicilio.getIdMunicipio());			
			this.registroCliente.getDomicilio().getDomicilio().put("idMunicipio", new Value("idMunicipio", domicilio.getIdMunicipio().getKey()));
      toAsignaMunicipio();
			loadLocalidades();
      this.registroCliente.getDomicilio().setLocalidad(domicilio.getLocalidad());			
      this.registroCliente.getDomicilio().setIdLocalidad(domicilio.getIdLocalidad());			
			this.registroCliente.getDomicilio().getDomicilio().put("idLocalidad", new Value("idLocalidad", domicilio.getLocalidad().getKey()));
      toAsignaLocalidad();
			doCompleteCodigoPostal(domicilio.getCodigoPostal());
			asignaCodigoPostal();			
      this.registroCliente.getDomicilio().setCalle(domicilio.getCalle());
      this.registroCliente.getDomicilio().setNumeroExterior(domicilio.getNumeroExterior());
      this.registroCliente.getDomicilio().setNumeroInterior(domicilio.getNumeroInterior());
      this.registroCliente.getDomicilio().setAsentamiento(domicilio.getAsentamiento());
      this.registroCliente.getDomicilio().setEntreCalle(domicilio.getEntreCalle());
      this.registroCliente.getDomicilio().setYcalle(domicilio.getYcalle());
      this.registroCliente.getDomicilio().setIdTipoDomicilio(domicilio.getIdTipoDomicilio());
      this.registroCliente.getDomicilio().setPrincipal(domicilio.getPrincipal());
			this.registroCliente.getDomicilio().setNuevoCp(domicilio.isNuevoCp());
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // doEliminarArticuloCodigo	
	
	public void doActualizaDomicilio(){
		try {
			this.registroCliente.doActualizarClienteDomicilio();
			this.registroCliente.setDomicilio(new Domicilio());
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
			this.registroCliente.doEliminarClienteDomicilio();;
			this.registroCliente.setDomicilio(new Domicilio());
      loadDomicilios();
      doLoadAtributos(true);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
	} // doActualizaDomicilio
	
	public void doAgregarClienteRepresentante(){
		List<UISelectItem> representantes = null;
		try {
			representantes= (List<UISelectItem>) this.attrs.get("representantes");
			if(!representantes.isEmpty())
				this.registroCliente.doAgregarClienteRepresentante();
			else
				JsfBase.addMessage("Agregar representante", "No hay representantes registrados", ETipoMensaje.INFORMACION);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
	} // doAgregarClienteRepresentante
	
	public void doAgregarRepresentante() {
    try {
      this.registroCliente.doAgregarRepresentante();
      this.registroCliente.setPersonaTipoContactoPivote(new ClienteContactoRepresentante());      
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // doAgregarCliente
	
	public void doActualizaRepresentante(){
		try {
			this.registroCliente.doActualizaRepresentante();
      this.registroCliente.setPersonaTipoContactoPivote(new ClienteContactoRepresentante());      
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
	} // doActualizaRepresentante
	
	public void doEliminarRepresentante(){
		try {
      this.registroCliente.doEliminarRepresentante();
      this.registroCliente.setPersonaTipoContactoPivote(new ClienteContactoRepresentante());      
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
	} // doEliminarRepresentante
	
	private void loadTiposVentas(){
		List<UISelectItem> tiposVentas= null;
		try {
			tiposVentas= new ArrayList<>();
			for(ETipoVenta tipoVenta: ETipoVenta.values())
				tiposVentas.add(new UISelectItem(tipoVenta.getIdTipoVenta(), Cadena.reemplazarCaracter(tipoVenta.name(), '_', ' ')));
			this.attrs.put("tiposVentas", tiposVentas);
		} // try
		catch (Exception e) {
			throw e;
		} // catch		
	} // loadTiposVentas
	
	public List<UISelectEntity> doCompleteCodigoPostal(String query) {		
		if(this.registroCliente.getDomicilio().getIdEntidad().getKey()>= 1L && !Cadena.isVacio(query)){
			this.attrs.put("condicionCodigoPostal", query);
			this.doUpdateCodigosPostales();		
			return (List<UISelectEntity>)this.attrs.get("allCodigosPostales");
		} // if
		else{
			this.registroCliente.getDomicilio().setNuevoCp(false);
			this.registroCliente.getDomicilio().setIdCodigoPostal(-1L);
			this.registroCliente.getDomicilio().setCodigoPostal("");
			return new ArrayList<>();
		} // else		
	}	// doCompleteCliente
	
	public void doUpdateCodigosPostales() {
		List<Columna> columns     = null;
    Map<String, Object> params= null;
    try {
			params= new HashMap<>();
			columns= new ArrayList<>();
      columns.add(new Columna("codigo", EFormatoDinamicos.MAYUSCULAS));      
  		params.put(Constantes.SQL_CONDICION, "id_entidad=" + this.registroCliente.getDomicilio().getIdEntidad().getKey() + " and codigo like '" + this.attrs.get("condicionCodigoPostal") + "%'");						  		
      this.attrs.put("allCodigosPostales", (List<UISelectEntity>) UIEntity.build("TcManticCodigosPostalesDto", "row", params, columns, 20L));
		} // try
	  catch (Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    } // finally
	}	// doUpdateClientes
	
	public void doAsignaCodigoPostal(SelectEvent event) {
		UISelectEntity seleccion            = null;
		List<UISelectEntity> codigosPostales= null;
		try {
			codigosPostales= (List<UISelectEntity>) this.attrs.get("allCodigosPostales");
			seleccion= codigosPostales.get(codigosPostales.indexOf((UISelectEntity)event.getObject()));
			this.registroCliente.getDomicilio().setCodigoPostal(seleccion.toString("codigo"));
      this.registroCliente.getDomicilio().setNuevoCp(true);
			this.registroCliente.getDomicilio().setIdCodigoPostal(seleccion.getKey());
			this.attrs.put("codigoSeleccionado", seleccion);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
	} // doAsignaCliente
	
	public void asignaCodigoPostal() {
		List<UISelectEntity> codigosPostales= null;
		try {
			codigosPostales= (List<UISelectEntity>) this.attrs.get("allCodigosPostales");
			if(codigosPostales!= null && !codigosPostales.isEmpty()){
				this.registroCliente.getDomicilio().setCodigoPostal(codigosPostales.get(0).toString("codigo"));
				this.registroCliente.getDomicilio().setNuevoCp(true);
				this.registroCliente.getDomicilio().setIdCodigoPostal(codigosPostales.get(0).getKey());
				this.attrs.put("codigoSeleccionado", codigosPostales.get(0));
			} // if
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
	} // doAsignaCliente
	
	public void doInicializaCodigo(){
		try {
			this.registroCliente.getDomicilio().setIdCodigoPostal(-1L);
			this.registroCliente.getDomicilio().setCodigoPostal("");
			if((Boolean)this.attrs.get("cpNuevo")){
				this.registroCliente.getDomicilio().setNuevoCp(true);		
				this.attrs.put("codigoSeleccionado", new UISelectEntity(-1L));
			} // 				
			else
				this.registroCliente.getDomicilio().setNuevoCp(false);			
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);		
		} // catch		
	} // doInicializaCodigo
}
