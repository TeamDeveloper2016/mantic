package mx.org.kaana.mantic.ventas.backing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.libs.formato.Error;
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
import mx.org.kaana.mantic.catalogos.clientes.beans.ClienteTipoContacto;
import mx.org.kaana.mantic.catalogos.clientes.beans.Domicilio;
import mx.org.kaana.mantic.ventas.reglas.Transaccion;
import mx.org.kaana.mantic.catalogos.clientes.reglas.MotorBusqueda;
import mx.org.kaana.mantic.db.dto.TcManticClientesDto;
import mx.org.kaana.mantic.db.dto.TcManticDomiciliosDto;
import mx.org.kaana.mantic.db.dto.TrManticClienteTipoContactoDto;
import mx.org.kaana.mantic.enums.ETipoVenta;
import mx.org.kaana.mantic.enums.ETiposContactos;
import mx.org.kaana.mantic.enums.ETiposDomicilios;
import mx.org.kaana.mantic.ventas.beans.ClienteVenta;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.primefaces.event.SelectEvent;

@Named(value = "manticVentasCliente")
@ViewScoped
public class Cliente extends IBaseAttribute implements Serializable {

  private static final Log LOG = LogFactory.getLog(Cliente.class);
  private static final long serialVersionUID = -7668104942302148046L;
  
	private Domicilio domicilio;
	private UISelectEntity domicilioBusqueda;
  private UISelectEntity ikRegimenFiscal;  

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
	
	public void setIkRegimenFiscal(UISelectEntity ikRegimenFiscal) {
		this.ikRegimenFiscal=ikRegimenFiscal;
    TcManticClientesDto cliente= (TcManticClientesDto)this.attrs.get("registroCliente");
    if(this.ikRegimenFiscal!= null && cliente!= null)
  	  cliente.setIdRegimenFiscal(this.ikRegimenFiscal.getKey());
	}

	public UISelectEntity getIkRegimenFiscal() {
		return ikRegimenFiscal;
	}  
  
