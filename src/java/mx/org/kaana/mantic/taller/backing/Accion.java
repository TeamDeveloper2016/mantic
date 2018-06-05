package mx.org.kaana.mantic.taller.backing;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.mantic.taller.reglas.Transaccion;
import mx.org.kaana.mantic.taller.beans.RegistroServicio;

@Named(value = "manticCatalogosClientesAccion")
@ViewScoped
public class Accion extends IBaseAttribute implements Serializable {

  private static final long serialVersionUID = -7668104942302148046L;
  private RegistroServicio registroServicio;

	public RegistroServicio getRegistroServicio() {
		return registroServicio;
	}

	public void setRegistroServicio(RegistroServicio registroServicio) {
		this.registroServicio = registroServicio;
	}  
	
  @PostConstruct
  @Override
  protected void init() {
    try {
      this.attrs.put("accion", JsfBase.getFlashAttribute("accion"));
      this.attrs.put("idServicio", JsfBase.getFlashAttribute("idCliente"));
			this.attrs.put("admin", JsfBase.isAdminEncuestaOrAdmin());
      doLoad();      					
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

	private void loadCollections(){
		
	}
	
  public void doLoad() {
    EAccion eaccion = null;
    Long idServicio = -1L;
    try {
      eaccion = (EAccion) this.attrs.get("accion");
      this.attrs.put("nombreAccion", Cadena.letraCapital(eaccion.name()));
      switch (eaccion) {
        case AGREGAR:
          this.registroServicio = new RegistroServicio();
					loadCollections();
          break;
        case MODIFICAR:
        case CONSULTAR:
          idServicio = Long.valueOf(this.attrs.get("idServicio").toString());
          this.registroServicio = new RegistroServicio(idServicio);
					loadCollections();					
          break;
      } // switch 			
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // doLoad

  public String doAceptar() {
    Transaccion transaccion = null;
    String regresar = null;
    try {
      transaccion = new Transaccion(this.registroServicio);
      if (transaccion.ejecutar((EAccion) this.attrs.get("accion"))) {
        regresar = "filtro".concat(Constantes.REDIRECIONAR);
        JsfBase.addMessage("Se registro el servicio de taller de forma correcta.", ETipoMensaje.INFORMACION);
      } // if
      else {
        JsfBase.addMessage("Ocurrió un error al registrar el registro de taller", ETipoMensaje.ERROR);
      }
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    return regresar;
  } // doAccion

  public String doCancelar() {
    return "filtro".concat(Constantes.REDIRECIONAR);
  } // doAccion  
}
