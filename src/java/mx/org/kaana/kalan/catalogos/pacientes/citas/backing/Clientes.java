package mx.org.kaana.kalan.catalogos.pacientes.citas.backing;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
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
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.pagina.IBaseFilter;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.primefaces.event.TabChangeEvent;

@Named(value = "kalanCatalogosPacientesCitasClientes")
@ViewScoped
public class Clientes extends IBaseFilter implements Serializable {

  private static final Log LOG = LogFactory.getLog(Clientes.class);
  private static final long serialVersionUID = 8793667741599428879L;
  
  private Integer idCriterio;
  private Entity seleccionado;  

  @PostConstruct
  @Override
  protected void init() {
    try {
      this.idCriterio= 0;
      this.attrs.put("idCliente", JsfBase.getFlashAttribute("idClienteProcess"));
      this.attrs.put("idClienteProcess", JsfBase.getFlashAttribute("idClienteProcess"));
      this.doLoad();
      this.attrs.put("idClienteProcess", null);
      Calendar inicio = Calendar.getInstance();
      inicio.add(Calendar.YEAR, -1);
      this.attrs.put("inicio", new Date(inicio.getTimeInMillis()));
			this.attrs.put("termino", new Date(Calendar.getInstance().getTimeInMillis()));
 			this.attrs.put("idServicio", new Object[] {});
      this.toLoadServicios();
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
      params.put("idCliente", -1L);
      columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));      
      columns.add(new Columna("rfc", EFormatoDinamicos.MAYUSCULAS));      
      columns.add(new Columna("cliente", EFormatoDinamicos.MAYUSCULAS));      
      columns.add(new Columna("inicio", EFormatoDinamicos.DIA_FECHA_HORA_CORTA));      
      columns.add(new Columna("servicios", EFormatoDinamicos.MAYUSCULAS));      
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
		Map<String, Object> regresar= new HashMap<>();
    StringBuilder sb            = new StringBuilder();
		UISelectEntity cliente      = null;
		List<UISelectEntity>clientes= null;
		try {
			cliente = (UISelectEntity)this.attrs.get("cliente");
			clientes= (List<UISelectEntity>)this.attrs.get("clientes");
			if(!Cadena.isVacio(this.attrs.get("idClienteProcess"))) 
        sb.append("(tc_mantic_clientes.id_cliente= ").append(this.attrs.get("idClienteProcess")).append(") and");
      switch(this.idCriterio) {
        case 0: // BUSCAR POR CLIENTE
          if(clientes!= null && cliente!= null && clientes.indexOf(cliente)>= 0) 
            sb.append("(tc_mantic_clientes.id_cliente= ").append(clientes.get(clientes.indexOf(cliente)).toLong("idCliente")).append(") and");
          else 
            if(!Cadena.isVacio(JsfBase.getParametro("contenedorGrupos:razonSocial_input"))) {
              String codigo= JsfBase.getParametro("contenedorGrupos:razonSocial_input").replaceAll(Constantes.CLEAN_SQL, "").trim().replaceAll("(,| |\\t)+", ".*.*");
              sb.append("(upper(concat(tc_mantic_clientes.razon_social, ' ', ifnull(tc_mantic_clientes.paterno, ''), ' ', ifnull(tc_mantic_clientes.materno, ''))) regexp '.*").append(codigo).append(".*' or upper(tc_mantic_clientes.rfc) regexp '.*").append(codigo).append(".*') and");
            } // if  
          break;  
        case 1: // BUSCAR POR SERVICIO
          Object[] idServicio= (Object[])this.attrs.get("idServicio");
          if(idServicio.length> 0) {
            StringBuilder servicios= new StringBuilder();
            for (Object item: idServicio) {
              servicios.append(((UISelectEntity)item).getKey()).append(",");
            } // for
            sb.append("(tc_kalan_citas_detalles.id_articulo in (").append(servicios.substring(0, servicios.length()-1)).append(")) and ");
          } // if  
          break;  
        case 2: // BUSCAR POR PERIODO
          sb.append("(date_format(tc_kalan_citas.inicio, '%Y%m%d')>= '").append(Fecha.formatear(Fecha.FECHA_ESTANDAR, (Date)this.attrs.get("inicio"))).append("') and ");
          sb.append("(date_format(tc_kalan_citas.inicio, '%Y%m%d')<= '").append(Fecha.formatear(Fecha.FECHA_ESTANDAR, (Date)this.attrs.get("termino"))).append("') and ");
          break;  
        case 3: // BUSCAR POR CUMPLEAÑOS
          sb.append("(substring(tc_mantic_clientes.rfc, 6, 2)= '").append((String)this.attrs.get("idMes")).append("' or substring(tc_mantic_clientes.rfc, 7, 2)= '").append((String)this.attrs.get("idMes")).append("') and ");
          break;  
      } // swtich  
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
  		params.put("idCliente", -1L);
  		params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
			if(!Cadena.isVacio(codigo)) {
  			codigo= codigo.replaceAll(Constantes.CLEAN_SQL, "").trim();
				codigo= codigo.toUpperCase().replaceAll("(,| |\\t)+", ".*.*");
			} // if	
			else
				codigo= "WXYZ";
  		params.put(Constantes.SQL_CONDICION, "(upper(concat(tc_mantic_clientes.razon_social, ' ', ifnull(tc_mantic_clientes.paterno, ''), ' ', ifnull(tc_mantic_clientes.materno, ''))) regexp '.*".concat(codigo).concat(".*' or upper(tc_mantic_clientes.rfc) regexp '.*").concat(codigo).concat(".*')"));
      this.attrs.put("clientes", UIEntity.build("VistaClientesCitasDto", "clientes", params, columns, 40L));
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
			JsfBase.setFlashAttribute("accion", EAccion.AGREGAR);		
			JsfBase.setFlashAttribute("idCliente", this.seleccionado.getKey());
			JsfBase.setFlashAttribute("fecha", new Timestamp(Calendar.getInstance().getTimeInMillis()));		
			JsfBase.setFlashAttribute("retorno", "/Paginas/Kalan/Catalogos/Pacientes/Citas/clientes.jsf");
			regresar= "nuevo".concat(Constantes.REDIRECIONAR);			
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

  public String doExpediente() {
    String regresar= null;
    try {
			JsfBase.setFlashAttribute("accion", EAccion.AGREGAR);		
			JsfBase.setFlashAttribute("idCliente", this.seleccionado.getKey());
			JsfBase.setFlashAttribute("retorno", "/Paginas/Kalan/Catalogos/Pacientes/Citas/clientes.jsf");
			regresar= "/Paginas/Kalan/Catalogos/Pacientes/Expedientes/importar".concat(Constantes.REDIRECIONAR);			
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
    return regresar;
  }
  
  public String doGaleria() {
    String regresar= null;
    try {
			JsfBase.setFlashAttribute("accion", EAccion.AGREGAR);		
			JsfBase.setFlashAttribute("idCliente", this.seleccionado.getKey());
			JsfBase.setFlashAttribute("retorno", "/Paginas/Kalan/Catalogos/Pacientes/Citas/clientes.jsf");
			regresar= "/Paginas/Kalan/Catalogos/Pacientes/Expedientes/galeria".concat(Constantes.REDIRECIONAR);			
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
    return regresar;
  }
  
  public String doDiagnostico() {
    String regresar= null;
    try {
			JsfBase.setFlashAttribute("accion", EAccion.AGREGAR);		
			JsfBase.setFlashAttribute("idCita", -1L);
			JsfBase.setFlashAttribute("idCliente", this.seleccionado.toLong("idCliente"));
			JsfBase.setFlashAttribute("retorno", "/Paginas/Kalan/Catalogos/Pacientes/Citas/clientes.jsf");
			regresar= "/Paginas/Kalan/Catalogos/Pacientes/Expedientes/diagnostico".concat(Constantes.REDIRECIONAR);			
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
    return regresar;
  }

	public void doTabChange(TabChangeEvent event) {
    switch(event.getTab().getTitle()) {
      case "Cliente":
    		this.idCriterio= 0;
        break;
      case "Servicio(s)":
    		this.idCriterio= 1;
        break;
      case "Periodo":
    		this.idCriterio= 2;
        break;
      case "Cumpleaños":
    		this.idCriterio= 3;
        break;
    } // switch
  }  
 
  public void doUpdateInicio() {
    Calendar inicio = Calendar.getInstance();
    Calendar termino= Calendar.getInstance();
    inicio.setTimeInMillis(((Date)this.attrs.get("inicio")).getTime());
    termino.setTimeInMillis(((Date)this.attrs.get("termino")).getTime());
    if(inicio.after(termino)) {
      this.attrs.put("inicio", new Date(termino.getTimeInMillis()));
      this.attrs.put("termino", new Date(inicio.getTimeInMillis()));
    } // if  
  }
          
  public void doUpdateTermino() {
    Calendar inicio = Calendar.getInstance();
    Calendar termino= Calendar.getInstance();
    inicio.setTimeInMillis(((Date)this.attrs.get("inicio")).getTime());
    termino.setTimeInMillis(((Date)this.attrs.get("termino")).getTime());
    if(termino.before(inicio)) {
      this.attrs.put("inicio", new Date(termino.getTimeInMillis()));
      this.attrs.put("termino", new Date(inicio.getTimeInMillis()));
    } // if  
  }

  public void doEraseServicios() {
    LOG.info("doEraseServicios()");
  }

  private void toLoadServicios() {
    List<Columna> columns     = new ArrayList<>();    
    Map<String, Object> params= new HashMap<>();
    try {      
			params.put("idArticuloTipo", "4");			
      columns.add(new Columna("codigo", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      this.attrs.put("servicios", UIEntity.build("VistaClientesCitasDto", "servicios", params, columns));
    } // try
    catch (Exception e) {
			throw e;
    } // catch	
    finally {
      Methods.clean(params);
      Methods.clean(columns);
    } // finally        
  }
  
}
