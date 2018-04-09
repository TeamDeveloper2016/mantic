package mx.org.kaana.kajool.procesos.usuarios.backing;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 13/10/2016
 *@time 10:40:04 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>@kaana.org.mx>
 */

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UISelect;
import mx.org.kaana.libs.pagina.UISelectItem;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.db.dto.TcJanalEmpleadosDto;
import mx.org.kaana.kajool.db.dto.TcJanalUsuariosDto;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.EPerfiles;
import mx.org.kaana.kajool.procesos.usuarios.reglas.RandomCuenta;
import mx.org.kaana.kajool.procesos.usuarios.reglas.Transaccion;
import mx.org.kaana.libs.formato.Cadena;
import org.primefaces.context.RequestContext;

@ManagedBean(name="kajoolUsuariosAccion")
@ViewScoped
public class Accion extends IBaseAttribute implements Serializable {	
  
  private static final long serialVersionUID = 5319332808932704073L;
		
  @PostConstruct
	@Override
	protected void init() {		
    try {
			this.attrs.put("tcJanalEmpleadoDto", new TcJanalEmpleadosDto());
			this.attrs.put("tcJanalUsuarioDto", new TcJanalUsuariosDto());			
      this.attrs.put("accion", (EAccion) JsfBase.getFlashAttribute("accion"));	
      this.attrs.put("esperada", "");
      this.attrs.put("texto", "<div class=\"TexAlCenter\">Se encontró un usuario con el mismo nombre y apellidos<br/><span class=\"FontBold Fs14\">¿Desea tomar lo datos este usuario para esta cuenta de acceso?</span></div>");
      this.attrs.put("showConfirmDialog", true);
			loadEntidades();
			loadPerfiles();
			if(((EAccion)this.attrs.get("accion")).equals(EAccion.MODIFICAR)) {
				this.attrs.put("titulo", "Modificar usuario de grupos de trabajo");
				cargarUsuario((Long)JsfBase.getFlashAttribute("idUsuario"));
			} // if
			else
				this.attrs.put("titulo", "Agregar usuario a grupos de trabajo");				
    } // try // try
    catch(Exception e) {
      Error.mensaje(e);
    } // catch
	}

	public String doAceptar() {
		Transaccion transaccion= null;
		String regresar        = null;
    try {					
      TcJanalEmpleadosDto empleado= (TcJanalEmpleadosDto)this.attrs.get("tcJanalEmpleadoDto");
      transaccion= new Transaccion(this.attrs);
			if(transaccion.ejecutar((EAccion)this.attrs.get("accion"))) {
				regresar= "filtro".concat(Constantes.REDIRECIONAR);
				JsfBase.addMessage(((EAccion)this.attrs.get("accion")).equals(EAccion.AGREGAR)? "Se agregó el usuario con éxito.": "Se modificó el usuario con éxito.");
			} // if
			else
				JsfBase.addMessage("El usuario "+ empleado.getNombres()+ empleado.getPrimerApellido() + " con ese perfil ya éxiste.");			
    } // try
    catch(Exception e) {
			JsfBase.addMessageError(e);
      Error.mensaje(e);
    } // catch
		return regresar;
	}
	
	private void cargarUsuario(Long idUsuario) {		
		try {			
			this.attrs.put("tcJanalUsuarioDto", (TcJanalUsuariosDto)DaoFactory.getInstance().findById(TcJanalUsuariosDto.class, idUsuario));
			this.attrs.put("tcJanalEmpleadoDto", (TcJanalEmpleadosDto) DaoFactory.getInstance().findById(TcJanalEmpleadosDto.class, ((TcJanalUsuariosDto)this.attrs.get("tcJanalUsuarioDto")).getIdEmpleado()));			
		} // try
		catch(Exception e) {
			JsfBase.addMessageError(e);
      Error.mensaje(e);
		} // catch		
	}
	
	public void doSearch() {
		Map<String, String> params  = null;
		TcJanalEmpleadosDto empleado= null;
		TcJanalUsuariosDto usuario  = null;
		try {
			params= new HashMap();
			params.put("curp", ((TcJanalEmpleadosDto)this.attrs.get("tcJanalEmpleadoDto")).getCurp());
			empleado= (TcJanalEmpleadosDto) DaoFactory.getInstance().findFirst(TcJanalEmpleadosDto.class, "curp", params);			
			if(empleado!= null) {
				this.attrs.put("tcJanalEmpleadoDto", empleado);
				params.put("idEmpleado", empleado.getIdEmpleado().toString());
				params.put("idPerfil", ((TcJanalUsuariosDto)this.attrs.get("tcJanalUsuarioDto")).getIdPerfil().toString());
				usuario = (TcJanalUsuariosDto) DaoFactory.getInstance().findFirst(TcJanalUsuariosDto.class, "identically", params);
				if(usuario!= null)
					this.attrs.put("tcJanalUsuarioDto", usuario);
			} // if
      else {
        this.attrs.put("tcJanalEmpleadoDto", new TcJanalEmpleadosDto());
        this.attrs.put("tcJanalUsuarioDto", new TcJanalUsuariosDto());	
      } // else
		} // try // try
		catch(Exception e) {
			JsfBase.addMessageError(e);
      Error.mensaje(e);
		} // exception
		finally {
			Methods.clean(params);
		} // finally
	}	
	
