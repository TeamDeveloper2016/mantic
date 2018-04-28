package mx.org.kaana.mantic.catalogos.proveedores.backing;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
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
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.proveedores.beans.Agente;
import mx.org.kaana.mantic.catalogos.proveedores.beans.CondicionPago;
import mx.org.kaana.mantic.catalogos.proveedores.beans.Contacto;
import mx.org.kaana.mantic.catalogos.proveedores.beans.Domicilio;
import mx.org.kaana.mantic.catalogos.proveedores.beans.RenglonProveedor;
import mx.org.kaana.mantic.catalogos.proveedores.beans.Responsable;
import mx.org.kaana.mantic.catalogos.proveedores.reglas.Gestor;
import mx.org.kaana.mantic.catalogos.proveedores.reglas.Transaccion;
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
    try {
      this.attrs.put("idTemporal", -1L);
      this.attrs.put("idDomicilioPrincipal",-1L);
      renglonProveedor = new RenglonProveedor(JsfBase.getFlashAttribute("idProveedor") == null ? -1L : (Long) JsfBase.getFlashAttribute("idProveedor"));
      this.attrs.put("accion", JsfBase.getFlashAttribute("accion"));
      gestor = new Gestor();
      this.domicilio = new Domicilio(ESql.SELECT);
      gestor.loadEntidades(!renglonProveedor.getTcManticProveedoresDto().isValid());
      this.attrs.put("entidades", gestor.getEntidades());
      this.domicilio.setEntidad((UISelectEntity) UIBackingUtilities.toFirstKeySelectEntity(gestor.getEntidades()));
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

  private void loadTiposProveedores() throws Exception{    
     this.renglonProveedor.loadTiposProveedores();
  }
  
  private void loadCondicionesPago() throws Exception {
    loadTipoPago();
    this.renglonProveedor.loadCondicionesPago();
  }

  private void loadPersonas() throws Exception {
    Map params = new HashMap();
    try {
      params.put("sortOrder", "order by tc_mantic_personas.nombres");
      params.put("idTipoPersona", ETipoPersona.RESPONSABLE.getIdTipoPersona());
      this.attrs.put("personas", UISelect.build("TcManticPersonasDto", "row", params, "nombres|paterno|materno", " ", EFormatoDinamicos.MAYUSCULAS));
    } // try
    catch (Exception e) {
      throw e;
    } // catch   
  }

  private void loadAgentes() throws Exception {
    Map params = new HashMap();
    try {
      params.put("sortOrder", "order by tc_mantic_personas.nombres");
      params.put("idTipoPersona", ETipoPersona.AGENTE_VENTAS.getIdTipoPersona());
      this.attrs.put("personas", UISelect.build("TcManticPersonasDto", "row", params, "nombres|paterno|materno", " ", EFormatoDinamicos.MAYUSCULAS));
    } // try
    catch (Exception e) {
      throw e;
    } // catch   
  }

  private void loadContactos() throws Exception {
    Map params = new HashMap();
    try {
      params.put(Constantes.SQL_CONDICION, Constantes.SQL_TODOS_REGISTROS);
      this.attrs.put("tiposContactos", UISelect.build("TcManticTiposContactosDto", "row", params, "nombre", " ", EFormatoDinamicos.MAYUSCULAS));
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
    EAccion accion  = (EAccion) this.attrs.get("accion");
    String regresar = null;
    Transaccion transaccion = null;
    String mensajeNotificacion= null;    
    try {
      regresar = "filtro".concat(Constantes.REDIRECIONAR);
      switch (accion){
        case AGREGAR:
          transaccion = new Transaccion(this.renglonProveedor.getTcManticProveedoresDto(),this.renglonProveedor.getDomicilios(),this.getRenglonProveedor().getAgentes(),this.renglonProveedor.getContactos(),this.renglonProveedor.getCondicionPagos(),this.renglonProveedor.getResponsables());
          mensajeNotificacion = UIMessage.toMessage("mantic", "proveedor_correcto");          
        break; 
        case MODIFICAR:
          transaccion  = new Transaccion(this.renglonProveedor.getTcManticProveedoresDto(),this.renglonProveedor.getDomicilios(),this.getRenglonProveedor().getAgentes(),this.renglonProveedor.getContactos(),this.renglonProveedor.getCondicionPagos(),this.renglonProveedor.getResponsables(),this.renglonProveedor.getListaEliminar());
          mensajeNotificacion = UIMessage.toMessage("mantic", "proveedor_actualizacion");
        break;  
        case ELIMINAR:
          transaccion  = new Transaccion(this.renglonProveedor.getTcManticProveedoresDto());
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

  
  private void fillDomicilio(Domicilio domicilio){
    if (!domicilio.getDetalleCalle().equals(-1L)){
      domicilio.setIdDomicilio(domicilio.getDetalleCalle().getKey());
    }
  }
  
  public void doAgregarDocimilio() {   
    this.domicilio.setIdProveedorDomicilio(toIdTemporal());
    this.domicilio.setAccion(ESql.INSERT);
    int posPrincipal          = -1;
    Domicilio domicilioBefore     = null;
    Long idPrincipalKeybefore =(Long) this.attrs.get("idDomicilioPrincipal");
    if (this.domicilio.getIdPrincipal().equals(1L)){
      this.attrs.put("idDomicilioPrincipal", this.domicilio.getKey());
      domicilioBefore = new Domicilio();
      domicilioBefore.setIdPrincipal(idPrincipalKeybefore);      
      posPrincipal = this.renglonProveedor.getDomicilios().indexOf(domicilioBefore);
      this.renglonProveedor.getDomicilios().get(posPrincipal).setIdPrincipal(2L);    
    }
    fillDomicilio(domicilio);
    this.renglonProveedor.addDomicilio(this.domicilio);
    this.domicilio = new Domicilio();
  }

  public void doEliminarDomicilio(Domicilio domicilio) {
    this.renglonProveedor.getDomicilios().remove(domicilio);    
    if (domicilio.getKey()>0L)
      this.renglonProveedor.getListaEliminar().add(domicilio);
  }

  public void doBuscarDomicilio(Domicilio domicilio) {
    int pos = -1;
    pos = this.renglonProveedor.getDomicilios().indexOf(domicilio);
    domicilio = this.renglonProveedor.getDomicilios().get(pos);
  }

  public void doAgregarResponsable() {
    try {

    } // try
    catch (Exception e) {

    } // catch
  }

  public void doEliminarResponsable(Responsable responsable) {    
    this.renglonProveedor.getResponsables().remove(responsable);  
    if (responsable.getKey()>0){
      this.renglonProveedor.getListaEliminar().add(responsable);
    }
  }
  

  public void doAgregarContacto() {

  }

  public void doEliminarContacto(Contacto contacto) {
    this.renglonProveedor.getContactos().remove(contacto);
  }

  public void doAgregarAgente(Agente agente) {

  }

  public void doEliminarAgente(Agente agente) {
    this.renglonProveedor.getAgentes().remove(agente);
    if (agente.getKey()>0L)
      this.renglonProveedor.getListaEliminar().add(agente);
  }
  
  public void doAgregarCondicionPago() {

  }

  public void doEliminarCondicionPago(CondicionPago condicionPago) {
    this.renglonProveedor.getCondicionPagos().remove(condicionPago);
    if (condicionPago.getKey()>0L)
      this.renglonProveedor.getListaEliminar().add(condicionPago);
  }

  public String doCancelar() {

    return "filtro";
  } // doAccion

}
