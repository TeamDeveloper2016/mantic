/**
 *  TreeLeaf.java
 */
package com.coolservlets.beans.tree;


public class TreeLeaf extends TreeObject  {
	
  public TreeLeaf(String name, String link, String rama, String imageIn, String hint, String publicar, String icono) {
    super(0, name, link, rama, imageIn, hint, Tree.LEAF, publicar,icono);
  }

  public TreeLeaf(int id, String name, String link, String rama, String imageIn, String hint, String publicar, String icono) {
    super(id, name, link, rama, imageIn, hint, Tree.LEAF, publicar,icono);
  }
} 
