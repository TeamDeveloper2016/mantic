package mx.org.kaana.kajool.procesos.mantenimiento.temas.backing;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.JsfUtilities;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.db.dto.TcJanalEmpleadosDto;
import mx.org.kaana.kajool.db.dto.TcJanalUsuariosDto;
import mx.org.kaana.kajool.procesos.mantenimiento.temas.reglas.Transaccion;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import mx.org.kaana.kajool.procesos.mantenimiento.temas.beans.Tema;
import mx.org.kaana.kajool.procesos.mantenimiento.temas.reglas.Estilos;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 10/09/2015
 * @time 11:15:25 AM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */

@ManagedBean(name= "kajoolMantenimientoTemasDisponibles")
@ViewScoped
public class Disponibles implements Serializable {

	private static final Log LOG              = LogFactory.getLog(Disponibles.class);
	private static final long serialVersionUID= 4892526535758426848L;
  @ManagedProperty(value="#{kajoolTemaActivo}")
	private TemaActivo temaActivo;
	private Tema theme;

	public List<Tema> getThemes() {
		return Estilos.getInstance().getThemes();
	}

	public TemaActivo getTemaActivo() {
		return temaActivo;
	}

	public void setTemaActivo(TemaActivo temaActivo) {
		this.temaActivo= temaActivo;
	}

  public Tema getTheme() {
    return theme;
  }

  public void setTheme(Tema theme) {
    this.theme= theme;
  }	

  @PostConstruct
	private void init() {
		setTheme(new Tema(getTemaActivo().getName(), getTemaActivo().getName(), getTemaActivo().getName()));
		LOG.debug("Se inicializo el tema del proyecto");
		LOG.debug(FacesContext.getCurrentInstance().getExternalContext().getInitParameter("primefaces.THEME"));
	}

  private void saveTheme() {
    Transaccion transaccion= null;
    TcJanalUsuariosDto dto = null;
		TcJanalEmpleadosDto persona= null;
    try {
      dto= (TcJanalUsuariosDto)DaoFactory.getInstance().findById(TcJanalUsuariosDto.class, JsfBase.getIdUsuario());
			persona= (TcJanalEmpleadosDto) DaoFactory.getInstance().findById(TcJanalEmpleadosDto.class, dto.getIdEmpleado());						
      persona.setEstilo(getTemaActivo().getName());
      transaccion= new Transaccion(persona);
      transaccion.ejecutar(EAccion.MODIFICAR);
    } // try // try
    catch(Exception e) {
      JsfUtilities.addMessageError(e);
			mx.org.kaana.libs.formato.Error.mensaje(e);
    }// catch
  }

  public void onChangeTheme(ActionEvent event) {
    setTheme((Tema)event.getComponent().getAttributes().get("tema"));
    getTemaActivo().setName(getTheme().getName());
    saveTheme();
    JsfBase.addMessage("Tema: ".concat(getTheme().getName()), "Se cambió con éxito el tema !", ETipoMensaje.INFORMACION);
  }

}
