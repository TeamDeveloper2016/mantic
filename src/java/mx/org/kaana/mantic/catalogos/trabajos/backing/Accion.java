package mx.org.kaana.mantic.catalogos.trabajos.backing;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EBooleanos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.mantic.catalogos.trabajos.reglas.Transaccion;
import mx.org.kaana.mantic.db.dto.TcManticTrabajosDto;

@Named(value = "manticCatalogosTrabajosAccion")
@ViewScoped
public class Accion extends IBaseAttribute implements Serializable {

  private static final long serialVersionUID = -7668104942302148046L;
  
  @PostConstruct
  @Override
  protected void init() {
    try {
      this.attrs.put("accion", JsfBase.getFlashAttribute("accion"));
      this.attrs.put("idTrabajo", JsfBase.getFlashAttribute("idTrabajo"));
			this.attrs.put("admin", JsfBase.isAdminEncuestaOrAdmin());
			this.attrs.put("vigente", false);
			this.attrs.put("isMatriz", false);
      doLoad();      					
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

	public void doLoad() {
    EAccion eaccion= null;
    Long idTrabajo = -1L;
    try {
      eaccion = (EAccion) this.attrs.get("accion");
      this.attrs.put("nombreAccion", Cadena.letraCapital(eaccion.name()));
      switch (eaccion) {
        case AGREGAR:
					this.attrs.put("trabajo", new TcManticTrabajosDto());          
          break;
        case MODIFICAR:
        case CONSULTAR:
          idTrabajo = Long.valueOf(this.attrs.get("idTrabajo").toString());
          this.attrs.put("trabajo", DaoFactory.getInstance().findById(TcManticTrabajosDto.class, idTrabajo));
					this.attrs.put("vigente", ((TcManticTrabajosDto)this.attrs.get("trabajo")).getIdVigente().equals(EBooleanos.SI.getIdBooleano()));
          break;
      } // switch 						
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // doLoad

  public String doAceptar() {
    Transaccion transaccion    = null;
		TcManticTrabajosDto trabajo= null;
    String regresar            = null;
    try {
			trabajo= (TcManticTrabajosDto) this.attrs.get("trabajo");
			trabajo.setIdVigente(Boolean.valueOf(this.attrs.get("vigente").toString()) ? EBooleanos.SI.getIdBooleano() : EBooleanos.NO.getIdBooleano());
			trabajo.setIdEmpresa(JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
      transaccion = new Transaccion(trabajo);
      if (transaccion.ejecutar((EAccion) this.attrs.get("accion"))) {
        regresar = "filtro".concat(Constantes.REDIRECIONAR);
        JsfBase.addMessage("Se registro el trabajo de forma correcta.", ETipoMensaje.INFORMACION);
      } // if
      else 
        JsfBase.addMessage("Ocurrió un error al registrar el trabajo.", ETipoMensaje.ERROR);      
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
