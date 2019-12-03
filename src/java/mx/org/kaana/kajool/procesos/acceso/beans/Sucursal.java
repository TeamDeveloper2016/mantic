package mx.org.kaana.kajool.procesos.acceso.beans;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.libs.Constantes;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 12/04/82018
 * @time 11:24:24 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public class Sucursal extends Empresa implements Serializable, IBaseDto {

  private static final long serialVersionUID=8942894310320196756L;  
  
  private Long idEmpresaPersona;
  private Long idAlmacen;
 
  public Sucursal() {
    this(-1L);
  }
  
  public Sucursal(Long idEmpresaPersona) {
    this(idEmpresaPersona,-1L, -1L, -1L, "", "", "", "-1");
  }  
  
  public Sucursal(Long idEmpresaPersona, Long idEmpresa, Long idEmpresaDepende, Long idTipoEmpresa, String nombre, String nombreCorto, String titulo, String sucursales) {
	  this(idEmpresaPersona, idEmpresa, idEmpresaDepende, idTipoEmpresa, nombre, nombreCorto, titulo, sucursales, -1L);	
	}
	
  public Sucursal(Long idEmpresaPersona, Long idEmpresa, Long idEmpresaDepende, Long idTipoEmpresa, String nombre, String nombreCorto, String titulo, String sucursales, Long idAlmacen) {
    super(idEmpresa, idEmpresa, idTipoEmpresa, nombre, nombreCorto, titulo, sucursales, idAlmacen);
    this.idEmpresaPersona= idEmpresaPersona;    
		this.idAlmacen       = idAlmacen; 
  }   

  public Long getIdEmpresaPersonal() {
    return idEmpresaPersona;
  }

  public void setIdEmpresaPersona(Long idEmpresaPersona) {
    this.idEmpresaPersona = idEmpresaPersona;
  }

	public Long getIdAlmacen() {
		return idAlmacen;
	}

	public void setIdAlmacen(Long idAlmacen) {
		this.idAlmacen=idAlmacen;
	}
  
  @Override
  public String toString() {
    StringBuilder regresar = new StringBuilder();
    regresar.append(Constantes.SEPARADOR);
    regresar.append("empresaPersonal=");
    regresar.append(Constantes.SEPARADOR);
    regresar.append(this.idEmpresaPersona);
    regresar.append("nombre=");
    regresar.append(this.getNombre());
    regresar.append(Constantes.SEPARADOR);    
    regresar.append("idAlmacen=");
    regresar.append(this.getIdAlmacen());
    return regresar.toString();
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 97 * hash + Objects.hashCode(this.idEmpresaPersona);
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
    final Sucursal other = (Sucursal) obj;
    if (!Objects.equals(this.idEmpresaPersona, other.idEmpresaPersona)) {
      return false;
    }
    return true;
  }

  @Override
  public Long getKey() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void setKey(Long key) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public Map<String, Object> toMap() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public Object[] toArray() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public boolean isValid() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public Object toValue(String name) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public String toAllKeys() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public String toKeys() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public Class toHbmClass() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
  
  
 
}
