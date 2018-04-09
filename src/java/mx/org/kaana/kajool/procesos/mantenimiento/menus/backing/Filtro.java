package mx.org.kaana.kajool.procesos.mantenimiento.menus.backing;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 26/08/2015
 * @time 03:29:06 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.kajool.db.dto.TcJanalMenusDto;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.procesos.enums.ESemaforos;
import mx.org.kaana.kajool.procesos.mantenimiento.menus.reglas.Consulta;
import mx.org.kaana.kajool.procesos.mantenimiento.menus.reglas.Transaccion;
import mx.org.kaana.libs.formato.Cadena;

@ManagedBean(name = "kajoolMantenimientoMenusFiltro")
@ViewScoped
public class Filtro implements Serializable {

	private static final long serialVersionUID=5810763207778245423L;
	private List<TcJanalMenusDto> lazyModel;
	private TcJanalMenusDto current;
	private TcJanalMenusDto opcionPadre;
	private TcJanalMenusDto seleccionado;

	public List<TcJanalMenusDto> getLazyModel() {
		return lazyModel;
	}

	public TcJanalMenusDto getSeleccionado() {
		return seleccionado;
	}

	public void setSeleccionado(TcJanalMenusDto seleccionado) {
		this.seleccionado=seleccionado;
	}

	@PostConstruct
	private void init() {
		Consulta consulta=null;
		try {
			consulta=new Consulta();
			consulta.cleanLevels();
			this.current=(TcJanalMenusDto) (JsfBase.getFlashAttribute("current")==null ? JsfBase.getParametro("current") : JsfBase.getFlashAttribute("current"));
			if (this.current==null) {
				this.current=new TcJanalMenusDto();
				this.current.setClave(consulta.toCode());
				this.current.setNivel(1L);
				actualizarChildren(0);
			} // if	
			else {
				actualizarChildren(1);
			} // esle
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
	} // init

	public List<TcJanalMenusDto> getVisitados() {
		List<TcJanalMenusDto> regresar=null;
		Consulta consulta=null;
		try {
			consulta=new Consulta();
			regresar=consulta.toFather(this.seleccionado==null ? this.current.getClave() : this.seleccionado.getClave());
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
		return regresar;
	} // getVisitados

	private void actualizarChildren(int nivel) throws Exception {
		Consulta consulta=null;
		try {
			this.current=this.seleccionado==null ? this.current : this.seleccionado;
			consulta=new Consulta();
			this.lazyModel=consulta.toChildren(this.current.getClave(), this.current.getNivel().intValue()+nivel);
		} // try
		catch (Exception e) {
			throw e;
		} // catch
	} // actualizarChildren

	public void doActualizarChildren() {
		try {
			this.opcionPadre=this.seleccionado;
			actualizarChildren(1);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
	} // doActualizar

	public void doVisitado(TcJanalMenusDto menu) {
		try {
			this.current=menu;
			actualizarChildren(1);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
	} // doVisitado

	public String doActualizar(String accion) {
		JsfBase.setFlashAttribute("current", this.seleccionado==null ? this.current : this.seleccionado);
		JsfBase.setFlashAttribute("opcionPadre", this.opcionPadre==null ? this.current : this.opcionPadre);
		JsfBase.setFlashAttribute("accion", EAccion.valueOf(accion));
		return "agregar".concat(Constantes.REDIRECIONAR);
	} // doActualizar

	public void doInicio() {
		JsfBase.setFlashAttribute("current", null);
		init();
	} // doInicio

	public void doMoverPosicion(String accion) {
		Transaccion transaccion=null;
		Consulta consulta=null;
		List<TcJanalMenusDto> dtos=null;
		int index=-1;
		try {
			transaccion=new Transaccion(this.seleccionado);
			if (transaccion.ejecutar(EAccion.valueOf(accion))) {
				JsfBase.addMessage("La posición de menú se movió exitosamente");
			} // if
			else {
				JsfBase.addMessage("Error", "La posición de menú no se pudo modificar.", ETipoMensaje.ERROR);
			} // else
			consulta=new Consulta();
			dtos=consulta.toFather(this.seleccionado.getClave());
			index=dtos.indexOf(this.seleccionado);
			this.seleccionado=index>0 ? this.seleccionado=dtos.get(index-1) : null;
			if (index>0) {
				actualizarChildren(1);
				JsfBase.setFlashAttribute("current", null);
			} // if
			else {
				doInicio();
			} // else
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
	} // doMoverPosicion

	public void doReasignarClaves() {
		Transaccion transaccion=null;
		try {
			transaccion=new Transaccion(this.seleccionado!=null&&!this.seleccionado.getClave().equals("0000000000") ? this.seleccionado.getClave() : "", this.seleccionado!=null ? this.seleccionado.getNivel() : 1L);
			transaccion.ejecutar(EAccion.PROCESAR);
			JsfBase.addMessage("Claves de menú reasignadas correctamente.");
			actualizarChildren(this.seleccionado!=null&&!this.seleccionado.getClave().equals("0000000000") ? 1 : 0);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
	} // doReasignarClaves
  
  public String doSemaforo(String publicar) {		
	  return Cadena.isVacio(publicar) && publicar.equals("0")? ESemaforos.ROJO.getNombre(): ESemaforos.VERDE.getNombre();
  } // doSemaforo	

}
