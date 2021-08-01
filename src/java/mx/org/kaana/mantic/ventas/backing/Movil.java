package mx.org.kaana.mantic.ventas.backing;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.mantic.catalogos.clientes.reglas.MotorBusqueda;
import mx.org.kaana.mantic.db.dto.TcManticClientesDto;
import org.apache.log4j.Logger;

@Named(value= "manticVentasMovil")
@ViewScoped
public class Movil extends Accion implements Serializable {

	private static final Logger LOG            = Logger.getLogger(Movil.class);
  private static final long serialVersionUID = 327393488565639361L;
  
	@PostConstruct
  @Override
  protected void init() {		
    try { 
      super.init();
      MotorBusqueda motor= new MotorBusqueda(-1L);
      TcManticClientesDto cliente= motor.toCliente(JsfBase.getAutentifica().getPersona().getIdPersona());
      if(cliente!= null)
        this.doAsignaClienteInicial(cliente.getIdCliente());
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
  } // init

	@Override
  public void doLoad() {
    super.doLoad();	
  } // doLoad

}