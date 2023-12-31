package mx.org.kaana.kalan.catalogos.pacientes.citas.backing;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
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
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;

@Named(value = "kalanCatalogosPacientesCitasFiltro")
@ViewScoped
public class Filtro extends IBaseFilter implements Serializable {

  private static final long serialVersionUID = 8793667741599428879L;

  @PostConstruct
  @Override
  protected void init() {
    try {
      this.attrs.put("fecha", new Date(Calendar.getInstance().getTimeInMillis()));     
      if(JsfBase.getFlashAttribute("idClienteProcess")!= null) {
        this.attrs.put("idClienteProcess", JsfBase.getFlashAttribute("idClienteProcess"));
        this.doLoad();
        this.attrs.put("idClienteProcess", null);
      } // if
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

  @Override
  public void doLoad() {
    List<Columna> columns    = new ArrayList<>();		
		Map<String, Object>params= this.toPrepare();
    try {
      columns.add(new Columna("cliente", EFormatoDinamicos.MAYUSCULAS));      
      columns.add(new Columna("inicio", EFormatoDinamicos.FECHA_HORA));    
      columns.add(new Columna("motivo", EFormatoDinamicos.MAYUSCULAS));    
      columns.add(new Columna("estatus", EFormatoDinamicos.MAYUSCULAS));    
      params.put("sortOrder", "order by tc_mantic_clientes.razon_social, tc_mantic_clientes.paterno, tc_mantic_clientes.materno");
			params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getDependencias());			
      this.lazyModel = new FormatCustomLazy("VistaClientesCitasDto", params, columns);
      UIBackingUtilities.resetDataTable();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    } // finally		
  } // doLoad

	private Map<String, Object> toPrepare() {
		Map<String, Object> regresar= new HashMap();
		StringBuilder sb            = null;
		UISelectEntity cliente      = null;
		List<UISelectEntity>clientes= null;
		try {
			sb      = new StringBuilder("");
			cliente = (UISelectEntity)this.attrs.get("cliente");
			clientes= (List<UISelectEntity>)this.attrs.get("clientes");
  		if(!Cadena.isVacio(this.attrs.get("fecha")))
	  	  sb.append("(date_format(tc_kalan_citas.cuando, '%Y%m%d')= '").append(Fecha.formatear(Fecha.FECHA_ESTANDAR, (Date)this.attrs.get("fecha"))).append("') and ");	
			if(clientes!= null && cliente!= null && clientes.indexOf(cliente)>= 0) 
				sb.append("concat(tc_mantic_clientes.razon_social, ' ', ifnull(tc_mantic_clientes.paterno, ''), ' ', ifnull(tc_mantic_clientes.materno, '')) regexp '.*").append(clientes.get(clientes.indexOf(cliente)).toString("razonSocial").replaceAll(Constantes.CLEAN_SQL, "").trim().replaceAll("(,| |\\t)+", ".*")).append(".*' and ");				
			else 
				if(!Cadena.isVacio(JsfBase.getParametro("razonSocial_input"))) 
					sb.append("concat(tc_mantic_clientes.razon_social, ' ', ifnull(tc_mantic_clientes.paterno, ''), ' ', ifnull(tc_mantic_clientes.materno, '')) regexp '.*").append(JsfBase.getParametro("razonSocial_input").replaceAll(Constantes.CLEAN_SQL, "").trim().replaceAll("(,| |\\t)+", ".*")).append(".*' and ");				
			if(Cadena.isVacio(sb))
				sb.append(Constantes.SQL_VERDADERO);
			else
				sb.delete(sb.length()- 4, sb.length());
			regresar.put(Constantes.SQL_CONDICION, sb.toString());
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toPrepare
	
  public String doAccion(String accion) {
    EAccion eaccion= null;
		try {
			eaccion= EAccion.valueOf(accion.toUpperCase());
			JsfBase.setFlashAttribute("puntoVenta", this.attrs.get("puntoVenta"));		
			JsfBase.setFlashAttribute("accion", eaccion);		
			JsfBase.setFlashAttribute("idCliente", (eaccion.equals(EAccion.MODIFICAR) || eaccion.equals(EAccion.CONSULTAR)) ? ((Entity)this.attrs.get("seleccionado")).getKey() : -1L);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch
		return "accion".concat(Constantes.REDIRECIONAR);
  } // doAccion

  public void doEliminar() {
		Entity seleccionado= null;
		try {
			seleccionado= (Entity) this.attrs.get("seleccionado");			
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch			
  } // doEliminar
	
	public List<UISelectEntity> doCompleteCliente(String codigo) {
 		List<Columna> columns     = new ArrayList<>();
    Map<String, Object> params= new HashMap<>();
    try {
      columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("rfc", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("razonSocial", EFormatoDinamicos.MAYUSCULAS));
  		params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
			if(!Cadena.isVacio(codigo)) {
  			codigo= codigo.replaceAll(Constantes.CLEAN_SQL, "").trim();
				codigo= codigo.toUpperCase().replaceAll("(,| |\\t)+", ".*");
			} // if	
			else
				codigo= "WXYZ";
  		params.put("codigo", codigo);
      this.attrs.put("clientes", UIEntity.build("VistaClientesCitasDto", "nombre", params, columns, 40L));
		} // try
	  catch (Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    }// finally
		return (List<UISelectEntity>)this.attrs.get("clientes");
	}	
	
}
