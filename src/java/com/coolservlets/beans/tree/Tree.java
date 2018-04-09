
/*
 *  Tree.java
 */
package com.coolservlets.beans.tree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import com.coolservlets.beans.menu.MenuModel;
import mx.org.kaana.kajool.procesos.acceso.menu.reglas.CleanFlashListener;
import org.primefaces.component.menuitem.UIMenuItem;
import org.primefaces.component.separator.UISeparator;
import org.primefaces.component.submenu.UISubmenu;
import mx.org.kaana.libs.formato.Error;

public class Tree implements TreeInterface, Serializable {

  private static final long serialVersionUID = -7154922158674801011L;

  private Vector children;
  private int selected;
  private String name;
  public static int NODE = 0;
  public static int LEAF = 1;
  private MenuModel model;

  public Tree() {
    this(null);
  }

  public Tree(String name) {
    this.name = name;
    children = new Vector();
    this.model = new MenuModel();
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getSelected() {
    return this.selected;
  }

  public void setSelected(int selected) {
    this.selected = selected;
  }

  @Override
  public void addChild(TreeObject child) {
    children.addElement(child);
  }

  public TreeObject getChild(int index) {
    return (TreeObject) children.elementAt(index);
  }

  public int size() {
    return children.size();
  }

  public MenuModel getModel() {
    return model;
  }

  public void loadMenu() {
    try {
      if (children != null && !this.children.isEmpty()) {
        build(getChildren(((TreeNode) this.getChild(0)).getChildren()), null);
      }
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
  }

  public List<TreeObject> loadChildren() {
    List<TreeObject> regresar = new ArrayList<>();
    try {
      if (this.children.size() > 0) {
        regresar = getChildren(((TreeNode) this.getChild(0)).getChildren());
      }
    } // try
    catch (Exception e) {
      mx.org.kaana.libs.formato.Error.mensaje(e);
    } // catch
    return regresar;
  }

  private void build(List<TreeObject> list, UISubmenu padre) throws Exception {
    try {
      UIMenuItem nodo = null;
      for (TreeObject treeObject : list) {
        if (treeObject.getType() == Tree.LEAF) {
          if (treeObject.getName().toUpperCase().equals("SEPARADOR")) {
            UISeparator separator = new UISeparator();
            separator.setId("nodo_".concat(treeObject.getRama()));
            if (padre == null) {
              this.model.addElement(separator);
            } else {
              padre.getChildren().add(separator);
            }
          } // if
          else {
            nodo = new UIMenuItem();
            nodo.setValue(treeObject.getName());
            if (treeObject.getIcono() != null) {
              nodo.setIcon(treeObject.getIcono());
            }
            nodo.setUrl(treeObject.getUrl());
            nodo.setId("nodo_".concat(treeObject.getRama()));
            nodo.setProcess("@this");
            nodo.addActionListener(new CleanFlashListener());
            if (padre == null) {
              this.model.addElement(nodo);
            } else {
              padre.getChildren().add(nodo);
            }
          } // else
        } // if
        else {
          UISubmenu submenu = new UISubmenu();
          submenu.setLabel(treeObject.getName());
          if (treeObject.getIcono() != null) {
            submenu.setIcon(treeObject.getIcono());
          }
          submenu.setId("s_".concat(treeObject.getRama()));
          if (padre == null) {
            this.model.addElement(submenu);
          } else {
            padre.getChildren().add(submenu);
          }
          TreeNode treeNode = (TreeNode) treeObject;
          build(getChildren(treeNode.getChildren()), submenu);
        } // else
      } // for
    } // try
    catch (Exception e) {
      throw e;
    } // catch
  }

  public List<TreeObject> getChildren(Tree arbol) throws Exception {
    List<TreeObject> regresar = new ArrayList<>();
    try {
      for (int i = 0; i < arbol.size(); i++) {
        TreeObject treeObject = arbol.getChild(i);
        regresar.add(treeObject);
      } // for
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  }

}
