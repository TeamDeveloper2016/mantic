package mx.org.kaana.kajool.procesos.mantenimiento.menus.encabezado.backing;

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
import mx.org.kaana.kajool.db.dto.TcJanalMenusEncabezadoDto;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.procesos.enums.ESemaforos;
import mx.org.kaana.kajool.procesos.mantenimiento.menus.encabezado.reglas.MenuEncabezado;
import mx.org.kaana.kajool.procesos.mantenimiento.menus.encabezado.reglas.Transaccion;
import mx.org.kaana.libs.formato.Cadena;

@ManagedBean(name = "kajoolMantenimientoMenusEncabezadoFiltro")
@ViewScoped
public class Filtro implements Serializable {

	private static final long serialVersionUID=5810763207778245423L;
	private List<TcJanalMenusEncabezadoDto> lazyModel;
	private TcJanalMenusEncabezadoDto actual;
	private TcJanalMenusEncabezadoDto opcionPadre;
	private TcJanalMenusEncabezadoDto seleccionado;

	public List<TcJanalMenusEncabezadoDto> getLazyModel() {
		return lazyModel;
	}

	public TcJanalMenusEncabezadoDto getSeleccionado() {
		return seleccionado;
	}

	public void setSeleccionado(TcJanalMenusEncabezadoDto seleccionado) {
		this.seleccionado=seleccionado;
	}

	@PostConstruct
	private void init() {
		MenuEncabezado menuEncabezado=null;
		try {
			menuEncabezado=new MenuEncabezado();
			menuEncabezado.cleanLevels();
			this.actual=(TcJanalMenusEncabezadoDto) (JsfBase.getFlashAttribute("actual")==null ? JsfBase.getParametro("actual") : JsfBase.getFlashAttribute("actual"));
			if (this.actual==null) {
				this.actual=new TcJanalMenusEncabezadoDto();
				this.actual.setClave(menuEncabezado.toCode());
				this.actual.setNivel(1L);
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

	public List<TcJanalMenusEncabezadoDto> getVisitados() {
		List<TcJanalMenusEncabezadoDto> regresar=null;
		MenuEncabezado menuEncabezado=null;
		try {
			menuEncabezado=new MenuEncabezado();
			regresar=menuEncabezado.toFather(this.seleccionado==null || this.seleccionado.getNivel().equals(3L) ? this.actual.getClave() : this.seleccionado.getClave());
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
		return regresar;
	} // getVisitados

	private void actualizarChildren(int nivel) throws Exception {
		MenuEncabezado menuEncabezado=null;
		try {
			this.actual=this.seleccionado==null ? this.actual : this.seleccionado;
			menuEncabezado=new MenuEncabezado();
			this.lazyModel=menuEncabezado.toChildren(this.actual.getClave(), this.actual.getNivel().intValue()+nivel);
		} // try
		catch (Exception e) {
			throw e;
		} // catch
	} // actualizarChildren

	public void doActualizarChildren() {
		try {
			if(this.seleccionado.getNivel()>= 3L)
				JsfBase.addMessage("No es posible avanzar más niveles", ETipoMensaje.ALERTA);
			else{
				this.opcionPadre=this.seleccionado;
				actualizarChildren(1);
			} // else
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
	} // doActualizar

	public void doVisitado(TcJanalMenusEncabezadoDto menu) {
		try {
			this.actual=menu;
			actualizarChildren(1);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
	} // doVisitado

	public String doActualizar(String accion) {
		String regresar= null;
		if(this.actual.getNivel()>= 3L || (this.seleccionado!= null && this.seleccionado.getNivel()>= 3L && (accion.equals("AGREGAR") || accion.equals("REGISTRAR"))))
			JsfBase.addMessage("No es posible agregar más niveles a este menu", ETipoMensaje.ALERTA);
		else{
			JsfBase.setFlashAttribute("actual", this.seleccionado==null ? this.actual : this.seleccionado);
			JsfBase.setFlashAttribute("opcionPadre", this.opcionPadre==null ? this.actual : this.opcionPadre);
			JsfBase.setFlashAttribute("accion", EAccion.valueOf(accion));
			regresar= "agregar".concat(Constantes.REDIRECIONAR);
		} // else
			
		return regresar;
	} // doActualizar

	public void doInicio() {
		JsfBase.setFlashAttribute("actual", null);
		this.seleccionado= null;
		init();
	} // doInicio

	public void doMoverPosicion(String accion) {
		Transaccion transaccion=null;
		MenuEncabezado menuEncabezado=null;
		List<TcJanalMenusEncabezadoDto> dtos=null;
		int index=-1;
		try {
			transaccion=new Transaccion(this.seleccionado);
			if (transaccion.ejecutar(EAccion.valueOf(accion))) {
				JsfBase.addMessage("La posición de menú se movió exitosamente");
			} // if
			else {
				JsfBase.addMessage("Error", "La posición de menú no se pudo modificar.", ETipoMensaje.ERROR);
			} // else
			menuEncabezado=new MenuEncabezado();
			dtos=menuEncabezado.toFather(this.seleccionado.getClave());
			index=dtos.indexOf(this.seleccionado);
			this.seleccionado=index>0 ? this.seleccionado=dtos.get(index-1) : null;
			if (index>0) {
				actualizarChildren(1);
				JsfBase.setFlashAttribute("actual", null);
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
