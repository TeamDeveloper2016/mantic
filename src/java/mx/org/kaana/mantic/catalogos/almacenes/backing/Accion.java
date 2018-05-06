package mx.org.kaana.mantic.catalogos.almacenes.backing;

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
import mx.org.kaana.mantic.catalogos.almacenes.bean.RegistroAlmacen;
import mx.org.kaana.mantic.catalogos.almacenes.reglas.MotorBusqueda;
import mx.org.kaana.mantic.catalogos.almacenes.reglas.Transaccion;
import mx.org.kaana.mantic.catalogos.clientes.bean.Domicilio;
import mx.org.kaana.mantic.db.dto.TcManticDomiciliosDto;
import mx.org.kaana.mantic.enums.ETipoPersona;
import mx.org.kaana.mantic.enums.ETiposContactos;
import mx.org.kaana.mantic.enums.ETiposDomicilios;


@Named(value = "manticCatalogosAlamacenesAccion")
@ViewScoped
public class Accion extends IBaseAttribute implements Serializable {

  private static final long serialVersionUID = 327393488565639367L;  
	private RegistroAlmacen registroAlmacen;

	public RegistroAlmacen getRegistroAlmacen() {
		return registroAlmacen;
	}

	public void setRegistroAlmacen(RegistroAlmacen registroAlmacen) {
		this.registroAlmacen = registroAlmacen;
	}		

