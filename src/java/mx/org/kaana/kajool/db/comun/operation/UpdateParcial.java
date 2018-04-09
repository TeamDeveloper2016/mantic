package mx.org.kaana.kajool.db.comun.operation;

import java.util.Map;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import org.hibernate.Session;

public class UpdateParcial implements IActions{

  private Map<String, Object> params;
  private Class dto;
  private Long key;

	public UpdateParcial(Map<String, Object> params, Class dto, Long key) {
		this.params=params;
		this.dto=dto;
		this.key=key;
	}

	@Override
	public Long ejecutar(Session session) throws Exception {
    return DaoFactory.getInstance().update(session, this.dto, this.key, this.params);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null)
      return false;
    if ((getClass() != obj.getClass()) && (this.dto!= ((UpdateParcial)obj).dto))
      return false;
    final Long other = ((UpdateParcial)obj).key;
    if (this.key != other && (this.key == null || !this.key.equals(other)))
      return false;
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (this.key != null ? this.key.hashCode() : 0);
    return hash;
  }

}
