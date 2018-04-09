package mx.org.kaana.kajool.procesos.acceso.perfil.reglas;

import com.coolservlets.beans.TreeBean;
import java.io.Serializable;
import javax.servlet.http.HttpSession;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.dto.TcJanalEmpleadosDto;
import mx.org.kaana.kajool.db.dto.TcJanalUsuariosDto;
import mx.org.kaana.kajool.procesos.acceso.beans.Autentifica;
import mx.org.kaana.kajool.procesos.mantenimiento.temas.backing.TemaActivo;

public class RegistroPerfil implements Serializable{

	private static final long serialVersionUID= 2310097990755216904L;	
  private Autentifica autentifica;
	private TemaActivo temaActivo;
	
	public RegistroPerfil(Autentifica autentifica) {
		this(autentifica, null);
	} // RegistroPerfil
	
	public RegistroPerfil(Autentifica autentifica, TemaActivo temaActivo) {
		this.autentifica= autentifica;		
		this.temaActivo = temaActivo;
	}	// RegistroPerfil
	
	public void addAutentifica(HttpSession sesion) throws Exception {			
		synchronized (sesion) {
			sesion.setAttribute(Constantes.ATRIBUTO_AUTENTIFICA, autentifica);
		} // synchronized
	} // addAutentifica
	
	public void addMenuSesion(HttpSession sesion) throws Exception {			
		synchronized (sesion) {        			
			TreeBean tree = new TreeBean();
			tree.setReload(true);
			tree.setTreeName("janal-"+ this.autentifica.getEmpleado().getIdPerfil());
			tree.populateJdbc(this.autentifica.getModulos());		
			sesion.setAttribute("tree", tree);			
		} // synchronized
  } // addMenuSesion
	
	public void addTopMenuSesion(HttpSession session) throws Exception {
		synchronized (session) {        			
			TreeBean tree = new TreeBean();
			tree.setReload(true);
			tree.setTreeName("kajool encabezado-"+ this.autentifica.getEmpleado().getIdPerfil());
			tree.populateJdbc(this.autentifica.getTopModulos());
			session.setAttribute("treeEncabezado", tree);	
		} // synchronized
  } // addTopMenuSesion
	
  public void updateTheme() {    		
    TcJanalUsuariosDto usuario  = null;    		
		TcJanalEmpleadosDto empleado= null;
    try {			
			usuario = (TcJanalUsuariosDto) DaoFactory.getInstance().findById(TcJanalUsuariosDto.class, this.autentifica.getEmpleado().getIdUsuario());						
			empleado= (TcJanalEmpleadosDto) DaoFactory.getInstance().findById(TcJanalEmpleadosDto.class, this.autentifica.getEmpleado().getIdEmpleado());						
			this.temaActivo.setName(usuario!= null && !Cadena.isVacio(empleado.getEstilo()) ? empleado.getEstilo() : Constantes.TEMA_INICIAL);
    } // try // try
    catch(Exception e) {
      Error.mensaje(e);
    } // catch    		
  } // updateTheme
}
