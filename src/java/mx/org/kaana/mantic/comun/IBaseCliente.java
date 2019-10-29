package mx.org.kaana.mantic.comun;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelect;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.pagina.UISelectItem;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.clientes.beans.Domicilio;
import mx.org.kaana.mantic.catalogos.clientes.reglas.MotorBusqueda;
import mx.org.kaana.mantic.db.dto.TcManticClientesDto;
import mx.org.kaana.mantic.db.dto.TcManticDomiciliosDto;
import mx.org.kaana.mantic.db.dto.TrManticClienteTipoContactoDto;
import mx.org.kaana.mantic.enums.ETipoVenta;
import mx.org.kaana.mantic.enums.ETiposContactos;
import mx.org.kaana.mantic.enums.ETiposDomicilios;
import mx.org.kaana.mantic.ventas.beans.ClienteVenta;
import mx.org.kaana.mantic.ventas.reglas.Transaccion;

public abstract class IBaseCliente extends IBaseArticulos implements Serializable{

	private static final long serialVersionUID = -3665820954965883158L;
	private Domicilio domicilio;
	private UISelectEntity domicilioBusqueda;

	public IBaseCliente(String precio) {
		super(precio);
	}	
	
	public UISelectEntity getDomicilioBusqueda() {
		return domicilioBusqueda;
	}

	public void setDomicilioBusqueda(UISelectEntity domicilioBusqueda) {
		this.domicilioBusqueda = domicilioBusqueda;
	}

	public Domicilio getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(Domicilio domicilio) {
		this.domicilio = domicilio;
	}
	
