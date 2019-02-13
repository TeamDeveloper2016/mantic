package mx.org.kaana.mantic.facturas.backing;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.facturama.reglas.CFDIFactory;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.mantic.facturas.reglas.Transferir;

@Named(value= "manticFacturasSincronizar")
@ViewScoped
public class Sincronizar extends IBaseAttribute implements Serializable {

	private static final long serialVersionUID=8124872510277721444L;

	@PostConstruct
	@Override
	protected void init() {
		if(JsfBase.getFlashAttribute("accion")== null)
			UIBackingUtilities.execute("janal.isPostBack('cancelar')");
		try {
		  this.attrs.put("top", CFDIFactory.getInstance().toCfdisSize());
		} // try
		catch(Exception e) {
		  this.attrs.put("top", 0);
		} // catch
		this.attrs.put("total", 0);
	}

  public String doCancelar() {   
    return this.attrs.get("retorno") != null ? (String)this.attrs.get("retorno") : "filtro";
  } 

  public String doAceptar() {  
    Transferir transferir= null;
    String regresar      = null;
    try {			
			transferir = new Transferir();
			if (transferir.ejecutar(EAccion.GENERAR)) {
				this.attrs.put("total", transferir.getCount());
				this.attrs.put("clientes", transferir.getClientes());
  			regresar = this.attrs.get("retorno")!= null ? this.attrs.get("retorno").toString().concat(Constantes.REDIRECIONAR) : null;
   			UIBackingUtilities.execute("janal.alert('Se transfirieron ["+ transferir.getCount()+ "] facturas del portal de facturama');");
			} // if
			else 
				JsfBase.addMessage("Ocurrió un error al transferir las facturas.", ETipoMensaje.ERROR);      			
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
 			UIBackingUtilities.execute("cancel();");
    } // catch
    return regresar;
  } // doAccion

	public void doCompleto() {
		
	}
	
}
