package mx.org.kaana.mantic.catalogos.personas.backing;

import java.io.Serializable;
import java.util.ArrayList;
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
import mx.org.kaana.kajool.procesos.acceso.beans.Sucursal;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelect;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.pagina.UISelectItem;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.clientes.bean.Domicilio;
import mx.org.kaana.mantic.catalogos.personas.beans.RegistroPersona;
import mx.org.kaana.mantic.catalogos.personas.reglas.MotorBusqueda;
import mx.org.kaana.mantic.catalogos.personas.reglas.Transaccion;
import mx.org.kaana.mantic.db.dto.TcManticDomiciliosDto;
import mx.org.kaana.mantic.enums.ETipoPersona;
import mx.org.kaana.mantic.enums.ETiposContactos;
import mx.org.kaana.mantic.enums.ETiposDomicilios;


@Named(value = "manticCatalogosPersonasAccion")
@ViewScoped
public class Accion extends IBaseAttribute implements Serializable {

  private static final long serialVersionUID = 327393488565639367L;
	private static final String TEMA   = "sentinel";
	private static final String GERENTE= "GERENTE";
	private RegistroPersona registroPersona;

	public RegistroPersona getRegistroPersona() {
		return registroPersona;
	}

	public void setRegistroPersona(RegistroPersona registroPersona) {
		this.registroPersona = registroPersona;
	}		
	
