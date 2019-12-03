package mx.org.kaana.kajool.procesos.acceso.beans;

import java.io.Serializable;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
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
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.mantic.db.dto.TcManticEmpresasDto;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Autentifica implements Serializable {

  private static final long serialVersionUID = 8226041225011231930L;
  private static final Log LOG = LogFactory.getLog(Autentifica.class);
	private static final String ADMIN= "ADMINISTRADOR";
  private static final String GERENTE= "GERENTE";
  private static final String VENDEDOR_DE_PISO= "VENDEDORDEPISO";
  private Persona persona;
  private Monitoreo monitoreo;
  private String paginaActual;   
  private Credenciales credenciales;
  private EPaginasPrivilegios redirect;
  private Sucursal empresa;
  private List<Sucursal> sucursales;
  private List<UsuarioMenu> topMenu;
  private List<UsuarioMenu> menu;
  private String ultimoAcceso;

  public Autentifica() {
    this(new Persona());
  } // Autentifica

  public Autentifica(Persona persona) {
    this(new Monitoreo(), persona, new Sucursal(serialVersionUID), new ArrayList<UsuarioMenu>(), new ArrayList<UsuarioMenu>(), EPaginasPrivilegios.DEFAULT, new Credenciales(), "/Paginas/Contenedor/bienvenida.jsf");
  } // Autentifica

  public Autentifica(Monitoreo monitoreo, Persona persona, Sucursal empresa, List<UsuarioMenu> menu, List<UsuarioMenu> topMenu, EPaginasPrivilegios redirect, Credenciales credenciales, String paginaActual) {
    this.monitoreo    = monitoreo;
    this.persona      = persona;
    this.empresa      = empresa;
    this.menu         = menu;
    this.topMenu      = topMenu;
    this.redirect     = redirect;
    this.credenciales = credenciales;
    this.paginaActual = paginaActual;
  }	// Autentifica

  public Sucursal getEmpresa() {
    return empresa;
  }  
  
  public String getPaginaActual() {
    return paginaActual;
  }

  public void setPaginaActual(String paginaActual) {
    this.paginaActual = paginaActual;
  }

  public Persona getPersona() {
    return persona;
  }

  public void setEmpleado(Persona persona) {
    this.persona = persona;
  }

  public Monitoreo getMonitoreo() {
    return monitoreo;
  }

  public void setMonitoreo(Monitoreo monitoreo) {
    this.monitoreo = monitoreo;
  }

  public List<UsuarioMenu> getMenu() {
    return menu;
  }

  public void setMenu(List<UsuarioMenu> menu) {
    this.menu = menu;
  }

  
  public EPaginasPrivilegios getRedirect() {
    return redirect;
  }

  public void setRedirect(EPaginasPrivilegios redirect) {
    this.redirect = redirect;
  }

  public Credenciales getCredenciales() {
    return credenciales;
  }

  public void setCredenciales(Credenciales credenciales) {
    this.credenciales = credenciales;
  }

  public String getUltimoAcceso() {
    return ultimoAcceso;
  }

  public List<UsuarioMenu> getModulos() throws SQLException {
    return menu == null ? null : menu;
  }

  public List<UsuarioMenu> getTopModulos() throws SQLException {
    return topMenu == null ? null : topMenu;
  }

  private boolean verificaCredencial() throws Exception {
    return verificaCredencial(this.persona.getContrasenia());
  }

  private boolean verificaCredencial(String contrasenia) throws Exception {
    String frase = BouncyEncryption.decrypt(contrasenia);
    return frase.equals(this.credenciales.getContrasenia());
  }

  private void procesarPermisos() throws Exception {
    Privilegios privilegios = null;
    try {
      privilegios = new Privilegios(this.persona);
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

  private void validaRedirect() throws Exception {
    try {
      if (this.credenciales.validaPerfilesUsuario()) {
        this.redirect = EPaginasPrivilegios.PERFILES;
        this.credenciales.setGrupoPerfiles(true);
        this.credenciales.setMenuEncabezado(false);
      } // if
      else if (this.credenciales.getPerfiles().equals(1L)) {
        loadSucursales();
      } else {
        this.credenciales.setAccesoDelega(true);
        this.redirect = EPaginasPrivilegios.PERFILES;
      } // else			
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
  } // validaRedirect

  public void loadSucursales() throws Exception {
    Privilegios privilegios= null;
    try {
      privilegios= new Privilegios(this.persona);
      this.sucursales= privilegios.toSucursales();
      this.redirect= EPaginasPrivilegios.DEFAULT;
      this.empresa= this.sucursales.get(0);
			this.empresa.setSucursales(this.toLoadSucursales());
			this.empresa.setDependencias(this.toLoadDependencias());
      this.menu= privilegios.procesarModulosPerfil();
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
    boolean regresar = false;
    Map<String, Object> params = null;
    try {
      this.credenciales.setIp(ip);
      this.credenciales.setCuenta(cuenta);
      this.credenciales.setContrasenia(contrasenia);
      params = new HashMap<>();
      LOG.debug("[".concat(cuenta).concat("] Inicia la consulta sobre la vista VistaTcJanalUsuariosDto"));
      params.put("cuenta", cuenta);
      this.persona = (Persona) DaoFactory.getInstance().toEntity(Persona.class, "VistaTcJanalUsuariosDto", "acceso", params);
      if (this.persona != null) {
        regresar = isAdministrador() || verificaCredencial();
        if (regresar) {
          procesarPermisos();
        } // if
				else {
          this.persona = null;
          LOG.info(" No tiene acceso al sistema, favor de verificar esta situación ");
        } // else
      } // if
      else {
        regresar = isDelegaActivo();
      } // else
      if (regresar) {
        this.ultimoAcceso= Fecha.formatear(Fecha.DIA_FECHA_HORA, this.persona.getUltimoAcceso()== null? new Timestamp(Calendar.getInstance().getTimeInMillis()): this.persona.getUltimoAcceso());        
      } // else
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    finally {
      Methods.clean(params);
    } // finally
    return regresar;
  } // tieneAccesoBD
	  
  public boolean validaCambioUsuario(String cuenta, String contrasenia) throws Exception {
    boolean regresar          = false;
    Map<String, Object> params= null;
		List<Persona> personas    = null;
		this.persona              = null;
    try {
      this.credenciales.setCuenta(cuenta);
      this.credenciales.setContrasenia(contrasenia);
      params = new HashMap<>();
      LOG.debug("[".concat(cuenta).concat("] Inicia la consulta sobre la vista VistaTcJanalUsuariosDto"));
      params.put("cuenta", cuenta);
			personas= DaoFactory.getInstance().toEntitySet(Persona.class, "VistaTcJanalUsuariosDto", "cambioUsuarioAutentifica", params);
			if(!personas.isEmpty()) {
				if(personas.size()== 1)
					this.persona= personas.get(0);
				else
					this.persona= toEvaluaJerarquiaPersona(personas);				
			} // if
      if (this.persona != null) {
        regresar = isAdministrador() || verificaCredencial();
        if (regresar) {
          procesarPermisos();
        } else {
          this.persona = null;
          LOG.info(" No tiene acceso al sistema, favor de verificar esta situación ");
        } // else
      }// if
      else 
        regresar = isDelegaActivo();
      if (regresar) 
        this.ultimoAcceso = Fecha.formatear(Fecha.DIA_FECHA_HORA, this.persona== null && this.persona.getUltimoAcceso().equals(null) ? new Timestamp(Calendar.getInstance().getTimeInMillis()) : this.persona.getUltimoAcceso());        
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    finally {
      Methods.clean(params);
    } // finally
    return regresar;
  } // tieneAccesoBD

	private Persona toEvaluaJerarquiaPersona(List<Persona> personas){
		Persona regresar= null;
		regresar= toSeleccionPersona(personas, ADMIN);		
		if(regresar== null)
			regresar= toSeleccionPersona(personas, ADMIN);		
		else{ 
			regresar= toSeleccionPersona(personas, GERENTE);		
			if(regresar== null)
				regresar= toSeleccionPersona(personas, VENDEDOR_DE_PISO);		
			else
				regresar= personas.get(0);
		} // else
		return regresar;		
	} // toEvaluaJerarquiaPersona
	
	private Persona toSeleccionPersona(List<Persona> personas, String perfil){
		Persona regresar= null;
		for(Persona recordPersona: personas){
			if(Cadena.eliminaCaracter(recordPersona.getDescripcionPerfil(), ' ').equals(perfil))
				regresar= recordPersona;
		} // for
		return regresar;
	} // toSeleccionPersona
	
  private boolean isDelegaActivo() throws Exception {
    boolean regresar = false;
    Privilegios privilegios = null;
    List<Entity> delegas = null;
    Entity delega = null;
    try {
      privilegios = new Privilegios();
      delegas = privilegios.toUsersDelegaActivo(this.credenciales.getCuenta());
      if (delegas != null && !delegas.isEmpty()) {
        delega = delegas.get(0);
        regresar = verificaCredencial(delega.toString("contrasenia"));
        if (regresar) {
          if (delegas.size() > 1) {
            createEmpleadoDelega(delega);
            validaRedirect();
          } // if
          else {
            this.persona = privilegios.toPersona(delega.toLong("personaOrigen"), delega.toLong("idPerfil"), delega.toString("cuenta"));
            regresar = this.persona != null;
            if (regresar) {
              loadSucursales();
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
    boolean regresar = false;
    try {
      regresar = BouncyEncryption.decrypt(Configuracion.getInstance().getPropiedad("sistema.administrador")).equals(this.credenciales.getContrasenia());
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
    return regresar;
  } // isAdministrador

  public boolean isFirmado() {
    return (getPersona().getIdUsuario() != null || getPersona().getIdUsuario() > 0L);
  } // isFirmado	

  public void createEmpleadoDelega(Entity persona) {
    this.persona = new Persona();
    this.persona.setIdUsuario(persona.toLong("idUsuario"));
    this.persona.setCuenta(persona.toString("login"));
    this.credenciales.setGrupoPerfiles(true);
    this.credenciales.setPerfilesDelega(0L);
    this.credenciales.setPerfiles(0L);
  } // createEmpleadoDelega

  public void updateEmpleadoDelega(GrupoPerfiles seleccionado) throws Exception {
    Privilegios privilegios = null;
    try {
      privilegios = new Privilegios(this.persona);
      this.persona = privilegios.toPersonaDelega(seleccionado);
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
  } // updateEmpleadoDelega

  public void updateEmpleado(GrupoPerfiles seleccionado) throws Exception {
    Privilegios privilegios = null;
    try {
      privilegios = new Privilegios(this.persona);
      this.persona = privilegios.toPersona(seleccionado);
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
  } // updateEmpleado

  public String redirectMenu() throws Exception {
		String regresar= this.redirectMenu(this.persona.getIdMenu());
    return Cadena.isVacio(regresar)? "/Paginas/Contenedor/bienvenida.jsf".concat(Constantes.REDIRECIONAR): regresar.concat(Constantes.REDIRECIONAR);
  } // toRedirectMenu

  public String redirectMenu(Long idMenu) throws Exception {
    String regresar = null;
    Privilegios privilegios = null;
    try {
      regresar = this.redirect.getPath();
      if (!this.redirect.equals(EPaginasPrivilegios.PERFILES)) {
        if (idMenu!= null) {
          privilegios = new Privilegios();
          regresar = privilegios.toPaginaMenu(idMenu);
        } // if					
      } // if							
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    return regresar;
  } // toRedirectMenu  

	public List<Sucursal> getSucursales() {
		return sucursales;
	}	

	private String toLoadSucursales() throws Exception {
		String regresar           = "";
		Map<String, Object> params= null;
		try {
			params=new HashMap<>();
			params.put("idEmpresa", this.getEmpresa().getIdEmpresaDepende());
			List<TcManticEmpresasDto> items= DaoFactory.getInstance().findViewCriteria(TcManticEmpresasDto.class, params, "sucursales");
			if(items.isEmpty())
				regresar= this.getEmpresa().getIdEmpresa().toString().concat(", ");
			else		
				for (TcManticEmpresasDto item: items) {
					regresar= regresar.concat(item.getIdEmpresa().toString()).concat(", ");
				} // for
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			throw e;
		} // catch
		finally {
			Methods.clean(params);
		} // finally
		return regresar.substring(0, regresar.length()- 2);
	}
	
	private String toLoadDependencias() throws Exception {
		StringBuilder regresar    = null;
		Map<String, Object> params= null;
		try {
			regresar= new StringBuilder("");
			params=new HashMap<>();
			params.put("idEmpresa", this.getEmpresa().getIdEmpresaDepende());
			List<TcManticEmpresasDto> items= DaoFactory.getInstance().findViewCriteria(TcManticEmpresasDto.class, params, "sucursales");
			if(items.isEmpty())
				regresar.append(this.getEmpresa().getIdEmpresa().toString().concat(", "));
			else{		
				for (TcManticEmpresasDto item: items) 
					regresar.append(item.getIdEmpresa()).append(", ");				
			} // else
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			throw e;
		} // catch
		finally {
			Methods.clean(params);
		} // finally
		return regresar.substring(0, regresar.length()- 2);
	} // toLoadDependencias
}
