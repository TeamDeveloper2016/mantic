package mx.org.kaana.mantic.solicitudes.backing;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.mantic.catalogos.almacenes.transferencias.backing.Normal;
import mx.org.kaana.mantic.catalogos.almacenes.transferencias.beans.Transferencia;
import mx.org.kaana.mantic.catalogos.almacenes.transferencias.reglas.Transaccion;
import mx.org.kaana.mantic.comun.IBaseStorage;
import mx.org.kaana.mantic.db.dto.TcManticTransferenciasDto;
import mx.org.kaana.mantic.solicitudes.beans.Persona;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.primefaces.event.SelectEvent;

@Named(value= "manticSolicitudesSolicitud")
@ViewScoped
public class Solicitud extends Normal implements IBaseStorage, Serializable {

	private static final Log LOG              = LogFactory.getLog(Solicitud.class);
  private static final long serialVersionUID= 127393418565639367L;
	private Persona persona;

  public Persona getPersona() {
    return persona;
  }

  public void setPersona(Persona persona) {
    this.persona = persona;
  }
  
  public Boolean getEdit() {
    return Objects.equals(((Transferencia)this.getAdminOrden().getOrden()).getIdTransferenciaEstatus(), 1L); // INTEGRANDO
  }
  
  public Transferencia getTransferencia() {
    return (Transferencia)this.getAdminOrden().getOrden();
  }

  @PostConstruct
  @Override
  protected void init() {		
    super.init();
    this.attrs.put("automatico", Boolean.TRUE);
    this.attrs.put("retorno", "filtro"); 
    this.attrs.put("notificar", 0);
    this.persona= new Persona();
  }  
  
	@Override
  public void doLoad() {
    try {
      super.doLoad();
      switch (this.accion) {
        case AGREGAR:											
          break;
        case MODIFICAR:			
        case CONSULTAR:											
          ((Transferencia)this.getAdminOrden().getOrden()).toLoadPersonas();
          break;
      } // switch
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } 
  
  public void doAdd() {  
    List<UISelectEntity> empleados= (List<UISelectEntity>)this.attrs.get("empleados");
    try {
      int index= empleados.indexOf(this.persona.getIkPersona());
      if(index>= 0)
        this.persona.setIkPersona(empleados.get(index));
      this.persona.setIdTransferencia(((Transferencia)this.getAdminOrden().getOrden()).getIdTransferencia());
      this.persona.setIdPersona(this.persona.getIkPersona().toLong("idPersona"));
      this.persona.setNombre(this.persona.getIkPersona().toString("nombre"));
      this.persona.setCelular(this.persona.getIkPersona().toString("celular"));
      this.persona.setIdUsuario(JsfBase.getIdUsuario());
      if(((Transferencia)this.getAdminOrden().getOrden()).add(this.persona))
        JsfBase.addMessage("Alerta", "La persona se encuentra en la lista !");
      this.persona= new Persona();
      this.attrs.put("notificar", ((Transferencia)this.getAdminOrden().getOrden()).getPersonas().size());
		} // try
		catch (Exception e) {			
      Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
  }

  public void doRemove(Persona persona) {  
    try {
      ((Transferencia)this.getAdminOrden().getOrden()).remove(persona);
      this.attrs.put("notificar", ((Transferencia)this.getAdminOrden().getOrden()).getPersonas().size());
		} // try
		catch (Exception e) {			
      Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
  }
  
  public void doRecover(Persona persona) {  
    try {
      ((Transferencia)this.getAdminOrden().getOrden()).recover(persona);
		} // try
		catch (Exception e) {			
      Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
  }
 
  public void doAddItem(SelectEvent event) {
    try {      
      this.persona.setIkPersona((UISelectEntity)event.getObject());
      if((Boolean)this.attrs.get("automatico"))
        this.doAdd();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);      
    } // catch	
  }  
 
  @Override
  public String doAceptar() {  
    Transaccion transaccion= null;
    String regresar        = null;
    try {			
      ((TcManticTransferenciasDto)this.getAdminOrden().getOrden()).setIdTransferenciaTipo(4L); // SOLICITUDES
			transaccion = new Transaccion((TcManticTransferenciasDto)this.getAdminOrden().getOrden(), ((Transferencia)this.getAdminOrden().getOrden()).getPersonas(), this.getAdminOrden().getArticulos());
			this.getAdminOrden().toAdjustArticulos();
			if (transaccion.ejecutar(this.accion)) {
				if(this.accion.equals(EAccion.AGREGAR)) {
   			  UIBackingUtilities.execute("janal.back(' gener\\u00F3 la solicitud ', '"+ ((Transferencia)this.getAdminOrden().getOrden()).getConsecutivo()+ "');");
		  		JsfBase.addMessage("Se registró la solicitud de correcta", ETipoMensaje.INFORMACION);
				} // if	
 				if(!this.accion.equals(EAccion.CONSULTAR)) 
    			JsfBase.addMessage("Se ".concat(this.accion.equals(EAccion.AGREGAR) ? "agregó" : "modificó").concat(" la solicitud de articulos"), ETipoMensaje.INFORMACION);
			  regresar= this.doCancelar();
			} // if
			else 
				JsfBase.addMessage("Ocurrió un error al registrar la solicitud de articulos", ETipoMensaje.ALERTA);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    return regresar;
  } 
  
}