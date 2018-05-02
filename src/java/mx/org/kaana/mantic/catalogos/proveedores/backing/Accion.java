package mx.org.kaana.mantic.catalogos.proveedores.backing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ESql;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIMessage;
import mx.org.kaana.libs.pagina.UISelect;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.pagina.UISelectItem;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.proveedores.beans.Agente;
import mx.org.kaana.mantic.catalogos.proveedores.beans.CondicionPago;
import mx.org.kaana.mantic.catalogos.proveedores.beans.Contacto;
import mx.org.kaana.mantic.catalogos.proveedores.beans.Domicilio;
import mx.org.kaana.mantic.catalogos.proveedores.beans.RenglonProveedor;
import mx.org.kaana.mantic.catalogos.proveedores.beans.Responsable;
import mx.org.kaana.mantic.catalogos.proveedores.reglas.Gestor;
import mx.org.kaana.mantic.catalogos.proveedores.reglas.Transaccion;
import mx.org.kaana.mantic.db.dto.TrManticProveedorDomicilioDto;
import mx.org.kaana.mantic.enums.ETipoPersona;

@Named(value = "manticCatalogosProveedoresAccion")
@ViewScoped
public class Accion extends IBaseAttribute implements Serializable {

  private static final long serialVersionUID = 327393488565639367L;
  private RenglonProveedor renglonProveedor;
  private Domicilio domicilio;

  public RenglonProveedor getRenglonProveedor() {
    return renglonProveedor;
  }

  public Domicilio getDomicilio() {
    return domicilio;
  }

