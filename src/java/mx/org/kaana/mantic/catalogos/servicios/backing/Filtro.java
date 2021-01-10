package mx.org.kaana.mantic.catalogos.servicios.backing;

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

@Named(value = "manticCatalogosServiciosFiltro")
@ViewScoped
public class Filtro extends IBaseFilter implements Serializable {

  private static final long serialVersionUID = 8793667741599428369L;

  @PostConstruct
  @Override
  protected void init() {
    try {
      this.attrs.put("isMatriz", JsfBase.getAutentifica().getEmpresa().isMatriz());
      this.attrs.put("idDescontinuado", 2L);
      if(this.attrs.get("idEncargo")!= null) {
			  this.doLoad();
        this.attrs.put("idEncargo", null);
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
      params.put("sortOrder", "order by tc_mantic_encargos.codigo");
      campos = new ArrayList<>();
      campos.add(new Columna("codigo", EFormatoDinamicos.MAYUSCULAS));      
      campos.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));      
      campos.add(new Columna("linea", EFormatoDinamicos.MAYUSCULAS));      
      campos.add(new Columna("costo", EFormatoDinamicos.NUMERO_CON_DECIMALES));
      campos.add(new Columna("precio", EFormatoDinamicos.NUMERO_CON_DECIMALES));
      this.lazyModel = new FormatCustomLazy("VistaServiciosDto", params, campos);
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
			JsfBase.setFlashAttribute("retorno", "/Paginas/Mantic/Catalogos/Servicios/filtro");		
			JsfBase.setFlashAttribute("idEncargo", (eaccion.equals(EAccion.MODIFICAR) || eaccion.equals(EAccion.CONSULTAR)) ? ((Entity)this.attrs.get("seleccionado")).getKey() : -1L);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch
		return "/Paginas/Mantic/Catalogos/Servicios/accion".concat(Constantes.REDIRECIONAR);
  } // doAccion  
	
  public void doEliminar() {
		Transaccion transaccion= null;
		Entity seleccionado    = null;
		try {
			seleccionado= (Entity) this.attrs.get("seleccionado");			
			transaccion= new Transaccion(new TcManticHistorialIvaDto(seleccionado.getKey()));
			if(transaccion.ejecutar(EAccion.ELIMINAR))
				JsfBase.addMessage("Eliminar", "El servicio se ha eliminado correctamente.", ETipoMensaje.ERROR);
			else
				JsfBase.addMessage("Eliminar", "Ocurrió un error al eliminar el registro del servicio.", ETipoMensaje.ERROR);								
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
			if(!Cadena.isVacio(this.attrs.get("idEncargo")))
  		  sb.append("(tc_mantic_encargos.id_encargo=").append(this.attrs.get("idEncargo")).append(") and ");
			if(!Cadena.isVacio(this.attrs.get("codigo")))
				sb.append("upper(tc_mantic_encargos.codigo) like upper('%").append((String)this.attrs.get("codigo")).append("%') and ");						
			if(!Cadena.isVacio(this.attrs.get("nombre")))
				sb.append("upper(tc_mantic_encargos.nombre) like upper('%").append((String)this.attrs.get("nombre")).append("%') and ");						
			if(!Cadena.isVacio(this.attrs.get("linea")))
				sb.append("upper(tc_mantic_encargos.linea) like upper('%").append((String)this.attrs.get("linea")).append("%') and ");						
			if(!Cadena.isVacio(this.attrs.get("idVigente")))
  		  sb.append("(tc_mantic_encargos.id_vigente=").append(this.attrs.get("idVigente")).append(") and ");
			if(!Cadena.isVacio(this.attrs.get("idDescontinuado")))
  		  sb.append("(tc_mantic_encargos.id_descontinuado=").append(this.attrs.get("idDescontinuado")).append(") and ");
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