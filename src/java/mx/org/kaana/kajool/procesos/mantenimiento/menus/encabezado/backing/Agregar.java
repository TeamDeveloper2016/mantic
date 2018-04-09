package mx.org.kaana.kajool.procesos.mantenimiento.menus.encabezado.backing;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.db.dto.TcJanalMenusEncabezadoDto;
import mx.org.kaana.kajool.procesos.mantenimiento.menus.encabezado.reglas.MenuEncabezado;
import mx.org.kaana.kajool.procesos.mantenimiento.menus.encabezado.reglas.Transaccion;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date Sep 14, 2015
 *@time 7:08:31 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

@ManagedBean(name = "kajoolMantenimientoMenusEncabezadoAgregar")
@ViewScoped
public class Agregar implements Serializable {
	
	private final String ICON_DEFAULT="fa-star";
	private static final String TAMANIO_DEFAULT="fa-1x";
	private static final long serialVersionUID=1426781201924304703L;
	private TcJanalMenusEncabezadoDto actual;
	private TcJanalMenusEncabezadoDto source;
	private TcJanalMenusEncabezadoDto opcionPadre;
	private String titulo;
	private String tamanio;
	private Boolean soloLectura;
	private EAccion accion;
	
	public TcJanalMenusEncabezadoDto getActual() {
		return actual;
	}

	public void setActual(TcJanalMenusEncabezadoDto actual) {
		this.actual=actual;
	}

	public String getTitulo() {
		return titulo;
	}

	public Boolean getSoloLectura() {
		return soloLectura;
	}
	
	public String getTamanio() {
		return tamanio;
	}
	
	public void setTamanio(String tamanio){
		this.tamanio= tamanio;
	}
	
	@PostConstruct
	private void init() {
		MenuEncabezado menuEncabezado=null;
		try {
			menuEncabezado=new MenuEncabezado();
			this.soloLectura= false;
			this.source=(TcJanalMenusEncabezadoDto) JsfBase.getFlashAttribute("actual");
			this.opcionPadre=(TcJanalMenusEncabezadoDto) JsfBase.getFlashAttribute("opcionPadre");
			this.accion= (EAccion) JsfBase.getFlashAttribute("accion");
			if (this.accion!=null&&this.source!=null) {
				switch (this.accion) {
					case AGREGAR:
						this.titulo= "Agregar opción a menu";
						this.tamanio= TAMANIO_DEFAULT;
						this.actual=new TcJanalMenusEncabezadoDto();
						this.actual.setClave(menuEncabezado.toNextKey(this.source.getClave(), this.source.getClave().equals("0000000000") ? this.source.getNivel().intValue() : this.source.getNivel().intValue()+1, 1));
						this.actual.setNivel(this.source.getClave().equals("0000000000") ? 1 : this.source.getNivel()+1);
						this.actual.setIcono("fa ".concat(ICON_DEFAULT));
						this.actual.setUltimo(0L);
						break;
					case MODIFICAR:
						this.titulo= "Modificar opción al menú";
						this.actual=this.source;
						this.tamanio= this.actual.getIcono().substring(this.actual.getIcono().length()- 5, this.actual.getIcono().length());
						break;
					case ELIMINAR:
						this.soloLectura= true;
						this.titulo= "Eliminar opción del menú";
						this.actual=this.source;
						this.tamanio= this.actual.getIcono().substring(this.actual.getIcono().length()- 5, this.actual.getIcono().length());
						break;
					case REGISTRAR:
						this.titulo= "Agregar descendencia a la opción";
						this.actual=new TcJanalMenusEncabezadoDto();
						this.actual.setClave(menuEncabezado.toNextKey(this.source.getClave(), this.source.getClave().equals("0000000000") ? this.source.getNivel().intValue() : this.source.getNivel().intValue()+1, 1));
						this.actual.setNivel(this.source.getClave().equals("0000000000") ? 1 : this.source.getNivel()+1);
						this.actual.setIcono("fa ".concat(ICON_DEFAULT));
						this.actual.setUltimo(0L);
						break;
				} // switch
			} // if
		} // try
		catch (Exception e) {
			mx.org.kaana.libs.formato.Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
	} // init
	
	public void doAsignarIcono() {
		String icono= null;
		try {
			icono= JsfBase.getExternalContext().getRequestParameterMap().get("iconoSeleccionado");
			Methods.setValue(this.actual, "icono", new Object[]{icono});
			this.tamanio= TAMANIO_DEFAULT;
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			mx.org.kaana.libs.formato.Error.mensaje(e);
		} // catch
	} // doAsignarImagen
	
	public void doAsignarTamanio(){		
		boolean inicio= true;
		try {
			for(int i= 1; i< 6; i++){
				if(this.actual.getIcono().contains(" fa-"+ i+ "x")){
					this.actual.setIcono(this.actual.getIcono().replace(" fa-"+ i+ "x", " "+ this.tamanio));
					inicio= false;
					break;
				} // if
			} // for
			if(inicio)
				this.actual.setIcono(this.actual.getIcono().concat(" ").concat(this.tamanio));
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			mx.org.kaana.libs.formato.Error.mensaje(e);
		} // catch
	}
	
	public String doAceptar() {
		Transaccion transaccion=null;
		try {
			this.actual.setIcono(this.actual.getIcono().concat(" ").concat(this.tamanio));
			transaccion=new Transaccion(this.actual);
			if (transaccion.ejecutar(this.accion)) {
				JsfBase.addMessage("La acción de ".concat(this.accion.name()).concat(" se realizó con éxito."));
			} // if
			else {
				throw new RuntimeException("La acción de ".concat(this.accion.name()).concat(" no se puedo realizar, verifiquelo."));
			} // esle
		} // try
		catch (Exception e) {
			mx.org.kaana.libs.formato.Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
		return doCancelar();
	} // doAceptar

	public String doCancelar() {
		switch (this.accion) {
			case ELIMINAR:
				this.actual=(this.source!=null&&this.source.getNivel().equals(1L)&&this.accion.equals(EAccion.ELIMINAR))||this.actual.getIdMenuEncabezado().equals(-1L) ? null : actual;
				if (actual!=null) {
					this.actual=opcionPadre;
				} // if
				break;
			case MODIFICAR:
				this.actual=opcionPadre;
				break;
			default:
				this.actual=(this.source!=null&&this.source.getNivel().equals(1L)&&this.accion.equals(EAccion.ELIMINAR))||this.actual.getIdMenuEncabezado().equals(-1L) ? null : actual;
				break;
		} // switch
		JsfBase.setFlashAttribute("actual", actual);
		return "/Paginas/Mantenimiento/Menus/Encabezado/filtro".concat(Constantes.REDIRECIONAR);
	} // doCancelar

}
