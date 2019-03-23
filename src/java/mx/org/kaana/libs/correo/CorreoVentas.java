package mx.org.kaana.libs.correo;

import javax.mail.PasswordAuthentication;
import mx.org.kaana.libs.formato.Encriptar;
import mx.org.kaana.libs.recurso.TcConfiguraciones;

public class CorreoVentas extends javax.mail.Authenticator {

	@Override
	protected PasswordAuthentication getPasswordAuthentication() {
		Encriptar encriptado= new Encriptar();    
    String cuenta       = TcConfiguraciones.getInstance().getPropiedad("correo.mantic.ventas.cuenta");    
    String password     = encriptado.desencriptar(TcConfiguraciones.getInstance().getPropiedad("correo.mantic.ventas.pass"), Encriptar._CLAVE);    
    return new PasswordAuthentication(cuenta, password);
	} // getPasswordAuthentication
	
}
