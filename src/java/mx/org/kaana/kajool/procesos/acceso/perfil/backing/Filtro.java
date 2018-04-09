package mx.org.kaana.kajool.procesos.acceso.perfil.backing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpSession;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.reflection.Methods;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EMenus;
import mx.org.kaana.kajool.procesos.acceso.beans.GrupoPerfiles;
import mx.org.kaana.kajool.procesos.acceso.beans.Autentifica;
import mx.org.kaana.kajool.procesos.acceso.beans.Credenciales;
import mx.org.kaana.kajool.procesos.acceso.beans.Empleado;
import mx.org.kaana.kajool.procesos.acceso.reglas.Privilegios;
import mx.org.kaana.kajool.procesos.acceso.perfil.reglas.RegistroPerfil;
import mx.org.kaana.kajool.procesos.acceso.reglas.Transaccion;
import mx.org.kaana.kajool.procesos.beans.Usuario;
import mx.org.kaana.kajool.procesos.beans.UsuariosEnLinea;
import mx.org.kaana.kajool.procesos.mantenimiento.temas.backing.TemaActivo;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 14/09/2015
 *@time 06:21:57 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

@ManagedBean(name = "kajoolAccesoPerfilFiltro")
@ViewScoped
public class Filtro extends IBaseAttribute implements Serializable {

	private static final long serialVersionUID=312478692664202327L;	
	private List<GrupoPerfiles> gruposDelega;
	private List<GrupoPerfiles> grupos;
	private GrupoPerfiles grupoPerfil;		  	
	@ManagedProperty(value="#{kajoolTemaActivo}")
	private TemaActivo temaActivo;
	private TreeNode rootDelega;  	
	private TreeNode root;  		

	public TemaActivo getTemaActivo() {
		return temaActivo;
	}

	public void setTemaActivo(TemaActivo temaActivo) {
		this.temaActivo= temaActivo;
	}	
	
	public TreeNode getRoot() {
		return root;
	}

	public void setRoot(TreeNode root) {
		this.root = root;
	}

	public GrupoPerfiles getGrupoPerfil() {
		return grupoPerfil;
	}

	public void setGrupoPerfil(GrupoPerfiles grupoPerfil) {
		this.grupoPerfil = grupoPerfil;
	}	

	public TreeNode getRootDelega() {
		return rootDelega;
	}

	public void setRootDelega(TreeNode rootDelega) {
		this.rootDelega = rootDelega;
	}	
	
	@Override
	@PostConstruct
	protected void init() {
		Credenciales credenciales= null;
		try{		
			this.grupos      = new ArrayList<>();		
			this.gruposDelega= new ArrayList<>();		
			credenciales= JsfBase.getAutentifica().getCredenciales();
			credenciales.setMenuEncabezado(false);
			credenciales.setAccesoDelega(false);
			clearMenus();			
			this.attrs.put("cuenta", credenciales.getCuentaInicial());						
			validaEmpleado();
			/*if(credenciales.isAccesoDelega())
				inicializaArbolPrivilegios();		
			else{	*/			
				//inicializaArbolPrivilegios();
				inicializaArbolGrupos();			
			//} // else					
			this.attrs.put("delega", !this.gruposDelega.isEmpty());		
			this.attrs.put("mostrarTreeProyectos", !this.grupos.isEmpty());
			updateSession();
		} // try
		catch(Exception e){
			JsfBase.addMessageError(e);
			Error.mensaje(e);
		}
	} // init
	
	private void clearMenus(){
		HttpSession session= null;
		Object menu        = null;
		try {
			session= JsfBase.getSession();
			for(EMenus emenu: EMenus.values()){
				menu= session.getAttribute(emenu.getVariableSesion());
				if(menu!= null)
					session.setAttribute(emenu.getVariableSesion(), null);
			} // for
			menu= session.getAttribute("treeEncabezado");
			if(menu!= null)
					session.setAttribute("treeEncabezado", null);
		} // try
		catch (Exception e) {						
			throw e;
		} // catch		
	} // clearMenus
	
	private void validaEmpleado(){
		Autentifica autentifica= null;
		Privilegios privilegio = null;
		Long idEmpleado        = -1L;
		try {
			autentifica= JsfBase.getAutentifica();			
			privilegio = new Privilegios();
			idEmpleado = privilegio.getIdEmpleado(autentifica.getCredenciales().getCuentaInicial());
			autentifica.getEmpleado().setIdEmpleado(idEmpleado);
		} // try
		catch (Exception e) {						
			Error.mensaje(e);			
		} // catch				
	} // validaEmpleado
	
	private void inicializaArbolGrupos(){
		setRoot(new DefaultTreeNode("root", null));
		crearArbolGrupos(getRoot());		
	} // inicializaArbolGrupos
	
	private void inicializaArbolPrivilegios(){
		setRootDelega(new DefaultTreeNode("root", null));		
		crearArbolDelega(getRootDelega());				
	} // inicializaArbolPrivilegios
	
	private void updateSession(){		
		Transaccion transaccion= null;
		try{			
			transaccion= new Transaccion(JsfBase.getSessionId());
			transaccion.ejecutar(EAccion.COMPLEMENTAR);
		} // try
		catch(Exception e){
			Error.mensaje(e);
		} // catch
	} // doUpdate	
		
