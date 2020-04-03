package mx.org.kaana.mantic.facturas.backing;

import java.io.Serializable;
import java.util.Objects;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.libs.formato.Cadena;
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
		super.doLoad();
  } // doLoad

	@Override
	public String doAceptar() {
		String regresar= super.doAceptar();
		JsfBase.setFlashAttribute("retorno", "/Paginas/Mantic/Facturas/creditos");
		return regresar;
	} // doAceptar
	
	@Override
	public boolean doDisponible(Entity row) {
		return Cadena.isVacio(row.toString("folio")) && row.toLong("idFacturar").equals(2L) && row.toDouble("importe")> 0D && (this.ventaPublico.indexOf(row.toLong("idCliente"))>= 0 || this.pivote== null || Objects.equals(this.pivote.toLong("idCliente"), row.toLong("idCliente")));
	}
}