  @PostConstruct
  @Override
  public void init() {
    Gestor gestor = null;
    Map params = new HashMap();
    try {
      this.attrs.put("nombreAccion", JsfBase.getFlashAttribute("nombreAccion"));
      renglonProveedor = new RenglonProveedor(JsfBase.getFlashAttribute("idProveedor") == null ? -1L : (Long) JsfBase.getFlashAttribute("idProveedor"));
      this.attrs.put("idTemporal", -1L);
      this.attrs.put("idDomicilioPrincipal", -1L);
      this.attrs.put("confirmar", false);
      if (renglonProveedor.getDomicilios() != null && renglonProveedor.getDomicilios().size() > 0) {
        params.put(Constantes.SQL_CONDICION, "id_proveedor=".concat(renglonProveedor.getTcManticProveedoresDto().getIdProveedor().toString()).concat(" and id_principal=1"));
        TrManticProveedorDomicilioDto proveedorDom = (TrManticProveedorDomicilioDto) DaoFactory.getInstance().findFirst(TrManticProveedorDomicilioDto.class, "row", params);
        if (proveedorDom != null) {
          this.attrs.put("idDomicilioPrincipal", proveedorDom.getIdProveedorDomicilio());
        }
      }
      renglonProveedor.getTcManticProveedoresDto().setIdEmpresa(JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
      renglonProveedor.getTcManticProveedoresDto().setIdUsuario(JsfBase.getIdUsuario());
      this.attrs.put("accion", JsfBase.getFlashAttribute("accion"));
      gestor = new Gestor();
      this.domicilio = new Domicilio(ESql.SELECT);
      gestor.loadEntidades(true);
      this.attrs.put("entidades", gestor.getEntidades());
      if (!this.renglonProveedor.getTcManticProveedoresDto().isValid()) {
        this.domicilio.setEntidad((UISelectEntity) UIBackingUtilities.toFirstKeySelectEntity(gestor.getEntidades()));
      }
      loadCondicionesPago();
      loadPersonas();
      loadContactos();
      loadAgentes();
      loadTiposProveedores();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

  private void loadTiposProveedores() throws Exception {
    this.renglonProveedor.loadTiposProveedores();
  }

  private void loadCondicionesPago() throws Exception {
    loadTipoPago();
    this.renglonProveedor.loadCondicionesPago();
  }

  private void loadPersonas() throws Exception {
    Map params = new HashMap();
    try {
      params.put(Constantes.SQL_CONDICION, "id_Tipo_Persona=".concat(ETipoPersona.RESPONSABLE.getIdTipoPersona().toString()));
      this.attrs.put("personas", UISelect.build("TcManticPersonasDto", "row", params, "nombres|paterno|materno", " ", EFormatoDinamicos.MAYUSCULAS));
    } // try
    catch (Exception e) {
      throw e;
    } // catch   
  }

  private void loadAgentes() throws Exception {
    Map params = new HashMap();
    try {
      params.put(Constantes.SQL_CONDICION, "id_tipo_persona=".concat(ETipoPersona.AGENTE_VENTAS.getIdTipoPersona().toString()));
      this.attrs.put("agentes", UISelect.build("TcManticPersonasDto", "row", params, "nombres|paterno|materno", " ", EFormatoDinamicos.MAYUSCULAS));
    } // try
    catch (Exception e) {
      throw e;
    } // catch   
  }

  private void loadContactos() throws Exception {
    Map params = new HashMap();
    try {
      params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
      this.attrs.put("tiposContactos", (List<UISelectItem>) UISelect.build("TcManticTiposContactosDto", "row", params, "nombre", " ", EFormatoDinamicos.MAYUSCULAS));
    } // try
    catch (Exception e) {
      throw e;
    } // catch   
    finally {
      Methods.clean(params);
    }// finally
  }

  private void loadTipoPago() throws Exception {
    Gestor gestor = new Gestor();
    try {
      this.attrs.put("tiposPago", gestor.toTiposPagos());
    } // try
    catch (Exception e) {
      throw e;
    }// catch
  }

  public void doLoadMunicipios() {
    Gestor gestor = null;
    try {
      gestor = new Gestor();
      gestor.loadMunicipios(this.domicilio.getEntidad().getKey());
      this.attrs.put("municipios", gestor.getMunicipios());
      this.domicilio.setMunicipio((UISelectEntity) UIBackingUtilities.toFirstKeySelectEntity(gestor.getMunicipios()));
      doLoadLocalidades();
      doLoadLocalidadesCalle();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
  }

  public void doLoadLocalidades() {
    Gestor gestor = null;
    try {
      gestor = new Gestor();
      gestor.loadLocalidades(this.domicilio.getMunicipio().getKey());
      this.attrs.put("localidades", gestor.getLocalidades());
      this.domicilio.setLocalidad((UISelectEntity) UIBackingUtilities.toFirstKeySelectEntity(gestor.getLocalidades()));
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
  }

  public void doLoadLocalidadesCalle() {
    Gestor gestor = null;
    try {
      gestor = new Gestor();
      gestor.loadCodigosPostales(this.domicilio.getLocalidad().getKey());
      this.attrs.put("detalleCalles", gestor.getCodigosPostales());
      this.domicilio.setDetalleCalle((UISelectEntity) UIBackingUtilities.toFirstKeySelectEntity(gestor.getCodigosPostales()));
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
  }

  private Long toIdTemporal() {
    Long regresar = (Long) this.attrs.get("idTemporal") + -1L;
    this.attrs.put("idTemporal", regresar);
    return regresar;
  }

  public String doAceptar() {
    EAccion accion = (EAccion) this.attrs.get("accion");
    this.renglonProveedor.getTcManticProveedoresDto().setIdTipoProveedor(this.renglonProveedor.getIdTipoProveedor().getKey());
    String regresar = null;
    Transaccion transaccion = null;
    String mensajeNotificacion = null;
    try {
      regresar = "filtro".concat(Constantes.REDIRECIONAR);
      switch (accion) {
        case AGREGAR:
          transaccion = new Transaccion(this.renglonProveedor.getTcManticProveedoresDto(), this.renglonProveedor.getDomicilios(), this.getRenglonProveedor().getAgentes(), this.renglonProveedor.getContactos(), this.renglonProveedor.getCondicionPagos(), this.renglonProveedor.getResponsables());
          mensajeNotificacion = UIMessage.toMessage("mantic", "proveedor_correcto");
          break;
        case MODIFICAR:
          transaccion = new Transaccion(this.renglonProveedor.getTcManticProveedoresDto(), this.renglonProveedor.getDomicilios(), this.getRenglonProveedor().getAgentes(), this.renglonProveedor.getContactos(), this.renglonProveedor.getCondicionPagos(), this.renglonProveedor.getResponsables(), this.renglonProveedor.getListaEliminar());
          mensajeNotificacion = UIMessage.toMessage("mantic", "proveedor_actualizacion");
          break;
        case ELIMINAR:
          transaccion = new Transaccion(this.renglonProveedor.getTcManticProveedoresDto());
          mensajeNotificacion = UIMessage.toMessage("mantic", "proveedor_eliminacion");
          regresar = null;
          break;
      } // switch
      transaccion.ejecutar(accion);
      JsfBase.addMessage(mensajeNotificacion, ETipoMensaje.INFORMACION);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      regresar = null;
      JsfBase.addMessage(UIMessage.toMessage("mantic", "proveedor_error"), ETipoMensaje.ERROR);
    } // catch
    return regresar;
  } // doAccion  

  private void fillDomicilio(Domicilio domicilio) {
    if (!domicilio.getDetalleCalle().equals(-1L)) {
      domicilio.setIdDomicilio(domicilio.getDetalleCalle().getKey());
    }
  }

  public void doAgregarDocimilio() {
    Domicilio cloneDomicilio = (Domicilio) this.domicilio.clone();
    cloneDomicilio.setIdProveedorDomicilio(toIdTemporal());
    cloneDomicilio.setAccion(ESql.INSERT);
    cloneDomicilio.setIdUsuario(JsfBase.getIdUsuario());
    int posPrincipal = -1;
    Domicilio domicilioBefore = null;
    Long idPrincipalKeybefore = (Long) this.attrs.get("idDomicilioPrincipal");
    if (cloneDomicilio.getIdPrincipal().equals(1L)) {
      this.attrs.put("idDomicilioPrincipal", cloneDomicilio.getKey());
      domicilioBefore = new Domicilio();
      domicilioBefore.setIdProveedorDomicilio(idPrincipalKeybefore);
      posPrincipal = this.renglonProveedor.getDomicilios().indexOf(domicilioBefore);
      this.renglonProveedor.getDomicilios().get(posPrincipal).setIdPrincipal(2L);
    }
    fillDomicilio(cloneDomicilio);
    this.renglonProveedor.addDomicilio(cloneDomicilio);
    clean();
  }

  private void clean() {
    this.domicilio.getTcManticDomicilioDto().setCalle(null);
    this.domicilio.getTcManticDomicilioDto().setAsentamiento(null);
    this.domicilio.getTcManticDomicilioDto().setNumeroExterior(null);
    this.domicilio.getTcManticDomicilioDto().setNumeroInterior(null);
    this.domicilio.getTcManticDomicilioDto().setEntreCalle(null);
    this.domicilio.getTcManticDomicilioDto().setYcalle(null);
    this.domicilio.getTcManticDomicilioDto().setCodigoPostal(null);
    this.domicilio.getTcManticDomicilioDto().setCodigoPostal(null);
    this.domicilio.setIdPrincipal(2L);
    doLoadLocalidadesCalle();
  }

  public void doEliminarDomicilio(Domicilio domicilio) {
    this.renglonProveedor.getDomicilios().remove(domicilio);
    if (domicilio.getKey() > 0L) {
      //this.renglonProveedor.getListaEliminar().add(domicilio);
      try {
        eliminar(domicilio);
      } // try
      catch (Exception e) {
        JsfBase.addMessageError(e);
      } // catc
    }
  }

  public void doActualizarDomicilio() {
    Domicilio modificar = (Domicilio) this.domicilio.clone();
    domicilio.setAccion(ESql.UPDATE);
    domicilio.setModificar(false);
    int pos = this.renglonProveedor.getDomicilios().indexOf(modificar);
    this.renglonProveedor.getDomicilios().remove(pos);
    this.renglonProveedor.getDomicilios().add(pos, modificar);
    this.attrs.put("idDomicilioPrincipal", modificar.getKey());
  }

  private void cleanModify(){
    for(Domicilio record: this.renglonProveedor.getDomicilios()) {
       record.setModificar(false);
    }//for
  }
  
  public void doBuscarDomicilio(Domicilio domicilio) {
    int pos = -1;
    pos = this.renglonProveedor.getDomicilios().indexOf(domicilio);
    this.domicilio = (Domicilio) this.renglonProveedor.getDomicilios().get(pos).clone();
    cleanModify();
    switch (domicilio.getAccion()) {
      case UPDATE:
        Gestor gestor = new Gestor();
        gestor.loadMunicipios(this.domicilio.getEntidad().getKey());
        this.attrs.put("municipios", gestor.getMunicipios());
        gestor.loadLocalidades(this.domicilio.getMunicipio().getKey());
        this.attrs.put("localidades", gestor.getLocalidades());
        gestor.loadCodigosPostales(this.domicilio.getLocalidad().getKey());
        this.attrs.put("detalleCalles", gestor.getCodigosPostales());
        this.renglonProveedor.getDomicilios().get(pos).setModificar(true);
        break;
      case SELECT:
        this.attrs.put("municipios", new ArrayList<UISelectEntity>());
        this.attrs.put("localidades", new ArrayList<UISelectEntity>());
        this.attrs.put("detalleCalles", new ArrayList<UISelectEntity>());
        break;
      case INSERT:
        this.renglonProveedor.getDomicilios().get(pos).setModificar(true);
       break;
    } // switch 

  }

  public void doAgregarResponsable() {
    Responsable responsable = new Responsable(ESql.INSERT);
    try {
      responsable.setIdProveedorPersona(toIdTemporal());
      responsable.setIdResponsable(2L);
      responsable.setIdUsuario(JsfBase.getIdUsuario());      
      if (exist("personas")) {
        this.renglonProveedor.getResponsables().add(responsable);
      }
       else {
        JsfBase.addMessage("No hay responsables disponibles para el proveedor,favor de verificar el catálogo de representantes", ETipoMensaje.INFORMACION);
      }
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
  }

  private void eliminar(IBaseDto baseDto) throws Exception {
    Transaccion transaccion = null;
    try {
      transaccion = new Transaccion(baseDto);
      if (transaccion.ejecutar(EAccion.DEPURAR)) {
        JsfBase.addMessage(UIMessage.toMessage("mantic", "proveedor_general_eliminacion"));
      } // if
      else {
        JsfBase.addMessage(UIMessage.toMessage("mantic", "proveedor_general_error"), ETipoMensaje.ERROR);
      } // else
    } // try
    catch (Exception e) {
      throw e;
    } // catch
  }

  public void doEliminarResponsable(Responsable responsable) {
    try {
      this.renglonProveedor.getResponsables().remove(responsable);
      if (responsable.getKey() > 0) {
        //this.renglonProveedor.getListaEliminar().add(responsable);    
        try {
          eliminar(responsable);
        } // try
        catch (Exception e) {
          JsfBase.addMessageError(e);
        } // catc
      } // if
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    }// catch
  }

  public void doAgregarContacto() {
    Contacto contacto = new Contacto(ESql.INSERT);
    try {
      contacto.setIdUsuario(JsfBase.getIdUsuario());
      contacto.setIdProveedorTipoContacto(toIdTemporal());
      contacto.setOrden(this.renglonProveedor.getContactos().size() == 0 ? 1L : Integer.valueOf(this.renglonProveedor.getContactos().size() + 1).longValue());
      if (exist("tiposContactos")) {
        this.renglonProveedor.getContactos().add(contacto);
      } else {
        JsfBase.addMessage("No hay medio de contacto para el prooveedor", ETipoMensaje.INFORMACION);
      }
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
  }

  public void doEliminarContacto(Contacto contacto) {
    this.renglonProveedor.getContactos().remove(contacto);
    if (contacto.getKey() > 0) {
      //this.renglonProveedor.getListaEliminar().add(contacto);
      try {
        eliminar(contacto);
      } // try
      catch (Exception e) {
        JsfBase.addMessageError(e);
      } // catch     
    }
  }

  public void doAgregarAgente() {
    Agente agente = new Agente(ESql.INSERT);
    try {
      agente.setIdPrincipal(2L);
      agente.setIdProveedorAgente(toIdTemporal());
      agente.setIdUsuario(JsfBase.getIdUsuario());
      if (exist("agentes")) {
        this.renglonProveedor.getAgentes().add(agente);
      } else {
        JsfBase.addMessage("No hay agentes  disponibles,favor de verificar el catálogo de agentes", ETipoMensaje.INFORMACION);
      }
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    }// cathc
  }

  public void doEliminarAgente(Agente agente) {
    this.renglonProveedor.getAgentes().remove(agente);
    if (agente.getKey() > 0L) {
      //this.renglonProveedor.getListaEliminar().add(agente);
      try {
        eliminar(agente);
      } // try
      catch (Exception e) {
        JsfBase.addMessageError(e);
      } // catc
    }
  }

  public void doAgregarCondicionPago() {
    CondicionPago condicionPago = null;
    try {
      condicionPago = new CondicionPago(ESql.INSERT);
      condicionPago.setIdProveedorPago(toIdTemporal());
      condicionPago.setDescuento(0.0);
      condicionPago.setIdUsuario(JsfBase.getIdUsuario());
      if (exist("tiposPago")) {
        this.renglonProveedor.getCondicionPagos().add(condicionPago);
      } else {
        JsfBase.addMessage("No hay condiciones de pago establecidas", ETipoMensaje.INFORMACION);
      }
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
  }

  public void doEliminarCondicionPago(CondicionPago condicionPago) {
    this.renglonProveedor.getCondicionPagos().remove(condicionPago);
    if (condicionPago.getKey() > 0L) {
      //this.renglonProveedor.getListaEliminar().add(condicionPago);
      try {
        eliminar(condicionPago);
      } // try
      catch (Exception e) {
        JsfBase.addMessageError(e);
      } // catc

    }
  }

  public void doActualizaCalle() {
    boolean isNuevo = false;
    int posDetalle = -1;
    UISelectEntity detalleCalle = null;
    try {
      posDetalle = ((List<UISelectEntity>) this.attrs.get("detalleCalles")).indexOf(new UISelectEntity(this.domicilio.getDetalleCalle().getKey().toString()));
      detalleCalle = ((List<UISelectEntity>) this.attrs.get("detalleCalles")).get(posDetalle);
      isNuevo = detalleCalle.getKey().equals(-1L);
      this.domicilio.getTcManticDomicilioDto().setCalle(isNuevo ? null : detalleCalle.toString("calle"));
      this.domicilio.getTcManticDomicilioDto().setAsentamiento(isNuevo ? null : detalleCalle.toString("asentamiento"));
      this.domicilio.getTcManticDomicilioDto().setCodigoPostal(isNuevo ? null : detalleCalle.toString("codigoPostal"));
      this.domicilio.getTcManticDomicilioDto().setYcalle(isNuevo ? null : detalleCalle.toString("ycalle"));
      this.domicilio.getTcManticDomicilioDto().setEntreCalle(isNuevo ? null : detalleCalle.toString("entreCalle"));
      this.domicilio.getTcManticDomicilioDto().setNumeroExterior(isNuevo ? null : detalleCalle.toString("numeroExterior"));
      this.domicilio.getTcManticDomicilioDto().setNumeroInterior(isNuevo ? null : detalleCalle.toString("numeroInterior"));
      this.domicilio.getTcManticDomicilioDto().setIdLocalidad(detalleCalle.toLong("idLocalidad"));
      //this.domicilio.getTcManticDomicilioDto().setIdLocalidad(detalleCalle.toLong("idLocalidad"));
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    }// catch
  }
  
  private boolean exist(String nameKeyList) {
    boolean regresar = false;
    if (this.attrs.get(nameKeyList) != null) {
      regresar = ((List<UISelectItem>) this.attrs.get(nameKeyList)).size() > 0;
    }// if
    return regresar;
  }

  public String doCancelar() {

    return "filtro";
  } // doAccion

}
