package mx.org.kaana.mantic.catalogos.listasPrecios.backing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import mx.org.kaana.libs.pagina.IBaseFilter;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.listasPrecios.reglas.Transaccion;
import mx.org.kaana.mantic.db.dto.TcManticListasPreciosDto;

@Named(value = "manticCatalogosListaPreciosFiltro")
@ViewScoped
public class Filtro extends IBaseFilter implements Serializable {

  private static final long serialVersionUID = 862453337895123687L;

  @PostConstruct
  @Override
  protected void init() {
    try {
      this.attrs.put("sortOrder", "order by tc_mantic_proveedores.razon_social");
      this.attrs.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

  public void doLoadProveedores(){
		Map<String, Object>params     = null;
		List<UISelectEntity> proveedores= null;
		try {
			params= new HashMap<>();
			params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
      proveedores =  UIEntity.build("TcManticProveedoresDto", "sucursales", params);
      this.attrs.put("proveedores", proveedores);
			this.attrs.put("idProveedor", new UISelectEntity("-1"));
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
		finally {
			Methods.clean(params);
		} // finally
	} // doLoadEstatus

  @Override
  public void doLoad() {
    List<Columna> campos = null;
    try {
      campos = new ArrayList<>();
      campos.add(new Columna("razonSocial", EFormatoDinamicos.MAYUSCULAS));
      campos.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
      campos.add(new Columna("observaciones", EFormatoDinamicos.MAYUSCULAS));
      campos.add(new Columna("registro", EFormatoDinamicos.FECHA_CORTA));
      this.lazyModel = new FormatCustomLazy("VistaListasArchivosDto", "lazy", this.attrs, campos);
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
  
  public void doAgregarListaProveedor(){
		Transaccion transaccion                 = null;
		TcManticListasPreciosDto listaProveedor = null;
    try {
      listaProveedor = new TcManticListasPreciosDto();
      listaProveedor.setIdProveedor(((UISelectEntity)this.attrs.get("idProveedor")).getKey());
      listaProveedor.setObservaciones(this.attrs.get("observacion").toString());
      listaProveedor.setIdUsuario(JsfBase.getAutentifica().getPersona().getIdUsuario());
      transaccion= new Transaccion(listaProveedor);
      if(transaccion.ejecutar(EAccion.AGREGAR)){
        JsfBase.addMessage("Agregar lista de precios proveedor", "Se agregó con exito el proveedor para la lista", ETipoMensaje.INFORMACION);
      }
      else
        JsfBase.addMessage("Agregar lista de precios proveedor", "Ocurrio un agregar el proveedor para la lista", ETipoMensaje.ERROR);
		} // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
    finally{
      this.attrs.put("observacion", "");
      this.attrs.put("idProveedor", new UISelectEntity("-1"));
    } // finally
  }	// doAgregarListaProveedor

  public void doEliminar() {
    Transaccion transaccion = null;
    try {
      transaccion = new Transaccion(new TcManticListasPreciosDto(((Entity)this.attrs.get("seleccionado")).getKey()));
      transaccion.ejecutar(EAccion.ELIMINAR);
      JsfBase.addMessage("Eliminar lista de precios proveedor", "La lista de precios de el proveedor se ha eliminado correctamente.", ETipoMensaje.INFORMACION);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // doEliminar
}
