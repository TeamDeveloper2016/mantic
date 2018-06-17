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
import mx.org.kaana.libs.formato.BouncyEncryption;
import mx.org.kaana.libs.pagina.JsfBase;

public class CambioUsuario extends Acceso implements Serializable{

	private String contraseniaUsuario;
	private Long idPerfil;	
	
	public CambioUsuario(String cuenta, String contrasenia, String contraseniaUsuario, Long idPerfil) {
		this(new Cliente(cuenta, contrasenia, "", "", ""), contraseniaUsuario, idPerfil);		
	}
		
	public CambioUsuario(Cliente cliente, String contraseniaUsuario, Long idPerfil) {
		super(cliente);
		this.contraseniaUsuario= contraseniaUsuario;
		this.idPerfil          = idPerfil;
	}
	
	public boolean validaUsuario() throws Exception{
		boolean regresar       = false;
		try {
			if(verificaCredencial()){
				valida();
				JsfBase.getAutentifica().loadSucursales();
				regresar= JsfBase.getAutentifica().getRedirect().equals(EPaginasPrivilegios.DEFAULT) || JsfBase.getAutentifica().getRedirect().equals(EPaginasPrivilegios.PERFILES);				
			} // if
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
    if (autentifica.validaCambioUsuario(getCliente().getCuenta(), getCliente().getContrasenia(), this.idPerfil, JsfBase.getAutentifica().getEmpresa().getIdEmpresa())) {      
			if (JsfBase.isLockUsers()) {
        throw new BloqueoSitioException();
      }
      registro= new RegistroPerfil(autentifica);
      session = JsfBase.getSession();
      registro.addAutentifica(session);
      agregarUsuariosSitio(session, autentifica);
      registro.addMenuSesion(session);
      registro.addTopMenuSesion(session);
    }//if
    else {
      if (session != null) {
        synchronized (session) {
          JsfBase.cleanSesion(session);
        } // synchronized
      }
      throw new AccesoDenegadoException();
    } // else
  } // valida
	
	private boolean verificaCredencial() throws Exception {
    String frase = BouncyEncryption.decrypt(this.contraseniaUsuario);
    return frase.equals(getCliente().getContrasenia());
  } // verificaCredencial
}
