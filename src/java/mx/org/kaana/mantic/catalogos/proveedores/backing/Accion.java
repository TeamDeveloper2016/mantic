package mx.org.kaana.mantic.catalogos.proveedores.backing;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.ESql;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.mantic.catalogos.proveedores.beans.Domicilio;
import mx.org.kaana.mantic.catalogos.proveedores.beans.RenglonProveedor;
import mx.org.kaana.mantic.catalogos.proveedores.reglas.Gestor;

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
      renglonProveedor = new RenglonProveedor(JsfBase.getFlashAttribute("idProveedor") == null ? -1L : (Long) JsfBase.getFlashAttribute("idProveedor"));
      this.attrs.put("accion", JsfBase.getFlashAttribute("accion"));
      gestor = new Gestor();
      this.domicilio = new Domicilio(ESql.SELECT);
      gestor.loadEntidades(!renglonProveedor.getTcManticProveedoresDto().isValid());
      this.attrs.put("entidades", gestor.getEntidades());
      this.domicilio.setEntidad((UISelectEntity) UIBackingUtilities.toFirstKeySelectEntity(gestor.getEntidades()));
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

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

  }

  public void doEliminarDomicilio(Domicilio domicilio, int index) {

  }

  public void doBuscarDomicilio(Domicilio domicilio, int index) {

  }

  public String doCancelar() {

    return "filtro";
  } // doAccion

}
