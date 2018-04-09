package mx.org.kaana.kajool.db.dto;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 21/09/2015
 * @time 03:50:43 PM
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
@Table(name = "tr_janal_estatus_avances")
public class TrJanalEstatusAvancesDto implements IBaseDto, Serializable {

	private static final long serialVersionUID=1L;
	@Column(name = "ID_ESTATUS_AVANCE")
	private Long idEstatusAvance;
	@Column(name = "ID_DEFINICION_AVANCE")
	private Long idDefinicionAvance;
	@Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	//@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "idEstatusAvance_sequence")
	//@SequenceGenerator(name = "idEstatusAvance_sequence", sequenceName = "SEQ_TR_JANAL_ESTATUS_AVANCES", allocationSize = 1)
	@Column(name = "ORDEN")
	private Long orden;

	public TrJanalEstatusAvancesDto() {
		this(new Long(-1L));
	}

	public TrJanalEstatusAvancesDto(Long key) {
		this(null, null, null);
		setKey(key);
	}

	public TrJanalEstatusAvancesDto(Long idEstatusAvance, Long idDefinicionAvance, Long orden) {
		setIdEstatusAvance(idEstatusAvance);
		setIdDefinicionAvance(idDefinicionAvance);
		setOrden(orden);
	}

	public Long getIdEstatusAvance() {
		return idEstatusAvance;
	}

	public void setIdEstatusAvance(Long idEstatusAvance) {
		this.idEstatusAvance=idEstatusAvance;
	}

	public Long getIdDefinicionAvance() {
		return idDefinicionAvance;
	}

	public void setIdDefinicionAvance(Long idDefinicionAvance) {
		this.idDefinicionAvance=idDefinicionAvance;
	}

	public Long getOrden() {
		return orden;
	}

	public void setOrden(Long orden) {
		this.orden=orden;
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
		regresar.append(getIdDefinicionAvance());
		regresar.append(Constantes.SEPARADOR);
		regresar.append(getOrden());
		regresar.append("]");
		return regresar.toString();
	}

	@Override
	public Map toMap() {
		Map regresar=new HashMap();
		regresar.put("idEstatusAvance", getIdEstatusAvance());
		regresar.put("idDefinicionAvance", getIdDefinicionAvance());
		regresar.put("orden", getOrden());
		return regresar;
	}

	@Override
	public Object[] toArray() {
		Object[] regresar=new Object[]{
			getIdEstatusAvance(), getIdDefinicionAvance(), getOrden()
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
		return TrJanalEstatusAvancesDto.class;
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
		final TrJanalEstatusAvancesDto other=(TrJanalEstatusAvancesDto) obj;
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
