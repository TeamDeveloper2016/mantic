package mx.org.kaana.kajool.procesos.acceso.beans;

import java.io.Serializable;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.BouncyEncryption;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.catalogos.backing.Monitoreo;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.procesos.acceso.reglas.Privilegios;
import mx.org.kaana.kajool.enums.EPaginasPrivilegios;
import mx.org.kaana.libs.formato.Fecha;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Autentifica implements Serializable {

  private static final long serialVersionUID= 8226041225011231930L;
  private static final Log LOG= LogFactory.getLog(Autentifica.class);	
  private Empleado empleado;
	private Monitoreo monitoreo;
	private String paginaActual;
  private CentroTrabajo escuela;
	private List<UsuarioMenu> menu;
	private List<UsuarioMenu> topMenu;
	private Credenciales credenciales;	
	private EPaginasPrivilegios redirect;	
	private List<CentroTrabajo> escuelas;	
  private String ultimoAcceso;

  public Autentifica() {
    this(new Empleado());
  } // Autentifica

  public Autentifica(Empleado persona) {
		this(new Monitoreo(), persona, new CentroTrabajo(), new ArrayList<UsuarioMenu>(), new ArrayList<UsuarioMenu>(), EPaginasPrivilegios.DEFAULT, new Credenciales(), "/Paginas/Contenedor/bienvenida.jsf");
	} // Autentifica

	public Autentifica(Monitoreo monitoreo, Empleado empleado, CentroTrabajo escuela, List<UsuarioMenu> menu, List<UsuarioMenu> topMenu, EPaginasPrivilegios redirect, Credenciales credenciales, String paginaActual) {	
		this.monitoreo   = monitoreo;
		this.empleado    = empleado;
		this.escuela     = escuela;
		this.menu        = menu;
		this.topMenu     = topMenu;
		this.redirect    = redirect;
		this.credenciales= credenciales;
    this.paginaActual= paginaActual;
	}	// Autentifica

  public String getPaginaActual() {
    return paginaActual;
  }

  public void setPaginaActual(String paginaActual) {
    this.paginaActual = paginaActual;
  }

  public Empleado getEmpleado() {
    return empleado;
  }

  public void setEmpleado(Empleado persona) {
    this.empleado= persona;
  }

  public Monitoreo getMonitoreo() {
    return monitoreo;
  }

  public void setMonitoreo(Monitoreo monitoreo) {
    this.monitoreo= monitoreo;
  }
	
	public List<UsuarioMenu> getMenu() {
		return menu;
	}

	public void setMenu(List<UsuarioMenu> menu) {
		this.menu= menu;
	}
	
	public CentroTrabajo getCentroTrabajo() {
    return escuela;
  }

  public void setCentroTrabajo(CentroTrabajo centroTrabajo) {
    this.escuela= centroTrabajo;
  }

	public EPaginasPrivilegios getRedirect() {
		return redirect;
	}

	public void setRedirect(EPaginasPrivilegios redirect) {
		this.redirect= redirect;
	}		

	public Credenciales getCredenciales() {
		return credenciales;
	}

	public void setCredenciales(Credenciales credenciales) {
		this.credenciales= credenciales;
	}		

  public String getUltimoAcceso() {
    return ultimoAcceso;
  }

	public List<UsuarioMenu> getModulos() throws SQLException {
		return menu== null ? null : menu;
	}
	
	public List<UsuarioMenu> getTopModulos() throws SQLException {
		return topMenu== null ? null : topMenu;
	}
	
  private boolean verificaCredencial() throws Exception {
		return verificaCredencial(this.empleado.getContrasenia());
	}
	
  private boolean verificaCredencial(String contrasenia) throws Exception {
    String frase= BouncyEncryption.decrypt(contrasenia);
    return frase.equals(this.credenciales.getContrasenia());
  }

  private void procesarPermisos() throws Exception{
    Privilegios privilegios= null;    	
    try {
      privilegios= new Privilegios(this.empleado);
			this.credenciales.setPerfilesDelega(privilegios.verificarDelega());
      this.credenciales.setPerfiles(privilegios.verificarPerfiles());
			validaRedirect();
    }// try
    catch (Exception e) {
      throw e;
    }// catcth
    finally {
      privilegios = null;
    }// finally
  } // procresaCentroTrabajo

	private void validaRedirect() throws Exception{				
		try {
			if(this.credenciales.validaPerfilesUsuario()){
				this.redirect= EPaginasPrivilegios.PERFILES;
				this.credenciales.setGrupoPerfiles(true);
				this.credenciales.setMenuEncabezado(false);
			} // if
			else if(this.credenciales.getPerfiles().equals(1L))
				loadEscuelas();
			else{
				this.credenciales.setAccesoDelega(true);
				this.redirect= EPaginasPrivilegios.PERFILES;								
			} // else			
		} // try
		catch (Exception e) {						
			throw e;
		} // catch		
	} // validaRedirect

	public void loadEscuelas() throws Exception{
		Privilegios privilegios= null;
		try {
			privilegios  = new Privilegios(this.empleado);
			//this.escuelas= privilegios.toEscuelas();
			//if(this.escuelas.size()> 1)
			//	this.redirect= EPaginasPrivilegios.ESCUELAS;
			//else
			this.redirect= EPaginasPrivilegios.DEFAULT;
      this.escuela= null;
      this.menu   = privilegios.procesarModulosPerfil();
      this.topMenu= privilegios.procesarTopModulos();
      if (this.menu.isEmpty() && this.topMenu.isEmpty()) {
				LOG.info(" Error: El usuario no tiene acceso a ningun modulo.");
				Error.mensaje(new Exception("El usuario no tiene acceso a ninguna opción del sistema"));
			} // if
		} // try
		catch (Exception e) {									
			throw e;
		} // catch		
	} // loadEscuelas
	
  public boolean tieneAccesoBD(String cuenta, String contrasenia, String ip) throws Exception {
    boolean regresar          = false;
    Map<String, Object> params= null;
    try {
      this.credenciales.setIp(ip);
      this.credenciales.setCuenta(cuenta);
      this.credenciales.setContrasenia(contrasenia);
      params= new HashMap<>();
      LOG.debug("[".concat(cuenta).concat("] Inicia la consulta sobre la vista VistaTcJanalUsuariosDto"));
      params.put("cuenta", cuenta);
      this.empleado= (Empleado) DaoFactory.getInstance().toEntity(Empleado.class,"VistaTcJanalUsuariosDto", "acceso", params);
      if (this.empleado != null) {
        regresar= isAdministrador() || verificaCredencial();
        if (regresar)
          procesarPermisos();
        else {
          this.empleado= null;
          LOG.info(" No tiene acceso al sistema, favor de verificar esta situación ");
        } // else
      }// if
			else
				regresar= isDelegaActivo();
      if(regresar)
        toUltimoAcceso();
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    finally {
      Methods.clean(params);
    } // finally
    return regresar;
  } // tieneAccesoBD

	private boolean isDelegaActivo() throws Exception{
		boolean regresar       = false;
		Privilegios privilegios= null;
		List<Entity> delegas   = null;
		Entity delega          = null;		
		try {
			privilegios= new Privilegios();
			delegas= privilegios.toUsersDelegaActivo(this.credenciales.getCuenta());
			if(delegas!= null && !delegas.isEmpty()){				
				delega= delegas.get(0);
				regresar= verificaCredencial(delega.toString("contrasenia"));
				if(regresar){				
					if(delegas.size()> 1){
						createEmpleadoDelega(delega);
						validaRedirect();		
					} // if
					else{										
						this.empleado= privilegios.toEmpleado(delega.toLong("empleadoOrigen"), delega.toLong("idPerfil"), delega.toString("cuenta"));
						regresar= this.empleado!= null;
						if(regresar){
							loadEscuelas();																				
							this.credenciales.setAccesoDelega(true);
						} // if
					} // else				
				} // if					
			} // if
		} // try
		catch (Exception e) {						
			throw e;
		} // catch		
		return regresar;
	} // isDelegaActivo
	
  private boolean isAdministrador() {
    boolean regresar= false;		
    try {			
      regresar= BouncyEncryption.decrypt(Configuracion.getInstance().getPropiedad("sistema.administrador")).equals(this.credenciales.getContrasenia());
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
    return regresar;
  } // isAdministrador

  public boolean isFirmado() {
    return (getEmpleado().getIdUsuario()!= null || getEmpleado().getIdUsuario()> 0L);
  } // isFirmado	
	
	public void createEmpleadoDelega(Entity empleado){
		this.empleado= new Empleado();		
		this.empleado.setIdUsuario(empleado.toLong("idUsuario"));
		this.empleado.setCuenta(empleado.toString("login"));
		this.credenciales.setGrupoPerfiles(true);
		this.credenciales.setPerfilesDelega(0L);
		this.credenciales.setPerfiles(0L);
	} // createEmpleadoDelega
	
	public void updateEmpleadoDelega(GrupoPerfiles seleccionado) throws Exception{
		Privilegios privilegios= null;
		try {
			privilegios= new Privilegios(this.empleado);			
			this.empleado= privilegios.toEmpleadoDelega(seleccionado);
		} // try
		catch (Exception e) {						
			throw e;
		} // catch		
	} // updateEmpleadoDelega
	
	public void updateEmpleado(GrupoPerfiles seleccionado) throws Exception{
		Privilegios privilegios= null;
		try {
			privilegios= new Privilegios(this.empleado);			
			this.empleado= privilegios.toEmpleado(seleccionado);
		} // try
		catch (Exception e) {						
			throw e;
		} // catch		
	} // updateEmpleado
	
	public String redirectMenu() throws Exception{
		return redirectMenu(this.empleado.getIdMenu()).concat(Constantes.REDIRECIONAR);
	} // toRedirectMenu
	
	public String redirectMenu(Long idMenu) throws Exception{
		String regresar        = null;		
		Privilegios privilegios= null;
		try {
			regresar= this.redirect.getPath();
			if(!this.redirect.equals(EPaginasPrivilegios.PERFILES)){				
				if(idMenu!= null){
					privilegios= new Privilegios();
					regresar= privilegios.toPaginaMenu(idMenu);
				} // if					
			} // if							
		} // try
		catch (Exception e) {						
			throw e;
		} // catch		
		return regresar;
	} // toRedirectMenu
  
  protected void toUltimoAcceso() {
    Timestamp fecha          = null;
    Map<String, Object>params= null;
    try {
      params= new HashMap<>();
      params.put("idUsuario", this.getEmpleado().getIdUsuario());
      fecha= DaoFactory.getInstance().toField("TrJanalSesionesDto", "ultimoAcceso", params, "registroInicio").toTimestamp();
      if(fecha!= null)
        this.ultimoAcceso= Fecha.formatear(Fecha.DIA_FECHA_HORA, fecha);
    } // try
    catch (Exception e) {      
      Error.mensaje(e);
    } // catch    
    finally{
      Methods.clean(params);
    } // finally        
  } // toultimoAcceso
  
}
