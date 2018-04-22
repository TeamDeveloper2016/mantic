package mx.org.kaana.mantic.catalogos.proveedores.backing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.procesos.comun.Comun;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.pagina.IBaseFilter;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.proveedores.reglas.Gestor;

@Named(value = "manticCatalogosProveedoresFiltro")
@ViewScoped
public class Filtro extends IBaseFilter implements Serializable {

  private static final long serialVersionUID = 8793667741599428879L;

  @PostConstruct
  @Override
  protected void init() {
    try {      
      this.attrs.put("sortOrder","order by tc_mantic_proveedores.razon_social");
      this.attrs.put("idEmpresa",JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
      loadTiposProveedores();      
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init
  
  private void loadTiposProveedores() throws Exception {
    Gestor gestor = new Gestor();
    gestor.loadTiposProveedores();
    this.attrs.put("tiposProveedores", gestor.getTiposProveedores());
    this.attrs.put("tipoProveedor", UIBackingUtilities.toFirstKeySelectEntity(gestor.getTiposProveedores()));    
  }

  private String toAllTiposProveedores () {
    StringBuilder regresar  = new StringBuilder();
    List<UISelectEntity> tiposProveedores = (List<UISelectEntity>) this.attrs.get("tiposProveedores");
    for (UISelectEntity tipoProvedor: tiposProveedores){
      regresar.append(tipoProvedor.getKey());
      regresar.append(",");
    } // for
    return regresar.substring(0,regresar.length()-1);
  }
  @Override
  public void doLoad() {
    List<Columna> campos = null;
    try {
      campos = new ArrayList<>();
      campos.add(new Columna("razonSocial", EFormatoDinamicos.MAYUSCULAS));
      campos.add(new Columna("tipoProveedor", EFormatoDinamicos.MAYUSCULAS));
      campos.add(new Columna("tipoDia", EFormatoDinamicos.MAYUSCULAS));
      campos.add(new Columna("rfc", EFormatoDinamicos.MAYUSCULAS));
      this.attrs.put("idTipoProveedor",((UISelectEntity)this.attrs.get("tipoProveedor")).getKey().equals(-1L)?toAllTiposProveedores():((UISelectEntity)this.attrs.get("tipoProveedor")).getKey());
      this.lazyModel = new FormatCustomLazy("VistaProveedoresDto", "row", this.attrs, campos);
      UIBackingUtilities.resetDataTable();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    finally {
      Methods.clean(campos);
    } // finally		
  } // doLoad

  public String doAccion(String accion) {
    EAccion eaccion = null;
    try {

    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    return "accion".concat(Constantes.REDIRECIONAR);
  } // doAccion

  public void doEliminar() {

    try {

    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // doEliminar
}
