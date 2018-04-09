package mx.org.kaana.kajool.db.comun.operation;

import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;


import org.hibernate.Session;

public class Insert implements IActions {

  private IBaseDto dto;

  public Insert(IBaseDto dto) {
    this.dto= dto;
  }

	public IBaseDto getDto() {
		return dto;
	}

	@Override
  public Long ejecutar(Session session) throws Exception {
    return DaoFactory.getInstance().insert(session, this.dto);
  }

	@Override
	public boolean equals(Object obj) {
		if(obj==null)
			return false;
		if(getClass()!=obj.getClass())
			return false;
		final Insert other=(Insert) obj;
		if(this.dto!=other.dto&&(this.dto==null||!this.dto.equals(other.dto)))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		int hash=7;
		hash=67*hash+(this.dto!=null ? this.dto.hashCode() : 0);
		return hash;
	}

	
}
