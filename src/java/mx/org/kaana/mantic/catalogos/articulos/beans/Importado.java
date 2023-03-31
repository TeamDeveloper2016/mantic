package mx.org.kaana.mantic.catalogos.articulos.beans;

import java.io.Serializable;
import java.util.Map;
import java.util.Random;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.kajool.enums.EFormatos;

public class Importado implements IBaseDto, Serializable {

	private static final long serialVersionUID= 1922005398393887945L;
	private Long id;
	private String name;
	private String original;
  private String content;
  private EFormatos format;
  private Long size;
  private Long fileSize;
	private String medicion;
	private String ruta;
	private String observaciones;
  private Long idTipoDocumento;
  private Long idComodin;
  private String comodin;
  private Long idEstatus;

  public Importado() {
		this("", "", EFormatos.FREE, 0L, 0L, "", "", "");
	}
	
  public Importado(String name, String content, EFormatos format, Long size, Long fileSize, String medicion, String ruta, String observaciones) {
		this(name, content, format, size, fileSize, medicion, ruta, observaciones, name);
	}
	
  public Importado(String name, String content, EFormatos format, Long size, Long fileSize, String medicion, String ruta, String observaciones, String original) {
    this(name, content, format, size, fileSize, medicion, ruta, observaciones, original, 13L);
  }
  
  public Importado(String name, String content, EFormatos format, Long size, Long fileSize, String medicion, String ruta, String observaciones, String original, Long idTipoDocumento) {
    this.name    = name;
    this.content = content;
    this.format  = format;
    this.size    = size;
		this.fileSize= fileSize;
		this.medicion= medicion;
		this.ruta    = ruta;
		this.observaciones= observaciones;
		this.original= original;
    this.idTipoDocumento= idTipoDocumento;
    this.id= new Random().nextLong();
    if(this.id> 0)
      this.id*= -1L;
    this.idComodin= -1L;
    this.comodin  = "";
    this.idEstatus= 1L;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

	public String getRuta() {
		return ruta;
	}

	public void setRuta(String ruta) {
		this.ruta = ruta;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones=observaciones;
	}

	public String getOriginal() {
		return original;
	}

	public void setOriginal(String original) {
		this.original=original;
	}

  public Long getIdTipoDocumento() {
    return idTipoDocumento;
  }

  public void setIdTipoDocumento(Long idTipoDocumento) {
    this.idTipoDocumento = idTipoDocumento;
  }

  public Long getIdComodin() {
    return idComodin;
  }

  public void setIdComodin(Long idComodin) {
    this.idComodin = idComodin;
  }

  public String getComodin() {
    return comodin;
  }

  public void setComodin(String comodin) {
    this.comodin = comodin;
  }

  public Long getIdEstatus() {
    return idEstatus;
  }

  public void setIdEstatus(Long idEstatus) {
    this.idEstatus = idEstatus;
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
		return "Importado{"+"name="+name+", original="+original+", content="+content+", format="+format+", size="+size+", fileSize="+fileSize+", medicion="+medicion+", ruta="+ruta+", observaciones="+observaciones+'}';
	}

  @Override
  public Long getKey() {
    return getId();
  }

  @Override
  public void setKey(Long key) {
    this.id= key;
  }

  @Override
  public Map<String, Object> toMap() {
    throw new UnsupportedOperationException("Not supported yet."); 
  }

  @Override
  public Object[] toArray() {
    throw new UnsupportedOperationException("Not supported yet."); 
  }

  @Override
  public boolean isValid() {
    throw new UnsupportedOperationException("Not supported yet."); 
  }

  @Override
  public Object toValue(String name) {
    throw new UnsupportedOperationException("Not supported yet."); 
  }

  @Override
  public String toAllKeys() {
    throw new UnsupportedOperationException("Not supported yet."); 
  }

  @Override
  public String toKeys() {
    throw new UnsupportedOperationException("Not supported yet."); 
  }

  @Override
  public Class toHbmClass() {
    throw new UnsupportedOperationException("Not supported yet."); 
  }
	
}
