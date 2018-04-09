package mx.org.kaana.kajool.procesos.acceso.confirmacion.backing;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.pagina.IBaseFilter;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.procesos.acceso.beans.Cliente;
import mx.org.kaana.kajool.procesos.acceso.reglas.Acceso;
import mx.org.kaana.kajool.procesos.mantenimiento.temas.backing.TemaActivo;
import mx.org.kaana.kajool.procesos.usuarios.reglas.Transaccion;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import mx.org.kaana.libs.formato.Error;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 21/10/2016
 *@time 10:43:17 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>@kaana.org.mx>
 */

@ManagedBean(name="kajoolAccesoConfirmacionFiltro")
@RequestScoped
public class Filtro extends IBaseFilter implements Serializable {

	
  private Cliente cliente;
  @ManagedProperty(value="#{kajoolTemaActivo}")
  private TemaActivo temaActivo;
  private static final Log LOG=LogFactory.getLog(Filtro.class);	

  @Override
	@PostConstruct
  protected void init() {
  	setCliente((Cliente) JsfBase.getFlashAttribute("cliente"));		
  }

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

  public TemaActivo getTemaActivo() {
    return temaActivo;
  }

  public void setTemaActivo(TemaActivo temaActivo) {
    this.temaActivo = temaActivo;
  }

  public String doConfirmar() {
		String regresar           = null;
		Acceso acceso             = null;
		String estilo             = null;		
		Map<String, Object> params= null;		
		Transaccion transaccion   = null;
		try {				
			params= new HashMap();			
		  acceso= new Acceso(getCliente());							
			if(getCliente().getNueva().equals(getCliente().getConfirma())) {
        acceso.valida();
        params.put("idEmpleado", JsfBase.getAutentifica().getEmpleado().getIdEmpleado());
        params.put("nuevaContrasenia", getCliente().getConfirma());
        transaccion= new Transaccion(params);
        if(transaccion.ejecutar(EAccion.COMPLEMENTAR)) {
          regresar= acceso.toForward();		
          estilo  = JsfBase.getAutentifica().getEmpleado().getEstilo();
          this.temaActivo.setName(estilo!= null ? estilo : Constantes.TEMA_INICIAL);	
        } // IF
			} // if
			else
				JsfBase.addMessage("La confirmación de la contraseña es incorrecta.");			
		} // try
		catch(Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch	
		finally {
			Methods.clean(params);
		}  // finally
		return regresar;
  } // doIngresar
	
	public String doRegresar() {
		String regresar= null;
		try {
		  regresar= "/indice.jsf".concat(Constantes.REDIRECIONAR);
		} // try
		catch(Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
		return regresar;
	}

	@Override
	public void doLoad() {
	}

}
