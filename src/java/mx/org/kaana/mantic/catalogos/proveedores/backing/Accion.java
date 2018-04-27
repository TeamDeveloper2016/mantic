package mx.org.kaana.mantic.catalogos.proveedores.backing;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ESql;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UISelect;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.proveedores.beans.Agente;
import mx.org.kaana.mantic.catalogos.proveedores.beans.Contacto;
import mx.org.kaana.mantic.catalogos.proveedores.beans.Domicilio;
import mx.org.kaana.mantic.catalogos.proveedores.beans.RenglonProveedor;
import mx.org.kaana.mantic.catalogos.proveedores.beans.Responsable;
import mx.org.kaana.mantic.catalogos.proveedores.reglas.Gestor;
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
      this.attrs.put("idTemporal", -1);
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
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init
  
  private void loadCondicionesPago () throws Exception {
    loadTipoPago();
    this.renglonProveedor.loadCondicionesPago();
  }
  
   private void loadPersonas() throws Exception {
    Map params = new HashMap();
    try {
      params.put("sortOrder", "order by tc_mantic_personas.nombres");
      params.put("idTipoPersona", ETipoPersona.RESPONSABLE.getIdTipoPersona());
      this.attrs.put("personas",UISelect.build("TcManticPersonasDto", "row", params, "nombres|paterno|materno", " ", EFormatoDinamicos.MAYUSCULAS));
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
      this.attrs.put("personas",UISelect.build("TcManticPersonasDto", "row", params, "nombres|paterno|materno", " ", EFormatoDinamicos.MAYUSCULAS));
    } // try
    catch (Exception e) {
      throw e;
    } // catch   
  }
  
  private void loadContactos() throws Exception {
    Map params = new HashMap();
    try {     
      params.put(Constantes.SQL_CONDICION, Constantes.SQL_TODOS_REGISTROS);
      this.attrs.put("tiposContactos",UISelect.build("TcManticTiposContactosDto", "row", params, "nombre", " ", EFormatoDinamicos.MAYUSCULAS));
    } // try
    catch (Exception e) {
      throw e;
    } // catch   
    finally {
      Methods.clean(params);
    }// finally
  }  
  
  private void loadTipoPago() throws Exception{
    Gestor gestor = new Gestor();
    try {
      this.attrs.put("tiposPago", gestor.toTiposPagos());
    } // try
    catch(Exception e){
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

  private Long toIdTemporal () {
      Long regresar = (Long)this.attrs.get("idTemporal")+-1L;
      this.attrs.put("idTemporal", regresar);
      return regresar;
  }
  
  private void load() {
    EAccion eaccion = null;

    try {

    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // doLoad

  public String doAceptar() {

    String regresar = null;
    try {

    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    return regresar;
  } // doAccion  
  
  public void doAgregarDocimilio(Domicilio domicilio, int index) {
    domicilio.setIdDomicilio(toIdTemporal());
    domicilio.setAccion(ESql.INSERT);
    this.renglonProveedor.addDomicilio(new Domicilio(ESql.SELECT));    
  }

  public void doEliminarDomicilio(Domicilio domicilio, int index) {     
     this.renglonProveedor.getDomicilios().remove(domicilio);
  }

  public void doBuscarDomicilio(Domicilio domicilio, int index) {
    int pos = -1;
    pos=this.renglonProveedor.getDomicilios().indexOf(domicilio);
    domicilio = this.renglonProveedor.getDomicilios().get(pos);
  }
  
  public void doAgregarResponsable() {
    try {
    
    } // try
    catch (Exception e) {
    
    } // catch
  }
  
  public void doEliminarResponsable (Responsable responsable) {
  
  }

  public void doAgregarContacto () {
  
  }
  
  public void doEliminarContacto (Contacto contacto) {
  
  }
  
  public void doAgregarAgente (Agente agente) {
  
  }
  
  public void doEliminarAgente (Agente contacto) {
  
  }
  
  
  
  
  public String doCancelar() {

    return "filtro";
  } // doAccion

}
