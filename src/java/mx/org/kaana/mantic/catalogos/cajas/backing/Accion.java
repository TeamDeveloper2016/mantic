package mx.org.kaana.mantic.catalogos.cajas.backing;

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
import mx.org.kaana.mantic.catalogos.cajas.reglas.Transaccion;
import mx.org.kaana.mantic.db.dto.TcManticCajasDto;

@Named(value = "manticCatalogosCajasAccion")
@ViewScoped
public class Accion extends IBaseAttribute implements Serializable {

  private static final long serialVersionUID = 8635741239546845369L;
  
  @PostConstruct
  @Override
  protected void init() {
    try {
      this.attrs.put("accion", JsfBase.getFlashAttribute("accion"));
      this.attrs.put("idCaja", JsfBase.getFlashAttribute("idCaja"));
			this.attrs.put("activa", false);
      doLoad();      					
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

	public void doLoad() {
    EAccion eaccion = null;
    Long idCaja     = -1L;
    try {
      eaccion = (EAccion) this.attrs.get("accion");
      this.attrs.put("nombreAccion", Cadena.letraCapital(eaccion.name()));
      switch (eaccion) {
        case AGREGAR:
					this.attrs.put("caja", new TcManticCajasDto());          
          break;
        case MODIFICAR:
        case CONSULTAR:
          idCaja = Long.valueOf(this.attrs.get("idCaja").toString());
          this.attrs.put("caja", DaoFactory.getInstance().findById(TcManticCajasDto.class, idCaja));
					this.attrs.put("activa", ((TcManticCajasDto)this.attrs.get("caja")).getIdActiva().equals(EBooleanos.SI.getIdBooleano()));
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
		TcManticCajasDto caja   = null;
    String regresar         = null;
    try {
			caja= (TcManticCajasDto) this.attrs.get("caja");
			caja.setIdActiva(Boolean.valueOf(this.attrs.get("activa").toString()) ? EBooleanos.SI.getIdBooleano() : EBooleanos.NO.getIdBooleano());
			caja.setIdEmpresa(JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
			caja.setIdUsuario(JsfBase.getAutentifica().getPersona().getIdUsuario());
      transaccion = new Transaccion(caja, caja.getIdCaja());
      if (transaccion.ejecutar((EAccion) this.attrs.get("accion"))) {
        regresar = "filtro".concat(Constantes.REDIRECIONAR);
        JsfBase.addMessage("Se registro la caja de forma correcta.", ETipoMensaje.INFORMACION);
      } // if
      else 
        JsfBase.addMessage("Ocurrió un error al registrar la caja.", ETipoMensaje.ERROR);      
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