	protected void loadEntidades() {
    try {
			if(JsfBase.getAutentifica().getEmpleado().getIdPerfil().equals(EPerfiles.CAPTURISTA.getIdPerfil()) ||
				 JsfBase.getAutentifica().getEmpleado().getIdPerfil().equals(EPerfiles.RESPONSABLE_ESTATAL.getIdPerfil())) 
			  this.attrs.put(Constantes.SQL_CONDICION, "tc_janal_entidades.id_entidad=".concat(JsfBase.getAutentifica().getEmpleado().getIdEntidad().toString()));			
			else
				this.attrs.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);						
      this.attrs.put("listaEntidades", UISelect.build("VistaCargasTrabajoDto", "cargasTrabajo", this.attrs, "descripcion", EFormatoDinamicos.MAYUSCULAS, Constantes.SQL_TODOS_REGISTROS));			
      ((TcJanalUsuariosDto)this.attrs.get("tcJanalUsuarioDto")).setIdEntidad((Long) UIBackingUtilities.toFirstKeySelectItem((List<UISelectItem>) this.attrs.get("listaEntidades")));
    } // try
    catch (Exception e) {
      throw e;
    } // catch
  } // loadEntidades
	
	protected void loadPerfiles() {
    try {
			this.attrs.put("idPerfil", JsfBase.getAutentifica().getEmpleado().getIdPerfil());
      this.attrs.put("listaPerfiles", UISelect.build("VistaMantenimientoPerfilesDto", "jerarquiaMostrarAsignados", this.attrs, "descripcion", EFormatoDinamicos.MAYUSCULAS));			
      ((TcJanalUsuariosDto)this.attrs.get("tcJanalUsuarioDto")).setIdEntidad((Long) UIBackingUtilities.toFirstKeySelectItem((List<UISelectItem>) this.attrs.get("listaEntidades")));
    } // try
    catch (Exception e) {
      throw e;
    } // catch
  } // loadEntidades		

  public void doCuenta() {
    TcJanalEmpleadosDto empleado= (TcJanalEmpleadosDto)this.attrs.get("tcJanalEmpleadoDto");
    if(!Cadena.isVacio(empleado.getNombres()) || !Cadena.isVacio(empleado.getPrimerApellido()) || !Cadena.isVacio(empleado.getSegundoApellido())) {
      RandomCuenta random= new RandomCuenta(
        Cadena.isVacio(empleado.getNombres())? "kajool": empleado.getNombres(),
        Cadena.isVacio(empleado.getPrimerApellido())? "kajool": empleado.getPrimerApellido(), 
        Cadena.isVacio(empleado.getSegundoApellido())? "kajool": empleado.getSegundoApellido(), -1L);
      empleado.setCuenta(random.getCuentaGenerada());
    } // if
    if(!isLockField() && !Cadena.isVacio(empleado.getNombres()) && (!Cadena.isVacio(empleado.getPrimerApellido()) || !Cadena.isVacio(empleado.getSegundoApellido()))) {
      if((boolean)this.attrs.get("showConfirmDialog") && toFindUserByName(empleado)) {
        RequestContext.getCurrentInstance().execute("janal.bloquear();PF('dialogoConfirmacion').show();");
        RequestContext.getCurrentInstance().update("confirmacion");
				this.attrs.put("showConfirmDialog", false);
      } // if  
    } // if  
  }
  
  public boolean isLockField() {
    return ((TcJanalEmpleadosDto)this.attrs.get("tcJanalEmpleadoDto")).isValid();
  }
  
  private boolean toFindUserByName(TcJanalEmpleadosDto empleado) {
    boolean regresar           = false;
    Map<String, String> params  = null;
		try {
			params= new HashMap();
			params.put("nombres", empleado.getNombres());
			params.put("primerApellido", empleado.getPrimerApellido());
			params.put("segundoApellido", empleado.getSegundoApellido());
			empleado= (TcJanalEmpleadosDto) DaoFactory.getInstance().findFirst(TcJanalEmpleadosDto.class, "nombre", params);			
			if(empleado!= null) {
				this.attrs.put("empleado", empleado);
        regresar= true;
			} // if
		} // try // try
		catch(Exception e) {
			JsfBase.addMessageError(e);
      Error.mensaje(e);
		} // exception
		finally {
			Methods.clean(params);
		} // finally
    return regresar;
  }
  
  public void doUpdateEmpleado() {
    ((TcJanalEmpleadosDto)this.attrs.get("tcJanalEmpleadoDto")).setCurp(((TcJanalEmpleadosDto)this.attrs.get("empleado")).getCurp());
    doSearch();
  }
  
}
