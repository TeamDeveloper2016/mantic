package mx.org.kaana.kajool.procesos.acceso.beans;

import java.io.Serializable;
import java.util.Objects;
import mx.org.kaana.libs.Constantes;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 12/04/82018
 * @time 11:24:24 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public class Empresa implements Serializable {

  private static final long serialVersionUID=8542894310320196756L;  
  
  private Long idEmpresa;
  private Long idEmpresaDepende;
  private Long idTipoEmpresa;
  private String nombre;
  private String nombreCorto;
  private String titulo;
  private String sucursales;

  public Empresa () {
    this(-1L, -1L, -1L, "", "", "", "-1");
  }     

  public Empresa(Long idEmpresa, Long idEmpresaDepende, Long idTipoEmpresa, String nombre, String nombreCorto, String titulo, String sucursales) {
    this.nombre          = nombre;
    this.idEmpresa       = idEmpresa;
    this.idEmpresaDepende= idEmpresaDepende;
    this.idTipoEmpresa   = idTipoEmpresa;
		this.sucursales      = sucursales;
    this.nombreCorto     = nombreCorto;
    this.titulo          = titulo;
  }

  public Long getIdEmpresaDepende() {
    return idEmpresaDepende;
  }

  public void setIdEmpresaDepende(Long idEmpresaDepende) {
    this.idEmpresaDepende = idEmpresaDepende;
  } 
  
  public String getNombre() {
    return nombre.toUpperCase();
  }  

	public Long getIdTipoEmpresa() {
		return idTipoEmpresa;
	}

	public void setIdTipoEmpresa(Long idTipoEmpresa) {
		this.idTipoEmpresa=idTipoEmpresa;
	}
  
  public void setNombre(String nombre) {
    this.nombre = nombre;
  }  

  public Long getIdEmpresa() {
    return idEmpresa;
  }

  public void setIdEmpresa(Long idEmpresa) {
    this.idEmpresa = idEmpresa;
  }

  public String getNombreCorto() {
    return nombreCorto.toUpperCase();
  }

  public void setNombreCorto(String nombreCorto) {
    this.nombreCorto = nombreCorto;
  }

  public String getTitulo() {
    return titulo.toUpperCase();
  }

  public void setTitulo(String titulo) {
    this.titulo = titulo;
  }  

	public String getSucursales() {
		return sucursales;
	}

	public void setSucursales(String sucursales) {
		this.sucursales=sucursales;
	}

	public boolean isMatriz() {
	  return this.getIdTipoEmpresa()!= null && this.getIdTipoEmpresa().intValue()==Constantes.SI;
	}
	
  @Override
  public String toString() {
    StringBuilder regresar = new StringBuilder();
    regresar.append(Constantes.SEPARADOR);
    regresar.append("empresa=");
    regresar.append(Constantes.SEPARADOR);
    regresar.append(this.idEmpresa);
    regresar.append("nombre=");
    regresar.append(this.nombre);
    regresar.append(Constantes.SEPARADOR);    
    return regresar.toString();
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 97 * hash + Objects.hashCode(this.idEmpresa);
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Empresa other = (Empresa) obj;
    if (!Objects.equals(this.idEmpresa, other.idEmpresa)) {
      return false;
    }
    return true;
  }
  
  
 
}
