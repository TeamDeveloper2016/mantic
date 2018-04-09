package mx.org.kaana.kajool.db.dto;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 21/09/2015
 * @time 12:56:43 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;

@Entity
@Table(name = "tc_janal_menus_enc_perfil")
public class TcJanalMenusEncPerfilDto implements IBaseDto, Serializable {

	private static final long serialVersionUID=1L;
	@Column(name = "ID_MENU_ENC_PERFIL")
	private Long idMenuEncPerfil;
	@Column(name = "ID_PERFIL")
	private Long idPerfil;
	@Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	//@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "idMenuEncPerfil_sequence")
	//@SequenceGenerator(name = "idMenuEncPerfil_sequence", sequenceName = "SEQ_TC_JANAL_MENUS_ENCABEZADO", allocationSize = 1)
	@Column(name = "ID_MENU_ENCABEZADO")
	private Long idMenuEncabezado;

	public TcJanalMenusEncPerfilDto() {
		this(new Long(-1L));
	}

	public TcJanalMenusEncPerfilDto(Long key) {
		this(null, null, null);
		setKey(key);
	}

	public TcJanalMenusEncPerfilDto(Long idMenuEncPerfil, Long idPerfil, Long idMenuEncabezado) {
		setIdMenuEncPerfil(idMenuEncPerfil);
		setIdPerfil(idPerfil);
		setIdMenuEncabezado(idMenuEncabezado);
	}

	public Long getIdMenuEncPerfil() {
		return idMenuEncPerfil;
	}

	public void setIdMenuEncPerfil(Long idMenuEncPerfil) {
		this.idMenuEncPerfil=idMenuEncPerfil;
	}

	public Long getIdPerfil() {
		return idPerfil;
	}

	public void setIdPerfil(Long idPerfil) {
		this.idPerfil=idPerfil;
	}

	public Long getIdMenuEncabezado() {
		return idMenuEncabezado;
	}

	public void setIdMenuEncabezado(Long idMenuEncabezado) {
		this.idMenuEncabezado=idMenuEncabezado;
	}

	@Transient
	@Override
	public Long getKey() {
		return getIdMenuEncPerfil();
	}

	@Override
	public void setKey(Long key) {
		this.idMenuEncPerfil=key;
	}

	@Override
	public String toString() {
		StringBuilder regresar=new StringBuilder();
		regresar.append("[");
		regresar.append(getIdMenuEncPerfil());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdPerfil());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getIdMenuEncabezado());
		regresar.append("]");
		return regresar.toString();
	}

	@Override
	public Map toMap() {
		Map regresar=new HashMap();
		regresar.put("idMenuEncPerfil", getIdMenuEncPerfil());
		regresar.put("idPerfil", getIdPerfil());
		regresar.put("idMenuEncabezado", getIdMenuEncabezado());
		return regresar;
	}

	@Override
	public Object[] toArray() {
		Object[] regresar=new Object[]{
			getIdMenuEncPerfil(), getIdPerfil(), getIdMenuEncabezado()
		};
		return regresar;
	}

	@Override
	public Object toValue(String name) {
		return Methods.getValue(this, name);
	}

	@Override
	public String toAllKeys() {
		StringBuilder regresar=new StringBuilder();
		regresar.append("|");
		regresar.append("idMenuEncPerfil~");
		regresar.append(getIdMenuEncPerfil());
		regresar.append("|");
		return regresar.toString();
	}

	@Override
	public String toKeys() {
		StringBuilder regresar=new StringBuilder();
		regresar.append(getIdMenuEncPerfil());
		return regresar.toString();
	}

	@Override
	public Class toHbmClass() {
		return TcJanalMenusEncPerfilDto.class;
	}

	@Override
	public boolean isValid() {
		return getIdMenuEncPerfil()!=null&&getIdMenuEncPerfil()!=-1L;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj==null) {
			return false;
		} // if
		if (getClass()!=obj.getClass()) {
			return false;
		} // if
		final TcJanalMenusEncPerfilDto other=(TcJanalMenusEncPerfilDto) obj;
		if (getIdMenuEncPerfil()!=other.idMenuEncPerfil&&(getIdMenuEncPerfil()==null||!getIdMenuEncPerfil().equals(other.idMenuEncPerfil))) {
			return false;
		} // if
		return true;
	}

	@Override
	public int hashCode() {
		int hash=7;
		hash=67*hash+(getIdMenuEncPerfil()!=null ? getIdMenuEncPerfil().hashCode() : 0);
		return hash;
	}
}
