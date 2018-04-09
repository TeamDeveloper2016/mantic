package mx.org.kaana.kajool.procesos.utilerias.usuariosenlinea.backing;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 14/10/2016
 *@time 10:53:34 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import java.util.List;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.kajool.procesos.beans.DetalleAcceso;
import mx.org.kaana.kajool.procesos.beans.Grupo;
import mx.org.kaana.kajool.procesos.beans.Perfil;
import mx.org.kaana.kajool.procesos.beans.Usuario;
import mx.org.kaana.kajool.procesos.beans.UsuariosEnLinea;
import mx.org.kaana.kajool.procesos.utilerias.usuariosenlinea.reglas.UsuariosLazyModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

@ManagedBean(name="kajoolMantenimientoUtileriasUsuarioFiltro")
@ViewScoped
public class Filtro implements Serializable {

	private static final Log LOG              = LogFactory.getLog(Filtro.class);	
  private static final long serialVersionUID= -5527336954051808568L;
  private UsuariosLazyModel lazyModel;
  private TreeNode grupos;

  public UsuariosLazyModel getLazyModel() {
    return lazyModel;
  }

  public TreeNode getGrupos() {
    return grupos;
  }

  @PostConstruct
  public void init() {
    UsuariosEnLinea usrLinea= null;
    Map sesiones            = null;
    List<Usuario> usuarios  = null;
    try {
			usrLinea= (UsuariosEnLinea) JsfBase.getUsuariosSitio();
			sesiones= usrLinea.getSessiones();
			usuarios= new ArrayList<>();
      Iterator sesion=sesiones.entrySet().iterator();
      loadTree();
      while (sesion.hasNext()) {
        Map.Entry sesionEntry  = (java.util.Map.Entry)  sesion.next();
        Map cuentas = (HashMap) sesionEntry.getValue();
        Iterator cuenta =  cuentas.entrySet().iterator();
         while(cuenta.hasNext()){
           Map.Entry cuentaEntry = (java.util.Map.Entry)cuenta.next();
           usuarios.add((Usuario) cuentaEntry.getValue());
         } // while
      } // while
      this.lazyModel= new UsuariosLazyModel(usuarios);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
  } // init

  private void loadTree() throws Exception {
    Grupo recordGrupo       = null;
    Perfil recordPerfil     = null;
    Iterator gruposEnLinea  = null;
    Iterator perfilesEnLinea= null;
    Map.Entry entry         = null;
    Map.Entry entryPerfiles = null;
		DetalleAcceso acceso    = null;
    try {
			this.grupos= new DefaultTreeNode("root", null);
      gruposEnLinea= (Iterator) JsfBase.getUsuariosSitio().getGrupos().entrySet().iterator();
      while (gruposEnLinea.hasNext()) {
	      entry= (Map.Entry)gruposEnLinea.next();	
        recordGrupo= (Grupo)entry.getValue();
				acceso= new DetalleAcceso(recordGrupo.getIdGrupo(), recordGrupo.tototalPerfiles().longValue(), recordGrupo.getDescripcion());
        TreeNode treeGrupo= new DefaultTreeNode(acceso, this.grupos);
        perfilesEnLinea= recordGrupo.getPerfiles().entrySet().iterator();
        while (perfilesEnLinea.hasNext()) {
          entryPerfiles= (Map.Entry)perfilesEnLinea.next();
          recordPerfil = (Perfil)entryPerfiles.getValue();
					acceso.setTotalUsuarios(acceso.getTotalUsuarios()+ recordPerfil.getTotal().longValue());
          new DefaultTreeNode(new DetalleAcceso(recordPerfil.getIdPerfil(), recordPerfil.getTotal().longValue(), recordPerfil.getTotal().longValue(), recordPerfil.getDescripcion()), treeGrupo);
        } // while
      } // while
    } // try
    catch (Exception e) {
      throw e;
    } // catch
  } // loadTree
}
