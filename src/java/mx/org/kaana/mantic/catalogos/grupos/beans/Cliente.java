package mx.org.kaana.mantic.catalogos.grupos.beans;

import java.io.Serializable;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.mantic.db.dto.TcManticGruposClientesDto;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date Jun 15, 2012
 *@time 12:23:54 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */


public class Cliente extends TcManticGruposClientesDto implements Serializable {

	private static final long serialVersionUID=6542385871790548229L;

	private UISelectEntity idEntity;

	public Cliente() {
		this.idEntity= new UISelectEntity(new Entity(-1L));
	}

	public Cliente(Long key) {
		super(key);
	}

	public Cliente(Long idGrupo, Long idCliente, Long idGrupoCliente) {
		super(idGrupo, idCliente, idGrupoCliente);
	}

	public UISelectEntity getIdEntity() {
		return idEntity;
	}

	public void setIdEntity(UISelectEntity idEntity) {
		this.idEntity=idEntity;
		if(this.idEntity!= null)
			this.setIdCliente(this.idEntity.getKey());
	}
	
	@Override
	public Class toHbmClass() {
		return TcManticGruposClientesDto.class;
	}

	
}
