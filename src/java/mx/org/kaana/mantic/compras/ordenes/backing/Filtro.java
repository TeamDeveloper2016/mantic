package mx.org.kaana.mantic.compras.ordenes.backing;

import java.io.Serializable;
import java.sql.Date;
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
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.pagina.IBaseFilter;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UISelect;
import mx.org.kaana.libs.pagina.UISelectItem;
import mx.org.kaana.libs.reflection.Methods;

@Named(value = "manticComprasOrdenesFiltro")
@ViewScoped
public class Filtro extends IBaseFilter implements Serializable {

  private static final long serialVersionUID = 8793667741599428332L;

  @PostConstruct
  @Override
  protected void init() {
    try {
      this.attrs.put("isMatriz", JsfBase.getAutentifica().getEmpresa().isMatriz());
      this.attrs.put("sortOrder", "order by tc_mantic_ordenes_compras.id_empresa, tc_mantic_ordenes_compras.ejercicio, tc_mantic_ordenes_compras.orden");
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init
 
  @Override
  public void doLoad() {
    List<Columna> campos      = null;
		Map<String, Object> params= toPrepare();
    try {
			this.attrs.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
      campos = new ArrayList<>();
      campos.add(new Columna("proveedor", EFormatoDinamicos.MAYUSCULAS));
      campos.add(new Columna("empresa", EFormatoDinamicos.MAYUSCULAS));
      campos.add(new Columna("estatus", EFormatoDinamicos.MAYUSCULAS));
      campos.add(new Columna("total", EFormatoDinamicos.MONEDA_CON_DECIMALES));
      campos.add(new Columna("registro", EFormatoDinamicos.FECHA_CORTA));      
      this.lazyModel = new FormatCustomLazy("VistaOrdenesComprasDto", "row", params, campos);
      UIBackingUtilities.resetDataTable();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    finally {
      Methods.clean(params);
      Methods.clean(campos);
    } // finally		
  } // doLoad

  public String doAccion(String accion) {
    EAccion eaccion= null;
		try {
			eaccion= EAccion.valueOf(accion.toUpperCase());
			JsfBase.setFlashAttribute("accion", eaccion);		
			JsfBase.setFlashAttribute("retorno", "/Paginas/Mantic/Compras/Ordenes/filtro");		
			JsfBase.setFlashAttribute("idOrdenCompra", eaccion.equals(EAccion.MODIFICAR) ? ((Entity)this.attrs.get("seleccionado")).getKey() : -1L);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch
		return "/Paginas/Mantic/Compras/Ordenes/accion".concat(Constantes.REDIRECIONAR);
  } // doAccion  
	
  public void doEliminar() {
//		Transaccion transaccion = null;
//		Entity seleccionado     = null;
//		try {
//			seleccionado= (Entity) this.attrs.get("seleccionado");			
//			transaccion= new Transaccion(new TcManticHistorialIvaDto(seleccionado.getKey()));
//			if(transaccion.ejecutar(EAccion.ELIMINAR))
//				JsfBase.addMessage("Eliminar", "La orden de compra se ha eliminado correctamente.", ETipoMensaje.ERROR);
//			else
//				JsfBase.addMessage("Eliminar", "Ocurrió un error al eliminar la orden de compra.", ETipoMensaje.ERROR);								
//		} // try
//		catch (Exception e) {
//			Error.mensaje(e);
//			JsfBase.addMessageError(e);			
//		} // catch			
  } // doEliminar

	private Map<String, Object> toPrepare() {
	  Map<String, Object> regresar= new HashMap<>();	
		regresar.put("consecutivo", attrs.get("consecutivo"));
		regresar.put("idEmpresa", attrs.get("idEmpresa"));
		regresar.put("idProveedor", attrs.get("idProveedor"));
		regresar.put("idCompraEstatus", attrs.get("idCompraEstatus"));
		if(Cadena.isVacio(attrs.get("fechaInicio")))
		  regresar.put("fechaInicio", "20160101");	
		else
			regresar.put("fechaInicio", Fecha.formatear(Fecha.FECHA_ESTANDAR, (Date)attrs.get("fechaInicio")));	
		if(Cadena.isVacio(attrs.get("fechaTermino")))
		  regresar.put("fechaTermino", "29991231");	
		else
			regresar.put("fechaTermino", Fecha.formatear(Fecha.FECHA_ESTANDAR, (Date)attrs.get("fechaTermino")));	
		return regresar;
	}
	
	private void toLoadCatalog() {
    Map<String, Object> params = new HashMap<>();
    try {
      params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
      this.attrs.put("sucursales", (List<UISelectItem>) UISelect.build("TcSucursalesDto", "row", params, "nombre", " ", EFormatoDinamicos.MAYUSCULAS));
    } // try
    catch (Exception e) {
      throw e;
    } // catch   
    finally {
      Methods.clean(params);
    }// finally
	}
	
}
