package mx.org.kaana.kajool.procesos.acceso.menu.motor;

import com.coolservlets.beans.tree.Tree;
import com.coolservlets.beans.tree.TreeNode;
import com.coolservlets.beans.tree.TreeObject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UtilAplicacion;
import mx.org.kaana.kajool.procesos.acceso.menu.reglas.IBaseMenu;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date Sep 2, 2015
 * @time 12:26:33 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public class Sentinel extends IBaseMenu implements Serializable {

  private static final long serialVersionUID = -8625682254925450037L;

  public Sentinel(Tree root) {
    super(root);
  } // Sentinel

  @Override
  public Object toBuild() {
    try {
      if (getRoot() != null) {
        generar(getRoot().loadChildren());
      } else {
        generar(new ArrayList<TreeObject>());
      }
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
    return getMenu().toString();
  } // toBuild

  private void generar(List<TreeObject> list) throws Exception {
    try {
      if (JsfBase.getAutentifica().getCredenciales().isMenuEncabezado()) {
        for (TreeObject treeObject : list) {
          if (treeObject.getType() == Tree.LEAF) {
            getMenu().append("<li class=\"Fleft BordRadHalf TexAlCenter\"><a href=\"");
            getMenu().append(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath());
            getMenu().append(treeObject.getUrl());
            getMenu().append("\"><i class=\"");
            getMenu().append(treeObject.getIcono());
            getMenu().append("\"></i></a></li>");
          } // if
          else {
            getMenu().append("<li class=\"Fleft BordRadHalf TexAlCenter\"><i class=\"");
            getMenu().append(treeObject.getIcono());
            getMenu().append("\"></i><ul class=\"layout-header-widgets-submenu BordRad5 shadows janal-text-white Animated05\">");
            TreeNode treeNode = (TreeNode) treeObject;
            for (TreeObject treeSubMenu : getRoot().getChildren(treeNode.getChildren())) {
              getMenu().append("<li class=\"Animated05\"><a href=\"");
              getMenu().append(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath());
              getMenu().append(treeSubMenu.getUrl().startsWith("/?") ? "#" : treeSubMenu.getUrl());
              getMenu().append("\" class=\"janal-text-white Unselectable\"><i class=\"");
              getMenu().append(treeSubMenu.getIcono());
              getMenu().append(" janal-text-one-line");
              getMenu().append("\"></i>");
              getMenu().append(treeSubMenu.getName());
              getMenu().append("</a></li>");
            } // for
            getMenu().append("</ul></li>");
          } // else
        } // for
        getMenu().append(toComplementMenu());
      } // if
      else {
        getMenu().append("<li class=\"Fleft BordRadHalf TexAlCenter\"><i class=\"");
        //getMenu().append(JsfBase.getAutentifica().getPersona().getIdTipoSexo.equals(1L) ? "icon-user-male" : "icon-user-female");
        getMenu().append(true ? "icon-user-male" : "icon-user-female");
        getMenu().append("\"></i>");
        getMenu().append("<ul class=\"layout-header-widgets-submenu BordRad5 shadows janal-text-white Animated05\">");
        getMenu().append("<li class=\"Animated05\">");
        getMenu().append("<a href=\"");
        getMenu().append(JsfBase.getFacesContext().getExternalContext().getRequestContextPath());
        getMenu().append("/Exclusiones/salir.jsf\" class=\"janal-text-white Unselectable\"><i class=\"icon-logout\"></i>Salir</a></li>");
        getMenu().append("</ul></li>");
      } // else				
    } // try
    catch (Exception e) {
      throw e;
    } // catch
  } // generar

  private String toComplementMenu() {
    StringBuilder regresar = null;
    String context = null;
    Long cantidadMensajes = 0L;
    try {
      regresar = new StringBuilder();
      context = JsfBase.getFacesContext().getExternalContext().getRequestContextPath();
      regresar.append("<li class=\"Fleft BordRadHalf TexAlCenter\"><i class=\"");
      //regresar.append(JsfBase.getAutentifica().getPersona().getIdTipoSexo().equals(1L) ? "icon-user-male" : "icon-user-female");
      regresar.append(true ? "icon-user-male" : "icon-user-female");
      regresar.append("\"></i>");
      regresar.append("<span id=\"contadorMensajes\" class=\"alertBubble BordRad10 Fs9\"");
      cantidadMensajes = new UtilAplicacion().getContadorMensajes();
      if (cantidadMensajes <= 0) {
        regresar.append(" style=\"display: none\"");
      } // if
      regresar.append(">");
      regresar.append(cantidadMensajes);
      regresar.append("</span>");
      regresar.append("<ul class=\"layout-header-widgets-submenu BordRad5 shadows janal-text-white Animated05\">");
      regresar.append("<li class=\"Animated05\">");
      regresar.append("<a href=\"#\" class=\"white Unselectable\" onclick=\"janal.bloquear();PF('dialogoPerfil').show();\"><i class=\"icon-info-circled-alt\"></i>Usuario</a></li>");
      if (JsfBase.getAutentifica().getCredenciales().isGrupoPerfiles()) {
        regresar.append("<li class=\"Animated05\">");
        regresar.append("<a href=\"").append(context);
        regresar.append("/Paginas/Acceso/Perfil/filtro.jsf\" class=\"janal-text-white Unselectable\"><i class=\"fa fa-group\"></i>Perfiles</a></li>");
      } // if
      /*if (!JsfBase.getAutentifica().getCredenciales().isAccesoDelega()) {
        regresar.append("<li class=\"Animated05\">");
        regresar.append("<a href=\"").append(context);
        regresar.append("/Paginas/Usuarios/Delegar/filtro.jsf\" class=\"janal-text-white Unselectable\"><i class=\"icon-users-2\"></i>Delegar</a></li>");
      } // if*/
      regresar.append("<li class=\"Animated05\">");
      regresar.append("<a href=\"").append(context);
      regresar.append("/Paginas/Mantenimiento/Mensajes/Notificacion/filtro.jsf\" class=\"janal-text-white Unselectable\"><i class=\"icon-mail\"></i>Mensajes</a></li>");      
			regresar.append("<li onclick=\"PF('dlgFaltantes').show();\" class=\"Animated05\">");
      regresar.append("<a class=\"janal-text-white Unselectable\"><i class=\"icon-list-numbered\"></i>Faltantes</a></li>");			
			regresar.append("<li onclick=\"PF('dlgVerificador').show();\" class=\"Animated05\">");
      regresar.append("<a class=\"janal-text-white Unselectable\"><i class=\"icon-list-numbered\"></i>Precios</a></li>");			
			regresar.append("<li onclick=\"PF('dlgCalculadora').show();\" class=\"Animated05\">");
      regresar.append("<a class=\"janal-text-white Unselectable\"><i class=\"icon-calculator\"></i>Calculadora</a></li>");			
			regresar.append("<li class=\"Animated05\">");
      regresar.append("<a href=\"").append(context);
      regresar.append("/Exclusiones/salir.jsf\" class=\"janal-text-white Unselectable\"><i class=\"icon-logout\"></i>Salir</a></li>");
      regresar.append("</ul></li>");
      regresar.append("<li onclick=\"janal.bloquear();PF('wDialogoBuzonSugerencias').show();\" class=\"Fleft BordRadHalf TexAlCenter\"><i class=\"fa fa-inbox fa-1x");
      regresar.append("\"></i></li>");
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
    return regresar.toString();
  } // toComplementMenu
}
