package mx.org.kaana.kalan.catalogos.pacientes.citas.backing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
import mx.org.kaana.libs.pagina.IBaseFilter;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;

@Named(value = "kalanCatalogosPacientesCitasClientes")
@ViewScoped
public class Clientes extends IBaseFilter implements Serializable {

  private static final long serialVersionUID = 8793667741599428879L;
  
  private Entity seleccionado;  

  @PostConstruct
  @Override
  protected void init() {
    try {
      this.attrs.put("idClienteProcess", JsfBase.getFlashAttribute("idClienteProcess"));
      this.doLoad();
      this.attrs.put("idClienteProcess", null);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

	public Entity getSeleccionado() {
		return seleccionado;
	}

	public void setSeleccionado(Entity seleccionado) {
		this.seleccionado = seleccionado;
	}	
  
  @Override
  public void doLoad() {
    List<Columna> columns    = new ArrayList<>();		
		Map<String, Object>params= this.toPrepare();
    try {
      columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));      
      columns.add(new Columna("rfc", EFormatoDinamicos.MAYUSCULAS));      
      columns.add(new Columna("cliente", EFormatoDinamicos.MAYUSCULAS));      
      columns.add(new Columna("domicilio", EFormatoDinamicos.MAYUSCULAS));      
      this.lazyModel = new FormatCustomLazy("VistaClientesCitasDto", "clientes", params, columns);
      UIBackingUtilities.resetDataGrid();
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
    // concat(tc_mantic_clientes.razon_social, ' ', ifnull(tc_mantic_clientes.paterno, ''), ' ', ifnull(tc_mantic_clientes.materno, '')) regexp '.*{codigo}.*'
    // (upper(concat(tc_mantic_clientes.razon_social, ' ', ifnull(tc_mantic_clientes.paterno, ''), ' ', ifnull(tc_mantic_clientes.materno, ''))) regexp '.*{codigo}.*' or upper(rfc) regexp '.*{codigo}.*')
		Map<String, Object> regresar= new HashMap<>();
    StringBuilder sb            = new StringBuilder();
		UISelectEntity cliente      = null;
		List<UISelectEntity>clientes= null;
		try {
			cliente = (UISelectEntity)this.attrs.get("cliente");
			clientes= (List<UISelectEntity>)this.attrs.get("clientes");
			if(!Cadena.isVacio(this.attrs.get("idCliente"))) 
        sb.append("(tc_mantic_clientes.id_cliente= ").append(this.attrs.get("idCliente")).append(") and");
			if(clientes!= null && cliente!= null && clientes.indexOf(cliente)>= 0) 
        sb.append("(tc_mantic_clientes.id_cliente= ").append(clientes.get(clientes.indexOf(cliente)).toLong("idCliente")).append(") and");
			else 
				if(!Cadena.isVacio(JsfBase.getParametro("razonSocial_input"))) {
          String codigo= JsfBase.getParametro("razonSocial_input").replaceAll(Constantes.CLEAN_SQL, "").trim().replaceAll("(,| |\\t)+", ".*.*");
          sb.append("(upper(concat(tc_mantic_clientes.razon_social, ' ', ifnull(tc_mantic_clientes.paterno, ''), ' ', ifnull(tc_mantic_clientes.materno, ''))) regexp '.*").append(codigo).append(".*' or upper(rfc) regexp '.*").append(codigo).append(".*') and");
        } // if  
			regresar.put("sucursales", JsfBase.getAutentifica().getEmpresa().getDependencias());			
			if(Objects.equals(sb.length(), 0))
        regresar.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
      else
        regresar.put(Constantes.SQL_CONDICION, sb.substring(0, sb.length()- 4));
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
			JsfBase.setFlashAttribute("accion", eaccion);		
			JsfBase.setFlashAttribute("idCliente", (eaccion.equals(EAccion.MODIFICAR) || eaccion.equals(EAccion.CONSULTAR)) ? ((Entity)this.attrs.get("seleccionado")).getKey() : -1L);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch
		return "accion".concat(Constantes.REDIRECIONAR);
  } // doAccion

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
				codigo= codigo.toUpperCase().replaceAll("(,| |\\t)+", ".*.*");
			} // if	
			else
				codigo= "WXYZ";
  		params.put("codigo", codigo);
      this.attrs.put("clientes", UIEntity.build("TcManticClientesDto", "porNombre", params, columns, 40L));
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

  public String doAgendar() {
    String regresar= null;
    try {
			JsfBase.setFlashAttribute("idCliente", this.seleccionado.getKey());
			JsfBase.setFlashAttribute("retorno", "/Paginas/Kalan/Catalogos/Pacientes/Citas/clientes.jsf");
			regresar= "agendar".concat(Constantes.REDIRECIONAR);			
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
    return regresar;
  }
  
  public String doCitas() {
    String regresar= null;
    try {
			JsfBase.setFlashAttribute("idCliente", this.seleccionado.getKey());
			JsfBase.setFlashAttribute("retorno", "/Paginas/Kalan/Catalogos/Pacientes/Citas/clientes.jsf");
			regresar= "citas".concat(Constantes.REDIRECIONAR);			
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
    return regresar;
  }
  
}
