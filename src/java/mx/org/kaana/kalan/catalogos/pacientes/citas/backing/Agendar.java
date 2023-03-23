package mx.org.kaana.kalan.catalogos.pacientes.citas.backing;

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
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import mx.org.kaana.kalan.catalogos.pacientes.citas.beans.Paciente;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.pagina.IBaseFilter;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


@Named(value = "kalanCatalogosPacientesCitasAgendar")
@ViewScoped
public class Agendar extends IBaseFilter implements Serializable {

  private static final Log LOG = LogFactory.getLog(Agendar.class);
  private static final long serialVersionUID = 327393488565639267L;  
	
  private EAccion accion;
  private Paciente paciente;
	private List<Entity> seleccionados;

  public Paciente getPaciente() {
    return paciente;
  }

  public void setPaciente(Paciente paciente) {
    this.paciente = paciente;
  }
          
	public List<Entity> getSeleccionados() {
		return seleccionados;
	}

	public void setSeleccionados(List<Entity> seleccionados) {
		this.seleccionados = seleccionados;
	}	
  
  @PostConstruct
  @Override
  protected void init() {
    try {
      this.accion= JsfBase.getFlashAttribute("accion")== null? EAccion.AGREGAR: (EAccion)JsfBase.getFlashAttribute("accion");
      this.attrs.put("idCliente", JsfBase.getFlashAttribute("idCliente")== null? -1L: JsfBase.getFlashAttribute("idCliente"));
      this.attrs.put("retorno", JsfBase.getFlashAttribute("retorno")== null? "clientes": JsfBase.getFlashAttribute("retorno"));
      this.seleccionados= new ArrayList<>();      
      this.doLoad();   
      this.toLoadPersonal();
      this.toLoadServicios();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

  @Override
  public void doLoad() {
    try {
      this.attrs.put("nombreAccion", Cadena.letraCapital(this.accion.name()));
      switch (this.accion) {
        case AGREGAR:
          this.paciente= new Paciente();
          break;
        case MODIFICAR:
        case CONSULTAR:
          this.paciente= new Paciente();
          break;
      } // switch      
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // doLoad

	private void toLoadPersonal() {
		List<Columna> columns        = new ArrayList<>();    
    Map<String, Object> params   = new HashMap<>();
    List<UISelectEntity> personal= null;
    try {      
			params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getDependencias());			
      columns.add(new Columna("empleado", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("correo", EFormatoDinamicos.MAYUSCULAS));
      personal= (List<UISelectEntity>) UIEntity.seleccione("VistaClientesCitasDto", "personal", params, columns, "empleado");
			this.attrs.put("personal", personal);
      if(personal!= null && !personal.isEmpty()) 
        this.paciente.setIkAtendio(personal.get(0));
    } // try
    catch (Exception e) {
			throw e;
    } // catch	
    finally {
      Methods.clean(params);
      Methods.clean(columns);
    } // finally    
	}
	  
  private void toLoadServicios() {
		List<Columna> columns     = new ArrayList<>();    
    Map<String, Object> params= new HashMap<>();
    try {      
			params.put("idArticuloTipo", 4L);			
      columns.add(new Columna("codigo", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      this.lazyModel = new FormatCustomLazy("VistaClientesCitasDto", "servicios", params, columns);
      UIBackingUtilities.resetDataTable();
    } // try
    catch (Exception e) {
			throw e;
    } // catch	
    finally {
      Methods.clean(params);
      Methods.clean(columns);
    } // finally    
  }
  
  public String doAceptar() {  
    String regresar = null;
    try {
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    return regresar;
  } // doAccion

  public String doCancelar() {   
    String regresar= null;
    try {
			JsfBase.setFlashAttribute("idClienteProcess", this.attrs.get("idCliente"));
			regresar= ((String)this.attrs.get("retorno")).concat(Constantes.REDIRECIONAR);			
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
    return regresar;
  } // doAccion

  public void doRowSeleccionado() {
    LOG.info(this.seleccionados.size());
  }
  
}