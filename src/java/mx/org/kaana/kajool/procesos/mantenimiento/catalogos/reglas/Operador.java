package mx.org.kaana.kajool.procesos.mantenimiento.catalogos.reglas;

import java.io.Serializable;
import java.util.Objects;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 21/09/2015
 *@time 04:42:01 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public final class Operador implements Serializable {

	private Integer index;
	private String label;
  private String code;

	public Operador(Integer index) {
		this(index, "", "");
	}
	
	public Operador(Integer index, String label, String code) {
		this.index=index;
		this.label=label;
		this.code=code;
	}

	public Integer getIndex() {
		return index;
	}

	public String getLabel() {
		return label;
	}

	public String getCode() {
		return code;
	}

	@Override
	public String toString() {
		return "Operador{"+"index="+index+", code="+code+", name="+label+'}';
	}

  @Override
  public int hashCode() {
    int hash = 3;
    hash = 67 * hash + Objects.hashCode(this.index);
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Operador other = (Operador) obj;
    if (!Objects.equals(this.index, other.index)) {
      return false;
    }
    return true;
  }
}
