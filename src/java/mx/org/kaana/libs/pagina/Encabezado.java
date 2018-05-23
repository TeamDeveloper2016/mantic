package mx.org.kaana.libs.pagina;

import java.io.Serializable;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named(value = "kaanaPlantillasSecciones")
@ViewScoped
public class Encabezado implements Serializable {

	private static final long serialVersionUID=-4446715067043818422L;

	public void doCleanFlash() {
	  JsfBase.cleanFlashParams();
	}
	
}