  @PostConstruct
  @Override
  protected void init() {
    try {
      this.attrs.put("accion", JsfBase.getFlashAttribute("accion"));
      this.attrs.put("idAlmacen", JsfBase.getFlashAttribute("idAlmacen"));
      doLoad();
      loadTiposContactos();
      loadTiposDomicilios();
      loadEntidades();
      loadMunicipios();
      loadLocalidades();
      loadCodigosPostales();
      loadDomicilios();
			loadResponsables();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

  public void doLoad() {
    EAccion eaccion= null;
    Long idAlmacen = -1L;
    try {
      eaccion = (EAccion) this.attrs.get("accion");
      this.attrs.put("nombreAccion", Cadena.letraCapital(eaccion.name()));
      switch (eaccion) {
        case AGREGAR:
          this.registroAlmacen = new RegistroAlmacen();
          break;
        case MODIFICAR:
          idAlmacen = Long.valueOf(this.attrs.get("idAlmacen").toString());
          this.registroAlmacen = new RegistroAlmacen(idAlmacen);
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
      transaccion = new Transaccion(this.registroAlmacen);
      if (transaccion.ejecutar((EAccion) this.attrs.get("accion"))) {
        regresar = "filtro".concat(Constantes.REDIRECIONAR);
        JsfBase.addMessage("Se registro el almacen de forma correcta.", ETipoMensaje.INFORMACION);
      } // if
      else 
        JsfBase.addMessage("Ocurrió un error al registrar el almacen", ETipoMensaje.ERROR);      
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

  private void loadMunicipios() {
    List<UISelectEntity> municipios = null;
    Map<String, Object> params = null;
		List<Columna>campos = null;
    try {
      params = new HashMap<>();
      params.put("idEntidad", this.registroAlmacen.getDomicilio().getIdEntidad());
			campos= new ArrayList<>();
			campos.add(new Columna("descripcion", EFormatoDinamicos.MAYUSCULAS));
      municipios = UIEntity.build("TcJanalMunicipiosDto", "comboMunicipios", params, campos, Constantes.SQL_TODOS_REGISTROS);
      this.attrs.put("municipios", municipios);
      this.registroAlmacen.getDomicilio().setIdMunicipio(municipios.get(0));
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
		List<Columna>campos= null;
    try {
      params = new HashMap<>();
      params.put("idMunicipio", this.registroAlmacen.getDomicilio().getIdMunicipio());
			campos= new ArrayList<>();
			campos.add(new Columna("descripcion", EFormatoDinamicos.MAYUSCULAS));
      localidades = UIEntity.build("TcJanalLocalidadesDto", "comboLocalidades", params, campos, Constantes.SQL_TODOS_REGISTROS);
      this.attrs.put("localidades", localidades);
			this.registroAlmacen.getDomicilio().setLocalidad(localidades.get(0));
      this.registroAlmacen.getDomicilio().setIdLocalidad(localidades.get(0).getKey());
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
      params.put(Constantes.SQL_CONDICION, "id_entidad=" + this.registroAlmacen.getDomicilio().getIdEntidad().getKey());
      codigosPostales = UISelect.build("TcManticCodigosPostalesDto", "row", params, "codigo", EFormatoDinamicos.MAYUSCULAS, Constantes.SQL_TODOS_REGISTROS);
      this.attrs.put("codigosPostales", codigosPostales);
      if (!codigosPostales.isEmpty()) {
        this.registroAlmacen.getDomicilio().setCodigoPostal(codigosPostales.get(0).getLabel());
        this.registroAlmacen.getDomicilio().setIdCodigoPostal((Long) codigosPostales.get(0).getValue());
        this.registroAlmacen.getDomicilio().setNuevoCp(false);
      } // if
      else 
        this.registroAlmacen.getDomicilio().setNuevoCp(true);      
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
    List<UISelectEntity> domicilios = null;
    Map<String, Object> params = null;
		List<Columna>campos= null;
    try {
      params = new HashMap<>();
      params.put("idLocalidad", this.registroAlmacen.getDomicilio().getLocalidad().getKey());
      params.put("codigoPostal", this.registroAlmacen.getDomicilio().getCodigoPostal());
      campos= new ArrayList<>();
			campos.add(new Columna("calle", EFormatoDinamicos.MAYUSCULAS));
			campos.add(new Columna("numeroExterior", EFormatoDinamicos.MAYUSCULAS));
			campos.add(new Columna("numeroInterior", EFormatoDinamicos.MAYUSCULAS));
			campos.add(new Columna("asentamiento", EFormatoDinamicos.MAYUSCULAS));
			domicilios = UIEntity.build("TcManticDomiciliosDto", "domicilios", params, campos, Constantes.SQL_TODOS_REGISTROS);
      this.attrs.put("domicilios", domicilios);
      if (!domicilios.isEmpty()) {
        this.registroAlmacen.getDomicilio().setDomicilio(domicilios.get(0));
        this.registroAlmacen.getDomicilio().setIdDomicilio(domicilios.get(0).getKey());
        doLoadAtributos();
      } // if			
      else 
        clearAtributos();      
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
      }
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
  } // doLoadAtributos

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
      loadEntidades();
      doActualizaMunicipios();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // doAgregarAlmacen

  public void doConsultarAlmacenDomicilio() {
    MotorBusqueda motor = null;
    Domicilio domicilio = null;
    TcManticDomiciliosDto dtoDomicilio = null;
    List<UISelectItem> codigos = null;
    try {
      this.registroAlmacen.doConsultarAlmacenDomicilio();
      motor = new MotorBusqueda(this.registroAlmacen.getIdAlmacen());
      domicilio = this.registroAlmacen.getDomicilioPivote();
      dtoDomicilio = motor.toDomicilio(domicilio.getIdDomicilio());
      loadEntidades();
      this.registroAlmacen.getDomicilio().setIdEntidad(domicilio.getIdEntidad());
      loadMunicipios();
      this.registroAlmacen.getDomicilio().setIdMunicipio(domicilio.getIdMunicipio());
      loadLocalidades();
			this.registroAlmacen.getDomicilio().setLocalidad(domicilio.getLocalidad());
      this.registroAlmacen.getDomicilio().setIdLocalidad(domicilio.getIdLocalidad());
      loadCodigosPostales();
      codigos = (List<UISelectItem>) this.attrs.get("codigosPostales");
      for (UISelectItem codigo : codigos) {
        if (codigo.getLabel().equals(dtoDomicilio.getCodigoPostal())) {
          this.registroAlmacen.getDomicilio().setIdCodigoPostal((Long) codigo.getValue());
          this.registroAlmacen.getDomicilio().setCodigoPostal(codigo.getLabel());
					this.registroAlmacen.getDomicilio().setNuevoCp(true);
        } // if
      }	// for
      loadDomicilios();
      this.registroAlmacen.getDomicilio().setIdDomicilio(domicilio.getIdDomicilio());
      this.registroAlmacen.getDomicilio().setCalle(dtoDomicilio.getCalle());
      this.registroAlmacen.getDomicilio().setNumeroExterior(dtoDomicilio.getNumeroExterior());
      this.registroAlmacen.getDomicilio().setNumeroInterior(dtoDomicilio.getNumeroInterior());
      this.registroAlmacen.getDomicilio().setAsentamiento(dtoDomicilio.getAsentamiento());
      this.registroAlmacen.getDomicilio().setEntreCalle(dtoDomicilio.getEntreCalle());
      this.registroAlmacen.getDomicilio().setYcalle(dtoDomicilio.getYcalle());
      this.registroAlmacen.getDomicilio().setIdTipoDomicilio(domicilio.getIdTipoDomicilio());
      this.registroAlmacen.getDomicilio().setPrincipal(domicilio.getPrincipal());
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
			this.registroAlmacen.doEliminarAlmacenDomicilio();
			this.registroAlmacen.setDomicilio(new Domicilio());
      loadEntidades();
      doActualizaMunicipios();
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
}