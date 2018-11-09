package mx.org.kaana.mantic.catalogos.almacenes.backing;

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
import mx.org.kaana.mantic.catalogos.almacenes.beans.RegistroAlmacen;
import mx.org.kaana.mantic.catalogos.almacenes.reglas.MotorBusqueda;
import mx.org.kaana.mantic.catalogos.almacenes.reglas.Transaccion;
import mx.org.kaana.mantic.catalogos.clientes.beans.Domicilio;
import mx.org.kaana.mantic.db.dto.TcManticDomiciliosDto;
import mx.org.kaana.mantic.enums.ETipoPersona;
import mx.org.kaana.mantic.enums.ETiposContactos;
import mx.org.kaana.mantic.enums.ETiposDomicilios;


@Named(value = "manticCatalogosAlamacenesAccion")
@ViewScoped
public class Accion extends IBaseAttribute implements Serializable {

  private static final long serialVersionUID = 327393488565639367L;  
	private RegistroAlmacen registroAlmacen;
	private UISelectEntity domicilioBusqueda;

	public RegistroAlmacen getRegistroAlmacen() {
		return registroAlmacen;
	}

	public void setRegistroAlmacen(RegistroAlmacen registroAlmacen) {
		this.registroAlmacen = registroAlmacen;
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
      this.attrs.put("idAlmacen", JsfBase.getFlashAttribute("idAlmacen"));
			this.attrs.put("codigo", "");
			this.attrs.put("articulos", new ArrayList<>());
      doLoad();      
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

	private void loadCollections(){
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
		loadResponsables();
	}
	
  public void doLoad() {
    EAccion eaccion= null;
    Long idAlmacen = -1L;
    try {
      eaccion = (EAccion) this.attrs.get("accion");
      this.attrs.put("nombreAccion", Cadena.letraCapital(eaccion.name()));
      switch (eaccion) {
        case AGREGAR:
          this.registroAlmacen = new RegistroAlmacen();
					loadCollections();
          break;
        case MODIFICAR:
        case CONSULTAR:
          idAlmacen = Long.valueOf(this.attrs.get("idAlmacen").toString());
          this.registroAlmacen = new RegistroAlmacen(idAlmacen);
					loadCollections();
					if(!this.registroAlmacen.getAlmacenDomicilio().isEmpty()){
						this.registroAlmacen.setAlmacenDomicilioSelecion(this.registroAlmacen.getAlmacenDomicilio().get(0));
						doConsultarAlmacenDomicilio();
					} // if
					/*if(!this.registroAlmacen.getAlmacenArticulo().isEmpty()){
						this.registroAlmacen.setAlmacenArticuloSeleccion(this.registroAlmacen.getAlmacenArticulo().get(0));
						doConsultarAlmacenArticulo();
					} // if*/
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
			if(!this.registroAlmacen.getAlmacenDomicilio().isEmpty()){
				transaccion = new Transaccion(this.registroAlmacen);
				if (transaccion.ejecutar((EAccion) this.attrs.get("accion"))) {
					regresar = "filtro".concat(Constantes.REDIRECIONAR);
					JsfBase.addMessage("Se registro el almacen de forma correcta.", ETipoMensaje.INFORMACION);
				} // if
				else 
					JsfBase.addMessage("Ocurrió un error al registrar el almacen", ETipoMensaje.ERROR);      
			} // if
			else
				JsfBase.addMessage("Es necesario que se captura por lo menos un domicilio", ETipoMensaje.ERROR);      
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
      for (ETiposContactos tipoContacto : ETiposContactos.values()) {
        tiposContactos.add(new UISelectItem(tipoContacto.getKey(), Cadena.reemplazarCaracter(tipoContacto.name(), '_', ' ')));
      } // for
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
    List<UISelectEntity> entidades = null;
    Map<String, Object> params = null;
		List<Columna>campos = null;
    try {
      params = new HashMap<>();
      params.put("idPais", 1);
      params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
			campos= new ArrayList<>();
			campos.add(new Columna("descripcion", EFormatoDinamicos.MAYUSCULAS));
      entidades = UIEntity.build("TcJanalEntidadesDto", "comboEntidades", params, campos, Constantes.SQL_TODOS_REGISTROS);
      this.attrs.put("entidades", entidades);    
      this.registroAlmacen.getDomicilio().setIdEntidad(entidades.get(0));
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
			if(!this.registroAlmacen.getDomicilio().getIdDomicilio().equals(-1L)){
				domicilio= this.registroAlmacen.getDomicilio().getDomicilio();
				entidades= (List<Entity>) this.attrs.get("entidades");
				for(Entity entidad: entidades){
					if(entidad.getKey().equals(domicilio.toLong("idEntidad")))
						this.registroAlmacen.getDomicilio().setIdEntidad(entidad);
				} // for
			} // if
			else
				this.registroAlmacen.getDomicilio().setIdEntidad(new Entity(-1L));
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
			if(!this.registroAlmacen.getDomicilio().getIdEntidad().getKey().equals(-1L)){
				params = new HashMap<>();
				params.put("idEntidad", this.registroAlmacen.getDomicilio().getIdEntidad().getKey());
				campos= new ArrayList<>();
				campos.add(new Columna("descripcion", EFormatoDinamicos.MAYUSCULAS));
				municipios = UIEntity.build("TcJanalMunicipiosDto", "comboMunicipios", params, campos, Constantes.SQL_TODOS_REGISTROS);
				this.attrs.put("municipios", municipios);
				this.registroAlmacen.getDomicilio().setIdMunicipio(municipios.get(0));
			} // if
			else
				this.registroAlmacen.getDomicilio().setIdMunicipio(new Entity(-1L));
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
			if(!this.registroAlmacen.getDomicilio().getIdMunicipio().getKey().equals(-1L)){
				domicilio= this.registroAlmacen.getDomicilio().getDomicilio();
				municipios= (List<Entity>) this.attrs.get("municipios");
				for(Entity municipio: municipios){
					if(municipio.getKey().equals(domicilio.toLong("idMunicipio")))
						this.registroAlmacen.getDomicilio().setIdMunicipio(municipio);
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
			if(!this.registroAlmacen.getDomicilio().getIdMunicipio().getKey().equals(-1L)){
				params = new HashMap<>();
				params.put("idMunicipio", this.registroAlmacen.getDomicilio().getIdMunicipio().getKey());
				campos= new ArrayList<>();
				campos.add(new Columna("descripcion", EFormatoDinamicos.MAYUSCULAS));
				localidades = UIEntity.build("TcJanalLocalidadesDto", "comboLocalidades", params, campos, Constantes.SQL_TODOS_REGISTROS);
				this.attrs.put("localidades", localidades);
				this.registroAlmacen.getDomicilio().setLocalidad(localidades.get(0));
				this.registroAlmacen.getDomicilio().setIdLocalidad(localidades.get(0).getKey());
			} // if
			else{
				this.registroAlmacen.getDomicilio().setLocalidad(new Entity(-1L));
				this.registroAlmacen.getDomicilio().setIdLocalidad(-1L);
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
			if(!this.registroAlmacen.getDomicilio().getIdDomicilio().equals(-1L)){
				domicilio= this.registroAlmacen.getDomicilio().getDomicilio();
				localidades= (List<Entity>) this.attrs.get("localidades");
				for(Entity localidad: localidades){
					if(localidad.getKey().equals(domicilio.toLong("idLocalidad"))){
						this.registroAlmacen.getDomicilio().setIdLocalidad(localidad.getKey());
						this.registroAlmacen.getDomicilio().setLocalidad(localidad);
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
			if(!this.registroAlmacen.getDomicilio().getIdEntidad().getKey().equals(-1L)){
				params = new HashMap<>();
				params.put(Constantes.SQL_CONDICION, "id_entidad=" + this.registroAlmacen.getDomicilio().getIdEntidad().getKey());
				codigosPostales = UISelect.build("TcManticCodigosPostalesDto", "row", params, "codigo", EFormatoDinamicos.MAYUSCULAS, Constantes.SQL_TODOS_REGISTROS);
				this.attrs.put("codigosPostales", codigosPostales);
				if (!codigosPostales.isEmpty()) {
					this.registroAlmacen.getDomicilio().setCodigoPostal(codigosPostales.get(0).getLabel());
					this.registroAlmacen.getDomicilio().setIdCodigoPostal((Long) codigosPostales.get(0).getValue());
					this.registroAlmacen.getDomicilio().setNuevoCp(true);
				} // if
				else 
					this.registroAlmacen.getDomicilio().setNuevoCp(false);				
			} // if
			else
				this.registroAlmacen.getDomicilio().setNuevoCp(false);    
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
			if(!this.registroAlmacen.getDomicilio().getIdDomicilio().equals(-1L)){
				domicilio= this.registroAlmacen.getDomicilio().getDomicilio();
				codigos= (List<UISelectItem>) this.attrs.get("codigosPostales");
				for(UISelectItem codigo: codigos){
					if(codigo.getLabel().equals(domicilio.toString("codigoPostal"))){
						this.registroAlmacen.getDomicilio().setCodigoPostal(codigo.getLabel());
						this.registroAlmacen.getDomicilio().setIdCodigoPostal((Long) codigo.getValue());
						this.registroAlmacen.getDomicilio().setNuevoCp(true);
						count++;
					} // if
				} // for
				if(count==0){
					this.registroAlmacen.getDomicilio().setNuevoCp(false);
					this.registroAlmacen.getDomicilio().setIdCodigoPostal(-1L);
					this.registroAlmacen.getDomicilio().setCodigoPostal("");
				} // if
			} // if
			else{
				this.registroAlmacen.getDomicilio().setNuevoCp(false);
				this.registroAlmacen.getDomicilio().setIdCodigoPostal(-1L);
				this.registroAlmacen.getDomicilio().setCodigoPostal("");
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
			this.registroAlmacen.getDomicilio().setDomicilio(new Entity(-1L));
      this.registroAlmacen.getDomicilio().setIdDomicilio(-1L);
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
      this.registroAlmacen.getDomicilio().setDomicilio(new Entity(-1L));
      this.registroAlmacen.getDomicilio().setIdDomicilio(-1L);
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
			this.registroAlmacen.getDomicilio().setDomicilio(domicilio);
      this.registroAlmacen.getDomicilio().setIdDomicilio(domicilio.getKey());
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
      if (this.registroAlmacen.getDomicilio().getIdCodigoPostal().equals(-1L)) {
        this.registroAlmacen.getDomicilio().setCodigoPostal("");
        this.registroAlmacen.getDomicilio().setNuevoCp(true);
      } // if
      else {
        codigosPostales = (List<UISelectItem>) this.attrs.get("codigosPostales");
        for (UISelectItem codigo : codigosPostales) {
          if (codigo.getValue().equals(this.registroAlmacen.getDomicilio().getIdCodigoPostal())) {
            this.registroAlmacen.getDomicilio().setCodigoPostal(codigo.getLabel());
            this.registroAlmacen.getDomicilio().setNuevoCp(false);
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
				if(!this.registroAlmacen.getDomicilio().getDomicilio().getKey().equals(-1L)){
					domicilios= (List<Entity>) this.attrs.get("domicilios");
					this.registroAlmacen.getDomicilio().setDomicilio(domicilios.get(domicilios.indexOf(this.registroAlmacen.getDomicilio().getDomicilio())));
					this.registroAlmacen.getDomicilio().setIdDomicilio(domicilios.get(domicilios.indexOf(this.registroAlmacen.getDomicilio().getDomicilio())).getKey());
				} // if
				else{
					this.registroAlmacen.getDomicilio().setDomicilio(new Entity(-1L));
					this.registroAlmacen.getDomicilio().setIdDomicilio(-1L);
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
			if (!this.registroAlmacen.getDomicilio().getIdDomicilio().equals(-1L)) {
        motor = new MotorBusqueda(this.registroAlmacen.getIdAlmacen());
        domicilio = motor.toDomicilio(this.registroAlmacen.getDomicilio().getIdDomicilio());
        this.registroAlmacen.getDomicilio().setNumeroExterior(domicilio.getNumeroExterior());
        this.registroAlmacen.getDomicilio().setNumeroInterior(domicilio.getNumeroInterior());
        this.registroAlmacen.getDomicilio().setCalle(domicilio.getCalle());
        this.registroAlmacen.getDomicilio().setAsentamiento(domicilio.getAsentamiento());
        this.registroAlmacen.getDomicilio().setEntreCalle(domicilio.getEntreCalle());
        this.registroAlmacen.getDomicilio().setYcalle(domicilio.getYcalle());
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
      this.registroAlmacen.getDomicilio().setNumeroExterior("");
      this.registroAlmacen.getDomicilio().setNumeroInterior("");
      this.registroAlmacen.getDomicilio().setCalle("");
      this.registroAlmacen.getDomicilio().setAsentamiento("");
      this.registroAlmacen.getDomicilio().setEntreCalle("");
      this.registroAlmacen.getDomicilio().setYcalle("");
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
  } // doLoadAtributos

  public void doAgregarAlmacen() {
    try {
      this.registroAlmacen.doAgregarAlmacenDomicilio();
      this.registroAlmacen.setDomicilio(new Domicilio());
      loadDomicilios();
      doLoadAtributos(true);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // doAgregarAlmacen

  public void doConsultarAlmacenDomicilio() {
    Domicilio domicilio = null;
    List<UISelectItem> codigos = null;
    try {
      this.registroAlmacen.doConsultarAlmacenDomicilio();
			domicilio = this.registroAlmacen.getDomicilioPivote();
      this.registroAlmacen.getDomicilio().setIdDomicilio(domicilio.getIdDomicilio());
      this.registroAlmacen.getDomicilio().setDomicilio(domicilio.getDomicilio());      			
      this.registroAlmacen.getDomicilio().setIdEntidad(domicilio.getIdEntidad());	
			this.registroAlmacen.getDomicilio().getDomicilio().put("idEntidad", new Value("idEntidad", domicilio.getIdEntidad().getKey()));
      toAsignaEntidad();
			loadMunicipios();
      this.registroAlmacen.getDomicilio().setIdMunicipio(domicilio.getIdMunicipio());			
			this.registroAlmacen.getDomicilio().getDomicilio().put("idMunicipio", new Value("idMunicipio", domicilio.getIdMunicipio().getKey()));
      toAsignaMunicipio();
			loadLocalidades();
      this.registroAlmacen.getDomicilio().setLocalidad(domicilio.getLocalidad());			
      this.registroAlmacen.getDomicilio().setIdLocalidad(domicilio.getIdLocalidad());			
			this.registroAlmacen.getDomicilio().getDomicilio().put("idLocalidad", new Value("idLocalidad", domicilio.getLocalidad().getKey()));
      toAsignaLocalidad();
			loadCodigosPostales();
      codigos = (List<UISelectItem>) this.attrs.get("codigosPostales");
      for (UISelectItem codigo : codigos) {
        if (codigo.getLabel().equals(domicilio.getCodigoPostal())) {
          this.registroAlmacen.getDomicilio().setIdCodigoPostal((Long) codigo.getValue());
          this.registroAlmacen.getDomicilio().setCodigoPostal(codigo.getLabel());
					this.registroAlmacen.getDomicilio().setNuevoCp(true);
        } // if
      }	// for      
      this.registroAlmacen.getDomicilio().setCalle(domicilio.getCalle());
      this.registroAlmacen.getDomicilio().setNumeroExterior(domicilio.getNumeroExterior());
      this.registroAlmacen.getDomicilio().setNumeroInterior(domicilio.getNumeroInterior());
      this.registroAlmacen.getDomicilio().setAsentamiento(domicilio.getAsentamiento());
      this.registroAlmacen.getDomicilio().setEntreCalle(domicilio.getEntreCalle());
      this.registroAlmacen.getDomicilio().setYcalle(domicilio.getYcalle());
      this.registroAlmacen.getDomicilio().setIdTipoDomicilio(domicilio.getIdTipoDomicilio());
      this.registroAlmacen.getDomicilio().setPrincipal(domicilio.getPrincipal());
			this.registroAlmacen.getDomicilio().setNuevoCp(domicilio.isNuevoCp());
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // doEliminarArticuloCodigo	
	
	public void doActualizaDomicilio(){
		try {
			this.registroAlmacen.doActualizarAlmacenDomicilio();
			this.registroAlmacen.setDomicilio(new Domicilio());
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
			this.registroAlmacen.doEliminarAlmacenDomicilio();
			this.registroAlmacen.setDomicilio(new Domicilio());
      loadDomicilios();
      doLoadAtributos(true);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
	} // doActualizaDomicilio
	
	private void loadResponsables(){
		List<UISelectItem> responsables= null;
		List<String> campos            = null;
    Map<String, Object> params     = null;
    try {
      params = new HashMap<>();
      params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
      params.put("idTipoPersona", ETipoPersona.RESPONSABLE.getIdTipoPersona());
			campos= new ArrayList<>();
			campos.add("nombres");
			campos.add("paterno");
			campos.add("materno");
      responsables = UISelect.build("VistaPersonasDto", "tipoPersona", params, campos, " ", EFormatoDinamicos.MAYUSCULAS, Constantes.SQL_TODOS_REGISTROS);
      this.attrs.put("responsables", responsables);
      if (!responsables.isEmpty()) 
        this.registroAlmacen.getAlmacen().setIdResponsable((Long) responsables.get(0).getValue());        
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    finally {
      Methods.clean(params);
      Methods.clean(campos);
    } // finally
	} // loadResponsables
	
	public void doConsultarArticulos(){
		List<UISelectEntity> articulos= null;
		Map<String, Object>params     = null;
		List<Columna>campos           = null;
		try {
			if(this.attrs.get("codigo")!= null && !Cadena.isVacio(this.attrs.get("codigo").toString()) && this.attrs.get("codigo").toString().length() > 3){
				params= new HashMap<>();
				params.put("codigo", this.attrs.get("codigo"));
				params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
				campos= new ArrayList<>();
				campos.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
				articulos= UIEntity.build("VistaArticulosAlmacenDto", "findArticulo", params, campos, Constantes.SQL_TODOS_REGISTROS);
				this.attrs.put("articulos", articulos);
				this.registroAlmacen.setResultadoBusquedaArticulo(articulos.get(0));
				this.registroAlmacen.doSeleccionarArticulo();
			} // if
			else
				JsfBase.addMessage("Cosultar articulos", "Captura un criterio de busqueda mayor a 3 caracteres", ETipoMensaje.INFORMACION);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
		finally {
			Methods.clean(params);
			Methods.clean(campos);
		} // finally
	} // doConsultarArticulos
	
	public void doConsultarAlmacenArticulo(){
		List<UISelectEntity>articulos= null;		
		try {
			this.registroAlmacen.doConsultarAlmacenArticulo();
			articulos= new ArrayList<>();
			articulos.add(new UISelectEntity(this.registroAlmacen.getAlmacenArticuloPivote().getArticulo()));
			this.attrs.put("articulos", articulos);
		} // try
		catch (Exception e) {			
			throw e;
		} // catch
	} // doConsultarAlmacenArticulo		
}