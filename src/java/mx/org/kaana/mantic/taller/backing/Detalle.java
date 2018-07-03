package mx.org.kaana.mantic.taller.backing;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.mantic.taller.beans.Servicio;
import mx.org.kaana.mantic.comun.IBaseArticulos;
import mx.org.kaana.mantic.taller.reglas.AdminServicios;
import org.primefaces.context.RequestContext;


@Named(value= "manticTallerDetalle")
@ViewScoped
public class Detalle extends IBaseArticulos implements Serializable {

  private static final long serialVersionUID = 327393488565639367L;
	private EAccion accion;

	public String getAgregar() {
		return this.accion.equals(EAccion.AGREGAR)? "none": "";
	}
	
	@PostConstruct
  @Override
  protected void init() {		
    try {
			//if(JsfBase.getFlashAttribute("accion")== null)
			//	RequestContext.getCurrentInstance().execute("janal.isPostBack('cancelar')");
      this.accion   = JsfBase.getFlashAttribute("accion")== null? EAccion.MODIFICAR: (EAccion)JsfBase.getFlashAttribute("accion");
      this.attrs.put("idServicio", JsfBase.getFlashAttribute("idServicio")== null? -1L: JsfBase.getFlashAttribute("idServicio"));
			this.attrs.put("retorno", JsfBase.getFlashAttribute("retorno")== null? "filtro": JsfBase.getFlashAttribute("retorno"));
      this.attrs.put("isPesos", false);
			this.attrs.put("sinIva", false);
			this.attrs.put("buscaPorCodigo", false);
			doLoad();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

  public void doLoad() {
    try {
      this.attrs.put("nombreAccion", Cadena.letraCapital(this.accion.name()));
      switch (this.accion) {
        case MODIFICAR:			
        case CONSULTAR:											
          this.setAdminOrden(new AdminServicios((Servicio)DaoFactory.getInstance().toEntity(Servicio.class, "TcManticServiciosDto", "detalle", this.attrs)));
          break;
      } // switch
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // doLoad

  public String doAceptar() {  
    try {			
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    return null;
  } // doAccion

  public String doCancelar() {   
  	JsfBase.setFlashAttribute("idServicio", this.attrs.get("idServicio"));
    return (String)this.attrs.get("retorno");
  } // doCancelar


}