  @PostConstruct
  @Override
  protected void init() {		
    try {
      this.attrs.put("accion", JsfBase.getFlashAttribute("accion"));
      this.attrs.put("tipoPersona", JsfBase.getFlashAttribute("tipoPersona"));
      this.attrs.put("idPersona", JsfBase.getFlashAttribute("idPersona"));
			this.attrs.put("retorno", JsfBase.getFlashAttribute("retorno"));
			this.attrs.put("general", this.attrs.get("tipoPersona")== null);
			this.attrs.put("mostrarEmpresas", JsfBase.isAdminEncuestaOrAdmin() || JsfBase.getAutentifica().getPersona().getDescripcionPerfil().equals(GERENTE));
			if(Boolean.valueOf(this.attrs.get("mostrarEmpresas").toString()))
				loadEmpresas();		
			this.attrs.put("mostrarPuestos", Long.valueOf(this.attrs.get("tipoPersona").toString()).equals(ETipoPersona.EMPLEADO.getIdTipoPersona()));			
			for(ETipoPersona tipoPersona: ETipoPersona.values()){
				if(tipoPersona.getIdTipoPersona().equals(Long.valueOf(this.attrs.get("tipoPersona").toString())))
					this.attrs.put("catalogo", Cadena.reemplazarCaracter(tipoPersona.name().toLowerCase(), '_' , ' '));
			} // for
			loadPuestos();
			doLoad();
			loadTitulos();
			loadTiposPersonas();   
			loadTiposContactos();
      loadTiposDomicilios();
      loadEntidades();
      loadMunicipios();
      loadLocalidades();
      loadCodigosPostales();
      loadDomicilios();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

	private void loadEmpresas(){
		List<UISelectItem> sucursales= null;
		try {
			sucursales= new ArrayList<>();
			for(Sucursal sucursal: JsfBase.getAutentifica().getSucursales())
				sucursales.add(new UISelectItem(sucursal.getIdEmpresa(), sucursal.getNombre()));
			this.attrs.put("empresas", sucursales);
		} // try
		catch (Exception e) {			
			throw e;
		} // catch			
	} // loadEncuestas
	
	private void loadPuestos(){
		List<UISelectItem> puestos= null;
    Map<String, Object> params= null;
    try {
      params = new HashMap<>();
      params.put(Constantes.SQL_CONDICION, "id_empresa=" + JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
      puestos = UISelect.build("TcManticPuestosDto", "row", params, "nombre", EFormatoDinamicos.MAYUSCULAS, Constantes.SQL_TODOS_REGISTROS);
			if(!puestos.isEmpty()){
				this.attrs.put("puestos", puestos);
				this.attrs.put("idPuesto", UIBackingUtilities.toFirstKeySelectItem(puestos));
			} // if
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    finally {
      Methods.clean(params);
    } // finally
	} // loadPuestos
	
  public void doLoad() {
    EAccion eaccion= null;
    Long idPersona = -1L;
    try {
      eaccion= (EAccion) this.attrs.get("accion");
      this.attrs.put("nombreAccion", Cadena.letraCapital(eaccion.name()));
      switch (eaccion) {
        case AGREGAR:											
          this.registroPersona= new RegistroPersona();
					if(this.attrs.get("tipoPersona")!= null)
						this.registroPersona.getPersona().setIdTipoPersona(Long.valueOf(this.attrs.get("tipoPersona").toString()));
          break;
        case MODIFICAR:					
          idPersona= Long.valueOf(this.attrs.get("idPersona").toString());
          this.registroPersona= new RegistroPersona(idPersona);
          break;
      } // switch
			this.registroPersona.getPersona().setEstilo(TEMA);
			this.registroPersona.getPersona().setIdUsuario(JsfBase.getIdUsuario());
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // doLoad

  public String doAceptar() {  
    Transaccion transaccion= null;
    String regresar        = null;
		EAccion eaccion        = null;
		Long idEmpresa         = -1L;
    try {			
			idEmpresa= Boolean.valueOf(this.attrs.get("mostrarEmpresas").toString()) ? Long.valueOf(this.attrs.get("idEmpresa").toString()) : JsfBase.getAutentifica().getEmpresa().getIdEmpresa();
			eaccion= (EAccion) this.attrs.get("accion");
			transaccion = new Transaccion(this.registroPersona, idEmpresa, Long.valueOf(this.attrs.get("idPuesto").toString()));
			if (transaccion.ejecutar(eaccion)) {
				regresar = this.attrs.get("retorno").toString().concat(Constantes.REDIRECIONAR);
				JsfBase.addMessage("Se ".concat(eaccion.equals(EAccion.AGREGAR) ? "agregó" : "modifico").concat(" la persona de forma correcta. \nCuenta de acceso [").concat(transaccion.getCuenta()).concat("]"), ETipoMensaje.INFORMACION);
			} // if
			else 
				JsfBase.addMessage("Ocurrió un error al registrar la persona.", ETipoMensaje.ERROR);      			
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    return regresar;
  } // doAccion

  public String doCancelar() {   
    return this.attrs.get("retorno").toString();
  } // doAccion
	
	private void loadTiposPersonas(){
		List<UISelectItem> tiposPersonas= null;
		try {
			tiposPersonas= new ArrayList<>();
			for(ETipoPersona tipoPersona: ETipoPersona.values())
				tiposPersonas.add(new UISelectItem(tipoPersona.getIdTipoPersona(), Cadena.reemplazarCaracter(tipoPersona.name(), '_', ' ')));
			this.attrs.put("tiposPersonas", tiposPersonas);
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
	} // loadTiposPersonas
	
	private void loadTitulos() {
    List<UISelectItem> titulos= null;
    Map<String, Object> params= null;
    EAccion eaccion           = null;
    try {
      params = new HashMap<>();
      params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
      titulos = UISelect.build("TcManticPersonasTitulosDto", "row", params, "nombre", EFormatoDinamicos.MAYUSCULAS, Constantes.SQL_TODOS_REGISTROS);
      this.attrs.put("titulos", titulos);
      eaccion = (EAccion) this.attrs.get("accion");
      if (eaccion.equals(EAccion.AGREGAR)) 
        this.registroPersona.getPersona().setIdPersonaTitulo((Long) UIBackingUtilities.toFirstKeySelectItem(titulos));
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    finally {
      Methods.clean(params);
    } // finally
  } // loadTitulos
	
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
      this.registroPersona.getDomicilio().setIdEntidad(entidades.get(0));
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    finally {
      Methods.clean(params);
    } // finally
  } // loadEntidades

  private void loadMunicipios() {
    List<UISelectEntity> municipios = null;
    Map<String, Object> params = null;
		List<Columna>campos = null;
    try {
      params = new HashMap<>();
      params.put("idEntidad", this.registroPersona.getDomicilio().getIdEntidad().getKey());
			campos= new ArrayList<>();
			campos.add(new Columna("descripcion", EFormatoDinamicos.MAYUSCULAS));
      municipios = UIEntity.build("TcJanalMunicipiosDto", "comboMunicipios", params, campos, Constantes.SQL_TODOS_REGISTROS);
      this.attrs.put("municipios", municipios);
      this.registroPersona.getDomicilio().setIdMunicipio(municipios.get(0));
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    finally {
      Methods.clean(params);
    } // finally
  } // loadMunicipios

  private void loadLocalidades() {
    List<UISelectEntity> localidades = null;
    Map<String, Object> params = null;
		List<Columna>campos = null;
    try {
      params = new HashMap<>();
      params.put("idMunicipio", this.registroPersona.getDomicilio().getIdMunicipio().getKey());
			campos= new ArrayList<>();
			campos.add(new Columna("descripcion", EFormatoDinamicos.MAYUSCULAS));
      localidades = UIEntity.build("TcJanalLocalidadesDto", "comboLocalidades", params, campos, Constantes.SQL_TODOS_REGISTROS);
      this.attrs.put("localidades", localidades);
      this.registroPersona.getDomicilio().setLocalidad(localidades.get(0));
      this.registroPersona.getDomicilio().setIdLocalidad(localidades.get(0).getKey());
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    finally {
      Methods.clean(params);
    } // finally
  } // loadLocalidades

  private void loadCodigosPostales() {
    List<UISelectItem> codigosPostales = null;
    Map<String, Object> params = null;
    try {
      params = new HashMap<>();
      params.put(Constantes.SQL_CONDICION, "id_entidad=" + this.registroPersona.getDomicilio().getIdEntidad().getKey());
      codigosPostales = UISelect.build("TcManticCodigosPostalesDto", "row", params, "codigo", EFormatoDinamicos.MAYUSCULAS, Constantes.SQL_TODOS_REGISTROS);
      this.attrs.put("codigosPostales", codigosPostales);
      if (!codigosPostales.isEmpty()) {
        this.registroPersona.getDomicilio().setCodigoPostal(codigosPostales.get(0).getLabel());
        this.registroPersona.getDomicilio().setIdCodigoPostal((Long) codigosPostales.get(0).getValue());
        this.registroPersona.getDomicilio().setNuevoCp(false);
      } // if
      else {
        this.registroPersona.getDomicilio().setNuevoCp(true);
      }
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    finally {
      Methods.clean(params);
    } // finally
  } // loadCodigosPostales

  public void doLoadDomicilios() {
    try {
      updateCodigoPostal();
      loadDomicilios();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // doLoadDomicilios

  private void loadDomicilios() {
    List<UISelectEntity> domicilios= null;
    Map<String, Object> params= null;
		List<Columna>campos= null;
    try {
      params = new HashMap<>();
      params.put("idLocalidad", this.registroPersona.getDomicilio().getLocalidad().getKey());
      params.put("codigoPostal", this.registroPersona.getDomicilio().getCodigoPostal());
			campos= new ArrayList<>();
			campos.add(new Columna("calle", EFormatoDinamicos.MAYUSCULAS));
			campos.add(new Columna("numeroExterior", EFormatoDinamicos.MAYUSCULAS));
			campos.add(new Columna("numeroInterior", EFormatoDinamicos.MAYUSCULAS));
			campos.add(new Columna("asentamiento", EFormatoDinamicos.MAYUSCULAS));
      domicilios = UIEntity.build("TcManticDomiciliosDto", "domicilios", params, campos, Constantes.SQL_TODOS_REGISTROS);
      this.attrs.put("domicilios", domicilios);
      if (!domicilios.isEmpty()) {
        this.registroPersona.getDomicilio().setDomicilio(domicilios.get(0));
        this.registroPersona.getDomicilio().setIdDomicilio(domicilios.get(0).getKey());
        doLoadAtributos();
      } // if			
      else {
        clearAtributos();
      }
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    finally {
      Methods.clean(params);
    } // finally
  } // doLoadDomicilios

  private void updateCodigoPostal() {
    List<UISelectItem> codigosPostales = null;
    try {
      if (this.registroPersona.getDomicilio().getIdCodigoPostal().equals(-1L)) {
        this.registroPersona.getDomicilio().setCodigoPostal("");
        this.registroPersona.getDomicilio().setNuevoCp(true);
      } // if
      else {
        codigosPostales = (List<UISelectItem>) this.attrs.get("codigosPostales");
        for (UISelectItem codigo : codigosPostales) {
          if (codigo.getValue().equals(this.registroPersona.getDomicilio().getIdCodigoPostal())) {
            this.registroPersona.getDomicilio().setCodigoPostal(codigo.getLabel());
            this.registroPersona.getDomicilio().setNuevoCp(false);
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
      loadDomicilios();
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
      loadDomicilios();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // doActualizaMunicipios

  public void doActualizaCodigosPostales() {
    try {
      loadCodigosPostales();
      loadDomicilios();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // doActualizaMunicipios

  public void doLoadAtributos() {
    TcManticDomiciliosDto domicilio = null;
    MotorBusqueda motor = null;
    try {
      if (!this.registroPersona.getDomicilio().getIdDomicilio().equals(-1L)) {
        motor = new MotorBusqueda(this.registroPersona.getIdPersona());
        domicilio = motor.toDomicilio(this.registroPersona.getDomicilio().getIdDomicilio());
        this.registroPersona.getDomicilio().setNumeroExterior(domicilio.getNumeroExterior());
        this.registroPersona.getDomicilio().setNumeroInterior(domicilio.getNumeroInterior());
        this.registroPersona.getDomicilio().setCalle(domicilio.getCalle());
        this.registroPersona.getDomicilio().setAsentamiento(domicilio.getAsentamiento());
        this.registroPersona.getDomicilio().setEntreCalle(domicilio.getEntreCalle());
        this.registroPersona.getDomicilio().setYcalle(domicilio.getYcalle());
      } // if
      else {
        clearAtributos();
      }
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
  } // doLoadAtributos

  public void clearAtributos() {
    try {
      this.registroPersona.getDomicilio().setNumeroExterior("");
      this.registroPersona.getDomicilio().setNumeroInterior("");
      this.registroPersona.getDomicilio().setCalle("");
      this.registroPersona.getDomicilio().setAsentamiento("");
      this.registroPersona.getDomicilio().setEntreCalle("");
      this.registroPersona.getDomicilio().setYcalle("");
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
  } // doLoadAtributos

  public void doAgregarCliente() {
    try {
      this.registroPersona.doAgregarClienteDomicilio();
      this.registroPersona.setDomicilio(new Domicilio());
      loadEntidades();
      doActualizaMunicipios();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // doAgregarCliente

  public void doConsultarClienteDomicilio() {
    MotorBusqueda motor = null;
    Domicilio domicilio = null;
    TcManticDomiciliosDto dtoDomicilio = null;
    List<UISelectItem> codigos = null;
    try {
      this.registroPersona.doConsultarClienteDomicilio();
      motor = new MotorBusqueda(this.registroPersona.getIdPersona());
      domicilio = this.registroPersona.getDomicilioPivote();
      dtoDomicilio = motor.toDomicilio(domicilio.getIdDomicilio());
      loadEntidades();
      this.registroPersona.getDomicilio().setIdEntidad(domicilio.getIdEntidad());
      loadMunicipios();
      this.registroPersona.getDomicilio().setIdMunicipio(domicilio.getIdMunicipio());
      loadLocalidades();
			this.registroPersona.getDomicilio().setLocalidad(domicilio.getLocalidad());
      this.registroPersona.getDomicilio().setIdLocalidad(domicilio.getIdLocalidad());
      loadCodigosPostales();
      codigos = (List<UISelectItem>) this.attrs.get("codigosPostales");
      for (UISelectItem codigo : codigos) {
        if (codigo.getLabel().equals(dtoDomicilio.getCodigoPostal())) {
          this.registroPersona.getDomicilio().setIdCodigoPostal((Long) codigo.getValue());
          this.registroPersona.getDomicilio().setCodigoPostal(codigo.getLabel());
					this.registroPersona.getDomicilio().setNuevoCp(true);
        } // if
      }	// for
      loadDomicilios();
      this.registroPersona.getDomicilio().setIdDomicilio(domicilio.getIdDomicilio());
      this.registroPersona.getDomicilio().setCalle(dtoDomicilio.getCalle());
      this.registroPersona.getDomicilio().setNumeroExterior(dtoDomicilio.getNumeroExterior());
      this.registroPersona.getDomicilio().setNumeroInterior(dtoDomicilio.getNumeroInterior());
      this.registroPersona.getDomicilio().setAsentamiento(dtoDomicilio.getAsentamiento());
      this.registroPersona.getDomicilio().setEntreCalle(dtoDomicilio.getEntreCalle());
      this.registroPersona.getDomicilio().setYcalle(dtoDomicilio.getYcalle());
      this.registroPersona.getDomicilio().setIdTipoDomicilio(domicilio.getIdTipoDomicilio());
      this.registroPersona.getDomicilio().setPrincipal(domicilio.getPrincipal());
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // doEliminarArticuloCodigo	
	
	public void doActualizaDomicilio(){
		try {
			this.registroPersona.doActualizarClienteDomicilio();
			this.registroPersona.setDomicilio(new Domicilio());
      loadEntidades();
      doActualizaMunicipios();
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
	} // doActualizaDomicilio
	
	public void doEliminarDomicilio(){
		try {
			this.registroPersona.doEliminarClienteDomicilio();;
			this.registroPersona.setDomicilio(new Domicilio());
      loadEntidades();
      doActualizaMunicipios();
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
	} // doActualizaDomicilio
}