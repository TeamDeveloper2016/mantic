package mx.org.kaana.kajool.procesos.mantenimiento.configuracion.backing;

/**
 *@company KAANA
 *@project  KAJOOL (Control system polls)
 *@date 09/04/2024
 *@time 20:33:29 PM
 *@author One Developer 2016 <one.developer@kaana.org.mx>
 */
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import mx.org.kaana.kajool.control.bean.Portal;
import mx.org.kaana.kajool.procesos.mantenimiento.contadores.reglas.Ayudas;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.Messages;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.recurso.TcConfiguraciones;
import mx.org.kaana.xml.Dml;
import org.apache.commons.logging.Log; 
import org.apache.commons.logging.LogFactory;

@Named(value="kajoolMantenimientoConfiguracionReiniciar")
@javax.faces.view.ViewScoped
public class Reiniciar extends IBaseAttribute implements Serializable {

	private static final Log LOG              = LogFactory.getLog(Reiniciar.class);	
  private static final long serialVersionUID= -832641138131197051L;
  
  @PostConstruct
  @Override
	protected void init() {
    try {			  
    } // try
    catch(Exception e) {
			JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
	} // init
  
  public void doAceptar() {
    LOG.error("RE-INICIAR SERVIDOR: ["+ JsfBase.getAutentifica().getPersona().getNombreCompleto()+"]");
    try {      
  		LOG.error("Deteniendo el servidor ...");
      Process runtimeProcess = Runtime.getRuntime().exec("jk");
      int processComplete = runtimeProcess.waitFor();
      LOG.error("Resultado del proceso: "+ processComplete);
  		/*NOTE: processComplete=0 if correctly executed, will contain other values if not*/
 	  	if (processComplete== 0) {
    		LOG.error("Reinciando el servidor ...");
        runtimeProcess = Runtime.getRuntime().exec("jr");
        processComplete = runtimeProcess.waitFor();
        LOG.error("Resultado del proceso: "+ processComplete);
        if (processComplete== 0)
          LOG.error("RE-INICIAR CON EXITO");
      } // if
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);      
    } // catch	
    
  }
  
  
}
