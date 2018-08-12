package mx.org.kaana.mantic.catalogos.listasprecios.backing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.IBaseFilter;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.listasprecios.reglas.Transaccion;
import mx.org.kaana.mantic.db.dto.TcManticListasPreciosDto;

@javax.inject.Named("manticCatalogosListaPreciosFiltro")
@ViewScoped
public class Filtro extends IBaseFilter implements java.io.Serializable
{
  private static final long serialVersionUID = 862453337895123687L;
  
  public Filtro() {}
  
  @PostConstruct
  protected void init(){
    try {
      attrs.put("sortOrder", "order by tc_mantic_proveedores.razon_social");
      attrs.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
    }
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    }
  }
  
  public void doLoadProveedores() {
    Map<String, Object> params = null;
    List<UISelectEntity> proveedores = null;
    try {
      params = new HashMap();
      params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
      proveedores = mx.org.kaana.libs.pagina.UIEntity.build("TcManticProveedoresDto", "sucursales", params);
      attrs.put("proveedores", proveedores);
      attrs.put("idProveedor", new UISelectEntity("-1"));
    }
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    }
    finally {
      Methods.clean(params);
    }
  }
  
  public void doLoad(){
    List<Columna> campos = null;
    try {
      campos = new ArrayList();
      campos.add(new Columna("razonSocial", EFormatoDinamicos.MAYUSCULAS));
      campos.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
      campos.add(new Columna("observaciones", EFormatoDinamicos.MAYUSCULAS));
      campos.add(new Columna("registro", EFormatoDinamicos.FECHA_CORTA));
      lazyModel = new FormatCustomLazy("VistaListasArchivosDto", "lazy", attrs, campos);
      mx.org.kaana.libs.pagina.UIBackingUtilities.resetDataTable();
    }
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    }
    finally {
      Methods.clean(campos);
    }
  }
  
  public void doAgregarListaProveedor() {
    Transaccion transaccion = null;
    TcManticListasPreciosDto listaProveedor = null;
    try {
      listaProveedor = new TcManticListasPreciosDto();
      listaProveedor.setIdProveedor(((UISelectEntity)attrs.get("idProveedor")).getKey());
      listaProveedor.setObservaciones(attrs.get("observacion").toString());
      listaProveedor.setIdUsuario(JsfBase.getAutentifica().getPersona().getIdUsuario());
      transaccion = new Transaccion(listaProveedor);
      if (transaccion.ejecutar(EAccion.AGREGAR)) {
        JsfBase.addMessage("Agregar lista de precios proveedor", "Se agregó con exito el proveedor para la lista", ETipoMensaje.INFORMACION);
      }
      else {
        JsfBase.addMessage("Agregar lista de precios proveedor", "Ocurrio un agregar el proveedor para la lista", ETipoMensaje.ERROR);
      }
    } catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    }
    finally {
      attrs.put("observacion", "");
      attrs.put("idProveedor", new UISelectEntity("-1"));
    }
  }
  
  public void doEliminar() {
    Transaccion transaccion = null;
    try {
      transaccion = new Transaccion(new TcManticListasPreciosDto(((Entity)attrs.get("seleccionado")).getKey()));
      transaccion.ejecutar(EAccion.ELIMINAR);
      JsfBase.addMessage("Eliminar lista de precios proveedor", "La lista de precios de el proveedor se ha eliminado correctamente.", ETipoMensaje.INFORMACION);
    }
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    }
  }
  
  public String doImportar() {
    JsfBase.setFlashAttribute("retorno", "/Paginas/Mantic/Catalogo/ListasPrecios/filtro");
    JsfBase.setFlashAttribute("idListaPrecio", ((Entity)attrs.get("seleccionado")).getKey());
    return "importar".concat("?faces-redirect=true");
  }
}
