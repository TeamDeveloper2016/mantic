package mx.org.kaana.mantic.facturas.backing;

import java.io.Serializable;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.mantic.facturas.reglas.Transferir;
import org.primefaces.context.RequestContext;

@Named(value= "manticFacturasSincronizar")
@ViewScoped
public class Sincronizar extends IBaseAttribute implements Serializable {

	private static final long serialVersionUID=8124872510277721444L;

	@Override
	protected void init() {
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
   			RequestContext.getCurrentInstance().execute("jsArticulos.back('transfirieron las facturas', '"+ transferir.getCount()+ "');");
			} // if
			else 
				JsfBase.addMessage("Ocurrió un error al transferir las facturas.", ETipoMensaje.ERROR);      			
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    return regresar;
  } // doAccion

}
