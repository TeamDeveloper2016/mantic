package mx.org.kaana.kajool.control.backing;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Encriptar;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.facturas.beans.FacturaFicticia;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 21/08/2015
 * @time 12:27:03 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
@ViewScoped
@Named(value = "kajoolControlDescargar")
public class Descargar extends BaseMenu implements Serializable {

  private static final Log LOG = LogFactory.getLog(Descargar.class);
  private static final long serialVersionUID = 5323749709626263802L;
  
  @Override
  public void doLoad() {
  }
  
  public void doRecoverTicket() {
		Map<String, Object> params= new HashMap<>();
    Encriptar encriptar       = new Encriptar();
		try {
      if(!Cadena.isVacio(this.attrs.get("seguridad")) && !Objects.equals((String)this.attrs.get("seguridad"), "000000000000000000")) {
        String codigo= encriptar.desencriptar((String)this.attrs.get("seguridad"));
        params.put("codigo", codigo);
        params.put("rfc", this.attrs.get("rfc"));
        params.put("folio", this.attrs.get("folio"));
        FacturaFicticia venta= (FacturaFicticia)DaoFactory.getInstance().toEntity(FacturaFicticia.class, "TcManticVentasDto", "codigo", params);
        if(venta!= null) 
          if(!Cadena.isVacio(venta.getIdFactura())) 
            this.toLoadDocumentos();
          else
            JsfBase.addAlert("Error", "Este ticket no se ha facturado !", ETipoMensaje.ERROR);
        else 
          JsfBase.addAlert("Error", "Los datos capturados son incorrectos !", ETipoMensaje.ERROR);
      } // if
      else
        this.toLoadDocumentos();
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
		finally {
			Methods.clean(params);
		} // finally
		this.attrs.put("seguridad", "");
  } 

}