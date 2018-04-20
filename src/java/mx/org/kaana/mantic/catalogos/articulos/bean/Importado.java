package mx.org.kaana.mantic.catalogos.articulos.bean;

import java.io.Serializable;
import mx.org.kaana.kajool.enums.EFormatos;

public class Importado implements Serializable {

	private static final long serialVersionUID= 1922005398393887945L;
	private String name;
  private String content;
  private EFormatos format;
  private Long size;
  private Long fileSize;
	private String medicion;

  public Importado() {
		this("", "", EFormatos.FREE, 0L, 0L, "");
	}
	
  public Importado(String name, String content, EFormatos format, Long size, Long fileSize, String medicion) {
    this.name    = name;
    this.content = content;
    this.format  = format;
    this.size    = size;
		this.fileSize= fileSize;
		this.medicion= medicion;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name=name;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content=content;
  }

  public EFormatos getFormat() {
    return format;
  }

  public void setFormat(EFormatos format) {
    this.format=format;
  }

  public Long getSize() {
    return size;
  }

  public void setSize(Long size) {
    this.size=size;
  }

	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	public String getMedicion() {
		return medicion;
	}

	public void setMedicion(String medicion) {
		this.medicion = medicion;
	}
	
  @Override
  public int hashCode() {
    int hash=5;
    hash=37*hash+(this.name!=null ? this.name.hashCode() : 0);
    return hash;
  } // hashCode

  @Override
  public boolean equals(Object obj) {
    if (obj==null) {
      return false;
    } // if
    if (getClass()!=obj.getClass()) {
      return false;
    } // if
    final Importado other=(Importado) obj;
    if ((this.name==null) ? (other.name!=null) : !this.name.equals(other.name)) {
      return false;
    } // if
    return true;
  } // equals

  @Override
  public String toString() {
    return "Importado{"+"name="+name+", content="+content+", format="+format+", size="+size+", fileSize="+fileSize+'}';
  }	// toString
}