  @PostConstruct
  @Override
  protected void init() {
    try {
			this.attrs.put("regreso", JsfBase.getFlashAttribute("regreso"));
			this.attrs.put("idCliente", JsfBase.getFlashAttribute("idCliente"));
			this.attrs.put("idFicticia", JsfBase.getFlashAttribute("idFicticia"));
			this.attrs.put("idVenta", JsfBase.getFlashAttribute("idVenta"));
			this.attrs.put("observaciones", JsfBase.getFlashAttribute("observaciones"));
			this.attrs.put("accion", JsfBase.getFlashAttribute("accion"));
			this.attrs.put("admin", JsfBase.isAdminEncuestaOrAdmin());
			this.attrs.put("contactos", new ArrayList<>());
			this.attrs.put("cpNuevo", false);
      this.doLoad();      					
      this.doLoadRegimenesFiscales();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

	private void loadCollections() {		
		this.loadTiposDomicilios();	
		this.loadTiposVentas();
		this.loadDomicilios();
		this.loadEntidades();
		this.toAsignaEntidad();
		this.loadMunicipios();
		this.toAsignaMunicipio();
		this.loadLocalidades();
		this.toAsignaLocalidad();
		//loadCodigosPostales();      
		//toAsignaCodigoPostal();
	}
	
  public void doLoad() { 
		TcManticClientesDto registroCliente= null;
		EAccion accion                     = null;
		Long idCliente                     = -1L;
    try {      
			accion= this.attrs.get("idCliente")!= null && !Long.valueOf(this.attrs.get("idCliente").toString()).equals(-1L) ? EAccion.MODIFICAR : EAccion.AGREGAR;
			switch(accion){
				case MODIFICAR:
					idCliente= Long.valueOf(this.attrs.get("idCliente").toString());
					registroCliente= (TcManticClientesDto) DaoFactory.getInstance().findById(TcManticClientesDto.class, idCliente);					
					this.loadTiposDomicilios();	
					this.loadTiposVentas();
					this.loadDomicilioActual(idCliente);
					this.loadContactosActual(idCliente);
					this.attrs.put("cpNuevo", true);
					break;
				case AGREGAR:
					registroCliente= new TcManticClientesDto();
					this.domicilio= new Domicilio();					
					this.loadCollections();
					break;
			} // switch
      this.toLoadTiposClientes();
			this.attrs.put("registroCliente", registroCliente);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // doLoad

	private void loadDomicilioActual(Long idCliente) throws Exception {
		MotorBusqueda motor            = null;		
		List<UISelectEntity> domicilios= null;
		try {
			motor= new MotorBusqueda(idCliente);			
			this.domicilio= motor.toClienteDomicilioPrinicipal(true);
			domicilios    = this.doBusquedaDomiciliosActual(this.domicilio.getIdDomicilio());
			this.attrs.put("domicilios", domicilios);		
      if(domicilios!= null && !domicilios.isEmpty()) {
			  this.domicilio.setDomicilio(domicilios.get(0));
  			this.doCompleteCodigoPostal(this.domicilio.getDomicilio().toString("codigoPostal"));
      } // if  
			this.loadEntidades();
			this.toAsignaEntidad();
			this.loadMunicipios();
			this.toAsignaMunicipio();
			this.loadLocalidades();
			this.toAsignaLocalidad();
			this.asignaCodigoPostal();
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
	} // loadDomicilioActual
	
	private void loadContactosActual(Long idCliente) throws Exception {
		MotorBusqueda motor                = null;		
		List<ClienteTipoContacto> contactos= null;
		try {
			motor= new MotorBusqueda(idCliente);			
			contactos= motor.toClientesTipoContacto();			
			this.attrs.put("contactos", contactos);
			for(ClienteTipoContacto contacto: contactos){
				if(contacto.getIdTipoContacto().equals(ETiposContactos.CORREO.getKey()))
					this.attrs.put("correo", contacto.getValor());
				else if(contacto.getIdTipoContacto().equals(ETiposContactos.TELEFONO.getKey()))
					this.attrs.put("telefono", contacto.getValor());
				else if(contacto.getIdTipoContacto().equals(ETiposContactos.CELULAR.getKey()))
					this.attrs.put("celular", contacto.getValor());
			} // for
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
	} // loadDomicilioActual
	
	private List<UISelectEntity> doBusquedaDomiciliosActual(Long idDomicilio) {
    List<UISelectEntity> regresar= null;
    Map<String, Object> params   = null;
		List<Columna>campos          = null;
    try {
      params = new HashMap<>();      
			campos= new ArrayList<>();
      params.put(Constantes.SQL_CONDICION, "tc_mantic_domicilios.id_domicilio=".concat(idDomicilio.toString()));			
			campos.add(new Columna("calle", EFormatoDinamicos.MAYUSCULAS));
			campos.add(new Columna("numeroExterior", EFormatoDinamicos.MAYUSCULAS));
			campos.add(new Columna("numeroInterior", EFormatoDinamicos.MAYUSCULAS));
			campos.add(new Columna("asentamiento", EFormatoDinamicos.MAYUSCULAS));
			campos.add(new Columna("entidad", EFormatoDinamicos.MAYUSCULAS));
			campos.add(new Columna("municipio", EFormatoDinamicos.MAYUSCULAS));
			campos.add(new Columna("domicilio", EFormatoDinamicos.MAYUSCULAS));
      regresar = UIEntity.build("VistaDomiciliosCatalogosDto", "domicilios", params, campos, Constantes.SQL_TODOS_REGISTROS);      
			this.attrs.put("resultados", regresar.size());      
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    finally {
      Methods.clean(params);
    } // finally
		return regresar;
  } // doLoadDomicilios
	
  public String doAceptar() {
    Transaccion transaccion  = null;
		ClienteVenta clienteVenta= null;
    String regresar          = null;
		EAccion accion           = null;
    try {
			accion      = this.attrs.get("idCliente")!= null && !Long.valueOf(this.attrs.get("idCliente").toString()).equals(-1L)? EAccion.TRANSFORMACION: EAccion.ASIGNAR;
			clienteVenta= this.toClienteVenta();			
      if(clienteVenta.getCliente()!= null && clienteVenta.getCliente().getIdRegimenFiscal()!= null && Objects.equals(clienteVenta.getCliente().getIdRegimenFiscal(), -1L))
        clienteVenta.getCliente().setIdRegimenFiscal(null);
      transaccion = new Transaccion(clienteVenta);
      if (transaccion.ejecutar(accion)) {
				if(this.attrs.get("regreso")!= null) {
					JsfBase.setFlashAttribute("observaciones", this.attrs.get("observaciones"));																							
					JsfBase.setFlashAttribute("idFicticia", this.attrs.get("idFicticia"));																							
					JsfBase.setFlashAttribute("idVenta", this.attrs.get("idVenta"));
					if(accion.equals(EAccion.ASIGNAR))						
						JsfBase.setFlashAttribute("idCliente", transaccion.getIdClienteNuevo());					
					else
						JsfBase.setFlashAttribute("idCliente", this.attrs.get("idCliente"));					
					JsfBase.setFlashAttribute("accion", (this.attrs.get("idFicticia")!= null && !Long.valueOf(this.attrs.get("idFicticia").toString()).equals(-1L)) || this.attrs.get("idVenta")!= null && !Long.valueOf(this.attrs.get("idVenta").toString()).equals(-1L) ? EAccion.MODIFICAR : EAccion.AGREGAR);
					regresar= this.attrs.get("regreso").toString().concat(Constantes.REDIRECIONAR);
				} // if
				else
					regresar = "accion".concat(Constantes.REDIRECIONAR);
        JsfBase.addMessage("Se " + (accion.equals(EAccion.TRANSFORMACION) ? "modific�" : "agreg�") + "registro el cliente de forma correcta", ETipoMensaje.INFORMACION);
      } // if
      else 
        JsfBase.addMessage("Ocurri� un error al " + (accion.equals(EAccion.TRANSFORMACION)? "modificar": "agregar") + " el cliente", ETipoMensaje.ERROR);
      if(clienteVenta.getCliente()!= null && clienteVenta.getCliente().getIdRegimenFiscal()!= null)
        clienteVenta.getCliente().setIdRegimenFiscal(-1L);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    return regresar;
  } // doAccion

	private ClienteVenta toClienteVenta() {
		ClienteVenta regresar                         = new ClienteVenta();
		List<TrManticClienteTipoContactoDto> contactos= new ArrayList<>();
		TrManticClienteTipoContactoDto contacto       = null;
		List<ClienteTipoContacto> contactosList       = null;
		try {
			regresar.setCliente((TcManticClientesDto) this.attrs.get("registroCliente"));
			regresar.setDomicilio(this.domicilio);			
			regresar.setIdClienteDomicilio(this.domicilio.getIdClienteDomicilio());
			contactosList= (List<ClienteTipoContacto>) this.attrs.get("contactos");
			contacto = new TrManticClienteTipoContactoDto();
			contacto.setIdTipoContacto(ETiposContactos.CORREO.getKey());
			contacto.setValor(this.attrs.get("correo").toString());
			contacto.setOrden(1L);
			if(!contactosList.isEmpty()) {
				for(ClienteTipoContacto record: contactosList){
					if(record.getIdTipoContacto().equals(ETiposContactos.CORREO.getKey()))
						contacto.setIdClienteTipoContacto(record.getIdClienteTipoContacto());
				} // for
			} // if
			contactos.add(contacto);
			contacto= new TrManticClienteTipoContactoDto();
			contacto.setIdTipoContacto(ETiposContactos.TELEFONO.getKey());
			contacto.setValor(this.attrs.get("telefono").toString());
			contacto.setOrden(2L);
			if(!contactosList.isEmpty()){
				for(ClienteTipoContacto record: contactosList){
					if(record.getIdTipoContacto().equals(ETiposContactos.TELEFONO.getKey()))
						contacto.setIdClienteTipoContacto(record.getIdClienteTipoContacto());
				} // for
			} // if
			contactos.add(contacto);
			contacto= new TrManticClienteTipoContactoDto();
			contacto.setIdTipoContacto(ETiposContactos.CELULAR.getKey());
			contacto.setValor(this.attrs.get("celular").toString());
			contacto.setOrden(3L);
			if(!contactosList.isEmpty()){
				for(ClienteTipoContacto record: contactosList){
					if(record.getIdTipoContacto().equals(ETiposContactos.CELULAR.getKey()))
						contacto.setIdClienteTipoContacto(record.getIdClienteTipoContacto());
				} // for
			} // if
			contactos.add(contacto);
			regresar.setContacto(contactos);
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toClienteVenta
	
  public String doCancelar() {
		String regresar= "accion".concat(Constantes.REDIRECIONAR);
		if(this.attrs.get("regreso")!= null){
			JsfBase.setFlashAttribute("idFicticia", this.attrs.get("idFicticia"));																							
			JsfBase.setFlashAttribute("idVenta", this.attrs.get("idVenta"));
			JsfBase.setFlashAttribute("idCliente", this.attrs.get("idCliente"));					
			JsfBase.setFlashAttribute("observaciones", this.attrs.get("observaciones"));					
			JsfBase.setFlashAttribute("accion", (this.attrs.get("idFicticia")!= null && !Long.valueOf(this.attrs.get("idFicticia").toString()).equals(-1L)) || this.attrs.get("idVenta")!= null && !Long.valueOf(this.attrs.get("idVenta").toString()).equals(-1L) ? EAccion.MODIFICAR : EAccion.AGREGAR);
			regresar= this.attrs.get("regreso").toString().concat(Constantes.REDIRECIONAR);						
		} // if
    return regresar;
  } // doAccion

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
		List<Columna>columns      = new ArrayList<>();
    Map<String, Object> params= new HashMap<>();
    try {
      params.put("idPais", 1);
      params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
			columns.add(new Columna("descripcion", EFormatoDinamicos.MAYUSCULAS));
      entidades = UIEntity.build("TcJanalEntidadesDto", "comboEntidades", params, columns, Constantes.SQL_TODOS_REGISTROS);
      this.attrs.put("entidades", entidades);
      this.domicilio.setIdEntidad(entidades.get(0));
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    finally {
      Methods.clean(params);
      Methods.clean(columns);
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
    Map<String, Object> params     = new HashMap<>();
		List<Columna>columns           = new ArrayList<>();
    try {
			if(!this.domicilio.getIdEntidad().getKey().equals(-1L)){
				params.put("idEntidad", this.domicilio.getIdEntidad().getKey());
				columns.add(new Columna("descripcion", EFormatoDinamicos.MAYUSCULAS));
				municipios = UIEntity.build("TcJanalMunicipiosDto", "comboMunicipios", params, columns, Constantes.SQL_TODOS_REGISTROS);
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
      Methods.clean(columns);
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
    Map<String, Object> params= new HashMap<>();
		List<Columna>columns      = new ArrayList<>();
    try {
			if(!this.domicilio.getIdMunicipio().getKey().equals(-1L)){
				params.put("idMunicipio", this.domicilio.getIdMunicipio().getKey());
				columns.add(new Columna("descripcion", EFormatoDinamicos.MAYUSCULAS));
				localidades = UIEntity.build("TcJanalLocalidadesDto", "comboLocalidades", params, columns, Constantes.SQL_TODOS_REGISTROS);
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
      Methods.clean(columns);
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
    Map<String, Object> params        = new HashMap<>();
    try {
			if(!this.domicilio.getIdEntidad().getKey().equals(-1L)){
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
		Entity domicilio= null;
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
	
  public void doBusquedaDomicilios() {
    List<UISelectEntity> domicilios= null;
    Map<String, Object> params= new HashMap<>();
		List<Columna>columns      = new ArrayList<>();
    try {
      params.put(Constantes.SQL_CONDICION, "upper(calle) like upper('%".concat(this.attrs.get("calle").toString()).concat("%')"));
			columns.add(new Columna("calle", EFormatoDinamicos.MAYUSCULAS));
			columns.add(new Columna("numeroExterior", EFormatoDinamicos.MAYUSCULAS));
			columns.add(new Columna("numeroInterior", EFormatoDinamicos.MAYUSCULAS));
			columns.add(new Columna("asentamiento", EFormatoDinamicos.MAYUSCULAS));
			columns.add(new Columna("entidad", EFormatoDinamicos.MAYUSCULAS));
			columns.add(new Columna("municipio", EFormatoDinamicos.MAYUSCULAS));
			columns.add(new Columna("domicilio", EFormatoDinamicos.MAYUSCULAS));
      domicilios = UIEntity.build("VistaDomiciliosCatalogosDto", "domicilios", params, columns, Constantes.SQL_TODOS_REGISTROS);
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
      this.loadMunicipios();
      this.loadLocalidades();
      //loadCodigosPostales();      
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // doActualizaMunicipios

  public void doActualizaLocalidades() {
    try {
      this.loadLocalidades();
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
		this.doLoadAtributos(true);
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
					this.domicilio.setNuevoCp(false);
					this.domicilio.setIdCodigoPostal(-1L);
					this.domicilio.setCodigoPostal("");
					this.attrs.put("codigoSeleccionado", new UISelectEntity(-1L));
				} // else					
				this.toAsignaEntidad();
				this.loadMunicipios();
				this.toAsignaMunicipio();
				this.loadLocalidades();
				this.toAsignaLocalidad();
				//loadCodigosPostales();      
				//toAsignaCodigoPostal();
			} // if
      this.loadAtributosComplemento();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
  } // doLoadAtributos

	private void loadAtributosComplemento() throws Exception {
		MotorBusqueda motor        = null;
		TcManticDomiciliosDto item = null;
		TcManticClientesDto cliente= null;
		try {
			cliente= (TcManticClientesDto) this.attrs.get("registroCliente");
			if (!this.domicilio.getIdDomicilio().equals(-1L)) {
        motor = new MotorBusqueda(cliente.getIdCliente());
        item = motor.toDomicilio(this.domicilio.getIdDomicilio());
        this.domicilio.setNumeroExterior(item.getNumeroExterior());
        this.domicilio.setNumeroInterior(item.getNumeroInterior());
        this.domicilio.setCalle(item.getCalle());
        this.domicilio.setAsentamiento(item.getAsentamiento());
        this.domicilio.setEntreCalle(item.getEntreCalle());
        this.domicilio.setYcalle(item.getYcalle());
      } // if
      else 
        this.clearAtributos();
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
		if(this.domicilio.getIdEntidad().getKey()>= 1L && !Cadena.isVacio(query)){
			this.attrs.put("condicionCodigoPostal", query);
			this.doUpdateCodigosPostales();		
			return (List<UISelectEntity>)this.attrs.get("allCodigosPostales");
		} // if
		else{
			this.domicilio.setNuevoCp(false);
			this.domicilio.setIdCodigoPostal(-1L);
			this.domicilio.setCodigoPostal("");
			return new ArrayList<>();
		} // else		
	}	// doCompleteCliente
	
	public void doUpdateCodigosPostales() {
		List<Columna> columns     = new ArrayList<>();
    Map<String, Object> params= new HashMap<>();
    try {
      columns.add(new Columna("codigo", EFormatoDinamicos.MAYUSCULAS));      
  		params.put(Constantes.SQL_CONDICION, "id_entidad=" + this.domicilio.getIdEntidad().getKey() + " and codigo like '" + this.attrs.get("condicionCodigoPostal") + "%'");						  		
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
			this.domicilio.setCodigoPostal(seleccion.toString("codigo"));
      this.domicilio.setNuevoCp(true);
			this.domicilio.setIdCodigoPostal(seleccion.getKey());
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
			if(!codigosPostales.isEmpty()){
				this.domicilio.setCodigoPostal(codigosPostales.get(0).toString("codigo"));
				this.domicilio.setNuevoCp(true);
				this.domicilio.setIdCodigoPostal(codigosPostales.get(0).getKey());
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
			this.domicilio.setIdCodigoPostal(-1L);
			this.domicilio.setCodigoPostal("");
			if((Boolean)this.attrs.get("cpNuevo")){
				this.domicilio.setNuevoCp(true);		
				this.attrs.put("codigoSeleccionado", new UISelectEntity(-1L));
			} // 				
			else
				this.domicilio.setNuevoCp(false);			
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);		
		} // catch		
	} // doInicializaCodigo
  
	private void toLoadTiposClientes() {
		List<UISelectItem> tiposClientes= null;
    Map<String, Object> params      = new HashMap<>();
		try {
      params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
			tiposClientes= UISelect.build("TcManticTiposClientesDto", "row", params, "nombre", EFormatoDinamicos.MAYUSCULAS);
			this.attrs.put("tiposClientes", tiposClientes);
		} // try
		catch (Exception e) {
			throw e;
		} // catch		
    finally {
      Methods.clean(params);
    } // finally
	} 
  
	public void doLoadRegimenesFiscales() {
		List<Columna> columns     = new ArrayList<>();    
    Map<String, Object> params= new HashMap<>();
    List<UISelectEntity> regimenesFiscales= null;
    try {      
      TcManticClientesDto cliente= (TcManticClientesDto)this.attrs.get("registroCliente");
      params.put("idTipoRegimenPersona", "1, 2");                  
      columns.add(new Columna("codigo", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      regimenesFiscales= (List<UISelectEntity>) UIEntity.seleccione("TcManticRegimenesFiscalesDto", "tipo", params, columns, "codigo");
			this.attrs.put("regimenesFiscales", regimenesFiscales);
      if(cliente!= null && regimenesFiscales!= null && !regimenesFiscales.isEmpty()) {
        int index= regimenesFiscales.indexOf(new UISelectEntity(cliente.getIdRegimenFiscal()== null? -1L: cliente.getIdRegimenFiscal()));
        if(index< 0)
          this.setIkRegimenFiscal(regimenesFiscales.get(0));
        else
          this.setIkRegimenFiscal(regimenesFiscales.get(index));
      } // else
      else
        this.setIkRegimenFiscal(new UISelectEntity(-1L));
    } // try
    catch (Exception e) {
			throw e;
    } // catch	
    finally {
      Methods.clean(params);
      Methods.clean(columns);
    } // finally
	} // doLoadRegimenesFiscales  

  public void doUpdateChange() {
    
  } 
  
  public void doCodigoPostal() {
    Map<String, Object> params = new HashMap<>();
    try {      
      params.put("codigo", JsfBase.getParametro("cp")== null? JsfBase.getParametro("codigoPostal_input"): JsfBase.getParametro("cp"));      
      Entity codigo = (Entity)DaoFactory.getInstance().toEntity("TcManticCodigosPostalesDto", "igual", params);
      if(codigo!= null && !codigo.isEmpty()) {
				this.domicilio.setCodigoPostal(codigo.toString("codigo"));
        List<UISelectEntity> entidades= (List<UISelectEntity>)this.attrs.get("entidades");
        int index= entidades.indexOf(new UISelectEntity(codigo.toLong("idEntidad")));
        if(index>= 0) {
  				this.domicilio.setIdEntidad(entidades.get(index));
          this.doActualizaMunicipios();
        } // if  
        else {
          Entity entidad= codigo.clone();
          entidad.setKey(entidad.toLong("idEntidad"));
  				this.domicilio.setIdEntidad(entidad);
        } // if  
				this.domicilio.setIdCodigoPostal(codigo.toLong("idCodigoPostal"));
				this.attrs.put("codigoSeleccionado", new UISelectEntity(codigo));
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
