package mx.org.kaana.kalan.catalogos.pacientes.expedientes.backing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import mx.org.kaana.kalan.catalogos.pacientes.expedientes.reglas.Transaccion;
import mx.org.kaana.kalan.db.dto.TcKalanDiagnosticosDto;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.pagina.IBaseFilter;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;
import org.primefaces.event.TabChangeEvent;

@Named(value = "kalanCatalogosPacientesExpedientesDiagnostico")
@ViewScoped
public class Diagnostico extends IBaseFilter implements Serializable {

  private static final long serialVersionUID = 8793667711599428879L;
  private Entity cliente;
  private TcKalanDiagnosticosDto diagnostico;

  public Entity getCliente() {
    return cliente;
  }

  public void setCliente(Entity cliente) {
    this.cliente = cliente;
  }

  public TcKalanDiagnosticosDto getDiagnostico() {
    return diagnostico;
  }

  public void setDiagnostico(TcKalanDiagnosticosDto diagnostico) {
    this.diagnostico = diagnostico;
  }
  
  @PostConstruct
  @Override
  protected void init() {
    try {
			if(JsfBase.getFlashAttribute("idCliente")== null)
				UIBackingUtilities.execute("janal.isPostBack('cancelar')");
      this.attrs.put("idCita", JsfBase.getFlashAttribute("idCita"));
      this.attrs.put("idCliente", JsfBase.getFlashAttribute("idCliente"));
//      this.attrs.put("idCita", 1L);
//      this.attrs.put("idCliente", 1L);
      this.attrs.put("retorno", JsfBase.getFlashAttribute("retorno")== null? "/Paginas/Kalan/Catalogos/Pacientes/Citas/clientes": JsfBase.getFlashAttribute("retorno"));
      this.toLoadCliente();
      this.toLoadCitas();
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
      params.put("sortOrder", "order by tc_kalan_citas.registro desc");
      columns.add(new Columna("inicio", EFormatoDinamicos.FECHA_HORA_CORTA));    
      columns.add(new Columna("termino", EFormatoDinamicos.FECHA_HORA_CORTA));    
      this.lazyModel = new FormatCustomLazy("VistaCitasDiagnosticosDto", params, columns);
      UIBackingUtilities.resetDataTable("contenedorGrupos:tabla");
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
		try {
      regresar.put("idCliente", this.attrs.get("idCliente"));
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
	
  public String doAccion() {
		Transaccion transaccion= null;
		try {
      this.diagnostico.setIdCita(((UISelectEntity)this.attrs.get("ikCita")).getKey());
      transaccion= new Transaccion(this.diagnostico);
      if(transaccion.ejecutar(EAccion.ACTIVAR)) {
        JsfBase.addMessage("Documento", "Se actualizó la información del diagnostico !", ETipoMensaje.INFORMACION);
        this.doLoadDiagnostico();
        UIBackingUtilities.execute("janal.refresh();");
      } // if
      else
        JsfBase.addMessage("Documento", "Ocurrio un error al actualizar la información del diagnostico, intente de nueva cuenta !", ETipoMensaje.INFORMACION);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch
		return null;
  } // doAccion

  public String doCancelar() {
    String regresar= null;
    try {
			JsfBase.setFlashAttribute("idCita", this.attrs.get("idCita"));
			JsfBase.setFlashAttribute("idCliente", this.attrs.get("idCliente"));
			JsfBase.setFlashAttribute("idClienteProcess", this.attrs.get("idCliente"));
			regresar= ((String)this.attrs.get("retorno")).concat(Constantes.REDIRECIONAR);			
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
    return regresar;
  }
  
  private void toLoadCliente() {
    List<Columna> columns    = new ArrayList<>();	
		Map<String, Object>params= new HashMap<>();
    try {
      params.put("idCliente", this.attrs.get("idCliente"));
			params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getDependencias());			
      params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
      columns.add(new Columna("inicio", EFormatoDinamicos.DIA_FECHA_HORA_CORTA));    
      this.cliente= (Entity)DaoFactory.getInstance().toEntity("VistaClientesCitasDto", "clientes", params);
      if(this.cliente!= null && !this.cliente.isEmpty())
        UIBackingUtilities.toFormatEntity(this.cliente, columns);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    } // finally		
  } // toLoadCliente
  
  public void doLoadDiagnostico() {
		List<Columna> columns     = new ArrayList<>();
    Map<String, Object> params= new HashMap<>();
    try {
      if(!Objects.equals(this.attrs.get("ikCita"), null) && !Objects.equals(((UISelectEntity)this.attrs.get("ikCita")).getKey(), -1L)) {
        this.diagnostico= (TcKalanDiagnosticosDto)DaoFactory.getInstance().findById(TcKalanDiagnosticosDto.class, ((UISelectEntity)this.attrs.get("ikCita")).getKey());
        if(this.diagnostico== null)
          this.diagnostico= new TcKalanDiagnosticosDto(); 
      } // if  
      else
        this.diagnostico= new TcKalanDiagnosticosDto(); 
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    } // finally
  }  
    
  private void toLoadCitas() throws Exception {
		List<Columna> columns     = new ArrayList<>();
    Map<String, Object> params= new HashMap<>();
    try {
      Calendar inicio = Calendar.getInstance();
      inicio.add(Calendar.DATE, -360);
      params.put("idCliente", this.attrs.get("idCliente"));
      params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getDependencias());			
      params.put(Constantes.SQL_CONDICION, "(date_format(tc_kalan_citas.inicio, '%Y%m%d')>= '".concat(Fecha.formatear(Fecha.FECHA_ESTANDAR, inicio)).concat("')"));
      params.put("sortOrder", "order by tc_kalan_citas.inicio desc");
      columns.add(new Columna("inicio", EFormatoDinamicos.DIA_FECHA_HORA_CORTA));    
      columns.add(new Columna("termino", EFormatoDinamicos.DIA_FECHA_HORA_CORTA));    
      columns.add(new Columna("servicios", EFormatoDinamicos.MAYUSCULAS));    
      List<UISelectEntity> citas= (List<UISelectEntity>) UIEntity.seleccione("VistaClientesCitasDto", "lazy", params, columns, "consecutivo");
      this.attrs.put("citas", citas);    
      if(citas!= null && !citas.isEmpty()) {
        if(!Objects.equals(this.attrs.get("idCita"), null) && !Objects.equals((Long)this.attrs.get("idCita"), -1L)) {
          int index= citas.indexOf(new UISelectEntity((Long)this.attrs.get("idCita")));
          if(index>= 0)
            this.attrs.put("ikCita", citas.get(index));    
          else
            this.attrs.put("ikCita", UIBackingUtilities.toFirstKeySelectEntity(citas));    
        } // if
        else
          this.attrs.put("ikCita", UIBackingUtilities.toFirstKeySelectEntity(citas));    
      } // if  
      else
        this.attrs.put("ikCita", new UISelectEntity(-1L));    
      this.doLoadDiagnostico();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    } // finally
  }  

	public void doTabChange(TabChangeEvent event) {
		if(event.getTab().getTitle().equals("Historial")) 
			this.doLoad();
	}	// doTabChange	
  
}
