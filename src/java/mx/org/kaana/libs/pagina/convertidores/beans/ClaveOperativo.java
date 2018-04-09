package mx.org.kaana.libs.pagina.convertidores.beans;

import java.io.Serializable;
import mx.org.kaana.libs.Constantes;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date Oct 31, 2012
 *@time 2:51:30 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class ClaveOperativo  implements Serializable {
	
	private static final long serialVersionUID=8877477811289426760L;

	private	Long idTipo;
	private	String clave;

	public ClaveOperativo(Long idTipo, String clave) {
		this.idTipo=idTipo;
		this.clave=clave;
	}

	public String getClave() {
		return clave;
	}

	public Long getIdTipo() {
		return idTipo;
	}

	public void setClave(String clave) {
		this.clave=clave;
	}

	public void setIdTipo(Long idTipo) {
		this.idTipo=idTipo;
	}

	@Override
	public String toString() {
		StringBuilder regresar = new StringBuilder();
		regresar.append(this.clave);
		regresar.append(Constantes.SEPARADOR);
		regresar.append(this.idTipo);
		return regresar.toString();
	}

  @Override
  public int hashCode() {
    int hash=7;
    hash=73*hash+(this.clave!=null ? this.clave.hashCode() : 0);
    return hash;
  }

	
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof ClaveOperativo)) {
			return false;
		}
		ClaveOperativo other = (ClaveOperativo) object;
		if ((this.clave == null && other.clave != null) || (this.clave != null && !this.clave.equals(other.clave))) {
			return false;
		}
		return true;
	}
	
}
