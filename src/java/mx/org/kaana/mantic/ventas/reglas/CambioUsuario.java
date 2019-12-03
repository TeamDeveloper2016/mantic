package mx.org.kaana.mantic.ventas.reglas;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EPaginasPrivilegios;
import mx.org.kaana.kajool.procesos.acceso.beans.Autentifica;
import mx.org.kaana.kajool.procesos.acceso.beans.Cliente;
import mx.org.kaana.kajool.procesos.acceso.beans.Persona;
import mx.org.kaana.kajool.procesos.acceso.exceptions.AccesoDenegadoException;
import mx.org.kaana.kajool.procesos.acceso.exceptions.BloqueoSitioException;
import mx.org.kaana.kajool.procesos.acceso.perfil.reglas.RegistroPerfil;
import mx.org.kaana.kajool.procesos.acceso.reglas.Acceso;
import mx.org.kaana.libs.formato.BouncyEncryption;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.reflection.Methods;

public class CambioUsuario extends Acceso implements Serializable{

	private static final long serialVersionUID=-1750582993624963783L;
	private boolean acceso;	
	private Long idPersona;
	
	public CambioUsuario(String cuenta, String contrasenia) {
		this(new Cliente(cuenta, contrasenia, "", "", ""));		
	} // CambioUsuario
		
	public CambioUsuario(Cliente cliente) {
		super(cliente);
		this.acceso= false;
	} // CambioUsuario

	public boolean isAcceso() {
		return acceso;
	}

	public void setAcceso(boolean acceso) {
		this.acceso=acceso;
	}

	public Long getIdPersona() {
		return idPersona;
	}

	public void setIdPersona(Long idPersona) {
		this.idPersona=idPersona;
	}

	public boolean validaUsuario(){
		boolean regresar= false;
		try {
			valida();				
			if(this.acceso)
				regresar= JsfBase.getAutentifica().getRedirect().equals(EPaginasPrivilegios.DEFAULT) || JsfBase.getAutentifica().getRedirect().equals(EPaginasPrivilegios.PERFILES);				
		} // try
		catch (Exception e) {			
			Error.mensaje(e);
		} // catch		
		return regresar;
	} // validaUsuario	
	
	@Override
	public void valida() throws Exception {
    Autentifica autentifica = new Autentifica();
    HttpSession session     = null;
    RegistroPerfil registro = null;    
		autentifica.getCredenciales().setCuentaInicial(getCliente().getCuenta());
    if (autentifica.validaCambioUsuario(getCliente().getCuenta(), getCliente().getContrasenia())) {      
			if (JsfBase.isLockUsers()) {
        throw new BloqueoSitioException();
      } // if
			session = JsfBase.getSession();
			session.setAttribute("mmenu", null);
			session.setAttribute("encabezado", null);
			session.setAttribute("sentinel", null);
			autentifica.getCredenciales().setMenuEncabezado(true);
			autentifica.loadSucursales();
      registro= new RegistroPerfil(autentifica);      
      registro.addAutentifica(session);
      agregarUsuariosSitio(session, autentifica);
      registro.addMenuSesion(session);
      registro.addTopMenuSesion(session);
			this.setIdPersona(autentifica.getPersona().getIdPersona());
			this.acceso= true;
    } // if
    else {
			this.acceso= false;
      if (session != null) {
        synchronized (session) {
          JsfBase.cleanSesion(session);
        } // synchronized
      } // if
      throw new AccesoDenegadoException();
    } // else
  } 
	
	public boolean validaPrivilegiosDescuentos() throws Exception {
    boolean regresar          = false;
    Map<String, Object> params= null;
		Persona persona           = null;
    try {      
      params = new HashMap<>();
      params.put("cuenta", getCliente().getCuenta());
      persona = (Persona) DaoFactory.getInstance().toEntity(Persona.class, "VistaTcJanalUsuariosDto", "acceso", params);
      if (persona != null) {
				this.setIdPersona(persona.getIdPersona());
        regresar = verificaPerfil(persona) && verificaCredencial(getCliente().getContrasenia(), persona.getContrasenia()); 
			} // if
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    finally {
      Methods.clean(params);
    } // finally
    return regresar;
	} // validaPrivilegiosDescuentos
	
	private boolean verificaCredencial(String contrasenia, String contraseniaPersona) throws Exception {
    String frase = BouncyEncryption.decrypt(contraseniaPersona);
    return frase.equals(contrasenia);
  }
	
	private boolean verificaPerfil(Persona persona) throws Exception {
		boolean regresar         = false;
		List<Entity> perfiles    = null;
		Map<String, Object>params= null;
		int count                = 0;
		try {
			params= new HashMap<>();
			params.put("idPersona", persona.getIdPersona());
			perfiles= DaoFactory.getInstance().toEntitySet("VistaGruposAccesoDto", "perfilesPersona", params);
			if(!perfiles.isEmpty()){
				for(Entity perfil: perfiles){
					if(perfil.toString("descripcion").toUpperCase().equals("ADMINISTRADOR DE ENCUESTA") || perfil.toString("descripcion").toUpperCase().equals("GERENTE") || perfil.toString("descripcion").toUpperCase().equals("CAJERO"))
						count++;
				} // for
				regresar= count > 0;
			} // if
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
		return regresar;
	} // verificaPerfil
}
