package com.coolservlets.beans;

import com.coolservlets.beans.tree.Tree;
import com.coolservlets.beans.tree.TreeLeaf;
import com.coolservlets.beans.tree.TreeNode;
import com.coolservlets.beans.tree.TreeObject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.ListIterator;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.procesos.acceso.beans.UsuarioMenu;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * A CSTreeBean is a server-side JSP JavaBean that generates trees populated from a JDBC
 * data source, and renders the trees as HTML. State information is maintained using URI
 * parameters.
 */

public class TreeBean implements Serializable {

  private static final Log LOG              = LogFactory.getLog(TreeBean.class);
	private static final long serialVersionUID=-8132222179370230126L;
  private static Hashtable trees            = new Hashtable();
	
  private List<UsuarioMenu> menu;
  private String treeName   = "default"; // unique name of this tree
  private int maxHijos      = 2;
  private boolean reload    = false;
  private String imagen     = "imagen";
  private String icono      = "icono";
	
  //------------------------------------------------------------------------
  // Getter and Setter methods

   public void setMenu(List<UsuarioMenu> menu) {
     this.menu = menu;
   }

   public List<UsuarioMenu> getMenu() {
     return menu;
   }

  public String getTreeName() {
    return this.treeName;
  }

  public void setTreeName(String str) {
    this.treeName = str;
  }

  public int getMaxHijos() {
    return this.maxHijos;
  }

  public void setMaxHijos(int x) {
    this.maxHijos = x;
  }

  public boolean getReload() {
    return reload;
  }

  public void setReload(boolean reload) {
    this.reload = reload;
  }

  public String getIcono() {
    return icono;
  }

  public void setIcono(String icono) {
    this.icono = icono;
  }

  // Now on to the real stuff...

  /**
       * Populates the tree specified in the bean parameter 'treeName', or the default tree
       * if none was specified. The trees are held statically in a Hashtable, keyed on the tree
       * name.
       *
       * A tree is populated from a denormalised table or a view using JDBC. Ordinarily, this operation
       * happens only once when the tree is first accessed.
       *
       * This method should probably be reorganised to lend itself more to populating from sources other
       * than JDBC.
       */

   public synchronized boolean populateJdbc(List<UsuarioMenu> menu) {
     return populateJdbc(menu,true);
   }

