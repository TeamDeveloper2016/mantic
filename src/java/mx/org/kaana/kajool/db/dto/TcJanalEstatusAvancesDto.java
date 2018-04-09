package mx.org.kaana.kajool.db.dto;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 21/09/2015
 * @time 03:42:38 PM
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
@Table(name = "tc_janal_estatus_avances")
public class TcJanalEstatusAvancesDto implements IBaseDto, Serializable {

	private static final long serialVersionUID=1L;
	@Column(name = "ID_ESTATUS_AVANCE")
	private Long idEstatusAvance;
	@Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	//@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "idEstatusAvance_sequence")
	//@SequenceGenerator(name = "idEstatusAvance_sequence", sequenceName = "SEQ_TC_JANAL_ESTATUS_AVANCES", allocationSize = 1)
	@Column(name = "DESCRIPCION")
	private String descripcion;

	public TcJanalEstatusAvancesDto() {
		this(new Long(-1L));
	}

	public TcJanalEstatusAvancesDto(Long key) {
		this(null, null);
		setKey(key);
	}

	public TcJanalEstatusAvancesDto(Long idEstatusAvance, String descripcion) {
		setIdEstatusAvance(idEstatusAvance);
		setDescripcion(descripcion);
	}

	public Long getIdEstatusAvance() {
		return idEstatusAvance;
	}

	public void setIdEstatusAvance(Long idEstatusAvance) {
		this.idEstatusAvance=idEstatusAvance;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion=descripcion;
	}

	@Transient
	@Override
	public Long getKey() {
		return getIdEstatusAvance();
	}

	@Override
	public void setKey(Long key) {
		this.idEstatusAvance=key;
	}

	@Override
	public String toString() {
		StringBuilder regresar=new StringBuilder();
		regresar.append("[");
		regresar.append(getIdEstatusAvance());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getDescripcion());
		regresar.append("]");
		return regresar.toString();
	}

	@Override
	public Map toMap() {
		Map regresar=new HashMap();
		regresar.put("idEstatusAvance", getIdEstatusAvance());
		regresar.put("descripcion", getDescripcion());
		return regresar;
	}

	@Override
	public Object[] toArray() {
		Object[] regresar=new Object[]{
			getIdEstatusAvance(), getDescripcion()
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
		regresar.append("idEstatusAvance~");
		regresar.append(getIdEstatusAvance());
		regresar.append("|");
		return regresar.toString();
	}

	@Override
	public String toKeys() {
		StringBuilder regresar=new StringBuilder();
		regresar.append(getIdEstatusAvance());
		return regresar.toString();
	}

	@Override
	public Class toHbmClass() {
		return TcJanalEstatusAvancesDto.class;
	}

	@Override
	public boolean isValid() {
		return getIdEstatusAvance()!=null&&getIdEstatusAvance()!=-1L;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj==null) {
			return false;
		} // if
		if (getClass()!=obj.getClass()) {
			return false;
		} // if
		final TcJanalEstatusAvancesDto other=(TcJanalEstatusAvancesDto) obj;
		if (getIdEstatusAvance()!=other.idEstatusAvance&&(getIdEstatusAvance()==null||!getIdEstatusAvance().equals(other.idEstatusAvance))) {
			return false;
		} // if
		return true;
	}

	@Override
	public int hashCode() {
		int hash=7;
		hash=67*hash+(getIdEstatusAvance()!=null ? getIdEstatusAvance().hashCode() : 0);
		return hash;
	}
}
