package mx.org.kaana.mantic.facturas.backing;

import java.io.Serializable;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.libs.pagina.JsfBase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@Named(value= "manticFacturasCreditos")
@ViewScoped
public class Creditos extends Tickets implements Serializable {

  private static final long serialVersionUID= 8743667741599428338L;
	private static final Log LOG              = LogFactory.getLog(Creditos.class);	

  @Override
  public void doLoad() {
		this.doLoadComun("4");
  } // doLoad

	@Override
	public String doAceptar() {
		String regresar= super.doAceptar();
		JsfBase.setFlashAttribute("retorno", "/Paginas/Mantic/Facturas/creditos");
		return regresar;
	} // doAceptar
}