  public synchronized boolean populateJdbc(List<UsuarioMenu> menu, boolean withIcon) {
    if (menu == null)
      LOG.info("Menu nulo el perfil del usuario no tiene ninguna opcion");
    boolean sqlerror = false;
    /** Try to get the named tree from the Hashtable. If no name is provided, use 'default'.
        If a tree matches, then it must already be populated.
    **/
		if(this.treeName== null || this.treeName.length()== 0)
			this.treeName= "default";
		
    if (trees.get(treeName) != null && reload == false)
      return true; // already populated
    TreeNode tn[] = new TreeNode[30];
    int nodeID    = 1;
    Tree root     = new Tree("root");
    try {
      String[] descripciones = new String[100]; // hardwired maximum depth of 30 levels (!!)
      String[] rutas         = new String[100];
      String[] ramaArbol     = new String[100];
      String[] imagen       = new String[100];
      String[] icono          = new String[100];
      int[] idMenu           = new int [100];
      String[] hints         = new String[100];
      String[] publicar      = new String[100];
      String clave = "";
      String token = "";
      int ini;
      int fin;
      int numNiveles = 1;
      int x;
      int j = 0;
      String ceros = "";
      for (int z = 1; z <= maxHijos; z++)
        ceros += "0";
      for(ListIterator<UsuarioMenu> listaIterador=menu.listIterator(); listaIterador.hasNext();){
        x = 0;
        ini = 0;
        fin = maxHijos;
        UsuarioMenu vistaUsuarioMenuDto = listaIterador.next();
        clave = vistaUsuarioMenuDto.getClave();
        token = clave.substring(ini, fin);
        while (!token.equals(ceros)) {
          x++;
          if (x > numNiveles)
            numNiveles++;
          ini += maxHijos;
          fin += maxHijos;
          if (fin <= clave.length())
            token = clave.substring(ini, fin);
          else
            token = ceros;
        } // while
        if (x > 0)
          x--;
        if (ini >= maxHijos)
          ini -= maxHijos;
        if (fin >= maxHijos)
          fin -= maxHijos;
        token            = clave.substring(ini, fin);
        descripciones[x] = vistaUsuarioMenuDto.getDescripcion();
        rutas[x]         = vistaUsuarioMenuDto.getRuta();
        ramaArbol[x]     = vistaUsuarioMenuDto.getClave();
        imagen[x]        = vistaUsuarioMenuDto.getImagen();
        idMenu[x]        = vistaUsuarioMenuDto.getIdMenu().intValue();
        hints[x]         = vistaUsuarioMenuDto.getAyuda();
        publicar[x]      = vistaUsuarioMenuDto.getPublicar();
        icono[x]         = null;
        if (withIcon)
          icono[x]          =vistaUsuarioMenuDto.getIcono();
        int y = x + 1;
        while (y < numNiveles) {
          descripciones[y] = null;
          rutas[y] = null;
          ramaArbol[y] = null;
          imagen[y] = null;
          icono[y] = null;
          y++;
        } // while
        String comparaClave = "";
        String comparaToken = "";
        if (listaIterador.hasNext()) {
          //comparaClave = vistaUsuarioMenuDto.getClave();
          comparaClave = listaIterador.next().getClave();
          comparaToken = comparaClave.substring(ini, fin);
          listaIterador.previous();
        } else {
          comparaToken = " ";
        }
        if ((x == numNiveles) | (!token.equals(comparaToken))) {
          try {
            for (j = 0; j < x; j++) {
              if (tn[j] == null || !ramaArbol[j].equals(tn[j].getRama())) { // then this is the first or a new item
                tn[j] = new TreeNode(nodeID++, descripciones[j], rutas[j], ramaArbol[j], imagen[j], hints[j], publicar[x],withIcon?icono[j]: null);
                if (j > 0)
                  tn[j - 1].addChild(tn[j]);
                else
                  root.addChild(tn[j]);
                continue;
              } // if
            } // for
            if (j > 0)
              tn[j-1].addChild(new TreeLeaf(idMenu[j], descripciones[j], rutas[j], ramaArbol[j], imagen[j], hints[j], publicar[x],withIcon?icono[j]: null));
            else
              root.addChild(new TreeLeaf(descripciones[j], rutas[j], ramaArbol[j], imagen[j], hints[j], publicar[x],withIcon?icono[j]: null));
          }
          catch (Exception e) {
            Error.mensaje(e);
          } // try
        } // if
      } // while
			//root.loadMenu();
      trees.put(treeName, root); // Store the tree in the static Hashtable
    }
    catch (Exception e) {
      sqlerror = true;
    } // try
    return !sqlerror;
  }

  public void setImagen(String imagen) {
    this.imagen = imagen;
  }

  public String getImagen() {
    return imagen;
  }

  public Tree getRoot() {
    return getRoot((treeName == null || treeName.length() == 0)? "default": this.treeName);
  }

  public Tree getRoot(String treeName) {
    return (Tree)trees.get(treeName);
  }
	
  public Tree getTree() {
    if(this.treeName!= null)
      return (Tree)trees.get(this.treeName);
    else
      return null;
  }

  protected final Tree getChildren(Tree tree, String rama) {
		Tree regresar= null;
    for(int i=0; i< tree.size() && regresar== null; i++) {
			TreeObject treeObject = tree.getChild(i);
      if(treeObject.getType()== Tree.NODE) {
        TreeNode node= (TreeNode)treeObject;
        if(!node.getRama().equals(rama))
          regresar= getChildren(node.getChildren(), rama);
        else
          return node.getChildren();
      } // if
    } // for
    return regresar;
  }

  public List<TreeObject> getChildrenNombre(Tree tree, String name) {
    List<TreeObject> regresar= new ArrayList<TreeObject>();
    for(int i=0; i< tree.size() ; i++) {
	    TreeObject treeObject = tree.getChild(i);
      if(treeObject.getType()== Tree.NODE) {
        TreeNode node= (TreeNode)treeObject;
        String nodeName =Cadena.sinAcentos(node.getName().toLowerCase());
        name = Cadena.sinAcentos(Cadena.toLowerCase(name));
        if(nodeName.indexOf(name)<0)
          regresar.addAll((getChildrenNombre(node.getChildren(), name)));
        else
          regresar.add(node);
      } // if
			else {
    	  String nodeName =Cadena.sinAcentos(treeObject.getName().toLowerCase());
    	  name = Cadena.sinAcentos(Cadena.toLowerCase(name));
    	  if(nodeName.indexOf(name)>= 0)
          regresar.add(treeObject);
      } // if
    } // for
    return regresar;
  }
}
