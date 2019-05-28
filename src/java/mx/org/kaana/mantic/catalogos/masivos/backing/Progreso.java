package mx.org.kaana.mantic.catalogos.masivos.backing;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.catalogos.backing.Monitoreo;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 17/12/2018
 *@time 07:51:53 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

@Named(value = "manticCatalogosMasivosProgreso")
@ViewScoped
public class Progreso extends IBaseAttribute implements Serializable {

  private static final long serialVersionUID = 8793667741599428311L;

	private Monitoreo monitoreo;	
	
  @PostConstruct
  @Override
  protected void init() {
    try {
			this.monitoreo= JsfBase.getAutentifica().getMonitoreo();
      this.attrs.put("value", this.monitoreo.getPorcentaje());
      this.attrs.put("cancelar", this.monitoreo.isCorriendo());
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

	public Monitoreo getMonitoreo() {
		return monitoreo;
	}
 
	public void doCancelar() {
		this.monitoreo.terminar();
    this.attrs.put("cancelar", this.monitoreo.isCorriendo());
		UIBackingUtilities.execute("cancel();");
	}
	
	public String doRegresar() {
		return "importar".concat(Constantes.REDIRECIONAR);
	}

  public void doProgreso() {
		this.attrs.put("value", this.monitoreo.getPorcentaje());
		this.attrs.put("cancelar", this.monitoreo.isCorriendo());
	}	
	
}
