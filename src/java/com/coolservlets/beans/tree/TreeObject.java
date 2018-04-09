/**
 * TreeObject.java
 */
package com.coolservlets.beans.tree;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import mx.org.kaana.libs.Constantes;

public class TreeObject implements Serializable {
	private static final long serialVersionUID=3865231079768904315L;

  private int id;
  private String name;
  private String link;
  private String rama;
  private String imagen;
  private String hint;
  private int type;
  private TreeObject parent;
  private String publicar;
  private String icono;

  public TreeObject(int id, String name, String link, String rama, String imagen, String hint, int type, String publicar, String icono) {
    setId(id);
    setName(name);
    setLink(link);
    setRama(rama);
    setImagen(imagen);
    setHint(hint);
    setType(type);
    setParent(null);
    setPublicar(publicar);
    setIcono(icono);
  }

  public String getIcono() {
    return icono;
  }

  public void setIcono(String icono) {
    this.icono = icono;
  }

  public int getId() {
    return this.id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getLink() {
    return link;
  }

  public void setLink(String link) {
    this.link = link;
  }

  public java.lang.String getRama() {
    return rama;
  }

  public void setRama(java.lang.String rama) {
    this.rama = rama;
  }

  public java.lang.String getImagen() {
		    //Parche temporal para asignar la imagen del menu
	    return imagen.contains("/Librerias")?Constantes.RUTA_IMAGENES_MENU.concat(imagen.substring(imagen.lastIndexOf("/")+1)):imagen;		
  }

  public void setImagen(java.lang.String imagen) {
    this.imagen = imagen;
  }

  public String getHint() {
    return hint == null || hint.equalsIgnoreCase("null") || hint.equalsIgnoreCase("") ? getName() : hint;
  }

  public String getHintShort() {
    String regresar = getHint();
    if(regresar.length()> 40)
       regresar = getHint().substring(0, 40).concat("...");
    return regresar;
  }

  public void setHint(String hint) {
    this.hint = hint;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public TreeObject getParent() {
    return parent;
  }

  public void setParent(TreeObject parent) {
    this.parent = parent;
  }

  public String getPublicar() {
    return publicar;
  }

  public void setPublicar(String publicar) {
    this.publicar=publicar;
  }

  public String getUrl() {
    StringBuilder sb = new StringBuilder();
    String params = "id=" + getId() + "&opcionRama=" + getRama();
    if (getLink() != null && getLink().trim().length() > 0) {
      sb.append(getLink().trim());
      if (getLink().indexOf("?") > 0) {
        sb.append("&");
      } else {
        sb.append("?");
      }
      sb.append(params);
    } // if
    return sb.toString();
  }

  public Map toMap() {
    Map<String, Object> regresar = new HashMap<String, Object>();
    regresar.put("text", getName());
    regresar.put("url", getUrl());
    regresar.put("imagen", getImagen());
    regresar.put("hint", getHint());
    regresar.put("publicar", getPublicar());
    return regresar;
  }
		
	@Override
	public String toString () {
	  StringBuilder regresar = new StringBuilder();
		regresar.append("[");
		regresar.append(getId());
		regresar.append(",");
		regresar.append(getName());	
		regresar.append(",");
		regresar.append(getRama());	
		regresar.append(",");
		regresar.append(getLink());		
		regresar.append("]");			
		return regresar.toString();
	}
}
