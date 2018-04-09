/**
 *  TreeNode.java
 */
package com.coolservlets.beans.tree;

import java.io.Serializable;

public class TreeNode extends TreeObject implements TreeInterface,  Serializable {
	
	private static final long serialVersionUID=8595108123204832585L;

  private boolean visible;
  private Tree children;

  public TreeNode(int id, String name,  String rama, String imageIn) {
    this(id, name, null, rama, imageIn);
  }

  public TreeNode(int id, String name, String link, String rama, String imageIn) {
    this(id, name, null, rama, imageIn, name, null,null);
  }

	public TreeNode(int id, String name, String link, String rama, String imageIn, String hint, String publicar,String icono) {
    super(id, name, link, rama, imageIn, hint, Tree.NODE, publicar,icono);
    setVisible(false);
    children = new Tree();
  }

  public void addChild(TreeObject child) {
    child.setParent(this);
    children.addChild(child);
  }

  public void setVisible(boolean value) {
    visible = value;
  }

  public boolean isVisible() {
    return visible;
  }

  public void toggleVisible() {
    visible = !visible;
  }

  public Tree getChildren() {
    return children;
  }  
}
