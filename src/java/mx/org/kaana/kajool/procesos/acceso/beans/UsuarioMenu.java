package mx.org.kaana.kajool.procesos.acceso.beans;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.Transient;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date Jul 10, 2012
 *@time 9:16:22 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class UsuarioMenu implements IBaseDto, Serializable {
	
	private static final long serialVersionUID= 5920359034128623130L;
	private Long idMenu;
	private Long idCfgClave;
  private String clave;
  private String descripcion;
  private String ruta;
	private String publicar;
	private String imagen;
	private Long nivel;	
	private Long ultimo;	
  private String ayuda;	
	private String icono;	

	public UsuarioMenu() {
		this(-1L);
	}
	
	public UsuarioMenu(Long idMenu) {
		this(idMenu, -1L, "", "", "", "", "", -1L, -1L, "", "");
	}	
	
	public UsuarioMenu(Long idMenu, Long idCfgClave, String clave, String descripcion, String ruta, String publicar, String imagen, Long nivel, Long ultimo, String ayuda, String icono) {
		this.idMenu     = idMenu;
		this.idCfgClave = idCfgClave;
		this.clave      = clave;
		this.descripcion= descripcion;
		this.ruta       = ruta;
		this.publicar   = publicar;
		this.imagen     = imagen;
		this.nivel      = nivel;
		this.ultimo     = ultimo;
		this.ayuda      = ayuda;
		this.icono      = icono;
	}

	public Long getIdMenu() {
		return idMenu;
	}

	public void setIdMenu(Long idMenu) {
		this.idMenu= idMenu;
	}

	public Long getIdCfgClave() {
		return idCfgClave;
	}

	public void setIdCfgClave(Long idCfgClave) {
		this.idCfgClave= idCfgClave;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave= clave;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion= descripcion;
	}

	public String getRuta() {
		return ruta;
	}

	public void setRuta(String ruta) {
		this.ruta= ruta;
	}

	public String getPublicar() {
		return publicar;
	}

	public void setPublicar(String publicar) {
		this.publicar= publicar;
	}

	public String getImagen() {
		return imagen;
	}

	public void setImagen(String imagen) {
		this.imagen= imagen;
	}

	public Long getNivel() {
		return nivel;
	}

	public void setNivel(Long nivel) {
		this.nivel= nivel;
	}

	public Long getUltimo() {
		return ultimo;
	}

	public void setUltimo(Long ultimo) {
		this.ultimo= ultimo;
	}

	public String getAyuda() {
		return ayuda;
	}

	public void setAyuda(String ayuda) {
		this.ayuda= ayuda;
	}

	public String getIcono() {
		return icono;
	}

	public void setIcono(String icono) {
		this.icono= icono;
	}
	
  @Transient
  public Long getKey() {
    return getIdMenu();
  }

  public void setKey(Long key) {
    this.idMenu = key;
  }
	
	public String getAyudaRecortada() {
    String regresar = getAyuda();
    if(regresar.length()> 40)
       regresar = getAyuda().substring(0, 40).concat("...");
    return regresar;
  }

  @Transient
  public boolean isValid() {
    return getIdMenu() != null && getIdMenu() != -1L;
  }

  public String toString() {
    StringBuilder regresar = new StringBuilder();
    regresar.append("[");
		regresar.append("idMenu=");
		regresar.append(getIdMenu());
    regresar.append(",");
		regresar.append("idCfgClave=");
		regresar.append(getIdCfgClave());
    regresar.append(",");
		regresar.append("clave=");
		regresar.append(getClave());
    regresar.append(",");
		regresar.append("descripcion=");
		regresar.append(getDescripcion());
    regresar.append(",");
		regresar.append("ruta=");
		regresar.append(getRuta());
    regresar.append(",");
		regresar.append("publicar=");
		regresar.append(getPublicar());
    regresar.append(",");
		regresar.append("imagen=");
		regresar.append(getImagen());
    regresar.append(",");
		regresar.append("nivel=");
		regresar.append(getNivel());
    regresar.append(",");
		regresar.append("ultimo=");
		regresar.append(getUltimo());
    regresar.append(",");
		regresar.append("ayuda=");
		regresar.append(getAyuda());
    regresar.append(",");
		regresar.append("icono=");
		regresar.append(getIcono());
    regresar.append("]");
    return regresar.toString();
  }

  public Map<String, Object> toMap() {
    Map<String, Object> regresar = new HashMap<>();
		regresar.put("idMenu", getIdMenu());
		regresar.put("idCfgClave", getIdCfgClave());
		regresar.put("clave", getClave());
		regresar.put("descripcion", getDescripcion());
		regresar.put("ruta", getRuta());
		regresar.put("publicar", getPublicar());
		regresar.put("imagen", getImagen());
		regresar.put("nivel", getNivel());
		regresar.put("ultimo", getUltimo());
		regresar.put("ayuda", getAyuda());
		regresar.put("icono", getIcono());
    return regresar;
  }

  public Object[] toArray() {
    Object[] regresar = new Object[]{getClave(), getDescripcion(), getPublicar(), getRuta(), getIdMenu(), getAyuda()};
    return regresar;
  }

  public Object toValue(String name) {
    return Methods.getValue(this, name);
  }

  public String toAllKeys() {
    StringBuilder regresar = new StringBuilder();
    regresar.append("|");
    regresar.append("idMenu~");
    regresar.append(getIdMenu());
    regresar.append("|");
    return regresar.toString();
  }

  public String toKeys() {
    StringBuilder regresar = new StringBuilder();
    regresar.append(getIdMenu());
    return regresar.toString();
  }

  @Override
  public Class toHbmClass() {
    return this.getClass();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final UsuarioMenu dto = (UsuarioMenu) obj;
    if (getIdMenu() != dto.getIdMenu()
            && (getIdMenu() == null || !getIdMenu().equals(dto.getIdMenu()))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (getIdMenu() != null ? getIdMenu().hashCode() : 0);
    return hash;
  }
}
