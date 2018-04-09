package mx.org.kaana.kajool.db.comun.operation;

import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import org.hibernate.Session;

public class DeleteKey implements IActions {

  private Class dto;
  private Long key;

	public DeleteKey(Class dto, Long key) {
		this.dto=dto;
		this.key=key;
	}

	@Override
  public Long ejecutar(Session session) throws Exception {
    return new Long(DaoFactory.getInstance().delete(session, this.dto, this.key));
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null)
      return false;
    if ((getClass() != obj.getClass()) && (this.dto!= ((DeleteKey)obj).dto))
      return false;
    final Long other = ((DeleteKey)obj).key;
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
