package mx.org.kaana.mantic.catalogos.clientes.backing;

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
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UISelect;
import mx.org.kaana.libs.pagina.UISelectItem;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.clientes.bean.Domicilio;
import mx.org.kaana.mantic.catalogos.clientes.reglas.Transaccion;
import mx.org.kaana.mantic.catalogos.clientes.bean.RegistroCliente;
import mx.org.kaana.mantic.catalogos.clientes.reglas.MotorBusqueda;
import mx.org.kaana.mantic.db.dto.TcManticDomiciliosDto;
import mx.org.kaana.mantic.enums.ETipoPersona;
import mx.org.kaana.mantic.enums.ETiposContactos;
import mx.org.kaana.mantic.enums.ETiposDomicilios;

@Named(value = "manticCatalogosClientesAccion")
@ViewScoped
public class Accion extends IBaseAttribute implements Serializable {

  private static final long serialVersionUID = -7668104942302148046L;
  private RegistroCliente registroCliente;

  public RegistroCliente getRegistroCliente() {
    return registroCliente;
  }

  public void setRegistroCliente(RegistroCliente registroCliente) {
    this.registroCliente = registroCliente;
  }

  @PostConstruct
  @Override
  protected void init() {
    try {
      this.attrs.put("accion", JsfBase.getFlashAttribute("accion"));
      this.attrs.put("idCliente", JsfBase.getFlashAttribute("idCliente"));
      doLoad();
      loadRepresentantes();
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

  public void doLoad() {
    EAccion eaccion = null;
    Long idCliente = -1L;
    try {
      eaccion = (EAccion) this.attrs.get("accion");
      this.attrs.put("nombreAccion", Cadena.letraCapital(eaccion.name()));
      switch (eaccion) {
        case AGREGAR:
          this.registroCliente = new RegistroCliente();
          break;
        case MODIFICAR:
          idCliente = Long.valueOf(this.attrs.get("idCliente").toString());
          this.registroCliente = new RegistroCliente(idCliente);
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
    return "filtro";
  } // doAccion

  private void loadRepresentantes() {
    List<UISelectItem> representantes = null;
    Map<String, Object> params = null;
    try {
      params = new HashMap<>();
      params.put(Constantes.SQL_CONDICION, "id_tipo_persona=" + ETipoPersona.REPRESENTANTE_LEGAL.getIdTipoPersona());
      representantes = UISelect.build("TcManticPersonasDto", "row", params, "nombre", EFormatoDinamicos.MAYUSCULAS, Constantes.SQL_TODOS_REGISTROS);
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
    List<UISelectItem> entidades = null;
    Map<String, Object> params = null;
    try {
      params = new HashMap<>();
      params.put("idPais", 1);
      params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
      entidades = UISelect.build("TcJanalEntidadesDto", "comboEntidades", params, "descripcion", EFormatoDinamicos.MAYUSCULAS, Constantes.SQL_TODOS_REGISTROS);
      this.attrs.put("entidades", entidades);
      this.registroCliente.getDomicilio().setIdEntidad((Long) UIBackingUtilities.toFirstKeySelectItem(entidades));
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    finally {
      Methods.clean(params);
    } // finally
  } // loadEntidades

  private void loadMunicipios() {
    List<UISelectItem> municipios = null;
    Map<String, Object> params = null;
    try {
      params = new HashMap<>();
      params.put("idEntidad", this.registroCliente.getDomicilio().getIdEntidad());
      municipios = UISelect.build("TcJanalMunicipiosDto", "comboMunicipios", params, "descripcion", EFormatoDinamicos.MAYUSCULAS, Constantes.SQL_TODOS_REGISTROS);
      this.attrs.put("municipios", municipios);
      this.registroCliente.getDomicilio().setIdMunicipio((Long) UIBackingUtilities.toFirstKeySelectItem(municipios));
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    finally {
      Methods.clean(params);
    } // finally
  } // loadMunicipios

  private void loadLocalidades() {
    List<UISelectItem> localidades = null;
    Map<String, Object> params = null;
    try {
      params = new HashMap<>();
      params.put("idMunicipio", this.registroCliente.getDomicilio().getIdMunicipio());
      localidades = UISelect.build("TcJanalLocalidadesDto", "comboLocalidades", params, "descripcion", EFormatoDinamicos.MAYUSCULAS, Constantes.SQL_TODOS_REGISTROS);
      this.attrs.put("localidades", localidades);
      this.registroCliente.getDomicilio().setIdLocalidad((Long) UIBackingUtilities.toFirstKeySelectItem(localidades));
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
      params.put("idLocalidad", this.registroCliente.getDomicilio().getIdLocalidad());
      codigosPostales = UISelect.build("TcManticDomiciliosDto", "codigosPostales", params, "codigoPostal", EFormatoDinamicos.MAYUSCULAS, Constantes.SQL_TODOS_REGISTROS);
      this.attrs.put("codigosPostales", codigosPostales);
      if (!codigosPostales.isEmpty()) {
        this.registroCliente.getDomicilio().setCodigoPostal(codigosPostales.get(0).getLabel());
        this.registroCliente.getDomicilio().setIdCodigoPostal((Long) codigosPostales.get(0).getValue());
        this.registroCliente.getDomicilio().setNuevoCp(false);
      } // if
      else {
        this.registroCliente.getDomicilio().setNuevoCp(true);
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
    List<UISelectItem> domicilios = null;
    Map<String, Object> params = null;
    try {
      params = new HashMap<>();
      params.put("idLocalidad", this.registroCliente.getDomicilio().getIdLocalidad());
      params.put("codigoPostal", this.registroCliente.getDomicilio().getCodigoPostal());
      domicilios = UISelect.build("TcManticDomiciliosDto", "domicilios", params, "domicilio", EFormatoDinamicos.MAYUSCULAS, Constantes.SQL_TODOS_REGISTROS);
      this.attrs.put("domicilios", domicilios);
      if (!domicilios.isEmpty()) {
        this.registroCliente.getDomicilio().setIdDomicilio((Long) domicilios.get(0).getValue());
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
      if (this.registroCliente.getDomicilio().getIdCodigoPostal().equals(-1L)) {
        this.registroCliente.getDomicilio().setCodigoPostal("");
        this.registroCliente.getDomicilio().setNuevoCp(true);
      } // if
      else {
        codigosPostales = (List<UISelectItem>) this.attrs.get("codigosPostales");
        for (UISelectItem codigo : codigosPostales) {
          if (codigo.getValue().equals(this.registroCliente.getDomicilio().getIdCodigoPostal())) {
            this.registroCliente.getDomicilio().setCodigoPostal(codigo.getLabel());
            this.registroCliente.getDomicilio().setNuevoCp(false);
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
      }
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
  } // doLoadAtributos

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
      this.registroCliente.doConsultarClienteDomicilio();
      motor = new MotorBusqueda(this.registroCliente.getIdCliente());
      domicilio = this.registroCliente.getDomicilioPivote();
      dtoDomicilio = motor.toDomicilio(domicilio.getIdDomicilio());
      loadEntidades();
      this.registroCliente.getDomicilio().setIdEntidad(domicilio.getIdEntidad());
      loadMunicipios();
      this.registroCliente.getDomicilio().setIdMunicipio(domicilio.getIdMunicipio());
      loadLocalidades();
      this.registroCliente.getDomicilio().setIdLocalidad(domicilio.getIdLocalidad());
      loadCodigosPostales();
      codigos = (List<UISelectItem>) this.attrs.get("codigosPostales");
      for (UISelectItem codigo : codigos) {
        if (codigo.getLabel().equals(dtoDomicilio.getCodigoPostal())) {
          this.registroCliente.getDomicilio().setIdCodigoPostal((Long) codigo.getValue());
          this.registroCliente.getDomicilio().setCodigoPostal(codigo.getLabel());
        } // if
      }	// for
      loadDomicilios();
      this.registroCliente.getDomicilio().setIdDomicilio(domicilio.getIdDomicilio());
      this.registroCliente.getDomicilio().setCalle(dtoDomicilio.getCalle());
      this.registroCliente.getDomicilio().setNumeroExterior(dtoDomicilio.getNumeroExterior());
      this.registroCliente.getDomicilio().setNumeroInterior(dtoDomicilio.getNumeroInterior());
      this.registroCliente.getDomicilio().setAsentamiento(dtoDomicilio.getAsentamiento());
      this.registroCliente.getDomicilio().setEntreCalle(dtoDomicilio.getEntreCalle());
      this.registroCliente.getDomicilio().setYcalle(dtoDomicilio.getYcalle());
      this.registroCliente.getDomicilio().setIdTipoDomicilio(domicilio.getIdTipoDomicilio());
      this.registroCliente.getDomicilio().setPrincipal(domicilio.getPrincipal());
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // doEliminarArticuloCodigo	
}