	private void crearArbolGrupos(TreeNode padre) {		
		Privilegios privilegios= null;
		Empleado empleado      = null;
		try {
			empleado= JsfBase.getAutentifica().getEmpleado();
			privilegios= new Privilegios(empleado);
			this.grupos= privilegios.toGrupos();
			for (GrupoPerfiles grupo : this.grupos) {
				TreeNode treeEnc= new DefaultTreeNode(grupo, padre);
				crearArbolPerfiles(treeEnc, grupo.getIdGrupo(), empleado.getIdEmpleado());
			} // for
		} // try
		catch (Exception e) {
			Error.mensaje(e);
		} // catch
	} // crearArbolGrupos
	
	private void crearArbolDelega(TreeNode padre) {						
		List<GrupoPerfiles> usuarios= null;				
		Privilegios privilegio      = null;
		Empleado empleado           = null;		
		try {						
			empleado  = JsfBase.getAutentifica().getEmpleado();		
			privilegio= new Privilegios(empleado);
			usuarios  = privilegio.toUsuariosDelega();			
			if(usuarios!= null){
				for (GrupoPerfiles item : usuarios) {
					TreeNode treeUsuario= new DefaultTreeNode(item, padre);									
					this.gruposDelega= privilegio.toGruposDelega(item.getCuenta());
					if(!this.gruposDelega.isEmpty()){
						for(GrupoPerfiles proyecto: this.gruposDelega){
							TreeNode treeProyecto= new DefaultTreeNode(proyecto, treeUsuario);
							crearArbolPerfilesDelega(treeProyecto, proyecto.getIdGrupo(), item.getCuenta(), empleado.getIdUsuario());
						} // for
					} // if
				} // for
			} // if
		} // try
		catch (Exception e) {
			Error.mensaje(e);
		} // catch
		finally{
			Methods.clean(usuarios);
		} // finally
	} // crearArbolDelega

	private void crearArbolPerfiles(TreeNode padre, Long idGrupo, Long idEmpleado) {		
		Privilegios privilegios     = null;
		List<GrupoPerfiles> perfiles= null;
		try {
			privilegios= new Privilegios();
			perfiles   = privilegios.toPerfiles(idGrupo, idEmpleado);
      if (!perfiles.isEmpty()) {
			  for (GrupoPerfiles perfil: perfiles)
				  new DefaultTreeNode(perfil, padre);            			
      }// if
		} // try
		catch (Exception e) {
			Error.mensaje(e);
		} // catch		
	} // crearArbolPerfiles
	
	private void crearArbolPerfilesDelega(TreeNode padre, Long idGrupo, String cuenta, Long idEmpleado){
		Privilegios privilegio      = null;
		List<GrupoPerfiles> perfiles= null;
		try {
			privilegio= new Privilegios();
			perfiles  = privilegio.toPerfilesDelega(idGrupo, cuenta, idEmpleado);
			for (GrupoPerfiles perfil: perfiles)
				new DefaultTreeNode(perfil, padre);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
		} // catch		
	} // crearArbolPerfilesDelega

	public String doActualizarValores(){
		String regresar        = null;				
		Autentifica autentifica= null;		
		RegistroPerfil perfil  = null;					
		try {									
			autentifica= JsfBase.getAutentifica();									
			/*if(this.grupoPerfil.isTipoAccesoDelega()){
				autentifica.updateEmpleadoDelega(this.grupoPerfil);						
				autentifica.getCredenciales().setAccesoDelega(true);
			} // if
			else */
			autentifica.updateEmpleado(this.grupoPerfil);
			autentifica.getCredenciales().setMenuEncabezado(true);			
			autentifica.loadEscuelas();								
			perfil= new RegistroPerfil(autentifica, this.temaActivo);						
			perfil.addMenuSesion(JsfBase.getSession());						
			perfil.addTopMenuSesion(JsfBase.getSession());						
			perfil.updateTheme();			
      updateUsuarioEnLinea();
			regresar= autentifica.redirectMenu(this.grupoPerfil.getIdMenu());			
			new Transaccion().ejecutar(EAccion.AGREGAR);					
		}// try
		catch (Exception e) {
			Error.mensaje(e);
			regresar= "/indice".concat(Constantes.REDIRECIONAR);			
      JsfBase.addMessageError(e);
		} // catch
		return regresar;
	} // doActualizarValores
	
  private void updateUsuarioEnLinea() throws Exception {
		UsuariosEnLinea usuarioEnLinea= null;
		Usuario usuario               = null;
		usuarioEnLinea                = JsfBase.getUsuariosSitio();
		usuario                       = usuarioEnLinea.getCuenta(JsfBase.getSession().getId(), JsfBase.getAutentifica().getEmpleado().getCuenta());
		if (usuario != null) {
      usuarioEnLinea.removeUsuarioPerfil(usuario.getIdGrupo(),usuario.getIdPerfil());
      usuario.setIdGrupo(getGrupoPerfil().getIdGrupo());
			usuario.setIdPerfil(getGrupoPerfil().getIdPerfil());
			usuario.setPerfil(getGrupoPerfil().getPerfil());            			
      usuarioEnLinea.refresh(usuario.getIdGrupo(),usuario.getIdPerfil(), usuario.getGrupo(),usuario.getPerfil());
		} // if
	}
  
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		Methods.clean(this.grupos);
		Methods.clean(this.gruposDelega);	
	} // finalize
}
