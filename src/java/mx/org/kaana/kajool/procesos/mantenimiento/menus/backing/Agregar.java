package mx.org.kaana.kajool.procesos.mantenimiento.menus.backing;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.db.dto.TcJanalMenusDto;
import mx.org.kaana.kajool.enums.EAccion;
import static mx.org.kaana.kajool.enums.EAccion.AGREGAR;
import static mx.org.kaana.kajool.enums.EAccion.ELIMINAR;
import static mx.org.kaana.kajool.enums.EAccion.MODIFICAR;
import static mx.org.kaana.kajool.enums.EAccion.REGISTRAR;
import mx.org.kaana.kajool.procesos.mantenimiento.menus.reglas.Consulta;
import mx.org.kaana.kajool.procesos.mantenimiento.menus.reglas.Transaccion;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 26/08/2015
 * @time 05:08:12 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
@ManagedBean(name = "kajoolMantenimientoMenusAgregar")
@ViewScoped
public class Agregar implements Serializable {

	private static final Log LOG=LogFactory.getLog(Agregar.class);	
	private static final String ICON_DEFAULT="fa-star";
	private static final String TAMANIO_DEFAULT="fa-1x";
	private static final long serialVersionUID=1754290985514507235L;
	private TcJanalMenusDto current;
	private TcJanalMenusDto source;
	private TcJanalMenusDto opcionPadre;
	private EAccion accion;
	private String titulo;
	private String tamanio;
	private Boolean soloLectura;

	public TcJanalMenusDto getCurrent() {
		return current;
	}

	public void setCurrent(TcJanalMenusDto current) {
		this.current=current;
	}

	public String getTitulo() {
		return titulo;
	}
	
	public void setTitulo(String titulo){
	this.titulo= titulo;
	}

	public Boolean getSoloLectura() {
		return soloLectura;
	}
	
	public void setSoloLectura(Boolean soloLectura) {
		this.soloLectura= soloLectura;
	}
	
	public EAccion getAccion(){
		return accion;
	}
	
	public void setAccion(EAccion accion){
		this.accion= accion;
	}

	public String getTamanio() {
		return tamanio;
	}
	
	public void setTamanio(String tamanio){
		this.tamanio= tamanio;
	}

	@PostConstruct
	private void init() {
		Consulta consulta=null;
		try {
			consulta=new Consulta();
			this.soloLectura=false;
			this.source=(TcJanalMenusDto) JsfBase.getFlashAttribute("current");
			this.opcionPadre=(TcJanalMenusDto) JsfBase.getFlashAttribute("opcionPadre");
			this.accion=(EAccion) JsfBase.getFlashAttribute("accion");
			if (this.accion!=null&&this.source!=null) {
				switch (this.accion) {
					case AGREGAR:
						this.titulo="Agregar opción a menu";
						this.tamanio= TAMANIO_DEFAULT;
						this.current=new TcJanalMenusDto(consulta.toNextKey(this.source.getClave(), this.source.getClave().equals("0000000000") ? this.source.getNivel().intValue() : this.source.getNivel().intValue()+1, 1), this.source.getClave().equals("0000000000") ? 1 : this.source.getNivel()+1, "", "0", 1L, "", -1L, "", 0L, "", 1L);						
						this.current.setIcono("fa ".concat(ICON_DEFAULT));
						break;
					case MODIFICAR:
						this.titulo="Modificar opción al menú";
						this.current=this.source;
						this.tamanio= this.current.getIcono().substring(this.current.getIcono().length()- 5, this.current.getIcono().length());
						break;
					case ELIMINAR:
						this.soloLectura=true;
						this.titulo="Eliminar opción del menú";
						this.current=this.source;
						this.tamanio= this.current.getIcono().substring(this.current.getIcono().length()- 5, this.current.getIcono().length());
						break;
					case REGISTRAR:
						this.titulo="Agregar descendencia a la opción";
						this.current=new TcJanalMenusDto(consulta.toNextKey(this.source.getClave(), this.source.getNivel().intValue()+1, 1), this.source.getNivel()+1, "", "0", 1L, "", -1L, "", 0L, "", 1L);

						this.current.setIcono("fa ".concat(ICON_DEFAULT));
						break;
				} // switch
			} // if
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
	} // init

	public void doAsignarIcono() {
		String icono= null;
		try {
			icono= JsfBase.getExternalContext().getRequestParameterMap().get("iconoSeleccionado");
			Methods.setValue(this.current, "icono", new Object[]{icono});
			this.tamanio= TAMANIO_DEFAULT;
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);
		} // catch
	} // doAsignarImagen
	
	public void doAsignarTamanio(){		
		boolean inicio= true;
		try {
			for(int i= 1; i< 6; i++){
				if(this.current.getIcono().contains(" fa-"+ i+ "x")){
					this.current.setIcono(this.current.getIcono().replace(" fa-"+ i+ "x", " "+ this.tamanio));
					inicio= false;
					break;
				} // if
			} // for
			if(inicio)
				this.current.setIcono(this.current.getIcono().concat(" ").concat(this.tamanio));
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);
		} // catch
	}

	public String doAceptar() {
		Transaccion transaccion=null;
		try {
			this.current.setIcono(this.current.getIcono().concat(" ").concat(this.tamanio));
			transaccion=new Transaccion(this.current);
			if (transaccion.ejecutar(this.accion)) {
				JsfBase.addMessage("La acción de ".concat(this.accion.name()).concat(" se realizó con éxito."));
			} // if
			else {
				throw new RuntimeException("La acción de ".concat(this.accion.name()).concat(" no se puedo realizar, verifiquelo."));
			} // esle
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
		return doCancelar();
	} // doAceptar

	public String doCancelar() {
		switch (this.accion) {
			case ELIMINAR:
				this.current=(this.source!=null&&this.source.getNivel().equals(1L)&&this.accion.equals(EAccion.ELIMINAR))||this.current.getIdMenu().equals(-1L) ? null : current;
				if (current!=null) {
					this.current=opcionPadre;
				} // if
				break;
			case MODIFICAR:
				this.current=opcionPadre;
				break;
			default:
				this.current=(this.source!=null&&this.source.getNivel().equals(1L)&&this.accion.equals(EAccion.ELIMINAR))||this.current.getIdMenu().equals(-1L) ? null : current;
				break;
		} // switch
		JsfBase.setFlashAttribute("current", current);
		return "/Paginas/Mantenimiento/Menus/filtro".concat(Constantes.REDIRECIONAR);
	} // doCancelar
}
