package mx.org.kaana.kajool.procesos.mantenimiento.temas.beans;

import java.io.Serializable;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 10/09/2015
 * @time 11:08:25 AM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public final class Tema implements Serializable {
	
	private static final long serialVersionUID=2567973410933037625L;

	private String title;
	private String name;
	private String image;

	public Tema() {
		this("", "", "");
	}

  public Tema(String name) {
   this("", name, "");
  }


	public Tema(String title, String name, String image) {
		setTitle(title);
		setName(name);
		setImage(image);
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image= image;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name= name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title= title;
	}

	@Override
	public String toString() {
		StringBuilder sb= new StringBuilder();
		sb.append(getName());
		return sb.toString();
	}


  @Override
  public int hashCode() {
    final int prime= 31;
    int result= 1;
    result= prime * result + ((name == null) ? 0 : name.hashCode());
    return result;
  }


  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Tema other = (Tema) obj;
    if (name == null) {
      if (other.name != null)
        return false;
    } else if (!name.equals(other.name))
      return false;
    return true;
  }
	
}
