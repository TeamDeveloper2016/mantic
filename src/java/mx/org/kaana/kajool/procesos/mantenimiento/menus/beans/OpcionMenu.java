package mx.org.kaana.kajool.procesos.mantenimiento.menus.beans;

import java.util.HashMap;
import java.util.Map;
import javax.persistence.Transient;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 27/08/2015
 * @time 11:02:53 AM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public class OpcionMenu implements IBaseDto {

	private Long idMenu;
	private Long idMenuGrupo;
	private Long idMenuPerfil;
	private String descripcion;

	public OpcionMenu() {
		this(null, null, -1L);
	}

	public OpcionMenu(Long idMenu, String descripcion, Long idMenuGrupo) {
		this(idMenu, descripcion, idMenuGrupo, -1L);
	}

	public OpcionMenu(Long idMenu, String descripcion, Long idMenuGrupo, Long idMenuPerfil) {
		this.idMenu=idMenu;
		this.descripcion=descripcion;
		this.idMenuGrupo=idMenuGrupo;
		this.idMenuPerfil=idMenuPerfil;
	}

	public Long getIdMenuGrupo() {
		return idMenuGrupo;
	}

	public void setIdMenuGrupo(Long idMenuGrupo) {
		this.idMenuGrupo=idMenuGrupo;
	}

	public Long getIdMenuPerfil() {
		return idMenuPerfil;
	}

	public void setIdMenuPerfil(Long idMenuPerfil) {
		this.idMenuPerfil=idMenuPerfil;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion=descripcion;
	}


	public Long getIdMenu() {
		return idMenu;
	}

	public void setIdMenu(Long idMenu) {
		this.idMenu=idMenu;
	}

	@Override
	public Long getKey() {
		return this.idMenu;
	}

	@Override
	public void setKey(Long key) {
		this.idMenu=key;
	}

	public Map<String, Object> toMap() {
		Map<String, Object> regresar=new HashMap<String, Object>();
		regresar.put("idMenu", this.idMenu);
		regresar.put("descripcion", this.descripcion);
		return regresar;
	}

	public Object[] toArray() {
		Object[] regresar=new Object[]{
			this.idMenu, this.descripcion};
		return regresar;
	}

	public Object toValue(String name) {
		return Methods.getValue(this, name);
	}

	public String toAllKeys() {
		StringBuilder regresar=new StringBuilder();
		regresar.append("|");
		regresar.append("idMenu~");
		regresar.append(this.idMenu);
		regresar.append("|");
		return regresar.toString();
	}

	public String toKeys() {
		StringBuilder regresar=new StringBuilder();
		regresar.append(this.idMenu);
		return regresar.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj==null) {
			return false;
		}
		if (getClass()!=obj.getClass()) {
			return false;
		}
		final OpcionMenu other=(OpcionMenu) obj;
		if (this.idMenu!=other.idMenu&&(this.idMenu==null||!this.idMenu.equals(other.idMenu))) {
			return false;
		}
		return true;
	}

	@Override
	public Class toHbmClass() {
		return this.getClass();
	}

	@Override
	public int hashCode() {
		int hash=7;
		hash=37*hash+(this.idMenu!=null ? this.idMenu.hashCode() : 0);
		return hash;
	}

	@Transient
	public boolean isValid() {
		return this.idMenu!=null&&this.idMenu!=-1L;
	}
}
