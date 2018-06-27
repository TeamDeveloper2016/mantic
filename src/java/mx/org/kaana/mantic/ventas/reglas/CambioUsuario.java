package mx.org.kaana.mantic.ventas.reglas;

import java.io.Serializable;
import javax.servlet.http.HttpSession;
import mx.org.kaana.kajool.enums.EPaginasPrivilegios;
import mx.org.kaana.kajool.procesos.acceso.beans.Autentifica;
import mx.org.kaana.kajool.procesos.acceso.beans.Cliente;
import mx.org.kaana.kajool.procesos.acceso.exceptions.AccesoDenegadoException;
import mx.org.kaana.kajool.procesos.acceso.exceptions.BloqueoSitioException;
import mx.org.kaana.kajool.procesos.acceso.perfil.reglas.RegistroPerfil;
import mx.org.kaana.kajool.procesos.acceso.reglas.Acceso;
import mx.org.kaana.libs.pagina.JsfBase;

public class CambioUsuario extends Acceso implements Serializable{

	private static final long serialVersionUID=-1750582993624963783L;
	private boolean acceso;	
	
	public CambioUsuario(String cuenta, String contrasenia) {
		this(new Cliente(cuenta, contrasenia, "", "", ""));		
	} // CambioUsuario
		
	public CambioUsuario(Cliente cliente) {
		super(cliente);
		this.acceso= false;
	} // CambioUsuario
	
	public boolean validaUsuario() throws Exception{
		boolean regresar= false;
		try {
			valida();				
			if(this.acceso)
				regresar= JsfBase.getAutentifica().getRedirect().equals(EPaginasPrivilegios.DEFAULT) || JsfBase.getAutentifica().getRedirect().equals(EPaginasPrivilegios.PERFILES);				
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // validaUsuario	
	
	@Override
	public void valida() throws Exception {
    Autentifica autentifica = new Autentifica();
    HttpSession session     = null;
    RegistroPerfil registro = null;    
		autentifica.getCredenciales().setCuentaInicial(getCliente().getCuenta());
    if (autentifica.validaCambioUsuario(getCliente().getCuenta(), getCliente().getContrasenia(), JsfBase.getAutentifica().getEmpresa().getIdEmpresa())) {      
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
	
}
