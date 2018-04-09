package mx.org.kaana.kajool.db.comun.operation;

import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import org.hibernate.Session;

public class Update implements IActions {

  private IBaseDto dto;

	public Update(IBaseDto dto) {
		this.dto=dto;
	}

	public IBaseDto getDto() {
		return dto;
	}

	@Override
	public Long ejecutar(Session session) throws Exception {
    return DaoFactory.getInstance().update(session, this.dto);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    else if (this.dto.getClass()!= ((Update)obj).dto.getClass())
      return false;
    final IBaseDto other = ((Update)obj).getDto();
    if (dto.getKey() != other.getKey() && (dto.getKey() == null || !dto.getKey().equals(other.getKey())))
      return false;
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (dto.getKey() != null ? dto.getKey().hashCode() : 0);
    return hash;
  }

}