	private void loadCollections(){		
		loadTiposDomicilios();	
		loadTiposVentas();
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
	
	protected void loadDefaultCollections(){				
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
	
	public void initCliente() { 
		TcManticClientesDto registroCliente= null;
    try {      
			registroCliente= new TcManticClientesDto();
			this.domicilio= new Domicilio();
			this.attrs.put("registroCliente", registroCliente);
      loadCollections();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // doLoad
	
	public String doAceptarCliente() {
    Transaccion transaccion  = null;
		ClienteVenta clienteVenta= null;
    String regresar          = null;
    try {
			clienteVenta= toClienteVenta();			
      transaccion = new Transaccion(clienteVenta);
      if (transaccion.ejecutar(EAccion.ASIGNAR)) {
        regresar = "accion".concat(Constantes.REDIRECIONAR);
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

	private ClienteVenta toClienteVenta(){
		ClienteVenta regresar                         = null;
		List<TrManticClienteTipoContactoDto> contactos= null;
		TrManticClienteTipoContactoDto contacto       = null;
		try {
			regresar= new ClienteVenta();
			regresar.setCliente((TcManticClientesDto) this.attrs.get("registroCliente"));
			regresar.setDomicilio(this.domicilio);
			contactos= new ArrayList<>();
			contacto= new TrManticClienteTipoContactoDto();
			contacto.setIdTipoContacto(ETiposContactos.CORREO.getKey());
			contacto.setValor(this.attrs.get("correo").toString());
			contacto.setOrden(1L);
			contactos.add(contacto);
			contacto= new TrManticClienteTipoContactoDto();
			contacto.setIdTipoContacto(ETiposContactos.TELEFONO.getKey());
			contacto.setValor(this.attrs.get("telefono").toString());
			contacto.setOrden(2L);
			contactos.add(contacto);
			contacto= new TrManticClienteTipoContactoDto();
			contacto.setIdTipoContacto(ETiposContactos.CELULAR.getKey());
			contacto.setValor(this.attrs.get("celular").toString());
			contacto.setOrden(3L);
			contactos.add(contacto);
			regresar.setContacto(contactos);
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toClienteVenta
	
	private void loadTiposDomicilios() {
    List<UISelectItem> tiposDomicilios= null;
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
	
	private void loadDomicilios() {
		List<UISelectEntity> domicilios= null;
		try {
			domicilios= new ArrayList<>();
			this.attrs.put("domicilios", domicilios);     
			this.domicilio.setDomicilio(new Entity(-1L));
      this.domicilio.setIdDomicilio(-1L);
		} // try
		catch (Exception e) {		
			throw e;
		} // catch		
	} // loadDomicilios
	
	private void loadEntidades() {
    List<UISelectEntity> entidades= null;
		List<Columna>campos           = null;
    Map<String, Object> params    = null;
    try {
      params = new HashMap<>();
      params.put("idPais", 1);
      params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
			campos= new ArrayList<>();
			campos.add(new Columna("descripcion", EFormatoDinamicos.MAYUSCULAS));
      entidades = UIEntity.build("TcJanalEntidadesDto", "comboEntidades", params, campos, Constantes.SQL_TODOS_REGISTROS);
      this.attrs.put("entidades", entidades);
      this.domicilio.setIdEntidad(entidades.get(0));
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    finally {
      Methods.clean(params);
    } // finally
  } // loadEntidades
	
	private void toAsignaEntidad(){
		Entity domicilio     = null;
		List<Entity>entidades= null;
		try {
			if(!this.domicilio.getIdDomicilio().equals(-1L)){
				domicilio= this.domicilio.getDomicilio();
				entidades= (List<Entity>) this.attrs.get("entidades");
				for(Entity entidad: entidades){
					if(entidad.getKey().equals(domicilio.toLong("idEntidad")))
						this.domicilio.setIdEntidad(entidad);
				} // for
			} // if
			else
				this.domicilio.setIdEntidad(new Entity(-1L));
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
	} // toAsignaEntidad

  private void loadMunicipios() {
    List<UISelectEntity> municipios= null;
    Map<String, Object> params     = null;
		List<Columna>campos            = null;
    try {
			if(!this.domicilio.getIdEntidad().getKey().equals(-1L)){
				params = new HashMap<>();
				params.put("idEntidad", this.domicilio.getIdEntidad().getKey());
				campos= new ArrayList<>();
				campos.add(new Columna("descripcion", EFormatoDinamicos.MAYUSCULAS));
				municipios = UIEntity.build("TcJanalMunicipiosDto", "comboMunicipios", params, campos, Constantes.SQL_TODOS_REGISTROS);
				this.attrs.put("municipios", municipios);
				this.domicilio.setIdMunicipio(municipios.get(0));
			} // if
			else
				this.domicilio.setIdMunicipio(new Entity(-1L));
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    finally {
      Methods.clean(params);
    } // finally
  } // loadMunicipios
	
	private void toAsignaMunicipio(){
		Entity domicilio      = null;
		List<Entity>municipios= null;
		try {
			if(!this.domicilio.getIdMunicipio().getKey().equals(-1L)){
				domicilio= this.domicilio.getDomicilio();
				municipios= (List<Entity>) this.attrs.get("municipios");
				for(Entity municipio: municipios){
					if(municipio.getKey().equals(domicilio.toLong("idMunicipio")))
						this.domicilio.setIdMunicipio(municipio);
				} // for
			} // if
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
	} // toAsignaMunicipio

  private void loadLocalidades() {
    List<UISelectEntity> localidades= null;
    Map<String, Object> params      = null;
		List<Columna>campos             = null;
    try {
			if(!this.domicilio.getIdMunicipio().getKey().equals(-1L)){
				params = new HashMap<>();
				params.put("idMunicipio", this.domicilio.getIdMunicipio().getKey());
				campos= new ArrayList<>();
				campos.add(new Columna("descripcion", EFormatoDinamicos.MAYUSCULAS));
				localidades = UIEntity.build("TcJanalLocalidadesDto", "comboLocalidades", params, campos, Constantes.SQL_TODOS_REGISTROS);
				this.attrs.put("localidades", localidades);
				this.domicilio.setLocalidad(localidades.get(0));
				this.domicilio.setIdLocalidad(localidades.get(0).getKey());
			} // if
			else{
				this.domicilio.setLocalidad(new Entity(-1L));
				this.domicilio.setIdLocalidad(-1L);
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
		Entity domicilio       = null;
		List<Entity>localidades= null;
		try {
			if(!this.domicilio.getIdDomicilio().equals(-1L)){
				domicilio= this.domicilio.getDomicilio();
				localidades= (List<Entity>) this.attrs.get("localidades");
				for(Entity localidad: localidades){
					if(localidad.getKey().equals(domicilio.toLong("idLocalidad"))){
						this.domicilio.setIdLocalidad(localidad.getKey());
						this.domicilio.setLocalidad(localidad);
					} // if
				} // for
			} // if			
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
	} // toAsignaLocalidad

  private void loadCodigosPostales() {
    List<UISelectItem> codigosPostales= null;
    Map<String, Object> params        = null;
    try {
			if(!this.domicilio.getIdEntidad().getKey().equals(-1L)){
				params = new HashMap<>();
				params.put(Constantes.SQL_CONDICION, "id_entidad=" + this.domicilio.getIdEntidad().getKey());
				codigosPostales = UISelect.build("TcManticCodigosPostalesDto", "row", params, "codigo", EFormatoDinamicos.MAYUSCULAS, Constantes.SQL_TODOS_REGISTROS);
				this.attrs.put("codigosPostales", codigosPostales);
				if (!codigosPostales.isEmpty()) {
					this.domicilio.setCodigoPostal(codigosPostales.get(0).getLabel());
					this.domicilio.setIdCodigoPostal((Long) codigosPostales.get(0).getValue());
					this.domicilio.setNuevoCp(true);
				} // if
				else 
					this.domicilio.setNuevoCp(false);				
			} // if
			else
				this.domicilio.setNuevoCp(false);				
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    finally {
      Methods.clean(params);
    } // finally
  } // loadCodigosPostales

	private void toAsignaCodigoPostal(){
		Entity domicilio         = null;
		List<UISelectItem>codigos= null;
		int count=0;
		try {
			if(!this.domicilio.getIdDomicilio().equals(-1L)){
				domicilio= this.domicilio.getDomicilio();
				codigos= (List<UISelectItem>) this.attrs.get("codigosPostales");
				for(UISelectItem codigo: codigos){
					if(codigo.getLabel().equals(domicilio.toString("codigoPostal"))){
						this.domicilio.setCodigoPostal(codigo.getLabel());
						this.domicilio.setIdCodigoPostal((Long) codigo.getValue());
						this.domicilio.setNuevoCp(true);
						count++;
					} // if
				} // for
				if(count==0){
					this.domicilio.setNuevoCp(false);
					this.domicilio.setIdCodigoPostal(-1L);
					this.domicilio.setCodigoPostal("");
				} // if
			} // if
			else{
				this.domicilio.setNuevoCp(false);
				this.domicilio.setIdCodigoPostal(-1L);
				this.domicilio.setCodigoPostal("");
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
	
  public void doBusquedaDomicilios() {
    List<UISelectEntity> domicilios= null;
    Map<String, Object> params     = null;
		List<Columna>campos            = null;
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
      this.domicilio.setDomicilio(new Entity(-1L));
      this.domicilio.setIdDomicilio(-1L);
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
			this.domicilio.setDomicilio(domicilio);
      this.domicilio.setIdDomicilio(domicilio.getKey());
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
	}
	
	public void doAsignaDomicilioClienteInicial(Long idCliente){
		TcManticClientesDto registroCliente= null;
		MotorBusqueda motorBusqueda        = null;
		Entity domicilioCliente            = null;
		List<UISelectEntity> domicilios    = null;
		try {
			loadTiposDomicilios();
			loadTiposVentas();
			motorBusqueda= new MotorBusqueda(idCliente);
			registroCliente= motorBusqueda.toCliente();
			this.attrs.put("registroCliente", registroCliente);
			domicilioCliente= motorBusqueda.toDomicilioCliente();
			domicilios= new ArrayList<>();
			if(domicilioCliente!= null)
				domicilios.add(new UISelectEntity(domicilioCliente));
			this.attrs.put("domicilios", domicilios);			
			this.domicilio= new Domicilio();
			if(domicilioCliente!= null){
				this.domicilio.setDomicilio(domicilioCliente);
				this.domicilio.setIdDomicilio(domicilioCliente.getKey());
			} // if
			loadEntidades();
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
	}
	
  private void updateCodigoPostal() {
    List<UISelectItem> codigosPostales= null;
    try {
      if (this.domicilio.getIdCodigoPostal().equals(-1L)) {
        this.domicilio.setCodigoPostal("");
        this.domicilio.setNuevoCp(false);
      } // if
      else {
        codigosPostales = (List<UISelectItem>) this.attrs.get("codigosPostales");
        for (UISelectItem codigo : codigosPostales) {
          if (codigo.getValue().equals(this.domicilio.getIdCodigoPostal())) {
            this.domicilio.setCodigoPostal(codigo.getLabel());
            this.domicilio.setNuevoCp(true);
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
				if(!this.domicilio.getDomicilio().getKey().equals(-1L)){
					domicilios= (List<Entity>) this.attrs.get("domicilios");
					this.domicilio.setDomicilio(domicilios.get(domicilios.indexOf(this.domicilio.getDomicilio())));
					this.domicilio.setIdDomicilio(domicilios.get(domicilios.indexOf(this.domicilio.getDomicilio())).getKey());
				} // if
				else{
					this.domicilio.setDomicilio(new Entity(-1L));
					this.domicilio.setIdDomicilio(-1L);
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
		TcManticClientesDto cliente    = null;
		try {
			cliente= (TcManticClientesDto) this.attrs.get("registroCliente");
			if (!this.domicilio.getIdDomicilio().equals(-1L)) {
        motor = new MotorBusqueda(cliente.getIdCliente());
        domicilio = motor.toDomicilio(this.domicilio.getIdDomicilio());
        this.domicilio.setNumeroExterior(domicilio.getNumeroExterior());
        this.domicilio.setNumeroInterior(domicilio.getNumeroInterior());
        this.domicilio.setCalle(domicilio.getCalle());
        this.domicilio.setAsentamiento(domicilio.getAsentamiento());
        this.domicilio.setEntreCalle(domicilio.getEntreCalle());
        this.domicilio.setYcalle(domicilio.getYcalle());
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
      this.domicilio.setNumeroExterior("");
      this.domicilio.setNumeroInterior("");
      this.domicilio.setCalle("");
      this.domicilio.setAsentamiento("");
      this.domicilio.setEntreCalle("");
      this.domicilio.setYcalle("");
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
  } // doLoadAtributos    
}
