package mx.org.kaana.mantic.catalogos.refacciones.backing;

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
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.pagina.IBaseFilter;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.iva.reglas.Transaccion;
import mx.org.kaana.mantic.db.dto.TcManticHistorialIvaDto;

@Named(value = "manticCatalogosRefaccionesFiltro")
@ViewScoped
public class Filtro extends IBaseFilter implements Serializable {

  private static final long serialVersionUID = 8793667741599428369L;

  @PostConstruct
  @Override
  protected void init() {
    try {
      this.attrs.put("isMatriz", JsfBase.getAutentifica().getEmpresa().isMatriz());
      this.attrs.put("idDescontinuado", 2L);
      if(this.attrs.get("idRefaccion")!= null) {
			  this.doLoad();
        this.attrs.put("idRefaccion", null);
			} // if	
      this.toLoadCatalog();      
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init
 
  @Override
  public void doLoad() {
    List<Columna> campos      = null;
		Map<String, Object> params= this.toPrepare();
    try {
      params.put("sortOrder", "order by tc_mantic_refacciones.id_proveedor, tc_mantic_refacciones.codigo");
      campos = new ArrayList<>();
      campos.add(new Columna("codigo", EFormatoDinamicos.MAYUSCULAS));      
      campos.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));      
      campos.add(new Columna("fabricante", EFormatoDinamicos.MAYUSCULAS));      
      campos.add(new Columna("herramienta", EFormatoDinamicos.MAYUSCULAS));      
      campos.add(new Columna("costo", EFormatoDinamicos.NUMERO_CON_DECIMALES));
      campos.add(new Columna("precio", EFormatoDinamicos.NUMERO_CON_DECIMALES));
      this.lazyModel = new FormatCustomLazy("VistaRefaccionesDto", params, campos);
      UIBackingUtilities.resetDataTable();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    finally {
      Methods.clean(campos);
      Methods.clean(params);
    } // finally		
  } // doLoad

  public String doAccion(String accion) {
    EAccion eaccion= null;
		try {
			eaccion= EAccion.valueOf(accion.toUpperCase());
			JsfBase.setFlashAttribute("accion", eaccion);		
			JsfBase.setFlashAttribute("retorno", "/Paginas/Mantic/Catalogos/Refacciones/filtro");		
			JsfBase.setFlashAttribute("idRefaccion", (eaccion.equals(EAccion.MODIFICAR) || eaccion.equals(EAccion.CONSULTAR)) ? ((Entity)this.attrs.get("seleccionado")).getKey() : -1L);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch
		return "/Paginas/Mantic/Catalogos/Refacciones/accion".concat(Constantes.REDIRECIONAR);
  } // doAccion  
	
  public void doEliminar() {
		Transaccion transaccion= null;
		Entity seleccionado    = null;
		try {
			seleccionado= (Entity) this.attrs.get("seleccionado");			
			transaccion= new Transaccion(new TcManticHistorialIvaDto(seleccionado.getKey()));
			if(transaccion.ejecutar(EAccion.ELIMINAR))
				JsfBase.addMessage("Eliminar", "La refacción se ha eliminado correctamente.", ETipoMensaje.ERROR);
			else
				JsfBase.addMessage("Eliminar", "Ocurrió un error al eliminar el registro de la refacción.", ETipoMensaje.ERROR);								
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch			
  } // doEliminar	
  
	private void toLoadCatalog() {
		List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
    try {
			columns= new ArrayList<>();
			if(JsfBase.getAutentifica().getEmpresa().isMatriz())
        params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresaDepende());
			else
				params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
			params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
			params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
      columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      this.attrs.put("empresas", (List<UISelectEntity>) UIEntity.build("TcManticEmpresasDto", "empresas", params, columns));
			this.attrs.put("idEmpresa", new UISelectEntity("-1"));
      this.attrs.put("proveedores", (List<UISelectEntity>) UIEntity.build("VistaNotasEntradasDto", "proveedores", params, columns));
			this.attrs.put("idProveedor", new UISelectEntity("-1"));
    } // try
    catch (Exception e) {
      throw e;
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    }// finally
	}

	private Map<String, Object> toPrepare() {
		Map<String, Object> regresar= new HashMap<>();
		StringBuilder sb            = null;
		try {
			sb= new StringBuilder();			
			if(!Cadena.isVacio(this.attrs.get("idRefaccion")))
  		  sb.append("(tc_mantic_refacciones.id_refaccion=").append(this.attrs.get("idRefaccion")).append(") and ");
			if(!Cadena.isVacio(this.attrs.get("idProveedor")) && ((UISelectEntity)this.attrs.get("idProveedor")).getKey()> 0)
  		  sb.append("(tc_mantic_refacciones.id_proveedor=").append(((UISelectEntity)this.attrs.get("idProveedor")).getKey()).append(") and ");
			if(!Cadena.isVacio(this.attrs.get("codigo")))
				sb.append("upper(tc_mantic_refacciones.codigo) like upper('%").append((String)this.attrs.get("codigo")).append("%') and ");						
			if(!Cadena.isVacio(this.attrs.get("nombre")))
				sb.append("upper(tc_mantic_refacciones.nombre) like upper('%").append((String)this.attrs.get("nombre")).append("%') and ");						
			if(!Cadena.isVacio(this.attrs.get("fabricante")))
				sb.append("upper(tc_mantic_refacciones.fabricante) like upper('%").append((String)this.attrs.get("fabricante")).append("%') and ");						
			if(!Cadena.isVacio(this.attrs.get("herramienta")))
				sb.append("upper(tc_mantic_refacciones.herramienta) like upper('%").append((String)this.attrs.get("herramienta")).append("%') and ");						
			if(!Cadena.isVacio(this.attrs.get("idVigente")))
  		  sb.append("(tc_mantic_refacciones.id_vigente=").append(this.attrs.get("idVigente")).append(") and ");
			if(!Cadena.isVacio(this.attrs.get("idDescontinuado")))
  		  sb.append("(tc_mantic_refacciones.id_descontinuado=").append(this.attrs.get("idDescontinuado")).append(") and ");
			if(Cadena.isVacio(sb.toString()))
				regresar.put("condicion", Constantes.SQL_VERDADERO);
			else
			  regresar.put("condicion", sb.substring(0, sb.length()- 4));			
		  if(Cadena.isVacio(this.attrs.get("idEmpresa")) || this.attrs.get("idEmpresa").toString().equals("-1"))
			  regresar.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getDependencias());
			else
			  regresar.put("idEmpresa", ((UISelectEntity)this.attrs.get("idEmpresa")).getKey());
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toPrepare
  
}